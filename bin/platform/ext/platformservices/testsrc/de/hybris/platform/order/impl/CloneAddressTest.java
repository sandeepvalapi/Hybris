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
package de.hybris.platform.order.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.AddressService;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Test presenting a {@link AddressModel} and {@link PaymentInfoModel} clone within {@link OrderModel}.
 */
@IntegrationTest
public class CloneAddressTest extends ServicelayerTest
{
	@Resource
	private AddressService addressService;
	@Resource
	private ModelService modelService;
	@Resource
	private CommonI18NService commonI18NService;

	private UserModel user;

	private AddressModel originalDeliveryAdress;
	private AddressModel originalPaymentAdress;

	private PaymentInfoModel originalPayment;


	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();

		user = new UserModel();
		user.setUid("testUser");
		user.setName("TestUser");

		modelService.save(user);

		originalDeliveryAdress = addressService.createAddressForUser(user);
		modelService.save(originalDeliveryAdress);

		originalPaymentAdress = addressService.createAddressForUser(user);
		modelService.save(originalPaymentAdress);


		originalPayment = modelService.create(PaymentInfoModel.class);
		originalPayment.setCode("Code test");
		originalPayment.setUser(user);
		modelService.save(originalPayment);
	}

	@Test
	public void testAssignOnce() throws Exception
	{
		final OrderModel testOrder = modelService.create(OrderModel.class);

		testOrder.setCode("order calc test");
		testOrder.setUser(user);
		testOrder.setCurrency(commonI18NService.getBaseCurrency());
		testOrder.setDate(new Date());
		testOrder.setNet(Boolean.FALSE);

		testOrder.setDeliveryAddress(originalDeliveryAdress);
		testOrder.setPaymentAddress(originalPaymentAdress);
		testOrder.setPaymentInfo(originalPayment);

		assertNotNull(testOrder.getPaymentInfo());

		modelService.save(testOrder);

		assertNotNull(testOrder.getPaymentInfo());
		assertTrue(modelService.isUpToDate(originalPayment));

		verifyIfClonedCorrectly(originalPayment, testOrder.getPaymentInfo());
		verifyIfClonedCorrectly(originalDeliveryAdress, testOrder.getDeliveryAddress());
		verifyIfClonedCorrectly(originalPaymentAdress, testOrder.getPaymentAddress());
	}

	@Test
	public void testAssignTwice() throws Exception
	{
		final OrderModel testOrder = modelService.create(OrderModel.class);

		testOrder.setCode("order calc test");
		testOrder.setUser(user);
		testOrder.setCurrency(commonI18NService.getBaseCurrency());
		testOrder.setDate(new Date());
		testOrder.setNet(Boolean.FALSE);

		testOrder.setDeliveryAddress(originalDeliveryAdress);
		testOrder.setPaymentAddress(originalPaymentAdress);
		testOrder.setPaymentInfo(originalPayment);

		modelService.save(testOrder);


		verifyIfClonedCorrectly(originalPayment, testOrder.getPaymentInfo());
		verifyIfClonedCorrectly(originalDeliveryAdress, testOrder.getDeliveryAddress());
		verifyIfClonedCorrectly(originalPaymentAdress, testOrder.getPaymentAddress());

		final PaymentInfoModel originalPaymentClone = testOrder.getPaymentInfo();
		final AddressModel orignalDeliveryAdressClone = testOrder.getDeliveryAddress();
		final AddressModel orignalPaymentAdressClone = testOrder.getPaymentAddress();

		final AddressModel originalDeliveryAdress2 = addressService.createAddressForUser(user);
		modelService.save(originalDeliveryAdress2);

		final AddressModel originalPaymentAdress2 = addressService.createAddressForUser(user);
		modelService.save(originalPaymentAdress2);


		final PaymentInfoModel originalPayment2 = modelService.create(PaymentInfoModel.class);
		originalPayment2.setCode("Code test");
		originalPayment2.setUser(user);
		modelService.save(originalPayment2);

		testOrder.setDeliveryAddress(originalDeliveryAdress2);
		testOrder.setPaymentAddress(originalPaymentAdress2);
		testOrder.setPaymentInfo(originalPayment2);

		modelService.save(testOrder);

		verifyIfClonedCorrectly(originalPayment2, testOrder.getPaymentInfo());
		verifyIfClonedCorrectly(originalDeliveryAdress2, testOrder.getDeliveryAddress());
		verifyIfClonedCorrectly(originalPaymentAdress2, testOrder.getPaymentAddress());

		Assert.assertTrue("original's payment clone should be removed ", modelService.isRemoved(originalPaymentClone));
		Assert.assertTrue("original's delivery adress clone should be removed ", modelService.isRemoved(orignalDeliveryAdressClone));
		Assert.assertTrue("original's payment adress clone should be removed ", modelService.isRemoved(orignalPaymentAdressClone));
	}


	@Test
	public void testReAssign() throws Exception
	{
		final OrderModel testOrder = modelService.create(OrderModel.class);

		testOrder.setCode("order calc test");
		testOrder.setUser(user);
		testOrder.setCurrency(commonI18NService.getBaseCurrency());
		testOrder.setDate(new Date());
		testOrder.setNet(Boolean.FALSE);

		testOrder.setDeliveryAddress(originalDeliveryAdress);
		testOrder.setPaymentAddress(originalPaymentAdress);
		testOrder.setPaymentInfo(originalPayment);

		modelService.save(testOrder);

		verifyIfClonedCorrectly(originalPayment, testOrder.getPaymentInfo());
		verifyIfClonedCorrectly(originalDeliveryAdress, testOrder.getDeliveryAddress());
		verifyIfClonedCorrectly(originalPaymentAdress, testOrder.getPaymentAddress());

		testOrder.setDeliveryAddress(testOrder.getDeliveryAddress());
		testOrder.setPaymentAddress(testOrder.getPaymentAddress());
		testOrder.setPaymentInfo(testOrder.getPaymentInfo());

		final AddressModel deliveryAdress = testOrder.getDeliveryAddress();
		final AddressModel paymentAdress = testOrder.getPaymentAddress();
		final PaymentInfoModel payment = testOrder.getPaymentInfo();

		modelService.save(testOrder);

		verifyIfNotCloned(payment, testOrder.getPaymentInfo());
		verifyIfNotCloned(deliveryAdress, testOrder.getDeliveryAddress());
		verifyIfNotCloned(paymentAdress, testOrder.getPaymentAddress());
	}

	@Ignore("Cross assignment causes EJBItemNotFoundException while refresh PLA-10985")
	@Test
	public void testCrossReAssign() throws Exception
	{
		final OrderModel testOrder = modelService.create(OrderModel.class);

		testOrder.setCode("order calc test");
		testOrder.setUser(user);
		testOrder.setCurrency(commonI18NService.getBaseCurrency());
		testOrder.setDate(new Date());
		testOrder.setNet(Boolean.FALSE);

		testOrder.setDeliveryAddress(originalDeliveryAdress);
		testOrder.setPaymentAddress(originalPaymentAdress);
		testOrder.setPaymentInfo(originalPayment);

		modelService.save(testOrder);

		verifyIfClonedCorrectly(originalPayment, testOrder.getPaymentInfo());
		verifyIfClonedCorrectly(originalDeliveryAdress, testOrder.getDeliveryAddress());
		verifyIfClonedCorrectly(originalPaymentAdress, testOrder.getPaymentAddress());

		final AddressModel deliveryAdress = testOrder.getDeliveryAddress();
		final AddressModel paymentAdress = testOrder.getPaymentAddress();
		final PaymentInfoModel payment = testOrder.getPaymentInfo();

		testOrder.setPaymentInfo(payment);
		testOrder.setDeliveryAddress(paymentAdress);
		testOrder.setPaymentAddress(deliveryAdress);

		modelService.save(testOrder);

		verifyIfNotCloned(payment, testOrder.getPaymentInfo());
		verifyIfNotCloned(paymentAdress, testOrder.getDeliveryAddress());
		verifyIfNotCloned(deliveryAdress, testOrder.getPaymentAddress());
	}


	@Test
	public void testAssignOneForBoth() throws Exception
	{
		final OrderModel testOrder = modelService.create(OrderModel.class);

		testOrder.setCode("order calc test");
		testOrder.setUser(user);
		testOrder.setCurrency(commonI18NService.getBaseCurrency());
		testOrder.setDate(new Date());
		testOrder.setNet(Boolean.FALSE);

		testOrder.setDeliveryAddress(originalDeliveryAdress);
		//testOrder.setPaymentAddress(originalPaymentAdress);

		modelService.save(testOrder);

		verifyIfClonedCorrectly(originalDeliveryAdress, testOrder.getDeliveryAddress());

		testOrder.setPaymentAddress(originalDeliveryAdress);

		modelService.save(testOrder);

		verifyIfClonedCorrectly(originalDeliveryAdress, testOrder.getPaymentAddress());
	}

	private void verifyIfClonedCorrectly(final PaymentInfoModel current, final PaymentInfoModel modified)
	{
		//assertEquals("Adresses should  be cloned", current.getPk(), modified.getOriginal().getPk());
		assertEquals("Adresses should  be marked as  cloned", Boolean.TRUE, modified.getDuplicate());
	}

	private void verifyIfClonedCorrectly(final AddressModel current, final AddressModel modified)
	{
		assertEquals("Adresses should  be cloned", current.getPk(), modified.getOriginal().getPk());
		assertEquals("Adresses should  be marked as  cloned", Boolean.TRUE, modified.getDuplicate());
	}

	private void verifyIfNotCloned(final PaymentInfoModel current, final PaymentInfoModel modified)
	{
		assertEquals("Payment info should not be cloned", current.getPk(), modified.getPk());
	}

	private void verifyIfNotCloned(final AddressModel current, final AddressModel modified)
	{
		assertEquals("Adresses should not be cloned", current.getPk(), modified.getPk());
	}
}
