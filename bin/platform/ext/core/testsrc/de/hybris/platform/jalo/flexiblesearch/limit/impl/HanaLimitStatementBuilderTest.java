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

import static de.hybris.platform.jalo.flexiblesearch.limit.impl.LimitStatementBuilderAssert.assertThat;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilder;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery.ExecutableQuery;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class HanaLimitStatementBuilderTest
{
	private static final String ORIGINAL_QUERY = "SELECT * FROM FooBar WHERE baz='boom' ORDER BY foo";

	@Mock
	private ExecutableQuery originalQuery;
	private LimitStatementBuilder builder;
	private final List originalValueList = Lists.newArrayList("foo", "bar");

	@Before
	public void setUp() throws Exception
	{
		given(originalQuery.getSQL()).willReturn(ORIGINAL_QUERY);
		given(originalQuery.getValueList()).willReturn(originalValueList);
	}

	@Test
	public void shouldBuildQueryWithStartZeroAndCountZero()
	{
		// given
		final int start = 0;
		final int count = 0;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, false);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT ? OFFSET ?");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(0), Integer.valueOf(0));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(0), Integer.valueOf(0));
	}

	@Test
	public void shouldBuildQueryWithStartZeroAndCountZeroLiteral()
	{
		// given
		final int start = 0;
		final int count = 0;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, true);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT 0 OFFSET 0");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(0), Integer.valueOf(0));
		assertThat(builder).hasNoAdditionalStatementValues();
	}

	
	@Test
	public void shouldBuildQueryWithStartZeroAndCountGreaterThanZero()
	{
		// given
		final int start = 0;
		final int count = 1;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, false);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT ? OFFSET ?");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(0), Integer.valueOf(1));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(1), Integer.valueOf(0));
	}

	@Test
	public void shouldBuildQueryWithStartZeroAndCountGreaterThanZeroLiteral()
	{
		// given
		final int start = 0;
		final int count = 1;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, true);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT 1 OFFSET 0");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(0), Integer.valueOf(1));
		assertThat(builder).hasNoAdditionalStatementValues();
	}

	
	@Test
	public void shouldBuildQueryWithStartGreaterThanZeroAndCountZero()
	{
		// given
		final int start = 1;
		final int count = 0;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, false);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT ? OFFSET ?");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(1), Integer.valueOf(0));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(0), Integer.valueOf(1));
	}

	@Test
	public void shouldBuildQueryWithStartGreaterThanZeroAndCountZeroLiteral()
	{
		// given
		final int start = 1;
		final int count = 0;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, true);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT 0 OFFSET 1");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(1), Integer.valueOf(0));
		assertThat(builder).hasNoAdditionalStatementValues();
	}

	
	@Test
	public void shouldBuildQueryWithStartGreaterThanZeroAndCountGreaterThanZero()
	{
		// given
		final int start = 1;
		final int count = 1;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, false);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT ? OFFSET ?");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(1), Integer.valueOf(1));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(1), Integer.valueOf(1));
	}

	@Test
	public void shouldBuildQueryWithStartGreaterThanZeroAndCountGreaterThanZeroLiteral()
	{
		// given
		final int start = 1;
		final int count = 1;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, true);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT 1 OFFSET 1");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(1), Integer.valueOf(1));
		assertThat(builder).hasNoAdditionalStatementValues();
	}

	
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenStartIsLowerThanZero()
	{
		// given
		final int start = -1;
		final int count = 1;

		try
		{
			// when
			builder = new HanaLimitStatementBuilder(originalQuery, start, count, false);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("start parameter cannot be lower than 0");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenStartIsLowerThanZeroLiteral()
	{
		// given
		final int start = -1;
		final int count = 1;

		try
		{
			// when
			builder = new HanaLimitStatementBuilder(originalQuery, start, count, true);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("start parameter cannot be lower than 0");
		}
	}

	@Test
	public void shouldBuildQueryWithStartZeroAndCountLowerThanZero()
	{
		// given
		final int start = 0;
		final int count = -1;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, false);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT ? OFFSET ?");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(0), Integer.valueOf(-1));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
	}

	@Test
	public void shouldBuildQueryWithStartZeroAndCountLowerThanZeroLiteral()
	{
		// given
		final int start = 0;
		final int count = -1;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, true);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT "+Integer.MAX_VALUE+" OFFSET 0");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(0), Integer.valueOf(-1));
		assertThat(builder).hasNoAdditionalStatementValues();
	}

	@Test
	public void shouldBuildQueryWithStartGreaterThanZeroAndCountLowerThanZero()
	{
		// given
		final int start = 1;
		final int count = -1;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, false);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT ? OFFSET ?");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(1), Integer.valueOf(-1));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(1));
	}

	@Test
	public void shouldBuildQueryWithStartGreaterThanZeroAndCountLowerThanZeroLiteral()
	{
		// given
		final int start = 1;
		final int count = -1;

		// when
		builder = new HanaLimitStatementBuilder(originalQuery, start, count, true);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(ORIGINAL_QUERY + " LIMIT "+Integer.MAX_VALUE+" OFFSET 1");
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(1), Integer.valueOf(-1));
		assertThat(builder).hasNoAdditionalStatementValues();
	}

}
