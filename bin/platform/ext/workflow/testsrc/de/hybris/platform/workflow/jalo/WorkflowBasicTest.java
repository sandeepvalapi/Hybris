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
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


/**
 * Basic tests for Workflow-Extension.
 * 
 * 
 */
@IntegrationTest
public class WorkflowBasicTest extends WorkflowTest
{
	/**
	 * Checks if actions of test workflow has correct status.
	 */
	@Test
	public void testWorkflowCreate()
	{
		// test action 1
		final WorkflowAction action1 = getAction(ACTIONCODES.ACTION1.name());
		assertEquals("Expected number of decisions of action 1", 2, action1.getDecisionsCount());
		assertFalse("Is the action 1 disabled?", action1.isDisabled());
		assertTrue("Is the action 1 active?", action1.isActive());

		for (final WorkflowDecision decision : action1.getDecisions())
		{
			assertEquals("Expected number of actions of decisions of action 1", 1, decision.getToActionsCount());
		}

		// test action 2
		final WorkflowAction action2 = getAction(ACTIONCODES.ACTION2.name());
		assertEquals("Expected number of decisions of action 2", 2, action2.getDecisionsCount());
		assertFalse("Is the action 2 disabled?", action2.isDisabled());
		assertTrue("Is the action 2 active?", action2.isActive());

		for (final WorkflowDecision decision : action2.getDecisions())
		{
			if (decision.getCode().equals(DECISIONCODES.DECISION4.name()))
			{
				assertEquals("Expected number of actions of decisions of action 2", 2, decision.getToActionsCount());
			}
			else
			{
				assertEquals("Expected number of actions of decisions of action 2", 1, decision.getToActionsCount());
			}
		}

		// test action 3
		final WorkflowAction action3 = getAction(ACTIONCODES.ACTION3.name());
		assertEquals("Expected number of decisions of action 3", 1, action3.getDecisionsCount());
		assertFalse("Is the action 3 disabled?", action3.isDisabled());
		assertFalse("Is the action 3 active?", action3.isActive());

		for (final WorkflowDecision decision : action3.getDecisions())
		{
			assertEquals("Expected number of actions of decisions of action 3", 1, decision.getToActionsCount());
		}

		// test action 4
		final WorkflowAction action4 = getAction(ACTIONCODES.ACTION4.name());
		assertEquals("Expected number of decisions of action 4", 1, action4.getDecisionsCount());
		assertFalse("Is the action 4 disabled?", action4.isDisabled());
		assertFalse("Is the action 4 active?", action4.isActive());

		for (final WorkflowDecision decision : action4.getDecisions())
		{
			assertEquals("Expected number of actions of decisions of action 4", 1, decision.getToActionsCount());
		}

		// test action 5
		final WorkflowAction action5 = getAction(ACTIONCODES.ACTION5.name());
		assertEquals("Expected number of decisions of action 5", 1, action5.getDecisionsCount());
		assertFalse("Is the action 5 disabled?", action5.isDisabled());
		assertFalse("Is the action 5 active?", action5.isActive());

		for (final WorkflowDecision decision : action5.getDecisions())
		{
			assertEquals("Expected number of actions of decisions of action 5", 1, decision.getToActionsCount());
		}

		// test action 6
		final WorkflowAction action6 = getAction(ACTIONCODES.ACTION6.name());
		assertEquals("Expected number of decisions of action 6", 0, action5.getPredecessorsCount());
		assertFalse("Is the action 6 disabled?", action6.isDisabled());
		assertFalse("Is the action 6 active?", action6.isActive());
	}

