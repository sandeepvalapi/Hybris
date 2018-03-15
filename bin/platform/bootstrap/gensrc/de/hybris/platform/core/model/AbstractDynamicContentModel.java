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
package de.hybris.platform.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractDynamicContent first defined at extension core.
 */
@SuppressWarnings("all")
public class AbstractDynamicContentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractDynamicContent";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.active</code> attribute defined at extension <code>core</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.checksum</code> attribute defined at extension <code>core</code>. */
	public static final String CHECKSUM = "checksum";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.content</code> attribute defined at extension <code>core</code>. */
	public static final String CONTENT = "content";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.version</code> attribute defined at extension <code>core</code>. */
	public static final String VERSION = "version";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractDynamicContentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractDynamicContentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractDynamicContentModel(final String _code, final String _content)
	{
		super();
		setCode(_code);
		setContent(_content);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractDynamicContentModel(final String _code, final String _content, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setContent(_content);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.active</code> attribute defined at extension <code>core</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.checksum</code> attribute defined at extension <code>core</code>. 
	 * @return the checksum
	 */
	@Accessor(qualifier = "checksum", type = Accessor.Type.GETTER)
	public String getChecksum()
	{
		return getPersistenceContext().getPropertyValue(CHECKSUM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.content</code> attribute defined at extension <code>core</code>. 
	 * @return the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent()
	{
		return getPersistenceContext().getPropertyValue(CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.version</code> attribute defined at extension <code>core</code>. 
	 * @return the version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public Long getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractDynamicContent.active</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractDynamicContent.checksum</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the checksum
	 */
	@Accessor(qualifier = "checksum", type = Accessor.Type.SETTER)
	public void setChecksum(final String value)
	{
		getPersistenceContext().setPropertyValue(CHECKSUM, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractDynamicContent.code</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractDynamicContent.content</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value)
	{
		getPersistenceContext().setPropertyValue(CONTENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractDynamicContent.version</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final Long value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
}
