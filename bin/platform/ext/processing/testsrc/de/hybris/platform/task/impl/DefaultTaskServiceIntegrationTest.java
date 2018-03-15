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
package de.hybris.platform.task.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.task.AbstractTaskTest;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.Task;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.TestTaskRunner;
import de.hybris.platform.util.Config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;


@IntegrationTest
public class DefaultTaskServiceIntegrationTest extends AbstractTaskTest
{
	private TestTaskRunner testTaskRunner;
	private final double timeFactor = Config.getDouble("platform.test.timefactor", 10.0);
	private TaskDAO orginal;

	@Override
	protected Map<String, Object> createCustomSingletons()
	{
		final Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("testTaskRunner", testTaskRunner = Mockito.spy(new TestTaskRunner()));

		return ret;
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		orginal = ((DefaultTaskService) taskService).getTaskDao();
	}

	@Override
	public void tearDown()
	{
		((DefaultTaskService) taskService).setTaskDao(orginal);
		Mockito.reset(testTaskRunner);
		testTaskRunner.reset();
		testTaskRunner = null;
		super.tearDown();
	}

	@Test
	public void testIgnoreFailedTasks() throws Exception
	{

		assertEquals(testTaskRunner, Registry.getApplicationContext().getBean("testTaskRunner"));
		assertTrue("task engine is not running", taskService.getEngine().isRunning());
		final TaskModel failedTask = modelService.create(TaskModel.class);
		failedTask.setRunnerBean("testTaskRunner");

		final TaskConditionModel condition = modelService.create(TaskConditionModel.class);
		condition.setUniqueID("MyEvent");
		failedTask.setConditions(Collections.singleton(condition));

		taskService.getEngine().stop();
		taskService.scheduleTask(failedTask);
		modelService.refresh(failedTask);
		taskService.getEngine().start();
		((DefaultTaskService) taskService).setTaskDao(new AdjustTaskToFailedDAO(failedTask.getPk(), jaloSession.getTenant())
		//adjust task in lock section
				{
					@Override
					protected void adjustMatchingPK(final PK taskToAdjust)
					{
						final TaskModel model = modelService.get(taskToAdjust);
						final Task failedTaskItem = modelService.getSource(model);
						failedTaskItem.setProperty(Task.FAILED, Boolean.TRUE);
						modelService.refresh(model);
					}
				});
		taskService.triggerEvent("MyEvent");

		Thread.sleep((long) (1000 * timeFactor));

		assureTaskRunnerNotCalledForTask(failedTask);
		modelService.refresh(failedTask);
		assertTrue("Failed task should not remove its conditions ", CollectionUtils.isNotEmpty(failedTask.getConditions()));

	}

	private void assureTaskRunnerNotCalledForTask(final TaskModel failedTask) throws RetryLaterException
	{
		Mockito.verify(testTaskRunner, Mockito.times(0)).run(Mockito.any(TaskService.class),
				Mockito.argThat(new TaskModelArgumentMatcher(failedTask)));
		Mockito.verify(testTaskRunner, Mockito.times(0)).handleError(Mockito.any(TaskService.class),
				Mockito.argThat(new TaskModelArgumentMatcher(failedTask)), Mockito.any(Throwable.class));
	}

	abstract class AdjustTaskToFailedDAO extends TaskDAO
	{
		private final PK taskToAdjust;

		public AdjustTaskToFailedDAO(final PK toAdjust, final Tenant t)
		{
			super(t);
			this.taskToAdjust = toAdjust;
			// YTODO Auto-generated constructor stub
		}

		@Override
		boolean lock(final Long pk, final long hjmpTS)
		{
			final boolean result = super.lock(pk, hjmpTS);
			if (pk.longValue() == taskToAdjust.getLongValue())
			{
				adjustMatchingPK(taskToAdjust);
			}
			return result;
		}

		abstract protected void adjustMatchingPK(PK taskToAdjust);
	}


	class TaskModelArgumentMatcher extends ArgumentMatcher<TaskModel>
	{
		private final TaskModel expectedTask;

		TaskModelArgumentMatcher(final TaskModel expectedTask)
		{
			this.expectedTask = expectedTask;
		}

		@Override
		public boolean matches(final Object argument)
		{
			if (argument instanceof TaskModel)
			{
				final TaskModel givenModel = (TaskModel) argument;
				return givenModel.getPk().equals(expectedTask.getPk());
			}
			return false;
		}
	}
}
