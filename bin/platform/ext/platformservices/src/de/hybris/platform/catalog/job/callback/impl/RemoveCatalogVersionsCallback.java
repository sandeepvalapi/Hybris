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
package de.hybris.platform.catalog.job.callback.impl;

import de.hybris.platform.catalog.job.callback.RemoveCallback;
import de.hybris.platform.catalog.job.util.CatalogVersionJobDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.RemoveCatalogVersionCronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation for handling collection of the {@link CatalogVersionModel} as whole through the impex.
 */
public class RemoveCatalogVersionsCallback implements RemoveCallback<Collection<CatalogVersionModel>>
{

	private static final Logger LOG = Logger.getLogger(RemoveCatalogVersionsCallback.class.getName());

	private ModelService modelService;

	private CatalogVersionJobDao catalogVersionJobDao;

	@Required
	public void setCatalogVersionJobDao(final CatalogVersionJobDao catalogVersionJobDao)
	{
		this.catalogVersionJobDao = catalogVersionJobDao;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * Performed after import job finishes. Given <code>result</code> could be null if non impex removal was performed.
	 */
	@Override
	public void afterRemoved(final RemoveCatalogVersionCronJobModel cronJobModel,
			final Collection<CatalogVersionModel> catalogVersions, final ImportResult result)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Before performing remove callback for a cronjob(" + cronJobModel.getCode() + ")/catalogversions "
					+ catalogVersions);
		}
		try
		{
			for (final CatalogVersionModel catalogVersion : catalogVersions)
			{
				final int countAfter = catalogVersionJobDao.getItemInstanceCount(catalogVersions, catalogVersionJobDao
						.getOrderedComposedTypes());

				if (countAfter == 0)
				{

					LOG.info("Removing catalogversion " + catalogVersion);
					try
					{
						modelService.remove(catalogVersion);
					}
					catch (final SystemException e)
					{
						LOG.info("Could not delete catalogversion " + catalogVersion);
					}
				}
				else
				{
					LOG.info("Removing catalogversion impossible since there is(are)  " + countAfter + " item(s) left.");
				}
			}

			if (result != null && result.hasUnresolvedLines())
			{
				LOG.info("Import has finished (" + (result.isSuccessful() ? CronJobResult.SUCCESS : CronJobResult.FAILURE)
						+ " ),  nevertheless it had some unresolved lines ");
				cronJobModel.setNotRemovedItems(result.getUnresolvedLines());
			}

			modelService.save(cronJobModel);
		}
		finally
		{
			LOG.debug("After performing remove callback for a cronjob(" + cronJobModel.getCode() + ")/catalogversions "
					+ catalogVersions);
		}
	}

	/**
	 * Initiates the total amount of the items to remove
	 */
	@Override
	public void beforeRemove(final RemoveCatalogVersionCronJobModel cronJobModel,
			final Collection<CatalogVersionModel> catalogVersions)
	{

		final int maxItemCount = catalogVersionJobDao.getItemInstanceCount(catalogVersions, catalogVersionJobDao
				.getOrderedComposedTypes());
		cronJobModel.setTotalDeleteItemCount(Integer.valueOf(maxItemCount));
		modelService.save(cronJobModel);
		LOG.info("Start removing " + maxItemCount + " items.");

	}

	/**
	 * Performed while import job is running, poll called for the state of the import every 1 second. To reflect the
	 * status/progress of the process into {@link RemoveCatalogVersionCronJobModel}.
	 */
	@Override
	public void doRemove(final RemoveCatalogVersionCronJobModel cronJobModel,
			final Collection<CatalogVersionModel> catalogVersion, final ImportResult result)
	{
		if (result.getCronJob() != null)
		{
			//poll for some status
			modelService.refresh(result.getCronJob());
			cronJobModel.setCurrentProcessingItemCount(result.getCronJob().getValueCount());
			modelService.save(cronJobModel);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Updating current processed item counter for " + cronJobModel + " to "
						+ cronJobModel.getCurrentProcessingItemCount());
			}
		}
	}

}
