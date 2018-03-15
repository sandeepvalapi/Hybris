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
package de.hybris.platform.core.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.JDBCConnectionPool;
import de.hybris.platform.jdbcwrapper.JDBCConnectionPoolInterruptedException;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Config;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;


@PerformanceTest
public class InitializationLockHandlerPerformanceTest extends AbstractLockHandlerIntegrationTest
{
	private static final Logger LOG = Logger.getLogger(InitializationLockHandlerPerformanceTest.class.getName());

	private InitializationLockHandler handler;

	@Before
	public void setUp()
	{
		//PLA-11236 - don't execute this test on hsql, it may hang
		Assume.assumeTrue(!Config.isHSQLDBUsed());
		this.handler = prepareHandler("LockPerformanceTest");
		printConnectionPoolStats(Registry.getCurrentTenant().getDataSource().getConnectionPool());
	}

	/**
	 * 
	 */
	private void printConnectionPoolStats(final JDBCConnectionPool connectionPool)
	{
		LOG.info("------Connection pool stats------");
		LOG.info("Max active :" + connectionPool.getMaxActive());
		LOG.info("Max Physical opened :" + connectionPool.getMaxPhysicalOpen());
		LOG.info("Active :" + connectionPool.getNumActive());
		LOG.info("Idle :" + connectionPool.getNumIdle());
		LOG.info("Physical opened :" + connectionPool.getNumPhysicalOpen());

	}

	@After
	public void tearDown()
	{
		//PLA-11236 - don't execute this test on hsql, it may hang
		if (!Config.isHSQLDBUsed())
		{
			clearHandler(handler);
		}
	}

