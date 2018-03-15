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
import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.DefaultCacheController;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import de.hybris.platform.regioncache.region.CacheRegion;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/OneRegionTestEHCacheRegion-context.xml" }, inheritLocations = false)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class OneRegionTestEHCacheRegion extends AbstractCacheControllerOneRegionTest
{
	// class only provides spring configuration

	@Test
	public void resolveCacheRegionForAdd()
	{
		final int keys = 7000;

		final DefaultCacheController ctrl = controller;

		final CacheKey keyTbl[] = new CacheKey[keys];
		for (int i = 0; i < keys; ++i)
		{
			keyTbl[i] = new TestCacheKey(Integer.valueOf(i), CacheUnitValueType.SERIALIZABLE, "key");
		}

		final long start = System.currentTimeMillis();
		for (int i = 0; i < keys; ++i)
		{
			ctrl.resolveCacheRegionForAdd(keyTbl[i]);
		}
		final long end = System.currentTimeMillis();

		LOGGER.info(keys + " resolvings took [ms] " + (end - start));

	}

	@Ignore("Proves that EHCache region FIFO is not strict")
	@Test
	public void ehCacheFifoTest()
	{
		region.clearCache();

		final CacheLifecycleCallback callback = new CacheLifecycleCallback()
		{

			@Override
			public void onAfterRemove(final CacheKey key, final Object value, final CacheRegion region)
			{
				//
			}

			@Override
			public void onAfterEviction(final CacheKey key, final Object value, final CacheRegion region)
			{
				LOGGER.info("Key evicted " + key);
			}

			@Override
			public void onAfterAdd(final CacheKey key, final Object value, final CacheRegion region)
			{
				// 
			}

			@Override
			public void onMissLoad(final CacheKey key, final Object value, final CacheRegion lruCacheRegion)
			{
				// not used 
			}
		};
		region.registerLifecycleCallback(callback);

		final int size = (int) region.getCacheMaxEntries();
		final CacheValueLoader loader = new TestCacheValueLoader();

		final TestCacheKey key = new TestCacheKey(Integer.valueOf(0), CacheUnitValueType.SERIALIZABLE, Integer.toString(0 % 7));
		region.getWithLoader(key, loader);
		sleep(2);

		for (int i = 1; i <= size; ++i)
		{
			region.getWithLoader(new TestCacheKey(Integer.valueOf(i), CacheUnitValueType.SERIALIZABLE, Integer.toString(i % 7)),
					loader);
			sleep(2); // ehcache is timestamp based with milisecond precision
		}

		Assert.assertFalse("First element put is not evicted !", region.containsKey(key));
	}

	private void sleep(final int i)
	{
		try
		{
			Thread.sleep(i);
		}
		catch (final InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}
}
