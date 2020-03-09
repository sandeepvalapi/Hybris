/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.event;

import de.hybris.platform.commerceservices.event.QuoteSalesRepSubmitEvent;
import de.hybris.platform.commerceservices.model.process.QuoteProcessModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import com.hybris.training.core.constants.TrainingCoreConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Event Listener for {@link QuoteSalesRepSubmitEvent}, which is used to trigger quote sales representative business
 * process.
 */
public class QuoteSalesRepSubmitEventListener extends AbstractEventListener<QuoteSalesRepSubmitEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;
	private static final Logger LOG = Logger.getLogger(QuoteSalesRepSubmitEventListener.class);

	@Override
	protected void onEvent(final QuoteSalesRepSubmitEvent event)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Received QuoteSalesRepSubmitEvent..");
		}

		final QuoteProcessModel quoteSalesRepProcessModel = (QuoteProcessModel) getBusinessProcessService().createProcess(
				"quoteSalesRepProcess" + "-" + event.getQuote().getCode() + "-" + event.getQuote().getStore().getUid() + "-"
						+ System.currentTimeMillis(), TrainingCoreConstants.QUOTE_SALES_REP_PROCESS);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Created business process for QuoteSalesRepSubmitEvent. Process code : [%s] ...",
					quoteSalesRepProcessModel.getCode()));
		}

		final QuoteModel quoteModel = event.getQuote();
		quoteSalesRepProcessModel.setQuoteCode(quoteModel.getCode());
		getModelService().save(quoteSalesRepProcessModel);
		//start the business process
		getBusinessProcessService().startProcess(quoteSalesRepProcessModel);

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
