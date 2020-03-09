/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.actions.quote;

import static com.hybris.training.core.constants.TrainingCoreConstants.QUOTE_USER_TYPE;

import de.hybris.platform.commerceservices.enums.QuoteUserType;
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
 * This action class creates a sales representative snapshot.
 */
public class QuoteBuyerSubmitAction extends AbstractSimpleDecisionAction<QuoteProcessModel>
{
	private static final Logger LOG = Logger.getLogger(QuoteBuyerSubmitAction.class);
	private CommerceQuoteService commerceQuoteService;
	private QuoteService quoteService;

	@Override
	public Transition executeAction(final QuoteProcessModel process) throws RetryLaterException, Exception
	{
		Transition result;

		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("In QuoteBuyerSubmitAction for process code : [%s]", process.getCode()));
		}

		final QuoteUserType quoteUserType = (QuoteUserType) processParameterHelper
				.getProcessParameterByName(process, QUOTE_USER_TYPE).getValue();

		final QuoteModel quoteModel = getQuoteService().getCurrentQuoteForCode(process.getQuoteCode());

		if (QuoteUserType.BUYER.equals(quoteUserType))
		{
			getCommerceQuoteService().createQuoteSnapshotWithState(quoteModel, QuoteState.SELLER_REQUEST);
			result = Transition.OK;
		}
		else
		{
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
