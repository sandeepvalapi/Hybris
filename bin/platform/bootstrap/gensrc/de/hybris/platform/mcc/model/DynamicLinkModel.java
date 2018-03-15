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
package de.hybris.platform.mcc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.mcc.enums.CockpitLinkArea;
import de.hybris.platform.mcc.model.StaticLinkModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DynamicLink first defined at extension mcc.
 */
@SuppressWarnings("all")
public class DynamicLinkModel extends StaticLinkModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DynamicLink";
	
	/** <i>Generated constant</i> - Attribute key of <code>DynamicLink.script</code> attribute defined at extension <code>mcc</code>. */
	public static final String SCRIPT = "script";
	
	/** <i>Generated constant</i> - Attribute key of <code>DynamicLink.visibleScript</code> attribute defined at extension <code>mcc</code>. */
	public static final String VISIBLESCRIPT = "visibleScript";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DynamicLinkModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DynamicLinkModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _area initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _code initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 */
	@Deprecated
	public DynamicLinkModel(final CockpitLinkArea _area, final String _code)
	{
		super();
		setArea(_area);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _area initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _code initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DynamicLinkModel(final CockpitLinkArea _area, final String _code, final ItemModel _owner)
	{
		super();
		setArea(_area);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DynamicLink.script</code> attribute defined at extension <code>mcc</code>. 
	 * @return the script - Script that returns title->URL dynamic link map
	 */
	@Accessor(qualifier = "script", type = Accessor.Type.GETTER)
	public String getScript()
	{
		return getPersistenceContext().getPropertyValue(SCRIPT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DynamicLink.visibleScript</code> attribute defined at extension <code>mcc</code>. 
	 * @return the visibleScript - Script that returns true if the link should be visible
	 */
	@Accessor(qualifier = "visibleScript", type = Accessor.Type.GETTER)
	public String getVisibleScript()
	{
		return getPersistenceContext().getPropertyValue(VISIBLESCRIPT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DynamicLink.script</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the script - Script that returns title->URL dynamic link map
	 */
	@Accessor(qualifier = "script", type = Accessor.Type.SETTER)
	public void setScript(final String value)
	{
		getPersistenceContext().setPropertyValue(SCRIPT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DynamicLink.visibleScript</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the visibleScript - Script that returns true if the link should be visible
	 */
	@Accessor(qualifier = "visibleScript", type = Accessor.Type.SETTER)
	public void setVisibleScript(final String value)
	{
		getPersistenceContext().setPropertyValue(VISIBLESCRIPT, value);
	}
	
}
