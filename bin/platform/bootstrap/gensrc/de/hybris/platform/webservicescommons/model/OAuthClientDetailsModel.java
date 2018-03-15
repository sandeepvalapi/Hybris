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
import java.util.Set;

/**
 * Generated model class for type OAuthClientDetails first defined at extension oauth2.
 */
@SuppressWarnings("all")
public class OAuthClientDetailsModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OAuthClientDetails";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.clientId</code> attribute defined at extension <code>oauth2</code>. */
	public static final String CLIENTID = "clientId";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.resourceIds</code> attribute defined at extension <code>oauth2</code>. */
	public static final String RESOURCEIDS = "resourceIds";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.clientSecret</code> attribute defined at extension <code>oauth2</code>. */
	public static final String CLIENTSECRET = "clientSecret";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.scope</code> attribute defined at extension <code>oauth2</code>. */
	public static final String SCOPE = "scope";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.authorizedGrantTypes</code> attribute defined at extension <code>oauth2</code>. */
	public static final String AUTHORIZEDGRANTTYPES = "authorizedGrantTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.registeredRedirectUri</code> attribute defined at extension <code>oauth2</code>. */
	public static final String REGISTEREDREDIRECTURI = "registeredRedirectUri";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.authorities</code> attribute defined at extension <code>oauth2</code>. */
	public static final String AUTHORITIES = "authorities";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.accessTokenValiditySeconds</code> attribute defined at extension <code>oauth2</code>. */
	public static final String ACCESSTOKENVALIDITYSECONDS = "accessTokenValiditySeconds";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.refreshTokenValiditySeconds</code> attribute defined at extension <code>oauth2</code>. */
	public static final String REFRESHTOKENVALIDITYSECONDS = "refreshTokenValiditySeconds";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.autoApprove</code> attribute defined at extension <code>oauth2</code>. */
	public static final String AUTOAPPROVE = "autoApprove";
	
	/** <i>Generated constant</i> - Attribute key of <code>OAuthClientDetails.disabled</code> attribute defined at extension <code>oauth2</code>. */
	public static final String DISABLED = "disabled";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OAuthClientDetailsModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OAuthClientDetailsModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clientId initial attribute declared by type <code>OAuthClientDetails</code> at extension <code>oauth2</code>
	 */
	@Deprecated
	public OAuthClientDetailsModel(final String _clientId)
	{
		super();
		setClientId(_clientId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clientId initial attribute declared by type <code>OAuthClientDetails</code> at extension <code>oauth2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public OAuthClientDetailsModel(final String _clientId, final ItemModel _owner)
	{
		super();
		setClientId(_clientId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.accessTokenValiditySeconds</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the accessTokenValiditySeconds - Set of authorities granted to client
	 */
	@Accessor(qualifier = "accessTokenValiditySeconds", type = Accessor.Type.GETTER)
	public Integer getAccessTokenValiditySeconds()
	{
		return getPersistenceContext().getPropertyValue(ACCESSTOKENVALIDITYSECONDS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.authorities</code> attribute defined at extension <code>oauth2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the authorities - Set of authorities granted to client
	 */
	@Accessor(qualifier = "authorities", type = Accessor.Type.GETTER)
	public Set<String> getAuthorities()
	{
		return getPersistenceContext().getPropertyValue(AUTHORITIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.authorizedGrantTypes</code> attribute defined at extension <code>oauth2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the authorizedGrantTypes - Set of grant types for client
	 */
	@Accessor(qualifier = "authorizedGrantTypes", type = Accessor.Type.GETTER)
	public Set<String> getAuthorizedGrantTypes()
	{
		return getPersistenceContext().getPropertyValue(AUTHORIZEDGRANTTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.autoApprove</code> attribute defined at extension <code>oauth2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the autoApprove - Set of auto approve scopes of client
	 */
	@Accessor(qualifier = "autoApprove", type = Accessor.Type.GETTER)
	public Set<String> getAutoApprove()
	{
		return getPersistenceContext().getPropertyValue(AUTOAPPROVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.clientId</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the clientId - Client Id
	 */
	@Accessor(qualifier = "clientId", type = Accessor.Type.GETTER)
	public String getClientId()
	{
		return getPersistenceContext().getPropertyValue(CLIENTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.clientSecret</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the clientSecret - Client Secret
	 */
	@Accessor(qualifier = "clientSecret", type = Accessor.Type.GETTER)
	public String getClientSecret()
	{
		return getPersistenceContext().getPropertyValue(CLIENTSECRET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.disabled</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the disabled - Client disabled
	 */
	@Accessor(qualifier = "disabled", type = Accessor.Type.GETTER)
	public Boolean getDisabled()
	{
		return getPersistenceContext().getPropertyValue(DISABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.refreshTokenValiditySeconds</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the refreshTokenValiditySeconds - Set of authorities granted to client
	 */
	@Accessor(qualifier = "refreshTokenValiditySeconds", type = Accessor.Type.GETTER)
	public Integer getRefreshTokenValiditySeconds()
	{
		return getPersistenceContext().getPropertyValue(REFRESHTOKENVALIDITYSECONDS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.registeredRedirectUri</code> attribute defined at extension <code>oauth2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the registeredRedirectUri - Set of redirect Uri for client
	 */
	@Accessor(qualifier = "registeredRedirectUri", type = Accessor.Type.GETTER)
	public Set<String> getRegisteredRedirectUri()
	{
		return getPersistenceContext().getPropertyValue(REGISTEREDREDIRECTURI);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.resourceIds</code> attribute defined at extension <code>oauth2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the resourceIds - Set of Resource Id's
	 */
	@Accessor(qualifier = "resourceIds", type = Accessor.Type.GETTER)
	public Set<String> getResourceIds()
	{
		return getPersistenceContext().getPropertyValue(RESOURCEIDS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.scope</code> attribute defined at extension <code>oauth2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the scope - Set of client scopes
	 */
	@Accessor(qualifier = "scope", type = Accessor.Type.GETTER)
	public Set<String> getScope()
	{
		return getPersistenceContext().getPropertyValue(SCOPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.accessTokenValiditySeconds</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the accessTokenValiditySeconds - Set of authorities granted to client
	 */
	@Accessor(qualifier = "accessTokenValiditySeconds", type = Accessor.Type.SETTER)
	public void setAccessTokenValiditySeconds(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ACCESSTOKENVALIDITYSECONDS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.authorities</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the authorities - Set of authorities granted to client
	 */
	@Accessor(qualifier = "authorities", type = Accessor.Type.SETTER)
	public void setAuthorities(final Set<String> value)
	{
		getPersistenceContext().setPropertyValue(AUTHORITIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.authorizedGrantTypes</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the authorizedGrantTypes - Set of grant types for client
	 */
	@Accessor(qualifier = "authorizedGrantTypes", type = Accessor.Type.SETTER)
	public void setAuthorizedGrantTypes(final Set<String> value)
	{
		getPersistenceContext().setPropertyValue(AUTHORIZEDGRANTTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.autoApprove</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the autoApprove - Set of auto approve scopes of client
	 */
	@Accessor(qualifier = "autoApprove", type = Accessor.Type.SETTER)
	public void setAutoApprove(final Set<String> value)
	{
		getPersistenceContext().setPropertyValue(AUTOAPPROVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>OAuthClientDetails.clientId</code> attribute defined at extension <code>oauth2</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the clientId - Client Id
	 */
	@Accessor(qualifier = "clientId", type = Accessor.Type.SETTER)
	public void setClientId(final String value)
	{
		getPersistenceContext().setPropertyValue(CLIENTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.clientSecret</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the clientSecret - Client Secret
	 */
	@Accessor(qualifier = "clientSecret", type = Accessor.Type.SETTER)
	public void setClientSecret(final String value)
	{
		getPersistenceContext().setPropertyValue(CLIENTSECRET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.disabled</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the disabled - Client disabled
	 */
	@Accessor(qualifier = "disabled", type = Accessor.Type.SETTER)
	public void setDisabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DISABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.refreshTokenValiditySeconds</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the refreshTokenValiditySeconds - Set of authorities granted to client
	 */
	@Accessor(qualifier = "refreshTokenValiditySeconds", type = Accessor.Type.SETTER)
	public void setRefreshTokenValiditySeconds(final Integer value)
	{
		getPersistenceContext().setPropertyValue(REFRESHTOKENVALIDITYSECONDS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.registeredRedirectUri</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the registeredRedirectUri - Set of redirect Uri for client
	 */
	@Accessor(qualifier = "registeredRedirectUri", type = Accessor.Type.SETTER)
	public void setRegisteredRedirectUri(final Set<String> value)
	{
		getPersistenceContext().setPropertyValue(REGISTEREDREDIRECTURI, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.resourceIds</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the resourceIds - Set of Resource Id's
	 */
	@Accessor(qualifier = "resourceIds", type = Accessor.Type.SETTER)
	public void setResourceIds(final Set<String> value)
	{
		getPersistenceContext().setPropertyValue(RESOURCEIDS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OAuthClientDetails.scope</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the scope - Set of client scopes
	 */
	@Accessor(qualifier = "scope", type = Accessor.Type.SETTER)
	public void setScope(final Set<String> value)
	{
		getPersistenceContext().setPropertyValue(SCOPE, value);
	}
	
}
