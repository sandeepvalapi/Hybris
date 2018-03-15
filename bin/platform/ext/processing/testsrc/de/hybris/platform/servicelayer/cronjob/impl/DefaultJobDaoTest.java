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
import de.hybris.platform.servicelayer.cronjob.JobDao;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultJobDaoTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private JobDao jobDao;

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
	public void testGetNullJob()
	{
		jobDao.findJobs(null);
	}

	@Test
	public void testGetEmptyJob()
	{
		final List<JobModel> result = jobDao.findJobs("");

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testGetNotEmptyNotExistingJob()
	{
		final List<JobModel> result = jobDao.findJobs("powerpuffy");

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

	}


	@Test
	public void testGetNotEmptyJob()
	{
		final List<JobModel> result = jobDao.findJobs("curlie");

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("curlie", result.get(0).getCode());
	}
}
