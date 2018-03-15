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
package de.hybris.platform.catalog.impl;

import de.hybris.platform.catalog.ClassificationUtils;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.Date;


/**
 * PrepareInterceptor for the {@link ProductFeatureModel}. Sets the {@link ProductFeatureModel#QUALIFIER} if a
 * {@link ClassAttributeAssignmentModel} on the ProductFeatureModel exists.
 */
public class ProductFeaturePrepareInterceptor implements PrepareInterceptor
{
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof ProductFeatureModel)
		{
			final ProductFeatureModel pfm = (ProductFeatureModel) model;
			if (pfm.getQualifier() == null && pfm.getClassificationAttributeAssignment() != null)
			{
				pfm.setQualifier(ClassificationUtils.createFeatureQualifier(pfm.getClassificationAttributeAssignment()));
			}

			markProductAsModified(ctx, pfm.getProduct());

			handleValueAndFeaturePosition(pfm);
		}
	}

	private void handleValueAndFeaturePosition(final ProductFeatureModel pfm)
	{
		if (pfm.getValuePosition() == null)
		{
			pfm.setValuePosition(Integer.valueOf(0));
		}
		if (pfm.getFeaturePosition() == null)
		{
			pfm.setFeaturePosition(Integer.valueOf(0));
		}
	}

	private void markProductAsModified(final InterceptorContext ctx, final ProductModel product)
	{
		product.setModifiedtime(new Date());

		if (!ctx.contains(product, PersistenceOperation.SAVE))
		{
			ctx.registerElementFor(product, PersistenceOperation.SAVE);
		}
	}
}
