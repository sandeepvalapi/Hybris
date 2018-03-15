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
package de.hybris.platform.core;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.SQLSearchResult;
import de.hybris.platform.util.config.ConfigIntf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 *
 */
@IntegrationTest
public class SearchResultTest extends HybrisJUnit4Test
{
	private String prefetchSizeBefore = null;

	private final int localPrefetchSize = 10;
	private final int itemsCount = localPrefetchSize * 2;

	private List<PK> itemPKs;

	private static Query multiColumnQueryParams = new Query().setQuery(
			"SELECT 'someText',{code},{code}, {pk} FROM {Title} WHERE {code} LIKE 'Title-%'").setResultClasses(String.class,
			String.class, String.class, Item.class);
	private static Query singleColumnQueryParams = new Query().setQuery("SELECT {PK} FROM {Title} WHERE {code} LIKE 'Title-%'")
			.setResultClasses(Item.class);
	private static Query rawColumnQueryParams = new Query().setQuery("SELECT {code} FROM {Title} WHERE {code} LIKE 'Title-%'")
			.setResultClasses(String.class);

	@Before
	public void prepare() throws JaloGenericCreationException, JaloAbstractTypeException, ConsistencyCheckException
	{
		itemPKs = new ArrayList<PK>();
		for (int i = 0; i < itemsCount; i++)
		{
			itemPKs.add(UserManager.getInstance().createTitle("Title-" + i).getPK());
		}
		prefetchSizeBefore = getConfig().getParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY);
		getConfig().setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, String.valueOf(localPrefetchSize));
		final SessionContext localCtx = jaloSession.createLocalSessionContext();
		localCtx.setAttribute(FlexibleSearch.PREFETCH_SIZE, Integer.valueOf(localPrefetchSize));
		Registry.getCurrentTenantNoFallback().getCache().clear();
	}


	@After
	public void restorePrefetchSize() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		jaloSession.removeLocalSessionContext();

		if (prefetchSizeBefore != null)
		{
			getConfig().setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, prefetchSizeBefore);
		}
	}

	@Test
	public void testResultsBeingCached()
	{
		SearchResult resFirst = query(getNonWrappingFlexibleSearch(), multiColumnQueryParams);
		SearchResult resAgain = query(getNonWrappingFlexibleSearch(), multiColumnQueryParams);
		Assert.assertSame(resFirst, resAgain);
		Assert.assertEquals(itemsCount, resFirst.getCount());

		SearchResult resNotCached = query(getNonWrappingFlexibleSearch(),
				new Query(multiColumnQueryParams).setDontNeedTotal(!multiColumnQueryParams.isDontNeedTotal()));
		Assert.assertNotSame(resFirst, resNotCached);
		Assert.assertEquals(itemsCount, resNotCached.getCount());

		// single column
		resFirst = query(getNonWrappingFlexibleSearch(), singleColumnQueryParams);
		resAgain = query(getNonWrappingFlexibleSearch(), singleColumnQueryParams);
		Assert.assertSame(resFirst, resAgain);
		Assert.assertEquals(itemsCount, resFirst.getCount());

		resNotCached = query(getNonWrappingFlexibleSearch(),
				new Query(singleColumnQueryParams).setDontNeedTotal(!multiColumnQueryParams.isDontNeedTotal()));
		Assert.assertNotSame(resFirst, resNotCached);
		Assert.assertEquals(itemsCount, resNotCached.getCount());
	}

	@Test
	public void testResults()
	{
		SearchResult result = query(getWrappingFlexibleSearch(), singleColumnQueryParams);
		Assert.assertEquals(LazyLoadItemList.class, ((SQLSearchResult) result).getOriginalResultList().getClass());
		Assert.assertEquals(itemsCount, result.getCount());
		final List<Title> items = result.getResult();
		Assert.assertEquals(itemsCount, items.size());
		final Set<PK> expectedPKs = new HashSet<PK>(itemPKs);
		for (final Title t : items)
		{
			Assert.assertNotNull(t);
			Assert.assertTrue(expectedPKs.remove(t.getPK()));
		}

		result = query(getWrappingFlexibleSearch(), rawColumnQueryParams);
		Assert.assertEquals(ArrayList.class, ((SQLSearchResult) result).getOriginalResultList().getClass());
		final List<String> codes = result.getResult();
		Assert.assertEquals(itemsCount, codes.size());
		for (final String code : codes)
		{
			Assert.assertNotNull(code);
			Assert.assertTrue(code.startsWith("Title-"));
		}

		result = query(getWrappingFlexibleSearch(), multiColumnQueryParams);
		Assert.assertEquals(LazyLoadMultiColumnList.class, ((SQLSearchResult) result).getOriginalResultList().getClass());
		final List<List<Object>> rows = result.getResult();
		Assert.assertEquals(itemsCount, rows.size());
		// "SELECT 'someText',{code},{code}, {pk} FROM {Title} WHERE {code} LIKE 'Title-%'"
		for (final List<Object> row : rows)
		{
			final Title title = (Title) row.get(3);
			Assert.assertNotNull(title);
			Assert.assertEquals("someText", row.get(0));
			Assert.assertEquals(title.getCode(), row.get(1));
			Assert.assertTrue(((String) row.get(1)).startsWith("Title-"));
			Assert.assertEquals(title.getCode(), row.get(2));
		}
	}

	private SearchResult query(final FlexibleSearch flexibleSearch, final Query query)
	{
		return flexibleSearch.search(//
				query.getQuery(),//
				query.getValues(), //
				query.getResultClasses(), //
				query.isFailOnUnknownFields(),//
				query.isDontNeedTotal(),//
				query.getStart(),//
				query.getCount()//
				);
	}

	static class Query
	{
		String dbQuery;
		Map values = Collections.EMPTY_MAP;
		List resultClasses;
		boolean failOnUnknownFields = true;
		boolean dontNeedTotal = true;
		int start = 0;
		int count = -1;

		Query()
		{
			//
		}

		Query(final Query query)
		{
			this.dbQuery = query.dbQuery;
			this.values = query.values;
			this.resultClasses = query.resultClasses;
			this.failOnUnknownFields = query.failOnUnknownFields;
			this.dontNeedTotal = query.dontNeedTotal;
			this.start = query.start;
			this.count = query.count;
		}

		String getQuery()
		{
			return dbQuery;
		}

		Query setQuery(final String query)
		{
			this.dbQuery = query;
			return this;
		}

		Map getValues()
		{
			return values;
		}

		Query setValues(final Map values)
		{
			this.values = values;
			return this;
		}

		List getResultClasses()
		{
			return resultClasses;
		}

		Query setResultClasses(final Class... resultClasses)
		{
			return setResultClasses(Arrays.asList(resultClasses));
		}

		Query setResultClasses(final List resultClasses)
		{
			this.resultClasses = resultClasses;
			return this;
		}

		boolean isFailOnUnknownFields()
		{
			return failOnUnknownFields;
		}

		Query setFailOnUnknownFields(final boolean failOnUnknownFields)
		{
			this.failOnUnknownFields = failOnUnknownFields;
			return this;
		}

		boolean isDontNeedTotal()
		{
			return dontNeedTotal;
		}

		Query setDontNeedTotal(final boolean dontNeedTotal)
		{
			this.dontNeedTotal = dontNeedTotal;
			return this;
		}

		int getStart()
		{
			return start;
		}

		Query setStart(final int start)
		{
			this.start = start;
			return this;
		}

		int getCount()
		{
			return count;
		}

		Query setCount(final int count)
		{
			this.count = count;
			return this;
		}
	}

	static class NonWrappingFlexibleSearch extends FlexibleSearch
	{
		@Override
		protected SearchResult wrapSearchResult(final SearchResult cachedSearchResult) throws Exception
		{
			return cachedSearchResult;
		}
	}

	private FlexibleSearch getNonWrappingFlexibleSearch()
	{
		return new NonWrappingFlexibleSearch();
	}

	private FlexibleSearch getWrappingFlexibleSearch()
	{
		return FlexibleSearch.getInstance();
	}

	private ConfigIntf getConfig()
	{
		return Registry.getCurrentTenant().getConfig();
	}
}
