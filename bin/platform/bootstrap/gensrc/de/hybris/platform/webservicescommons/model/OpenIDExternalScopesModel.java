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
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.webservicescommons.model.OpenIDClientDetailsModel;
import java.util.Collection;
import java.util.Set;

/**
 * Generated model class for type OpenIDExternalScopes first defined at extension oauth2.
 */
@SuppressWarnings("all")
public class OpenIDExternalScopesModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OpenIDExternalScopes";
	
	/** <i>Generated constant</i> - Attribute key of <code>OpenIDExternalScopes.code</code> attribute defined at extension <code>oauth2</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>OpenIDExternalScopes.clientDetailsId</code> attribute defined at extension <code>oauth2</code>. */
	public static final String CLIENTDETAILSID = "clientDetailsId";
	
	/** <i>Generated constant</i> - Attribute key of <code>OpenIDExternalScopes.permittedPrincipals</code> attribute defined at extension <code>oauth2</code>. */
	public static final String PERMITTEDPRINCIPALS = "permittedPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>OpenIDExternalScopes.scope</code> attribute defined at extension <code>oauth2</code>. */
	public static final String SCOPE = "scope";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OpenIDExternalScopesModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OpenIDExternalScopesModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clientDetailsId initial attribute declared by type <code>OpenIDExternalScopes</code> at extension <code>oauth2</code>
	 * @param _code initial attribute declared by type <code>OpenIDExternalScopes</code> at extension <code>oauth2</code>
	 */
	@Deprecated
	public OpenIDExternalScopesModel(final OpenIDClientDetailsModel _clientDetailsId, final String _code)
	{
		super();
		setClientDetailsId(_clientDetailsId);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clientDetailsId initial attribute declared by type <code>OpenIDExternalScopes</code> at extension <code>oauth2</code>
	 * @param _code initial attribute declared by type <code>OpenIDExternalScopes</code> at extension <code>oauth2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public OpenIDExternalScopesModel(final OpenIDClientDetailsModel _clientDetailsId, final String _code, final ItemModel _owner)
	{
		super();
		setClientDetailsId(_clientDetailsId);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OpenIDExternalScopes.clientDetailsId</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the clientDetailsId - Client Details Id
	 */
	@Accessor(qualifier = "clientDetailsId", type = Accessor.Type.GETTER)
	public OpenIDClientDetailsModel getClientDetailsId()
	{
		return getPersistenceContext().getPropertyValue(CLIENTDETAILSID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OpenIDExternalScopes.code</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OpenIDExternalScopes.permittedPrincipals</code> attribute defined at extension <code>oauth2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the permittedPrincipals - Collection of Principals that are assigned to this set od scopes
	 */
	@Accessor(qualifier = "permittedPrincipals", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getPermittedPrincipals()
	{
		return getPersistenceContext().getPropertyValue(PERMITTEDPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OpenIDExternalScopes.scope</code> attribute defined at extension <code>oauth2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the scope - Set of scopes assigned to given Principals
	 */
	@Accessor(qualifier = "scope", type = Accessor.Type.GETTER)
	public Set<String> getScope()
	{
		return getPersistenceContext().getPropertyValue(SCOPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OpenIDExternalScopes.clientDetailsId</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the clientDetailsId - Client Details Id
	 */
	@Accessor(qualifier = "clientDetailsId", type = Accessor.Type.SETTER)
	public void setClientDetailsId(final OpenIDClientDetailsModel value)
	{
		getPersistenceContext().setPropertyValue(CLIENTDETAILSID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OpenIDExternalScopes.code</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OpenIDExternalScopes.permittedPrincipals</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the permittedPrincipals - Collection of Principals that are assigned to this set od scopes
	 */
	@Accessor(qualifier = "permittedPrincipals", type = Accessor.Type.SETTER)
	public void setPermittedPrincipals(final Collection<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(PERMITTEDPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OpenIDExternalScopes.scope</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the scope - Set of scopes assigned to given Principals
	 */
	@Accessor(qualifier = "scope", type = Accessor.Type.SETTER)
	public void setScope(final Set<String> value)
	{
		getPersistenceContext().setPropertyValue(SCOPE, value);
	}
	
}
