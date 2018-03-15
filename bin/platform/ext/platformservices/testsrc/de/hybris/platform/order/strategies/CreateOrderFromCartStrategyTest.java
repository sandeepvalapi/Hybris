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
package de.hybris.platform.order.strategies;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.impl.DefaultCreateOrderFromCartStrategy;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



@UnitTest
public class CreateOrderFromCartStrategyTest
{
	@InjectMocks
	private final DefaultCreateOrderFromCartStrategy defaultCreateOrderFromCartStrategy = new DefaultCreateOrderFromCartStrategy();

	@Mock
	private CartValidator cartValidator;

	//gets injected at defaultCreateOrderFromCartStrategy automatically
	@SuppressWarnings("unused")
	@Mock
	private KeyGenerator keyGenerator;

	@Mock
	private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSubmitOrder() throws InvalidCartException
	{
		final CartModel cart = new CartModel();
		final OrderModel order = new OrderModel();
		Mockito.when(cloneAbstractOrderStrategy.clone(null, null, cart, null, OrderModel.class, OrderEntryModel.class)).thenReturn(
				order);

		defaultCreateOrderFromCartStrategy.createOrderFromCart(cart);

		Mockito.verify(cartValidator).validateCart(cart);

	}
}
