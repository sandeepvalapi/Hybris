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

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.apache.log4j.Logger;


/**
 * Implementation of a {@link ValidateInterceptor} and {@link PrepareInterceptor} for classification systems. Does the
 * same as the {@link UniqueCatalogItemInterceptor}, only the
 * {@link #getDefaultCatalogVersion(InterceptorContext, Object)} is overwritten to adept to classification systems.
 */
public class UniqueClassificationSystemItemInterceptor extends UniqueCatalogItemInterceptor
{

	private static final Logger LOG = Logger.getLogger(UniqueClassificationSystemItemInterceptor.class.getName());

	private CategoryService categoryService;

	/**
	 * @param categoryService
	 *           the categoryService to set
	 */
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	@Override
	protected CatalogVersionModel getDefaultCatalogVersion(final InterceptorContext ctx, final Object model)
	{

		if (model instanceof ClassAttributeAssignmentModel)
		{

			final ClassAttributeAssignmentModel modelLocal = (ClassAttributeAssignmentModel) model;
			if (modelLocal.getSystemVersion() == null)
			{
				//try to get from ClassificationClass like in jalo
				if (modelLocal.getClassificationClass() != null && modelLocal.getClassificationClass().getCatalogVersion() != null)
				{
					return modelLocal.getClassificationClass().getCatalogVersion();
				}
				final CategoryModel possibleClassification = categoryService.getCategory(modelLocal.getClassificationClass()
						.getCode());
				if (possibleClassification instanceof ClassificationClassModel)
				{
					final ClassificationClassModel ccModel = (ClassificationClassModel) possibleClassification;
					if (ccModel.getCatalogVersion() != null)
					{
						return ccModel.getCatalogVersion();
					}
				}

			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Provided model " + model
					+ " can't fetch anyhow classification class version either from service or composed classification model class ");
		}
		return null;
	}
}
