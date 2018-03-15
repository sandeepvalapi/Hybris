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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Integration test checking prepare interceptors for a {@link CurrencyModel}.
 */
@IntegrationTest
public class PrepareCurrencyInterceptorTest extends ServicelayerTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18nService;

	@Test
	public void testDefaultCurrencySymbol()
	{
		final CurrencyModel currency = modelService.create(CurrencyModel.class);
		currency.setIsocode("EUR");
		modelService.save(currency);
		Assert.assertEquals(Currency.getInstance("EUR").getSymbol(), currency.getSymbol());
	}

	@Test
	public void testDefaultCurrencySymbolNotExist()
	{
		try
		{
			final CurrencyModel currency = modelService.create(CurrencyModel.class);
			currency.setIsocode("myTestCode");
			modelService.save(currency);
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof MandatoryAttributesValidator);
		}
	}


	@Test
	public void testFallback()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setName("catalog");
		catalog.setId("unique");

		final CatalogVersionModel version = modelService.create(CatalogVersionModel.class);
		version.setVersion("v 1.0");
		version.setCatalog(catalog);

		final LanguageModel firstLanguage = modelService.create(LanguageModel.class);
		firstLanguage.setIsocode("first");

		final LanguageModel secondLanguage = modelService.create(LanguageModel.class);
		secondLanguage.setIsocode("second");
		secondLanguage.setFallbackLanguages(Arrays.asList(firstLanguage));
		modelService.saveAll();


		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("sampleProduct");
		product.setCatalogVersion(version);
		//value for first language
		product.setDescription("some value in first ", new Locale(firstLanguage.getIsocode()));
		//value for second language

		modelService.saveAll();

		i18nService.setCurrentLocale(new Locale(firstLanguage.getIsocode()));

		modelService.refresh(product);

		System.out.println(product.getDescription());

		i18nService.setCurrentLocale(new Locale(secondLanguage.getIsocode()));

		modelService.refresh(product);

		System.out.println(product.getDescription());


	}
}
