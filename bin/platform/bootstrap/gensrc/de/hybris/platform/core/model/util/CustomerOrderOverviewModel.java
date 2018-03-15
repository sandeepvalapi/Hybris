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
package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CustomerOrderOverview first defined at extension core.
 */
@SuppressWarnings("all")
public class CustomerOrderOverviewModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CustomerOrderOverview";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerOrderOverview.customer</code> attribute defined at extension <code>core</code>. */
	public static final String CUSTOMER = "customer";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerOrderOverview.orderCount</code> attribute defined at extension <code>core</code>. */
	public static final String ORDERCOUNT = "orderCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerOrderOverview.orderTotals</code> attribute defined at extension <code>core</code>. */
	public static final String ORDERTOTALS = "orderTotals";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerOrderOverview.currency</code> attribute defined at extension <code>core</code>. */
	public static final String CURRENCY = "currency";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CustomerOrderOverviewModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CustomerOrderOverviewModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CustomerOrderOverviewModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerOrderOverview.currency</code> attribute defined at extension <code>core</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerOrderOverview.customer</code> attribute defined at extension <code>core</code>. 
	 * @return the customer
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
	public CustomerModel getCustomer()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerOrderOverview.orderCount</code> attribute defined at extension <code>core</code>. 
	 * @return the orderCount
	 */
	@Accessor(qualifier = "orderCount", type = Accessor.Type.GETTER)
	public Integer getOrderCount()
	{
		return getPersistenceContext().getPropertyValue(ORDERCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerOrderOverview.orderTotals</code> attribute defined at extension <code>core</code>. 
	 * @return the orderTotals
	 */
	@Accessor(qualifier = "orderTotals", type = Accessor.Type.GETTER)
	public Double getOrderTotals()
	{
		return getPersistenceContext().getPropertyValue(ORDERTOTALS);
	}
	
}
