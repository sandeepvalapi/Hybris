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
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/OneRegionTestDefaultLRUCacheRegion-context.xml" }, inheritLocations = false)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class OneRegionTestDefaultLRUCacheRegion extends AbstractCacheControllerOneRegionTest
{

	@Ignore("Google's LRU map max size is not predictable")
	@Override
	public void statsTest()
	{
		// Google's LRU map max size is not predictable
	}

	@Ignore("Google's LRU map max size is not predictable")
	@Override
	public void evictionTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
	{
		// Google's LRU map max size is not predictable
	}

}
