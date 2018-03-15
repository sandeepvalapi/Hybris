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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type WidgetPreferences first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class WidgetPreferencesModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "WidgetPreferences";
	
	/** <i>Generated constant</i> - Attribute key of <code>WidgetPreferences.title</code> attribute defined at extension <code>cockpit</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>WidgetPreferences.ownerUser</code> attribute defined at extension <code>cockpit</code>. */
	public static final String OWNERUSER = "ownerUser";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WidgetPreferencesModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WidgetPreferencesModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public WidgetPreferencesModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetPreferences.ownerUser</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the ownerUser
	 */
	@Accessor(qualifier = "ownerUser", type = Accessor.Type.GETTER)
	public UserModel getOwnerUser()
	{
		return getPersistenceContext().getPropertyValue(OWNERUSER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetPreferences.title</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetPreferences.title</code> attribute defined at extension <code>cockpit</code>. 
	 * @param loc the value localization key 
	 * @return the title
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetPreferences.ownerUser</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the ownerUser
	 */
	@Accessor(qualifier = "ownerUser", type = Accessor.Type.SETTER)
	public void setOwnerUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(OWNERUSER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetPreferences.title</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetPreferences.title</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the title
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
}
