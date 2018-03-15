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
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.generation.GenerationalCounterService;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;
import de.hybris.platform.regioncache.test.TestCacheKeyFactory.TestLegacyCacheKeyWithLoader;

import java.util.Collection;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/ThreeRegionFlexibleSearchAndEntityAndDefaultTest-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ThreeRegionFlexibleSearchAndEntityAndDefaultTest
{

	private static final long[] ONE_LONG = new long[]
	{ 1L };

	public static final String TENANT_ID = de.hybris.platform.core.MasterTenant.MASTERTENANT_ID;
	public static final String TYPE_A = "typeA";
	public static final String TYPE_B = "typeB";
	public static final String TYPE_C = "typeC";
	public static final String TYPE_D = "typeD";

	@Resource
	private CacheController controller;

	private final TestCacheKeyFactory cacheKeyFactory = new TestCacheKeyFactory();

	@Resource(name = "generationalCounterService")
	private GenerationalCounterService<String> counterService;

	@Resource(name = "EntityCacheRegion")
	private CacheRegion entityCacheRegion;

	@Resource(name = "DefaultCacheRegion")
	private CacheRegion defaultCacheRegion;

	@Resource(name = "FlexibleSearchCacheRegion")
	private CacheRegion flexibleSearchCacheRegion;

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
	public void flexibleSearchInvalidationTest() throws CacheRegionNotSpecifiedException
	{

		Assert.assertEquals(0, defaultCacheRegion.getMaxReachedSize());
		Assert.assertEquals(0, flexibleSearchCacheRegion.getMaxReachedSize());

		//1.) Create a set of EntityCacheTestKey and add to cache:
		final TestLegacyCacheKeyWithLoader entityKeyA = cacheKeyFactory.createEntityKey(TYPE_A, TENANT_ID);
		final TestLegacyCacheKeyWithLoader entityKeyB = cacheKeyFactory.createEntityKey(TYPE_B, TENANT_ID);
		final TestLegacyCacheKeyWithLoader entityKeyC = cacheKeyFactory.createEntityKey(TYPE_C, TENANT_ID);

		try
		{
			for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
			{ entityKeyA, entityKeyB, entityKeyC })
			{
				final Object result = controller.getWithLoader(key, key);
				Assert.assertEquals(key.getTypeCode(), result);
			}
		}
		catch (final CacheValueLoadException ex)
		{
			throw new RuntimeException(ex);
		}

		//2.) Create a set of RegistrableCacheKey and add to cache:
		//add to cache key A that depends on typeA
		//add to cache key B that depends on typeB
		//add to cache key C that depends on typeC
		//add to cache key AB that depends on typeA and typeB
		//add to cache key BC that depends on typeB and typeC

		final TestLoadableRegistrableCacheTestKey registerableKeyA = cacheKeyFactory.createLoadableKey(TENANT_ID, new String[]
		{ TYPE_A });
		final TestLoadableRegistrableCacheTestKey registerableKeyB = cacheKeyFactory.createLoadableKey(TENANT_ID, new String[]
		{ TYPE_B });
		final TestLoadableRegistrableCacheTestKey registerableKeyC = cacheKeyFactory.createLoadableKey(TENANT_ID, new String[]
		{ TYPE_C });
		final TestLoadableRegistrableCacheTestKey registerableKeyAB = cacheKeyFactory.createLoadableKey(TENANT_ID, new String[]
		{ TYPE_A, TYPE_B });
		final TestLoadableRegistrableCacheTestKey registerableKeyBC = cacheKeyFactory.createLoadableKey(TENANT_ID, new String[]
		{ TYPE_B, TYPE_C });

		try
		{
			for (final TestLoadableRegistrableCacheTestKey key : new TestLoadableRegistrableCacheTestKey[]
			{ registerableKeyA, registerableKeyB, registerableKeyC, registerableKeyAB, registerableKeyBC })
			{
				final Object result = controller.getWithLoader(key, key);
				Assert.assertEquals(key.getDependentTypes(), result);
			}
		}
		catch (final CacheValueLoadException ex)
		{
			throw new RuntimeException(ex);
		}

		//3.) Verify that 2 keys has been added to defaultCacheRegion

		//Check size
		Assert.assertEquals(2, defaultCacheRegion.getMaxReachedSize());

		//Verify correct keys are in the region map.
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyA, entityKeyB })
		{
			final Object result = defaultCacheRegion.get(key);
			Assert.assertEquals(key.getTypeCode(), result);
		}
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyC })
		{
			final Object result = defaultCacheRegion.get(key);
			Assert.assertEquals(null, result);
		}

		//4.) Verify that 1 key has been added to entityCacheRegion

		//Check size
		Assert.assertEquals(1, entityCacheRegion.getMaxReachedSize());

		//Verify correct keys are in the region map.
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyC })
		{
			final Object result = entityCacheRegion.get(key);
			Assert.assertEquals(key.getTypeCode(), result);
		}
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyA, entityKeyB })
		{
			final Object result = entityCacheRegion.get(key);
			Assert.assertEquals(null, result);
		}


		//5.) Verify that 5 keys has been added to valid cache region and that the registry for that region contains correct entries.

		//Check size
		Assert.assertEquals(5, flexibleSearchCacheRegion.getMaxReachedSize());

		//Verify correct keys are in the region map.
		for (final TestLoadableRegistrableCacheTestKey key : new TestLoadableRegistrableCacheTestKey[]
		{ registerableKeyA, registerableKeyB, registerableKeyC, registerableKeyAB, registerableKeyBC })
		{
			final Object result = flexibleSearchCacheRegion.get(key);
			Assert.assertEquals(key.getDependentTypes(), result);
		}

		//6.) Invalidate using typeA
		//Verify that: entityKeyA has been removed from defaultCacheRegion
		// !!! important !!! 2 keys (registerableKeyA and registerableKeyAB) !!!ARE NOT!!! removed from flexibleSearchCacheRegion and that the registry for that region contains correct entries.


		controller.invalidate(entityKeyA);

		//test counter invalidations
		Assert.assertArrayEquals(ThreeRegionFlexibleSearchAndEntityAndDefaultTest.ONE_LONG,
				counterService.getGenerations(new String[]
				{ TYPE_A }, TENANT_ID));

		//Check size
		Assert.assertEquals(1, entityCacheRegion.getMaxReachedSize());
		Assert.assertEquals(1, defaultCacheRegion.getMaxReachedSize());
		Assert.assertEquals(5, flexibleSearchCacheRegion.getMaxReachedSize());

		//Verify correct keys are in the region map.
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyC })
		{
			final Object result = entityCacheRegion.get(key);
			Assert.assertEquals(key.getTypeCode(), result);
		}
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyB })
		{
			final Object result = defaultCacheRegion.get(key);
			Assert.assertEquals(key.getTypeCode(), result);
		}
		for (final TestLoadableRegistrableCacheTestKey key : new TestLoadableRegistrableCacheTestKey[]
		{ registerableKeyA, registerableKeyB, registerableKeyC, registerableKeyAB, registerableKeyBC })
		{
			final Object result = flexibleSearchCacheRegion.get(key);
			Assert.assertEquals(key.getDependentTypes(), result);
		}


		//Verify invalidated keys are not in the region map.
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyA, entityKeyB })
		{
			final Object result = entityCacheRegion.get(key);
			Assert.assertEquals(null, result);
		}
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyA, entityKeyC })
		{
			final Object result = defaultCacheRegion.get(key);
			Assert.assertEquals(null, result);
		}
		for (final TestLoadableRegistrableCacheTestKey key : new TestLoadableRegistrableCacheTestKey[]
		{ registerableKeyA, registerableKeyB, registerableKeyC, registerableKeyAB, registerableKeyBC })
		{
			final Object result = flexibleSearchCacheRegion.get(key);
			Assert.assertEquals(key.getDependentTypes(), result);
		}

		//7.) Invalidate using typeB
		//Verify that: entityKeyB has been removed from defaultCacheRegion and 2 keys (registerableKeyB and registerableKeyBC) has been removed from flexibleSearchCacheRegion and that the registry for that region contains correct entries.

		controller.invalidate(entityKeyB);

		//test counter invalidations
		Assert.assertArrayEquals(ThreeRegionFlexibleSearchAndEntityAndDefaultTest.ONE_LONG,
				counterService.getGenerations(new String[]
				{ TYPE_A }, TENANT_ID));
		Assert.assertArrayEquals(ThreeRegionFlexibleSearchAndEntityAndDefaultTest.ONE_LONG,
				counterService.getGenerations(new String[]
				{ TYPE_B }, TENANT_ID));

		//Check size
		Assert.assertEquals(1, entityCacheRegion.getMaxReachedSize());
		Assert.assertEquals(0, defaultCacheRegion.getMaxReachedSize());
		Assert.assertEquals(5, flexibleSearchCacheRegion.getMaxReachedSize());

		//Verify correct keys are in the region map.
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyC })
		{
			final Object result = entityCacheRegion.get(key);
			Assert.assertEquals(key.getTypeCode(), result);
		}
		for (final TestLoadableRegistrableCacheTestKey key : new TestLoadableRegistrableCacheTestKey[]
		{ registerableKeyA, registerableKeyB, registerableKeyC, registerableKeyAB, registerableKeyBC })
		{
			final Object result = flexibleSearchCacheRegion.get(key);
			Assert.assertEquals(key.getDependentTypes(), result);
		}

		//Verify invalidated keys are not in the region map.
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyA, entityKeyB })
		{
			final Object result = entityCacheRegion.get(key);
			Assert.assertEquals(null, result);
		}
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyA, entityKeyB, entityKeyC })
		{
			final Object result = defaultCacheRegion.get(key);
			Assert.assertEquals(null, result);
		}
		for (final TestLoadableRegistrableCacheTestKey key : new TestLoadableRegistrableCacheTestKey[]
		{ registerableKeyA, registerableKeyB, registerableKeyC, registerableKeyAB, registerableKeyBC })
		{
			final Object result = flexibleSearchCacheRegion.get(key);
			Assert.assertEquals(key.getDependentTypes(), result);
		}

		//8.) Invalidate using typeC
		// Verify that 1 key has been removed from valid cache region and that the registry for that region contains correct entries.
		// Verify that: entityKeyC has been removed from defaultCacheRegion and 1 keys (registerableKeyC) has been removed from flexibleSearchCacheRegion and that the registry for that region contains correct entries.
		// Verify that flexibleSearchCacheRegion is empty and it's registry is empty.

		controller.invalidate(entityKeyC);

		//test counter invalidations
		Assert.assertArrayEquals(ONE_LONG, counterService.getGenerations(new String[]
		{ TYPE_A }, TENANT_ID));
		Assert.assertArrayEquals(ONE_LONG, counterService.getGenerations(new String[]
		{ TYPE_B }, TENANT_ID));
		Assert.assertArrayEquals(ONE_LONG, counterService.getGenerations(new String[]
		{ TYPE_C }, TENANT_ID));

		//Check size
		Assert.assertEquals(0, entityCacheRegion.getMaxReachedSize());
		Assert.assertEquals(0, defaultCacheRegion.getMaxReachedSize());
		Assert.assertEquals(5, flexibleSearchCacheRegion.getMaxReachedSize());

		//Verify invalidated keys are not in the region map.
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyA, entityKeyB, entityKeyC })
		{
			final Object result = entityCacheRegion.get(key);
			Assert.assertEquals(null, result);
		}
		for (final TestLegacyCacheKeyWithLoader key : new TestLegacyCacheKeyWithLoader[]
		{ entityKeyA, entityKeyB, entityKeyC })
		{
			final Object result = defaultCacheRegion.get(key);
			Assert.assertEquals(null, result);
		}
		for (final TestLoadableRegistrableCacheTestKey key : new TestLoadableRegistrableCacheTestKey[]
		{ registerableKeyA, registerableKeyB, registerableKeyC, registerableKeyAB, registerableKeyBC })
		{
			final Object result = flexibleSearchCacheRegion.get(key);
			Assert.assertEquals(key.getDependentTypes(), result);
		}



	}



}
