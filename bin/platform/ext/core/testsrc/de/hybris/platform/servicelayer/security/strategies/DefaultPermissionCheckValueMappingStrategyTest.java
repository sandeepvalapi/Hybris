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
package de.hybris.platform.servicelayer.security.strategies;

import static de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue.ALLOWED;
import static de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue.CONFLICTING;
import static de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue.DENIED;
import static de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue.NOT_DEFINED;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.strategies.impl.DefaultPermissionCheckValueMappingStrategy;

import org.junit.Assert;
import org.junit.Test;


/**
 * This is a test for {@link DefaultPermissionCheckValueMappingStrategy} class
 */
@UnitTest
public class DefaultPermissionCheckValueMappingStrategyTest
{
	@Test
	public void testBooleanMappingOfPermissionCheckValue()
	{
		final PermissionCheckValueMappingStrategy strategy = new DefaultPermissionCheckValueMappingStrategy();

		final PermissionCheckValue[] possibleValues =
		{ ALLOWED, DENIED, CONFLICTING, NOT_DEFINED };
		final boolean[] expectedGrantedOutcomes =
		{ true, false, false, false };

		for (int i = 0; i < possibleValues.length; i++)
		{
			final PermissionCheckResult actualOutcome = strategy.getPermissionCheckResult(possibleValues[i]);
			Assert.assertEquals(
					"PermissionCheckValue used in strategy invocation should be returned in PermissionCheckResult object",
					possibleValues[i], actualOutcome.getCheckValue());
			Assert.assertEquals("isGranted() value does not match expected value", Boolean.valueOf(expectedGrantedOutcomes[i]),
					Boolean.valueOf(actualOutcome.isGranted()));
		}
	}
}
