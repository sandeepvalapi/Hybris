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
 * Collects configurator settings from categories.
 *
 * @see de.hybris.platform.category.model.ConfigurationCategoryModel
 * @see ConfiguratorSettingsService#getConfiguratorSettingsForProduct(ProductModel)
 */
public interface ConfiguratorSettingsResolutionStrategy
{
	/**
	 * Collects configurator settings for given product filtered by settings' qualifier.
     * To override a setting you should create one with the same qualifier in a descending category.
	 *
	 * @param product
	 *           product to get settings for
	 * @return list of configuration settings. The list items are unique relative to their {@code qualifier}.
	 */
	@Nonnull
	List<AbstractConfiguratorSettingModel> getConfiguratorSettingsForProduct(@Nonnull ProductModel product);
}
