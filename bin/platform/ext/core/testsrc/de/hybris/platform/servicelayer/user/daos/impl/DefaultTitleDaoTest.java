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
package de.hybris.platform.servicelayer.user.daos.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.common.collect.Lists;


@UnitTest
public class DefaultTitleDaoTest
{
	private static final String DOC = "dr";
	private static final String PROF = "prof";

	private DefaultTitleDao defaultTitleDao;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void prepare()
	{
		initMocks(this);
		defaultTitleDao = new DefaultTitleDao();
		defaultTitleDao.setFlexibleSearchService(flexibleSearchService);
	}

	@Test
	public void testFindTitlesEmpty()
	{
		final List<TitleModel> persistedTitles = Collections.emptyList();

		final SearchResult searchResult = mock(SearchResult.class);
		when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		when(searchResult.getResult()).thenReturn(persistedTitles);

		final Collection<TitleModel> results = defaultTitleDao.findTitles();
		assertThat(results).isNotNull().isEmpty();
		verify(flexibleSearchService, times(1)).search(argThat(findTitlesQueryArgumentMatcher()));
	}

	@Test
	public void testFindTitles()
	{
		final List<TitleModel> persistedTitles = Lists.newArrayList(createTitle(DOC), createTitle(PROF));

		final SearchResult searchResult = mock(SearchResult.class);
		when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		when(searchResult.getResult()).thenReturn(persistedTitles);

		final Collection<TitleModel> results = defaultTitleDao.findTitles();

		assertThat(results).hasSize(2).extracting(TitleModel::getCode).containsExactlyInAnyOrder(DOC, PROF);
		verify(flexibleSearchService, times(1)).search(argThat(findTitlesQueryArgumentMatcher()));
	}

	@Test
	public void testFindTitleByCodeEmpty()
	{
		final List<TitleModel> persistedTitles = Collections.emptyList();

		final SearchResult searchResult = mock(SearchResult.class);
		when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		when(searchResult.getResult()).thenReturn(persistedTitles);

		final TitleModel result = defaultTitleDao.findTitleByCode(DOC);

		assertThat(result).isNull();
		verify(flexibleSearchService, times(1)).search(argThat(findTitlesByCodeQueryArgumentMatcher()));
	}

	@Test
	public void testFindTitleByCode()
	{

		final List<TitleModel> persistedTitles = Collections.singletonList(createTitle(DOC));

		final SearchResult searchResult = mock(SearchResult.class);
		when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		when(searchResult.getResult()).thenReturn(persistedTitles);

		final TitleModel result = defaultTitleDao.findTitleByCode(DOC);

		assertThat(result).isNotNull().extracting(TitleModel::getCode).containsExactly(DOC);
		verify(flexibleSearchService, times(1)).search(argThat(findTitlesByCodeQueryArgumentMatcher()));
	}

	private TitleModel createTitle(final String code)
	{
		return createTitle(code, "(" + code + ")");
	}

	private TitleModel createTitle(final String code, final String name)
	{
		final TitleModel title = Mockito.mock(TitleModel.class);
		when(title.getCode()).thenReturn(code);
		when(title.getName()).thenReturn(name);

		return title;
	}

	private static ArgumentMatcher<FlexibleSearchQuery> findTitlesQueryArgumentMatcher()
	{
		final String expectedQuery = "SELECT {c:" + TitleModel.PK + "} FROM {" + TitleModel._TYPECODE + " as c}";
		return getFlexibleSearchQueryArgumentMatcher(expectedQuery);
	}

	private static ArgumentMatcher<FlexibleSearchQuery> findTitlesByCodeQueryArgumentMatcher()
	{
		final String expectedQuery = "SELECT {c:" + TitleModel.PK + "} FROM {" + TitleModel._TYPECODE + " as c} WHERE {c:"
				+ TitleModel.CODE + "}=?code";
		return getFlexibleSearchQueryArgumentMatcher(expectedQuery);
	}

	private static ArgumentMatcher<FlexibleSearchQuery> getFlexibleSearchQueryArgumentMatcher(final String expectedQuery)
	{
		return new ArgumentMatcher<FlexibleSearchQuery>()
		{
			@Override
			public boolean matches(final Object o)
			{

				if (!(o instanceof FlexibleSearchQuery))
				{
					return false;
				}
				return ((FlexibleSearchQuery) o).getQuery().trim().equalsIgnoreCase(expectedQuery);
			}

		};
	}
}
