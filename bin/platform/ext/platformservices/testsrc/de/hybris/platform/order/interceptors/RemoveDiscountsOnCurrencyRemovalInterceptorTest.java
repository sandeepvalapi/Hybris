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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.order.DiscountService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test of {@link RemoveDiscountsOnCurrencyRemovalInterceptor}
 */
@UnitTest
public class RemoveDiscountsOnCurrencyRemovalInterceptorTest
{
	private RemoveDiscountsOnCurrencyRemovalInterceptor interceptor;

	@Mock
	private ModelService mockModelService;

	@Mock
	private DiscountService mockDiscountService;

	private CurrencyModel currencyA;
	private CurrencyModel currencyB;

	private Collection<DiscountModel> discountsB;

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
		interceptor = new RemoveDiscountsOnCurrencyRemovalInterceptor();
		interceptor.setDiscountService(mockDiscountService);
		interceptor.setModelService(mockModelService);

		currencyA = new CurrencyModel();
		currencyB = new CurrencyModel();

		final DiscountModel discountB1 = new DiscountModel();
		discountB1.setCurrency(currencyB);
		final DiscountModel discountB2 = new DiscountModel();
		discountB2.setCurrency(currencyB);

		discountsB = new ArrayList<DiscountModel>();
		discountsB.add(discountB1);
		discountsB.add(discountB2);

		//set mock responses
		when(mockDiscountService.getDiscountsForCurrency(currencyA)).thenReturn(Collections.<DiscountModel> emptyList());
		when(mockDiscountService.getDiscountsForCurrency(currencyB)).thenReturn(discountsB);
	}

	@Test
	public void testOnRemove() throws Exception
	{
		interceptor.onRemove(currencyA, null);
		verify(mockDiscountService, times(1)).getDiscountsForCurrency(currencyA);
		verify(mockModelService, never()).removeAll(Mockito.anyCollection());

		interceptor.onRemove(currencyB, null);
		verify(mockDiscountService, times(1)).getDiscountsForCurrency(currencyB);
		verify(mockModelService, times(1)).removeAll(discountsB);

	}

}
