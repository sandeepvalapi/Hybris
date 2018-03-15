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
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CouponRedemption first defined at extension couponservices.
 */
@SuppressWarnings("all")
public class CouponRedemptionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CouponRedemption";
	
	/** <i>Generated constant</i> - Attribute key of <code>CouponRedemption.couponCode</code> attribute defined at extension <code>couponservices</code>. */
	public static final String COUPONCODE = "couponCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CouponRedemption.coupon</code> attribute defined at extension <code>couponservices</code>. */
	public static final String COUPON = "coupon";
	
	/** <i>Generated constant</i> - Attribute key of <code>CouponRedemption.order</code> attribute defined at extension <code>couponservices</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>CouponRedemption.user</code> attribute defined at extension <code>couponservices</code>. */
	public static final String USER = "user";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CouponRedemptionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CouponRedemptionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _coupon initial attribute declared by type <code>CouponRedemption</code> at extension <code>couponservices</code>
	 * @param _couponCode initial attribute declared by type <code>CouponRedemption</code> at extension <code>couponservices</code>
	 */
	@Deprecated
	public CouponRedemptionModel(final AbstractCouponModel _coupon, final String _couponCode)
	{
		super();
		setCoupon(_coupon);
		setCouponCode(_couponCode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _coupon initial attribute declared by type <code>CouponRedemption</code> at extension <code>couponservices</code>
	 * @param _couponCode initial attribute declared by type <code>CouponRedemption</code> at extension <code>couponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CouponRedemptionModel(final AbstractCouponModel _coupon, final String _couponCode, final ItemModel _owner)
	{
		super();
		setCoupon(_coupon);
		setCouponCode(_couponCode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponRedemption.coupon</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the coupon
	 */
	@Accessor(qualifier = "coupon", type = Accessor.Type.GETTER)
	public AbstractCouponModel getCoupon()
	{
		return getPersistenceContext().getPropertyValue(COUPON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponRedemption.couponCode</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the couponCode
	 */
	@Accessor(qualifier = "couponCode", type = Accessor.Type.GETTER)
	public String getCouponCode()
	{
		return getPersistenceContext().getPropertyValue(COUPONCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponRedemption.order</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public AbstractOrderModel getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CouponRedemption.user</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CouponRedemption.coupon</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the coupon
	 */
	@Accessor(qualifier = "coupon", type = Accessor.Type.SETTER)
	public void setCoupon(final AbstractCouponModel value)
	{
		getPersistenceContext().setPropertyValue(COUPON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CouponRedemption.couponCode</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the couponCode
	 */
	@Accessor(qualifier = "couponCode", type = Accessor.Type.SETTER)
	public void setCouponCode(final String value)
	{
		getPersistenceContext().setPropertyValue(COUPONCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CouponRedemption.order</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final AbstractOrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CouponRedemption.user</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
