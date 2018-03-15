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
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;


public class PriceRowValidateInterceptor implements ValidateInterceptor<PriceRowModel>
{

	@Override
	public void onValidate(final PriceRowModel priceRow, final InterceptorContext ctx) throws InterceptorException
	{
		validateMinQuantity(priceRow);
		validateUnitFactor(priceRow);
	}

	private void validateMinQuantity(final PriceRowModel priceRow) throws InterceptorException
	{
		if (priceRow.getMinqtd() == null || priceRow.getMinqtd() < 0)
		{
			throw new InterceptorException("Min quantity must be equal or greater zero but was " + priceRow.getMinqtd());
		}
	}

	private void validateUnitFactor(final PriceRowModel priceRow) throws InterceptorException
	{
		if (priceRow.getUnitFactor() == null)
		{
			throw new InterceptorException("Unit factor cannot be null");
		}
		else if (priceRow.getUnitFactor() == 0)
		{
			throw new InterceptorException("Unit factor cannot be zero");
		}
	}
}
