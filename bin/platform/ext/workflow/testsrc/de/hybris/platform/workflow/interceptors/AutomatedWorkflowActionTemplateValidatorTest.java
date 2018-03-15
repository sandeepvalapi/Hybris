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
package de.hybris.platform.workflow.interceptors;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


@IntegrationTest
public class AutomatedWorkflowActionTemplateValidatorTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	private AutomatedWorkflowActionTemplateModel template;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		createCoreData();
		createDefaultUsers();
		template = modelService.create(AutomatedWorkflowActionTemplateModel.class);
		template.setPrincipalAssigned(userService.getAdminUser());
		template.setWorkflow(createWorkflowTemplate());
		template.setCode("FooBar");
	}

	@Test
	public void shouldThrowInterceptorExceptionWhenBothJobClassAndJobHandlerAreSet()
	{
		// given
		template.setJobClass(AutomatedWorkflowTemplateJobTest.class);
		template.setJobHandler("FooBar");

		try
		{
			// when
			modelService.save(template);
			fail("Interceptor exception should be thrown");
		}
		catch (final ModelSavingException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
			assertThat(e.getMessage()).contains(
					"Both " + AutomatedWorkflowActionTemplateModel.JOBHANDLER + " and "
							+ AutomatedWorkflowActionTemplateModel.JOBCLASS + " are used, use only one");
		}
	}

	@Test
	public void shouldThrowInterceptorExceptionWhenBothJobClassAndJobHandlerAreNull()
	{
		// given
		template.setJobClass(null);
		template.setJobHandler(null);

		try
		{
			// when
			modelService.save(template);
			fail("Interceptor exception should be thrown");
		}
		catch (final ModelSavingException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
			assertThat(e.getMessage()).contains(AutomatedWorkflowActionTemplateModel.JOBHANDLER + " is required");
		}
	}

	@Test
	public void shouldThrowInterceptorExceptionWhenJobClassIsSetButWithWrongClass()
	{
		// given
		template.setJobClass(String.class); // here should be set class which implements AutomatedWorkflowTemplateJob interface
		template.setJobHandler(null);

		try
		{
			// when
			modelService.save(template);
			fail("Interceptor exception should be thrown");
		}
		catch (final ModelSavingException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
			assertThat(e.getMessage())
					.contains(
							"Only a class which implements de.hybris.platform.workflow.jalo.AutomatedWorkflowTemplateJob or de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob interface is allowed as "
									+ AutomatedWorkflowActionTemplateModel.JOBCLASS);
		}
	}

	@Test
	public void shouldPassValidationWhenJobClassIsSetAndJobHandlerIsNull()
	{
		// given
		template.setJobClass(AutomatedWorkflowTemplateJobTest.class);
		template.setJobHandler(null);

		// when
		modelService.save(template);

		// then
		assertThat(modelService.isNew(template)).isFalse();
	}

	@Test
	public void shouldPassValidationWhenJobHandlerIsSetAndJobClassIsNull()
	{
		// given
		template.setJobClass(null);
		template.setJobHandler("FooBar");

		// when
		modelService.save(template);

		// then
		assertThat(modelService.isNew(template)).isFalse();
	}

	private WorkflowTemplateModel createWorkflowTemplate()
	{
		final WorkflowTemplateModel template = modelService.create(WorkflowTemplateModel.class);
		template.setOwner(userService.getAdminUser());
		template.setCode("FooBar");
		template.setDescription("FooBar");
		template.setActivationScript("Some script");

		modelService.save(template);
		return template;
	}

	private class AutomatedWorkflowTemplateJobTest implements AutomatedWorkflowTemplateJob
	{

		@Override
		public WorkflowDecisionModel perform(final WorkflowActionModel action)
		{
			return null;
		}
	}

}
