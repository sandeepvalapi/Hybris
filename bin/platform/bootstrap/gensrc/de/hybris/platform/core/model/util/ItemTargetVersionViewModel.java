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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ItemTargetVersionView first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ItemTargetVersionViewModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ItemTargetVersionView";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemTargetVersionView.sourceItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEITEM = "sourceItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemTargetVersionView.targetItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETITEM = "targetItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemTargetVersionView.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETVERSION = "targetVersion";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ItemTargetVersionViewModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ItemTargetVersionViewModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ItemTargetVersionViewModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTargetVersionView.sourceItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
	public ItemModel getSourceItem()
	{
		return getPersistenceContext().getPropertyValue(SOURCEITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTargetVersionView.targetItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetItem
	 */
	@Accessor(qualifier = "targetItem", type = Accessor.Type.GETTER)
	public ItemModel getTargetItem()
	{
		return getPersistenceContext().getPropertyValue(TARGETITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTargetVersionView.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getTargetVersion()
	{
		return getPersistenceContext().getPropertyValue(TARGETVERSION);
	}
	
}
