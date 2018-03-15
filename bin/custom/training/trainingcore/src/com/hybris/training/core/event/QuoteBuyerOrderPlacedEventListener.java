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
package com.hybris.training.core.event;

import de.hybris.platform.commerceservices.event.QuoteBuyerOrderPlacedEvent;
import de.hybris.platform.commerceservices.order.CommerceQuoteService;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Event Listener for {@link QuoteBuyerOrderPlacedEvent} which updates the quote state and attaches the latest quote
 * snapshot to the order placed.
 */
public class QuoteBuyerOrderPlacedEventListener extends AbstractEventListener<QuoteBuyerOrderPlacedEvent>
{
	private ModelService modelService;
	private CommerceQuoteService commerceQuoteService;

	@Override
	protected void onEvent(final QuoteBuyerOrderPlacedEvent event)
	{
		final OrderModel orderModel = event.getOrder();
		final QuoteModel quoteModel = getCommerceQuoteService().createQuoteSnapshotWithState(event.getQuote(),
				QuoteState.BUYER_ORDERED);
		getModelService().refresh(orderModel);
		orderModel.setQuoteReference(quoteModel);
		getModelService().save(orderModel);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CommerceQuoteService getCommerceQuoteService()
	{
		return commerceQuoteService;
	}

	@Required
	public void setCommerceQuoteService(final CommerceQuoteService commerceQuoteService)
	{
		this.commerceQuoteService = commerceQuoteService;
	}

}
