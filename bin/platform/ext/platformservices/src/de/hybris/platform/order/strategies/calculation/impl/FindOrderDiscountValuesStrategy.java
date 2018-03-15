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
package de.hybris.platform.order.strategies.calculation.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindDiscountValuesStrategy;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * Implementation of {@link FindDiscountValuesStrategy} that resolves {@link DiscountValue}s from the order's attached
 * {@link DiscountModel}s.
 */
public class FindOrderDiscountValuesStrategy extends AbstractBusinessService implements FindDiscountValuesStrategy
{
	private static final Logger LOG = Logger.getLogger(FindOrderDiscountValuesStrategy.class);

	@Override
	public List<DiscountValue> findDiscountValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<DiscountValue> findDiscountValues(final AbstractOrderModel order) throws CalculationException
	{
		final List<DiscountModel> discounts = order.getDiscounts();
		if (discounts != null && !discounts.isEmpty())
		{
			final List<DiscountValue> result = new ArrayList<DiscountValue>(discounts.size());
			for (final DiscountModel orderDiscount : discounts)
			{
				final DiscountValue discountValue = getDiscountValue(orderDiscount, order);
				if (discountValue != null)
				{
					result.add(discountValue);
				}
			}
			return result;
		}
		return Collections.EMPTY_LIST;
	}

	//phase 1 delegate to jalo
	protected DiscountValue getDiscountValue(final DiscountModel discount, final AbstractOrderModel order)
	{
		try
		{
			getModelService().save(order);
			final AbstractOrder orderItem = getModelService().getSource(order);
			final Discount discountItem = getModelService().getSource(discount);
			return discountItem.getDiscountValue(orderItem);
		}
		catch (final Exception e)
		{
			//jalo exceptions
			LOG.warn("Discount value of discount " + discount + " for order " + order + " could not be resolved due to : "
					+ e.getMessage());
			return null;
		}
	}

}
