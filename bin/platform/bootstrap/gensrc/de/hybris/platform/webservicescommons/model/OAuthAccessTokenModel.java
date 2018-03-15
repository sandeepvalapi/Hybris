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
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OAuthRefreshTokenModel;

/**
 * Generated model class for type OAuthAccessToken first defined at extension oauth2.
 */
@SuppressWarnings("all")
public class OAuthAccessTokenModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OAuthAccessToken";
	
	/**<i>Generated relation code constant for relation <code>User2TokenRelation</code> defining source attribute <code>user</code> in extension <code>oauth2</code>.</i>*/
	public static final String _USER2TOKENRELATION = "User2TokenRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthAccessToken.tokenId</code> attribute defined at extension <code>oauth2</code>. */
	public static final String TOKENID = "tokenId";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthAccessToken.token</code> attribute defined at extension <code>oauth2</code>. */
	public static final String TOKEN = "token";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthAccessToken.authenticationId</code> attribute defined at extension <code>oauth2</code>. */
	public static final String AUTHENTICATIONID = "authenticationId";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthAccessToken.client</code> attribute defined at extension <code>oauth2</code>. */
	public static final String CLIENT = "client";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthAccessToken.authentication</code> attribute defined at extension <code>oauth2</code>. */
	public static final String AUTHENTICATION = "authentication";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthAccessToken.refreshToken</code> attribute defined at extension <code>oauth2</code>. */
	public static final String REFRESHTOKEN = "refreshToken";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthAccessToken.user</code> attribute defined at extension <code>oauth2</code>. */
	public static final String USER = "user";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OAuthAccessTokenModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OAuthAccessTokenModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _authenticationId initial attribute declared by type <code>OAuthAccessToken</code> at extension <code>oauth2</code>
	 * @param _client initial attribute declared by type <code>OAuthAccessToken</code> at extension <code>oauth2</code>
	 * @param _tokenId initial attribute declared by type <code>OAuthAccessToken</code> at extension <code>oauth2</code>
	 */
	@Deprecated
	public OAuthAccessTokenModel(final String _authenticationId, final OAuthClientDetailsModel _client, final String _tokenId)
	{
		super();
		setAuthenticationId(_authenticationId);
		setClient(_client);
		setTokenId(_tokenId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _authenticationId initial attribute declared by type <code>OAuthAccessToken</code> at extension <code>oauth2</code>
	 * @param _client initial attribute declared by type <code>OAuthAccessToken</code> at extension <code>oauth2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _refreshToken initial attribute declared by type <code>OAuthAccessToken</code> at extension <code>oauth2</code>
	 * @param _token initial attribute declared by type <code>OAuthAccessToken</code> at extension <code>oauth2</code>
	 * @param _tokenId initial attribute declared by type <code>OAuthAccessToken</code> at extension <code>oauth2</code>
	 */
	@Deprecated
	public OAuthAccessTokenModel(final String _authenticationId, final OAuthClientDetailsModel _client, final ItemModel _owner, final OAuthRefreshTokenModel _refreshToken, final Object _token, final String _tokenId)
	{
		super();
		setAuthenticationId(_authenticationId);
		setClient(_client);
		setOwner(_owner);
		setRefreshToken(_refreshToken);
		setToken(_token);
		setTokenId(_tokenId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthAccessToken.authentication</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the authentication - Serialized authentication object
	 */
	@Accessor(qualifier = "authentication", type = Accessor.Type.GETTER)
	public Object getAuthentication()
	{
		return getPersistenceContext().getPropertyValue(AUTHENTICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthAccessToken.authenticationId</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the authenticationId - Authentication identifier
	 */
	@Accessor(qualifier = "authenticationId", type = Accessor.Type.GETTER)
	public String getAuthenticationId()
	{
		return getPersistenceContext().getPropertyValue(AUTHENTICATIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthAccessToken.client</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the client - Client identifier
	 */
	@Accessor(qualifier = "client", type = Accessor.Type.GETTER)
	public OAuthClientDetailsModel getClient()
	{
		return getPersistenceContext().getPropertyValue(CLIENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthAccessToken.refreshToken</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the refreshToken
	 */
	@Accessor(qualifier = "refreshToken", type = Accessor.Type.GETTER)
	public OAuthRefreshTokenModel getRefreshToken()
	{
		return getPersistenceContext().getPropertyValue(REFRESHTOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthAccessToken.token</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the token - serialized token object
	 */
	@Accessor(qualifier = "token", type = Accessor.Type.GETTER)
	public Object getToken()
	{
		return getPersistenceContext().getPropertyValue(TOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthAccessToken.tokenId</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the tokenId - Token key
	 */
	@Accessor(qualifier = "tokenId", type = Accessor.Type.GETTER)
	public String getTokenId()
	{
		return getPersistenceContext().getPropertyValue(TOKENID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthAccessToken.user</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthAccessToken.authentication</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the authentication - Serialized authentication object
	 */
	@Accessor(qualifier = "authentication", type = Accessor.Type.SETTER)
	public void setAuthentication(final Object value)
	{
		getPersistenceContext().setPropertyValue(AUTHENTICATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>OAuthAccessToken.authenticationId</code> attribute defined at extension <code>oauth2</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the authenticationId - Authentication identifier
	 */
	@Accessor(qualifier = "authenticationId", type = Accessor.Type.SETTER)
	public void setAuthenticationId(final String value)
	{
		getPersistenceContext().setPropertyValue(AUTHENTICATIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>OAuthAccessToken.client</code> attribute defined at extension <code>oauth2</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the client - Client identifier
	 */
	@Accessor(qualifier = "client", type = Accessor.Type.SETTER)
	public void setClient(final OAuthClientDetailsModel value)
	{
		getPersistenceContext().setPropertyValue(CLIENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>OAuthAccessToken.refreshToken</code> attribute defined at extension <code>oauth2</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the refreshToken
	 */
	@Accessor(qualifier = "refreshToken", type = Accessor.Type.SETTER)
	public void setRefreshToken(final OAuthRefreshTokenModel value)
	{
		getPersistenceContext().setPropertyValue(REFRESHTOKEN, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>OAuthAccessToken.token</code> attribute defined at extension <code>oauth2</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the token - serialized token object
	 */
	@Accessor(qualifier = "token", type = Accessor.Type.SETTER)
	public void setToken(final Object value)
	{
		getPersistenceContext().setPropertyValue(TOKEN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthAccessToken.tokenId</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the tokenId - Token key
	 */
	@Accessor(qualifier = "tokenId", type = Accessor.Type.SETTER)
	public void setTokenId(final String value)
	{
		getPersistenceContext().setPropertyValue(TOKENID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthAccessToken.user</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
