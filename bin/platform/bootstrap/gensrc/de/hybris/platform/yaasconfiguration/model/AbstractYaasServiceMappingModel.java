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
import de.hybris.platform.yaasconfiguration.model.YaasClientCredentialModel;
import de.hybris.platform.yaasconfiguration.model.YaasServiceModel;

/**
 * Generated model class for type AbstractYaasServiceMapping first defined at extension yaasconfiguration.
 */
@SuppressWarnings("all")
public class AbstractYaasServiceMappingModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractYaasServiceMapping";
	
	/**<i>Generated relation code constant for relation <code>ServiceYaasMappingRelation</code> defining source attribute <code>yaasService</code> in extension <code>yaasconfiguration</code>.</i>*/
	public static final String _SERVICEYAASMAPPINGRELATION = "ServiceYaasMappingRelation";
	
	/**<i>Generated relation code constant for relation <code>CredentialYaasMappingRelation</code> defining source attribute <code>yaasClientCredential</code> in extension <code>yaasconfiguration</code>.</i>*/
	public static final String _CREDENTIALYAASMAPPINGRELATION = "CredentialYaasMappingRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractYaasServiceMapping.yaasService</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String YAASSERVICE = "yaasService";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractYaasServiceMapping.yaasClientCredential</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String YAASCLIENTCREDENTIAL = "yaasClientCredential";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractYaasServiceMappingModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractYaasServiceMappingModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _yaasClientCredential initial attribute declared by type <code>AbstractYaasServiceMapping</code> at extension <code>yaasconfiguration</code>
	 * @param _yaasService initial attribute declared by type <code>AbstractYaasServiceMapping</code> at extension <code>yaasconfiguration</code>
	 */
	@Deprecated
	public AbstractYaasServiceMappingModel(final YaasClientCredentialModel _yaasClientCredential, final YaasServiceModel _yaasService)
	{
		super();
		setYaasClientCredential(_yaasClientCredential);
		setYaasService(_yaasService);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _yaasClientCredential initial attribute declared by type <code>AbstractYaasServiceMapping</code> at extension <code>yaasconfiguration</code>
	 * @param _yaasService initial attribute declared by type <code>AbstractYaasServiceMapping</code> at extension <code>yaasconfiguration</code>
	 */
	@Deprecated
	public AbstractYaasServiceMappingModel(final ItemModel _owner, final YaasClientCredentialModel _yaasClientCredential, final YaasServiceModel _yaasService)
	{
		super();
		setOwner(_owner);
		setYaasClientCredential(_yaasClientCredential);
		setYaasService(_yaasService);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractYaasServiceMapping.yaasClientCredential</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the yaasClientCredential
	 */
	@Accessor(qualifier = "yaasClientCredential", type = Accessor.Type.GETTER)
	public YaasClientCredentialModel getYaasClientCredential()
	{
		return getPersistenceContext().getPropertyValue(YAASCLIENTCREDENTIAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractYaasServiceMapping.yaasService</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the yaasService
	 */
	@Accessor(qualifier = "yaasService", type = Accessor.Type.GETTER)
	public YaasServiceModel getYaasService()
	{
		return getPersistenceContext().getPropertyValue(YAASSERVICE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractYaasServiceMapping.yaasClientCredential</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the yaasClientCredential
	 */
	@Accessor(qualifier = "yaasClientCredential", type = Accessor.Type.SETTER)
	public void setYaasClientCredential(final YaasClientCredentialModel value)
	{
		getPersistenceContext().setPropertyValue(YAASCLIENTCREDENTIAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractYaasServiceMapping.yaasService</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the yaasService
	 */
	@Accessor(qualifier = "yaasService", type = Accessor.Type.SETTER)
	public void setYaasService(final YaasServiceModel value)
	{
		getPersistenceContext().setPropertyValue(YAASSERVICE, value);
	}
	
}
