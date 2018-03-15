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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type CatalogVersionSyncCronJobHistory first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionSyncCronJobHistoryModel extends CronJobHistoryModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CatalogVersionSyncCronJobHistory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncCronJobHistory.processedItemsCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String PROCESSEDITEMSCOUNT = "processedItemsCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncCronJobHistory.scheduledItemsCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String SCHEDULEDITEMSCOUNT = "scheduledItemsCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncCronJobHistory.dumpedItemsCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String DUMPEDITEMSCOUNT = "dumpedItemsCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncCronJobHistory.fullSync</code> attribute defined at extension <code>catalog</code>. */
	public static final String FULLSYNC = "fullSync";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionSyncCronJobHistoryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionSyncCronJobHistoryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJobCode initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _jobCode initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _startTime initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CatalogVersionSyncCronJobHistoryModel(final String _cronJobCode, final String _jobCode, final int _nodeID, final Date _startTime)
	{
		super();
		setCronJobCode(_cronJobCode);
		setJobCode(_jobCode);
		setNodeID(_nodeID);
		setStartTime(_startTime);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJobCode initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _jobCode initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _startTime initial attribute declared by type <code>CronJobHistory</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CatalogVersionSyncCronJobHistoryModel(final String _cronJobCode, final String _jobCode, final int _nodeID, final ItemModel _owner, final Date _startTime)
	{
		super();
		setCronJobCode(_cronJobCode);
		setJobCode(_jobCode);
		setNodeID(_nodeID);
		setOwner(_owner);
		setStartTime(_startTime);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncCronJobHistory.dumpedItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the dumpedItemsCount
	 */
	@Accessor(qualifier = "dumpedItemsCount", type = Accessor.Type.GETTER)
	public Integer getDumpedItemsCount()
	{
		return getPersistenceContext().getPropertyValue(DUMPEDITEMSCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncCronJobHistory.fullSync</code> attribute defined at extension <code>catalog</code>. 
	 * @return the fullSync
	 */
	@Accessor(qualifier = "fullSync", type = Accessor.Type.GETTER)
	public Boolean getFullSync()
	{
		return getPersistenceContext().getPropertyValue(FULLSYNC);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncCronJobHistory.processedItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the processedItemsCount
	 */
	@Accessor(qualifier = "processedItemsCount", type = Accessor.Type.GETTER)
	public Integer getProcessedItemsCount()
	{
		return getPersistenceContext().getPropertyValue(PROCESSEDITEMSCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncCronJobHistory.scheduledItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the scheduledItemsCount
	 */
	@Accessor(qualifier = "scheduledItemsCount", type = Accessor.Type.GETTER)
	public Integer getScheduledItemsCount()
	{
		return getPersistenceContext().getPropertyValue(SCHEDULEDITEMSCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncCronJobHistory.dumpedItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dumpedItemsCount
	 */
	@Accessor(qualifier = "dumpedItemsCount", type = Accessor.Type.SETTER)
	public void setDumpedItemsCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(DUMPEDITEMSCOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncCronJobHistory.fullSync</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the fullSync
	 */
	@Accessor(qualifier = "fullSync", type = Accessor.Type.SETTER)
	public void setFullSync(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(FULLSYNC, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncCronJobHistory.processedItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the processedItemsCount
	 */
	@Accessor(qualifier = "processedItemsCount", type = Accessor.Type.SETTER)
	public void setProcessedItemsCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PROCESSEDITEMSCOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncCronJobHistory.scheduledItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the scheduledItemsCount
	 */
	@Accessor(qualifier = "scheduledItemsCount", type = Accessor.Type.SETTER)
	public void setScheduledItemsCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(SCHEDULEDITEMSCOUNT, value);
	}
	
}
