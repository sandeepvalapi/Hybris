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
package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.model.CleanUpCronJobModel;
import de.hybris.platform.cronjob.model.JobModel;

/**
 * This factory is intended to be just a test factory. Not for a production usage.
 */
public class TestCronJobFactory implements CronJobFactory<CleanUpCronJobModel, JobModel>
{
	@Override
	public CleanUpCronJobModel createCronJob(final JobModel jobModel)
	{
		final CleanUpCronJobModel cronJob = new CleanUpCronJobModel();
		cronJob.setCode("testCleanupCronJob");
		cronJob.setJob(jobModel);

		return cronJob;
	}

}
