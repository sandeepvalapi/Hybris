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
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.stock.model.StockLevelHistoryEntryModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type StockLevel first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class StockLevelModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StockLevel";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.product</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.available</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String AVAILABLE = "available";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.releaseDate</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RELEASEDATE = "releaseDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.nextDeliveryTime</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String NEXTDELIVERYTIME = "nextDeliveryTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.productCode</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PRODUCTCODE = "productCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.reserved</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RESERVED = "reserved";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.overSelling</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String OVERSELLING = "overSelling";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.preOrder</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PREORDER = "preOrder";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.maxPreOrder</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String MAXPREORDER = "maxPreOrder";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.treatNegativeAsZero</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String TREATNEGATIVEASZERO = "treatNegativeAsZero";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.inStockStatus</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String INSTOCKSTATUS = "inStockStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.maxStockLevelHistoryCount</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String MAXSTOCKLEVELHISTORYCOUNT = "maxStockLevelHistoryCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.products</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PRODUCTS = "products";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.warehouse</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String WAREHOUSE = "warehouse";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevel.stockLevelHistoryEntries</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String STOCKLEVELHISTORYENTRIES = "stockLevelHistoryEntries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StockLevelModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StockLevelModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _available initial attribute declared by type <code>StockLevel</code> at extension <code>basecommerce</code>
	 * @param _productCode initial attribute declared by type <code>StockLevel</code> at extension <code>basecommerce</code>
	 * @param _warehouse initial attribute declared by type <code>StockLevel</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public StockLevelModel(final int _available, final String _productCode, final WarehouseModel _warehouse)
	{
		super();
		setAvailable(_available);
		setProductCode(_productCode);
		setWarehouse(_warehouse);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _available initial attribute declared by type <code>StockLevel</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _productCode initial attribute declared by type <code>StockLevel</code> at extension <code>basecommerce</code>
	 * @param _warehouse initial attribute declared by type <code>StockLevel</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public StockLevelModel(final int _available, final ItemModel _owner, final String _productCode, final WarehouseModel _warehouse)
	{
		super();
		setAvailable(_available);
		setOwner(_owner);
		setProductCode(_productCode);
		setWarehouse(_warehouse);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.available</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the available
	 */
	@Accessor(qualifier = "available", type = Accessor.Type.GETTER)
	public int getAvailable()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(AVAILABLE));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.inStockStatus</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the inStockStatus
	 */
	@Accessor(qualifier = "inStockStatus", type = Accessor.Type.GETTER)
	public InStockStatus getInStockStatus()
	{
		return getPersistenceContext().getPropertyValue(INSTOCKSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.maxPreOrder</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the maxPreOrder
	 */
	@Accessor(qualifier = "maxPreOrder", type = Accessor.Type.GETTER)
	public int getMaxPreOrder()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(MAXPREORDER));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.maxStockLevelHistoryCount</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the maxStockLevelHistoryCount - size of the StockLevelHistoyEntry, negative values for unlimited
	 */
	@Accessor(qualifier = "maxStockLevelHistoryCount", type = Accessor.Type.GETTER)
	public int getMaxStockLevelHistoryCount()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(MAXSTOCKLEVELHISTORYCOUNT));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.nextDeliveryTime</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the nextDeliveryTime
	 */
	@Accessor(qualifier = "nextDeliveryTime", type = Accessor.Type.GETTER)
	public Date getNextDeliveryTime()
	{
		return getPersistenceContext().getPropertyValue(NEXTDELIVERYTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.overSelling</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the overSelling
	 */
	@Accessor(qualifier = "overSelling", type = Accessor.Type.GETTER)
	public int getOverSelling()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(OVERSELLING));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.preOrder</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the preOrder
	 */
	@Accessor(qualifier = "preOrder", type = Accessor.Type.GETTER)
	public int getPreOrder()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(PREORDER));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.product</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.productCode</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the productCode
	 */
	@Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
	public String getProductCode()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.products</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getProducts()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.releaseDate</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the releaseDate
	 */
	@Accessor(qualifier = "releaseDate", type = Accessor.Type.GETTER)
	public Date getReleaseDate()
	{
		return getPersistenceContext().getPropertyValue(RELEASEDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.reserved</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the reserved
	 */
	@Accessor(qualifier = "reserved", type = Accessor.Type.GETTER)
	public int getReserved()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(RESERVED));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.stockLevelHistoryEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the stockLevelHistoryEntries
	 */
	@Accessor(qualifier = "stockLevelHistoryEntries", type = Accessor.Type.GETTER)
	public List<StockLevelHistoryEntryModel> getStockLevelHistoryEntries()
	{
		return getPersistenceContext().getPropertyValue(STOCKLEVELHISTORYENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.warehouse</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the warehouse
	 */
	@Accessor(qualifier = "warehouse", type = Accessor.Type.GETTER)
	public WarehouseModel getWarehouse()
	{
		return getPersistenceContext().getPropertyValue(WAREHOUSE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.treatNegativeAsZero</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the treatNegativeAsZero
	 */
	@Accessor(qualifier = "treatNegativeAsZero", type = Accessor.Type.GETTER)
	public boolean isTreatNegativeAsZero()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(TREATNEGATIVEASZERO));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.available</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the available
	 */
	@Accessor(qualifier = "available", type = Accessor.Type.SETTER)
	public void setAvailable(final int value)
	{
		getPersistenceContext().setPropertyValue(AVAILABLE, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.inStockStatus</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the inStockStatus
	 */
	@Accessor(qualifier = "inStockStatus", type = Accessor.Type.SETTER)
	public void setInStockStatus(final InStockStatus value)
	{
		getPersistenceContext().setPropertyValue(INSTOCKSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.maxPreOrder</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the maxPreOrder
	 */
	@Accessor(qualifier = "maxPreOrder", type = Accessor.Type.SETTER)
	public void setMaxPreOrder(final int value)
	{
		getPersistenceContext().setPropertyValue(MAXPREORDER, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.maxStockLevelHistoryCount</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the maxStockLevelHistoryCount - size of the StockLevelHistoyEntry, negative values for unlimited
	 */
	@Accessor(qualifier = "maxStockLevelHistoryCount", type = Accessor.Type.SETTER)
	public void setMaxStockLevelHistoryCount(final int value)
	{
		getPersistenceContext().setPropertyValue(MAXSTOCKLEVELHISTORYCOUNT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.nextDeliveryTime</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the nextDeliveryTime
	 */
	@Accessor(qualifier = "nextDeliveryTime", type = Accessor.Type.SETTER)
	public void setNextDeliveryTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(NEXTDELIVERYTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.overSelling</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the overSelling
	 */
	@Accessor(qualifier = "overSelling", type = Accessor.Type.SETTER)
	public void setOverSelling(final int value)
	{
		getPersistenceContext().setPropertyValue(OVERSELLING, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.preOrder</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the preOrder
	 */
	@Accessor(qualifier = "preOrder", type = Accessor.Type.SETTER)
	public void setPreOrder(final int value)
	{
		getPersistenceContext().setPropertyValue(PREORDER, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.product</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.productCode</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the productCode
	 */
	@Accessor(qualifier = "productCode", type = Accessor.Type.SETTER)
	public void setProductCode(final String value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.products</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final Collection<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.releaseDate</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the releaseDate
	 */
	@Accessor(qualifier = "releaseDate", type = Accessor.Type.SETTER)
	public void setReleaseDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(RELEASEDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.reserved</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the reserved
	 */
	@Accessor(qualifier = "reserved", type = Accessor.Type.SETTER)
	public void setReserved(final int value)
	{
		getPersistenceContext().setPropertyValue(RESERVED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.stockLevelHistoryEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the stockLevelHistoryEntries
	 */
	@Accessor(qualifier = "stockLevelHistoryEntries", type = Accessor.Type.SETTER)
	public void setStockLevelHistoryEntries(final List<StockLevelHistoryEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(STOCKLEVELHISTORYENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevel.treatNegativeAsZero</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the treatNegativeAsZero
	 */
	@Accessor(qualifier = "treatNegativeAsZero", type = Accessor.Type.SETTER)
	public void setTreatNegativeAsZero(final boolean value)
	{
		getPersistenceContext().setPropertyValue(TREATNEGATIVEASZERO, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StockLevel.warehouse</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the warehouse
	 */
	@Accessor(qualifier = "warehouse", type = Accessor.Type.SETTER)
	public void setWarehouse(final WarehouseModel value)
	{
		getPersistenceContext().setPropertyValue(WAREHOUSE, value);
	}
	
}
