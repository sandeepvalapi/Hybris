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
package de.hybris.platform.europe1.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.DummyPriceFactoryImpl;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.Arrays;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CustomPriceFactoryTest extends HybrisJUnit4TransactionalTest
{
	private Customer customer = null;
	private UserManager userManager = null;
	private EnumerationManager enumerationManager = null;
	private OrderManager orderManager = null;
	private Order order = null;
	private Currency currency = null;
	private static final String CURRENCYCODE = "TESTCURRENCY";
	private static final String ORDERCODE = "TESTORDER";
	private static final String ORDERSTATUS = "ORDERSTATUS";
	private static final boolean NET = true;
	private EnumerationValue orderStatusEnum = null;
	private Unit unit = null;
	private Product product = null;

	private PriceFactory sessionPriceFactoryBefore = null;

	@Before
	public void setUp() throws Exception
	{
		assertNoSessionPriceFactoryActive(jaloSession);

		userManager = jaloSession.getUserManager();
		orderManager = jaloSession.getOrderManager();
		enumerationManager = jaloSession.getEnumerationManager();
		currency = jaloSession.getC2LManager().createCurrency(CURRENCYCODE);

		assertNotNull(currency);

		customer = userManager.createCustomer("TESTCUSTOMER");

		assertNotNull(customer);

		order = orderManager.createOrder(ORDERCODE, customer, currency, new Date(), NET);
		order.setCurrency(currency);

		assertNotNull(order);

		final EnumerationType enumType;

		assertNotNull(enumType = jaloSession.getEnumerationManager().createDefaultEnumerationType("TESTORDERSTATUS"));

		orderStatusEnum = enumerationManager.createEnumerationValue(enumType, ORDERSTATUS);

		assertNotNull(orderStatusEnum);

		order.setStatus(orderStatusEnum);

		assertNotNull(product = ProductManager.getInstance().createProduct("TESTPRODUCT"));

		assertNotNull(unit = ProductManager.getInstance().createUnit("type", "TESTUNIT"));

		final Europe1PriceFactory priceFactory = assertEurope1IsDefault();

		assertNotNull(priceFactory.createPriceRow(product, null, null, null, 1, currency, unit, 1, true, null, 1.234));

	}

	@After
	public void tearDown()
	{
		jaloSession.setPriceFactory(sessionPriceFactoryBefore);
	}

	protected void assertNoSessionPriceFactoryActive(final JaloSession jaloSession)
	{
		sessionPriceFactoryBefore = jaloSession.getPriceFactory();
		jaloSession.setPriceFactory(null);
		assertNull(jaloSession.getPriceFactory());
	}

	protected Europe1PriceFactory assertEurope1IsDefault()
	{
		try
		{
			return (Europe1PriceFactory) jaloSession.getOrderManager().getPriceFactory();
		}
		catch (final ClassCastException e)
		{
			fail("default price factory is " + jaloSession.getOrderManager().getPriceFactory() + " but not europe1");
			return null;
		}
	}

	@Test
	public void testCustomPriceFactoryHandling()
	{
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final OrderEntry entry = (OrderEntry) order.addNewEntry(product, 1, unit);
		entry.setQuantity(10);
		entry.setBasePrice(1.234);
		entry.setTaxValues(Arrays.asList(new Object[]
		{ new TaxValue("VAT FULL", 19.0, false, currency.getIsoCode()) }));

		// using europe1
		try
		{
			order.calculate();
			assertEquals(12.34d, order.getTotal(), 0.0001);
		}
		catch (final JaloPriceFactoryException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		// deactivating the installed pricefactory and using the tinypricefactory
		ctx.setPriceFactory(new TinyPriceFactory());

		try
		{
			order.recalculate();
			assertEquals(47110d, order.getTotal(), 0.0001);
		}
		catch (final JaloPriceFactoryException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		// using europe1 again...
		ctx.setPriceFactory(null);

		try
		{
			order.recalculate();
			assertEquals(12.34d, order.getTotal(), 0.0001);
		}
		catch (final JaloPriceFactoryException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	public class TinyPriceFactory extends DummyPriceFactoryImpl
	{
		@Override
		public PriceValue getBasePrice(final AbstractOrderEntry entry) throws JaloPriceFactoryException
		{
			return new PriceValue(CURRENCYCODE, 4711, true);
		}
	}

}
