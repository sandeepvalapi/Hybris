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
package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.ItemPropertyValue;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@PerformanceTest
public class NewSyncTimestampTest extends HybrisJUnit4Test
{

	private static final int time = 60 * 1000;

	private int threads = 2;

	private Item src1, tgt1;
	private Item src2, tgt2;
	private Object pair1Lock;
	private Object pair2Lock;

	private CatalogManager catalogManager;
	private FlexibleSearch flexibleSearch;
	private SyncItemJob job;

	@Before
	public void setUp() throws ConsistencyCheckException
	{
		catalogManager = CatalogManager.getInstance();
		flexibleSearch = FlexibleSearch.getInstance();
		src1 = UserManager.getInstance().createTitle("foo1");
		tgt1 = UserManager.getInstance().createTitle("bar1");
		src2 = UserManager.getInstance().createTitle("foo2");
		tgt2 = UserManager.getInstance().createTitle("bar2");

		final Catalog catalog = CatalogManager.getInstance().createCatalog("foo");
		final CatalogVersion src = CatalogManager.getInstance().createCatalogVersion(catalog, "bar", null);
		src.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));
		final CatalogVersion tgt = CatalogManager.getInstance().createCatalogVersion(catalog, "ba2r", null);
		tgt.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));

		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, "foo");
		args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
		args.put(CatalogVersionSyncJob.TARGETVERSION, tgt);
		job = catalogManager.createCatalogVersionSyncJob(args);

		pair1Lock = new Object();
		pair2Lock = new Object();

		threads = CatalogVersionSyncJob.getDefaultMaxThreads(jaloSession.getTenant()) * 2;
	}

	@Test
	public void testTimestamps()
	{
		final Tenant ten = Registry.getCurrentTenant();
		final JaloSession jSession = jaloSession;
		final TestWorker[] testWorker = new TestWorker[threads];
		for (int i = 0; i < threads; i++)
		{
			testWorker[i] = new TestWorker(ten, jSession);
		}
		for (int i = 0; i < threads; i++)
		{
			new RegistrableThread(testWorker[i], "TestWorker-" + i).start();
		}

		try
		{
			Thread.sleep(2000);
		}
		catch (final InterruptedException e)
		{
			// fine
		}

		boolean allDone;
		do
		{
			allDone = true;
			for (int i = 0; i < threads; i++)
			{
				if (!testWorker[i].isDone())
				{
					allDone = false;
					break;
				}
			}
			if (!allDone)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (final InterruptedException e)
				{
					// fine
				}
			}
		}
		while (!allDone);

		for (int i = 0; i < threads; i++)
		{
			final Error error = testWorker[i].getError();
			if (error != null)
			{
				Assert.fail("got worker " + i + " error: " + error.getMessage());
			}
		}

	}

	protected ItemSyncTimestamp queryNonCatalogItemCopy(final Item source)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final boolean exclusive = job.isExclusiveModeAsPrimitive();
		if (exclusive)
		{
			params.put("job", job);
		}
		params.put("srcItem", source);
		params.put("tgtVer", job.getTargetVersion());
		final List<ItemSyncTimestamp> rows = flexibleSearch.search(//
				null,//
				"SELECT {" + Item.PK + "} " + //
						"FROM {" + CatalogConstants.TC.ITEMSYNCTIMESTAMP + "*}" + //
						"WHERE {" + ItemSyncTimestamp.SYNCJOB + "}" + (exclusive ? "=?job" : "=0") + " AND " + //
						(exclusive ? "" : "{" + ItemSyncTimestamp.TARGETVERSION + "}=?tgtVer AND ") + //
						"{" + ItemSyncTimestamp.SOURCEITEM + "}=?srcItem", //
				params, //
				ItemSyncTimestamp.class).getResult();

		ItemSyncTimestamp chosenTS = null;

		if (!rows.isEmpty())
		{

			for (final ItemSyncTimestamp ts : rows)
			{
				if (chosenTS == null || chosenTS.getCreationTime().after(ts.getCreationTime()))
				{
					chosenTS = ts;
				}
			}
			Assert.assertEquals(1, rows.size());
		}
		return chosenTS != null ? chosenTS : null;
	}

	protected ItemSyncTimestamp createSyncTimestamp(final Item source, final Item copy)
	{

		final Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put(ItemSyncTimestamp.SOURCEITEM, source);
		attributes.put(ItemSyncTimestamp.TARGETITEM, copy != null ? new ItemPropertyValue(copy.getPK()) : null);
		if (job.isExclusiveModeAsPrimitive())
		{
			attributes.put(ItemSyncTimestamp.SYNCJOB, job);
		}
		else
		{
			attributes.put(ItemSyncTimestamp.SOURCEVERSION, job.getSourceVersion());
			attributes.put(ItemSyncTimestamp.TARGETVERSION, job.getTargetVersion());
		}
		attributes.put(ItemSyncTimestamp.LASTSYNCTIME, new Date());
		Date modTS = source.getModificationTime();
		if (modTS == null)
		{
			modTS = source.getCreationTime();
		}
		attributes.put(ItemSyncTimestamp.LASTSYNCSOURCEMODIFIEDTIME, modTS);
		return catalogManager.createItemSyncTimestamp(attributes);
	}

	protected void addOrRemoveTimestamp(final int pairNr)
	{
		if (pairNr == 1)
		{
			synchronized (pair1Lock)
			{
				final ItemSyncTimestamp itemSyncTimestamp = queryNonCatalogItemCopy(src1);
				if (itemSyncTimestamp != null)
				{
					try
					{
						itemSyncTimestamp.remove();
					}
					catch (final ConsistencyCheckException e)
					{
						throw new RuntimeException(e);
					}
				}
				else
				{
					createSyncTimestamp(src1, tgt1);
				}
			}
		}
		else
		{
			synchronized (pair2Lock)
			{
				final ItemSyncTimestamp itemSyncTimestamp = queryNonCatalogItemCopy(src2);
				if (itemSyncTimestamp != null)
				{
					try
					{
						itemSyncTimestamp.remove();
					}
					catch (final ConsistencyCheckException e)
					{
						throw new RuntimeException(e);
					}
				}
				else
				{
					createSyncTimestamp(src2, tgt2);
				}
			}
		}
	}

	private class TestWorker implements Runnable
	{
		private final Tenant tenant;
		private final JaloSession jSession;
		private final long endTS;
		private boolean done = false;
		private Error error;

		TestWorker(final Tenant tenant, final JaloSession jSession)
		{
			this.tenant = tenant;
			this.jSession = jSession;
			this.endTS = System.currentTimeMillis() + time;
		}

		Error getError()
		{
			return error;
		}

		boolean isDone()
		{
			return done;
		}

		@Override
		public void run()
		{
			try
			{
				Registry.setCurrentTenant(tenant);
				jSession.activate();
				int counter = 0;
				while (System.currentTimeMillis() <= endTS)
				{
					addOrRemoveTimestamp((counter++ % 2) + 1);
				}
			}
			catch (final Error e)
			{
				error = e;
			}
			finally
			{
				done = true;
				JaloSession.deactivate();
				Registry.unsetCurrentTenant();
			}
		}
	}
}
