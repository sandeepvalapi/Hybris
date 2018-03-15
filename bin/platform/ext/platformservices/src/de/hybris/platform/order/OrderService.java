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
package de.hybris.platform.order;

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
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.order.strategies.CartValidator;
import de.hybris.platform.order.strategies.OrderCalculation;
import de.hybris.platform.order.strategies.impl.EventPublishingSubmitOrderStrategy;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;


/**
 * Business service for handling orders. Use this service to place new orders, recalculate orders. It allows adding new
 * order entries, as well as finding entries by product or entry number.
 * 
 * @spring.bean orderService
 */
public interface OrderService extends AbstractOrderService<OrderModel, OrderEntryModel>
{

	/**
	 * Create the order for the given <code>cart</code>. This method focuses on creating an {@link OrderModel} instance
	 * from the given {@link CartModel} instance. The order instance remains unsaved and not calculated.</br> This method
	 * does nothing with the cart member attributes (addresses, paymentInfo). It also leaves the target cart untouched.
	 * 
	 * If you want to calculate cart or order, use {@link CalculationService}.
	 * 
	 * 
	 * @param cart
	 *           the target {@link CartModel}
	 * @return a non persisted {@link OrderModel}
	 * @throws InvalidCartException
	 *            if the cart is invalid according to the used {@link CartValidator}.
	 */
	OrderModel createOrderFromCart(CartModel cart) throws InvalidCartException;

	/**
	 * Submits an order. Fire list of submit order strategies. Default implementation
	 * {@link EventPublishingSubmitOrderStrategy} fires {@link SubmitOrderEvent}.
	 * 
	 * @param order
	 *           Order to submit.
	 */
	void submitOrder(OrderModel order);

	/**
	 * Place the order for the given <code>cart</code>. This means: first the cart is calculated (see
	 * {@link OrderService#calculateOrder(AbstractOrderModel)} than (if not <code>null</code>) the
	 * <code>deliveryAddress</code> and <code>paymentAddress</code> and <code>paymentInfo</code> are stored for the
	 * current session user and also to the cart. After this from the <code>cart</code> an {@link OrderModel} is created
	 * and the cart is removed from the session.
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
	 * 
	 * @deprecated since ages - Use lightweight{@link #createOrderFromCart(CartModel)} instead or your own implementation of order
	 *             placement flow.
	 */
	@Deprecated
	OrderModel placeOrder(final CartModel cart, AddressModel deliveryAddress, AddressModel paymentAddress,
			PaymentInfoModel paymentInfo) throws InvalidCartException;

	/**
	 * Calculates the given <code>order</code> and returns <code>true</code> if each entry and after this the
	 * {@link AbstractOrderModel} was calculated. Thereby any invalid entry will be automatically removed.
	 * 
	 * The default implementation delegates to the {@link OrderCalculation#calculate(AbstractOrderModel)} strategy.
	 * Please check the API doc of your current calculation strategy.
	 * 
	 * @param order
	 *           the {@link AbstractOrderModel}
	 * @return <code>false</code> if the <code>order</code> was already calculated.
	 * @deprecated since ages - Use{@link CalculationService} to calculate orders.
	 */
	@Deprecated
	boolean calculateOrder(AbstractOrderModel order);

	/**
	 * Adds a new order entry to the given order on the required entry number. The new entry is neither saved nor
	 * calculated. If requested entryNumber collide with existing entry, an {@link AmbiguousIdentifierException} will be
	 * thrown.
	 * 
	 * @param order
	 *           - target order
	 * 
	 * @param product
	 *           -product to add, must not be null
	 * @param qty
	 *           - quantity
	 * @param unit
	 *           - must not be null
	 * @param number
	 *           - entry number of the new entry in the order. Entries are indexed starting from 0. Set number to -1 if
	 *           you want to append the entry as the last one. You can request any non-negative, that is not already
	 *           occupied by existing order entry.
	 * @param addToPresent
	 *           - if true an existing entry with matching product <b>and</b> unit will get its quantity increased;
	 *           otherwise a new entry is created
	 * @return {@link AbstractOrderEntryModel} - newly created order entry
	 * 
	 * @throws IllegalArgumentException
	 *            if either order or product is null or quantity is negative.
	 * @throws AmbiguousIdentifierException
	 *            if there is already entry with the requested number.
	 * 
	 * @see AbstractOrderEntryTypeService#getAbstractOrderEntryType(AbstractOrderModel)
	 */
	@Override
	OrderEntryModel addNewEntry(final OrderModel order, final ProductModel product, final long qty, final UnitModel unit,
			final int number, final boolean addToPresent);

	/**
	 * Adds a new entry of the given {@link ComposedTypeModel} to the given order. The new entry is neither saved nor
	 * calculated. If requested entryNumber collide with existing entry, an {@link AmbiguousIdentifierException} will be
	 * thrown.
	 * 
	 * @param entryType
	 *           - the requested sub-type AbstractOrderEntry
	 * @param order
	 *           - target order
	 * 
	 * @param product
	 *           -product to add, must not be null
	 * @param qty
	 *           - quantity
	 * @param unit
	 *           - must not be null
	 * @param number
	 *           - entry number of the new entry in the order. Entries are indexed starting from 0. Set number to -1 if
	 *           you want to append the entry as the last one. You can request any non-negative, that is not already
	 *           occupied by existing order entry.
	 * @param addToPresent
	 *           - if true an existing entry with matching product <b>and</b> unit will get its quantity increased;
	 *           otherwise a new entry is created
	 * @return {@link AbstractOrderEntryModel} - newly created order entry
	 * 
	 * @throws IllegalArgumentException
	 *            if either entryType or order or product is null or quantity is negative.
	 * 
	 * @throws AmbiguousIdentifierException
	 *            if there is already entry with the requested number.
	 * 
	 */
	@Override
	AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final OrderModel order, final ProductModel product,
			final long qty, final UnitModel unit, final int number, final boolean addToPresent);


}
