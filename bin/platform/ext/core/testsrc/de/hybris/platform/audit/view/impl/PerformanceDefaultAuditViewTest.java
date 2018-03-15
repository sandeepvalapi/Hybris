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

package de.hybris.platform.audit.view.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.util.Config;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;


@PerformanceTest
public class PerformanceDefaultAuditViewTest extends ServicelayerBaseTest
{
	private UserService userService;
	private AuditViewService auditViewService;
	private String userId;
	private String personalDataReport;
	private int parallelReportsGeneration;
	private Integer parallelFirstUserNumber;


	private Tenant previousTenant;

	@Before
	public void setUp() throws Exception
	{

		userId = Config.getString("test.audit.performance.user", "");
		personalDataReport = Config.getString("test.audit.performance.config", "");
		parallelReportsGeneration = Config.getInt("test.audit.performance.parallel.count", 1);

		if (parallelReportsGeneration < 1)
		{
			parallelReportsGeneration = 1;
		}

		Assume.assumeFalse("ignoring performance test - no user set to run a test", StringUtils.isBlank(userId));
		Assume.assumeFalse("ignoring performance test - no report configuration set to run a test",
				StringUtils.isBlank(personalDataReport));

		if (parallelReportsGeneration > 1)
		{
			parallelFirstUserNumber = Config.getInt("test.audit.performance.parallel.firstUser", 0);
		}

		previousTenant = Registry.activateMasterTenant();

		userService = Registry.getApplicationContext().getBean("userService", UserService.class);
		auditViewService = Registry.getApplicationContext().getBean("auditViewService", AuditViewService.class);
	}

	@After
	public void tearDown() throws Exception
	{
		if (previousTenant != null)
		{
			Registry.setCurrentTenant(previousTenant);
		}
	}

	@Test
	public void shouldRunPerformanceTest() throws Exception
	{
		if (parallelReportsGeneration == 1)
		{
			final UserModel user = userService.getUserForUID(userId);
			generateReport(user);
		}
		else
		{
			generateParallelReports();
		}
	}

	private void generateParallelReports()
			throws InterruptedException, java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException
	{
		final List<Duration> results = Collections.synchronizedList(new ArrayList<>());
		final TestThreadsHolder<Runnable> tth = new TestThreadsHolder<Runnable>(parallelReportsGeneration, threadNumber -> {
			final UserModel user = userService.getUserForUID(String.format(userId, parallelFirstUserNumber + threadNumber));
			return () -> {
				results.add(generateReport(user));
			};
		}, true);


		tth.startAll();
		tth.waitForAll(30, TimeUnit.MINUTES);

		assertThat(tth.getErrors()).isNullOrEmpty();

		final LongSummaryStatistics stats = results.stream().collect(Collectors.summarizingLong(Duration::toMillis));
		System.out.println("Count: " + stats.getCount());
		System.out.println("Min: " + DurationFormatUtils.formatDurationHMS(stats.getMin()));
		System.out.println("Avg: " + DurationFormatUtils.formatDurationHMS(Math.round(stats.getAverage())));
		System.out.println("Max: " + DurationFormatUtils.formatDurationHMS(stats.getMax()));
	}


	private Duration generateReport(final UserModel user)
	{
		System.out.println("Running test for " + user.getUid());
		final Instant start = Instant.now();

		final Stream<ReportView> s = auditViewService.getViewOn(TypeAuditReportConfig.builder().withConfigName(personalDataReport)
				.withFullReport().withRootTypePk(user.getPk()).build());

		final Duration time = Duration.between(start, Instant.now());
		System.out.println(s.count());
		System.out.println(DurationFormatUtils.formatDurationHMS(time.toMillis()));

		return time;
	}
}
