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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheTestHelper;
import de.hybris.platform.regioncache.DefaultCacheController;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/EHCacheRegionPerformanceTest-context.xml" }, inheritLocations = false)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MultiThreadedStaleDataTest
{
	@Resource
	protected DefaultCacheController controller;

	protected final static Logger LOGGER = Logger.getLogger(MultiThreadedStaleDataTest.class);

	private static final int NUMBER_OF_THREADS = 64;
	private static final long TEST_TIME_MS = 10000; // 30 seconds
	private final TestCacheKey key = new TestCacheKey("test");

	private final AtomicInteger errors = new AtomicInteger();

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

	@Test
	public void changesVisibleVolatile() throws InterruptedException, CacheRegionNotSpecifiedException
	{
		changesToCacheVisibleToOtherThread(new CacheValueFactory()
		{
			@Override
			public CacheValue createValue(final int val)
			{
				return new CacheValue(val)
				{
					private volatile boolean valid;

					@Override
					public boolean isValid()
					{
						return valid;
					}

					@Override
					public void setValid(final boolean valid)
					{
						this.valid = valid;
					}

				};
			}

		});
	}

	@Test
	public void changesVisibleNonVolatile() throws InterruptedException, CacheRegionNotSpecifiedException
	{
		changesToCacheVisibleToOtherThread(new CacheValueFactory()
		{
			@Override
			public CacheValue createValue(final int val)
			{
				return new CacheValue(val)
				{

					private boolean valid;

					@Override
					public boolean isValid()
					{
						return valid;
					}

					@Override
					public void setValid(final boolean valid)
					{
						this.valid = valid;
					}

				};
			}

		});
	}


	public void changesToCacheVisibleToOtherThread(final CacheValueFactory factory) throws InterruptedException,
			CacheRegionNotSpecifiedException
	{
		LOGGER.info("changesToCacheVisibleToOtherThread() starting for[ms] " + TEST_TIME_MS);

		errors.set(0);

		controller.clearCache(CacheTestHelper.resolveCacheRegion(key, controller));

		final RunnerCreator<Runnable> runnerCreator = new RunnerCreator()
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				if (threadNumber == 0)
				{
					// Writer
					return new Runnable()
					{
						@Override
						public void run()
						{
							int val = 0;
							try
							{
								while (!Thread.interrupted())
								{
									final CacheValue value = factory.createValue(++val);
									controller.getWithLoader(key, new TestCacheValueLoader(value));
									controller.invalidate(key);
									value.setValid(false);
								}
							}
							catch (final Throwable t)
							{
								LOGGER.error("Error in writer", t);
								errors.incrementAndGet();
							}
						}
					};
				}
				else
				{
					// Reader
					return new Runnable()
					{
						@Override
						public void run()
						{
							int val = 0;
							try
							{
								while (!Thread.interrupted())
								{
									final CacheValue value = (CacheValue) controller.get(key);
									if (value == null)
									{
										continue;
									}
									if (value.value <= val)
									{
										errors.incrementAndGet();
										LOGGER.warn("current: " + value.value + " expected <= " + val);
									}
									if (value.isValid())
									{
										val = value.value - 1;
									}
									else
									{
										val = value.value;
									}
								}
							}
							catch (final Throwable t)
							{
								LOGGER.error("Error in loader", t);
								errors.incrementAndGet();
							}
						}
					};
				}
			}
		};

		final TestThreadsHolder workerThreads = new TestThreadsHolder<Runnable>(NUMBER_OF_THREADS, runnerCreator);
		workerThreads.startAll();

		LOGGER.info("Waiting for threads " + NUMBER_OF_THREADS);
		Thread.sleep(TEST_TIME_MS);

		workerThreads.stopAll();
		if (!workerThreads.waitForAll(7, TimeUnit.SECONDS))
		{
			workerThreads.stopAndDestroy(10);
		}

		LOGGER.info("Test finished. Number of errors " + errors.intValue());
		Assert.assertFalse(errors.intValue() > 0);
	}

	public static interface CacheValueFactory
	{
		CacheValue createValue(int val);
	}

	public abstract static class CacheValue
	{
		private final int value;

		public CacheValue(final int value)
		{
			this.value = value;
			setValid(true);
		}

		public int getValue()
		{
			return value;
		}

		public abstract boolean isValid();

		public abstract void setValid(boolean valid);
	}
}
