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
package de.hybris.platform.processengine.action;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.processengine.helpers.WorkflowIntegrationService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.workflow.WorkflowAttachmentService;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class StartingWorkflowActionTest
{
	@InjectMocks
	StartingWorkflowAction startingWorkflowAction = new StartingWorkflowAction();

	@Mock
	private ModelService modelService;
	@Mock
	private UserService userService;
	@Mock
	private WorkflowIntegrationService workflowIntegrationService;
	@Mock
	private WorkflowAttachmentService workflowAttachmentService;

	private final static String TEMPLATE_ID = "templateId";

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		startingWorkflowAction.setTemplateId(TEMPLATE_ID);
	}

	/**
	 * Test method for {@link StartingWorkflowAction#executeAction(BusinessProcessModel)}.
	 */
	@Test
	public void testExecuteAction() throws RetryLaterException, Exception
	{
		final EmployeeModel employee = new EmployeeModel();
		Mockito.when(userService.getAdminUser()).thenReturn(employee);

		final WorkflowTemplateModel workflowTemplateModel = new WorkflowTemplateModel();
		Mockito.when(workflowIntegrationService.getWorkflowTemplateModelById(TEMPLATE_ID)).thenReturn(workflowTemplateModel);

		final WorkflowModel workflow = new WorkflowModel();
		Mockito.when(workflowIntegrationService.createWorkflow(workflowTemplateModel, employee)).thenReturn(workflow);

		final BusinessProcessModel process = new BusinessProcessModel();
		startingWorkflowAction.executeAction(process);

		Mockito.verify(workflowAttachmentService).addItems(workflow, Arrays.asList(process));
		Mockito.verify(modelService).save(workflow);
		Mockito.verify(workflowIntegrationService).startWorkflow(workflow);
	}

}
