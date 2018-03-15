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
package de.hybris.platform.workflow.daos.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.daos.WorkflowActionDao;
import de.hybris.platform.workflow.daos.WorkflowDao;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Integrational tests for {@link WorkflowActionDao}
 */
@IntegrationTest
public class DefaultWorkflowActionDaoTest extends ServicelayerTest
{
	@Resource
	private WorkflowActionDao workflowActionDao;

	@Resource
	private WorkflowDao defaultWorkflowDao;

	@Resource
	private TypeService typeService;

	@Resource
	private UserService userService;

	@Before
	public void setUp() throws Exception
	{
		//given
		createCoreData();
		createDefaultCatalog();
		importCsv("/workflow/testdata/workflow_test_data.csv", "windows-1252");
	}

	@Test
	public void testFindEndWorkflowsActions()
	{
		//given
		final WorkflowModel workflow = defaultWorkflowDao.findWorkflowsByCode("workflow1").get(0);

		//when
		final List<WorkflowActionModel> findedActions = workflowActionDao.findEndWorkflowActions(workflow);

		//then
		assertThat(findedActions).hasSize(1);
	}

	@Test
	public void testFindEndWorkflowsActionsWhenNothingFound()
	{
		//given
		final WorkflowModel workflow = defaultWorkflowDao.findWorkflowsByCode("workflow2").get(0);

		//when
		final List<WorkflowActionModel> findedActions = workflowActionDao.findEndWorkflowActions(workflow);

		//then
		assertThat(findedActions).isEmpty();
	}

	@Test
	public void testFindNormalWorkflowsActions()
	{
		//given
		final WorkflowModel workflow = defaultWorkflowDao.findWorkflowsByCode("workflow1").get(0);

		//when
		final List<WorkflowActionModel> findedActions = workflowActionDao.findNormalWorkflowActions(workflow);

		//then
		assertThat(findedActions).hasSize(2);
	}

	@Test
	public void testFindNormalWorkflowsActionsWhenNothingFound()
	{
		//given
		final WorkflowModel workflow = defaultWorkflowDao.findWorkflowsByCode("workflow2").get(0);

		//when
		final List<WorkflowActionModel> findedActions = workflowActionDao.findNormalWorkflowActions(workflow);

		//then
		assertThat(findedActions).isEmpty();
	}

	@Test
	public void testFindStartWorkflowsActions()
	{
		//given
		final WorkflowModel workflow = defaultWorkflowDao.findWorkflowsByCode("workflow1").get(0);

		//when
		final List<WorkflowActionModel> findedActions = workflowActionDao.findStartWorkflowActions(workflow);

		//then
		assertThat(findedActions).hasSize(1);
	}

	@Test
	public void testFindStartWorkflowsActionsWhenNothingFound()
	{
		//given
		final WorkflowModel workflow = defaultWorkflowDao.findWorkflowsByCode("workflow2").get(0);

		//when
		final List<WorkflowActionModel> findedActions = workflowActionDao.findStartWorkflowActions(workflow);

		//then
		assertThat(findedActions).isEmpty();
	}

	@Test
	public void testFindWorkflowActionsByStatusAndAttachmentType()
	{
		//given
		setSessionUser("admin");
		final ComposedTypeModel productComposedType = typeService.getComposedTypeForClass(ProductModel.class);

		//when
		final List<WorkflowActionModel> findedActions = workflowActionDao.findWorkflowActionsByStatusAndAttachmentType(
				Collections.singletonList(productComposedType), Collections.singletonList(WorkflowActionStatus.IN_PROGRESS));

		//then
		//TODO: should return 1 result 
		assertThat(findedActions).isEmpty();
	}

	private UserModel setSessionUser(final String uid)
	{
		final UserModel sessionUser = userService.getUserForUID(uid);
		userService.setCurrentUser(sessionUser);
		return sessionUser;
	}
}
