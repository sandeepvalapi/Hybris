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
package de.hybris.platform.workflow.jalo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.user.User;

import java.util.Collection;

import org.junit.Test;


/**
 * Iteration tests for for Workflow-Extension.
 * 
 * 
 */
@IntegrationTest
public class WorkflowIterationTest extends WorkflowTest
{
	@Test
	@Override
	public void testTemplateActionsSize()
	{
		final Collection<WorkflowActionTemplate> actionTemplates = testTemplate.getActions();
		assertEquals("Expected number of action templates", 5, actionTemplates.size());
	}

	@Test
	@Override
	public void testActionsSize()
	{
		final Collection<WorkflowAction> actions = testWorkflow.getActions();
		assertEquals("Expected number of actions", 5, actions.size());
	}

	@Test
	public void testWorkflowIteration()
	{
		final WorkflowAction action1 = getAction(ACTIONCODES.ACTION1.name());
		final WorkflowAction action2 = getAction(ACTIONCODES.ACTION2.name());
		final WorkflowAction action3 = getAction(ACTIONCODES.ACTION3.name());
		final WorkflowAction action4 = getAction(ACTIONCODES.ACTION4.name());
		final WorkflowAction action5 = getAction(ACTIONCODES.ACTION5.name());

		assertTrue("Action 1 should be active", action1.isActive());
		assertFalse("Action 2 should be inactive", action2.isActive());
		assertFalse("Action 3 should be inactive", action3.isActive());
		assertFalse("Action 4 should be inactive", action4.isActive());
		assertFalse("Action 5 should be inactive", action5.isActive());

		// complete action 1 with decision 1
		final WorkflowDecision decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		action1.setSelectedDecision(decision1);
		action1.decide();
		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be active", action2.isActive());
		assertFalse("Action 3 should be inactive", action3.isActive());
		assertFalse("Action 4 should be inactive", action4.isActive());
		assertFalse("Action 5 should be inactive", action5.isActive());

		// complete action 2 with decision 2
		final WorkflowDecision decision2 = getDecision(DECISIONCODES.DECISION2.name(), action2);
		action2.setSelectedDecision(decision2);
		action2.decide();
		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be completed", action2.isCompleted());
		assertTrue("Action 3 should be active", action3.isActive());
		assertFalse("Action 4 should be inactive", action4.isActive());
		assertFalse("Action 5 should be inactive", action5.isActive());

		// complete action 3 with decision 3
		final WorkflowDecision decision3 = getDecision(DECISIONCODES.DECISION3.name(), action3);
		action3.setSelectedDecision(decision3);
		action3.decide();
		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be completed", action2.isCompleted());
		assertTrue("Action 3 should be completed", action3.isCompleted());
		assertTrue("Action 4 should be active", action4.isActive());
		assertFalse("Action 5 should be inactive", action5.isActive());

		// complete action 4 with decision 4
		final WorkflowDecision decision4 = getDecision(DECISIONCODES.DECISION4.name(), action4);
		action4.setSelectedDecision(decision4);
		action4.decide();
		assertTrue("Action 1 should be active", action1.isActive());
		assertTrue("Action 2 should be completed", action2.isCompleted());
		assertTrue("Action 3 should be completed", action3.isCompleted());
		assertTrue("Action 4 should be completed", action4.isCompleted());
		assertFalse("Action 5 should be inactive", action5.isActive());


		// iterate 2 times
		for (int i = 0; i < 2; i++)
		{
			// complete action 1 with decision 1
			action1.setSelectedDecision(decision1);
			action1.decide();
			assertTrue("Action 1 should be completed", action1.isCompleted());
			assertTrue("Action 2 should be active", action2.isActive());
			assertTrue("Action 3 should be completed", action3.isCompleted());
			assertTrue("Action 4 should be completed", action4.isCompleted());
			assertFalse("Action 5 should be inactive", action5.isActive());

			// complete action 2 with decision 2
			action2.setSelectedDecision(decision2);
			action2.decide();
			assertTrue("Action 1 should be completed", action1.isCompleted());
			assertTrue("Action 2 should be completed", action2.isCompleted());
			assertTrue("Action 3 should be active", action3.isActive());
			assertTrue("Action 4 should be completed", action4.isCompleted());
			assertFalse("Action 5 should be inactive", action5.isActive());

			// complete action 3 with decision 3
			action3.setSelectedDecision(decision3);
			action3.decide();
			assertTrue("Action 1 should be completed", action1.isCompleted());
			assertTrue("Action 2 should be completed", action2.isCompleted());
			assertTrue("Action 3 should be completed", action3.isCompleted());
			assertTrue("Action 4 should be active", action4.isActive());
			assertFalse("Action 5 should be inactive", action5.isActive());

			// complete action 4 with decision 4
			action4.setSelectedDecision(decision4);
			action4.decide();
			assertTrue("Action 1 should be active", action1.isActive());
			assertTrue("Action 2 should be completed", action2.isCompleted());
			assertTrue("Action 3 should be completed", action3.isCompleted());
			assertTrue("Action 4 should be completed", action4.isCompleted());
			assertFalse("Action 5 should be inactive", action5.isActive());
		}

		// complete action 1 with decision 1
		action1.setSelectedDecision(decision1);
		action1.decide();
		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be active", action2.isActive());
		assertTrue("Action 3 should be completed", action3.isCompleted());
		assertTrue("Action 4 should be completed", action4.isCompleted());
		assertFalse("Action 5 should be inactive", action5.isActive());

		// complete action 2 with decision 5
		final WorkflowDecision decision5 = getDecision(DECISIONCODES.DECISION5.name(), action2);
		action2.setSelectedDecision(decision5);
		action2.decide();
		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be completed", action2.isCompleted());
		assertTrue("Action 3 should be completed", action3.isCompleted());
		assertTrue("Action 4 should be completed", action4.isCompleted());
		assertTrue("Action 5 should be ended by workflow", action5.isEndedByWorkflow());
		assertTrue("Workflow should be finished", testWorkflow.isFinished());
	}

