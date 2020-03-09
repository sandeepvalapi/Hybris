/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.actions.quote;

import de.hybris.platform.commerceservices.model.process.QuoteProcessModel;
import de.hybris.platform.commerceservices.order.CommerceQuoteService;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.QuoteService;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This action class will be used by business process to determine, whether the quote requires manual approval from
 * seller approver. <br>
 * If manual approval is not required, a new quote snapshot with status SELLERAPPROVER_APPROVED is created. <br>
 * Otherwise, a new quote snapshot with status SELLERAPPROVER_PENDING is created.
 *
 */
public class CheckForQuoteAutoApprovalAction extends AbstractSimpleDecisionAction<QuoteProcessModel>
{
	private QuoteService quoteService;
	private CommerceQuoteService commerceQuoteService;
	private static final Logger LOG = Logger.getLogger(CheckSellerApproverResponseOnQuoteAction.class);

	@Override
	public Transition executeAction(final QuoteProcessModel process) throws RetryLaterException, Exception
	{
		Transition result;

		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("In CheckForQuoteAutoApprovalAction for process code : [%s]", process.getCode()));
		}

		final QuoteModel quoteModel = getQuoteService().getCurrentQuoteForCode(process.getQuoteCode());

		if (getCommerceQuoteService().shouldAutoApproveTheQuoteForSellerApproval(quoteModel))
		{
			getCommerceQuoteService().createQuoteSnapshotWithState(quoteModel, QuoteState.SELLERAPPROVER_APPROVED);
			result = Transition.OK;
		}
		else
		{
			getCommerceQuoteService().createQuoteSnapshotWithState(quoteModel, QuoteState.SELLERAPPROVER_PENDING);
			result = Transition.NOK;
		}

		return result;
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
