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
package de.hybris.platform.servicelayer.media.dao.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for a {@link DefaultMediaContainerDao} logic.
 */
@UnitTest
public class DefaultMediaContainerDaoTest
{
	@Mock
	private FlexibleSearchService service;

	private DefaultMediaContainerDao dao;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		dao = new DefaultMediaContainerDao();
		dao.setFlexibleSearchService(service);
	}

	@Test
	public void testCaseFindMediaContainerByQualifier()
	{
		final SearchResult result = Mockito.mock(SearchResult.class);

		Mockito.when(service.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		dao.findMediaContainersByQualifier("qualifierFoo");

		Mockito.verify(service).search(
				Mockito.eq("SELECT {pk} FROM {MediaContainer} WHERE {qualifier}=?qualifier ORDER BY {pk} ASC"),
				Mockito.argThat(new ArgumentMatcher<Map<String, Object>>()
				{

					@Override
					public boolean matches(final Object argument)
					{
						Assert.assertTrue(argument instanceof Map);
						final Map arguments = (Map) argument;
						Assert.assertEquals("qualifierFoo", arguments.get(MediaContainerModel.QUALIFIER));
						return true;
					}
				}));

	}

	@Test
	public void testCaseFindMediaContextByQualifier()
	{
		final SearchResult result = Mockito.mock(SearchResult.class);

		Mockito.when(service.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		dao.findMediaContextByQualifier("qualifierFoo");

		Mockito.verify(service).search(
				Mockito.eq("SELECT {pk} FROM {MediaContext} WHERE {qualifier}=?qualifier ORDER BY {pk} ASC"),
				Mockito.argThat(new ArgumentMatcher<Map<String, Object>>()
				{

					@Override
					public boolean matches(final Object argument)
					{
						Assert.assertTrue(argument instanceof Map);
						final Map arguments = (Map) argument;
						Assert.assertEquals("qualifierFoo", arguments.get(MediaContainerModel.QUALIFIER));
						return true;
					}
				}));

	}
}
