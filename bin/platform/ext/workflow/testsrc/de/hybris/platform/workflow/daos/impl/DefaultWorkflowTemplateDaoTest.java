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
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.daos.WorkflowTemplateDao;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultWorkflowTemplateDaoTest extends ServicelayerTransactionalTest
{
	@Resource
	WorkflowTemplateDao workflowTemplateDao;

	@Resource
	UserService userService;

	@Before
	public void setUp() throws Exception
	{
		//given
		createCoreData();
		createDefaultCatalog();
		importCsv("/workflow/testdata/workflow_test_data.csv", "windows-1252");
	}

	@Test
	public void testFindAdhocWorkflowTemplateDummyOwner()
	{
		//when
		final EmployeeModel owner = workflowTemplateDao.findAdhocWorkflowTemplateDummyOwner();

		//then
		assertThat(owner).isNotNull();
		assertThat(owner.getUid()).isEqualTo("admin");
	}

	@Test
	public void testFindAdhocWorkflowTemplates()
	{
		//when
		final List<WorkflowTemplateModel> adhocTemplates = workflowTemplateDao.findAdhocWorkflowTemplates();

		//then
		assertThat(adhocTemplates).hasSize(1);
		assertThat(adhocTemplates.get(0).getCode()).isEqualTo("adhoctemplate");
	}

	@Test
	public void testFindAdhocWorkflowTemplatesWhenAdHocTemplateNameIsNull()
	{
		//when
		((DefaultWorkflowTemplateDao) workflowTemplateDao).setAdHocTemplateName(null);
		final List<WorkflowTemplateModel> adhocTemplates = workflowTemplateDao.findAdhocWorkflowTemplates();

		//then
		assertThat(adhocTemplates).hasSize(1);
		assertThat(adhocTemplates.get(0).getCode()).isEqualTo("adhoctemplate");
	}

	@Test
	public void testFindAllWorkflowTemplates()
	{
		//when
		final List<WorkflowTemplateModel> adhocTemplates = workflowTemplateDao.findAllWorkflowTemplates();

		//then
		assertThat(adhocTemplates).hasSize(3);
	}

	@Test
	public void testFindWorkflowTemplatesByUser()
	{
		//given
		final UserModel user = userService.getUserForUID("workflowTestUser");

		//when
		final List<WorkflowTemplateModel> adhocTemplates = workflowTemplateDao.findWorkflowTemplatesByUser(user);

		//then
		assertThat(adhocTemplates).hasSize(1);
		assertThat(adhocTemplates.get(0).getCode()).isEqualTo("adhoctemplate");
	}

	@Test
	public void testFindWorkflowTemplatesByUserWithNoTemplates()
	{
		//given
		final UserModel user = userService.getAnonymousUser();

		//when
		final List<WorkflowTemplateModel> adhocTemplates = workflowTemplateDao.findWorkflowTemplatesByUser(user);

		//then
		assertThat(adhocTemplates).isEmpty();
	}
}
