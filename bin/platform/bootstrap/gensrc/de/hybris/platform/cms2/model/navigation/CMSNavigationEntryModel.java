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
package de.hybris.platform.cms2.model.navigation;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CMSNavigationEntry first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSNavigationEntryModel extends CMSItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSNavigationEntry";
	
	/**<i>Generated relation code constant for relation <code>CMSNavNodesToCMSNavEntries</code> defining source attribute <code>navigationNode</code> in extension <code>cms2</code>.</i>*/
	public static final String _CMSNAVNODESTOCMSNAVENTRIES = "CMSNavNodesToCMSNavEntries";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationEntry.item</code> attribute defined at extension <code>cms2</code>. */
	public static final String ITEM = "item";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationEntry.navigationNodePOS</code> attribute defined at extension <code>cms2</code>. */
	public static final String NAVIGATIONNODEPOS = "navigationNodePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationEntry.navigationNode</code> attribute defined at extension <code>cms2</code>. */
	public static final String NAVIGATIONNODE = "navigationNode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSNavigationEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSNavigationEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _item initial attribute declared by type <code>CMSNavigationEntry</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSNavigationEntryModel(final CatalogVersionModel _catalogVersion, final ItemModel _item, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setItem(_item);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _item initial attribute declared by type <code>CMSNavigationEntry</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSNavigationEntryModel(final CatalogVersionModel _catalogVersion, final ItemModel _item, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setItem(_item);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationEntry.item</code> attribute defined at extension <code>cms2</code>. 
	 * @return the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.GETTER)
	public ItemModel getItem()
	{
		return getPersistenceContext().getPropertyValue(ITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationEntry.navigationNode</code> attribute defined at extension <code>cms2</code>. 
	 * @return the navigationNode
	 */
	@Accessor(qualifier = "navigationNode", type = Accessor.Type.GETTER)
	public CMSNavigationNodeModel getNavigationNode()
	{
		return getPersistenceContext().getPropertyValue(NAVIGATIONNODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationEntry.item</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.SETTER)
	public void setItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(ITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationEntry.navigationNode</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the navigationNode
	 */
	@Accessor(qualifier = "navigationNode", type = Accessor.Type.SETTER)
	public void setNavigationNode(final CMSNavigationNodeModel value)
	{
		getPersistenceContext().setPropertyValue(NAVIGATIONNODE, value);
	}
	
}
