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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.daos.CountryDao;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.servicelayer.i18n.daos.RegionDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


/**
 * Basic test for a {@link DefaultCommonI18NService#getAllLanguages}.
 */
@UnitTest
public class DefaultCommonI18NServiceTest
{
	private CommonI18NService service;

	@Spy
	private final DefaultConversionStrategy strategy = new DefaultConversionStrategy();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		service = new DefaultCommonI18NService();
		((DefaultCommonI18NService) service).setConversionStrategy(strategy);

	}

	/**
	 * Tests String to Locale conversion.
	 */
	@Test
	public void testStringToLocale()
	{
		//correct mappings
		Object[][] locales = new Object[][]
		{
		{ "de", Locale.GERMAN },
		{ "de_DE", Locale.GERMANY },
		{ "de,DE", Locale.GERMANY },
		{ "de-DE", Locale.GERMANY },
		{ "DE-de", Locale.GERMANY },
		{ "de.dE-WIN32", new Locale("de", "dE", "WIN32") },
		{ "DE.dE-WIN32", new Locale("DE", "dE", "WIN32") },
		{ "_DE_win32", new Locale("", "DE", "win32") },
		{ ",DE.WIN32", new Locale("", "DE", "WIN32") } };

		final LanguageModel languageModel = new LanguageModel();
		//and assert each mapping
		for (final Object[] _locale : locales)
		{
			final String isocodes = (String) _locale[0];
			languageModel.setIsocode(isocodes);
			final Locale locale = (Locale) _locale[1];

			final Locale convertedLocale = service.getLocaleForLanguage(languageModel);
			Assert.assertEquals(locale, convertedLocale);
		}


		//incorrect mappings
		locales = new Object[][]
		{
		{ "en", Locale.GERMAN },
		{ "de.DE-WIN32", new Locale("de", "De", "win32") },
		{ "_DE_WIN32", new Locale("", "dE", "Win32") },
		{ ",DE.WIN32", new Locale("", "DE", "qwe") } };

		//and assert each mapping
		for (final Object[] _locale : locales)
		{
			final String isocodes = (String) _locale[0];
			languageModel.setIsocode(isocodes);
			final Locale locale = (Locale) _locale[1];

			final Locale convertedLocale = service.getLocaleForLanguage(languageModel);
			Assert.assertFalse("should not be the same: " + isocodes + "<->" + locale, locale.equals(convertedLocale));
		}

	}

	/**
	 * Tests {@link LanguageModel} to {@link Locale} conversion.
	 */
	@Test
	public void testLanguageToLocale()
	{
		//Mapping: actual language-code to expected Locale instance
		final Object[][] locales = new Object[][]
		{
		{ "de", Locale.GERMAN },
		{ "en", Locale.ENGLISH } };

		//and assert each mapping
		for (final Object[] _locale : locales)
		{
			final String isocodes = (String) _locale[0];
			final Locale locale = (Locale) _locale[1];

			//using a model mock as this test assumes modelconversion works fine
			final LanguageModel language = new LanguageModel();
			language.setIsocode(isocodes);

			final Locale convertedLocale = new Locale(language.getIsocode());
			Assert.assertEquals(locale, convertedLocale);
		}
	}


	/**
	 * Tests {@link LanguageModel} and {@link CountryModel} to {@link Locale} conversion.
	 */
	@Test
	public void testLanguageCountryToLocale()
	{
		//Mapping: actual Language,Country,Variant codes to expected Locale instance
		final Object[][] locales = new Object[][]
		{
		{ "de,,", Locale.GERMAN },
		{ "en,,", Locale.ENGLISH },
		{ "de,de,", Locale.GERMANY },
		{ "de,DE,", Locale.GERMANY },
		{ "de,at,", new Locale("de", "AT") },
		{ "de,ch,", new Locale("de", "CH") },
		{ "de,at,WIN32", new Locale("de", "AT", "WIN32") } };

		for (final Object[] _locale : locales)
		{
			final String[] params = ((String) _locale[0]).split(",", 3);
			final Locale locale = (Locale) _locale[1];

			//using a model mock as this test assumes modelconversion works fine
			//...for languagemodel
			final LanguageModel language = new LanguageModel();
			language.setIsocode(params[0]);

			//...for countrymodel
			final CountryModel country = new CountryModel();
			country.setIsocode(params[1]);

			//...and assert
			final Locale convertedLocale = new Locale(language.getIsocode(), country.getIsocode(), params[2]);
			Assert.assertEquals(locale, convertedLocale);
		}
	}

	@Test
	public void testGetLocaleIsocodeSplitting()
	{
		final LanguageModel languageModel = new LanguageModel();
		languageModel.setIsocode("de,DE,");
		final Locale deloc = service.getLocaleForLanguage(languageModel);
		languageModel.setIsocode("de-DE-");
		Assert.assertTrue("The locale: " + service.getLocaleForLanguage(languageModel) + " should be the same as locale: " + deloc,
				compareLanguageAndCountry(deloc, service.getLocaleForLanguage(languageModel)));
		languageModel.setIsocode("de_DE");
		Assert.assertTrue("The locale: " + service.getLocaleForLanguage(languageModel) + " should be the same as locale: " + deloc,
				compareLanguageAndCountry(deloc, service.getLocaleForLanguage(languageModel)));
		languageModel.setIsocode("de.DE");
		Assert.assertTrue("The locale: " + service.getLocaleForLanguage(languageModel) + " should be the same as locale: " + deloc,
				compareLanguageAndCountry(deloc, service.getLocaleForLanguage(languageModel)));
		languageModel.setIsocode("de,DE.");
		Assert.assertTrue("The locale: " + service.getLocaleForLanguage(languageModel) + " should be the same as locale: " + deloc,
				compareLanguageAndCountry(deloc, service.getLocaleForLanguage(languageModel)));

		languageModel.setIsocode("_");
		final Locale strange1 = service.getLocaleForLanguage(languageModel);
		assertEquals("The locale displayName: " + strange1.getDisplayName() + " should be the same as: _", "_",
				strange1.getDisplayName());
		assertEquals("The locale country: " + strange1.getCountry() + " should be empty!", "", strange1.getCountry());
		assertEquals("The locale variant: " + strange1.getVariant() + " should be empty!", "", strange1.getVariant());
		assertEquals("The locale language: " + strange1.getLanguage() + " should be the same as: _", "_", strange1.getLanguage());
	}

	/**
	 * 
	 */
	private boolean compareLanguageAndCountry(final Locale deloc, final Locale localeForLanguage)
	{
		return deloc.getLanguage().equals(localeForLanguage.getLanguage())
				&& deloc.getCountry().equals(localeForLanguage.getCountry());
	}

	@Test
	public void testGetAllLanguages()
	{
		final LanguageModel one = new LanguageModel();
		one.setIsocode("one");
		final LanguageModel two = new LanguageModel();
		two.setIsocode("two");
		final List<LanguageModel> allLanguages = Arrays.asList(one, two);

		final LanguageDao mockDao = mock(LanguageDao.class);
		((DefaultCommonI18NService) service).setLanguageDao(mockDao);
		when(mockDao.findLanguages()).thenReturn(allLanguages);


		Assert.assertEquals("Language list returned from service.getAllLanguages  should be the same as from dao.findLanguages.",
				service.getAllLanguages(), allLanguages);

		verify(mockDao, times(1)).findLanguages();
	}

	@Test
	public void testGetLanguagesByCode()
	{
		final LanguageModel one = new LanguageModel();
		one.setIsocode("one");
		final LanguageModel two = new LanguageModel();
		two.setIsocode("two");
		final List<LanguageModel> allLanguages = Arrays.asList(one, two);

		final LanguageDao mockDao = mock(LanguageDao.class);
		((DefaultCommonI18NService) service).setLanguageDao(mockDao);

		when(mockDao.findLanguagesByCode("tooSpecific")).thenReturn(Collections.EMPTY_LIST);

		try
		{
			service.getLanguage("tooSpecific");
			Assert.fail("Service should throw an exception if dao returned empty list");
		}
		catch (final UnknownIdentifierException uie)
		{
			//OK
		}
		verify(mockDao, times(1)).findLanguagesByCode("tooSpecific");

		reset(mockDao);
		when(mockDao.findLanguagesByCode("tooCommon")).thenReturn(allLanguages);

		try
		{
			service.getLanguage("tooCommon");
			Assert.fail("Service should throw an exception if dao returned list.size > 1 ");
		}
		catch (final AmbiguousIdentifierException uie)
		{
			//OK
		}
		verify(mockDao, times(1)).findLanguagesByCode("tooCommon");

		reset(mockDao);
		when(mockDao.findLanguagesByCode("one")).thenReturn(Arrays.asList(one));

		Assert.assertEquals("Language  returned from service.getLanguage  should be the same as from dao.findLanguagesByCode.",
				service.getLanguage("one"), one);
		verify(mockDao, times(1)).findLanguagesByCode("one");

	}


	@Test
	public void testGetAllCountries()
	{

		final CountryModel one = new CountryModel();
		one.setIsocode("one");
		final CountryModel two = new CountryModel();
		two.setIsocode("two");
		final List<CountryModel> allCountries = Arrays.asList(one, two);

		final CountryDao mockDao = mock(CountryDao.class);
		((DefaultCommonI18NService) service).setCountryDao(mockDao);
		when(mockDao.findCountries()).thenReturn(allCountries);

		Assert.assertEquals("Country list returned from service.getAllCountries  should be the same as from dao.findCountries.",
				service.getAllCountries(), allCountries);

		verify(mockDao, times(1)).findCountries();
	}

	@Test
	public void testGetCountriesByCode()
	{
		final CountryModel one = new CountryModel();
		one.setIsocode("one");
		final CountryModel two = new CountryModel();
		two.setIsocode("two");
		final List<CountryModel> allCountries = Arrays.asList(one, two);

		final CountryDao mockDao = mock(CountryDao.class);
		((DefaultCommonI18NService) service).setCountryDao(mockDao);

		when(mockDao.findCountriesByCode("tooSpecific")).thenReturn(Collections.EMPTY_LIST);

		try
		{
			service.getCountry("tooSpecific");
			Assert.fail("Service should throw an exception if dao returned empty list");
		}
		catch (final UnknownIdentifierException uie)
		{
			//OK
		}
		verify(mockDao, times(1)).findCountriesByCode("tooSpecific");

		reset(mockDao);
		when(mockDao.findCountriesByCode("tooCommon")).thenReturn(allCountries);

		try
		{
			service.getCountry("tooCommon");
			Assert.fail("Service should throw an exception if dao returned list.size > 1 ");
		}
		catch (final AmbiguousIdentifierException uie)
		{
			//OK
		}
		verify(mockDao, times(1)).findCountriesByCode("tooCommon");

		reset(mockDao);
		when(mockDao.findCountriesByCode("one")).thenReturn(Arrays.asList(one));

		Assert.assertEquals("Country returned from service.getCountry  should be the same as from dao.findCountriesByCode.",
				service.getCountry("one"), one);

		verify(mockDao, times(1)).findCountriesByCode("one");

	}


	@Test
	public void testGetAllRegions()
	{

		final RegionModel one = new RegionModel();
		one.setIsocode("one");
		final RegionModel two = new RegionModel();
		two.setIsocode("two");
		final List<RegionModel> allRegions = Arrays.asList(one, two);

		final RegionDao mockDao = mock(RegionDao.class);
		((DefaultCommonI18NService) service).setRegionDao(mockDao);
		when(mockDao.findRegions()).thenReturn(allRegions);

		Assert.assertEquals("Regions list returned from service.getAllCountries should be the same as from dao.findAllRegions.",
				service.getAllRegions(), allRegions);
		verify(mockDao, times(1)).findRegions();
	}


	@Test
	public void testGetAllRegionsForCountryWithCode()
	{

		final CountryModel modelToFind = new CountryModel();
		modelToFind.setIsocode("woobyland");

		final RegionModel one = new RegionModel();
		one.setIsocode("one");
		final RegionModel two = new RegionModel();
		two.setIsocode("two");
		final List<RegionModel> allRegions = Arrays.asList(one, two);

		final RegionDao mockDao = mock(RegionDao.class);
		((DefaultCommonI18NService) service).setRegionDao(mockDao);

		when(mockDao.findRegionsByCountryAndCode(modelToFind, "tooSpecifc")).thenReturn(Collections.EMPTY_LIST);
		try
		{
			service.getRegion(modelToFind, "tooSpecific");
			Assert.fail("Service should throw an exception if dao returned empty list");
		}
		catch (final UnknownIdentifierException uie)
		{
			//OK
		}
		verify(mockDao, times(1)).findRegionsByCountryAndCode(modelToFind, "tooSpecific");

		reset(mockDao);
		when(mockDao.findRegionsByCountryAndCode(modelToFind, "tooCommon")).thenReturn(allRegions);

		try
		{
			service.getRegion(modelToFind, "tooCommon");
			Assert.fail("Service should throw an exception if dao returned list.size > 1 ");
		}
		catch (final AmbiguousIdentifierException uie)
		{
			//OK
		}
		verify(mockDao, times(1)).findRegionsByCountryAndCode(modelToFind, "tooCommon");

		reset(mockDao);
		when(mockDao.findRegionsByCountryAndCode(modelToFind, "one")).thenReturn(Arrays.asList(one));

		Assert.assertEquals(
				"Regions returned from service.getRegion should be the same as from dao.findAllRegionsByCountryAndCode.",
				service.getRegion(modelToFind, "one"), one);
		verify(mockDao, times(1)).findRegionsByCountryAndCode(modelToFind, "one");

	}

	@Test
	public void testGetAllCurrencies()
	{
		final CurrencyModel currencyOne = new CurrencyModel();
		currencyOne.setIsocode("currencyOne");
		final CurrencyModel currencyTwo = new CurrencyModel();
		currencyTwo.setIsocode("currencyTwo");
		final List<CurrencyModel> allCurrencies = Arrays.asList(currencyOne, currencyTwo);

		final CurrencyDao mockDao = mock(CurrencyDao.class);
		((DefaultCommonI18NService) service).setCurrencyDao(mockDao);
		when(mockDao.findCurrencies()).thenReturn(allCurrencies);


		Assert.assertEquals("Language list returned from service.getAllCurrencies  should be the same as from dao.findCurrencies.",
				service.getAllCurrencies(), allCurrencies);

		verify(mockDao, times(1)).findCurrencies();
	}

	@Test
	public void testGetCurrenciesByCode()
	{
		final CurrencyModel one = new CurrencyModel();
		one.setIsocode("one");
		final CurrencyModel two = new CurrencyModel();
		two.setIsocode("two");
		final List<CurrencyModel> allCurrencies = Arrays.asList(one, two);

		final CurrencyDao mockDao = mock(CurrencyDao.class);
		((DefaultCommonI18NService) service).setCurrencyDao(mockDao);

		when(mockDao.findCurrenciesByCode("tooSpecific")).thenReturn(Collections.EMPTY_LIST);

		try
		{
			service.getCurrency("tooSpecific");
			Assert.fail("Service should throw an exception if dao returned empty list");
		}
		catch (final UnknownIdentifierException uie)
		{
			//OK
		}
		verify(mockDao, times(1)).findCurrenciesByCode("tooSpecific");

		reset(mockDao);
		when(mockDao.findCurrenciesByCode("tooCommon")).thenReturn(allCurrencies);

		try
		{
			service.getCurrency("tooCommon");
			Assert.fail("Service should throw an exception if dao returned list.size > 1 ");
		}
		catch (final AmbiguousIdentifierException uie)
		{
			//OK
		}
		verify(mockDao, times(1)).findCurrenciesByCode("tooCommon");

		reset(mockDao);
		when(mockDao.findCurrenciesByCode("one")).thenReturn(Arrays.asList(one));

		Assert.assertEquals("Currency  returned from service.getCurrency  should be the same as from dao.findCurrencyByCode.",
				service.getCurrency("one"), one);
		verify(mockDao, times(1)).findCurrenciesByCode("one");
	}

	@Test
	public void testGetBaseCurrencyExist()
	{
		final CurrencyModel oneCurrency = new CurrencyModel();
		oneCurrency.setIsocode("oneCurrency");
		oneCurrency.setBase(Boolean.TRUE);
		final List<CurrencyModel> currencyList = new ArrayList<CurrencyModel>();
		currencyList.add(oneCurrency);
		final CurrencyDao mockDao = mock(CurrencyDao.class);
		((DefaultCommonI18NService) service).setCurrencyDao(mockDao);

		when(mockDao.findBaseCurrencies()).thenReturn(currencyList);

		Assert.assertEquals("Currency returned from service.findBaseCurrency should be the same as from dao.findBaseCurrency",
				service.getBaseCurrency(), oneCurrency);
		verify(mockDao, times(1)).findBaseCurrencies();
	}

	@Test(expected = SystemException.class)
	public void testGetBaseCurrencyNoExist()
	{

		final CurrencyDao mockDao = mock(CurrencyDao.class);
		((DefaultCommonI18NService) service).setCurrencyDao(mockDao);

		when(mockDao.findBaseCurrencies()).thenReturn(Collections.EMPTY_LIST);

		service.getBaseCurrency();
		verify(mockDao, times(1)).findBaseCurrencies();
	}

	@Test
	public void testRoundCurrency()
	{
		final double value = 3.14159;
		final double roundValue = service.roundCurrency(value, 2);
		Assert.assertEquals(Double.valueOf(3.14), Double.valueOf(roundValue));

		Mockito.verify(strategy).round(value, 2);
	}

	@Test
	public void testConvertCurrency()
	{
		final double value = 3.14;
		final double convertValue = service.convertCurrency(4, 2, value);
		Assert.assertEquals(Double.valueOf(1.57), Double.valueOf(convertValue));

		Mockito.verify(strategy).convert(value, 4, 2);
	}

	@Test
	public void testRoundAndConvertCurrency()
	{
		final double value = 3.14;
		final double resultValue = service.convertAndRoundCurrency(4, 2, 1, value);
		Assert.assertEquals(Double.valueOf(1.6), Double.valueOf(resultValue));

		Mockito.verify(strategy).convert(value, 4, 2);
		Mockito.verify(strategy).round(1.57, 1);
	}
}
