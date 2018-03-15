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
package de.hybris.platform.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.category.model.ConfigurationCategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ConfiguratorSettingsResolutionStrategy;
import de.hybris.platform.product.model.AbstractConfiguratorSettingModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@code ConfiguratorSettingsResolutionStrategy}.
 */
public class DefaultConfiguratorSettingsResolutionStrategy implements ConfiguratorSettingsResolutionStrategy
{
	private CategoryService categoryService;


	/**
	 * This implementation merges {@link AbstractConfiguratorSettingModel} of given product respecting the following rules:
	 * <ul>
	 * <li>Settings with different qualifiers are simply combined</li>
	 *    <li>In case of qualifier collisions the winner is the setting that is closer to the product in category hierarchy</li>
	 *    <li>If there is a collision between setting qualifier on the same distance in category hierarchy,
	 *    random one of the settings will be taken. The behavior can be overridden
	 *    in {@link DefaultConfiguratorSettingsResolutionStrategy#processConfiguratorSettingAmbiguity}.</li>
	 * <li>For variant products settings of base product are also taken according to the rules above, but with priority
	 * lower then product variant's own settings.</li>
	 *    <li>The method keeps setting item order
	 *    as it has been defined in {@link ConfigurationCategoryModel#getConfiguratorSettings()}. The list is built from
	 *    root categories to leafs. Order of items from different categories of the same level is not defined,
	 *    e.g., if a product has super categories {@code A} with settings {@code A1} and {@code A2}
	 *    and {@code B} with settings {@code B1} and {@code B2}, the result list can be
	 *    either {@code [A1, A2, B1, B2]} or {@code [B1, B2, A1, A2]}.</li>
	 * </ul>
	 *
	 * @see ConfigurationCategoryModel
	 *
	 * @param product product of collect configurator settings for
	 * @return list of configurator settings
	 */
	@Nonnull
	@Override
	public List<AbstractConfiguratorSettingModel> getConfiguratorSettingsForProduct(@Nonnull final ProductModel product)
	{
		validateParameterNotNullStandardMessage("product", product);

		final Map<String, AbstractConfiguratorSettingModel> settings = new LinkedHashMap<>();

		for (final ProductModel p : getProductHierarchy(product))
		{
			// avoid copying maps
			settings.putAll(getSettingsMappedByQualifier(p));
		}
		return settings.isEmpty() ? Collections.emptyList() : new ArrayList<>(settings.values());
	}


	// returns [  base -> var0 -> var1 -> var2 -> product ]
	protected List<ProductModel> getProductHierarchy(final ProductModel product)
	{
		// in case people have cycles in their variants hierarchy we need to prevent endless loops!
		final Set<ProductModel> cycleDetectionSet = new HashSet<>();

		final List<ProductModel> ret = new ArrayList<>();

		for (ProductModel p = product; p != null && cycleDetectionSet.add(p); p = getSuperiorProduct(p))
		{
			ret.add(p);
		}
		Collections.reverse(ret);
		return ret;
	}

	// returns { variant -> base | product -> null }
	protected ProductModel getSuperiorProduct(final ProductModel product)
	{
		if (product instanceof VariantProductModel)
		{
			return ((VariantProductModel) product).getBaseProduct();
		}
		return null;
	}

	/**
	 * Builds a map configurator qualifier to configurator collected from all categories of given product.
	 *
	 * @param product product to gather configurator settings from
	 * @return configurations
	 */
	@Nonnull
	protected Map<String, AbstractConfiguratorSettingModel> getSettingsMappedByQualifier(final @Nonnull ProductModel product)
	{
		Map<String, AbstractConfiguratorSettingModel> result = new LinkedHashMap<>();
		Collection<List<CategoryModel>> paths = getAllCategoryPathsForProduct(product);
		int maxLn = paths.stream().mapToInt(List::size).max().orElse(0);
		for (int level = 0; level < maxLn; level++)
		{
			final Map<String, AbstractConfiguratorSettingModel> singleLevelSettings = getSettingsCutByIndex(paths, level);
			result = mergeConfiguratorSettings(result, singleLevelSettings);
		}
		return result;
	}

	/**
	 * Collects configurator settings from section of given category paths at given index,
	 * so for index=1 only second items (if any) of each path will be considered.
	 *
	 * @see DefaultConfiguratorSettingsResolutionStrategy#getAllCategoryPathsForProduct(ProductModel)
	 *
	 * @param paths category paths
	 * @param index which items of each of the paths to take
	 * @return map of configurator settings. Key collisions are already resolved by
	 *         {@link DefaultConfiguratorSettingsResolutionStrategy#processConfiguratorSettingAmbiguity(AbstractConfiguratorSettingModel, AbstractConfiguratorSettingModel)}
	 */
	@Nonnull
	protected Map<String, AbstractConfiguratorSettingModel> getSettingsCutByIndex(
			@Nonnull final Collection<List<CategoryModel>> paths,
			final int index)
	{
		return paths.stream()
				.filter(path -> path.size() > index)
				.map(path -> path.get(index))
				.filter(node -> node instanceof ConfigurationCategoryModel)
				.map(node -> (ConfigurationCategoryModel) node)
				.map(ConfigurationCategoryModel::getConfiguratorSettings)
				.flatMap(Collection::stream)
				.collect(
						Collectors.toMap(AbstractConfiguratorSettingModel::getQualifier,
								Function.identity(),
								this::processConfiguratorSettingAmbiguity)
				);
	}

	/**
	 * Collects all category paths for given product.
	 *
	 * @param product product
	 * @return category paths
	 */
	@Nonnull
	protected Collection<List<CategoryModel>> getAllCategoryPathsForProduct(final @Nonnull ProductModel product)
	{
		final Collection<CategoryModel> supercategories = product.getSupercategories();
		if (supercategories == null)
		{
			return Collections.emptyList();
		}
		return supercategories.stream()
				.map(category -> getCategoryService().getPathsForCategory(category))
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	/**
	 * Merges one map of configurator settings into the other.
	 * Default implementation simply adds all items, overriding colliding ones.
	 *
	 * @param first map setting qualifier to setting
	 * @param second map setting qualifier to setting
	 * @return result of merging of the two maps
	 */
	protected Map<String, AbstractConfiguratorSettingModel> mergeConfiguratorSettings(
			@Nonnull final Map<String, AbstractConfiguratorSettingModel> first,
			@Nonnull final Map<String, AbstractConfiguratorSettingModel> second)
	{
		final Map<String, AbstractConfiguratorSettingModel> result = new LinkedHashMap<>(first);
		result.putAll(second);
		return result;
	}

	/**
	 * The method decides which of the conflicting configurator settings to take.
	 * Conflicting settings are the ones with the same {@code Qualifier}.
	 *
	 * <p>The default implementation simply returns the first option.</p>
	 *
	 * @param option1 first option
	 * @param option2 second option
	 * @return the option chosen
	 */
	protected AbstractConfiguratorSettingModel processConfiguratorSettingAmbiguity(
			final AbstractConfiguratorSettingModel option1,
			final AbstractConfiguratorSettingModel option2)
	{
		return option1;
	}

	/**
	 * @return category service
	 */
	protected CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}
}
