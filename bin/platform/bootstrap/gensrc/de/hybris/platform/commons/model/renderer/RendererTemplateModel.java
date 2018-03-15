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
package de.hybris.platform.commons.model.renderer;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.enums.RendererTypeEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type RendererTemplate first defined at extension commons.
 */
@SuppressWarnings("all")
public class RendererTemplateModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RendererTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>RendererTemplate.code</code> attribute defined at extension <code>commons</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>RendererTemplate.description</code> attribute defined at extension <code>commons</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>RendererTemplate.content</code> attribute defined at extension <code>commons</code>. */
	public static final String CONTENT = "content";
	
	/** <i>Generated constant</i> - Attribute key of <code>RendererTemplate.contextClass</code> attribute defined at extension <code>commons</code>. */
	public static final String CONTEXTCLASS = "contextClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>RendererTemplate.outputMimeType</code> attribute defined at extension <code>commons</code>. */
	public static final String OUTPUTMIMETYPE = "outputMimeType";
	
	/** <i>Generated constant</i> - Attribute key of <code>RendererTemplate.rendererType</code> attribute defined at extension <code>commons</code>. */
	public static final String RENDERERTYPE = "rendererType";
	
	/** <i>Generated constant</i> - Attribute key of <code>RendererTemplate.templateScript</code> attribute defined at extension <code>commons</code>. */
	public static final String TEMPLATESCRIPT = "templateScript";
	
	/** <i>Generated constant</i> - Attribute key of <code>RendererTemplate.contextClassDescription</code> attribute defined at extension <code>commons</code>. */
	public static final String CONTEXTCLASSDESCRIPTION = "contextClassDescription";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RendererTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RendererTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>RendererTemplate</code> at extension <code>commons</code>
	 * @param _rendererType initial attribute declared by type <code>RendererTemplate</code> at extension <code>commons</code>
	 */
	@Deprecated
	public RendererTemplateModel(final String _code, final RendererTypeEnum _rendererType)
	{
		super();
		setCode(_code);
		setRendererType(_rendererType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>RendererTemplate</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _rendererType initial attribute declared by type <code>RendererTemplate</code> at extension <code>commons</code>
	 */
	@Deprecated
	public RendererTemplateModel(final String _code, final ItemModel _owner, final RendererTypeEnum _rendererType)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setRendererType(_rendererType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.code</code> attribute defined at extension <code>commons</code>. 
	 * @return the code - unique item identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.content</code> attribute defined at extension <code>commons</code>. 
	 * @return the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public MediaModel getContent()
	{
		return getContent(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.content</code> attribute defined at extension <code>commons</code>. 
	 * @param loc the value localization key 
	 * @return the content
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public MediaModel getContent(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(CONTENT, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.contextClass</code> attribute defined at extension <code>commons</code>. 
	 * @return the contextClass
	 */
	@Accessor(qualifier = "contextClass", type = Accessor.Type.GETTER)
	public String getContextClass()
	{
		return getPersistenceContext().getPropertyValue(CONTEXTCLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.contextClassDescription</code> attribute defined at extension <code>commons</code>. 
	 * @return the contextClassDescription
	 */
	@Accessor(qualifier = "contextClassDescription", type = Accessor.Type.GETTER)
	public String getContextClassDescription()
	{
		return getPersistenceContext().getPropertyValue(CONTEXTCLASSDESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.description</code> attribute defined at extension <code>commons</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.description</code> attribute defined at extension <code>commons</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.outputMimeType</code> attribute defined at extension <code>commons</code>. 
	 * @return the outputMimeType
	 */
	@Accessor(qualifier = "outputMimeType", type = Accessor.Type.GETTER)
	public String getOutputMimeType()
	{
		return getPersistenceContext().getPropertyValue(OUTPUTMIMETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.rendererType</code> attribute defined at extension <code>commons</code>. 
	 * @return the rendererType
	 */
	@Accessor(qualifier = "rendererType", type = Accessor.Type.GETTER)
	public RendererTypeEnum getRendererType()
	{
		return getPersistenceContext().getPropertyValue(RENDERERTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.templateScript</code> attribute defined at extension <code>commons</code>. 
	 * @return the templateScript
	 */
	@Accessor(qualifier = "templateScript", type = Accessor.Type.GETTER)
	public String getTemplateScript()
	{
		return getTemplateScript(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>RendererTemplate.templateScript</code> attribute defined at extension <code>commons</code>. 
	 * @param loc the value localization key 
	 * @return the templateScript
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "templateScript", type = Accessor.Type.GETTER)
	public String getTemplateScript(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TEMPLATESCRIPT, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.code</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the code - unique item identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.content</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final MediaModel value)
	{
		setContent(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.content</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the content
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final MediaModel value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CONTENT, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.contextClass</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the contextClass
	 */
	@Accessor(qualifier = "contextClass", type = Accessor.Type.SETTER)
	public void setContextClass(final String value)
	{
		getPersistenceContext().setPropertyValue(CONTEXTCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.description</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.description</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.outputMimeType</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the outputMimeType
	 */
	@Accessor(qualifier = "outputMimeType", type = Accessor.Type.SETTER)
	public void setOutputMimeType(final String value)
	{
		getPersistenceContext().setPropertyValue(OUTPUTMIMETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.rendererType</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the rendererType
	 */
	@Accessor(qualifier = "rendererType", type = Accessor.Type.SETTER)
	public void setRendererType(final RendererTypeEnum value)
	{
		getPersistenceContext().setPropertyValue(RENDERERTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.templateScript</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the templateScript
	 */
	@Accessor(qualifier = "templateScript", type = Accessor.Type.SETTER)
	public void setTemplateScript(final String value)
	{
		setTemplateScript(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>RendererTemplate.templateScript</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the templateScript
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "templateScript", type = Accessor.Type.SETTER)
	public void setTemplateScript(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TEMPLATESCRIPT, loc, value);
	}
	
}
