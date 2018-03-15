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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CartSessionCurrencyChangeTest extends ServicelayerTest
{
	@Resource
	SessionService sessionService;
	@Resource
	CartService cartService;
	@Resource
	ModelService modelService;

	CurrencyModel currencyDef;
	CurrencyModel currencyNew;

	final static String CURRENCY_ATR_NAME = "currency";

	CartModel cart;

	@Before
	public void setUp()
	{
		currencyDef = modelService.create(CurrencyModel.class);
		currencyDef.setIsocode("EUR");
		modelService.save(currencyDef);


		currencyNew = modelService.create(CurrencyModel.class);
		currencyNew.setIsocode("USD");
		modelService.save(currencyNew);

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("user");
		modelService.save(user);

		cart = modelService.create(CartModel.class);
		cart.setNet(Boolean.FALSE);
		cart.setCurrency(currencyDef);
		cart.setDate(new Date());
		cart.setUser(user);
		modelService.save(cart);

		sessionService.setAttribute(JaloSession.CART, cart);
	}


	@Test
	public void changeSessionCurrency()
	{


		sessionService.setAttribute(CURRENCY_ATR_NAME, currencyDef);
		final CartModel cart = sessionService.getAttribute(JaloSession.CART);
		assertThat(cart.getCurrency()).isEqualTo(sessionService.getAttribute(CURRENCY_ATR_NAME));

		sessionService.setAttribute(CURRENCY_ATR_NAME, currencyNew);
		assertThat(cart.getCurrency()).isEqualTo(sessionService.getAttribute(CURRENCY_ATR_NAME));


	}


}
