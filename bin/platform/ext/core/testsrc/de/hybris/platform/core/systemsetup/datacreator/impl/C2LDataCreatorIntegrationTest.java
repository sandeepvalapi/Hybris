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
package de.hybris.platform.core.systemsetup.datacreator.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.junit.Test;


@IntegrationTest
public class C2LDataCreatorIntegrationTest extends ServicelayerTransactionalBaseTest
{
	@Resource(mappedName = "c2lDataCreator")
	private C2LDataCreator creator;


	@Test
	public void shouldPopulateDatabaseWithDefaultLanguageCurrencyCountryCleanupArtificialItemsAndLocalizeOrderStatus()
	{
		// given
		C2LAssert.assertThat("EUR").isNotInSystemAsCurrency();
		C2LAssert.assertThat("---").isInSystemAsCurrency();

		// when
		creator.populateDatabase();

		// then
		C2LAssert.assertThat("EUR").isInSystemAsCurrency();
		C2LAssert.assertThat("---").isNotInSystemAsCurrency();
	}


	@Test
	public void shouldThrowIllegalArgumentExceptionWhenLangIsoCodeIsNull()
	{
		// given
		final String isoCode = null;

		try
		{
			// when
			creator.createOrGetLanguage(isoCode, true);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("isoCode is required");
		}
	}


	@Test
	public void shouldCreateActiveLanguageIfDoesNotExist()
	{
		// given
		final String isoCode = "pl";
		C2LAssert.assertThat(isoCode).isNotInSystemAsLanguage();

		// when
		final Language language = creator.createOrGetLanguage(isoCode, true);

		// then
		assertThat(language).isNotNull();
		assertThat(language.getIsocode()).isEqualTo("pl");
		assertThat(language.isActive()).isTrue();
	}

	@Test
	public void shouldCreateInActiveLanguageIfDoesNotExist()
	{
		// given
		final String isoCode = "pl";
		C2LAssert.assertThat(isoCode).isNotInSystemAsLanguage();

		// when
		final Language language = creator.createOrGetLanguage(isoCode, false);

		// then
		assertThat(language).isNotNull();
		assertThat(language.getIsocode()).isEqualTo("pl");
		assertThat(language.isActive()).isFalse();
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenCountryIsoCodeIsNull()
	{
		// given
		final String isoCode = null;

		try
		{
			// when
			creator.createOrGetCountry(isoCode, true);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("isoCode is required");
		}
	}


	@Test
	public void shouldCreateActiveCountryIfDoesNotExist()
	{
		// given
		final String isoCode = "pl";
		C2LAssert.assertThat(isoCode).isNotInSystemAsCountry();

		// when
		final Country country = creator.createOrGetCountry(isoCode, true);

		// then
		assertThat(country).isNotNull();
		assertThat(country.getIsocode()).isEqualTo("pl".toUpperCase());
		assertThat(country.isActive()).isTrue();
	}

	@Test
	public void shouldCreateInActiveCountryIfDoesNotExist()
	{
		// given
		final String isoCode = "pl";
		C2LAssert.assertThat(isoCode).isNotInSystemAsCountry();

		// when
		final Country country = creator.createOrGetCountry(isoCode, false);

		// then
		assertThat(country).isNotNull();
		assertThat(country.getIsocode()).isEqualTo("pl".toUpperCase());
		assertThat(country.isActive()).isFalse();
	}


	@Test
	public void shouldCreateActiveNotBaseCurrencyIfDoesNotExist()
	{
		// given
		final String isoCode = "PLN";
		final String symbol = "PLN";

		// when
		final Currency currency = creator.createOrGetCurrency(isoCode, symbol);

		// then
		assertThat(currency).isNotNull();
		assertThat(currency.getIsocode()).isEqualTo("PLN");
		assertThat(currency.getSymbol()).isEqualTo("PLN");
		assertThat(currency.isActive()).isTrue();
		assertThat(currency.isBase()).isFalse();
	}

	private static class C2LAssert extends GenericAssert<C2LAssert, String>
	{
		private final C2LManager c2lManager = C2LManager.getInstance();

		public C2LAssert(final String actual)
		{
			super(C2LAssert.class, actual);
		}

		public static C2LAssert assertThat(final String actual)
		{
			return new C2LAssert(actual);
		}

		public C2LAssert isNotInSystemAsLanguage()
		{
			try
			{
				c2lManager.getLanguageByIsoCode(actual);
				fail("Language with isoCode: " + actual + " exist!");
			}
			catch (final JaloItemNotFoundException e)
			{
				// Fine
			}
			return this;
		}

		public C2LAssert isInSystemAsLanguage()
		{
			try
			{
				final Language language = c2lManager.getLanguageByIsoCode(actual);
				Assertions.assertThat(language).isNotNull();
				Assertions.assertThat(language.getIsocode()).isEqualTo(actual);
			}
			catch (final JaloItemNotFoundException e)
			{
				fail("currency with isoCode: " + actual + " does not exist!");
			}
			return this;
		}

		public C2LAssert isNotInSystemAsCurrency()
		{
			try
			{
				c2lManager.getCurrencyByIsoCode(actual);
				fail("currency with isoCode: " + actual + " exist!");
			}
			catch (final JaloItemNotFoundException e)
			{
				// Fine
			}
			return this;
		}

		public C2LAssert isInSystemAsCurrency()
		{
			try
			{
				final Currency currency = c2lManager.getCurrencyByIsoCode(actual);
				Assertions.assertThat(currency).isNotNull();
				Assertions.assertThat(currency.getIsocode()).isEqualTo(actual);
			}
			catch (final JaloItemNotFoundException e)
			{
				fail("currency with isoCode: " + actual + " does not exist!");
			}
			return this;
		}

		public C2LAssert isNotInSystemAsCountry()
		{
			try
			{
				c2lManager.getCountryByIsoCode(actual.toUpperCase());
				fail("Country with isoCode: " + actual + " exist!");
			}
			catch (final JaloItemNotFoundException e)
			{
				// Fine
			}
			return this;
		}

		public C2LAssert isInSystemAsCountry()
		{
			try
			{
				final Country country = c2lManager.getCountryByIsoCode(actual.toUpperCase());
				Assertions.assertThat(country).isNotNull();
				Assertions.assertThat(country.getIsocode()).isEqualTo(actual.toUpperCase());
			}
			catch (final JaloItemNotFoundException e)
			{
				fail("country with isoCode: " + actual + " does not exist!");
			}
			return this;
		}
	}

}
