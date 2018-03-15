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
package de.hybris.platform.order.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractOrderEntryProductInfo first defined at extension catalog.
 */
@SuppressWarnings("all")
public class AbstractOrderEntryProductInfoModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractOrderEntryProductInfo";
	
	/**<i>Generated relation code constant for relation <code>AbstractOrderEntry2AbstractOrderEntryProductInfoRelation</code> defining source attribute <code>orderEntry</code> in extension <code>catalog</code>.</i>*/
	public static final String _ABSTRACTORDERENTRY2ABSTRACTORDERENTRYPRODUCTINFORELATION = "AbstractOrderEntry2AbstractOrderEntryProductInfoRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntryProductInfo.productInfoStatus</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRODUCTINFOSTATUS = "productInfoStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntryProductInfo.configuratorType</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONFIGURATORTYPE = "configuratorType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntryProductInfo.orderEntryPOS</code> attribute defined at extension <code>catalog</code>. */
	public static final String ORDERENTRYPOS = "orderEntryPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntryProductInfo.orderEntry</code> attribute defined at extension <code>catalog</code>. */
	public static final String ORDERENTRY = "orderEntry";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractOrderEntryProductInfoModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractOrderEntryProductInfoModel(final ItemModelContext ctx)
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
	public AbstractOrderEntryProductInfoModel(final ConfiguratorType _configuratorType, final AbstractOrderEntryModel _orderEntry)
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
	public AbstractOrderEntryProductInfoModel(final ConfiguratorType _configuratorType, final AbstractOrderEntryModel _orderEntry, final ItemModel _owner)
	{
		super();
		setConfiguratorType(_configuratorType);
		setOrderEntry(_orderEntry);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntryProductInfo.configuratorType</code> attribute defined at extension <code>catalog</code>. 
	 * @return the configuratorType - Configurator type for configurable product
	 */
	@Accessor(qualifier = "configuratorType", type = Accessor.Type.GETTER)
	public ConfiguratorType getConfiguratorType()
	{
		return getPersistenceContext().getPropertyValue(CONFIGURATORTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntryProductInfo.orderEntry</code> attribute defined at extension <code>catalog</code>. 
	 * @return the orderEntry
	 */
	@Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
	public AbstractOrderEntryModel getOrderEntry()
	{
		return getPersistenceContext().getPropertyValue(ORDERENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntryProductInfo.productInfoStatus</code> attribute defined at extension <code>catalog</code>. 
	 * @return the productInfoStatus - Status of AbstractOrderEntryProductInfo, NONE by default
	 */
	@Accessor(qualifier = "productInfoStatus", type = Accessor.Type.GETTER)
	public ProductInfoStatus getProductInfoStatus()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTINFOSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractOrderEntryProductInfo.configuratorType</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the configuratorType - Configurator type for configurable product
	 */
	@Accessor(qualifier = "configuratorType", type = Accessor.Type.SETTER)
	public void setConfiguratorType(final ConfiguratorType value)
	{
		getPersistenceContext().setPropertyValue(CONFIGURATORTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractOrderEntryProductInfo.orderEntry</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the orderEntry
	 */
	@Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
	public void setOrderEntry(final AbstractOrderEntryModel value)
	{
		getPersistenceContext().setPropertyValue(ORDERENTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntryProductInfo.productInfoStatus</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the productInfoStatus - Status of AbstractOrderEntryProductInfo, NONE by default
	 */
	@Accessor(qualifier = "productInfoStatus", type = Accessor.Type.SETTER)
	public void setProductInfoStatus(final ProductInfoStatus value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTINFOSTATUS, value);
	}
	
}
