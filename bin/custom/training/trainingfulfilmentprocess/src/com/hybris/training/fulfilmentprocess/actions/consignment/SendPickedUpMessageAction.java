/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.actions.consignment;

import de.hybris.platform.orderprocessing.events.SendPickedUpMessageEvent;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.event.EventService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class SendPickedUpMessageAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(SendPickedUpMessageAction.class);

	private EventService eventService;

	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		getEventService().publishEvent(getEvent(process));
		if (LOG.isInfoEnabled())
		{
			LOG.info("Process: " + process.getCode() + " in step " + getClass());
		}
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

	protected SendPickedUpMessageEvent getEvent(final ConsignmentProcessModel process)
	{
		return new SendPickedUpMessageEvent(process);
	}
}
