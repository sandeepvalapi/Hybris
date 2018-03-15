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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.Utilities;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Unit test class for {@link Utilities} class.
 */
@UnitTest
public class UtilitiesTest
{

	@Test
	public void parseLocaleCodesTest1()
	{
		final Locale loc = parseLocale("pl,PL");
		Assert.assertEquals("pl", loc.getLanguage());
		Assert.assertEquals("PL", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest2()
	{
		final Locale loc = parseLocale("pl,");
		Assert.assertEquals("pl", loc.getLanguage());
		Assert.assertEquals("", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest3()
	{
		final Locale loc = parseLocale("pl,,slask");
		Assert.assertEquals("pl", loc.getLanguage());
		Assert.assertEquals("", loc.getCountry());
		Assert.assertEquals("slask", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest4()
	{
		final Locale loc = parseLocale("pl,,slask-zabrze-");
		Assert.assertEquals("pl", loc.getLanguage());
		Assert.assertEquals("", loc.getCountry());
		Assert.assertEquals("slask-zabrze-", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest5()
	{
		final Locale loc = parseLocale("--");
		Assert.assertEquals("--", loc.getLanguage());
		Assert.assertEquals("", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest6()
	{
		final Locale loc = parseLocale("---");
		Assert.assertEquals("---", loc.getLanguage());
		Assert.assertEquals("", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest7()
	{
		final Locale loc = parseLocale("de");
		Assert.assertEquals("de", loc.getLanguage());
		Assert.assertEquals("", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest8()
	{
		final Locale loc = parseLocale("de,,");
		Assert.assertEquals("de", loc.getLanguage());
		Assert.assertEquals("", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest9()
	{
		final Locale loc = parseLocale("-de,,");
		Assert.assertEquals("", loc.getLanguage());
		Assert.assertEquals("DE", loc.getCountry());
		Assert.assertEquals(",", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest10()
	{
		final Locale loc = parseLocale("de,DE,");
		Assert.assertEquals("de", loc.getLanguage());
		Assert.assertEquals("DE", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest12()
	{
		final Locale loc = parseLocale("de,DE,,");
		Assert.assertEquals("de", loc.getLanguage());
		Assert.assertEquals("DE", loc.getCountry());
		Assert.assertEquals(",", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest14()
	{
		final Locale loc = parseLocale("DE__");
		Assert.assertEquals("de", loc.getLanguage());
		Assert.assertEquals("", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest15()
	{
		final Locale loc = parseLocale("pl_PL_");
		Assert.assertEquals("pl", loc.getLanguage());
		Assert.assertEquals("PL", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest16()
	{
		final Locale loc = parseLocale("pl_PL_slask_gliwice");
		Assert.assertEquals("pl", loc.getLanguage());
		Assert.assertEquals("PL", loc.getCountry());
		Assert.assertEquals("slask_gliwice", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest17()
	{
		final Locale loc = parseLocale("_PL_slask_gliwice");
		Assert.assertEquals("", loc.getLanguage());
		Assert.assertEquals("PL", loc.getCountry());
		Assert.assertEquals("slask_gliwice", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest18()
	{
		final Locale loc = parseLocale("_p_p");
		Assert.assertEquals("", loc.getLanguage());
		Assert.assertEquals("P", loc.getCountry());
		Assert.assertEquals("p", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest19()
	{
		final Locale loc = parseLocale("p_p");
		Assert.assertEquals("p", loc.getLanguage());
		Assert.assertEquals("P", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest20()
	{
		final Locale loc = parseLocale("_p");
		Assert.assertEquals("", loc.getLanguage());
		Assert.assertEquals("P", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}

	@Test
	public void parseLocaleCodesTest21()
	{
		final Locale loc = parseLocale("p_");
		Assert.assertEquals("p", loc.getLanguage());
		Assert.assertEquals("", loc.getCountry());
		Assert.assertEquals("", loc.getVariant());
	}


	@Test
	public void testFormatTimePeriod()
	{
		Assert.assertEquals("-24d -20h:-31m:-23s:52ms", Utilities.formatTime(Integer.MIN_VALUE));
		//Assert.assertEquals("0d 00h:00m:00s:00-991ms", Utilities.formatTime(-991));
		//Assert.assertEquals("0d 00h:00m:00s:00-1ms", Utilities.formatTime(-99));
		//Assert.assertEquals("0d 00h:00m:00s:00-1ms", Utilities.formatTime(-1));
		Assert.assertEquals("0d 00h:00m:00s:000ms", Utilities.formatTime(0));
		Assert.assertEquals("0d 00h:00m:00s:001ms", Utilities.formatTime(1));
		Assert.assertEquals("0d 00h:00m:00s:999ms", Utilities.formatTime(999));
		Assert.assertEquals("0d 00h:00m:09s:999ms", Utilities.formatTime(9999));
		Assert.assertEquals("0d 00h:01m:39s:999ms", Utilities.formatTime(99999));
		Assert.assertEquals("0d 00h:16m:39s:999ms", Utilities.formatTime(999999));
		Assert.assertEquals("0d 02h:46m:39s:999ms", Utilities.formatTime(9999999));
		Assert.assertEquals("1d 03h:46m:39s:999ms", Utilities.formatTime(99999999));
		Assert.assertEquals("11d 13h:46m:39s:999ms", Utilities.formatTime(999999999));
		Assert.assertEquals("24d 20h:31m:23s:647ms", Utilities.formatTime(Integer.MAX_VALUE));
	}

	private Locale parseLocale(final String isoCode)
	{
		final String[] loc = Utilities.parseLocaleCodes(isoCode);
		return new Locale(loc[0], loc[1], loc[2]);
	}

}
