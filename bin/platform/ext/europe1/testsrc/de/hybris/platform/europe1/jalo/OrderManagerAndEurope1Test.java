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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.TaxValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OrderManagerAndEurope1Test extends HybrisJUnit4Test
{
	private final static Logger LOG = Logger.getLogger(OrderManagerAndEurope1Test.class);

	private OrderManager orderManager;
	private UserManager userManager;
	private ProductManager productManager;
	private C2LManager c2lm;
	private Country deC;
	private Currency oldCurr, europe;
	private Customer dirk;
	private Customer tom;
	private Product laserfazer;
	private Product copylifepapier;
	private Product lampe;
	private Unit uPs;
	private Tax tax;
	@SuppressWarnings("unused")
	private Country addr1Country, addr2Country, addr3Country;

	private final String[] addr1Fields = new String[]
	{ Address.FIRSTNAME, "Dirk", Address.LASTNAME, "Ahlrichs", Address.COMPANY, "Mainau GmbH", Address.STREETNAME,
			"Kriegstein 67", Address.TOWN, "Koeln", Address.POSTALCODE, "50400", Address.EMAIL, "ahlrichs@yahoo.com" };

	private final String[] addr2Fields = new String[]
	{ Address.FIRSTNAME, "Dirk", Address.LASTNAME, "Ahlrichs", Address.COMPANY, "Mainau GmbH", Address.STREETNAME,
			"Friedrichstrasse 12", Address.TOWN, "Berlin", Address.POSTALCODE, "10808", Address.EMAIL, "ahlrichs@mainau.de" };
	private final String[] addr3Fields = new String[]
	{ Address.FIRSTNAME, "Dirk", Address.LASTNAME, "Ahlrichs", Address.COMPANY, "Mainau GmbH", Address.STREETNAME,
			"Jungfernstieg 25", Address.TOWN, "Hamburg", Address.POSTALCODE, "20434", Address.EMAIL, "ahlrichs@mainau.de" };
	private Order cartOrder;
	private Order order1;
	private Order order2;
	private Order order3;

	private PriceFactory sessionPriceFactoryBefore = null;


	@Before
	public void setUp() throws Exception
	{
		assertNoSessionPriceFactoryActive(jaloSession);

		assertTrue(OrderManager.getInstance().getPriceFactory() instanceof Europe1PriceFactory);

		// Managers
		orderManager = jaloSession.getOrderManager();
		productManager = jaloSession.getProductManager();
		userManager = jaloSession.getUserManager();
		c2lm = jaloSession.getC2LManager();

		// needed other objects
		assertNotNull(europe = c2lm.createCurrency("om-eu"));
		oldCurr = jaloSession.getSessionContext().getCurrency();
		jaloSession.getSessionContext().setCurrency(europe);

		//eu.setActive( true );
		europe.setSymbol("E");
		europe.setDigits(2);
		europe.setConversionFactor(1);
	}

	protected void assertNoSessionPriceFactoryActive(final JaloSession jaloSession)
	{
		sessionPriceFactoryBefore = jaloSession.getPriceFactory();
		jaloSession.setPriceFactory(null);
		assertNull(jaloSession.getPriceFactory());
	}


	@After
	public void tearDown() throws Exception
	{
		jaloSession.setPriceFactory(sessionPriceFactoryBefore);
		jaloSession.getSessionContext().setCurrency(oldCurr);
	}

	@Test
	public void testAll() throws Exception
	{

		assertNotNull(deC = c2lm.createCountry("om-de"));
		//deC.setActive( true );
		assertNotNull(dirk = userManager.createCustomer("dirk"));
		dirk.setName("Dirk Ahlrichs");
		dirk.setDescription("dirk surfer");
		dirk.setPassword("krid");
		Address adr = dirk.createAddress();
		for (int i = 0; i < addr1Fields.length; i += 2)
		{
			adr.setAttribute(addr1Fields[i], addr1Fields[i + 1]);
		}
		adr.setCountry(addr1Country = deC);
		dirk.setDefaultPaymentAddress(adr);

		adr = dirk.createAddress();
		for (int i = 0; i < addr2Fields.length; i += 2)
		{
			adr.setAttribute(addr2Fields[i], addr2Fields[i + 1]);
		}
		adr.setCountry(addr2Country = deC);
		dirk.setDefaultDeliveryAddress(adr);
		adr = dirk.createAddress();
		for (int i = 0; i < addr3Fields.length; i += 2)
		{
			adr.setAttribute(addr3Fields[i], addr3Fields[i + 1]);
		}
		adr.setCountry(addr3Country = deC);
		assertNotNull(tom = userManager.createCustomer("tom"));
		tom.setName("Thomas Latka");
		tom.setPassword("mot");
		adr = tom.createAddress();
		adr.setAttribute(Address.FIRSTNAME, "Tom");
		adr.setAttribute(Address.LASTNAME, "Latka");
		adr.setAttribute(Address.COMPANY, "Special GmbH");
		adr.setAttribute(Address.STREETNAME, "Profistrasse 1");
		adr.setAttribute(Address.TOWN, "Muenchen");
		adr.setAttribute(Address.POSTALCODE, "80000");
		adr.setAttribute(Address.EMAIL, "latka@special.com");
		adr.setCountry(deC);
		tom.setDefaultPaymentAddress(adr);
		tom.setDefaultDeliveryAddress(adr);
		assertNotNull(uPs = productManager.createUnit("pieces", "pieces"));
		uPs.setConversionFactor(1.0);
		assertNotNull(copylifepapier = productManager.createProduct("10011"));
		copylifepapier.setUnit(uPs);
		assertNotNull(lampe = productManager.createProduct("10012"));
		lampe.setUnit(uPs);
		assertNotNull(laserfazer = productManager.createProduct("10013"));
		laserfazer.setUnit(uPs);
		final Europe1PriceFactory priceFactory = (Europe1PriceFactory) jaloSession.getOrderManager().getPriceFactory();
		assertNotNull(priceFactory.createPriceRow(copylifepapier, null, null, null, 1, europe, uPs, 1, true, null, 4.90));
		assertNotNull(priceFactory.createPriceRow(lampe, null, null, null, 1, europe, uPs, 1, true, null, 5.90));
		assertNotNull(priceFactory.createPriceRow(laserfazer, null, null, null, 1, europe, uPs, 1, true, null, 6.90));
		assertNotNull(tax = orderManager.createTax("tax"));
		tax.setValue(16);
		assertNotNull(priceFactory.createTaxRow(null, null, null, null, tax, null, null));
		// Orders
		assertNotNull(order1 = orderManager.createOrder("100010", dirk, europe, new Date(System.currentTimeMillis()), true));
		//order1.setStatus("Pending");
		order1.setDeliveryAddress(dirk.getDefaultDeliveryAddress());
		order1.setPaymentAddress(dirk.getDefaultPaymentAddress());
		final AbstractOrderEntry entry11 = order1.addNewEntry(lampe, 1, uPs);
		assertNotNull(entry11);
		final AbstractOrderEntry entry12 = order1.addNewEntry(laserfazer, 3, uPs);
		assertNotNull(entry12);
		assertNotNull(order2 = orderManager.createOrder("100011", dirk, europe, new Date(System.currentTimeMillis()), true));
		//order2.setStatus("Completed");
		order2.setDeliveryAddress(dirk.getDefaultDeliveryAddress());
		order2.setPaymentAddress(dirk.getDefaultPaymentAddress());
		final AbstractOrderEntry entry21 = order2.addNewEntry(laserfazer, 1, uPs);
		assertNotNull(entry21);
		final AbstractOrderEntry entry22 = order2.addNewEntry(copylifepapier, 1, uPs);
		assertNotNull(entry22);
		assertNotNull(order3 = orderManager.createOrder("100012", tom, europe, new Date(System.currentTimeMillis()), true));
		//order3.setStatus("Canceled");
		order3.setDeliveryAddress(tom.getDefaultDeliveryAddress());
		order3.setPaymentAddress(tom.getDefaultPaymentAddress());
		final AbstractOrderEntry entry31 = order3.addNewEntry(laserfazer, 10, uPs);
		assertNotNull(entry31);
		final AbstractOrderEntry entry32 = order3.addNewEntry(lampe, 10, uPs);
		assertNotNull(entry32);

		jaloSession.getSessionContext().setCurrency(europe);
		final Cart cart = jaloSession.getCart();
		LOG.info("### Using cart: " + cart.getClass().getName() + ", session cart type: "
				+ jaloSession.getSessionContext().getAttribute(JaloSession.CART_TYPE) + " configured cart "
				+ jaloSession.getTenant().getConfig().getParameter(JaloSession.CART_TYPE));
		cart.setDeliveryAddress(dirk.getDefaultDeliveryAddress());
		cart.setPaymentAddress(dirk.getDefaultPaymentAddress());
		final AbstractOrderEntry cartEntry = cart.addNewEntry(laserfazer, 10, uPs);
		assertNotNull(cartEntry);
		try
		{
			cart.calculate();
		}
		catch (final de.hybris.platform.jalo.order.price.JaloPriceFactoryException e)
		{
			e.printStackTrace(System.err);
			fail("Exception calculation order.");
		}

		assertNotNull(cartOrder = orderManager.createOrder(cart));
		assertEquals(cart.getDate(), cartOrder.getDate());
		assertEquals(cart.getCode(), cartOrder.getCode());
		//assertEquals( cart.getStatus(), cartOrder.getStatus() );			
		assertEquals(cart.getStatusInfo(), cartOrder.getStatusInfo());
		assertEquals(cart.getAllEntries().size(), cartOrder.getAllEntries().size());
		assertEquals(cart.getUser().getLogin(), cartOrder.getUser().getLogin());
		try
		{
			cartOrder.calculate();
		}
		catch (final de.hybris.platform.jalo.order.price.JaloPriceFactoryException e)
		{
			e.printStackTrace(System.err);
			fail("Exception calculation order.");
		}
		assertEquals(cart.getCurrency().getIsoCode(), cartOrder.getCurrency().getIsoCode());
		assertEquals(cart.getGlobalDiscountValues(), cartOrder.getGlobalDiscountValues());
		assertEquals(cart.getPaymentCost(), cartOrder.getPaymentCost());
		assertEquals(cart.getTotalPrice(), cartOrder.getTotalPrice());
		assertEquals(cart.getTotalTax(), cartOrder.getTotalTax());
		assertEquals(cart.getTotalTaxValues(), cartOrder.getTotalTaxValues());
		assertEquals(Boolean.valueOf(cart.isNet().booleanValue()), Boolean.valueOf(cartOrder.isNet().booleanValue()));
		//cart is fixed to session
		cart.setDeliveryAddress(null);
		cart.setPaymentAddress(null);
		cart.removeAllEntries();
		cartOrder.remove();
		assertNotNull(cartOrder);
		cartOrder = null;

		try
		{
			order1.calculate();
			order2.calculate();
			order3.calculate();
		}
		catch (final de.hybris.platform.jalo.order.price.JaloPriceFactoryException e)
		{
			e.printStackTrace(System.err);
			fail("Exception calculation order.");
		}
	}

	@Test
	public void testTaxCaching() throws JaloInvalidParameterException, ConsistencyCheckException, JaloPriceFactoryException
	{
		final String taxCacheBefore = jaloSession.getTenant().getConfig().getParameter(Europe1Constants.KEY_CACHE_TAXES);
		try
		{
			final Europe1PriceFactory priceFactory1 = Europe1PriceFactory.getInstance();
			jaloSession.getTenant().getConfig().setParameter(Europe1Constants.KEY_CACHE_TAXES, "true");
			priceFactory1.invalidateTaxCache();

			final Tax tax1 = OrderManager.getInstance().createTax("FULL");
			final Tax tax2 = OrderManager.getInstance().createTax("HALF");

			final EnumerationValue full = EnumerationManager.getInstance().createEnumerationValue(
					Europe1Constants.TC.PRODUCTTAXGROUP, "test_full");
			final EnumerationValue half = EnumerationManager.getInstance().createEnumerationValue(
					Europe1Constants.TC.PRODUCTTAXGROUP, "test_half");

			final EnumerationValue userGrp1 = EnumerationManager.getInstance().createEnumerationValue(
					Europe1Constants.TC.PRODUCTTAXGROUP, "userGrp1");
			final EnumerationValue userGrp2 = EnumerationManager.getInstance().createEnumerationValue(
					Europe1Constants.TC.PRODUCTTAXGROUP, "userGrp2");

			final User user = jaloSession.getUser();
			final Product product = ProductManager.getInstance().createProduct("foo");
			final Product product2 = ProductManager.getInstance().createProduct("bar");
			final Unit unit = ProductManager.getInstance().createUnit("xxx", "yyy");


			priceFactory1.setEurope1PriceFactory_PTG(product, full);
			priceFactory1.setEurope1PriceFactory_PTG(product2, half);
			priceFactory1.setEurope1PriceFactory_UTG(user, userGrp1);

			final TaxRow tr1 = priceFactory1.createTaxRow(null, full, null, userGrp1, tax1, null, Double.valueOf(19));
			final TaxRow tr2 = priceFactory1.createTaxRow(null, half, null, userGrp1, tax2, null, Double.valueOf(7));
			final TaxRow tr3 = priceFactory1.createTaxRow(null, full, null, userGrp2, tax1, null, Double.valueOf(25));
			final TaxRow tr4 = priceFactory1.createTaxRow(null, half, null, userGrp2, tax2, null, Double.valueOf(10));

			final Cart cart = jaloSession.getCart();
			final CartEntry entry1 = (CartEntry) cart.addNewEntry(product, 2, unit);
			final CartEntry entry2 = (CartEntry) cart.addNewEntry(product2, 10, unit);

			Collection<TaxValue> taxes = priceFactory1.getTaxValues(entry1);
			assertEquals(Collections.singletonList(new TaxValue(tax1.getCode(), 19, false, null)), taxes);
			taxes = priceFactory1.getTaxValues(entry2);
			assertEquals(Collections.singletonList(new TaxValue(tax2.getCode(), 7, false, null)), taxes);

			priceFactory1.setEurope1PriceFactory_UTG(user, userGrp2);

			taxes = priceFactory1.getTaxValues(entry1);
			assertEquals(Collections.singletonList(new TaxValue(tax1.getCode(), 25, false, null)), taxes);
			taxes = priceFactory1.getTaxValues(entry2);
			assertEquals(Collections.singletonList(new TaxValue(tax2.getCode(), 10, false, null)), taxes);

			final TaxRow tr5 = priceFactory1.createTaxRow(null, null, null, null, tax2, null, Double.valueOf(100));

			priceFactory1.setEurope1PriceFactory_PTG(product2, null);
			taxes = priceFactory1.getTaxValues(entry2);
			assertEquals(Collections.singletonList(new TaxValue(tax2.getCode(), 100, false, null)), taxes);

			final TaxRow tr6 = priceFactory1.createTaxRow(product, null, null, null, tax1, null, Double.valueOf(30));

			taxes = priceFactory1.getTaxValues(entry1);
			assertEquals(
					new HashSet(Arrays.asList(new TaxValue(tax1.getCode(), 30, false, null), new TaxValue(tax1.getCode(), 25, false,
							null), new TaxValue(tax2.getCode(), 100, false, null))), new HashSet(taxes));

			final TaxRow tr7 = priceFactory1.createTaxRow(null, null, user, null, tax1, null, Double.valueOf(40));

			taxes = priceFactory1.getTaxValues(entry2);
			assertEquals(
					new HashSet(Arrays.asList(new TaxValue(tax1.getCode(), 40, false, null), new TaxValue(tax2.getCode(), 100, false,
							null))), new HashSet(taxes));

			assertTrue(priceFactory1.isCachingTaxes());

			final int count = 10000;
			final long ts1 = System.currentTimeMillis();
			for (int i = 0; i < count; i++)
			{
				taxes = priceFactory1.getTaxValues(entry2);
			}
			final long ts2 = System.currentTimeMillis();

			jaloSession.getTenant().getConfig().setParameter(Europe1Constants.KEY_CACHE_TAXES, "false");
			priceFactory1.invalidateTaxCache();
			assertFalse(priceFactory1.isCachingTaxes());

			final long ts3 = System.currentTimeMillis();
			for (int i = 0; i < count; i++)
			{
				taxes = priceFactory1.getTaxValues(entry2);
			}
			final long ts4 = System.currentTimeMillis();

			final long cachedTime = (ts2 - ts1);
			final long nonCachedTime = (ts4 - ts3);

			System.out.println("caching taxes perf [ " + count + " invocations cached=" + cachedTime + "ms, non-cached="
					+ nonCachedTime + "ms, difference=" + (((nonCachedTime - cachedTime) * 100) / nonCachedTime) + "%");

			tr1.remove();
			tr2.remove();
			tr3.remove();
			tr4.remove();
			tr5.remove();
			tr6.remove();
			tr7.remove();

			jaloSession.getTenant().getConfig().setParameter(Europe1Constants.KEY_CACHE_TAXES, "true");
			priceFactory1.invalidateTaxCache();
			assertTrue(priceFactory1.isCachingTaxes());

			assertEquals(Collections.EMPTY_LIST, priceFactory1.getTaxValues(entry1));
		}
		finally
		{
			jaloSession.getTenant().getConfig().setParameter(Europe1Constants.KEY_CACHE_TAXES, taxCacheBefore);
		}

	}
}
