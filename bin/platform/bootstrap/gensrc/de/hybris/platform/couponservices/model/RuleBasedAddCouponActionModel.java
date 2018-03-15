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
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RuleBasedAddCouponAction first defined at extension couponservices.
 */
@SuppressWarnings("all")
public class RuleBasedAddCouponActionModel extends AbstractRuleBasedPromotionActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleBasedAddCouponAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleBasedAddCouponAction.couponId</code> attribute defined at extension <code>couponservices</code>. */
	public static final String COUPONID = "couponId";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleBasedAddCouponAction.couponCode</code> attribute defined at extension <code>couponservices</code>. */
	public static final String COUPONCODE = "couponCode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleBasedAddCouponActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleBasedAddCouponActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>RuleBasedAddCouponAction</code> at extension <code>couponservices</code>
	 */
	@Deprecated
	public RuleBasedAddCouponActionModel(final String _couponId)
	{
		super();
		setCouponId(_couponId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>RuleBasedAddCouponAction</code> at extension <code>couponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RuleBasedAddCouponActionModel(final String _couponId, final ItemModel _owner)
	{
		super();
		setCouponId(_couponId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleBasedAddCouponAction.couponCode</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the couponCode - Coupon Code
	 */
	@Accessor(qualifier = "couponCode", type = Accessor.Type.GETTER)
	public String getCouponCode()
	{
		return getPersistenceContext().getPropertyValue(COUPONCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleBasedAddCouponAction.couponId</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the couponId - Coupon Id
	 */
	@Accessor(qualifier = "couponId", type = Accessor.Type.GETTER)
	public String getCouponId()
	{
		return getPersistenceContext().getPropertyValue(COUPONID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleBasedAddCouponAction.couponCode</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the couponCode - Coupon Code
	 */
	@Accessor(qualifier = "couponCode", type = Accessor.Type.SETTER)
	public void setCouponCode(final String value)
	{
		getPersistenceContext().setPropertyValue(COUPONCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleBasedAddCouponAction.couponId</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the couponId - Coupon Id
	 */
	@Accessor(qualifier = "couponId", type = Accessor.Type.SETTER)
	public void setCouponId(final String value)
	{
		getPersistenceContext().setPropertyValue(COUPONID, value);
	}
	
}
