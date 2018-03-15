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
package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.TypeManagerManagedModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type Type first defined at extension core.
 */
@SuppressWarnings("all")
public class TypeModel extends TypeManagerManagedModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Type";
	
	/** <i>Generated constant</i> - Attribute key of <code>Type.xmldefinition</code> attribute defined at extension <code>core</code>. */
	public static final String XMLDEFINITION = "xmldefinition";
	
	/** <i>Generated constant</i> - Attribute key of <code>Type.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Type.defaultValue</code> attribute defined at extension <code>core</code>. */
	public static final String DEFAULTVALUE = "defaultValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>Type.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 */
	@Deprecated
	public TypeModel(final String _code, final Boolean _generate)
	{
		super();
		setCode(_code);
		setGenerate(_generate);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public TypeModel(final String _code, final Boolean _generate, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setGenerate(_generate);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Type.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Type.defaultValue</code> attribute defined at extension <code>core</code>. 
	 * @return the defaultValue
	 */
	@Accessor(qualifier = "defaultValue", type = Accessor.Type.GETTER)
	public Object getDefaultValue()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Type.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Type.description</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Type.xmldefinition</code> attribute defined at extension <code>core</code>. 
	 * @return the xmldefinition
	 */
	@Accessor(qualifier = "xmldefinition", type = Accessor.Type.GETTER)
	public String getXmldefinition()
	{
		return getPersistenceContext().getPropertyValue(XMLDEFINITION);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Type.code</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Type.defaultValue</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the defaultValue
	 */
	@Accessor(qualifier = "defaultValue", type = Accessor.Type.SETTER)
	public void setDefaultValue(final Object value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Type.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Type.description</code> attribute defined at extension <code>core</code>. 
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
