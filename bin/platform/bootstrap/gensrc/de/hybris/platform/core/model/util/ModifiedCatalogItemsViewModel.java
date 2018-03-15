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
package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type ModifiedCatalogItemsView first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ModifiedCatalogItemsViewModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ModifiedCatalogItemsView";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.job</code> attribute defined at extension <code>catalog</code>. */
	public static final String JOB = "job";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.sourceItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEITEM = "sourceItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.targetItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETITEM = "targetItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.sourceModifiedTime</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEMODIFIEDTIME = "sourceModifiedTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.lastSyncTime</code> attribute defined at extension <code>catalog</code>. */
	public static final String LASTSYNCTIME = "lastSyncTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.typeCode</code> attribute defined at extension <code>catalog</code>. */
	public static final String TYPECODE = "typeCode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ModifiedCatalogItemsViewModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ModifiedCatalogItemsViewModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ModifiedCatalogItemsViewModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.job</code> attribute defined at extension <code>catalog</code>. 
	 * @return the job
	 */
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public SyncItemJobModel getJob()
	{
		return getPersistenceContext().getPropertyValue(JOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.lastSyncTime</code> attribute defined at extension <code>catalog</code>. 
	 * @return the lastSyncTime
	 */
	@Accessor(qualifier = "lastSyncTime", type = Accessor.Type.GETTER)
	public Date getLastSyncTime()
	{
		return getPersistenceContext().getPropertyValue(LASTSYNCTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.sourceItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
	public ItemModel getSourceItem()
	{
		return getPersistenceContext().getPropertyValue(SOURCEITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.sourceModifiedTime</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceModifiedTime
	 */
	@Accessor(qualifier = "sourceModifiedTime", type = Accessor.Type.GETTER)
	public Date getSourceModifiedTime()
	{
		return getPersistenceContext().getPropertyValue(SOURCEMODIFIEDTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.targetItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetItem
	 */
	@Accessor(qualifier = "targetItem", type = Accessor.Type.GETTER)
	public ItemModel getTargetItem()
	{
		return getPersistenceContext().getPropertyValue(TARGETITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.typeCode</code> attribute defined at extension <code>catalog</code>. 
	 * @return the typeCode
	 */
	@Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
	public String getTypeCode()
	{
		return getPersistenceContext().getPropertyValue(TYPECODE);
	}
	
}
