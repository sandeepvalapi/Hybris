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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type TextFieldConfiguredProductInfo first defined at extension textfieldconfiguratortemplateservices.
 * <p>
 * Order entry side of TextFieldConfiguratorSetting.
 */
@SuppressWarnings("all")
public class TextFieldConfiguredProductInfoModel extends AbstractOrderEntryProductInfoModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "TextFieldConfiguredProductInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>TextFieldConfiguredProductInfo.configurationLabel</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. */
	public static final String CONFIGURATIONLABEL = "configurationLabel";
	
	/** <i>Generated constant</i> - Attribute key of <code>TextFieldConfiguredProductInfo.configurationValue</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. */
	public static final String CONFIGURATIONVALUE = "configurationValue";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TextFieldConfiguredProductInfoModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TextFieldConfiguredProductInfoModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _configuratorType initial attribute declared by type <code>AbstractOrderEntryProductInfo</code> at extension <code>catalog</code>
	 * @param _orderEntry initial attribute declared by type <code>AbstractOrderEntryProductInfo</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public TextFieldConfiguredProductInfoModel(final ConfiguratorType _configuratorType, final AbstractOrderEntryModel _orderEntry)
	{
		super();
		setConfiguratorType(_configuratorType);
		setOrderEntry(_orderEntry);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _configuratorType initial attribute declared by type <code>AbstractOrderEntryProductInfo</code> at extension <code>catalog</code>
	 * @param _orderEntry initial attribute declared by type <code>AbstractOrderEntryProductInfo</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public TextFieldConfiguredProductInfoModel(final ConfiguratorType _configuratorType, final AbstractOrderEntryModel _orderEntry, final ItemModel _owner)
	{
		super();
		setConfiguratorType(_configuratorType);
		setOrderEntry(_orderEntry);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguredProductInfo.configurationLabel</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 * @return the configurationLabel - Text fiel label
	 */
	@Accessor(qualifier = "configurationLabel", type = Accessor.Type.GETTER)
	public String getConfigurationLabel()
	{
		return getPersistenceContext().getPropertyValue(CONFIGURATIONLABEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TextFieldConfiguredProductInfo.configurationValue</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 * @return the configurationValue - Text field value
	 */
	@Accessor(qualifier = "configurationValue", type = Accessor.Type.GETTER)
	public String getConfigurationValue()
	{
		return getPersistenceContext().getPropertyValue(CONFIGURATIONVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TextFieldConfiguredProductInfo.configurationLabel</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 *  
	 * @param value the configurationLabel - Text fiel label
	 */
	@Accessor(qualifier = "configurationLabel", type = Accessor.Type.SETTER)
	public void setConfigurationLabel(final String value)
	{
		getPersistenceContext().setPropertyValue(CONFIGURATIONLABEL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TextFieldConfiguredProductInfo.configurationValue</code> attribute defined at extension <code>textfieldconfiguratortemplateservices</code>. 
	 *  
	 * @param value the configurationValue - Text field value
	 */
	@Accessor(qualifier = "configurationValue", type = Accessor.Type.SETTER)
	public void setConfigurationValue(final String value)
	{
		getPersistenceContext().setPropertyValue(CONFIGURATIONVALUE, value);
	}
	
}
