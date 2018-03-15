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
package de.hybris.platform.servicelayer.cronjob.impl;

import static org.fest.assertions.Assertions.assertThat;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CronJobInitDefaultsInterceptorTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private SessionService sessionService;
	@Resource
	private ModelService modelService;
	private LanguageModel language, languageBackup;
	private CurrencyModel currency, currencyBackup;

	@Before
	public void setUp() throws Exception
	{
		languageBackup = sessionService.getAttribute(I18NConstants.LANGUAGE_SESSION_ATTR_KEY);
		currencyBackup = sessionService.getAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY);

		language = modelService.create(LanguageModel.class);
		language.setIsocode("pl_PL");
		language.setActive(Boolean.TRUE);
		modelService.save(language);
		sessionService.setAttribute(I18NConstants.LANGUAGE_SESSION_ATTR_KEY, language);

		currency = modelService.create(CurrencyModel.class);
		currency.setActive(Boolean.TRUE);
		currency.setIsocode("PLN");
		modelService.save(currency);
		sessionService.setAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY, currency);

		assertThat(commonI18NService.getCurrentLanguage()).isEqualTo(language);
		assertThat(commonI18NService.getCurrentCurrency()).isEqualTo(currency);
	}

	@After
	public void tearDown() throws Exception
	{
		sessionService.setAttribute(I18NConstants.LANGUAGE_SESSION_ATTR_KEY, languageBackup);
		sessionService.setAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY, currencyBackup);

		assertThat(commonI18NService.getCurrentLanguage()).isEqualTo(languageBackup);
		assertThat(commonI18NService.getCurrentCurrency()).isEqualTo(currencyBackup);
	}

	@Test
	public void shouldSetCurrentLanguageFromTheSessionToTheNewCronJob() throws Exception
	{
		// given
		final CronJobModel cronJob = modelService.create(CronJobModel.class);

		// when
		final LanguageModel sessionLanguage = cronJob.getSessionLanguage();

		// then
		assertThat(sessionLanguage).isNotNull().isEqualTo(language);
	}

	@Test
	public void shouldSetCurrentCurrencyFromTheSessionToTheNewCronJob() throws Exception
	{
		// given
		final CronJobModel cronJob = modelService.create(CronJobModel.class);

		// when
		final CurrencyModel currency = cronJob.getSessionCurrency();

		// then
		assertThat(currency).isNotNull();
		assertThat(currency.getIsocode()).isEqualTo("PLN");
	}

}