	@Test
	public void testConcurrentIsLocked()
	{
		TestUtils.disableFileAnalyzer("Expecting that table 'LockPerformanceTest' hasn't been created yet");
		try
		{
			assertFalse(checkTestTableExists(handler));
			handler.lock(Registry.getCurrentTenantNoFallback(), "testing concurrent isLocked()");
			assertTrue(checkTestTableExists(handler));
			assertTrue(handler.isLocked());

			final int threads = THREADS / 2;
			final TestThreadsHolder<IsSystemUpdatingRunnable> randomAccessHolder = new TestThreadsHolder<IsSystemUpdatingRunnable>(
					threads, true)
			{
				@Override
				public IsSystemUpdatingRunnable newRunner(final int threadNumber)
				{
					return new IsSystemUpdatingRunnable(handler);
				}
			};

			randomAccessHolder.startAll();
			try
			{
				Thread.sleep(WAIT_SECONDS * 1000);
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
			assertTrue("not all test threads shut down orderly", randomAccessHolder.stopAndDestroy(30));
			assertEquals("found worker errors", Collections.EMPTY_MAP, randomAccessHolder.getErrors());

			long totalLocked = 0;
			long totalUnlocked = 0;
			for (final IsSystemUpdatingRunnable r : randomAccessHolder.getRunners())
			{
				if (r.lockedCount != -1)
				{
					totalLocked += r.lockedCount;
				}
				if (r.unlockedCount != -1)
				{
					totalUnlocked += r.unlockedCount;
				}
			}
			assertEquals(0, totalUnlocked);
			assertTrue(totalLocked > 0);

			final long totalCount = totalLocked + totalUnlocked;

			LOG.info("performed " + totalCount + " isLocked() operations in " + WAIT_SECONDS + " seconds using " + threads
					+ " threads (" + (totalCount / WAIT_SECONDS) + " operations/seconds)");
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}

	}

	@Test
	public void testConcurrentProcessingInLock()
	{
		assertFalse(checkTestTableExists(handler));

		//no synchronization 
		final Callable<Boolean> doItOnlyOnce = new Callable<Boolean>()
		{
			private final AtomicInteger stackCounter = new AtomicInteger(0);

			@Override
			public Boolean call() throws Exception
			{
				final int current = stackCounter.incrementAndGet();
				assertEquals("There could be only one executor at time", 1, current);
				stackCounter.decrementAndGet();
				return Boolean.TRUE;
			}
		};

		final AbstractTenant currentTenant = (AbstractTenant) Registry.getCurrentTenant();

		final AbstractTenant otherCusterIdTenant = createOtherClusterIdTenant();

		final TestThreadsHolder<AbstractProcessWithinLock> randomAccessHolder = new TestThreadsHolder<AbstractProcessWithinLock>(
				THREADS, true)
		{
			@Override
			public AbstractProcessWithinLock newRunner(final int threadNumber)
			{
				switch (threadNumber % 4)
				{
					case 0:
						return new UpdateProcessWithinLock(currentTenant, handler, doItOnlyOnce);
					case 1:
						return new UpdateProcessWithinLock(otherCusterIdTenant, handler, doItOnlyOnce);
					case 2:
						return new InitializationProcessWithinLock(otherCusterIdTenant, handler, doItOnlyOnce);
					case 3:
						return new InitializationProcessWithinLock(currentTenant, handler, doItOnlyOnce);
				}
				throw new IllegalArgumentException();
			}
		};

		randomAccessHolder.startAll();
		try
		{
			Thread.sleep(WAIT_SECONDS * 1000);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		assertTrue("not all test threads shut down orderly", randomAccessHolder.stopAndDestroy(30));
		assertEquals("found worker errors", Collections.EMPTY_MAP, randomAccessHolder.getErrors());

		int totalCount = 0;
		for (final AbstractProcessWithinLock r : randomAccessHolder.getRunners())
		{
			if (r.count != -1)
			{
				totalCount += r.count;
			}
		}
		assertTrue(totalCount > 0);
		LOG.info("performed " + totalCount + " in " + WAIT_SECONDS + " seconds using " + THREADS + " threads ("
				+ (totalCount / WAIT_SECONDS) + " operations/seconds)");
	}


	/**
	 * Worker which is only checking isLocked flag
	 */
	static private class IsSystemUpdatingRunnable implements Runnable
	{
		private final InitializationLockHandler handlerLocal;

		volatile long lockedCount = -1;
		volatile long unlockedCount = -1;

		IsSystemUpdatingRunnable(final InitializationLockHandler handlerLocal)
		{
			this.handlerLocal = handlerLocal;
		}

		@Override
		public void run()
		{
			int locked = 0;
			int unlocked = 0;
			try
			{
				while (!Thread.currentThread().isInterrupted())
				{
					if (handlerLocal.isLocked())
					{
						locked++;
					}
					else
					{
						unlocked++;
					}
				}
			}
			catch (final JDBCConnectionPoolInterruptedException e)
			{
				// this is ok
			}
			finally
			{
				lockedCount = locked;
				unlockedCount = unlocked;
			}
		}
	}


	private static abstract class AbstractProcessWithinLock implements Runnable
	{
		protected final InitializationLockHandler handlerLocal;
		protected final Callable<Boolean> runMethod;

		volatile long count = -1;

		AbstractProcessWithinLock(final InitializationLockHandler handlerLocal, final Callable<Boolean> runMethod)
		{
			this.handlerLocal = handlerLocal;
			this.runMethod = runMethod;
		}

		@Override
		public void run()
		{
			int index = 0;
			try
			{
				while (!Thread.currentThread().isInterrupted())
				{
					doSynchronized();
					index++;
				}
			}
			catch (final JDBCConnectionPoolInterruptedException e)
			{
				// this is ok
			}
			catch (final Exception e)
			{
				if (e instanceof RuntimeException)
				{
					throw (RuntimeException) e;
				}
				else
				{
					throw new RuntimeException(e);
				}
			}
			finally
			{
				count = index;
			}
		}

		abstract protected void doSynchronized() throws Exception;

		@Override
		public String toString()
		{
			return getClass().getSimpleName();
		}
	}

	/**
	 * Worker which does a {@link #runMethod} inside a update lock
	 */
	static class UpdateProcessWithinLock extends AbstractProcessWithinLock
	{
		final Tenant tenant;

		UpdateProcessWithinLock(final Tenant tenant, final InitializationLockHandler handlerLocal, final Callable<Boolean> runMethod)
		{
			super(handlerLocal, runMethod);
			this.tenant = tenant;
		}

		@Override
		protected void doSynchronized() throws Exception
		{
			handlerLocal.performLocked(tenant, runMethod, "UpdateProcessWithinLock");
		}
	}

	/**
	 * Worker which does a {@link #runMethod} inside a initialization lock
	 */
	static class InitializationProcessWithinLock extends AbstractProcessWithinLock
	{
		final Tenant tenant;

		InitializationProcessWithinLock(final Tenant tenant, final InitializationLockHandler handlerLocal,
				final Callable<Boolean> runMethod)
		{
			super(handlerLocal, runMethod);
			this.tenant = tenant;
		}

		@Override
		protected void doSynchronized() throws Exception
		{
			handlerLocal.performLocked(tenant, runMethod, "InitializationProcessWithinLock");
		}
	}
}