	/**
	 * Completes the actions of the test workflow and checks the action status and the comments.
	 */
	@Test
	public void testWorkflowCompleteChain()
	{
		// complete action 1 with decision 1
		WorkflowAction action1 = getAction(ACTIONCODES.ACTION1.name());
		assertTrue("Action 1 should be active", action1.isActive());
		final WorkflowDecision decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		action1.setSelectedDecision(decision1);
		assertEquals("Expected number of decisions of action 1", 2, action1.getDecisionsCount());
		action1.decide();

		// test action 1
		action1 = getAction(ACTIONCODES.ACTION1.name());
		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertEquals("Excpected number of comments of action 1", 1, action1.getWorkflowActionComments().size());

		// test action 2 
		final WorkflowAction action2 = getAction(ACTIONCODES.ACTION2.name());
		assertFalse("Action 2 should not be disabled", action2.isDisabled());
		assertFalse("Action 2 should not be completed", action2.isCompleted());
		assertTrue("Action 2 should be active", action2.isActive());
		assertEquals("Excpected number of comments of action 2", 0, action2.getWorkflowActionComments().size());

		// test action 3 
		final WorkflowAction action3 = getAction(ACTIONCODES.ACTION3.name());
		assertFalse("Action 3 should not be disabled", action3.isDisabled());
		assertFalse("Action 3 should not be completed", action3.isCompleted());
		assertFalse("Action 3 should be inactive", action3.isActive());
		assertEquals("Excpected number of comments of action 3", 0, action3.getWorkflowActionComments().size());

		// test action 4 
		final WorkflowAction action4 = getAction(ACTIONCODES.ACTION4.name());
		assertFalse("Action 4 should not be disabled", action4.isDisabled());
		assertFalse("Action 4 should not be completed", action4.isCompleted());
		assertFalse("Action 4 should be inactive", action4.isActive());
		assertEquals("Excpected number of comments of action 4", 0, action4.getWorkflowActionComments().size());

		// test action 5 
		final WorkflowAction action5 = getAction(ACTIONCODES.ACTION5.name());
		assertFalse("Action 5 should not be disabled", action5.isDisabled());
		assertFalse("Action 5 should not be completed", action5.isCompleted());
		assertFalse("Action 5 should be inactive", action5.isActive());
		assertEquals("Excpected number of comments of action 5", 0, action5.getWorkflowActionComments().size());

		// test action 6 
		final WorkflowAction action6 = getAction(ACTIONCODES.ACTION6.name());
		assertFalse("Action 6 should not be disabled", action6.isDisabled());
		assertFalse("Action 6 should not be completed", action6.isCompleted());
		assertFalse("Action 6 should be inactive", action6.isActive());
		assertEquals("Excpected number of comments of action 6", 0, action6.getWorkflowActionComments().size());

		// complete action 2 with decision 3
		final WorkflowDecision decision3 = getDecision(DECISIONCODES.DECISION3.name(), action2);
		action2.setSelectedDecision(decision3);
		action2.decide();

		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertEquals("Excpected number of comments of action 1", 1, action1.getWorkflowActionComments().size());
		assertTrue("Action 2 should be completed", action2.isCompleted());
		assertEquals("Excpected number of comments of action 2", 1, action2.getWorkflowActionComments().size());
		assertTrue("Action 3 should be active", action3.isActive());
		assertEquals("Excpected number of comments of action 3", 1, action3.getWorkflowActionComments().size());
		assertFalse("Action 4 should be inactive", action4.isActive());
		assertEquals("Excpected number of comments of action 4", 0, action4.getWorkflowActionComments().size());
		assertFalse("Action 5 should be inactive", action5.isActive());
		assertEquals("Excpected number of comments of action 5", 0, action5.getWorkflowActionComments().size());
		assertFalse("Action 6 should be inactive", action6.isActive());
		assertEquals("Excpected number of comments of action 6", 0, action6.getWorkflowActionComments().size());
		assertFalse("Workflow should not be finished", testWorkflow.isFinished());


		// complete action 3 with decision 5
		final WorkflowDecision decision5 = getDecision(DECISIONCODES.DECISION5.name(), action3);
		action3.setSelectedDecision(decision5);
		action3.decide();
		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertEquals("Excpected number of comments of action 1", 1, action1.getWorkflowActionComments().size());
		assertTrue("Action 2 should be completed", action2.isCompleted());
		assertEquals("Excpected number of comments of action 2", 1, action2.getWorkflowActionComments().size());
		assertTrue("Action 3 should be completed", action3.isCompleted());
		assertEquals("Excpected number of comments of action 3", 2, action3.getWorkflowActionComments().size());
		assertFalse("Action 4 should be inactive", action4.isActive());
		assertEquals("Excpected number of comments of action 4", 0, action4.getWorkflowActionComments().size());
		assertTrue("Action 5 should be ended by workflow", action5.isEndedByWorkflow());
		assertEquals("Excpected number of comments of action 5", 0, action5.getWorkflowActionComments().size());
		assertFalse("Action 6 should be inactive", action6.isActive());
		assertEquals("Excpected number of comments of action 6", 1, action6.getWorkflowActionComments().size());
		assertTrue("Workflow should be finished", testWorkflow.isFinished());
	}

	@Test
	public void testAndDecisions()
	{
		final WorkflowAction action1 = getAction(ACTIONCODES.ACTION1.name());
		final WorkflowAction action2 = getAction(ACTIONCODES.ACTION2.name());
		final WorkflowAction action3 = getAction(ACTIONCODES.ACTION3.name());
		final WorkflowAction action4 = getAction(ACTIONCODES.ACTION4.name());
		final WorkflowAction action5 = getAction(ACTIONCODES.ACTION5.name());
		final WorkflowAction action6 = getAction(ACTIONCODES.ACTION6.name());

		// complete action 1 with decision 1
		final WorkflowDecision decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		action1.setSelectedDecision(decision1);
		action1.decide();

		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be active", action2.isActive());
		assertFalse("Action 3 should be inactive", action3.isActive());
		assertFalse("Action 4 should be inactive", action4.isActive());
		assertFalse("Action 5 should be inactive", action5.isActive());
		assertFalse("Action 6 should be inactive", action6.isActive());

		// complete action 2 with decision 3
		final WorkflowDecision decision3 = getDecision(DECISIONCODES.DECISION3.name(), action2);
		action2.setSelectedDecision(decision3);
		action2.decide();

		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be completed", action2.isCompleted());
		assertTrue("Action 3 should be active", action3.isActive());
		assertFalse("Action 4 should be inactive", action4.isActive());
		assertFalse("Action 5 should be inactive", action5.isActive());
		assertFalse("Action 6 should be inactive", action6.isActive());
	}

