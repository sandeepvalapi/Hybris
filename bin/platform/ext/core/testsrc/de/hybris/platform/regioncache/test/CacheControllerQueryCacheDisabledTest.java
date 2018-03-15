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
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.DefaultCacheController;
import de.hybris.platform.regioncache.generation.GenerationalCounterService;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import de.hybris.platform.regioncache.key.legacy.LegacyCacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;

import java.util.Collection;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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
{ "/test/CacheControllerQueryCacheDisabledTest-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CacheControllerQueryCacheDisabledTest
{
	@Resource
	private CacheController controller;

	@Resource
	private GenerationalCounterService<String> counterService;

	private final TestCacheKeyFactory cacheKeyFactory = new TestCacheKeyFactory();


	@Resource(name = "EHCacheRegion")
	private CacheRegion region;

	@Before
	public void init()
	{
		controller.clearCache(region);
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
	public void getTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		final CacheKey key1 = createEntityKey();
		final CacheKey fsKey1 = createFSKey();

		final TestCacheValueLoader loader = new TestCacheValueLoader();
		controller.getWithLoader(key1, loader);
		controller.getWithLoader(fsKey1, loader);


		controller.invalidate(key1);

		Assert.assertArrayEquals(new long[]
		{ 1 }, counterService.getGenerations(new String[]
		{ "Type1" }, "master"));

		controller.getWithLoader(createEntityKey(), loader);
		controller.getWithLoader(createFSKey(), loader);


		final CacheStatistics stats = region.getCacheRegionStatistics();
		Assert.assertNotNull(stats);
		Assert.assertEquals(1, stats.getHits());
		Assert.assertEquals(3, stats.getMisses());
		Assert.assertEquals(0, stats.getEvictions());
		Assert.assertEquals(1, stats.getInvalidations());

		Assert.assertEquals(3, loader.getLoads());
	}

	/**
	 * 
	 */
	private RegistrableCacheKey createFSKey()
	{
		return cacheKeyFactory.create("key3", new String[]
		{ "Type1", "Type2" });
	}

	/**
	 * 
	 */
	private CacheKey createEntityKey()
	{

		final CacheKey key1 = new LegacyCacheKey("Type1", new Object[]
		{ "key1" }, "master");
		return key1;
	}
}
