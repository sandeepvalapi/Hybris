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
package de.hybris.platform.orderscheduling.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CartToOrderCronJob first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class CartToOrderCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CartToOrderCronJob";
	
	/**<i>Generated relation code constant for relation <code>Cart2CartToOrderCronJob</code> defining source attribute <code>cart</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _CART2CARTTOORDERCRONJOB = "Cart2CartToOrderCronJob";
	
	/**<i>Generated relation code constant for relation <code>DeliveryAddresss2CartToOrderCronJob</code> defining source attribute <code>deliveryAddress</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _DELIVERYADDRESSS2CARTTOORDERCRONJOB = "DeliveryAddresss2CartToOrderCronJob";
	
	/**<i>Generated relation code constant for relation <code>PaymentAddresss2CartToOrderCronJob</code> defining source attribute <code>paymentAddress</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _PAYMENTADDRESSS2CARTTOORDERCRONJOB = "PaymentAddresss2CartToOrderCronJob";
	
	/**<i>Generated relation code constant for relation <code>PaymentInfo2CartToOrderCronJob</code> defining source attribute <code>paymentInfo</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _PAYMENTINFO2CARTTOORDERCRONJOB = "PaymentInfo2CartToOrderCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CartToOrderCronJob.cart</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CART = "cart";
	
	/** <i>Generated constant</i> - Attribute key of <code>CartToOrderCronJob.deliveryAddress</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DELIVERYADDRESS = "deliveryAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>CartToOrderCronJob.paymentAddress</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PAYMENTADDRESS = "paymentAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>CartToOrderCronJob.paymentInfo</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PAYMENTINFO = "paymentInfo";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CartToOrderCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CartToOrderCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CartToOrderCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CartToOrderCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CartToOrderCronJob.cart</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the cart
	 */
	@Accessor(qualifier = "cart", type = Accessor.Type.GETTER)
	public CartModel getCart()
	{
		return getPersistenceContext().getPropertyValue(CART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CartToOrderCronJob.deliveryAddress</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the deliveryAddress
	 */
	@Accessor(qualifier = "deliveryAddress", type = Accessor.Type.GETTER)
	public AddressModel getDeliveryAddress()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CartToOrderCronJob.paymentAddress</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the paymentAddress
	 */
	@Accessor(qualifier = "paymentAddress", type = Accessor.Type.GETTER)
	public AddressModel getPaymentAddress()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CartToOrderCronJob.paymentInfo</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the paymentInfo
	 */
	@Accessor(qualifier = "paymentInfo", type = Accessor.Type.GETTER)
	public PaymentInfoModel getPaymentInfo()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTINFO);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CartToOrderCronJob.cart</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the cart
	 */
	@Accessor(qualifier = "cart", type = Accessor.Type.SETTER)
	public void setCart(final CartModel value)
	{
		getPersistenceContext().setPropertyValue(CART, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CartToOrderCronJob.deliveryAddress</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the deliveryAddress
	 */
	@Accessor(qualifier = "deliveryAddress", type = Accessor.Type.SETTER)
	public void setDeliveryAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CartToOrderCronJob.paymentAddress</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the paymentAddress
	 */
	@Accessor(qualifier = "paymentAddress", type = Accessor.Type.SETTER)
	public void setPaymentAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CartToOrderCronJob.paymentInfo</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the paymentInfo
	 */
	@Accessor(qualifier = "paymentInfo", type = Accessor.Type.SETTER)
	public void setPaymentInfo(final PaymentInfoModel value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTINFO, value);
	}
	
}
