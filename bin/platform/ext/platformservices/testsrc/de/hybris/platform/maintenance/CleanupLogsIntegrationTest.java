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
package de.hybris.platform.maintenance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CompositeJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.LogFileModel;
import de.hybris.platform.jobs.GenericMaintenanceJobPerformable;
import de.hybris.platform.jobs.maintenance.impl.CleanUpLogsStrategy;
import de.hybris.platform.processengine.enums.BooleanOperator;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Date;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CleanupLogsIntegrationTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private SessionService sessionService;

	private CronJobModel pollutedCronJob;
	private CleanUpLogsStrategy culs;
	private GenericMaintenanceJobPerformable gmjp;
	private CompositeJobModel pollutedJob;

	@Before
	public void setUp()
	{
		final ItemObjectResolver modelResolver = Registry.getApplicationContext()
				.getBean("modelResolver", ItemObjectResolver.class);

		gmjp = new GenericMaintenanceJobPerformable();
		gmjp.setModelService(modelService);
		gmjp.setFlexibleSearchService(flexibleSearchService);
		gmjp.setSessionService(sessionService);
		gmjp.setModelResolver(modelResolver);

		culs = new CleanUpLogsStrategy();
		culs.setFlexibleSearchService(flexibleSearchService);
		culs.setModelService(modelService);

		gmjp.setMaintenanceCleanupStrategy(culs);

		pollutedJob = modelService.create(CompositeJobModel.class);
		pollutedJob.setActive(true);
		pollutedJob.setCode("pollutedJob");
		modelService.save(pollutedJob);

		pollutedCronJob = modelService.create(CronJobModel.class);
		pollutedCronJob.setActive(true);
		pollutedCronJob.setJob(pollutedJob);

		createLogFileModel("logFileModel1", pollutedCronJob, DateTime.now().minusDays(8).toDate());
		createLogFileModel("logFileModel2", pollutedCronJob, DateTime.now().minusDays(9).toDate());
		createLogFileModel("logFileModel3", pollutedCronJob, DateTime.now().minusDays(10).toDate());
		createLogFileModel("logFileModel4", pollutedCronJob, DateTime.now().minusDays(11).toDate());
		createLogFileModel("logFileModel5", pollutedCronJob, DateTime.now().minusDays(12).toDate());
		createLogFileModel("logFileModel6", pollutedCronJob, DateTime.now().minusDays(13).toDate());

		pollutedCronJob.setFilesCount(5);
		pollutedCronJob.setFilesDaysOld(10);

		modelService.save(pollutedCronJob);
	}

	@Test
	public void testAndOperator()
	{
		pollutedCronJob.setFilesOperator(BooleanOperator.AND);

		assertEquals(6, pollutedCronJob.getLogFiles().size());

		final PerformResult result = gmjp.perform(pollutedCronJob);

		assertEquals(CronJobStatus.FINISHED, result.getStatus());
		modelService.refresh(pollutedCronJob);

		assertEquals(5, pollutedCronJob.getLogFiles().size());
		assertContains(pollutedCronJob, "logFileModel1");
		assertContains(pollutedCronJob, "logFileModel2");
		assertContains(pollutedCronJob, "logFileModel3");
		assertContains(pollutedCronJob, "logFileModel4");
		assertContains(pollutedCronJob, "logFileModel5");
	}

	@Test
	public void testOrOperator()
	{
		pollutedCronJob.setFilesOperator(BooleanOperator.OR);

		assertEquals(6, pollutedCronJob.getLogFiles().size());

		final PerformResult result = gmjp.perform(pollutedCronJob);

		assertEquals(CronJobStatus.FINISHED, result.getStatus());
		modelService.refresh(pollutedCronJob);

		assertEquals(2, pollutedCronJob.getLogFiles().size());
		assertContains(pollutedCronJob, "logFileModel1");
		assertContains(pollutedCronJob, "logFileModel2");
	}

	@Test
	public void testQueryCount()
	{
		pollutedCronJob.setQueryCount(0);
		gmjp.perform(pollutedCronJob);

		assertThat(pollutedCronJob.getLogFiles().size()).isEqualTo(6);
	}

	private LogFileModel createLogFileModel(final String code, final CronJobModel owner, final Date creationTime)
	{
		final LogFileModel logFileModel = modelService.create(LogFileModel.class);
		logFileModel.setCode(code);
		logFileModel.setOwner(owner);
		logFileModel.setCreationtime(creationTime);
		modelService.save(logFileModel);
		return logFileModel;
	}

	private void assertContains(final CronJobModel cronJobModel, final String logFileCode)
	{
		boolean contains = false;
		for (final LogFileModel logFileModel : cronJobModel.getLogFiles())
		{
			if (logFileModel.getCode().equals(logFileCode))
			{
				contains = true;
				break;
			}
		}
		if (!contains)
		{
			fail("given cronjob model does NOT contain LogFile with code: " + logFileCode);
		}
	}
}
