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
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * tests behaviour of Customer
 * 
 * 
 * 
 */
@IntegrationTest
public class CustomerCartTest extends HybrisJUnit4TransactionalTest
{
	private final static Logger LOG = Logger.getLogger(CustomerCartTest.class);

	private Cart cart;
	private Customer customer;
	private Cart savedCart1;
	private Cart savedCart2;
	private Cart errorCart;

	private Unit unit;
	private Product product1;
	private Product product2;

	private ProductManager pm;

	@Before
	public void setUp() throws Exception
	{
		cart = jaloSession.getCart();
		customer = (Customer) jaloSession.getUser();
		pm = jaloSession.getProductManager();
		assertNotNull(product1 = pm.createProduct("test.product1"));
		assertNotNull(product2 = pm.createProduct("test.product2"));
		assertNotNull(unit = pm.createUnit("packaging", "pieces"));

		cart.addNewEntry(product1, 3, unit);
		cart.addNewEntry(product2, 4, unit);
	}

	@After
	public void tearDown() throws Exception
	{
		try
		{
			if (errorCart != null)
			{
				errorCart.remove();
			}
		}
		finally
		{
			// DOCTODO Document reason, why this block is empty
		}
	}

	@Test
	public void testCart() throws Exception
	{
		assertNotNull(cart);
		assertNotNull(customer);
		LOG.info("### Using cart: " + cart.getClass().getName() + ", session cart type: "
				+ jaloSession.getSessionContext().getAttribute(JaloSession.CART_TYPE) + " configured cart "
				+ jaloSession.getTenant().getConfig().getParameter(JaloSession.CART_TYPE));
		savedCart1 = customer.saveCurrentCart("test_1");

		assertEquals("test_1", savedCart1.getCode());
		final ArrayList carts = new ArrayList(customer.getCarts());
		assertTrue("saved carts didnt contain " + savedCart1 + " got " + carts, carts.contains(savedCart1));

		final ArrayList o_entries = new ArrayList(savedCart1.getAllEntries());
		assertEquals("", 2, o_entries.size());

		final ArrayList products = new ArrayList();
		products.add(((AbstractOrderEntry) o_entries.get(0)).getProduct());
		products.add(((AbstractOrderEntry) o_entries.get(1)).getProduct());

		assertTrue(products.contains(product1));
		assertTrue(products.contains(product2));

		products.clear();

		savedCart2 = customer.saveCurrentCart("test_2");

		carts.clear();
		carts.addAll(customer.getCarts());

		assertTrue(carts.contains(savedCart1));
		assertTrue(carts.contains(savedCart2));

		try
		{
			errorCart = customer.saveCurrentCart(savedCart1.getCode());
			fail("saved duplicate cart but expected ConsistencyCheckException");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine here
		}

		try
		{
			savedCart1.remove();
			savedCart2.remove();
		}
		catch (final ConsistencyCheckException jcex)
		{
			assertNotNull("Problems to remove saved carts.", null);
		}

		try
		{
			savedCart1.getCode();
			fail("savedCart1 was not removed property");
		}
		catch (final Exception ex)
		{
			// fine
		}

		try
		{
			savedCart2.getCode();
			fail("savedCart2 was not removed property");
		}
		catch (final Exception ex)
		{
			// fine
		}
	}
}
