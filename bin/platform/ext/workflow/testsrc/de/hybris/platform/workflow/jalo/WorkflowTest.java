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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


/**
 * Test for extension Workflow.
 */
@IntegrationTest
public class WorkflowTest extends HybrisJUnit4TransactionalTest
{
	/**
	 * Reference to the manager instance.
	 */
	protected WorkflowManager manager;

	/**
	 * Template instance created at each set up.
	 */
	protected WorkflowTemplate testTemplate = null;

	/**
	 * workflow instance created at each set up.
	 */
	protected Workflow testWorkflow = null;

	protected static enum ACTIONCODES
	{
		ACTION1, ACTION2, ACTION3, ACTION4, ACTION5, ACTION6
	}

	protected static enum DECISIONCODES
	{
		DECISION1, DECISION2, DECISION3, DECISION4, DECISION5, DECISION6, DECISION7
	}


	@Before
	public void setUp() throws Exception
	{
		manager = WorkflowManager.getInstance();
		final User testUser = createUser("TestUser");
		testTemplate = createWorkflowTemplate(testUser);

		jaloSession.setUser(testUser);

		testWorkflow = testTemplate.createWorkflow();
		assertNotNull("Workflow not null", testWorkflow);

		// start workflow
		testWorkflow.startWorkflow();
	}

	/**
	 * check actions template size
	 */
	@Test
	public void testTemplateActionsSize()
	{
		final Collection<WorkflowActionTemplate> actionTemplates = testTemplate.getActions();
		assertEquals("Expected number of action templates", 6, actionTemplates.size());
	}

	/**
	 * check actions size
	 */
	@Test
	public void testActionsSize()
	{
		final Collection<WorkflowAction> actions = testWorkflow.getActions();
		assertEquals("Expected number of actions", 6, actions.size());
	}

