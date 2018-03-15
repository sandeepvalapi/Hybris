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
package com.hybris.training.fulfilmentprocess.actions.order;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.orderprocessing.events.OrderFraudCustomerNotificationEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;


public class NotifyCustomerAboutFraudAction extends AbstractProceduralAction<OrderProcessModel>
{
	private EventService eventService;

	@Override
	public void executeAction(final OrderProcessModel process)
	{
		ServicesUtil.validateParameterNotNull(process, "Process can not be null");
		ServicesUtil.validateParameterNotNull(process.getOrder(), "Order can not be null");
		process.getOrder().setStatus(OrderStatus.SUSPENDED);
		getModelService().save(process.getOrder());
		getEventService().publishEvent(new OrderFraudCustomerNotificationEvent(process));
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}
