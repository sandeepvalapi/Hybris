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


import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.model.AbstractConfiguratorSettingModel;

import java.util.List;

import javax.annotation.Nonnull;


/**
 * Encapsulates configurator-settings related logic.
 *
 * @see AbstractConfiguratorSettingModel
 * @see de.hybris.platform.category.model.ConfigurationCategoryModel
 */
public interface ConfiguratorSettingsService
{
	/**
	 * Returns configurator settings to be applied to given product.
	 * <p>
	 * Settings are collected from all categories assigned to the product including the whole hierarchy. When two
	 * categories have equal Id, only one which is closer to the product is taken. The method keeps setting item order
	 * from root to leaf categories.
	 * </p>
	 *
	 * @see ConfiguratorSettingsResolutionStrategy
	 * @see de.hybris.platform.category.model.ConfigurationCategoryModel
	 * @see de.hybris.platform.category.CategoryService
	 *
	 * @param product
	 *           product to get settings for
	 * @return list of configurator settings
	 */
	@Nonnull
	List<AbstractConfiguratorSettingModel> getConfiguratorSettingsForProduct(@Nonnull ProductModel product);
}
