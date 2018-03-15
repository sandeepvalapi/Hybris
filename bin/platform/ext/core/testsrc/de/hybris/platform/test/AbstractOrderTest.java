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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SearchContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.order.TestOrder;
import de.hybris.platform.persistence.order.TestOrderEntry;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AbstractOrderTest extends HybrisJUnit4Test
{
	TypeManager typeManager;
	ComposedType testOrderType, testOrderEntryType;
	Product product, product2, product3, product4;
	Unit unit1, unit2;
	Order order1, order2;
	OrderEntry orderEntry1, orderEntry2, orderEntry3, orderEntry4;
	Date date;
	boolean dateTruncated = false;

	private static final Logger LOG = Logger.getLogger(AbstractOrderTest.class);

	@Before
	public void setUp() throws Exception
	{
		//make sure that every order/cart is removed, necessary since session carts may be still present
		final Collection orders = jaloSession.getTypeManager().getComposedType(AbstractOrder.class).getAllInstances();
		for (final Iterator it = orders.iterator(); it.hasNext();)
		{
			((AbstractOrder) it.next()).remove();
		}

		final ProductManager productManager = jaloSession.getProductManager();
		assertNotNull(product = productManager.createProduct("AO-Test-Product"));
		assertNotNull(product2 = productManager.createProduct("AO-Test-Product2"));
		assertNotNull(product3 = productManager.createProduct("AO-Test-Product3"));
		assertNotNull(product4 = productManager.createProduct("AO-Test-Product4"));
		assertNotNull(unit1 = productManager.createUnit("type", "AO-Test-Unit1"));
		assertNotNull(unit2 = productManager.createUnit("type", "AO-Test-Unit2"));
		final Customer customer = (Customer) jaloSession.getUser();
		final Currency curr = jaloSession.getSessionContext().getCurrency();
		date = new Date(12345L);
		assertNotNull(order1 = jaloSession.getOrderManager().createOrder("blah", customer, curr, date, true));
		if (!date.equals(order1.getDate()))
		{
			LOG.warn("order date seems to be truncated from " + date.getTime() + " to " + order1.getDate().getTime()
					+ " - trying to use truncated date for test");
			dateTruncated = true;
			date = order1.getDate();
		}
		typeManager = jaloSession.getTypeManager();
		assertNotNull(testOrderType = typeManager.createComposedType(typeManager.getComposedType(Order.class), "TestOrderType"));
		testOrderType.setJaloClass(TestOrder.class);
		assertNotNull(testOrderEntryType = typeManager.createComposedType(typeManager.getComposedType(OrderEntry.class),
				"TestOrderEntryType"));
		testOrderEntryType.setJaloClass(TestOrderEntry.class);

		assertNotNull(order2 = jaloSession.getOrderManager().createOrder("blah2", customer, curr,
				new Date(date.getTime() + 1234567L), true));
		orderEntry1 = (OrderEntry) order2.addNewEntry(product, 1, unit1);
		orderEntry2 = (OrderEntry) order2.addNewEntry(product2, 2, unit1);
		orderEntry3 = (OrderEntry) order2.addNewEntry(product3, 3, unit1);
		orderEntry4 = (OrderEntry) order2.addNewEntry(product4, 4, unit1);

	}
	
	@Test
	public void testProductInfo() throws ConsistencyCheckException
	{
		String cfgBefore = null;
		Locale jvmDefaultBefore = Locale.getDefault();
		try
		{
			cfgBefore = Config.getParameter("orderentry.infofield.product");
			Config.setParameter("orderentry.infofield.product","${code}-${name}");
			Locale.setDefault(Locale.ITALIAN);
			
   		Language someLang = getOrCreateLanguage("someLang");
   		Language tenantLang = getOrCreateLanguage(Registry.getCurrentTenant().getTenantSpecificLocale().toString());
   		Language jvmLang = getOrCreateLanguage(Locale.getDefault().toString());
   		Language otherLang = getOrCreateLanguage("otherLang");
   		
   		Customer cust = UserManager.getInstance().createCustomer("InfoTestCustomer");
   		cust.setSessionLanguage(null);
   		
   		Map<Language,String> names = new HashMap<>();
   		names.put(someLang, "someName");
   		names.put(tenantLang, "tenantName");
   		names.put(jvmLang, "jvmName");
   		
   		product.setAllName(names);

   		// test localized value is null -> n/a
   		jaloSession.getSessionContext().setLanguage(otherLang);
   		Order order = OrderManager.getInstance().createOrder("infoTestOrder", cust, jaloSession.getSessionContext().getCurrency(), new Date(), true);
   		OrderEntry entry = (OrderEntry) order.addNewEntry(product, 1, unit1);
   		assertEquals(product.getCode()+"-n/a",entry.getInfo());

   		// test localized value for session lang 
   		jaloSession.getSessionContext().setLanguage(someLang);
   		OrderEntry entry2 = (OrderEntry) order.addNewEntry(product, 1, unit1);
   		assertEquals(product.getCode()+"-someName",entry2.getInfo());

   		// test localized value + ctx.lang = null -> fallback to tenant lang  
   		jaloSession.getSessionContext().setLanguage(null);
   		OrderEntry entry3 = (OrderEntry) order.addNewEntry(product, 1, unit1);
   		assertEquals(product.getCode()+"-tenantName",entry3.getInfo());
   		
   		// test localized value + ctx.lang = null + no tenant lang -> fallback to jvm lang  
   		tenantLang.remove();
   		OrderEntry entry4 = (OrderEntry) order.addNewEntry(product, 1, unit1);
   		assertEquals(product.getCode()+"-jvmName",entry4.getInfo());
   		
   		// test localized value + ctx.lang = null + user.sessionLang -> fallback to user language  
   		cust.setSessionLanguage(someLang);
   		OrderEntry entry5 = (OrderEntry) order.addNewEntry(product, 1, unit1);
   		assertEquals(product.getCode()+"-someName",entry5.getInfo());
		}
		finally
		{
			Locale.setDefault(jvmDefaultBefore);
			if(cfgBefore != null)
			{
				Config.setParameter("orderentry.infofield.product",cfgBefore);
			}
		}
		
	}
	

	@Test
	public void testSetComposedTypeDelegation()
	{
		assertNotNull(order2);
		assertNotNull(orderEntry1);
		assertNotNull(orderEntry2);
		assertNotNull(orderEntry3);
		assertNotNull(orderEntry4);

		final ComposedType orderType = typeManager.getComposedType(Order.class);
		final ComposedType orderEntryType = typeManager.getComposedType(OrderEntry.class);

		assertFalse(orderType.getCode() + "==" + testOrderType.getCode(), orderType.equals(testOrderType));
		assertFalse(orderEntryType.getCode() + "==" + testOrderEntryType.getCode(), orderEntryType.equals(testOrderEntryType));

		assertEquals(orderType, order2.getComposedType());
		assertTrue(Order.class.isAssignableFrom(order2.getClass()));
		assertEquals(orderEntryType, orderEntry1.getComposedType());
		assertTrue(OrderEntry.class.isAssignableFrom(orderEntry1.getClass()));
		assertEquals(orderEntryType, orderEntry2.getComposedType());
		assertTrue(OrderEntry.class.isAssignableFrom(orderEntry2.getClass()));
		assertEquals(orderEntryType, orderEntry3.getComposedType());
		assertTrue(OrderEntry.class.isAssignableFrom(orderEntry3.getClass()));
		assertEquals(orderEntryType, orderEntry4.getComposedType());
		assertTrue(OrderEntry.class.isAssignableFrom(orderEntry4.getClass()));

		final Order order3 = (Order) order2.setComposedType(testOrderType);

		assertTrue(TestOrder.class.isAssignableFrom(order3.getClass()));
		assertEquals(testOrderType, order3.getComposedType());
		for (final Iterator it = order3.getAllEntries().iterator(); it.hasNext();)
		{
			final OrderEntry orderEntry = (OrderEntry) it.next();
			assertEquals(testOrderEntryType, orderEntry.getComposedType());
			assertTrue(TestOrderEntry.class.isAssignableFrom(orderEntry.getClass()));
		}
	}

	@Test
	public void testSearchByDate()
	{
		try
		{
			final Date less = new Date(date.getTime() - 10000);
			final String otCode = jaloSession.getTypeManager().getRootComposedTypeForJaloClass(AbstractOrder.class).getCode();
			final SearchContext ctx = jaloSession.createSearchContext();

			// find exact
			final Date totest = date;
			GenericCondition genericCondition = GenericCondition.equals(new GenericSearchField(otCode, AbstractOrder.DATE), totest);
			Collection res = jaloSession.search(new GenericQuery(otCode, genericCondition), ctx).getResult();
			assertCollection(Collections.singleton(order1), res);

			// find by less
			genericCondition = GenericCondition.createGreaterCondition(new GenericSearchField(otCode, AbstractOrder.DATE), less);
			res = jaloSession.search(new GenericQuery(otCode, genericCondition), ctx).getResult();
			assertCollection(Arrays.asList(new Object[]
			{ order1, order2 }), res);
			// not-find by less
			genericCondition = GenericCondition.createLessCondition(new GenericSearchField(otCode, AbstractOrder.DATE), less);
			res = jaloSession.search(new GenericQuery(otCode, genericCondition), ctx).getResult();
			assertCollection(Collections.EMPTY_LIST, res);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error : " + e);
		}
	}

	@Test
	public void testAddToEntry()
	{
		final Cart cart = jaloSession.getCart();
		assertNotNull(cart);

		final AbstractOrderEntry abstractOrderEntry1 = cart.addNewEntry(product, 1, unit1);
		assertNotNull(abstractOrderEntry1);

		assertNotNull("entry was NULL", abstractOrderEntry1);

		final AbstractOrderEntry abstractOrderEntry2 = cart.addNewEntry(product, 10, unit1, true);
		assertNotNull(abstractOrderEntry2);

		assertTrue("second entry was different (e=" + abstractOrderEntry1 + ",e2=" + abstractOrderEntry2 + ")",
				abstractOrderEntry1.equals(abstractOrderEntry2));
		assertTrue("quantity was not increased (q=" + abstractOrderEntry1.getQuantity() + ")", abstractOrderEntry1.getQuantity()
				.longValue() == 11);

		final AbstractOrderEntry abstractOrderEntry3 = cart.addNewEntry(product, 10, unit2, true);
		assertNotNull(abstractOrderEntry3);

		assertFalse("third entry was equal (e=" + abstractOrderEntry1 + ",e3=" + abstractOrderEntry3 + ")",
				abstractOrderEntry1.equals(abstractOrderEntry3));
		assertTrue("quantity was falsely increased (q=" + abstractOrderEntry1.getQuantity() + ")", abstractOrderEntry1
				.getQuantity().longValue() != 21);

		final AbstractOrderEntry abstractOrderEntry4 = cart.addNewEntry(product, 10, unit1);
		assertNotNull(abstractOrderEntry4);

		assertFalse("fourth entry was equal (e=" + abstractOrderEntry1 + ",e4=" + abstractOrderEntry4 + ")",
				abstractOrderEntry1.equals(abstractOrderEntry4));
		assertTrue("quantity was falsely increased (q=" + abstractOrderEntry1.getQuantity() + ")", abstractOrderEntry1
				.getQuantity().longValue() != 21);
	}

	public void createNewAddressTrans()
	{
		Address address1 = null, address2 = null;
		try
		{
			final Cart cart;
			final Address oldPA;
			final Address oldDA;

			final Transaction tx = Transaction.current();
			tx.begin();

			boolean success = false;
			try
			{
				cart = jaloSession.getCart();
				assertNotNull(cart);

				oldPA = cart.getPaymentAddress();
				oldDA = cart.getDeliveryAddress();

				assertNotNull(address1 = cart.createNewPaymentAddress());

				assertNotNull(address1);
				assertEquals(address1, cart.getPaymentAddress());

				assertNotNull(address2 = cart.createNewDeliveryAddress());
				assertNotNull(address2);
				assertEquals(address2, cart.getDeliveryAddress());

				success = true;
			}
			finally
			{
				if (success)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			assertEquals(address2, cart.getDeliveryAddress());

			assertEquals(address1, cart.getPaymentAddress());
			assertEquals(address1.getUser(), cart.getUser());
			assertEquals(address2.getUser(), cart.getUser());

			cart.setPaymentAddress(oldPA);
			cart.setDeliveryAddress(oldDA);
			assertEquals(oldPA, cart.getPaymentAddress());
			assertEquals(oldDA, cart.getDeliveryAddress());

			try
			{
				address1.getTitle();
				fail("a1 was not deleted");
			}
			catch (final Exception e)
			{
				assertNotNull(address1);
			}

			try
			{
				address2.getTitle();
				fail("a2 was not deleted");
			}
			catch (final Exception e)
			{
				assertNotNull(address2);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testCreateNewAddress()
	{
		final Cart cart = jaloSession.getCart();
		assertNotNull(cart);

		final Address oldPA = cart.getPaymentAddress();
		final Address oldDA = cart.getDeliveryAddress();

		final Address address1 = cart.createNewPaymentAddress();
		assertNotNull(address1);
		assertEquals(address1, cart.getPaymentAddress());
		assertEquals(address1, cart.getPaymentAddress());

		final Address address2 = cart.createNewDeliveryAddress();
		assertNotNull(address2);
		assertEquals(address2, cart.getDeliveryAddress());

		assertEquals(address1.getUser(), cart.getUser());
		assertEquals(address2.getUser(), cart.getUser());

		cart.setPaymentAddress(oldPA);
		cart.setDeliveryAddress(oldDA);
		assertEquals(oldPA, cart.getPaymentAddress());
		assertEquals(oldDA, cart.getDeliveryAddress());
	}

	@Test
	public void testGetAllEntries() throws Exception
	{
		final List coll = new ArrayList(order2.getAllEntries());
		assertEquals(4, coll.size());
		assertCollection(Arrays.asList(new Object[]
		{ orderEntry1, orderEntry2, orderEntry3, orderEntry4 }), coll);
		assertEquals(orderEntry1, coll.get(0));
		assertEquals(orderEntry2, coll.get(1));
		assertEquals(orderEntry3, coll.get(2));
		assertEquals(orderEntry4, coll.get(3));
		assertEquals(0, orderEntry1.getEntryNumber().intValue());
		assertEquals(1, orderEntry2.getEntryNumber().intValue());
		assertEquals(2, orderEntry3.getEntryNumber().intValue());
		assertEquals(3, orderEntry4.getEntryNumber().intValue());
	}

	@Test
	public void testGetEntry() throws Exception
	{
		assertEquals(0, orderEntry1.getEntryNumber().intValue());
		assertEquals(order2, orderEntry1.getOrder());
		assertEquals(jaloSession.getTypeManager().getComposedType(OrderEntry.class), orderEntry1.getComposedType());
		assertFalse(Transaction.current().isRunning());
		final AbstractOrderEntry abstractOrderEntry = order2.getEntry(0);
		assertEquals(product, abstractOrderEntry.getProduct());
	}

	@Test
	public void testGetEntries() throws Exception
	{
		assertEquals(0, orderEntry1.getEntryNumber().intValue());
		assertEquals(order2, orderEntry1.getOrder());
		assertEquals(1, orderEntry2.getEntryNumber().intValue());
		assertEquals(order2, orderEntry2.getOrder());
		assertEquals(2, orderEntry3.getEntryNumber().intValue());
		assertEquals(order2, orderEntry3.getOrder());
		assertCollection(Arrays.asList(new Object[]
		{ orderEntry1, orderEntry2, orderEntry3 }), order2.getEntries(0, 2));
	}

	@Test
	public void testCreateNewAddressTrans() throws Exception
	{
		final Address address;
		final Cart cart;

		final Transaction tx = Transaction.current();
		tx.begin();

		boolean success = true;
		try
		{
			cart = jaloSession.getCart();
			assertNull(cart.getPaymentAddress());
			address = cart.createNewPaymentAddress();
			assertEquals(address, cart.getPaymentAddress());

			success = true;
		}
		finally
		{
			if (success)
			{
				tx.commit();
			}
			else
			{
				tx.rollback();
			}
		}

		assertEquals(address, cart.getPaymentAddress());
	}

	@Test
	public void testRoundingError()
	{
		final Order order = OrderManager.getInstance().createOrder(jaloSession.getUser(),
				jaloSession.getSessionContext().getCurrency(), new Date(), false);

		final double subtotal = 3.95;
		final double payment = 0;
		final double delivery = 9.95;
		final double totalDiscount = 0;

		final double total = subtotal + payment + delivery - totalDiscount;
		order.setTotal(total);

		final String totalStr = Double.toString(order.getTotal());

		assertTrue("expected '13.9' or '13.90...' but got '"+totalStr+"'", "13.9".equals(totalStr) || totalStr.startsWith("13.90"));
	}
}
