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
package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

/**
 * Generated model class for type RegularCustomerOrderTotalRestriction first defined at extension voucher.
 */
@SuppressWarnings("all")
public class RegularCustomerOrderTotalRestrictionModel extends RestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RegularCustomerOrderTotalRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>RegularCustomerOrderTotalRestriction.allOrdersTotal</code> attribute defined at extension <code>voucher</code>. */
	public static final String ALLORDERSTOTAL = "allOrdersTotal";
	
	/** <i>Generated constant</i> - Attribute key of <code>RegularCustomerOrderTotalRestriction.currency</code> attribute defined at extension <code>voucher</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>RegularCustomerOrderTotalRestriction.net</code> attribute defined at extension <code>voucher</code>. */
	public static final String NET = "net";
	
	/** <i>Generated constant</i> - Attribute key of <code>RegularCustomerOrderTotalRestriction.valueofgoodsonly</code> attribute defined at extension <code>voucher</code>. */
	public static final String VALUEOFGOODSONLY = "valueofgoodsonly";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RegularCustomerOrderTotalRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RegularCustomerOrderTotalRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _allOrdersTotal initial attribute declared by type <code>RegularCustomerOrderTotalRestriction</code> at extension <code>voucher</code>
	 * @param _currency initial attribute declared by type <code>RegularCustomerOrderTotalRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public RegularCustomerOrderTotalRestrictionModel(final Double _allOrdersTotal, final CurrencyModel _currency, final VoucherModel _voucher)
	{
		super();
		setAllOrdersTotal(_allOrdersTotal);
		setCurrency(_currency);
		setVoucher(_voucher);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _allOrdersTotal initial attribute declared by type <code>RegularCustomerOrderTotalRestriction</code> at extension <code>voucher</code>
	 * @param _currency initial attribute declared by type <code>RegularCustomerOrderTotalRestriction</code> at extension <code>voucher</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public RegularCustomerOrderTotalRestrictionModel(final Double _allOrdersTotal, final CurrencyModel _currency, final ItemModel _owner, final VoucherModel _voucher)
	{
		super();
		setAllOrdersTotal(_allOrdersTotal);
		setCurrency(_currency);
		setOwner(_owner);
		setVoucher(_voucher);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegularCustomerOrderTotalRestriction.allOrdersTotal</code> attribute defined at extension <code>voucher</code>. 
	 * @return the allOrdersTotal - restrict to customers who ordered a total of x or greater in 
	 * 						their lifetime.
	 */
	@Accessor(qualifier = "allOrdersTotal", type = Accessor.Type.GETTER)
	public Double getAllOrdersTotal()
	{
		return getPersistenceContext().getPropertyValue(ALLORDERSTOTAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegularCustomerOrderTotalRestriction.currency</code> attribute defined at extension <code>voucher</code>. 
	 * @return the currency - the currency associated with the orderTotalSum.
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegularCustomerOrderTotalRestriction.net</code> attribute defined at extension <code>voucher</code>. 
	 * @return the net - Specifies if total sum of all existing orders is consulted 
	 * 						inclusive or exclusive VAT. Default is true (exclusive VAT).
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.GETTER)
	public Boolean getNet()
	{
		return getPersistenceContext().getPropertyValue(NET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegularCustomerOrderTotalRestriction.valueofgoodsonly</code> attribute defined at extension <code>voucher</code>. 
	 * @return the valueofgoodsonly - Specifies if total sum of all orders is consulted inclusive or 
	 * 						exclusive shipping and payment costs. Default is true (exclusive shipping 
	 * 						and payment costs).
	 */
	@Accessor(qualifier = "valueofgoodsonly", type = Accessor.Type.GETTER)
	public Boolean getValueofgoodsonly()
	{
		return getPersistenceContext().getPropertyValue(VALUEOFGOODSONLY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RegularCustomerOrderTotalRestriction.allOrdersTotal</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the allOrdersTotal - restrict to customers who ordered a total of x or greater in 
	 * 						their lifetime.
	 */
	@Accessor(qualifier = "allOrdersTotal", type = Accessor.Type.SETTER)
	public void setAllOrdersTotal(final Double value)
	{
		getPersistenceContext().setPropertyValue(ALLORDERSTOTAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RegularCustomerOrderTotalRestriction.currency</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the currency - the currency associated with the orderTotalSum.
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RegularCustomerOrderTotalRestriction.net</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the net - Specifies if total sum of all existing orders is consulted 
	 * 						inclusive or exclusive VAT. Default is true (exclusive VAT).
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.SETTER)
	public void setNet(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(NET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RegularCustomerOrderTotalRestriction.valueofgoodsonly</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the valueofgoodsonly - Specifies if total sum of all orders is consulted inclusive or 
	 * 						exclusive shipping and payment costs. Default is true (exclusive shipping 
	 * 						and payment costs).
	 */
	@Accessor(qualifier = "valueofgoodsonly", type = Accessor.Type.SETTER)
	public void setValueofgoodsonly(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(VALUEOFGOODSONLY, value);
	}
	
}
