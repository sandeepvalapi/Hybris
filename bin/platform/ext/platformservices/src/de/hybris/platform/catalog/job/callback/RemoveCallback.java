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
package de.hybris.platform.catalog.job.callback;

import de.hybris.platform.catalog.model.RemoveCatalogVersionCronJobModel;
import de.hybris.platform.servicelayer.impex.ImportResult;


/**
 * Callback for handling 'events' during remove import cron job work.
 * 
 * @since 4.3
 * @spring.bean removeJobCallback
 */
public interface RemoveCallback<P>
{
	/**
	 * Hook for the before handling.
	 */
	void beforeRemove(RemoveCatalogVersionCronJobModel cronJobModel, P parentType);

	/**
	 * Hook for the period polling current status of the import.
	 */
	void doRemove(RemoveCatalogVersionCronJobModel cronJobModel, P parentType, ImportResult result);

	/**
	 * Hook for the after import handling.
	 */
	void afterRemoved(RemoveCatalogVersionCronJobModel cronJobModel, P parentType, ImportResult result);
}
