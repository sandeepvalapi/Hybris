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
package de.hybris.platform.task;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.model.ScriptingTaskModel;
import de.hybris.platform.test.TestThreadsHolder;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TaskConditionWithCounterIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private TaskService taskService;

	private static AtomicReference<String> TASK_EXECUTED;

	@Before
	public void setUp()
	{
		TASK_EXECUTED = new AtomicReference<>();
	}

	@Test
	public void shouldTreatTaskConditionWithNullCounterAsRegularCondition() throws InterruptedException, TimeoutException
	{
		final String id = UUID.randomUUID().toString();
		final TaskModel task = givenTestTask(id);
		final TaskConditionModel condition = givenCondition(task);

		taskService.scheduleTask(task);

		assertThatConditionIsNotFulfilled(condition, null);

		taskService.triggerEvent(id);

		waitForTask();
		assertThat(TASK_EXECUTED.get()).isEqualTo(id);

		assertThatConditionIsFulfilled(condition);
	}

	@Test
	public void shouldTreatTaskConditionWithCounterSetToZeroAsRegularCondition() throws InterruptedException, TimeoutException
	{
		final String id = UUID.randomUUID().toString();
		final TaskModel task = givenTestTask(id);
		final TaskConditionModel condition = givenCondition(task, 0);

		taskService.scheduleTask(task);

		assertThatConditionIsNotFulfilled(condition, Integer.valueOf(0));

		taskService.triggerEvent(id);

		waitForTask();
		assertThat(TASK_EXECUTED.get()).isEqualTo(id);

		assertThatConditionIsFulfilled(condition);
	}

	@Test
	public void shouldExecuteTaskWaitingOnConditionWithCounterSetToOne() throws InterruptedException, TimeoutException
	{
		final String id = UUID.randomUUID().toString();
		final TaskModel task = givenTestTask(id);
		final TaskConditionModel condition = givenCondition(task, 1);

		taskService.scheduleTask(task);
		taskService.triggerEvent(id);

		waitForTask();
		assertThat(TASK_EXECUTED.get()).isEqualTo(id);

		assertThatConditionIsFulfilled(condition);
	}

	@Test
	public void shouldExecuteTaskWaitingOnConditionWithCounterGreaterThanZero() throws InterruptedException, TimeoutException
	{
		final String id = UUID.randomUUID().toString();
		final TaskModel task = givenTestTask(id);
		final TaskConditionModel condition = givenCondition(task, 2);

		taskService.scheduleTask(task);
		taskService.triggerEvent(id);

		assertThatConditionIsNotFulfilled(condition, Integer.valueOf(1));

		taskService.triggerEvent(id);

		waitForTask();
		assertThat(TASK_EXECUTED.get()).isEqualTo(id);

		assertThatConditionIsFulfilled(condition);
	}

	@Test
	public void shouldExecuteTaskWaitingOnConditionWithCounterGreaterThanZeroAndTriggeredFrommultipleThreads()
			throws InterruptedException, TimeoutException
	{
		final String id = UUID.randomUUID().toString();
		final TaskModel task = givenTestTask(id);
		final TaskConditionModel condition = givenCondition(task, 2001);


		taskService.scheduleTask(task);

		final TestThreadsHolder testThreads = new TestThreadsHolder<>(20, () -> {
			final TaskService ts = Registry.getApplicationContext().getBean(TaskService.BEAN_ID, TaskService.class);
			for (int i = 0; i < 100; i++)
			{
				ts.triggerEvent(id);
			}
		} , true);

		testThreads.startAll();

		assertThat(testThreads.waitAndDestroy(60)).isTrue();
		assertThatConditionIsNotFulfilled(condition, Integer.valueOf(1));

		taskService.triggerEvent(id);

		waitForTask();
		assertThat(TASK_EXECUTED.get()).isEqualTo(id);
		assertThatConditionIsFulfilled(condition);
	}

	private void assertThatConditionIsFulfilled(final TaskConditionModel condition) throws InterruptedException
	{
		final long cacheWaitEndTime = System.currentTimeMillis() + 10_000;
		while (!modelService.isRemoved(condition))
		{
			Thread.sleep(100);
			assertThat(System.currentTimeMillis()).isLessThan(cacheWaitEndTime)
					.overridingErrorMessage("Condition was expected to be removed, but it wasn't.");
		}
	}

	private void assertThatConditionIsNotFulfilled(final TaskConditionModel condition, final Integer expectedCounter)
	{
		assertThat(TASK_EXECUTED.get()).isNull();
		modelService.refresh(condition);
		assertThat(condition.getFulfilled()).isNotNull().isFalse();
		assertThat(condition.getCounter()).isEqualTo(expectedCounter);
	}

	private TaskModel givenTestTask(final String id)
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setCode(id);
		script.setActive(Boolean.TRUE);
		script.setContent("return new " + TestRunner.class.getName() + "(\"" + id + "\");");
		modelService.save(script);

		final ScriptingTaskModel task = modelService.create(ScriptingTaskModel.class);
		task.setContext(id);
		task.setScriptURI("model://" + script.getCode());

		return task;
	}

	private TaskConditionModel givenCondition(final TaskModel task)
	{
		final TaskConditionModel condition = modelService.create(TaskConditionModel.class);

		condition.setUniqueID((String) task.getContext());

		task.setConditions(Collections.singleton(condition));

		return condition;
	}


	private TaskConditionModel givenCondition(final TaskModel task, final int counter)
	{
		final TaskConditionModel condition = modelService.create(TaskConditionModel.class);

		condition.setCounter(Integer.valueOf(counter));
		condition.setUniqueID((String) task.getContext());

		task.setConditions(Collections.singleton(condition));

		return condition;
	}

	private void waitForTask() throws InterruptedException, TimeoutException
	{
		final long timeoutTimeMs = System.currentTimeMillis() + 30_000;
		while (TASK_EXECUTED.get() == null)
		{
			if (System.currentTimeMillis() > timeoutTimeMs)
			{
				throw new TimeoutException();
			}
			Thread.sleep(100);
		}
	}

	public static class TestRunner implements TaskRunner<ScriptingTaskModel>
	{
		private final String executionId;

		public TestRunner(final String executionId)
		{
			this.executionId = Objects.requireNonNull(executionId);
		}

		@Override
		public void run(final TaskService taskService, final ScriptingTaskModel task) throws RetryLaterException
		{
			TASK_EXECUTED.set(executionId);
		}

		@Override
		public void handleError(final TaskService taskService, final ScriptingTaskModel task, final Throwable error)
		{
			//
		}

	}

}
