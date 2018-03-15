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
package de.hybris.platform.cache.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.ConfigIntf;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Tests for CacheFactory
 */
@UnitTest
public class CacheFactoryTest
{

	@Mock
	private Tenant tenant;

	@Mock
	private Tenant slaveTenant;

	@Mock
	private ConfigIntf config;

	private CacheFactory cacheFactory;

	private static final String CACHE_MAIN_MAP = "cache.main.map=de.hybris.platform.cache.impl.BaseCacheYFastFIFOMap";

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		cacheFactory = new CacheFactory();

		//mock logic required for creating the old cache
		//TODO: try to continue with mocking till it's ready to test creating the old cache.
		//Then add tests with old cache and legacymode enabled
		when(Integer.valueOf(config.getInt(DefaultCache.CONFIG_CACHE_MAIN, Cache.DEFAULT_CACHE_MAIN))).thenReturn(
				Integer.valueOf(0));
		when(Boolean.valueOf(config.getBoolean(Config.Params.BYPASS_HYBRIS_RECOMMENDATIONS, false))).thenReturn(Boolean.FALSE);
		when(Boolean.valueOf(config.getBoolean(Cache.CONFIG_FORCE_EXCLUSIVE_CALCULATION, false))).thenReturn(Boolean.FALSE);
		when(config.getParameter("cache.main.map")).thenReturn(CACHE_MAIN_MAP);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cache.impl.CacheFactory#createCacheInstance(de.hybris.platform.core.Tenant)}.
	 */
	@Test
	public void testCreateCacheInstanceForTenantAndRegionCache()
	{
		when(tenant.getConfig()).thenReturn(config);
		when(Boolean.valueOf(config.getBoolean(CacheFactory.CACHE_LEGACY_MODE, false))).thenReturn(Boolean.FALSE);

		final Cache result = cacheFactory.createCacheInstance(tenant);
		assertTrue("RegionCacheAdapter instance was expected!", result instanceof RegionCacheAdapter);
	}

	//
	// TODO: enable this if you have proper mocking ready (see setUp()).
	//	/**
	//	 * Test method for
	//	 * {@link de.hybris.platform.cache.impl.CacheFactory#createCacheInstance(de.hybris.platform.core.Tenant)}.
	//	 */
	//	@Test
	//	public void testCreateCacheInstanceForTenantAndOldCache()
	//	{
	//		when(tenant.getConfig()).thenReturn(config);
	//		when(Boolean.valueOf(config.getBoolean(CacheFactory.CACHE_LEGACY_MODE, false))).thenReturn(Boolean.TRUE);
	//		//	When(new CacheBase(tenant, 20)).thenReturn(cacheBase);
	//
	//		final Cache result = cacheFactory.createCacheInstance(tenant);
	//		assertTrue(result instanceof DefaultCache);
	//	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.cache.impl.CacheFactory#createCacheInstance(de.hybris.platform.core.Tenant, de.hybris.platform.core.Tenant)}
	 * .
	 */
	@Test
	public void testCreateCacheInstanceForSlaveTenantAndRegionCache()
	{
		when(slaveTenant.getConfig()).thenReturn(config);
		when(Boolean.valueOf(config.getBoolean(CacheFactory.CACHE_LEGACY_MODE, false))).thenReturn(Boolean.FALSE);

		final Cache result = cacheFactory.createCacheInstance(slaveTenant, tenant);
		assertTrue("RegionCacheAdapter instance was expected!", result instanceof RegionCacheAdapter);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cache.impl.CacheFactory#createCacheInstance(de.hybris.platform.core.Tenant, int)}.
	 */
	@Test
	public void testCreateCacheInstanceForTenantWithCacheSizeAndRegionCache()
	{
		when(tenant.getConfig()).thenReturn(config);
		when(Boolean.valueOf(config.getBoolean(CacheFactory.CACHE_LEGACY_MODE, false))).thenReturn(Boolean.FALSE);

		final Cache result = cacheFactory.createCacheInstance(tenant, 20);
		assertTrue("RegionCacheAdapter instance was expected!", result instanceof RegionCacheAdapter);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cache.impl.CacheFactory#createCacheInstance(de.hybris.platform.core.Tenant, int, boolean)}
	 * .
	 */
	@Test
	public void testCrateCacheInstanceForTenantWithCacheSizeAndRegionCacheForceExclComputation()
	{
		when(tenant.getConfig()).thenReturn(config);
		when(Boolean.valueOf(config.getBoolean(CacheFactory.CACHE_LEGACY_MODE, false))).thenReturn(Boolean.FALSE);

		final Cache result = cacheFactory.createCacheInstance(tenant, 20, true);
		assertTrue("RegionCacheAdapter instance was expected!", result instanceof RegionCacheAdapter);
	}

}
