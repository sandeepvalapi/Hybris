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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


/**
 * Test for extension Workflow.
 */
@IntegrationTest
public class WorkflowCompatibleTest extends HybrisJUnit4TransactionalTest
{
	/**
	 * Reference to the manager instance.
	 */
	private WorkflowManager manager;
	/**
	 * Template instance created at each set up.
	 */
	private WorkflowTemplate testTemplate = null;
	/**
	 * workflow instance created at each set up.
	 */
	private Workflow testWorkflow = null;

	private static enum CODES
	{
		ACTION1, ACTION2, ACTION3, ACTION4, ACTION5
	}

	@Before
	public void setUp() throws Exception
	{
		manager = WorkflowManager.getInstance();
		final User testUser = createUser("TestUser");
		testTemplate = createWorkflowTemplate(testUser);

		// check actions template size
		final Collection<WorkflowActionTemplate> actionTemplates = testTemplate.getActions();
		assertEquals("Excpected number of actions", 5, actionTemplates.size());
		testWorkflow = testTemplate.createWorkflow();
		assertNotNull("Workflow not null", testWorkflow);

		// check actions size
		final Collection<WorkflowAction> actions = testWorkflow.getActions();
		assertEquals("Excpected number of actions", 5, actions.size());
	}

	/**
	 * Checks if actions of test workflow has correct status.
	 */
	@Test
	public void testWorkflowCreate()
	{
		// test action 1
		final WorkflowAction action1 = getAction(CODES.ACTION1.name());
		assertEquals(action1.getPredecessorsCount(), 0);
		assertFalse(action1.isDisabled());
		// test action 2
		final WorkflowAction action2 = getAction(CODES.ACTION2.name());
		assertEquals(action2.getPredecessorsCount(), 0);
		assertFalse(action2.isDisabled());
		// test action 3
		final WorkflowAction action3 = getAction(CODES.ACTION3.name());
		assertEquals(action3.getPredecessorsCount(), 2);
		assertFalse(action3.isDisabled());
		// test action 4
		final WorkflowAction action4 = getAction(CODES.ACTION4.name());
		assertEquals(action4.getPredecessorsCount(), 1);
		assertFalse(action4.isDisabled());
		// test action 5
		final WorkflowAction action5 = getAction(CODES.ACTION5.name());
		assertEquals(action5.getPredecessorsCount(), 1);
		assertFalse(action5.isDisabled());
	}

	/**
	 * Disable actions 1 and 2 of test workflow and checks the status of the workflow actions.
	 */
	@Test
	public void testWorkflowCreateDisableActivate()
	{
		// disable some actions
		WorkflowAction action1 = getAction(CODES.ACTION1.name());
		action1.disable();
		WorkflowAction action2 = getAction(CODES.ACTION2.name());
		action2.disable();
		testWorkflow.toggleActions();
		// test action 1
		action1 = getAction(CODES.ACTION1.name());
		assertTrue(action1.isDisabled());
		assertTrue(action1.isCompleted());
		assertTrue(action1.getStatus().equals(WorkflowAction.getDisabledStatus()));
		// test action 2
		action2 = getAction(CODES.ACTION2.name());
		assertTrue(action2.isDisabled());
		assertTrue(action2.isCompleted());
		assertTrue(action2.getStatus().equals(WorkflowAction.getDisabledStatus()));
		// test action 3
		final WorkflowAction action3 = getAction(CODES.ACTION3.name());
		assertFalse(action3.isDisabled());
		assertFalse(action3.isCompleted());
		assertTrue(action3.getStatus().equals(WorkflowAction.getActiveStatus()));
		assertNotNull(action3.getActivated());
		assertNotNull(action3.getFirstActivated());
		// test action 4
		final WorkflowAction action4 = getAction(CODES.ACTION4.name());
		assertFalse(action4.isDisabled());
		assertFalse(action4.isCompleted());
		assertTrue(action4.getStatus().equals(WorkflowAction.getIdleStatus()));
		// test action 5
		final WorkflowAction action5 = getAction(CODES.ACTION5.name());
		assertFalse(action5.isDisabled());
		assertFalse(action5.isCompleted());
		assertTrue(action5.getStatus().equals(WorkflowAction.getIdleStatus()));
		// check workflow
		assertFalse(testWorkflow.isFinished());
	}

