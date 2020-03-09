/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.orderprocessing.model.OrderModificationProcessModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.math.BigDecimal;
import java.util.List;



/**
 * Velocity context for email about partially order refund
 */
public class OrderPartiallyRefundedEmailContext extends OrderPartiallyModifiedEmailContext
{
	private PriceData refundAmount;

	@Override
	public void init(final OrderModificationProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		calculateRefundAmount();
	}

	protected void calculateRefundAmount()
	{
		BigDecimal refundAmountValue = BigDecimal.ZERO;
		PriceData totalPrice = null;
		for (final OrderEntryData entryData : getRefundedEntries())
		{
			totalPrice = entryData.getTotalPrice();
			refundAmountValue = refundAmountValue.add(totalPrice.getValue());
		}

		if (totalPrice != null)
		{
			refundAmount = getPriceDataFactory().create(totalPrice.getPriceType(), refundAmountValue, totalPrice.getCurrencyIso());
		}
	}

	public List<OrderEntryData> getRefundedEntries()
	{
		return super.getModifiedEntries();
	}

	public PriceData getRefundAmount()
	{
		return refundAmount;
	}
}
