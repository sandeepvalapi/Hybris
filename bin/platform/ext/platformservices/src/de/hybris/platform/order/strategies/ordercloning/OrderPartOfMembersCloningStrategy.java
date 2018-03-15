/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.order.strategies.ordercloning;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;


/**
 * Defines a strategy for cloning the order's contract members. Regulates the rules whether:<br>
 * <ul>
 * <li>{@link AbstractOrderModel#PAYMENTADDRESS},</li>
 * <li>{@link AbstractOrderModel#DELIVERYADDRESS},</li>
 * <li>{@link AbstractOrderModel#PAYMENTINFO}</li>
 * </ul>
 * need to be cloned for the given case. Allow to trigger cloning actions.
 */
public interface OrderPartOfMembersCloningStrategy
{

	/**
	 * Clones an address and sets the order as the clone's owner.
	 * 
	 * @param address
	 *           address to clone
	 * @param order
	 *           owning order
	 * @return cloned but <b>not persisted</b> {@link AddressModel} instance.
	 */
	AddressModel cloneAddressForOrder(AddressModel address, OrderModel order);

	/**
	 * Checks according to business strategies whether given address needs to be cloned as a part of order's contract.
	 * 
	 * @param address
	 * 
	 * @param order
	 * 
	 * @return true if this address needs to be cloned.
	 * @throws IllegalArgumentException
	 *            if order is null
	 */
	boolean addressNeedsCloning(AddressModel address, OrderModel order);

	/**
	 * Clones a payment info and sets the order as the clone's owner.
	 * 
	 * @param paymentInfo
	 *           payment info to clone
	 * @param order
	 *           owning order
	 * @return cloned but <b>not persisted</b> {@link PaymentInfoModel} instance.
	 */
	PaymentInfoModel clonePaymentInfoForOrder(PaymentInfoModel paymentInfo, OrderModel order);

	/**
	 * Checks according to business strategies whether given payment info needs to be cloned as a part of order's
	 * contract.
	 * 
	 * @param paymentInfo
	 * 
	 * @param order
	 * 
	 * @return true if this address needs to be cloned.
	 * @throws IllegalArgumentException
	 *            if order is null
	 */
	boolean paymentInfoNeedsCloning(PaymentInfoModel paymentInfo, OrderModel order);
}
