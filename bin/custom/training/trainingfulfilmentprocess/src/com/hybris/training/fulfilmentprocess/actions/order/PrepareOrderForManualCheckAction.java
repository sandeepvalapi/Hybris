/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.actions.order;


import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.events.OrderFraudEmployeeNotificationEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.task.RetryLaterException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Action in order business process, which sets the order status to {@link OrderStatus#WAIT_FRAUD_MANUAL_CHECK} and
 * sends event {@link OrderFraudEmployeeNotificationEvent}. <br>
 * To notify back-end user about potential order frauds, a proper business logic needs to be implemented in appropriate
 * event listener.
 * 
 * 
 */
public class PrepareOrderForManualCheckAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(PrepareOrderForManualCheckAction.class);

	private EventService eventService;

	@Override
	public void executeAction(final OrderProcessModel process) throws RetryLaterException, Exception
	{
		ServicesUtil.validateParameterNotNull(process, "Process cannot be null");
		final OrderModel order = process.getOrder();
		ServicesUtil.validateParameterNotNull(order, "Process order cannot be null");
		order.setStatus(OrderStatus.WAIT_FRAUD_MANUAL_CHECK);
		modelService.save(order);

		eventService.publishEvent(new OrderFraudEmployeeNotificationEvent(order));
		LOG.info("Process: " + process.getCode() + " in step " + getClass());

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
