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

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.impl.EHCacheRegion;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;


/**
 * Unit tests for {@link EHCacheRegion}
 */
@UnitTest
public class EHCacheRegionTTLTest
{
	private final long TTL_SECONDS = 5l;

	/**
	 * Tests "Time to live" functionality of EHCacheRegion. This test adds one key/value to the cache and then measures
	 * the "time to live" for this key. It enforces eviction by periodic "ping" to the EHCache, because EHCache does ttl
	 * evictions synchronously with user requests (this means there must be some user request to perform "ttl eviction").
	 */
	@Test
	public void testTTL() throws InterruptedException
	{
		final EHCacheRegion ttlEHCacheRegion = new EHCacheRegion("cacheReagion", 1000, "LRU", false, true,
				Long.valueOf(TTL_SECONDS));
		ttlEHCacheRegion.init();

		final AtomicLong addTime = new AtomicLong(-1);
		final AtomicLong evictedTime = new AtomicLong(-1);


		final CacheLifecycleCallback callback = new CacheLifecycleCallback()
		{

			@Override
			public void onAfterAdd(final CacheKey key, final Object value, final CacheRegion region)
			{
				addTime.set(System.currentTimeMillis());
			}

			@Override
			public void onAfterRemove(final CacheKey key, final Object value, final CacheRegion region)
			{
				//not used
			}

			@Override
			public void onAfterEviction(final CacheKey key, final Object value, final CacheRegion region)
			{
				evictedTime.set(System.currentTimeMillis());
			}

			@Override
			public void onMissLoad(final CacheKey key, final Object value, final CacheRegion lruCacheRegion)
			{
				// not used
			}
		};

		ttlEHCacheRegion.registerLifecycleCallback(callback);

		final TestCacheKey testKey = new TestCacheKey("aaa");

		ttlEHCacheRegion.getWithLoader(testKey, new CacheValueLoader()
		{
			@Override
			public Object load(final CacheKey key) throws CacheValueLoadException
			{
				return ((TestCacheKey) key).getKeyName();
			}
		});

		//Since bamboo doesn't guarantee fast servers we MUST expect delays up to 30 seconds; so we wait at least for 60 seconds
		final long waitUntilMS = System.currentTimeMillis() + (Math.max(TTL_SECONDS + 1, 60) * 1000);

		do
		{
			ttlEHCacheRegion.get(testKey);
			Thread.sleep(1000);
		}
		while (evictedTime.get() == -1 && System.currentTimeMillis() < waitUntilMS);

		assertTrue("not added", addTime.get() > 0);
		assertTrue("not evicted", evictedTime.get() > 0);

		final long deltaMS = evictedTime.get() - addTime.get();
		assertTrue("evicted before added", deltaMS > 0);

		final long deltaSeconds = deltaMS / 1000;
		assertTrue("TTL time to short", deltaSeconds >= TTL_SECONDS);
	}
}
