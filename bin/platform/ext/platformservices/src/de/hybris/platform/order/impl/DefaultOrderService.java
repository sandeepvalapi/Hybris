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
package de.hybris.platform.order.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.strategies.CreateOrderFromCartStrategy;
import de.hybris.platform.order.strategies.OrderCalculation;
import de.hybris.platform.order.strategies.PlaceOrderStrategy;
import de.hybris.platform.order.strategies.SubmitOrderStrategy;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link OrderService}.
 */
public class DefaultOrderService extends DefaultAbstractOrderService<OrderModel, OrderEntryModel> implements OrderService
{


	private static final Logger LOG = Logger.getLogger(DefaultOrderService.class);

	@SuppressWarnings("deprecation")
	private PlaceOrderStrategy placeOrderStrategy;
	@SuppressWarnings("deprecation")
	private OrderCalculation orderCalculation;
	private CreateOrderFromCartStrategy createOrderFromCartStrategy;
	private List<SubmitOrderStrategy> submitOrderStrategies = Collections.EMPTY_LIST;

	@Override
	public OrderModel createOrderFromCart(final CartModel cart) throws InvalidCartException
	{
		return createOrderFromCartStrategy.createOrderFromCart(cart);
	}


	@Override
	public void submitOrder(final OrderModel order)
	{
		for (final SubmitOrderStrategy strategy : submitOrderStrategies)
		{
			strategy.submitOrder(order);
		}
	}


	//Deprecations
	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public OrderModel placeOrder(final CartModel cart, final AddressModel deliveryAddress, final AddressModel paymentAddress,
			final PaymentInfoModel paymentInfo) throws InvalidCartException
	{
		return placeOrderStrategy.placeOrder(cart, deliveryAddress, paymentAddress, paymentInfo);
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public boolean calculateOrder(final AbstractOrderModel order)
	{
		return orderCalculation.calculate(order);
	}


	//Spring Injections
	/**
	 * @deprecated since ages
	 */
	@Deprecated
	public void setPlaceOrderStrategy(@SuppressWarnings("deprecation") final PlaceOrderStrategy placeOrderStrategy)
	{
		this.placeOrderStrategy = placeOrderStrategy;
	}


	/**
	 * @deprecated since ages
	 */
	@Deprecated
	public void setOrderCalculation(@SuppressWarnings("deprecation") final OrderCalculation orderCalculation)
	{
		this.orderCalculation = orderCalculation;
	}


	@Required
	public void setCreateOrderFromCartStrategy(final CreateOrderFromCartStrategy submitOrderStrategy)
	{
		this.createOrderFromCartStrategy = submitOrderStrategy;
	}


	public void setSubmitOrderStrategies(final List<SubmitOrderStrategy> submitOrderStrategies)
	{
		this.submitOrderStrategies = submitOrderStrategies;
	}

	@Override
	public OrderModel clone(final ComposedTypeModel orderType, final ComposedTypeModel entryType,
			final AbstractOrderModel original, final String code)
	{

		return (OrderModel) getCloneAbstractOrderStrategy().clone(orderType, entryType, original, code, OrderModel.class,
				OrderEntryModel.class);
	}

	@Override
	public OrderEntryModel addNewEntry(final OrderModel order, final ProductModel product, final long qty, final UnitModel unit,
			final int number, final boolean addToPresent)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		checkOrderEntryNumberCollisions(order, number);
		return super.addNewEntry(order, product, qty, unit, number, addToPresent);
	}

	@Override
	public AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final OrderModel order,
			final ProductModel product, final long qty, final UnitModel unit, final int number, final boolean addToPresent)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		ServicesUtil.validateParameterNotNullStandardMessage("entryType", entryType);
		checkOrderEntryNumberCollisions(order, number);
		return super.addNewEntry(entryType, order, product, qty, unit, number, addToPresent);
	}

	private void checkOrderEntryNumberCollisions(final OrderModel order, final int number)
	{
		final AbstractOrderEntryModel collidingEntry = getCollidingEntry(number, order);
		if (collidingEntry != null)
		{
			throw new AmbiguousIdentifierException("Cannot add new entry with number " + number + " as it would collide with "
					+ collidingEntry);
		}
	}
}
