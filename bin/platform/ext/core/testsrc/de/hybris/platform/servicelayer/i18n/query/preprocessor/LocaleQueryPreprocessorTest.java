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
package de.hybris.platform.servicelayer.i18n.query.preprocessor;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.preprocessor.QueryPreprocessor;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class LocaleQueryPreprocessorTest
{

	@InjectMocks
	private final QueryPreprocessor preprocessor = new LocaleQueryPreprocessor();
	@Mock
	private I18NService i18nService;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private LanguageModel language;
	private final FlexibleSearchQuery query = new FlexibleSearchQuery("foo");


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldNotProcessWhenLanguageAndLocaleInQueryObjectIsNull()
	{
		// given
		query.setLocale(null);
		query.setLanguage(null);

		// when
		preprocessor.process(query);

		// then
		verify(i18nService, times(0)).setCurrentLocale(((Locale) anyObject()));
	}

	@Test
	public void shouldProcessWhenThereIsLocaleInQueryObject()
	{
		// given
		final Locale locale = new Locale("en");
		query.setLocale(locale);
		query.setLanguage(null);

		// when
		preprocessor.process(query);

		// then
		verify(i18nService, times(1)).setCurrentLocale(locale);
	}

	@Test
	public void shouldProcessWhenThereIsLanguageInQueryObjectButLocaleIsNull()
	{
		// given
		final Locale expectedLocale = new Locale("en", "GB");
		given(language.getIsocode()).willReturn("en", "GB");
		given(commonI18NService.getLocaleForLanguage(language)).willReturn(expectedLocale);
		query.setLanguage(language);
		query.setLocale(null);

		// when
		preprocessor.process(query);

		// then
		verify(i18nService, times(1)).setCurrentLocale(expectedLocale);
	}

	@Test
	public void shouldProcessWhenThereIsLanguageAndLocaleInQueryObjectUsingLocaleInsteadOfLanguage()
	{
		// given
		final Locale locale = new Locale("en");
		given(language.getIsocode()).willReturn("de");
		query.setLanguage(language);
		query.setLocale(locale);

		// when
		preprocessor.process(query);

		// then
		verify(i18nService, times(1)).setCurrentLocale(new Locale("en"));
	}

}
