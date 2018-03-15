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
package de.hybris.platform.core.model.order.price;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type Discount first defined at extension core.
 */
@SuppressWarnings("all")
public class DiscountModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Discount";
	
	/**<i>Generated relation code constant for relation <code>OrderDiscountRelation</code> defining source attribute <code>orders</code> in extension <code>core</code>.</i>*/
	public static final String _ORDERDISCOUNTRELATION = "OrderDiscountRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Discount.absolute</code> attribute defined at extension <code>core</code>. */
	public static final String ABSOLUTE = "absolute";
	
	/** <i>Generated constant</i> - Attribute key of <code>Discount.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Discount.currency</code> attribute defined at extension <code>core</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>Discount.global</code> attribute defined at extension <code>core</code>. */
	public static final String GLOBAL = "global";
	
	/** <i>Generated constant</i> - Attribute key of <code>Discount.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Discount.priority</code> attribute defined at extension <code>core</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>Discount.value</code> attribute defined at extension <code>core</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>Discount.discountString</code> attribute defined at extension <code>core</code>. */
	public static final String DISCOUNTSTRING = "discountString";
	
	/** <i>Generated constant</i> - Attribute key of <code>Discount.orders</code> attribute defined at extension <code>core</code>. */
	public static final String ORDERS = "orders";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DiscountModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DiscountModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Discount</code> at extension <code>core</code>
	 */
	@Deprecated
	public DiscountModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Discount</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DiscountModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.absolute</code> attribute defined at extension <code>core</code>. 
	 * @return the absolute
	 */
	@Accessor(qualifier = "absolute", type = Accessor.Type.GETTER)
	public Boolean getAbsolute()
	{
		return getPersistenceContext().getPropertyValue(ABSOLUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.currency</code> attribute defined at extension <code>core</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.discountString</code> attribute defined at extension <code>core</code>. 
	 * @return the discountString
	 * @deprecated since ages - use { @link #getDiscountString()} instead
	 */
	@Deprecated
	public String getDiscountstring()
	{
		return this.getDiscountString();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.discountString</code> attribute defined at extension <code>core</code>. 
	 * @return the discountString
	 */
	@Accessor(qualifier = "discountString", type = Accessor.Type.GETTER)
	public String getDiscountString()
	{
		return getPersistenceContext().getPropertyValue(DISCOUNTSTRING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.global</code> attribute defined at extension <code>core</code>. 
	 * @return the global
	 */
	@Accessor(qualifier = "global", type = Accessor.Type.GETTER)
	public Boolean getGlobal()
	{
		return getPersistenceContext().getPropertyValue(GLOBAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.name</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Discount.orders</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the orders
	 */
	@Accessor(qualifier = "orders", type = Accessor.Type.GETTER)
	public Collection<AbstractOrderModel> getOrders()
	{
		return getPersistenceContext().getPropertyValue(ORDERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.priority</code> attribute defined at extension <code>core</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		final Integer value = getPersistenceContext().getPropertyValue(PRIORITY);
		return value != null ? value : Integer.valueOf(0);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Discount.value</code> attribute defined at extension <code>core</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public Double getValue()
	{
		final Double value = getPersistenceContext().getPropertyValue(VALUE);
		return value != null ? value : Double.valueOf(0.0d);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Discount.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Discount.currency</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Discount.global</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the global
	 */
	@Accessor(qualifier = "global", type = Accessor.Type.SETTER)
	public void setGlobal(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(GLOBAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Discount.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Discount.name</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Setter of <code>Discount.orders</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the orders
	 */
	@Accessor(qualifier = "orders", type = Accessor.Type.SETTER)
	public void setOrders(final Collection<AbstractOrderModel> value)
	{
		getPersistenceContext().setPropertyValue(ORDERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Discount.priority</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Discount.value</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final Double value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
}
