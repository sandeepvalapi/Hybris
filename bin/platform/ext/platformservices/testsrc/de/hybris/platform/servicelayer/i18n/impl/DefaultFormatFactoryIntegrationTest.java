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
package de.hybris.platform.servicelayer.i18n.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


/**
 * Integration checking correctness of the spring configuration. Example showing getting a some format for a custom
 * {@link Locale}, {@link Currency}.
 */
@IntegrationTest
public class DefaultFormatFactoryIntegrationTest extends ServicelayerTest
{

	private static final String CURRENCY_SYMBOL = Currency.getInstance(Locale.KOREA).getSymbol();

	@Resource
	private FormatFactory formatFactory;
	@Resource
	private SessionService sessionService;
	@Resource
	private I18NService i18nService;
	@Resource
	private ModelService modelService;

	@Test
	public void testDateFormatFactory()
	{

		final Long now = Long.valueOf(System.currentTimeMillis());
		final DateFormat format = formatFactory.createDateTimeFormat(DateFormat.FULL, -1);
		Assert.assertEquals(DateFormat.getDateInstance(DateFormat.FULL, i18nService.getCurrentLocale()).format(now),
				format.format(now));

	}

	@Test
	public void testCurrencyFactoryWithinCustomContext()
	{

		final Double number = Double.valueOf(10000000.00001);

		final LanguageModel korean = modelService.create(LanguageModel.class);
		korean.setIsocode(Locale.KOREA.toString());

		modelService.save(korean);

		final CurrencyModel currencyModel = modelService.create(CurrencyModel.class);
		currencyModel.setIsocode(CURRENCY_SYMBOL);
		currencyModel.setDigits(Integer.valueOf(5));
		currencyModel.setSymbol(CURRENCY_SYMBOL);

		modelService.save(currencyModel);

		final Object formattedCurrency = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				i18nService.setCurrentLocale(Locale.KOREA);
				i18nService.setCurrentCurrency(currencyModel);

				return formatFactory.createCurrencyFormat().format(number);
			}

		});

		Assert.assertFalse(NumberFormat.getCurrencyInstance().format(number).equals(formattedCurrency));
		Assert.assertFalse(NumberFormat.getCurrencyInstance(Locale.KOREA).format(number).equals(formattedCurrency));
		Assert.assertEquals(CURRENCY_SYMBOL + "10,000,000.00001", formattedCurrency);

	}
}
