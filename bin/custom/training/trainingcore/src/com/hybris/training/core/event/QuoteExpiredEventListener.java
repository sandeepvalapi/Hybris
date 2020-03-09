/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.event;

import static com.hybris.training.core.constants.TrainingCoreConstants.QUOTE_EXPIRED_EMAIL_PROCESS;

import de.hybris.platform.commerceservices.event.QuoteExpiredEvent;
import de.hybris.platform.commerceservices.model.process.QuoteProcessModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Event Listener for {@link QuoteExpiredEvent}. This Event Listener starts the quote expired business process.
 */
public class QuoteExpiredEventListener extends AbstractEventListener<QuoteExpiredEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;
	private static final Logger LOG = Logger.getLogger(QuoteExpiredEventListener.class);

	@Override
	protected void onEvent(final QuoteExpiredEvent event)
	{
		LOG.debug("Received QuoteExpiredEvent..");

		final QuoteProcessModel quoteProcessModel = createQuoteProcessModel(event);

		getModelService().save(quoteProcessModel);

		getBusinessProcessService().startProcess(quoteProcessModel);
	}

	protected QuoteProcessModel createQuoteProcessModel(final QuoteExpiredEvent event)
	{
		final QuoteModel quote = event.getQuote();

		final QuoteProcessModel quoteProcessModel = (QuoteProcessModel) getBusinessProcessService()
				.createProcess(String.format("quoteExpired-%s-%s-%s", quote.getCode(), quote.getStore().getUid(),
						Long.valueOf(System.currentTimeMillis())), QUOTE_EXPIRED_EMAIL_PROCESS, MapUtils.EMPTY_MAP);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Created business process for QuoteExpiredEvent. Process code : [%s] ...",
					quoteProcessModel.getCode()));
		}

		quoteProcessModel.setQuoteCode(quote.getCode());

		return quoteProcessModel;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
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
}
