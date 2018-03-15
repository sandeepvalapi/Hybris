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
package de.hybris.platform.jalo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.regioncache.test.helper.DeadlockDetector;
import de.hybris.platform.regioncache.test.helper.ThreadDump;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.BridgeInterface;

import java.util.concurrent.TimeUnit;

import org.junit.Test;


/**
 * Testing particular performance optimization in {@link JaloImplementationManager}. For comparison we copied the
 * previous implementation into {@link PreviousJaloImplementationManager}!
 */
@IntegrationTest
public class JaloItemWrappingTest extends HybrisJUnit4Test
{
	private enum ImplType
	{
		NEW, OLD, NONCACHED;
	}

	@Test
	public void testItemWrappingOptimization() throws ConsistencyCheckException
	{
		final int COUNT = 1500000;

		final Item i = UserManager.getInstance().createTitle("ttt");
		final BridgeInterface itf = i.getImplementation();

		final long oldTimeSingleThreaded = testWrappingPerformanceSingleThreaded(itf, COUNT, ImplType.OLD);
		final long newTimeSingleThreaded = testWrappingPerformanceSingleThreaded(itf, COUNT, ImplType.NEW);
		final long nonCachedTimeSingleThreaded = testWrappingPerformanceSingleThreaded(itf, COUNT, ImplType.NONCACHED);


		System.out.println("oldTimeSingleThreaded:" + oldTimeSingleThreaded);
		System.out.println("newTimeSingleThreaded:" + newTimeSingleThreaded);
		System.out.println("nonCachedTimeSingleThreaded:" + nonCachedTimeSingleThreaded);

		assertTrue(oldTimeSingleThreaded >= newTimeSingleThreaded);
	}

	@Test
	public void testItemWrappingOptimizationMultiThreaded() throws ConsistencyCheckException
	{

		final int THREADS = 50;
		final int COUNT = 150000;
		final int TIMEOUT_SECONDS_PER_RUN = 30;

		final long oldTimeMultiThreaded = testWrappingPerformanceMultiThreaded(THREADS, COUNT, ImplType.OLD,
				TIMEOUT_SECONDS_PER_RUN);
		final long newTimeMultThreaded = testWrappingPerformanceMultiThreaded(THREADS, COUNT, ImplType.NEW, TIMEOUT_SECONDS_PER_RUN);
		final long nonCachedTimeMultiThreaded = testWrappingPerformanceMultiThreaded(THREADS, COUNT, ImplType.NONCACHED,
				TIMEOUT_SECONDS_PER_RUN);

		System.out.println("oldTimeMultiThreaded:" + oldTimeMultiThreaded);
		System.out.println("newTimeMultThreaded:" + newTimeMultThreaded);
		System.out.println("nonCachedTimeMultiThreaded:" + nonCachedTimeMultiThreaded);

		assertTrue(oldTimeMultiThreaded >= newTimeMultThreaded);
	}


	private long testWrappingPerformanceMultiThreaded(final int threads, final int count, final ImplType impl,
			final int timeoutSeconds)
	{
		final RunnerCreator<TestRunner> creator = new RunnerCreator<JaloItemWrappingTest.TestRunner>()
		{
			@Override
			public TestRunner newRunner(final int threadNumber)
			{
				try
				{
					final Item i = UserManager.getInstance().createTitle("ttt-" + impl + "-" + threadNumber);
					final BridgeInterface itf = i.getImplementation();

					return new TestRunner(itf, count, impl);
				}
				catch (final ConsistencyCheckException e)
				{
					fail(e.getMessage());
					return null;
				}
			}
		};
		final TestThreadsHolder<TestRunner> holder = new TestThreadsHolder<TestRunner>(threads, creator, true);

		holder.startAll();
		final boolean finished = holder.waitForAll(timeoutSeconds, TimeUnit.SECONDS);

		if (!finished)
		{
			ThreadDump.dumpThreads(System.err);
			DeadlockDetector.printDeadlocks(System.err);
			holder.stopAll();
		}

		assertTrue("not all threads finished correctly", finished);
		long maxTime = 0;
		for (final TestRunner r : holder.getRunners())
		{
			maxTime = Math.max(maxTime, r.time);
		}
		return maxTime;
	}

	static class TestRunner implements Runnable
	{
		final BridgeInterface itf;
		final int count;
		final ImplType impl;

		TestRunner(final BridgeInterface itf, final int count, final ImplType impl)
		{
			this.itf = itf;
			this.count = count;
			this.impl = impl;
		}

		volatile long time = -1;

		@Override
		public void run()
		{
			final long t = testWrappingPerformanceSingleThreaded(itf, count, impl);
			time = t; // volatile write
		}
	}

	private static long testWrappingPerformanceSingleThreaded(final BridgeInterface itf, final int count, final ImplType impl)
	{
		final Tenant t = Registry.getCurrentTenantNoFallback();
		final long t1 = System.currentTimeMillis();
		switch (impl)
		{
			case OLD:
				for (int i = 0; i < count && !Thread.currentThread().isInterrupted(); i++)
				{
					assertNotNull(PreviousJaloImplementationManager.createJaloObject(t, itf));
				}
				break;
			case NEW:
				for (int i = 0; i < count && !Thread.currentThread().isInterrupted(); i++)
				{
					assertNotNull(JaloImplementationManager.createJaloObject(t, itf));
				}
				break;
			case NONCACHED:
				for (int i = 0; i < count && !Thread.currentThread().isInterrupted(); i++)
				{
					assertNotNull(NonCacheJaloImplementationManager.createJaloObject(t, itf));
				}
				break;
		}
		return System.currentTimeMillis() - t1;
	}

}
