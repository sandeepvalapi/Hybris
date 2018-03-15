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
package de.hybris.platform.jalo.flexiblesearch.limit.impl;

import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;


/**
 * Assertion for easy checking <code>LimitStatementBuilder</code> implementations.
 */
public class LimitStatementBuilderAssert extends GenericAssert<LimitStatementBuilderAssert, LimitStatementBuilder>
{
	public LimitStatementBuilderAssert(final LimitStatementBuilder actual)
	{
		super(LimitStatementBuilderAssert.class, actual);
	}

	public static LimitStatementBuilderAssert assertThat(final LimitStatementBuilder actual)
	{
		return new LimitStatementBuilderAssert(actual);
	}

	public LimitStatementBuilderAssert hasOriginalStartAndCountValues(final Integer val1, final Integer val2)
	{
		Assertions.assertThat(actual).isNotNull();
		Assertions.assertThat(actual.getOriginalStart()).isEqualTo(val1);
		Assertions.assertThat(actual.getOriginalCount()).isEqualTo(val2);
		return this;
	}

	public LimitStatementBuilderAssert hasAdditionalStatementValues(final Integer... values)
	{
		final List<Object> expected = new ArrayList<Object>();
		expected.add("foo");
		expected.add("bar");
		expected.addAll(Arrays.asList(values));

		Assertions.assertThat(actual).isNotNull();
		Assertions.assertThat(actual.getModifiedStatementValues()).hasSize(2 + values.length);
		Assertions.assertThat(actual.getModifiedStatementValues()).isEqualTo(expected);
		return this;
	}

	public LimitStatementBuilderAssert hasNoAdditionalStatementValues()
	{
		Assertions.assertThat(actual).isNotNull();
		Assertions.assertThat(actual.getModifiedStatementValues()).hasSize(2);
		return this;
	}
}