	/**
	 * Completes the actions of the test workflow and checks the action status.
	 */
	@Test
	public void testWorkflowCompleteChain()
	{
		// disable action 1
		WorkflowAction action1 = getAction(CODES.ACTION1.name());
		action1.disable();
		testWorkflow.toggleActions();
		// test action 1
		action1 = getAction(CODES.ACTION1.name());
		assertTrue(action1.isDisabled());
		assertTrue(action1.isCompleted());
		assertTrue(action1.getStatus().equals(WorkflowAction.getDisabledStatus()));
		// test action 2
		final WorkflowAction action2 = getAction(CODES.ACTION2.name());
		assertFalse(action2.isDisabled());
		assertFalse(action2.isCompleted());
		assertEquals(WorkflowAction.getActiveStatus(), action2.getStatus());
		// test action 3
		final WorkflowAction action3 = getAction(CODES.ACTION3.name());
		assertFalse(action3.isDisabled());
		assertFalse(action3.isCompleted());
		assertEquals(WorkflowAction.getIdleStatus(), action3.getStatus());
		// test action 4
		final WorkflowAction action4 = getAction(CODES.ACTION4.name());
		assertFalse(action4.isDisabled());
		assertFalse(action4.isCompleted());
		assertEquals(WorkflowAction.getIdleStatus(), action4.getStatus());
		// test action 5
		final WorkflowAction action5 = getAction(CODES.ACTION5.name());
		assertFalse(action5.isDisabled());
		assertFalse(action5.isCompleted());
		assertEquals(WorkflowAction.getIdleStatus(), action5.getStatus());
		// complete action 2
		action2.complete();
		testWorkflow.toggleActions();
		assertEquals(WorkflowAction.getDisabledStatus(), action1.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action2.getStatus());
		assertEquals(WorkflowAction.getActiveStatus(), action3.getStatus());
		assertEquals(WorkflowAction.getIdleStatus(), action4.getStatus());
		assertEquals(WorkflowAction.getIdleStatus(), action5.getStatus());
		assertFalse(testWorkflow.isFinished());
		// complete action 3
		action3.complete();
		testWorkflow.toggleActions();
		assertEquals(WorkflowAction.getDisabledStatus(), action1.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action2.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action3.getStatus());
		assertEquals(WorkflowAction.getActiveStatus(), action4.getStatus());
		assertEquals(WorkflowAction.getActiveStatus(), action5.getStatus());
		assertFalse(testWorkflow.isFinished());
		action4.complete();
		testWorkflow.toggleActions();
		assertEquals(WorkflowAction.getDisabledStatus(), action1.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action2.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action3.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action4.getStatus());
		assertEquals(WorkflowAction.getActiveStatus(), action5.getStatus());
		assertFalse(testWorkflow.isFinished());
		// complete action 5
		action5.complete();
		testWorkflow.toggleActions();
		assertEquals(WorkflowAction.getDisabledStatus(), action1.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action2.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action3.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action4.getStatus());
		assertEquals(WorkflowAction.getCompletedStatus(), action5.getStatus());
		assertTrue(testWorkflow.isFinished());
	}

	/**
	 * Rejects action 3 of test workflow and checks action status.
	 */
	@Test
	@SuppressWarnings("deprecation")
	public void testWorkflowCompleteReject()
	{
		// disable action 1
		WorkflowAction action1 = getAction(CODES.ACTION1.name());
		action1.disable();
		testWorkflow.toggleActions();
		// check stati
		action1 = getAction(CODES.ACTION1.name());
		assertEquals(WorkflowAction.getDisabledStatus(), action1.getStatus());
		final WorkflowAction action2 = getAction(CODES.ACTION2.name());
		assertEquals(WorkflowAction.getActiveStatus(), action2.getStatus());
		assertNotNull(action2.getActivated());
		assertNotNull(action2.getFirstActivated());
		final Date firstActivatedDate = action2.getFirstActivated();
		final WorkflowAction action3 = getAction(CODES.ACTION3.name());
		assertEquals(WorkflowAction.getIdleStatus(), action3.getStatus());
		assertNull(action3.getActivated());
		assertNull(action3.getFirstActivated());
		// complete action 2
		action2.complete();
		testWorkflow.toggleActions();
		assertEquals(WorkflowAction.getCompletedStatus(), action2.getStatus());
		assertEquals(WorkflowAction.getActiveStatus(), action3.getStatus());
		assertNotNull(action3.getActivated());
		assertNotNull(action3.getFirstActivated());
		// reject action 3
		action3.reject();
		testWorkflow.toggleActions();
		assertEquals(WorkflowAction.getDisabledStatus(), action1.getStatus());
		assertEquals(WorkflowAction.getActiveStatus(), action2.getStatus());
		assertNotNull(action2.getActivated());
		assertNotNull(action2.getFirstActivated());
		assertEquals(firstActivatedDate, action2.getFirstActivated());
		assertEquals(WorkflowAction.getIdleStatus(), action3.getStatus());
		assertFalse(testWorkflow.isFinished());
	}

