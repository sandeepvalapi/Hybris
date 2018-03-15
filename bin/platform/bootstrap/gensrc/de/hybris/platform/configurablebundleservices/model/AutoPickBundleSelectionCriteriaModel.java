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
package de.hybris.platform.configurablebundleservices.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AutoPickBundleSelectionCriteria first defined at extension configurablebundleservices.
 */
@SuppressWarnings("all")
public class AutoPickBundleSelectionCriteriaModel extends BundleSelectionCriteriaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AutoPickBundleSelectionCriteria";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AutoPickBundleSelectionCriteriaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AutoPickBundleSelectionCriteriaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>BundleSelectionCriteria</code> at extension <code>configurablebundleservices</code>
	 */
	@Deprecated
	public AutoPickBundleSelectionCriteriaModel(final CatalogVersionModel _catalogVersion)
	{
		super();
		setCatalogVersion(_catalogVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>BundleSelectionCriteria</code> at extension <code>configurablebundleservices</code>
	 * @param _id initial attribute declared by type <code>BundleSelectionCriteria</code> at extension <code>configurablebundleservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AutoPickBundleSelectionCriteriaModel(final CatalogVersionModel _catalogVersion, final String _id, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setId(_id);
		setOwner(_owner);
	}
	
	
}
