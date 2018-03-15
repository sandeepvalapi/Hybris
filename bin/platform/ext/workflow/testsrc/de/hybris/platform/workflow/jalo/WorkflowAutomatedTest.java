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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.xml.AbstractValueObject;
import de.hybris.platform.jalo.user.User;

import java.util.Collection;

import org.junit.Test;


/**
 * Tests of the automated action and activation script for Workflow-Extension.
 * 
 * 
 */
@IntegrationTest
public class WorkflowAutomatedTest extends WorkflowTest
{

	@Test
	@Override
	public void testTemplateActionsSize()
	{
		final Collection<WorkflowActionTemplate> actionTemplates = testTemplate.getActions();
		assertEquals("Expected number of action templates", 3, actionTemplates.size());
	}

	@Test
	@Override
	public void testActionsSize()
	{
		final Collection<WorkflowAction> actions = testWorkflow.getActions();
		assertEquals("Expected number of actions", 3, actions.size());
	}

	@Test
	public void testAutomatedWorkflowAction()
	{
		final WorkflowAction action1 = getAction(ACTIONCODES.ACTION1.name());
		final WorkflowAction action2 = getAction(ACTIONCODES.ACTION2.name());
		final WorkflowAction action3 = getAction(ACTIONCODES.ACTION3.name());

		assertTrue("Action 1 should be active", action1.isActive());
		assertFalse("Action 2 should be inactive", action2.isActive());
		assertFalse("Action 3 should be inactive", action3.isActive());

		// complete action 1 with decision 1
		final WorkflowDecision decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		action1.setSelectedDecision(decision1);
		action1.decide();

		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be completed", action2.isCompleted());
		assertTrue("Action 3 should be ended by workflow", action3.isEndedByWorkflow());
		assertTrue("Workflow should be finished", testWorkflow.isFinished());
	}


	@Test(expected = AutomatedWorkflowActionException.class)
	public void testAutomatedWorkflowActionException()
	{
		final User testUser = createUser("TestUser2");

		jaloSession.setUser(testUser);

		final WorkflowTemplate template = createWorkflowTemplate(testUser, "Test Template 2", "Test Template Descr");
		final WorkflowActionTemplate templateAction4 = createWorkflowActionTemplate(testUser, ACTIONCODES.ACTION4.name(),
				WorkflowAction.getStartActionType(), template);
		final WorkflowActionTemplate templateAction5 = createAutomatedWorkflowActionTemplate(testUser, ACTIONCODES.ACTION5.name(),
				WorkflowAction.getNormalActionType(), template, WorkflowAutomatedTestAction.class);
		final WorkflowActionTemplate templateAction6 = createWorkflowActionTemplate(testUser, ACTIONCODES.ACTION6.name(),
				WorkflowAction.getEndActionType(), template);

		final WorkflowDecisionTemplate templateDecision3 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION3.name(),
				templateAction4);
		final WorkflowDecisionTemplate templateDecision4 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION4.name(),
				templateAction5);

		createWorkflowActionTemplateLinkTemplateRelation(templateDecision3, templateAction5, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision4, templateAction6, Boolean.FALSE);

		testWorkflow = template.createWorkflow();
		assertNotNull("Workflow not null", testWorkflow);
		testWorkflow.startWorkflow();

		final WorkflowAction action4 = getAction(ACTIONCODES.ACTION4.name());
		assertTrue("Action 1 should be active", action4.isActive());

		// complete action 4 with decision 3
		final WorkflowDecision decision3 = getDecision(DECISIONCODES.DECISION3.name(), action4);
		action4.setSelectedDecision(decision3);
		action4.decide();
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
		final WorkflowActionTemplate templateAction2 = createAutomatedWorkflowActionTemplate(user, ACTIONCODES.ACTION2.name(),
				WorkflowAction.getNormalActionType(), template, WorkflowAutomatedAction.class);
		final WorkflowActionTemplate templateAction3 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION3.name(),
				WorkflowAction.getEndActionType(), template);

		final WorkflowDecisionTemplate templateDecision1 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION1.name(),
				templateAction1);
		final WorkflowDecisionTemplate templateDecision2 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION2.name(),
				templateAction2);

		createWorkflowActionTemplateLinkTemplateRelation(templateDecision1, templateAction2, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision2, templateAction3, Boolean.FALSE);

		return template;
	}


	public class WorkflowAutomatedTestAction
	{
		public WorkflowDecision perform(final WorkflowAction action)
		{
			for (final WorkflowDecision decision : action.getDecisions())
			{
				return decision;
			}
			return null;
		}

	}


	public static class TestObject extends AbstractValueObject
	{
		private final String name;

		public TestObject(final String name)
		{
			super();
			this.name = name;
		}

		/**
		 * @return Returns the name.
		 */
		public String getName()
		{
			return name;
		}
	}
}
