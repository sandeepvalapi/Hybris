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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@IntegrationTest
public class OrderCodePatternTest extends ServicelayerBaseTest
{

	public static final String NUMERIC = "numeric";
	public static final String KEYGEN_ORDER_CODE_TYPE = "keygen.order.code.type";
	public static final String KEYGEN_ORDER_CODE_DIGITS = "keygen.order.code.digits";
	public static final String KEYGEN_ORDER_CODE_TEMPLATE = "keygen.order.code.template";
	@Resource
	private CartService cartService;
	@Resource
	private OrderService orderService;
	@Resource
	private ModelService modelService;

	/**
	 * Retrieves the session cart with service layer, calls the placeOrder for the cart, and get the first order code.
	 * Retrieves another cart within jalo session, converts it the model, also calls the placeOrder for it, and get the
	 * second order code. Compare the pattern of these two codes, and check they have the same length and pattern. For
	 * the pattern, the test is to check if they begin with the same code except the last two characters.
	 */
	@Test
	public void testOrderCode() throws Exception
	{
		//order code with normal service layer creation
		final CartModel cart1 = cartService.getSessionCart();
		final OrderModel order1 = orderService.placeOrder(cart1, null, null, null);
		final String patternCode = order1.getCode();

		//cart from jalo manager
		final Cart cart = jaloSession.getCart();
		final CartModel cartModel = modelService.get(cart);
		System.out.println(cart.getCode());
		final OrderModel order2 = orderService.placeOrder(cartModel, null, null, null);
		final String jaloOrderCode = order2.getCode();

		assertEquals("both codes must have the same length", patternCode.length(), jaloOrderCode.length());

		final Pattern pattern = createPatternFromTemplate(Config.getParameter(KEYGEN_ORDER_CODE_TEMPLATE));
		final Matcher matcher1 = pattern.matcher(patternCode);
		final Matcher matcher2 = pattern.matcher(jaloOrderCode);

		assertTrue("both codes must match the pattern", matcher1.matches());
		assertTrue("both codes must match the pattern", matcher2.matches());
	}

	private Pattern createPatternFromTemplate(final String template)
	{
		if(Config.getParameter(KEYGEN_ORDER_CODE_TYPE).equalsIgnoreCase(NUMERIC))
		{
			return Pattern.compile(template.replaceAll("\\$",
					"\\\\d{" + Config.getParameter(KEYGEN_ORDER_CODE_DIGITS) + "}"));
		}
		else
		{
			return Pattern.compile(template.replaceAll("\\$",
					"[A-Za-z0-9]{" + Config.getParameter(KEYGEN_ORDER_CODE_DIGITS) + "}"));
		}
	}

}
