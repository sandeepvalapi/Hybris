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

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.daos.OrderDao;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * If there are still orders using {@link DeliveryModeModel} to be removed, the interceptor stops removal action with
 * {@link InterceptorException}.
 */
public class RemoveDeliveryModeCheckOrdersInterceptor implements RemoveInterceptor
{

	private OrderDao orderDao;

	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof DeliveryModeModel)
		{
			final DeliveryModeModel deliveryMode = (DeliveryModeModel) model;
			final Collection<AbstractOrderModel> deliveryModeOrders = orderDao.findOrdersByDelivereMode(deliveryMode);
			if (!deliveryModeOrders.isEmpty())
			{
				throw new InterceptorException("Cannot remove deliveryMode [" + deliveryMode.getCode()
						+ "] as there are still orders using it!", this);
			}
		}
		else
		{
			Logger.getLogger(this.getClass()).warn("Unexpected model type");
		}

	}

	@Required
	public void setOrderDao(final OrderDao orderDao)
	{
		this.orderDao = orderDao;
	}

}
