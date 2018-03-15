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

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.model.ReturnProcessModel;
import org.apache.log4j.Logger;

import java.util.Set;


/**
 * Mock implementation for approving the ReturnRequest
 */
public class ApproveReturnAction extends AbstractProceduralAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(ApproveReturnAction.class);

	@Override
	public void executeAction(final ReturnProcessModel process)
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

}
