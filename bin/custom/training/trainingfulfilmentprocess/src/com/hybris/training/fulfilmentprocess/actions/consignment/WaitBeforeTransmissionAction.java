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
