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
package de.hybris.platform.cms2.model.contents.contentslot;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type ContentSlot first defined at extension cms2.
 */
@SuppressWarnings("all")
public class ContentSlotModel extends CMSItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ContentSlot";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlot.active</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlot.activeFrom</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVEFROM = "activeFrom";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlot.activeUntil</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVEUNTIL = "activeUntil";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlot.currentPosition</code> attribute defined at extension <code>cms2</code>. */
	public static final String CURRENTPOSITION = "currentPosition";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlot.originalSlot</code> attribute defined at extension <code>cms2</code>. */
	public static final String ORIGINALSLOT = "originalSlot";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlot.cmsComponents</code> attribute defined at extension <code>cms2</code>. */
	public static final String CMSCOMPONENTS = "cmsComponents";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ContentSlotModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ContentSlotModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ContentSlotModel(final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ContentSlotModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlot.active</code> attribute defined at extension <code>cms2</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlot.activeFrom</code> attribute defined at extension <code>cms2</code>. 
	 * @return the activeFrom
	 */
	@Accessor(qualifier = "activeFrom", type = Accessor.Type.GETTER)
	public Date getActiveFrom()
	{
		return getPersistenceContext().getPropertyValue(ACTIVEFROM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlot.activeUntil</code> attribute defined at extension <code>cms2</code>. 
	 * @return the activeUntil
	 */
	@Accessor(qualifier = "activeUntil", type = Accessor.Type.GETTER)
	public Date getActiveUntil()
	{
		return getPersistenceContext().getPropertyValue(ACTIVEUNTIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlot.cmsComponents</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cmsComponents
	 */
	@Accessor(qualifier = "cmsComponents", type = Accessor.Type.GETTER)
	public List<AbstractCMSComponentModel> getCmsComponents()
	{
		return getPersistenceContext().getPropertyValue(CMSCOMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlot.currentPosition</code> attribute defined at extension <code>cms2</code>. 
	 * @return the currentPosition
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "currentPosition", type = Accessor.Type.GETTER)
	public String getCurrentPosition()
	{
		return getPersistenceContext().getPropertyValue(CURRENTPOSITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlot.originalSlot</code> attribute defined at extension <code>cms2</code>. 
	 * @return the originalSlot - The slot to override.
	 */
	@Accessor(qualifier = "originalSlot", type = Accessor.Type.GETTER)
	public ContentSlotModel getOriginalSlot()
	{
		return getPersistenceContext().getPropertyValue(ORIGINALSLOT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlot.active</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlot.activeFrom</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the activeFrom
	 */
	@Accessor(qualifier = "activeFrom", type = Accessor.Type.SETTER)
	public void setActiveFrom(final Date value)
	{
		getPersistenceContext().setPropertyValue(ACTIVEFROM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlot.activeUntil</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the activeUntil
	 */
	@Accessor(qualifier = "activeUntil", type = Accessor.Type.SETTER)
	public void setActiveUntil(final Date value)
	{
		getPersistenceContext().setPropertyValue(ACTIVEUNTIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlot.cmsComponents</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the cmsComponents
	 */
	@Accessor(qualifier = "cmsComponents", type = Accessor.Type.SETTER)
	public void setCmsComponents(final List<AbstractCMSComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(CMSCOMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlot.currentPosition</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the currentPosition
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "currentPosition", type = Accessor.Type.SETTER)
	public void setCurrentPosition(final String value)
	{
		getPersistenceContext().setPropertyValue(CURRENTPOSITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlot.originalSlot</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the originalSlot - The slot to override.
	 */
	@Accessor(qualifier = "originalSlot", type = Accessor.Type.SETTER)
	public void setOriginalSlot(final ContentSlotModel value)
	{
		getPersistenceContext().setPropertyValue(ORIGINALSLOT, value);
	}
	
}
