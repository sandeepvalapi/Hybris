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
package de.hybris.platform.jobs.maintenance;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jobs.GenericMaintenanceJobPerformable;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.List;


/**
 * This is the strategy interface for the {@link GenericMaintenanceJobPerformable}. The implementation of this interface
 * consists of two parts. The search of the items (see {@link #createFetchQuery(CronJobModel)}) and the removing of the
 * items (see {@link #process(List)}). The {@link GenericMaintenanceJobPerformable} is responsible for the paging of the
 * large result list, this means if the search contains a large list of items the list in {@link #process(List)}
 * contains only a small number of elements. (at the moment 100 elements, see
 * {@link GenericMaintenanceJobPerformable#setPageSize(int)})
 */
public interface MaintenanceCleanupStrategy<T extends ItemModel, C extends CronJobModel>
{
	/**
	 * Implement and return a {@link FlexibleSearchQuery} object here which searches for the items to be removed.
	 */
	FlexibleSearchQuery createFetchQuery(C cjm);

	/**
	 * Based on the given {@link FlexibleSearchQuery} of {@link #createFetchQuery(CronJobModel)} the job pages through
	 * the results and with each sublist (contains the concrete instances of the items) of the large result this method
	 * is called. Implement here the remove logic for each item element.
	 */
	void process(List<T> elements);
}
