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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CatalogVersionSyncScheduleMedia first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionSyncScheduleMediaModel extends MediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CatalogVersionSyncScheduleMedia";
	
	/**<i>Generated relation code constant for relation <code>SyncJobScheduleMediaRelation</code> defining source attribute <code>cronjob</code> in extension <code>catalog</code>.</i>*/
	public static final String _SYNCJOBSCHEDULEMEDIARELATION = "SyncJobScheduleMediaRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncScheduleMedia.scheduledCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String SCHEDULEDCOUNT = "scheduledCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncScheduleMedia.cronjobPOS</code> attribute defined at extension <code>catalog</code>. */
	public static final String CRONJOBPOS = "cronjobPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncScheduleMedia.cronjob</code> attribute defined at extension <code>catalog</code>. */
	public static final String CRONJOB = "cronjob";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionSyncScheduleMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionSyncScheduleMediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _cronjob initial attribute declared by type <code>CatalogVersionSyncScheduleMedia</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionSyncScheduleMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final CatalogVersionSyncCronJobModel _cronjob)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setCronjob(_cronjob);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _cronjob initial attribute declared by type <code>CatalogVersionSyncScheduleMedia</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CatalogVersionSyncScheduleMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final CatalogVersionSyncCronJobModel _cronjob, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setCronjob(_cronjob);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncScheduleMedia.cronjob</code> attribute defined at extension <code>catalog</code>. 
	 * @return the cronjob
	 */
	@Accessor(qualifier = "cronjob", type = Accessor.Type.GETTER)
	public CatalogVersionSyncCronJobModel getCronjob()
	{
		return getPersistenceContext().getPropertyValue(CRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncScheduleMedia.scheduledCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the scheduledCount
	 */
	@Accessor(qualifier = "scheduledCount", type = Accessor.Type.GETTER)
	public Integer getScheduledCount()
	{
		return getPersistenceContext().getPropertyValue(SCHEDULEDCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersionSyncScheduleMedia.cronjob</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the cronjob
	 */
	@Accessor(qualifier = "cronjob", type = Accessor.Type.SETTER)
	public void setCronjob(final CatalogVersionSyncCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncScheduleMedia.scheduledCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the scheduledCount
	 */
	@Accessor(qualifier = "scheduledCount", type = Accessor.Type.SETTER)
	public void setScheduledCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(SCHEDULEDCOUNT, value);
	}
	
}
