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
package de.hybris.platform.validation.services.impl;

import de.hybris.bootstrap.annotations.PerformanceTest;

import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
@PerformanceTest
public class DefaultHybrisConstraintViolationTest
{
	private static final Logger LOG = Logger.getLogger(DefaultHybrisConstraintViolationTest.class);

	private static final String valueChukedBefore = "{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1"
			+ //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1" + //
			"{a}{b}{c}{d}{e}{f}{g}{h}{i}{j}{k}{l}{m}{n}{o}{p}{q}{r}{s}{t}{u}{w}{v}{x}{y}{z}1";


	private static final String valueChukedAfter = "111111111111";

	private static final String valueAvgBefore = "fofsdbhdkjsbsdbjfdb{a}idhsudhus hu{b} uyvgduysv yuvsdyu {c}" + //
			"dhdsisdfdsfkjkfdsdvjksdfjkdsfk{foo}ddfdfhdfnhdfnldfdskljdklsddklsdnkl{foo}" + //
			"dhdsisdfdsfkjkfdsdvjksdfjkdsfk{verylongfooreplacerwithsomelongstringinside}ddfdfhdfnhdfnldfdskljdklsddklsdnkl{foo}";

	private static final String valueAvgAfter = "fofsdbhdkjsbsdbjfdbidhsudhus hu uyvgduysv yuvsdyu " + //
			"dhdsisdfdsfkjkfdsdvjksdfjkdsfkddfdfhdfnhdfnldfdskljdklsddklsdnkl" + //
			"dhdsisdfdsfkjkfdsdvjksdfjkdsfkddfdfhdfnhdfnldfdskljdklsddklsdnkl";


	private static final String valueHugeBefore = "{fofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdb"
			+ "fofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdb}fofsdbhdkjsbsdbjfdbfofsdbhdkjs"
			+ "bsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdb{fofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfof"
			+ "sdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdb"
			+ "jfdbfofsdbhdkjsbsdbjfdb}";


	private static final String valueHugeAfter = "fofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdb";
	public static final int REPLACEMENTS = 10000;

	final DefaultHybrisConstraintViolation viol = new DefaultHybrisConstraintViolation();


	@Ignore
	@Test
	public void testNested()
	{
		final Map replacers = ImmutableMap.of("1", "a",//
				"2", "",//
				"3", "c", "4", "foo", "5", "bar");


		testReplace(replacers, "{3{4{5}}}", "{3{4bar}}");
	}

	@Ignore
	@Test
	public void testNested2()
	{
		LOG.info("Nested ...");
		final Map replacers = ImmutableMap.of("1", "say hello",//
				"2hi", "",//
				"3foo", "hi", "4bar", "foo", "5", "bar");

		testReplace(replacers, "{1{2{3{4{5}}}}}", "say hello");
	}

	@Test
	public void testAZChunks()
	{
		LOG.info("a .... z chunks");
		final ImmutableMap.Builder replacers = new ImmutableMap.Builder();
		for (int i = 'a'; i <= 'z'; i++)
		{
			replacers.put(String.valueOf(Character.toChars(i)), "");
		}
		testReplace(replacers.build(), valueChukedBefore, valueChukedAfter);

	}


	@Test
	public void testSimple()
	{
		LOG.info(" simple ...");
		final Map replacers = ImmutableMap.of("1", "a",//
				"2", "",//
				"3", "c");
		testReplace(replacers, "1{1}{2}23{3}456789", "1a23c456789");

	}

	@Test
	public void testAverage()
	{
		LOG.info(" avg ....");
		final Map replacers = ImmutableMap.of("a", ""//
				, "b", "" //
				, "c", "" //
				, "foo", "" //
				, "verylongfooreplacerwithsomelongstringinside", "" //
		//,"verylongfooreplacerwithsomelongstringinside",""
				);
		testReplace(replacers, valueAvgBefore, valueAvgAfter);

	}

	@Test
	public void testHugeChunks()
	{
		LOG.info(" huge ");

		//final DefaultHybrisConstraintViolation viol = new DefaultHybrisConstraintViolation();

		final Map replacers = ImmutableMap
				.of("fofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdbfofsdbhdkjsbsdbjfdb",
						""//

				);

		testReplace(replacers, valueHugeBefore, valueHugeAfter);
	}


	private void testReplace(final Map replacers, final String valueBefore, final String valueAfter)
	{
		//LOG.info("########## substr");
		String result = null;
		final long start = System.currentTimeMillis();
		for (int i = 0; i < REPLACEMENTS; i++)
		{
			result = viol.replacePlaceholders(valueBefore, replacers);
		}
		final long end = System.currentTimeMillis();
		Assert.assertEquals(valueAfter, result);

		LOG.info(" time avg [ms] " + (end - start));
	}
}
