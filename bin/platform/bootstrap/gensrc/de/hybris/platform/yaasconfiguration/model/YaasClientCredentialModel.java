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
package de.hybris.platform.yaasconfiguration.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.yaasconfiguration.model.AbstractYaasServiceMappingModel;
import de.hybris.platform.yaasconfiguration.model.YaasProjectModel;
import java.util.Set;

/**
 * Generated model class for type YaasClientCredential first defined at extension yaasconfiguration.
 */
@SuppressWarnings("all")
public class YaasClientCredentialModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "YaasClientCredential";
	
	/**<i>Generated relation code constant for relation <code>YaasProjectClientCredentialRelation</code> defining source attribute <code>yaasProject</code> in extension <code>yaasconfiguration</code>.</i>*/
	public static final String _YAASPROJECTCLIENTCREDENTIALRELATION = "YaasProjectClientCredentialRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasClientCredential.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String IDENTIFIER = "identifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasClientCredential.clientId</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String CLIENTID = "clientId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasClientCredential.clientSecret</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String CLIENTSECRET = "clientSecret";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasClientCredential.pubsubClient</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String PUBSUBCLIENT = "pubsubClient";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasClientCredential.oauthURL</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String OAUTHURL = "oauthURL";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasClientCredential.yaasProject</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String YAASPROJECT = "yaasProject";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasClientCredential.yaasServiceMappings</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String YAASSERVICEMAPPINGS = "yaasServiceMappings";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public YaasClientCredentialModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public YaasClientCredentialModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clientId initial attribute declared by type <code>YaasClientCredential</code> at extension <code>yaasconfiguration</code>
	 * @param _clientSecret initial attribute declared by type <code>YaasClientCredential</code> at extension <code>yaasconfiguration</code>
	 * @param _identifier initial attribute declared by type <code>YaasClientCredential</code> at extension <code>yaasconfiguration</code>
	 */
	@Deprecated
	public YaasClientCredentialModel(final String _clientId, final String _clientSecret, final String _identifier)
	{
		super();
		setClientId(_clientId);
		setClientSecret(_clientSecret);
		setIdentifier(_identifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clientId initial attribute declared by type <code>YaasClientCredential</code> at extension <code>yaasconfiguration</code>
	 * @param _clientSecret initial attribute declared by type <code>YaasClientCredential</code> at extension <code>yaasconfiguration</code>
	 * @param _identifier initial attribute declared by type <code>YaasClientCredential</code> at extension <code>yaasconfiguration</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public YaasClientCredentialModel(final String _clientId, final String _clientSecret, final String _identifier, final ItemModel _owner)
	{
		super();
		setClientId(_clientId);
		setClientSecret(_clientSecret);
		setIdentifier(_identifier);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasClientCredential.clientId</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the clientId
	 */
	@Accessor(qualifier = "clientId", type = Accessor.Type.GETTER)
	public String getClientId()
	{
		return getPersistenceContext().getPropertyValue(CLIENTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasClientCredential.clientSecret</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the clientSecret
	 */
	@Accessor(qualifier = "clientSecret", type = Accessor.Type.GETTER)
	public String getClientSecret()
	{
		return getPersistenceContext().getPropertyValue(CLIENTSECRET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasClientCredential.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.GETTER)
	public String getIdentifier()
	{
		return getPersistenceContext().getPropertyValue(IDENTIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasClientCredential.oauthURL</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the oauthURL
	 */
	@Accessor(qualifier = "oauthURL", type = Accessor.Type.GETTER)
	public String getOauthURL()
	{
		return getPersistenceContext().getPropertyValue(OAUTHURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasClientCredential.pubsubClient</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the pubsubClient
	 */
	@Accessor(qualifier = "pubsubClient", type = Accessor.Type.GETTER)
	public String getPubsubClient()
	{
		return getPersistenceContext().getPropertyValue(PUBSUBCLIENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasClientCredential.yaasProject</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the yaasProject
	 */
	@Accessor(qualifier = "yaasProject", type = Accessor.Type.GETTER)
	public YaasProjectModel getYaasProject()
	{
		return getPersistenceContext().getPropertyValue(YAASPROJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasClientCredential.yaasServiceMappings</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the yaasServiceMappings
	 */
	@Accessor(qualifier = "yaasServiceMappings", type = Accessor.Type.GETTER)
	public Set<AbstractYaasServiceMappingModel> getYaasServiceMappings()
	{
		return getPersistenceContext().getPropertyValue(YAASSERVICEMAPPINGS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasClientCredential.clientId</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the clientId
	 */
	@Accessor(qualifier = "clientId", type = Accessor.Type.SETTER)
	public void setClientId(final String value)
	{
		getPersistenceContext().setPropertyValue(CLIENTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasClientCredential.clientSecret</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the clientSecret
	 */
	@Accessor(qualifier = "clientSecret", type = Accessor.Type.SETTER)
	public void setClientSecret(final String value)
	{
		getPersistenceContext().setPropertyValue(CLIENTSECRET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasClientCredential.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.SETTER)
	public void setIdentifier(final String value)
	{
		getPersistenceContext().setPropertyValue(IDENTIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasClientCredential.oauthURL</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the oauthURL
	 */
	@Accessor(qualifier = "oauthURL", type = Accessor.Type.SETTER)
	public void setOauthURL(final String value)
	{
		getPersistenceContext().setPropertyValue(OAUTHURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasClientCredential.pubsubClient</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the pubsubClient
	 */
	@Accessor(qualifier = "pubsubClient", type = Accessor.Type.SETTER)
	public void setPubsubClient(final String value)
	{
		getPersistenceContext().setPropertyValue(PUBSUBCLIENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasClientCredential.yaasProject</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the yaasProject
	 */
	@Accessor(qualifier = "yaasProject", type = Accessor.Type.SETTER)
	public void setYaasProject(final YaasProjectModel value)
	{
		getPersistenceContext().setPropertyValue(YAASPROJECT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasClientCredential.yaasServiceMappings</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the yaasServiceMappings
	 */
	@Accessor(qualifier = "yaasServiceMappings", type = Accessor.Type.SETTER)
	public void setYaasServiceMappings(final Set<AbstractYaasServiceMappingModel> value)
	{
		getPersistenceContext().setPropertyValue(YAASSERVICEMAPPINGS, value);
	}
	
}