	@Test
	public void testOrDecisions()
	{
		final WorkflowAction action1 = getAction(ACTIONCODES.ACTION1.name());
		final WorkflowAction action2 = getAction(ACTIONCODES.ACTION2.name());
		final WorkflowAction action3 = getAction(ACTIONCODES.ACTION3.name());
		final WorkflowAction action4 = getAction(ACTIONCODES.ACTION4.name());
		final WorkflowAction action5 = getAction(ACTIONCODES.ACTION5.name());
		final WorkflowAction action6 = getAction(ACTIONCODES.ACTION6.name());

		// complete action 1 with decision 2
		final WorkflowDecision decision2 = getDecision(DECISIONCODES.DECISION2.name(), action1);
		action1.setSelectedDecision(decision2);
		action1.decide();

		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be active", action2.isActive());
		assertFalse("Action 3 should be inactive", action3.isActive());
		assertTrue("Action 4 should be active", action4.isActive());
		assertFalse("Action 5 should be inactive", action5.isActive());
		assertFalse("Action 6 should be inactive", action6.isActive());

		// complete action 2 with decision 4
		final WorkflowDecision decision4 = getDecision(DECISIONCODES.DECISION4.name(), action2);
		action2.setSelectedDecision(decision4);
		action2.decide();

		assertTrue("Action 1 should be completed", action1.isCompleted());
		assertTrue("Action 2 should be completed", action2.isCompleted());
		assertFalse("Action 3 should be inactive", action3.isActive());
		assertTrue("Action 4 should be active", action4.isActive());
		assertTrue("Action 5 should be active", action5.isActive());
		assertFalse("Action 6 should be inactive", action6.isActive());
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
		assertNotNull("Product not null", product);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(WorkflowItemAttachment.CODE, "productTest");
		map.put(WorkflowItemAttachment.ITEM, product);
		map.put(WorkflowItemAttachment.WORKFLOW, testWorkflow);
		final WorkflowItemAttachment attachProduct = WorkflowManager.getInstance().createWorkflowItemAttachment(map);
		assertNotNull("Attachment not null", attachProduct);

		// create category attachment
		final Category category = CategoryManager.getInstance().createCategory(PK.createUUIDPK(0).getHex());
		assertNotNull("Category not null", category);
		map = new HashMap<String, Object>();
		map.put(WorkflowItemAttachment.CODE, "categoryTest");
		map.put(WorkflowItemAttachment.ITEM, category);
		map.put(WorkflowItemAttachment.WORKFLOW, testWorkflow);
		final WorkflowItemAttachment attachCategory = WorkflowManager.getInstance().createWorkflowItemAttachment(map);
		assertNotNull("Attachment not null", attachCategory);

		final WorkflowAction action1 = getAction(ACTIONCODES.ACTION1.name());
		action1.setAttachments(Arrays.asList(new WorkflowItemAttachment[]
		{ attachProduct, attachCategory }));

		// restart
		Registry.getCurrentTenant().getCache();

		// check attachments
		final Workflow found = JaloSession.getCurrentSession().getItem(workflowPk);
		assertEquals("Excpected number of attachments", 2, found.getAttachments().size());
		final WorkflowAction foundAction = getAction(ACTIONCODES.ACTION1.name());
		assertEquals("Excpected number of attachments of action 1", 2, foundAction.getAttachments().size());
	}

	@Test(expected = JaloSystemException.class)
	public void testAssignedUserCheck()
	{
		createWorkflowActionTemplate(UserManager.getInstance().getAnonymousCustomer(), "cyclic action",
				WorkflowAction.getNormalActionType(), testTemplate);
		fail("The user can not be assigned to the action, because it has no read access to type WorkflowAction");
	}

	/**
	 * tries to complete a action with the wrong user
	 */
	@Test(expected = WorkflowActionDecideException.class)
	public void testAssignedUserDecide()
	{
		final User testUser1 = createUser("TestUser1");
		jaloSession.setUser(testUser1);

		// complete action 1 with decision 1
		final WorkflowAction action1 = getAction(ACTIONCODES.ACTION1.name());
		assertTrue("Action 1 should be active", action1.isActive());
		final WorkflowDecision decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		action1.setSelectedDecision(decision1);
		action1.decide();
		fail("The user can not decide an action he is not assigned to");
	}
}
