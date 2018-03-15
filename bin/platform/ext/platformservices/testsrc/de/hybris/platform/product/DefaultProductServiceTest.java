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
package de.hybris.platform.product;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@DemoTest
public class DefaultProductServiceTest extends ServicelayerTransactionalTest
{

	@Resource
	private CategoryService categoryService;
	@Resource
	private ProductService productService;


	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createHardwareCatalog();
	}

	/**
	 * Checks the product amount of the category.
	 * <ul>
	 * <li>checks that category "HW1000" has 12 products,</li>
	 * <li>checks that category "HW1100" has 3 products.</li>
	 * </ul>
	 */
	@Test
	public void testGetAllProductsCount()
	{
		String code = "HW1000";
		CategoryModel category = categoryService.getCategoryForCode(code);
		Integer count = productService.getAllProductsCountForCategory(category);
		assertEquals(12, count.intValue());
		code = "HW1100";
		category = categoryService.getCategoryForCode(code);
		count = productService.getAllProductsCountForCategory(category);
		assertEquals(3, count.intValue());
	}

	/**
	 * Checks that the category "HW1000" contains at least one product.
	 */
	@Test
	public void testContainsProducts()
	{
		final String code = "HW1000";
		final CategoryModel category = categoryService.getCategoryForCode(code);
		final boolean result = productService.containsProductsForCategory(category);
		assertTrue(result);
	}

}
