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
package de.hybris.platform.personalizationservices.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CxDefaultTrigger first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxDefaultTriggerModel extends CxAbstractTriggerModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxDefaultTrigger";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxDefaultTriggerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxDefaultTriggerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _variation initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxDefaultTriggerModel(final CatalogVersionModel _catalogVersion, final String _code, final CxVariationModel _variation)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setVariation(_variation);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _variation initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxDefaultTriggerModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner, final CxVariationModel _variation)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
		setVariation(_variation);
	}
	
	
}
