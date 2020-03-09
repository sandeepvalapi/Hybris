/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.actions.quote;

import de.hybris.platform.commerceservices.model.process.QuoteProcessModel;
import de.hybris.platform.commerceservices.order.CommerceQuoteService;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.QuoteService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This action class checks for seller approver response. If quote was approved, a new quote snapshot will be created
 * with status as BUYER_OFFER
 */
public class CreateBuyerOfferAction extends AbstractProceduralAction<QuoteProcessModel>
{
	private QuoteService quoteService;
	private CommerceQuoteService commerceQuoteService;
	private static final Logger LOG = Logger.getLogger(CheckSellerApproverResponseOnQuoteAction.class);

	@Override
	public void executeAction(final QuoteProcessModel process) throws RetryLaterException, Exception
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("In CreateBuyerOfferAction for process code : [%s]", process.getCode()));
		}

		final QuoteModel quoteModel = getQuoteService().getCurrentQuoteForCode(process.getQuoteCode());

		if (QuoteState.SELLERAPPROVER_APPROVED.equals(quoteModel.getState()))
		{
			getCommerceQuoteService().createQuoteSnapshotWithState(quoteModel, QuoteState.BUYER_OFFER);
		}
	}

	protected QuoteService getQuoteService()
	{
		return quoteService;
	}

	@Required
	public void setQuoteService(final QuoteService quoteService)
	{
		this.quoteService = quoteService;
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
