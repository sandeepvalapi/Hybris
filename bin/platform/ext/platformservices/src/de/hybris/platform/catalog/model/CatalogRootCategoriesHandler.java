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
package de.hybris.platform.catalog.model;


import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import java.util.Collections;
import java.util.List;


public class CatalogRootCategoriesHandler implements DynamicAttributeHandler<List<CategoryModel>, CatalogModel>
{
	@Override
	public List<CategoryModel> get(final CatalogModel model)
	{
		if (model.getActiveCatalogVersion() != null)
		{
			return model.getActiveCatalogVersion().getRootCategories();
		}
		return Collections.emptyList();
	}

	@Override
	public void set(final CatalogModel model, final List<CategoryModel> categoryModels)
	{
		// read-only
	}
}
