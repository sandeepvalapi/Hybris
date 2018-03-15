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


import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.TestTaskRunner;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.Lists;


@IntegrationTest
public class DefaultTaskServiceNodeGroupsIntegrationTest extends ServicelayerBaseTest
{

	private static final int DEFAULT_SHUTDOWN_WAIT = 15;
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private TaskService taskService;
	@Resource
	private TypeService typeService;
	@Resource
	private MetricRegistry metricRegistry;

	private CountDownLatch countDownLatch;
	private CountDownLatch deletionCountDownLatch;
	private boolean taskEngineWasRunningBefore;

	private final PropertyConfigSwitcher taskEngineExclusiveModeSwitch = new PropertyConfigSwitcher("task.engine.exclusive.mode");

	@Before
	public void setUp()
	{
		if (taskService.getEngine().isRunning())
		{
			taskEngineWasRunningBefore = true;
			taskService.getEngine().stop();
		}
	}

	@After
	public void tearDown()
	{
		resetExclusiveMode();
		if (taskEngineWasRunningBefore)
		{
			taskService.getEngine().start();
		}
	}

	@Test
	public void shouldExecuteOnlyTasksForOwnNodeGroup() throws Exception
	{
		assureTaskEngineStopped();

		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long tA1 = createTask(group_a);
		final Long tA2 = createTask(group_a);
		final Long tA3 = createTask(group_a);

		final Long tB1 = createTask(group_b);
		final Long tB2 = createTask(group_b);
		final Long tB3 = createTask(group_b);
		final Long tB4 = createTask(group_b);

		final Long tNone1 = createTask(null);
		final Long tNone2 = createTask(null);
		final Long tNone3 = createTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1, tA2, tA3, tB1, tB2, tB3, tB4, tNone1, tNone2, tNone3);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).contains(tA1, tA2, tA3).doesNotContain(tB1, tB2, tB3, tB4);
			assertThat(serviceB.getExecutedTasks()).contains(tB1, tB2, tB3, tB4).doesNotContain(tA1, tA2, tA3);

			final List<Long> allExecutedTasks = Lists.newArrayList(serviceA.getExecutedTasks());
			allExecutedTasks.addAll(serviceB.getExecutedTasks());

			assertThat(allExecutedTasks).containsOnlyOnce(relevantTasks.toArray(new Long[relevantTasks.size()]));

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}

	@Test
	public void shouldExecuteOnlyTasksForOwnNodeGroupInExclusiveMode() throws Exception
	{
		assureTaskEngineStopped();
		enableExclusiveMode();

		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long tA1 = createTask(group_a);
		final Long tA2 = createTask(group_a);
		final Long tA3 = createTask(group_a);

		final Long tB1 = createTask(group_b);
		final Long tB2 = createTask(group_b);
		final Long tB3 = createTask(group_b);
		final Long tB4 = createTask(group_b);

		final Long tNone1 = createTask(null);
		final Long tNone2 = createTask(null);
		final Long tNone3 = createTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1, tA2, tA3, tB1, tB2, tB3, tB4, tNone1, tNone2, tNone3);

		countDownLatch = new CountDownLatch(relevantTasks.size() - 3);
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size() - 3);

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);


		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).containsOnly(tA1, tA2, tA3);
			assertThat(serviceB.getExecutedTasks()).containsOnly(tB1, tB2, tB3, tB4);

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}

	@Test
	public void shouldFailAllExpiredTasks() throws Exception
	{
		assureTaskEngineStopped();

		final String group_a = "group_a";
		final String group_b = "group_b";

		//expired tasks
		final Long tA1ex = createExpiredTask(group_a);
		final Long tB1ex = createExpiredTask(group_b);
		final Long tNone1ex = createExpiredTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1ex, tB1ex, tNone1ex);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).isEmpty();
			assertThat(serviceB.getExecutedTasks()).isEmpty();

			final List<Long> failedTasks = Lists.newArrayList(serviceA.getFailedTasks());
			failedTasks.addAll(serviceB.getFailedTasks());
			assertThat(failedTasks).containsOnlyOnce(relevantTasks.toArray(new Long[relevantTasks.size()]));

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}


	@Test
	public void shouldFailOnlyExpiredTasksForOwnNodeGroupInExclusiveMode() throws Exception
	{
		assureTaskEngineStopped();
		enableExclusiveMode();

		final String group_a = "group_a";
		final String group_b = "group_b";

		//expired tasks
		final Long tA1ex = createExpiredTask(group_a);
		final Long tB1ex = createExpiredTask(group_b);
		final Long tNone1ex = createExpiredTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1ex, tB1ex, tNone1ex);

		countDownLatch = new CountDownLatch(relevantTasks.size() - 1);
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size() - 1);

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).isEmpty();
			assertThat(serviceB.getExecutedTasks()).isEmpty();

			assertThat(serviceA.getFailedTasks()).contains(tA1ex);
			assertThat(serviceB.getFailedTasks()).contains(tB1ex);

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}


	@Test
	public void shouldExecuteOnlyTasksForOwnNode() throws Exception
	{
		assureTaskEngineStopped();

		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long tA1 = createTask(0);
		final Long tA2 = createTask(0);
		final Long tA3 = createTask(0);

		final Long tB1 = createTask(1);
		final Long tB2 = createTask(1);
		final Long tB3 = createTask(1);
		final Long tB4 = createTask(1);

		final Long tNone1 = createTask(null);
		final Long tNone2 = createTask(null);
		final Long tNone3 = createTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1, tA2, tA3, tB1, tB2, tB3, tB4, tNone1, tNone2, tNone3);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).contains(tA1, tA2, tA3).doesNotContain(tB1, tB2, tB3, tB4);
			assertThat(serviceB.getExecutedTasks()).contains(tB1, tB2, tB3, tB4).doesNotContain(tA1, tA2, tA3);

			final List<Long> allExecutedTasks = Lists.newArrayList(serviceA.getExecutedTasks());
			allExecutedTasks.addAll(serviceB.getExecutedTasks());

			assertThat(allExecutedTasks).containsOnlyOnce(relevantTasks.toArray(new Long[relevantTasks.size()]));

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}

	@Test
	public void shouldFailOnlyExpiredTasksForOwnNodeIdInExclusiveMode() throws Exception
	{
		assureTaskEngineStopped();
		enableExclusiveMode();

		final String group_a = "group_a";
		final String group_b = "group_b";

		//expired tasks
		final Long tA1ex = createExpiredTask(0);
		final Long tB1ex = createExpiredTask(1);
		final Long tNone1ex = createExpiredTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1ex, tB1ex, tNone1ex);

		countDownLatch = new CountDownLatch(relevantTasks.size() - 1);
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size() - 1);

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).isEmpty();
			assertThat(serviceB.getExecutedTasks()).isEmpty();

			assertThat(serviceA.getFailedTasks()).contains(tA1ex).doesNotContain(tB1ex);
			assertThat(serviceB.getFailedTasks()).contains(tB1ex).doesNotContain(tA1ex);

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}


	@Test
	public void shouldExecuteOnlyTasksForOwnNodeWhenNoGroupDefined() throws Exception
	{
		assureTaskEngineStopped();

		final Long tA1 = createTask(0);
		final Long tA2 = createTask(0);
		final Long tA3 = createTask(0);

		final Long tB1 = createTask(1);
		final Long tB2 = createTask(1);
		final Long tB3 = createTask(1);
		final Long tB4 = createTask(1);

		final Long tNone1 = createTask(null);
		final Long tNone2 = createTask(null);
		final Long tNone3 = createTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1, tA2, tA3, tB1, tB2, tB3, tB4, tNone1, tNone2, tNone3);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Collections.emptyList(), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Collections.emptyList(), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).contains(tA1, tA2, tA3).doesNotContain(tB1, tB2, tB3, tB4);
			assertThat(serviceB.getExecutedTasks()).contains(tB1, tB2, tB3, tB4).doesNotContain(tA1, tA2, tA3);

			final List<Long> allExecutedTasks = Lists.newArrayList(serviceA.getExecutedTasks());
			allExecutedTasks.addAll(serviceB.getExecutedTasks());

			assertThat(allExecutedTasks).containsOnlyOnce(relevantTasks.toArray(new Long[relevantTasks.size()]));

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}


	@Test
	public void shouldExecuteOnlyTasksForOwnNodeInExclusiveMode() throws Exception
	{
		assureTaskEngineStopped();
		enableExclusiveMode();

		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long tA1 = createTask(0);
		final Long tA2 = createTask(0);
		final Long tA3 = createTask(0);

		final Long tB1 = createTask(1);
		final Long tB2 = createTask(1);
		final Long tB3 = createTask(1);
		final Long tB4 = createTask(1);

		final Long tNone1 = createTask(null);
		final Long tNone2 = createTask(null);
		final Long tNone3 = createTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1, tA2, tA3, tB1, tB2, tB3, tB4, tNone1, tNone2, tNone3);

		countDownLatch = new CountDownLatch(relevantTasks.size() - 3);
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size() - 3);

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).containsOnly(tA1, tA2, tA3);
			assertThat(serviceB.getExecutedTasks()).containsOnly(tB1, tB2, tB3, tB4);

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}

	@Test
	public void shouldExecuteTasksWithConflictingNodeIfAndNodeGroupAssignment() throws Exception
	{
		assureTaskEngineStopped();

		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long tA1 = createTask(0, group_b);
		final Long tA2 = createTask(0, group_b);
		final Long tA3 = createTask(0, group_b);

		final Long tB1 = createTask(1, group_a);
		final Long tB2 = createTask(1, group_a);
		final Long tB3 = createTask(1, group_a);
		final Long tB4 = createTask(1, group_a);

		final Long tNone1 = createTask(null);
		final Long tNone2 = createTask(null);
		final Long tNone3 = createTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1, tA2, tA3, tB1, tB2, tB3, tB4, tNone1, tNone2, tNone3);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).contains(tA1, tA2, tA3).doesNotContain(tB1, tB2, tB3, tB4);
			assertThat(serviceB.getExecutedTasks()).contains(tB1, tB2, tB3, tB4).doesNotContain(tA1, tA2, tA3);

			final List<Long> allExecutedTasks = Lists.newArrayList(serviceA.getExecutedTasks());
			allExecutedTasks.addAll(serviceB.getExecutedTasks());

			assertThat(allExecutedTasks).containsOnlyOnce(relevantTasks.toArray(new Long[relevantTasks.size()]));

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}


	@Test
	public void shouldExecuteTaskWithoutSpecifiedNodeGroupByAnyNode() throws Exception
	{
		assureTaskEngineStopped();

		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long t1 = createTask(null);
		final Long t2 = createTask(null);
		final Long t3 = createTask(null);
		final Long t4 = createTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(t1, t2, t3, t4);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			final Collection<Long> allExecutedTasks = new ArrayList<>();

			allExecutedTasks.addAll(serviceA.getExecutedTasks());
			allExecutedTasks.addAll(serviceB.getExecutedTasks());

			assertThat(allExecutedTasks).hasSize(4);
			assertThat(allExecutedTasks).containsOnly(t1, t2, t3, t4);

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}


	private Long createTask(final String group)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setNodeGroup(group);
		task.setRunnerBean("runner");
		taskService.scheduleTask(task);
		return task.getPk().getLong();
	}

	private Long createExpiredTask(final String group)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setNodeGroup(group);
		task.setRunnerBean("runner");
		task.setExpirationDate(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
		task.setExecutionDate(Date.from(Instant.now().minus(2, ChronoUnit.DAYS)));
		taskService.scheduleTask(task);
		return task.getPk().getLong();
	}

	private Long createTask(final int nodeId)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setNodeId(Integer.valueOf(nodeId));
		task.setRunnerBean("runner");
		taskService.scheduleTask(task);
		return task.getPk().getLong();
	}

	private Long createExpiredTask(final int nodeId)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setNodeId(Integer.valueOf(nodeId));
		task.setRunnerBean("runner");
		task.setExpirationDate(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
		task.setExecutionDate(Date.from(Instant.now().minus(2, ChronoUnit.DAYS)));
		taskService.scheduleTask(task);
		return task.getPk().getLong();
	}

	private Long createTask(final int nodeId, final String group)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setNodeId(Integer.valueOf(nodeId));
		task.setNodeGroup(group);
		task.setRunnerBean("runner");
		taskService.scheduleTask(task);
		return task.getPk().getLong();
	}

	public class TestTaskService extends DefaultTaskService
	{

		private final int nodeId;
		private final Collection<String> nodeGroups;
		private final TestExecutionStrategy testExecutionStrategy;

		public TestTaskService(final int nodeId, final Collection<String> nodeGroups, final Collection<Long> relevantTasks)
		{
			this.nodeId = nodeId;
			this.nodeGroups = nodeGroups;
			this.testExecutionStrategy = new TestExecutionStrategy(relevantTasks);

			this.setModelService(modelService);
			this.setTypeService(typeService);
			this.setMetricRegistry(metricRegistry);
			this.setTaskExecutionStrategies(Lists.newArrayList(testExecutionStrategy));
			this.setFlexibleSearchService(flexibleSearchService);
			this.setTaskDao(new TaskDAO(getTenant())
			{
				@Override
				protected int initializeClusterId()
				{
					return nodeId;
				}
			});
		}

		@Override
		protected TaskExecutionStrategy getExecutionStrategy(final TaskRunner<? extends TaskModel> runner)
		{
			return testExecutionStrategy;
		}

		@Override
		Collection<String> getClusterGroupsForThisNode()
		{
			return nodeGroups;
		}

		@Override
		int getClusterNodeID()
		{
			return nodeId;
		}

		@Override
		void scheduleTaskForExecution(final VersionPK versionedPK)
		{
			super.scheduleTaskForExecution(versionedPK);
		}

		public Collection<Long> getExecutedTasks()
		{
			return testExecutionStrategy.getExecutedTasks();
		}

		public Collection<Long> getFailedTasks()
		{
			return testExecutionStrategy.getFailedTasks();
		}

		@Override
		protected TaskRunner getRunner(final String runnerBean) throws IllegalStateException
		{
			return null;
		}
	}

	class TestExecutionStrategy implements TaskExecutionStrategy
	{

		private final Collection<Long> relevantTasks;
		private final Collection<Long> executedTasks = Collections.synchronizedList(new ArrayList<Long>());
		private final Collection<Long> failedTasks = Collections.synchronizedList(new ArrayList<Long>());

		public TestExecutionStrategy(final Collection<Long> relevantTasks)
		{
			this.relevantTasks = relevantTasks;
		}

		@Override
		public void run(final TaskService taskService, final TaskRunner<TaskModel> runner, final TaskModel model)
				throws RetryLaterException
		{
			final Long pk = model.getPk().getLong();
			if (relevantTasks.contains(pk))
			{
				executedTasks.add(pk);
				countDownLatch.countDown();
			}
		}

		@Override
		public Throwable handleError(final TaskService taskService, final TaskRunner<TaskModel> runner, final TaskModel model,
				final Throwable error)
		{
			final Long pk = model.getPk().getLong();
			if (relevantTasks.contains(pk))
			{
				failedTasks.add(pk);
				countDownLatch.countDown();
			}
			return null;
		}

		@Override
		public void finished(final TaskService taskService, final TaskRunner<TaskModel> runner, final TaskModel model,
				final Throwable error)
		{
			modelService.remove(model);
			deletionCountDownLatch.countDown();
		}

		@Override
		public Date handleRetry(final TaskService taskService, final TaskRunner<TaskModel> runner, final TaskModel model,
				final RetryLaterException retry, final int currentRetries)
		{
			return null;
		}

		@Override
		public Class<? extends TaskRunner<? extends TaskModel>> getRunnerClass()
		{
			return TestTaskRunner.class;
		}

		public Collection<Long> getExecutedTasks()
		{
			return executedTasks;
		}

		public Collection<Long> getFailedTasks()
		{
			return failedTasks;
		}
	}

	private void assureTaskEngineStopped()
	{
		int shutdownWait = DEFAULT_SHUTDOWN_WAIT;
		while (taskService.getEngine().isRunning() && shutdownWait-- > 0)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e)
			{
				break;
			}
		}
		assertThat(taskService.getEngine().isRunning())
				.overridingErrorMessage("Task Engine failed to shut down in " + DEFAULT_SHUTDOWN_WAIT + " seconds.").isFalse();
	}

	private void enableExclusiveMode()
	{
		taskEngineExclusiveModeSwitch.switchToValue("true");
	}

	private void resetExclusiveMode()
	{
		taskEngineExclusiveModeSwitch.switchBackToDefault();
	}

}
