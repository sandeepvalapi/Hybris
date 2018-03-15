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
import de.hybris.platform.regioncache.RegionRegistryAllocationStrategy;
import de.hybris.platform.regioncache.key.AbstractCacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.DefaultCacheRegionResolver;
import de.hybris.platform.regioncache.region.RegionType;
import de.hybris.platform.regioncache.region.impl.DefaultCacheRegion;
import de.hybris.platform.regioncache.region.impl.DefaultCacheRegionProvider;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;


@UnitTest
public class DefaultTestRegionResolverTest
{

	protected DefaultCacheRegionResolver createDefaultCacheRegionResolver(final List<CacheRegion> regions)
	{
		final DefaultCacheRegionProvider cacheRegionProvider = new DefaultCacheRegionProvider(regions);
		// Initialize dependencies
		cacheRegionProvider.setRegionRegistryAllocationStrategy(new RegionRegistryAllocationStrategy());
		cacheRegionProvider.init();

		final DefaultCacheRegionResolver resolver = new DefaultCacheRegionResolver(cacheRegionProvider);
		return resolver;
	}

	/**
	 * When there's a single region with type=CacheController.ALL_TYPES_REGION_TYPE, it should be selected for any key
	 * even when no region name is provided.
	 */
	@Test
	public void testSingleRegionForAllTypeCodes()
	{
		final List<CacheRegion> regions = new ArrayList<CacheRegion>();

		final CacheRegion region1 = new DefaultCacheRegion("region1", null, null, new String[]
		{ RegionType.ALL_TYPES.value() });
		regions.add(region1);

		final DefaultCacheRegionResolver resolver = createDefaultCacheRegionResolver(regions);

		//resolve with region name (SUCCESS)
		{
			final CacheRegion result1 = resolver.resolveForGet(new AbstractCacheKey(CacheUnitValueType.SERIALIZABLE, "type1",
					"master")
			{/**/}); // , "region1");

			Assert.assertEquals("invalid region found", region1, result1);
		}

		//resolve without region name (SUCCESS)
		{
			final CacheRegion result1 = resolver.resolveForGet(new AbstractCacheKey(CacheUnitValueType.SERIALIZABLE, "type1",
					"master")
			{/**/}); // , null);

			Assert.assertEquals("invalid region found", region1, result1);
		}

	}

	/**
	 * When there is more than one region, but a type is mapped only to one region, the region should be selected for a
	 * key of the type even when no region name is provided.
	 */
	@Test
	public void testSingleRegionForSpecificTypeCodes()
	{
		final List<CacheRegion> regions = new ArrayList<CacheRegion>();

		final CacheRegion region1 = new DefaultCacheRegion("region1", null, null, new String[]
		{ "type1", "type2" });

		final CacheRegion region2 = new DefaultCacheRegion("region2", null, null, new String[]
		{ "type2", "type3" });

		regions.add(region1);
		regions.add(region2);

		final DefaultCacheRegionResolver resolver = createDefaultCacheRegionResolver(regions);

		//resolve with region name (SUCCESS)
		{
			final CacheRegion result = resolver.resolveForGet(new AbstractCacheKey(CacheUnitValueType.SERIALIZABLE, "type1",
					"master")
			{/**/}); // , "region1");

			Assert.assertEquals("invalid region found", region1, result);
		}

		//resolve without region name (SUCCESS)
		{
			final CacheRegion result = resolver.resolveForGet(new AbstractCacheKey(CacheUnitValueType.SERIALIZABLE, "type1",
					"master")
			{/**/}); // , null);

			Assert.assertEquals("invalid region found", region1, result);
		}
	}

	/**
	 * One region can handle both specific types and "all other types" via use of CacheController.ALL_TYPES_REGION_TYPE
	 * type. This test method verifies if the dispatching works in that case.
	 */
	@Test
	public void testRegionsMixed()
	{
		final List<CacheRegion> regions = new ArrayList<CacheRegion>();

		final CacheRegion region1 = new DefaultCacheRegion("region1", null, null, new String[]
		{ "type1", RegionType.ALL_TYPES.value() });

		final CacheRegion region2 = new DefaultCacheRegion("region2", null, null, new String[]
		{ "type2" });


		regions.add(region1);
		regions.add(region2);


		final DefaultCacheRegionResolver resolver = createDefaultCacheRegionResolver(regions);


		//resolve region1 (no name needed)
		{
			final CacheRegion result = resolver.resolveForGet(new AbstractCacheKey(CacheUnitValueType.SERIALIZABLE, "type1",
					"master")
			{/**/}); // , null);
			Assert.assertEquals("region1 should have been found", region1, result);
		}

		//resolve region2 (no name needed)
		{
			final CacheRegion result = resolver.resolveForGet(new AbstractCacheKey(CacheUnitValueType.SERIALIZABLE, "type2",
					"master")
			{/**/}); // , null);

			Assert.assertEquals("region2 should have been found", region2, result);
		}

		//resolve region1 (all types handling fallback)
		{
			final CacheRegion result = resolver.resolveForGet(new AbstractCacheKey(CacheUnitValueType.SERIALIZABLE, "type3",
					"master")
			{/**/}); // , null);
			Assert.assertEquals("region1 should have been found", region1, result);
		}
	}

	@Test
	public void testMultipleAllTypesRegions()
	{
		final List<CacheRegion> regions = new ArrayList<CacheRegion>();

		final CacheRegion region1 = new DefaultCacheRegion("region1", null, null, new String[]
		{ "type1", RegionType.ALL_TYPES.value() });

		final CacheRegion region2 = new DefaultCacheRegion("region2", null, null, new String[]
		{ "type2", RegionType.ALL_TYPES.value() });


		regions.add(region1);
		regions.add(region2);

		try
		{
			//new DefaultCacheRegionProvider(regions);
			final DefaultCacheRegionProvider cacheRegionProvider = new DefaultCacheRegionProvider(regions);
			// Initialize dependencies
			cacheRegionProvider.setRegionRegistryAllocationStrategy(new RegionRegistryAllocationStrategy());
			cacheRegionProvider.init();
			Assert.fail("Expecting IllegalArgument exception");
		}
		catch (final IllegalArgumentException e)
		{
			// Expecting this
		}
	}

}
