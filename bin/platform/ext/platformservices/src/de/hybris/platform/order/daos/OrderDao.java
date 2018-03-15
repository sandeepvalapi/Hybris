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
package de.hybris.platform.order.daos;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collections;
import java.util.List;


/**
 * Data Access Object oriented on orders and order entries
 */
public interface OrderDao
{
	/**
	 * Returns order entries with the matching order entry number
	 * 
	 * @param entryTypeCode
	 *           - entries of this specific type will be searched. I.e 'OrderEntry', 'CartEntry'
	 * @param number
	 *           - requested entry number
	 * @param order
	 *           - target order
	 * 
	 * @return List of matching order entries, or {@link Collections#EMPTY_LIST} in case if no entries were found.
	 * 
	 * @deprecated since ages - use{@link #findEntriesByNumber(AbstractOrderModel, int)}
	 */
	@Deprecated
	List<AbstractOrderEntryModel> findEntriesByNumber(String entryTypeCode, AbstractOrderModel order, int number);

	/**
	 * Returns order entries with the matching order entry number
	 * 
	 * @param number
	 *           - requested entry number
	 * @param order
	 *           - target order
	 * 
	 * @return List of matching order entries, or {@link Collections#EMPTY_LIST} in case if no entries were found.
	 */
	List<AbstractOrderEntryModel> findEntriesByNumber(AbstractOrderModel order, int number);


	/**
	 * Returns order entries with the order entry number from the requested range
	 * 
	 * @param entryTypeCode
	 *           - entries of this specific type will be searched. I.e 'OrderEntry', 'CartEntry'
	 * 
	 * @param order
	 *           - target order
	 * @param start
	 *           lower range limit
	 * @param end
	 *           upper range limit
	 * 
	 * @return List of matching order entries, or {@link Collections#EMPTY_LIST} in case if no entries were found.
	 * 
	 * @deprecated since ages - use{@link #findEntriesByNumber(AbstractOrderModel, int, int)}
	 */
	@Deprecated
	List<AbstractOrderEntryModel> findEntriesByNumber(String entryTypeCode, AbstractOrderModel order, int start, int end);

	/**
	 * Returns order entries with the order entry number from the requested range
	 *
	 * @param order
	 *           - target order
	 * @param start
	 *           lower range limit
	 * @param end
	 *           upper range limit
	 * 
	 * @return List of matching order entries, or {@link Collections#EMPTY_LIST} in case if no entries were found.
	 */
	List<AbstractOrderEntryModel> findEntriesByNumber(AbstractOrderModel order, int start, int end);

	/**
	 * Returns order entries with the matching product
	 * 
	 * @param entryTypeCode
	 *           - entries of this specific type will be searched. I.e 'OrderEntry', 'CartEntry'
	 * @param product
	 *           - requested {@link ProductModel}
	 * @param order
	 *           - target order
	 * 
	 * @return List of matching order entries, or {@link Collections#EMPTY_LIST} in case if no entries were found.
	 * 
	 * @deprecated since ages - use{@link #findEntriesByProduct(AbstractOrderModel, ProductModel)}
	 */
	@Deprecated
	List<AbstractOrderEntryModel> findEntriesByProduct(String entryTypeCode, final AbstractOrderModel order,
			final ProductModel product);

	/**
	 * Returns order entries with the matching product
	 * 
	 * @param product
	 *           - requested {@link ProductModel}
	 * @param order
	 *           - target order
	 * 
	 * @return List of matching order entries, or {@link Collections#EMPTY_LIST} in case if no entries were found.
	 */
	List<AbstractOrderEntryModel> findEntriesByProduct(final AbstractOrderModel order, final ProductModel product);

	/**
	 * Returns orders of the type specified with the given currency.
	 * 
	 * @param currency
	 *           the target currency
	 * 
	 * @return {@link List} of {@link AbstractOrderModel} - matched orders
	 * @throws IllegalArgumentException
	 *            if currency is null
	 * @throws IllegalStateException
	 *            if currency is not persisted.
	 */
	List<AbstractOrderModel> findOrdersByCurrency(CurrencyModel currency);

	/**
	 * Returns orders with the given delivery mode.
	 * 
	 * @param deliveryMode
	 *           target {@link DeliveryModeModel}
	 * 
	 * @return {@link List} of {@link AbstractOrderModel} - matched orders
	 * @throws IllegalArgumentException
	 *            if deliveryMode is null
	 * @throws IllegalStateException
	 *            if deliveryMode is not persisted.
	 */
	List<AbstractOrderModel> findOrdersByDelivereMode(DeliveryModeModel deliveryMode);

	/**
	 * Returns orders with the given payment mode.
	 * 
	 * @param paymentMode
	 *           target {@link PaymentModeModel}
	 * 
	 * @return {@link List} of {@link AbstractOrderModel} - matched orders
	 * @throws IllegalArgumentException
	 *            if paymentMode is null
	 * @throws IllegalStateException
	 *            if paymentMode is not persisted.
	 */
	List<AbstractOrderModel> findOrdersByPaymentMode(PaymentModeModel paymentMode);

}
