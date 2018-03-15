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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.category.model.ConfigurationCategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.impl.DefaultConfiguratorSettingsResolutionStrategy;
import de.hybris.platform.product.model.AbstractConfiguratorSettingModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultConfiguratorSettingsResolutionStrategyTest
{
	private DefaultConfiguratorSettingsResolutionStrategy strategy;

	@Before
	public void setUp()
	{
		// unit testing this requires manual setup
		strategy = new DefaultConfiguratorSettingsResolutionStrategy();
		strategy.setCategoryService(new DefaultCategoryService());
	}

	@Test
	public void shouldWorkWithNullCategoryList()
	{
		final ProductModel product = new ProductModel();
		product.setSupercategories(null);
		strategy.getConfiguratorSettingsForProduct(product);
	}

	@Test
	public void shouldWorkWithNullCategoryListOfBaseProduct()
	{
		final VariantProductModel variantProduct = new VariantProductModel();
		final ProductModel baseProduct = new ProductModel();
		variantProduct.setBaseProduct(baseProduct);
		baseProduct.setSupercategories(null);
		variantProduct.setSupercategories(null);
		strategy.getConfiguratorSettingsForProduct(variantProduct);
	}

	@Test
	public void shouldTakeSingleDirectCategory()
	{
		final ProductModel product = new ProductModel();
		final CategoryModel directCategory = configurableCategory("test");
		category(directCategory, configurableCategory("wrong"));
		product.setSupercategories(Collections.singletonList(directCategory));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(product);
		assertThat(configs, contains(hasProperty("qualifier", is("test"))));
	}

	@Test
	public void shouldTakeAscendingCategory()
	{
		final ProductModel product = new ProductModel();
		final CategoryModel theCategory = category();
		category(configurableCategory("test", theCategory), configurableCategory("wrong"));
		product.setSupercategories(Collections.singletonList(theCategory));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(product);
		assertThat(configs, contains(hasProperty("qualifier", is("test"))));
	}

	@Test
	public void shouldMergeSettingsOfTheSameLevel()
	{
		final ProductModel product = new ProductModel();
		final CategoryModel direct = category();
		category(configurableCategory(Arrays.asList("test1", "test2"), direct), configurableCategory("wrong"));
		product.setSupercategories(Collections.singletonList(direct));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(product);
		assertThat(configs, containsInAnyOrder(hasProperty("qualifier", is("test1")), hasProperty("qualifier", is("test2"))));
	}

	@Test
	public void shouldMergeSettingsOfDifferentLevels()
	{
		final ProductModel product = new ProductModel();
		final ConfigurationCategoryModel direct = configurableCategory("test2");
		category(configurableCategory("test1", direct), configurableCategory("wrong"));
		product.setSupercategories(Collections.singletonList(direct));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(product);
		assertThat(configs, containsInAnyOrder(hasProperty("qualifier", is("test1")), hasProperty("qualifier", is("test2"))));
	}

	@Test
	public void shouldOverrideSettingsWithTheClosest()
	{
		final ConfigurationCategoryModel direct = configurableCategory("test1");
		configurableCategory("test1", category(direct));
		final ProductModel product = new ProductModel();
		product.setSupercategories(Collections.singletonList(direct));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(product);
		assertThat(configs, iterableWithSize(1));
		assertTrue(configs.get(0) == direct.getConfiguratorSettings().get(0));
	}

	@Test
	public void shouldTakeSettingsOfBaseProduct()
	{
		final VariantProductModel variantProduct = new VariantProductModel();
		final ProductModel baseProduct = new ProductModel();
		variantProduct.setBaseProduct(baseProduct);
		baseProduct.setSupercategories(Collections.singletonList(configurableCategory("test")));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(variantProduct);
		assertThat(configs, contains(hasProperty("qualifier", is("test"))));
	}

	@Test
	public void shouldMergeSettingsWithBaseProduct()
	{
		final VariantProductModel variantProduct = new VariantProductModel();
		final ProductModel baseProduct = new ProductModel();
		variantProduct.setBaseProduct(baseProduct);
		baseProduct.setSupercategories(Collections.singletonList(configurableCategory("test1")));
		variantProduct.setSupercategories(Collections.singletonList(configurableCategory("test2")));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(variantProduct);
		assertThat(configs, containsInAnyOrder(hasProperty("qualifier", is("test1")), hasProperty("qualifier", is("test2"))));
	}

	@Test
	public void shouldOverrideSettingsOfBaseProduct()
	{
		final VariantProductModel variantProduct = new VariantProductModel();
		final ProductModel baseProduct = new ProductModel();
		variantProduct.setBaseProduct(baseProduct);
		baseProduct.setSupercategories(Collections.singletonList(configurableCategory("test1")));
		final ConfigurationCategoryModel ownCategory = configurableCategory("test1");
		variantProduct.setSupercategories(Collections.singletonList(ownCategory));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(variantProduct);
		assertThat(configs, iterableWithSize(1));
		assertTrue(configs.get(0) == ownCategory.getConfiguratorSettings().get(0));
	}

	@Test
	public void shouldGetOnlyOneOfContradictingSettings()
	{
		final ProductModel product = new ProductModel();
		product.setSupercategories(Arrays.asList(configurableCategory("test"), configurableCategory("test")));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(product);
		assertThat(configs, iterableWithSize(1));
	}

	@Test
	public void shouldWorkWithMultipath()
	{
		final CategoryModel c1 = configurableCategory("path1");
		final CategoryModel c2 = category();
		category(c1);
		category(configurableCategory("path2", c2));
		final ProductModel product = new ProductModel();
		product.setSupercategories(Arrays.asList(c1, c2));
		final List<AbstractConfiguratorSettingModel> configs = strategy.getConfiguratorSettingsForProduct(product);
		assertThat(configs, containsInAnyOrder(hasProperty("qualifier", is("path1")), hasProperty("qualifier", is("path2"))));
	}

	protected CategoryModel category(final CategoryModel... children)
	{
		final CategoryModel result = new CategoryModel();
		updateCategoryHierarchy(result, children);
		return result;
	}

	protected ConfigurationCategoryModel configurableCategory(final String configQualifier, final CategoryModel... children)
	{
		return configurableCategory(Collections.singletonList(configQualifier), children);
	}

	protected ConfigurationCategoryModel configurableCategory(final List<String> configs, final CategoryModel... children)
	{
		final ConfigurationCategoryModel result = new ConfigurationCategoryModel();
		updateCategoryHierarchy(result, children);
		result.setConfiguratorSettings(configs.stream().map(this::config).collect(Collectors.toList()));
		return result;
	}

	protected void updateCategoryHierarchy(final CategoryModel parent, final CategoryModel[] children)
	{
		parent.setCategories(Arrays.asList(children));
		for (final CategoryModel child : children)
		{
			assert child.getSupercategories() == null;
			child.setSupercategories(Collections.singletonList(parent));
		}
	}

	protected AbstractConfiguratorSettingModel config(final String qualifier)
	{
		final AbstractConfiguratorSettingModel result = new AbstractConfiguratorSettingModel();
        result.setQualifier(qualifier);
		return result;
	}

	protected DefaultConfiguratorSettingsResolutionStrategy getStrategy()
	{
		return strategy;
	}
}
