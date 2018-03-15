/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.servicelayer.tx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests item data invalidation after updates inside and outside of transaction for both model and jalo layer.
 */
@PerformanceTest
public class ItemModelTransactionPerformanceTest extends ServicelayerBaseTest
{
	private static final String MIME_BEFORE = "mime";
	private static final String MIME_AFTER = "mimeNew";

	private final double timeFactor = Config.getDouble("platform.test.timefactor", 1.0);

	@Resource
	private ModelService modelService;

	@Before
	public void prepare()
	{
		// enable new tx cache isolation feature
		Config.setItemCacheIsolation(Boolean.TRUE);
	}

	@After
	public void unprepare()
	{
		// clear new tx cache isolation feature
		Config.setItemCacheIsolation(null);
	}

	@Test
	public void testModificationTimeUpdateTxCommitStressTest() throws Exception
	{
		final long end = System.currentTimeMillis() + (45 * 1000); // run for 45 seconds at most
		do
		{
			testModificationTimeUpdateTx(true);
		}
		while (end > System.currentTimeMillis());
	}

	private void testModificationTimeUpdateTx(final boolean commit) throws InterruptedException
	{
		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		try
		{
			final Media jaloMedia = modelService.getSource(media);
			final PK mediaPK = media.getPk();
			assertEquals(mediaPK, jaloMedia.getPK());

			final Date modModelBefore = media.getModifiedtime();
			final Date modJaloBefore = jaloMedia.getModificationTime();
			assertNotNull(modModelBefore);
			assertNotNull(modJaloBefore);
			assertEquals(modJaloBefore, modModelBefore);
			Thread.sleep((long) (1500 * timeFactor)); // Some DB (MySql) do not have milliseconds!


			Date[] innerTxMod = null;
			final Transaction tx = Transaction.current();
			try
			{
				innerTxMod = (Date[]) tx.execute(new TransactionBody()
				{
					@Override
					public Object execute() throws Exception
					{
						media.setMime(MIME_AFTER);
						modelService.save(media);
						final Date modModelAfter = media.getModifiedtime();
						final Date modJaloAfter = jaloMedia.getModificationTime();

						assertNotNull(modModelAfter);
						assertNotNull(modJaloAfter);
						assertEquals(modModelAfter, modJaloAfter);
						assertTrue(modJaloBefore.before(modJaloAfter));

						final Date[] otherThreadMod = getModTimeFromOtherThread(mediaPK);

						assertNotNull(otherThreadMod);
						assertNotNull(otherThreadMod[0]);
						assertNotNull(otherThreadMod[1]);
						assertEquals(modJaloBefore, otherThreadMod[0]);
						assertEquals(modModelBefore, otherThreadMod[1]);

						if (commit)
						{
							return new Date[]
							{ modJaloAfter, modModelAfter };
						}
						else
						{
							throw new RuntimeException("rollback");
						}
					}
				});
			}
			catch (final Exception e)
			{
				assertFalse("unexpected tx error " + e, commit);
			}

			modelService.refresh(media); // actually not necessary but more likely to find error

			final Date modModelAfterTx = media.getModifiedtime();
			final Date modJaloAfterTx = jaloMedia.getModificationTime();

			assertNotNull(modModelAfterTx);
			assertNotNull(modJaloAfterTx);
			assertEquals(modModelAfterTx, modJaloAfterTx);
			if (commit)
			{
				assertEquals(PersistenceTestUtils.adjustToDB(innerTxMod[0]).getTime(), modJaloAfterTx.getTime());
				assertEquals(PersistenceTestUtils.adjustToDB(innerTxMod[1]).getTime(), modModelAfterTx.getTime());
				assertTrue(modJaloBefore.before(modJaloAfterTx));
			}
			else
			{
				assertEquals(modJaloBefore.getTime(), modJaloAfterTx.getTime());
				assertEquals(modModelBefore.getTime(), modModelAfterTx.getTime());
			}
		}
		finally
		{
			destroyMedia(media);
		}
	}

