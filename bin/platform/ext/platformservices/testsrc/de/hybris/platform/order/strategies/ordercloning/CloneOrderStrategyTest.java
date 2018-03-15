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
package de.hybris.platform.order.strategies.ordercloning;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * The test demonstrates problem that is the key problem of BAM-260. Randomly, cloned order entries has different order
 * entry numbers than cart entries.
 */
@IntegrationTest
public class CloneOrderStrategyTest extends ServicelayerTransactionalTest
{

	@Resource
	private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;

	@Resource
	private CartService cartService;

	@Resource
	private UserService userService;

	@Resource
	private ProductService productService;

	@Resource
	private TypeService typeService;

	@Resource
	private OrderService orderService;

	@Resource
	private ModelService modelService;

	private ProductModel product1;
	private ProductModel product2;
	private ProductModel product3;

	private ComposedTypeModel orderType;
	private ComposedTypeModel orderEntryType;
	private CartModel cart;


	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultUsers();
		createDefaultCatalog();
		product1 = productService.getProductForCode("testProduct1");
		product2 = productService.getProductForCode("testProduct2");
		product3 = productService.getProductForCode("testProduct3");

		cart = cartService.getSessionCart();
		final UserModel user = userService.getCurrentUser();

		cartService.addToCart(cart, product1, 1, null);
		cartService.addToCart(cart, product2, 2, null);
		cartService.addToCart(cart, product3, 3, null);

		final AddressModel deliveryAddress = new AddressModel();
		deliveryAddress.setOwner(user);
		deliveryAddress.setFirstname("Der");
		deliveryAddress.setLastname("Buck");
		deliveryAddress.setTown("Muenchen");

		final DebitPaymentInfoModel paymentInfo = new DebitPaymentInfoModel();
		paymentInfo.setOwner(cart);
		paymentInfo.setBank("MeineBank");
		paymentInfo.setUser(user);
		paymentInfo.setAccountNumber("34434");
		paymentInfo.setBankIDNumber("1111112");
		paymentInfo.setBaOwner("Ich");

		orderEntryType = typeService.getComposedTypeForCode("OrderEntry");
		orderType = typeService.getComposedTypeForCode("Order");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy#clone(ComposedTypeModel, ComposedTypeModel, de.hybris.platform.core.model.order.AbstractOrderModel, String, Class, Class)}
	 * .
	 */
	@Test
	public void testClone()
	{

		Assert.assertEquals("Unexpected Cart product ", product1, cartService.getEntryForNumber(cart, 0).getProduct());
		Assert.assertEquals("Unexpected Cart product ", product2, cartService.getEntryForNumber(cart, 1).getProduct());
		Assert.assertEquals("Unexpected Cart product ", product3, cartService.getEntryForNumber(cart, 2).getProduct());

		final OrderModel order = cloneAbstractOrderStrategy.clone(orderType, orderEntryType, cart, "orderCode_" + cart.getCode(),
				OrderModel.class, OrderEntryModel.class);
		modelService.save(order);

		Assert.assertEquals("Unexpected Order product ", product1, orderService.getEntryForNumber(order, 0).getProduct());
		Assert.assertEquals("Unexpected Order product ", product2, orderService.getEntryForNumber(order, 1).getProduct());
		Assert.assertEquals("Unexpected Order product ", product3, orderService.getEntryForNumber(order, 2).getProduct());

	}
}
