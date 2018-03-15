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
package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.internal.model.ScriptingJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class CronJobLoggingTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private ModelService modelService;
	private CronJobModel cronJob;
	private final PropertyConfigSwitcher logToDbTrhesoldSwitcher = new PropertyConfigSwitcher("cronjob.logtodb.threshold");

	@Before
	public void setUp() throws Exception
	{
		final ScriptingJobModel job = modelService.create(ScriptingJobModel.class);
		job.setScriptURI("model://fake.uri");
		job.setCode("testJob");
		modelService.save(job);

		cronJob = modelService.create(CronJobModel.class);
		cronJob.setCode("testCronJob");
		cronJob.setJob(job);
		cronJob.setLogToDatabase(Boolean.TRUE);
        // WARNING is default
        cronJob.setLogLevelDatabase(JobLogLevel.WARNING);
	}

	@After
	public void tearDown() throws Exception
	{
		logToDbTrhesoldSwitcher.switchBackToDefault();
	}

    @Test
    public void shouldLowerDatabaseLogLevelToConfiguredOneIfEffectiveLevelExeedsConfiguration() throws Exception
    {
    	// given
        logToDbTrhesoldSwitcher.switchToValue("INFO");
        cronJob.setLogLevelDatabase(JobLogLevel.DEBUG);
        modelService.save(cronJob);

    	// when
        final JobLogLevel dbLogLevel = cronJob.getLogLevelDatabase();

        // then
        assertThat(dbLogLevel).isEqualTo(JobLogLevel.INFO);
    }

	@Test
	public void shouldNotLogToDatabaseIfMessageHasLowerLevelThanConfigured() throws Exception
	{
		// given
		logToDbTrhesoldSwitcher.switchToValue("WARN");
		modelService.save(cronJob);

		// when
		writeLogEntry("test message", "INFO");

		// then
        assertThat(cronJob.getLogs()).isEmpty();
	}

    @Test
    public void shouldLogToDatabaseIfMessageHasSameLevelThanConfiguredDebugLevel() throws Exception
    {
        // given
        logToDbTrhesoldSwitcher.switchToValue("DEBUG");
        cronJob.setLogLevelDatabase(JobLogLevel.DEBUG);
        modelService.save(cronJob);

        // when
        writeLogEntry("test message", "DEBUG");

        // then
        assertThat(cronJob.getLogs()).isNotEmpty();
        assertThat(cronJob.getLogs().get(0).getMessage()).isEqualTo("test message");
    }

	@Test
	public void shouldLogToDatabaseIfMessageHasSameLevelAsConfigured() throws Exception
	{
		// given
		logToDbTrhesoldSwitcher.switchToValue("WARN");
		modelService.save(cronJob);

		// when
		writeLogEntry("test message", "WARNING");

		// then
        assertThat(cronJob.getLogs()).isNotEmpty();
        assertThat(cronJob.getLogs().get(0).getMessage()).isEqualTo("test message");
	}


	@Test
	public void shouldLogToDatabaseIfMessageHasGreaterLevelThanConfigured() throws Exception
	{
		// given
		logToDbTrhesoldSwitcher.switchToValue("WARN");
		modelService.save(cronJob);

		// when
		writeLogEntry("test message", "ERROR");

		// then
        assertThat(cronJob.getLogs()).isNotEmpty();
        assertThat(cronJob.getLogs().get(0).getMessage()).isEqualTo("test message");
	}

	private void writeLogEntry(final String message, final String level)
	{
		final CronJob _cronJob = modelService.getSource(cronJob);
		_cronJob.addLog(message, null, EnumerationManager.getInstance().getEnumerationValue("JobLogLevel", level), false);

	}
}
