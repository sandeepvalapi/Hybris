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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OrderInterceptorsIntegrationTest extends ServicelayerTransactionalTest
{

	private static final Logger LOG = Logger.getLogger(OrderInterceptorsIntegrationTest.class);
	private static final String CUSTOMR_UID = "testcustomer";
	private static final String PRODUCT0_CODE = "testProduct0";
	private static final String PRODUCT1_CODE = "testProduct1";
	private static final String PRODUCT2_CODE = "testProduct2";
	private static final String PRODUCT3_CODE = "testProduct3";

	@Resource
	private UserService userService;
	@Resource
	private OrderService orderService;
	@Resource
	private CartService cartService;
	@Resource
	private ProductService productService;
	@Resource
	private ModelService modelService;
	@Resource
	private CalculationService calculationService;

	private CustomerModel customer;
	private ProductModel product0;
	private ProductModel product1;
	private ProductModel product2;
	private ProductModel product3;

	private CartModel cart;

	private DebitPaymentInfoModel paymentInfo;
	private AddressModel deliveryAddress;
	private AddressModel paymentAddress;


	@Before
	public void setUp() throws Exception
	{
		LOG.info("Creating order integration test data ..");
		userService.setCurrentUser(userService.getAdminUser());
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		importCsv("/platformservices/test/orderIntegrationTestData.csv", "utf-8");
		LOG.info("Finished creating order test data " + (System.currentTimeMillis() - startTime) + "ms");

		customer = (CustomerModel) userService.getUserForUID(CUSTOMR_UID);
		product0 = productService.getProductForCode(PRODUCT0_CODE);
		product1 = productService.getProductForCode(PRODUCT1_CODE);
		product2 = productService.getProductForCode(PRODUCT2_CODE);
		product3 = productService.getProductForCode(PRODUCT3_CODE);

		cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product0, 1, null);
		cartService.addNewEntry(cart, product1, 1, null);
		cartService.addNewEntry(cart, product2, 1, null);
		modelService.save(cart);

		paymentInfo = modelService.create(DebitPaymentInfoModel.class);
		paymentInfo.setOwner(cart);
		paymentInfo.setBank("MeineBank");
		paymentInfo.setUser(customer);
		paymentInfo.setAccountNumber("34434");
		paymentInfo.setBankIDNumber("1111112");
		paymentInfo.setBaOwner("Ich");
		paymentInfo.setCode("testPayment");

		deliveryAddress = modelService.create(AddressModel.class);
		deliveryAddress.setOwner(customer);
		deliveryAddress.setFirstname("AAA");
		deliveryAddress.setLastname("BBB");
		deliveryAddress.setTown("Chicago");
		deliveryAddress.setStreetname("BBB");
		deliveryAddress.setPostalcode("00-000");

		paymentAddress = modelService.create(AddressModel.class);
		paymentAddress.setOwner(customer);
		paymentAddress.setFirstname("ZZZ");
		paymentAddress.setLastname("XXX");
		paymentAddress.setTown("New York");
		paymentAddress.setStreetname("1st Avenue");
		paymentAddress.setPostalcode("00-000");

		cart.setDeliveryAddress(deliveryAddress);
		cart.setPaymentAddress(paymentAddress);
		cart.setPaymentInfo(paymentInfo);

		modelService.saveAll(deliveryAddress, paymentAddress, paymentInfo);
	}



	/**
	 * With all the new interceptors for
	 */
	@Test
	public void testOrderLifeCycle() throws InvalidCartException
	{
		try
		{
			final OrderModel order = orderService.createOrderFromCart(cart);



			order.setStatus(OrderStatus.CREATED);
			modelService.save(order);

			orderService.addNewEntry(order, product0, 1, null);

			calculationService.calculate(order);

			final AbstractOrderEntryModel entry = orderService.getEntryForNumber(order, 0);
			Assert.assertTrue(order.getCalculated().booleanValue());
			Assert.assertTrue(entry.getCalculated().booleanValue());

			//change calculation sensitive data in entry model:
			entry.setProduct(product1);
			modelService.save(entry);

			Assert.assertFalse("Order should not be calculated", order.getCalculated().booleanValue());
			Assert.assertFalse("Order entry should not be calculated", entry.getCalculated().booleanValue());

			orderService.calculateOrder(order);

			order.setStatus(OrderStatus.COMPLETED);
			modelService.save(order);

			int size = order.getEntries().size();
			orderService.addNewEntry(order, product3, 1, null);

			assertEquals(++size, order.getEntries().size());

			//that should result adding new order Entry
			orderService.addNewEntry(order, product1, 1, null, -1, false);


			assertEquals(++size, order.getEntries().size());

			modelService.refresh(order);

			modelService.remove(order);

		}
		catch (final Exception e)
		{
			fail("No exception expected, but was : " + e.getClass() + " -> " + e.getMessage());
		}

	}

	@Test
	public void testOrderCalculationStatusOnDiscountFlagsChange() throws Exception
	{
		final OrderModel order = orderService.placeOrder(cart, deliveryAddress, deliveryAddress, paymentInfo);
		try
		{
			Assert.assertTrue("Order should be calculated", order.getCalculated().booleanValue());

			Assert.assertFalse(order.isDiscountsIncludeDeliveryCost());
			order.setDiscountsIncludeDeliveryCost(true);

			modelService.save(order);
			modelService.refresh(order);
			Assert.assertFalse("Order should not be calculated", order.getCalculated().booleanValue());
			Assert.assertTrue(order.isDiscountsIncludeDeliveryCost());
			assertEntriesCalculatedStatus(order, true);

			orderService.calculateOrder(order);
			Assert.assertTrue("Order should be calculated", order.getCalculated().booleanValue());
			Assert.assertFalse(order.isDiscountsIncludePaymentCost());
			order.setDiscountsIncludePaymentCost(true);

			modelService.save(order);
			modelService.refresh(order);

			Assert.assertFalse("Order should not be calculated", order.getCalculated().booleanValue());
			Assert.assertTrue(order.isDiscountsIncludePaymentCost());
		}
		catch (final Exception e)
		{
			fail("Failed due to :" + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			modelService.remove(order);
		}
	}

	@Test
	public void testOrderCalculationOnOrderDateChange() throws Exception
	{
		final OrderModel order = orderService.placeOrder(cart, deliveryAddress, deliveryAddress, paymentInfo);
		try
		{
			Assert.assertTrue("Order should be calculated", order.getCalculated().booleanValue());
			assertEntriesCalculatedStatus(order, true);
			order.setDate(new Date());
			modelService.save(order);
			modelService.refresh(order);

			Assert.assertFalse("Order should not be calculated", order.getCalculated().booleanValue());
			assertEntriesCalculatedStatus(order, false);

		}
		catch (final Exception e)
		{
			fail("Failed due to :" + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			modelService.remove(order);
		}
	}

	@Test
	public void testContractMembersCloning() throws Exception
	{
		final OrderModel order = orderService.placeOrder(cart, deliveryAddress, paymentAddress, paymentInfo);
		//this is still needed as long as place order strategy is jalo-based.
		order.setPaymentAddress(paymentAddress);
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentInfo(paymentInfo);
		modelService.save(order);//<-- trigger interceptors here

		//check proper member cloning after place order handling.
		Assert.assertEquals("Payment address should be order owned clone", order, order.getPaymentAddress().getOwner());
		Assert.assertTrue("Payment address should be duplicated", order.getPaymentAddress().getDuplicate().booleanValue());
		Assert.assertEquals("Delivery address should be order owned clone", order, order.getDeliveryAddress().getOwner());
		Assert.assertTrue("Delivery address should be duplicated", order.getDeliveryAddress().getDuplicate().booleanValue());
		Assert.assertEquals("Payment info should be order owned clone", order, order.getPaymentInfo().getOwner());
		Assert.assertTrue("Payment info should be duplicated", order.getPaymentInfo().getDuplicate().booleanValue());

		order.setPaymentAddress(paymentAddress);
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentInfo(paymentInfo);

		//here DefaultOrderPrepareInterceptor should be called
		modelService.save(order);

		Assert.assertNotSame("PaymentAddress should be a cloned one", paymentAddress, order.getPaymentAddress());
		Assert.assertNotSame("DeliveryAddress should be a cloned one", deliveryAddress, order.getDeliveryAddress());
		Assert.assertNotSame("PaymentInfo should be a cloned one", paymentInfo, order.getPaymentInfo());

		//not the same items
		Assert.assertFalse("PaymentAddress shold be cloned one", paymentAddress.equals(order.getPaymentAddress()));
		Assert.assertFalse("DeliveryAddress shold be cloned one", deliveryAddress.equals(order.getDeliveryAddress()));
		Assert.assertFalse("PaymentInfo shold be cloned one", paymentInfo.equals(order.getPaymentInfo()));


		//check clones ownership
		Assert.assertEquals("Incorrect clone's owner", order, order.getPaymentAddress().getOwner());
		Assert.assertEquals("Incorrect clone's owner", order, order.getDeliveryAddress().getOwner());
		Assert.assertEquals("Incorrect clone's owner", order, order.getPaymentInfo().getOwner());

		//check duplicate flag
		Assert.assertTrue("Clone should be a dupliacate", order.getPaymentAddress().getDuplicate().booleanValue());
		Assert.assertTrue("Incorrect clone's owner", order.getDeliveryAddress().getDuplicate().booleanValue());
		Assert.assertTrue("Incorrect clone's owner", order.getPaymentInfo().getDuplicate().booleanValue());

		//now .. nothing shall change as neither addresses nor paymentInfo is not changed:

		final AddressModel clonePaymentAddress = order.getPaymentAddress();
		final AddressModel cloneDeliveryAddress = order.getDeliveryAddress();
		final PaymentInfoModel clonePaymentInfo = order.getPaymentInfo();

		order.setStatus(OrderStatus.CREATED);
		modelService.save(order);

		Assert.assertEquals("Incorrect order member", clonePaymentAddress, order.getPaymentAddress());
		Assert.assertEquals("Incorrect order member", cloneDeliveryAddress, order.getDeliveryAddress());
		Assert.assertEquals("Incorrect order member", clonePaymentInfo, order.getPaymentInfo());


	}

	private void assertEntriesCalculatedStatus(final AbstractOrderModel order, final boolean calculatedFlag)
	{
		if (order.getEntries() != null)
		{
			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				Assert.assertTrue("Entry should " + (calculatedFlag ? "" : " not ") + " be calculated", entry.getCalculated()
						.booleanValue() == calculatedFlag);
			}
		}
	}
}
