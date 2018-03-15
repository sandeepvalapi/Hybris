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
package de.hybris.platform.order.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.junit.Test;



/**
 * Integration test for {@link AfterSessionUserChangeListener}.
 */
@IntegrationTest
public class AfterSessionUserChangeListenerTest extends ServicelayerTest
{
	@Resource
	private UserService userService;

	@Resource
	private CartService cartService;

	@Resource
	private ModelService modelService;

	@Test
	public void testOnEvent() throws Exception
	{
		// get admin user
		final UserModel user = userService.getAdminUser();

		// create session cart, anonymous user is set on cart
		cartService.getSessionCart();

		// set admin user as current
		userService.setCurrentUser(user);

		// AfterSessionUserChangeEvent processed in background
		// ....

		// get current cart
		final CartModel cart = cartService.getSessionCart();

		// refresh cart to ensure that cart user is persisted
		modelService.refresh(cart);

		assertNotNull("Cart is null.", cart);
		assertNotNull("Cart user is null.", cart.getUser());
		assertEquals("Cart user differs.", user, cart.getUser());
	}

}
