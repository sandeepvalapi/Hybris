/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.suggestion;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;
import com.hybris.training.core.suggestion.impl.DefaultSimpleSuggestionService;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test suite for {@link DefaultSimpleSuggestionService}
 */
@IntegrationTest
public class DefaultSimpleSuggestionServiceIntegrationTest extends ServicelayerTransactionalTest
{

	@Resource
	private SimpleSuggestionService simpleSuggestionService;

	@Resource
	private UserService userService;

	@Resource
	private CategoryService categoryService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/trainingcore/test/testSimpleSuggestionService.csv", "utf-8");
	}

	@Test
	public void testReferencesForPurchasedInCategory()
	{
		final UserModel user = userService.getUserForUID("dejol");
		final CategoryModel category = categoryService.getCategoryForCode("cameras");

		List<ProductModel> result = simpleSuggestionService.getReferencesForPurchasedInCategory(category, user, null, false, null);
		Assert.assertEquals(4, result.size());
		result = simpleSuggestionService.getReferencesForPurchasedInCategory(category, user, null, false, NumberUtils.INTEGER_ONE);
		Assert.assertEquals(1, result.size());
		result = simpleSuggestionService.getReferencesForPurchasedInCategory(category, user, ProductReferenceTypeEnum.SIMILAR,
				false, null);
		Assert.assertEquals(1, result.size());
		result = simpleSuggestionService.getReferencesForPurchasedInCategory(category, user, ProductReferenceTypeEnum.ACCESSORIES,
				false, null);
		Assert.assertEquals(2, result.size());
		result = simpleSuggestionService.getReferencesForPurchasedInCategory(category, user, ProductReferenceTypeEnum.ACCESSORIES,
				true, null);
		Assert.assertEquals(1, result.size());
		final ProductModel product = result.get(0);
		Assert.assertEquals("adapterDC", product.getCode());
		Assert.assertEquals("adapter", product.getName());
	}
}