	/**
	 * Creates a workflow template with given user assigned.
	 * 
	 * @param user
	 *           user instance to use for template
	 * @return new created template instance
	 */
	protected WorkflowTemplate createWorkflowTemplate(final User user)
	{
		final WorkflowTemplate template = createWorkflowTemplate(user, "Test Template", "Test Template Descr");
		final WorkflowActionTemplate templateAction1 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION1.name(),
				WorkflowAction.getStartActionType(), template);
		final WorkflowActionTemplate templateAction2 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION2.name(),
				WorkflowAction.getStartActionType(), template);
		final WorkflowActionTemplate templateAction3 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION3.name(),
				WorkflowAction.getNormalActionType(), template);
		final WorkflowActionTemplate templateAction4 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION4.name(),
				WorkflowAction.getNormalActionType(), template);
		final WorkflowActionTemplate templateAction5 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION5.name(),
				WorkflowAction.getNormalActionType(), template);
		final WorkflowActionTemplate templateAction6 = createWorkflowActionTemplate(user, ACTIONCODES.ACTION6.name(),
				WorkflowAction.getEndActionType(), template);

		final WorkflowDecisionTemplate templateDecision1 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION1.name(),
				templateAction1);
		final WorkflowDecisionTemplate templateDecision2 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION2.name(),
				templateAction1);
		final WorkflowDecisionTemplate templateDecision3 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION3.name(),
				templateAction2);
		final WorkflowDecisionTemplate templateDecision4 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION4.name(),
				templateAction2);
		final WorkflowDecisionTemplate templateDecision5 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION5.name(),
				templateAction3);
		final WorkflowDecisionTemplate templateDecision6 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION6.name(),
				templateAction4);
		final WorkflowDecisionTemplate templateDecision7 = createWorkflowDecisionTemplate(DECISIONCODES.DECISION7.name(),
				templateAction5);

		createWorkflowActionTemplateLinkTemplateRelation(templateDecision1, templateAction3, Boolean.TRUE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision2, templateAction4, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision3, templateAction3, Boolean.TRUE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision4, templateAction4, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision4, templateAction5, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision5, templateAction6, Boolean.FALSE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision6, templateAction6, Boolean.TRUE);
		createWorkflowActionTemplateLinkTemplateRelation(templateDecision7, templateAction6, Boolean.TRUE);

		return template;
	}

	/**
	 * Creates a user instance using given uid.
	 * 
	 * @param userName
	 *           user id used for user creation
	 * @return created user
	 */
	protected User createUser(final String userName)
	{
		User user = null;
		try
		{
			user = UserManager.getInstance().createCustomer(userName);
			final UserRight readRight = AccessManager.getInstance().getOrCreateUserRightByCode(AccessManager.READ);
			assertNotNull("UserRight should not be null", readRight);
			TypeManager.getInstance().getComposedType(WorkflowAction.class).addPositivePermission(user, readRight);
			assertNotNull("User should not be null", user);
		}
		catch (final ConsistencyCheckException e)
		{
			fail("Can not create user caused by: " + e.getMessage());
		}
		return user;
	}

	/**
	 * Creates new workflow template using given user, code, and description.
	 * 
	 * @param owner
	 *           user assigned to new template
	 * @param code
	 *           code of new template
	 * @param desc
	 *           description assigned to template
	 * @return created template
	 */
	protected WorkflowTemplate createWorkflowTemplate(final User owner, final String code, final String desc)
	{
		return createWorkflowTemplate(owner, code, desc, null);
	}

	/**
	 * Creates new workflow template using given user, code, description and activation script.
	 * 
	 * @param owner
	 *           user assigned to new template
	 * @param code
	 *           code of new template
	 * @param desc
	 *           description assigned to template
	 * @param activationScript
	 *           code of the activation script
	 * @return created template
	 */
	protected WorkflowTemplate createWorkflowTemplate(final User owner, final String code, final String desc,
			final String activationScript)
	{
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put(WorkflowTemplate.OWNER, owner);
		values.put(WorkflowTemplate.CODE, code);
		values.put(WorkflowTemplate.DESCRIPTION, desc);
		values.put(WorkflowTemplate.ACTIVATIONSCRIPT, activationScript);
		final WorkflowTemplate template = manager.createWorkflowTemplate(values);
		assertNotNull("Template should not be null", template);
		return template;
	}

	/**
	 * Creates new action template.
	 * 
	 * @param user
	 *           user assigned to template
	 * @param code
	 *           code of template
	 * @param workflow
	 *           workflow assigned to action template
	 * @return created action template
	 */
	protected WorkflowActionTemplate createWorkflowActionTemplate(final User user, final String code,
			final EnumerationValue actionType, final WorkflowTemplate workflow)
	{
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put(WorkflowActionTemplate.PRINCIPALASSIGNED, user);
		values.put(WorkflowActionTemplate.WORKFLOW, workflow);
		values.put(WorkflowActionTemplate.CODE, code);
		values.put(WorkflowActionTemplate.SENDEMAIL, Boolean.FALSE);
		values.put(WorkflowActionTemplate.ACTIONTYPE, actionType);
		final WorkflowActionTemplate action = manager.createWorkflowActionTemplate(values);
		assertNotNull("Action Template should not be null", action);
		return action;
	}

	/**
	 * Creates new automated action template.
	 * 
	 * @param user
	 *           user assigned to template
	 * @param code
	 *           code of template
	 * @param workflow
	 *           workflow assigned to action template
	 * @return created action template
	 */
	protected AutomatedWorkflowActionTemplate createAutomatedWorkflowActionTemplate(final User user, final String code,
			final EnumerationValue actionType, final WorkflowTemplate workflow, final Class jobClass)
	{
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put(AutomatedWorkflowActionTemplate.PRINCIPALASSIGNED, user);
		values.put(AutomatedWorkflowActionTemplate.WORKFLOW, workflow);
		values.put(AutomatedWorkflowActionTemplate.CODE, code);
		values.put(AutomatedWorkflowActionTemplate.SENDEMAIL, Boolean.FALSE);
		values.put(AutomatedWorkflowActionTemplate.ACTIONTYPE, actionType);
		values.put(AutomatedWorkflowActionTemplate.JOBCLASS, jobClass);
		final AutomatedWorkflowActionTemplate action = manager.createAutomatedWorkflowActionTemplate(values);
		assertNotNull("Automated Action Template should not be null", action);
		return action;
	}

	/**
	 * Creates new decision template.
	 * 
	 * @param code
	 *           code of template
	 * @param actionTemplate
	 *           action template assigned to decision template
	 * @return created decision template
	 */
	protected WorkflowDecisionTemplate createWorkflowDecisionTemplate(final String code,
			final WorkflowActionTemplate actionTemplate)
	{
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put(WorkflowDecisionTemplate.CODE, code);
		values.put(WorkflowDecisionTemplate.ACTIONTEMPLATE, actionTemplate);
		final WorkflowDecisionTemplate decision = manager.createWorkflowDecisionTemplate(values);
		assertNotNull("The decision should not be null", decision);
		return decision;
	}

	/**
	 * 
	 * @param decisionTemplate
	 * @param toAction
	 * @param hasAndConnection
	 */
	protected void createWorkflowActionTemplateLinkTemplateRelation(final WorkflowDecisionTemplate decisionTemplate,
			final WorkflowActionTemplate toAction, final Boolean hasAndConnection)
	{
		decisionTemplate.addToToTemplateActions(toAction);
		final Collection<Link> incomingLinkList = manager.getLinkTemplates(decisionTemplate, toAction);

		for (final Link link : incomingLinkList)
		{
			manager.setAndConnectionTemplate(link, hasAndConnection);
		}

	}

	/**
	 * Gets the action with given code from test workflow instance.
	 * 
	 * @param code
	 *           code of needed action
	 * @return action of test workflow with given code
	 */
	protected WorkflowAction getAction(final String code)
	{
		final Collection<WorkflowAction> actions = testWorkflow.getActions();
		for (final WorkflowAction action : actions)
		{
			if (action.getTemplate().getCode().equals(code))
			{
				return action;
			}
		}
		fail("Action " + code + "can not be found");
		return null;
	}

	/**
	 * Gets the decision with given code from test workflow instance.
	 * 
	 * @param code
	 *           code of needed decision
	 * @return decision of test workflow with given code
	 */
	protected WorkflowDecision getDecision(final String code, final WorkflowAction action)
	{
		final Collection<WorkflowDecision> decisions = action.getDecisions();
		for (final WorkflowDecision decision : decisions)
		{
			if (decision.getCode().equals(code))
			{
				return decision;
			}
		}
		fail("Decision " + code + "can not be found");
		return null;
	}

	/**
	 * Gets the action template with given code from test workflow template.
	 * 
	 * @param code
	 *           code of needed action
	 * @return action template of test workflow template with given code
	 */
	protected WorkflowActionTemplate getActionTemplate(final String code)
	{
		final Collection<WorkflowActionTemplate> actions = testTemplate.getActions();
		for (final WorkflowActionTemplate action : actions)
		{
			if (action.getCode().equals(code))
			{
				return action;
			}
		}
		fail("ActionTemplate " + code + "can not be found");
		return null;
	}
}
