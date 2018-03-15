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

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class GenericMaintenanceJobPerformable extends AbstractMaintenanceJobPerformable
{
	private MaintenanceCleanupStrategy maintenanceCleanupStrategy;

	@Override
	public FlexibleSearchQuery getFetchQuery(final CronJobModel cronJob)
	{
		return maintenanceCleanupStrategy.createFetchQuery(cronJob);
	}

	@Override
	public void process(final List list, final CronJobModel cronJob)
	{
		maintenanceCleanupStrategy.process(list);
	}


	@Required
	public void setMaintenanceCleanupStrategy(final MaintenanceCleanupStrategy maintenanceCleanupStrategy)
	{
		this.maintenanceCleanupStrategy = maintenanceCleanupStrategy;
	}

}
