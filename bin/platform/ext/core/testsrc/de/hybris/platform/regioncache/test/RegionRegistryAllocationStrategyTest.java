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
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.RegionType;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test for RegionRegistryAllocationStrategy
 */
@UnitTest
public class RegionRegistryAllocationStrategyTest
{

	@Mock
	private CacheRegion region;

	private final RegionRegistryAllocationStrategy obj = new RegionRegistryAllocationStrategy();

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * For region configuration with empty types RegionRegistryAllocationStrategy should "create" registry. Not to create
	 * registry RegionType.NON_REGISTRABLE (only) should be provided as a type.
	 */
	@Test
	public void shouldUseRegistryForManualRegion()
	{
		Mockito.when(region.getHandledTypes()).thenReturn(new String[] {});
		Assert.assertTrue(obj.isRegionRequiresRegistry(region));
	}

	@Test
	public void shouldNotUseRegistryForManualRegion()
	{
		Mockito.when(region.getHandledTypes()).thenReturn(new String[]
		{ RegionType.NON_REGISTRABLE.value() });

		Assert.assertFalse(obj.isRegionRequiresRegistry(region));
	}

	@Test
	public void testNullValue()
	{
		Assert.assertFalse(obj.isRegionRequiresRegistry(null));
	}

}
