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
package de.hybris.platform.europe1.persistence;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.order.price.EJBPriceFactoryException;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.StandardDateRange;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PriceRowTest extends HybrisJUnit4Test
{
	Europe1PriceFactory europe1;
	PriceRow priceRow;
	Product product;
	Currency currency;
	Unit unit;
	EnumerationValue userGroup;

	@Before
	public void setUp() throws Exception
	{
		europe1 = Europe1PriceFactory.getInstance();
		assertNotNull(currency = C2LManager.getInstance().createCurrency("europe1/dr"));
		assertNotNull(product = ProductManager.getInstance().createProduct("europe1/discount"));
		assertNotNull(unit = ProductManager.getInstance().createUnit(null, "europe1/u", "typ"));
		assertNotNull(userGroup = EnumerationManager.getInstance().createEnumerationValue(Europe1Constants.TYPES.DISCOUNT_USER_GROUP,
				"test"));

		assertNotNull(priceRow = europe1.createPriceRow(product, null, null, userGroup, 0, currency, unit, 1, true, null, 0));
	}

	@Test
	public void testTransaction() throws EJBPriceFactoryException, ConsistencyCheckException
	{
		final StandardDateRange range = new StandardDateRange(new Date(0L), new Date(1000L));

		final Transaction tx = Transaction.current();
		tx.begin();
		try
		{
			assertEquals(null, priceRow.getDateRange());
			priceRow.setDateRange(range);
			assertEquals(range, priceRow.getDateRange());
		}
		finally
		{
			tx.rollback();
		}
		assertEquals(null, priceRow.getDateRange());
	}

	@Test
	public void testDifferentPricesForDifferentUser() throws ConsistencyCheckException, JaloPriceFactoryException
	{
		//special price for user1 is higher than price for all users
		final User user1 = UserManager.getInstance().createCustomer("pricerowuser");
		final User user2 = UserManager.getInstance().createCustomer("pricerowuser2");
		//final User user3 = UserManager.getInstance().createCustomer("pricerowuser3");
		assertNotNull(user1);
		final Product prod = ProductManager.getInstance().createProduct("pricerowprod");
		assertNotNull(prod);
		final PriceRow priceRow1 = europe1.createPriceRow(prod, null, user1, null, 0, currency, unit, 1, true, null, 150);
		assertNotNull(priceRow1);
		final PriceRow priceRow2 = europe1.createPriceRow(prod, null, null, null, 0, currency, unit, 1, true, null, 20);
		assertNotNull(priceRow2);
		//		final PriceRow priceRow3 = europe1.createPriceRow(prod, null, user3, null, 0, currency, unit, 1, true, null, 200);
		//		priceRow3.setGiveAwayPrice(true);
		final Order order = OrderManager.getInstance().createOrder(user1, currency, new Date(), true);
		order.addNewEntry(prod, 1, unit);
		order.calculate();
		assertEquals(Double.valueOf(150.0), Double.valueOf(order.getTotal()));

		final Order order2 = OrderManager.getInstance().createOrder(user2, currency, new Date(), true);
		order2.addNewEntry(prod, 1, unit);
		order2.calculate();
		assertEquals(Double.valueOf(20.0), Double.valueOf(order2.getTotal()));

		//		final Order order3 = OrderManager.getInstance().createOrder(user3, currency, new Date(), true);
		//		order3.addNewEntry(prod, 1, unit);
		//		order3.calculate();
		//		assertEquals(Double.valueOf(0.0), Double.valueOf(order3.getTotal()));
	}

	@Test
	public void testDifferentPricesForDifferentUser2() throws ConsistencyCheckException, JaloPriceFactoryException
	{
		//special price for user1 is lower than price for all users
		final User user1 = UserManager.getInstance().createCustomer("pricerowuser");
		final User user2 = UserManager.getInstance().createCustomer("pricerowuser2");
		assertNotNull(user1);
		final Product prod = ProductManager.getInstance().createProduct("pricerowprod");
		assertNotNull(prod);
		final PriceRow priceRow1 = europe1.createPriceRow(prod, null, user1, null, 0, currency, unit, 1, true, null, 50);
		assertNotNull(priceRow1);
		final PriceRow priceRow2 = europe1.createPriceRow(prod, null, null, null, 0, currency, unit, 1, true, null, 100);
		assertNotNull(priceRow2);
		final Order order = OrderManager.getInstance().createOrder(user1, currency, new Date(), true);
		order.addNewEntry(prod, 1, unit);
		order.calculate();
		assertEquals(Double.valueOf(50.0), Double.valueOf(order.getTotal()));

		final Order order2 = OrderManager.getInstance().createOrder(user2, currency, new Date(), true);
		order2.addNewEntry(prod, 1, unit);
		order2.calculate();
		assertEquals(Double.valueOf(100.0), Double.valueOf(order2.getTotal()));
	}

	@Test
	public void testCreationWithDateRange() throws ConsistencyCheckException, JaloGenericCreationException,
			JaloAbstractTypeException, JaloItemNotFoundException
	{
		final Currency currency = C2LManager.getInstance().createCurrency("foo");
		final Unit unit = ProductManager.getInstance().createUnit("foo", "bar");

		// test create without any time
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(PriceRow.CURRENCY, currency);
		params.put(PriceRow.PRICE, Double.valueOf("123.45"));
		params.put(PriceRow.UNIT, unit);

		final PriceRow row1 = ComposedType.newInstance(jaloSession.getSessionContext(), PriceRow.class, params);

		assertNull(row1.getStartTime());
		assertNull(row1.getEndTime());

		final Date startTime = new Date(0);
		final Date endTime = new Date(50000);

		// test create with date range
		params.put(PriceRow.DATERANGE, new StandardDateRange(startTime, endTime));
		final PriceRow row2 = ComposedType.newInstance(jaloSession.getSessionContext(), PriceRow.class, params);

		assertEquals(startTime, row2.getStartTime());
		assertEquals(endTime, row2.getEndTime());

		// test create with single times
		params.remove(PriceRow.DATERANGE);
		params.put(PriceRow.STARTTIME, startTime);
		params.put(PriceRow.ENDTIME, endTime);
		final PriceRow row3 = ComposedType.newInstance(jaloSession.getSessionContext(), PriceRow.class, params);

		assertEquals(startTime, row3.getStartTime());
		assertEquals(endTime, row3.getEndTime());

		// test create with both (same time)
		params.put(PriceRow.DATERANGE, new StandardDateRange(startTime, endTime));
		final PriceRow row4 = ComposedType.newInstance(jaloSession.getSessionContext(), PriceRow.class, params);

		assertEquals(startTime, row4.getStartTime());
		assertEquals(endTime, row4.getEndTime());

		// test create with both (different time)
		params.put(PriceRow.DATERANGE, new StandardDateRange(new Date(200), new Date(100000)));
		try
		{
			ComposedType.newInstance(jaloSession.getSessionContext(), PriceRow.class, params);
			fail("creation exception expected");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		catch (final JaloGenericCreationException e)
		{
			// may be fine too
			final Throwable cause = e.getCause();
			assertNotNull(cause);
			assertTrue(cause instanceof JaloInvalidParameterException);
		}
	}
}
