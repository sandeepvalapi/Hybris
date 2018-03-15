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
package de.hybris.platform.order;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.delivery.DefaultDeliveryCostsStrategy;
import de.hybris.platform.jalo.order.delivery.DeliveryCostsStrategy;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.internal.jalo.order.InMemoryCart;
import de.hybris.platform.servicelayer.internal.jalo.order.InMemoryCartEntry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest(excludedAppserver = "websphere,weblogic")
public class InMemoryCartTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(InMemoryCartTest.class);
	private static double DELTA = 0.00000001;

	private PriceFactory previousPf;
	private TestPriceFactory priceFactory;

	private String cartTypeBefore;
	private DeliveryCostsStrategy originalStrategy;

	@Before
	public void setUp()
	{
		this.priceFactory = new TestPriceFactory();
		previousPf = jaloSession.getSessionContext().getPriceFactory();
		jaloSession.getSessionContext().setPriceFactory(priceFactory);
		cartTypeBefore = Config.getParameter(JaloSession.CART_TYPE);
		originalStrategy = OrderManager.getInstance().getDeliveryCostsStrategy();
		OrderManager.getInstance().setDeliveryCostsStrategy(new DefaultDeliveryCostsStrategy());
	}

	@After
	public void tearDown()
	{
		OrderManager.getInstance().setDeliveryCostsStrategy(originalStrategy);
		Config.setParameter(JaloSession.CART_TYPE, cartTypeBefore);
		jaloSession.getSessionContext().setPriceFactory(previousPf);
	}

	@Test
	public void testSessionCart()
	{
		Cart normalCart = jaloSession.getCart();
		assertFalse(normalCart instanceof InMemoryCart);
		jaloSession.removeCart();
		assertFalse(jaloSession.hasCart());

		final ComposedType composedType = TypeManager.getInstance().getComposedType(InMemoryCart.class);

		Config.setParameter(JaloSession.CART_TYPE, composedType.getCode());

		Cart tempCart = jaloSession.getCart();
		assertTrue(tempCart instanceof InMemoryCart);
		jaloSession.removeCart();
		assertFalse(jaloSession.hasCart());

		Config.setParameter(JaloSession.CART_TYPE, null);

		normalCart = jaloSession.getCart();
		assertFalse(normalCart instanceof InMemoryCart);
		jaloSession.removeCart();
		assertFalse(jaloSession.hasCart());

		jaloSession.getSessionContext().setAttribute(JaloSession.CART_TYPE, composedType);

		tempCart = jaloSession.getCart();
		assertTrue(tempCart instanceof InMemoryCart);
		jaloSession.removeCart();
		assertFalse(jaloSession.hasCart());

		jaloSession.getSessionContext().setAttribute(JaloSession.CART_TYPE, composedType.getCode());

		tempCart = jaloSession.getCart();
		assertTrue(tempCart instanceof InMemoryCart);
		jaloSession.removeCart();
		assertFalse(jaloSession.hasCart());

		jaloSession.getSessionContext().removeAttribute(JaloSession.CART_TYPE);

		normalCart = jaloSession.getCart();
		assertFalse(normalCart instanceof InMemoryCart);
		jaloSession.removeCart();
		assertFalse(jaloSession.hasCart());
	}

	@Test
	public void testCart() throws Exception
	{
		final User user = jaloSession.getUser();
		final Currency curr = jaloSession.getSessionContext().getCurrency();

		final ComposedType composedType = TypeManager.getInstance().getComposedType(InMemoryCart.class);
		final Map<String, Object> values = new HashMap<String, Object>();
		// YTODO let code be generated !!!
		values.put(Cart.CODE, "TempCart");
		values.put(Cart.USER, user);
		values.put(Cart.CURRENCY, curr);
		final InMemoryCart cart = (InMemoryCart) composedType.newInstance(values);

		assertNotNull(cart);
		assertNotNull(cart.getPK());
		assertNotNull(cart.getCreationTime());
		assertTrue(System.currentTimeMillis() >= cart.getCreationTime().getTime());
		assertTrue(cart.isAlive());
		assertEquals(composedType, cart.getComposedType());
		assertEquals("TempCart", cart.getCode());
		assertEquals(user, cart.getUser());
		assertEquals(curr, cart.getCurrency());

		assertFalse(cart.isCacheBound());
		assertEquals(cart, cart.getCacheBoundItem());

		assertEquals(Collections.EMPTY_LIST, cart.getAllEntries());

		assertFalse(cart.isCalculated().booleanValue());

		cart.calculate();
		assertCalculated(cart, true);

		checkGetters(cart);
		for (final InMemoryCartEntry e : (List<InMemoryCartEntry>) cart.getAllEntries())
		{
			checkGetters(e);
		}

		final Product product1 = ProductManager.getInstance().createProduct("foo");
		final Product product2 = ProductManager.getInstance().createProduct("bar");
		final Unit unit1 = ProductManager.getInstance().createUnit("kg", "weight");
		final Unit unit2 = ProductManager.getInstance().createUnit("pieces", "pieces");

		cart.addNewEntry(product1, 3, unit1);
		cart.addNewEntry(product2, 10, unit1);

		assertCalculated(cart, false);

		final List<InMemoryCartEntry> entries = cart.getAllEntries();
		assertNotNull(entries);
		assertEquals(2, entries.size());

		final InMemoryCartEntry memoryCartEntry1 = entries.get(0);
		assertNotNull(memoryCartEntry1);

		assertEquals(cart, memoryCartEntry1.getOrder());
		assertEquals(product1, memoryCartEntry1.getProduct());
		assertEquals(3, memoryCartEntry1.getQuantity().longValue());
		assertEquals(unit1, memoryCartEntry1.getUnit());
		assertEquals(0, memoryCartEntry1.getEntryNumber().intValue());

		assertEquals(memoryCartEntry1, cart.getEntry(0));
		assertEquals(Collections.singletonList(memoryCartEntry1), cart.getEntries(0, 0));
		assertEquals(Collections.singletonList(memoryCartEntry1), cart.getEntriesByProduct(product1));

		final InMemoryCartEntry memoryCartEntry2 = entries.get(1);
		assertNotNull(memoryCartEntry2);

		assertEquals(cart, memoryCartEntry2.getOrder());
		assertEquals(product2, memoryCartEntry2.getProduct());
		assertEquals(10, memoryCartEntry2.getQuantity().longValue());
		assertEquals(unit1, memoryCartEntry2.getUnit());
		assertEquals(1, memoryCartEntry2.getEntryNumber().intValue());

		assertEquals(memoryCartEntry2, cart.getEntry(1));
		assertEquals(Collections.singletonList(memoryCartEntry2), cart.getEntries(1, 1));
		assertEquals(Collections.singletonList(memoryCartEntry2), cart.getEntriesByProduct(product2));

		assertEquals(Arrays.asList(memoryCartEntry1, memoryCartEntry2), cart.getEntries(0, 1));

		testCalculation(cart, curr, unit2);

		testOrderCreation(cart);

		testModelLayer(cart);

		testCartService(cart, unit1);

		testSerialization(cart);
	}


	protected void testSerialization(final InMemoryCart cart) throws Exception
	{
		LOG.info("testSerialization");

		ByteArrayOutputStream bos;
		final ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos = new ByteArrayOutputStream());

		try
		{
			objectOutputStream.writeObject(cart);
		}
		finally
		{
			objectOutputStream.close();
		}
		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		InMemoryCart copy = null;
		try
		{
			copy = (InMemoryCart) ois.readObject();
		}
		finally
		{
			ois.close();
		}
		assertNotNull(copy);
		assertNotSame(cart, copy);
		assertCalculated(copy, cart.isCalculated().booleanValue());
		assertEquals(cart.getTenant(), copy.getTenant());
		assertEquals(cart.getPK(), copy.getPK());
		assertEquals(cart.getComposedType(), copy.getComposedType());
		assertEquals(cart.getUser(), copy.getUser());
		assertEquals(cart.getCurrency(), copy.getCurrency());
		assertEquals(cart.getDate(), copy.getDate());
		assertEquals(cart.isNet(), copy.isNet());
		assertEquals(cart.getPaymentAddress(), copy.getPaymentAddress());
		assertEquals(cart.getPaymentInfo(), copy.getPaymentInfo());
		assertEquals(cart.getDeliveryAddress(), copy.getDeliveryAddress());
		assertEquals(cart.getTotalTaxValues(), copy.getTotalTaxValues());
		assertEquals(cart.getGlobalDiscountValues(), copy.getGlobalDiscountValues());
		assertEquals(cart.getAllEntries(), copy.getAllEntries());
		assertEquals(cart.getTotal(), copy.getTotal(), DELTA);
		assertEquals(cart.getSubtotal().doubleValue(), copy.getSubtotal().doubleValue(), DELTA);
		assertEquals(cart.getTotalTax().doubleValue(), copy.getTotalTax().doubleValue(), DELTA);
		assertEquals(cart.getTotalDiscounts().doubleValue(), copy.getTotalDiscounts().doubleValue(), DELTA);

		final List<InMemoryCartEntry> cartEntries = cart.getAllEntries();
		final List<InMemoryCartEntry> copyCartEntries = copy.getAllEntries();

		assertEquals(cartEntries.size(), copyCartEntries.size());

		for (int i = 0; i < cartEntries.size(); i++)
		{
			final InMemoryCartEntry memoryCartEntry1 = cartEntries.get(i);
			final InMemoryCartEntry memoryCartEntry2 = copyCartEntries.get(i);

			assertNotSame(memoryCartEntry1, memoryCartEntry2);
			assertEquals(memoryCartEntry1.getEntryNumber(), memoryCartEntry2.getEntryNumber());
			assertEquals(memoryCartEntry1.isCalculated(), memoryCartEntry2.isCalculated());
			assertEquals(memoryCartEntry1.getProduct(), memoryCartEntry2.getProduct());
			assertEquals(memoryCartEntry1.getUnit(), memoryCartEntry2.getUnit());
			assertEquals(memoryCartEntry1.getQuantity(), memoryCartEntry2.getQuantity());
			assertEquals(memoryCartEntry1.getBasePrice().doubleValue(), memoryCartEntry2.getBasePrice().doubleValue(), DELTA);
			assertEquals(memoryCartEntry1.getTotalPrice().doubleValue(), memoryCartEntry2.getTotalPrice().doubleValue(), DELTA);
			assertEquals(memoryCartEntry1.getTaxValues(), memoryCartEntry2.getTaxValues());
			assertEquals(memoryCartEntry1.getDiscountValues(), memoryCartEntry2.getDiscountValues());
		}
	}

	protected void testCartService(final InMemoryCart cart, final Unit unit) throws InvalidCartException
	{
		LOG.info("testCartService");


		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		final CartService cartService = (CartService) Registry.getApplicationContext().getBean("cartService");

		jaloSession.setCart(cart);

		assertTrue(jaloSession.hasCart());
		assertEquals(cart, jaloSession.getCart());

		final CartModel model = cartService.getSessionCart();

		assertCartModel(modelService, cart, model);

		final Product product3 = ProductManager.getInstance().createProduct("another");

		priceFactory.setBasePrice(product3, new PriceValue(cart.getCurrency().getIsoCode(), 9.99, cart.isNet().booleanValue()));

		cartService.addToCart(model, (ProductModel) modelService.get(product3), 30, (UnitModel) modelService.get(unit));

		final InMemoryCartEntry newOne = (InMemoryCartEntry) cart.getEntry(2);
		assertNotNull(newOne);
		assertEquals(product3, newOne.getProduct());
		assertEquals(30, newOne.getQuantity().longValue());
		assertEquals(unit, newOne.getUnit());
		assertEquals(30d * 9.99d, newOne.getTotalPrice().doubleValue(), DELTA);
		assertEquals(9.99d, newOne.getBasePrice().doubleValue(), DELTA);
		assertTrue(newOne.isCalculated().booleanValue());

		assertCalculated(cart, true);

		assertCartModel(modelService, cart, model);

		final InMemoryCartEntry memoryCartEntry1 = (InMemoryCartEntry) cart.getEntry(0);
		final InMemoryCartEntry memoryCartEntry2 = (InMemoryCartEntry) cart.getEntry(1);
		final InMemoryCartEntry memoryCartEntry3 = (InMemoryCartEntry) cart.getEntry(2);

		assertEquals((3d * 4d) + (1.5d * 7d) + (30d * 9.99d), cart.getTotal(), DELTA);
		assertEquals((3d * 4d), memoryCartEntry1.getTotalPrice().doubleValue(), DELTA);
		assertEquals(4d, memoryCartEntry1.getBasePrice().doubleValue(), DELTA);
		assertEquals((1.5d * 7d), memoryCartEntry2.getTotalPrice().doubleValue(), DELTA);
		assertEquals(1.5d, memoryCartEntry2.getBasePrice().doubleValue(), DELTA);
		assertEquals((9.99d * 30d), memoryCartEntry3.getTotalPrice().doubleValue(), DELTA);
		assertEquals(9.99d, memoryCartEntry3.getBasePrice().doubleValue(), DELTA);

		final CartEntryModel cartEntryModel = (CartEntryModel) model.getEntries().get(0);

		cartEntryModel.setQuantity(Long.valueOf(1));

		modelService.save(cartEntryModel);

		assertFalse(cart.isCalculated().booleanValue());
		assertFalse(cart.getEntry(0).isCalculated().booleanValue());
		assertTrue(cart.getEntry(1).isCalculated().booleanValue());
		assertTrue(cart.getEntry(2).isCalculated().booleanValue());

		cartService.calculateCart(model);

		assertCalculated(cart, true);

		assertEquals((1d * 4d) + (1.5d * 7d) + (30d * 9.99d), cart.getTotal(), DELTA);
		assertEquals((1d * 4d), memoryCartEntry1.getTotalPrice().doubleValue(), DELTA);
		assertEquals(4d, memoryCartEntry1.getBasePrice().doubleValue(), DELTA);
		assertEquals((1.5d * 7d), memoryCartEntry2.getTotalPrice().doubleValue(), DELTA);
		assertEquals(1.5d, memoryCartEntry2.getBasePrice().doubleValue(), DELTA);
		assertEquals((9.99d * 30d), memoryCartEntry3.getTotalPrice().doubleValue(), DELTA);
		assertEquals(9.99d, memoryCartEntry3.getBasePrice().doubleValue(), DELTA);

	}

	protected void testModelLayer(final InMemoryCart cart) throws IllegalArgumentException, IllegalAccessException,
																  InvocationTargetException
	{
		LOG.info("testModelLayer");


		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");

		// YTODO real model for JaloOnly item !!!
		final CartModel cartModel = modelService.get(cart);

		assertNotNull(cartModel);

		checkGetters(cartModel);

		assertCartModel(modelService, cart, cartModel);

	}

	protected void assertCartModel(final ModelService modelService, final InMemoryCart cart, final CartModel cartModel)
	{
		// test ctx caching
		assertSame(cartModel, modelService.get(cart));

		assertEquals(cart.getCode(), cartModel.getCode());
		assertEquals(cart.isCalculated(), cartModel.getCalculated());
		assertEquals(cart.isNet(), cartModel.getNet());
		assertEquals(cart.getDate(), cartModel.getDate());
		assertEquals(cart.getUser(), modelService.toPersistenceLayer(cartModel.getUser()));
		assertEquals(cart.getCurrency(), modelService.toPersistenceLayer(cartModel.getCurrency()));
		assertEquals(cart.getGlobalDiscountValues(), cartModel.getGlobalDiscountValues());
		assertEquals(cart.getTotalTaxValues(), cartModel.getTotalTaxValues());

		final List<InMemoryCartEntry> cartEntries = cart.getAllEntries();
		final List<CartEntryModel> cartEntryModels = (List) cartModel.getEntries();

		assertEquals(cartEntries.size(), cartEntryModels.size());

		for (int i = 0; i < cartEntries.size(); i++)
		{
			final InMemoryCartEntry memoryCartEntry = cartEntries.get(i);
			final CartEntryModel cartEntryModel = cartEntryModels.get(i);

			assertEquals(memoryCartEntry.getEntryNumber().intValue(), cartEntryModel.getEntryNumber().intValue());
			assertEquals(memoryCartEntry.isCalculated().booleanValue(), cartEntryModel.getCalculated().booleanValue());
			assertEquals(memoryCartEntry.getProduct(), modelService.toPersistenceLayer(cartEntryModel.getProduct()));
			assertEquals(memoryCartEntry.getUnit(), modelService.toPersistenceLayer(cartEntryModel.getUnit()));
			assertEquals(memoryCartEntry.getQuantity().longValue(), cartEntryModel.getQuantity().longValue());
			assertEquals(memoryCartEntry.getBasePrice().doubleValue(), cartEntryModel.getBasePrice().doubleValue(), DELTA);
			assertEquals(memoryCartEntry.getTotalPrice().doubleValue(), cartEntryModel.getTotalPrice().doubleValue(), DELTA);
			assertEquals(memoryCartEntry.getTaxValues(), cartEntryModel.getTaxValues());
			assertEquals(memoryCartEntry.getDiscountValues(), cartEntryModel.getDiscountValues());
		}



	}

	protected void testOrderCreation(final InMemoryCart cart) throws JaloPriceFactoryException
	{
		LOG.info("testOrderCreation");

		cart.calculate();

		assertCalculated(cart, true);

		final double total = cart.getTotal();
		final double subTotal = cart.getSubtotal().doubleValue();
		final double totalTaxes = cart.getTotalTax().doubleValue();
		final double totalDiscounts = cart.getTotalDiscounts().doubleValue();

		final Order order = OrderManager.getInstance().createOrder(cart);

		assertNotNull(order);

		assertCalculated(cart, true);
		assertCalculated(order, true);

		assertEquals(cart.getCode(), order.getCode());
		assertEquals(total, order.getTotal(), DELTA);
		assertEquals(total, cart.getTotal(), DELTA);
		assertEquals(subTotal, order.getSubtotal().doubleValue(), DELTA);
		assertEquals(subTotal, cart.getSubtotal().doubleValue(), DELTA);
		assertEquals(totalTaxes, order.getTotalTax().doubleValue(), DELTA);
		assertEquals(totalTaxes, cart.getTotalTax().doubleValue(), DELTA);
		assertEquals(totalDiscounts, order.getTotalDiscounts().doubleValue(), DELTA);
		assertEquals(totalDiscounts, cart.getTotalDiscounts().doubleValue(), DELTA);
		assertEquals(cart.getCurrency(), order.getCurrency());
		assertEquals(cart.getUser(), order.getUser());
		assertEquals(cart.isNet(), order.isNet());
		assertEquals(cart.getGlobalDiscountValues(), order.getGlobalDiscountValues());
		assertEquals(cart.getTotalTaxValues(), order.getTotalTaxValues());

		final Currency curr = cart.getCurrency();

		assertEquals(curr.round(((3d * 4d) * 19) / 119) + curr.round(((1.5d * 7d) * 7) / 107), cart.getTotalTax().doubleValue(),
				DELTA);

		final Collection<TaxValue> totalTaxValues = cart.getTotalTaxValues();
		assertEquals(2, totalTaxValues.size());
		final Iterator<TaxValue> iterator = totalTaxValues.iterator();
		TaxValue tv1 = iterator.next();
		TaxValue tv2 = iterator.next();
		if ("VAT_FULL".equals(tv2.getCode()))
		{
			final TaxValue tmp = tv1;
			tv1 = tv2;
			tv2 = tmp;
		}
		assertEquals("VAT_FULL", tv1.getCode());
		assertFalse(tv1.isAbsolute());
		assertEquals(19, tv1.getValue(), DELTA);
		assertEquals(curr.round(((3d * 4d) * 19) / 119), tv1.getAppliedValue(), DELTA);

		assertEquals("VAT_HALF", tv2.getCode());
		assertFalse(tv2.isAbsolute());
		assertEquals(7, tv2.getValue(), DELTA);
		assertEquals(curr.round(((1.5d * 7d) * 7) / 107), tv2.getAppliedValue(), DELTA);

		assertEquals(cart.getAllEntries().size(), order.getAllEntries().size());

		final Iterator<InMemoryCartEntry> cartIt = cart.getAllEntries().iterator();
		final Iterator<OrderEntry> orderIt = order.getAllEntries().iterator();
		while (cartIt.hasNext() && orderIt.hasNext())
		{
			final InMemoryCartEntry cartEntry = cartIt.next();
			final OrderEntry orderEntry = orderIt.next();

			assertNotNull(cartEntry.getProduct());
			assertNotNull(orderEntry.getProduct());
			assertEquals(cartEntry.getProduct(), orderEntry.getProduct());

			assertNotNull(cartEntry.getUnit());
			assertNotNull(orderEntry.getUnit());
			assertEquals(cartEntry.getUnit(), orderEntry.getUnit());

			assertEquals(cartEntry.getQuantity(), orderEntry.getQuantity());
			assertEquals(cartEntry.getEntryNumber(), orderEntry.getEntryNumber());
			assertEquals(cartEntry.getBasePrice().doubleValue(), orderEntry.getBasePrice().doubleValue(), DELTA);
			assertEquals(cartEntry.getTotalPrice().doubleValue(), orderEntry.getTotalPrice().doubleValue(), DELTA);
			assertEquals(cartEntry.getTaxValues(), orderEntry.getTaxValues());
			assertEquals(cartEntry.getDiscountValues(), orderEntry.getDiscountValues());
		}

		assertFalse(cartIt.hasNext());
		assertFalse(orderIt.hasNext());

	}

	protected void testCalculation(final InMemoryCart cart, final Currency curr, final Unit unit) throws JaloPriceFactoryException
	{
		LOG.info("testCalculation");

		final InMemoryCartEntry memoryCartEntry1 = (InMemoryCartEntry) cart.getEntry(0);
		final InMemoryCartEntry memoryCartEntry2 = (InMemoryCartEntry) cart.getEntry(1);

		priceFactory.setBasePrice(memoryCartEntry1, new PriceValue(curr.getIsoCode(), 5, cart.isNet().booleanValue()));
		priceFactory.setTaxes(memoryCartEntry1, new TaxValue("VAT_FULL", 19, false, null));

		priceFactory.setBasePrice(memoryCartEntry2, new PriceValue(curr.getIsoCode(), 1.5, cart.isNet().booleanValue()));
		priceFactory.setTaxes(memoryCartEntry2, new TaxValue("VAT_HALF", 7, false, null));

		cart.calculate();

		assertCalculated(cart, true);

		assertEquals((3d * 5d) + (1.5d * 10d), cart.getTotal(), DELTA);
		assertEquals((3d * 5d), memoryCartEntry1.getTotalPrice().doubleValue(), DELTA);
		assertEquals(5d, memoryCartEntry1.getBasePrice().doubleValue(), DELTA);
		assertEquals((1.5d * 10d), memoryCartEntry2.getTotalPrice().doubleValue(), DELTA);
		assertEquals(1.5d, memoryCartEntry2.getBasePrice().doubleValue(), DELTA);

		assertEquals(curr.round(((3d * 5d) * 19) / 119) + curr.round(((1.5d * 10d) * 7) / 107), cart.getTotalTax().doubleValue(),
				DELTA);

		Collection<TaxValue> totalTaxValues = cart.getTotalTaxValues();
		assertEquals(2, totalTaxValues.size());
		Iterator<TaxValue> iterator = totalTaxValues.iterator();
		TaxValue tv1 = iterator.next();
		TaxValue tv2 = iterator.next();
		if ("VAT_FULL".equals(tv2.getCode()))
		{
			final TaxValue tmp = tv1;
			tv1 = tv2;
			tv2 = tmp;
		}
		assertEquals("VAT_FULL", tv1.getCode());
		assertFalse(tv1.isAbsolute());
		assertEquals(19, tv1.getValue(), DELTA);
		assertEquals(curr.round(((3d * 5d) * 19) / 119), tv1.getAppliedValue(), DELTA);

		assertEquals("VAT_HALF", tv2.getCode());
		assertFalse(tv2.isAbsolute());
		assertEquals(7, tv2.getValue(), DELTA);
		assertEquals(curr.round(((1.5d * 10d) * 7) / 107), tv2.getAppliedValue(), DELTA);

		memoryCartEntry1.setUnit(unit);

		assertEquals(unit, memoryCartEntry1.getUnit());
		assertFalse(cart.isCalculated().booleanValue());
		assertFalse(memoryCartEntry1.isCalculated().booleanValue());
		assertTrue(memoryCartEntry2.isCalculated().booleanValue());

		priceFactory.setBasePrice(memoryCartEntry1, new PriceValue(curr.getIsoCode(), 4, cart.isNet().booleanValue()));
		cart.calculate();

		assertCalculated(cart, true);

		assertEquals((3d * 4d) + (1.5d * 10d), cart.getTotal(), DELTA);
		assertEquals((3d * 4d), memoryCartEntry1.getTotalPrice().doubleValue(), DELTA);
		assertEquals(4d, memoryCartEntry1.getBasePrice().doubleValue(), DELTA);
		assertEquals((1.5d * 10d), memoryCartEntry2.getTotalPrice().doubleValue(), DELTA);
		assertEquals(1.5d, memoryCartEntry2.getBasePrice().doubleValue(), DELTA);

		assertEquals(curr.round(((3d * 4d) * 19) / 119) + curr.round(((1.5d * 10d) * 7) / 107), cart.getTotalTax().doubleValue(),
				DELTA);

		totalTaxValues = cart.getTotalTaxValues();
		assertEquals(2, totalTaxValues.size());
		iterator = totalTaxValues.iterator();
		tv1 = iterator.next();
		tv2 = iterator.next();
		if ("VAT_FULL".equals(tv2.getCode()))
		{
			final TaxValue tmp = tv1;
			tv1 = tv2;
			tv2 = tmp;
		}
		assertEquals("VAT_FULL", tv1.getCode());
		assertFalse(tv1.isAbsolute());
		assertEquals(19, tv1.getValue(), DELTA);
		assertEquals(curr.round(((3d * 4d) * 19) / 119), tv1.getAppliedValue(), DELTA);

		assertEquals("VAT_HALF", tv2.getCode());
		assertFalse(tv2.isAbsolute());
		assertEquals(7, tv2.getValue(), DELTA);
		assertEquals(curr.round(((1.5d * 10d) * 7) / 107), tv2.getAppliedValue(), DELTA);


		memoryCartEntry2.setQuantity(7);

		assertEquals(7d, memoryCartEntry2.getQuantity().doubleValue(), DELTA);
		assertFalse(cart.isCalculated().booleanValue());
		assertTrue(memoryCartEntry1.isCalculated().booleanValue());
		assertFalse(memoryCartEntry2.isCalculated().booleanValue());

		cart.calculate();

		assertCalculated(cart, true);

		assertEquals((3d * 4d) + (1.5d * 7d), cart.getTotal(), DELTA);
		assertEquals((3d * 4d), memoryCartEntry1.getTotalPrice().doubleValue(), DELTA);
		assertEquals(4d, memoryCartEntry1.getBasePrice().doubleValue(), DELTA);
		assertEquals((1.5d * 7d), memoryCartEntry2.getTotalPrice().doubleValue(), DELTA);
		assertEquals(1.5d, memoryCartEntry2.getBasePrice().doubleValue(), DELTA);

		assertEquals(curr.round(((3d * 4d) * 19) / 119) + curr.round(((1.5d * 7d) * 7) / 107), cart.getTotalTax().doubleValue(),
				DELTA);

		totalTaxValues = cart.getTotalTaxValues();
		assertEquals(2, totalTaxValues.size());
		iterator = totalTaxValues.iterator();
		tv1 = iterator.next();
		tv2 = iterator.next();
		if ("VAT_FULL".equals(tv2.getCode()))
		{
			final TaxValue tmp = tv1;
			tv1 = tv2;
			tv2 = tmp;
		}
		assertEquals("VAT_FULL", tv1.getCode());
		assertFalse(tv1.isAbsolute());
		assertEquals(19, tv1.getValue(), DELTA);
		assertEquals(curr.round(((3d * 4d) * 19) / 119), tv1.getAppliedValue(), DELTA);

		assertEquals("VAT_HALF", tv2.getCode());
		assertFalse(tv2.isAbsolute());
		assertEquals(7, tv2.getValue(), DELTA);
		assertEquals(curr.round(((1.5d * 7d) * 7) / 107), tv2.getAppliedValue(), DELTA);

	}


	protected void assertCalculated(final AbstractOrder order, final boolean calculated)
	{
		assertEquals(calculated, order.isCalculated().booleanValue());
		for (final AbstractOrderEntry e : (List<AbstractOrderEntry>) order.getAllEntries())
		{
			assertEquals(calculated, e.isCalculated().booleanValue());
		}
	}

	// check for exceptions
	protected void checkGetters(final Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		final Object[] ctx = new Object[]
				{ jaloSession.getSessionContext() };

		for (final Method m : object.getClass().getMethods())
		{
			if (m.getName().startsWith("get") || m.getName().startsWith("is"))
			{
				final Class[] params = m.getParameterTypes();
				if (params == null || params.length == 0)
				{
					m.invoke(object, (Object[]) null);
				}
				else if (params.length == 1 && SessionContext.class.getName().equals(params[0].getName()))
				{
					m.invoke(object, ctx);
				}
			}
		}
	}

}
