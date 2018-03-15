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
package de.hybris.platform.category.daos.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests the implementation of {@link CategoryDao}.
 */
@IntegrationTest
public class DefaultCategoryDaoTest extends ServicelayerTransactionalTest
{
	@Resource
	private CategoryDao categoryDao;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private ProductService productService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		//		createHardwareCatalog();
		createDefaultCatalog();
	}


	@Test
	public void shouldFindCategoriesForCatalogVersionAndProduct()
	{
		// given
		final ProductModel product = productService.getProductForCode("testProduct1");
		final CatalogVersionModel catalogVersion = product.getCatalogVersion();

		// when
		final Collection<CategoryModel> categories = categoryDao.findCategoriesByCatalogVersionAndProduct(catalogVersion, product);

		// then
		assertThat(categories).isNotNull();
		assertThat(categories).hasSize(2);
	}

	@Test
	public void testFindCategoryByCode()
	{
		String categoryCode = "testCategory0";
		Collection<CategoryModel> categories = categoryDao.findCategoriesByCode(categoryCode);
		assertEquals("more than one category found", 1, categories.size());
		assertEquals("different categories", categoryCode, categories.iterator().next().getCode());

		categoryCode = "does_not_exist";
		categories = categoryDao.findCategoriesByCode(categoryCode);
		assertEquals("no category should be found", 0, categories.size());
	}

	@Test
	public void testFindCategoriesByCode()
	{
		//test findCategories(catalogVersion, code)
		String categoryCode = "testCategory0";
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		Collection<CategoryModel> categories = categoryDao.findCategoriesByCode(catalogVersion, categoryCode);
		assertEquals("more than one category found", 1, categories.size());
		assertEquals("different categories", categoryCode, categories.iterator().next().getCode());

		categoryCode = "does_not_exist";
		categories = categoryDao.findCategoriesByCode(catalogVersion, categoryCode);
		assertEquals("no category should be found", 0, categories.size());
	}

	@Test
	public void testFindRootCategoriesByCatalogVersion()
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Collection<CategoryModel> categories = categoryDao.findRootCategoriesByCatalogVersion(catalogVersion);

		final Set<String> expectedCodes = new HashSet<String>();
		expectedCodes.add("testCategory0");
		expectedCodes.add("testCategory10");
		//		expectedCodes.add("HW1000");
		//		expectedCodes.add("topseller");
		//		expectedCodes.add("specialoffers");

		final Set<String> categoryCodes = new HashSet<String>();
		for (final CategoryModel cat : categories)
		{
			categoryCodes.add(cat.getCode());
		}
		assertEquals("the amount of category-codes doesn't match", expectedCodes.size(), categoryCodes.size());
		assertTrue("expectedCodes does not contain all categoryCodes", expectedCodes.containsAll(categoryCodes));
		assertTrue("categoryCodes does not contain all expectedCodes", categoryCodes.containsAll(expectedCodes));
	}

}
