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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionService;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * Concurrency Test for {@link CartService}
 */
@UnitTest
public class CartServiceConcurrencyTest
{
	private CartService cartService;

	@Before
	public void setUp()
	{
		cartService = setupCartService();
	}

	@Test
	public void testMultiThreadedGetOrCreate()
	{
		final int THREADS = 10;
		final RunnerCreator<GetCartRunner> runnerCreator = new TestThreadsHolder.RunnerCreator<GetCartRunner>()
		{
			@Override
			public GetCartRunner newRunner(final int threadNumber)
			{
				return new GetCartRunner(cartService);
			}
		};
		final TestThreadsHolder<GetCartRunner> threads = new TestThreadsHolder<GetCartRunner>(THREADS, runnerCreator);

		threads.startAll();
		Assert.assertTrue(threads.waitAndDestroy(5));
		Assert.assertEquals(Collections.EMPTY_MAP, threads.getErrors());

		// compare carts -> all must be same
		final Set<CartModel> carts = new LinkedHashSet<CartModel>();
		for (final GetCartRunner runner : threads.getRunners())
		{
			carts.add(runner.cart);
		}
		Assert.assertEquals("did get more than one session cart: " + carts, 1, carts.size());
	}

	private static class GetCartRunner implements Runnable
	{
		private final CartService cartService;

		private volatile CartModel cart;

		GetCartRunner(final CartService cartService)
		{
			this.cartService = cartService;
		}

		@Override
		public void run()
		{
			this.cart = cartService.getSessionCart();
		}
	}

	private CartService setupCartService()
	{
		final DefaultCartService service = new DefaultCartService();
		service.setCartFactory(new CartFactory()
		{
			final AtomicInteger counter = new AtomicInteger();

			@Override
			public CartModel createCart()
			{
				return new CartModel()
				{
					final int cartNumber = counter.getAndIncrement();

					@Override
					public String toString()
					{
						return "Cart(" + cartNumber + ")";
					}
				};
			}
		});
		service.setSessionService(new DefaultSessionService()
		{
			private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

			@Override
			public void setAttribute(final String name, final Object value)
			{
				attributes.put(name, value);
			}

			@Override
			public <T> T getAttribute(final String name)
			{
				return (T) attributes.get(name);
			}

			@Override
			public <T> T getOrLoadAttribute(final String name, final SessionAttributeLoader<T> loader)
			{
				T result = (T) attributes.get(name);
				if (result == null)
				{
					synchronized (this)
					{
						result = (T) attributes.get(name);
						if (result == null)
						{
							result = loader.load();
							attributes.put(name, result);
						}
					}
				}
				return result;
			}
		});
		return service;
	}
}
