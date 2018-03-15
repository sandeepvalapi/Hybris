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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.exceptions.WorkflowTerminatedException;
import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultWorkflowActionCommentServiceTest
{
	private DefaultWorkflowActionCommentService workflowActionCommentService;

	@Mock
	private WorkflowService workflowService;

	@Mock
	private ModelService modelService;

	@Mock
	private UserService userService;

	@Mock
	private WorkflowActionModel mockAction;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		workflowActionCommentService = new DefaultWorkflowActionCommentService();
		workflowActionCommentService.setModelService(modelService);
		workflowActionCommentService.setUserService(userService);
		workflowActionCommentService.setWorkflowService(workflowService);
	}

	@Test
	public void testAddCommentToActionAndSucceed()
	{
		//given
		final WorkflowActionCommentModel mockComment = mock(WorkflowActionCommentModel.class);
		when(modelService.create(WorkflowActionCommentModel.class)).thenReturn(mockComment);

		//when
		final WorkflowActionCommentModel comment = workflowActionCommentService.addCommentToAction("my test comment", mockAction);

		//then
		assertThat(comment).isNotNull();
		assertThat(comment).isSameAs(mockComment);
		verify(comment).setComment("my test comment");
	}

	@Test
	public void testAddCommentToActionAndThrowException()
	{
		//given
		final WorkflowModel mockWorkflow = mock(WorkflowModel.class);
		when(mockAction.getWorkflow()).thenReturn(mockWorkflow);
		when(Boolean.valueOf(workflowService.isTerminated(mockWorkflow))).thenReturn(Boolean.TRUE);

		//when
		try
		{
			workflowActionCommentService.addCommentToAction("my test comment", mockAction);
			fail("Should throw WorkflowTerminatedException");
		}
		catch (final WorkflowTerminatedException ex)
		{
			//then OK
		}
	}

	@Test
	public void testIsAutomatedCommentAndReturnTrue()
	{
		//given
		final WorkflowActionCommentModel mockComment = mock(WorkflowActionCommentModel.class);
		when(mockComment.getUser()).thenReturn(null);

		//when
		final boolean automatedComment = workflowActionCommentService.isAutomatedComment(mockComment);

		//then
		assertThat(automatedComment).isTrue();
	}

	@Test
	public void testIsAutomatedCommentAndReturnFalse()
	{
		//given
		final WorkflowActionCommentModel mockComment = mock(WorkflowActionCommentModel.class);
		final UserModel user = mock(UserModel.class);
		when(mockComment.getUser()).thenReturn(user);

		//when
		final boolean automatedComment = workflowActionCommentService.isAutomatedComment(mockComment);

		//then
		assertThat(automatedComment).isFalse();
	}
}
