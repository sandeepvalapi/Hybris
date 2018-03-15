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
package de.hybris.platform.commons.model.translator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.model.translator.JaloTranslatorConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type JaloVelocityRenderer first defined at extension commons.
 */
@SuppressWarnings("all")
public class JaloVelocityRendererModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "JaloVelocityRenderer";
	
	/**<i>Generated relation code constant for relation <code>TranslatorConfig2Renderers</code> defining source attribute <code>translatorConfiguration</code> in extension <code>commons</code>.</i>*/
	public static final String _TRANSLATORCONFIG2RENDERERS = "TranslatorConfig2Renderers";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloVelocityRenderer.name</code> attribute defined at extension <code>commons</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloVelocityRenderer.template</code> attribute defined at extension <code>commons</code>. */
	public static final String TEMPLATE = "template";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloVelocityRenderer.translatorConfigurationPOS</code> attribute defined at extension <code>commons</code>. */
	public static final String TRANSLATORCONFIGURATIONPOS = "translatorConfigurationPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloVelocityRenderer.translatorConfiguration</code> attribute defined at extension <code>commons</code>. */
	public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JaloVelocityRendererModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JaloVelocityRendererModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>JaloVelocityRenderer</code> at extension <code>commons</code>
	 */
	@Deprecated
	public JaloVelocityRendererModel(final String _name)
	{
		super();
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>JaloVelocityRenderer</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public JaloVelocityRendererModel(final String _name, final ItemModel _owner)
	{
		super();
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloVelocityRenderer.name</code> attribute defined at extension <code>commons</code>. 
	 * @return the name - renderer name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloVelocityRenderer.template</code> attribute defined at extension <code>commons</code>. 
	 * @return the template - velocity template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.GETTER)
	public String getTemplate()
	{
		return getPersistenceContext().getPropertyValue(TEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloVelocityRenderer.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
	 * @return the translatorConfiguration
	 */
	@Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.GETTER)
	public JaloTranslatorConfigurationModel getTranslatorConfiguration()
	{
		return getPersistenceContext().getPropertyValue(TRANSLATORCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloVelocityRenderer.name</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the name - renderer name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloVelocityRenderer.template</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the template - velocity template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.SETTER)
	public void setTemplate(final String value)
	{
		getPersistenceContext().setPropertyValue(TEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloVelocityRenderer.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the translatorConfiguration
	 */
	@Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.SETTER)
	public void setTranslatorConfiguration(final JaloTranslatorConfigurationModel value)
	{
		getPersistenceContext().setPropertyValue(TRANSLATORCONFIGURATION, value);
	}
	
}
