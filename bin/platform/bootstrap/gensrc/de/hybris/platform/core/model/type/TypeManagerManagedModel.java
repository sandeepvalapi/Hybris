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
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type TypeManagerManaged first defined at extension core.
 */
@SuppressWarnings("all")
public class TypeManagerManagedModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "TypeManagerManaged";
	
	/** <i>Generated constant</i> - Attribute key of <code>TypeManagerManaged.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>TypeManagerManaged.extensionName</code> attribute defined at extension <code>core</code>. */
	public static final String EXTENSIONNAME = "extensionName";
	
	/** <i>Generated constant</i> - Attribute key of <code>TypeManagerManaged.deprecated</code> attribute defined at extension <code>core</code>. */
	public static final String DEPRECATED = "deprecated";
	
	/** <i>Generated constant</i> - Attribute key of <code>TypeManagerManaged.autocreate</code> attribute defined at extension <code>core</code>. */
	public static final String AUTOCREATE = "autocreate";
	
	/** <i>Generated constant</i> - Attribute key of <code>TypeManagerManaged.generate</code> attribute defined at extension <code>core</code>. */
	public static final String GENERATE = "generate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TypeManagerManagedModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TypeManagerManagedModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 */
	@Deprecated
	public TypeManagerManagedModel(final Boolean _generate)
	{
		super();
		setGenerate(_generate);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public TypeManagerManagedModel(final Boolean _generate, final ItemModel _owner)
	{
		super();
		setGenerate(_generate);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TypeManagerManaged.autocreate</code> attribute defined at extension <code>core</code>. 
	 * @return the autocreate
	 */
	@Accessor(qualifier = "autocreate", type = Accessor.Type.GETTER)
	public Boolean getAutocreate()
	{
		return getPersistenceContext().getPropertyValue(AUTOCREATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TypeManagerManaged.deprecated</code> attribute defined at extension <code>core</code>. 
	 * @return the deprecated
	 */
	@Accessor(qualifier = "deprecated", type = Accessor.Type.GETTER)
	public Boolean getDeprecated()
	{
		return getPersistenceContext().getPropertyValue(DEPRECATED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TypeManagerManaged.extensionName</code> attribute defined at extension <code>core</code>. 
	 * @return the extensionName
	 */
	@Accessor(qualifier = "extensionName", type = Accessor.Type.GETTER)
	public String getExtensionName()
	{
		return getPersistenceContext().getPropertyValue(EXTENSIONNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TypeManagerManaged.generate</code> attribute defined at extension <code>core</code>. 
	 * @return the generate
	 */
	@Accessor(qualifier = "generate", type = Accessor.Type.GETTER)
	public Boolean getGenerate()
	{
		return getPersistenceContext().getPropertyValue(GENERATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TypeManagerManaged.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>TypeManagerManaged.name</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TypeManagerManaged.autocreate</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the autocreate
	 */
	@Accessor(qualifier = "autocreate", type = Accessor.Type.SETTER)
	public void setAutocreate(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(AUTOCREATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TypeManagerManaged.extensionName</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the extensionName
	 */
	@Accessor(qualifier = "extensionName", type = Accessor.Type.SETTER)
	public void setExtensionName(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTENSIONNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TypeManagerManaged.generate</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the generate
	 */
	@Accessor(qualifier = "generate", type = Accessor.Type.SETTER)
	public void setGenerate(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(GENERATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TypeManagerManaged.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>TypeManagerManaged.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
}
