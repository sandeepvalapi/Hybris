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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@DemoTest
public class OrderBasicLifecycleDemoTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;

	@Resource
	private CartService cartService;

	@Resource
	private OrderService orderService;

	@Resource
	private CalculationService calculationService;

	@Resource
	private UserService userService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private CartEntryService cartEntryService;

	@Resource
	private ProductService productService;

	private CurrencyModel eur;
	private CustomerModel customer;

	private ProductModel product0;
	private ProductModel product1;

	private AddressModel customerAddress;
	private DebitPaymentInfoModel customerPaymentInfo;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/platformservices/test/orderIntegrationTestData.csv", "utf-8");

		eur = commonI18NService.getCurrency("EUR");
		customer = (CustomerModel) userService.getUserForUID("testcustomer");

		product0 = productService.getProductForCode("testProduct0");
		product1 = productService.getProductForCode("testProduct1");

		customerAddress = modelService.create(AddressModel.class);
		customerAddress.setOwner(customer);
		customerAddress.setFirstname("Der");
		customerAddress.setLastname("Buck");
		customerAddress.setTown("Muenchen");
		modelService.save(customerAddress);

		customerPaymentInfo = modelService.create(DebitPaymentInfoModel.class);
		customerPaymentInfo.setOwner(customer);
		customerPaymentInfo.setBank("MeineBank");
		customerPaymentInfo.setUser(customer);
		customerPaymentInfo.setAccountNumber("34434");
		customerPaymentInfo.setBankIDNumber("1111112");
		customerPaymentInfo.setBaOwner("Ich");
		customerPaymentInfo.setCode("testInfo");
		modelService.save(customerPaymentInfo);

		customer.setDefaultPaymentAddress(customerAddress);
		customer.setPaymentInfos(Collections.<PaymentInfoModel> singletonList(customerPaymentInfo));
		modelService.save(customer);

	}

	/**
	 * The method presents process of order placement pattern.
	 */
	@Test
	public void testOrderLifecycle() throws Exception
	{

		commonI18NService.setCurrentCurrency(eur);
		userService.setCurrentUser(customer);

		//get (create) the session cart.
		CartModel cart = cartService.getSessionCart();

		cart = cartService.getSessionCart();
		cart.setDeliveryAddress(customerAddress);
		cart.setPaymentAddress(customerAddress);
		cart.setPaymentInfo(customerPaymentInfo);

		//check cart:
		assertEquals(customer, cart.getUser());
		assertEquals(eur, cart.getCurrency());
		assertEquals(customerAddress, cart.getDeliveryAddress());
		assertEquals(customerAddress, cart.getPaymentAddress());
		assertEquals(customerPaymentInfo, cart.getPaymentInfo());

		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null);
		//setting 100E as a base price manually (when you have set the price in the price factory, you do not need to do it). 
		cartEntry0.setBasePrice(Double.valueOf(100d));

		//add new entry does not persists or calculates cart/order
		assertTrue(modelService.isNew(cartEntry0));
		assertFalse(modelService.isUpToDate(cart));

		//add a discount on entry0
		final DiscountValue discountValue0 = new DiscountValue("testDiscount0", 10d, true, eur.getIsocode());
		cartEntryService.addDiscountValue(cartEntry0, discountValue0);

		//save them and refresh. The same could be achieved by cartService.saveOrder(cart)
		modelService.saveAll(cart, cartEntry0);
		modelService.refresh(cart);
		modelService.refresh(cartEntry0);

		assertFalse(cart.getCalculated().booleanValue());
		assertFalse(cartEntry0.getCalculated().booleanValue());

		//calculateCart - only calculate, do not fetch base prices, discounts and taxes from price factory
		calculationService.calculateTotals(cart, true);
		assertTrue(cart.getCalculated().booleanValue());
		assertTrue(cartEntry0.getCalculated().booleanValue());

		assertEquals(90, cart.getTotalPrice().doubleValue(), 0.001);
		//discoun0 is not order level (global) discount.
		assertEquals(0, cart.getTotalDiscounts().doubleValue(), 0.001);
		assertEquals(90, cartEntry0.getTotalPrice().doubleValue(), 0.001);

		//before checking/removing discountValue0 from cartEntry discountValues we must get completeDiscountValue0 using cartEntryService
		//completeDiscountValue contains calculated appliedValue (discountValue0 doesn't have appliedValue set because it must be calculated)
		final DiscountValue completeDiscountValue0 = cartEntryService.getGlobalDiscountValue(cartEntry0, discountValue0);
		assertThat(cartEntry0.getDiscountValues()).containsOnly(completeDiscountValue0);

		//add another entry:
		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null);
		//set the base price
		cartEntry1.setBasePrice(Double.valueOf(25d));

		//add new entry does not persists or calculates cart/order
		assertTrue(modelService.isNew(cartEntry1));
		assertFalse(modelService.isUpToDate(cart));

		//add a discount and tax on entry1
		final DiscountValue discountValue1 = new DiscountValue("testDiscount1", 5d, true, eur.getIsocode());
		cartEntryService.addDiscountValue(cartEntry1, discountValue1);
		final TaxValue taxValue1 = new TaxValue("testTax1", 10d, true, eur.getIsocode());
		cartEntryService.addTaxValue(cartEntry1, taxValue1);

		//save them - try the second approach this time:
		cartService.saveOrder(cart);
		assertFalse(cart.getCalculated().booleanValue());
		assertFalse(cartEntry1.getCalculated().booleanValue());

		//calculate cart:
		calculationService.calculateTotals(cart, true);
		assertTrue(cart.getCalculated().booleanValue());
		assertTrue(cartEntry0.getCalculated().booleanValue());
		assertTrue(cartEntry1.getCalculated().booleanValue());

		//check prices
		assertEquals(110, cart.getTotalPrice().doubleValue(), 0.001);
		assertEquals(90, cartEntry0.getTotalPrice().doubleValue(), 0.001);
		assertEquals(20, cartEntry1.getTotalPrice().doubleValue(), 0.001);

		//before checking/removing discountValue0 from cartEntry discountValues we must get completeDiscountValue0 using cartEntryService
		//completeDiscountValue contains calculated appliedValue (discountValue0 doesn't have appliedValue set because it must be calculated)
		final DiscountValue completeDiscountValue1 = cartEntryService.getGlobalDiscountValue(cartEntry1, discountValue1);

		//check discounts
		assertThat(cartEntry0.getDiscountValues()).containsOnly(completeDiscountValue0);
		assertThat(cartEntry1.getDiscountValues()).containsOnly(completeDiscountValue1);

		//check Taxes
		assertEquals(10d, cart.getTotalTax().doubleValue(), 0.001);
		assertThat(cartEntry1.getDiscountValues()).containsOnly(completeDiscountValue1);


		//time to place actual order:
		final OrderModel order = orderService.createOrderFromCart(cart);

		//the order is not saved, not calculated
		assertTrue(modelService.isNew(order));
		modelService.save(order);

		//the cart is still there. The createOrderFromCartStrategy does not remove it
		assertTrue(cartService.hasSessionCart());
		cartService.removeSessionCart();
		assertFalse(cartService.hasSessionCart());

		//check the cloned contract address, payment info
		Assert.assertNotSame(customerAddress, order.getDeliveryAddress());
		Assert.assertNotSame(customerAddress, order.getPaymentAddress());
		Assert.assertNotSame(customerPaymentInfo, order.getPaymentInfo());

		Assert.assertEquals(order, order.getDeliveryAddress().getOwner());
		Assert.assertEquals(order, order.getPaymentAddress().getOwner());
		Assert.assertEquals(order, order.getPaymentInfo().getOwner());

		//So that you reached the end of the test, we offer a special global discount on the order : -50%
		orderService.addGlobalDiscountValue(order, new DiscountValue("testGlobalDiscount", 50, false, null));
		assertFalse(modelService.isUpToDate(order));
		modelService.save(order);
		assertFalse(order.getCalculated().booleanValue());

		calculationService.calculateTotals(order, true);
		assertTrue(order.getCalculated().booleanValue());

		//check prices
		assertEquals(55, order.getTotalPrice().doubleValue(), 0.001);
		//check taxes -- applied in the cart phase
		assertEquals(10, order.getTotalTax().doubleValue(), 0.001);
		assertEquals(55, order.getTotalDiscounts().doubleValue(), 0.001);

	}
}
