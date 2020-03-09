/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.event;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.orderprocessing.events.SendNotPickedUpConsignmentCanceledMessageEvent;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;


/**
 * Listener for SendNotPickedUpConsignmentMessageEvent events.
 */
public class SendNotPickedUpConsignmentCanceledMessageEventListener extends
		AbstractAcceleratorSiteEventListener<SendNotPickedUpConsignmentCanceledMessageEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	/**
	 * @return the businessProcessService
	 */
	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	protected void onSiteEvent(final SendNotPickedUpConsignmentCanceledMessageEvent sendNotPickedUpConsignmentCanceledMessageEvent)
	{
		final ConsignmentModel consignmentModel = sendNotPickedUpConsignmentCanceledMessageEvent.getProcess().getConsignment();
		final ConsignmentProcessModel consignmentProcessModel = getBusinessProcessService().createProcess(
				"sendNotPickedUpConsignmentCanceledEmailProcess-" + consignmentModel.getCode() + "-" + System.currentTimeMillis(),
				"sendNotPickedUpConsignmentCanceledEmailProcess");
		consignmentProcessModel.setConsignment(consignmentModel);
		getModelService().save(consignmentProcessModel);
		getBusinessProcessService().startProcess(consignmentProcessModel);
	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final SendNotPickedUpConsignmentCanceledMessageEvent event)
	{
		final AbstractOrderModel order = event.getProcess().getConsignment().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return site.getChannel();
	}
}
