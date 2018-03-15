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
package de.hybris.platform.servicelayer.i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.exceptions.ConfigurationException;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.i18n.impl.DefaultI18NService;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.servicelayer.internal.i18n.impl.DefaultLocalizationService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * The unit tests for i18NService.
 * <p>
 * hint: after removing {@link LocalizationService} the tests will require corrections, because the logic from
 * {@link DefaultLocalizationService} methods will be moved to the {@link DefaultI18NService}.
 * <p>
 * Currently the content of the {@link DefaultLocalizationService} methods are not migrated to the servicelayer.
 */
@UnitTest
public class DefaultI18NServiceTest
{

	private static final int NUMBER_OF_ELEMENTS = 2;
	private static final int NUMBER_OF_CORRECT_CURRENCIES = 1;
	private static final int NUMBER_OF_ALL_LOCALES = 3;
	private static final int NUMBER_OF_FALLBACK_LOCALES = 2;

	@InjectMocks
	private I18NService i18NService;
	@Mock
	private SessionService sessionService;
	@Mock
	private LocalizationService localizationService;
	@Mock
	private CurrencyDao currencyDao;

	@Before
	public void setUp()
	{
		i18NService = new DefaultI18NService();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetCurrentLocale()
	{
		final Locale locale = new Locale("de");
		when(localizationService.getCurrentLocale()).thenReturn(locale);

		final Locale currentLocale = i18NService.getCurrentLocale();
		assertEquals(
				"Wrong current locale isocode! Should be: '" + locale.getLanguage() + "' but was: '" + currentLocale.getLanguage()
						+ "'.", locale.getLanguage(), currentLocale.getLanguage());

		verify(localizationService, times(1)).getCurrentLocale();
	}

	@Test
	public void testSetCurrentLocale() //NOPMD the assert(), fail() methods do not occur because the localizationService.setCurrentLocale doing all the work.
	{
		final Locale locale = new Locale("de");
		i18NService.setCurrentLocale(locale);

		verify(localizationService, times(1)).setCurrentLocale(locale);
	}

	@Test
	public void testGetSupportedLocales()
	{
		final Set<Locale> locales = new HashSet<Locale>();
		locales.add(new Locale("de"));
		locales.add(new Locale("en"));

		when(localizationService.getSupportedDataLocales()).thenReturn(locales);

		final Set<Locale> supportesLocales = i18NService.getSupportedDataLocales();

		assertEquals("Wrong number of supported locales! Should be: '" + locales.size() + "' but was: '" + supportesLocales.size()
				+ "'.", locales.size(), supportesLocales.size());
		assertEquals(
				"Wrong number of supported locales! Should be: '" + NUMBER_OF_ELEMENTS + "' but was: '" + supportesLocales.size()
						+ "'.", NUMBER_OF_ELEMENTS, supportesLocales.size());

		final Iterator<Locale> itLocales = locales.iterator();
		final Iterator<Locale> itSupportesLocales = supportesLocales.iterator();

		while (itLocales.hasNext())
		{
			final Locale tempLocale = itLocales.next();
			final Locale tempSupportesLocale = itSupportesLocales.next();
			assertEquals("Wrong current locale isocode! Should be: '" + tempLocale + "' but was: '" + tempSupportesLocale + "'.",
					tempLocale, tempSupportesLocale);
		}

		verify(localizationService, times(1)).getSupportedDataLocales();
	}

	@Test
	public void testGetSupportedJavaCurrencies()
	{
		final List<CurrencyModel> currencies = new ArrayList<CurrencyModel>();
		final CurrencyModel currency1 = new CurrencyModel();
		currency1.setIsocode("USD");

		final CurrencyModel currency2 = new CurrencyModel();
		currency2.setIsocode("EUR");
		currencies.add(currency1);
		currencies.add(currency2);

		when(currencyDao.findCurrencies()).thenReturn(currencies);

		final List<Currency> supportedJavaCurrencies = new ArrayList<Currency>(i18NService.getSupportedJavaCurrencies());

		assertEquals("Wrong number of supported java currencies! Should be: '" + currencies.size() + "' but was: '"
				+ supportedJavaCurrencies.size() + "'.", currencies.size(), supportedJavaCurrencies.size());
		assertEquals("Wrong number of supported java currencies! Should be: '" + NUMBER_OF_ELEMENTS + "' but was: '"
				+ supportedJavaCurrencies.size() + "'.", NUMBER_OF_ELEMENTS, supportedJavaCurrencies.size());

		assertEquals("Wrong current java currency isocode! Should be: '" + currencies.get(0).getIsocode() + "' but was: '"
				+ supportedJavaCurrencies.get(0).getCurrencyCode() + "'.", currencies.get(0).getIsocode(), supportedJavaCurrencies
				.get(0).getCurrencyCode());
		assertEquals("Wrong current java currency isocode! Should be: '" + currencies.get(1).getIsocode() + "' but was: '"
				+ supportedJavaCurrencies.get(1).getCurrencyCode() + "'.", currencies.get(1).getIsocode(), supportedJavaCurrencies
				.get(1).getCurrencyCode());

		verify(currencyDao, times(1)).findCurrencies();
	}

	@Test
	public void testGetSupportedJavaCurrenciesWithOneNonExistentCurrency()
	{
		final List<CurrencyModel> currencies = new ArrayList<CurrencyModel>();
		final CurrencyModel currency1 = new CurrencyModel();
		currency1.setIsocode("USD");

		final CurrencyModel currency2 = new CurrencyModel();
		currency2.setIsocode("BLABLA");
		currencies.add(currency1);
		currencies.add(currency2);

		when(currencyDao.findCurrencies()).thenReturn(currencies);

		final List<Currency> supportedJavaCurrencies = new ArrayList<Currency>(i18NService.getSupportedJavaCurrencies());

		assertEquals("Wrong number of supported java currencies! Should be: '" + NUMBER_OF_CORRECT_CURRENCIES + "' but was: '"
				+ supportedJavaCurrencies.size() + "'.", NUMBER_OF_CORRECT_CURRENCIES, supportedJavaCurrencies.size());

		assertEquals("Wrong current java currency isocode! Should be: '" + currencies.get(0).getIsocode() + "' but was: '"
				+ supportedJavaCurrencies.get(0).getCurrencyCode() + "'.", currencies.get(0).getIsocode(), supportedJavaCurrencies
				.get(0).getCurrencyCode());

		verify(currencyDao, times(1)).findCurrencies();
	}

	@Test
	public void testGetCurrentTimeZone()
	{
		final TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
		when(sessionService.getAttribute(I18NConstants.TIMEZONE_SESSION_ATTR_KEY)).thenReturn(timeZone);

		final TimeZone currentTimeZone = i18NService.getCurrentTimeZone();
		assertEquals("Wrong current time zone ID! Should be: '" + timeZone.getID() + "' but was: '" + currentTimeZone.getID()
				+ "'.", timeZone.getID(), currentTimeZone.getID());

		verify(sessionService, times(1)).getAttribute(Mockito.anyString());
	}

	@Test
	public void testSetCurrentTimeZone() //NOPMD the assert(), fail() methods do not occur because the sessionService.setAttribute doing all the work
	{
		final TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
		i18NService.setCurrentTimeZone(timeZone);

		verify(sessionService, times(1)).setAttribute(I18NConstants.TIMEZONE_SESSION_ATTR_KEY, timeZone);
	}

	@Test
	public void testGetCurrentJavaCurrency()
	{
		final CurrencyModel currency1 = new CurrencyModel();
		currency1.setIsocode("USD");

		when(sessionService.getAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY)).thenReturn(currency1);
		final Currency currentJavaCurrency = i18NService.getCurrentJavaCurrency();

		assertEquals("Wrong current java currency isocode! Should be: '" + currency1.getIsocode() + "' but was: '"
				+ currentJavaCurrency.getCurrencyCode() + "'.", currency1.getIsocode(), currentJavaCurrency.getCurrencyCode());

		verify(sessionService, times(1)).getAttribute(Mockito.anyString());
	}

