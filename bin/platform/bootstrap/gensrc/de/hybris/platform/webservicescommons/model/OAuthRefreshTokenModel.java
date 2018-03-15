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
package de.hybris.platform.webservicescommons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OAuthRefreshToken first defined at extension oauth2.
 */
@SuppressWarnings("all")
public class OAuthRefreshTokenModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OAuthRefreshToken";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthRefreshToken.tokenId</code> attribute defined at extension <code>oauth2</code>. */
	public static final String TOKENID = "tokenId";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthRefreshToken.token</code> attribute defined at extension <code>oauth2</code>. */
	public static final String TOKEN = "token";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthRefreshToken.authentication</code> attribute defined at extension <code>oauth2</code>. */
	public static final String AUTHENTICATION = "authentication";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OAuthRefreshTokenModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OAuthRefreshTokenModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _tokenId initial attribute declared by type <code>OAuthRefreshToken</code> at extension <code>oauth2</code>
	 */
	@Deprecated
	public OAuthRefreshTokenModel(final String _tokenId)
	{
		super();
		setTokenId(_tokenId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _tokenId initial attribute declared by type <code>OAuthRefreshToken</code> at extension <code>oauth2</code>
	 */
	@Deprecated
	public OAuthRefreshTokenModel(final ItemModel _owner, final String _tokenId)
	{
		super();
		setOwner(_owner);
		setTokenId(_tokenId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthRefreshToken.authentication</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the authentication - Serialized authentication object
	 */
	@Accessor(qualifier = "authentication", type = Accessor.Type.GETTER)
	public Object getAuthentication()
	{
		return getPersistenceContext().getPropertyValue(AUTHENTICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthRefreshToken.token</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the token - Serialized token object
	 */
	@Accessor(qualifier = "token", type = Accessor.Type.GETTER)
	public Object getToken()
	{
		return getPersistenceContext().getPropertyValue(TOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthRefreshToken.tokenId</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the tokenId - Token key
	 */
	@Accessor(qualifier = "tokenId", type = Accessor.Type.GETTER)
	public String getTokenId()
	{
		return getPersistenceContext().getPropertyValue(TOKENID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthRefreshToken.authentication</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the authentication - Serialized authentication object
	 */
	@Accessor(qualifier = "authentication", type = Accessor.Type.SETTER)
	public void setAuthentication(final Object value)
	{
		getPersistenceContext().setPropertyValue(AUTHENTICATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthRefreshToken.token</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the token - Serialized token object
	 */
	@Accessor(qualifier = "token", type = Accessor.Type.SETTER)
	public void setToken(final Object value)
	{
		getPersistenceContext().setPropertyValue(TOKEN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthRefreshToken.tokenId</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the tokenId - Token key
	 */
	@Accessor(qualifier = "tokenId", type = Accessor.Type.SETTER)
	public void setTokenId(final String value)
	{
		getPersistenceContext().setPropertyValue(TOKENID, value);
	}
	
}
