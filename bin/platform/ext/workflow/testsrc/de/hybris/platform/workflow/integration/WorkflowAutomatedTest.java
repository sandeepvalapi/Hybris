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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.exceptions.AutomatedWorkflowActionException;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Collection;

import org.junit.Test;


/**
 * Tests of the automated action and activation script for Workflow-Extension.
 * 
 * 
 */
@DemoTest
public class WorkflowAutomatedTest extends WorkflowTest
{

	@Test
	@Override
	public void testTemplateActionsSize()
	{
		final Collection<WorkflowActionTemplateModel> actionTemplates = testTemplate.getActions();
		assertEquals("Expected number of action templates", 3, actionTemplates.size());
	}

	@Test
	@Override
	public void testActionsSize()
	{
		final Collection<WorkflowActionModel> actions = testWorkflow.getActions();
		assertEquals("Expected number of actions", 3, actions.size());
	}

	@Test
	public void testAutomatedWorkflowAction()
	{
		final WorkflowActionModel action1 = getAction(ACTIONCODES.ACTION1.name());
		final WorkflowActionModel action2 = getAction(ACTIONCODES.ACTION2.name());
		final WorkflowActionModel action3 = getAction(ACTIONCODES.ACTION3.name());

		assertTrue("Action 1 should be active", workflowActionService.isActive(action1));
		assertFalse("Action 2 should be inactive", workflowActionService.isActive(action2));
		assertFalse("Action 3 should be inactive", workflowActionService.isActive(action3));

		// complete action 1 with decision 1
		final WorkflowDecisionModel decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		workflowProcessingService.decideAction(action1, decision1);

		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
		assertTrue("Action 3 should be ended by workflow", workflowActionService.isEndedByWorkflow(action3));
		assertTrue("Workflow should be finished", workflowService.isFinished(testWorkflow));
	}


	@Test(expected = AutomatedWorkflowActionException.class)
	public void testAutomatedWorkflowActionException()
	{
		final UserModel testUser = createUser("TestUser2");

		userService.setCurrentUser(testUser);

		final WorkflowTemplateModel template = createWorkflowTemplate(testUser, "Test Template 2", "Test Template Descr");
		final WorkflowActionTemplateModel templateAction4 = createWorkflowActionTemplateModel(testUser, ACTIONCODES.ACTION4.name(),
				WorkflowActionType.START, template);
		final WorkflowActionTemplateModel templateAction5 = createAutomatedWorkflowActionTemplateModel(testUser,
				ACTIONCODES.ACTION5.name(), WorkflowActionType.NORMAL, template, WorkflowAutomatedTestAction.class);
		final WorkflowActionTemplateModel templateAction6 = createWorkflowActionTemplateModel(testUser, ACTIONCODES.ACTION6.name(),
				WorkflowActionType.END, template);

		final WorkflowDecisionTemplateModel templateDecision3 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION3.name(),
				templateAction4);
		final WorkflowDecisionTemplateModel templateDecision4 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION4.name(),
				templateAction5);

		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision3, templateAction5, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision4, templateAction6, Boolean.FALSE);

		testWorkflow = workflowService.createWorkflow(template, testUser);
		workflowProcessingService.toggleActions(testWorkflow);
		assertNotNull("Workflow not null", testWorkflow);
		workflowProcessingService.startWorkflow(testWorkflow);

		final WorkflowActionModel action4 = getAction(ACTIONCODES.ACTION4.name());
		assertTrue("Action 1 should be active", workflowActionService.isActive(action4));

		// complete action 4 with decision 3
		final WorkflowDecisionModel decision3 = getDecision(DECISIONCODES.DECISION3.name(), action4);
		action4.setSelectedDecision(decision3);
		workflowProcessingService.decideAction(action4, decision3);
	}

	/**
	 * Creates a workflow template with given user assigned.
	 * 
	 * @param user
	 *           user instance to use for template
	 * @return new created template instance
	 */
	@Override
	protected WorkflowTemplateModel createWorkflowTemplate(final UserModel user)
	{
		final WorkflowTemplateModel template = createWorkflowTemplate(user, "Test Template", "Test Template Descr");
		final WorkflowActionTemplateModel templateAction1 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION1.name(),
				WorkflowActionType.START, template);
		final WorkflowActionTemplateModel templateAction2 = createAutomatedWorkflowActionTemplateModel(user, ACTIONCODES.ACTION2
				.name(), WorkflowActionType.NORMAL, template, WorkflowAutomatedAction.class);
		final WorkflowActionTemplateModel templateAction3 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION3.name(),
				WorkflowActionType.END, template);

		final WorkflowDecisionTemplateModel templateDecision1 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION1.name(),
				templateAction1);
		final WorkflowDecisionTemplateModel templateDecision2 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION2.name(),
				templateAction2);

		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision1, templateAction2, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision2, templateAction3, Boolean.FALSE);

		return template;
	}


	public class WorkflowAutomatedTestAction implements AutomatedWorkflowTemplateJob
	{
		@Override
		public WorkflowDecisionModel perform(final WorkflowActionModel action)
		{
			for (final WorkflowDecisionModel decision : action.getDecisions())
			{
				return decision;
			}
			return null;
		}

	}

}
