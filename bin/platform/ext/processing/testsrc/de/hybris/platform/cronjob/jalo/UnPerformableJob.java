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

import de.hybris.platform.cronjob.jalo.CronJob.CronJobResult;


/**
 * this job isn't performable, at all!!!
 */
public class UnPerformableJob extends TestJob
{
	@Override
	protected boolean canPerform(final CronJob cronJob)
	{
		return false;
	}

	@Override
	protected CronJobResult performCronJob(final CronJob cronJob)
	{
		throw new IllegalStateException("... calling 'performCronJob(final CronJob cronJob)' shouldn't be possible at all!!!");
	}
}
