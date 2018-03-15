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
package de.hybris.platform.servicelayer.model.visitor;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@UnitTest
public class ItemVisitorRegistryTest
{

	@InjectMocks
	private final ItemVisitorRegistry itemVisitorRegistry = new ItemVisitorRegistry();
	@Mock
	private ItemVisitor<ItemModel> defaultItemVisitor;
	@Mock
	private TypeService typeService;

	final Map<String, ItemVisitor<? extends ItemModel>> visitors = Maps.newHashMap();


	@Mock
	private ComposedTypeModel productComposedType;
	@Mock
	private ComposedTypeModel variantComposedType;
	@Mock
	private ComposedTypeModel categoryComposedType;
	@Mock
	private ComposedTypeModel itemComposedType;
	@Mock
	private ProductModel productModel;
	@Mock
	private VariantProductModel variantProductModel;
	@Mock
	private CategoryModel categoryModel;
	@Mock
	private ItemModel itemModel;

	@Before
	public void setUp()
	{

		MockitoAnnotations.initMocks(this);

		visitors.clear();
		itemVisitorRegistry.setVisitors(visitors);


		Mockito.when(typeService.getComposedTypeForCode("Product")).thenReturn(productComposedType);
		Mockito.when(typeService.getComposedTypeForCode("MyVariant")).thenReturn(variantComposedType);
		Mockito.when(typeService.getComposedTypeForCode("Category")).thenReturn(categoryComposedType);
		Mockito.when(typeService.getComposedTypeForCode("Item")).thenReturn(itemComposedType);

		Mockito.when(productComposedType.getAllSuperTypes()).thenReturn(Lists.newArrayList(itemComposedType));
		Mockito.when(variantComposedType.getAllSuperTypes()).thenReturn(Lists.newArrayList(productComposedType, itemComposedType));
		Mockito.when(categoryComposedType.getAllSuperTypes()).thenReturn(Lists.newArrayList(itemComposedType));

		Mockito.when(productModel.getItemtype()).thenReturn("Product");
		Mockito.when(variantProductModel.getItemtype()).thenReturn("MyVariant");
		Mockito.when(categoryModel.getItemtype()).thenReturn("Category");

		Mockito.when(productComposedType.getCode()).thenReturn("Product");
		Mockito.when(variantComposedType.getCode()).thenReturn("MyVariant");
		Mockito.when(categoryComposedType.getCode()).thenReturn("Category");

	}

	@Test
	public void testGetCrawlerStrategyByItemTypeExactMatch()
	{

		// given
		final ItemVisitor<ProductModel> givenProductStrategy = (theProduct, parents, ctx) -> Lists
				.newArrayList(theProduct.getSupercategories());
		final ItemVisitor<VariantProductModel> givenVariantProductStrategy = (variantProductModel, parents, ctx) -> Lists
				.newArrayList(variantProductModel.getBaseProduct());
		final ItemVisitor<CategoryModel> givenCategoryStrategy = (categoryModel, parents, ctx) -> Lists
				.newArrayList(categoryModel.getThumbnail());
		visitors.put("Product", givenProductStrategy);
		visitors.put("MyVariant", givenVariantProductStrategy);
		visitors.put("Category", givenCategoryStrategy);

		// when
		final ItemVisitor<ProductModel> productStrategy = itemVisitorRegistry.getVisitorByTypeCode(productModel.getItemtype());
		final ItemVisitor<ProductModel> variantProductStrategy = itemVisitorRegistry
				.getVisitorByTypeCode(variantProductModel.getItemtype());
		final ItemVisitor<ProductModel> categoryStrategy = itemVisitorRegistry.getVisitorByTypeCode(categoryModel.getItemtype());

		// then
		Assertions.assertThat(productStrategy).isNotNull().isEqualTo(givenProductStrategy);
		Assertions.assertThat(variantProductStrategy).isNotNull().isEqualTo(givenVariantProductStrategy);
		Assertions.assertThat(categoryStrategy).isNotNull().isEqualTo(givenCategoryStrategy);

	}


	@Test
	public void testGetCrawlerStrategyByItemTypeWithFallback()
	{


		// given
		final ItemVisitor<ProductModel> givenProductStrategy = (theProduct, parents, ctx) -> Lists
				.newArrayList(theProduct.getSupercategories());
		final ItemVisitor<CategoryModel> givenCategoryStrategy = (categoryModel, parents, ctx) -> Lists
				.newArrayList(categoryModel.getThumbnail());

		visitors.put("Product", givenProductStrategy);
		visitors.put("Category", givenCategoryStrategy);

		// when
		final ItemVisitor<ProductModel> productStrategy = itemVisitorRegistry.getVisitorByTypeCode(productModel.getItemtype());
		final ItemVisitor<ProductModel> variantProductStrategy = itemVisitorRegistry
				.getVisitorByTypeCode(variantProductModel.getItemtype());
		final ItemVisitor<ProductModel> categoryStrategy = itemVisitorRegistry.getVisitorByTypeCode(categoryModel.getItemtype());


		// then
		Assertions.assertThat(productStrategy).isNotNull().isEqualTo(givenProductStrategy);
		Assertions.assertThat(variantProductStrategy).isNotNull().isEqualTo(givenProductStrategy);
		Assertions.assertThat(categoryStrategy).isNotNull().isEqualTo(givenCategoryStrategy);

	}


	@Test
	public void testGetCrawlerStrategyByItemTypeNoFallback()
	{

		// given
		final ItemVisitor<ProductModel> givenProductStrategy = (theProduct, parents, ctx) -> Lists
				.newArrayList(theProduct.getSupercategories());
		final ItemVisitor<CategoryModel> givenCategoryStrategy = (categoryModel, parents, ctx) -> Lists
				.newArrayList(categoryModel.getThumbnail());
		visitors.put("Product!", givenProductStrategy);
		visitors.put("Category", givenCategoryStrategy);

		// when
		final ItemVisitor<ProductModel> productStrategy = itemVisitorRegistry.getVisitorByTypeCode(productModel.getItemtype());
		final ItemVisitor<ProductModel> variantProductStrategy = itemVisitorRegistry
				.getVisitorByTypeCode(variantProductModel.getItemtype());
		final ItemVisitor<ProductModel> categoryStrategy = itemVisitorRegistry.getVisitorByTypeCode(categoryModel.getItemtype());

		// then
		Assertions.assertThat(productStrategy).isNotNull().isEqualTo(givenProductStrategy);
		Assertions.assertThat(variantProductStrategy).isNotNull().isEqualTo(defaultItemVisitor);
		Assertions.assertThat(categoryStrategy).isNotNull().isEqualTo(givenCategoryStrategy);
	}


}
