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
package de.hybris.platform.servicelayer.cronjob.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.internal.model.ScriptingJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.model.TriggerTaskModel;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


//Unignore it after enabling the interceptor
@Ignore
@IntegrationTest
public class TriggerPrepareInterceptorTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	private CronJobModel testCronjob;

	@Before
	public void setUp()
	{
		final ScriptingJobModel testJob = modelService.create(ScriptingJobModel.class);
		testJob.setCode("testJob");
		testJob.setScriptURI("model://anyScript");
		modelService.save(testJob);

		testCronjob = modelService.create(CronJobModel.class);
		testCronjob.setCode("testCronJob");
		testCronjob.setJob(testJob);
		modelService.save(testCronjob);
	}

	@Test
	public void testCreateTaskForNewTrigger() throws Exception
	{
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronJob(testCronjob);

		modelService.save(trigger);
		modelService.refresh(trigger);

		assertThat(trigger).isNotNull();
		assertThat(findTaskForTrigger(trigger)).isInstanceOf(TriggerTaskModel.class);
	}

	@Test
	public void testDoNothingWithTaskForUpdatedTrigger() throws Exception
	{
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronJob(testCronjob);

		modelService.save(trigger);
		modelService.refresh(trigger);

		final TaskModel currentTask = findTaskForTrigger(trigger);
		assertThat(currentTask).isNotNull();

		//update trigger - should NOT change the task
		trigger.setMonth(11);
		modelService.save(trigger);
		modelService.refresh(trigger);

		final TaskModel unchangedTaskForModifiedTrigger = findTaskForTrigger(trigger);
		assertThat(unchangedTaskForModifiedTrigger).isNotNull();
		assertThat(unchangedTaskForModifiedTrigger).isEqualTo(currentTask);
	}

	@Test
	public void testCheckNodeIdAssignmentForTask() throws Exception
	{
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronJob(testCronjob);

		modelService.save(trigger);
		modelService.refresh(trigger);

		assertThat(trigger).isNotNull();
		final TaskModel triggerTask = findTaskForTrigger(trigger);
		assertThat(triggerTask).isInstanceOf(TriggerTaskModel.class);
		assertThat(triggerTask.getNodeId()).isEqualTo(testCronjob.getNodeID());
	}

	private TaskModel findTaskForTrigger(final TriggerModel trigger)
	{
		final SearchResult<TaskModel> searchResult = flexibleSearchService.search("SELECT {PK} from {TriggerTask} WHERE {trigger}="
				+ trigger.getPk());
		assertThat(searchResult).isNotNull();
		return searchResult.getResult().get(0);
	}
}
