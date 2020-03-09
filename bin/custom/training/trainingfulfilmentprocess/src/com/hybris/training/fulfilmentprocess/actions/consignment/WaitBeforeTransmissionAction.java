/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.actions.consignment;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;


public class WaitBeforeTransmissionAction extends AbstractSimpleDecisionAction<ConsignmentProcessModel>
{
	@Override
	public Transition executeAction(final ConsignmentProcessModel process)
	{
		// If you return NOK this action will be called again.
		// You might want to do this when you want to poll a resource to be ready.
		return Transition.OK;
	}
}
