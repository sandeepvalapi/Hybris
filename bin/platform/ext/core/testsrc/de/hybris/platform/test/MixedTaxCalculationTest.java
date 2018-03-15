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
package de.hybris.platform.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.TaxValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;


/**
 *
 */
public class MixedTaxCalculationTest extends HybrisJUnit4Test
{
	Order order;
	Currency currency;
	Product product;
	Unit unit;

	@Before
	public void setUp() throws ConsistencyCheckException
	{
		currency = C2LManager.getInstance().createCurrency("xxx");
		currency.setDigits(2);
		currency.setActive(true);

		unit = ProductManager.getInstance().createUnit("package", "barrel");
		unit.setConversion(1);

		product = ProductManager.getInstance().createProduct("ppp");

		order = OrderManager.getInstance().createOrder(jaloSession.getUser(), currency, new Date(), false);
	}

	// see PLA-12661
	@Test
	public void testMixedTaxedEntriesGross() throws JaloPriceFactoryException
	{
		addEntry(order, product, unit, 1, 20.0, new TaxValue("VAT_FULL", 19, false, null), new TaxValue("CUSTOM", 2, false, null));
		addEntry(order, product, unit, 1, 30.0, new TaxValue("VAT_FULL", 19, false, null));

		order.calculateTotals(true);

		final Collection<TaxValue> totalTaxValues = order.getTotalTaxValues();
		assertEquals(2, totalTaxValues.size());

		final TaxValue full = getValue(totalTaxValues, "VAT_FULL");
		assertNotNull(full);
		final double expected_full = CoreAlgorithms.round((20.0 * 19.0 / 121.0) + (30.0 * 19.0 / 119.0), 2);
		assertEquals(expected_full, full.getAppliedValue(), 0.0000001);

		final TaxValue custom = getValue(totalTaxValues, "CUSTOM");
		assertNotNull(custom);
		final double expected_custom = CoreAlgorithms.round(20.0 * 2.0 / 121.0, 2);
		assertEquals(expected_custom, custom.getAppliedValue(), 0.0000001);

		assertEquals(expected_custom + expected_full, order.getTotalTaxAsPrimitive(), 0.000001);
	}

	private TaxValue getValue(final Collection<TaxValue> totalTaxValues, final String code)
	{
		for (final TaxValue tv : totalTaxValues)
		{
			if (code.equalsIgnoreCase(tv.getCode()))
			{
				return tv;
			}
		}
		return null;
	}

	private OrderEntry addEntry(final Order order, final Product product, final Unit unit, final long quantity,
			final double basePrice, final TaxValue... taxValues)
	{
		final OrderEntry entry = (OrderEntry) order.addNewEntry(product, quantity, unit);
		entry.setBasePrice(basePrice);
		if (taxValues != null)
		{
			entry.setTaxValues(Arrays.asList(taxValues));
		}
		return entry;
	}
}
