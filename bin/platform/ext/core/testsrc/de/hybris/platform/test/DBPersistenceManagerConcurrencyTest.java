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
package de.hybris.platform.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.property.DBPersistenceManager;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;


/**
 * PLA-11453
 */
@PerformanceTest
public class DBPersistenceManagerConcurrencyTest extends HybrisJUnit4Test
{
	private static final int MULTIPLICATOR = 3; // getting rid off this bamboo ": not all threads did fetch at least once in time" by extending the time setup ( see PLA-11827, PLA-11832 for example)

	@Test
	public void testConcurrentAccessLoadedBefore()
	{
		testConcurrentAccess(50, 90, false, false);
	}

	@Test
	public void testConcurrentAccessNotLoaded()
	{
		testConcurrentAccess(50, 90, true, false);
	}

	@Test
	public void testConcurrentAccessCleared()
	{
		testConcurrentAccess(50, 90, false, true);
	}

	private void testConcurrentAccess(final int THREADS, final int DURATION_SECONDS, final boolean clearType,
			final boolean clearComplete)
	{
		final Tenant t = Registry.getCurrentTenant();

		final String TYPE = "Product";
		final ComposedType ct = TypeManager.getInstance().getComposedType(TYPE);

		final TestDBPM dbpm = new TestDBPM((AbstractTenant) t, t.getInvalidationManager());

		dbpm.loadPersistenceInfos();

		final long endTime = System.currentTimeMillis() + (DURATION_SECONDS * 1000);
		do
		{
			testConcurrentAccessLoop(dbpm, TYPE, ct, THREADS, clearType, clearComplete);
		}
		while (System.currentTimeMillis() < endTime);
	}

	private void testConcurrentAccessLoop(final DBPersistenceManager dbpm, final String TYPE, final ComposedType ct,
			final int THREADS, final boolean clearType, final boolean clearComplete)
	{
		final CountDownLatch atLeastOnceFetched = new CountDownLatch(THREADS);

		final TestThreadsHolder<DBPMAccessRunner> threads = new TestThreadsHolder<DBPMAccessRunner>(THREADS, true)
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new DBPMAccessRunner(dbpm, TYPE, atLeastOnceFetched);
			}
		};

		assertTrue("threads did not prepare in time", threads.waitForPrepared(15 * MULTIPLICATOR, TimeUnit.SECONDS));
		threads.startAll();
		boolean threadsBeingStopped = false;
		try
		{
			if (clearType || clearComplete)
			{
				// let all access at least for one time
				try
				{
					assertTrue("not all threads did fetch at least once in time",
							atLeastOnceFetched.await(30 * MULTIPLICATOR, TimeUnit.SECONDS));

					// now clear if requested
					if (clearType)
					{
						dbpm.clearComposedType(ct.getPK(), TYPE);
					}
					else if (clearComplete)
					{
						dbpm.simulateFullClear();
					}
					// wait again and let threads access for some time
					waitSeconds(5 * MULTIPLICATOR);
				}
				catch (final InterruptedException e)
				{
					Thread.currentThread().interrupt(); // restore flag
				}
			}
			else
			{
				waitSeconds(5 * MULTIPLICATOR);
			}
			threadsBeingStopped = true; // if we got here we can safely assume that threads are being stopped
			assertTrue("not all threads ended normally", threads.stopAndDestroy(30 * MULTIPLICATOR));
			assertEquals("got worker errors", Collections.EMPTY_MAP, threads.getErrors());
		}
		finally
		{
			if (!threadsBeingStopped)
			{
				threads.stopAndDestroy(30 * MULTIPLICATOR);
			}
		}
	}

	private boolean waitSeconds(final int sec)
	{
		try
		{
			Thread.sleep(sec * 1000);
			return true;
		}
		catch (final InterruptedException e1)
		{
			Thread.currentThread().interrupt();
			return false;
		}
	}

	static class DBPMAccessRunner implements Runnable
	{
		final DBPersistenceManager dbPersistenceManager;
		final String typeCode;
		final CountDownLatch atLeastOnceFetched;

		DBPMAccessRunner(final DBPersistenceManager dbpm, final String typeCode, final CountDownLatch atLeastOnceFetched)
		{
			this.dbPersistenceManager = dbpm;
			this.typeCode = typeCode;
			this.atLeastOnceFetched = atLeastOnceFetched;
		}

		@Override
		public void run()
		{
			final Thread t = Thread.currentThread();
			boolean first = true;
			do
			{
				final TypeInfoMap info = dbPersistenceManager.getPersistenceInfo(typeCode);
				if (first)
				{
					first = false;
					atLeastOnceFetched.countDown();
				}
				assertNotNull("got no info", info);
				if (info.isEmpty())
				{
					assertFalse("empty info despite manager still loaded", dbPersistenceManager.isLoaded());
				}
				else
				{
					assertEquals(typeCode, info.getCode());
				}
			}
			while (!t.isInterrupted());
		}
	}

	static class TestDBPM extends DBPersistenceManager
	{
		public TestDBPM(final AbstractTenant tenant, final InvalidationManager invManager)
		{
			super(tenant, invManager);
		}

		@Override
		protected void registerHJMPFinderListeners(final InvalidationManager invMan)
		{
			// no op to avoid real registering for this test class
		}
	}
}
