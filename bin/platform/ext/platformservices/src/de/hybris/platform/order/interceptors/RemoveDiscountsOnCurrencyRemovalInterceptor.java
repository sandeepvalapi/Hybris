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
package de.hybris.platform.order.interceptors;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.order.DiscountService;
import de.hybris.platform.servicelayer.i18n.interceptors.RemoveBaseCurrencyInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorMapping;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * The interceptor performs cascade removal of {@link DiscountModel}s having this particular currency.<br>
 * <b>NOTE: </b> {@link InterceptorMapping} handling this interceptor must be positioned after all interceptors which
 * perform removal checks, i.e:<br>
 * <ul>
 * <li>{@link RemoveCurrencyCheckOrdersInterceptor}</li>
 * <li>{@link RemoveBaseCurrencyInterceptor}</li>
 * </ul>
 */
public class RemoveDiscountsOnCurrencyRemovalInterceptor implements RemoveInterceptor
{

	private DiscountService discountService;
	private ModelService modelService;

	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CurrencyModel)
		{
			final CurrencyModel currency = (CurrencyModel) model;
			final Collection<DiscountModel> currencyDiscounts = discountService.getDiscountsForCurrency(currency);
			if (!currencyDiscounts.isEmpty())
			{
				modelService.removeAll(currencyDiscounts);
			}
		}
		else
		{
			final String modelType = modelService.getModelType(model);
			Logger.getLogger(this.getClass()).warn("Unexpected model type : " + modelType);
		}
	}

	@Required
	public void setDiscountService(final DiscountService discountService)
	{
		this.discountService = discountService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
