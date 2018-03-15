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
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.DefaultCacheController;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;
import net.sf.ehcache.CacheManager;

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
{ "/test/CacheControllerLifecycleCallbackTest-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CacheControllerLifecycleCallbackTest
{
	@Resource
	private CacheController controller;

	@Resource(name = "DefaultCacheRegion")
	private CacheRegion defaultRegion1;

	@Resource(name = "EHCacheRegion")
	private CacheRegion ehcacheRegion2;

	private static final String DEFAULT_REGION_TYPE = "Type1";
	private static final String EHCACHE_REGION_TYPE = "Type2";

	@Before
	public void init()
	{
		controller.clearCache(defaultRegion1);
		controller.clearCache(ehcacheRegion2);

		((DefaultCacheController) controller).getFilters().clear();
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

	@Test
	public void testOnAfterAddDualRegion() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		final CacheKey key1 = new TestCacheKey("key1", CacheUnitValueType.NON_SERIALIZABLE, DEFAULT_REGION_TYPE);
		final CacheKey key2 = new TestCacheKey("key2", CacheUnitValueType.NON_SERIALIZABLE, EHCACHE_REGION_TYPE);
		final CacheKey key3 = new TestCacheKey("key3", CacheUnitValueType.NON_SERIALIZABLE, DEFAULT_REGION_TYPE);
		final CacheKey key4 = new TestCacheKey("key4", CacheUnitValueType.NON_SERIALIZABLE, EHCACHE_REGION_TYPE);


		final List<Integer> results = new ArrayList<Integer>();

		final CacheLifecycleCallback callback = new CacheLifecycleCallback()
		{

			@Override
			public void onAfterRemove(final CacheKey key, final Object value, final CacheRegion region)
			{
				//not important here
			}

			@Override
			public void onAfterEviction(final CacheKey key, final Object value, final CacheRegion region)
			{
				//not important here
			}

			@Override
			public void onAfterAdd(final CacheKey key, final Object value, final CacheRegion region)
			{
				//Here we expect that the "value" argument is exactly the same as the thing read from cache (we are running single-threaded, in multi-threaded environment this is not guaranteed).
				//To prove this, we are collecting both values for later comparison.
				results.add((Integer) value);
				results.add((Integer) controller.get(key));
			}

			@Override
			public void onMissLoad(final CacheKey key, final Object value, final CacheRegion lruCacheRegion)
			{
				//not important here
			}
		};
		//callback.onAfterEvict(null, null);

		controller.addLifecycleCallback(callback);


		controller.getWithLoader(key1, new TestCacheValueLoader(Integer.valueOf(0)));
		controller.getWithLoader(key2, new TestCacheValueLoader(Integer.valueOf(1)));
		controller.getWithLoader(key3, new TestCacheValueLoader(Integer.valueOf(2)));
		controller.getWithLoader(key4, new TestCacheValueLoader(Integer.valueOf(3)));

		Assert.assertEquals(8, results.size());


		//Quick verification that the "value" argument to the onAfterAdd method AND the current cache value for the key are the same.
		for (int i = 0; i < 4; i++)
		{
			Assert.assertEquals(Integer.valueOf(i), results.get(i * 2));
			Assert.assertEquals(Integer.valueOf(i), results.get(i * 2 + 1));
			Assert.assertTrue(results.get(i * 2) == results.get(i * 2 + 1));
		}
	}


	@Test
	public void testOnAfterRemoveDualRegion() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		final CacheKey key1 = new TestCacheKey("key1", CacheUnitValueType.NON_SERIALIZABLE, DEFAULT_REGION_TYPE);
		final CacheKey key2 = new TestCacheKey("key2", CacheUnitValueType.NON_SERIALIZABLE, EHCACHE_REGION_TYPE);
		final CacheKey key3 = new TestCacheKey("key3", CacheUnitValueType.NON_SERIALIZABLE, DEFAULT_REGION_TYPE);
		final CacheKey key4 = new TestCacheKey("key4", CacheUnitValueType.NON_SERIALIZABLE, EHCACHE_REGION_TYPE);


		final List<Object> results = new ArrayList<Object>();

		final CacheLifecycleCallback callback = new CacheLifecycleCallback()
		{

			@Override
			public void onAfterRemove(final CacheKey key, final Object value, final CacheRegion region)
			{
				//Here we expect that the "value" argument is DIFFERENT from the thing read from cache (we are running single-threaded, in multi-threaded environment this is not guaranteed).
				//Since we're in "onAfterRemove", we expect that the current cache content is null, but the argument should be the "thing that was removed".
				results.add(key);
				results.add(value);
				results.add(controller.get(key));
			}

			@Override
			public void onAfterEviction(final CacheKey key, final Object value, final CacheRegion region)
			{
				//not important here
			}

			@Override
			public void onAfterAdd(final CacheKey key, final Object value, final CacheRegion region)
			{
				//not important here
			}

			@Override
			public void onMissLoad(final CacheKey key, final Object value, final CacheRegion lruCacheRegion)
			{
				//not important here
			}
		};
		//callback.onAfterEvict(null, null);

		controller.addLifecycleCallback(callback);


		controller.getWithLoader(key1, new TestCacheValueLoader(Integer.valueOf(0)));
		controller.getWithLoader(key2, new TestCacheValueLoader(Integer.valueOf(1)));
		controller.getWithLoader(key3, new TestCacheValueLoader(Integer.valueOf(2)));
		controller.getWithLoader(key4, new TestCacheValueLoader(Integer.valueOf(3)));

		controller.invalidate(key1);
		Assert.assertEquals(3, results.size());
		Assert.assertTrue(key1 == results.get(0));
		Assert.assertEquals(Integer.valueOf(0), results.get(1));
		Assert.assertTrue(results.get(2) == null);
		results.clear();

		controller.invalidate(key2);
		Assert.assertEquals(3, results.size());
		Assert.assertTrue(key2 == results.get(0));
		Assert.assertEquals(Integer.valueOf(1), results.get(1));
		Assert.assertTrue(results.get(2) == null);
		results.clear();

		controller.invalidate(key3);
		Assert.assertEquals(3, results.size());
		Assert.assertTrue(key3 == results.get(0));
		Assert.assertEquals(Integer.valueOf(2), results.get(1));
		Assert.assertTrue(results.get(2) == null);
		results.clear();

		controller.invalidate(key4);
		Assert.assertEquals(3, results.size());
		Assert.assertTrue(key4 == results.get(0));
		Assert.assertEquals(Integer.valueOf(3), results.get(1));
		Assert.assertTrue(results.get(2) == null);
		results.clear();

	}

	@Test
	public void testOnAfterEvictDualRegion() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{


		final List<Object> results1 = new ArrayList<Object>();
		final List<Object> results2 = new ArrayList<Object>();

		final CacheLifecycleCallback callback = new CacheLifecycleCallback()
		{

			@Override
			public void onAfterRemove(final CacheKey key, final Object value, final CacheRegion region)
			{
				//not important here
			}

			@Override
			public void onAfterEviction(final CacheKey key, final Object value, final CacheRegion region)
			{
				List<Object> results;
				if (((Integer) value).intValue() > 0)
				{
					results = results1;
				}
				else
				{
					results = results2;
				}

				results.add(key);
				results.add(value);
			}

			@Override
			public void onAfterAdd(final CacheKey key, final Object value, final CacheRegion region)
			{
				//not important here
			}

			@Override
			public void onMissLoad(final CacheKey key, final Object value, final CacheRegion lruCacheRegion)
			{
				//not important here
			}
		};
		//callback.onAfterAdd(null, null);

		controller.addLifecycleCallback(callback);

		final CacheKey[] cacheKeyTable1 = new CacheKey[200];
		final CacheKey[] cacheKeyTable2 = new CacheKey[200];

		for (int i = 1; i <= 200; i++)
		{
			final CacheKey key1 = new TestCacheKey("key" + i, CacheUnitValueType.NON_SERIALIZABLE, DEFAULT_REGION_TYPE);
			cacheKeyTable1[i - 1] = key1;
			final CacheKey key2 = new TestCacheKey("key-" + i, CacheUnitValueType.NON_SERIALIZABLE, EHCACHE_REGION_TYPE);
			cacheKeyTable2[i - 1] = key2;
			controller.getWithLoader(key1, new TestCacheValueLoader(Integer.valueOf(i)));
			controller.getWithLoader(key2, new TestCacheValueLoader(Integer.valueOf(-i)));
		}

		Assert.assertTrue(200 == results1.size());
		Assert.assertTrue(200 == results2.size());

		//For DefaultCacheMap there's a simple eviction strategy: remove eldest (FIFO)
		for (int i = 0; i < 100; i++)
		{
			final Object expectedKey1 = cacheKeyTable1[i];
			final Object actualKey1 = results1.get(2 * i);
			final Integer evictedValue1 = (Integer) results1.get(2 * i + 1);
			Assert.assertTrue("Invalid evicted key. Should be: " + expectedKey1 + " but is: " + actualKey1,
					expectedKey1 == actualKey1);

			Assert.assertTrue("Invalid evicted value. Should be: " + i + 1 + " but is: " + evictedValue1.intValue(),
					evictedValue1.intValue() == i + 1);
		}

		//For ehcache, we just check that all evicted are negative...
		for (int i = 0; i < 100; i++)
		{
			final Object actualKey2 = results2.get(2 * i);
			final String keySuffix = String.valueOf(((TestCacheKey) actualKey2).getKeyName()).substring(3);
			final Integer evictedValue2 = (Integer) results2.get(2 * i + 1);

			Assert.assertEquals("Invalid evicted value. Should be: " + keySuffix + " but is: " + evictedValue2.toString(),
					keySuffix, evictedValue2.toString());
		}

	}
}
