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
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

/**
 * Generated model class for type OpenIDClientDetails first defined at extension oauth2.
 */
@SuppressWarnings("all")
public class OpenIDClientDetailsModel extends OAuthClientDetailsModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OpenIDClientDetails";
	
	/** <i>Generated constant</i> - Attribute key of <code>OpenIDClientDetails.externalScopeClaimName</code> attribute defined at extension <code>oauth2</code>. */
	public static final String EXTERNALSCOPECLAIMNAME = "externalScopeClaimName";
	
	/** <i>Generated constant</i> - Attribute key of <code>OpenIDClientDetails.issuer</code> attribute defined at extension <code>oauth2</code>. */
	public static final String ISSUER = "issuer";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OpenIDClientDetailsModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OpenIDClientDetailsModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clientId initial attribute declared by type <code>OAuthClientDetails</code> at extension <code>oauth2</code>
	 * @param _issuer initial attribute declared by type <code>OpenIDClientDetails</code> at extension <code>oauth2</code>
	 */
	@Deprecated
	public OpenIDClientDetailsModel(final String _clientId, final String _issuer)
	{
		super();
		setClientId(_clientId);
		setIssuer(_issuer);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clientId initial attribute declared by type <code>OAuthClientDetails</code> at extension <code>oauth2</code>
	 * @param _issuer initial attribute declared by type <code>OpenIDClientDetails</code> at extension <code>oauth2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public OpenIDClientDetailsModel(final String _clientId, final String _issuer, final ItemModel _owner)
	{
		super();
		setClientId(_clientId);
		setIssuer(_issuer);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OpenIDClientDetails.externalScopeClaimName</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the externalScopeClaimName - External Scope Claim Name (if not null will be used in the JWT)
	 */
	@Accessor(qualifier = "externalScopeClaimName", type = Accessor.Type.GETTER)
	public String getExternalScopeClaimName()
	{
		return getPersistenceContext().getPropertyValue(EXTERNALSCOPECLAIMNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OpenIDClientDetails.issuer</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the issuer - Issuer Name
	 */
	@Accessor(qualifier = "issuer", type = Accessor.Type.GETTER)
	public String getIssuer()
	{
		return getPersistenceContext().getPropertyValue(ISSUER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OpenIDClientDetails.externalScopeClaimName</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the externalScopeClaimName - External Scope Claim Name (if not null will be used in the JWT)
	 */
	@Accessor(qualifier = "externalScopeClaimName", type = Accessor.Type.SETTER)
	public void setExternalScopeClaimName(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTERNALSCOPECLAIMNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OpenIDClientDetails.issuer</code> attribute defined at extension <code>oauth2</code>. 
	 *  
	 * @param value the issuer - Issuer Name
	 */
	@Accessor(qualifier = "issuer", type = Accessor.Type.SETTER)
	public void setIssuer(final String value)
	{
		getPersistenceContext().setPropertyValue(ISSUER, value);
	}
	
}
