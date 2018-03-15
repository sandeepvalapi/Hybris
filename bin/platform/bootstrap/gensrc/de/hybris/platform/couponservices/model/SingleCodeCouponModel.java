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
package de.hybris.platform.couponservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SingleCodeCoupon first defined at extension couponservices.
 * <p>
 * A SingleCodeCoupon uses one coupon code (i.e. the inherited couponId attribute) for coupon redemption.
 * 				The additional attributes allow to restrict the number of times the coupon can be redeemed.
 */
@SuppressWarnings("all")
public class SingleCodeCouponModel extends AbstractCouponModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SingleCodeCoupon";
	
	/** <i>Generated constant</i> - Attribute key of <code>SingleCodeCoupon.maxRedemptionsPerCustomer</code> attribute defined at extension <code>couponservices</code>. */
	public static final String MAXREDEMPTIONSPERCUSTOMER = "maxRedemptionsPerCustomer";
	
	/** <i>Generated constant</i> - Attribute key of <code>SingleCodeCoupon.maxTotalRedemptions</code> attribute defined at extension <code>couponservices</code>. */
	public static final String MAXTOTALREDEMPTIONS = "maxTotalRedemptions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SingleCodeCouponModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SingleCodeCouponModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 */
	@Deprecated
	public SingleCodeCouponModel(final String _couponId)
	{
		super();
		setCouponId(_couponId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SingleCodeCouponModel(final String _couponId, final ItemModel _owner)
	{
		super();
		setCouponId(_couponId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SingleCodeCoupon.maxRedemptionsPerCustomer</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the maxRedemptionsPerCustomer
	 */
	@Accessor(qualifier = "maxRedemptionsPerCustomer", type = Accessor.Type.GETTER)
	public Integer getMaxRedemptionsPerCustomer()
	{
		return getPersistenceContext().getPropertyValue(MAXREDEMPTIONSPERCUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SingleCodeCoupon.maxTotalRedemptions</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the maxTotalRedemptions
	 */
	@Accessor(qualifier = "maxTotalRedemptions", type = Accessor.Type.GETTER)
	public Integer getMaxTotalRedemptions()
	{
		return getPersistenceContext().getPropertyValue(MAXTOTALREDEMPTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SingleCodeCoupon.maxRedemptionsPerCustomer</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the maxRedemptionsPerCustomer
	 */
	@Accessor(qualifier = "maxRedemptionsPerCustomer", type = Accessor.Type.SETTER)
	public void setMaxRedemptionsPerCustomer(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXREDEMPTIONSPERCUSTOMER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SingleCodeCoupon.maxTotalRedemptions</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the maxTotalRedemptions
	 */
	@Accessor(qualifier = "maxTotalRedemptions", type = Accessor.Type.SETTER)
	public void setMaxTotalRedemptions(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXTOTALREDEMPTIONS, value);
	}
	
}
