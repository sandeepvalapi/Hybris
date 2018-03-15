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
package de.hybris.platform.servicelayer.i18n.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test covering the (@link ValidateCurrencyDataInterceptor) logic.
 */
@UnitTest
public class ValidateCurrencyDataInterceptorTest
{
	@Mock
	private InterceptorContext interceptorContext;
	private ValidateCurrencyDataInterceptor interceptor;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		interceptor = new ValidateCurrencyDataInterceptor();
	}

	@Test
	public void testCurrencyDigitNoValid() throws InterceptorException
	{
		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("frog");
		curr.setConversion(Double.valueOf(1));
		curr.setDigits(Integer.valueOf(-1));
		try
		{
			interceptor.onValidate(curr, interceptorContext);
			Assert.fail("Interceptor should fail since currency digit is not valid ");
		}
		catch (final InterceptorException ie)
		{
			//fine here 
		}
	}

	@Test
	public void testCurrencyConversionNoValid() throws InterceptorException
	{
		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("frog");
		curr.setConversion(Double.valueOf(0));
		curr.setDigits(Integer.valueOf(2));
		try
		{
			interceptor.onValidate(curr, interceptorContext);
			Assert.fail("Interceptor should fail since currency conversion  is not valid ");
		}
		catch (final InterceptorException ie)
		{
			//fine here 
		}
	}

	@Test
	public void testCurrencyDataValid() throws InterceptorException
	{
		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("frog");
		curr.setConversion(Double.valueOf(1));
		curr.setDigits(Integer.valueOf(2));
		interceptor.onValidate(curr, interceptorContext);
	}

	@Test
	public void testNullConversionValue() throws InterceptorException
	{
		final CurrencyModel curr = new CurrencyModel();
		try
		{
			interceptor.onValidate(curr, interceptorContext);
			Assert.fail("Interceptor should fail since currency conversion null ");
		}
		catch (final InterceptorException ie)
		{
			//fine here 
		}
	}

	@Test
	public void nullDigitsValueDefaultsToZero() throws InterceptorException
	{
		final CurrencyModel curr = new CurrencyModel();
		curr.setConversion(Double.valueOf(0.4d));

		interceptor.onValidate(curr, interceptorContext);

		// fine
	}
}
