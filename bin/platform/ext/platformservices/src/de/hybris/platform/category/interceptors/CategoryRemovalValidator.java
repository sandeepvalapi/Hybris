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
package de.hybris.platform.category.interceptors;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import org.springframework.beans.factory.annotation.Required;


/**
 * Validator for removal of {@link CategoryModel}. It checks if the specific category can be removed, and denies removal
 * request if it holds sub-categories. It can be disabled by setting the
 * {@link CategoryConstants#DISABLE_SUBCATEGORY_REMOVALCHECK} in the session to true.
 * 
 * @spring.bean categoryRemovalValidator
 */
public class CategoryRemovalValidator implements RemoveInterceptor
{
	private CategoryService categoryService;

	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CategoryModel)
		{
			if (!categoryService.isSubcategoryRemovalCheckDisabled() && ((CategoryModel) model).getAllSubcategories().size() > 0)
			{
				throw new InterceptorException("cannot remove [" + ((CategoryModel) model).getCode()
						+ "], since this category still has sub-categories");
			}
		}
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}
}
