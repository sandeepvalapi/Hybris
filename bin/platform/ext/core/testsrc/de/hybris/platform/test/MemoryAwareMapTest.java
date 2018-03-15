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
package de.hybris.platform.test;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.impl.DefaultCache;
import de.hybris.platform.core.Registry;

import java.text.NumberFormat;
import java.util.Map;

import org.apache.log4j.Logger;


@IntegrationTest
public class MemoryAwareMapTest extends AbstractMapTest
{
	static final Logger log = Logger.getLogger(MemoryAwareMapTest.class.getName());

	static NumberFormat format;

	static
	{
		format = NumberFormat.getInstance();
		format.setGroupingUsed(true);
	}

	protected static void printMem()
	{
		final Logger log = Logger.getLogger(MemoryAwareMapTest.class.getName());

		final long total = Runtime.getRuntime().totalMemory();
		final long free = Runtime.getRuntime().freeMemory();
		/* conv-log */log.debug("mem: " + format.format(total - free) + " = " + format.format(total) + " - " + format.format(free));
	}

	@Override
	protected Map createMapInstance()
	{
		final Cache cache = Registry.getCurrentTenant().getCache();
		if (cache instanceof DefaultCache)
		{
			return ((DefaultCache) cache).internalCreateMapInstance(1000);
		}
		return DefaultCache.internalCreateMapInstanceStatic(Registry.getCurrentTenant(), 1000);
	}

	/*
	 * public void testMemory() { long startTime = System.currentTimeMillis();
	 * ((MemoryAwareMap)map).setGCIntervalMinimum(10000); final int SIZE = 101000; int maxSize = 0; for (int i=0; i <
	 * 1100010001000; i+=SIZE) { map.put( new DummyAbstractCacheUnit(i), new byte[SIZE] ); maxSize = Math.max( maxSize,
	 * map.size() ); } log.debug("testMemory took "+(System.currentTimeMillis()-startTime)+" ms");
	 * log.debug("maxSize = "+maxSize); }
	 */

	@SuppressWarnings("unused")
	private static class DummyAbstractCacheUnit extends AbstractCacheUnit
	{
		int i;

		DummyAbstractCacheUnit(final int i)
		{
			super(Registry.getCurrentTenant().getCache());
			this.i = i;
		}

		@Override
		public Object compute() throws Exception
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Object[] createKey()
		{
			return new Object[]
			{ "dummy", Integer.valueOf(i) };
		}

	}

}
