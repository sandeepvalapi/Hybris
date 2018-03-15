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
package de.hybris.platform.order.strategies.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.CartValidator;
import de.hybris.platform.order.strategies.OrderCalculation;
import de.hybris.platform.order.strategies.PlaceOrderStrategy;
import de.hybris.platform.order.strategies.CreateOrderFromCartStrategy;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link PlaceOrderStrategy}.
 * 
 * @deprecated since ages - Implement{@link CreateOrderFromCartStrategy} instead or use the default one - {@link DefaultCreateOrderFromCartStrategy}
 * 
 */
@Deprecated
public class DefaultPlaceOrderStrategy extends AbstractBusinessService implements PlaceOrderStrategy
{
	private OrderCalculation orderCalculation;
	private UserService userService;
	private CartValidator cartValidator;
	private KeyGenerator keyGenerator;
	private CartService cartService;

	@Override
	public OrderModel placeOrder(final CartModel cart, final AddressModel deliveryAddress, final AddressModel paymentAddress,
			final PaymentInfoModel paymentInfo) throws InvalidCartException
	{
		//TODO: InvalidCartException is never thrown! but ModelSavingExceptions

		orderCalculation.calculate(cart);
		final UserModel user = userService.getCurrentUser();
		if (deliveryAddress != null)
		{
			if (getModelService().isNew(deliveryAddress))
			{
				deliveryAddress.setOwner(user);
			}
			getModelService().save(deliveryAddress);
			cart.setDeliveryAddress(deliveryAddress);
		}
		if (paymentAddress != null)
		{
			if (getModelService().isNew(paymentAddress))
			{
				paymentAddress.setOwner(user);
			}
			getModelService().save(paymentAddress);
			cart.setPaymentAddress(paymentAddress);
		}
		if (paymentInfo != null)
		{
			paymentInfo.setCode(String.valueOf(keyGenerator.generate()));
			if (getModelService().isNew(paymentInfo))
			{
				paymentInfo.setUser(user);
			}
			getModelService().save(paymentInfo);
			cart.setPaymentInfo(paymentInfo);
		}
		cart.setUser(user);
		cartValidator.validateCart(cart);
		getModelService().save(cart);
		final Cart cartItem = getModelService().getSource(cart);
		final Order orderItem = OrderManager.getInstance().createOrder(cartItem);
		final OrderModel order = getModelService().get(orderItem);
		orderCalculation.calculate(order);
		cartService.removeSessionCart();
		return order;
	}

	@Required
	public void setOrderCalculation(final OrderCalculation orderCalculation)
	{
		this.orderCalculation = orderCalculation;
	}

	@Required
	public void setCartValidator(final CartValidator cartValidator)
	{
		this.cartValidator = cartValidator;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Required
	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

}
