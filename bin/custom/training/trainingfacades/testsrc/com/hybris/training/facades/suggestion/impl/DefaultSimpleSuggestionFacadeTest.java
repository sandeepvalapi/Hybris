/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.facades.suggestion.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.converters.populator.ProductPopulator;
import de.hybris.platform.commercefacades.product.converters.populator.ProductPrimaryImagePopulator;
import de.hybris.platform.commercefacades.product.converters.populator.VariantSelectedPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.url.impl.DefaultProductModelUrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import com.hybris.training.core.suggestion.SimpleSuggestionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for {@link DefaultSimpleSuggestionFacade}.
 */
@UnitTest
public class DefaultSimpleSuggestionFacadeTest
{
	@Mock
	private UserService userService;
	@Mock
	private CategoryService categoryService;
	@Mock
	private SimpleSuggestionService simpleSuggestionService;
	@Mock
	private ProductModel productModel;
	@Mock
	private ProductData productData;
	@Mock
	private AbstractPopulatingConverter abstractPopulatingConverter;
	@Mock
	private DefaultProductModelUrlResolver defaultProductModelUrlResolver;
	@Mock
	private VariantSelectedPopulator variantSelectedPopulator;
	@Mock
	private ProductBasicPopulator productBasicPopulator;
	@Mock
	private ProductPrimaryImagePopulator productPrimaryImagePopulator;

	private DefaultSimpleSuggestionFacade defaultSimpleSuggestionFacade;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultSimpleSuggestionFacade = new DefaultSimpleSuggestionFacade();
		defaultSimpleSuggestionFacade.setUserService(userService);
		defaultSimpleSuggestionFacade.setCategoryService(categoryService);
		defaultSimpleSuggestionFacade.setSimpleSuggestionService(simpleSuggestionService);
		defaultSimpleSuggestionFacade.setProductConverter(abstractPopulatingConverter);

		final ProductPopulator productPopulator = new ProductPopulator();

		productPopulator.setProductBasicPopulator(productBasicPopulator);
		productPopulator.setProductPrimaryImagePopulator(productPrimaryImagePopulator);
		productPopulator.setVariantSelectedPopulator(variantSelectedPopulator);
		productPopulator.setProductModelUrlResolver(defaultProductModelUrlResolver);

		final List<Populator<ProductModel, ProductData>> populators = new ArrayList<Populator<ProductModel, ProductData>>();
		populators.add(productPopulator);

		given(abstractPopulatingConverter.getPopulators()).willReturn(populators);
	}

	@Test
	public void testGetReferencedProductsForBoughtCategory()
	{
		final UserModel user = mock(UserModel.class);
		final CategoryModel category = mock(CategoryModel.class);

		final String categoryCode = "code";
		given(categoryService.getCategoryForCode(categoryCode)).willReturn(category);
		final Integer limit = NumberUtils.INTEGER_ONE;
		final boolean excludeBoughtProducts = true;
		final ProductReferenceTypeEnum type = ProductReferenceTypeEnum.FOLLOWUP;
		given(userService.getCurrentUser()).willReturn(user);
		given(simpleSuggestionService.getReferencesForPurchasedInCategory(category, user, type, excludeBoughtProducts, limit))
				.willReturn(Collections.singletonList(productModel));
		given(abstractPopulatingConverter.convert(productModel)).willReturn(productData);

		final List<ProductData> result = defaultSimpleSuggestionFacade.getReferencesForPurchasedInCategory(categoryCode, type,
				excludeBoughtProducts, limit);
		Assert.assertTrue(result.contains(productData));
	}
}
