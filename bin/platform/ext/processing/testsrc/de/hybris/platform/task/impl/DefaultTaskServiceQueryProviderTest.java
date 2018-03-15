/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *
 */

package de.hybris.platform.task.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@IntegrationTest
public class DefaultTaskServiceQueryProviderTest extends ServicelayerBaseTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private TaskService taskService;

	@Resource
	private ModelService modelService;

	private int noNode;
	private int nodeId;
	private Collection<String> myGroups;
	private boolean processTriggerTasks;
	private boolean taskEngineWasRunningBefore;
	private Set<PK> createdTasks;

	@Before
	public void setUp() throws Exception
	{
		noNode = -1;
		nodeId = 1;
		myGroups = Registry.getClusterGroups();
		processTriggerTasks = true;

		if (taskService.getEngine().isRunning())
		{
			taskEngineWasRunningBefore = true;
			taskService.getEngine().stop();
		}

		createdTasks = new HashSet<>();
	}

	@After
	public void tearDown()
	{
		createdTasks.forEach(modelService::remove);

		if (taskEngineWasRunningBefore)
		{
			taskService.getEngine().start();
		}
	}

	@Test
	public void shouldProvideQueryForExpiredTasksWithoutExclusiveMode() throws Exception
	{
		final DefaultTaskServiceQueryProvider queryProvider = new DefaultTaskServiceQueryProvider();
		final Map<String, Object> parameters = queryProvider.setQueryParameters(myGroups, processTriggerTasks, nodeId, noNode,
				Collections.emptySet(), Duration.ofHours(0));

		final String query = queryProvider.getExpiredTasksToExecuteQuery(myGroups, processTriggerTasks, false);

		final FlexibleSearchQuery q = new FlexibleSearchQuery(query);
		q.setResultClassList(Collections.singletonList(PK.class));
		final SearchResult<PK> search = flexibleSearchService.search(query, parameters);

		assertThat(search).isNotNull();

	}

	@Test
	public void shouldProvideQueryForExpiredTasksWithExclusiveMode() throws Exception
	{
		final DefaultTaskServiceQueryProvider queryProvider = new DefaultTaskServiceQueryProvider();
		final Map<String, Object> parameters = queryProvider.setQueryParameters(myGroups, processTriggerTasks, nodeId, noNode,
				Collections.emptySet(), Duration.ofHours(0));

		final String query = queryProvider.getExpiredTasksToExecuteQuery(myGroups, processTriggerTasks, true);

		final SearchResult<Object> search = flexibleSearchService.search(query, parameters);

		assertThat(search).isNotNull();
	}

	@Test
	public void shouldProvideSimpleQueryForTasksWithoutExclusiveMode() throws Exception
	{
		assureTaskEngineStopped();

		final PK t1 = youngTask().build();
		final PK t2 = youngTask().withNodeId(1).build();
		final PK t3 = youngTask().withNodeGroup("a").build();
		final PK t4 = youngTask().withConditions(2).build();
		final PK t5 = youngTask().withNodeGroup("b").build();

		final PK t1old = oldTask().build();
		final PK t2old = oldTask().withNodeId(1).build();
		final PK t3old = oldTask().withNodeGroup("a").build();
		final PK t4old = oldTask().withConditions(2).build();
		final PK t5old = oldTask().withNodeGroup("b").build();

		final Collection<String> myGroups = Sets.newHashSet("a");

		final DefaultTaskServiceQueryProvider queryProvider = new DefaultTaskServiceQueryProvider();
		final Map<String, Object> parameters = queryProvider.setQueryParameters(myGroups, processTriggerTasks, nodeId, noNode,
				Collections.emptySet(), Duration.ofHours(1));

		final String query = queryProvider.getValidTasksToExecuteQuery(myGroups, processTriggerTasks, false, false);
		final FlexibleSearchQuery q = new FlexibleSearchQuery(query);
		q.setResultClassList(Collections.singletonList(PK.class));
		q.addQueryParameters(parameters);
		final SearchResult<PK> search = flexibleSearchService.search(q);

		assertThat(search).isNotNull();

		assertThat(search.getResult()).contains(t1, t2, t3).doesNotContain(t1old, t2old, t3old, t4, t4old, t5, t5old);
	}

	@Test
	public void shouldProvideSimpleQueryForTasksWithExclusiveMode() throws Exception
	{
		assureTaskEngineStopped();

		final PK t1 = youngTask().build();
		final PK t2 = youngTask().withNodeId(1).build();
		final PK t3 = youngTask().withNodeGroup("a").build();
		final PK t4 = youngTask().withConditions(2).build();
		final PK t5 = youngTask().withNodeGroup("b").build();

		final PK t1old = oldTask().build();
		final PK t2old = oldTask().withNodeId(1).build();
		final PK t3old = oldTask().withNodeGroup("a").build();
		final PK t4old = oldTask().withConditions(2).build();
		final PK t5old = oldTask().withNodeGroup("b").build();

		final Collection<String> myGroups = Sets.newHashSet("a");

		final DefaultTaskServiceQueryProvider queryProvider = new DefaultTaskServiceQueryProvider();
		final Map<String, Object> parameters = queryProvider.setQueryParameters(myGroups, processTriggerTasks, 1, noNode,
				Collections.emptySet(), Duration.ofHours(1));

		final String query = queryProvider.getValidTasksToExecuteQuery(myGroups, processTriggerTasks, true, false);

		final FlexibleSearchQuery q = new FlexibleSearchQuery(query);
		q.setResultClassList(Collections.singletonList(PK.class));
		q.addQueryParameters(parameters);
		final SearchResult<PK> search = flexibleSearchService.search(q);

		assertThat(search).isNotNull();
		assertThat(search.getResult()).contains(t2, t3).doesNotContain(t1, t4, t5, t1old, t2old, t3old, t4old, t5old);
	}


	@Test
	public void shouldProvideFullQueryForTasksWithoutExclusiveMode() throws Exception
	{
		assureTaskEngineStopped();

		final PK t1 = youngTask().build();
		final PK t2 = youngTask().withNodeId(1).build();
		final PK t3 = youngTask().withNodeGroup("a").build();
		final PK t4 = youngTask().withConditions(2).build();
		final PK t5 = youngTask().withNodeGroup("b").build();

		final PK t1old = oldTask().build();
		final PK t2old = oldTask().withNodeId(1).build();
		final PK t3old = oldTask().withNodeGroup("a").build();
		final PK t4old = oldTask().withConditions(2).build();
		final PK t5old = oldTask().withNodeGroup("b").build();

		final Collection<String> myGroups = Sets.newHashSet("a");

		final DefaultTaskServiceQueryProvider queryProvider = new DefaultTaskServiceQueryProvider();
		final Map<String, Object> parameters = queryProvider.setQueryParameters(myGroups, processTriggerTasks, nodeId, noNode,
				Collections.emptySet(), Duration.ofHours(1));

		final String query = queryProvider.getValidTasksToExecuteQuery(myGroups, processTriggerTasks, false, true);

		final FlexibleSearchQuery q = new FlexibleSearchQuery(query);
		q.setResultClassList(Collections.singletonList(PK.class));
		q.addQueryParameters(parameters);
		final SearchResult<PK> search = flexibleSearchService.search(q);

		assertThat(search).isNotNull();
		assertThat(search.getResult()).contains(t1, t2, t3, t1old, t2old, t3old).doesNotContain(t4, t4old, t5, t5old);
	}

	@Test
	public void shouldProvideFullQueryForTasksWithExclusiveMode() throws Exception
	{

		assureTaskEngineStopped();

		final PK t1 = youngTask().build();
		final PK t2 = youngTask().withNodeId(1).build();
		final PK t3 = youngTask().withNodeGroup("a").build();
		final PK t4 = youngTask().withConditions(2).build();
		final PK t5 = youngTask().withNodeGroup("b").build();

		final PK t1old = oldTask().build();
		final PK t2old = oldTask().withNodeId(1).build();
		final PK t3old = oldTask().withNodeGroup("a").build();
		final PK t4old = oldTask().withConditions(2).build();
		final PK t5old = oldTask().withNodeGroup("b").build();

		final Collection<String> myGroups = Sets.newHashSet("a");

		final DefaultTaskServiceQueryProvider queryProvider = new DefaultTaskServiceQueryProvider();
		final Map<String, Object> parameters = queryProvider.setQueryParameters(myGroups, processTriggerTasks, nodeId, noNode,
				Collections.emptySet(), Duration.ofHours(1));

		final String query = queryProvider.getValidTasksToExecuteQuery(myGroups, processTriggerTasks, true, true);

		final FlexibleSearchQuery q = new FlexibleSearchQuery(query);
		q.setResultClassList(Collections.singletonList(PK.class));
		q.addQueryParameters(parameters);
		final SearchResult<PK> search = flexibleSearchService.search(q);

		assertThat(search).isNotNull();
		assertThat(search.getResult()).contains(t2, t3, t2old, t3old).doesNotContain(t1, t1old, t4, t4old, t5, t5old);
	}

	private TaskBuilder youngTask()
	{
		return new TaskBuilder(Instant.now(), taskService, createdTasks);
	}

	private TaskBuilder oldTask()
	{
		return new TaskBuilder(Instant.now().minus(Duration.ofHours(6)), taskService, createdTasks);
	}

	private static class TaskBuilder
	{
		private String nodeGroup = null;
		private Integer nodeId = null;
		private final Instant executionTime;
		private int conditions = 0;
		private final TaskService taskService;
		private final Set<PK> createdTasks;

		private TaskBuilder(final Instant now, final TaskService taskService, final Set<PK> createdTasks)
		{
			executionTime = now;
			this.taskService = taskService;
			this.createdTasks = createdTasks;
		}



		TaskBuilder withNodeId(final int nodeId)
		{
			this.nodeId = nodeId;
			return this;
		}

		TaskBuilder withNodeGroup(final String nodeGroup)
		{
			this.nodeGroup = nodeGroup;
			return this;
		}

		TaskBuilder withConditions(final int conditions)
		{
			this.conditions = conditions;
			return this;
		}

		PK build()
		{

			final ModelService modelService = Registry.getApplicationContext().getBean("modelService", ModelService.class);
			final TaskModel task = modelService.create(TaskModel.class);

			if (nodeId != null)
			{
				task.setNodeId(nodeId);
			}

			if (StringUtils.isNotBlank(nodeGroup))
			{
				task.setNodeGroup(nodeGroup);
			}
			task.setRunnerBean("runner");
			task.setExecutionDate(Date.from(executionTime));

			if (conditions > 0)
			{
				final Set<TaskConditionModel> conditionModels = new HashSet<>(conditions);
				for (int i = 0; i < conditions; i++)
				{
					final TaskConditionModel condition = modelService.create(TaskConditionModel.class);
					condition.setFulfilled(false);
					condition.setUniqueID(UUID.randomUUID().toString());
					conditionModels.add(condition);
				}
				task.setConditions(conditionModels);
			}

			taskService.scheduleTask(task);
			createdTasks.add(task.getPk());
			return task.getPk();
		}
	}

	private void assureTaskEngineStopped()
	{
		int shutdownWait = 60;
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
				.overridingErrorMessage("Task Engine failed to shut down in " + 60 + " seconds.").isFalse();
	}
}