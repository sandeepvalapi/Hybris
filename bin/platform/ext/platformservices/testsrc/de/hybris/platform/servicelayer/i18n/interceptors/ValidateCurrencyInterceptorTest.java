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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * Integration test checking validation interceptors for a {@link CurrencyModel}.
 */
@IntegrationTest
public class ValidateCurrencyInterceptorTest extends ServicelayerTest
{
	@Resource
	private ModelService modelService;

	@Test
	public void testCreateCurrencyWrongDigit()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("eurogabka");
		curr.setSymbol("eurogabka");
		curr.setDigits(Integer.valueOf(-1));
		try
		{
			modelService.save(curr);
			fail("expected ModelSavingException");

		}
		catch (final Exception e)
		{
			assertTrue(e instanceof ModelSavingException);
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof ValidateCurrencyDataInterceptor);
		}
	}

	@Test
	public void testCreateCurrencyWrongConversion()
	{
		try
		{
			final CurrencyModel curr = modelService.create(CurrencyModel.class);
			curr.setIsocode("eurogabka");
			curr.setSymbol("eurogabka");
			curr.setConversion(Double.valueOf(0));
			modelService.save(curr);
			fail("expected ModelSavingException");

		}
		catch (final Exception e)
		{
			assertTrue(e instanceof ModelSavingException);
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof ValidateCurrencyDataInterceptor);
		}
	}

	@Test
	public void testCreateCurrencyCorrectConversion()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setIsocode("eurogabka");
		curr.setSymbol("eurogabka");
		curr.setConversion(Double.valueOf(1));
		curr.setDigits(Integer.valueOf(5));
		modelService.save(curr);
	}
}
