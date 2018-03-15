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
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Collection;

import org.junit.Test;


/**
 * Iteration tests for for Workflow-Extension.
 * 
 * 
 */
@DemoTest
public class WorkflowIterationTest extends WorkflowTest
{
	@Test
	@Override
	public void testTemplateActionsSize()
	{
		final Collection<WorkflowActionTemplateModel> actionTemplates = testTemplate.getActions();
		assertEquals("Expected number of action templates", 5, actionTemplates.size());
	}

	@Test
	@Override
	public void testActionsSize()
	{
		final Collection<WorkflowActionModel> actions = testWorkflow.getActions();
		assertEquals("Expected number of actions", 5, actions.size());
	}

	@Test
	public void testWorkflowIteration()
	{
		final WorkflowActionModel action1 = getAction(ACTIONCODES.ACTION1.name());
		final WorkflowActionModel action2 = getAction(ACTIONCODES.ACTION2.name());
		final WorkflowActionModel action3 = getAction(ACTIONCODES.ACTION3.name());
		final WorkflowActionModel action4 = getAction(ACTIONCODES.ACTION4.name());
		final WorkflowActionModel action5 = getAction(ACTIONCODES.ACTION5.name());

		assertTrue("Action 1 should be active", workflowActionService.isActive(action1));
		assertFalse("Action 2 should be inactive", workflowActionService.isActive(action2));
		assertFalse("Action 3 should be inactive", workflowActionService.isActive(action3));
		assertFalse("Action 4 should be inactive", workflowActionService.isActive(action4));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));

		// complete action 1 with decision 1
		final WorkflowDecisionModel decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		action1.setSelectedDecision(decision1);

		workflowProcessingService.decideAction(action1, decision1);

		modelService.refresh(action2);
		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		modelService.refresh(action2);
		assertTrue("Action 2 should be active", workflowActionService.isActive(action2));
		assertFalse("Action 3 should be inactive", workflowActionService.isActive(action3));
		assertFalse("Action 4 should be inactive", workflowActionService.isActive(action4));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));

		// complete action 2 with decision 2
		final WorkflowDecisionModel decision2 = getDecision(DECISIONCODES.DECISION2.name(), action2);
		action2.setSelectedDecision(decision2);

		workflowProcessingService.decideAction(action2, decision2);

		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
		assertTrue("Action 3 should be active", workflowActionService.isActive(action3));
		assertFalse("Action 4 should be inactive", workflowActionService.isActive(action4));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));

		// complete action 3 with decision 3
		final WorkflowDecisionModel decision3 = getDecision(DECISIONCODES.DECISION3.name(), action3);
		action3.setSelectedDecision(decision3);
		workflowProcessingService.decideAction(action3, decision3);
		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
		assertTrue("Action 3 should be completed", workflowActionService.isCompleted(action3));
		assertTrue("Action 4 should be active", workflowActionService.isActive(action4));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));

		// complete action 4 with decision 4
		final WorkflowDecisionModel decision4 = getDecision(DECISIONCODES.DECISION4.name(), action4);
		action4.setSelectedDecision(decision4);
		workflowProcessingService.decideAction(action4, decision4);
		assertTrue("Action 1 should be active", workflowActionService.isActive(action1));
		assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
		assertTrue("Action 3 should be completed", workflowActionService.isCompleted(action3));
		assertTrue("Action 4 should be completed", workflowActionService.isCompleted(action4));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));


		// iterate 2 times
		for (int i = 0; i < 2; i++)
		{
			// complete action 1 with decision 1
			action1.setSelectedDecision(decision1);
			workflowProcessingService.decideAction(action1, decision1);
			assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
			assertTrue("Action 2 should be active", workflowActionService.isActive(action2));
			assertTrue("Action 3 should be completed", workflowActionService.isCompleted(action3));
			assertTrue("Action 4 should be completed", workflowActionService.isCompleted(action4));
			assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));

			// complete action 2 with decision 2
			action2.setSelectedDecision(decision2);
			workflowProcessingService.decideAction(action2, decision2);
			assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
			assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
			assertTrue("Action 3 should be active", workflowActionService.isActive(action3));
			assertTrue("Action 4 should be completed", workflowActionService.isCompleted(action4));
			assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));

			// complete action 3 with decision 3
			action3.setSelectedDecision(decision3);
			workflowProcessingService.decideAction(action3, decision3);
			assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
			assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
			assertTrue("Action 3 should be completed", workflowActionService.isCompleted(action3));
			assertTrue("Action 4 should be active", workflowActionService.isActive(action4));
			assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));

			// complete action 4 with decision 4
			action4.setSelectedDecision(decision4);
			workflowProcessingService.decideAction(action4, decision4);
			assertTrue("Action 1 should be active", workflowActionService.isActive(action1));
			assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
			assertTrue("Action 3 should be completed", workflowActionService.isCompleted(action3));
			assertTrue("Action 4 should be completed", workflowActionService.isCompleted(action4));
			assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));
		}

		// complete action 1 with decision 1
		action1.setSelectedDecision(decision1);
		workflowProcessingService.decideAction(action1, decision1);
		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertTrue("Action 2 should be active", workflowActionService.isActive(action2));
		assertTrue("Action 3 should be completed", workflowActionService.isCompleted(action3));
		assertTrue("Action 4 should be completed", workflowActionService.isCompleted(action4));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));

		// complete action 2 with decision 5
		final WorkflowDecisionModel decision5 = getDecision(DECISIONCODES.DECISION5.name(), action2);
		action2.setSelectedDecision(decision5);
		workflowProcessingService.decideAction(action2, decision5);
		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
		assertTrue("Action 3 should be completed", workflowActionService.isCompleted(action3));
		assertTrue("Action 4 should be completed", workflowActionService.isCompleted(action4));
		assertTrue("Action 5 should be ended by workflow", workflowActionService.isEndedByWorkflow(action5));
		assertTrue("Workflow should be finished", workflowService.isFinished(testWorkflow));
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
		final WorkflowActionTemplateModel templateAction2 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION2.name(),
				WorkflowActionType.NORMAL, template);
		final WorkflowActionTemplateModel templateAction3 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION3.name(),
				WorkflowActionType.NORMAL, template);
		final WorkflowActionTemplateModel templateAction4 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION4.name(),
				WorkflowActionType.NORMAL, template);
		final WorkflowActionTemplateModel templateAction5 = createWorkflowActionTemplateModel(user, ACTIONCODES.ACTION5.name(),
				WorkflowActionType.END, template);

		final WorkflowDecisionTemplateModel templateDecision1 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION1.name(),
				templateAction1);
		final WorkflowDecisionTemplateModel templateDecision2 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION2.name(),
				templateAction2);
		final WorkflowDecisionTemplateModel templateDecision3 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION3.name(),
				templateAction3);
		final WorkflowDecisionTemplateModel templateDecision4 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION4.name(),
				templateAction4);
		final WorkflowDecisionTemplateModel templateDecision5 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION5.name(),
				templateAction2);

		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision1, templateAction2, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision2, templateAction3, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision3, templateAction4, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision4, templateAction1, Boolean.FALSE);
		createWorkflowActionTemplateModelLinkTemplateRelation(templateDecision5, templateAction5, Boolean.FALSE);

		return template;
	}
}
