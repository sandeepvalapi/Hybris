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
package de.hybris.platform.catalog.job.strategy.impl;

import de.hybris.platform.catalog.job.strategy.RemoveStrategy;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.RemoveCatalogVersionCronJobModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import java.util.List;

import org.apache.log4j.Logger;


/**
 * Specific strategy for removing {@link CatalogModel}.
 */
public class RemoveCatalogStrategy extends AbstractRemoveStrategy implements RemoveStrategy<RemoveCatalogVersionCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(RemoveCatalogStrategy.class.getName());


	@Override
	public PerformResult remove(final RemoveCatalogVersionCronJobModel cronJob)
	{
		final CatalogModel catalog = cronJob.getCatalog();

		LOG.info("Started removeCatalogStrategy for a job " + cronJob.getCode() + " for removing catalog: " + catalog.getId());

		final List<ComposedTypeModel> orderedComposedTypes = catalogVersionDao.getOrderedComposedTypes();

		removeCatalogVersionCollection(catalog.getCatalogVersions(), cronJob, orderedComposedTypes);

		modelService.refresh(catalog);

		final int itemInstancesLeft = catalogVersionDao.getItemInstanceCount(catalog, orderedComposedTypes);
		if (catalog.getCatalogVersions().isEmpty() && itemInstancesLeft == 0)
		{
			try
			{
				modelService.remove(catalog);
				return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}
			catch (final SystemException e)
			{
				LOG.error("Could not remove Catalog " + catalog.getId() + " for details set debug log level");
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage(), e);
				}
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
			}
		}
		else
		{
			LOG.error("Could not remove Catalog " + catalog.getId() + " catalog versions [" + catalog.getCatalogVersions()
					+ "], item instances = " + itemInstancesLeft);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
		}

	}
}
