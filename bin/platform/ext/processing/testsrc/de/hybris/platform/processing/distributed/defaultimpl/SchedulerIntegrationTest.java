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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.DistributedProcessTransitionTaskModel;
import de.hybris.platform.processing.model.DistributedProcessWorkerTaskModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.testframework.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;


@IntegrationTest
public class SchedulerIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private TaskService taskService;

	@Resource
	private ModelService modelService;

	@Test
	@Transactional
	public void shouldScheduleTransitionTaskWithDispatcherBeanNameAsRunner()
	{
		final Scheduler scheduler = givenScheduler("TEST_DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);

		final DistributedProcessTransitionTaskModel task = scheduler.scheduleTransitionTask(process);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getRunnerBean()).isNotNull().isEqualTo("TEST_DISPATCHER");
	}

	@Test
	@Transactional
	public void shouldScheduleTransitionTaskWithTheSameStateAsProcessIs()
	{
		final Scheduler scheduler = givenScheduler("TEST_DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);

		final DistributedProcessTransitionTaskModel task = scheduler.scheduleTransitionTask(process);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getState()).isNotNull().isEqualTo(DistributedProcessState.INITIALIZING);
	}

	@Test
	@Transactional
	public void shouldScheduleTransitionTaskWithAttachedProcess()
	{
		final Scheduler scheduler = givenScheduler("TEST_DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);

		final DistributedProcessTransitionTaskModel task = scheduler.scheduleTransitionTask(process);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getContextItem()).isNotNull().isSameAs(process);
	}

	@Test
	@Transactional
	public void shouldScheduleTransitionTaskWithNodeGroupFromProcess()
	{
		final Scheduler scheduler = givenScheduler("TEST_DISPATCHER");
		final DistributedProcessModel process = givenProcessWithNodeGroup("testNodeGroup");

		final DistributedProcessTransitionTaskModel task = scheduler.scheduleTransitionTask(process);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getContextItem()).isNotNull().isSameAs(process);
		assertThat(task.getNodeGroup()).isEqualTo(process.getNodeGroup());
	}

	@Test
	@Transactional
	public void shouldScheduleTransitionTaskWithoutConditionsWhenConditionsAreNotGiven()
	{
		final Scheduler scheduler = givenScheduler("TEST_DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);

		final DistributedProcessTransitionTaskModel task = scheduler.scheduleTransitionTask(process);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getConditions()).isEmpty();
	}

	@Test
	@Transactional
	public void shouldScheduleTransitionTaskWithConditionsWhenConditionsAreGiven()
	{
		final Scheduler scheduler = givenScheduler("TEST_DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);
		final Set<TaskConditionModel> conditions = Stream.of("C1", "C2").map(id -> {
			final TaskConditionModel cnd = modelService.create(TaskConditionModel.class);
			cnd.setUniqueID(id);
			return cnd;
		}).collect(Collectors.toSet());

		final DistributedProcessTransitionTaskModel task = scheduler.scheduleTransitionTask(process, conditions);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getConditions()).isNotEmpty().hasSize(2);
		assertThat(task.getConditions().stream().map(c -> c.getUniqueID()).collect(Collectors.toSet()))
				.isEqualTo(ImmutableSet.of("C2", "C1"));
	}

	@Test
	@Transactional
	public void shouldScheduleWorkerTaskWithDispatcherBeanNameAsRunner()
	{
		final Scheduler scheduler = givenScheduler("DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);
		final BatchModel batch = givenInputBatch("B_ID", process);

		final DistributedProcessWorkerTaskModel task = scheduler.scheduleWorkerTask(batch);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getRunnerBean()).isNotNull().isEqualTo("DISPATCHER");
	}

	@Test
	@Transactional
	public void shouldScheduleWorkerTaskWithNodeGroupFromProcess()
	{
		final Scheduler scheduler = givenScheduler("DISPATCHER");
		final DistributedProcessModel process = givenProcessWithNodeGroup("testNodeGroup");
		final BatchModel batch = givenInputBatch("B_ID", process);

		final DistributedProcessWorkerTaskModel task = scheduler.scheduleWorkerTask(batch);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getRunnerBean()).isNotNull().isEqualTo("DISPATCHER");
		assertThat(task.getNodeGroup()).isEqualTo(process.getNodeGroup());
	}

	@Test
	@Transactional
	public void shouldScheduleWorkerTaskWithAttachedBatch()
	{
		final Scheduler scheduler = givenScheduler("DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);
		final BatchModel batch = givenInputBatch("B_ID", process);

		final DistributedProcessWorkerTaskModel task = scheduler.scheduleWorkerTask(batch);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getContextItem()).isNotNull().isSameAs(batch);
	}

	@Test
	@Transactional
	public void shouldScheduleWorkerTaskWithConditionIdRelatedToProcessBatchAndExecution()
	{
		final Scheduler scheduler = givenScheduler("DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);
		final BatchModel batch = givenInputBatch("B_ID", process);

		final DistributedProcessWorkerTaskModel task = scheduler.scheduleWorkerTask(batch);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getConditionId()).isNotNull().contains(process.getCode()).contains(batch.getId())
				.contains(batch.getExecutionId());
	}

	@Test
	@Transactional
	public void shouldScheduleWorkerTaskWithoutConditions()
	{
		final Scheduler scheduler = givenScheduler("DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);
		final BatchModel batch = givenInputBatch("B_ID", process);

		final DistributedProcessWorkerTaskModel task = scheduler.scheduleWorkerTask(batch);

		assertThat(task).isNotNull();
		assertThat(task.getPk()).isNotNull();
		assertThat(task.getConditions()).isEmpty();
	}

	@Test
	@Transactional
	public void shouldFailToScheduleWorkerTaskForInitialBatch()
	{
		final Scheduler scheduler = givenScheduler("DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);
		final BatchModel batch = givenInputBatch("B_ID", process);
		batch.setType(BatchType.INITIAL);

		try
		{
			scheduler.scheduleWorkerTask(batch);
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class).hasNoCause();
			assertThat(e.getMessage()).isNotNull().startsWith("batch");
			return;
		}
		fail("Expected IllegalArgumentException to be thrown but nothing has been thrown");
	}

	@Test
	@Transactional
	public void shouldFailToScheduleWorkerTaskForResultBatch()
	{
		final Scheduler scheduler = givenScheduler("DISPATCHER");
		final DistributedProcessModel process = givenProcessInState(DistributedProcessState.INITIALIZING);
		final BatchModel batch = givenInputBatch("B_ID", process);
		batch.setType(BatchType.RESULT);

		try
		{
			scheduler.scheduleWorkerTask(batch);
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class).hasNoCause();
			assertThat(e.getMessage()).isNotNull().startsWith("batch");
			return;
		}
		fail("Expected IllegalArgumentException to be thrown but nothing has been thrown");
	}

	private BatchModel givenInputBatch(final String id, final DistributedProcessModel process)
	{
		final BatchModel batch = modelService.create(BatchModel.class);

		batch.setProcess(process);
		batch.setId(id);
		batch.setExecutionId(process.getCurrentExecutionId());
		batch.setRemainingWorkLoad(123);
		batch.setType(BatchType.INPUT);

		modelService.save(batch);

		return batch;
	}

	private DistributedProcessModel givenProcessInState(final DistributedProcessState state)
	{
		final DistributedProcessModel process = modelService.create(DistributedProcessModel.class);

		process.setCode(UUID.randomUUID().toString());
		process.setState(state);
		process.setCurrentExecutionId("EXECUTION");

		modelService.save(process);

		return process;
	}

	private DistributedProcessModel givenProcessWithNodeGroup(final String nodeGroup)
	{
		final DistributedProcessModel process = modelService.create(DistributedProcessModel.class);

		process.setCode(UUID.randomUUID().toString());
		process.setState(DistributedProcessState.INITIALIZING);
		process.setCurrentExecutionId("EXECUTION");
		process.setNodeGroup(nodeGroup);

		modelService.save(process);
		return process;
	}



	private Scheduler givenScheduler(final String dispatcherBeanName)
	{
		return new Scheduler(modelService, taskService, dispatcherBeanName);
	}

}
