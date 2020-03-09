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
 * This class creates a new quote snapshot with status SELLER_REQUEST, if seller approver has rejected the quote.
 */
public class QuoteApprovalRejectedAction extends AbstractProceduralAction<QuoteProcessModel>
{
	private QuoteService quoteService;
	private CommerceQuoteService commerceQuoteService;
	private static final Logger LOG = Logger.getLogger(CheckSellerApproverResponseOnQuoteAction.class);

	@Override
	public void executeAction(final QuoteProcessModel process) throws RetryLaterException, Exception
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("In QuoteApprovalRejectedAction for process code : [%s]", process.getCode()));
		}

		final QuoteModel quoteModel = getQuoteService().getCurrentQuoteForCode(process.getQuoteCode());

		if (QuoteState.SELLERAPPROVER_REJECTED.equals(quoteModel.getState()))
		{
			getCommerceQuoteService().createQuoteSnapshotWithState(quoteModel, QuoteState.SELLER_REQUEST);
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
