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
package de.hybris.platform.ordersplitting.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type Warehouse first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class WarehouseModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Warehouse";
	
	/**<i>Generated relation code constant for relation <code>VendorWarehouseRelation</code> defining source attribute <code>vendor</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _VENDORWAREHOUSERELATION = "VendorWarehouseRelation";
	
	/**<i>Generated relation code constant for relation <code>ConsignmentWarehouseRelation</code> defining source attribute <code>consignments</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _CONSIGNMENTWAREHOUSERELATION = "ConsignmentWarehouseRelation";
	
	/**<i>Generated relation code constant for relation <code>StockLevelWarehouseRelation</code> defining source attribute <code>stockLevels</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _STOCKLEVELWAREHOUSERELATION = "StockLevelWarehouseRelation";
	
	/**<i>Generated relation code constant for relation <code>PoS2WarehouseRel</code> defining source attribute <code>pointsOfService</code> in extension <code>commerceservices</code>.</i>*/
	public static final String _POS2WAREHOUSEREL = "PoS2WarehouseRel";
	
	/**<i>Generated relation code constant for relation <code>BaseStore2WarehouseRel</code> defining source attribute <code>baseStores</code> in extension <code>commerceservices</code>.</i>*/
	public static final String _BASESTORE2WAREHOUSEREL = "BaseStore2WarehouseRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>Warehouse.code</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Warehouse.name</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Warehouse.default</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DEFAULT = "default";
	
	/** <i>Generated constant</i> - Attribute key of <code>Warehouse.vendorPOS</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String VENDORPOS = "vendorPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>Warehouse.vendor</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String VENDOR = "vendor";
	
	/** <i>Generated constant</i> - Attribute key of <code>Warehouse.consignments</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CONSIGNMENTS = "consignments";
	
	/** <i>Generated constant</i> - Attribute key of <code>Warehouse.stockLevels</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String STOCKLEVELS = "stockLevels";
	
	/** <i>Generated constant</i> - Attribute key of <code>Warehouse.pointsOfService</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String POINTSOFSERVICE = "pointsOfService";
	
	/** <i>Generated constant</i> - Attribute key of <code>Warehouse.baseStores</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String BASESTORES = "baseStores";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WarehouseModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WarehouseModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Warehouse</code> at extension <code>basecommerce</code>
	 * @param _vendor initial attribute declared by type <code>Warehouse</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public WarehouseModel(final String _code, final VendorModel _vendor)
	{
		super();
		setCode(_code);
		setVendor(_vendor);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Warehouse</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _vendor initial attribute declared by type <code>Warehouse</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public WarehouseModel(final String _code, final ItemModel _owner, final VendorModel _vendor)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setVendor(_vendor);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.baseStores</code> attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the baseStores
	 */
	@Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
	public Collection<BaseStoreModel> getBaseStores()
	{
		return getPersistenceContext().getPropertyValue(BASESTORES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.code</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.consignments</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the consignments
	 */
	@Accessor(qualifier = "consignments", type = Accessor.Type.GETTER)
	public Set<ConsignmentModel> getConsignments()
	{
		return getPersistenceContext().getPropertyValue(CONSIGNMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.default</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the default
	 */
	@Accessor(qualifier = "default", type = Accessor.Type.GETTER)
	public Boolean getDefault()
	{
		return getPersistenceContext().getPropertyValue(DEFAULT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.name</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.name</code> attribute defined at extension <code>basecommerce</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.pointsOfService</code> attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the pointsOfService
	 */
	@Accessor(qualifier = "pointsOfService", type = Accessor.Type.GETTER)
	public Collection<PointOfServiceModel> getPointsOfService()
	{
		return getPersistenceContext().getPropertyValue(POINTSOFSERVICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.stockLevels</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the stockLevels
	 */
	@Accessor(qualifier = "stockLevels", type = Accessor.Type.GETTER)
	public Set<StockLevelModel> getStockLevels()
	{
		return getPersistenceContext().getPropertyValue(STOCKLEVELS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Warehouse.vendor</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the vendor
	 */
	@Accessor(qualifier = "vendor", type = Accessor.Type.GETTER)
	public VendorModel getVendor()
	{
		return getPersistenceContext().getPropertyValue(VENDOR);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Warehouse.baseStores</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the baseStores
	 */
	@Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
	public void setBaseStores(final Collection<BaseStoreModel> value)
	{
		getPersistenceContext().setPropertyValue(BASESTORES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Warehouse.code</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Warehouse.consignments</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the consignments
	 */
	@Accessor(qualifier = "consignments", type = Accessor.Type.SETTER)
	public void setConsignments(final Set<ConsignmentModel> value)
	{
		getPersistenceContext().setPropertyValue(CONSIGNMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Warehouse.default</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the default
	 */
	@Accessor(qualifier = "default", type = Accessor.Type.SETTER)
	public void setDefault(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DEFAULT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Warehouse.name</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Warehouse.name</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Warehouse.pointsOfService</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the pointsOfService
	 */
	@Accessor(qualifier = "pointsOfService", type = Accessor.Type.SETTER)
	public void setPointsOfService(final Collection<PointOfServiceModel> value)
	{
		getPersistenceContext().setPropertyValue(POINTSOFSERVICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Warehouse.stockLevels</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the stockLevels
	 */
	@Accessor(qualifier = "stockLevels", type = Accessor.Type.SETTER)
	public void setStockLevels(final Set<StockLevelModel> value)
	{
		getPersistenceContext().setPropertyValue(STOCKLEVELS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Warehouse.vendor</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the vendor
	 */
	@Accessor(qualifier = "vendor", type = Accessor.Type.SETTER)
	public void setVendor(final VendorModel value)
	{
		getPersistenceContext().setPropertyValue(VENDOR, value);
	}
	
}
