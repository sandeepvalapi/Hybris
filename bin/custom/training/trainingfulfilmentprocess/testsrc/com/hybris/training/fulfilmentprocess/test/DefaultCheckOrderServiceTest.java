/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import com.hybris.training.fulfilmentprocess.impl.DefaultCheckOrderService;

import java.util.Arrays;
import java.util.Collections;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultCheckOrderServiceTest
{
	private final DefaultCheckOrderService defaultCheckOrderService = new DefaultCheckOrderService();

	private OrderModel order;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		order = new OrderModel();
		order.setCalculated(Boolean.TRUE);
		order.setEntries(Arrays.<AbstractOrderEntryModel> asList(new OrderEntryModel()));
		order.setDeliveryAddress(new AddressModel());
		order.setPaymentInfo(new PaymentInfoModel());
	}

	@Test
	public void testCheck()
	{
		Assertions.assertThat(defaultCheckOrderService.check(order)).isFalse();
	}

	@Test
	public void testNotCalculated()
	{
		order.setCalculated(Boolean.FALSE);
		Assertions.assertThat(defaultCheckOrderService.check(order)).isFalse();
	}

	@Test
	public void testNoEntries()
	{
		order.setEntries(Collections.EMPTY_LIST);
		Assertions.assertThat(defaultCheckOrderService.check(order)).isFalse();
	}

	@Test
	public void testNoPaymentInfo()
	{
		order.setPaymentInfo(null);
		Assertions.assertThat(defaultCheckOrderService.check(order)).isFalse();
	}
}
