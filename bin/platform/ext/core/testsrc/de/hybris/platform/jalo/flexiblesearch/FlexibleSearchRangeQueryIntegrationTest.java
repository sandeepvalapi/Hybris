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
package de.hybris.platform.jalo.flexiblesearch;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LItem;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests range queries with usage of <code>FlexibleSearch</code> in low level jalo layer.
 */
@IntegrationTest
public class FlexibleSearchRangeQueryIntegrationTest extends HybrisJUnit4TransactionalTest
{
	private enum QueryRange
	{
		START_AT_ZERO_COUNT_NOLIMIT(0, -1), START_AT_ONE_COUNT_NOLIMIT(1, -1), START_AT_ONE_COUNT_ONE(1, 1), START_AT_ONE_COUNT_ZERO(
				1, 0), START_AT_ZERO_COUNT_ONE(0, 1), START_AT_TEN_COUNT_NOLIMIT(10, -1);

		int count;

		int start;

		QueryRange(final int start, final int count)
		{
			this.start = start;
			this.count = count;
		}

		public int getCount()
		{
			return count;
		}

		public int getStart()
		{
			return start;
		}
	}

	private Language de, en;
	private FlexibleSearch flexibleSearch;

	private SessionContext ctx;

	@Before
	public void setUp() throws Exception
	{
		flexibleSearch = jaloSession.getFlexibleSearch();
		ctx = jaloSession.createSessionContext();
		de = getOrCreateLanguage("de");
		en = getOrCreateLanguage("en");
	}

	@Test
	public void shouldReturnResultStartingFromOneOfCountOne()
	{
		// given
		final QueryRange queryRange = QueryRange.START_AT_ONE_COUNT_ONE;

		// when
		final SearchResult result = executeFlexibleSearch(queryRange);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getTotalCount()).isEqualTo(2);
		assertThat(result.getCount()).isEqualTo(1);
		assertThat(result.getResult()).containsOnly(en);
	}

	@Test
	public void shouldReturnResultStartingFromOneOfCountZeroWhichIsEmptyResult()
	{
		// given
		final QueryRange queryRange = QueryRange.START_AT_ONE_COUNT_ZERO;

		// when
		final SearchResult result = executeFlexibleSearch(queryRange);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getTotalCount()).isEqualTo(2);
		assertThat(result.getCount()).isEqualTo(0);
		assertThat(result.getResult()).isEmpty();
	}


	@Test
	public void shouldReturnResultStartingFromOneOfNoLimitCount()
	{
		// given
		final QueryRange queryRange = QueryRange.START_AT_ONE_COUNT_NOLIMIT;

		// when
		final SearchResult result = executeFlexibleSearch(queryRange);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getTotalCount()).isEqualTo(2);
		assertThat(result.getCount()).isEqualTo(1);
		assertThat(result.getResult()).containsOnly(en);
	}

	@Test
	public void shouldReturnResultStartingFromTenOfNoLimitCountWhichIsEmptyResult()
	{
		// given
		final QueryRange queryRange = QueryRange.START_AT_TEN_COUNT_NOLIMIT;

		// when
		final SearchResult result = executeFlexibleSearch(queryRange);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getTotalCount()).isEqualTo(2);
		assertThat(result.getCount()).isEqualTo(0);
		assertThat(result.getResult()).isEmpty();
	}

	@Test
	public void shouldReturnResultStartingFromZeroOfCountOne()
	{
		// given
		final QueryRange queryRange = QueryRange.START_AT_ZERO_COUNT_ONE;

		// when
		final SearchResult result = executeFlexibleSearch(queryRange);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getTotalCount()).isEqualTo(2);
		assertThat(result.getCount()).isEqualTo(1);
		assertThat(result.getResult()).containsOnly(de);
	}


	@Test
	public void shouldReturnResultStartingFromZeroOfNoLimitCount()
	{
		// given
		final QueryRange queryRange = QueryRange.START_AT_ZERO_COUNT_NOLIMIT;

		// when
		final SearchResult result = executeFlexibleSearch(queryRange);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getTotalCount()).isEqualTo(2);
		assertThat(result.getCount()).isEqualTo(2);
		assertThat(result.getResult()).containsOnly(de, en);
	}

	@SuppressWarnings("deprecation")
	private SearchResult executeFlexibleSearch(final QueryRange queryRange)
	{
		final String query = "SELECT {1:" + Item.PK + "} FROM {Language} WHERE {" + C2LItem.ISOCODE
				+ "} IN ( 'de', 'en' ) ORDER BY {" + C2LItem.ISOCODE + "}, {" + Item.PK + "} ASC";
		final boolean dontNeedTotal = false;
		final Map values = null;
		final List<Class<Language>> resultClasses = Collections.singletonList(Language.class);
		final boolean failOnUnknownFields = true;

		return flexibleSearch.search(this.ctx, query, values, resultClasses, failOnUnknownFields, dontNeedTotal,
				queryRange.getStart(), queryRange.getCount());
	}
}
