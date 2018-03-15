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
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.exceptions.WorkflowActionDecideException;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;

import org.junit.Test;


/**
 * Basic tests for Workflow-Extension.
 * 
 * 
 */
@DemoTest
public class WorkflowBasicTest extends WorkflowTest
{
	/**
	 * Checks if actions of test workflow has correct status.
	 */
	@Test
	public void testWorkflowCreate()
	{
		// test action 1
		final WorkflowActionModel action1 = getAction(ACTIONCODES.ACTION1.name());
		assertEquals("Expected number of decisions of action 1", 2, action1.getDecisions().size());
		assertFalse("Is the action 1 disabled?", workflowActionService.isDisabled(action1));
		assertTrue("Is the action 1 active?", workflowActionService.isActive(action1));

		for (final WorkflowDecisionModel decision : action1.getDecisions())
		{
			assertEquals("Expected number of actions of decisions of action 1", 1, decision.getToActions().size());
		}

		// test action 2
		final WorkflowActionModel action2 = getAction(ACTIONCODES.ACTION2.name());
		assertEquals("Expected number of decisions of action 2", 2, action2.getDecisions().size());
		assertFalse("Is the action 2 disabled?", workflowActionService.isDisabled(action2));
		assertTrue("Is the action 2 active?", workflowActionService.isActive(action2));

		for (final WorkflowDecisionModel decision : action2.getDecisions())
		{
			if (decision.getCode().equals(DECISIONCODES.DECISION4.name()))
			{
				assertEquals("Expected number of actions of decisions of action 2", 2, decision.getToActions().size());
			}
			else
			{
				assertEquals("Expected number of actions of decisions of action 2", 1, decision.getToActions().size());
			}
		}

		// test action 3
		final WorkflowActionModel action3 = getAction(ACTIONCODES.ACTION3.name());
		assertEquals("Expected number of decisions of action 3", 1, action3.getDecisions().size());
		assertFalse("Is the action 3 disabled?", workflowActionService.isDisabled(action3));
		assertFalse("Is the action 3 active?", workflowActionService.isActive(action3));

		for (final WorkflowDecisionModel decision : action3.getDecisions())
		{
			assertEquals("Expected number of actions of decisions of action 3", 1, decision.getToActions().size());
		}

		// test action 4
		final WorkflowActionModel action4 = getAction(ACTIONCODES.ACTION4.name());
		assertEquals("Expected number of decisions of action 4", 1, action4.getDecisions().size());
		assertFalse("Is the action 4 disabled?", workflowActionService.isDisabled(action4));
		assertFalse("Is the action 4 active?", workflowActionService.isActive(action4));

		for (final WorkflowDecisionModel decision : action4.getDecisions())
		{
			assertEquals("Expected number of actions of decisions of action 4", 1, decision.getToActions().size());
		}

		// test action 5
		final WorkflowActionModel action5 = getAction(ACTIONCODES.ACTION5.name());
		assertEquals("Expected number of decisions of action 5", 1, action5.getDecisions().size());
		assertFalse("Is the action 5 disabled?", workflowActionService.isDisabled(action5));
		assertFalse("Is the action 5 active?", workflowActionService.isActive(action5));

		for (final WorkflowDecisionModel decision : action5.getDecisions())
		{
			assertEquals("Expected number of actions of decisions of action 5", 1, decision.getToActions().size());
		}

		// test action 6
		final WorkflowActionModel action6 = getAction(ACTIONCODES.ACTION6.name());
		assertEquals("Expected number of decisions of action 6", 0, action5.getPredecessors().size());
		assertFalse("Is the action 6 disabled?", workflowActionService.isDisabled(action6));
		assertFalse("Is the action 6 active?", workflowActionService.isActive(action6));
	}

