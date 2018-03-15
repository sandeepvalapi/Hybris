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
package de.hybris.platform.workflow.services.internal.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ActionsFromWorkflowTemplateFactoryTest
{
	private final ActionsFromWorkflowTemplateFactory factory = new ActionsFromWorkflowTemplateFactory();

	@Mock
	private ModelService modelService;

	@Mock
	private WorkflowActionService workflowActionService;


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		factory.setModelService(modelService);
		factory.setWorkflowActionService(workflowActionService);
	}

	@Test
	public void testWorkflowTemplateFactoryEmpty()
	{
		final WorkflowModel workFlow = Mockito.mock(WorkflowModel.class);
		final WorkflowTemplateModel workTemplate = Mockito.mock(WorkflowTemplateModel.class);

		Assert.assertEquals(Collections.EMPTY_LIST, factory.create(workFlow, workTemplate));
		Mockito.verifyNoMoreInteractions(modelService);
	}

	@Test
	public void testActionsFromWorkflowTemplateFactoryEmpty()
	{
		final WorkflowActionModel actionResult = new WorkflowActionModel();

		final WorkflowModel workFlow = Mockito.mock(WorkflowModel.class);
		final WorkflowTemplateModel workTemplate = Mockito.mock(WorkflowTemplateModel.class);

		final WorkflowActionTemplateModel actionOne = Mockito.mock(WorkflowActionTemplateModel.class);

		final WorkflowActionTemplateModel actionTwo = Mockito.mock(WorkflowActionTemplateModel.class);
		Mockito.when(workTemplate.getActions()).thenReturn(Arrays.asList(actionOne, actionTwo));

		Mockito.when(
				workflowActionService.createWorkflowAction(Mockito.any(WorkflowActionTemplateModel.class),
						Mockito.any(WorkflowModel.class))).thenReturn(actionResult);

		Assert.assertEquals(Arrays.asList(actionResult, actionResult), factory.create(workFlow, workTemplate));

		Mockito.verify(workflowActionService).createWorkflowAction(actionOne, workFlow);
		Mockito.verify(workflowActionService).createWorkflowAction(actionTwo, workFlow);
		Mockito.verify(modelService, Mockito.times(2)).save(actionResult);
	}
}
