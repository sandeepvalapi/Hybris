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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CockpitUIComponentConfiguration first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitUIComponentConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CockpitUIComponentConfiguration";
	
	/**<i>Generated relation code constant for relation <code>Principal2CockpitUIComponentConfigurationRelation</code> defining source attribute <code>principal</code> in extension <code>cockpit</code>.</i>*/
	public static final String _PRINCIPAL2COCKPITUICOMPONENTCONFIGURATIONRELATION = "Principal2CockpitUIComponentConfigurationRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.factoryBean</code> attribute defined at extension <code>cockpit</code>. */
	public static final String FACTORYBEAN = "factoryBean";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.code</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.objectTemplateCode</code> attribute defined at extension <code>cockpit</code>. */
	public static final String OBJECTTEMPLATECODE = "objectTemplateCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.media</code> attribute defined at extension <code>cockpit</code>. */
	public static final String MEDIA = "media";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.principal</code> attribute defined at extension <code>cockpit</code>. */
	public static final String PRINCIPAL = "principal";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitUIComponentConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitUIComponentConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _factoryBean initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _media initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _objectTemplateCode initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitUIComponentConfigurationModel(final String _code, final String _factoryBean, final MediaModel _media, final String _objectTemplateCode)
	{
		super();
		setCode(_code);
		setFactoryBean(_factoryBean);
		setMedia(_media);
		setObjectTemplateCode(_objectTemplateCode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _factoryBean initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _media initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _objectTemplateCode initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CockpitUIComponentConfigurationModel(final String _code, final String _factoryBean, final MediaModel _media, final String _objectTemplateCode, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setFactoryBean(_factoryBean);
		setMedia(_media);
		setObjectTemplateCode(_objectTemplateCode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.code</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the code - Code of configuration
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.factoryBean</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the factoryBean - ID of Spring bean that implements de.hybris.platform.cockpit.services.config.UIComponentConfigurationFactory
	 */
	@Accessor(qualifier = "factoryBean", type = Accessor.Type.GETTER)
	public String getFactoryBean()
	{
		return getPersistenceContext().getPropertyValue(FACTORYBEAN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.media</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.GETTER)
	public MediaModel getMedia()
	{
		return getPersistenceContext().getPropertyValue(MEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.objectTemplateCode</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the objectTemplateCode - Code of Object Template this UIConfiguration is associated with
	 */
	@Accessor(qualifier = "objectTemplateCode", type = Accessor.Type.GETTER)
	public String getObjectTemplateCode()
	{
		return getPersistenceContext().getPropertyValue(OBJECTTEMPLATECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.principal</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the principal
	 */
	@Accessor(qualifier = "principal", type = Accessor.Type.GETTER)
	public PrincipalModel getPrincipal()
	{
		return getPersistenceContext().getPropertyValue(PRINCIPAL);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.code</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the code - Code of configuration
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.factoryBean</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the factoryBean - ID of Spring bean that implements de.hybris.platform.cockpit.services.config.UIComponentConfigurationFactory
	 */
	@Accessor(qualifier = "factoryBean", type = Accessor.Type.SETTER)
	public void setFactoryBean(final String value)
	{
		getPersistenceContext().setPropertyValue(FACTORYBEAN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.media</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.SETTER)
	public void setMedia(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(MEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.objectTemplateCode</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the objectTemplateCode - Code of Object Template this UIConfiguration is associated with
	 */
	@Accessor(qualifier = "objectTemplateCode", type = Accessor.Type.SETTER)
	public void setObjectTemplateCode(final String value)
	{
		getPersistenceContext().setPropertyValue(OBJECTTEMPLATECODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.principal</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the principal
	 */
	@Accessor(qualifier = "principal", type = Accessor.Type.SETTER)
	public void setPrincipal(final PrincipalModel value)
	{
		getPersistenceContext().setPropertyValue(PRINCIPAL, value);
	}
	
}
