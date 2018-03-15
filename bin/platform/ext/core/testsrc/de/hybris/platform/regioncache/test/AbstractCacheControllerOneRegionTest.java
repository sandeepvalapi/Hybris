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
import de.hybris.platform.cache.impl.DefaultCache;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheTestHelper;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.DefaultCacheController;
import de.hybris.platform.regioncache.invalidation.InvalidationFilter;
import de.hybris.platform.regioncache.key.AbstractCacheKey;
import de.hybris.platform.regioncache.key.AbstractRegistrableCacheKey;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import de.hybris.platform.regioncache.key.legacy.LegacyCacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;
import de.hybris.platform.regioncache.test.helper.DeadlockDetector;
import de.hybris.platform.regioncache.test.helper.ThreadDump;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;


/**
 * Needs to be instantiated to provide spring configuration.
 */
@UnitTest
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@Ignore
public abstract class AbstractCacheControllerOneRegionTest
{
	@Resource
	protected DefaultCacheController controller;

	@Resource(name = "CacheRegion")
	protected CacheRegion region;


	protected final TestCacheKeyFactory cacheKeyFactory = new TestCacheKeyFactory();

	protected static final Logger LOGGER = Logger.getLogger(AbstractCacheControllerOneRegionTest.class);

	@BeforeClass
	public static void initialize()
	{
		System.setProperty("net.sf.ehcache.skipUpdateCheck", "true");
	}

	@Before
	public void init()
	{
		controller.clearCache(region);
		controller.getFilters().clear();
	}

