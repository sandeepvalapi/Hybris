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
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CountryTest extends HybrisJUnit4TransactionalTest
{
	private C2LManager c2lm = null;

	@Before
	public void setUp() throws Exception
	{
		c2lm = jaloSession.getC2LManager();
	}

	@Test
	public void testGetAllCountries()
	{
		final Collection collection = jaloSession.getC2LManager().getAllCountries();
		for (final Iterator it = collection.iterator(); it.hasNext();)
		{
			final Country cty = (Country) it.next();
			cty.getIsoCode();
		}
	}


	/*
	 * BUG 97: getAllCountries() gives emptySet, but getCountryByIsoCode() delivers country
	 * 
	 * Note: be sure all used isocodes are less than 10 characters.
	 */
	@Test
	public void testBug97() throws Exception
	{
		Country country = null;

		final Iterator iterator = jaloSession.getC2LManager().getAllCountries().iterator();
		if (iterator.hasNext())
		{
			fail("No country should exist after initialize().");
			country = (Country) iterator.next();
		}
		else
		{
			try
			{
				country = jaloSession.getC2LManager().getCountryByIsoCode("TestCode");
				fail("No country should exist after initialize().");
			}
			catch (final JaloItemNotFoundException e)
			{
				country = jaloSession.getC2LManager().createCountry("TestCode");
				assertTrue(jaloSession.getC2LManager().getCountryByIsoCode("TestCode").equals(country));
				assertFalse(jaloSession.getC2LManager().getAllCountries().isEmpty());
				country.remove();
				country = null;
			}
			try
			{
				country = jaloSession.getC2LManager().getCountryByIsoCode("DE");
				fail("No country should exist after initialize().");
			}
			catch (final JaloItemNotFoundException e)
			{
				country = jaloSession.getC2LManager().createCountry("DE");
				assertTrue(jaloSession.getC2LManager().getCountryByIsoCode("DE").equals(country));
				country.remove();
				country = null;
			}
		}
	}

	@Test
	public void testRegions() throws Exception
	{
		Country country1 = null;
		Country country2 = null;
		Region region1 = null;
		Region region2 = null;
		Region region3 = null;
		final String region1Code = "region1Code";

		country1 = c2lm.createCountry("country1IsoCode");
		country2 = c2lm.createCountry("country2IsoCode");
		region1 = country1.addNewRegion(region1Code);
		region2 = country1.addNewRegion("region2Code");
		region3 = country2.addNewRegion("region3Code");

		Collection coll = country1.getRegions();
		assertEquals(2, coll.size());

		coll = country2.getRegions();
		assertEquals(1, coll.size());
		assertEquals(region3, coll.iterator().next());

		final Region region = country1.getRegionByCode(region1Code);
		assertEquals(region1, region);

		region1.remove();
		region2.remove();
		region3.remove();
		country2.remove();
		country1.remove();
	}
}
