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
import de.hybris.platform.catalog.daos.ItemSyncTimestampDao;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncCronJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.catalog.synchronization.strategy.SyncJobApplicableTypesStrategy;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


public class DefaultCatalogSynchronizationService implements CatalogSynchronizationService
{
	private static final Logger LOG = Logger.getLogger(DefaultCatalogSynchronizationService.class);

	private CronJobService cronJobService;
	private ModelService modelService;
	private ItemSyncTimestampDao itemSyncTimestampDao;
	private SyncJobApplicableTypesStrategy syncJobApplicableTypesStrategy;
	private CatalogTypeService catalogTypeService;
	private FlexibleSearchService flexibleSearchService;

	@Override
	public void synchronizeFully(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		final CatalogVersionSyncJobModel syncJob = createSyncJob(source, target);
		final CatalogVersionSyncCronJobModel syncCronJob = (CatalogVersionSyncCronJobModel) createSyncCronJob(syncJob);

		cronJobService.performCronJob(syncCronJob, true);
	}

	@Override
	public void synchronizeFully(final CatalogVersionModel source, final CatalogVersionModel target, final int numberOfThreads)
	{
		final CatalogVersionSyncJobModel syncJob = createSyncJob(source, target, numberOfThreads);
		final CatalogVersionSyncCronJobModel syncCronJob = (CatalogVersionSyncCronJobModel) createSyncCronJob(syncJob);

		cronJobService.performCronJob(syncCronJob, true);
	}

	@Override
	public void synchronizeFullyInBackground(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		final CatalogVersionSyncJobModel syncJob = createSyncJob(source, target);
		final CatalogVersionSyncCronJobModel syncCronJob = (CatalogVersionSyncCronJobModel) createSyncCronJob(syncJob);

		cronJobService.performCronJob(syncCronJob, false);
	}

	@Override
	public SyncResult synchronize(final SyncItemJobModel syncJob, final SyncConfig syncConfig)
	{
		final SyncItemCronJobModel syncCronJob = createSyncCronJob(syncJob);
		syncCronJob.setForceUpdate(syncConfig.getForceUpdate());
		syncCronJob.setCreateSavedValues(syncConfig.getCreateSavedValues());
		syncCronJob.setLogToDatabase(syncConfig.getLogToDatabase());
		syncCronJob.setLogToFile(syncConfig.getLogToFile());
		syncCronJob.setLogLevelDatabase(syncConfig.getLogLevelDatabase());
		syncCronJob.setLogLevelFile(syncConfig.getLogLevelFile());
		syncCronJob.setErrorMode(syncConfig.getErrorMode());
		syncCronJob.setFullSync(Boolean.valueOf(!syncConfig.hasPartialSyncSchedule()));
		syncCronJob.setAbortOnCollidingSync(Boolean.valueOf(syncConfig.getAbortWhenCollidingSyncIsRunning()));

		modelService.save(syncCronJob);

		if (Boolean.FALSE.equals(syncConfig.getFullSync()))
		{
			addScheduleFromConfig(syncCronJob, syncConfig);
		}

		modelService.refresh(syncCronJob);
		final SyncResult syncResult = new SyncResult(syncCronJob);

		cronJobService.performCronJob(syncCronJob, BooleanUtils.isNotFalse(syncConfig.getSynchronous()));

		return syncResult;
	}

	@Override
	public List<SyncResult> performSynchronization(final List<ItemModel> givenItems, final List<SyncItemJobModel> syncItemJobs,
			final SyncConfig syncConfig)
	{
		final List<SyncResult> ret = new ArrayList<>();

		for (final SyncItemJobModel syncJob : syncItemJobs)
		{
			final List<ItemModel> applicableItems = getApplicableItems(givenItems, syncJob);
			if (CollectionUtils.isProperSubCollection(applicableItems, givenItems))
			{
				LOG.warn(String.format("%s %s", "Found some items that aren't applicable for the given sync item job:",
						syncJob.getCode()));
			}
			if (CollectionUtils.isNotEmpty(applicableItems))
			{
				ret.add(synchronize(syncJob, scheduleItemsWithConfig(syncConfig, syncJob, applicableItems)));
			}
		}
		return ret;
	}

