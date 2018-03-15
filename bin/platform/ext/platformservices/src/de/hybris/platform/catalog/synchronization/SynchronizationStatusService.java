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


import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;


/**
 * A {@link SynchronizationStatusService} offers useful operations for perform more fine grained synchronization operations.
 * <br>
 * <br>
 *
 * Note:
 * Below operations get current synchronization status for given items from a different sides i.e for convenience
 * one can get synchronization status for an items that resides in the source as well in the target catalog version -
 * just to get the status from the target catalog version side please use status methods that contains <b>Pull</b> in
 * their names, other status methods computes the status from the source catalog version side.
 *
 * In addition to that what was stated above we can schedule the synchronization for a bunch of items.
 *
 */
public interface SynchronizationStatusService
{
	/**
	 * Gets the {@link SyncItemStatus} from the all sources of the synchronization side defined by
	 * <code>syncItemJobs</code> for a given item.
	 *
	 * @param theItem given item
	 * @param syncItemJobs all synchronization jobs
	 * @return {@link SyncItemStatus}
	 */
	List<SyncItemInfo> getSyncInfo(final ItemModel theItem, List<SyncItemJobModel> syncItemJobs);

	/**
	 * Gets the {@link SyncItemStatus} from the source of the synchronization side defined by <code>syncItemJob</code>
	 * for a given item.
	 *
	 * @param theItem given item
	 * @param syncItemJob synchronization jobs
	 * @return {@link SyncItemStatus}
	 */
	SyncItemInfo getSyncInfo(final ItemModel theItem, final SyncItemJobModel syncItemJob);

	/**
	 * Gets the list of {@link SyncItemStatus} from the source of the synchronization side defined by
	 * <code>syncItemJob</code> for a given items <code>givenItems</code>.
	 *
	 * @param givenItems list of given item
	 * @param syncItemJob synchronization jobs
	 * @return {@link SyncItemStatus}
	 */
	List<SyncItemInfo> getSyncInfo(final List<ItemModel> givenItems, SyncItemJobModel syncItemJob);

	/**
	 * Returns all defined inbound synchronization sync jobs for a given item.
	 */
	List<SyncItemJobModel> getInboundSynchronizations(final ItemModel theItem);

	/**
	 * Returns all defined outbound synchronization sync jobs for a given item.
	 */
	List<SyncItemJobModel> getOutboundSynchronizations(final ItemModel theItem);

	/**
	 * Convenient method that check whether given items are in one particular {@link SyncItemStatus} for all the sources
	 * of the synchronization side defined by <code>syncItemJobs</code>
	 *
	 * @param givenItems list of given item
	 * @param syncItemJobs synchronization jobs
	 * @param syncStatus {@link SyncItemStatus}
	 * @return true when all items match given status false otherwise
	 */
	boolean matchesSyncStatus(final List<ItemModel> givenItems, final List<SyncItemJobModel> syncItemJobs,
			final SyncItemStatus syncStatus);
}
