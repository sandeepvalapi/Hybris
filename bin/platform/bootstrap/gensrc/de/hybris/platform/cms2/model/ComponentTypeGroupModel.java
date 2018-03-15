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
package de.hybris.platform.cms2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type ComponentTypeGroup first defined at extension cms2.
 */
@SuppressWarnings("all")
public class ComponentTypeGroupModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ComponentTypeGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComponentTypeGroup.code</code> attribute defined at extension <code>cms2</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComponentTypeGroup.description</code> attribute defined at extension <code>cms2</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComponentTypeGroup.cmsComponentTypes</code> attribute defined at extension <code>cms2</code>. */
	public static final String CMSCOMPONENTTYPES = "cmsComponentTypes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ComponentTypeGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ComponentTypeGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ComponentTypeGroup</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ComponentTypeGroupModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ComponentTypeGroup</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ComponentTypeGroupModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComponentTypeGroup.cmsComponentTypes</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cmsComponentTypes
	 */
	@Accessor(qualifier = "cmsComponentTypes", type = Accessor.Type.GETTER)
	public Set<CMSComponentTypeModel> getCmsComponentTypes()
	{
		return getPersistenceContext().getPropertyValue(CMSCOMPONENTTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComponentTypeGroup.code</code> attribute defined at extension <code>cms2</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComponentTypeGroup.description</code> attribute defined at extension <code>cms2</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ComponentTypeGroup.description</code> attribute defined at extension <code>cms2</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComponentTypeGroup.cmsComponentTypes</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the cmsComponentTypes
	 */
	@Accessor(qualifier = "cmsComponentTypes", type = Accessor.Type.SETTER)
	public void setCmsComponentTypes(final Set<CMSComponentTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(CMSCOMPONENTTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComponentTypeGroup.code</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComponentTypeGroup.description</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ComponentTypeGroup.description</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
}
