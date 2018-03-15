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

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler.ModelWithDependencies;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.DistributedProcessWorkerTaskModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskEvent;
import de.hybris.platform.task.TaskService;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@IntegrationTest
public class ExecutionHandlerIntegrationTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private ModelService modelService;

	@Mock
	private TaskService taskService;

	@Mock
	DistributedProcessHandler processHandler;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldOnlyTriggerConditionWhenProcessMovedForward()
	{
		final ExecutionHandler handler = givenExecutionHandler();
		final DistributedProcessModel process = givenProcess();
		final BatchModel batch = givenBatch(process, BatchType.INPUT);
		final DistributedProcessWorkerTaskModel task = givenTask(batch);
		process.setCurrentExecutionId("MOVED_FORWARD");

		handler.runWorkerTask(task);

		verifyZeroInteractions(processHandler);
		verify(taskService).triggerEvent(notNull(TaskEvent.class));
		verifyNoMoreInteractions(taskService);
	}

	@Test
	public void shouldOnlyTriggerConditionWhenProcessIsAlreadyFinished()
	{
		final ExecutionHandler handler = givenExecutionHandler();
		final DistributedProcessModel process = givenProcess();
		final BatchModel batch = givenBatch(process, BatchType.INPUT);
		final DistributedProcessWorkerTaskModel task = givenTask(batch);
		process.setState(DistributedProcessState.SUCCEEDED);

		handler.runWorkerTask(task);

		verifyZeroInteractions(processHandler);
		verify(taskService).triggerEvent(notNull(TaskEvent.class));
		verifyNoMoreInteractions(taskService);
	}

	@Test
	public void shouldOnlyTriggerConditionWhenStopHasBeenRequested()
	{
		final ExecutionHandler handler = givenExecutionHandler();
		final DistributedProcessModel process = givenProcess();
		final BatchModel batch = givenBatch(process, BatchType.INPUT);
		final DistributedProcessWorkerTaskModel task = givenTask(batch);
		process.setStopRequested(true);

		handler.runWorkerTask(task);

		verifyZeroInteractions(processHandler);
		verify(taskService).triggerEvent(notNull(TaskEvent.class));
		verifyNoMoreInteractions(taskService);
	}

	@Test
	public void shouldUseProcessHandlerForProducingResultBatch()
	{
		final ExecutionHandler handler = givenExecutionHandler();
		final DistributedProcessModel process = givenProcess();
		final BatchModel batch = givenBatch(process, BatchType.INPUT);
		final DistributedProcessWorkerTaskModel task = givenTask(batch);
		final BatchModel resultBatch = givenBatch(process, BatchType.RESULT);
		when(processHandler.createResultBatch(notNull(BatchModel.class)))
				.thenReturn(ModelWithDependencies.singleModel(resultBatch));

		handler.runWorkerTask(task);

		verify(processHandler).createResultBatch(batch);
		verify(taskService).triggerEvent(notNull(TaskEvent.class));
		verifyNoMoreInteractions(processHandler, taskService);
	}

	@Test
	public void shouldOnlyTriggerConditionWhenHandlingExceptiond()
	{
		final ExecutionHandler handler = givenExecutionHandler();
		final DistributedProcessModel process = givenProcess();
		final BatchModel batch = givenBatch(process, BatchType.INPUT);
		final DistributedProcessWorkerTaskModel task = givenTask(batch);

		handler.handleErrorDuringBatchExecution(task);

		verifyZeroInteractions(processHandler);
		verify(taskService).triggerEvent(notNull(TaskEvent.class));
		verifyNoMoreInteractions(taskService);
	}

	private DistributedProcessWorkerTaskModel givenTask(final BatchModel batch)
	{
		final DistributedProcessWorkerTaskModel task = modelService.create(DistributedProcessWorkerTaskModel.class);

		task.setContextItem(batch);
		task.setConditionId(UUID.randomUUID().toString());
		task.setRunnerBean("alabama");

		modelService.save(task);

		return task;
	}

	private BatchModel givenBatch(final DistributedProcessModel process, final BatchType type)
	{
		final BatchModel batch = modelService.create(BatchModel.class);

		batch.setId(UUID.randomUUID().toString());
		batch.setExecutionId(process.getCurrentExecutionId());
		batch.setRemainingWorkLoad(123);
		batch.setProcess(process);
		batch.setType(type);

		return batch;
	}

	private DistributedProcessModel givenProcess()
	{
		final DistributedProcessModel process = modelService.create(DistributedProcessModel.class);

		process.setCurrentExecutionId("EXECUTION");
		process.setCode(UUID.randomUUID().toString());
		process.setState(DistributedProcessState.WAITING_FOR_EXECUTION);

		modelService.save(process);

		return process;
	}

	private ExecutionHandler givenExecutionHandler()
	{
		return new ExecutionHandler(taskService, modelService)
		{
			@Override
			DistributedProcessHandler getHandler(final String handlerBeanId)
			{
				return processHandler;
			}
		};
	}

}
