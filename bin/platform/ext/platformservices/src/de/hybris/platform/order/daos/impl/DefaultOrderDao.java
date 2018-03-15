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
package de.hybris.platform.order.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.daos.OrderDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DefaultOrderDao extends AbstractItemDao implements OrderDao
{
	/**
	 * @deprecated since ages
	 */
	@Deprecated
	@Override
	public List<AbstractOrderEntryModel> findEntriesByNumber(final String entryTypeCode, final AbstractOrderModel order,
			final int number)
	{
		return findEntriesByNumber(order, number, number);
	}

	/**
	 * @deprecated since ages
	 */
	@Deprecated
	@Override
	public List<AbstractOrderEntryModel> findEntriesByNumber(final String entryTypeCode, final AbstractOrderModel order,
			final int start, final int end)
	{
		return findEntriesByNumber(order, start, end);
	}

	/**
	 * @deprecated since ages
	 */
	@Deprecated
	@Override
	public List<AbstractOrderEntryModel> findEntriesByProduct(final String entryTypeCode, final AbstractOrderModel order,
			final ProductModel product)
	{
		return findEntriesByProduct(order, product);
	}

	@Override
	public List<AbstractOrderEntryModel> findEntriesByNumber(final AbstractOrderModel order, final int number)
	{
		return findEntriesByNumber(order, number, number);
	}

	@Override
	public List<AbstractOrderEntryModel> findEntriesByNumber(final AbstractOrderModel order, final int start, final int end)
	{
		validateParameterNotNull(order, "order must not be null!");

		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries == null || entries.isEmpty() || end < start)
		{
			return Collections.EMPTY_LIST;
		}
		final List<AbstractOrderEntryModel> result = new ArrayList<>();
		for (final AbstractOrderEntryModel entry : entries)
		{

			if (entry != null && entry.getEntryNumber() != null && entry.getEntryNumber().intValue() >= start
					&& entry.getEntryNumber().intValue() <= end)
			{
				result.add(entry);
			}
		}
		return result;
	}

	@Override
	public List<AbstractOrderEntryModel> findEntriesByProduct(final AbstractOrderModel order, final ProductModel product)
	{
		validateParameterNotNull(order, "order must not be null!");
		validateParameterNotNull(product, "product must not be null!");

		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries == null || entries.isEmpty() || product.getPk() == null)
		{
			return Collections.EMPTY_LIST;
		}

		final List<AbstractOrderEntryModel> result = new ArrayList<>();
		final PK productPk = product.getPk();
		for (final AbstractOrderEntryModel entry : entries)
		{
			if (entry != null && entry.getProduct() != null && productPk.equals(entry.getProduct().getPk()))
			{
				result.add(entry);
			}
		}
		return result;
	}


	@Override
	public List<AbstractOrderModel> findOrdersByCurrency(final CurrencyModel currency)
	{
		validateParameterNotNull(currency, "currency must not be null!");
		//return findOrdersByModelAttribute(orderTypeCode, currency, AbstractOrderModel.CURRENCY);
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setCurrency(currency);
		return getFlexibleSearchService().getModelsByExample(order);

	}

	@Override
	public List<AbstractOrderModel> findOrdersByDelivereMode(final DeliveryModeModel deliveryMode)
	{
		validateParameterNotNull(deliveryMode, "deliveryMode must not be null!");
		//return findOrdersByModelAttribute(null, deliveryMode, AbstractOrderModel.DELIVERYMODE);
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setDeliveryMode(deliveryMode);
		return getFlexibleSearchService().getModelsByExample(order);
	}

	@Override
	public List<AbstractOrderModel> findOrdersByPaymentMode(final PaymentModeModel paymentMode)
	{
		validateParameterNotNull(paymentMode, "paymentMode must not be null!");
		//return findOrdersByModelAttribute(null, paymentMode, AbstractOrderModel.PAYMENTMODE);
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setPaymentMode(paymentMode);
		return getFlexibleSearchService().getModelsByExample(order);
	}
}
