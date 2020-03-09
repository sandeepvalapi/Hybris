/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.actions.returns;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import org.apache.log4j.Logger;


/**
 * Mock implementation for refunding the money to the customer for the ReturnRequest
 */
public class CaptureRefundAction extends AbstractSimpleDecisionAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CaptureRefundAction.class);

	@Override
	public Transition executeAction(final ReturnProcessModel process)
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final ReturnRequestModel returnRequest = process.getReturnRequest();

		// Implement the logic to refund the money to the customer

		final ReturnStatus returnStatus = ReturnStatus.PAYMENT_REVERSED;
		returnRequest.setStatus(returnStatus);
		returnRequest.getReturnEntries().stream().forEach(entry -> {
			entry.setStatus(returnStatus);
			getModelService().save(entry);
		});
		getModelService().save(returnRequest);

		return Transition.OK;
	}
}
