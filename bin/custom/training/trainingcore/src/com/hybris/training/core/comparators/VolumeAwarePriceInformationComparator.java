/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.comparators;

import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.util.PriceValue;

import java.util.Comparator;


/**
 * Compares two prices.<br>
 * The prices are compared by minqty and the lowest minqty (or null) is treated as lower. <br>
 * If there are multiple prices then the lowest price is treated as lower. <br>
 * If there are still multiple records then the first is treated as lower.
 */
public class VolumeAwarePriceInformationComparator implements Comparator<PriceInformation>
{
	@Override
	public int compare(final PriceInformation priceInfo1, final PriceInformation priceInfo2)
	{
		final Long o1Quantity = (Long) priceInfo1.getQualifierValue(PriceRow.MINQTD);
		final Long o2Quantity = (Long) priceInfo2.getQualifierValue(PriceRow.MINQTD);

		if (o1Quantity == null && o2Quantity == null)
		{
			return 0;
		}

		if (o1Quantity == null)
		{
			return -1;
		}

		if (o2Quantity == null)
		{
			return 1;
		}

		if (o1Quantity.longValue() == o2Quantity.longValue())
		{
			final PriceValue pv1 = priceInfo1.getPriceValue();
			final PriceValue pv2 = priceInfo2.getPriceValue();
			return Double.compare(pv1.getValue(), pv2.getValue());
		}
		return o1Quantity.compareTo(o2Quantity);
	}
}
