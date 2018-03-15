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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;


@IntegrationTest
public class OrderEntryPriceFieldsTest extends HybrisJUnit4TransactionalTest
{

	AbstractOrderEntry abstractOrderEntry = null;
	Order order = null;
	Unit unit = null;
	Product product = null;
	Tax tax = null;
	Discount discount = null;
	TaxValue taxValue;
	DiscountValue discountValue;

	@Test
	public void testPriceFields() throws Exception
	{
		final OrderManager orderManager = jaloSession.getOrderManager();

		assertNotNull(order = orderManager.createOrder("order", jaloSession.getUser(), jaloSession.getSessionContext()
				.getCurrency(), new Date(), true));

		assertNotNull(product = jaloSession.getProductManager().createProduct("product"));
		assertNotNull(unit = jaloSession.getProductManager().createUnit("piece", "unit"));

		order.addNewEntry(product, 3, unit);
		abstractOrderEntry = order.getEntry(0);

		assertNotNull("order entry is null", abstractOrderEntry);

		abstractOrderEntry.setCalculated(true);
		assertTrue("isCalculated is not true.", abstractOrderEntry.isCalculated().booleanValue());

		abstractOrderEntry.setBasePrice(1.1111);
		assertTrue("base price is not 1.1111.", abstractOrderEntry.getBasePrice().doubleValue() == 1.1111);

		abstractOrderEntry.setTotalPrice(2.2222);
		assertTrue("total price is not 2.2222.", abstractOrderEntry.getTotalPrice().doubleValue() == 2.2222);

		assertNotNull(tax = orderManager.createTax("tax"));
		taxValue = new TaxValue(tax.getCode(), 3.3333, false, null);

		final ArrayList taxRates = new ArrayList(abstractOrderEntry.getTaxValues());
		assertTrue("Tax values is not empty.", taxRates.isEmpty());

		abstractOrderEntry.addTaxValue(taxValue);
		taxRates.addAll(abstractOrderEntry.getTaxValues());
		assertTrue("Tax values does not contain " + taxValue, taxRates.size() == 1
				&& ((TaxValue) taxRates.get(0)).getValue() == 3.3333);

		abstractOrderEntry.removeTaxValue(taxValue);
		taxRates.clear();
		taxRates.addAll(abstractOrderEntry.getTaxValues());
		assertTrue("Tax values is not empty.", taxRates.isEmpty());

		assertNotNull(discount = orderManager.createDiscount("discount"));
		discountValue = new DiscountValue(discount.getCode(), 4.4444, false, null);

		taxRates.clear();
		assertTrue("Discount values is not empty.", taxRates.isEmpty());

		abstractOrderEntry.addDiscountValue(discountValue);
		taxRates.addAll(abstractOrderEntry.getDiscountValues());
		assertTrue("Discount values does not contain " + discountValue,
				taxRates.size() == 1 && ((DiscountValue) taxRates.get(0)).getValue() == 4.4444);

		abstractOrderEntry.removeDiscountValue(discountValue);
		taxRates.clear();
		taxRates.addAll(abstractOrderEntry.getDiscountValues());
		assertTrue("Discount values is not empty.", taxRates.isEmpty());


		assertEquals("Order entry product is not right.", abstractOrderEntry.getProduct(), product);
	}
}
