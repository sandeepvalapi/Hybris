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
package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;
import de.hybris.platform.regioncache.test.TestRegistrableCacheKey;

import java.util.Collection;

import org.junit.Assert;


/**
 * Helper class for getting inside cache registry implementation.
 */
public final class CacheTestHelper
{

	/**
	 * Helper method. TODO: To be moved somewhere else.
	 */
	public static void registryEvictionTest(final DefaultCacheController controller, final CacheRegion region,
			final String[] typeCodes) throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		Assert.assertNotNull(controller);
		final int length = (int) region.getCacheMaxEntries() + 10;
		final CacheValueLoader loader = new CacheValueLoader()
		{
			@Override
			public Object load(final CacheKey key) throws CacheValueLoadException
			{
				return key.getTypeCode() + ":" + key;
			}
		};
		final CacheKey[] askedKeys = new CacheKey[length];
		for (int i = 0; i < length; ++i)
		{
			final CacheKey key = new TestRegistrableCacheKey(String.valueOf(i), new String[]
			{ typeCodes[i % typeCodes.length] });
			askedKeys[i] = key;
			final CacheRegion tmpRegion = resolveCacheRegion(key, false, controller);
			Assert.assertNotNull(tmpRegion);
			Assert.assertEquals(region.getName(), tmpRegion.getName());
			controller.getWithLoader(key, loader);
		}

		final CacheStatistics stats = region.getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		Assert.assertEquals(length, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(10, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());

	}

	/**
	 * 
	 */
	public static CacheRegion resolveCacheRegion(final CacheKey key, final boolean forInvalidation,
			final DefaultCacheController controller)
	{
		if (forInvalidation)
		{
			final Collection<CacheRegion> regions = controller.resolveRegionsForInvalidation(key);
			if (regions == null)
			{
				return null;
			}
			if (regions.isEmpty())
			{
				return null;
			}
			if (regions.size() > 1)
			{
				throw new RuntimeException("More than one region resolved");
			}
			return regions.iterator().next();
		}
		return controller.resolveCacheRegionForAdd(key);
	}

	/**
	 * 
	 */
	public static CacheRegion resolveCacheRegion(final CacheKey key, final DefaultCacheController controller)
	{
		return resolveCacheRegion(key, false, controller);
	}

	public static CacheRegion resolveCacheRegion(final DefaultCacheController controller, final CacheKey key)
	{
		return resolveCacheRegion(key, false, controller);
	}

}
