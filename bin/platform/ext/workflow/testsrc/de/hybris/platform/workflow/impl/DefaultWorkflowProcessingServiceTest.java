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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.jalo.Workflow;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.strategies.ActionActivationStrategy;
import de.hybris.platform.workflow.strategies.DecideActionStrategy;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultWorkflowProcessingServiceTest
{

	private DefaultWorkflowProcessingService workflowProcessingService;

	@Mock
	private WorkflowModel workflow;

	@Mock
	private ModelService modelService;

	@Mock
	private WorkflowActionService workflowActionService;

	@Mock
	private WorkflowService workflowService;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private ActionActivationStrategy actionActivationStrategy;

	@Mock
	private DecideActionStrategy decideActionStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		workflowProcessingService = new DefaultWorkflowProcessingService();
		workflowProcessingService.setActionActivationStrategy(actionActivationStrategy);
		workflowProcessingService.setDecideActionStrategy(decideActionStrategy);
		workflowProcessingService.setFlexibleSearchService(flexibleSearchService);
		workflowProcessingService.setModelService(modelService);
		workflowProcessingService.setWorkflowActionService(workflowActionService);
		workflowProcessingService.setWorkflowService(workflowService);
	}

	@Test
	public void testStartWorkflow()
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getActionType()).thenReturn(WorkflowActionType.START);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);
		org.mockito.Mockito.doNothing().when(actionActivationStrategy).doAfterActivation(mockWorkflowAction);
		//when(Boolean.valueOf(workflowProcessingService.activate(mockWorkflowAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean started = workflowProcessingService.startWorkflow(workflow);

		//then
		assertThat(started).isTrue();
		verify(mockWorkflowSource).setPausedStatus();
		//verify(mockWorkflowSource).setEndTime((Date) Mockito.anyObject());
	}

	@Test
	public void testStartWorkflowNotStarted()
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getActionType()).thenReturn(WorkflowActionType.NORMAL);
		final WorkflowActionModel mockPredecessor = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getPredecessors()).thenReturn((List) Collections.singletonList(mockPredecessor));
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);


		//when
		final boolean started = workflowProcessingService.startWorkflow(workflow);

		//then
		assertThat(started).isFalse();
		verify(mockWorkflowSource).setPausedStatus();
	}

	@Test
	public void testEndWorkflowForInProgress()
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getStatus()).thenReturn(WorkflowActionStatus.IN_PROGRESS);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when(Boolean.valueOf(workflowProcessingService.activate(mockWorkflowAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean ended = workflowProcessingService.endWorkflow(workflow);

		//then
		assertThat(ended).isTrue();
		verify(mockWorkflowSource).setEndTime((Date) Mockito.anyObject());
		verify(mockWorkflowSource).setFinishedStatus();
		verify(mockWorkflowAction).setStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
	}

	@Test
	public void testEndWorkflowForPending()
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getStatus()).thenReturn(WorkflowActionStatus.PENDING);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when(Boolean.valueOf(workflowProcessingService.activate(mockWorkflowAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean ended = workflowProcessingService.endWorkflow(workflow);

		//then
		assertThat(ended).isTrue();
		verify(mockWorkflowSource).setEndTime((Date) Mockito.anyObject());
		verify(mockWorkflowSource).setFinishedStatus();
		verify(mockWorkflowAction).setStatus(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
	}

	@Test
	public void testEndWorkflowAndFailByStatus_ENDED_THROUGH_END_OF_WORKFLOW() //NOPMD
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getStatus()).thenReturn(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when(Boolean.valueOf(workflowProcessingService.activate(mockWorkflowAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean ended = workflowProcessingService.endWorkflow(workflow);

		//then
		assertThat(ended).isFalse();
		verify(mockWorkflowSource).setEndTime((Date) Mockito.anyObject());
	}

	@Test
	public void testEndWorkflowAndFailByWrongStatuses()
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getStatus()).thenReturn(WorkflowActionStatus.COMPLETED).thenReturn(WorkflowActionStatus.DISABLED)
				.thenReturn(WorkflowActionStatus.PAUSED).thenReturn(WorkflowActionStatus.TERMINATED);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when(Boolean.valueOf(workflowProcessingService.activate(mockWorkflowAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean ended1 = workflowProcessingService.endWorkflow(workflow);
		final boolean ended2 = workflowProcessingService.endWorkflow(workflow);
		final boolean ended3 = workflowProcessingService.endWorkflow(workflow);
		final boolean ended4 = workflowProcessingService.endWorkflow(workflow);

		//then
		assertThat(ended1).isFalse();
		assertThat(ended2).isFalse();
		assertThat(ended3).isFalse();
		assertThat(ended4).isFalse();
		verify(mockWorkflowSource, times(4)).setEndTime((Date) Mockito.anyObject());
	}

	@Test
	public void testEndWorkflowAndFailBecauseOfNoActions()
	{
		//given
		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);
		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when
		final boolean ended = workflowProcessingService.endWorkflow(workflow);

		//then
		assertThat(ended).isFalse();
		verify(mockWorkflowSource).setEndTime((Date) Mockito.anyObject());
	}

	@Test
	public void testTerminateWorkflowForInProgress()
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getStatus()).thenReturn(WorkflowActionStatus.IN_PROGRESS);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when(Boolean.valueOf(workflowProcessingService.activate(mockWorkflowAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean ended = workflowProcessingService.terminateWorkflow(workflow);

		//then
		assertThat(ended).isTrue();
		verify(mockWorkflowSource).setEndTime((Date) Mockito.anyObject());
		verify(mockWorkflowSource).setAbortedStatus();
		verify(mockWorkflowAction).setStatus(WorkflowActionStatus.TERMINATED);
	}

	@Test
	public void testTerminateWorkflowForPending()
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getStatus()).thenReturn(WorkflowActionStatus.PENDING);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when(Boolean.valueOf(workflowProcessingService.activate(mockWorkflowAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean ended = workflowProcessingService.terminateWorkflow(workflow);

		//then
		assertThat(ended).isTrue();
		verify(mockWorkflowSource).setEndTime((Date) Mockito.anyObject());
		verify(mockWorkflowSource).setAbortedStatus();
		verify(mockWorkflowAction).setStatus(WorkflowActionStatus.TERMINATED);
	}

	@Test
	public void testTerminateWorkflowAndFailByStatus_ENDED_THROUGH_END_OF_WORKFLOW() //NOPMD
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getStatus()).thenReturn(WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when(Boolean.valueOf(workflowProcessingService.activate(mockWorkflowAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean ended = workflowProcessingService.terminateWorkflow(workflow);

		//then
		assertThat(ended).isFalse();
		verify(mockWorkflowSource).setEndTime((Date) Mockito.anyObject());
	}

	@Test
	public void testTerminateWorkflowAndFailByWrongStatuses()
	{
		//given
		final WorkflowActionModel mockWorkflowAction = mock(WorkflowActionModel.class);
		when(mockWorkflowAction.getStatus()).thenReturn(WorkflowActionStatus.COMPLETED).thenReturn(WorkflowActionStatus.DISABLED)
				.thenReturn(WorkflowActionStatus.PAUSED).thenReturn(WorkflowActionStatus.TERMINATED);
		when(workflow.getActions()).thenReturn(Collections.singletonList(mockWorkflowAction));

		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when(Boolean.valueOf(workflowProcessingService.activate(mockWorkflowAction))).thenReturn(Boolean.TRUE);

		//when
		final boolean ended1 = workflowProcessingService.terminateWorkflow(workflow);
		final boolean ended2 = workflowProcessingService.terminateWorkflow(workflow);
		final boolean ended3 = workflowProcessingService.terminateWorkflow(workflow);
		final boolean ended4 = workflowProcessingService.terminateWorkflow(workflow);

		//then
		assertThat(ended1).isFalse();
		assertThat(ended2).isFalse();
		assertThat(ended3).isFalse();
		assertThat(ended4).isFalse();
		verify(mockWorkflowSource, times(4)).setEndTime((Date) Mockito.anyObject());
	}

	@Test
	public void testTerminateWorkflowAndFailBecauseOfNoActions()
	{
		//given
		when(workflow.getActions()).thenReturn(Collections.EMPTY_LIST);
		final Workflow mockWorkflowSource = mock(Workflow.class);
		when(modelService.getSource(workflow)).thenReturn(mockWorkflowSource);

		//when
		final boolean ended = workflowProcessingService.terminateWorkflow(workflow);

		//then
		assertThat(ended).isFalse();
		verify(mockWorkflowSource).setEndTime((Date) Mockito.anyObject());
	}

	@Test
	public void testToggleActions()
	{
		//given
		when(workflow.getStatus()).thenReturn(CronJobStatus.ABORTED).thenReturn(CronJobStatus.PAUSED)
				.thenReturn(CronJobStatus.RUNNINGRESTART).thenReturn(CronJobStatus.UNKNOWN);
		when(Boolean.valueOf(workflowService.isCompleted(workflow))).thenReturn(Boolean.TRUE);

		for (int i = 0; i < 4; i++)
		{
			//when
			final boolean isToggled = workflowProcessingService.toggleActions(workflow);

			//then
			assertThat(isToggled).isTrue();
		}
	}

	@Test
	public void testToggleActionsAndFail()
	{
		//given
		when(workflow.getStatus()).thenReturn(CronJobStatus.FINISHED).thenReturn(CronJobStatus.RUNNING);

		for (int i = 0; i < 2; i++)
		{
			//when
			final boolean isToggled = workflowProcessingService.toggleActions(workflow);

			//then
			assertThat(isToggled).isFalse();
		}
	}
}
