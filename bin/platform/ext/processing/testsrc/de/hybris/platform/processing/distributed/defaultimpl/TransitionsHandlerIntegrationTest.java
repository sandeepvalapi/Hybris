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
package de.hybris.platform.processing.distributed.defaultimpl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processing.distributed.ProcessConcurrentlyModifiedException;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.DistributedProcessTransitionTaskModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@IntegrationTest
public class TransitionsHandlerIntegrationTest extends ServicelayerBaseTest
{

	@Resource
	private ModelService modelService;

	@Mock
	Controller controller;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldFailWhenExecutingStaticTransition()
	{
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.CREATED);

		try
		{
			handler.runTransitionTask(task);
		}
		catch (final IllegalStateException e)
		{
			assertThat(e).isExactlyInstanceOf(IllegalStateException.class).hasNoCause();
			assertThat(e.getMessage()).isNotNull().contains("unexpected state");
			return;
		}
		fail("IllegalStateException was expected but nothing has been thrown.");
	}

	@Test
	public void shouldInitializeProcessWhenPerformingInitializeTransition()
	{
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.INITIALIZING);

		handler.runTransitionTask(task);

		verify(controller).initializeProcess(task.getContextItem());
		verifyNoMoreInteractions(controller);
	}

	@Test
	public void shouldStopProcessWhenPerformingInitializeTransitionButStopHasBeenRequested()
	{
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.INITIALIZING);
		task.getContextItem().setStopRequested(true);

		handler.runTransitionTask(task);

		verify(controller).stopProcess(task.getContextItem());
		verifyNoMoreInteractions(controller);
	}

	@Test
	public void shouldScheduleExecutionOfProcessProcessWhenPerformingSchedulingTransition()
	{
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.SCHEDULING_EXECUTION);

		handler.runTransitionTask(task);

		verify(controller).scheduleExecution(task.getContextItem());
		verifyNoMoreInteractions(controller);
	}

	@Test
	public void shouldStopProcessProcessWhenPerformingSchedulingTransitionButStopHasBeenRequested()
	{
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.SCHEDULING_EXECUTION);
		task.getContextItem().setStopRequested(true);

		handler.runTransitionTask(task);

		verify(controller).stopProcess(task.getContextItem());
		verifyNoMoreInteractions(controller);
	}

	@Test
	public void shouldAnalyseProcessExecutionWhenWaitForExecutionTaskIsExecuted()
	{
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.WAITING_FOR_EXECUTION);

		handler.runTransitionTask(task);

		verify(controller).analyseExecutionResults(task.getContextItem());
		verifyNoMoreInteractions(controller);
	}

	@Test
	public void shouldStopProcessWhenWaitForExecutionTaskIsExecutedButStopHasBeenRequested()
	{
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.WAITING_FOR_EXECUTION);
		task.getContextItem().setStopRequested(true);

		handler.runTransitionTask(task);

		verify(controller).stopProcess(task.getContextItem());
		verifyNoMoreInteractions(controller);
	}

	@Test
	public void shouldMarkTaskForRetryInCaseOfProcessConcurrentlyModifiedException()
	{
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.WAITING_FOR_EXECUTION);
		when(controller.analyseExecutionResults(notNull(DistributedProcessModel.class)))
				.thenThrow(ProcessConcurrentlyModifiedException.class);

		try
		{
			handler.runTransitionTask(task);
		}
		catch (final RetryLaterException e)
		{
			assertThat(e).isExactlyInstanceOf(RetryLaterException.class);
			assertThat(e.getCause()).isNotNull().isExactlyInstanceOf(ProcessConcurrentlyModifiedException.class);
			return;
		}
		fail("RetryLaterException was expected but nothing has been thrown.");
	}

	@Test
	public void shouldGiveUpRetringAfterFiveAttempts()
	{
		final ProcessConcurrentlyModifiedException exception = new ProcessConcurrentlyModifiedException(null);
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.WAITING_FOR_EXECUTION);
		when(controller.analyseExecutionResults(notNull(DistributedProcessModel.class))).thenThrow(exception);
		task.setRetry(Integer.valueOf(5));

		try
		{
			handler.runTransitionTask(task);
		}
		catch (final ProcessConcurrentlyModifiedException e)
		{
			assertThat(e).isSameAs(exception);
			return;
		}
		fail("ProcessConcurrentlyModifiedException was expected but nothing has been thrown.");
	}

	@Test
	public void shouldMarkProcessAsFailedWhenHandlingError()
	{
		final TransitionsHandler handler = givenHandler();
		final DistributedProcessTransitionTaskModel task = givenTransitionTask(DistributedProcessState.WAITING_FOR_EXECUTION);

		handler.handleErrorDuringTransition(task);

		final DistributedProcessModel process = task.getContextItem();
		assertThat(process.getState()).isEqualTo(DistributedProcessState.FAILED);
	}

	DistributedProcessTransitionTaskModel givenTransitionTask(final DistributedProcessState state)
	{
		final DistributedProcessModel process = modelService.create(DistributedProcessModel.class);
		process.setCode(UUID.randomUUID().toString());
		process.setCurrentExecutionId("EX_ID");
		process.setState(state);

		modelService.save(process);

		final DistributedProcessTransitionTaskModel task = modelService.create(DistributedProcessTransitionTaskModel.class);

		task.setState(process.getState());
		task.setContextItem(process);

		return task;
	}

	private TransitionsHandler givenHandler()
	{
		return new TransitionsHandler(modelService, controller);
	}

}
