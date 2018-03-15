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
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

/**
 * Generated model class for type RegularCustomerOrderQuantityRestriction first defined at extension voucher.
 */
@SuppressWarnings("all")
public class RegularCustomerOrderQuantityRestrictionModel extends RestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RegularCustomerOrderQuantityRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>RegularCustomerOrderQuantityRestriction.orderQuantity</code> attribute defined at extension <code>voucher</code>. */
	public static final String ORDERQUANTITY = "orderQuantity";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RegularCustomerOrderQuantityRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RegularCustomerOrderQuantityRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _orderQuantity initial attribute declared by type <code>RegularCustomerOrderQuantityRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public RegularCustomerOrderQuantityRestrictionModel(final Integer _orderQuantity, final VoucherModel _voucher)
	{
		super();
		setOrderQuantity(_orderQuantity);
		setVoucher(_voucher);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _orderQuantity initial attribute declared by type <code>RegularCustomerOrderQuantityRestriction</code> at extension <code>voucher</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public RegularCustomerOrderQuantityRestrictionModel(final Integer _orderQuantity, final ItemModel _owner, final VoucherModel _voucher)
	{
		super();
		setOrderQuantity(_orderQuantity);
		setOwner(_owner);
		setVoucher(_voucher);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegularCustomerOrderQuantityRestriction.orderQuantity</code> attribute defined at extension <code>voucher</code>. 
	 * @return the orderQuantity - restrict to customers with a total of x completed orders.
	 */
	@Accessor(qualifier = "orderQuantity", type = Accessor.Type.GETTER)
	public Integer getOrderQuantity()
	{
		return getPersistenceContext().getPropertyValue(ORDERQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RegularCustomerOrderQuantityRestriction.orderQuantity</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the orderQuantity - restrict to customers with a total of x completed orders.
	 */
	@Accessor(qualifier = "orderQuantity", type = Accessor.Type.SETTER)
	public void setOrderQuantity(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ORDERQUANTITY, value);
	}
	
}