	protected SyncConfig scheduleItemsWithConfig(final SyncConfig syncConfig, final SyncItemJobModel syncJob,
			final List<ItemModel> applicableItems)
	{
		final SyncConfig myCfg = cloneSyncConfig(syncConfig);
		for (final ItemModel theItem : applicableItems)
		{
			// item is from source
			if( ObjectUtils.equals(syncJob.getSourceVersion(), catalogTypeService.getCatalogVersionForCatalogVersionAwareModel(theItem)))
			{
				myCfg.addItemToSync(theItem.getPk());
			}
			// item is from target
			else
			{
				// lookup existing sync timestamp
				final ItemSyncTimestampModel timestamp = getSynchronizationTargetTimestampFor(syncJob, theItem);
				// update via source item
				if (timestamp != null && timestamp.getSourceItem() != null)
				{
					myCfg.addItemToSync(timestamp.getSourceItem().getPk());
				}
				// delete
				else
				{
					myCfg.addItemToDelete(theItem.getPk());
				}
			}
		}
		return myCfg;
	}

	@Override
	public SyncItemJobModel getSyncJob(final CatalogVersionModel source, final CatalogVersionModel target, final String qualifier)
	{
		ServicesUtil.validateParameterNotNull(source, "source cannot be null");
		ServicesUtil.validateParameterNotNull(target, "target cannot be null");
		Preconditions.checkArgument(!source.equals(target), "source and target models should be different");

		final Map params = new HashMap();
		params.put("src", source);
		params.put("tgt", target);

		if (qualifier != null)
		{
			params.put("code", qualifier);
		}

		final List<SyncItemJobModel> jobs = flexibleSearchService.search(//
				"SELECT {" + SyncItemJobModel.PK + "} " //
						+ "FROM {" + SyncItemJobModel._TYPECODE + "} " //
						+ "WHERE {" + SyncItemJobModel.SOURCEVERSION + "}=?src AND " //
						+ "{" + SyncItemJobModel.TARGETVERSION + "}=?tgt " //
						+ (qualifier == null ? "" : " AND {" + SyncItemJobModel.CODE + "}=?code"), //
				params).getResult();

		return jobs.isEmpty() ? null : jobs.get(0);
	}

	@Override
	public SyncResult performSynchronization(final List<ItemModel> givenItems, final SyncItemJobModel syncItemJob,
			final SyncConfig syncConfig)
	{
		final List<SyncResult> results = performSynchronization(givenItems, Collections.singletonList(syncItemJob), syncConfig);
		return CollectionUtils.isNotEmpty(results) ? results.iterator().next() : null;
	}

	@Override
	public List<ItemModel> getApplicableItems(final List<ItemModel> inputItems, final SyncItemJobModel syncItemJob)
	{

		ServicesUtil.validateParameterNotNullStandardMessage("inputItems", inputItems);
		ServicesUtil.validateParameterNotNullStandardMessage("syncItemJob", syncItemJob);

		return inputItems.stream().filter(
				theCandidate -> syncJobApplicableTypesStrategy.checkIfApplicable(theCandidate, syncItemJob))
				.collect(Collectors.toList());

	}

