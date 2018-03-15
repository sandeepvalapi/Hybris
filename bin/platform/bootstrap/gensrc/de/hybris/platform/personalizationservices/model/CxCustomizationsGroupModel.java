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

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type CxCustomizationsGroup first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxCustomizationsGroupModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxCustomizationsGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomizationsGroup.code</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomizationsGroup.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomizationsGroup.customizations</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CUSTOMIZATIONS = "customizations";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxCustomizationsGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxCustomizationsGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxCustomizationsGroup</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxCustomizationsGroup</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxCustomizationsGroupModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxCustomizationsGroup</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxCustomizationsGroup</code> at extension <code>personalizationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CxCustomizationsGroupModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomizationsGroup.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomizationsGroup.code</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the code - Group code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomizationsGroup.customizations</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the customizations
	 */
	@Accessor(qualifier = "customizations", type = Accessor.Type.GETTER)
	public List<CxCustomizationModel> getCustomizations()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMIZATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomizationsGroup.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CxCustomizationsGroup.code</code> attribute defined at extension <code>personalizationservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - Group code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomizationsGroup.customizations</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the customizations
	 */
	@Accessor(qualifier = "customizations", type = Accessor.Type.SETTER)
	public void setCustomizations(final List<CxCustomizationModel> value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMIZATIONS, value);
	}
	
}
