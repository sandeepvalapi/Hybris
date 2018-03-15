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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class MixedLogToFileCronJobTest extends ServicelayerBaseTest
{
	private ComposedType type;

	@Test
	public void shouldInvokeJobWithDisabledLoggingToFileFromWithinJobWithEnabledLoggingToFile() throws Exception
	{
		final CronJob testCronJob = execute(new JobWithTwoLevels(true, false), randomCode(), true);

		assertThat(testCronJob.getResult()).isEqualTo(testCronJob.getSuccessResult());
	}


	@Test
	public void shouldInvokeJobWithEnabledLoggingToFileFromWithinJobWithDisabledLoggingToFile() throws Exception
	{
		final CronJob testCronJob = execute(new JobWithTwoLevels(false, true), randomCode(), false);

		assertThat(testCronJob.getResult()).isEqualTo(testCronJob.getSuccessResult());
	}


	@Test
	public void shouldInterweave_Logging_notLogging_Logging_jobs() throws Exception
	{
		final CronJob testCronJob = execute(new JobWithThreeLevels(true, false, true), randomCode(), true);

		assertThat(testCronJob.getResult()).isEqualTo(testCronJob.getSuccessResult());
	}


	@Test
	public void shouldInterweave_notLogging_Logging_notLogging_jobs() throws Exception
	{
		final CronJob testCronJob = execute(new JobWithThreeLevels(false, true, false), randomCode(), false);

		assertThat(testCronJob.getResult()).isEqualTo(testCronJob.getSuccessResult());
	}


	private class JobWithThreeLevels implements TestJob.Performable
	{

		private final boolean firstLevelLogEnabled;
		private final boolean secondLevelLogEnabled;
		private final boolean thirdLevelLogEnabled;

		private JobWithThreeLevels(final boolean firstLevelLogEnabled, final boolean secondLevelLogEnabled,
				final boolean thirdLevelLogEnabled)
		{
			this.firstLevelLogEnabled = firstLevelLogEnabled;
			this.secondLevelLogEnabled = secondLevelLogEnabled;
			this.thirdLevelLogEnabled = thirdLevelLogEnabled;
		}

		@Override
		public CronJob.CronJobResult perform(final CronJob cronJob)
		{
			final CronJob nestedCronJob = execute(new JobWithTwoLevels(secondLevelLogEnabled, thirdLevelLogEnabled), randomCode(),
					secondLevelLogEnabled);

			boolean success = nestedCronJob.getResult().equals(nestedCronJob.getSuccessResult());
			if (firstLevelLogEnabled)
			{
				TestJob.getLog().info("Log message from top level");
				success = Job.getCurrentLogContainer() != null;
			}

			return cronJob.getFinishedResult(success);
		}
	}


	private class JobWithTwoLevels implements TestJob.Performable
	{

		private final boolean firstLevelLogEnabled;
		private final boolean secondLevelLogEnabled;

		public JobWithTwoLevels(final boolean firstLevelLogEnabled, final boolean secondLevelLogEnabled)
		{
			this.firstLevelLogEnabled = firstLevelLogEnabled;
			this.secondLevelLogEnabled = secondLevelLogEnabled;
		}

		@Override
		public CronJob.CronJobResult perform(final CronJob cronJob)
		{

			execute(new LastLevelPerformable(secondLevelLogEnabled), randomCode(), secondLevelLogEnabled);

			boolean success = true;
			if (firstLevelLogEnabled)
			{
				TestJob.getLog().info("Log message from second level");
				success = Job.getCurrentLogContainer() != null;
			}

			return cronJob.getFinishedResult(success);
		}
	}

	private class LastLevelPerformable implements TestJob.Performable
	{

		private final boolean loggingEnabled;

		public LastLevelPerformable(final boolean loggingEnabled)
		{
			this.loggingEnabled = loggingEnabled;
		}

		@Override
		public CronJob.CronJobResult perform(final CronJob cronJob)
		{
			if (loggingEnabled)
			{
				TestJob.getLog().info("Log message from last level");
			}

			return cronJob.getFinishedResult(true);
		}
	}

	private CronJob execute(final TestJob.Performable perf, final String code, final boolean logToFile)
	{
		try
		{
			final TestJob nestedTestJob = (TestJob) type.newInstance(Collections.singletonMap(Job.CODE, code));
			nestedTestJob.setPerformable(perf);

			final CronJob testCronJob = CronJobManager.getInstance().createCronJob(nestedTestJob, code, true);
			testCronJob.setLogToFile(logToFile);
			testCronJob.getJob().perform(testCronJob, true);
			return testCronJob;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	private String randomCode()
	{
		return "TopLevelCronJob" + System.currentTimeMillis();
	}


	@Before
	public void setUp() throws Exception
	{
		final TypeManager manager = TypeManager.getInstance();

		try
		{
			final ComposedType testMixedLogToFileSetting = manager.getComposedType("TestMixedLogToFileSetting");
			type = testMixedLogToFileSetting;
		}
		catch (final JaloItemNotFoundException e)
		{
			type = manager.createComposedType(manager.getComposedType(Job.class), "TestMixedLogToFileSetting");
			type.setJaloClass(TestJob.class);
		}
	}

}
