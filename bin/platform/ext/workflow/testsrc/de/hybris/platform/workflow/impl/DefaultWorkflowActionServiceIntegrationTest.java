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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commons.enums.RendererTypeEnum;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;


@IntegrationTest
public class DefaultWorkflowActionServiceIntegrationTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;

	@Resource
	private WorkflowActionService workflowActionService;

	@Resource
	private UserService userService;

	@Test
	public void testCreateWorkflowActionAssignment()
	{

		userService.setCurrentUser(userService.getAdminUser());

		final WorkflowTemplateModel workflowTemplate = modelService.create(WorkflowTemplateModel.class);
		workflowTemplate.setCode("testWorkflowTemplate");
		modelService.save(workflowTemplate);

		final WorkflowModel workflow = modelService.create(WorkflowModel.class);
		workflow.setCode("testWorkflow");
		workflow.setJob(workflowTemplate);
		modelService.save(workflow);

		final RendererTemplateModel rendererTemplate = modelService.create(RendererTemplateModel.class);
		rendererTemplate.setCode("testRendererTemplate");
		rendererTemplate.setRendererType(RendererTypeEnum.VELOCITY);
		modelService.save(rendererTemplate);

		final WorkflowActionTemplateModel template = modelService.create(WorkflowActionTemplateModel.class);

		template.setCode("testWorkflowTemplate");
		template.setWorkflow(workflowTemplate);

		template.setName("testName");
		template.setDescription("testDescription");
		template.setPrincipalAssigned(userService.getCurrentUser());
		template.setSendEmail(Boolean.TRUE);
		template.setEmailAddress("test@email.de");
		template.setRendererTemplate(rendererTemplate);
		template.setActionType(WorkflowActionType.NORMAL);

		modelService.save(template);

		final WorkflowActionModel mockAction = modelService.create(WorkflowActionModel.class);
		mockAction.setWorkflow(workflow);
		mockAction.setCode("testActionModel");
		mockAction.setTemplate(template);
		modelService.save(mockAction);

		final WorkflowActionModel action = workflowActionService.createWorkflowAction(template, workflow);

		Assert.assertEquals(template.getName(), action.getName());
		Assert.assertEquals(template.getPrincipalAssigned(), action.getPrincipalAssigned());
		Assert.assertEquals(template.getSendEmail(), action.getSendEmail());
		Assert.assertEquals(template.getEmailAddress(), action.getEmailAddress());
		Assert.assertEquals(template.getRendererTemplate(), action.getRendererTemplate());
		Assert.assertEquals(template.getActionType(), action.getActionType());
		Assert.assertEquals(workflow, action.getWorkflow());
		Assert.assertEquals(template, action.getTemplate());

	}

}
