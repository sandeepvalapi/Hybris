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
package de.hybris.platform.jobs;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CleanUpCronJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.TimeZone;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


/**
 * Test covers {@link CleanUpJobPerformable} logic.
 */
@UnitTest
public class CleanUpJobPerformableTest
{
	private CleanUpJobPerformable performable;

	@Mock
	private I18NService i18nService;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private ModelService modelService;


	private static final String WITHOUT_EXCLUDED_QUERY = "SELECT {pk} FROM {CronJob AS c} WHERE {c.pk} NOT IN ({{  SELECT {cronJob}   FROM {Trigger}   WHERE {cronJob} IS NOT NULL}}) AND {status} IN ( ?status ) AND {result} IN ( ?result ) AND {endTime} < ?date ";

	private static final String WITH_EXCLUDED_QUERY = "SELECT {pk} FROM {CronJob AS c} WHERE {c.pk} NOT IN ({{  SELECT {cronJob}   FROM {Trigger}   WHERE {cronJob} IS NOT NULL}}) AND {c.pk} NOT IN ( ?excludedCronJobs ) AND {status} IN ( ?status ) AND {result} IN ( ?result ) AND {endTime} < ?date ";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		performable = new CleanUpJobPerformable();
		performable.setI18nService(i18nService);
		performable.setFlexibleSearchService(flexibleSearchService);
		performable.setModelService(modelService);

		Mockito.when(i18nService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());
		Mockito.when(i18nService.getCurrentLocale()).thenReturn(Locale.getDefault());
	}

	@Test
	public void testEmptyCleanUpCronJob()
	{
		final PerformResult result = performable.perform(null);
		Assert.assertEquals(CronJobResult.ERROR, result.getResult());
		Assert.assertEquals(CronJobStatus.ABORTED, result.getStatus());
	}

	@Test
	public void testCleanUpCronJobWithEmptyExcludeList()
	{

		final Collection specificCollection = Mockito.mock(Collection.class);
		final CleanUpCronJobModel cronJobModel = new CleanUpCronJobModel();
		cronJobModel.setXDaysOld(100);
		cronJobModel.setExcludeCronJobs(Collections.EMPTY_LIST);//empty exclude list
		cronJobModel.setStatuscoll(specificCollection);
		cronJobModel.setResultcoll(specificCollection);
		//one PK in result
		final CronJobModel one = Mockito.mock(CronJobModel.class);
		Mockito.when(one.getCode()).thenReturn("expectedCode");
		final Stack<SearchResult<CronJobModel>> stackOfResults = new Stack<SearchResult<CronJobModel>>();
		stackOfResults.add(createSearchResultMock(one));

		Mockito.doAnswer(new Answer<SearchResult<CronJobModel>>()
		{

			@Override
			public SearchResult<CronJobModel> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				Assert.assertTrue(args[0] instanceof String);
				final String argString = (String) args[0];
				Assert.assertEquals(WITHOUT_EXCLUDED_QUERY, argString);
				Assert.assertTrue(args[1] instanceof Map);
				final Map argMap = (Map) args[1];
				Assert.assertEquals(argMap.get("result"), specificCollection);
				Assert.assertEquals(argMap.get("status"), specificCollection);
				Assert.assertNotNull(argMap.get("date"));
				Assert.assertNull(argMap.get("excludedCronJobs"));
				try
				{
					return stackOfResults.pop();
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(SearchResult.class);
				}
			}

		}).when(flexibleSearchService).search(Mockito.anyString(), Mockito.anyMap());

		final PerformResult result = performable.perform(cronJobModel);

		Mockito.verify(modelService).remove(one);

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
	}

	@Test
	public void testCleanUpCronJobWithNotEmptyExcludeList()
	{

		final Collection specificCollection = Mockito.mock(Collection.class);
		final List<CronJobModel> excludedCollection = Mockito.mock(List.class);

		final CleanUpCronJobModel cronJobModel = new CleanUpCronJobModel();
		cronJobModel.setXDaysOld(100);
		cronJobModel.setExcludeCronJobs(excludedCollection);//empty exclude list
		cronJobModel.setStatuscoll(specificCollection);
		cronJobModel.setResultcoll(specificCollection);
		//one PK in result
		final CronJobModel one = Mockito.mock(CronJobModel.class);
		Mockito.when(one.getCode()).thenReturn("expectedCode");
		final Stack<SearchResult<CronJobModel>> stackOfResults = new Stack<SearchResult<CronJobModel>>();
		stackOfResults.add(createSearchResultMock(one));

		Mockito.doAnswer(new Answer<SearchResult<CronJobModel>>()
		{

			@Override
			public SearchResult<CronJobModel> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				Assert.assertTrue(args[0] instanceof String);
				final String argString = (String) args[0];
				Assert.assertEquals(WITH_EXCLUDED_QUERY, argString);
				Assert.assertTrue(args[1] instanceof Map);
				final Map argMap = (Map) args[1];
				Assert.assertEquals(argMap.get("result"), specificCollection);
				Assert.assertEquals(argMap.get("status"), specificCollection);
				Assert.assertNotNull(argMap.get("date"));
				Assert.assertEquals(argMap.get("excludedCronJobs"), excludedCollection);
				try
				{
					return stackOfResults.pop();
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(SearchResult.class);
				}
			}

		}).when(flexibleSearchService).search(Mockito.anyString(), Mockito.anyMap());

		final PerformResult result = performable.perform(cronJobModel);

		Mockito.verify(modelService).remove(one);

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
	}

	/**
	 * Creates a mock of result returning one row as integer value of <code>resultCount</code>
	 */
	private SearchResult<CronJobModel> createSearchResultMock(final CronJobModel... models)
	{
		final List<CronJobModel> underlyingResult = Arrays.asList(models);

		final SearchResult<CronJobModel> result = Mockito.mock(SearchResult.class);
		Mockito.when(Integer.valueOf(result.getCount())).thenReturn(Integer.valueOf(models.length));
		Mockito.when(Integer.valueOf(result.getTotalCount())).thenReturn(Integer.valueOf(models.length));
		Mockito.when(result.getResult()).thenReturn(underlyingResult);
		return result;
	}

}
