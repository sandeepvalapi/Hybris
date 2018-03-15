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
package de.hybris.platform.category.attribute;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;


public class CategoryAllSupercategories implements DynamicAttributeHandler<Collection<CategoryModel>, CategoryModel>
{
	private CategoryService categoryService;

	@Override
	public Collection<CategoryModel> get(final CategoryModel category)
	{
		return categoryService.getAllSupercategoriesForCategory(category);
	}

	@Override
	public void set(final CategoryModel model, final Collection<CategoryModel> value)
	{
		throw new UnsupportedOperationException();
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}
}
