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
package de.hybris.platform.fraud.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.IntervalResolution;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type ProductOrderLimit first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class ProductOrderLimitModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductOrderLimit";
	
	/**<i>Generated relation code constant for relation <code>ProductProductOrderLimitRelation</code> defining source attribute <code>products</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _PRODUCTPRODUCTORDERLIMITRELATION = "ProductProductOrderLimitRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductOrderLimit.code</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductOrderLimit.intervalResolution</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String INTERVALRESOLUTION = "intervalResolution";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductOrderLimit.intervalValue</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String INTERVALVALUE = "intervalValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductOrderLimit.intervalMaxOrdersNumber</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String INTERVALMAXORDERSNUMBER = "intervalMaxOrdersNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductOrderLimit.maxNumberPerOrder</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String MAXNUMBERPERORDER = "maxNumberPerOrder";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductOrderLimit.products</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PRODUCTS = "products";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductOrderLimitModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductOrderLimitModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 * @param _intervalMaxOrdersNumber initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 * @param _intervalResolution initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 * @param _intervalValue initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 * @param _maxNumberPerOrder initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public ProductOrderLimitModel(final String _code, final Integer _intervalMaxOrdersNumber, final IntervalResolution _intervalResolution, final Integer _intervalValue, final Integer _maxNumberPerOrder)
	{
		super();
		setCode(_code);
		setIntervalMaxOrdersNumber(_intervalMaxOrdersNumber);
		setIntervalResolution(_intervalResolution);
		setIntervalValue(_intervalValue);
		setMaxNumberPerOrder(_maxNumberPerOrder);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 * @param _intervalMaxOrdersNumber initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 * @param _intervalResolution initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 * @param _intervalValue initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 * @param _maxNumberPerOrder initial attribute declared by type <code>ProductOrderLimit</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ProductOrderLimitModel(final String _code, final Integer _intervalMaxOrdersNumber, final IntervalResolution _intervalResolution, final Integer _intervalValue, final Integer _maxNumberPerOrder, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setIntervalMaxOrdersNumber(_intervalMaxOrdersNumber);
		setIntervalResolution(_intervalResolution);
		setIntervalValue(_intervalValue);
		setMaxNumberPerOrder(_maxNumberPerOrder);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductOrderLimit.code</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductOrderLimit.intervalMaxOrdersNumber</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the intervalMaxOrdersNumber
	 */
	@Accessor(qualifier = "intervalMaxOrdersNumber", type = Accessor.Type.GETTER)
	public Integer getIntervalMaxOrdersNumber()
	{
		return getPersistenceContext().getPropertyValue(INTERVALMAXORDERSNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductOrderLimit.intervalResolution</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the intervalResolution
	 */
	@Accessor(qualifier = "intervalResolution", type = Accessor.Type.GETTER)
	public IntervalResolution getIntervalResolution()
	{
		return getPersistenceContext().getPropertyValue(INTERVALRESOLUTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductOrderLimit.intervalValue</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the intervalValue
	 */
	@Accessor(qualifier = "intervalValue", type = Accessor.Type.GETTER)
	public Integer getIntervalValue()
	{
		return getPersistenceContext().getPropertyValue(INTERVALVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductOrderLimit.maxNumberPerOrder</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the maxNumberPerOrder
	 */
	@Accessor(qualifier = "maxNumberPerOrder", type = Accessor.Type.GETTER)
	public Integer getMaxNumberPerOrder()
	{
		return getPersistenceContext().getPropertyValue(MAXNUMBERPERORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductOrderLimit.products</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public Set<ProductModel> getProducts()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductOrderLimit.code</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductOrderLimit.intervalMaxOrdersNumber</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the intervalMaxOrdersNumber
	 */
	@Accessor(qualifier = "intervalMaxOrdersNumber", type = Accessor.Type.SETTER)
	public void setIntervalMaxOrdersNumber(final Integer value)
	{
		getPersistenceContext().setPropertyValue(INTERVALMAXORDERSNUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductOrderLimit.intervalResolution</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the intervalResolution
	 */
	@Accessor(qualifier = "intervalResolution", type = Accessor.Type.SETTER)
	public void setIntervalResolution(final IntervalResolution value)
	{
		getPersistenceContext().setPropertyValue(INTERVALRESOLUTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductOrderLimit.intervalValue</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the intervalValue
	 */
	@Accessor(qualifier = "intervalValue", type = Accessor.Type.SETTER)
	public void setIntervalValue(final Integer value)
	{
		getPersistenceContext().setPropertyValue(INTERVALVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductOrderLimit.maxNumberPerOrder</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the maxNumberPerOrder
	 */
	@Accessor(qualifier = "maxNumberPerOrder", type = Accessor.Type.SETTER)
	public void setMaxNumberPerOrder(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXNUMBERPERORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductOrderLimit.products</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final Set<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTS, value);
	}
	
}
