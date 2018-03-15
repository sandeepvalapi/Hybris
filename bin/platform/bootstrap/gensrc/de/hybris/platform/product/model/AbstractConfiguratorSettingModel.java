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
package de.hybris.platform.product.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.ConfigurationCategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractConfiguratorSetting first defined at extension catalog.
 */
@SuppressWarnings("all")
public class AbstractConfiguratorSettingModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractConfiguratorSetting";
	
	/**<i>Generated relation code constant for relation <code>ConfigurationCategory2ConfiguratorSettingsRelation</code> defining source attribute <code>configurationCategory</code> in extension <code>catalog</code>.</i>*/
	public static final String _CONFIGURATIONCATEGORY2CONFIGURATORSETTINGSRELATION = "ConfigurationCategory2ConfiguratorSettingsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConfiguratorSetting.id</code> attribute defined at extension <code>catalog</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConfiguratorSetting.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConfiguratorSetting.configuratorType</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONFIGURATORTYPE = "configuratorType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConfiguratorSetting.qualifier</code> attribute defined at extension <code>catalog</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConfiguratorSetting.configurationCategoryPOS</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONFIGURATIONCATEGORYPOS = "configurationCategoryPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConfiguratorSetting.configurationCategory</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONFIGURATIONCATEGORY = "configurationCategory";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractConfiguratorSettingModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractConfiguratorSettingModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _configurationCategory initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _configuratorType initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _id initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _qualifier initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public AbstractConfiguratorSettingModel(final CatalogVersionModel _catalogVersion, final ConfigurationCategoryModel _configurationCategory, final ConfiguratorType _configuratorType, final String _id, final String _qualifier)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setConfigurationCategory(_configurationCategory);
		setConfiguratorType(_configuratorType);
		setId(_id);
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _configurationCategory initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _configuratorType initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _id initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public AbstractConfiguratorSettingModel(final CatalogVersionModel _catalogVersion, final ConfigurationCategoryModel _configurationCategory, final ConfiguratorType _configuratorType, final String _id, final ItemModel _owner, final String _qualifier)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setConfigurationCategory(_configurationCategory);
		setConfiguratorType(_configuratorType);
		setId(_id);
		setOwner(_owner);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConfiguratorSetting.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConfiguratorSetting.configurationCategory</code> attribute defined at extension <code>catalog</code>. 
	 * @return the configurationCategory
	 */
	@Accessor(qualifier = "configurationCategory", type = Accessor.Type.GETTER)
	public ConfigurationCategoryModel getConfigurationCategory()
	{
		return getPersistenceContext().getPropertyValue(CONFIGURATIONCATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConfiguratorSetting.configuratorType</code> attribute defined at extension <code>catalog</code>. 
	 * @return the configuratorType - Type of the product configurator
	 */
	@Accessor(qualifier = "configuratorType", type = Accessor.Type.GETTER)
	public ConfiguratorType getConfiguratorType()
	{
		return getPersistenceContext().getPropertyValue(CONFIGURATORTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConfiguratorSetting.id</code> attribute defined at extension <code>catalog</code>. 
	 * @return the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConfiguratorSetting.qualifier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the qualifier - To override a setting you should create one with the same qualifier in a descending category
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractConfiguratorSetting.catalogVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractConfiguratorSetting.configurationCategory</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the configurationCategory
	 */
	@Accessor(qualifier = "configurationCategory", type = Accessor.Type.SETTER)
	public void setConfigurationCategory(final ConfigurationCategoryModel value)
	{
		getPersistenceContext().setPropertyValue(CONFIGURATIONCATEGORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractConfiguratorSetting.configuratorType</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the configuratorType - Type of the product configurator
	 */
	@Accessor(qualifier = "configuratorType", type = Accessor.Type.SETTER)
	public void setConfiguratorType(final ConfiguratorType value)
	{
		getPersistenceContext().setPropertyValue(CONFIGURATORTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractConfiguratorSetting.id</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractConfiguratorSetting.qualifier</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the qualifier - To override a setting you should create one with the same qualifier in a descending category
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIER, value);
	}
	
}
