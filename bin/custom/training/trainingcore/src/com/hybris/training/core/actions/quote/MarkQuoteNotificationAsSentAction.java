/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.actions.quote;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.commerceservices.enums.QuoteNotificationType;
import de.hybris.platform.commerceservices.model.process.QuoteProcessModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.QuoteService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;


/**
 * A generic quote process action used to mark a specific notification type {set in quoteNotificationType} as generated
 * for the quote.
 */
public class MarkQuoteNotificationAsSentAction extends AbstractProceduralAction<QuoteProcessModel>
{
	private QuoteService quoteService;
	private QuoteNotificationType quoteNotificationType;

	@Override
	public void executeAction(final QuoteProcessModel quoteProcessModel) throws Exception
	{
		final QuoteModel quoteModel = getQuoteService().getCurrentQuoteForCode(quoteProcessModel.getQuoteCode());

		final Set<QuoteNotificationType> generatedQuoteNotifications = newHashSet(quoteModel.getGeneratedNotifications());
		generatedQuoteNotifications.add(getQuoteNotificationType());
		quoteModel.setGeneratedNotifications(generatedQuoteNotifications);

		getModelService().save(quoteModel);
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

	protected QuoteNotificationType getQuoteNotificationType()
	{
		return quoteNotificationType;
	}

	@Required
	public void setQuoteNotificationType(final QuoteNotificationType quoteNotificationType)
	{
		this.quoteNotificationType = quoteNotificationType;
	}
}
