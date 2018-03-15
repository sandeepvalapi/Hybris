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
package de.hybris.platform.advancedsavedquery.model;

import de.hybris.platform.advancedsavedquery.model.AbstractAdvancedSavedQuerySearchParameterModel;
import de.hybris.platform.advancedsavedquery.model.WherePartModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SimpleAdvancedSavedQuerySearchParameter first defined at extension advancedsavedquery.
 */
@SuppressWarnings("all")
public class SimpleAdvancedSavedQuerySearchParameterModel extends AbstractAdvancedSavedQuerySearchParameterModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SimpleAdvancedSavedQuerySearchParameter";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SimpleAdvancedSavedQuerySearchParameterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SimpleAdvancedSavedQuerySearchParameterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _searchParameterName initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 * @param _wherePart initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 */
	@Deprecated
	public SimpleAdvancedSavedQuerySearchParameterModel(final String _searchParameterName, final WherePartModel _wherePart)
	{
		super();
		setSearchParameterName(_searchParameterName);
		setWherePart(_wherePart);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchParameterName initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 * @param _wherePart initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 */
	@Deprecated
	public SimpleAdvancedSavedQuerySearchParameterModel(final ItemModel _owner, final String _searchParameterName, final WherePartModel _wherePart)
	{
		super();
		setOwner(_owner);
		setSearchParameterName(_searchParameterName);
		setWherePart(_wherePart);
	}
	
	
}
