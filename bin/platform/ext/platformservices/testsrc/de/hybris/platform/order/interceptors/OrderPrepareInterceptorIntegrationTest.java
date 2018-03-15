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
package de.hybris.platform.order.interceptors;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration test of {@link de.hybris.platform.order.interceptors.DefaultOrderPrepareInterceptor}
 */
@IntegrationTest
public class OrderPrepareInterceptorIntegrationTest extends ServicelayerTest
{

	@Resource
	private ModelService modelService;


	private OrderModel order;
	private PaymentInfoModel paymentInfo;
	private AddressModel deliveryAddress;
	private AddressModel paymentAddress;
	private CurrencyModel curr;
	private UserModel testUser;

	@Before
	public void setUp()
	{

		curr = modelService.create(CurrencyModel.class);
		curr.setActive(Boolean.TRUE);
		curr.setIsocode("PLN");
		curr.setDigits(Integer.valueOf(2));
		curr.setConversion(Double.valueOf(0.76d));
		curr.setSymbol("PLN");

		testUser = modelService.create(UserModel.class);
		testUser.setUid("testUser");

		paymentInfo = paymentInfo("theCode");
		paymentAddress = addressModel();
		deliveryAddress = addressModel();
	}

	private PaymentInfoModel paymentInfo(final String theCode)
	{
		paymentInfo = new PaymentInfoModel();
		paymentInfo.setUser(testUser);
		paymentInfo.setCode(theCode);
		return paymentInfo;
	}


	private AddressModel addressModel()
	{
		final AddressModel result = new AddressModel();
		result.setOwner(testUser);
		result.setFirstname("Juergen");
		result.setLastname("Albertsen");
		result.setTown("Muenchen");
		return result;
	}

	@Test
	public void shouldRemoveOrphanedPaymentInfo_PaymentAddress_And_DeliveryAddressWhenOrderIsUpdated()
	{
		// given
		order = createOrder();
		order.setPaymentAddress(paymentAddress);
		order.setDeliveryAddress(deliveryAddress);
		order.setPaymentInfo(paymentInfo);

		modelService.save(order);

		final PaymentInfoModel paymentInfoClone = order.getPaymentInfo();
		final AddressModel deliveryAddressClone = order.getDeliveryAddress();
		final AddressModel paymentAddressClone = order.getPaymentAddress();

		// sanity check, to make sure provided items were cloned
		assertThat(paymentAddressClone).isNotSameAs(paymentAddress);
		assertThat(deliveryAddressClone).isNotSameAs(deliveryAddress);
		assertThat(paymentInfoClone).isNotSameAs(paymentInfo);

		// when
		order.setPaymentAddress(addressModel());
		order.setDeliveryAddress(addressModel());
		order.setPaymentInfo(paymentInfo("other"));

		modelService.save(order);

		// then
		assertIsDeleted(paymentInfoClone);
		assertIsDeleted(paymentAddressClone);
		assertIsDeleted(deliveryAddressClone);

		assertThat(modelService.isRemoved(paymentInfo)).isFalse();
		assertThat(modelService.isRemoved(paymentAddress)).isFalse();
		assertThat(modelService.isRemoved(deliveryAddress)).isFalse();
	}

	@Test
	public void shouldNotRemoveReplacedPaymentAddressWhenTheSameItemIsAlsoADeliveryAddress()
	{
		// given
		order = createOrder();
		order.setPaymentAddress(paymentAddress);
		order.setDeliveryAddress(paymentAddress);
		order.setPaymentInfo(paymentInfo);

		modelService.save(order);

		final AddressModel deliveryAddressClone = order.getDeliveryAddress();
		final AddressModel paymentAddressClone = order.getPaymentAddress();

		// when
		order.setPaymentAddress(addressModel());

		modelService.save(order);

		// then
		assertIsDeleted(paymentAddressClone);
		assertThat(modelService.isRemoved(deliveryAddressClone)).isFalse();
	}

	private OrderModel createOrder()
	{
		final OrderModel order = modelService.create(OrderModel.class);
		order.setDate(new Date());
		order.setCurrency(curr);
		order.setUser(testUser);
		order.setNet(Boolean.FALSE);
		order.setCode("test order");
		return order;
	}

	private void assertIsDeleted(final AbstractItemModel model)
	{
		assertThat(modelService.isRemoved(model)).isTrue();
		try
		{
			modelService.get(model.getPk());
			Assert.fail("should have failed with ModelLoadingException");
		}
		catch (final ModelLoadingException e)
		{
			assertThat(e.getMessage()).startsWith("No item found for given pk");
		}
	}

}
