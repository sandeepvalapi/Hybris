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
import de.hybris.platform.jalo.JaloTypeCacheUnit.JaloTypeCacheKey;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch.FlexibleSearchCacheKey;
import de.hybris.platform.persistence.hjmp.FinderResult.FinderResultCacheKey;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.key.AbstractCacheKey;
import de.hybris.platform.regioncache.key.AbstractRegistrableCacheKey;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.impl.GenerationalCacheDelegate;
import de.hybris.platform.regioncache.key.legacy.AbstractLegacyRegistrableCacheKey;
import de.hybris.platform.regioncache.key.legacy.LegacyCacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;

import java.util.Collection;

import javax.annotation.Resource;

import junit.framework.Assert;
import net.sf.ehcache.CacheManager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
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
{ "/test/EHCacheRegionPerformanceTest-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CacheKeyTest
{
	@Resource
	protected CacheController controller;

	@Resource
	private GenerationalCacheDelegate registrableLegacyCacheKeyFactory;

	@Before
	public void init()
	{
		Logger.getLogger(LegacyCacheKey.class).setLevel(Level.INFO);
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
	public void differentTenantLegacyKey() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{

		final Object key[] = new Object[]
		{ "A", "B", "C", "D" };

		final LegacyCacheKey key1 = new LegacyCacheKey(key, "tenant1");
		final LegacyCacheKey key2 = new LegacyCacheKey(key, "tenant2");

		final TestCacheValueLoader loader1 = new TestCacheValueLoader("val1");
		final TestCacheValueLoader loader2 = new TestCacheValueLoader("val2");

		Assert.assertNotNull(controller);

		final Object val1 = controller.getWithLoader(key1, loader1);
		Assert.assertEquals(loader1.getVal(), val1);
		final Object val2 = controller.getWithLoader(key2, loader2);

		Assert.assertEquals(loader1.getVal(), val1);
		Assert.assertEquals(loader2.getVal(), val2);

	}

	@Test
	public void differentTenantKey() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{

		final CacheKey key1 = new AbstractCacheKey("T", "tenant1")
		{ /* */};
		final CacheKey key2 = new AbstractCacheKey("T", "tenant2")
		{ /* */};

		final TestCacheValueLoader loader1 = new TestCacheValueLoader("val1");
		final TestCacheValueLoader loader2 = new TestCacheValueLoader("val2");

		Assert.assertNotNull(controller);

		final Object val1 = controller.getWithLoader(key1, loader1);
		Assert.assertEquals(loader1.getVal(), val1);
		final Object val2 = controller.getWithLoader(key2, loader2);

		Assert.assertEquals(loader1.getVal(), val1);
		Assert.assertEquals(loader2.getVal(), val2);

	}

	@Test
	public void cacheKeyUniqnessTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		for (int j = 0; j < 10; ++j)
		{
			for (int i = 100000; i < 101000; ++i)
			{
				final CacheKey key = new AbstractCacheKey(String.valueOf(i), "tenant1")
				{ /* */};
				final CacheKey key2 = (CacheKey) controller.getWithLoader(key, new TestCacheValueLoader(key));
				Assert.assertEquals(key, key2);
			}
		}
	}

	@Test
	public void legacyCacheKeyUniqnessTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		for (int j = 0; j < 10; ++j)
		{
			for (int i = 10000; i < 11000; ++i)
			{
				final CacheKey key = new LegacyCacheKey(new Object[]
				{ Integer.valueOf(i), Integer.valueOf(j) }, "tenant1");
				final CacheKey key2 = (CacheKey) controller.getWithLoader(key, new TestCacheValueLoader(key));
				Assert.assertEquals(key, key2);
			}
		}

		for (int j = 0; j < 10; ++j)
		{
			for (int i = 1000000; i < 1070000; ++i)
			{
				final CacheKey key = new LegacyCacheKey(new Object[]
				{ Integer.valueOf(j), Integer.valueOf(i) }, "tenant1");
				final CacheKey key2 = (CacheKey) controller.get(key);
				Assert.assertNull(key2);
			}
		}
	}

	/**
	 * Tests if all query types implements proper interface
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void flexibleSearchRegistrableKeyTest()
	{
		Assert.assertTrue(AbstractRegistrableCacheKey.class.isAssignableFrom(FlexibleSearchCacheKey.class));
		Assert.assertTrue(AbstractLegacyRegistrableCacheKey.class.isAssignableFrom(FinderResultCacheKey.class));
		Assert.assertTrue(AbstractLegacyRegistrableCacheKey.class.isAssignableFrom(JaloTypeCacheKey.class));
	}

}
