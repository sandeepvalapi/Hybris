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

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.CoreAlgorithms.LegacyRoundingConfigProvider;

import java.math.BigDecimal;

import org.junit.Test;


/**
 *
 */
@UnitTest
public class CoreAlgorithmsTest
{
	// see HORST-1652
	@Test
	public void testRoundHalfUp()
	{
		final BigDecimal v0 = new BigDecimal("1150.5");
		final String v0r = "1151.0";

		final BigDecimal v00 = new BigDecimal("1150.25");
		final String v00r = "1150.3";

		final BigDecimal v0000 = new BigDecimal("1150.2335");
		final String v0000r = "1150.234";

		final BigDecimal v0001 = new BigDecimal("1150.2334");
		final String v0001r = "1150.233";

		assertEquals(v0r, Double.toString(CoreAlgorithms.round(v0.doubleValue(), 0)));
		assertEquals(v00r, Double.toString(CoreAlgorithms.round(v00.doubleValue(), 1)));
		assertEquals(v0000r, Double.toString(CoreAlgorithms.round(v0000.doubleValue(), 3)));
		assertEquals(v0001r, Double.toString(CoreAlgorithms.round(v0001.doubleValue(), 3)));


		final BigDecimal v1150_235 = new BigDecimal("1150.235");
		final String v1150_235r = "1150.24";

		final BigDecimal v1100_235 = new BigDecimal("1100.235");
		final String v1100_235r = "1100.24";

		final BigDecimal v1400_235 = new BigDecimal("1400.235");
		final String v1400_235r = "1400.24";

		assertEquals(v1150_235r, Double.toString(CoreAlgorithms.round(v1150_235.doubleValue(), 2)));
		assertEquals(v1100_235r, Double.toString(CoreAlgorithms.round(v1100_235.doubleValue(), 2)));
		assertEquals(v1400_235r, Double.toString(CoreAlgorithms.round(v1400_235.doubleValue(), 2)));
	}

	@Test
	public void testRoundHalfUpNegative()
	{
		final BigDecimal value = new BigDecimal("-1150.2535");
		final String v0 = "-1150.0";
		final String v1 = "-1150.3";
		final String v2 = "-1150.25";
		final String v3 = "-1150.254";
		final String v4 = "-1150.2535";

		assertEquals(v0, Double.toString(CoreAlgorithms.round(value.doubleValue(), 0)));
		assertEquals(v1, Double.toString(CoreAlgorithms.round(value.doubleValue(), 1)));
		assertEquals(v2, Double.toString(CoreAlgorithms.round(value.doubleValue(), 2)));
		assertEquals(v3, Double.toString(CoreAlgorithms.round(value.doubleValue(), 3)));
		assertEquals(v4, Double.toString(CoreAlgorithms.round(value.doubleValue(), 4)));
	}

	@Test
	public void testConvertAndRound()
	{
		final BigDecimal price = new BigDecimal("20.75");
		final BigDecimal tgtRatio = new BigDecimal("1.38");
		final BigDecimal srcRatio = new BigDecimal("1");

		assertEquals("28.635",
				Double.toString(CoreAlgorithms.convert(srcRatio.doubleValue(), tgtRatio.doubleValue(), price.doubleValue())));
		assertEquals("28.64", Double.toString(CoreAlgorithms
				.round(CoreAlgorithms.convert(srcRatio.doubleValue(), tgtRatio.doubleValue(), price.doubleValue()), 2)));
	}

	@Test()
	public void testRoundNaN()
	{
		assertEquals("" + Double.POSITIVE_INFINITY, Double.toString(CoreAlgorithms.round(100.0 / 0.0, 2)));
		assertEquals("" + Double.NEGATIVE_INFINITY, Double.toString(CoreAlgorithms.round(-100.0 / 0.0, 2)));
		assertEquals("" + Double.NaN, Double.toString(CoreAlgorithms.round(0.0 / 0.0, 2)));
	}

	// see ECP-1211
	@Test
	public void testLegacyRoundingMode()
	{
		
		// new mode
		testRoundWithDouble4LegacyTesting(false, false);

		// old mode + old API
		testRoundWithDouble4LegacyTesting(true, true);

		
		// old mode + new API + legacy flag
		LegacyRoundingConfigProvider old = CoreAlgorithms.setLegacyConfigProvider(new LegacyRoundingConfigProvider()
		{
			@Override
			public boolean isLegacyRoundingEnabled()
			{
				return true;
			}
		});
		try
		{
			testRoundWithDouble4LegacyTesting(true, false);
		}
		finally
		{
			CoreAlgorithms.setLegacyConfigProvider(old);
		}
	}
	
	protected void testRoundWithDouble4LegacyTesting(boolean legacyMode, boolean legacyAPI)
	{
		final double value = 3.1415d;

		final double roundValueWithScaleOne = round(value, 1, legacyAPI);
		assertEquals(Double.valueOf(3.1d), Double.valueOf(roundValueWithScaleOne));

		final double roundValueWithScaleTwo = round(value, 2, legacyAPI);
		assertEquals(Double.valueOf(3.14d), Double.valueOf(roundValueWithScaleTwo));

		final double roundValueWithScaleThree = round(value, 3, legacyAPI);
		assertEquals(Double.valueOf(3.142d), Double.valueOf(roundValueWithScaleThree));

		final double negValue = -3.1415d;

		final double roundNegValueWithScaleOne = round(negValue, 1, legacyAPI);
		assertEquals(Double.valueOf(-3.1d), Double.valueOf(roundNegValueWithScaleOne));

		final double roundNegValueWithScaleTwo = round(negValue, 2, legacyAPI);
		assertEquals(Double.valueOf(-3.14d), Double.valueOf(roundNegValueWithScaleTwo));

		// special case in legacy mode: in the past we where rounding *negative* values HALF_DOWN instead of HALF_UP ( -1.5 -> -1.0 instead of -2.0)
		if (legacyMode)
		{
			final double roundNegValueWithScaleThree = round(negValue, 3, legacyAPI);
			assertEquals(Double.valueOf(-3.141d), Double.valueOf(roundNegValueWithScaleThree));
		}
		else
		{
			final double roundNegValueWithScaleThree = round(negValue, 3, legacyAPI);
			assertEquals(Double.valueOf(-3.142d), Double.valueOf(roundNegValueWithScaleThree));
		}
	}

	protected double round(final double value, final int digits, boolean legacyAPI)
	{
		if (legacyAPI)
		{
			return oldRound(value, digits);
		}
		else
		{
			return CoreAlgorithms.round(value, digits);
		}
	}

	protected double oldRound(final double value, final int digits)
	{
		final double digitFactor = Math.pow(10, digits);

		return Math.round(value * digitFactor) / digitFactor;
	}
}
