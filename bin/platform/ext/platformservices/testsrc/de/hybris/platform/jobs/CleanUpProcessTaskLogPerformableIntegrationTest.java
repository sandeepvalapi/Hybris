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
 */
package de.hybris.platform.jobs;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.ProcessTaskLogMaintenanceJobModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskLogModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.task.utils.NeedsTaskEngine;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
@NeedsTaskEngine
public class CleanUpProcessTaskLogPerformableIntegrationTest extends ServicelayerTransactionalTest
{
	private CleanUpProcessTaskLogPerformable performable;
	private ProcessTaskLogMaintenanceJobModel model;
	private CronJobModel cronJob;
	ProcessTaskLogModel processTaskLog2;
	ProcessTaskLogModel processTaskLog3;
	private final String PREFIX = "CleanUpProcessTaskLog";
	private final String QUERY = "SELECT {" + ProcessTaskLogModel.PK + "} FROM {" + ProcessTaskLogModel._TYPECODE + "} WHERE {" + ProcessTaskLogModel.ACTIONID +"} = '"+ PREFIX +"'";
    @Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		performable = new CleanUpProcessTaskLogPerformable();
		performable.setFlexibleSearchService(flexibleSearchService);
		performable.setModelService(modelService);
		model = modelService.create(ProcessTaskLogMaintenanceJobModel.class);

		cronJob = new CronJobModel();
		cronJob.setCode("myDynamicCronJob");
		cronJob.setJob(model);

		prepareData();
	}

	private void prepareData()
	{
		final BusinessProcessModel bpm = modelService.create(BusinessProcessModel.class);
		bpm.setCode(UUID.randomUUID().toString());
		bpm.setProcessDefinitionName("name");

		final ProcessTaskLogModel processTaskLog = modelService.create(ProcessTaskLogModel.class);
		processTaskLog.setStartDate(new Date(1, 3, 4));
		processTaskLog.setEndDate(new Date(1, 3, 4));
		processTaskLog.setActionId(PREFIX);
		processTaskLog.setClusterId(0);
		processTaskLog.setProcess(bpm);

		processTaskLog2 = modelService.create(ProcessTaskLogModel.class);
		processTaskLog2.setStartDate(new Date());
		processTaskLog2.setEndDate(new Date());
		processTaskLog2.setActionId(PREFIX);
		processTaskLog2.setClusterId(0);
		processTaskLog2.setProcess(bpm);

		processTaskLog3 = modelService.create(ProcessTaskLogModel.class);
		processTaskLog3.setStartDate(new Date(112, 3, 4));
		processTaskLog3.setEndDate(new Date());
		processTaskLog3.setActionId(PREFIX);
		processTaskLog3.setClusterId(0);
		processTaskLog3.setProcess(bpm);

		modelService.save(processTaskLog);
		modelService.save(processTaskLog2);
		modelService.save(processTaskLog3);
	}

	@Test
	public void testSuccesfullExecution() throws Exception
	{
		//when
		final PerformResult performResult = performable.perform(cronJob);

		//then
		assertThat(performResult).isNotNull();
		assertThat(performResult.getResult()).isEqualTo(CronJobResult.SUCCESS);
		assertThat(performResult.getStatus()).isEqualTo(CronJobStatus.FINISHED);
	}

	@Test
	public void testShouldRemoveOneLog() throws Exception
	{
		//given
		model.setAge(6000);
		model.setNumberOfLogs(0);
		//
		//when
		final PerformResult performResult = performable.perform(cronJob);

		//then
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(QUERY);
		fsq.setResultClassList(Arrays.asList(PK.class));
		final SearchResult<PK> searchRes = flexibleSearchService.search(fsq);
		final List<PK> result = searchRes.getResult();
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).getLongValue()).isEqualTo(processTaskLog2.getPk().getLongValue());
		assertThat(result.get(1).getLongValue()).isEqualTo(processTaskLog3.getPk().getLongValue());
		assertThat(performResult).isNotNull();
		assertThat(performResult.getResult()).isEqualTo(CronJobResult.SUCCESS);
	}

	@Test
	public void testShouldRemoveThreeLogs() throws Exception
	{
		//given
		model.setAge(0);
		model.setNumberOfLogs(0);

		//when
		final PerformResult performResult = performable.perform(cronJob);

		//then
		final SearchResult<PK> searchRes = flexibleSearchService.search(QUERY);
		final List<PK> result = searchRes.getResult();
		assertThat(result.size()).isEqualTo(0);
		assertThat(performResult).isNotNull();
		assertThat(performResult.getResult()).isEqualTo(CronJobResult.SUCCESS);
	}

	@Test
	public void testShouldRemoveZeroLogs() throws Exception
	{
		//given
		model.setAge(0);
		model.setNumberOfLogs(0);
		model.setQueryCount(0);

		//when
		final PerformResult performResult = performable.perform(cronJob);

		//then
		final SearchResult<PK> searchRes = flexibleSearchService.search(QUERY);
		final List<PK> result = searchRes.getResult();
		assertThat(result.size()).isEqualTo(3);
		assertThat(performResult).isNotNull();
		assertThat(performResult.getResult()).isEqualTo(CronJobResult.SUCCESS);
	}
}
