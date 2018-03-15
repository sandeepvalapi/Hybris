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
package de.hybris.platform.servicelayer.cronjob.scripting;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.scripting.engine.exception.ScriptCompilationException;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.internal.model.ScriptingJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.daos.TitleDao;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration tests for cronjob scripting
 */
@IntegrationTest
public class CronJobScriptingIntegrationTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private CronJobService cronJobService;
	@Resource
	private ModelService modelService;

	@Resource
	private TitleDao titleDao;

	ScriptModel existingGroovyScript;
	ScriptModel existingGroovyScriptWithPerformResult;
	ScriptModel existingBadGroovyScript;
	ScriptingJobModel scriptingJobForExistingScript;
	ScriptingJobModel scriptingJobForNotExistingScript;
	ScriptingJobModel scriptingJobForBadScript;
	ScriptingJobModel scriptingJobForExistingScriptWithPerformResult;

	private static final String MY_INFO_LOG = "My info log from script";
	private static final String MY_WARN_LOG = "My warning log from script";
	private static final String MY_ERROR_LOG = "My error log from script";

	@Before
	public void setUp() throws Exception
	{
		existingGroovyScript = modelService.create(ScriptModel.class);
		existingGroovyScript.setCode("myGroovyScript");
		existingGroovyScriptWithPerformResult = modelService.create(ScriptModel.class);
		existingGroovyScriptWithPerformResult.setCode("myGroovyScriptWithPerformResult");

		final StringBuilder content = new StringBuilder();
		content.append("import de.hybris.platform.core.model.user.TitleModel\n");
		content.append("import de.hybris.platform.servicelayer.cronjob.PerformResult\n");
		content.append("import de.hybris.platform.cronjob.enums.CronJobStatus\n");
		content.append("import de.hybris.platform.cronjob.enums.CronJobResult\n");
		content.append("def title = modelService.create(TitleModel.class)\n");
		content.append("title.code=\"groovyTitle\"\n");
		content.append("modelService.save(title)\n");
		content.append("log.info('" + MY_INFO_LOG + "')\n");
		content.append("log.warn('" + MY_WARN_LOG + "')\n");
		content.append("log.error('" + MY_ERROR_LOG + "')\n");
		existingGroovyScript.setContent(content.toString() + ("title")); // returned script result - titleModel instance
		existingGroovyScriptWithPerformResult.setContent(content.toString()
				+ ("new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.PAUSED)")); // script result - PerformResult
		modelService.saveAll(existingGroovyScript, existingGroovyScriptWithPerformResult);

		existingBadGroovyScript = modelService.create(ScriptModel.class);
		existingBadGroovyScript.setCode("myBadGroovyScript");
		existingBadGroovyScript.setContent("errors in groovy script!");
		modelService.save(existingBadGroovyScript);

		scriptingJobForExistingScript = modelService.create(ScriptingJobModel.class);
		scriptingJobForExistingScript.setCode("myGroovyJob");
		scriptingJobForExistingScript.setScriptURI("model://myGroovyScript");

		scriptingJobForExistingScriptWithPerformResult = modelService.create(ScriptingJobModel.class);
		scriptingJobForExistingScriptWithPerformResult.setCode("myGroovyJobWithPerformResult");
		scriptingJobForExistingScriptWithPerformResult.setScriptURI("model://myGroovyScriptWithPerformResult");

		scriptingJobForNotExistingScript = modelService.create(ScriptingJobModel.class);
		scriptingJobForNotExistingScript.setCode("myGroovyJobForNotExistingScript");
		scriptingJobForNotExistingScript.setScriptURI("model://myGroovyScriptDoesNotExist");

		scriptingJobForBadScript = modelService.create(ScriptingJobModel.class);
		scriptingJobForBadScript.setCode("myGroovyJobForBadScript");
		scriptingJobForBadScript.setScriptURI("model://myBadGroovyScript");

		modelService.saveAll(scriptingJobForExistingScript, scriptingJobForNotExistingScript, scriptingJobForBadScript);
	}

	@Test
	public void testPerformCronJobForStoredCorrectScript() throws Exception
	{
		final CronJobModel cronjob = prepareCronJob("myCronjob", scriptingJobForExistingScript, JobLogLevel.WARNING);
		assertThat(titleDao.findTitleByCode("groovyTitle")).isNull();

		cronJobService.performCronJob(cronjob, true);

		assertThat(cronJobService.isSuccessful(cronjob));
		assertThat(cronJobService.isFinished(cronjob));
		assertThat(titleDao.findTitleByCode("groovyTitle")).isNotNull();
		assertThat(cronJobService.getLogsAsText(cronjob)).contains(MY_WARN_LOG).contains(MY_ERROR_LOG).doesNotContain(MY_INFO_LOG);
	}

	@Test
	public void testPerformCronJobForStoredCorrectScriptWithPerformResult() throws Exception
	{
		final CronJobModel cronjob = prepareCronJob("myCronjob", scriptingJobForExistingScriptWithPerformResult,
				JobLogLevel.WARNING);
		assertThat(titleDao.findTitleByCode("groovyTitle")).isNull();
		cronJobService.performCronJob(cronjob, true);

		assertThat(cronJobService.isFinished(cronjob)).isFalse();
		assertThat(cronJobService.isPaused(cronjob));
		assertThat(titleDao.findTitleByCode("groovyTitle")).isNotNull();
		assertThat(cronJobService.getLogsAsText(cronjob)).contains(MY_WARN_LOG).contains(MY_ERROR_LOG).doesNotContain(MY_INFO_LOG);
	}

	@Test
	public void testPerformCronJobForNotExistingScript() throws Exception
	{
		final CronJobModel cronjob = prepareCronJob("myCronjob", scriptingJobForNotExistingScript, JobLogLevel.ERROR);

		cronJobService.performCronJob(cronjob, true);
		// during script lookup - ScripNotFoundException is thrown but the job perform logic swallows it
		// This way it's the Exception is not visible inside CronJobScriptingPerformable, only in stored logs
		assertThat(cronJobService.isError(cronjob));
		assertThat(cronJobService.isFinished(cronjob));
		assertThat(cronJobService.getLogsAsText(cronjob)).contains(ScriptNotFoundException.class.getName())
				.doesNotContain(MY_INFO_LOG).doesNotContain(MY_WARN_LOG).doesNotContain(MY_ERROR_LOG);
	}

	@Test
	public void testPerformCronJobForBadScript() throws Exception
	{
		final CronJobModel cronjob = prepareCronJob("myCronjob", scriptingJobForBadScript, JobLogLevel.ERROR);

		cronJobService.performCronJob(cronjob, true);
		assertThat(cronJobService.isError(cronjob));
		assertThat(cronJobService.isFinished(cronjob));
		assertThat(cronJobService.getLogsAsText(cronjob)).contains(ScriptCompilationException.class.getName())
				.doesNotContain(MY_INFO_LOG).doesNotContain(MY_WARN_LOG).doesNotContain(MY_ERROR_LOG);
	}

	private CronJobModel prepareCronJob(final String code, final JobModel job, final JobLogLevel dbLogLevel)
	{
		final CronJobModel cronjob = modelService.create(CronJobModel.class);
		cronjob.setCode(code);
		cronjob.setSingleExecutable(Boolean.TRUE);
		cronjob.setJob(job);
		cronjob.setLogToDatabase(Boolean.TRUE);
		cronjob.setLogLevelDatabase(dbLogLevel);
		modelService.save(cronjob);
		return cronjob;
	}
}
