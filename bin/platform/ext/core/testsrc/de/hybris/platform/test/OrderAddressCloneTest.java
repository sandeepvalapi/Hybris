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
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OrderAddressCloneTest extends HybrisJUnit4TransactionalTest
{
	private OrderManager orderManager;
	private UserManager userManager;
	private C2LManager c2lManager;

	private User user;
	private Address address;
	private Order order;
	private Currency curr;

	@Before
	public void setUp() throws Exception
	{
		// get ordermanager
		orderManager = jaloSession.getOrderManager();
		userManager = jaloSession.getUserManager();
		c2lManager = jaloSession.getC2LManager();
		// create currency
		assertNotNull(curr = c2lManager.createCurrency("RUE"));
		// create customer
		assertNotNull(user = userManager.createCustomer("TOM"));
		// create original address
		assertNotNull(address = userManager.createAddress(user));
		// create original order
		assertNotNull(order = orderManager.createOrder("OrderAddressCloneTest.order1", user, curr, new Date(), true));
	}

	@Test
	public void testClonedAddressReset()
	{
		// test cloning via setPaymentAddress()
		order.setPaymentAddress(address);
		final Address addressClone = order.getPaymentAddress();

		assertNotSame(address, addressClone);
		assertFalse(addressClone.equals(address));

		// test cloning via setDeliveryAddress
		order.setDeliveryAddress(address);
		final Address addressClone2 = order.getDeliveryAddress();

		assertNotSame(address, addressClone2);
		assertFalse(addressClone2.equals(address));
		assertNotSame(addressClone, addressClone2);
		assertFalse(addressClone2.equals(addressClone));

		order.setPaymentAddress(null);
		assertFalse(addressClone.isAlive());

		order.setDeliveryAddress(null);
		assertFalse(addressClone2.isAlive());

		assertTrue(address.isAlive());
	}

	@Test
	public void testAddressNotCloned() throws ConsistencyCheckException
	{
		// test cloning via setPaymentAddress()
		order.setPaymentAddress(address);
		final Address addressClone = order.getPaymentAddress();

		assertNotSame(address, addressClone);
		assertFalse(addressClone.equals(address));

		// prepare special address that doesn't trigger cloning
		final Address specialOne = userManager.createAddress(order);
		specialOne.setDuplicate(true);
		// now use for payment
		order.setPaymentAddress(specialOne);
		final Address addressNotCloned = order.getPaymentAddress();
		assertEquals(specialOne, addressNotCloned);
		assertFalse(addressClone.isAlive());

		// test correct removal if replaced
		order.setPaymentAddress(address);
		final Address addressClone2 = order.getPaymentAddress();

		assertNotSame(address, addressClone2);
		assertFalse(addressClone2.equals(address));
		assertFalse(specialOne.isAlive());

		// test no-copy not applicable ( address if copy BUT owner if wrong

		final Order order2 = orderManager.createOrder("OrderAddressCloneTest.order1", user, curr, new Date(), true);

		order2.setPaymentAddress(addressClone2);
		final Address addressClone3 = order2.getPaymentAddress();
		assertFalse(addressClone2.equals(addressClone3));
		assertTrue(addressClone3.isDuplicate().booleanValue());
		assertEquals(order2, addressClone3.getOwner());
		assertEquals(addressClone2, addressClone3.getOriginal());
	}

	@Test
	public void testAddressCloningSameAddressForPaymentAndDelivery()
	{
		// test cloning via setPaymentAddress()
		order.setPaymentAddress(address);
		final Address addressClone = order.getPaymentAddress();

		assertNotSame(address, addressClone);
		assertFalse(addressClone.equals(address));


		// test cloning via setDeliveryAddress
		order.setDeliveryAddress(address);
		final Address addressClone2 = order.getDeliveryAddress();

		assertNotSame(address, addressClone2);
		assertFalse(addressClone2.equals(address));
		assertNotSame(addressClone, addressClone2);
		assertFalse(addressClone2.equals(addressClone));


		// test re-use of first clone for both
		order.setDeliveryAddress(addressClone);
		final Address addressCloneSame = order.getDeliveryAddress();
		assertTrue(addressCloneSame.equals(addressClone));

		// now second clone must be deleted
		assertFalse(addressClone2.isAlive());

		// test cloning delivery address again -> shared clone must NOT be deleted (still referenced as payment address)
		order.setDeliveryAddress(address);
		final Address addressClone3 = order.getDeliveryAddress();
		assertNotSame(address, addressClone3);
		assertFalse(addressClone3.equals(address));
		assertFalse(addressClone3.equals(addressClone));
		assertTrue(addressClone.isAlive());
		assertEquals(addressClone, order.getPaymentAddress());

		// test cloning payment address again -> now shared clone MUST be deleted (last reference gone)
		order.setPaymentAddress(address);
		final Address addressClone4 = order.getPaymentAddress();
		assertNotSame(address, addressClone4);
		assertFalse(addressClone4.equals(address));
		assertFalse(addressClone4.equals(addressClone3));

		assertFalse(addressClone.isAlive());
		assertEquals(addressClone3, order.getDeliveryAddress());
	}

	@Test
	public void testAddressCloning() throws Exception
	{
		// first time clone test
		order.setPaymentAddress(address);
		final Address address2 = order.getPaymentAddress();
		assertNotSame(address, address2);
		assertFalse(address2.equals(address));

		// second time clone test
		final Order order2 = orderManager.createOrder(order);
		assertNotNull(order2);
		assertCollection(Arrays.asList(order, order2), user.getOrders());
		Address address3 = order2.getPaymentAddress();
		assertNotSame(address, address3);
		assertNotSame(address2, address3);
		assertFalse(address3.equals(address));
		assertFalse(address3.equals(address2));

		// test order removal
		order.remove(); // should remove a2
		assertFalse(address2.isAlive());
		assertNotNull(order);
		assertCollection(Arrays.asList(order2), user.getOrders());
		order = null;
		address3 = order2.getPaymentAddress();
		// is the clone still there ?
		assertTrue("a3 is gone", address3 != null && !address.equals(address3));

		order2.setPaymentAddressNoCopy(address);
		assertEquals(address, order2.getPaymentAddress());

		order2.setDeliveryAddressNoCopy(address);
		assertEquals(address, order2.getDeliveryAddress());
	}

}
