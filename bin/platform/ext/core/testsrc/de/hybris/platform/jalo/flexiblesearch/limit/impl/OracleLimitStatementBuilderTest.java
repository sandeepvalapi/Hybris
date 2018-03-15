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
public class OracleLimitStatementBuilderTest
{
	private static final String ORIGINAL_QUERY = "SELECT * FROM FooBar WHERE baz='boom' ORDER BY foo";
	private static final String MODIFIED_QUERY_WITHOUT_OFFSET = "SELECT * FROM (" + ORIGINAL_QUERY + ") WHERE rownum <= ?";
	private static final String MODIFIED_QUERY_WITH_OFFSET = "SELECT * FROM (SELECT row_.*, rownum rownum_ FROM ("
			+ ORIGINAL_QUERY + ") row_ WHERE rownum <= ?) WHERE rownum_ > ?";

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
	public void shouldThrowIllegalArgumentExceptionWhenOriginalQueryWillReturnNullForGetSQL()
	{
		// given
		final int start = 0;
		final int count = 0;
		given(originalQuery.getSQL()).willReturn(null);

		try
		{
			// when
			new OracleLimitStatementBuilder(originalQuery, start, count);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("ExecutableQuery objects does not contains SQL query!");
		}
	}

	@Test
	public void shouldBuildQueryWithStartZeroAndCountZero()
	{
		// given
		final int start = 0;
		final int count = 0;

		// when
		builder = new OracleLimitStatementBuilder(originalQuery, start, count);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(MODIFIED_QUERY_WITHOUT_OFFSET);
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(0), Integer.valueOf(0));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(0));
	}

	@Test
	public void shouldBuildQueryWithStartZeroAndCountGreaterThanZero()
	{
		// given
		final int start = 0;
		final int count = 1;

		// when
		builder = new OracleLimitStatementBuilder(originalQuery, start, count);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(MODIFIED_QUERY_WITHOUT_OFFSET);
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(0), Integer.valueOf(1));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(1));
	}

	@Test
	public void shouldBuildQueryWithStartGreaterThanZeroAndCountZero()
	{
		// given
		final int start = 1;
		final int count = 0;

		// when
		builder = new OracleLimitStatementBuilder(originalQuery, start, count);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(MODIFIED_QUERY_WITH_OFFSET);
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(1), Integer.valueOf(0));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(start + count), Integer.valueOf(1));
	}

	@Test
	public void shouldBuildQueryWithStartGreaterThanZeroAndCountGreaterThanZero()
	{
		// given
		final int start = 1;
		final int count = 1;

		// when
		builder = new OracleLimitStatementBuilder(originalQuery, start, count);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(MODIFIED_QUERY_WITH_OFFSET);
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(1), Integer.valueOf(1));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(start + count), Integer.valueOf(1));
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
			builder = new OracleLimitStatementBuilder(originalQuery, start, count);
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
		builder = new OracleLimitStatementBuilder(originalQuery, start, count);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(MODIFIED_QUERY_WITHOUT_OFFSET);
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(0), Integer.valueOf(-1));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(Integer.MAX_VALUE));
	}

	@Test
	public void shouldBuildQueryWithStartGreaterThanZeroAndCountLowerThanZero()
	{
		// given
		final int start = 1;
		final int count = -1;

		// when
		builder = new OracleLimitStatementBuilder(originalQuery, start, count);

		// then
		assertThat(builder.getModifiedStatement()).isEqualTo(MODIFIED_QUERY_WITH_OFFSET);
		assertThat(builder).hasOriginalStartAndCountValues(Integer.valueOf(1), Integer.valueOf(-1));
		assertThat(builder).hasAdditionalStatementValues(Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(1));
	}

}
