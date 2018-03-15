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

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ConfigurationException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class I18NServiceTest extends ServicelayerTransactionalBaseTest
{
	private static final Logger LOG = Logger.getLogger(I18NServiceTest.class);

	private static final int LANGUAGES_NUMBER = 2;
	private static final int JAVA_CURRENCY_SIZE = 2;
	private static final int MODEL_CURRENCY_SIZE = 3;

	@Resource
	private final I18NService i18nService = null;
	@Resource
	private final ModelService modelService = null;
	@Resource
	private final CommonI18NService commonI18NService = null;
	@Resource
	private final L10NService l10nService = null;

	@Before
	public void setUp()
	{
		new CoreBasicDataCreator().createBasicC2L();

		final CurrencyModel cur = new CurrencyModel();
		modelService.initDefaults(cur);
		cur.setIsocode("CHF");
		cur.setActive(Boolean.TRUE);
		cur.setName("Schweizer Franken", Locale.GERMANY);
		cur.setName("Swiss franc", Locale.UK);
		modelService.save(cur);

		cur.toString();
	}

	@Test
	public void testSystemCurrentLocaleFunctionality()
	{
		assertEquals("Wrong number of system languages. Should be: <" + LANGUAGES_NUMBER + "> but was: <"
				+ i18nService.getSupportedLocales().size() + ">", LANGUAGES_NUMBER, i18nService.getSupportedLocales().size());

		for (final Locale locale : i18nService.getSupportedLocales())
		{
			i18nService.setCurrentLocale(locale);
			final Locale currentLocale = i18nService.getCurrentLocale();
			assertEquals(
					"Wrong system current locale! Expected is: " + locale.getLanguage() + " but was: " + currentLocale.getLanguage(),
					locale.getLanguage(), currentLocale.getLanguage());
		}

		try
		{
			i18nService.setCurrentLocale(new Locale("pl"));
			fail("expected IllegalArgumentException using wrong locale which doesn't exist within the system.");
		}
		catch (final IllegalArgumentException iae)
		{
			//fine
		}
	}

	@Test
	public void testSystemCurrencyFunctionality()
	{
		//1) get supported java and model currencies

		int currencySize = i18nService.getSupportedJavaCurrencies().size();
		int currencyModelSize = commonI18NService.getAllCurrencies().size();
		assertEquals("The java currency list and model currency list are not the same size!", currencySize, currencyModelSize);
		assertEquals("The java currency list and model currency list have the wrong size!", JAVA_CURRENCY_SIZE, currencyModelSize);

		final CurrencyModel invalidJavaCurrency = new CurrencyModel();
		modelService.initDefaults(invalidJavaCurrency);
		invalidJavaCurrency.setIsocode("notExistingCurrency");
		invalidJavaCurrency.setSymbol("zzz");
		modelService.save(invalidJavaCurrency);

		currencySize = i18nService.getSupportedJavaCurrencies().size();
		currencyModelSize = commonI18NService.getAllCurrencies().size();
		assertEquals("The wrong size of supported java currency list!", JAVA_CURRENCY_SIZE, currencySize);
		assertEquals("The wrong size of supported model currency list!", MODEL_CURRENCY_SIZE, currencyModelSize);

		//2) set/get current currency

		//setting invalid currency(from java currency point of view)
		commonI18NService.setCurrentCurrency(invalidJavaCurrency);
		try
		{
			//exception, such currency doesn't exist on the ISO 4217 currency list.
			i18nService.getCurrentJavaCurrency();
			fail("expected ConfigurationException!");
		}
		catch (final ConfigurationException ce)
		{
			//fine
		}
		//for getting such currency we have to use model based i18n service
		commonI18NService.getCurrentCurrency();

		try
		{
			//exception, such currency doesn't exist within hybris system.
			i18nService.setCurrentJavaCurrency(Currency.getInstance("CAD"));
			fail("expected ConfigurationException!");
		}
		catch (final ConfigurationException ce)
		{
			//fine
		}

		//create CAD currency within hybris system
		final CurrencyModel cadCurrency = new CurrencyModel();
		cadCurrency.setIsocode("CAD");
		modelService.initDefaults(cadCurrency);
		modelService.save(cadCurrency);

		//now we are able to set current currency as a java currency
		i18nService.setCurrentJavaCurrency(Currency.getInstance("CAD"));

		modelService.remove(invalidJavaCurrency);
		modelService.remove(cadCurrency);
	}

	@Test
	public void testCurrentLanguageMethods()
	{
		final LanguageModel language = modelService.create(LanguageModel.class);
		language.setIsocode("pl");
		modelService.save(language);

		commonI18NService.setCurrentLanguage(language);
		final LanguageModel sessionLanguage = commonI18NService.getCurrentLanguage();

		assertEquals("Wrong session language isocode! Should be: '" + language.getIsocode() + "' but was: '" + sessionLanguage
				+ "'.", language.getIsocode(), sessionLanguage.getIsocode());
	}

	/**
	 * Updates the session Locale various time.<br/>
	 * Tests whether sessions {@link LanguageModel} and {@link CountryModel} are compatible.
	 */
	@Test
	public void testUpdateLocale()
	{
		i18nService.setCurrentLocale(Locale.GERMAN);
		this.assertSessionLocale(Locale.GERMAN);

		i18nService.setCurrentLocale(Locale.GERMANY);
		this.assertSessionLocale(Locale.GERMANY);

		i18nService.setCurrentLocale(Locale.ENGLISH);
		this.assertSessionLocale(Locale.ENGLISH);
	}

	/**
	 * Updates session {@link LanguageModel} and/or session {@link CountryModel} various times.<br/>
	 * Tests whether session {@link Locale} is compatible.
	 */
	@Test
	public void testUpdateLanguageAndCountry()
	{
		final String newLangIso = "de";
		final String newCountryIso = "DE";
		final Locale expectedLocale = Locale.GERMANY;

		final LanguageModel language = getCommonI18NService().getLanguage(newLangIso);
		final CountryModel country = getCommonI18NService().getCountry(newCountryIso);


		//set a calculated Locale from language/country
		final Locale locale = new Locale(language.getIsocode(), country.getIsocode());
		Assert.assertEquals(expectedLocale, locale);
		getI18nService().setCurrentLocale(locale);
		assertSessionLocale(expectedLocale);


		//set 'null' Locale is not allowed
		try
		{
			getI18nService().setCurrentLocale(null);
			Assert.fail("Expected an IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			//nop
		}
	}

	@Test
	public void testUpdateJaloSession()
	{
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();

		getI18nService().setCurrentLocale(Locale.GERMAN);
		Assert.assertEquals("de", ctx.getLanguage().getIsoCode());

		getI18nService().setCurrentLocale(Locale.ENGLISH);
		Assert.assertEquals("en", ctx.getLanguage().getIsoCode());

		getI18nService().setCurrentLocale(Locale.GERMANY);
		Assert.assertEquals("de", ctx.getLanguage().getIsoCode());

	}

	/**
	 * Internal.<br/>
	 * Assert whether session Locale is compatible with session Language and Country.
	 */
	private void assertSessionLocale(final Locale expectedLocale)
	{
		final Locale sessionLocale = i18nService.getCurrentLocale();
		final LanguageModel language = getCommonI18NService().getLanguage(sessionLocale.getLanguage());

		assertEquals(expectedLocale, sessionLocale);
		assertEquals(language.getIsocode(), sessionLocale.getLanguage());
	}


	@Test
	public void testMiscUseCases()
	{

		//
		// test models
		//

		//change session locale to de
		LOG.debug("Set session locale " + Locale.GERMAN);
		getI18nService().setCurrentLocale(Locale.GERMAN);
		final CurrencyModel cur1 = getCommonI18NService().getCurrency("CHF");
		//...assert german localization
		Assert.assertEquals("Schweizer Franken", cur1.getName());

		//change session locale to en
		LOG.debug("Set session locale " + Locale.ENGLISH);
		getI18nService().setCurrentLocale(Locale.ENGLISH);
		Assert.assertSame(cur1, getCommonI18NService().getCurrency("CHF"));
		//...both must show english name
		Assert.assertEquals("Swiss franc", cur1.getName());

		cur1.setName("foo");

		Assert.assertEquals("foo", cur1.getName());

		getI18nService().setCurrentLocale(Locale.GERMAN);
		Assert.assertEquals("Schweizer Franken", cur1.getName());

		//current reload behavior doesn't touch the model's language (can be discussed/changed)
		modelService.refresh(cur1);

		Assert.assertEquals("Schweizer Franken", cur1.getName());

		getI18nService().setCurrentLocale(Locale.ENGLISH);

		Assert.assertEquals("Swiss franc", cur1.getName());

		//
		// test resourcebundles (general)
		//

		ResourceBundle bundle = null;

		//check whether bundles are delivered according current session locale
		getI18nService().setCurrentLocale(Locale.ENGLISH);
		bundle = l10nService.getResourceBundle("servicelayer.test.testBundle");
		Assert.assertEquals("save", bundle.getString("action.mem"));
		getI18nService().setCurrentLocale(Locale.UK);
		bundle = l10nService.getResourceBundle("servicelayer.test.testBundle");
		Assert.assertEquals("memorise", bundle.getString("action.mem"));
		getI18nService().setCurrentLocale(Locale.US);
		bundle = l10nService.getResourceBundle("servicelayer.test.testBundle");
		Assert.assertEquals("memorize", bundle.getString("action.mem"));



		//
		// test resourcebundles (fallback)
		//

		//manually add en as fallback to de
		final LanguageModel _de = getCommonI18NService().getLanguage("de");
		final LanguageModel _en = getCommonI18NService().getLanguage("en");
		_de.setFallbackLanguages(Arrays.asList(_en));

		getI18nService().setCurrentLocale(Locale.GERMAN);

		bundle = l10nService.getResourceBundle("servicelayer.test.testBundle");
		//available in de
		Assert.assertEquals("Speichern", bundle.getString("action.save"));
		//fallback to en (empty localization)

		//YTODO does not work at the moment
		// Assert.assertEquals("save", bundle.getString("action.mem"));
		// Assert.assertEquals("noKeyFallback", bundle.getString("test.fallback.noKey"));
		// Assert.assertEquals("noValueFallback", bundle.getString("test.fallback.noValue"));


	}


	/**
	 * Not really Test, just a demonstration how java I18N works.
	 */
	public void demonstrateFormatters()
	{
		this.demonstrateFormatters(Locale.GERMAN); //german speaking
		this.demonstrateFormatters(Locale.GERMANY); //german speaking living in germany
		this.demonstrateFormatters(Locale.ENGLISH); //english speaking
		this.demonstrateFormatters(Locale.UK); //english speaking living in great britain
		this.demonstrateFormatters(Locale.US); //english speaking living in united states
	}

	/**
	 * Not really Test, just a demonstration how java I18N works.
	 */
	private void demonstrateFormatters(final Locale locale)
	{
		LOG.info("Locale sensitive operations for " + locale);

		//print a localized date
		//needs Language
		final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale);
		final Date date = new Date(System.currentTimeMillis());
		LOG.info("DateFormat(full): " + df.format(date));

		//print a localized number
		//needs Language
		final NumberFormat nf = NumberFormat.getInstance(locale);
		LOG.info("NumberFormat(number): " + nf.format(1000000));

		//print a localized currency
		//needs Language and Country
		final NumberFormat cf = NumberFormat.getCurrencyInstance(locale);
		LOG.info("NumberFormat(currency): " + cf.format(100));

		//combine messageformat and resourcebundle to print a localized date
		//needs Lamguage and Country
		final ResourceBundle bundle = ResourceBundle.getBundle("servicelayer.test.testBundle", locale);
		final String value = bundle.getString("hybris.date");
		final MessageFormat mf = new MessageFormat(value, locale);
		final StringBuffer msg = mf.format(new Object[]
		{ new Date() }, new StringBuffer(), null);
		LOG.info("MessageFormat: " + msg);

		LOG.info(" ");
	}

	/**
	 * @return the i18nService
	 */
	public I18NService getI18nService()
	{
		return i18nService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

}
