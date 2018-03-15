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
package de.hybris.platform.catalog.impl;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;


@Ignore
public abstract class AbstractCatalogTest extends ServicelayerTransactionalTest
{
	protected final static String TEST_CATALOG_1 = "testCatalog1";
	protected final static String TEST_CATALOG_2 = "testCatalog2";

	protected final static String WINTER_VERSION = "Winter";
	protected final static String SPRING_VERSION = "Spring";

	protected final static String TEST_USER_1 = "testUser1";
	protected final static String TEST_USER_2 = "testUser2";
	protected final static String TEST_USER_3 = "testUser3";

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	@Resource
	protected ModelService modelService;

	@Resource
	protected CatalogTypeService catalogTypeService;

	@Resource
	protected CatalogVersionService catalogVersionService;

	@Resource
	protected UserService userService;

	protected CatalogVersionModel test1Spring = null;
	protected CatalogVersionModel test1Winter = null;
	protected CatalogVersionModel test2Spring = null;
	protected CatalogVersionModel test2Winter = null;

	protected UserModel user1 = null;
	protected UserModel user2 = null;
	protected UserModel user3 = null;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/platformservices/test/catalog/testdata_catalogVersion.csv", "UTF-8");

		//DAO fetching already tested in CatalogVersionDao. The following models are needed as test data for the following test cases.
		test1Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, SPRING_VERSION);
		Assert.assertEquals("Unexpected catalog ID", TEST_CATALOG_1, test1Spring.getCatalog().getId());
		Assert.assertEquals("Unexpected catalog Version", SPRING_VERSION, test1Spring.getVersion());

		test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);
		Assert.assertEquals("Unexpected catalog ID", TEST_CATALOG_1, test1Winter.getCatalog().getId());
		Assert.assertEquals("Unexpected catalog Version", WINTER_VERSION, test1Winter.getVersion());

		test2Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, SPRING_VERSION);
		Assert.assertEquals("Unexpected catalog ID", TEST_CATALOG_2, test2Spring.getCatalog().getId());
		Assert.assertEquals("Unexpected catalog Version", SPRING_VERSION, test2Spring.getVersion());

		test2Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, WINTER_VERSION);
		Assert.assertEquals("Unexpected catalog ID", TEST_CATALOG_2, test2Winter.getCatalog().getId());
		Assert.assertEquals("Unexpected catalog Version", WINTER_VERSION, test2Winter.getVersion());

		catalogVersionService.setSessionCatalogVersions(Collections.EMPTY_SET);

		user1 = userService.getUserForUID(TEST_USER_1);
		user2 = userService.getUserForUID(TEST_USER_2);
		user3 = userService.getUserForUID(TEST_USER_3);
	}

}
