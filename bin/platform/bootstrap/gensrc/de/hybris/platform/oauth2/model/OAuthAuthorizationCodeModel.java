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
package de.hybris.platform.oauth2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OAuthAuthorizationCode first defined at extension oauth2.
 */
@SuppressWarnings("all")
public class OAuthAuthorizationCodeModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OAuthAuthorizationCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthAuthorizationCode.code</code> attribute defined at extension <code>oauth2</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthAuthorizationCode.authentication</code> attribute defined at extension <code>oauth2</code>. */
	public static final String AUTHENTICATION = "authentication";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OAuthAuthorizationCodeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OAuthAuthorizationCodeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OAuthAuthorizationCode</code> at extension <code>oauth2</code>
	 */
	@Deprecated
	public OAuthAuthorizationCodeModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OAuthAuthorizationCode</code> at extension <code>oauth2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public OAuthAuthorizationCodeModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthAuthorizationCode.authentication</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the authentication - Serialized authentication object
	 */
	@Accessor(qualifier = "authentication", type = Accessor.Type.GETTER)
	public Object getAuthentication()
	{
		return getPersistenceContext().getPropertyValue(AUTHENTICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthAuthorizationCode.code</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the code - Token key
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthAuthorizationCode.authentication</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the authentication - Serialized authentication object
	 */
	@Accessor(qualifier = "authentication", type = Accessor.Type.SETTER)
	public void setAuthentication(final Object value)
	{
		getPersistenceContext().setPropertyValue(AUTHENTICATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthAuthorizationCode.code</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the code - Token key
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
}
