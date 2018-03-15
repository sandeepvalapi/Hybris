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
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.junit.Test;


@IntegrationTest
public class OrderPriceFieldsTest extends HybrisJUnit4TransactionalTest
{

	Order order;
	Tax tax;
	Discount discount;
	TaxValue taxValue;
	DiscountValue discountValue;

	@Test
	public void testPriceFields() throws Exception
	{
		final OrderManager orderManager = jaloSession.getOrderManager();

		assertNotNull(order = orderManager.createOrder("order", jaloSession.getUser(), jaloSession.getSessionContext()
				.getCurrency(), new Date(), true));

		order.setCalculated(true);
		assertTrue("isCalculated is not true.", order.isCalculated().booleanValue());

		order.setDeliveryCosts(1.1111);
		assertEquals(1.1111, order.getDeliveryCosts(), 0.00001);

		order.setPaymentCosts(2.2222);
		assertEquals(2.2222, order.getPaymentCosts(), 0.00001);

		order.setTotal(3.3333);
		assertEquals(3.3333, order.getTotal(), 0.00001);

		order.setTotalTax(4.4444);
		assertEquals(4.4444, order.getTotalTax().doubleValue(), 0.00001);

		assertNotNull(tax = orderManager.createTax("tax"));
		taxValue = new TaxValue(tax.getCode(), 3.3333, false, null);

		final ArrayList taxTotalList = new ArrayList(order.getTotalTaxValues());
		assertEquals(Collections.EMPTY_LIST, taxTotalList);

		order.addTotalTaxValue(taxValue);
		taxTotalList.addAll(order.getTotalTaxValues());
		assertTrue("Tax values does not contain " + taxValue,
				taxTotalList.size() == 1 && ((TaxValue) taxTotalList.get(0)).getValue() == 3.3333);

		order.removeTotalTaxValue(taxValue);
		taxTotalList.clear();
		taxTotalList.addAll(order.getTotalTaxValues());
		assertTrue("Tax values is not empty.", taxTotalList.isEmpty());

		assertNotNull(discount = orderManager.createDiscount("discount"));
		discountValue = new DiscountValue(discount.getCode(), 4.4444, false, null);

		taxTotalList.clear();
		assertTrue("Discount values is not empty.", taxTotalList.isEmpty());

		order.addGlobalDiscountValue(discountValue);
		taxTotalList.addAll(order.getGlobalDiscountValues());
		assertTrue("Discount values does not contain " + discountValue,
				taxTotalList.size() == 1 && ((DiscountValue) taxTotalList.get(0)).getValue() == 4.4444);

		order.removeGlobalDiscountValue(discountValue);
		taxTotalList.clear();
		taxTotalList.addAll(order.getGlobalDiscountValues());
		assertTrue("Discount values is not empty.", taxTotalList.isEmpty());


		taxTotalList.clear();
		taxTotalList.add(taxValue);
	}
}
