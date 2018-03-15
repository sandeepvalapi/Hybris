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
package de.hybris.platform.catalog.synchronization;

import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * Represents results of the synchronization.
 */
public class SyncResult
{
	private final PK cronJobPK;
	private static final String BEAN_CRONJOBSERVICE = "cronJobService";
	private static final String BEAN_MODELSERVICE = "modelService";


	public SyncResult(final SyncItemCronJobModel syncCronJob)
	{
		this.cronJobPK = syncCronJob != null ? syncCronJob.getPk() : null;
	}

	/**
	 * Gets whether the synchronization was successfully.
	 *
	 * @return true if synchronization was successfully
	 * @throws IllegalStateException
	 *            in case synchronization is still running.
	 */
	public boolean isSuccessful()
	{
		return cronJobPK == null || getCronJobService().isSuccessful(getCronJob());
	}

	/**
	 * Gets whether the synchronization was not successfully.
	 *
	 * @return true if synchronization was not successfully
	 * @throws IllegalStateException
	 *            in case synchronization is still running.
	 */
	public boolean isError()
	{
		return cronJobPK != null && getCronJobService().isError(getCronJob());
	}

	/**
	 * Gets whether the synchronization is still running.
	 *
	 * @return true if synchronization is still running
	 */
	public boolean isRunning()
	{
		return cronJobPK != null && getCronJobService().isRunning(getCronJob());
	}


	/**
	 * Gets whether the synchronization is not running anymore.
	 *
	 * @return true if synchronization is not running
	 */
	public boolean isFinished()
	{
		return cronJobPK == null || getCronJobService().isFinished(getCronJob());
	}

	private CronJobService getCronJobService()
	{
		return Registry.getApplicationContext().getBean(BEAN_CRONJOBSERVICE, CronJobService.class);
	}

	private ModelService getModelService()
	{
		return Registry.getApplicationContext().getBean(BEAN_MODELSERVICE, ModelService.class);
	}

	public SyncItemCronJobModel getCronJob()
	{
		if (cronJobPK == null)
		{
			return null;
		}
		else
		{
			final ModelService modelService = getModelService();
			final SyncItemCronJobModel cronJob = modelService.get(cronJobPK);
			modelService.refresh(cronJob);
			return cronJob;
		}
	}
}
