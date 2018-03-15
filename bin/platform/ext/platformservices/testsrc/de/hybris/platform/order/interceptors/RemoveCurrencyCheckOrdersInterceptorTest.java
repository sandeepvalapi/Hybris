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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.daos.OrderDao;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class RemoveCurrencyCheckOrdersInterceptorTest
{
	private RemoveCurrencyCheckOrdersInterceptor interceptor;

	@Mock
	private OrderDao mockOrderDao;

	private CurrencyModel currency;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		interceptor = new RemoveCurrencyCheckOrdersInterceptor();
		interceptor.setOrderDao(mockOrderDao);

		currency = new CurrencyModel();

	}

	@Test
	public void testCurrencyHasCarts()
	{
		final CartModel cart = new CartModel();
		cart.setCurrency(currency);
		when(mockOrderDao.findOrdersByCurrency(currency)).thenReturn(Collections.<AbstractOrderModel> singletonList(cart));
		boolean sucess = false;
		try
		{
			interceptor.onRemove(currency, null);
			fail("InterceptorException expected while removing currency having carts");
		}
		catch (final InterceptorException e)
		{
			sucess = true;
		}
		Assert.assertTrue("InterceptorException expected while removing currency having carts", sucess);
	}

	@Test
	public void testCurrencyHasOrders()
	{
		final OrderModel order = new OrderModel();
		order.setCurrency(currency);
		when(mockOrderDao.findOrdersByCurrency(currency)).thenReturn(Collections.<AbstractOrderModel> singletonList(order));
		boolean sucess = false;
		try
		{
			interceptor.onRemove(currency, null);
			fail("InterceptorException expected while removing currency having orders");
		}
		catch (final InterceptorException e)
		{
			sucess = true;
		}
		Assert.assertTrue("InterceptorException expected while removing currency having orders", sucess);
	}

	@Test
	public void testCurrencyHasNoOrders()
	{
		when(mockOrderDao.findOrdersByCurrency(currency)).thenReturn(Collections.<AbstractOrderModel> emptyList());

		try
		{
			interceptor.onRemove(currency, null);
		}
		catch (final InterceptorException e)
		{
			fail("Unexpected InterceptorException on currency having no orders");
		}
	}

}
