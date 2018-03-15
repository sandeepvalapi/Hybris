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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bsh.EvalError;
import bsh.Interpreter;


@UnitTest
public class DefaultScriptEvaluationServiceTest
{
	@InjectMocks
	private DefaultScriptEvaluationService scriptEvaluationService;
	@Mock
	private WorkflowService workflowService;
	@Mock
	private WorkflowTemplateService workflowTemplateService;
	@Mock
	private TypeService typeService;
	@Mock
	private Interpreter interpreter;
	@Mock
	private UserService userService;
	@Mock
	private ModelService modelService;
	@Mock
	private WorkflowProcessingService workflowProcessingService;

	@Before
	public void setUp()
	{
		scriptEvaluationService = new DefaultScriptEvaluationService()
		{
			@Override
			Interpreter getInterpreter()
			{
				return interpreter;
			}
		};
		scriptEvaluationService.setUserService(userService);
		scriptEvaluationService.setModelService(modelService);
		scriptEvaluationService.setWorkflowProcessingService(workflowProcessingService);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testEvaluteActivationScriptsWhenItemIsNull() throws Exception
	{
		//when
		final WorkflowModel workflow = scriptEvaluationService.evaluateActivationScripts(null, null, null, null);

		//then
		assertThat(workflow).isNull();
	}

	@Test
	public void testEvaluteActivationScriptsAndSucceed() throws Exception
	{
		final ItemModel mockItem = mock(ItemModel.class);
		final Map currentValues = new HashMap();
		final Map initialValues = new HashMap();
		final String action = null;

		final WorkflowTemplateModel mockTemplate = mock(WorkflowTemplateModel.class);
		when(mockTemplate.getActivationScript()).thenReturn("activation script");
		when(workflowTemplateService.getAllWorkflowTemplates()).thenReturn(Collections.singletonList(mockTemplate));
		when(interpreter.eval("activation script")).thenReturn(Boolean.TRUE);
		final WorkflowModel mockWorkflow = mock(WorkflowModel.class);
		when(workflowService.createWorkflow(mockTemplate, mockItem, null)).thenReturn(mockWorkflow);

		//when
		final WorkflowModel workflow = scriptEvaluationService.evaluateActivationScripts(mockItem, currentValues, initialValues,
				action);

		//then
		assertThat(workflow).isNotNull();
		assertThat(workflow).isSameAs(mockWorkflow);
		verify(interpreter).set("action", action);
		verify(interpreter).set("item", mockItem);
		verify(interpreter).set("itemType", typeService.getComposedTypeForCode(ItemModel._TYPECODE));
		verify(interpreter).set("initialValues", initialValues);
		verify(interpreter).set("currentValues", currentValues);
	}

	@Test
	public void testEvaluteActivationScriptsAndExceptionOccurr() throws Exception
	{
		final ItemModel mockItem = mock(ItemModel.class);
		final String action = "activation script";

		doThrow(new EvalError(null, null, null)).when(interpreter).set("action", "activation script");

		//when
		try
		{
			scriptEvaluationService.evaluateActivationScripts(mockItem, null, null, action);
			fail("EvalError should be thrown");
		}
		catch (final EvalError ex)
		{
			// then OK
		}
	}
}
