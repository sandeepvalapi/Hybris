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
package de.hybris.platform.textfieldconfiguratortemplateservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.ConfigurationCategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.product.model.AbstractConfiguratorSettingModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type TextFieldConfiguratorSetting first defined at extension textfieldconfiguratortemplateservices.
 * <p>
 * TextField Configurator that contains additional attribute describing text field configuration.
 */
@SuppressWarnings("all")
public class TextFieldConfiguratorSettingModel extends AbstractConfiguratorSettingModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "TextFieldConfiguratorSetting";
	
	/** <i>Generated constant</i> - Attribute key of <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. */
	public static final String TEXTFIELDLABEL = "textFieldLabel";
	
	/** <i>Generated constant</i> - Attribute key of <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. */
	public static final String TEXTFIELDDEFAULTVALUE = "textFieldDefaultValue";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TextFieldConfiguratorSettingModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TextFieldConfiguratorSettingModel(final ItemModelContext ctx)
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
	public TextFieldConfiguratorSettingModel(final CatalogVersionModel _catalogVersion, final ConfigurationCategoryModel _configurationCategory, final ConfiguratorType _configuratorType, final String _id, final String _qualifier)
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
	public TextFieldConfiguratorSettingModel(final CatalogVersionModel _catalogVersion, final ConfigurationCategoryModel _configurationCategory, final ConfiguratorType _configuratorType, final String _id, final ItemModel _owner, final String _qualifier)
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
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 * @return the textFieldDefaultValue - Default value of the text field
	 */
	@Accessor(qualifier = "textFieldDefaultValue", type = Accessor.Type.GETTER)
	public String getTextFieldDefaultValue()
	{
		return getTextFieldDefaultValue(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 * @param loc the value localization key 
	 * @return the textFieldDefaultValue - Default value of the text field
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "textFieldDefaultValue", type = Accessor.Type.GETTER)
	public String getTextFieldDefaultValue(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TEXTFIELDDEFAULTVALUE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 * @return the textFieldLabel - Label of the text field
	 */
	@Accessor(qualifier = "textFieldLabel", type = Accessor.Type.GETTER)
	public String getTextFieldLabel()
	{
		return getTextFieldLabel(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 * @param loc the value localization key 
	 * @return the textFieldLabel - Label of the text field
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "textFieldLabel", type = Accessor.Type.GETTER)
	public String getTextFieldLabel(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TEXTFIELDLABEL, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 *  
	 * @param value the textFieldDefaultValue - Default value of the text field
	 */
	@Accessor(qualifier = "textFieldDefaultValue", type = Accessor.Type.SETTER)
	public void setTextFieldDefaultValue(final String value)
	{
		setTextFieldDefaultValue(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>TextFieldConfiguratorSetting.textFieldDefaultValue</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 *  
	 * @param value the textFieldDefaultValue - Default value of the text field
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "textFieldDefaultValue", type = Accessor.Type.SETTER)
	public void setTextFieldDefaultValue(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TEXTFIELDDEFAULTVALUE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 *  
	 * @param value the textFieldLabel - Label of the text field
	 */
	@Accessor(qualifier = "textFieldLabel", type = Accessor.Type.SETTER)
	public void setTextFieldLabel(final String value)
	{
		setTextFieldLabel(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>TextFieldConfiguratorSetting.textFieldLabel</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 *  
	 * @param value the textFieldLabel - Label of the text field
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "textFieldLabel", type = Accessor.Type.SETTER)
	public void setTextFieldLabel(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TEXTFIELDLABEL, loc, value);
	}
	
}