	/**
	 * Completes the actions of the test workflow and checks the action status and the comments.
	 */
	@Test
	public void testWorkflowCompleteChain()
	{
		// complete action 1 with decision 1
		WorkflowActionModel action1 = getAction(ACTIONCODES.ACTION1.name());
		assertTrue("Action 1 should be active", workflowActionService.isActive(action1));
		final WorkflowDecisionModel decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		assertEquals("Expected number of decisions of action 1", 2, action1.getDecisions().size());
		workflowProcessingService.decideAction(action1, decision1);

		// test action 1
		action1 = getAction(ACTIONCODES.ACTION1.name());
		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertEquals("Excpected number of comments of action 1", 1, action1.getWorkflowActionComments().size());

		// test action 2 
		final WorkflowActionModel action2 = getAction(ACTIONCODES.ACTION2.name());
		assertFalse("Action 2 should not be disabled", workflowActionService.isDisabled(action2));
		assertFalse("Action 2 should not be completed", workflowActionService.isCompleted(action2));
		assertTrue("Action 2 should be active", workflowActionService.isActive(action2));
		assertEquals("Excpected number of comments of action 2", 0, action2.getWorkflowActionComments().size());

		// test action 3 
		final WorkflowActionModel action3 = getAction(ACTIONCODES.ACTION3.name());
		assertFalse("Action 3 should not be disabled", workflowActionService.isDisabled(action3));
		assertFalse("Action 3 should not be completed", workflowActionService.isCompleted(action3));
		assertFalse("Action 3 should be inactive", workflowActionService.isActive(action3));
		assertEquals("Excpected number of comments of action 3", 0, action3.getWorkflowActionComments().size());

		// test action 4 
		final WorkflowActionModel action4 = getAction(ACTIONCODES.ACTION4.name());
		assertFalse("Action 4 should not be disabled", workflowActionService.isDisabled(action4));
		assertFalse("Action 4 should not be completed", workflowActionService.isCompleted(action4));
		assertFalse("Action 4 should be inactive", workflowActionService.isActive(action4));
		assertEquals("Excpected number of comments of action 4", 0, action4.getWorkflowActionComments().size());

		// test action 5 
		final WorkflowActionModel action5 = getAction(ACTIONCODES.ACTION5.name());
		assertFalse("Action 5 should not be disabled", workflowActionService.isDisabled(action5));
		assertFalse("Action 5 should not be completed", workflowActionService.isCompleted(action5));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));
		assertEquals("Excpected number of comments of action 5", 0, action5.getWorkflowActionComments().size());

		// test action 6 
		final WorkflowActionModel action6 = getAction(ACTIONCODES.ACTION6.name());
		assertFalse("Action 6 should not be disabled", workflowActionService.isDisabled(action6));
		assertFalse("Action 6 should not be completed", workflowActionService.isCompleted(action6));
		assertFalse("Action 6 should be inactive", workflowActionService.isActive(action6));
		assertEquals("Excpected number of comments of action 6", 0, action6.getWorkflowActionComments().size());

		// complete action 2 with decision 3
		final WorkflowDecisionModel decision3 = getDecision(DECISIONCODES.DECISION3.name(), action2);
		workflowProcessingService.decideAction(action2, decision3);

		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertEquals("Excpected number of comments of action 1", 1, action1.getWorkflowActionComments().size());
		assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
		assertEquals("Excpected number of comments of action 2", 1, action2.getWorkflowActionComments().size());
		assertTrue("Action 3 should be active", workflowActionService.isActive(action3));
		assertEquals("Excpected number of comments of action 3", 1, action3.getWorkflowActionComments().size());
		assertFalse("Action 4 should be inactive", workflowActionService.isActive(action4));
		assertEquals("Excpected number of comments of action 4", 0, action4.getWorkflowActionComments().size());
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));
		assertEquals("Excpected number of comments of action 5", 0, action5.getWorkflowActionComments().size());
		assertFalse("Action 6 should be inactive", workflowActionService.isActive(action6));
		assertEquals("Excpected number of comments of action 6", 0, action6.getWorkflowActionComments().size());
		assertFalse("Workflow should not be finished", workflowService.isFinished(testWorkflow));


		// complete action 3 with decision 5
		final WorkflowDecisionModel decision5 = getDecision(DECISIONCODES.DECISION5.name(), action3);
		workflowProcessingService.decideAction(action3, decision5);
		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertEquals("Excpected number of comments of action 1", 1, action1.getWorkflowActionComments().size());
		assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
		assertEquals("Excpected number of comments of action 2", 1, action2.getWorkflowActionComments().size());
		assertTrue("Action 3 should be completed", workflowActionService.isCompleted(action3));
		assertEquals("Excpected number of comments of action 3", 2, action3.getWorkflowActionComments().size());
		assertFalse("Action 4 should be inactive", workflowActionService.isActive(action4));
		assertEquals("Excpected number of comments of action 4", 0, action4.getWorkflowActionComments().size());
		assertTrue("Action 5 should be ended by workflow", workflowActionService.isEndedByWorkflow(action5));
		assertEquals("Excpected number of comments of action 5", 0, action5.getWorkflowActionComments().size());
		assertFalse("Action 6 should be inactive", workflowActionService.isActive(action6));
		assertEquals("Excpected number of comments of action 6", 1, action6.getWorkflowActionComments().size());
		assertTrue("Workflow should be finished", workflowService.isFinished(testWorkflow));
	}

	@Test
	public void testAndDecisions()
	{
		final WorkflowActionModel action1 = getAction(ACTIONCODES.ACTION1.name());
		final WorkflowActionModel action2 = getAction(ACTIONCODES.ACTION2.name());
		final WorkflowActionModel action3 = getAction(ACTIONCODES.ACTION3.name());
		final WorkflowActionModel action4 = getAction(ACTIONCODES.ACTION4.name());
		final WorkflowActionModel action5 = getAction(ACTIONCODES.ACTION5.name());
		final WorkflowActionModel action6 = getAction(ACTIONCODES.ACTION6.name());

		// complete action 1 with decision 1
		final WorkflowDecisionModel decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		workflowProcessingService.decideAction(action1, decision1);

		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertTrue("Action 2 should be active", workflowActionService.isActive(action2));
		assertFalse("Action 3 should be inactive", workflowActionService.isActive(action3));
		assertFalse("Action 4 should be inactive", workflowActionService.isActive(action4));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));
		assertFalse("Action 6 should be inactive", workflowActionService.isActive(action6));

		// complete action 2 with decision 3
		final WorkflowDecisionModel decision3 = getDecision(DECISIONCODES.DECISION3.name(), action2);
		workflowProcessingService.decideAction(action2, decision3);

		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
		assertTrue("Action 3 should be active", workflowActionService.isActive(action3));
		assertFalse("Action 4 should be inactive", workflowActionService.isActive(action4));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));
		assertFalse("Action 6 should be inactive", workflowActionService.isActive(action6));
	}

	@Test
	public void testOrDecisions()
	{
		final WorkflowActionModel action1 = getAction(ACTIONCODES.ACTION1.name());
		final WorkflowActionModel action2 = getAction(ACTIONCODES.ACTION2.name());
		final WorkflowActionModel action3 = getAction(ACTIONCODES.ACTION3.name());
		final WorkflowActionModel action4 = getAction(ACTIONCODES.ACTION4.name());
		final WorkflowActionModel action5 = getAction(ACTIONCODES.ACTION5.name());
		final WorkflowActionModel action6 = getAction(ACTIONCODES.ACTION6.name());

		// complete action 1 with decision 2
		final WorkflowDecisionModel decision2 = getDecision(DECISIONCODES.DECISION2.name(), action1);
		workflowProcessingService.decideAction(action1, decision2);

		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertTrue("Action 2 should be active", workflowActionService.isActive(action2));
		assertFalse("Action 3 should be inactive", workflowActionService.isActive(action3));
		assertTrue("Action 4 should be active", workflowActionService.isActive(action4));
		assertFalse("Action 5 should be inactive", workflowActionService.isActive(action5));
		assertFalse("Action 6 should be inactive", workflowActionService.isActive(action6));

		// complete action 2 with decision 4
		final WorkflowDecisionModel decision4 = getDecision(DECISIONCODES.DECISION4.name(), action2);
		workflowProcessingService.decideAction(action2, decision4);

		assertTrue("Action 1 should be completed", workflowActionService.isCompleted(action1));
		assertTrue("Action 2 should be completed", workflowActionService.isCompleted(action2));
		assertFalse("Action 3 should be inactive", workflowActionService.isActive(action3));
		assertTrue("Action 4 should be active", workflowActionService.isActive(action4));
		assertTrue("Action 5 should be active", workflowActionService.isActive(action5));
		assertFalse("Action 6 should be inactive", workflowActionService.isActive(action6));
	}

	/**
	 * Creates some attachments and assigns them to the test workflow.
	 */
	@Test
	public void testAttachments()
	{
		final CatalogVersionModel testCv = catalogVersionService.getCatalogVersion("DefaultTestCatalog", "Online");
		assertNotNull(testCv);

		final PK workflowPk = testWorkflow.getPk();
		// create product attachment
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("abc");

		product.setCatalogVersion(testCv);

		modelService.save(product);
		assertNotNull("Product not null", product);

		final WorkflowItemAttachmentModel attachProduct = createAttachment("productTest", product, testWorkflow);
		assertNotNull("Attachment not null", attachProduct);

		// create category attachment
		final CategoryModel category = modelService.create(CategoryModel.class);
		category.setCode("abc");
		category.setCatalogVersion(testCv);
		assertNotNull("Category not null", category);

		final WorkflowItemAttachmentModel attachCategory = createAttachment("categoryTest", category, testWorkflow);
		assertNotNull("Attachment not null", attachCategory);

		final WorkflowActionModel action1 = getAction(ACTIONCODES.ACTION1.name());
		action1.setAttachments(Arrays.asList(new WorkflowItemAttachmentModel[]
		{ attachProduct, attachCategory }));

		clearCache();

		// check attachments
		final WorkflowModel found = modelService.get(workflowPk);
		assertEquals("Excpected number of attachments", 2, found.getAttachments().size());
		final WorkflowActionModel foundAction = getAction(ACTIONCODES.ACTION1.name());
		assertEquals("Excpected number of attachments of action 1", 2, foundAction.getAttachments().size());
	}

	@Test(expected = ModelSavingException.class)
	public void testAssignedUserCheck()
	{
		createWorkflowActionTemplateModel(userService.getAnonymousUser(), "cyclic action", WorkflowActionType.NORMAL, testTemplate);
		fail("The user can not be assigned to the action, because it has no read access to type WorkflowAction");
	}

	private WorkflowItemAttachmentModel createAttachment(final String code, final ItemModel item, final WorkflowModel workflow)
	{
		final WorkflowItemAttachmentModel attachment = modelService.create(WorkflowItemAttachmentModel.class);
		attachment.setCode(code);
		attachment.setItem(item);
		attachment.setWorkflow(workflow);
		modelService.save(attachment);
		return attachment;
	}

	/**
	 * tries to complete a action with the wrong user
	 */
	@Test(expected = WorkflowActionDecideException.class)
	public void testAssignedUserDecide()
	{
		final UserModel testUser1 = createUser("TestUser1");
		userService.setCurrentUser(testUser1);

		// complete action 1 with decision 1
		final WorkflowActionModel action1 = getAction(ACTIONCODES.ACTION1.name());
		assertTrue("Action 1 should be active", workflowActionService.isActive(action1));
		final WorkflowDecisionModel decision1 = getDecision(DECISIONCODES.DECISION1.name(), action1);
		action1.setSelectedDecision(decision1);
		workflowProcessingService.decideAction(action1, decision1);
		fail("The user can not decide an action he is not assigned to");
	}
}
