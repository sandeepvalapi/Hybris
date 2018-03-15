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
package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ImportBatchContent first defined at extension impex.
 */
@SuppressWarnings("all")
public class ImportBatchContentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ImportBatchContent";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImportBatchContent.code</code> attribute defined at extension <code>impex</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImportBatchContent.content</code> attribute defined at extension <code>impex</code>. */
	public static final String CONTENT = "content";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImportBatchContentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImportBatchContentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ImportBatchContent</code> at extension <code>impex</code>
	 * @param _content initial attribute declared by type <code>ImportBatchContent</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ImportBatchContentModel(final String _code, final String _content)
	{
		super();
		setCode(_code);
		setContent(_content);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ImportBatchContent</code> at extension <code>impex</code>
	 * @param _content initial attribute declared by type <code>ImportBatchContent</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ImportBatchContentModel(final String _code, final String _content, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setContent(_content);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImportBatchContent.code</code> attribute defined at extension <code>impex</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImportBatchContent.content</code> attribute defined at extension <code>impex</code>. 
	 * @return the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent()
	{
		return getPersistenceContext().getPropertyValue(CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ImportBatchContent.code</code> attribute defined at extension <code>impex</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ImportBatchContent.content</code> attribute defined at extension <code>impex</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value)
	{
		getPersistenceContext().setPropertyValue(CONTENT, value);
	}
	
}
