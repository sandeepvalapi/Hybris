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
package de.hybris.platform.catalog.interceptors;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.references.daos.ProductReferencesDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import org.springframework.beans.factory.annotation.Required;


/**
 * Removes all {@link ProductModel#PRODUCTREFERENCES} and {@link ProductModel#FEATURES} when {@link ProductModel} is
 * removed.
 */
public class ProductRemoveInterceptor implements RemoveInterceptor
{

	private ProductReferencesDao productReferencesDao;

	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			for (final ProductReferenceModel reference : productReferencesDao.findAllReferences(product))
			{
				if (reference == null)
				{
					continue;
				}
				ctx.registerElementFor(reference, PersistenceOperation.DELETE);
			}

		}

	}

	@Required
	public void setProductReferencesDao(final ProductReferencesDao productReferencesDao)
	{
		this.productReferencesDao = productReferencesDao;
	}


}
