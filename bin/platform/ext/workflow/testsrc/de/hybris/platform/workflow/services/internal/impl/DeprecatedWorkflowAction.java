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
package de.hybris.platform.workflow.services.internal.impl;

import de.hybris.platform.workflow.jalo.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.jalo.WorkflowAction;
import de.hybris.platform.workflow.jalo.WorkflowDecision;


/**
 * Testing class for {@link DefaultAutomatedWorkflowTemplateRegistryTest} test case
 */
public class DeprecatedWorkflowAction implements AutomatedWorkflowTemplateJob
{

	@Override
	public WorkflowDecision perform(final WorkflowAction action)
	{
		return null;
	}

}