	@Test
	public void testGetCurrentJavaCurrencyWithNonExistentJavaCurrency()
	{
		final CurrencyModel currency1 = new CurrencyModel();
		currency1.setIsocode("BLABLA");

		when(sessionService.getAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY)).thenReturn(currency1);
		try
		{
			i18NService.getCurrentJavaCurrency();
			fail();
		}
		catch (final ConfigurationException e)
		{
			//ok
		}

		verify(sessionService, times(1)).getAttribute(Mockito.anyString());
	}

	@Test
	public void testSetCurrentJavaCurrency() //NOPMD the assert(), fail() methods do not occur because the sessionService.setAttribute doing all the work
	{
		final CurrencyModel currency1 = new CurrencyModel();
		currency1.setIsocode("USD");
		final List<CurrencyModel> currencyList = new ArrayList<CurrencyModel>(Collections.singletonList(currency1));

		when(currencyDao.findCurrenciesByCode("USD")).thenReturn(currencyList);
		Currency.getInstance("USD");
		i18NService.setCurrentJavaCurrency(Currency.getInstance("USD"));

		verify(currencyDao, times(1)).findCurrenciesByCode("USD");
		verify(sessionService, times(1)).setAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY, currency1);
	}

	@Test
	public void testIsLocalizationFallbackEnabled()
	{
		//both attributes are set to true
		when(sessionService.getAttribute(AbstractItemModel.LANGUAGE_FALLBACK_ENABLED_SERVICE_LAYER)).thenReturn(Boolean.TRUE);
		when(sessionService.getAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED)).thenReturn(Boolean.TRUE);
		assertTrue("The localization fallback mechanism should be enabled!", i18NService.isLocalizationFallbackEnabled());
		verify(sessionService, times(2)).getAttribute(Mockito.anyString());

		Mockito.reset(sessionService);

		//servicelayer attribute is set to false
		when(sessionService.getAttribute(AbstractItemModel.LANGUAGE_FALLBACK_ENABLED_SERVICE_LAYER)).thenReturn(Boolean.FALSE);
		when(sessionService.getAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED)).thenReturn(Boolean.TRUE);
		assertFalse("The localization fallback mechanism should be disabled!", i18NService.isLocalizationFallbackEnabled());
		verify(sessionService, times(1)).getAttribute(Mockito.anyString());

		Mockito.reset(sessionService);

		//jalo attribute is set to false
		when(sessionService.getAttribute(AbstractItemModel.LANGUAGE_FALLBACK_ENABLED_SERVICE_LAYER)).thenReturn(Boolean.TRUE);
		when(sessionService.getAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED)).thenReturn(Boolean.FALSE);
		assertFalse("The localization fallback mechanism should be disabled!", i18NService.isLocalizationFallbackEnabled());
		verify(sessionService, times(2)).getAttribute(Mockito.anyString());
	}

	@Test
	public void testSetLocalizationFallbackEnabled() //NOPMD the assert(), fail() methods do not occur because the sessionService.setAttribute doing all the work.
	{
		i18NService.setLocalizationFallbackEnabled(true);
		verify(sessionService, times(1)).setAttribute(AbstractItemModel.LANGUAGE_FALLBACK_ENABLED_SERVICE_LAYER, Boolean.TRUE);
		verify(sessionService, times(1)).setAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
	}

	@Test
	public void testGetBestMatchingLocale()
	{
		final Locale locale = new Locale("de");
		when(localizationService.getDataLocale(locale)).thenReturn(locale);

		final Locale bestMatchingLocale = i18NService.getBestMatchingLocale(locale);
		assertEquals("Wrong best matching locale isocode! Should be: '" + locale.getLanguage() + "' but was: '"
				+ bestMatchingLocale.getLanguage() + "'.", locale.getLanguage(), bestMatchingLocale.getLanguage());

		verify(localizationService, times(1)).getDataLocale(locale);

	}

	@Test
	public void testGetAllLocales()
	{
		final Locale locale1 = new Locale("de");
		//fallback languages
		final Locale locale2 = new Locale("en");
		final Locale locale3 = new Locale("pl");

		when(localizationService.getAllLocales(locale1)).thenReturn((Locale[]) Arrays.asList(locale1, locale2, locale3).toArray());

		final Locale[] allLocales = i18NService.getAllLocales(locale1);
		assertEquals("Wrong number of all locales for: '" + locale1.getLanguage() + "'! Should be: '" + NUMBER_OF_ALL_LOCALES
				+ "' but was: '" + allLocales.length + "'.", NUMBER_OF_ALL_LOCALES, allLocales.length);

		verify(localizationService, times(1)).getAllLocales(locale1);
	}

	@Test
	public void testGetFallbackLocales()
	{
		final Locale locale1 = new Locale("de");
		//fallback languages
		final Locale locale2 = new Locale("en");
		final Locale locale3 = new Locale("pl");

		when(localizationService.getFallbackLocales(locale1)).thenReturn((Locale[]) Arrays.asList(locale2, locale3).toArray());

		final Locale[] fallbackLocales = i18NService.getFallbackLocales(locale1);
		assertEquals("Wrong number of all locales for: '" + locale1.getLanguage() + "'! Should be: '" + NUMBER_OF_FALLBACK_LOCALES
				+ "' but was: '" + fallbackLocales.length + "'.", NUMBER_OF_FALLBACK_LOCALES, fallbackLocales.length);

		verify(localizationService, times(1)).getFallbackLocales(locale1);
	}

}
