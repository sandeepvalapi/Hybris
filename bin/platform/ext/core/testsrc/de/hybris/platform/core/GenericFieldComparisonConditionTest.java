/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.core;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.testframework.Assert;

import org.junit.Test;


/**
 * Unit tests for {@link GenericFieldComparisonCondition}
 */
@UnitTest
public class GenericFieldComparisonConditionTest
{

	private static final GenericSearchField ANY_FIELD = new GenericSearchField("TEST_FIELD");
	private static final GenericSearchField OTHER_FIELD = new GenericSearchField("TEST_OTHER_FIELD");

	@Test
	public void sameFieldsShouldBeEqual()
	{
		final GenericCondition condition1 = GenericCondition.getComparison(ANY_FIELD, Operator.EQUAL, ANY_FIELD);
		final GenericCondition condition2 = GenericCondition.getComparison(ANY_FIELD, Operator.EQUAL, ANY_FIELD);

		Assert.assertEquals(condition1, condition2);
	}

	@Test
	public void differentComparisonFieldsShouldNotBeEqual()
	{
		final GenericCondition condition1 = GenericCondition.getComparison(ANY_FIELD, Operator.EQUAL, ANY_FIELD);
		final GenericCondition condition2 = GenericCondition.getComparison(ANY_FIELD, Operator.EQUAL, OTHER_FIELD);

		Assert.assertNotEquals(condition1, condition2);
	}

}
