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
package de.hybris.platform.order.strategies;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;


/**
 * The place order strategy.
 * 
 * @spring.bean placeOrderStrategy
 * @deprecated since ages - Obsolete strategy definition. Use{@link CreateOrderFromCartStrategy} for order placement instead.
 */
@Deprecated
public interface PlaceOrderStrategy
{
	/**
	 * Place the order for the given <code>cart</code>. This means: first the cart is calculated (see
	 * {@link OrderService#calculateOrder(de.hybris.platform.core.model.order.AbstractOrderModel)} than (if not
	 * <code>null</code>) the <code>deliveryAddress</code> and <code>paymentAddress</code> and <code>paymentInfo</code>
	 * are stored for the current session user and also to the cart. After this from the <code>cart</code> an
	 * {@link OrderModel} is created and the cart is removed from the session.
	 * 
	 * @param cart
	 *           the {@link CartModel}
	 * @param deliveryAddress
	 *           the delivery {@link AddressModel} for the current session user. Can be <code>null</code>.
	 * @param paymentAddress
	 *           the payment {@link AddressModel} for the current session user. Can be <code>null</code>.
	 * @param paymentInfo
	 *           the {@link PaymentInfoModel} for the current session user. Can be <code>null</code>.
	 * @return the {@link OrderModel}
	 * @throws InvalidCartException
	 */
	OrderModel placeOrder(CartModel cart, AddressModel deliveryAddress, AddressModel paymentAddress, PaymentInfoModel paymentInfo)
			throws InvalidCartException;
}
