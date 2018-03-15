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
package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type CMSTimeRestriction first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSTimeRestrictionModel extends AbstractRestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSTimeRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSTimeRestriction.activeFrom</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVEFROM = "activeFrom";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSTimeRestriction.activeUntil</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVEUNTIL = "activeUntil";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSTimeRestriction.useStoreTimeZone</code> attribute defined at extension <code>cms2</code>. */
	public static final String USESTORETIMEZONE = "useStoreTimeZone";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSTimeRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSTimeRestrictionModel(final ItemModelContext ctx)
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
	public CMSTimeRestrictionModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public CMSTimeRestrictionModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSTimeRestriction.activeFrom</code> attribute defined at extension <code>cms2</code>. 
	 * @return the activeFrom
	 */
	@Accessor(qualifier = "activeFrom", type = Accessor.Type.GETTER)
	public Date getActiveFrom()
	{
		return getPersistenceContext().getPropertyValue(ACTIVEFROM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSTimeRestriction.activeUntil</code> attribute defined at extension <code>cms2</code>. 
	 * @return the activeUntil
	 */
	@Accessor(qualifier = "activeUntil", type = Accessor.Type.GETTER)
	public Date getActiveUntil()
	{
		return getPersistenceContext().getPropertyValue(ACTIVEUNTIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSTimeRestriction.useStoreTimeZone</code> attribute defined at extension <code>cms2</code>. 
	 * @return the useStoreTimeZone - If true then use the dates relative to the store time zone
	 */
	@Accessor(qualifier = "useStoreTimeZone", type = Accessor.Type.GETTER)
	public Boolean getUseStoreTimeZone()
	{
		return getPersistenceContext().getPropertyValue(USESTORETIMEZONE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSTimeRestriction.activeFrom</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the activeFrom
	 */
	@Accessor(qualifier = "activeFrom", type = Accessor.Type.SETTER)
	public void setActiveFrom(final Date value)
	{
		getPersistenceContext().setPropertyValue(ACTIVEFROM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSTimeRestriction.activeUntil</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the activeUntil
	 */
	@Accessor(qualifier = "activeUntil", type = Accessor.Type.SETTER)
	public void setActiveUntil(final Date value)
	{
		getPersistenceContext().setPropertyValue(ACTIVEUNTIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSTimeRestriction.useStoreTimeZone</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the useStoreTimeZone - If true then use the dates relative to the store time zone
	 */
	@Accessor(qualifier = "useStoreTimeZone", type = Accessor.Type.SETTER)
	public void setUseStoreTimeZone(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(USESTORETIMEZONE, value);
	}
	
}
