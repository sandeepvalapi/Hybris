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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OrderTest extends HybrisJUnit4TransactionalTest
{
	static final Logger log = Logger.getLogger(OrderTest.class.getName());
	Order o = null;
	Collection orders = Collections.EMPTY_LIST;
	Cart c;
	OrderManager om;
	SearchContext sc;
	EnumerationValue ps, ds;

	@Before
	public void setUp() throws Exception
	{
		//super.setUp();

		// create a card
		c = jaloSession.getCart();

		om = jaloSession.getOrderManager();
		sc = jaloSession.createSearchContext();

		// set an order      
		o = jaloSession.getOrderManager().createOrder(c);
		ps = createPaymentStatus("PA");
		//registerForRemoval( ps );
		ds = createDeliveryStatus("DE");
		//registerForRemoval( ds );
		o.setPaymentStatus(ps);
		o.setDeliveryStatus(ds);
	}

	@Test
	public void testDoubleRoundingError() throws JaloPriceFactoryException
	{
		final Product p = ProductManager.getInstance().createProduct("test_p");
		final Unit u = ProductManager.getInstance().createUnit("x", "y");
		final Currency c = jaloSession.getSessionContext().getCurrency();
		c.setDigits(2);

		final Order o = OrderManager.getInstance().createOrder("test", jaloSession.getUser(), c, new Date(), true);
		final OrderEntry entry = (OrderEntry) o.addNewEntry(p, 1, u);

		// 100.05
		entry.setBasePrice(100.05);
		// 5% vat
		entry.setTaxValues(Collections.singletonList(new TaxValue("vat", 5, false, null)));
		// 10% off
		o.setGlobalDiscountValues(Collections.singletonList(new DiscountValue("10percent", 10, false, null)));

		// now calculate
		o.calculateTotals(false);

		assertEquals(100.05, o.getSubtotal().doubleValue(), 0.000000001);
		assertEquals(4.50, o.getTotalTax().doubleValue(), 0.000000001);
		assertEquals(10.01, o.getTotalDiscounts().doubleValue(), 0.000000001);
		assertEquals(90.04, o.getTotal(), 0.000000001);

		final NumberFormat f = DecimalFormat.getNumberInstance(Locale.US);
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);

		assertEquals("100.05", f.format(o.getSubtotal()));
		assertEquals("4.50", f.format(o.getTotalTax()));
		assertEquals("10.01", f.format(o.getTotalDiscounts()));
		assertEquals("90.04", f.format(o.getTotal()));
	}

	@Test
	public void testOrder() throws Exception
	{
		try
		{
			// set search parameters
			sc.setProperty(AbstractOrder.DELIVERY_STATUS, ps);
			sc.setProperty(AbstractOrder.PAYMENT_STATUS, ds);
			orders = om.searchOrders(sc);

			for (final Iterator it = orders.iterator(); it.hasNext();)
			{
				final Order rem = (Order) it.next();
				assertTrue("order.getPaymentStatus() != \"PA\" (test data)", rem.getPaymentStatus().equals(ps));
				assertTrue("order.getDeliveryStatus() != \"DE\" (test data)", rem.getDeliveryStatus().equals(ds));
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("an error occurred : " + e);
		}
		finally
		{
			if (o != null)
			{
				try
				{
					o.remove();
					/* conv-log */log.debug("removed order " + o);
				}
				catch (final Exception e)
				{
					/* conv-log-err */log.error("could not remove order " + o);
				}
			}
		}
	}

	@After
	public void tearDown() throws Exception
	{
		// DOCTODO Document reason, why this block is empty
	}

	private static EnumerationValue createPaymentStatus(final String statusCode) throws ConsistencyCheckException
	{
		return JaloSession.getCurrentSession().getEnumerationManager()
				.createEnumerationValue(JaloSession.getCurrentSession().getOrderManager().getPaymentStatusType(), statusCode);
	}

	private static EnumerationValue createDeliveryStatus(final String statusCode) throws ConsistencyCheckException
	{
		return JaloSession.getCurrentSession().getEnumerationManager()
				.createEnumerationValue(JaloSession.getCurrentSession().getOrderManager().getDeliveryStatusType(), statusCode);
	}
}
