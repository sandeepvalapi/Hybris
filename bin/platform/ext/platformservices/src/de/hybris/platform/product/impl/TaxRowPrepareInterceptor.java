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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * {@link PrepareInterceptor} for the {@link TaxRowModel}.
 */
public class TaxRowPrepareInterceptor extends PDTRowPrepareInterceptor
{
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof TaxRowModel)
		{
			final TaxRowModel tModel = (TaxRowModel) model;
			super.onPrepare(tModel, ctx);
		}
	}

	@Override
	protected void updateCatalogVersion(final PDTRowModel pdtModel)
	{
		CatalogVersionModel catver = ((TaxRowModel) pdtModel).getCatalogVersion();
		if (catver == null)
		{
			final ProductModel prod = pdtModel.getProduct();
			if (prod != null)
			{
				catver = getCatalogTypeService().getCatalogVersionForCatalogVersionAwareModel(prod);
				//
				if (catver != null)
				{
					((TaxRowModel) pdtModel).setCatalogVersion(catver);
				}
			}
		}
	}

}
