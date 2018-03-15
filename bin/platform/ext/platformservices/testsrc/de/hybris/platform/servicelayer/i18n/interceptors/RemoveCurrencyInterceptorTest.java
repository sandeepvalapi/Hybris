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
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * Class for testing remove interceptor for {@link CurrencyModel}
 */
@IntegrationTest
public class RemoveCurrencyInterceptorTest extends ServicelayerTest
{
	@Resource
	private ModelService modelService;

	@Test
	public void testRemoveCurrency() //NOPMD , assert or fail method not needed there
	{
		final CurrencyModel currency = modelService.create(CurrencyModel.class);
		currency.setIsocode("eurogabka");
		currency.setSymbol("eurogabka");
		modelService.save(currency);
		currency.setBase(Boolean.TRUE);
		try
		{
			modelService.remove(currency);
			fail("expected ModelSavingException");
		}
		catch (final ModelRemovalException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof RemoveBaseCurrencyInterceptor);
		}
		catch (final Exception e)
		{
			fail("unknown exception:" + e);
		}
	}


}
