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
package de.hybris.platform.hac.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.hac.data.dto.CronJobData;
import de.hybris.platform.hac.facade.CronJobFacade;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DefaultCronJobFacadeTest
{
	private static final String JOB_CODE2 = "JobCode2";
	private static final String JOB_CODE1 = "JobCode1";
	private static final String CRON_JOB_CODE2 = "CronJobCode2";
	private static final String CRON_JOB_CODE1 = "CronJobCode1";

	@InjectMocks
	private final CronJobFacade cronJobFacade = new DefaultCronJobFacade();
	@Mock
	private CronJobService cronJobService;
	@Mock
	private CronJobModel cronJob1, cronJob2;
	@Mock
	private JobModel job1, job2;

	/**
	 * Test method for
	 * {@link de.hybris.platform.hac.facade.impl.DefaultCronJobFacade#getRunningOrRestartedCronJobsData()}.
	 */
	@Test
	public void shouldReturnEmptyResultListWhenThereIsNoRunningCronJobs()
	{
		// given
		given(cronJobService.getRunningOrRestartedCronJobs()).willReturn(Collections.EMPTY_LIST);

		// when
		final List<CronJobData> result = cronJobFacade.getRunningOrRestartedCronJobsData();

		// then
		assertThat(result).isNotNull().isEmpty();
		verify(cronJobService, times(1)).getRunningOrRestartedCronJobs();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.hac.facade.impl.DefaultCronJobFacade#getRunningOrRestartedCronJobsData()}.
	 */
	@Test
	public void shouldReturnEmptyResultListWhenCronJobServiceThrowedSystemException()
	{
		// given
		given(cronJobService.getRunningOrRestartedCronJobs()).willThrow(
				new SystemException("Returned result for running cron jobs is null "));

		// when
		final List<CronJobData> result = cronJobFacade.getRunningOrRestartedCronJobsData();

		// then
		assertThat(result).isNotNull().isEmpty();
		verify(cronJobService, times(1)).getRunningOrRestartedCronJobs();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.hac.facade.impl.DefaultCronJobFacade#getRunningOrRestartedCronJobsData()}.
	 */
	@Test
	public void shouldReturnEmptyResultListWhenThereArRunningCronJobs()
	{
		// given
		given(cronJobService.getRunningOrRestartedCronJobs()).willReturn(Lists.newArrayList(cronJob1, cronJob2));
		given(cronJob1.getCode()).willReturn(CRON_JOB_CODE1);
		given(cronJob2.getCode()).willReturn(CRON_JOB_CODE2);
		given(cronJob1.getJob()).willReturn(job1);
		given(cronJob2.getJob()).willReturn(job2);
		given(job1.getCode()).willReturn(JOB_CODE1);
		given(job2.getCode()).willReturn(JOB_CODE2);

		// when
		final List<CronJobData> result = cronJobFacade.getRunningOrRestartedCronJobsData();

		// then
		assertThat(result).isNotNull().isNotEmpty();
		assertThat(result).hasSize(2);
		verify(cronJobService, times(1)).getRunningOrRestartedCronJobs();
	}

	/**
	 * Test method for {@link de.hybris.platform.hac.facade.impl.DefaultCronJobFacade#requestAbortForRunningCronJobs()}.
	 */
	@Test
	public void shouldReturnEmptyResultMapWhenThereIsNoRunningCronJobsToAbort()
	{
		// given
		given(cronJobService.getRunningOrRestartedCronJobs()).willReturn(Collections.EMPTY_LIST);

		// when
		final Map<String, Boolean> requestResult = cronJobFacade.requestAbortForRunningCronJobs();

		// then
		assertThat(requestResult).isNotNull().isEmpty();
		verify(cronJobService, times(1)).getRunningOrRestartedCronJobs();
	}

	/**
	 * Test method for {@link de.hybris.platform.hac.facade.impl.DefaultCronJobFacade#requestAbortForRunningCronJobs()}.
	 */
	@Test
	public void shouldReturnEmptyResultMapWhenCronJobServiceThrowedSystemException()
	{
		// given
		given(cronJobService.getRunningOrRestartedCronJobs()).willThrow(
				new SystemException("Returned result for running cron jobs is null "));

		// when
		final Map<String, Boolean> requestResult = cronJobFacade.requestAbortForRunningCronJobs();

		// then
		assertThat(requestResult).isNotNull().isEmpty();
		verify(cronJobService, times(1)).getRunningOrRestartedCronJobs();
	}

	/**
	 * Test method for {@link de.hybris.platform.hac.facade.impl.DefaultCronJobFacade#requestAbortForRunningCronJobs()}.
	 */
	@Test
	public void shouldReturnEmptyResultMapWhenCronJobServiceHasReturnedRunningCronJobsButNonOfThemHasFlagIsRunning()
	{
		// given
		given(cronJobService.getRunningOrRestartedCronJobs()).willReturn(Lists.newArrayList(cronJob1, cronJob2));
		given(Boolean.valueOf(cronJobService.isRunning(cronJob1))).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(cronJobService.isRunning(cronJob2))).willReturn(Boolean.FALSE);

		// when
		final Map<String, Boolean> requestResult = cronJobFacade.requestAbortForRunningCronJobs();

		// then
		assertThat(requestResult).isNotNull().isEmpty();
		verify(cronJobService, times(1)).getRunningOrRestartedCronJobs();
		verify(cronJobService, times(2)).isRunning(any(CronJobModel.class));
	}

	/**
	 * Test method for {@link de.hybris.platform.hac.facade.impl.DefaultCronJobFacade#requestAbortForRunningCronJobs()}.
	 */
	@Test
	public void shouldRequestAbortForTwoRunningCronJobsIfTheyAreRunningAndAreAbortableAndReturnResulMapWithSizeOfTwo()
	{
		// given
		given(cronJobService.getRunningOrRestartedCronJobs()).willReturn(Lists.newArrayList(cronJob1, cronJob2));
		given(Boolean.valueOf(cronJobService.isRunning(cronJob1))).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(cronJobService.isRunning(cronJob2))).willReturn(Boolean.TRUE);
		given(cronJob1.getCode()).willReturn(CRON_JOB_CODE1);
		given(cronJob2.getCode()).willReturn(CRON_JOB_CODE2);

		// when
		final Map<String, Boolean> requestResult = cronJobFacade.requestAbortForRunningCronJobs();

		// then
		assertThat(requestResult).isNotNull().isNotEmpty();
		assertThat(requestResult).hasSize(2);
		assertThat(requestResult.get(CRON_JOB_CODE1)).isEqualTo(Boolean.TRUE);
		assertThat(requestResult.get(CRON_JOB_CODE2)).isEqualTo(Boolean.TRUE);
		verify(cronJobService, times(1)).getRunningOrRestartedCronJobs();
		verify(cronJobService, times(2)).isRunning(any(CronJobModel.class));
		verify(cronJobService, times(2)).requestAbortCronJob(any(CronJobModel.class));
	}

	/**
	 * Test method for {@link de.hybris.platform.hac.facade.impl.DefaultCronJobFacade#requestAbortForRunningCronJobs()}.
	 */
	@Test
	public void shouldRequestAbortForTwoRunningCronJobsIfTheyAreRunningAndOneIsAbortableAndAnotherNotAndReturnResulMapWithSizeOfTwo()
	{
		// given
		given(cronJobService.getRunningOrRestartedCronJobs()).willReturn(Lists.newArrayList(cronJob1, cronJob2));
		given(Boolean.valueOf(cronJobService.isRunning(cronJob1))).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(cronJobService.isRunning(cronJob2))).willReturn(Boolean.TRUE);
		willThrow(new IllegalStateException()).given(cronJobService).requestAbortCronJob(cronJob2);
		given(cronJob1.getCode()).willReturn(CRON_JOB_CODE1);
		given(cronJob2.getCode()).willReturn(CRON_JOB_CODE2);

		// when
		final Map<String, Boolean> requestResult = cronJobFacade.requestAbortForRunningCronJobs();

		// then
		assertThat(requestResult).isNotNull().isNotEmpty();
		assertThat(requestResult).hasSize(2);
		assertThat(requestResult.get(CRON_JOB_CODE1)).isEqualTo(Boolean.TRUE);
		assertThat(requestResult.get(CRON_JOB_CODE2)).isEqualTo(Boolean.FALSE);
		verify(cronJobService, times(1)).getRunningOrRestartedCronJobs();
		verify(cronJobService, times(2)).isRunning(any(CronJobModel.class));
		verify(cronJobService, times(2)).requestAbortCronJob(any(CronJobModel.class));
	}

}