	// returns [ jaloModTime, modelModTime]
	private Date[] getModTimeFromOtherThread(final PK mediaPK)
	{
		return runInOtherThread(new Callable<Date[]>()
		{
			@Override
			public Date[] call() throws Exception
			{
				final Media jaloMedia = JaloSession.getCurrentSession().getItem(mediaPK);
				final MediaModel media = modelService.get(jaloMedia);

				return new Date[]
				{ jaloMedia.getModificationTime(), media.getModifiedtime() };
			}
		}, 15);
	}

	@Test
	public void testBusyWaitingUpdateReload()
	{
		//testBusyWaitingUpdateReload(30 * 60);
		testBusyWaitingUpdateReload(2 * 60);
	}

	private void testBusyWaitingUpdateReload(final int durationSeconds)
	{
		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		final Media jaloMedia = modelService.getSource(media);
		final PK mediaPK = media.getPk();
		assertEquals(mediaPK, jaloMedia.getPK());

		final RunnerCreator<BusyWaitingUpdateReloadRunner> creator = new RunnerCreator<ItemModelTransactionPerformanceTest.BusyWaitingUpdateReloadRunner>()
		{
			@Override
			public BusyWaitingUpdateReloadRunner newRunner(final int threadNumber)
			{
				return new BusyWaitingUpdateReloadRunner(modelService, media, 0 == threadNumber);
			}
		};

		final TestThreadsHolder<BusyWaitingUpdateReloadRunner> threads = new TestThreadsHolder<ItemModelTransactionPerformanceTest.BusyWaitingUpdateReloadRunner>(
				2, creator);

		threads.startAll();

		for (int sec = 0; sec < durationSeconds && !threads.hasErrors(); sec++)
		{
			try
			{
				Thread.sleep((long) (1000 * timeFactor));
			}
			catch (final InterruptedException e)
			{
				break;
			}
		}
		threads.stopAndDestroy(5);

		assertEquals(Collections.EMPTY_MAP, threads.getErrors());
	}

	class BusyWaitingUpdateReloadRunner implements Runnable
	{
		private final MediaModel media;
		private final ModelService modelService;
		private final boolean isReader;
		private final Tenant tenant;

		BusyWaitingUpdateReloadRunner(final ModelService modelService, final MediaModel media, final boolean isReader)
		{
			this.media = media;
			this.modelService = modelService;
			this.isReader = isReader;
			this.tenant = Registry.getCurrentTenantNoFallback();
		}

		@Override
		public void run()
		{
			Registry.setCurrentTenant(tenant);
			if (isReader)
			{
				System.out.println("Starting in reader mode");
				runAsReader();
			}
			else
			{
				System.out.println("Starting in writer mode");
				runAsWriter();
			}
		}

		private void runAsReader()
		{
			final long mediaPK = media.getPk().getLongValue();
			long globalPK = 0;
			while (!Thread.currentThread().isInterrupted())
			{
				final MediaModel ctx = modelService.get(PK.fromLong(mediaPK));
				modelService.refresh(ctx);
				globalPK |= ctx.getPk().getLongValue();

			}
			assertEquals(mediaPK, globalPK);
		}

		private void runAsWriter()
		{
			long counter = 0;
			while (!Thread.currentThread().isInterrupted())
			{
				boolean success = false;
				try
				{
					doWrite(counter++);
					writeInTx(counter++);
					success = true;
				}
				finally
				{
					if (!success)
					{
						System.err.println(dumpMediaMimeInfo(media));
					}
				}
			}
		}

