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

import static junit.framework.Assert.assertNotNull;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.daos.ItemSyncTimestampDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.core.model.product.ProductModel;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ItemSyncTimestampDaoTest extends AbstractCatalogTest
{
	@Resource
	private ItemSyncTimestampDao itemSyncTimestampDao;

	private ProductModel product1 = null;
	private ProductModel product2 = null;

	private ItemSyncTimestampModel syncTimestamp;
	private ItemSyncTimestampModel syncTimestamp2;

	@Before
	public void before()
	{
		final CatalogModel catalogTemplate = new CatalogModel();
		catalogTemplate.setId(TEST_CATALOG_1);
		assertNotNull(flexibleSearchService.getModelByExample(catalogTemplate));
		catalogTemplate.setId(TEST_CATALOG_2);
		assertNotNull(flexibleSearchService.getModelByExample(catalogTemplate));

		setupTestData();
	}


	public void setupTestData()
	{
		product1 = modelService.create(ProductModel.class);
		product2 = modelService.create(ProductModel.class);


		product1.setCode("product1");
		product2.setCode("product2");
		modelService.save(product1);
		modelService.save(product2);

		syncTimestamp = modelService.create(ItemSyncTimestampModel.class);
		syncTimestamp.setSourceItem(product1);
		syncTimestamp.setTargetItem(product2);
		syncTimestamp.setSourceVersion(test1Winter);
		syncTimestamp.setTargetVersion(test1Spring);

		modelService.save(syncTimestamp);



		syncTimestamp2 = modelService.create(ItemSyncTimestampModel.class);
		syncTimestamp2.setSourceItem(product2);
		syncTimestamp2.setTargetItem(product1);
		syncTimestamp2.setTargetVersion(test1Winter);
		syncTimestamp2.setSourceVersion(test1Spring);

		modelService.save(syncTimestamp2);

	}

	@Test
	public void testFindSyncTimestampsForItem()
	{
		assertThat(itemSyncTimestampDao.findSyncTimestampsByItem(product1, -1)).contains(syncTimestamp2, syncTimestamp);
	}

	@Test
	public void testFindSyncTimestampsForCatalogVersion()
	{
		assertThat(itemSyncTimestampDao.findSyncTimestampsByCatalogVersion(test1Winter, -1))
				.contains(syncTimestamp2, syncTimestamp);
		assertThat(itemSyncTimestampDao.findSyncTimestampsByCatalogVersion(test1Spring, -1))
				.contains(syncTimestamp2, syncTimestamp);
	}

}
