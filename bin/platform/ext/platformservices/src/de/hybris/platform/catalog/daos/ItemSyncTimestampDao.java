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
package de.hybris.platform.catalog.daos;

import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;


/**
 * {@link ItemSyncTimestampModel} oriented Data Access Object.
 */
public interface ItemSyncTimestampDao
{
	/**
	 * Returns a limited list of sync timestamps for a specified
	 * <code>item<code>.<br>The <code>item<code> may be both: {@link ItemSyncTimestampModel#SOURCEITEM} or {@link ItemSyncTimestampModel#TARGETITEM}.
	 * 
	 * @param item
	 *           {@link ItemModel} for which we are searching sync timestamps
	 * @param limit
	 *           limit count of result (-1 : no limit)
	 */
	List<ItemSyncTimestampModel> findSyncTimestampsByItem(ItemModel item, int limit);

	/**
	 * Returns a limited list of sync timestamps for a specified
	 * <code>catalogVersion<code>.<br> The <code>catalogVersion<code> may be both {@link ItemSyncTimestampModel#SOURCEVERSION} or {@link ItemSyncTimestampModel#TARGETVERSION}.
	 * 
	 * @param catalogVersion
	 *           {@link CatalogVersionModel} for which we are searching sync timestamps
	 * @param limit
	 *           limit count of result (-1 : no limit)
	 */
	List<ItemSyncTimestampModel> findSyncTimestampsByCatalogVersion(CatalogVersionModel catalogVersion, int limit);

	/**
	 * Returns list of {@link ItemSyncTimestampModel} for a given source item
	 * <code>syncJob<code>.<br><code>source</code> and <code>target</code>
	 *
	 * @param syncJob {@link ItemModel} for which syncJob are searching sync timestamp
	 * @param source  {@link ItemModel} source item
	 */
	List<ItemSyncTimestampModel> findLastSourceSyncTimestamps(final SyncItemJobModel syncJob, final ItemModel source);

	/**
	 * Returns list of  {@link ItemSyncTimestampModel} for a given target item
	 * <code>syncJob<code>.<br><code>source</code> and <code>target</code>
	 *
	 * @param syncJob {@link ItemModel} for which syncJob are searching sync timestamp
	 * @param source  {@link ItemModel} source item
	 */
	List<ItemSyncTimestampModel> findLastTargetSyncTimestamps(final SyncItemJobModel syncJob, final ItemModel source);



}
