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

import de.hybris.platform.catalog.job.strategy.RemoveStrategy;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.RemoveCatalogVersionCronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * 
 * Job performable implementation responsible for a removing {@link CatalogVersionModel} or {@link CatalogModel} with
 * all its content using impex based mechanisms.
 * 
 *@since 4.3
 *@spring.bean removeCatalogVersionJobPerformable
 */
public class RemoveCatalogVersionJobPerformable extends AbstractJobPerformable<RemoveCatalogVersionCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(RemoveCatalogVersionJobPerformable.class.getName());

	private RemoveStrategy<RemoveCatalogVersionCronJobModel> removeCatalogVersionStrategy;

	private RemoveStrategy<RemoveCatalogVersionCronJobModel> removeCatalogStrategy;

	@Required
	public void setRemoveCatalogStrategy(final RemoveStrategy<RemoveCatalogVersionCronJobModel> removeCatalogStrategy)
	{
		this.removeCatalogStrategy = removeCatalogStrategy;
	}

	@Required
	public void setRemoveCatalogVersionStrategy(final RemoveStrategy<RemoveCatalogVersionCronJobModel> removeCatalogVersionStrategy)
	{
		this.removeCatalogVersionStrategy = removeCatalogVersionStrategy;
	}

	@Override
	public PerformResult perform(final RemoveCatalogVersionCronJobModel cronJob)
	{
		if (cronJob.getCatalog() == null || !isAlive(cronJob))
		{
			LOG.error("The Catalog attribute is null or already deleted. Aborting! ");

			return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
		}
		//	private final List<ComposedType> sortedComposedTypeList = calculateComposedTypeOrder(CatalogManager.getInstance()
		//			.getAllCatalogItemTypes());
		try
		{
			if (cronJob.getCatalogVersion() == null)
			{
				return removeCatalogStrategy.remove(cronJob);
			}
			else
			{
				return removeCatalogVersionStrategy.remove(cronJob);
			}
		}
		catch (final SystemException e)
		{
			LOG.error("System exception occurred " + e.getMessage() + " in perform method for cronjob "
					+ (cronJob.getCode() != null ? cronJob.getCode() : "??") + " for more detail set debug log level");
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
		}
		catch (final Exception e)
		{
			LOG.error("General exception occurred " + e.getMessage() + " in perform method for cronjob "
					+ (cronJob.getCode() != null ? cronJob.getCode() : "??") + " for more detail set debug log level");
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
		}
	}

	protected boolean isAlive(final RemoveCatalogVersionCronJobModel cronJob)
	{
		return ((de.hybris.platform.catalog.jalo.Catalog) modelService.getSource(cronJob.getCatalog())).isAlive();
	}





}