	@Override
	public boolean isInProgress(final SyncItemJobModel theSyncJob)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("theSyncJob", theSyncJob);
		return theSyncJob.getExecutions().stream().anyMatch(each -> cronJobService.isRunning(each));
	}


	@Override
	public Collection<SyncResult> lookupCurrentSynchronizations(final SyncItemJobModel theSyncJob)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("theSyncJob", theSyncJob);
		return theSyncJob.getExecutions().stream().filter(each -> cronJobService.isRunning(each))
				.map(cronJob -> new SyncResult(cronJob)).collect(Collectors.toList());
	}


	@Override
	public ItemSyncTimestampModel getSynchronizationSourceTimestampFor(final SyncItemJobModel theSyncJob, final ItemModel source)
	{
		final List<ItemSyncTimestampModel> timestamps = itemSyncTimestampDao.findLastSourceSyncTimestamps(theSyncJob, source);
		return CollectionUtils.isNotEmpty(timestamps) ? timestamps.iterator().next() : null;
	}

	@Override
	public ItemSyncTimestampModel getSynchronizationTargetTimestampFor(final SyncItemJobModel theSyncJob, final ItemModel target)
	{
		final List<ItemSyncTimestampModel> timestamps = itemSyncTimestampDao.findLastTargetSyncTimestamps(theSyncJob, target);
		return CollectionUtils.isNotEmpty(timestamps) ? timestamps.iterator().next() : null;
	}

	@Override
	public ItemModel getSynchronizationSourceFor(final SyncItemJobModel theSyncJob, final ItemModel targetItem)
	{
		final ItemSyncTimestampModel timestamp = getSynchronizationTargetTimestampFor(theSyncJob, targetItem);
		return timestamp == null ? null : timestamp.getSourceItem();
	}

	@Override
	public ItemModel getSynchronizationTargetFor(final SyncItemJobModel theSyncJob, final ItemModel sourceItem)
	{
		final ItemSyncTimestampModel timestamp = getSynchronizationSourceTimestampFor(theSyncJob, sourceItem);
		return timestamp == null ? null : timestamp.getTargetItem();
	}

	protected void addScheduleFromConfig(final SyncItemCronJobModel cronJob, final SyncConfig config)
	{
		// XXX: Jalo
		final SyncItemCronJob jaloCronJob = modelService.getSource(cronJob);
		if (jaloCronJob instanceof CatalogVersionSyncCronJob)
		{
			((CatalogVersionSyncCronJob) jaloCronJob).addPendingItems(config.getPartialSyncSchedule());
		}
		else
		{
			for (final PK[] pair : config.getPartialSyncSchedule())
			{
				jaloCronJob.addPendingItem(pair[0], pair[1]);
			}
		}
		// XXX: jalo end
	}

	protected CatalogVersionSyncJobModel createSyncJob(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		return createSyncJob(source, target, getMaxThreads());
	}

	protected CatalogVersionSyncJobModel createSyncJob(final CatalogVersionModel source, final CatalogVersionModel target,
			final int numberOfThreads)
	{
		final CatalogVersionSyncJobModel job = modelService.create(CatalogVersionSyncJobModel.class);
		job.setCode(RandomStringUtils.randomAlphanumeric(10));
		job.setSourceVersion(source);
		job.setTargetVersion(target);
		job.setRemoveMissingItems(true);
		job.setCreateNewItems(true);
		job.setMaxThreads(numberOfThreads);

		modelService.save(job);

		return job;
	}

	protected SyncItemCronJobModel createSyncCronJob(final SyncItemJobModel job)
	{
		final SyncItemJob jobItem = modelService.getSource(job);
		final SyncItemCronJob cronJob = jobItem.newExecution();

		return modelService.get(cronJob.getPK());
	}

	protected SyncConfig cloneSyncConfig(final SyncConfig givenSyncConfig)
	{
		final SyncConfig syncConfig = new SyncConfig();
		syncConfig.setCreateSavedValues(givenSyncConfig.getCreateSavedValues());
		syncConfig.setForceUpdate(givenSyncConfig.getForceUpdate());
		syncConfig.setLogLevelDatabase(givenSyncConfig.getLogLevelDatabase());
		syncConfig.setLogLevelFile(givenSyncConfig.getLogLevelFile());
		syncConfig.setLogToFile(givenSyncConfig.getLogToFile());
		syncConfig.setLogToDatabase(givenSyncConfig.getLogToDatabase());
		syncConfig.setSynchronous(givenSyncConfig.getSynchronous());
		syncConfig.setAbortWhenCollidingSyncIsRunning(givenSyncConfig.getAbortWhenCollidingSyncIsRunning());
		return syncConfig;
	}

	protected Integer getMaxThreads()
	{
		return Integer.valueOf(CatalogVersionSyncJob.getDefaultMaxThreads(Registry.getCurrentTenantNoFallback()));
	}

	@Required
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setItemSyncTimestampDao(final ItemSyncTimestampDao itemSyncTimestampDao)
	{
		this.itemSyncTimestampDao = itemSyncTimestampDao;
	}

	@Required
	public void setSyncJobApplicableTypesStrategy(final SyncJobApplicableTypesStrategy syncJobApplicableTypesStrategy)
	{
		this.syncJobApplicableTypesStrategy = syncJobApplicableTypesStrategy;
	}

	@Required
	public void setCatalogTypeService(final CatalogTypeService catalogTypeService)
	{
		this.catalogTypeService = catalogTypeService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
