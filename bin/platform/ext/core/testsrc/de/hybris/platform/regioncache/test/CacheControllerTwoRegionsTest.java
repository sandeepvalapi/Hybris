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
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheTestHelper;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.DefaultCacheController;
import de.hybris.platform.regioncache.key.CacheKey;
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


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/CacheControllerTwoRegionsTest-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CacheControllerTwoRegionsTest
{
	@Resource
	private DefaultCacheController controller;

	@Resource(name = "DefaultCacheRegion")
	private CacheRegion region1;

	@Resource(name = "EHCacheRegion")
	private CacheRegion region2;

	@Before
	public void init()
	{

		controller.clearCache(region1);
		controller.clearCache(region2);

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
		System.gc();
	}


	@AfterClass
	public static void cleanEHCache()
	{
		CacheManager.getInstance().clearAll();

	}

	@Test
	public void simpleGetTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		final CacheKey key1 = new TestCacheKey("key1", CacheUnitValueType.NON_SERIALIZABLE, "Type1_Region1");
		final CacheKey key2 = new TestCacheKey("key2", CacheUnitValueType.NON_SERIALIZABLE, "Type2_Region1");
		final CacheKey key3 = new TestCacheKey("key3", CacheUnitValueType.NON_SERIALIZABLE, "Type1_Region2");
		final CacheKey key4 = new TestCacheKey("key4", CacheUnitValueType.NON_SERIALIZABLE, "Type2_Region2");

		CacheRegion reg = CacheTestHelper.resolveCacheRegion(controller, key1);
		Assert.assertEquals(region1.getName(), reg.getName());
		reg = CacheTestHelper.resolveCacheRegion(controller, key2);
		Assert.assertEquals(region1.getName(), reg.getName());
		reg = CacheTestHelper.resolveCacheRegion(controller, key3);
		Assert.assertEquals(region2.getName(), reg.getName());
		reg = CacheTestHelper.resolveCacheRegion(controller, key4);
		Assert.assertEquals(region2.getName(), reg.getName());

		controller.getWithLoader(key1, new TestCacheValueLoader()); // , region1.getName());
		controller.getWithLoader(key2, new TestCacheValueLoader()); // , region1.getName());
		controller.getWithLoader(key3, new TestCacheValueLoader()); // , region2.getName());
		controller.getWithLoader(key4, new TestCacheValueLoader()); // , region2.getName());
		// check hit stats
		controller.getWithLoader(key2, new TestCacheValueLoader()); // , region1.getName());

		CacheStatistics stats = region1.getCacheRegionStatistics();
		Assert.assertNotNull(stats);
		Assert.assertEquals(2, stats.getMisses());
		Assert.assertEquals(1, stats.getHits());
		Assert.assertEquals(0, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());

		stats = region2.getCacheRegionStatistics();
		Assert.assertNotNull(stats);
		Assert.assertEquals(2, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(0, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());
	}

	@Test
	public void evictionTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{

		int length = (int) region1.getCacheMaxEntries() + 10;
		for (int i = 0; i < length; ++i)
		{
			controller.getWithLoader(new TestCacheKey("r1:" + i, CacheUnitValueType.NON_SERIALIZABLE, "Type1_Region1"),
					new TestCacheValueLoader()); // , region1.getName());
		}

		CacheStatistics stats = region1.getCacheRegionStatistics();
		Assert.assertNotNull(stats);
		Assert.assertEquals(length, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(10, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());

		length = (int) region2.getCacheMaxEntries() + 7;
		for (int i = 0; i < length; ++i)
		{
			controller.getWithLoader(new TestCacheKey("r2:" + i, CacheUnitValueType.NON_SERIALIZABLE, "Type1_Region2"),
					new TestCacheValueLoader()); // , region2.getName());
		}

		stats = region2.getCacheRegionStatistics();
		Assert.assertNotNull(stats);
		Assert.assertEquals(length, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(7, stats.getEvictions());
		Assert.assertEquals(0, stats.getInvalidations());

	}


	@Test
	public void invalidationTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		final CacheKey key1 = new TestCacheKey("key1", CacheUnitValueType.NON_SERIALIZABLE, "Type1_Region1");
		final CacheKey key2 = new TestCacheKey("key2", CacheUnitValueType.NON_SERIALIZABLE, "Type2_Region1");
		final CacheKey key3 = new TestCacheKey("key3", CacheUnitValueType.NON_SERIALIZABLE, "Type1_Region2");
		final CacheKey key4 = new TestCacheKey("key4", CacheUnitValueType.NON_SERIALIZABLE, "Type2_Region2");

		controller.getWithLoader(key1, new TestCacheValueLoader()); // , region1.getName());
		controller.getWithLoader(key2, new TestCacheValueLoader()); // , region1.getName());
		controller.getWithLoader(key3, new TestCacheValueLoader()); // , region2.getName());
		controller.getWithLoader(key4, new TestCacheValueLoader()); // , region2.getName());

		controller.invalidate(key1); // , region1.getName());
		controller.invalidate(key4); // , region2.getName());

		CacheStatistics stats = region1.getCacheRegionStatistics();
		Assert.assertNotNull(stats);
		Assert.assertEquals(2, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(0, stats.getEvictions());
		Assert.assertEquals(1, stats.getInvalidations());

		stats = region2.getCacheRegionStatistics();
		Assert.assertNotNull(stats);
		Assert.assertEquals(2, stats.getMisses());
		Assert.assertEquals(0, stats.getHits());
		Assert.assertEquals(0, stats.getEvictions());
		Assert.assertEquals(1, stats.getInvalidations());
	}
}
