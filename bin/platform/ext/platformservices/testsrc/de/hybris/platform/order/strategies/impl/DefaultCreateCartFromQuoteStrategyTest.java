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
package de.hybris.platform.order.strategies.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCreateCartFromQuoteStrategyTest
{
	@InjectMocks
	private final DefaultCreateCartFromQuoteStrategy defaultCreateCartFromQuoteStrategy = new DefaultCreateCartFromQuoteStrategy();

	@Mock
	private TypeService typeService;
	@Mock
	private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;
	@Mock
	private KeyGenerator keyGenerator; // NOPMD NOSONAR need to mock here to avoid NPE
	@Mock
	private ModelService modelService; // NOPMD NOSONAR need to mock here to avoid NPE


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldCreateCartFromQuote()
	{
		final QuoteModel quoteModel = new QuoteModel();

		final ComposedTypeModel composedTypeModel = mock(ComposedTypeModel.class);
		given(typeService.getComposedTypeForClass(any(Class.class))).willReturn(composedTypeModel);
		final String quoteCode = "quoteCode";
		given(defaultCreateCartFromQuoteStrategy.generateCode()).willReturn(quoteCode);
		given(cloneAbstractOrderStrategy.clone(null, null, quoteModel, quoteCode, CartModel.class, CartEntryModel.class))
				.willReturn(new CartModel());

		final CartModel cart = defaultCreateCartFromQuoteStrategy.createCartFromQuote(quoteModel);

		assertNotNull("Cart is null", cart);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotCreateCartFromQuoteIfQuoteIsNull()
	{
		defaultCreateCartFromQuoteStrategy.createCartFromQuote(null);
	}
}
