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
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ActionPredecessorsFromActionTemplateFactoryTest
{
	private final ActionPredecessorsFromActionTemplateFactory factory = new ActionPredecessorsFromActionTemplateFactory();

	@Mock
	private ModelService modelService;


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		factory.setModelService(modelService);
	}

	@Test
	public void testActionTemplateFactoryEmptyActions()
	{

		final WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
		Mockito.when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);

		final WorkflowActionTemplateModel actionTemplate = Mockito.mock(WorkflowActionTemplateModel.class);

		try
		{
			factory.create(workflow, actionTemplate);
			Assert.fail("Should have a NPE when no action found ??");
		}
		catch (final NullPointerException npe)
		{
			//ok here
		}
	}


	@Test
	public void testActionTemplateFactoryNoMatchingTemplate()
	{

		final WorkflowActionModel actionOne = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(actionOne.getTemplate()).thenReturn(new WorkflowActionTemplateModel());
		final WorkflowActionModel actionTwo = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(actionTwo.getTemplate()).thenReturn(new WorkflowActionTemplateModel());
		final WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
		Mockito.when(workflow.getActions()).thenReturn(Arrays.asList(actionOne, actionTwo));

		final WorkflowActionTemplateModel actionTemplate = Mockito.mock(WorkflowActionTemplateModel.class);
		try
		{
			factory.create(workflow, actionTemplate);
			Assert.fail("Should have a NPE when no action found, by matching WorkflowActionTemplateModel??");
		}
		catch (final NullPointerException npe)
		{
			//ok here
		}
	}


	@Test
	public void testActionTemplateFactoryMatchingTemplateNoPredecessors()
	{
		final List<AbstractWorkflowActionModel> predecessors = new ArrayList<AbstractWorkflowActionModel>();
		final WorkflowActionTemplateModel actionTemplate = Mockito.mock(WorkflowActionTemplateModel.class);
		Mockito.when(actionTemplate.getPredecessors()).thenReturn(predecessors);

		final WorkflowActionModel actionOne = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(actionOne.getTemplate()).thenReturn(new WorkflowActionTemplateModel());
		final WorkflowActionModel actionTwo = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(actionTwo.getTemplate()).thenReturn(actionTemplate);
		final WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
		Mockito.when(workflow.getActions()).thenReturn(Arrays.asList(actionOne, actionTwo));


		Assert.assertEquals(predecessors, factory.create(workflow, actionTemplate));
	}

	@Test
	public void testActionTemplateFactoryMatchingTemplatePredecessors()
	{
		final List<AbstractWorkflowActionModel> predecessors = new ArrayList<AbstractWorkflowActionModel>();


		final WorkflowActionTemplateModel actionTemplate = Mockito.mock(WorkflowActionTemplateModel.class);
		Mockito.when(actionTemplate.getPredecessors()).thenReturn(predecessors);

		final WorkflowActionTemplateModel actionTemplatePredecessor = Mockito.mock(WorkflowActionTemplateModel.class);
		predecessors.add(actionTemplatePredecessor);

		final WorkflowActionModel actionOne = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(actionOne.getTemplate()).thenReturn(actionTemplatePredecessor);
		final WorkflowActionModel actionTwo = Mockito.mock(WorkflowActionModel.class);
		Mockito.when(actionTwo.getTemplate()).thenReturn(actionTemplate);
		final WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
		Mockito.when(workflow.getActions()).thenReturn(Arrays.asList(actionOne, actionTwo));

		Assert.assertEquals(Arrays.asList(actionOne), factory.create(workflow, actionTemplate));
	}
}
