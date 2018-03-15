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
import de.hybris.platform.voucher.model.VoucherModel;

/**
 * Generated model class for type PromotionVoucher first defined at extension voucher.
 */
@SuppressWarnings("all")
public class PromotionVoucherModel extends VoucherModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PromotionVoucher";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionVoucher.voucherCode</code> attribute defined at extension <code>voucher</code>. */
	public static final String VOUCHERCODE = "voucherCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionVoucher.redemptionQuantityLimit</code> attribute defined at extension <code>voucher</code>. */
	public static final String REDEMPTIONQUANTITYLIMIT = "redemptionQuantityLimit";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionVoucher.redemptionQuantityLimitPerUser</code> attribute defined at extension <code>voucher</code>. */
	public static final String REDEMPTIONQUANTITYLIMITPERUSER = "redemptionQuantityLimitPerUser";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PromotionVoucherModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PromotionVoucherModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Voucher</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public PromotionVoucherModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Voucher</code> at extension <code>voucher</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PromotionVoucherModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionVoucher.redemptionQuantityLimit</code> attribute defined at extension <code>voucher</code>. 
	 * @return the redemptionQuantityLimit - the upper limit of uses possible for this voucher.
	 */
	@Accessor(qualifier = "redemptionQuantityLimit", type = Accessor.Type.GETTER)
	public Integer getRedemptionQuantityLimit()
	{
		return getPersistenceContext().getPropertyValue(REDEMPTIONQUANTITYLIMIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionVoucher.redemptionQuantityLimitPerUser</code> attribute defined at extension <code>voucher</code>. 
	 * @return the redemptionQuantityLimitPerUser - the upper limit of voucher uses possible per user. Default is one.
	 */
	@Accessor(qualifier = "redemptionQuantityLimitPerUser", type = Accessor.Type.GETTER)
	public Integer getRedemptionQuantityLimitPerUser()
	{
		return getPersistenceContext().getPropertyValue(REDEMPTIONQUANTITYLIMITPERUSER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionVoucher.voucherCode</code> attribute defined at extension <code>voucher</code>. 
	 * @return the voucherCode - the voucher code.
	 */
	@Accessor(qualifier = "voucherCode", type = Accessor.Type.GETTER)
	public String getVoucherCode()
	{
		return getPersistenceContext().getPropertyValue(VOUCHERCODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionVoucher.redemptionQuantityLimit</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the redemptionQuantityLimit - the upper limit of uses possible for this voucher.
	 */
	@Accessor(qualifier = "redemptionQuantityLimit", type = Accessor.Type.SETTER)
	public void setRedemptionQuantityLimit(final Integer value)
	{
		getPersistenceContext().setPropertyValue(REDEMPTIONQUANTITYLIMIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionVoucher.redemptionQuantityLimitPerUser</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the redemptionQuantityLimitPerUser - the upper limit of voucher uses possible per user. Default is one.
	 */
	@Accessor(qualifier = "redemptionQuantityLimitPerUser", type = Accessor.Type.SETTER)
	public void setRedemptionQuantityLimitPerUser(final Integer value)
	{
		getPersistenceContext().setPropertyValue(REDEMPTIONQUANTITYLIMITPERUSER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionVoucher.voucherCode</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the voucherCode - the voucher code.
	 */
	@Accessor(qualifier = "voucherCode", type = Accessor.Type.SETTER)
	public void setVoucherCode(final String value)
	{
		getPersistenceContext().setPropertyValue(VOUCHERCODE, value);
	}
	
}
