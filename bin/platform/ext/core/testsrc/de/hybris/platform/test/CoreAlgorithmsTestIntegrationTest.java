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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import org.junit.Test;

@IntegrationTest
public class CoreAlgorithmsTestIntegrationTest extends HybrisJUnit4Test
{

	// see ECP-1211
	@Test
	public void testLegacyRoundingMode()
	{
		
		// new mode
		testRoundWithDouble4LegacyTesting(false, false);

		// old mode + old API
		testRoundWithDouble4LegacyTesting(true, true);

		
		// old mode + new API + legacy flag
		PropertyConfigSwitcher cfg = new PropertyConfigSwitcher(CoreAlgorithms.CFG_LEGACY_ROUNDING);
		
		cfg.switchToValue("true");
		try
		{
			testRoundWithDouble4LegacyTesting(true, false);
		}
		finally
		{
			cfg.switchBackToDefault();
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
	
	protected double round(final double value, final int digits, boolean legacyAPI )
	{
		if( legacyAPI )
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
