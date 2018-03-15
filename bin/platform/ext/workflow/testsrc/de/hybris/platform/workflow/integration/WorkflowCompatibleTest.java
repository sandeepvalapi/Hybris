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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.jalo.WorkflowAction;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test for extension Workflow.
 */
@DemoTest
public class WorkflowCompatibleTest extends ServicelayerTransactionalTest
{
	/**
	 * Template instance created at each set up.
	 */
	private WorkflowTemplateModel testTemplate = null;
	/**
	 * workflow instance created at each set up.
	 */
	private WorkflowModel testWorkflow = null;

	@Resource(name = "newestWorkflowService")
	protected WorkflowService workflowService;
	@Resource
	protected ModelService modelService;
	@Resource
	protected UserService userService;
	@Resource
	protected TypeService typeService;

	@Resource
	protected CatalogVersionService catalogVersionService;

	@Resource
	protected WorkflowActionService workflowActionService;

	@Resource
	protected WorkflowProcessingService workflowProcessingService;

	private static enum CODES
	{
		ACTION1, ACTION2, ACTION3, ACTION4, ACTION5
	}

	//@Override
	@Before
	public void setUp() throws Exception
	{
		final CatalogModel testCatalog = modelService.create(CatalogModel.class);
		testCatalog.setId("DefaultTestCatalog");
		modelService.save(testCatalog);

		final CatalogVersionModel testCv = modelService.create(CatalogVersionModel.class);
		testCv.setVersion("Online");
		testCv.setCatalog(testCatalog);
		modelService.save(testCv);

		catalogVersionService.addSessionCatalogVersion(testCv);

		final UserModel testUser = createUser("TestUser");
		testTemplate = createWorkflowTemplate(testUser);

		// check actions template size
		final Collection<WorkflowActionTemplateModel> actionTemplates = testTemplate.getActions();
		assertEquals("Excpected number of actions", 5, actionTemplates.size());
		testWorkflow = workflowService.createWorkflow(testTemplate, testUser);
		workflowProcessingService.toggleActions(testWorkflow);
		assertNotNull("Workflow not null", testWorkflow);

		// check actions size
		final Collection<WorkflowActionModel> actions = testWorkflow.getActions();
		assertEquals("Excpected number of actions", 5, actions.size());
	}

