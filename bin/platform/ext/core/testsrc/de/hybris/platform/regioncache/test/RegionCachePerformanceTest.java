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

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.DefaultCacheController;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;

import java.util.Collection;

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

import com.bethecoder.ascii_table.ASCIITable;
import com.google.common.base.Stopwatch;


/**
 * RegionCache performance tests
 */
@PerformanceTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/EHCacheRegionPerformanceTest-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class RegionCachePerformanceTest
{
	private Stopwatch stopWatch;

	@Resource
	private CacheController controller;

	@Resource(name = "EHCacheRegionBig")
	private CacheRegion region1;

	@Resource(name = "EHCacheRegionMedium")
	private CacheRegion region2;


	@Before
	public void init()
	{
		controller.clearCache(region1);
		((DefaultCacheController) controller).getFilters().clear();

		stopWatch = Stopwatch.createUnstarted();
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
	public void fillBigEmptyCacheTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{

		final int length = 100010;
		final long cacheEntriesBefore = region1.getMaxReachedSize();

		Assert.assertEquals(0, cacheEntriesBefore);

		final String fillingTime = fillCache(length, "Type1_RegionBig", 0);

		final CacheStatistics stats = region1.getCacheRegionStatistics();
		Assert.assertNotNull(stats);
		Assert.assertEquals(length, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(10, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());

		final String[][] data = new String[1][];

		final String[] result1 =
		{ Long.toString(region1.getCacheMaxEntries()), Long.toString(cacheEntriesBefore),
				Long.toString(region1.getMaxReachedSize()), Integer.toString(length), fillingTime, Long.toString(stats.getHits()),
				Long.toString(stats.getMisses()), Long.toString(stats.getEvictions()) };
		data[0] = result1;

		writeResultTable(data);

	}

	@Test
	public void fillMediumEmptyCacheTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{

		final int length = 100010;
		final long cacheEntriesBefore = region2.getMaxReachedSize();

		Assert.assertEquals(0, cacheEntriesBefore);

		final String fillingTime = fillCache(length, "Type1_RegionMedium", 0);

		final CacheStatistics stats = region2.getCacheRegionStatistics();
		Assert.assertNotNull(stats);
		Assert.assertEquals(length, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(90010, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());

		final String[][] data = new String[1][];

		final String[] result2 =
		{ Long.toString(region2.getCacheMaxEntries()), Long.toString(cacheEntriesBefore),
				Long.toString(region2.getMaxReachedSize()), Integer.toString(length), fillingTime, Long.toString(stats.getHits()),
				Long.toString(stats.getMisses()), Long.toString(stats.getEvictions()) };
		data[0] = result2;

		writeResultTable(data);

	}

	@Test
	public void hitBigNotEmptyCacheTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{

		int length = (int) region1.getCacheMaxEntries() - 1000;
		fillCache(length, "Type1_RegionBig", 0);

		long cacheEntriesBefore = region1.getMaxReachedSize();
		Assert.assertEquals(length, cacheEntriesBefore);

		length = 100010;
		//now fill the already filled cache with more entries		
		String fillingTime = fillCache(length, "Type1_RegionBig", 0);
		CacheStatistics stats = region1.getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		final String[][] data = new String[2][];

		final String[] result1 =
		{ Long.toString(region1.getCacheMaxEntries()), Long.toString(cacheEntriesBefore),
				Long.toString(region1.getMaxReachedSize()), Integer.toString(length), fillingTime, Long.toString(stats.getHits()),
				Long.toString(stats.getMisses()), Long.toString(stats.getEvictions()) };
		data[0] = result1;

		//repeat it now with full cache
		stats.clear();
		cacheEntriesBefore = region1.getMaxReachedSize();
		fillingTime = fillCache(length, "Type1_RegionBig", 0);
		stats = region1.getCacheRegionStatistics();
		Assert.assertNotNull(stats);

		final String[] result2 =
		{ Long.toString(region1.getCacheMaxEntries()), Long.toString(cacheEntriesBefore),
				Long.toString(region1.getMaxReachedSize()), Integer.toString(length), fillingTime, Long.toString(stats.getHits()),
				Long.toString(stats.getMisses()), Long.toString(stats.getEvictions()) };
		data[1] = result2;
		writeResultTable(data);

	}

	@Test
	public void allGetsAreHitsTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		final CacheStatistics stats = region1.getCacheRegionStatistics();
		//fill the cache 			
		final int length = (int) region1.getCacheMaxEntries();
		fillCache(length, "Type1_RegionBig", 0);

		final long cacheEntriesBefore = region1.getMaxReachedSize();
		Assert.assertEquals(length, cacheEntriesBefore);
		stats.clear();

		//now hit the already filled cache with the same items  - all gets should be hits		
		final String fillingTime = fillCache(length, "Type1_RegionBig", 0);
		Assert.assertNotNull(stats);

		final String[][] data = new String[1][];

		final String[] result1 =
		{ Long.toString(region1.getCacheMaxEntries()), Long.toString(cacheEntriesBefore),
				Long.toString(region1.getMaxReachedSize()), Integer.toString(length), fillingTime, Long.toString(stats.getHits()),
				Long.toString(stats.getMisses()), Long.toString(stats.getEvictions()) };
		data[0] = result1;

		//		data[1] = result2;
		writeResultTable(data);

	}

	@Test
	public void noGetIsHitTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{

		final CacheStatistics stats = region1.getCacheRegionStatistics();
		//fill the cache 			
		final int length = (int) region1.getCacheMaxEntries();
		fillCache(length, "Type1_RegionBig", 0);

		final long cacheEntriesBefore = region1.getMaxReachedSize();
		Assert.assertEquals(length, cacheEntriesBefore);
		stats.clear();

		//now try to hit the already filled cache but with different items  - no get should be a hit		
		final String fillingTime = fillCache(length, "Type1_RegionBig", 150000);
		Assert.assertNotNull(stats);

		final String[][] data = new String[1][];

		final String[] result1 =
		{ Long.toString(region1.getCacheMaxEntries()), Long.toString(cacheEntriesBefore),
				Long.toString(region1.getMaxReachedSize()), Integer.toString(length), fillingTime, Long.toString(stats.getHits()),
				Long.toString(stats.getMisses()), Long.toString(stats.getEvictions()) };
		data[0] = result1;

		//		data[1] = result2;
		writeResultTable(data);

	}

	private String fillCache(final int size, final String type, final int startNumber)
	{
		stopWatch.start();
		for (int i = startNumber; i < size + startNumber; ++i)
		{
			controller.getWithLoader(new TestCacheKey("testRegion:" + i, CacheUnitValueType.NON_SERIALIZABLE, type),
					new TestCacheValueLoader()); // , region1.getName());
		}
		stopWatch.stop();
		final String result = stopWatch.toString();
		stopWatch.reset();

		return result;
	}

	private void writeResultTable(final String[][] data)
	{
		final String[] header =
		{ "cache max size(declared)", "cached items before", "cached items after", "Items to cache", "filling time", "hits",
				"misses", "evictions" };
		ASCIITable.getInstance().printTable(header, data);
	}

}
