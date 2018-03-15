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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CompositeJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


@IntegrationTest
public class DefaultCronJobDaoTest extends ServicelayerBaseTest
{

	@Resource
	private ModelService modelService;


	@Resource
	private CronJobService cronJobService;


	@Resource
	private de.hybris.platform.servicelayer.cronjob.CronJobDao cronJobDao;

	@Before
	public void prepare()
	{
		createEntity("buzz");
		createEntity("dougie");
		createEntity("sniffy");
		createEntity("curlie");
		createEntity("woolie");

	}

	private void createEntity(final String code)
	{
		final JobModel jobOne = modelService.create(CompositeJobModel.class);
		jobOne.setCode(code);


		final CronJobModel cronJobOne = modelService.create(CronJobModel.class);
		cronJobOne.setCode(code);
		cronJobOne.setJob(jobOne);

		modelService.save(jobOne);

		modelService.save(cronJobOne);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetNullCronJob()
	{
		cronJobDao.findCronJobs(null);
	}

	@Test
	public void testGetEmptyCronJob()
	{
		final List<CronJobModel> result = cronJobDao.findCronJobs("");

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testGetNotEmptyNotExistingCronJob()
	{
		final List<CronJobModel> result = cronJobDao.findCronJobs("powerpuffy");

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

	}


	@Test
	public void testGetNotEmptyCronJob()
	{
		final List<CronJobModel> result = cronJobDao.findCronJobs("curlie");

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("curlie", result.get(0).getCode());
	}





	@Test
	public void testGetRuningCronJobsNone()
	{

		final List<CronJobModel> result = cronJobDao.findRunningCronJobs();

		Assert.assertNotNull(result);
		for (final CronJobModel cron : result)
		{
			Assert.assertFalse("buzz".equalsIgnoreCase(cron.getCode()));
			Assert.assertFalse("dougie".equalsIgnoreCase(cron.getCode()));
			Assert.assertFalse("sniffy".equalsIgnoreCase(cron.getCode()));
			Assert.assertFalse("curlie".equalsIgnoreCase(cron.getCode()));
			Assert.assertFalse("woolie".equalsIgnoreCase(cron.getCode()));

		}

	}

	@Test
	public void testAbortNotAbortableCronJob()
	{

		final CronJobService mockedCronJobService = Mockito.spy(cronJobService);

		final CronJobModel singleCronJob = cronJobDao.findCronJobs("curlie").get(0);

		final JobModel composite = modelService.create(CompositeJobModel.class);
		composite.setCode("composite");

		singleCronJob.setJob(composite);


		Mockito.doReturn(Boolean.TRUE).when(mockedCronJobService).isRunning(singleCronJob);
		Mockito.doReturn(Boolean.FALSE).when(mockedCronJobService).isAbortable(singleCronJob);

		try
		{
			mockedCronJobService.requestAbortCronJob(singleCronJob);
			Assert.fail("Should not be able to abort non abortable job ");
		}
		catch (final IllegalStateException e)
		{
			//ok here
		}

		Assert.assertFalse(singleCronJob.getRequestAbort().booleanValue());

	}

	@Test
	public void testAbortNotRunningCronJob()
	{

		final CronJobService mockedCronJobService = Mockito.spy(cronJobService);

		final CronJobModel singleCronJob = cronJobDao.findCronJobs("curlie").get(0);

		final JobModel composite = modelService.create(CompositeJobModel.class);
		composite.setCode("composite");

		singleCronJob.setJob(composite);

		Mockito.when(Boolean.valueOf(mockedCronJobService.isRunning(singleCronJob))).thenReturn(Boolean.FALSE);

		try
		{
			mockedCronJobService.requestAbortCronJob(singleCronJob);

		}
		catch (final IllegalStateException e)
		{
			Assert.fail("Should not  a IllegalStateException popup for not runinng job (even if its non abortable) ");
		}

		Assert.assertFalse(singleCronJob.getRequestAbort().booleanValue());

	}


	//abortable
	@Test
	public void testIsAbortableCronJobWithoutJob()
	{
		final ModelService modelServiceMock = Mockito.spy(modelService);
		((DefaultCronJobDao) cronJobDao).setModelService(modelServiceMock);
		final CronJobModel cronJobModel = new CronJobModel();
		final de.hybris.platform.cronjob.jalo.CronJob cronJob = Mockito.mock(de.hybris.platform.cronjob.jalo.CronJob.class);

		Mockito.doReturn(cronJob).when(modelServiceMock).getSource(cronJobModel);

		Assert.assertFalse(cronJobDao.isAbortable(cronJobModel));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsAbortableCronJobWithRemovedCronJob()
	{
		final ModelService modelServiceMock = Mockito.spy(modelService);
		((DefaultCronJobDao) cronJobDao).setModelService(modelServiceMock);
		final CronJobModel cronJobModel = new CronJobModel();

		//final de.hybris.platform.cronjob.jalo.CronJob cronJob = Mockito.mock(de.hybris.platform.cronjob.jalo.CronJob.class);

		Mockito.doReturn(null).when(modelServiceMock).getSource(cronJobModel);

		Assert.assertFalse(cronJobDao.isAbortable(cronJobModel));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsAbortableCronJobWithRemovedJob()
	{
		final JobModel jobModel = new JobModel();

		final CronJobModel cronJobModel = new CronJobModel();
		cronJobModel.setJob(jobModel);

		final ModelService modelServiceMock = Mockito.spy(modelService);
		((DefaultCronJobDao) cronJobDao).setModelService(modelServiceMock);

		final de.hybris.platform.cronjob.jalo.CronJob cronJob = Mockito.mock(de.hybris.platform.cronjob.jalo.CronJob.class);
		Mockito.doReturn(cronJob).when(modelServiceMock).getSource(cronJobModel);
		Mockito.doReturn(null).when(modelServiceMock).getSource(jobModel);

		Assert.assertFalse(cronJobDao.isAbortable(cronJobModel));

	}

	@Test
	public void testIsAbortableCronJob()
	{
		final JobModel jobModel = new JobModel();

		final CronJobModel cronJobModel = new CronJobModel();
		cronJobModel.setJob(jobModel);

		final ModelService modelServiceMock = Mockito.spy(modelService);
		((DefaultCronJobDao) cronJobDao).setModelService(modelServiceMock);

		final de.hybris.platform.cronjob.jalo.CronJob cronJob = Mockito.mock(de.hybris.platform.cronjob.jalo.CronJob.class);
		Mockito.doReturn(cronJob).when(modelServiceMock).getSource(cronJobModel);
		final de.hybris.platform.cronjob.jalo.Job job = Mockito.mock(de.hybris.platform.cronjob.jalo.Job.class);
		Mockito.doReturn(job).when(modelServiceMock).getSource(jobModel);

		Mockito.when(Boolean.valueOf(job.isAbortable(cronJob))).thenReturn(Boolean.TRUE);//mock jalo result

		Assert.assertTrue(cronJobDao.isAbortable(cronJobModel));

	}


	//performable
	@Test
	public void testIsPerformableCronJobWithoutJob()
	{
		final ModelService modelServiceMock = Mockito.spy(modelService);
		((DefaultCronJobDao) cronJobDao).setModelService(modelServiceMock);
		final CronJobModel cronJobModel = new CronJobModel();
		final de.hybris.platform.cronjob.jalo.CronJob cronJob = Mockito.mock(de.hybris.platform.cronjob.jalo.CronJob.class);

		Mockito.doReturn(cronJob).when(modelServiceMock).getSource(cronJobModel);

		Assert.assertFalse(cronJobDao.isPerformable(cronJobModel));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsPerformableCronJobWithRemovedCronJob()
	{
		final ModelService modelServiceMock = Mockito.spy(modelService);
		((DefaultCronJobDao) cronJobDao).setModelService(modelServiceMock);
		final CronJobModel cronJobModel = new CronJobModel();

		//final de.hybris.platform.cronjob.jalo.CronJob cronJob = Mockito.mock(de.hybris.platform.cronjob.jalo.CronJob.class);

		Mockito.doReturn(null).when(modelServiceMock).getSource(cronJobModel);

		Assert.assertFalse(cronJobDao.isPerformable(cronJobModel));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsPerformableCronJobWithRemovedJob()
	{
		final JobModel jobModel = new JobModel();

		final CronJobModel cronJobModel = new CronJobModel();
		cronJobModel.setJob(jobModel);

		final ModelService modelServiceMock = Mockito.spy(modelService);
		((DefaultCronJobDao) cronJobDao).setModelService(modelServiceMock);

		final de.hybris.platform.cronjob.jalo.CronJob cronJob = Mockito.mock(de.hybris.platform.cronjob.jalo.CronJob.class);
		Mockito.doReturn(cronJob).when(modelServiceMock).getSource(cronJobModel);
		Mockito.doReturn(null).when(modelServiceMock).getSource(jobModel);

		Assert.assertFalse(cronJobDao.isPerformable(cronJobModel));

	}

	@Test
	public void testIsPerformableCronJob()
	{
		final JobModel jobModel = new JobModel();

		final CronJobModel cronJobModel = new CronJobModel();
		cronJobModel.setJob(jobModel);

		final ModelService modelServiceMock = Mockito.spy(modelService);
		((DefaultCronJobDao) cronJobDao).setModelService(modelServiceMock);

		final de.hybris.platform.cronjob.jalo.CronJob cronJob = Mockito.mock(de.hybris.platform.cronjob.jalo.CronJob.class);
		Mockito.doReturn(cronJob).when(modelServiceMock).getSource(cronJobModel);
		final de.hybris.platform.cronjob.jalo.Job job = Mockito.mock(de.hybris.platform.cronjob.jalo.Job.class);
		Mockito.doReturn(job).when(modelServiceMock).getSource(jobModel);

		Mockito.when(Boolean.valueOf(job.isPerformable(cronJob))).thenReturn(Boolean.TRUE);//mock jalo result

		Assert.assertTrue(cronJobDao.isPerformable(cronJobModel));

	}

}
