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

import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Configuration container for starting a new catalog synchronization. Allows to override several settings of the
 * underlying {@link SyncItemCronJobModel} and {@link CatalogVersionSyncCronJobModel}.
 * 
 * In addition it allows to schedule individual items to be synchronized and make the sync process running in partial
 * mode. If no items are specified always a full sync is performed.
 */
public class SyncConfig
{
	private Boolean logToFile;
	private Boolean logToDatabase;
	private Boolean forceUpdate;
	private Boolean keepCronJob;
	private JobLogLevel logLevelFile;
	private JobLogLevel logLevelDatabase;
	private Boolean createSavedValues;
	private ErrorMode errorMode;
	private Boolean synchronous;
	private final List<PK[]> syncSchedule = new ArrayList<>();
	private boolean abortWhenCollidingSyncIsRunning = false;

	public boolean getAbortWhenCollidingSyncIsRunning()
	{
		return abortWhenCollidingSyncIsRunning;
	}

	public void setAbortWhenCollidingSyncIsRunning(final boolean abortWhenCollidingSyncIsRunning)
	{
		this.abortWhenCollidingSyncIsRunning = abortWhenCollidingSyncIsRunning;
	}

	public Boolean getLogToFile()
	{
		return logToFile;
	}

	public void setLogToFile(final Boolean logToFile)
	{
		this.logToFile = logToFile;
	}

	public Boolean getLogToDatabase()
	{
		return logToDatabase;
	}

	public void setLogToDatabase(final Boolean logToDatabase)
	{
		this.logToDatabase = logToDatabase;
	}

	public Boolean getForceUpdate()
	{
		return forceUpdate;
	}

	public void setForceUpdate(final Boolean forceUpdate)
	{
		this.forceUpdate = forceUpdate;
	}

	public Boolean getKeepCronJob()
	{
		return keepCronJob;
	}

	public void setKeepCronJob(final Boolean keepCronJob)
	{
		this.keepCronJob = keepCronJob;
	}

	public JobLogLevel getLogLevelFile()
	{
		return logLevelFile;
	}

	public void setLogLevelFile(final JobLogLevel logLevelFile)
	{
		this.logLevelFile = logLevelFile;
	}

	public JobLogLevel getLogLevelDatabase()
	{
		return logLevelDatabase;
	}

	public void setLogLevelDatabase(final JobLogLevel logLevelDatabase)
	{
		this.logLevelDatabase = logLevelDatabase;
	}

	public Boolean getCreateSavedValues()
	{
		return createSavedValues;
	}

	public void setCreateSavedValues(final Boolean createSavedValues)
	{
		this.createSavedValues = createSavedValues;
	}

	public ErrorMode getErrorMode()
	{
		return errorMode;
	}

	public void setErrorMode(final ErrorMode errorMode)
	{
		this.errorMode = errorMode;
	}

	public Boolean getSynchronous()
	{
		return synchronous;
	}

	public void setSynchronous(final Boolean synchronous)
	{
		this.synchronous = synchronous;
	}

	/**
	 * @deprecated since 6.3.0 - use{@link #hasPartialSyncSchedule()} instead to determine whether a partial or full sync is to be
	 *             executed
	 */
	@Deprecated
	public Boolean getFullSync()
	{
		return Boolean.valueOf(!hasPartialSyncSchedule());
	}

	/**
	 * Tells whether items have been added to the configuration and therefore the sync process is running in partial sync
	 * mode.
	 */
	public boolean hasPartialSyncSchedule()
	{
		return CollectionUtils.isNotEmpty(syncSchedule);
	}

	/**
	 * @deprecated since 6.3.0 - not used any more
	 */
	@Deprecated
	public void setFullSync(final Boolean fullSync)
	{
		//
	}
	
	/**
	 * Adds a specific item from the source catalog to be copied or updated. Even if no target item is specified 
	 * here the process will detect the potentially existing copy. 
	 * 
	 *   @see #addItemToSync(PK, PK)
	 */
	public void addItemToSync( PK originalItemPK )
	{
		addItemToSync(originalItemPK, null);
	}
	
	/**
	 * Adds a specific item from the source catalog to be copied or updated. This method allows to specify the existing
	 * copy of the item as well. Note that if the copy is omitted the sync process will perform a lookup for the copy any
	 * way.
	 * 
	 * @see #addItemToSync(PK)
	 */
	public void addItemToSync(final PK originalItemPK, final PK copyItemPK)
	{
		this.syncSchedule.add(new PK[]{ originalItemPK, copyItemPK });
	}
	
	/**
	 * Adds a specific item from the <b>target catalog version</b> to be removed within the sync process. Use this to schedule
	 * removal of items which no longer exists in the source catalog version. However, be aware of the sync <b>not checking</b>
	 * whether there's really no original left and just deletes the scheduled item! 
	 */
	public void addItemToDelete( final PK copyItemPK )
	{
		this.syncSchedule.add(new PK[]{null,copyItemPK});
	}
	
	/**
	 * Returns the partial sync schedule or an empty list if a full sync is intended.
	 * @see #hasPartialSyncSchedule()
	 */
	public List<PK[]> getPartialSyncSchedule()
	{
		return Collections.unmodifiableList(this.syncSchedule);
	}
}
