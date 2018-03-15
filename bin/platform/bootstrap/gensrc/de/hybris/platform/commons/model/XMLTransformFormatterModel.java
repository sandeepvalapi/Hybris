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
package de.hybris.platform.commons.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commons.model.MediaFormatterModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type XMLTransformFormatter first defined at extension commons.
 */
@SuppressWarnings("all")
public class XMLTransformFormatterModel extends MediaFormatterModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "XMLTransformFormatter";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public XMLTransformFormatterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public XMLTransformFormatterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Formatter</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _inputMimeType initial attribute declared by type <code>MediaFormatter</code> at extension <code>commons</code>
	 * @param _outputMimeType initial attribute declared by type <code>Formatter</code> at extension <code>commons</code>
	 */
	@Deprecated
	public XMLTransformFormatterModel(final CatalogVersionModel _catalogVersion, final String _code, final String _inputMimeType, final String _outputMimeType)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setInputMimeType(_inputMimeType);
		setOutputMimeType(_outputMimeType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Formatter</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _inputMimeType initial attribute declared by type <code>MediaFormatter</code> at extension <code>commons</code>
	 * @param _outputMimeType initial attribute declared by type <code>Formatter</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public XMLTransformFormatterModel(final CatalogVersionModel _catalogVersion, final String _code, final String _inputMimeType, final String _outputMimeType, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setInputMimeType(_inputMimeType);
		setOutputMimeType(_outputMimeType);
		setOwner(_owner);
	}
	
	
}
