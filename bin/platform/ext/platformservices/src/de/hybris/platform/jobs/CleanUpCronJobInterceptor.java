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
package de.hybris.platform.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CleanUpCronJobModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Collections;


/**
 * Validates that specific attributes of {@link CleanUpCronJobModel} are not empty or negative and sets defaults for
 * some attributes
 * 
 * @deprecated since ages - please use the{@link MaintenanceCleanupStrategy} / {@link GenericMaintenanceJobPerformable}
 */
@Deprecated
public class CleanUpCronJobInterceptor implements ValidateInterceptor, InitDefaultsInterceptor
{
	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CleanUpCronJobModel)
		{
			final CleanUpCronJobModel cronJob = (CleanUpCronJobModel) model;
			if (cronJob.getXDaysOld() < 0)
			{
				throw new InterceptorException("CleanUpCronJob.xDaysOld must be not negative");
			}
			if (cronJob.getStatuscoll() != null && cronJob.getStatuscoll().isEmpty())
			{
				throw new InterceptorException("CleanUpCronJob.statuscoll must be not empty");
			}
			if (cronJob.getResultcoll() != null && cronJob.getResultcoll().isEmpty())
			{
				throw new InterceptorException("CleanUpCronJob.resultcoll must be not empty");
			}
		}
	}

	@Override
	public void onInitDefaults(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CleanUpCronJobModel)
		{
			final CleanUpCronJobModel cronJob = (CleanUpCronJobModel) model;
			if (cronJob.getStatuscoll() == null)
			{
				cronJob.setStatuscoll(Collections.singleton(CronJobStatus.FINISHED));
			}
			if (cronJob.getResultcoll() == null)
			{
				cronJob.setResultcoll(Collections.singleton(CronJobResult.SUCCESS));
			}
		}
	}
}
