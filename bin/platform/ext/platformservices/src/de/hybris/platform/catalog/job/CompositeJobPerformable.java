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
package de.hybris.platform.catalog.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.cronjob.jalo.TriggerableJob;
import de.hybris.platform.cronjob.model.CompositeCronJobModel;
import de.hybris.platform.cronjob.model.CompositeEntryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobFactory;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.cronjob.TriggerService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Executes synchronously in order of {@link CompositeCronJobModel#getCompositeEntries()} underlying
 * {@link CronJobModel} or {@link TriggerModel} created upon {@link CompositeEntryModel#getTriggerableJob()}.
 * <p>
 * {@link CronJobModel} instances are fired by {@link CronJobService#performCronJob(CronJobModel,boolean)}
 * <p>
 * {@link TriggerModel} instance are fired by {@link TriggerService#activate(TriggerModel)}.
 */
public class CompositeJobPerformable extends AbstractJobPerformable<CompositeCronJobModel>
{

	private final static Logger LOG = Logger.getLogger(CompositeJobPerformable.class.getName());

	private static final int DEFAULT_WAIT = 1000; // 1 sec.

	private int wait = DEFAULT_WAIT;

	private CronJobService cronJobService;

	/**
	 * Sets the wait time in poll loop checking the {@link CompositeEntryModel#getExecutableCronJob()} or
	 * {@link CompositeEntryModel#getTriggerableJob()} has ended.
	 */
	public void setWait(final int wait)
	{
		this.wait = wait;
	}

	@Required
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	@Override
	public PerformResult perform(final CompositeCronJobModel cronJob)
	{

		for (final CompositeEntryModel entry : cronJob.getCompositeEntries())
		{
			try
			{
				final CronJobModel executedCronJob = executeCompositeEntry(entry);

				while (cronJobService.isRunning(getRefreshedCronJobModel(executedCronJob)))
				{
					// Wait a little bit and try again
					try
					{
						Thread.sleep(wait);
					}
					catch (final InterruptedException e)
					{
						Thread.currentThread().interrupt();
					}
				}
			}
			catch (final SystemException e)
			{
				LOG.error("Error while performing the composite job entry " + entry + " :" + e.getMessage()
						+ ", for details set debug log level ");
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage(), e);
				}
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
			}
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * Performs {@link ModelService#refresh(Object model)} on given <code>cronJobModel</code> and returns it.
	 */
	protected CronJobModel getRefreshedCronJobModel(final CronJobModel cronJobModel)
	{
		modelService.refresh(cronJobModel);
		return cronJobModel;
	}

	/**
	 * Executes a {@link CompositeEntryModel#getTriggerableJob()} or {@link CompositeEntryModel#getExecutableCronJob()}
	 * respectively for a given <code>compositeEntryModel</code>.
	 */
	protected CronJobModel executeCompositeEntry(final CompositeEntryModel compositeEntryModel)
	{
		CronJobModel executableCronJob = compositeEntryModel.getExecutableCronJob();

		if (executableCronJob != null)
		{
			//got the cronjob -> execute it
			cronJobService.performCronJob(executableCronJob, true);
		}
		else if (compositeEntryModel.getTriggerableJob() != null) //do we have a triggerable
		{
			final Job job = modelService.getSource(compositeEntryModel.getTriggerableJob());

			if (job instanceof TriggerableJob) //old way
			{
				executableCronJob = (CronJobModel) modelService.get(((TriggerableJob) job).newExecution());
			}
			else if (compositeEntryModel.getTriggerableJob() instanceof ServicelayerJobModel)
			{
				final ServicelayerJobModel serviceLayerTriggerable = (ServicelayerJobModel) compositeEntryModel.getTriggerableJob();
				final CronJobFactory cronJobFactory = cronJobService.getCronJobFactory(serviceLayerTriggerable);
				executableCronJob = cronJobFactory.createCronJob(serviceLayerTriggerable);
			}
			else
			{
				throw new UnsupportedOperationException("Neither a CronJobModel or TriggerableJob instance was assigned!");
			}
			cronJobService.performCronJob(executableCronJob, true);
		}
		return executableCronJob;
	}

}
