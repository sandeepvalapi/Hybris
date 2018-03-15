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
package de.hybris.platform.regioncache.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.generation.GenerationalCounterService;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.impl.EHCacheRegion;
import de.hybris.platform.regioncache.test.helper.DeadlockDetector;
import de.hybris.platform.regioncache.test.helper.ThreadDump;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import junit.framework.Assert;
import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/CacheBlockingOnEvictionTest-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CacheBlockingOnEvictionTest
{
	public static final int NUMBER_OF_THREADS = 128;

	private static final Logger LOGGER = Logger.getLogger(CacheBlockingOnEvictionTest.class);

	@Resource
	protected CacheController controller;

	@Resource(name = "CacheRegion")
	protected EHCacheRegion region;

	@Resource(name = "generationalCounterService")
	protected GenerationalCounterService counterService;


	@Before
	public void init()
	{
		clean();
	}

	@After
	public void clean()
	{
		final Collection<CacheRegion> regions = controller.getRegions();
		for (final CacheRegion region : regions)
		{
			controller.clearCache(region);
		}

		System.gc();
	}


	@AfterClass
	public static void cleanEHCache()
	{
		CacheManager.getInstance().clearAll();
		// CacheManager.getInstance().removalAll();
		// CacheManager.getInstance().shutdown();
	}

	/**
	 * This test tries to verify that eviction does not lead to a deadlock. Deadlock can occur because of the following
	 * steps: 1.) Registrable key is added to a cache by a client.
	 * 
	 * 2.) Cache controller adds the key to proper registry, with a "callback" that inserts the key/value pair into the
	 * cache region map.
	 * 
	 * 3.) Cache registry acquires a lock for "registering". This lock is exclusive for registering, no invalidation or
	 * eviction can happen in parallel.
	 * 
	 * 4.) Cache registry registers the key
	 * 
	 * 5.) Cache registry executes callback
	 * 
	 * 6.) Callback inserts new entry into region map. If the region map is full, region map EVICTION happens.
	 * 
	 * 7.) Region map eviction causes region map eviction callback to execute.
	 * 
	 * 8.) Cache controller captures the callback and routes it AGAIN to cache registry to remove possible registration
	 * for just-evicted key.
	 * 
	 * 9.) Cache registry in "evict" method tries to acquire a lock for evicting. BUT... The cache registry already holds
	 * a lock for registering - see. 3.) We have a deadlock now.
	 * 
	 * 
	 * This problem is solved by "delaying" such evictions, so that two mutually exclusive locks does not have to be
	 * acquired at once, but one after another (first registration, then eviction).
	 * 
	 * How to make the test fail? Disable delayed eviction logic in evict method inside DefaultCacheRegistry.
	 * 
	 */
	@Test
	public void testCacheEvictionDeadlock()
	{
		controller.clearCache(controller.getRegions().iterator().next());

		final AtomicInteger errors = new AtomicInteger(0);

		final String[] types = new String[]
		{ "A", "B", "C", "D", "E" };

		final CountDownLatch endOfWorkLatch = new CountDownLatch(NUMBER_OF_THREADS);

		final RunnerCreator<Runnable> runnerCreator = new RunnerCreator()
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new CacheAddingWorker(counterService, controller, types, threadNumber, endOfWorkLatch, errors);
			}
		};

		final TestThreadsHolder workerThreads = new TestThreadsHolder<Runnable>(NUMBER_OF_THREADS, runnerCreator);

		//		System.out.println("starting workers...");
		workerThreads.startAll();

		boolean workersFinishedNormally = false;
		try
		{
			workersFinishedNormally = endOfWorkLatch.await(24, TimeUnit.SECONDS);
			if (!workersFinishedNormally)
			{
				LOGGER.error("Printing thread dump. Threads not stopped " + endOfWorkLatch.getCount());
				ThreadDump.dumpThreads(System.err);
				DeadlockDetector.printDeadlocks(System.err);
				workerThreads.stopAll();
			}
		}
		catch (final InterruptedException ex)
		{
			throw new RuntimeException(ex);
		}

		//verification
		Assert.assertTrue("Not all workers finished within time. This might indicate a deadlock! See threadDump(1)",
				workersFinishedNormally);
		Assert.assertEquals(0, errors.intValue());
	}

	@Test
	public void testCacheEvictionDeadlock2()
	{
		controller.clearCache(controller.getRegions().iterator().next());

		final AtomicInteger errors = new AtomicInteger(0);

		final CountDownLatch endOfWorkLatch = new CountDownLatch(2);

		final CacheValueLoader<Integer> loader = new CacheValueLoader<Integer>()
		{
			@Override
			public Integer load(final CacheKey key)
			{
				return Integer.valueOf(1);
			}
		};

		final RunnerCreator<Runnable> runnerCreator = new RunnerCreator()
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new Runnable()
				{
					@Override
					public void run()
					{
						final int ITERATIONS = 100000;

						Thread.currentThread().setName("testCacheEvictionDeadlock_" + threadNumber);

						LOGGER.info("starting worker " + Thread.currentThread().getName() + " for size "
								+ controller.getRegions().iterator().next().getCacheMaxEntries());

						try
						{
							for (int i = 0; i < ITERATIONS; ++i)
							{
								controller.getWithLoader(new EhCacheTestKey(threadNumber, (threadNumber * ITERATIONS) + (i % 100)),
										loader);
							}
						}
						catch (final Exception e)
						{
							errors.incrementAndGet();
							LOGGER.error("Error in thread " + threadNumber, e);
						}
						finally
						{
							endOfWorkLatch.countDown();
						}
					}
				};
			}
		};

		final TestThreadsHolder workerThreads = new TestThreadsHolder<Runnable>(2, runnerCreator);

		workerThreads.startAll();

		boolean workersFinishedNormally = false;
		try
		{
			workersFinishedNormally = endOfWorkLatch.await(36, TimeUnit.SECONDS);
			if (!workersFinishedNormally)
			{
				System.err.println("Printing thread dump. Threads not stopped " + endOfWorkLatch.getCount());
				ThreadDump.dumpThreads(System.err);
				DeadlockDetector.printDeadlocks(System.err);
				workerThreads.stopAll();
			}
		}
		catch (final InterruptedException ex)
		{
			throw new RuntimeException(ex);
		}

		//verification
		LOGGER.info("Evictions " + controller.getRegions().iterator().next().getCacheRegionStatistics().getEvictions());

		Assert.assertTrue(controller.getRegions().iterator().next().getCacheRegionStatistics().getEvictions() > 0l);
		Assert.assertTrue("Not all workers finished within time. This might indicate a deadlock! See threadDump(0)",
				workersFinishedNormally);
		Assert.assertEquals(0, errors.intValue());
	}

	@Test
	public void testCacheEvictionDeadlock3()
	{
		controller.clearCache(controller.getRegions().iterator().next());

		final CacheValueLoader<Integer> loader = new CacheValueLoader<Integer>()
		{
			@Override
			public Integer load(final CacheKey key)
			{
				return Integer.valueOf(1);
			}
		};

		for (int i = 0; i < 100; ++i)
		{
			controller.getWithLoader(new EhCacheTestKey(i, i), loader);
		}

		for (int y = 0; y < 1000; ++y)
		{
			for (int i = 5; i < 100; ++i)
			{
				controller.getWithLoader(new EhCacheTestKey(i, i), loader);
			}
		}

		Assert.assertTrue(controller.getRegions().iterator().next().getCacheRegionStatistics().getEvictions() == 0l);

		final SleepCacheLifecycleCallback callback = new SleepCacheLifecycleCallback();

		controller.addLifecycleCallback(callback);

		try
		{
			final AtomicInteger cnt = new AtomicInteger();

			final CyclicBarrier barrier = new CyclicBarrier(2);

			new RegistrableThread(new Runnable()
			{
				@Override
				public void run()
				{

					LOGGER.info(" Getting key 102 | 102");
					try
					{
						barrier.await();
					}
					catch (final Exception e)
					{
						//
					}
					controller.getWithLoader(new EhCacheTestKey(101, 102), loader);
					cnt.incrementAndGet();
				}
			}).start();

			new RegistrableThread(new Runnable()
			{
				@Override
				public void run()
				{

					LOGGER.info(" Getting key 5 | 4");
					try
					{
						barrier.await();
						Thread.sleep(1);
					}
					catch (final Exception e)
					{
						//
					}
					for (int i = 0; i < 100; ++i)
					{
						Thread.yield();
					}
					controller.getWithLoader(new EhCacheTestKey(4, 4), loader);
					cnt.incrementAndGet();
				}
			}).start();


			try
			{
				Thread.sleep(1200);
			}
			catch (final InterruptedException e)
			{
				//
			}

			if (cnt.intValue() < 2)
			{
				LOGGER.warn("Threads not stopped " + (2 - cnt.intValue()));
				ThreadDump.dumpThreads(System.err);
				DeadlockDetector.printDeadlocks(System.err);
			}

			Assert.assertEquals("Two threads did not finished within given time. Probable deadlock.", 2, cnt.intValue());
			Assert.assertTrue(controller.getRegions().iterator().next().getCacheRegionStatistics().getEvictions() >= 2);
		}
		finally
		{
			controller.removeLifecycleCallback(callback);
		}
	}

	private static class SleepCacheLifecycleCallback implements CacheLifecycleCallback
	{

		@Override
		public void onAfterRemove(final CacheKey key, final Object value, final CacheRegion region)
		{
			try
			{
				LOGGER.info("onAfterRemove " + ((EhCacheTestKey) key).hash + " | " + ((EhCacheTestKey) key).val);
				Thread.sleep(100);
				LOGGER.info("onAfterRemove[END] " + ((EhCacheTestKey) key).hash + " | " + ((EhCacheTestKey) key).val);
			}
			catch (final InterruptedException e)
			{
				// 
			}
		}

		@Override
		public void onAfterEviction(final CacheKey key, final Object value, final CacheRegion region)
		{
			try
			{
				LOGGER.info("onAfterEviction " + ((EhCacheTestKey) key).hash + " | " + ((EhCacheTestKey) key).val);
				Thread.sleep(100);
				LOGGER.info("onAfterEviction[END] " + ((EhCacheTestKey) key).hash + " | " + ((EhCacheTestKey) key).val);
			}
			catch (final InterruptedException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onAfterAdd(final CacheKey key, final Object value, final CacheRegion region)
		{
			try
			{
				LOGGER.info("onAfterAdd " + ((EhCacheTestKey) key).hash + " | " + ((EhCacheTestKey) key).val);
				Thread.sleep(100);
				LOGGER.info("onAfterAdd[END] " + ((EhCacheTestKey) key).hash + " | " + ((EhCacheTestKey) key).val);
			}
			catch (final InterruptedException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onMissLoad(final CacheKey key, final Object value, final CacheRegion lruCacheRegion)
		{
			// NOOP
		}
	}

	private static class EhCacheTestKey implements CacheKey
	{

		final int val;
		final int hash;

		public EhCacheTestKey(final int hash, final int val)
		{
			this.hash = hash + 1;
			this.val = val;
		}

		@Override
		public CacheUnitValueType getCacheValueType()
		{
			return CacheUnitValueType.NON_SERIALIZABLE;
		}

		@Override
		public Object getTypeCode()
		{
			return "A";
		}

		@Override
		public int hashCode()
		{
			return hash;
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (obj instanceof EhCacheTestKey)
			{
				return val == ((EhCacheTestKey) obj).val;
			}
			return false;
		}

		@Override
		public String getTenantId()
		{
			return "master";
		}

	}


	private static class CacheAddingWorker implements Runnable
	{
		private final CacheController cacheController;
		private final GenerationalCounterService counterService;
		private final String[] types;
		private final CountDownLatch endOfWorkLatch;
		private final int workerNumber;
		private final AtomicInteger errors;

		public CacheAddingWorker(final GenerationalCounterService counterService, final CacheController cacheController,
				final String[] types, final int workerNumber, final CountDownLatch endOfWorkLatch, final AtomicInteger errors)
		{
			super();
			this.cacheController = cacheController;
			this.types = types;
			this.endOfWorkLatch = endOfWorkLatch;
			this.workerNumber = workerNumber;
			this.errors = errors;
			this.counterService = counterService;
		}

		@Override
		public void run()
		{
			try
			{
				final RegistrableCacheKey key = new TestRegistrableCacheKey("testKey" + workerNumber, this.types);

				final Integer loaderValue = Integer.valueOf(workerNumber);
				final CacheValueLoader<Integer> loader = new CacheValueLoader<Integer>()
				{
					@Override
					public Integer load(final CacheKey key)
					{
						return loaderValue;
					}
				};

				cacheController.getWithLoader(key, loader);
			}
			catch (final Exception e)
			{
				errors.incrementAndGet();
				LOGGER.error("Error in worker " + workerNumber, e);
			}
			finally
			{
				endOfWorkLatch.countDown();
			}
		}
	}

}
