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


import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.delivery.DefaultDeliveryCostsStrategy;
import de.hybris.platform.jalo.order.delivery.DeliveryCostsStrategy;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.persistence.order.TestOrder;
import de.hybris.platform.persistence.order.TestOrderEntry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AbstractOrderCalculationTest extends HybrisJUnit4TransactionalTest
{
	private Product p, giveAwayProduct;
	private Unit u1, u2;
	private Order o, giveAwayOrder;
	private Currency curr;
	private ComposedType testOrderType, testOrderEntryType;
	private TypeManager tm;
	private OrderManager om;
	private DeliveryCostsStrategy strategy;

	@Before
	public void setUp() throws Exception
	{
		final ProductManager pm = jaloSession.getProductManager();
		assertNotNull(p = pm.createProduct("Product A"));
		assertNotNull(giveAwayProduct = pm.createProduct("Product B (Give Away, no price defined)"));
		assertNotNull(u1 = pm.createUnit("type", "unit1"));
		assertNotNull(u2 = pm.createUnit("type", "unit2"));
		final Customer u = (Customer) jaloSession.getUser();
		assertNotNull(curr = jaloSession.getC2LManager().createCurrency("BLAH"));
		curr.setDigits(3);
		om = jaloSession.getOrderManager();
		strategy = om.getDeliveryCostsStrategy();
		om.setDeliveryCostsStrategy(new DefaultDeliveryCostsStrategy());
		assertNotNull(o = om.createOrder("order calc test", u, curr, new Date(), false));
		assertNotNull(giveAwayOrder = om.createOrder("order calc test (including giveaways)", u, curr, new Date(), false));
		tm = jaloSession.getTypeManager();
		assertNotNull(testOrderType = tm.createComposedType(tm.getComposedType(Order.class), "TestOrderType"));
		testOrderType.setJaloClass(TestOrder.class);
		assertNotNull(testOrderEntryType = tm.createComposedType(tm.getComposedType(OrderEntry.class), "TestOrderEntryType"));
		testOrderEntryType.setJaloClass(TestOrderEntry.class);
	}

	@After
	public void tearDown()
	{
		om.setDeliveryCostsStrategy(strategy);
	}

	@Test
	public void testTransaction() throws JaloPriceFactoryException
	{
		assertEquals(TestOrder.class, testOrderType.getJaloClass());
		assertEquals(TestOrderEntry.class, testOrderEntryType.getJaloClass());
		Order o = null;
		TestOrderEntry entry1, entry2;

		final Transaction ta = Transaction.current();
		ta.begin();

		boolean success = false;
		try
		{
			o = om.createOrder("TAtestOrder", jaloSession.getUser(), jaloSession.getSessionContext().getCurrency(), new Date(),
					false);
			o = (TestOrder) o.setComposedType(testOrderType);
			assertEquals(testOrderType, o.getComposedType());
			assertFalse(o.isCalculated().booleanValue());
			entry1 = (TestOrderEntry) o.addNewEntry(p, 100, u1);
			assertFalse(entry1.isCalculated().booleanValue());
			entry2 = (TestOrderEntry) o.addNewEntry(p, 2, u2);
			assertFalse(entry2.isCalculated().booleanValue());
			o.calculate();
			assertEquals(100, entry1.getQuantity().longValue());
			assertCollection(Collections.EMPTY_LIST, entry1.getDiscountValues());
			assertCollection(Collections.EMPTY_LIST, entry1.getTaxValues());
			assertEquals(TestOrderEntry.PRICE, entry1.getBasePrice().doubleValue(), 0.00001);
			assertEquals(TestOrderEntry.PRICE * 100.0, entry1.getTotalPrice().doubleValue(), 0.00001);
			assertEquals(2, entry2.getQuantity().longValue());
			assertCollection(Collections.EMPTY_LIST, entry2.getDiscountValues());
			assertCollection(Collections.EMPTY_LIST, entry2.getTaxValues());
			assertEquals(TestOrderEntry.PRICE, entry2.getBasePrice().doubleValue(), 0.00001);
			assertEquals(TestOrderEntry.PRICE * 2.0, entry2.getTotalPrice().doubleValue(), 0.00001);
			assertEquals(0.0, o.getDeliveryCosts(), 0.00001);
			assertEquals(0.0, o.getPaymentCosts(), 0.00001);
			assertEquals(0.0, o.getTotalDiscounts().doubleValue(), 0.00001);
			assertEquals(0.0, o.getTotalTax().doubleValue(), 0.00001);
			assertEquals(TestOrderEntry.PRICE * 102.0, o.getTotal(), 0.00001);
			assertEquals(TestOrderEntry.PRICE * 102.0, o.getSubtotal().doubleValue(), 0.00001);
			assertTrue(entry1.isCalculated().booleanValue());
			assertTrue(entry2.isCalculated().booleanValue());
			assertTrue(o.isCalculated().booleanValue());

			success = true;

		}
		finally
		{
			if (success)
			{
				ta.commit();
			}
			else
			{
				ta.rollback();
			}
		}

		// check again
		assertNotNull(o);
		assertTrue(o.isCalculated().booleanValue());
		assertEquals(0.0, o.getDeliveryCosts(), 0.00001);
		assertEquals(0.0, o.getPaymentCosts(), 0.00001);
		assertEquals(0.0, o.getTotalDiscounts().doubleValue(), 0.00001);
		assertEquals(0.0, o.getTotalTax().doubleValue(), 0.00001);
		assertEquals(TestOrderEntry.PRICE * 102.0, o.getTotal(), 0.00001);
		assertEquals(TestOrderEntry.PRICE * 102.0, o.getSubtotal().doubleValue(), 0.00001);
		assertTrue(entry1.isCalculated().booleanValue());
		assertEquals(100, entry1.getQuantity().longValue());
		assertCollection(Collections.EMPTY_LIST, entry1.getDiscountValues());
		assertCollection(Collections.EMPTY_LIST, entry1.getTaxValues());
		assertEquals(TestOrderEntry.PRICE, entry1.getBasePrice().doubleValue(), 0.00001);
		assertEquals(TestOrderEntry.PRICE * 100.0, entry1.getTotalPrice().doubleValue(), 0.00001);
		assertTrue(entry2.isCalculated().booleanValue());
		assertEquals(2, entry2.getQuantity().longValue());
		assertCollection(Collections.EMPTY_LIST, entry2.getDiscountValues());
		assertCollection(Collections.EMPTY_LIST, entry2.getTaxValues());
		assertEquals(TestOrderEntry.PRICE, entry2.getBasePrice().doubleValue(), 0.00001);
		assertEquals(TestOrderEntry.PRICE * 2.0, entry2.getTotalPrice().doubleValue(), 0.00001);
	}

	@Test
	public void testCalculation() throws JaloPriceFactoryException
	{
		// test besic values
		assertFalse("order shouldnt be calculated yet", o.isCalculated().booleanValue());
		assertEquals(curr, o.getCurrency());
		assertFalse("order wasnt gross", o.isNet().booleanValue());
		// try empty calculation
		o.calculateTotals(false);
		checkOrderEmpty(o);
		// try with two entries - still without prices
		final AbstractOrderEntry oe1 = o.addNewEntry(p, 10, u1);
		final AbstractOrderEntry oe2 = o.addNewEntry(p, 3, u2);
		o.calculateTotals(false);
		checkOrderEmpty(o);
		for (final Iterator it = o.getAllEntries().iterator(); it.hasNext();)
		{
			checkOrderEntryEmpty((AbstractOrderEntry) it.next());
		}
		/*
		 * give entries prices 10 x 1.234 = 12.340 , 16% VAT FULL 3 x 3.333 = 9.999 , 7% VAT HALF, -0.999 DISC A, - 10%
		 * DISC B = 8.100
		 */
		oe1.setQuantity(10);
		oe1.setBasePrice(1.234);
		oe1.setTaxValues(Arrays.asList(new Object[]
		{ new TaxValue("VAT FULL", 16.0, false, curr.getIsoCode()) }));
		oe2.setQuantity(3);
		oe2.setBasePrice(3.333);
		oe2.addTaxValue(new TaxValue("VAT HALF", 7.0, false, curr.getIsoCode()));
		oe2.setDiscountValues(Arrays.asList(new Object[]
		{ new DiscountValue("DISC A", 0.333, true, curr.getIsoCode()), new DiscountValue("DISC B", 10, false, null) }));
		// entry total should still be 0
		assertEquals(0.0, oe1.getTotalPrice().doubleValue(), 0.0001);
		assertEquals(0.0, oe2.getTotalPrice().doubleValue(), 0.0001);
		assertFalse("order should not be calculated", o.isCalculated().booleanValue());
		/*
		 * now calculate and check order: 10 x 10 x 1.234 , 16% VAT FULL = 12.340 3 x 3.333 = 9.999 , 7% VAT HALF, -3 x
		 * 0.333 = -0.999 DISC A, - (10%,0.900) DISC B = 8.100 -------- subtotal = 20,440 VAT FULL 16% = 1,974 VAT HALF 7%
		 * = 0,567 taxes = 2,541 discounts = 0,000 total = 20.440
		 */
		o.calculateTotals(false);
		checkOrderEntry(oe1, 1.234, // base price
				12.340, // total price
				Arrays.asList(new Object[]
				{ new TaxValue("VAT FULL", 16.0, false, 1.702, curr.getIsoCode()) }), // tax values
				Collections.EMPTY_LIST // discount values
		);
		checkOrderEntry(oe2,
				3.333, // base price
				8.100, // total price
				Arrays.asList(new Object[]
				{ new TaxValue("VAT HALF", 7.0, false, 0.530, curr.getIsoCode()) }), // tax values
				Arrays.asList(new Object[]
				{ new DiscountValue("DISC A", 0.333, true, 0.999, curr.getIsoCode()),
						new DiscountValue("DISC B", 10, false, 0.9, null) }) // discount values
		);
		checkOrder(o,
				20.44, // subtotal
				0.0, // total discounts
				2.541, // total taxes
				20.44, // total
				0.0, // delivery cost
				0.0, // payment cost
				Arrays.asList(new Object[]
				{ new TaxValue("VAT FULL", 16, false, 1.702, curr.getIsoCode()),
						new TaxValue("VAT HALF", 7, false, 0.530, curr.getIsoCode()) }), // tax values
				Collections.EMPTY_LIST // discount values
		);
		/*
		 * test global discount subtotal = 20,440 ----------- - 10% -> -2.044 = 18,396 - 3 -> -3 = 15,396 -5% -> 0,770 =
		 * 14,626 discounts = 5,814 ----------- (internal) tax factor = 14,626 / 20,440 =
		 * 0,71555772994129158512720156555773 ----------- VAT FULL 16% = 1,413 VAT HALF 7% = 0,406 taxes = 1,819 total =
		 * 14,626
		 */
		o.addGlobalDiscountValue(new DiscountValue("10%off", 10, false, null)); // -10%
		o.addGlobalDiscountValue(new DiscountValue("3off", 3, true, curr.getIsoCode())); // -3
		o.addGlobalDiscountValue(new DiscountValue("5%off", 5, false, null)); // -3
		// calculate totals again
		o.calculateTotals(false);
		checkOrder(
				o,
				20.440, // subtotal
				5.814, // discount totals
				1.819, // tax totals
				14.626, // total
				0.0, // delivery cost
				0.0, // payment cost
				Arrays.asList(new Object[]
				{ new TaxValue("VAT FULL", 16, false, 1.218, curr.getIsoCode()),
						new TaxValue("VAT HALF", 7, false, 0.379, curr.getIsoCode()) }), // tax values
				Arrays.asList(new Object[]
				{ new DiscountValue("10%off", 10, false, 2.044, null), new DiscountValue("3off", 3, true, 3.0, curr.getIsoCode()),
						new DiscountValue("5%off", 5, false, 0.770, null) }) // discount
		// values
		);
		/*
		 * add payment and delivery costs subtotal = 20,440 ----------- - 10% -> -2.044 = 18,396 - 3 -> -3 = 15,396 -5% ->
		 * 0,770 = 14,626 discounts = 5,814 ----------- delivery = 4,44 payment = 2,222 ----------- (internal) tax factor
		 * = ( 14,626 + 4,44 + 2,222 ) / 20,440 = 1,0414872798434442270058708414873 ----------- VAT FULL 16% = 2,056 VAT
		 * HALF 7% = 0,591 taxes = 2,647 total = 21,288
		 */
		o.setPaymentCosts(4.44);
		o.setDeliveryCosts(2.222);
		o.calculateTotals(false);
		checkOrder(
				o,
				20.440, // subtotal
				5.814, // discount totals
				2.647, // tax totals
				21.288, // total
				2.222, // delivery cost
				4.44, // payment cost
				Arrays.asList(new Object[]
				{ new TaxValue("VAT FULL", 16, false, 1.773, curr.getIsoCode()),
						new TaxValue("VAT HALF", 7, false, 0.552, curr.getIsoCode()) }), // tax values
				Arrays.asList(new Object[]
				{ new DiscountValue("10%off", 10, false, 2.044, null), new DiscountValue("3off", 3, true, 3.0, curr.getIsoCode()),
						new DiscountValue("5%off", 5, false, 0.770, null) }) // discount
		// values
		);
	}

	private void checkOrderEmpty(final AbstractOrder o)
	{
		checkOrder(o, 0.0, // subtotal
				0.0, // total discounts
				0.0, // total taxes
				0.0, // total
				0.0, // delivery cost
				0.0, // payment cost
				Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		assertNull(o.getDeliveryMode());
		assertNull(o.getDeliveryAddress());
		assertNull(o.getPaymentMode());
		assertNull(o.getPaymentAddress());
	}

	private void checkOrder(final AbstractOrder o, final double subtotal, final double totalDiscounts,
			@SuppressWarnings("unused") final double totalTaxes, final double total, final double deliveryCost,
			final double paymentCost, final Collection taxValues, final Collection discountValues)
	{
		assertTrue(o.isCalculated().booleanValue());
		assertEquals(total, o.getTotal(), 0.0001);
		assertEquals(subtotal, o.getSubtotal().doubleValue(), 0.0001);
		assertEquals(totalDiscounts, o.getTotalDiscounts().doubleValue(), 0.0001);
		assertEquals(deliveryCost, o.getDeliveryCosts(), 0.0001);
		assertCollection(taxValues, o.getTotalTaxValues());
		assertEquals(discountValues, o.getGlobalDiscountValues()); // order is important here so we dont use assertCollection !!!
		assertEquals(paymentCost, o.getPaymentCosts(), 0.0001);
	}

	private void checkOrderEntryEmpty(final AbstractOrderEntry oe)
	{
		checkOrderEntry(oe, 0.0, 0.0, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}

	private void checkOrderEntry(final AbstractOrderEntry oe, final double basePrice, final double totalPrice,
			final Collection taxValues, final List discountValues)
	{
		assertTrue(oe.isCalculated().booleanValue());
		assertEquals(totalPrice, oe.getTotalPrice().doubleValue(), 0.0001);
		assertEquals(basePrice, oe.getBasePrice().doubleValue(), 0.0001);
		assertEquals(discountValues, oe.getDiscountValues()); // order is important here so we dont use assertCollection !!!
		assertCollection(taxValues, oe.getTaxValues());
	}

	@Test
	public void testGiveAwayHandling()
	{
		final SessionContext ctx = jaloSession.getSessionContext();

		// CASE 1 ( PRICE = n/a, ISGIVEAWAY = n/a, ISRECJECTED = n/a)
		final AbstractOrderEntry entry = giveAwayOrder.addNewEntry(giveAwayProduct, 1, u1);
		boolean welldone = false;

		try
		{
			giveAwayOrder.calculate();
		}
		catch (final JaloPriceFactoryException e)
		{
			welldone = true;
		}

		if (!welldone)
		{
			fail("Invalid state of order entry (" + entry + ") [ " + "product.price: n/a, " + "entry.ISGIVEAWAY: "
					+ entry.isGiveAway(ctx) + ", " + "entry.ISREJECTED: " + entry.isRejected(ctx) + "]");
		}

		// CASE 2 (PRICE = n/a, ISGIVEAWAY = true, ISRECJECTED = false
		entry.setGiveAway(ctx, true);
		entry.setRejected(ctx, false);

		try
		{
			giveAwayOrder.calculate();
		}
		catch (final JaloPriceFactoryException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		//	 CASE 3 (PRICE = n/a, ISGIVEAWAY = true, ISRECJECTED = true
		entry.setGiveAway(ctx, true);
		entry.setRejected(ctx, true);

		try
		{
			giveAwayOrder.recalculate();
		}
		catch (final JaloPriceFactoryException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
