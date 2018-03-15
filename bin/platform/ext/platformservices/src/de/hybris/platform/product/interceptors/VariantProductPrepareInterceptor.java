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
package de.hybris.platform.product.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.variants.model.VariantProductModel;


public class VariantProductPrepareInterceptor implements PrepareInterceptor
{
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (!(model instanceof VariantProductModel))
		{
			return;
		}

		final VariantProductModel variantProductModel = (VariantProductModel) model;
		UpdateBaseProductModificationTime.fromPrepareInterceptor(variantProductModel, ctx).execute();
	}
}
