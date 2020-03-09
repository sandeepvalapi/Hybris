/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.event;

import de.hybris.platform.acceleratorservices.orderprocessing.model.OrderModificationProcessModel;
import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.orderprocessing.events.SendOrderPartiallyCanceledMessageEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;


/**
 * Listener for SendOrderPartiallyCanceledMessageEvent events.
 */
public class SendOrderPartiallyCanceledMessageEventListener extends
		AbstractAcceleratorSiteEventListener<SendOrderPartiallyCanceledMessageEvent>
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
	protected void onSiteEvent(final SendOrderPartiallyCanceledMessageEvent sendOrderPartiallyCanceledMessageEvent)
	{
		final OrderModel order = sendOrderPartiallyCanceledMessageEvent.getProcess().getOrder();
		final OrderModificationRecordEntryModel modificationRecordEntry = sendOrderPartiallyCanceledMessageEvent.getProcess()
				.getOrderModificationRecordEntry();

		final OrderModificationProcessModel orderModificationProcessModel = getBusinessProcessService().createProcess(
				"sendOrderPartiallyCanceledEmailProcess-" + modificationRecordEntry.getCode() + "-" + System.currentTimeMillis(),
				"sendOrderPartiallyCanceledEmailProcess");
		orderModificationProcessModel.setOrder(order);
		orderModificationProcessModel.setOrderModificationRecordEntry(modificationRecordEntry);
		getModelService().save(orderModificationProcessModel);
		getBusinessProcessService().startProcess(orderModificationProcessModel);
	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final SendOrderPartiallyCanceledMessageEvent event)
	{
		final AbstractOrderModel order = event.getProcess().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return site.getChannel();
	}
}
