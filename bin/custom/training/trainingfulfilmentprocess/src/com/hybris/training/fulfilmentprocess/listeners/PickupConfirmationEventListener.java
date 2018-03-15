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
package com.hybris.training.fulfilmentprocess.listeners;

import de.hybris.platform.orderprocessing.events.PickupConfirmationEvent;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import com.hybris.training.fulfilmentprocess.constants.TrainingFulfilmentProcessConstants;

import org.springframework.beans.factory.annotation.Required;


/**
 * Listener for pickup confirmation events.
 */
public class PickupConfirmationEventListener extends AbstractEventListener<PickupConfirmationEvent>
{

	private BusinessProcessService businessProcessService;

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	@Override
	protected void onEvent(final PickupConfirmationEvent pickupConfirmationEvent)
	{
		final ConsignmentModel consignmentModel = pickupConfirmationEvent.getProcess().getConsignment();
		for (final ConsignmentProcessModel process : consignmentModel.getConsignmentProcesses())
		{
			getBusinessProcessService().triggerEvent(
					process.getCode() + "_" + TrainingFulfilmentProcessConstants.CONSIGNMENT_PICKUP);
		}
	}
}
