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
import java.util.Map;
import java.util.Set;

/**
 * Generated model class for type YaasService first defined at extension yaasconfiguration.
 */
@SuppressWarnings("all")
public class YaasServiceModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "YaasService";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasService.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String IDENTIFIER = "identifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasService.serviceURL</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String SERVICEURL = "serviceURL";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasService.serviceScope</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String SERVICESCOPE = "serviceScope";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasService.additionalConfigurations</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String ADDITIONALCONFIGURATIONS = "additionalConfigurations";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasService.yaasServiceMappings</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String YAASSERVICEMAPPINGS = "yaasServiceMappings";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public YaasServiceModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public YaasServiceModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _identifier initial attribute declared by type <code>YaasService</code> at extension <code>yaasconfiguration</code>
	 * @param _serviceScope initial attribute declared by type <code>YaasService</code> at extension <code>yaasconfiguration</code>
	 * @param _serviceURL initial attribute declared by type <code>YaasService</code> at extension <code>yaasconfiguration</code>
	 */
	@Deprecated
	public YaasServiceModel(final String _identifier, final String _serviceScope, final String _serviceURL)
	{
		super();
		setIdentifier(_identifier);
		setServiceScope(_serviceScope);
		setServiceURL(_serviceURL);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _identifier initial attribute declared by type <code>YaasService</code> at extension <code>yaasconfiguration</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _serviceScope initial attribute declared by type <code>YaasService</code> at extension <code>yaasconfiguration</code>
	 * @param _serviceURL initial attribute declared by type <code>YaasService</code> at extension <code>yaasconfiguration</code>
	 */
	@Deprecated
	public YaasServiceModel(final String _identifier, final ItemModel _owner, final String _serviceScope, final String _serviceURL)
	{
		super();
		setIdentifier(_identifier);
		setOwner(_owner);
		setServiceScope(_serviceScope);
		setServiceURL(_serviceURL);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasService.additionalConfigurations</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the additionalConfigurations
	 */
	@Accessor(qualifier = "additionalConfigurations", type = Accessor.Type.GETTER)
	public Map<String,String> getAdditionalConfigurations()
	{
		return getPersistenceContext().getPropertyValue(ADDITIONALCONFIGURATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasService.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.GETTER)
	public String getIdentifier()
	{
		return getPersistenceContext().getPropertyValue(IDENTIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasService.serviceScope</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the serviceScope
	 */
	@Accessor(qualifier = "serviceScope", type = Accessor.Type.GETTER)
	public String getServiceScope()
	{
		return getPersistenceContext().getPropertyValue(SERVICESCOPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasService.serviceURL</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the serviceURL
	 */
	@Accessor(qualifier = "serviceURL", type = Accessor.Type.GETTER)
	public String getServiceURL()
	{
		return getPersistenceContext().getPropertyValue(SERVICEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasService.yaasServiceMappings</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the yaasServiceMappings
	 */
	@Accessor(qualifier = "yaasServiceMappings", type = Accessor.Type.GETTER)
	public Set<AbstractYaasServiceMappingModel> getYaasServiceMappings()
	{
		return getPersistenceContext().getPropertyValue(YAASSERVICEMAPPINGS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasService.additionalConfigurations</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the additionalConfigurations
	 */
	@Accessor(qualifier = "additionalConfigurations", type = Accessor.Type.SETTER)
	public void setAdditionalConfigurations(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(ADDITIONALCONFIGURATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasService.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.SETTER)
	public void setIdentifier(final String value)
	{
		getPersistenceContext().setPropertyValue(IDENTIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasService.serviceScope</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the serviceScope
	 */
	@Accessor(qualifier = "serviceScope", type = Accessor.Type.SETTER)
	public void setServiceScope(final String value)
	{
		getPersistenceContext().setPropertyValue(SERVICESCOPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasService.serviceURL</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the serviceURL
	 */
	@Accessor(qualifier = "serviceURL", type = Accessor.Type.SETTER)
	public void setServiceURL(final String value)
	{
		getPersistenceContext().setPropertyValue(SERVICEURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasService.yaasServiceMappings</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the yaasServiceMappings
	 */
	@Accessor(qualifier = "yaasServiceMappings", type = Accessor.Type.SETTER)
	public void setYaasServiceMappings(final Set<AbstractYaasServiceMappingModel> value)
	{
		getPersistenceContext().setPropertyValue(YAASSERVICEMAPPINGS, value);
	}
	
}
