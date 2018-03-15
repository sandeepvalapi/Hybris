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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.workflow.exceptions.AutomatedWorkflowTemplateException;
import de.hybris.platform.workflow.integration.WorkflowAutomatedAction;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.services.internal.impl.DefaultAutomatedWorkflowTemplateRegistry.AutomatedWorkflowTemplateJobWrapper;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultAutomatedWorkflowTemplateRegistryTest
{
	private final DefaultAutomatedWorkflowTemplateRegistry registry = new DefaultAutomatedWorkflowTemplateRegistry();
	@Mock
	private AutomatedWorkflowActionTemplateModel template;
	private WorkflowAutomatedAction springManagedFakeAction;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		final Map<String, AutomatedWorkflowTemplateJob> automatedWorkflows = new HashMap<String, AutomatedWorkflowTemplateJob>();
		springManagedFakeAction = new WorkflowAutomatedAction();
		automatedWorkflows.put("workflowAutomatedAction", springManagedFakeAction);
		registry.setAutomatedWorkflows(automatedWorkflows);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.workflow.services.internal.impl.DefaultAutomatedWorkflowTemplateRegistry#getAutomatedWorkflowTemplateJobForTemplate(de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel)}
	 * .
	 * 
	 * @throws AutomatedWorkflowTemplateException
	 */
	@Test
	public void shouldReturnInstantiatedAutomatedWorkflowTemplateWhenTemplateHasOnlyJobClass()
			throws AutomatedWorkflowTemplateException
	{
		// given
		given(template.getJobHandler()).willReturn(null);
		given(template.getJobClass()).willReturn(WorkflowAutomatedAction.class);

		// when
		final AutomatedWorkflowTemplateJob automatedTemplate = registry.getAutomatedWorkflowTemplateJobForTemplate(template);

		// then
		assertThat(automatedTemplate).isNotNull();
		assertThat(automatedTemplate).isNotEqualTo(springManagedFakeAction);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.workflow.services.internal.impl.DefaultAutomatedWorkflowTemplateRegistry#getAutomatedWorkflowTemplateJobForTemplate(de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel)}
	 * .
	 * 
	 * @throws AutomatedWorkflowTemplateException
	 */
	@Test
	public void shouldReturnInstantiatedWrappedAutomatedWorkflowTemplateWhenTemplateHasOnlyJobClassFromJaloPackage()
			throws AutomatedWorkflowTemplateException
	{
		// given
		given(template.getJobHandler()).willReturn(null);
		given(template.getJobClass()).willReturn(DeprecatedWorkflowAction.class);

		// when
		final AutomatedWorkflowTemplateJob automatedTemplate = registry.getAutomatedWorkflowTemplateJobForTemplate(template);

		// then
		assertThat(automatedTemplate).isNotNull();
		assertThat(automatedTemplate).isNotEqualTo(springManagedFakeAction);
		assertThat(automatedTemplate).isInstanceOf(AutomatedWorkflowTemplateJobWrapper.class);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.workflow.services.internal.impl.DefaultAutomatedWorkflowTemplateRegistry#getAutomatedWorkflowTemplateJobForTemplate(de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel)}
	 * .
	 * 
	 * @throws AutomatedWorkflowTemplateException
	 */
	@Test
	public void shouldReturnAutomatedWorkflowTemplateFromSpringManagedMapWhenTemplateHasOnlyJobHandler()
			throws AutomatedWorkflowTemplateException
	{
		// given
		given(template.getJobHandler()).willReturn("workflowAutomatedAction");
		given(template.getJobClass()).willReturn(null);

		// when
		final AutomatedWorkflowTemplateJob automatedTemplate = registry.getAutomatedWorkflowTemplateJobForTemplate(template);

		// then
		assertThat(automatedTemplate).isNotNull();
		assertThat(automatedTemplate).isEqualTo(springManagedFakeAction);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.workflow.services.internal.impl.DefaultAutomatedWorkflowTemplateRegistry#getAutomatedWorkflowTemplateJobForTemplate(de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel)}
	 * .
	 * 
	 */
	@Test
	public void shouldThrowAutomatedWorkflowTemplateExceptionWhenNoAutomatedWorkflowTemplateWasFound()
	{
		// given
		given(template.getJobHandler()).willReturn("nonExistentBean");
		given(template.getJobClass()).willReturn(null);

		try
		{
			// when
			registry.getAutomatedWorkflowTemplateJobForTemplate(template);
			fail("AutomatedWorkflowTemplateException should be thrown");
		}
		catch (final AutomatedWorkflowTemplateException e)
		{
			// then
			assertThat(e.getMessage()).contains("No automated workflow has been found for workflow template: " + template);
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.workflow.services.internal.impl.DefaultAutomatedWorkflowTemplateRegistry#getAutomatedWorkflowTemplateJobForTemplate(de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel)}
	 * .
	 * 
	 */
	@Test
	public void shouldThrowAutomatedWorkflowTemplateExceptionWhenJobClassCannotBeInstantiated()
	{
		// given
		given(template.getJobHandler()).willReturn(null);
		given(template.getJobClass()).willReturn(ActionTemplate.class);

		try
		{
			// when
			registry.getAutomatedWorkflowTemplateJobForTemplate(template);
			fail("AutomatedWorkflowTemplateException should be thrown");
		}
		catch (final AutomatedWorkflowTemplateException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(InstantiationException.class);
			assertThat(e.getMessage()).contains("Cannot instantiate given class: " + template.getJobClass());
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.workflow.services.internal.impl.DefaultAutomatedWorkflowTemplateRegistry#getAutomatedWorkflowTemplateJobForTemplate(de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel)}
	 * .
	 * 
	 */
	@Test
	public void shouldThrowAutomatedWorkflowTemplateExceptionWhenJobClassCannotBeInstantiatedBecauseOfIllegalAccess()
	{
		// given
		given(template.getJobHandler()).willReturn(null);
		given(template.getJobClass()).willReturn(AnotherActionTemplate.class);

		try
		{
			// when
			registry.getAutomatedWorkflowTemplateJobForTemplate(template);
			fail("AutomatedWorkflowTemplateException should be thrown");
		}
		catch (final AutomatedWorkflowTemplateException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(IllegalAccessException.class);
			assertThat(e.getMessage()).contains("Cannot instantiate given class: " + template.getJobClass());
		}
	}

	private class ActionTemplate
	{
		// just for testing purposes
	}
}