	/**
	 * Creates a workflow template with given user assigned.
	 * 
	 * @param user
	 *           user instance to use for template
	 * @return new created template instance
	 */
	@Override
	protected WorkflowTemplate createWorkflowTemplate(final User user)
	{
		final WorkflowTemplate template = createWorkflowTemplate(user, "Test Template", "Test Template Descr");
		final WorkflowActionTemplate templateAction1 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION1.name(),
				WorkflowAction.getStartActionType(), template);
		final WorkflowActionTemplate templateAction2 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION2.name(),
				WorkflowAction.getNormalActionType(), template);
		final WorkflowActionTemplate templateAction3 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION3.name(),
				WorkflowAction.getNormalActionType(), template);
		final WorkflowActionTemplate templateAction4 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION4.name(),
				WorkflowAction.getNormalActionType(), template);
		final WorkflowActionTemplate templateAction5 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION5.name(),
				WorkflowAction.getEndActionType(), template);

		final WorkflowDecisionTemplate templateDecision1 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION1.name(),
				templateAction1);
		final WorkflowDecisionTemplate templateDecision2 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION2.name(),
				templateAction2);
		final WorkflowDecisionTemplate templateDecision3 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION3.name(),
				templateAction3);
		final WorkflowDecisionTemplate templateDecision4 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION4.name(),
				templateAction4);
		final WorkflowDecisionTemplate templateDecision5 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION5.name(),
				templateAction2);

		createWorkflowActionTemplateLinkTemplateRelation(templateDecision1, templateAction2, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision2, templateAction3, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision3, templateAction4, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision4, templateAction1, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision5, templateAction5, Boolean.FALSE);

		return template;
	}
}