		private void writeInTx(final long counter)
		{
			final Transaction tx = Transaction.current();
			try
			{
				tx.execute(new TransactionBody()
				{
					@Override
					public Object execute() throws Exception
					{
						doWrite(counter);
						return null;
					}
				});
			}
			catch (final Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		private void doWrite(final long counter)
		{
			final String newMime = "Mime-" + counter;
			final String oldMime = media.getMime();
			assertFalse("already got new mime before", newMime.equals(oldMime));
			media.setMime(newMime);
			modelService.save(media);
			assertEquals("new mime is not visible", newMime, media.getMime());
		}
	}

	protected String dumpMediaMimeInfo(final MediaModel media)
	{
		final PK mediaPK = media.getPk();
		final Media jaloItem = JaloSession.getCurrentSession().getItem(mediaPK);

		final boolean isNew = modelService.isNew(media);
		final boolean isDirty = modelService.isModified(media);
		final boolean txRunning = Transaction.current().isRunning();
		final boolean txCacheIsolationEnabled = Config.itemCacheIsolationActivated();
		final boolean itemCacheBound = jaloItem.isCacheBound();

		return "Media(pk:" + media.getPk() + "/" + jaloItem.getPK() + ", new=" + isNew + ", modified=" + isDirty + ", txRunning="
				+ txRunning + ", txCacheIsolation=" + txCacheIsolationEnabled + ", cacheBound=" + itemCacheBound;
	}

	private void destroyMedia(final MediaModel media)
	{
		try
		{
			final CatalogVersionModel catalogVersion = media.getCatalogVersion();
			final CatalogModel catalog = catalogVersion.getCatalog();

			modelService.remove(media);
			modelService.remove(catalogVersion);
			modelService.remove(catalog);
		}
		catch (final Exception e)
		{
			// ignore for now
		}
	}

	private MediaModel setupMedia(final String catalogId, final String mediaCode, final String initialMime)
	{
		final CatalogVersionModel catVersion = setUpCatalog(catalogId);

		final MediaModel media = createAndSaveMedia(catVersion, mediaCode, initialMime);
		final PK mediaPk = media.getPk();
		final Media jaloMedia = modelService.getSource(media);

		assertNotNull(mediaPk);
		assertNotNull(jaloMedia);
		assertEquals(mediaPk, jaloMedia.getPK());

		assertEquals(mediaCode, media.getCode());
		assertEquals(initialMime, media.getMime());

		assertEquals(mediaCode, jaloMedia.getCode());
		assertEquals(initialMime, jaloMedia.getMime());

		return media;
	}

	private <V> V runInOtherThread(final Callable<V> callable, final int timeoutSeconds)
	{
		final ExecutorService pool = Executors.newFixedThreadPool(1, new ThreadFactory()
		{
			final Tenant tenant = Registry.getCurrentTenantNoFallback();

			@Override
			public Thread newThread(final Runnable r)
			{
				return new RegistrableThread(r)
				{
					@Override
					public void internalRun()
					{
						try
						{
							Registry.setCurrentTenant(tenant);
							super.internalRun();
						}
						finally
						{
							JaloSession.deactivate();
							Registry.unsetCurrentTenant();
						}
					}
				};
			}
		});
		try
		{
			final Future<V> future = pool.submit(callable);
			return future.get(timeoutSeconds, TimeUnit.SECONDS);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
			fail("interrupted while waiting");
		}
		catch (final ExecutionException e)
		{
			fail("unexpected execution exception " + e.getCause());
		}
		catch (final TimeoutException e)
		{
			fail("callable " + callable + " did not finish within maximum " + timeoutSeconds + " seconds to wait");
		}
		finally
		{
			pool.shutdownNow();
		}
		return null;
	}

	private MediaModel createAndSaveMedia(final CatalogVersionModel cv, final String code, final String mime)
	{
		final MediaModel media = modelService.create(MediaModel.class);
		media.setCode(code);
		media.setMime(mime);
		media.setCatalogVersion(cv);

		modelService.save(media);

		return media;
	}

	private CatalogVersionModel setUpCatalog(final String catId)
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(catId);

		final CatalogVersionModel cv = modelService.create(CatalogVersionModel.class);
		cv.setVersion(catId + "Version");
		cv.setCatalog(catalog);

		modelService.saveAll(catalog, cv);

		return cv;
	}

}
