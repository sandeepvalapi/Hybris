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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.Assert;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OrderServiceTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(OrderServiceTest.class);
	@Resource
	private CartService cartService;
	@Resource
	private OrderService orderService;
	@Resource
	private ProductService productService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private CommonI18NService commonI18NService;

	private OrderModel testOrder;
	private UserModel user;
	private CurrencyModel curr;
	private OrderModel unsavedOrder;

	//some shared models
	private AddressModel deliveryAddress;
	private DebitPaymentInfoModel paymentInfo;
	private ProductModel product0, product1;
	private UnitModel testUnit;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		curr = commonI18NService.getBaseCurrency();

		testUnit = modelService.create(UnitModel.class);
		testUnit.setCode("myUnit");
		testUnit.setName("myUnit");
		testUnit.setUnitType("test");

		user = userService.getCurrentUser();
		testOrder = modelService.create(OrderModel.class);

		testOrder.setCode("order calc test");
		testOrder.setUser(user);
		testOrder.setCurrency(curr);
		testOrder.setDate(new Date());
		testOrder.setNet(Boolean.FALSE);
		modelService.save(testOrder);

		//create unsaved model
		unsavedOrder = modelService.create(OrderModel.class);
		unsavedOrder.setCurrency(curr);
		unsavedOrder.setDate(new Date());
		unsavedOrder.setNet(Boolean.TRUE);
		unsavedOrder.setUser(user);

		final OrderEntryModel originalEntry = modelService.create(OrderEntryModel.class);
		originalEntry.setProduct(productService.getProductForCode("testProduct0"));
		originalEntry.setQuantity(Long.valueOf(1));
		originalEntry.setUnit(testUnit);
		originalEntry.setBasePrice(Double.valueOf(10));
		originalEntry.setEntryNumber(Integer.valueOf(0));
		originalEntry.setOrder(unsavedOrder);

		unsavedOrder.setEntries(Collections.<AbstractOrderEntryModel> singletonList(originalEntry));

		//add part-of members
		deliveryAddress = modelService.create(AddressModel.class);
		deliveryAddress.setOwner(user);
		deliveryAddress.setFirstname("Der");
		deliveryAddress.setLastname("Buck");
		deliveryAddress.setTown("Muenchen");

		paymentInfo = modelService.create(DebitPaymentInfoModel.class);
		paymentInfo.setOwner(user);
		paymentInfo.setBank("MeineBank");
		paymentInfo.setUser(user);
		paymentInfo.setAccountNumber("34434");
		paymentInfo.setBankIDNumber("1111112");
		paymentInfo.setBaOwner("Ich");
		paymentInfo.setCode("testPayment");

		unsavedOrder.setPaymentInfo(paymentInfo);
		unsavedOrder.setDeliveryAddress(deliveryAddress);

		product0 = productService.getProductForCode("testProduct0");
		product1 = productService.getProductForCode("testProduct1");
	}

	@Test
	public void testPlaceOrder() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();
		final AbstractOrderEntryModel testEntry = modelService.create(CartEntryModel.class);
		testEntry.setBasePrice(Double.valueOf(0));

		//		final CartModel cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product0, 2, null);

		final AddressModel deliveryAddress = new AddressModel();
		deliveryAddress.setOwner(user);
		deliveryAddress.setFirstname("Juergen");
		deliveryAddress.setLastname("Albertsen");
		deliveryAddress.setTown("Muenchen");

		final DebitPaymentInfoModel paymentInfo = new DebitPaymentInfoModel();
		paymentInfo.setOwner(cart);
		paymentInfo.setBank("MeineBank");
		paymentInfo.setUser(user);
		paymentInfo.setAccountNumber("34434");
		paymentInfo.setBankIDNumber("1111112");
		paymentInfo.setBaOwner("Ich");
		paymentInfo.setCode("testPayment1");

		cart.setDeliveryAddress(deliveryAddress);
		cart.setPaymentInfo(paymentInfo);

		final OrderModel order = orderService.createOrderFromCart(cart);

		final AddressModel orderDeliveryAddress = order.getDeliveryAddress();
		assertNotNull("Delivery address", orderDeliveryAddress);
		assertEquals("Firstname", "Juergen", orderDeliveryAddress.getFirstname());
		assertEquals("Lastname", "Albertsen", orderDeliveryAddress.getLastname());
		assertEquals("Town", "Muenchen", orderDeliveryAddress.getTown());

		final DebitPaymentInfoModel orderPaymentInfo = (DebitPaymentInfoModel) order.getPaymentInfo();
		assertNotNull("Payment info", orderPaymentInfo);
		assertEquals("Account Number", "34434", orderPaymentInfo.getAccountNumber());
		assertEquals("Bank", "MeineBank", orderPaymentInfo.getBank());
		assertEquals("Bank ID Number", "1111112", orderPaymentInfo.getBankIDNumber());
		assertEquals("Ba Owner", "Ich", orderPaymentInfo.getBaOwner());

	}

	@Test
	public void testPlaceOrderLazyLoadingBug() throws Exception
	{
		final CartModel cartModel = cartService.getSessionCart();
		final UserModel userModel = userService.getCurrentUser();
		cartService.addNewEntry(cartModel, product0, 2, null);

		final AddressModel addressModel = modelService.create(AddressModel.class);
		addressModel.setOwner(userModel);

		final DebitPaymentInfoModel paymentInfo = new DebitPaymentInfoModel();
		paymentInfo.setOwner(cartModel);
		paymentInfo.setBank("MeineBank");
		paymentInfo.setUser(userModel);
		paymentInfo.setAccountNumber("34434");
		paymentInfo.setBankIDNumber("1111112");
		paymentInfo.setBaOwner("Ich");

		orderService.placeOrder(cartModel, addressModel, null, paymentInfo);
	}

	@Test
	public void testCartRemoveEntriesThenOrder() throws Exception
	{
		final ProductModel product0 = productService.getProductForCode("testProduct0");
		final ProductModel product1 = productService.getProductForCode("testProduct1");
		final ProductModel product2 = productService.getProductForCode("testProduct2");
		final ProductModel product3 = productService.getProductForCode("testProduct3");
		final ProductModel product4 = productService.getProductForCode("testProduct4");

		// add product to cart       
		final CartModel cart = cartService.getSessionCart();
		cartService.addToCart(cart, product0, 1, null);
		cartService.addToCart(cart, product1, 1, null);
		cartService.addToCart(cart, product2, 1, null);
		cartService.addToCart(cart, product3, 1, null);
		cartService.addToCart(cart, product4, 1, null);


		//on the beginning is 
		// 0->product0
		// 1->product1
		// 2->product2
		// 3->product3
		// 4->product4
		assertProductsAndEntryNumbers(cart, new ProductModel[]
		{ product0, product1, product2, product3, product4 }, new int[]
		{ 0, 1, 2, 3, 4 });

		cartService.updateQuantities(cart,
				Arrays.asList(Long.valueOf(1), Long.valueOf(0), Long.valueOf(1), Long.valueOf(1), Long.valueOf(1)));
		//removed test product 1

		modelService.refresh(cart);
		// 0->product0
		// 2->product2
		// 3->product3
		// 4->product4
		assertProductsAndEntryNumbers(cart, new ProductModel[]
		{ product0, product2, product3, product4 }, new int[]
		{ 0, 2, 3, 4 });

		cartService.updateQuantities(cart, Arrays.asList(new Long[]
		{ Long.valueOf(1), Long.valueOf(0), Long.valueOf(1), Long.valueOf(1) }));
		//removed test product 2

		modelService.refresh(cart);
		// 0->product0
		// 3->product3
		// 4->product4

		assertProductsAndEntryNumbers(cart, new ProductModel[]
		{ product0, product3, product4 }, new int[]
		{ 0, 3, 4 });

		cartService.updateQuantities(cart, Arrays.asList(Long.valueOf(1), Long.valueOf(0), Long.valueOf(1)));
		//removed test product 3
		// 0->product0
		// 4->product4
		modelService.refresh(cart);

		assertProductsAndEntryNumbers(cart, new ProductModel[]
		{ product0, product4 }, new int[]
		{ 0, 4 });

		cartService.addToCart(cart, product1, 10, null);
		cartService.calculateCart(cart);
		// 0->product0
		// 4->product4
		// 5->product1
		assertProductsAndEntryNumbers(cart, new ProductModel[]
		{ product0, product4, product1 }, new int[]
		{ 0, 4, 5 });

		final DebitPaymentInfoModel paymentInfo = new DebitPaymentInfoModel();

		final UserModel user = userService.getCurrentUser();
		final AddressModel addressModel = modelService.create(AddressModel.class);
		addressModel.setOwner(user);

		paymentInfo.setOwner(cart);
		paymentInfo.setBank("MeineBank");
		paymentInfo.setUser(userService.getCurrentUser());
		paymentInfo.setAccountNumber("34434");
		paymentInfo.setBankIDNumber("1111112");
		paymentInfo.setBaOwner("Ich");

		final OrderModel order = orderService.placeOrder(cart, addressModel, null, paymentInfo);
		//after remove there are 
		// 0->product0
		// 4->product4
		assertProductsAndEntryNumbers(order, new ProductModel[]
		{ product0, product4, product1 }, new int[]
		{ 0, 4, 5 });
	}

	private void assertProductsAndEntryNumbers(final AbstractOrderModel order, final ProductModel[] products,
			final int[] entryNumbers)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		assertEquals(products.length, entries.size());
		assertEquals(entryNumbers.length, entries.size());
		for (int i = 0; i < entryNumbers.length; i++)
		{
			final AbstractOrderEntryModel entry = entries.get(i);
			assertEquals(products[i], entry.getProduct());
			assertEquals(Integer.valueOf(entryNumbers[i]), entry.getEntryNumber());
		}
	}

	@Test
	public void testDoubleAddEntry() throws Exception
	{

		final OrderEntryModel entry0 = orderService.addNewEntry(testOrder, product0, 1, null);
		final OrderEntryModel entry1 = orderService.addNewEntry(testOrder, product1, 1, null);

		orderService.saveOrder(testOrder);

		assertEquals(Integer.valueOf(0), entry0.getEntryNumber());
		assertEquals(Integer.valueOf(1), entry1.getEntryNumber());

	}

	@Test
	public void testAddNewEntry() throws Exception
	{
		// add product to cart       
		final CartModel cart = cartService.getSessionCart();
		cartService.addToCart(cart, product0, 1, null);
		paymentInfo.setOwner(cart);

		assertEquals("Returned entry is incorrect", cart.getEntries().get(0), cartService.getEntryForNumber(cart, 0));

		final OrderModel order = orderService.placeOrder(cart, deliveryAddress, null, paymentInfo);

		AbstractOrderEntryModel newOrderEntry = orderService.addNewEntry(order, product0, 1, null);

		orderService.saveOrder(order);

		assertEquals("entry is incorrect ", newOrderEntry, order.getEntries().get(0));
		assertEquals("entries size has changed ", 1, order.getEntries().size());
		assertEquals("entry number after order has changed ", 0, order.getEntries().get(0).getEntryNumber().intValue());
		assertEquals("entry qty is incorrect ", 2, order.getEntries().get(0).getQuantity().longValue());
		assertEquals("entry product is incorrect ", product0, order.getEntries().get(0).getProduct());
		assertEquals("wrong entry returned", newOrderEntry, orderService.getEntryForNumber(order, 0));

		newOrderEntry = orderService.addNewEntry(order, product1, 5, null);
		assertTrue(modelService.isNew(newOrderEntry));

		orderService.saveOrder(order);

		assertEquals("entries size has changed ", 2, order.getEntries().size());
		assertEquals("entry number after order has changed ", 0, order.getEntries().get(0).getEntryNumber().intValue());
		assertEquals("entry qty is incorrect ", 2, order.getEntries().get(0).getQuantity().longValue());
		assertEquals("entry product is incorrect ", product0, order.getEntries().get(0).getProduct());


		assertEquals("entry is incorrect ", newOrderEntry, order.getEntries().get(1));
		assertEquals("entry number after order has changed ", 1, order.getEntries().get(1).getEntryNumber().intValue());
		assertEquals("entry qty is incorrect ", 5, order.getEntries().get(1).getQuantity().longValue());
		assertEquals("entry product is incorrect ", product1, order.getEntries().get(1).getProduct());

		assertEquals("wrong entry number returned", newOrderEntry, orderService.getEntryForNumber(order, 1));

		//corner cases
		//null order
		assertAddNewEntryCornerCase(null, product0, 1, null, 0, false, IllegalArgumentException.class,
				"Illegal Argument exception expected for null order");
		//null product
		assertAddNewEntryCornerCase(order, null, 1, null, 0, false, IllegalArgumentException.class,
				"Illegal Argument exception expected for null product");
		//zero qty
		assertAddNewEntryCornerCase(order, product0, 0, null, 0, false, IllegalArgumentException.class,
				"Illegal Argument exception expected for zero quantity");
		//invalid entry number
		assertAddNewEntryCornerCase(order, product0, 1, null, -2, true, IllegalArgumentException.class,
				"Illegal Argument exception expected for entry number less than -1");

		assertAddNewEntryCornerCase(order, product0, 1, null, 0, true, AmbiguousIdentifierException.class,
				"Entry already present at position 1");


		boolean success = false;
		try
		{
			orderService.addNewEntry(null, order, product0, 1, null, 0, true);
			fail("Illegal Argument exception expected");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		assertTrue("Illegal Argument exception expected for null type object", success);
	}


	@Test
	public void testFindEntryByNumber() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		final ProductModel product0 = productService.getProductForCode("testProduct0");
		final ProductModel product1 = productService.getProductForCode("testProduct1");
		final ProductModel product2 = productService.getProductForCode("testProduct2");

		//TODO fix that
		cartService.addNewEntry(cart, product0, 1, null);

		cartService.saveOrder(cart);

		cartService.addNewEntry(cart, product1, 1, null);

		cartService.saveOrder(cart);

		final AbstractOrderEntryModel entry0 = cartService.getEntryForNumber(cart, 0);
		assertTrue("Entry has wrong type", entry0 instanceof CartEntryModel);
		assertEquals("Entry has wrong product", product0, entry0.getProduct());
		assertEquals("Entry has wrong number", 0, entry0.getEntryNumber().longValue());
		assertEquals("Entry has wrong order", cart, entry0.getOrder());

		final AbstractOrderEntryModel entry1 = cartService.getEntryForNumber(cart, 1);
		assertTrue("Entry has wrong type", entry1 instanceof CartEntryModel);
		assertEquals("Entry has wrong product", product1, entry1.getProduct());
		assertEquals("Entry has wrong number", 1, entry1.getEntryNumber().longValue());
		assertEquals("Entry has wrong order", cart, entry1.getOrder());

		//add at pos 0
		final CartEntryModel newCartEntry = cartService.addNewEntry(cart, product2, 2, null, 0, true);
		assertTrue(modelService.isNew(newCartEntry));

		//need to save it in a special routine
		cartService.saveOrder(cart);

		final AbstractOrderEntryModel entry2 = cartService.getEntryForNumber(cart, 0);
		assertTrue("Entry has wrong type", entry2 instanceof CartEntryModel);
		assertEquals("Entry has wrong product", product2, entry2.getProduct());
		assertEquals("Entry has wrong number", 0, entry2.getEntryNumber().longValue());
		assertEquals("Entry has wrong order", cart, entry2.getOrder());
		assertEquals("Entry has wrong qty", 2, entry2.getQuantity().longValue());

		final AbstractOrderEntryModel movedEntry = cartService.getEntryForNumber(cart, 1);
		assertEquals("Entry has wrong product", product0, movedEntry.getProduct());
		assertEquals("Entry has wrong number", 1, movedEntry.getEntryNumber().longValue());
		assertEquals("Entry has wrong order", cart, movedEntry.getOrder());

		//corner cases
		assertGetEntryForNumberCornerCase(null, 1, IllegalArgumentException.class,
				"Illegal Argument exception expected for null order object");
		assertGetEntryForNumberCornerCase(cart, -2, IllegalArgumentException.class,
				"Illegal Argument exception expected for negative entry number");
		assertGetEntryForNumberCornerCase(cart, 3, UnknownIdentifierException.class,
				"Illegal Argument exception expected for entry out of range");
	}

	@Test
	public void testFindEntriesByProduct() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		final ProductModel product0 = productService.getProductForCode("testProduct0");
		final ProductModel product1 = productService.getProductForCode("testProduct1");
		final ProductModel product2 = productService.getProductForCode("testProduct2");
		final ProductModel product3 = productService.getProductForCode("testProduct3");

		cartService.addToCart(cart, product1, 1, null);
		cartService.addToCart(cart, product2, 1, null);

		boolean success = false;
		try
		{
			orderService.getEntriesForProduct(null, product1);
			fail("Illegal Argument exception expected");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		assertTrue("Illegal Argument exception expected for order = null", success);

		success = false;

		try
		{
			cartService.getEntriesForProduct(cart, null);
			fail("Illegal Argument exception expected");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		assertTrue("Illegal Argument exception expected for product = null", success);


		assertTrue("Empty collection expected", cartService.getEntriesForProduct(cart, product0).isEmpty());



		final AbstractOrderEntryModel entry0 = cartService.getEntryForNumber(cart, 0);
		assertTrue("Entry has wrong type", entry0 instanceof CartEntryModel);
		assertEquals("Entry has wrong product", product1, entry0.getProduct());
		assertEquals("Entry has wrong number", 0, entry0.getEntryNumber().longValue());
		assertEquals("Entry has wrong order", cart, entry0.getOrder());

		final AbstractOrderEntryModel entry1 = cartService.getEntryForNumber(cart, 1);
		assertTrue("Entry has wrong type", entry1 instanceof CartEntryModel);
		assertEquals("Entry has wrong product", product2, entry1.getProduct());
		assertEquals("Entry has wrong number", 1, entry1.getEntryNumber().longValue());
		assertEquals("Entry has wrong order", cart, entry1.getOrder());

		cartService.addToCart(cart, product3, 2, null);
		final AbstractOrderEntryModel entry2 = cartService.getEntryForNumber(cart, 2);

		final List<CartEntryModel> entries01 = cartService.getEntriesForNumber(cart, 0, 1);
		Assert.assertCollectionElements(entries01, entry0, entry1);
		final List<CartEntryModel> entries02 = cartService.getEntriesForNumber(cart, 0, 2);
		Assert.assertCollectionElements(entries02, entry0, entry1, entry2);
		final List<CartEntryModel> entries12 = cartService.getEntriesForNumber(cart, 1, 2);
		Assert.assertCollectionElements(entries12, entry1, entry2);

		final List<CartEntryModel> entries112 = cartService.getEntriesForNumber(cart, 1, 12);
		Assert.assertCollection("Collections should have the same elements", entries12, entries112);

		//corner cases
		assertGetEntriesForNumberCornerCase(cart, 3, 4, UnknownIdentifierException.class,
				"UnknownIdentifierException expected for range 3-4");
		assertGetEntriesForNumberCornerCase(cart, -1, 8, IllegalArgumentException.class,
				"Illegal Argument exception expected for negative start index");
		assertGetEntriesForNumberCornerCase(cart, 4, 1, IllegalArgumentException.class,
				"Illegal Argument exception expected for misconstructed entries range");
	}

	@Test
	public void testAddRemoveGlobalDiscountValue()
	{
		final DiscountValue testDiscount = new DiscountValue("testDiscount", 5, true, testOrder.getCurrency().getIsocode());
		final DiscountValue testDiscount2 = new DiscountValue("testDiscount2", 10, false, null);

		assertTrue("Order global discounts should be empty", testOrder.getGlobalDiscountValues().isEmpty());

		//not there yet.. should do nothing
		orderService.removeGlobalDiscountValue(testOrder, testDiscount);
		orderService.addGlobalDiscountValue(testOrder, testDiscount);

		assertThat(testOrder.getGlobalDiscountValues()).hasSize(1).contains(testDiscount);

		orderService.addGlobalDiscountValue(testOrder, testDiscount2);
		Assert.assertCollectionElements(testOrder.getGlobalDiscountValues(), testDiscount, testDiscount2);

		orderService.removeGlobalDiscountValue(testOrder, testDiscount);
		Assert.assertCollectionElements(testOrder.getGlobalDiscountValues(), testDiscount2);

		orderService.removeGlobalDiscountValue(testOrder, testDiscount2);
		assertTrue("Order global discounts should be empty", testOrder.getGlobalDiscountValues().isEmpty());

		orderService.removeGlobalDiscountValue(testOrder, testDiscount2);

		//corner cases for adding
		assertAddOrRemoveGlobalDiscountValueCornerCase(true, null, testDiscount, IllegalArgumentException.class,
				"IllegalArgumentException expected for null order");
		assertAddOrRemoveGlobalDiscountValueCornerCase(true, testOrder, null, IllegalArgumentException.class,
				"IllegalArgumentException expected for null discount");

		//corner cases for removal
		assertAddOrRemoveGlobalDiscountValueCornerCase(false, null, testDiscount, IllegalArgumentException.class,
				"IllegalArgumentException expected for null order");
		assertAddOrRemoveGlobalDiscountValueCornerCase(false, testOrder, null, IllegalArgumentException.class,
				"IllegalArgumentException expected for null discount");

		//Now I check new order model which is not persisted.
		orderService.addGlobalDiscountValue(unsavedOrder, testDiscount);
		assertTrue(modelService.isNew(unsavedOrder));
	}


	@Test
	public void testAddRemoveTotalTaxValue()
	{

		final TaxValue testTax = new TaxValue("testTax", 5, true, testOrder.getCurrency().getIsocode());
		final TaxValue testTax2 = new TaxValue("testTax2", 10, false, null);


		assertTrue("Order total taxes should be null", testOrder.getTotalTaxValues().isEmpty());

		//not there.. should do nothing
		orderService.removeTotalTaxValue(testOrder, testTax);

		orderService.addTotalTaxValue(testOrder, testTax);
		assertThat(testOrder.getTotalTaxValues()).hasSize(1).contains(testTax);

		orderService.addTotalTaxValue(testOrder, testTax2);
		assertThat(testOrder.getTotalTaxValues()).hasSize(2).contains(testTax, testTax2);

		orderService.removeTotalTaxValue(testOrder, testTax);
		assertThat(testOrder.getTotalTaxValues()).hasSize(1).contains(testTax2);

		orderService.removeTotalTaxValue(testOrder, testTax2);
		assertTrue("Order total taxes should be null", testOrder.getTotalTaxValues().isEmpty());

		//corner cases for adding
		assertAddOrRemoveTaxValueCornerCase(true, null, testTax, IllegalArgumentException.class,
				"IllegalArgumentException expected for null order");
		assertAddOrRemoveTaxValueCornerCase(true, testOrder, null, IllegalArgumentException.class,
				"IllegalArgumentException expected for null tax");

		//corner cases for adding removing
		assertAddOrRemoveTaxValueCornerCase(false, null, testTax, IllegalArgumentException.class,
				"IllegalArgumentException expected for null order");
		assertAddOrRemoveTaxValueCornerCase(false, testOrder, null, IllegalArgumentException.class,
				"IllegalArgumentException expected for null tax");

		//Now I check new order model which is not persisted.
		orderService.addTotalTaxValue(unsavedOrder, testTax2);
		assertTrue(modelService.isNew(unsavedOrder));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateOrderForNullOrder()
	{
		orderService.clone(null, null, null, "clone_Code");
	}


	@Test
	public void testCreateOrder()
	{
		final OrderModel orderClone = orderService.clone(null, null, unsavedOrder, "clone_code");
		assertTrue(modelService.isNew(orderClone));
		assertOrderClone(orderClone, unsavedOrder, "clone_code");
	}

	@Test
	public void testAddAllGlobalDiscountValuesCornerCases()
	{
		final DiscountValue testDiscount = new DiscountValue("testDiscount", 5, true, testOrder.getCurrency().getIsocode());

		assertAddGlobalDiscountsCornerCase(null, Collections.singletonList(testDiscount), IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null order");

		assertAddGlobalDiscountsCornerCase(unsavedOrder, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null discounts list");
	}

	@Test
	public void testAddAllGlobalDiscountValues()
	{
		final DiscountValue testDiscount1 = new DiscountValue("testDiscount1", 5, true, testOrder.getCurrency().getIsocode());
		final DiscountValue testDiscount2 = new DiscountValue("testDiscount2", 15, false, null);

		//for unsaved order
		orderService.addAllGlobalDiscountValues(unsavedOrder, Collections.singletonList(testDiscount1));
		assertThat(unsavedOrder.getGlobalDiscountValues()).hasSize(1).containsOnly(testDiscount1);
		orderService.addAllGlobalDiscountValues(unsavedOrder, Collections.singletonList(testDiscount2));
		assertThat(unsavedOrder.getGlobalDiscountValues()).hasSize(2).containsOnly(testDiscount1, testDiscount2);
		orderService.addAllGlobalDiscountValues(unsavedOrder, Collections.EMPTY_LIST);
		assertThat(unsavedOrder.getGlobalDiscountValues()).hasSize(2).containsOnly(testDiscount1, testDiscount2);
		assertTrue(modelService.isNew(unsavedOrder));

		//for placed order
		orderService.addAllGlobalDiscountValues(testOrder, Collections.singletonList(testDiscount2));
		assertThat(testOrder.getGlobalDiscountValues()).hasSize(1).containsOnly(testDiscount2);
		orderService.addAllGlobalDiscountValues(testOrder, Collections.singletonList(testDiscount1));
		assertThat(testOrder.getGlobalDiscountValues()).hasSize(2).containsOnly(testDiscount1, testDiscount2);
		assertFalse(modelService.isUpToDate(testOrder));
	}

	@Test
	public void testAddAllTotalTaxValuesCornerCases()
	{
		final TaxValue testTax = new TaxValue("testTax", 5, true, testOrder.getCurrency().getIsocode());

		assertAddAllTotalTaxCornerCase(null, Collections.singletonList(testTax), IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null order");

		assertAddAllTotalTaxCornerCase(unsavedOrder, null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null tax list");

	}

	@Test
	public void testAddAllTotalTaxValues()
	{
		final TaxValue testTax1 = new TaxValue("testTax1", 5, true, testOrder.getCurrency().getIsocode());
		final TaxValue testTax2 = new TaxValue("testTax2", 15, false, null);

		//for unsaved order
		orderService.addAllTotalTaxValues(unsavedOrder, Collections.singletonList(testTax1));
		assertThat(unsavedOrder.getTotalTaxValues()).hasSize(1).containsOnly(testTax1);
		orderService.addAllTotalTaxValues(unsavedOrder, Collections.singletonList(testTax2));
		assertThat(unsavedOrder.getTotalTaxValues()).hasSize(2).containsOnly(testTax1, testTax2);
		orderService.addAllTotalTaxValues(unsavedOrder, Collections.EMPTY_LIST);
		assertThat(unsavedOrder.getTotalTaxValues()).hasSize(2).containsOnly(testTax1, testTax2);
		assertTrue(modelService.isNew(unsavedOrder));

		//for placed order
		orderService.addAllTotalTaxValues(testOrder, Collections.singletonList(testTax2));
		assertThat(testOrder.getTotalTaxValues()).hasSize(1).containsOnly(testTax2);
		orderService.addAllTotalTaxValues(testOrder, Collections.singletonList(testTax1));
		assertThat(testOrder.getTotalTaxValues()).hasSize(2).containsOnly(testTax1, testTax2);
		assertFalse(modelService.isUpToDate(testOrder));
	}

	private void assertAddGlobalDiscountsCornerCase(final OrderModel order, final List<DiscountValue> globalDiscounts,
			final Class expected, final String msg)
	{
		boolean success = false;
		try
		{
			orderService.addAllGlobalDiscountValues(order, globalDiscounts);
			fail(msg);
		}
		catch (final Exception e)
		{
			success = expected.isInstance(e);
		}
		assertTrue(msg, success);
	}

	private void assertAddAllTotalTaxCornerCase(final OrderModel order, final List<TaxValue> taxes, final Class expected,
			final String msg)
	{
		boolean success = false;
		try
		{
			orderService.addAllTotalTaxValues(order, taxes);
			fail(msg);
		}
		catch (final Exception e)
		{
			success = expected.isInstance(e);
		}
		assertTrue(msg, success);
	}

	private void assertAddNewEntryCornerCase(final OrderModel order, final ProductModel product, final long qty,
			final UnitModel unit, final int requiredPos, final boolean addToPresent, final Class expectedException, final String msg)
	{
		boolean success = false;
		try
		{
			if (addToPresent)
			{
				orderService.addNewEntry(order, product, qty, null, requiredPos, addToPresent);
			}
			else
			{
				orderService.addNewEntry(order, product, qty, null);
			}
			fail(msg);
		}
		catch (final Exception e)
		{
			success = expectedException.isInstance(e);
		}
		assertTrue(msg, success);
	}

	private void assertGetEntryForNumberCornerCase(final AbstractOrderModel order, final int number, final Class expected,
			final String msg)
	{
		boolean success = false;
		try
		{
			cartService.getEntryForNumber((CartModel) order, number);
			fail(msg);
		}
		catch (final Exception e)
		{
			success = expected.isInstance(e);
		}
		assertTrue(msg, success);
	}

	private void assertAddOrRemoveGlobalDiscountValueCornerCase(final boolean add, final OrderModel order,
			final DiscountValue discount, final Class expected, final String msg)
	{
		boolean success = false;
		try
		{
			if (add)
			{
				orderService.addGlobalDiscountValue(order, discount);
			}
			else
			{
				orderService.removeGlobalDiscountValue(order, discount);
			}
			fail(msg);
		}
		catch (final Exception e)
		{
			success = expected.isInstance(e);
		}
		assertTrue(msg, success);
	}

	private void assertAddOrRemoveTaxValueCornerCase(final boolean add, final OrderModel order, final TaxValue taxValue,
			final Class expected, final String msg)
	{
		boolean success = false;
		try
		{
			if (add)
			{
				orderService.addTotalTaxValue(order, taxValue);
			}
			else
			{
				orderService.removeTotalTaxValue(order, taxValue);
			}
			fail(msg);
		}
		catch (final Exception e)
		{
			success = expected.isInstance(e);
		}
		assertTrue(msg, success);
	}

	private void assertGetEntriesForNumberCornerCase(final AbstractOrderModel cart, final int start, final int end,
			final Class expectedException, final String msg)
	{
		boolean success = false;
		try
		{
			cartService.getEntriesForNumber((CartModel) cart, start, end);
			fail(msg);
		}
		catch (final Exception e)
		{
			success = expectedException.isInstance(e);
		}
		assertTrue(msg, success);

	}

	private void assertOrderClone(final AbstractOrderModel cloneOrder, final AbstractOrderModel originalOrder,
			final String cloneCode)
	{
		assertNotNull(cloneOrder);
		assertEquals(cloneCode, cloneOrder.getCode());
		final Set<IgnoredAttribute> ignoredAttributes = new HashSet<IgnoredAttribute>();
		ignoredAttributes.add(new IgnoredAttribute(OrderModel._TYPECODE, OrderModel.CODE));
		ignoredAttributes.add(new IgnoredAttribute(OrderModel._TYPECODE, OrderModel.CREATIONTIME));
		ignoredAttributes.add(new IgnoredAttribute(OrderModel._TYPECODE, OrderModel.MODIFIEDTIME));
		final ClonedModelsAssertionContext context = new ClonedModelsAssertionContext(new HashSet<ItemModel>(), ignoredAttributes);
		assertClonedModel(originalOrder, cloneOrder, context);
	}

	private void assertClonedModel(final ItemModel original, final ItemModel clone,
			final ClonedModelsAssertionContext assertionContext)
	{
		if (assertionContext.modelAlreadyChecked(original))
		{
			return;
		}
		assertionContext.addCheckedModel(original);
		final ComposedTypeModel originalType = typeService.getComposedTypeForClass(original.getClass());
		final ComposedTypeModel cloneType = typeService.getComposedTypeForClass(clone.getClass());
		assertEquals("cloned and original models have different types", originalType, cloneType);
		for (final AttributeDescriptorModel originalAttributeDescriptor : typeService.getAttributeDescriptorsForType(originalType))
		{
			try
			{
				final String qualifier = originalAttributeDescriptor.getQualifier();
				if (assertionContext.ignoreAttribute(originalType.getCode(), qualifier))
				{
					continue;
				}
				final Object originalValue = modelService.getAttributeValue(original, qualifier);
				final Object clonedValue = modelService.getAttributeValue(clone, qualifier);
				//if its just null
				if (originalValue == null)
				{
					assertNull(clonedValue);
				}
				//if it is a non empty collection
				else if (originalAttributeDescriptor.getAttributeType() instanceof CollectionTypeModel)
				{
					if (((Collection) originalValue).isEmpty())
					{
						continue;
					}
					final List originalList = new ArrayList((Collection) originalValue);
					final List clonedList = new ArrayList((Collection) clonedValue);
					assertEquals("Collection of original and cloned [" + qualifier + "] have different sizes", originalList.size(),
							clonedList.size());
					for (int i = 0; i < originalList.size(); i++)
					{
						final Object originalListEntry = originalList.get(i);
						final Object clonedListEntry = clonedList.get(i);
						if (ItemModel.class.isAssignableFrom(originalListEntry.getClass()))
						{
							assertTrue(ItemModel.class.isAssignableFrom(clonedListEntry.getClass()));
							assertClonedModel((ItemModel) originalListEntry, (ItemModel) clonedListEntry, assertionContext);
						}
						else
						{
							assertEquals("Unexpected cloned collection entry ", originalListEntry, clonedListEntry);
						}
					}
				}
				//if a model
				else if (ItemModel.class.isAssignableFrom(originalValue.getClass()))
				{
					assertTrue(ItemModel.class.isAssignableFrom(clonedValue.getClass()));
					if (!originalValue.equals(clonedValue))
					{
						assertClonedModel((ItemModel) originalValue, (ItemModel) clonedValue, assertionContext);
					}

				}// simple type
				else
				{
					assertEquals("Unexpected cloned order attribute value", originalValue, clonedValue);
				}

			}
			catch (final AttributeNotSupportedException e)
			{
				LOG.info("Parameter skipped :" + e.getQualifier());
				continue;
			}
		}
	}

	private class ClonedModelsAssertionContext
	{
		private final Set<ItemModel> checkedModels;
		private final Set<IgnoredAttribute> ignoredAttributes;

		public ClonedModelsAssertionContext(final Set<ItemModel> checkedModels, final Set<IgnoredAttribute> ignoredAttributes)
		{
			super();
			this.checkedModels = checkedModels;
			this.ignoredAttributes = ignoredAttributes;
		}

		public Set<IgnoredAttribute> getIgnoredAttributes()
		{
			return ignoredAttributes;
		}

		boolean modelAlreadyChecked(final ItemModel model)
		{
			return checkedModels.contains(model);
		}

		boolean addCheckedModel(final ItemModel model)
		{
			return checkedModels.add(model);
		}

		boolean ignoreAttribute(final String type, final String qualifier)
		{
			return getIgnoredAttributes().contains(new IgnoredAttribute(type, qualifier));
		}

	}

	private class IgnoredAttribute
	{
		private final String typeCode;
		private final String ignoredQualifier;

		public IgnoredAttribute(final String typeCode, final String ignoredQualifier)
		{
			super();
			this.typeCode = typeCode;
			this.ignoredQualifier = ignoredQualifier;
		}


		public String getTypeCode()
		{
			return this.typeCode;
		}


		public String getIgnoredQualifier()
		{
			return ignoredQualifier;
		}


		@Override
		public boolean equals(final Object obj)
		{
			if (obj instanceof IgnoredAttribute)
			{
				final IgnoredAttribute ignored = (IgnoredAttribute) obj;
				return this.typeCode.equalsIgnoreCase(ignored.getTypeCode())
						&& this.ignoredQualifier.equalsIgnoreCase(ignored.getIgnoredQualifier());
			}
			return false;
		}

		@Override
		public int hashCode()
		{
			return this.typeCode.hashCode() + this.ignoredQualifier.hashCode();
		}

	}

}
