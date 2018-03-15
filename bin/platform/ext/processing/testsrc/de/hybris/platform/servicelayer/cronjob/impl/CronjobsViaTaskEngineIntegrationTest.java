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
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.internal.model.ScriptingJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.utils.NeedsTaskEngine;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for executing cronjobs via Task Engine.
 */
@IntegrationTest
@NeedsTaskEngine
public class CronjobsViaTaskEngineIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private CronJobService cronJobService;

	private final PropertyConfigSwitcher cronjobEnabledConfigSwitcher = new PropertyConfigSwitcher(
			"cronjob.timertask.loadonstartup");

	private ScriptingJobModel testJob, testJobForBadScript;
	private ScriptModel testScript, testScriptThrowingException;

	@Before
	public void setUp()
	{
		cronjobEnabledConfigSwitcher.switchToValue("true");
		testScript = modelService.create(ScriptModel.class);
		testScript.setCode("myGroovyScript");
		testScriptThrowingException = modelService.create(ScriptModel.class);
		testScriptThrowingException.setCode("myBadScript");
		final StringBuilder content = new StringBuilder();
		content.append("import de.hybris.platform.core.model.user.TitleModel\n");
		content.append("println 'hello groovy! '+ new Date()+ ' Cronjob='+cronjob\n");
		content.append("def title = modelService.create(TitleModel.class)\n");
		content.append("title.code='groovyTitle_'+System.currentTimeMillis()\n");
		testScriptThrowingException.setContent(content.toString() + "title.pk.longValue");

		content.append("modelService.save(title)\n");
		content.append("title.code");
		testScript.setContent(content.toString());
		modelService.saveAll(testScript, testScriptThrowingException);

		testJob = modelService.create(ScriptingJobModel.class);
		testJob.setCode("myGroovyJob");
		testJob.setScriptURI("model://myGroovyScript");
		testJobForBadScript = modelService.create(ScriptingJobModel.class);
		testJobForBadScript.setCode("myGroovyJobForBadScript");
		testJobForBadScript.setScriptURI("model://myBadScript");

		modelService.saveAll(testJob, testJobForBadScript);
	}

	@After
	public void tearDown()
	{
		cronjobEnabledConfigSwitcher.switchBackToDefault();
	}

	@Test
	public void testPerformCronJobManyTimesWithTriggerTask() throws Exception
	{
		final CronJobModel testCronJob = prepareCronjob("testCJ", testJob, false);
		modelService.save(testCronJob);
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronExpression("0 * * * * ? *"); //every minute
		trigger.setCronJob(testCronJob);
		modelService.save(trigger);

		waitTillCronJobFinished(testCronJob);
		assertThat(cronJobService.isSuccessful(testCronJob)).isTrue();
		Thread.currentThread().sleep(65000L); //wait one minute more for next execution

		final TaskModel triggerTask = findTaskForTrigger(trigger);
		assertThat(triggerTask).isNotNull();
		assertThat(triggerTask.getRetry()).isGreaterThanOrEqualTo(1);

		final List<TitleModel> titles = findTitles("groovyTitle");
		assertThat(titles).isNotEmpty().doesNotHaveDuplicates();
		assertThat(titles.size()).isGreaterThanOrEqualTo(2);
		for (final TitleModel title : titles)
		{
			assertThat(title.getCode()).startsWith("groovyTitle");
		}
	}

	@Test
	public void testPerformCronJobSingleExecutableWithTriggerTask() throws Exception
	{
		final CronJobModel testCronJobSingleExecutable = prepareCronjob("testCJ", testJob, true);
		modelService.save(testCronJobSingleExecutable);
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronExpression("0 * * * * ? *"); //every minute
		trigger.setCronJob(testCronJobSingleExecutable);
		modelService.save(trigger);

		waitTillCronJobFinished(testCronJobSingleExecutable);
		assertThat(cronJobService.isSuccessful(testCronJobSingleExecutable)).isTrue();
		Thread.currentThread().sleep(65000L); //wait one minute more for next execution

		final TaskModel triggerTask = findTaskForTrigger(trigger);
		assertThat(triggerTask).isNotNull();
		assertThat(triggerTask.getRetry()).isGreaterThanOrEqualTo(1);

		final List<TitleModel> titles = findTitles("groovyTitle");
		assertThat(titles).hasSize(1);
		assertThat(titles.get(0).getCode()).startsWith("groovyTitle");
	}

	@Test
	public void testPerformCronJobThrowingException() throws Exception
	{
		final CronJobModel testCronJobWithExc = prepareCronjob("testCJ", testJobForBadScript, false);
		modelService.save(testCronJobWithExc);
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronExpression("0 * * * * ? *"); //every minute
		trigger.setCronJob(testCronJobWithExc);
		modelService.save(trigger);

		waitTillCronJobFinished(testCronJobWithExc);
		assertThat(cronJobService.isSuccessful(testCronJobWithExc)).isFalse();

		Thread.currentThread().sleep(65000L); //wait one minute more for next execution

		final TaskModel triggerTask = findTaskForTrigger(trigger);
		assertThat(triggerTask).isNotNull();
		assertThat(triggerTask.getRetry()).isGreaterThanOrEqualTo(1);
		final List<TitleModel> titles = findTitles("groovyTitle");
		assertThat(titles).isNullOrEmpty();
	}

	@Test
	public void testPerformCronJobWithoutTriggerAndTaskEngine() throws Exception
	{
		final CronJobModel testCronJobWithoutTrigger = prepareCronjob("testCJ", testJob, false);
		modelService.save(testCronJobWithoutTrigger);

		cronJobService.performCronJob(testCronJobWithoutTrigger);

		waitTillCronJobFinished(testCronJobWithoutTrigger);
		assertThat(cronJobService.isSuccessful(testCronJobWithoutTrigger)).isTrue();

		final List<TitleModel> titles = findTitles("groovyTitle");
		assertThat(titles).hasSize(1);
		assertThat(titles.get(0).getCode()).startsWith("groovyTitle");
	}

	@Test
	public void testExecuteJobTriggeredWithoutCronJob() throws Exception
	{
		assertThat(testJob.getCronJobs()).hasSize(0);
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronExpression("0 * * * * ? *"); //every minute
		trigger.setJob(testJob);
		modelService.save(trigger);

		waitTillJobGetsCronJob(testJob);
		Thread.currentThread().sleep(10000L); //wait few seconds more to make sure execution finished

		final TaskModel triggerTask = findTaskForTrigger(trigger);
		assertThat(triggerTask).isNotNull();
		assertThat(triggerTask.getRetry()).isGreaterThanOrEqualTo(1);

		assertThat(testJob.getCronJobs()).hasSize(1);
		final CronJobModel cronJob = testJob.getCronJobs().iterator().next();
		assertThat(cronJobService.isSuccessful(cronJob)).isTrue();

		final List<TitleModel> titles = findTitles("groovyTitle");
		assertThat(titles).hasSize(1);
		assertThat(titles.get(0).getCode()).startsWith("groovyTitle");
	}

	private void waitTillCronJobFinished(final CronJobModel testCronJob) throws InterruptedException
	{
		int i = 0;
		while (!cronJobService.isFinished(testCronJob))
		{
			if (i++ > 25)
			{
				//within that time (25 * 5 seconds) the Cronjob Must have been executed - so something went wrong here
				Assert.fail("Cronjob could not finish his job for some reason. Current cronjob status: " + testCronJob.getStatus());
			}
			Thread.currentThread().sleep(5000L);
			modelService.refresh(testCronJob);
		}
	}

	private void waitTillJobGetsCronJob(final JobModel testJob) throws InterruptedException
	{
		int i = 0;
		while (testJob.getCronJobs().isEmpty())
		{
			if (i++ > 25)
			{
				//within that time (25 * 5 seconds) the Cronjob Must have been created - so something went wrong here
				Assert.fail("Cronjob could not be created or assigned to the job for some reason. ");
			}
			Thread.currentThread().sleep(5000L);
			modelService.refresh(testJob);
		}
	}

	private CronJobModel prepareCronjob(final String code, final JobModel job, final boolean singleExecutable)
	{
		final CronJobModel testCJ = modelService.create(CronJobModel.class);
		testCJ.setCode(code);
		testCJ.setSingleExecutable(Boolean.valueOf(singleExecutable));
		testCJ.setJob(job);
		return testCJ;
	}

	private TaskModel findTaskForTrigger(final TriggerModel trigger)
	{
		final SearchResult<TaskModel> searchResult = flexibleSearchService.search("SELECT {PK} from {TriggerTask} WHERE {trigger}="
				+ trigger.getPk());
		assertThat(searchResult).isNotNull();
		return searchResult.getResult().get(0);
	}

	private List<TitleModel> findTitles(final String codePrefix)
	{
		final SearchResult<TitleModel> searchResult = flexibleSearchService.search("SELECT {PK} from {Title} WHERE {code} LIKE '"
				+ codePrefix + "%'");
		assertThat(searchResult).isNotNull();
		return searchResult.getResult();
	}
}
