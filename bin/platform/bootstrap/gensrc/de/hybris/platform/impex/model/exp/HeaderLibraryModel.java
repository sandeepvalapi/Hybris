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
package de.hybris.platform.impex.model.exp;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type HeaderLibrary first defined at extension impex.
 */
@SuppressWarnings("all")
public class HeaderLibraryModel extends ImpExMediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "HeaderLibrary";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public HeaderLibraryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public HeaderLibraryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>ImpExMedia</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _linesToSkip initial attribute declared by type <code>ImpExMedia</code> at extension <code>impex</code>
	 * @param _removeOnSuccess initial attribute declared by type <code>ImpExMedia</code> at extension <code>impex</code>
	 */
	@Deprecated
	public HeaderLibraryModel(final CatalogVersionModel _catalogVersion, final String _code, final int _linesToSkip, final boolean _removeOnSuccess)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setLinesToSkip(_linesToSkip);
		setRemoveOnSuccess(_removeOnSuccess);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>ImpExMedia</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _linesToSkip initial attribute declared by type <code>ImpExMedia</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _removeOnSuccess initial attribute declared by type <code>ImpExMedia</code> at extension <code>impex</code>
	 */
	@Deprecated
	public HeaderLibraryModel(final CatalogVersionModel _catalogVersion, final String _code, final int _linesToSkip, final ItemModel _owner, final boolean _removeOnSuccess)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setLinesToSkip(_linesToSkip);
		setOwner(_owner);
		setRemoveOnSuccess(_removeOnSuccess);
	}
	
	
}
