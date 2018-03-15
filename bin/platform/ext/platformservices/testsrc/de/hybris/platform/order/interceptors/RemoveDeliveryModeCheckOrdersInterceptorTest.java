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

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.daos.OrderDao;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class RemoveDeliveryModeCheckOrdersInterceptorTest
{
	private RemoveDeliveryModeCheckOrdersInterceptor interceptor;

	@Mock
	private OrderDao mockOrderDao;

	private DeliveryModeModel deliveryMode1;
	private DeliveryModeModel deliveryMode2;

	private OrderModel order;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		interceptor = new RemoveDeliveryModeCheckOrdersInterceptor();
		interceptor.setOrderDao(mockOrderDao);

		deliveryMode1 = new DeliveryModeModel();
		deliveryMode2 = new DeliveryModeModel();

		order = new OrderModel();
		order.setDeliveryMode(deliveryMode1);

		//record mock returns
		when(mockOrderDao.findOrdersByDelivereMode(deliveryMode1))
				.thenReturn(Collections.<AbstractOrderModel> singletonList(order));
		when(mockOrderDao.findOrdersByDelivereMode(deliveryMode2)).thenReturn(Collections.<AbstractOrderModel> emptyList());
	}

	@Test(expected = InterceptorException.class)
	public void testOnRemoveHasOrders() throws InterceptorException
	{
		interceptor.onRemove(deliveryMode1, null);
	}

	@Test
	public void testOnRemoveHasNoOrders() throws InterceptorException
	{
		interceptor.onRemove(deliveryMode2, null);
	}
}
