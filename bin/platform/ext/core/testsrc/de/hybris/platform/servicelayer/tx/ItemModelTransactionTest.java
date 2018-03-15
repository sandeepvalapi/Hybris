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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultPersistenceTypeService;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests item data invalidation after updates inside and outside of transaction for both model and jalo layer.
 */
@IntegrationTest
public class ItemModelTransactionTest extends ServicelayerBaseTest
{
	private static final String MIME_BEFORE = "mime";
	private static final String MIME_AFTER = "mimeNew";

	private boolean cfgSaved = false;
	private String cfgBefore = null;

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

		if (cfgSaved)
		{
			Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, cfgBefore);
			cfgSaved = false;
			cfgBefore = null;
		}
	}

	private void enableDirectPersistenceMode()
	{
		if (!cfgSaved)
		{
			cfgBefore = Config.getParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE);
			cfgSaved = true;
		}
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "false");
	}

	private void disableDirectPersistenceMode()
	{
		if (!cfgSaved)
		{
			cfgBefore = Config.getParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE);
			cfgSaved = true;
		}
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "true");
	}

	@Test
	public void testModificationTimeUpdateNoTx() throws InterruptedException, JaloBusinessException
	{
		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		final Media jaloMedia = modelService.getSource(media);
		final PK mediaPK = media.getPk();
		assertEquals(mediaPK, jaloMedia.getPK());

		final Date modificationTimeBefore = jaloMedia.getModificationTime();
		Thread.sleep(1500);
		jaloMedia.setMime(MIME_AFTER);
		final Date modificationTimeAfter = jaloMedia.getModificationTime();

		assertNotNull(modificationTimeBefore);
		assertNotNull(modificationTimeAfter);
		assertTrue(modificationTimeBefore.before(modificationTimeAfter));
	}

	@Test
	public void testModificationTimeUpdateTxCommit() throws Exception
	{
		testModificationTimeUpdateTx(true);
	}

	@Test
	public void testModificationTimeUpdateTxRollback() throws Exception
	{
		testModificationTimeUpdateTx(false);
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
			Thread.sleep(1500); // Some DB (MySql) do not have milliseconds!


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

	@Test
	public void testModificationTimeUpdateOutsideTx() throws Exception
	{
		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		final Media jaloMedia = modelService.getSource(media);
		final PK mediaPK = media.getPk();
		assertEquals(mediaPK, jaloMedia.getPK());

		final Date modModelBefore = media.getModifiedtime();
		final Date modJaloBefore = jaloMedia.getModificationTime();
		assertNotNull(modModelBefore);
		assertNotNull(modJaloBefore);
		assertEquals(modJaloBefore, modModelBefore);
		Thread.sleep(1500); // Some DB (MySql) do not have milliseconds!


		final Transaction tx = Transaction.current();
		tx.execute(new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				// Attention: we MUST read the media at least once to attach it to this
				// transaction. Otherwise we'd see the outside change as legal COMMITTED
				// state!
				final Date modJaloInTxBefore = jaloMedia.getModificationTime();
				assertNotNull(modJaloInTxBefore);
				assertEquals(modJaloBefore, modJaloInTxBefore);

				final Date modJaloOutside = changeMimeInOtherThread(mediaPK, MIME_AFTER);

				final Date modModelAfter = media.getModifiedtime();
				final Date modJaloAfter = jaloMedia.getModificationTime();

				assertNotNull(modModelAfter);
				assertNotNull(modJaloAfter);
				assertNotNull(modJaloOutside);
				assertFalse(
						"tx can see mod time from outside tx (expected " + modJaloBefore.getTime() + " but got "
								+ modJaloAfter.getTime(), modJaloOutside.equals(modJaloAfter));

				assertEquals(modModelAfter.getTime(), modJaloAfter.getTime());
				assertEquals(modJaloBefore.getTime(), modJaloAfter.getTime());
				assertEquals(modModelBefore.getTime(), modModelAfter.getTime());

				return null;
			}
		});

		modelService.refresh(media); // actually not necessary but more likely to find error

		final Date modModelAfterTx = media.getModifiedtime();
		final Date modJaloAfterTx = jaloMedia.getModificationTime();

		assertNotNull(modModelAfterTx);
		assertNotNull(modJaloAfterTx);
		assertEquals(modModelAfterTx, modJaloAfterTx);
		assertTrue(modJaloBefore.before(modJaloAfterTx));
	}

	private Date changeMimeInOtherThread(final PK mediaPK, final String newMime)
	{
		return runInOtherThread(new Callable<Date>()
		{
			@Override
			public Date call() throws Exception
			{
				final Media jaloMedia = JaloSession.getCurrentSession().getItem(mediaPK);
				final MediaModel media = modelService.get(jaloMedia);

				media.setMime(newMime);
				modelService.save(media);

				return jaloMedia.getModificationTime();
			}
		}, 15);
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
	public void testManualInvalidationOutsideTx()
	{

		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		final Media jaloMedia = modelService.getSource(media);
		final PK mediaPK = media.getPk();
		assertEquals(mediaPK, jaloMedia.getPK());

		// check before
		assertEquals(MIME_BEFORE, media.getMime());
		assertEquals(MIME_BEFORE, jaloMedia.getMime());
		// read in other thread
		String[] outerMimes = getMimeFromOtherThread(mediaPK);
		assertEquals(MIME_BEFORE, outerMimes[0]);
		assertEquals(MIME_BEFORE, outerMimes[1]);
		// read direct
		assertEquals(MIME_BEFORE, readMimeViaJDBC(mediaPK));

		// update direct -> caches stay untouched
		changeMimeViaJDBC(mediaPK, MIME_AFTER);
		// read direct again
		assertEquals(MIME_AFTER, readMimeViaJDBC(mediaPK));

		// must have the old values here
		assertEquals(MIME_BEFORE, jaloMedia.getMime());
		assertEquals(MIME_BEFORE, media.getMime());
		modelService.refresh(media); // even after refreshing !
		assertEquals(MIME_BEFORE, media.getMime());
		// read in other thread
		outerMimes = getMimeFromOtherThread(mediaPK);
		assertEquals(MIME_BEFORE, outerMimes[0]);
		assertEquals(MIME_BEFORE, outerMimes[1]);

		// now invalidate manually
		Utilities.invalidateCache(mediaPK);

		// jalo must show new value now
		assertEquals(MIME_AFTER, jaloMedia.getMime());
		// model is still old but that's correct since it has not been refreshed yet
		assertEquals(MIME_BEFORE, media.getMime());
		modelService.refresh(media); // now refresh
		assertEquals(MIME_AFTER, media.getMime());
		// read in other thread-> both should be fresh already
		outerMimes = getMimeFromOtherThread(mediaPK);
		assertEquals(MIME_AFTER, outerMimes[0]);
		assertEquals(MIME_AFTER, outerMimes[1]);
	}

	@Test
	public void testOtherThreadManualInvalidationOutsideTx() throws TimeoutException, InterruptedException
	{
		ThreadSync syncPoints = null;
		try
		{
			final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
			final Media jaloMedia = modelService.getSource(media);
			final PK mediaPK = media.getPk();
			assertEquals(mediaPK, jaloMedia.getPK());

			// check before
			assertEquals(MIME_BEFORE, media.getMime());
			assertEquals(MIME_BEFORE, jaloMedia.getMime());
			// read in other thread
			String[] outerMimes = getMimeFromOtherThread(mediaPK);
			assertEquals(MIME_BEFORE, outerMimes[0]);
			assertEquals(MIME_BEFORE, outerMimes[1]);
			// read direct
			assertEquals(MIME_BEFORE, readMimeViaJDBC(mediaPK));

			// Now let other thread change it manually and fire a manual invalidation.
			syncPoints = new ThreadSync();
			final JoinGate afterJDBC = syncPoints.createJoinGate("afterJDBC", 1);
			final JoinGate beforeInvalidation = syncPoints.createJoinGate("beforeInvalidation", 1);
			final Gate afterInvalidation = syncPoints.createGate("afterInvalidation", 1);
			changeMimeAndInvalidateManuallyInOtherThread(mediaPK, MIME_AFTER, syncPoints);
			afterJDBC.waitForAndReleaseAll(10, TimeUnit.SECONDS); // make sure JDBC op has been finished

			// read direct again
			assertEquals(MIME_AFTER, readMimeViaJDBC(mediaPK));

			// must have the old values here
			assertEquals(MIME_BEFORE, jaloMedia.getMime());
			assertEquals(MIME_BEFORE, media.getMime());
			modelService.refresh(media); // even after refreshing !
			assertEquals(MIME_BEFORE, media.getMime());
			// read in other thread
			outerMimes = getMimeFromOtherThread(mediaPK);
			assertEquals(MIME_BEFORE, outerMimes[0]);
			assertEquals(MIME_BEFORE, outerMimes[1]);


			// now let other thread invalidate
			beforeInvalidation.waitForAndReleaseAll(10, TimeUnit.SECONDS);
			afterInvalidation.waitForAll(10, TimeUnit.SECONDS);


			// jalo must show new value now
			assertEquals(MIME_AFTER, jaloMedia.getMime());
			// model is still old but that's correct since it has not been refreshed yet
			assertEquals(MIME_BEFORE, media.getMime());
			modelService.refresh(media); // now refresh
			assertEquals(MIME_AFTER, media.getMime());
			// read in other thread-> both should be fresh already
			outerMimes = getMimeFromOtherThread(mediaPK);
			assertEquals(MIME_AFTER, outerMimes[0]);
			assertEquals(MIME_AFTER, outerMimes[1]);
		}
		finally
		{
			if (syncPoints != null)
			{
				syncPoints.destroy();
			}
		}
	}

	private void changeMimeAndInvalidateManuallyInOtherThread(final PK mediaPK, final String newMime, final ThreadSync syncPoints)
	{
		final SyncPoint afterJDBC = syncPoints.get("afterJDBC");
		final SyncPoint beforeInvalidation = syncPoints.get("beforeInvalidation");
		final SyncPoint afterInvalidation = syncPoints.get("afterInvalidation");

		final Tenant tenant = Registry.getCurrentTenantNoFallback();

		final Thread t = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(tenant);

					// change via JDBC
					changeMimeViaJDBC(mediaPK, newMime);
					afterJDBC.arrive();

					// invalidate
					beforeInvalidation.arrive();
					Utilities.invalidateCache(mediaPK);
					afterInvalidation.arrive();
				}
				catch (final Exception e)
				{
					// YTODO Auto-generated catch block
					e.printStackTrace();
				}
				finally
				{
					JaloSession.deactivate();
					Registry.unsetCurrentTenant();
				}
			}
		};
		syncPoints.registerThread(t);

		t.start();
	}

	@Test
	public void testManualInvalidationInsideTxCommitting() throws Exception
	{
		if (Config.isHSQLDBUsed())
		{
			return; //should work, but doesn't at the moment. thomas has stacktrace from bamboo. it hangs inside hsqldb
		}
		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		final Media jaloMedia = modelService.getSource(media);
		final PK mediaPK = media.getPk();
		assertEquals(mediaPK, jaloMedia.getPK());

		// check before
		assertEquals(MIME_BEFORE, media.getMime());
		assertEquals(MIME_BEFORE, jaloMedia.getMime());
		// read in other thread
		String[] outerMimes = getMimeFromOtherThread(mediaPK);
		assertEquals(MIME_BEFORE, outerMimes[0]);
		assertEquals(MIME_BEFORE, outerMimes[1]);
		// read direct
		assertEquals(MIME_BEFORE, readMimeViaJDBC(mediaPK));

		final Transaction tx = Transaction.current();
		tx.execute(//
		new TransactionBody()
		{

			@Override
			public Object execute() throws Exception
			{
				// check before (again) inside tx
				assertEquals(MIME_BEFORE, media.getMime());
				assertEquals(MIME_BEFORE, jaloMedia.getMime());
				// read direct inide tx
				assertEquals(MIME_BEFORE, readMimeViaJDBC(mediaPK));


				// update direct (not committed) -> caches stay untouched
				changeMimeViaJDBC(mediaPK, MIME_AFTER);
				// read direct again inside tx
				assertEquals(MIME_AFTER, readMimeViaJDBC(mediaPK));
				// read direct again outside tx
				assertEquals(MIME_BEFORE, readMimeViaJDBC(mediaPK, false));

				// must have the old values here
				assertEquals(MIME_BEFORE, jaloMedia.getMime());
				assertEquals(MIME_BEFORE, media.getMime());
				modelService.refresh(media); // even after refreshing !
				assertEquals(MIME_BEFORE, media.getMime());
				// read in other thread
				String[] outerMimes = getMimeFromOtherThread(mediaPK);
				assertEquals(MIME_BEFORE, outerMimes[0]);
				assertEquals(MIME_BEFORE, outerMimes[1]);

				// now invalidate manually -> should be only effective inside this tx !!!
				Utilities.invalidateCache(mediaPK);

				// jalo must show new value immediately
				assertEquals(MIME_AFTER, jaloMedia.getMime());
				// model is still old but that's correct since it has not been refreshed yet
				assertEquals(MIME_BEFORE, media.getMime());
				modelService.refresh(media); // now refresh
				assertEquals(MIME_AFTER, media.getMime());
				// read in other thread-> both must show old value
				outerMimes = getMimeFromOtherThread(mediaPK);
				assertEquals(MIME_BEFORE, outerMimes[0]);
				assertEquals(MIME_BEFORE, outerMimes[1]);
				return null;
			}
		});
		// after commit all values must be correct
		assertEquals(MIME_AFTER, readMimeViaJDBC(mediaPK));
		assertEquals(MIME_AFTER, jaloMedia.getMime());
		assertEquals(MIME_AFTER, media.getMime());
		modelService.refresh(media); // now refresh
		assertEquals(MIME_AFTER, media.getMime());
		// read in other thread-> both must show old value
		outerMimes = getMimeFromOtherThread(mediaPK);
		assertEquals(MIME_AFTER, outerMimes[0]);
		assertEquals(MIME_AFTER, outerMimes[1]);
	}

	@Test
	public void testManualInvalidationInsideTxRollbacking() throws Exception
	{
		if (Config.isHSQLDBUsed())
		{
			return; //should work, but doesn't at the moment. thomas has stacktrace from bamboo. it hangs inside hsqldb
		}
		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		final Media jaloMedia = modelService.getSource(media);
		final PK mediaPK = media.getPk();
		assertEquals(mediaPK, jaloMedia.getPK());

		// check before
		assertEquals(MIME_BEFORE, media.getMime());
		assertEquals(MIME_BEFORE, jaloMedia.getMime());
		// read in other thread
		String[] outerMimes = getMimeFromOtherThread(mediaPK);
		assertEquals(MIME_BEFORE, outerMimes[0]);
		assertEquals(MIME_BEFORE, outerMimes[1]);
		// read direct
		assertEquals(MIME_BEFORE, readMimeViaJDBC(mediaPK));

		final Transaction tx = Transaction.current();
		final AtomicReference<Exception> rollbackExc = new AtomicReference<Exception>(null);
		try
		{
			tx.execute(//
			new TransactionBody()
			{

				@Override
				public Object execute() throws Exception
				{
					// check before (again) inside tx
					assertEquals(MIME_BEFORE, media.getMime());
					assertEquals(MIME_BEFORE, jaloMedia.getMime());
					// read direct inide tx
					assertEquals(MIME_BEFORE, readMimeViaJDBC(mediaPK));


					// update direct (not committed) -> caches stay untouched
					changeMimeViaJDBC(mediaPK, MIME_AFTER);
					// read direct again inside tx
					assertEquals(MIME_AFTER, readMimeViaJDBC(mediaPK));
					// read direct again outside tx
					assertEquals(MIME_BEFORE, readMimeViaJDBC(mediaPK, false));

					// must have the old values here
					assertEquals(MIME_BEFORE, jaloMedia.getMime());
					assertEquals(MIME_BEFORE, media.getMime());
					modelService.refresh(media); // even after refreshing !
					assertEquals(MIME_BEFORE, media.getMime());
					// read in other thread
					String[] outerMimes = getMimeFromOtherThread(mediaPK);
					assertEquals(MIME_BEFORE, outerMimes[0]);
					assertEquals(MIME_BEFORE, outerMimes[1]);

					// now invalidate manually -> should be only effective inside this tx !!!
					Utilities.invalidateCache(mediaPK);

					// jalo must show new value immediately
					assertEquals(MIME_AFTER, jaloMedia.getMime());
					// model is still old but that's correct since it has not been refreshed yet
					assertEquals(MIME_BEFORE, media.getMime());
					modelService.refresh(media); // now refresh
					assertEquals(MIME_AFTER, media.getMime());
					// read in other thread-> both must show old value
					outerMimes = getMimeFromOtherThread(mediaPK);
					assertEquals(MIME_BEFORE, outerMimes[0]);
					assertEquals(MIME_BEFORE, outerMimes[1]);

					final RuntimeException ex = new RuntimeException("rollback please");
					rollbackExc.set(ex);
					throw ex;
				}
			});
		}
		catch (final Exception e)
		{
			assertEquals(rollbackExc.get(), e);
		}
		// after commit all values must be correct
		assertEquals(MIME_BEFORE, readMimeViaJDBC(mediaPK));
		assertEquals(MIME_BEFORE, jaloMedia.getMime());
		// please note that media *is stale* after rollback !!!
		assertEquals(MIME_AFTER, media.getMime());
		modelService.refresh(media); // now refresh should correct it
		assertEquals(MIME_BEFORE, media.getMime());
		// read in other thread-> both must show old value
		outerMimes = getMimeFromOtherThread(mediaPK);
		assertEquals(MIME_BEFORE, outerMimes[0]);
		assertEquals(MIME_BEFORE, outerMimes[1]);
	}


	// returns [ jaloMime, modelMime ]
	private String[] getMimeFromOtherThread(final PK mediaPK)
	{
		return runInOtherThread(new Callable<String[]>()
		{
			@Override
			public String[] call() throws Exception
			{
				final Media jaloMedia = JaloSession.getCurrentSession().getItem(mediaPK);
				final MediaModel media = modelService.get(jaloMedia);

				return new String[]
				{ jaloMedia.getMime(), media.getMime() };
			}

		}, 15);
	}

	private void changeMimeViaJDBC(final PK mediaPK, final String newMime)
	{
		changeMimeViaJDBC(mediaPK, newMime, true);
	}

	private void changeMimeViaJDBC(final PK mediaPK, final String newMime, final boolean sameTx)
	{
		Connection con = null;
		PreparedStatement stmt = null;
		try
		{
			con = getConnection(sameTx);
			stmt = createMimeUpdateQuery(con, mediaPK, newMime);

			assertEquals(1, stmt.executeUpdate());
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
			fail("sql error " + e.getMessage());
		}
		finally
		{
			Utilities.tryToCloseJDBC(con, stmt, null);
		}
	}

	private PreparedStatement createMimeUpdateQuery(final Connection con, final PK mediaPK, final String newMime)
			throws IllegalArgumentException, SQLException
	{
		final ComposedType ct = TypeManager.getInstance().getComposedType(Media.class);
		final String tbl = ct.getTable();
		final String pkCol = ct.getAttributeDescriptorIncludingPrivate(Media.PK).getDatabaseColumn();
		final String mimeCol = ct.getAttributeDescriptorIncludingPrivate(Media.MIME).getDatabaseColumn();

		final PreparedStatement stmt = con.prepareStatement("UPDATE " + tbl + " SET " + mimeCol + "=? WHERE " + pkCol + "=?");

		final JDBCValueMappings jdbc = JDBCValueMappings.getInstance();
		jdbc.getValueWriter(String.class).setValue(stmt, 1, newMime);
		jdbc.getValueWriter(PK.class).setValue(stmt, 2, mediaPK);

		return stmt;
	}

	private String readMimeViaJDBC(final PK mediaPK)
	{
		return readMimeViaJDBC(mediaPK, true);
	}

	private String readMimeViaJDBC(final PK mediaPK, final boolean sameTx)
	{
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			con = getConnection(sameTx);
			stmt = createMimeReadQuery(con, mediaPK);

			rs = stmt.executeQuery();

			assertTrue("no media exists for " + mediaPK, rs.next());

			final String mime = (String) JDBCValueMappings.getInstance().getValueReader(String.class).getValue(rs, 1);

			assertFalse("mode than one media exists for " + mediaPK, rs.next());

			return mime;
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
			fail("sql error " + e.getMessage());
			return null;
		}
		finally
		{
			Utilities.tryToCloseJDBC(con, stmt, rs);
		}
	}

	private Connection getConnection(final boolean sameTx) throws SQLException
	{
		if (!sameTx && Transaction.current().isRunning())
		{
			return Registry.getCurrentTenantNoFallback().getDataSource().getConnection(false);
		}
		else
		{
			return Registry.getCurrentTenantNoFallback().getDataSource().getConnection();
		}
	}

	private PreparedStatement createMimeReadQuery(final Connection con, final PK mediaPK) throws IllegalArgumentException,
			SQLException
	{
		final ComposedType ct = TypeManager.getInstance().getComposedType(Media.class);
		final String tbl = ct.getTable();
		final String pkCol = ct.getAttributeDescriptorIncludingPrivate(Media.PK).getDatabaseColumn();
		final String mimeCol = ct.getAttributeDescriptorIncludingPrivate(Media.MIME).getDatabaseColumn();

		final PreparedStatement stmt = con.prepareStatement("SELECT " + mimeCol + " FROM " + tbl + " WHERE " + pkCol + "=?");

		final JDBCValueMappings jdbc = JDBCValueMappings.getInstance();
		jdbc.getValueWriter(PK.class).setValue(stmt, 1, mediaPK);

		return stmt;

	}

	@Test
	public void testInvalidationOnSingleJaloUpdate() throws Exception
	{
		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		final Media jaloMedia = modelService.getSource(media);
		final PK mediaPK = media.getPk();
		assertEquals(mediaPK, jaloMedia.getPK());

		RecordingInvalidationListener l = null;
		try
		{
			l = new RecordingInvalidationListener(Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, mediaPK.getTypeCodeAsString());

			jaloMedia.setMime(MIME_AFTER);
			assertEquals(MIME_AFTER, jaloMedia.getMime());

			assertInvalidations(l, mediaPK, 1, 1); // only one INV, directly onto cache
		}
		finally
		{
			if (l != null)
			{
				l.destroy();
				l = null;
			}
		}
	}


	@Test
	public void testInvalidationOnSingleModelUpdateLegacy() throws Exception
	{
		disableDirectPersistenceMode();

		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		final Media jaloMedia = modelService.getSource(media);
		final PK mediaPK = media.getPk();
		assertEquals(mediaPK, jaloMedia.getPK());

		RecordingInvalidationListener l = null;
		try
		{
			l = new RecordingInvalidationListener(Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, mediaPK.getTypeCodeAsString());

			media.setMime(MIME_AFTER);
			assertEquals(MIME_AFTER, media.getMime());
			modelService.save(media);
			assertEquals(MIME_AFTER, media.getMime());
			assertEquals(MIME_AFTER, jaloMedia.getMime());

			assertInvalidations(l, mediaPK, 2, 1); // two INV , first simulated, second onto cache
		}
		finally
		{
			if (l != null)
			{
				l.destroy();
				l = null;
			}
		}
	}

	@Test
	public void testInvalidationOnSingleModelUpdateSLD() throws Exception
	{
		enableDirectPersistenceMode();

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("bar");
		modelService.save(user);

		final PaymentInfoModel payment = modelService.create(PaymentInfoModel.class);
		payment.setUser(user);
		payment.setCode("foo");
		payment.setDuplicate(Boolean.TRUE);
		modelService.save(payment);

		final PK paymentInfoPK = payment.getPk();
		final PaymentInfo paymentJalo = modelService.getSource(payment);

		RecordingInvalidationListener l = null;
		try
		{
			l = new RecordingInvalidationListener(Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, paymentInfoPK.getTypeCodeAsString());

			payment.setDuplicate(Boolean.FALSE);
			assertEquals(Boolean.FALSE, payment.getDuplicate());
			modelService.save(payment);
			assertEquals(Boolean.FALSE, payment.getDuplicate());
			assertEquals(Boolean.FALSE, paymentJalo.isDuplicate());

			assertInvalidations(l, paymentInfoPK, 2, 1); // one local invalidation +the global one)
		}
		finally
		{
			if (l != null)
			{
				l.destroy();
				l = null;
			}
		}
	}

	@Test
	public void testInvalidationOnSingleJaloUpdateInTransactionNoDelayedStore() throws Exception
	{
		testInvalidationOnSingleJaloUpdateInTransaction(false);
	}

	@Test
	public void testInvalidationOnSingleJaloUpdateInTransactionFullDelayedStore() throws Exception
	{
		testInvalidationOnSingleJaloUpdateInTransaction(true);
	}


	private void testInvalidationOnSingleJaloUpdateInTransaction(final boolean fullDelayedSore) throws Exception
	{
		final MediaModel media = setupMedia("cat", "media", MIME_BEFORE);
		final Media jaloMedia = modelService.getSource(media);
		final PK mediaPK = media.getPk();
		assertEquals(mediaPK, jaloMedia.getPK());

		RecordingInvalidationListener l = null;
		try
		{
			l = new RecordingInvalidationListener(Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, mediaPK.getTypeCodeAsString())
			{
				@Override
				void onInvalidation(final InvalidationEvent event)
				{
					System.out.println("got invalidation " + event);
				}
			};

			final Transaction tx = Transaction.current();

			final String innerTxMimeValue = (String) tx.execute( //
					new TransactionBody()
					{
						@Override
						public Object execute() throws Exception
						{
							tx.enableDelayedStore(fullDelayedSore);
							assertEquals(Boolean.valueOf(fullDelayedSore), Boolean.valueOf(tx.isDelayedStoreEnabled()));

							assertNull("found media with using mime", queryMediaByPKAndMime(mediaPK, MIME_AFTER));

							assertEquals(MIME_BEFORE, jaloMedia.getMime());
							jaloMedia.setMime(MIME_AFTER);

							if (fullDelayedSore)
							{
								// update is not yet written -> query for new mime will fail but query for old mime will succeed
								assertNull("unexpected query success for new mime", queryMediaByPKAndMime(mediaPK, MIME_AFTER));
								assertEquals("old mime query did not work", jaloMedia, queryMediaByPKAndMime(mediaPK, MIME_BEFORE));
							}
							else
							{
								// update *is* written -> query for new mime will succeed and query for old mime will fail
								assertNull("unexpected query success for old mime", queryMediaByPKAndMime(mediaPK, MIME_BEFORE));
								assertEquals("new mime query did not work", jaloMedia, queryMediaByPKAndMime(mediaPK, MIME_AFTER));
							}
							return jaloMedia.getMime();
						}
					});
			assertEquals(MIME_AFTER, innerTxMimeValue);
			assertEquals(MIME_AFTER, jaloMedia.getMime());
			assertEquals("did not find media using new mime after tx commit", jaloMedia, queryMediaByPKAndMime(mediaPK, MIME_AFTER));

			assertInvalidations(l, mediaPK, 2, 1); // two INV: first just simulate, second onto cache
		}
		finally
		{
			if (l != null)
			{
				l.destroy();
				l = null;
			}
		}
	}

	private Media queryMediaByPKAndMime(final PK mediaPK, final String mime)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("pk", mediaPK);
		params.put("mime", mime);

		final List<Media> rows = FlexibleSearch.getInstance().search(//
				"SELECT {PK} FROM {Media} WHERE {PK}=?pk AND {mime}=?mime",//
				params, //
				Media.class).getResult();

		assertTrue("unexpected result size " + rows.size() + " expected 0 or 1", rows.isEmpty() || rows.size() == 1);

		return rows.isEmpty() ? null : rows.get(0);
	}

	private void assertInvalidations(final RecordingInvalidationListener l, final PK mediaPK, final int expectedOverallCount,
			final int expectedCacheInvalidationCount)
	{
		final List<InvalidationEvent> allInvalidations = l.getInvalidations(mediaPK);

		logInvalidations(allInvalidations, expectedOverallCount);

		final Collection<InvalidationEvent> cacheInvalidations = filterCacheInvalidationEvents(allInvalidations, Registry
				.getCurrentTenantNoFallback().getCache());

		assertEquals("more than expected invalidation(s): " + allInvalidations, expectedOverallCount, allInvalidations.size());
		assertEquals("more than expected cache invalidation(s): " + cacheInvalidations, expectedCacheInvalidationCount,
				cacheInvalidations.size());
	}

	private Collection<InvalidationEvent> filterCacheInvalidationEvents(final Collection<InvalidationEvent> all, final Cache cache)
	{
		if (CollectionUtils.isEmpty(all))
		{
			return all;
		}
		else
		{
			final Collection<InvalidationEvent> ret = new ArrayList<InvalidationEvent>(all.size());
			for (final InvalidationEvent e : all)
			{
				if (cache == e.target)
				{
					ret.add(e);
				}
			}
			return ret;
		}
	}

	private void logInvalidations(final Collection<InvalidationEvent> invalidations, final int expectedOverallCount)
	{
		if (invalidations.size() != expectedOverallCount)
		{
			System.err.println("==================================================================");
			for (final InvalidationEvent i : invalidations)
			{
				System.err.println(i);
				System.err.println("---->" + Utilities.getStackTraceAsString(i.stack));
			}
			System.err.println("==================================================================");
		}
	}

	@Test
	public void testTxRefreshBehaviour() throws Exception
	{
		final MediaModel media = setupMedia("Catalog", "MediaCode", MIME_BEFORE);
		final PK mediaPk = media.getPk();
		final Media jaloMedia = modelService.getSource(media);

		final Transaction tx = Transaction.current();
		tx.enableDelayedStore(true);
		final RecordingInvalidationListener listener = installMimeCheckingInvalidationListener(mediaPk, MIME_BEFORE, MIME_AFTER);
		try
		{
			tx.execute(new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					changeMedia(media, jaloMedia, mediaPk, MIME_BEFORE, MIME_AFTER, listener);
					return null;
				}
			});
			assertEquals(MIME_AFTER, jaloMedia.getMime());
			assertEquals(MIME_AFTER, media.getMime());
		}
		finally
		{
			listener.destroy();
		}
	}

	@Test
	public void testNestedTxRefreshBehaviour() throws Exception
	{
		final MediaModel media = setupMedia("Catalog", "MediaCode", MIME_BEFORE);
		final PK mediaPk = media.getPk();
		final Media jaloMedia = modelService.getSource(media);

		final Transaction tx = Transaction.current();
		tx.enableDelayedStore(false);

		final RecordingInvalidationListener listener = installMimeCheckingInvalidationListener(mediaPk, MIME_BEFORE, MIME_AFTER);
		try
		{
			tx.execute(// outer transaction
			new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					final Transaction tx2 = Transaction.current();
					tx2.execute(// inner transaction
					new TransactionBody()
					{

						@Override
						public Object execute() throws Exception
						{
							changeMedia(media, jaloMedia, mediaPk, MIME_BEFORE, MIME_AFTER, listener);
							return null;
						}
					});
					assertEquals(MIME_AFTER, jaloMedia.getMime());
					assertEquals(MIME_AFTER, media.getMime());
					return null;
				}
			});
			assertEquals(MIME_AFTER, media.getMime());
		}
		finally
		{
			listener.destroy();
		}
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

	private void changeMedia(final MediaModel media, final Media jaloMedia, final PK mediaPk, final String initialMime,
			final String newMime, final RecordingInvalidationListener l)
	{
		media.setMime(newMime);
		media.setAltText("Foo");

		assertEquals(newMime, media.getMime());
		assertEquals(initialMime, jaloMedia.getMime());

		modelService.save(media);

		assertInnerTxInvalidations(l, mediaPk);

		assertEquals(newMime, jaloMedia.getMime());
		assertEquals(newMime, media.getMime());

	}

	private RecordingInvalidationListener installMimeCheckingInvalidationListener(final PK mediaPk,
			final String expectedMimeBeforeCommit, final String expectedMimeAfterCommit)
	{
		return new RecordingInvalidationListener(Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY)
		{
			@Override
			void onInvalidation(final InvalidationEvent event)
			{
				final boolean isAfterCommit = event.target instanceof Cache;
				if (isForPK(event, mediaPk))
				{
					assertMimeInOuterThread(mediaPk, isAfterCommit ? expectedMimeAfterCommit : expectedMimeBeforeCommit);
				}
			}
		};
	}

	// Assures that at least one invalidation has been recorded for the given media PK.
	private void assertInnerTxInvalidations(final RecordingInvalidationListener l, final PK mediaPk)
	{
		final List<InvalidationEvent> invalidations = l.getInvalidations(mediaPk);
		assertFalse(invalidations.isEmpty());
	}

	private void assertMimeInOuterThread(final PK mediaPk, final String expectedMime)
	{
		final Transaction currentTx = Transaction.current();
		final String[] mimes = runInOtherThread(//
				new Callable<String[]>()
				{
					@Override
					public String[] call() throws Exception
					{
						final Transaction otherThreadTx = Transaction.current();
						assertNotSame(currentTx, otherThreadTx);
						System.err.println("Triggering other thread after invalidation..");
						// trigger reload
						final Media otherThreadJaloMedia = (Media) JaloSession.getCurrentSession().getItem(mediaPk);
						final MediaModel otherThreadMediaModel = modelService.get(mediaPk);

						final String otherThreadJaloMediaMime = otherThreadJaloMedia.getMime();
						final String otherThreadMediaModelMime = otherThreadMediaModel.getMime();

						return new String[]
						{ otherThreadJaloMediaMime, otherThreadMediaModelMime };
					}
				}, 15 /* seconds */);

		assertEquals("unexpected mime value from Jalo layer", expectedMime, mimes[0]);
		assertEquals("unexpected mime value from Model layer", expectedMime, mimes[1]);
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

	private static class RecordingInvalidationListener implements InvalidationListener
	{
		final Collection<InvalidationEvent> recordedInvalidations = new ConcurrentLinkedQueue<InvalidationEvent>();
		final InvalidationTopic topic;

		RecordingInvalidationListener(final Object... path)
		{
			this(InvalidationManager.getInstance().getInvalidationTopic(path));
		}

		RecordingInvalidationListener(final InvalidationTopic topic)
		{
			super();
			this.topic = topic;
			topic.addInvalidationListener(this);
		}

		@Override
		public void keyInvalidated(final Object[] key, final int invalidationType, final InvalidationTarget target,
				final RemoteInvalidationSource remoteSrc)
		{
			final InvalidationEvent e = new InvalidationEvent(key, invalidationType, target);
			recordedInvalidations.add(e);

			onInvalidation(e);
		}

		void destroy()
		{
			topic.removeInvalidationListener(this);
		}

		boolean hasInvalidation(final PK pk)
		{
			return !getInvalidations(pk).isEmpty();
		}

		List<InvalidationEvent> getInvalidations(final PK pk)
		{
			final List<InvalidationEvent> ret = new ArrayList<InvalidationEvent>();
			for (final InvalidationEvent event : recordedInvalidations)
			{
				if (isForPK(event, pk))
				{
					ret.add(event);
				}
			}
			return ret;
		}

		boolean isForPK(final InvalidationEvent event, final PK pk)
		{
			final String tc = pk.getTypeCodeAsString();
			final Object[] key = event.key;
			return key.length == 4 && Cache.CACHEKEY_HJMP.equals("" + key[0]) && Cache.CACHEKEY_ENTITY.equals("" + key[1]) // NOPMD
					&& tc.equals("" + key[2]) && key[3] instanceof PK && pk.equals(key[3]); // NOPMD
		}

		void onInvalidation(final InvalidationEvent event)
		{
			// hook
		}
	}


	private static class InvalidationEvent
	{
		final Object[] key;
		final int invalidationType;
		final InvalidationTarget target;
		final Exception stack;

		InvalidationEvent(final Object[] key, final int invalidationType, final InvalidationTarget target)
		{
			this.key = new Object[key.length];
			System.arraycopy(key, 0, this.key, 0, key.length);
			this.invalidationType = invalidationType;
			this.target = target;
			this.stack = new RuntimeException();
		}

		@Override
		public String toString()
		{
			String typeStr = "unknown";
			if (AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED == invalidationType)
			{
				typeStr = "MODIFY";
			}
			else if (AbstractCacheUnit.INVALIDATIONTYPE_REMOVED == invalidationType)
			{
				typeStr = "REMOVE";
			}
			return "<" + Arrays.deepToString(key) + "/" + typeStr + ">>" + target + ">";
		}
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

	public interface SyncPoint
	{
		void arrive() throws InterruptedException;
	}

	public interface Join extends SyncPoint
	{
		long getJoinCount();
	}

	public interface Gate extends SyncPoint
	{
		void waitForAll(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException;
	}

	public interface JoinGate extends SyncPoint
	{
		void waitForAndReleaseAll(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException;
	}

	public static class ThreadSync
	{
		private final ConcurrentMap<String, SyncPoint> points;
		private final Collection<Thread> threads;

		public ThreadSync()
		{
			this.points = new ConcurrentHashMap<String, SyncPoint>();
			this.threads = new CopyOnWriteArrayList<Thread>();
		}

		/**
		 * Registers a thread to allow orderly shutdown of testing threads via {@link #destroy()}.
		 */
		public void registerThread(final Thread t)
		{
			threads.add(t);
		}

		public <S extends SyncPoint> S get(final String key)
		{
			final S ret = (S) points.get(key);
			if (ret == null)
			{
				throw new IllegalArgumentException("there is no sync point with key '" + key + "'");
			}
			return ret;
		}

		public boolean destroy()
		{
			for (final Thread t : threads)
			{
				if (t.isAlive())
				{
					t.interrupt();
				}
			}

			boolean allDead = true;
			final long maxWaitTime = System.currentTimeMillis() + (15 * 1000);
			do
			{
				for (final Thread t : threads)
				{
					if (t.isAlive())
					{
						allDead = false;
						try
						{
							t.join(500);
						}
						catch (final InterruptedException e)
						{
							break;
						}
					}
				}
			}
			while (!allDead && maxWaitTime > System.currentTimeMillis());

			return allDead;
		}

		/**
		 * Creates a point that allows to ensure a specific number of threads have passed it.
		 * 
		 * This sync point can only be used once!
		 */
		public Gate createGate(final String key, final int parties)
		{
			return putIfAbsent(key, new Gate()
			{
				final CountDownLatch latch = new CountDownLatch(parties);

				@Override
				public void arrive()
				{
					latch.countDown();
				}

				@Override
				public void waitForAll(final long timeout, final TimeUnit unit) throws TimeoutException, InterruptedException
				{
					latch.await(timeout, unit);
				}
			});
		}

		/**
		 * Creates a sync point that causes a specific number of threads to pause until all of them have arrived. After
		 * that they are released all at once.
		 * 
		 * This sync point may be used several times.
		 */
		public Join createJoin(final String key, final int parties)
		{
			return putIfAbsent(key, new Join()
			{
				final AtomicLong counter = new AtomicLong();
				final CyclicBarrier barrier = new CyclicBarrier(parties, new Runnable()
				{

					@Override
					public void run()
					{
						counter.incrementAndGet();
					}
				});

				@Override
				public void arrive() throws InterruptedException
				{
					try
					{
						barrier.await();
					}
					catch (final BrokenBarrierException e)
					{
						throw new IllegalStateException(e);
					}
				}

				@Override
				public long getJoinCount()
				{
					return counter.get();
				}
			});
		}

		/**
		 * Creates a sync point which ensures that a specified number of threads arrive. The are held until the master
		 * calls {@link JoinGate#waitForAndReleaseAll(long, TimeUnit)}.
		 * 
		 * This point may be used several times.
		 */
		public JoinGate createJoinGate(final String key, final int parties)
		{
			return putIfAbsent(key, new JoinGate()
			{
				final CyclicBarrier barrier = new CyclicBarrier(parties + 1);

				@Override
				public void arrive() throws InterruptedException
				{
					try
					{
						barrier.await();
					}
					catch (final BrokenBarrierException e)
					{
						throw new IllegalStateException(e);
					}
				}

				@Override
				public void waitForAndReleaseAll(final long timeout, final TimeUnit unit) throws TimeoutException,
						InterruptedException
				{
					try
					{
						barrier.await(timeout, unit);
					}
					catch (final BrokenBarrierException e)
					{
						new IllegalStateException(e);
					}
				}
			});
		}

		private <S extends SyncPoint> S putIfAbsent(final String key, final S point)
		{
			final S previous = (S) points.putIfAbsent(key, point);
			return previous == null ? point : previous;

		}
	}
}
