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
package de.hybris.platform.workflow.integration;


import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import org.apache.log4j.Logger;


public class WorkflowAutomatedAction implements AutomatedWorkflowTemplateJob
{
	private static final Logger LOG = Logger.getLogger(WorkflowAutomatedAction.class.getName());

	@Override
	public WorkflowDecisionModel perform(final WorkflowActionModel action)
	{
		LOG.info("This will complete the action automatically without any user interaction");

		for (final WorkflowDecisionModel decision : action.getDecisions())
		{
			return decision;
		}
		return null;
	}

}
