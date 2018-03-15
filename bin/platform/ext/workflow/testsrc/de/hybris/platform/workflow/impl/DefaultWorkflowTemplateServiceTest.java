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
package de.hybris.platform.workflow.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.workflow.daos.WorkflowTemplateDao;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



@UnitTest
public class DefaultWorkflowTemplateServiceTest
{
	private DefaultWorkflowTemplateService workflowTemplateService;

	@Mock
	private WorkflowTemplateDao workflowTemplateDao;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		workflowTemplateService = new DefaultWorkflowTemplateService();
		workflowTemplateService.setWorkflowTemplateDao(workflowTemplateDao);
	}

	@Test
	public void testGetAdhocWorkflowTemplate()
	{
		//given
		final WorkflowTemplateModel mockAdhocTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateDao.findAdhocWorkflowTemplates()).thenReturn(Collections.singletonList(mockAdhocTemplate));

		//when
		final WorkflowTemplateModel adhocWorkflowTemplate = workflowTemplateService.getAdhocWorkflowTemplate();

		//then
		assertThat(adhocWorkflowTemplate).isNotNull();
		assertThat(adhocWorkflowTemplate).isSameAs(mockAdhocTemplate);
	}

	@Test
	public void testGetAdhocWorkflowTemplateAndThrowAmbiguousIdentifierException()
	{
		//given
		final WorkflowTemplateModel mockAdhocTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateDao.findAdhocWorkflowTemplates()).thenReturn(Arrays.asList(mockAdhocTemplate, mockAdhocTemplate));

		//when
		try
		{
			workflowTemplateService.getAdhocWorkflowTemplate();
			fail("Should throw AmbiguousIdentifierException");
		}
		catch (final AmbiguousIdentifierException ex)
		{
			//then OK
		}
	}

	@Test
	public void testGetAdhocWorkflowTemplateAndThrowUnknownIdentifierException()
	{
		//given
		when(workflowTemplateDao.findAdhocWorkflowTemplates()).thenReturn(Collections.EMPTY_LIST);

		//when
		try
		{
			workflowTemplateService.getAdhocWorkflowTemplate();
			fail("Should throw AmbiguousIdentifierException");
		}
		catch (final UnknownIdentifierException ex)
		{
			//then OK
		}
	}

	@Test
	public void testGetAllWorkflowTemplates()
	{
		//given
		final WorkflowTemplateModel mockAdhocTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateDao.findAllWorkflowTemplates()).thenReturn(Arrays.asList(mockAdhocTemplate, mockAdhocTemplate));

		//when
		final List<WorkflowTemplateModel> workflowTemplates = workflowTemplateService.getAllWorkflowTemplates();

		//then
		assertThat(workflowTemplates).hasSize(2);
	}

	@Test
	public void testGetAdhocWorkflowTemplateDummyOwner()
	{
		//given
		final EmployeeModel mockDummyUser = mock(EmployeeModel.class);
		when(workflowTemplateDao.findAdhocWorkflowTemplateDummyOwner()).thenReturn(mockDummyUser);

		//when
		final EmployeeModel dummyUser = workflowTemplateService.getAdhocWorkflowTemplateDummyOwner();

		//then
		assertThat(dummyUser).isNotNull();
		assertThat(dummyUser).isSameAs(mockDummyUser);
	}

	@Test
	public void testGetAllVisibleWorkflowTemplatesByUser()
	{
		//given
		final UserModel mockDummyUser = mock(UserModel.class);
		final WorkflowTemplateModel mockTemplate1 = mock(WorkflowTemplateModel.class);
		final WorkflowTemplateModel userMockTemplate = mock(WorkflowTemplateModel.class);
		final WorkflowTemplateModel mockAdhocTemplate = mock(WorkflowTemplateModel.class);

		//when
		when(workflowTemplateDao.findWorkflowTemplatesByUser(mockDummyUser)).thenReturn(
				Arrays.asList(mockTemplate1, mockAdhocTemplate));
		when(workflowTemplateDao.findAdhocWorkflowTemplates()).thenReturn(Collections.singletonList(mockAdhocTemplate));
		when(workflowTemplateDao.findWorkflowTemplatesVisibleForPrincipal(mockDummyUser)).thenReturn(
				Arrays.asList(userMockTemplate));

		final List<WorkflowTemplateModel> workflowTemplates = workflowTemplateService
				.getAllVisibleWorkflowTemplatesForUser(mockDummyUser);

		//then
		assertThat(workflowTemplates).isNotEmpty();
		assertThat(workflowTemplates).hasSize(2);

	}
}
