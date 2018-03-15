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
package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.servicelayer.cronjob.JobLogDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultJobLogDaoTest
{

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private SearchResult searchResult;

	private JobLogDao dao;

	private final String expectedQueryAsc = "SELECT {c:" + JobLogModel.PK + "} FROM {" + JobLogModel._TYPECODE
			+ " AS c} WHERE {c:" + JobLogModel.CRONJOB + "}=?" + JobLogModel.CRONJOB + " ORDER BY {c:" + JobLogModel.CREATIONTIME
			+ "} asc";

	private final String expectedQueryDsc = "SELECT {c:" + JobLogModel.PK + "} FROM {" + JobLogModel._TYPECODE
			+ " AS c} WHERE {c:" + JobLogModel.CRONJOB + "}=?" + JobLogModel.CRONJOB + " ORDER BY {c:" + JobLogModel.CREATIONTIME
			+ "} desc";

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		dao = Mockito.spy(new TestDefaultJobLogDao(flexibleSearchService));

	}

	@Test
	public void testQueryBuildCustomCountAsc()
	{

		final CronJobModel model = Mockito.mock(CronJobModel.class);

		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);

		dao.findJobLogs(model, 100, true);//count 100, asc

		final ArgumentMatcher<FlexibleSearchQuery> matcher = new ArgumentMatcher<FlexibleSearchQuery>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				Assert.assertTrue(argument instanceof FlexibleSearchQuery);
				final FlexibleSearchQuery query = (FlexibleSearchQuery) argument;
				Assert.assertEquals(100, query.getCount());
				Assert.assertEquals(false, query.isNeedTotal());
				Assert.assertEquals(expectedQueryAsc, query.getQuery());
				return true;
			}

		};
		Mockito.verify(flexibleSearchService).search(Mockito.argThat(matcher));

	}

	@Test
	public void testQueryBuildNoCountDesc()
	{

		final CronJobModel model = Mockito.mock(CronJobModel.class);

		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		dao.findJobLogs(model, -1, false);//count -1, desc

		final ArgumentMatcher<FlexibleSearchQuery> matcher = new ArgumentMatcher<FlexibleSearchQuery>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				/*
				 * if (!(argument instanceof FlexibleSearchQuery)) { return false; } final FlexibleSearchQuery query =
				 * (FlexibleSearchQuery) argument; if (query.getCount() == -1 && query.isNeedTotal() &&
				 * expectedQuery.equalsIgnoreCase(query.getQuery())) { return true; }
				 */

				Assert.assertTrue(argument instanceof FlexibleSearchQuery);
				final FlexibleSearchQuery query = (FlexibleSearchQuery) argument;
				Assert.assertEquals(-1, query.getCount());
				Assert.assertEquals(true, query.isNeedTotal());
				Assert.assertEquals(expectedQueryDsc, query.getQuery());

				return true;
			}

		};
		Mockito.verify(flexibleSearchService).search(Mockito.argThat(matcher));

	}


	public static class TestDefaultJobLogDao extends DefaultJobLogDao
	{
		private final FlexibleSearchService mock;

		public TestDefaultJobLogDao(final FlexibleSearchService mock)
		{
			super();
			this.mock = mock;
		}

		@Override
		protected FlexibleSearchService getFlexibleSearchService()
		{
			return mock;
		}
	}
}
