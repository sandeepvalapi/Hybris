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
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.daos.WorkflowDao;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import de.hybris.platform.workflow.services.internal.WorkflowFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultWorkflowServiceTest
{
	private DefaultWorkflowService workflowService;

	@Mock
	private ModelService modelService;

	@Mock
	private WorkflowDao workflowDao;

	@Mock
	private WorkflowActionService workflowActionService;

	@Mock
	private WorkflowTemplateService workflowTemplateService;

	@Mock
	private WorkflowModel workflow;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		workflowService = new DefaultWorkflowService();
		workflowService.setModelService(modelService);
		workflowService.setWorkflowActionService(workflowActionService);
		workflowService.setWorkflowDao(workflowDao);
		workflowService.setWorkflowTemplateService(workflowTemplateService);
	}

	@Test
	public void testIsAdhocWorkflowPositive()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate);

		//when
		final boolean isAdhocWorkflow = workflowService.isAdhocWorkflow(workflow);

		//then
		assertThat(isAdhocWorkflow).isTrue();
	}

	@Test
	public void testIsAdhocWorkflowNegative1()
	{
		//given
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(null);

		//when
		final boolean isAdhocWorkflow = workflowService.isAdhocWorkflow(workflow);

		//then
		assertThat(isAdhocWorkflow).isFalse();
	}

	@Test
	public void testIsAdhocWorkflowNegative2()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(null);

		//when
		final boolean isAdhocWorkflow = workflowService.isAdhocWorkflow(workflow);

		//then
		assertThat(isAdhocWorkflow).isFalse();
	}

	@Test
	public void testIsAdhocWorkflowNegative3()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		final WorkflowTemplateModel mockWorkflowTemplate2 = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate2);

		//when
		final boolean isAdhocWorkflow = workflowService.isAdhocWorkflow(workflow);

		//then
		assertThat(isAdhocWorkflow).isFalse();
	}

	@Test
	public void testCanBeStartedPositive1()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		final WorkflowTemplateModel mockWorkflowTemplate2 = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate2);

		//when
		final boolean canBeStarted = workflowService.canBeStarted(workflow);

		//then
		assertThat(canBeStarted).isTrue();
	}

	@Test
	public void testCanBeStartedPositive2()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate);

		final EmployeeModel mockUser = mock(EmployeeModel.class);
		final EmployeeModel mockUser2 = mock(EmployeeModel.class);
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getPrincipalAssigned()).thenReturn(mockUser);
		when(workflowActionService.getStartWorkflowActions(workflow)).thenReturn(Collections.singletonList(mockWorkflowAction));
		when(workflowTemplateService.getAdhocWorkflowTemplateDummyOwner()).thenReturn(mockUser2);

		//when
		final boolean canBeStarted = workflowService.canBeStarted(workflow);

		//then
		assertThat(canBeStarted).isTrue();
	}

	@Test
	public void testCanBeStartedNegative1()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate);

		final EmployeeModel mockUser = mock(EmployeeModel.class);
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getPrincipalAssigned()).thenReturn(mockUser);
		when(workflowActionService.getStartWorkflowActions(workflow)).thenReturn(Collections.singletonList(mockWorkflowAction));
		when(workflowTemplateService.getAdhocWorkflowTemplateDummyOwner()).thenReturn(mockUser);

		//when
		final boolean canBeStarted = workflowService.canBeStarted(workflow);

		//then
		assertThat(canBeStarted).isFalse();
	}

	@Test
	public void testCanBeStartedNegative2()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate);

		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getPrincipalAssigned()).thenReturn(null);
		when(workflowActionService.getStartWorkflowActions(workflow)).thenReturn(Collections.singletonList(mockWorkflowAction));

		//when
		final boolean canBeStarted = workflowService.canBeStarted(workflow);

		//then
		assertThat(canBeStarted).isFalse();
	}

	@Test
	public void testCanBeStartedNegative3()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate);

		when(workflowActionService.getStartWorkflowActions(workflow)).thenReturn(Collections.EMPTY_LIST);

		//when
		final boolean canBeStarted = workflowService.canBeStarted(workflow);

		//then
		assertThat(canBeStarted).isFalse();
	}

	@Test
	public void testGetStartTime()
	{
		//given
		final Date mockDate = mock(Date.class);
		when(workflow.getStartTime()).thenReturn(mockDate);

		//when
		final Date startDate = workflowService.getStartTime(workflow);

		//then
		assertThat(startDate).isSameAs(mockDate);
	}

	@Test
	public void testGetStartTimeGivenFromAction()
	{
		//given
		final Date mockDate = mock(Date.class);
		when(workflow.getStartTime()).thenReturn(null);
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getFirstActivated()).thenReturn(mockDate);
		when(workflowActionService.getStartWorkflowActions(workflow)).thenReturn(Collections.singletonList(mockWorkflowAction));

		//when
		final Date startDate = workflowService.getStartTime(workflow);

		//then
		assertThat(startDate).isSameAs(mockDate);
	}

	@Test
	public void testGetStartTimeReturnsNullIfNoActionsAndNoStartDateSet()
	{
		//given
		when(workflow.getStartTime()).thenReturn(null);
		when(workflowActionService.getStartWorkflowActions(workflow)).thenReturn(Collections.EMPTY_LIST);

		//when
		final Date startDate = workflowService.getStartTime(workflow);

		//then
		assertThat(startDate).isNull();
	}

	@Test
	public void testAssignUserAndSucceed()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));
		final PrincipalModel mockPrincipal = mock(PrincipalModel.class);

		//when
		final boolean assigned = workflowService.assignUser(mockPrincipal, workflow);

		//then
		assertThat(assigned).isTrue();
		verify(mockAction).setPrincipalAssigned(mockPrincipal);
	}

	@Test
	public void testAssignUserAndSucceed2()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate);

		final PrincipalModel mockPrincipal = mock(PrincipalModel.class);
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(workflowActionService.getStartWorkflowActions(workflow)).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean assigned = workflowService.assignUser(mockPrincipal, workflow);

		//then
		assertThat(assigned).isTrue();
		verify(mockAction).setPrincipalAssigned(mockPrincipal);
	}

	@Test
	public void testAssignUserAndFailBecauseOfNoActionsAssigned()
	{
		//given
		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);
		final PrincipalModel mockPrincipal = mock(PrincipalModel.class);

		//when
		final boolean assigned = workflowService.assignUser(mockPrincipal, workflow);

		//then
		assertThat(assigned).isFalse();
	}

	@Test
	public void testAssignUserAndFailBecauseOfNoStartAction()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate);

		final PrincipalModel mockPrincipal = mock(PrincipalModel.class);
		when(workflowActionService.getStartWorkflowActions(workflow)).thenReturn(Collections.EMPTY_LIST);

		//when
		final boolean assigned = workflowService.assignUser(mockPrincipal, workflow);

		//then
		assertThat(assigned).isFalse();
	}

	@Test
	public void testUnassignUserAndSucceed()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate);

		final EmployeeModel mockPrincipal = mock(EmployeeModel.class);
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));
		when(workflowTemplateService.getAdhocWorkflowTemplateDummyOwner()).thenReturn(mockPrincipal);

		//when
		final boolean assigned = workflowService.unassignUser(workflow);

		//then
		assertThat(assigned).isTrue();
		verify(mockAction).setPrincipalAssigned(mockPrincipal);
	}

	@Test
	public void testUnassignUserAndFail1()
	{
		//given
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(null);

		//when
		final boolean assigned = workflowService.unassignUser(workflow);

		//then
		assertThat(assigned).isFalse();
	}

	@Test
	public void testUnassignUserAndFail2()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		when(workflow.getJob()).thenReturn(mockWorkflowTemplate);

		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);

		//when
		final boolean assigned = workflowService.unassignUser(workflow);

		//then
		assertThat(assigned).isFalse();
	}

	@Test
	public void testIsCompletedAndSucceed1()
	{
		//given
		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);

		//when
		final boolean isCompleted = workflowService.isCompleted(workflow);

		//then
		assertThat(isCompleted).isTrue();
	}

	@Test
	public void testIsCompletedAndSucceed2()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));
		when(Boolean.valueOf(workflowActionService.isCompleted(mockAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean isCompleted = workflowService.isCompleted(workflow);

		//then
		assertThat(isCompleted).isTrue();
	}

	@Test
	public void testIsCompletedAndFail()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));
		when(Boolean.valueOf(workflowActionService.isCompleted(mockAction))).thenReturn(Boolean.FALSE);

		//when
		final boolean isCompleted = workflowService.isCompleted(workflow);

		//then
		assertThat(isCompleted).isFalse();
	}

	@Test
	public void testIsFinishedAndSucceed()
	{
		//given
		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);

		//when
		final boolean isFinished = workflowService.isFinished(workflow);

		//then
		assertThat(isFinished).isTrue();
	}

	@Test
	public void testIsFinishedAndSucceed2()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isFinished = workflowService.isFinished(workflow);

		//then
		assertThat(isFinished).isTrue();
	}

	@Test
	public void testIsFinishedAndFail()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.COMPLETED);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isFinished = workflowService.isFinished(workflow);

		//then
		assertThat(isFinished).isFalse();
	}

	@Test
	public void testIsPausedAndSucceed()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.PAUSED);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isPaused = workflowService.isPaused(workflow);

		//then
		assertThat(isPaused).isTrue();
	}

	@Test
	public void testIsPausedAndFail()
	{
		//given
		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);

		//when
		final boolean isPaused = workflowService.isPaused(workflow);

		//then
		assertThat(isPaused).isFalse();
	}

	@Test
	public void testIsPausedAndFail2()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.COMPLETED);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isPaused = workflowService.isPaused(workflow);

		//then
		assertThat(isPaused).isFalse();
	}

	@Test
	public void testIsPlannedAndSucceed()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.PENDING);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isPlanned = workflowService.isPlanned(workflow);

		//then
		assertThat(isPlanned).isTrue();
	}

	@Test
	public void testIsPlannedAndSucceed2()
	{
		//given
		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);

		//when
		final boolean isPlanned = workflowService.isPlanned(workflow);

		//then
		assertThat(isPlanned).isTrue();
	}

	@Test
	public void testIsPlannedAndFail1()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.COMPLETED);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isPlanned = workflowService.isPlanned(workflow);

		//then
		assertThat(isPlanned).isFalse();
	}

	@Test
	public void testIsRunningAndSucceed1()
	{
		//given
		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);

		//when
		final boolean isRunning = workflowService.isRunning(workflow);

		//then
		assertThat(isRunning).isFalse();
	}

	@Test
	public void testIsRunningAndSucceed2()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.PENDING);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isRunning = workflowService.isRunning(workflow);

		//then
		assertThat(isRunning).isFalse();
	}

	@Test
	public void testIsRunningAndFail()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.COMPLETED);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isRunning = workflowService.isRunning(workflow);

		//then
		assertThat(isRunning).isFalse();
	}

	@Test
	public void testIsTerminatedAndSucceed()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.TERMINATED);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isTerminated = workflowService.isTerminated(workflow);

		//then
		assertThat(isTerminated).isTrue();
	}

	@Test
	public void testIsTerminatedAndFail()
	{
		//given
		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);

		//when
		final boolean isTerminated = workflowService.isTerminated(workflow);

		//then
		assertThat(isTerminated).isFalse();
	}

	@Test
	public void testIsTerminatedAndFail2()
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getStatus()).thenReturn(WorkflowActionStatus.COMPLETED);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));

		//when
		final boolean isTerminated = workflowService.isTerminated(workflow);

		//then
		assertThat(isTerminated).isFalse();
	}

	@Test
	//TODO: to improve
	public void testCreateWorkflowFromTemplate()
	{
		//given
		final WorkflowTemplateModel mockTemplate = mock(WorkflowTemplateModel.class);
		when(modelService.create(WorkflowModel.class)).thenReturn(workflow);
		final UserModel user = mock(UserModel.class);
		final WorkflowFactory factory = mock(WorkflowFactory.class);
		workflowService.setActionsWorkflowTemplateFactory(factory);

		//when
		final WorkflowModel workflow = workflowService.createWorkflow(mockTemplate, user);

		//then
		assertThat(workflow).isNotNull();
	}

	@Test
	//TODO: to improve
	public void testCreateWorkflowFromTemplate2()
	{
		//given
		final WorkflowTemplateModel mockWorkflowTemplate = mock(WorkflowTemplateModel.class);
		when(workflowTemplateService.getAdhocWorkflowTemplate()).thenReturn(mockWorkflowTemplate);
		final WorkflowActionModel mockNormalWorkflowAction = mock(WorkflowActionModel.class);
		when(workflowActionService.getNormalWorkflowActions(workflow)).thenReturn(
				Collections.singletonList(mockNormalWorkflowAction));
		final WorkflowActionModel mockEndWorkflowAction = mock(WorkflowActionModel.class);
		when(workflowActionService.getEndWorkflowActions(workflow)).thenReturn(Collections.singletonList(mockEndWorkflowAction));
		when(modelService.create(WorkflowModel.class)).thenReturn(workflow);
		final UserModel user = mock(UserModel.class);
		when(workflow.getActions()).thenReturn(Arrays.asList(mockNormalWorkflowAction, mockEndWorkflowAction));
		final WorkflowItemAttachmentModel mockAtt = mock(WorkflowItemAttachmentModel.class);
		when(modelService.create(WorkflowItemAttachmentModel.class)).thenReturn(mockAtt);
		final WorkflowFactory factory = mock(WorkflowFactory.class);
		workflowService.setActionsWorkflowTemplateFactory(factory);
		//when
		final WorkflowModel workflow = workflowService.createAdhocWorkflow("testName",
				Collections.singletonList(mock(ItemModel.class)), user);

		//then
		assertThat(workflow).isNotNull();
		verify(mockNormalWorkflowAction).setPrincipalAssigned(user);
		verify(mockEndWorkflowAction).setPrincipalAssigned(user);
	}

	@Test
	//TODO: to improve
	public void testCreateWorkflowFromTemplateAttachmentAndUser()
	{
		//given
		final WorkflowTemplateModel mockTemplate = mock(WorkflowTemplateModel.class);
		when(modelService.create(WorkflowModel.class)).thenReturn(workflow);
		final UserModel user = mock(UserModel.class);
		final ItemModel att = mock(ItemModel.class);
		final WorkflowItemAttachmentModel mockAtt = mock(WorkflowItemAttachmentModel.class);
		when(modelService.create(WorkflowItemAttachmentModel.class)).thenReturn(mockAtt);

		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockAction));
		final WorkflowFactory factory = mock(WorkflowFactory.class);
		workflowService.setActionsWorkflowTemplateFactory(factory);
		//when
		final WorkflowModel workflow = workflowService.createWorkflow(mockTemplate, att, user);

		//then
		assertThat(workflow).isNotNull();
		verify(workflow).setAttachments(anyListOf(WorkflowItemAttachmentModel.class));
		verify(mockAction).setAttachments(anyListOf(WorkflowItemAttachmentModel.class));
	}

	@Test
	public void testGetWorkflowsForTemplateAndUser()
	{
		//given
		final WorkflowTemplateModel mockTemplate = mock(WorkflowTemplateModel.class);
		final UserModel mockUser = mock(UserModel.class);
		final List<WorkflowModel> mockWorkflows = new ArrayList<WorkflowModel>();
		mockWorkflows.add(mock(WorkflowModel.class));
		mockWorkflows.add(mock(WorkflowModel.class));
		when(workflowDao.findWorkflowsByUserAndTemplate(mockUser, mockTemplate)).thenReturn(mockWorkflows);

		//when
		final List<WorkflowModel> workflows = workflowService.getWorkflowsForTemplateAndUser(mockTemplate, mockUser);

		//then
		assertThat(workflows).hasSize(2);
		assertThat(workflows).isSameAs(mockWorkflows);
	}

	@Test
	public void testGetAllAdhocWorkflows()
	{
		//given
		final WorkflowModel workflowFinished = getWorkflowWithStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);

		final WorkflowModel workflowAborted = getWorkflowWithStatus(WorkflowActionStatus.PENDING);

		final WorkflowModel workflowPaused = getWorkflowWithStatus(WorkflowActionStatus.TERMINATED);


		final WorkflowModel workflowRunning = getWorkflowWithStatus(WorkflowActionStatus.IN_PROGRESS);


		final List<WorkflowModel> mockWorkflows = new ArrayList<WorkflowModel>();
		mockWorkflows.add(workflowFinished);
		mockWorkflows.add(workflowAborted);
		mockWorkflows.add(workflowPaused);
		mockWorkflows.add(workflowRunning);

		when(workflowDao.findAllAdhocWorkflows(null, null)).thenReturn(mockWorkflows);

		//when
		final List<WorkflowModel> workflowsFinished = workflowService.getAllAdhocWorkflows(EnumSet.of(WorkflowStatus.FINISHED),
				null, null);
		final List<WorkflowModel> workflowsPlanned = workflowService.getAllAdhocWorkflows(EnumSet.of(WorkflowStatus.PLANNED), null,
				null);
		final List<WorkflowModel> workflowsPaused = workflowService.getAllAdhocWorkflows(EnumSet.of(WorkflowStatus.TERMINATED),
				null, null);
		final List<WorkflowModel> workflowsRunning = workflowService.getAllAdhocWorkflows(EnumSet.of(WorkflowStatus.RUNNING), null,
				null);

		//then
		assertThat(workflowsFinished).hasSize(1);
		assertThat(workflowsPlanned).hasSize(1);
		assertThat(workflowsRunning).hasSize(1);
		assertThat(workflowsPaused).hasSize(1);

	}

	private WorkflowModel getWorkflowWithStatus(final WorkflowActionStatus status)
	{
		final WorkflowModel workflowPaused = mock(WorkflowModel.class);
		final WorkflowActionModel mockPausedAction = mock(WorkflowActionModel.class);
		when(mockPausedAction.getStatus()).thenReturn(status);
		when(workflowPaused.getActions()).thenReturn(Collections.singletonList(mockPausedAction));
		return workflowPaused;
	}

	@Test
	public void testGetAllWorkflows()
	{
		//given
		final WorkflowModel workflowFinished = getWorkflowWithStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);

		final WorkflowModel workflowAborted = getWorkflowWithStatus(WorkflowActionStatus.PENDING);

		final WorkflowModel workflowPaused = getWorkflowWithStatus(WorkflowActionStatus.TERMINATED);


		final WorkflowModel workflowRunning = getWorkflowWithStatus(WorkflowActionStatus.IN_PROGRESS);


		final List<WorkflowModel> mockWorkflows = new ArrayList<WorkflowModel>();
		mockWorkflows.add(workflowFinished);
		mockWorkflows.add(workflowAborted);
		mockWorkflows.add(workflowPaused);
		mockWorkflows.add(workflowRunning);

		when(workflowDao.findAllWorkflows(null, null)).thenReturn(mockWorkflows);

		//when
		final List<WorkflowModel> workflowsFinished = workflowService.getAllWorkflows(WorkflowStatus.getAll(), null, null);

		//then
		assertThat(workflowsFinished).hasSize(4);

	}
}
