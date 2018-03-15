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
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.daos.OrderDao;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * If the {@link CurrencyModel} is used in existing orders, the interceptor prevents remove action.
 */
public class RemoveCurrencyCheckOrdersInterceptor implements RemoveInterceptor
{

	private OrderDao orderDao;

	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CurrencyModel)
		{
			final CurrencyModel currency = (CurrencyModel) model;
			final List<AbstractOrderModel> currencyOrders = orderDao.findOrdersByCurrency(currency);
			if (!currencyOrders.isEmpty())
			{
				throw new InterceptorException("Cannot remove currency [" + currency.getIsocode()
						+ "], as there are orders using it!", this);
			}
		}
	}

	@Required
	public void setOrderDao(final OrderDao orderDao)
	{
		this.orderDao = orderDao;
	}
}