	protected UserModel createUser(final String userName)
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
		return modelService.get(user);
	}

	/**
	 * Checks if actions of test workflow has correct status.
	 */
	@Test
	public void testWorkflowCreate()
	{
		// test action 1
		final WorkflowActionModel action1 = getAction(CODES.ACTION1.name());
		assertEquals(action1.getPredecessors().size(), 0);
		assertFalse(workflowActionService.isDisabled(action1));
		// test action 2
		final WorkflowActionModel action2 = getAction(CODES.ACTION2.name());
		assertEquals(action2.getPredecessors().size(), 0);
		assertFalse(workflowActionService.isDisabled(action2));
		// test action 3
		final WorkflowActionModel action3 = getAction(CODES.ACTION3.name());
		assertEquals(action3.getPredecessors().size(), 2);
		assertFalse(workflowActionService.isDisabled(action3));
		// test action 4
		final WorkflowActionModel action4 = getAction(CODES.ACTION4.name());
		assertEquals(action4.getPredecessors().size(), 1);
		assertFalse(workflowActionService.isDisabled(action4));
		// test action 5
		final WorkflowActionModel action5 = getAction(CODES.ACTION5.name());
		assertEquals(action5.getPredecessors().size(), 1);
		assertFalse(workflowActionService.isDisabled(action5));
	}

	/**
	 * Disable actions 1 and 2 of test workflow and checks the status of the workflow actions.
	 */
	@Test
	public void testWorkflowCreateDisableActivate()
	{
		// disable some actions
		WorkflowActionModel action1 = getAction(CODES.ACTION1.name());
		workflowActionService.disable(action1);
		WorkflowActionModel action2 = getAction(CODES.ACTION2.name());
		workflowActionService.disable(action2);
		workflowProcessingService.toggleActions(testWorkflow);
		// test action 1
		action1 = getAction(CODES.ACTION1.name());
		assertTrue(workflowActionService.isDisabled(action1));
		assertTrue(workflowActionService.isCompleted(action1));
		// test action 2
		action2 = getAction(CODES.ACTION2.name());
		assertTrue(workflowActionService.isDisabled(action2));
		assertTrue(workflowActionService.isCompleted(action2));
		// test action 3
		final WorkflowActionModel action3 = getAction(CODES.ACTION3.name());
		assertFalse(workflowActionService.isDisabled(action3));
		assertFalse(workflowActionService.isCompleted(action3));
		assertTrue(action3.getStatus().equals(WorkflowActionStatus.IN_PROGRESS));
		assertNotNull(action3.getActivated());
		assertNotNull(action3.getFirstActivated());
		// test action 4
		final WorkflowActionModel action4 = getAction(CODES.ACTION4.name());
		assertFalse(workflowActionService.isDisabled(action4));
		assertFalse(workflowActionService.isCompleted(action4));
		assertTrue(action4.getStatus().equals(WorkflowActionStatus.PENDING));
		// test action 5
		final WorkflowActionModel action5 = getAction(CODES.ACTION5.name());
		assertFalse(workflowActionService.isDisabled(action5));
		assertFalse(workflowActionService.isCompleted(action5));
		assertTrue(action5.getStatus().equals(WorkflowActionStatus.PENDING));
		// check workflow
		assertFalse(workflowService.isFinished(testWorkflow));
	}

	/**
	 * Completes the actions of the test workflow and checks the action status.
	 */
	@Test
	public void testWorkflowCompleteChain()
	{
		// disable action 1
		WorkflowActionModel action1 = getAction(CODES.ACTION1.name());
		workflowActionService.disable(action1);
		workflowProcessingService.toggleActions(testWorkflow);
		// test action 1
		action1 = getAction(CODES.ACTION1.name());
		assertTrue(workflowActionService.isDisabled(action1));
		assertTrue(workflowActionService.isCompleted(action1));
		assertTrue(action1.getStatus().equals(WorkflowActionStatus.DISABLED));
		// test action 2
		final WorkflowActionModel action2 = getAction(CODES.ACTION2.name());
		assertFalse(workflowActionService.isDisabled(action2));
		assertFalse(workflowActionService.isCompleted(action2));
		assertEquals(WorkflowActionStatus.IN_PROGRESS, action2.getStatus());
		// test action 3
		final WorkflowActionModel action3 = getAction(CODES.ACTION3.name());
		assertFalse(workflowActionService.isDisabled(action3));
		assertFalse(workflowActionService.isCompleted(action3));
		assertEquals(WorkflowActionStatus.PENDING, action3.getStatus());
		// test action 4
		final WorkflowActionModel action4 = getAction(CODES.ACTION4.name());
		assertFalse(workflowActionService.isDisabled(action4));
		assertFalse(workflowActionService.isCompleted(action4));
		assertEquals(WorkflowActionStatus.PENDING, action4.getStatus());
		// test action 5
		final WorkflowActionModel action5 = getAction(CODES.ACTION5.name());
		assertFalse(workflowActionService.isDisabled(action5));
		assertFalse(workflowActionService.isCompleted(action5));
		assertEquals(WorkflowActionStatus.PENDING, action5.getStatus());
		// complete action 2
		workflowActionService.complete(action2);
		workflowProcessingService.toggleActions(testWorkflow);
		assertEquals(WorkflowActionStatus.DISABLED, action1.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action2.getStatus());
		assertEquals(WorkflowActionStatus.IN_PROGRESS, action3.getStatus());
		assertEquals(WorkflowActionStatus.PENDING, action4.getStatus());
		assertEquals(WorkflowActionStatus.PENDING, action5.getStatus());
		assertFalse(workflowService.isFinished(testWorkflow));
		// complete action 3
		workflowActionService.complete(action3);
		workflowProcessingService.toggleActions(testWorkflow);
		assertEquals(WorkflowActionStatus.DISABLED, action1.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action2.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action3.getStatus());
		assertEquals(WorkflowActionStatus.IN_PROGRESS, action4.getStatus());
		assertEquals(WorkflowActionStatus.IN_PROGRESS, action5.getStatus());
		assertFalse(workflowService.isFinished(testWorkflow));
		workflowActionService.complete(action4);
		workflowProcessingService.toggleActions(testWorkflow);
		assertEquals(WorkflowActionStatus.DISABLED, action1.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action2.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action3.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action4.getStatus());
		assertEquals(WorkflowActionStatus.IN_PROGRESS, action5.getStatus());
		assertFalse(workflowService.isFinished(testWorkflow));
		// complete action 5
		workflowActionService.complete(action5);
		workflowProcessingService.toggleActions(testWorkflow);
		assertEquals(WorkflowActionStatus.DISABLED, action1.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action2.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action3.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action4.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action5.getStatus());
		assertTrue(workflowService.isFinished(testWorkflow));
	}

	/**
	 * Rejects action 3 of test workflow and checks action status.
	 */
	@Test
	//@Ignore("KAM-97, PLA-11421")
	@SuppressWarnings("deprecation")
	public void testWorkflowCompleteReject()
	{
		// disable action 1
		WorkflowActionModel action1 = getAction(CODES.ACTION1.name());
		workflowActionService.disable(action1);
		workflowProcessingService.toggleActions(testWorkflow);
		// check stati
		action1 = getAction(CODES.ACTION1.name());
		assertEquals(WorkflowActionStatus.DISABLED, action1.getStatus());
		final WorkflowActionModel action2 = getAction(CODES.ACTION2.name());
		assertEquals(WorkflowActionStatus.IN_PROGRESS, action2.getStatus());
		assertNotNull(action2.getActivated());
		assertNotNull(action2.getFirstActivated());
		final Date firstActivatedDate = action2.getFirstActivated();
		final WorkflowActionModel action3 = getAction(CODES.ACTION3.name());
		assertEquals(WorkflowActionStatus.PENDING, action3.getStatus());
		assertNull(action3.getActivated());
		assertNull(action3.getFirstActivated());
		// complete action 2
		workflowActionService.complete(action2);

		workflowProcessingService.toggleActions(testWorkflow);
		assertEquals(WorkflowActionStatus.COMPLETED, action2.getStatus());
		assertEquals(WorkflowActionStatus.IN_PROGRESS, action3.getStatus());
		assertNotNull(action3.getActivated());
		assertNotNull(action3.getFirstActivated());
		// reject action 3
		workflowActionService.idle(action3);

		workflowProcessingService.toggleActions(testWorkflow);
		assertEquals(WorkflowActionStatus.DISABLED, action1.getStatus());
		assertEquals(WorkflowActionStatus.COMPLETED, action2.getStatus());
		assertNotNull(action2.getActivated());
		assertNotNull(action2.getFirstActivated());
		assertEquals(firstActivatedDate, action2.getFirstActivated());
		assertEquals(WorkflowActionStatus.IN_PROGRESS, action3.getStatus());
		assertFalse(workflowService.isFinished(testWorkflow));
	}

	/**
	 * Creates some attachments and assigns them to the test workflow.
	 */
	@Test
	public void testAttachments()
	{
		final PK workflowPk = testWorkflow.getPk();
		// create product attachment
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("sabbers");
		product.setCatalogVersion(catalogVersionService.getCatalogVersion("DefaultTestCatalog", "Online"));

		assertNotNull(product);

		final WorkflowItemAttachmentModel attachProduct = createAttachment("productTest", product, testWorkflow);
		assertNotNull(attachProduct);

		// create category attachment
		final CategoryModel category = modelService.create(CategoryModel.class);
		category.setCatalogVersion(catalogVersionService.getCatalogVersion("DefaultTestCatalog", "Online"));
		category.setCode("abc");
		assertNotNull(category);
		final WorkflowItemAttachmentModel attachCategory = createAttachment("categoryTest", category, testWorkflow);
		assertNotNull(attachCategory);

		final WorkflowActionModel action1 = getAction(CODES.ACTION1.name());
		action1.setAttachments(Arrays.asList(new WorkflowItemAttachmentModel[]
		{ attachProduct, attachCategory }));
		// restart
		clearCache();
		// check attachments
		final WorkflowModel found = modelService.get(workflowPk);
		assertEquals(2, found.getAttachments().size());
		final WorkflowActionModel foundAction = getAction(CODES.ACTION1.name());
		assertEquals(2, foundAction.getAttachments().size());
	}

	protected void clearCache()
	{
		// restart
		Registry.getCurrentTenant().getCache().clear();
	}

	@Test
	public void testAssignedUserCheck()
	{
		try
		{
			createWorkflowActionTemplate(userService.getAnonymousUser(), "cyclic action", testTemplate);
			fail("The user can not be assigned to the action, because it has no read acces to type WorkflowAction");
		}
		catch (final ModelSavingException e) //NOPMD
		{
			// OK
		}
	}

	/**
	 * Creates a workflow template with given user assigned.
	 * 
	 * <pre>
	 * Action1 &lt; -Action3
	 * </pre>
	 * 
	 * <pre>
	 * Action2 &lt; -Action3
	 * </pre>
	 * 
	 * <pre>
	 * Action3 &lt; -Action4
	 * </pre>
	 * 
	 * <pre>
	 * Action3 &lt; -Action5
	 * </pre>
	 * 
	 * @param user
	 *           user instance to use for template
	 * @return new created template instance
	 */
	//@Override
	protected WorkflowTemplateModel createWorkflowTemplate(final UserModel user)
	{
		final WorkflowTemplateModel template = createWorkflowTemplate(user, "Test Template", "Test Template Descr");
		final WorkflowActionTemplateModel templateAction1 = createWorkflowActionTemplate(user, CODES.ACTION1.name(), template);
		final WorkflowActionTemplateModel templateAction2 = createWorkflowActionTemplate(user, CODES.ACTION2.name(), template);
		final WorkflowActionTemplateModel templateAction3 = createWorkflowActionTemplate(user, CODES.ACTION3.name(), template);
		final WorkflowActionTemplateModel templateAction4 = createWorkflowActionTemplate(user, CODES.ACTION4.name(), template);
		final WorkflowActionTemplateModel templateAction5 = createWorkflowActionTemplate(user, CODES.ACTION5.name(), template);
		addToPredecessors(templateAction3, templateAction1);
		addToPredecessors(templateAction3, templateAction2);
		addToPredecessors(templateAction4, templateAction3);
		addToPredecessors(templateAction5, templateAction3);
		modelService.saveAll();
		return template;
	}

	public void addToPredecessors(final WorkflowActionTemplateModel source, final WorkflowActionTemplateModel target)
	{
		final List<AbstractWorkflowActionModel> actions = new ArrayList(source.getPredecessors());
		actions.add(target);
		source.setPredecessors(actions);
		modelService.save(source);
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
	//@Override
	protected WorkflowTemplateModel createWorkflowTemplate(final UserModel owner, final String code, final String desc)
	{
		final WorkflowTemplateModel template = modelService.create(WorkflowTemplateModel.class);
		template.setOwner(owner);
		template.setCode(code);
		template.setDescription(desc);
		modelService.save(template);
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
	protected WorkflowActionTemplateModel createWorkflowActionTemplate(final UserModel user, final String code,
			final WorkflowTemplateModel workflow)
	{
		final WorkflowActionTemplateModel action = modelService.create(WorkflowActionTemplateModel.class);
		action.setPrincipalAssigned(user);
		action.setWorkflow(workflow);
		action.setCode(code);
		action.setSendEmail(Boolean.FALSE);
		modelService.save(action);

		final List<WorkflowActionTemplateModel> actions = new ArrayList(workflow.getActions());
		actions.add(action);
		workflow.setActions(actions);
		modelService.save(workflow);

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
	//@Override
	protected WorkflowActionModel getAction(final String code)
	{
		final Collection<WorkflowActionModel> actions = testWorkflow.getActions();
		for (final WorkflowActionModel action : actions)
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
	//@Override
	protected WorkflowActionTemplateModel getActionTemplate(final String code)
	{
		final Collection<WorkflowActionTemplateModel> actions = testTemplate.getActions();
		for (final WorkflowActionTemplateModel action : actions)
		{
			if (action.getCode().equals(code))
			{
				return action;
			}
		}
		fail("ActionTemplate " + code + "can not be found");
		return null;
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
}