	/**
	 * Creates some attachments and assigns them to the test workflow.
	 */
	@Test
	public void testAttachments()
	{
		final PK workflowPk = testWorkflow.getPK();
		// create product attachment
		final Product product = jaloSession.getProductManager().createProduct("sabbers");
		assertNotNull(product);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(WorkflowItemAttachment.CODE, "productTest");
		map.put(WorkflowItemAttachment.ITEM, product);
		map.put(WorkflowItemAttachment.WORKFLOW, testWorkflow);
		final WorkflowItemAttachment attachProduct = WorkflowManager.getInstance().createWorkflowItemAttachment(map);
		assertNotNull(attachProduct);

		// create category attachment
		final Category category = CategoryManager.getInstance().createCategory(PK.createUUIDPK(0).getHex());
		assertNotNull(category);
		map = new HashMap<String, Object>();
		map.put(WorkflowItemAttachment.CODE, "categoryTest");
		map.put(WorkflowItemAttachment.ITEM, category);
		map.put(WorkflowItemAttachment.WORKFLOW, testWorkflow);
		final WorkflowItemAttachment attachCategory = WorkflowManager.getInstance().createWorkflowItemAttachment(map);
		assertNotNull(attachCategory);

		final WorkflowAction action1 = getAction(CODES.ACTION1.name());
		action1.setAttachments(Arrays.asList(new WorkflowItemAttachment[]
		{ attachProduct, attachCategory }));
		// restart
		Registry.getCurrentTenant().getCache().clear();
		// check attachments
		final Workflow found = JaloSession.getCurrentSession().getItem(workflowPk);
		assertEquals(2, found.getAttachments().size());
		final WorkflowAction foundAction = getAction(CODES.ACTION1.name());
		assertEquals(2, foundAction.getAttachments().size());
	}

	@Test
	public void testAssignedUserCheck()
	{
		try
		{
			createWorkflowActionTemplate(UserManager.getInstance().getAnonymousCustomer(), "cyclic action", testTemplate);
			fail("The user can not be assigned to the action, because it has no read acces to type WorkflowAction");
		}
		catch (final JaloSystemException e) //NOPMD
		{
			// OK
		}
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
		final WorkflowActionTemplate templateAction1 = createWorkflowActionTemplate(user, CODES.ACTION1.name(), template);
		final WorkflowActionTemplate templateAction2 = createWorkflowActionTemplate(user, CODES.ACTION2.name(), template);
		final WorkflowActionTemplate templateAction3 = createWorkflowActionTemplate(user, CODES.ACTION3.name(), template);
		final WorkflowActionTemplate templateAction4 = createWorkflowActionTemplate(user, CODES.ACTION4.name(), template);
		final WorkflowActionTemplate templateAction5 = createWorkflowActionTemplate(user, CODES.ACTION5.name(), template);
		templateAction3.addToPredecessors(templateAction1);
		templateAction3.addToPredecessors(templateAction2);
		templateAction4.addToPredecessors(templateAction3);
		templateAction5.addToPredecessors(templateAction3);
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
			assertNotNull(readRight);
			TypeManager.getInstance().getComposedType(WorkflowAction.class).addPositivePermission(user, readRight);
			assertNotNull(user);
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
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put(WorkflowTemplate.OWNER, owner);
		values.put(WorkflowTemplate.CODE, code);
		values.put(WorkflowTemplate.DESCRIPTION, desc);
		final WorkflowTemplate template = manager.createWorkflowTemplate(values);
		assertNotNull(template);
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
			final WorkflowTemplate workflow)
	{
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put(WorkflowActionTemplate.PRINCIPALASSIGNED, user);
		values.put(WorkflowActionTemplate.WORKFLOW, workflow);
		values.put(WorkflowActionTemplate.CODE, code);
		values.put(WorkflowActionTemplate.SENDEMAIL, Boolean.FALSE);
		final WorkflowActionTemplate action = manager.createWorkflowActionTemplate(values);
		assertNotNull(action);
		return action;
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
