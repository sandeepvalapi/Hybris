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

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;
import java.util.List;


public interface CatalogSynchronizationService
{
	/**
	 * For the given catalog version pair a new {@link CatalogVersionSyncJobModel} is being created configured to sync
	 * all default catalog item types.
	 * The actual sync process is backed by a also new {@link CatalogVersionSyncCronJobModel} which is performed
	 * synchronously.
	 */
	void synchronizeFully(CatalogVersionModel source, CatalogVersionModel target);

	/**
	 * For the given catalog version pair a new {@link CatalogVersionSyncJobModel} is being created configured to sync
	 * all default catalog item types.
	 * The actual sync process is backed by a also new {@link CatalogVersionSyncCronJobModel} which is started
	 * synchronously.
	 *
	 * @param numberOfThreads specifies the number of sync workers to be used - overrides the number configured in
	 *           <code>catalog.sync.workers</code>
	 */
	void synchronizeFully(final CatalogVersionModel source, final CatalogVersionModel target, int numberOfThreads);

	/**
	 * For the given catalog version pair a new {@link CatalogVersionSyncJobModel} is being created configured to sync
	 * all default catalog item types.
	 * The actual sync process is backed by a also new {@link CatalogVersionSyncCronJobModel} which is started
	 * asynchronously.
	 */
	void synchronizeFullyInBackground(CatalogVersionModel source, CatalogVersionModel target);

	/**
	 * Starts synchronization for an existing {@link SyncItemJobModel}. The configuration parameter allows to
	 * configure/override a number of synchronization job settings.
	 *
	 * The returned {@link SyncResult} gives both access to the execution result or, in case the process was started
	 * asynchronously, to observe the running process
	 */
	SyncResult synchronize(SyncItemJobModel syncJob, SyncConfig syncConfig);

	/**
	 * Performs the synchronization for given items against given sync jobs.
	 *
	 * Note that for each job the list of items is being filtered to schedule only the ones which are actually applicable
	 * (being compatible with the job's root type collection).
	 *
	 * For influencing the sync behavior a sync config object can be passed, e.g. for controlling the level of
	 * multi-threading.
	 */
	List<SyncResult> performSynchronization(final List<ItemModel> givenItems, final List<SyncItemJobModel> syncItemJobs,
			final SyncConfig syncConfig);

	/**
	 * Performs the synchronization for given items.
	 *
	 * Note that the list of items is being filtered to schedule only the ones which are actually applicable (being
	 * compatible with the job's root type collection).
	 *
	 * For influencing the sync behavior a sync config object can be passed, e.g. for controlling the level of
	 * multi-threading.
	 */
	SyncResult performSynchronization(final List<ItemModel> givenItems, final SyncItemJobModel syncItemJob,
			final SyncConfig syncConfig);

	/**
	 * Filters given items returning only the ones being applicable to the given sync job. Being applicable means whether
	 * or not an item is compatible to the job's root type collection and whether it belongs to the specified catalog
	 * version.
	 */
	List<ItemModel> getApplicableItems(final List<ItemModel> givenItems, final SyncItemJobModel syncItemJob);

	/**
	 * Returns the existing {@link ItemSyncTimestampModel} for the <code>source</code> item in the scope of the given
	 * sync job. In case of the item never been synchronized before NULL is returned.
	 */
	ItemSyncTimestampModel getSynchronizationSourceTimestampFor(final SyncItemJobModel theSyncJob, final ItemModel source);

	/**
	 * Returns the existing {@link ItemSyncTimestampModel} for the target item in the scope of the given sync job. In
	 * case of the original source item being deleted this methods will return NULL.
	 */
	ItemSyncTimestampModel getSynchronizationTargetTimestampFor(final SyncItemJobModel theSyncJob, final ItemModel target);

	/**
	 * Returns the counterpart for the <code>target</code> item against given sync job <code>theSyncJob</code>.
	 */
	<T extends ItemModel> T getSynchronizationSourceFor(final SyncItemJobModel theSyncJob, final ItemModel target);

	/**
	 * Returns the counterpart for the <code>source</code> item against given sync job <code>theSyncJob</code>.
	 */
	<T extends ItemModel> T getSynchronizationTargetFor(final SyncItemJobModel theSyncJob, final ItemModel source);

	/**
	 * Checks whether the given <code>givenSyncJob</code> is currently in progress.
	 */
	boolean isInProgress(final SyncItemJobModel theSyncJob);

	/**
	 * Returns the current executions for the given <code>theSyncJob</code>.
	 */
	Collection<SyncResult> lookupCurrentSynchronizations(final SyncItemJobModel theSyncJob);

	/**
	 * Returns the publication rule (as {@link SyncItemJobModel} item) between a specified source catalog version and target
	 * catalog version.
	 */
	SyncItemJobModel getSyncJob(final CatalogVersionModel source, final CatalogVersionModel target, final String qualifier);
}