	@After
	public void clean()
	{

		final Collection<CacheRegion> regions = controller.getRegions();
		for (final CacheRegion region : regions)
		{
			controller.clearCache(region);
		}
		controller.getFilters().clear();
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
	public void valuePutTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{

		controller.clearCache(region);
		Assert.assertEquals(0, region.getMaxReachedSize());
		final CacheValueLoader loader = new TestCacheValueLoader();
		for (int i = 0; i < 1000; ++i)
		{
			final CacheKey key = new TestCacheKey("key" + i);
			controller.getWithLoader(key, loader); // , region.getName());
			Assert.assertEquals(i + 1, region.getMaxReachedSize());
		}

		// assuming cache size is 1000
		for (int i = 2000; i < 2010; ++i)
		{
			final CacheKey key = new TestCacheKey("key" + i);
			controller.getWithLoader(key, loader); // , region.getName());
			Assert.assertEquals(1000, region.getMaxReachedSize());
		}

		final CacheStatistics stats = region.getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		Assert.assertEquals(10, stats.getEvictions());
	}


	/**
	 * Tests if: cache stores loaded values
	 */
	@Test
	public void concurrentValuePutTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		final TestThreadsHolder<Runnable> randomAccessHolder = new TestThreadsHolder<Runnable>(128, new RunnerCreator<Runnable>()
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							final CacheValueLoader loader = new TestCacheValueLoader();
							for (int i = 0; i < 990; ++i)
							{
								final CacheKey key = new TestCacheKey("key" + i);
								controller.getWithLoader(key, loader); // , region.getName());
							}
						}
						catch (final Exception e)
						{
							e.printStackTrace();
						}
					}
				};
			}
		});

		randomAccessHolder.startAll();
		Assert.assertTrue(randomAccessHolder.waitForAll(60, TimeUnit.SECONDS));

		final CacheStatistics stats = region.getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		Assert.assertEquals(990, stats.getMisses());
		Assert.assertEquals((128 - 1) * 990, stats.getHits());
		Assert.assertEquals(0, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());

		Assert.assertEquals(990, region.getMaxReachedSize());

	}

	/**
	 * Test if: default cache region is chosen
	 */
	@Test
	public void choosingRegionTest() throws CacheRegionNotSpecifiedException
	{
		Assert.assertNotNull(controller);

		CacheKey key = new AbstractCacheKey(CacheUnitValueType.SERIALIZABLE, "test", "master")
		{ /* */};
		CacheRegion resolved = CacheTestHelper.resolveCacheRegion(key, controller);
		Assert.assertTrue(resolved.equals(region));

		key = new LegacyCacheKey(new Object[]
		{ "A", "B", "C" }, "master");
		resolved = CacheTestHelper.resolveCacheRegion(key, controller);
		Assert.assertTrue(resolved.equals(region));

		key = new AbstractRegistrableCacheKey("tenant1", new String[] {})
		{

			@Override
			public String[] getDependentTypes()
			{
				return new String[] {};
			}
		};

		resolved = CacheTestHelper.resolveCacheRegion(key, controller);
		Assert.assertTrue(resolved.equals(region));

	}

	@Test
	public void statsTest()
	{

		// Simulate cache operations
		final int size = 2 * (int) controller.getRegions().iterator().next().getCacheMaxEntries();
		final TestCacheValueLoader loader = new TestCacheValueLoader();

		final TestThreadsHolder<Runnable> randomAccessHolder = new TestThreadsHolder<Runnable>(8, new RunnerCreator<Runnable>()
		{
			final Random random = new Random(size);

			@Override
			public Runnable newRunner(final int threadNumber)
			{
				if (threadNumber == 0)
				{
					return new Runnable()
					{
						@Override
						public void run()
						{
							Thread.currentThread().setName("statsTest-Thread-" + threadNumber);
							for (int i = 0; i < 200 * size; ++i)
							{
								final Integer k = Integer.valueOf(random.nextInt(size));
								// legacy method called like this in RegionCacheAdapter
								controller.remove(new TestCacheKey(k, CacheUnitValueType.NON_SERIALIZABLE,
										String.valueOf(k.intValue() / 10)));
							}
						}
					};
				}
				else
				{
					return new Runnable()
					{
						@Override
						public void run()
						{
							Thread.currentThread().setName("statsTest-Thread-" + threadNumber);
							for (int i = 0; i < 200 * size; ++i)
							{
								final Integer k = Integer.valueOf(random.nextInt(size));
								CacheKey key = null;
								if (2 == (k.intValue() % 5))
								{
									key = cacheKeyFactory.create(k, new String[]
									{ String.valueOf(k.intValue() / 10) });
								}
								else
								{
									key = new TestCacheKey(k, CacheUnitValueType.NON_SERIALIZABLE, String.valueOf(k.intValue() / 10));

								}
								controller.getWithLoader(key, loader);
							}
						}
					};
				}
			}
		});

		randomAccessHolder.startAll();
		final boolean stopped = randomAccessHolder.waitForAll(60, TimeUnit.SECONDS);

		if (!stopped)
		{
			LOGGER.error("Printing thread dump. Threads not stopped " + randomAccessHolder.getAlive());
			ThreadDump.dumpThreads(System.err);
			DeadlockDetector.printDeadlocks(System.err);
			randomAccessHolder.stopAll();
		}

		Assert.assertTrue(stopped);

		Assert.assertTrue(controller.getRegions().iterator().next().getCacheRegionStatistics().getEvictions() > 0);

		// checks statistics
		final Collection<CacheRegion> regions = controller.getRegions();
		for (final CacheRegion region : regions)
		{
			final CacheStatistics statistics = region.getCacheRegionStatistics();

			final Collection<Object> types = statistics.getTypes();

			for (final Object statType : types)
			{
				Assert.assertTrue(region.getName() + "." + statType,
						statistics.getInstanceCount(statType) <= region.getCacheMaxEntries());
				Assert.assertTrue(region.getName() + "." + statType,
						statistics.getInstanceCount(statType) <= region.getMaxReachedSize());

			}

			Assert.assertTrue(region.getName() + " " + statistics.getInstanceCount() + " " + region.getCacheMaxEntries(),
					statistics.getInstanceCount() <= region.getCacheMaxEntries());
			Assert.assertTrue(region.getName(), statistics.getInstanceCount() <= region.getMaxReachedSize());
		}
	}

	/**
	 * Tests if: cache evicts values [no registry]
	 */
	@Test
	public void evictionTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		Assert.assertNotNull(controller);

		controller.clearCache(controller.getRegions().iterator().next());

		final int length = (int) controller.getRegions().iterator().next().getCacheMaxEntries() + 10;
		final CacheValueLoader loader = new CacheValueLoader()
		{
			@Override
			public Object load(final CacheKey key) throws CacheValueLoadException
			{
				return key.getTypeCode() + ":" + key;
			}
		};

		for (int i = 0; i < length; ++i)
		{
			final TestCacheKey key = new TestCacheKey(String.valueOf(i), CacheUnitValueType.SERIALIZABLE);
			controller.getWithLoader(key, loader); // , region.getName());
		}

		final CacheStatistics stats = controller.getRegions().iterator().next().getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		Assert.assertEquals(length, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(10, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());

	}

	/**
	 * Tests if: invalidation with no key inside. No filters defined.
	 */
	@Test
	public void invalidationTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		Assert.assertNotNull(controller);
		final CacheKey key = new TestCacheKey("A");
		Assert.assertTrue(this.region.equals(region));
		controller.invalidate(key);

		final CacheStatistics stats = controller.getRegions().iterator().next().getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		Assert.assertEquals(0, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(0, stats.getEvictions());
		/*
		 * We are NOT counting invalidations.<br/> Real invalidation may occur on another cluster node.
		 */
		Assert.assertEquals(0, stats.getInvalidations());
	}

	/**
	 * Tests if: invalidation with no key inside. No filters defined.
	 */
	@Test
	public void invalidationTestWithFilter() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{

		controller.getFilters().add(new InvalidationFilter()
		{
			@Override
			public boolean allowInvalidation(final CacheKey key, final CacheRegion region)
			{
				return false;
			}
		});

		final CacheKey key = new TestCacheKey("A");
		controller.invalidate(key);

		final CacheStatistics stats = controller.getRegions().iterator().next().getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		Assert.assertEquals(0, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(0, stats.getEvictions());
		/*
		 * Filter should not allow invalidation.
		 */
		Assert.assertEquals(0, stats.getInvalidations());
	}

	/**
	 * Tests if: invalidation when key inside cache map
	 */
	@Test
	public void invalidateKeyTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		final AtomicInteger cnt = new AtomicInteger();

		controller.getFilters().add(new InvalidationFilter()
		{
			@Override
			public boolean allowInvalidation(final CacheKey key, final CacheRegion region)
			{
				cnt.incrementAndGet();
				return true;
			}

		});

		final CacheKey key = new TestCacheKey("A");
		controller.getWithLoader(key, new TestCacheValueLoader());
		controller.invalidate(key);

		Assert.assertEquals(1, cnt.intValue());

		final CacheStatistics stats = controller.getRegions().iterator().next().getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		Assert.assertEquals(1, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(0, stats.getEvictions());
		Assert.assertEquals(1, stats.getInvalidations());
	}


	@Test
	public void multitenantCacheRegistryTest()
	{

		final CacheKey t1regKey = cacheKeyFactory.create("regKey1", "tenant1", new String[]
		{ "typeA", "typeB" });
		final CacheKey t2regKey = cacheKeyFactory.create("regKey1", "tenant2", new String[]
		{ "typeA", "typeB" });

		final CacheKey t1serKey = new TestCacheKey("serializableKey1", "tenant1", CacheUnitValueType.SERIALIZABLE, "typeA");
		final CacheKey t2serKey = new TestCacheKey("serializableKey1", "tenant2", CacheUnitValueType.SERIALIZABLE, "typeA");

		final TestCacheValueLoader loader = new TestCacheValueLoader();

		controller.getWithLoader(t1serKey, loader);
		controller.getWithLoader(t2serKey, loader);
		Assert.assertEquals(2, loader.getLoads());

		controller.getWithLoader(t1regKey, loader);
		Assert.assertEquals(3, loader.getLoads());

		controller.getWithLoader(t2regKey, loader);
		Assert.assertEquals(4, loader.getLoads()); // same key but different tenant

		controller.invalidate(t1regKey);

		controller.getWithLoader(cacheKeyFactory.create("regKey1", "tenant1", new String[]
		{ "typeA", "typeB" }), loader); //cached here
		Assert.assertEquals(5, loader.getLoads());

		controller.getWithLoader(t2regKey, loader);
		Assert.assertEquals(5, loader.getLoads()); // no read since we invalidated tenant1 !!!	

	}

	@Test
	public void statsTypesTest()
	{
		controller.getWithLoader(new LegacyCacheKey(new Object[]
		{ DefaultCache.CACHEKEY_C2LMANAGER, }, "master"), new TestCacheValueLoader("a"));

		final Collection<CacheRegion> regions = controller.getRegions();
		for (final CacheRegion region : regions)
		{
			final CacheStatistics stats = region.getCacheRegionStatistics();
			final List<Object> types = new LinkedList<Object>(stats.getTypes());

			Collections.sort(types, new Comparator<Object>()
			{
				@Override
				public int compare(final Object o1, final Object o2)
				{
					return String.valueOf(o1).compareTo(String.valueOf(o2));
				}
			});

			for (final Object type : types)
			{
				Assert.assertTrue(stats.getMisses(type) > 0l);
			}

		}


	}
}
