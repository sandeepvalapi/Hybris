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
import de.hybris.platform.yaasconfiguration.model.YaasClientCredentialModel;
import de.hybris.platform.yaasconfiguration.model.YaasServiceModel;

/**
 * Generated model class for type BaseSiteServiceMapping first defined at extension yaasconfiguration.
 */
@SuppressWarnings("all")
public class BaseSiteServiceMappingModel extends AbstractYaasServiceMappingModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BaseSiteServiceMapping";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSiteServiceMapping.baseSite</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String BASESITE = "baseSite";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BaseSiteServiceMappingModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BaseSiteServiceMappingModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>BaseSiteServiceMapping</code> at extension <code>yaasconfiguration</code>
	 * @param _yaasClientCredential initial attribute declared by type <code>AbstractYaasServiceMapping</code> at extension <code>yaasconfiguration</code>
	 * @param _yaasService initial attribute declared by type <code>AbstractYaasServiceMapping</code> at extension <code>yaasconfiguration</code>
	 */
	@Deprecated
	public BaseSiteServiceMappingModel(final String _baseSite, final YaasClientCredentialModel _yaasClientCredential, final YaasServiceModel _yaasService)
	{
		super();
		setBaseSite(_baseSite);
		setYaasClientCredential(_yaasClientCredential);
		setYaasService(_yaasService);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>BaseSiteServiceMapping</code> at extension <code>yaasconfiguration</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _yaasClientCredential initial attribute declared by type <code>AbstractYaasServiceMapping</code> at extension <code>yaasconfiguration</code>
	 * @param _yaasService initial attribute declared by type <code>AbstractYaasServiceMapping</code> at extension <code>yaasconfiguration</code>
	 */
	@Deprecated
	public BaseSiteServiceMappingModel(final String _baseSite, final ItemModel _owner, final YaasClientCredentialModel _yaasClientCredential, final YaasServiceModel _yaasService)
	{
		super();
		setBaseSite(_baseSite);
		setOwner(_owner);
		setYaasClientCredential(_yaasClientCredential);
		setYaasService(_yaasService);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSiteServiceMapping.baseSite</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the baseSite
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public String getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSiteServiceMapping.baseSite</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the baseSite
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final String value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
}
