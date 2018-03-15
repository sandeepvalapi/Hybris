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
import de.hybris.platform.commons.model.translator.JaloVelocityRendererModel;
import de.hybris.platform.commons.model.translator.ParserPropertyModel;
import de.hybris.platform.commons.model.translator.RenderersPropertyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type JaloTranslatorConfiguration first defined at extension commons.
 */
@SuppressWarnings("all")
public class JaloTranslatorConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "JaloTranslatorConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloTranslatorConfiguration.code</code> attribute defined at extension <code>commons</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloTranslatorConfiguration.renderers</code> attribute defined at extension <code>commons</code>. */
	public static final String RENDERERS = "renderers";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloTranslatorConfiguration.renderersProperties</code> attribute defined at extension <code>commons</code>. */
	public static final String RENDERERSPROPERTIES = "renderersProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloTranslatorConfiguration.parserProperties</code> attribute defined at extension <code>commons</code>. */
	public static final String PARSERPROPERTIES = "parserProperties";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JaloTranslatorConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JaloTranslatorConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>JaloTranslatorConfiguration</code> at extension <code>commons</code>
	 */
	@Deprecated
	public JaloTranslatorConfigurationModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>JaloTranslatorConfiguration</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public JaloTranslatorConfigurationModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloTranslatorConfiguration.code</code> attribute defined at extension <code>commons</code>. 
	 * @return the code - unique item identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloTranslatorConfiguration.parserProperties</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the parserProperties
	 */
	@Accessor(qualifier = "parserProperties", type = Accessor.Type.GETTER)
	public List<ParserPropertyModel> getParserProperties()
	{
		return getPersistenceContext().getPropertyValue(PARSERPROPERTIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloTranslatorConfiguration.renderers</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the renderers
	 */
	@Accessor(qualifier = "renderers", type = Accessor.Type.GETTER)
	public List<JaloVelocityRendererModel> getRenderers()
	{
		return getPersistenceContext().getPropertyValue(RENDERERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloTranslatorConfiguration.renderersProperties</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the renderersProperties
	 */
	@Accessor(qualifier = "renderersProperties", type = Accessor.Type.GETTER)
	public List<RenderersPropertyModel> getRenderersProperties()
	{
		return getPersistenceContext().getPropertyValue(RENDERERSPROPERTIES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloTranslatorConfiguration.code</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the code - unique item identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloTranslatorConfiguration.parserProperties</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the parserProperties
	 */
	@Accessor(qualifier = "parserProperties", type = Accessor.Type.SETTER)
	public void setParserProperties(final List<ParserPropertyModel> value)
	{
		getPersistenceContext().setPropertyValue(PARSERPROPERTIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloTranslatorConfiguration.renderers</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the renderers
	 */
	@Accessor(qualifier = "renderers", type = Accessor.Type.SETTER)
	public void setRenderers(final List<JaloVelocityRendererModel> value)
	{
		getPersistenceContext().setPropertyValue(RENDERERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloTranslatorConfiguration.renderersProperties</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the renderersProperties
	 */
	@Accessor(qualifier = "renderersProperties", type = Accessor.Type.SETTER)
	public void setRenderersProperties(final List<RenderersPropertyModel> value)
	{
		getPersistenceContext().setPropertyValue(RENDERERSPROPERTIES, value);
	}
	
}
