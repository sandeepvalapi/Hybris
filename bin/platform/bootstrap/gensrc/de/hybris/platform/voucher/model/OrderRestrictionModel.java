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
 * Generated model class for type OrderRestriction first defined at extension voucher.
 */
@SuppressWarnings("all")
public class OrderRestrictionModel extends RestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderRestriction.total</code> attribute defined at extension <code>voucher</code>. */
	public static final String TOTAL = "total";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderRestriction.currency</code> attribute defined at extension <code>voucher</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderRestriction.net</code> attribute defined at extension <code>voucher</code>. */
	public static final String NET = "net";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderRestriction.valueofgoodsonly</code> attribute defined at extension <code>voucher</code>. */
	public static final String VALUEOFGOODSONLY = "valueofgoodsonly";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>OrderRestriction</code> at extension <code>voucher</code>
	 * @param _total initial attribute declared by type <code>OrderRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public OrderRestrictionModel(final CurrencyModel _currency, final Double _total, final VoucherModel _voucher)
	{
		super();
		setCurrency(_currency);
		setTotal(_total);
		setVoucher(_voucher);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>OrderRestriction</code> at extension <code>voucher</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _total initial attribute declared by type <code>OrderRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public OrderRestrictionModel(final CurrencyModel _currency, final ItemModel _owner, final Double _total, final VoucherModel _voucher)
	{
		super();
		setCurrency(_currency);
		setOwner(_owner);
		setTotal(_total);
		setVoucher(_voucher);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderRestriction.currency</code> attribute defined at extension <code>voucher</code>. 
	 * @return the currency - the currency of the total sum of an order for which the given voucher is valid.
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderRestriction.net</code> attribute defined at extension <code>voucher</code>. 
	 * @return the net - Specifies if total sum of an order is consulted inclusive or exclusive VAT. 
	 * 						Default is true (exclusive VAT).
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.GETTER)
	public Boolean getNet()
	{
		return getPersistenceContext().getPropertyValue(NET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderRestriction.total</code> attribute defined at extension <code>voucher</code>. 
	 * @return the total - the total sum of an order for which the given voucher is valid.
	 */
	@Accessor(qualifier = "total", type = Accessor.Type.GETTER)
	public Double getTotal()
	{
		return getPersistenceContext().getPropertyValue(TOTAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderRestriction.valueofgoodsonly</code> attribute defined at extension <code>voucher</code>. 
	 * @return the valueofgoodsonly - Specifies if total sum of an order is consulted inclusive or exclusive shipping and payment costs.
	 * 						Default is true (exclusive shipping costs).
	 */
	@Accessor(qualifier = "valueofgoodsonly", type = Accessor.Type.GETTER)
	public Boolean getValueofgoodsonly()
	{
		return getPersistenceContext().getPropertyValue(VALUEOFGOODSONLY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderRestriction.currency</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the currency - the currency of the total sum of an order for which the given voucher is valid.
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderRestriction.net</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the net - Specifies if total sum of an order is consulted inclusive or exclusive VAT. 
	 * 						Default is true (exclusive VAT).
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.SETTER)
	public void setNet(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(NET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderRestriction.total</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the total - the total sum of an order for which the given voucher is valid.
	 */
	@Accessor(qualifier = "total", type = Accessor.Type.SETTER)
	public void setTotal(final Double value)
	{
		getPersistenceContext().setPropertyValue(TOTAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderRestriction.valueofgoodsonly</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the valueofgoodsonly - Specifies if total sum of an order is consulted inclusive or exclusive shipping and payment costs.
	 * 						Default is true (exclusive shipping costs).
	 */
	@Accessor(qualifier = "valueofgoodsonly", type = Accessor.Type.SETTER)
	public void setValueofgoodsonly(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(VALUEOFGOODSONLY, value);
	}
	
}
