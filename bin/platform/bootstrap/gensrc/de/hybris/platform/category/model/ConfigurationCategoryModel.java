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
package de.hybris.platform.category.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.product.model.AbstractConfiguratorSettingModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type ConfigurationCategory first defined at extension catalog.
 * <p>
 * Category type for configurable products.
 */
@SuppressWarnings("all")
public class ConfigurationCategoryModel extends CategoryModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ConfigurationCategory";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConfigurationCategory.configuratorSettings</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONFIGURATORSETTINGS = "configuratorSettings";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConfigurationCategoryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConfigurationCategoryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ConfigurationCategoryModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ConfigurationCategoryModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConfigurationCategory.configuratorSettings</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the configuratorSettings - Product configuration
	 */
	@Accessor(qualifier = "configuratorSettings", type = Accessor.Type.GETTER)
	public List<AbstractConfiguratorSettingModel> getConfiguratorSettings()
	{
		return getPersistenceContext().getPropertyValue(CONFIGURATORSETTINGS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConfigurationCategory.configuratorSettings</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the configuratorSettings - Product configuration
	 */
	@Accessor(qualifier = "configuratorSettings", type = Accessor.Type.SETTER)
	public void setConfiguratorSettings(final List<AbstractConfiguratorSettingModel> value)
	{
		getPersistenceContext().setPropertyValue(CONFIGURATORSETTINGS, value);
	}
	
}
