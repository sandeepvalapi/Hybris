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
import de.hybris.platform.processengine.action.AbstractProceduralAction;


/**
 * 
 */
public class SendCancelMessageAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.processengine.action.AbstractProceduralAction#executeAction(de.hybris.platform.processengine.model.
	 * BusinessProcessModel)
	 */
	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		// empty
	}
}
