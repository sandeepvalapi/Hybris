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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import de.hybris.platform.regioncache.region.impl.DefaultCacheMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Test;
import org.springframework.util.ReflectionUtils;


@UnitTest
public class DefaultCacheMapTest
{
	@Test
	public void testLimit()
	{
		final int MAX = 10;
		final DefaultCacheMap map = new DefaultCacheMap(MAX);

		int key = 0;

		map.put(new MyCacheKey(key++), "foo");
		map.put(new MyCacheKey(key++), "foo");

		assertEquals(2, map.size());

		for (int i = 0; i < MAX - 2; i++)
		{
			map.put(new MyCacheKey(key++), "foo");
		}

		assertEquals(MAX, map.size());
		assertEquals(getMapCapacity(MAX), getInternalThreshold(map));

		map.put(new MyCacheKey(key++), "foo");
		map.put(new MyCacheKey(key++), "foo");
		map.put(new MyCacheKey(key++), "foo");

		assertEquals(MAX, map.size());
		assertEquals(getMapCapacity(MAX), getInternalThreshold(map));

		map.clear();

		assertEquals(0, map.size());

		for (int i = 0; i < 10 * MAX; i++)
		{
			map.put(new MyCacheKey(key++), "foo");
		}

		assertEquals(MAX, map.size());
		assertEquals(getMapCapacity(MAX), getInternalThreshold(map));
	}

	private int getMapCapacity(final int max)
	{
		// Find a power of 2 >= initialCapacity
		int capacity = 1;
		while (capacity < max)
		{
			capacity <<= 1;
		}

		return capacity;
	}

	private int getInternalThreshold(final DefaultCacheMap map)
	{
		Field f = null;
		try
		{
			f = DefaultCacheMap.class.getDeclaredField("map");
			f.setAccessible(true);
			final LinkedHashMap internalMap = (LinkedHashMap) ReflectionUtils.getField(f, map);
			f = HashMap.class.getDeclaredField("threshold");
			f.setAccessible(true);
			final Integer threshold = (Integer) ReflectionUtils.getField(f, internalMap);
			return threshold.intValue();

		}
		catch (final SecurityException e)
		{
			fail(e.toString());
		}
		catch (final NoSuchFieldException e)
		{
			fail(e.toString());
		}
		return -1;
	}

	static class MyCacheKey implements CacheKey
	{
		final Integer nr;

		MyCacheKey(final int i)
		{
			this.nr = Integer.valueOf(i);
		}

		@Override
		public int hashCode()
		{
			return nr.hashCode();
		}

		@Override
		public boolean equals(final Object obj)
		{
			return super.equals(obj) || (obj != null && ((MyCacheKey) obj).nr.equals(nr));
		}

		@Override
		public CacheUnitValueType getCacheValueType()
		{
			return CacheUnitValueType.SERIALIZABLE;
		}

		@Override
		public Object getTypeCode()
		{
			return Cache.CACHEKEY_HJMP;
		}

		@Override
		public String getTenantId()
		{
			return "dummy";
		}
	}
}
