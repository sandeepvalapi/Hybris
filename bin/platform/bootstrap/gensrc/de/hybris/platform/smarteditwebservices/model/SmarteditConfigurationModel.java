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
package de.hybris.platform.smarteditwebservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SmarteditConfiguration first defined at extension smarteditwebservices.
 */
@SuppressWarnings("all")
public class SmarteditConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SmarteditConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>SmarteditConfiguration.key</code> attribute defined at extension <code>smarteditwebservices</code>. */
	public static final String KEY = "key";
	
	/** <i>Generated constant</i> - Attribute key of <code>SmarteditConfiguration.value</code> attribute defined at extension <code>smarteditwebservices</code>. */
	public static final String VALUE = "value";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SmarteditConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SmarteditConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _key initial attribute declared by type <code>SmarteditConfiguration</code> at extension <code>smarteditwebservices</code>
	 * @param _value initial attribute declared by type <code>SmarteditConfiguration</code> at extension <code>smarteditwebservices</code>
	 */
	@Deprecated
	public SmarteditConfigurationModel(final String _key, final String _value)
	{
		super();
		setKey(_key);
		setValue(_value);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _key initial attribute declared by type <code>SmarteditConfiguration</code> at extension <code>smarteditwebservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _value initial attribute declared by type <code>SmarteditConfiguration</code> at extension <code>smarteditwebservices</code>
	 */
	@Deprecated
	public SmarteditConfigurationModel(final String _key, final ItemModel _owner, final String _value)
	{
		super();
		setKey(_key);
		setOwner(_owner);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SmarteditConfiguration.key</code> attribute defined at extension <code>smarteditwebservices</code>. 
	 * @return the key - name of the property
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.GETTER)
	public String getKey()
	{
		return getPersistenceContext().getPropertyValue(KEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SmarteditConfiguration.value</code> attribute defined at extension <code>smarteditwebservices</code>. 
	 * @return the value - value of the property
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public String getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SmarteditConfiguration.key</code> attribute defined at extension <code>smarteditwebservices</code>. 
	 *  
	 * @param value the key - name of the property
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.SETTER)
	public void setKey(final String value)
	{
		getPersistenceContext().setPropertyValue(KEY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SmarteditConfiguration.value</code> attribute defined at extension <code>smarteditwebservices</code>. 
	 *  
	 * @param value the value - value of the property
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
}
