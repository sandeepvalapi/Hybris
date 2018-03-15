/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.catalog.model.synchronization;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobHistoryModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncScheduleMediaModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type CatalogVersionSyncCronJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionSyncCronJobModel extends SyncItemCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CatalogVersionSyncCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncCronJob.statusMessage</code> attribute defined at extension <code>catalog</code>. */
	public static final String STATUSMESSAGE = "statusMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncCronJob.scheduleMedias</code> attribute defined at extension <code>catalog</code>. */
	public static final String SCHEDULEMEDIAS = "scheduleMedias";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionSyncCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionSyncCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>SyncItemCronJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionSyncCronJobModel(final SyncItemJobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>SyncItemCronJob</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CatalogVersionSyncCronJobModel(final SyncItemJobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.activeCronJobHistory</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>catalog</code>. 
	 * @return the activeCronJobHistory
	 */
	@Override
	@Accessor(qualifier = "activeCronJobHistory", type = Accessor.Type.GETTER)
	public CatalogVersionSyncCronJobHistoryModel getActiveCronJobHistory()
	{
		return (CatalogVersionSyncCronJobHistoryModel) super.getActiveCronJobHistory();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncCronJob.scheduleMedias</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the scheduleMedias
	 */
	@Accessor(qualifier = "scheduleMedias", type = Accessor.Type.GETTER)
	public List<CatalogVersionSyncScheduleMediaModel> getScheduleMedias()
	{
		return getPersistenceContext().getPropertyValue(SCHEDULEMEDIAS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncCronJob.statusMessage</code> attribute defined at extension <code>catalog</code>. 
	 * @return the statusMessage
	 */
	@Accessor(qualifier = "statusMessage", type = Accessor.Type.GETTER)
	public String getStatusMessage()
	{
		return getPersistenceContext().getPropertyValue(STATUSMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CronJob.activeCronJobHistory</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>catalog</code>. Will only accept values of type {@link de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobHistoryModel}. 
	 *  
	 * @param value the activeCronJobHistory
	 */
	@Override
	@Accessor(qualifier = "activeCronJobHistory", type = Accessor.Type.SETTER)
	public void setActiveCronJobHistory(final CronJobHistoryModel value)
	{
		if( value == null || value instanceof CatalogVersionSyncCronJobHistoryModel)
		{
			super.setActiveCronJobHistory(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobHistoryModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncCronJob.scheduleMedias</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the scheduleMedias
	 */
	@Accessor(qualifier = "scheduleMedias", type = Accessor.Type.SETTER)
	public void setScheduleMedias(final List<CatalogVersionSyncScheduleMediaModel> value)
	{
		getPersistenceContext().setPropertyValue(SCHEDULEMEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncCronJob.statusMessage</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the statusMessage
	 */
	@Accessor(qualifier = "statusMessage", type = Accessor.Type.SETTER)
	public void setStatusMessage(final String value)
	{
		getPersistenceContext().setPropertyValue(STATUSMESSAGE, value);
	}
	
}
