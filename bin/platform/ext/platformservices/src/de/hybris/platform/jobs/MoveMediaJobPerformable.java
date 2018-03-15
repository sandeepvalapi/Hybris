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

import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.MoveMediaCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.media.MediaIOException;
import de.hybris.platform.servicelayer.media.MediaService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Moves medias configured at {@link MoveMediaCronJobModel#getMedias()} to folder configured at
 * {@link MoveMediaCronJobModel#getTargetFolder()}. Is abortable between each media move.
 * 
 * @spring.bean moveMediaJob
 */
public class MoveMediaJobPerformable extends AbstractJobPerformable<MoveMediaCronJobModel>
{
	private static final Integer INITIAL_MEDIA_COUNT = Integer.valueOf(0);

	private static final Logger LOG = Logger.getLogger(MoveMediaJobPerformable.class.getName());

	private MediaService mediaService;

	@Override
	public PerformResult perform(final MoveMediaCronJobModel myCronJob)
	{
		if (myCronJob == null)
		{
			LOG.warn("Provided MoveMediaCronJobModel is null");
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
		}

		final MediaFolderModel newFolder = myCronJob.getTargetFolder();

		myCronJob.setMovedMediasCount(INITIAL_MEDIA_COUNT);
		modelService.save(myCronJob);

		boolean success = true;
		for (final MediaModel media : myCronJob.getMedias())
		{
			if (clearAbortRequestedIfNeeded(myCronJob))
			{
				return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
			}
			try
			{
				mediaService.setFolderForMedia(media, newFolder);
				increaseMoveCount(myCronJob);
			}
			catch (final MediaIOException e)
			{
				LOG.error("Exception during setting media " + media + " for folder " + newFolder + " in cronjob " + myCronJob + " : "
						+ e.getMessage() + ", for more details set debug log level");
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage(), e);
				}
				success = false;
			}
		}
		if (success)
		{
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
	}

	/**
	 * Increases the count status and saves it. Assumes value in cronjob is up to date before.
	 */
	private void increaseMoveCount(final MoveMediaCronJobModel cronJob)
	{
		cronJob.setMovedMediasCount(Integer.valueOf(cronJob.getMovedMediasCount().intValue() + 1));
		modelService.save(cronJob);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}
}
