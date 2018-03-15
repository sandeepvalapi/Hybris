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

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.strategy.SyncJobApplicableTypesStrategy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Required;


public class DefaultSynchronizationStatusService implements SynchronizationStatusService
{
	private ModelService modelService;
	private CatalogTypeService catalogTypeService;
	private CatalogSynchronizationService catalogSynchronizationService;
	private SyncJobApplicableTypesStrategy syncJobApplicableTypesStrategy;

	@Override
	public List<SyncItemInfo> getSyncInfo(final ItemModel item, final List<SyncItemJobModel> syncItemJobs)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("item", item);
		ServicesUtil.validateParameterNotNullStandardMessage("syncItemJobModels", syncItemJobs);

		return syncItemJobs.stream().map(theJob -> getSyncInfo(item, theJob)).collect(Collectors.toList());

	}

	@Override
	public SyncItemInfo getSyncInfo(final ItemModel item, final SyncItemJobModel syncItemJob)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("item", item);
		ServicesUtil.validateParameterNotNullStandardMessage("syncItemJob", syncItemJob);

		final SyncItemStatus syncStatus;
		final boolean isRemoved = modelService.isRemoved(item);
		Boolean fromSourceFlag = null;
		ItemSyncTimestampModel timestamp = null;

		if ( syncJobApplicableTypesStrategy.checkIfApplicable(item, syncItemJob) )
		{
			if (isRemoved)
			{
				syncStatus = SyncItemStatus.ITEM_MISSING;
			}
			else
			{
				final boolean fromSource = ObjectUtils.equals(syncItemJob.getSourceVersion(), getCatalogVersionForItem(item));
				fromSourceFlag = Boolean.valueOf(fromSource);
				final ItemModel sourceItem;
				final ItemModel targetItem;
				if( fromSource )
				{
   				timestamp = catalogSynchronizationService.getSynchronizationSourceTimestampFor(syncItemJob,item);
					sourceItem = item;
   				targetItem = timestamp == null ? null : timestamp.getTargetItem();
				}
				else
				{
					timestamp = catalogSynchronizationService.getSynchronizationTargetTimestampFor(syncItemJob, item);
					targetItem = item;
					sourceItem = timestamp == null ? null : timestamp.getSourceItem();
				}
   			if (sourceItem == null || targetItem == null)
   			{
   				syncStatus = SyncItemStatus.COUNTERPART_MISSING;
   			}
   			else if (timestamp.getLastSyncSourceModifiedTime().getTime() < sourceItem.getModifiedtime().getTime())
   			{
   				syncStatus = SyncItemStatus.NOT_SYNC;
   			}
   			else
   			{
   				syncStatus = SyncItemStatus.IN_SYNC;
   			}
			}
		}
		else
		{
			syncStatus = SyncItemStatus.NOT_APPLICABLE;
		}
		
		final SyncItemInfo syncItemInfo = new SyncItemInfo();
		syncItemInfo.setSyncStatus(syncStatus);
		syncItemInfo.setItemPk(item.getPk());
		syncItemInfo.setSyncJobPk(syncItemJob.getPk());
		syncItemInfo.setFromSource(fromSourceFlag);
		syncItemInfo.setSyncTimestampPk(timestamp == null ? null : timestamp.getPk());
		return syncItemInfo;
	}

	@Override
	public List<SyncItemJobModel> getOutboundSynchronizations(final ItemModel item)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("item", item);

		if (!modelService.isRemoved(item) && catalogTypeService.isCatalogVersionAwareModel(item) )
		{
   		final CatalogVersionModel currentCatalogVersion = getCatalogVersionForItem(item);
   		if (currentCatalogVersion != null)
   		{
   			return Collections.unmodifiableList(currentCatalogVersion.getSynchronizations());
   		}
		}
		return Collections.emptyList();
	}

	@Override
	public List<SyncItemJobModel> getInboundSynchronizations(final ItemModel item)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("item", item);

		if( !modelService.isRemoved(item) && catalogTypeService.isCatalogVersionAwareModel(item) )
		{
   		final CatalogVersionModel currentCatalogVersion = getCatalogVersionForItem(item);
   		if (currentCatalogVersion != null)
   		{
   			return Collections.unmodifiableList(currentCatalogVersion.getIncomingSynchronizations());
   		}
		}
		return Collections.emptyList();
	}


	@Override
	public boolean matchesSyncStatus(final List<ItemModel> givenItems, final List<SyncItemJobModel> syncItemJobs,
			final SyncItemStatus syncStatus)
	{

		ServicesUtil.validateParameterNotNullStandardMessage("givenItems", givenItems);
		ServicesUtil.validateParameterNotNullStandardMessage("syncItemJobs", syncItemJobs);
		ServicesUtil.validateParameterNotNullStandardMessage("syncStatus", syncStatus);


		for (final ItemModel theItem : givenItems)
		{
			for (final SyncItemJobModel theJob : syncItemJobs)
			{
				final SyncItemInfo syncItemInfo = getSyncInfo(theItem, theJob);
				if (syncItemInfo != null && !syncStatus.equals(syncItemInfo.getSyncStatus()))
				{
					return false;

				}
			}
		}
		return true;
	}

	@Override
	public List<SyncItemInfo> getSyncInfo(final List<ItemModel> givenItems, final SyncItemJobModel syncItemJob)
	{

		ServicesUtil.validateParameterNotNullStandardMessage("givenItems", givenItems);
		ServicesUtil.validateParameterNotNullStandardMessage("syncItemJob", syncItemJob);

		return givenItems.stream().map(theItem -> getSyncInfo(theItem, syncItemJob)).collect(Collectors.toList());
	}

	protected CatalogVersionModel getCatalogVersionForItem(final ItemModel item)
	{
		CatalogVersionModel ret = null;
		if (!modelService.isRemoved(item))
		{
			ret = catalogTypeService.getCatalogVersionForCatalogVersionAwareModel(item);
		}
		return ret;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setCatalogTypeService(CatalogTypeService catalogTypeService)
	{
		this.catalogTypeService = catalogTypeService;
	}

	@Required
	public void setCatalogSynchronizationService(CatalogSynchronizationService catalogSynchronizationService)
	{
		this.catalogSynchronizationService = catalogSynchronizationService;
	}

	@Required
	public void setSyncJobApplicableTypesStrategy(SyncJobApplicableTypesStrategy syncJobApplicableTypesStrategy)
	{
		this.syncJobApplicableTypesStrategy = syncJobApplicableTypesStrategy;
	}
}
