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
package de.hybris.platform.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class C2LManagerTest extends HybrisJUnit4TransactionalTest
{
	private C2LManager c2lm = null;
	private Language lang = null;
	private Currency curr1 = null;
	private Currency curr2 = null;
	private Country c1 = null;
	private Country c2 = null;
	private Region r1 = null;
	private Region r2 = null;
	static private final String langIsoCode = "lang1";
	static private final String curr1IsoCode = "curr1";
	static private final String curr2IsoCode = "curr2";
	static private final String country1IsoCode = "country1";
	static private final String country2IsoCode = "country2";
	static private final String regionCode = "region";
	static private final String region1Code = regionCode + "1";
	static private final String region2Code = regionCode + "2";

	private int existingCurrenciesCount = 0;
	private int existingActiveCurrenciesCount = 0;

	private int existingLanguagesCount = 0;
	private int existingActiveLanguagesCount = 0;

	private int existingCountriesCount = 0;
	private int existingActiveCountriesCount = 0;

	private int existingRegionsCount = 0;
	private int existingActiveRegionsCount = 0;

	@Before
	public void setUp() throws Exception
	{
		c2lm = jaloSession.getC2LManager();

		existingCurrenciesCount = c2lm.getAllCurrencies().size();
		existingActiveCurrenciesCount = c2lm.getActiveCurrencies().size();

		existingLanguagesCount = c2lm.getAllLanguages().size();
		existingActiveLanguagesCount = c2lm.getActiveLanguages().size();

		existingCountriesCount = c2lm.getAllCountries().size();
		existingActiveCountriesCount = c2lm.getActiveCountries().size();

		existingRegionsCount = c2lm.getAllRegions().size();
		existingActiveRegionsCount = c2lm.getActiveRegions().size();

		assertNotNull(lang = c2lm.createLanguage(langIsoCode));
		lang.setActive(false);

		assertNotNull(curr1 = c2lm.createCurrency(curr1IsoCode));
		assertNotNull(curr2 = c2lm.createCurrency(curr2IsoCode));
		curr1.setActive(true);
		curr1.setBase();
		curr2.setActive(false);

		assertNotNull(c1 = c2lm.createCountry(country1IsoCode));
		assertNotNull(c2 = c2lm.createCountry(country2IsoCode));
		c1.setActive(false);
		c2.setActive(false);

		assertNotNull(r1 = c1.addNewRegion(region1Code));
		assertNotNull(r2 = c2.addNewRegion(region2Code));
		r1.setActive(true);
		r2.setActive(false);
	}

	@Test
	public void testGetLanguageByIsoCode() throws Exception
	{
		try
		{
			c2lm.getLanguageByIsoCode(langIsoCode + "X");
			fail();
		}
		catch (final JaloItemNotFoundException e)
		{
			//ok here
		}

		final Language language = c2lm.getLanguageByIsoCode(langIsoCode);
		assertEquals(lang, language);
		assertEquals(langIsoCode, language.getIsoCode());
		assertEquals(langIsoCode, lang.getIsoCode());

		language.setIsoCode(langIsoCode + "X");
		assertEquals(langIsoCode + "X", language.getIsoCode());

		final Language language2 = c2lm.getLanguageByIsoCode(langIsoCode + "X");
		assertEquals(langIsoCode + "X", language2.getIsoCode());
	}

	@Test
	public void getAllLanguages() throws Exception
	{
		final Collection coll = c2lm.getAllLanguages();
		assertEquals(1 + existingLanguagesCount, coll.size());
	}

	@Test
	public void getActiveLanguages() throws Exception
	{
		final Collection coll = c2lm.getActiveLanguages();
		assertEquals(0 + existingActiveLanguagesCount, coll.size());
		final Language lang = (Language) coll.iterator().next();
		assertTrue(lang.getIsoCode() != langIsoCode);
		assertTrue(lang.isActive().booleanValue());
	}

	@Test
	public void getCurrencyByIsoCode() throws Exception
	{
		final Currency curr = c2lm.getCurrencyByIsoCode(curr1IsoCode);
		assertEquals(curr1, curr);
	}

	@Test
	public void getAllCurrencies() throws Exception
	{
		final Collection coll = c2lm.getAllCurrencies();
		assertEquals(2 + existingCurrenciesCount, coll.size());
	}

	@Test
	public void getActiveCurrencies() throws Exception
	{
		final Collection coll = c2lm.getActiveCurrencies();
		assertEquals(1 + existingActiveCurrenciesCount, coll.size());
		assertTrue(coll.contains(curr1));
		assertTrue(curr1.isActive().booleanValue());
	}

	@Test
	public void getBaseCurrency() throws Exception
	{
		final Currency curr = c2lm.getBaseCurrency();
		assertEquals(curr1, curr);
	}

	@Test
	public void getCountryByIsoCode() throws Exception
	{
		final Country c = c2lm.getCountryByIsoCode(country1IsoCode);
		assertEquals(c1, c);
	}

	@Test
	public void getAllCountries() throws Exception
	{
		final Collection coll = c2lm.getAllCountries();
		assertEquals(2 + existingCountriesCount, coll.size());
	}

	@Test
	public void getActiveCountries() throws Exception
	{
		final Collection coll = c2lm.getActiveCountries();
		assertEquals(0 + existingActiveCountriesCount, coll.size());
	}

	@Test
	public void getRegionByCode() throws Exception
	{
		final Region r = c2lm.getRegionByCode(c1, region1Code);
		assertEquals(r1, r);
	}

	@Test
	public void getRegions() throws Exception
	{
		final Collection coll = c1.getRegions();
		assertEquals(1, coll.size());
		assertEquals(r1, coll.iterator().next());
	}

	@Test
	public void getAllRegions() throws Exception
	{
		final Collection coll = c2lm.getAllRegions();
		assertEquals(2 + existingRegionsCount, coll.size());
	}

	@Test
	public void getActiveRegions() throws Exception
	{
		final Collection coll = c2lm.getActiveRegions();
		assertEquals(1 + existingActiveRegionsCount, coll.size());
		final Region r = (Region) coll.iterator().next();
		assertEquals(r1, r);
		assertTrue(r.isActive().booleanValue());
	}

	@After
	public void tearDown() throws Exception
	{
		final Currency c = c2lm.getCurrencyByIsoCode("---");
		c.setBase();
	}

}
