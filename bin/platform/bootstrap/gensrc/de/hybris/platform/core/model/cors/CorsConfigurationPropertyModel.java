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
package de.hybris.platform.core.model.cors;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CorsConfigurationProperty first defined at extension core.
 */
@SuppressWarnings("all")
public class CorsConfigurationPropertyModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CorsConfigurationProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>CorsConfigurationProperty.context</code> attribute defined at extension <code>core</code>. */
	public static final String CONTEXT = "context";
	
	/** <i>Generated constant</i> - Attribute key of <code>CorsConfigurationProperty.key</code> attribute defined at extension <code>core</code>. */
	public static final String KEY = "key";
	
	/** <i>Generated constant</i> - Attribute key of <code>CorsConfigurationProperty.value</code> attribute defined at extension <code>core</code>. */
	public static final String VALUE = "value";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CorsConfigurationPropertyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CorsConfigurationPropertyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _context initial attribute declared by type <code>CorsConfigurationProperty</code> at extension <code>core</code>
	 * @param _key initial attribute declared by type <code>CorsConfigurationProperty</code> at extension <code>core</code>
	 * @param _value initial attribute declared by type <code>CorsConfigurationProperty</code> at extension <code>core</code>
	 */
	@Deprecated
	public CorsConfigurationPropertyModel(final String _context, final String _key, final String _value)
	{
		super();
		setContext(_context);
		setKey(_key);
		setValue(_value);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _context initial attribute declared by type <code>CorsConfigurationProperty</code> at extension <code>core</code>
	 * @param _key initial attribute declared by type <code>CorsConfigurationProperty</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _value initial attribute declared by type <code>CorsConfigurationProperty</code> at extension <code>core</code>
	 */
	@Deprecated
	public CorsConfigurationPropertyModel(final String _context, final String _key, final ItemModel _owner, final String _value)
	{
		super();
		setContext(_context);
		setKey(_key);
		setOwner(_owner);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CorsConfigurationProperty.context</code> attribute defined at extension <code>core</code>. 
	 * @return the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.GETTER)
	public String getContext()
	{
		return getPersistenceContext().getPropertyValue(CONTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CorsConfigurationProperty.key</code> attribute defined at extension <code>core</code>. 
	 * @return the key
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.GETTER)
	public String getKey()
	{
		return getPersistenceContext().getPropertyValue(KEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CorsConfigurationProperty.value</code> attribute defined at extension <code>core</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public String getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CorsConfigurationProperty.context</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.SETTER)
	public void setContext(final String value)
	{
		getPersistenceContext().setPropertyValue(CONTEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CorsConfigurationProperty.key</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the key
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.SETTER)
	public void setKey(final String value)
	{
		getPersistenceContext().setPropertyValue(KEY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CorsConfigurationProperty.value</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
}
