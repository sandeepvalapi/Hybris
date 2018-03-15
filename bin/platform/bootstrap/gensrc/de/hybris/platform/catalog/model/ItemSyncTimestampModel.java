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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

/**
 * Generated model class for type ItemSyncTimestamp first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ItemSyncTimestampModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ItemSyncTimestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.syncJob</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCJOB = "syncJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.sourceItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEITEM = "sourceItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.targetItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETITEM = "targetItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEVERSION = "sourceVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETVERSION = "targetVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.lastSyncSourceModifiedTime</code> attribute defined at extension <code>catalog</code>. */
	public static final String LASTSYNCSOURCEMODIFIEDTIME = "lastSyncSourceModifiedTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.lastSyncTime</code> attribute defined at extension <code>catalog</code>. */
	public static final String LASTSYNCTIME = "lastSyncTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.pendingAttributesOwnerJob</code> attribute defined at extension <code>catalog</code>. */
	public static final String PENDINGATTRIBUTESOWNERJOB = "pendingAttributesOwnerJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.pendingAttributesScheduledTurn</code> attribute defined at extension <code>catalog</code>. */
	public static final String PENDINGATTRIBUTESSCHEDULEDTURN = "pendingAttributesScheduledTurn";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.pendingAttributeQualifiers</code> attribute defined at extension <code>catalog</code>. */
	public static final String PENDINGATTRIBUTEQUALIFIERS = "pendingAttributeQualifiers";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.outdated</code> attribute defined at extension <code>catalog</code>. */
	public static final String OUTDATED = "outdated";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.pendingAttributes</code> attribute defined at extension <code>catalog</code>. */
	public static final String PENDINGATTRIBUTES = "pendingAttributes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ItemSyncTimestampModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ItemSyncTimestampModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _sourceItem initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _targetItem initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ItemSyncTimestampModel(final ItemModel _sourceItem, final CatalogVersionModel _sourceVersion, final ItemModel _targetItem, final CatalogVersionModel _targetVersion)
	{
		super();
		setSourceItem(_sourceItem);
		setSourceVersion(_sourceVersion);
		setTargetItem(_targetItem);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceItem initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _syncJob initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _targetItem initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ItemSyncTimestampModel(final ItemModel _owner, final ItemModel _sourceItem, final CatalogVersionModel _sourceVersion, final SyncItemJobModel _syncJob, final ItemModel _targetItem, final CatalogVersionModel _targetVersion)
	{
		super();
		setOwner(_owner);
		setSourceItem(_sourceItem);
		setSourceVersion(_sourceVersion);
		setSyncJob(_syncJob);
		setTargetItem(_targetItem);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.lastSyncSourceModifiedTime</code> attribute defined at extension <code>catalog</code>. 
	 * @return the lastSyncSourceModifiedTime
	 */
	@Accessor(qualifier = "lastSyncSourceModifiedTime", type = Accessor.Type.GETTER)
	public Date getLastSyncSourceModifiedTime()
	{
		return getPersistenceContext().getPropertyValue(LASTSYNCSOURCEMODIFIEDTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.lastSyncTime</code> attribute defined at extension <code>catalog</code>. 
	 * @return the lastSyncTime
	 */
	@Accessor(qualifier = "lastSyncTime", type = Accessor.Type.GETTER)
	public Date getLastSyncTime()
	{
		return getPersistenceContext().getPropertyValue(LASTSYNCTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.outdated</code> attribute defined at extension <code>catalog</code>. 
	 * @return the outdated
	 */
	@Accessor(qualifier = "outdated", type = Accessor.Type.GETTER)
	public Boolean getOutdated()
	{
		return getPersistenceContext().getPropertyValue(OUTDATED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.pendingAttributeQualifiers</code> attribute defined at extension <code>catalog</code>. 
	 * @return the pendingAttributeQualifiers
	 */
	@Accessor(qualifier = "pendingAttributeQualifiers", type = Accessor.Type.GETTER)
	public String getPendingAttributeQualifiers()
	{
		return getPersistenceContext().getPropertyValue(PENDINGATTRIBUTEQUALIFIERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.pendingAttributes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the pendingAttributes
	 */
	@Accessor(qualifier = "pendingAttributes", type = Accessor.Type.GETTER)
	public Collection<AttributeDescriptorModel> getPendingAttributes()
	{
		return getPersistenceContext().getPropertyValue(PENDINGATTRIBUTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.pendingAttributesOwnerJob</code> attribute defined at extension <code>catalog</code>. 
	 * @return the pendingAttributesOwnerJob
	 */
	@Accessor(qualifier = "pendingAttributesOwnerJob", type = Accessor.Type.GETTER)
	public SyncItemCronJobModel getPendingAttributesOwnerJob()
	{
		return getPersistenceContext().getPropertyValue(PENDINGATTRIBUTESOWNERJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.pendingAttributesScheduledTurn</code> attribute defined at extension <code>catalog</code>. 
	 * @return the pendingAttributesScheduledTurn
	 */
	@Accessor(qualifier = "pendingAttributesScheduledTurn", type = Accessor.Type.GETTER)
	public Integer getPendingAttributesScheduledTurn()
	{
		return getPersistenceContext().getPropertyValue(PENDINGATTRIBUTESSCHEDULEDTURN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.sourceItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
	public ItemModel getSourceItem()
	{
		return getPersistenceContext().getPropertyValue(SOURCEITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getSourceVersion()
	{
		return getPersistenceContext().getPropertyValue(SOURCEVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.syncJob</code> attribute defined at extension <code>catalog</code>. 
	 * @return the syncJob
	 */
	@Accessor(qualifier = "syncJob", type = Accessor.Type.GETTER)
	public SyncItemJobModel getSyncJob()
	{
		return getPersistenceContext().getPropertyValue(SYNCJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.targetItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetItem
	 */
	@Accessor(qualifier = "targetItem", type = Accessor.Type.GETTER)
	public ItemModel getTargetItem()
	{
		return getPersistenceContext().getPropertyValue(TARGETITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getTargetVersion()
	{
		return getPersistenceContext().getPropertyValue(TARGETVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.lastSyncSourceModifiedTime</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the lastSyncSourceModifiedTime
	 */
	@Accessor(qualifier = "lastSyncSourceModifiedTime", type = Accessor.Type.SETTER)
	public void setLastSyncSourceModifiedTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(LASTSYNCSOURCEMODIFIEDTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.lastSyncTime</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the lastSyncTime
	 */
	@Accessor(qualifier = "lastSyncTime", type = Accessor.Type.SETTER)
	public void setLastSyncTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(LASTSYNCTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.pendingAttributeQualifiers</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the pendingAttributeQualifiers
	 */
	@Accessor(qualifier = "pendingAttributeQualifiers", type = Accessor.Type.SETTER)
	public void setPendingAttributeQualifiers(final String value)
	{
		getPersistenceContext().setPropertyValue(PENDINGATTRIBUTEQUALIFIERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.pendingAttributesOwnerJob</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the pendingAttributesOwnerJob
	 */
	@Accessor(qualifier = "pendingAttributesOwnerJob", type = Accessor.Type.SETTER)
	public void setPendingAttributesOwnerJob(final SyncItemCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(PENDINGATTRIBUTESOWNERJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.pendingAttributesScheduledTurn</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the pendingAttributesScheduledTurn
	 */
	@Accessor(qualifier = "pendingAttributesScheduledTurn", type = Accessor.Type.SETTER)
	public void setPendingAttributesScheduledTurn(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PENDINGATTRIBUTESSCHEDULEDTURN, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.sourceItem</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.SETTER)
	public void setSourceItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCEITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.sourceVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
	public void setSourceVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.syncJob</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the syncJob
	 */
	@Accessor(qualifier = "syncJob", type = Accessor.Type.SETTER)
	public void setSyncJob(final SyncItemJobModel value)
	{
		getPersistenceContext().setPropertyValue(SYNCJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.targetItem</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetItem
	 */
	@Accessor(qualifier = "targetItem", type = Accessor.Type.SETTER)
	public void setTargetItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.targetVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
	public void setTargetVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETVERSION, value);
	}
	
}
