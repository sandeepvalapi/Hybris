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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.VoucherModel;

/**
 * Generated model class for type VoucherInvalidation first defined at extension voucher.
 */
@SuppressWarnings("all")
public class VoucherInvalidationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "VoucherInvalidation";
	
	/**<i>Generated relation code constant for relation <code>VoucherInvalidationsRelation</code> defining source attribute <code>voucher</code> in extension <code>voucher</code>.</i>*/
	public static final String _VOUCHERINVALIDATIONSRELATION = "VoucherInvalidationsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>VoucherInvalidation.code</code> attribute defined at extension <code>voucher</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>VoucherInvalidation.user</code> attribute defined at extension <code>voucher</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>VoucherInvalidation.order</code> attribute defined at extension <code>voucher</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>VoucherInvalidation.status</code> attribute defined at extension <code>voucher</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>VoucherInvalidation.voucher</code> attribute defined at extension <code>voucher</code>. */
	public static final String VOUCHER = "voucher";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public VoucherInvalidationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public VoucherInvalidationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>VoucherInvalidation</code> at extension <code>voucher</code>
	 * @param _order initial attribute declared by type <code>VoucherInvalidation</code> at extension <code>voucher</code>
	 * @param _user initial attribute declared by type <code>VoucherInvalidation</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>VoucherInvalidation</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public VoucherInvalidationModel(final String _code, final OrderModel _order, final UserModel _user, final VoucherModel _voucher)
	{
		super();
		setCode(_code);
		setOrder(_order);
		setUser(_user);
		setVoucher(_voucher);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>VoucherInvalidation</code> at extension <code>voucher</code>
	 * @param _order initial attribute declared by type <code>VoucherInvalidation</code> at extension <code>voucher</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>VoucherInvalidation</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>VoucherInvalidation</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public VoucherInvalidationModel(final String _code, final OrderModel _order, final ItemModel _owner, final UserModel _user, final VoucherModel _voucher)
	{
		super();
		setCode(_code);
		setOrder(_order);
		setOwner(_owner);
		setUser(_user);
		setVoucher(_voucher);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VoucherInvalidation.code</code> attribute defined at extension <code>voucher</code>. 
	 * @return the code - the code used redeeming the voucher.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VoucherInvalidation.order</code> attribute defined at extension <code>voucher</code>. 
	 * @return the order - the order for which the voucher code was applied.
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public OrderModel getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VoucherInvalidation.status</code> attribute defined at extension <code>voucher</code>. 
	 * @return the status - the status of the invalidation.
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public String getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VoucherInvalidation.user</code> attribute defined at extension <code>voucher</code>. 
	 * @return the user - the user who redeemed the voucher.
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VoucherInvalidation.voucher</code> attribute defined at extension <code>voucher</code>. 
	 * @return the voucher
	 */
	@Accessor(qualifier = "voucher", type = Accessor.Type.GETTER)
	public VoucherModel getVoucher()
	{
		return getPersistenceContext().getPropertyValue(VOUCHER);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>VoucherInvalidation.code</code> attribute defined at extension <code>voucher</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - the code used redeeming the voucher.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>VoucherInvalidation.order</code> attribute defined at extension <code>voucher</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the order - the order for which the voucher code was applied.
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final OrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VoucherInvalidation.status</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the status - the status of the invalidation.
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final String value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>VoucherInvalidation.user</code> attribute defined at extension <code>voucher</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the user - the user who redeemed the voucher.
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>VoucherInvalidation.voucher</code> attribute defined at extension <code>voucher</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the voucher
	 */
	@Accessor(qualifier = "voucher", type = Accessor.Type.SETTER)
	public void setVoucher(final VoucherModel value)
	{
		getPersistenceContext().setPropertyValue(VOUCHER, value);
	}
	
}
