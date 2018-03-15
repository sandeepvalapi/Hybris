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
package com.hybris.training.fulfilmentprocess.actions.returns;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import org.apache.log4j.Logger;


/**
 * Mock implementation for updating he inventory for the ReturnRequest
 */
public class InventoryUpdateAction extends AbstractProceduralAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(InventoryUpdateAction.class);

	@Override
	public void executeAction(final ReturnProcessModel process)
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		// Implement the update inventory behavior that you want to apply after a successful return

		final ReturnRequestModel returnRequest = process.getReturnRequest();
		returnRequest.setStatus(ReturnStatus.COMPLETED);
		returnRequest.getReturnEntries().stream().forEach(entry -> {
			entry.setStatus(ReturnStatus.COMPLETED);
			getModelService().save(entry);
		});
		getModelService().save(returnRequest);
	}
}
