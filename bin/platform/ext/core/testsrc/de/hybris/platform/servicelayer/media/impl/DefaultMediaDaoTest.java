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
package de.hybris.platform.servicelayer.media.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
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


@UnitTest
public class DefaultMediaDaoTest
{
	@Mock
	private FlexibleSearchService service;

	private DefaultMediaDao dao;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		dao = new DefaultMediaDao();
		dao.setFlexibleSearchService(service);
	}

	@Test
	public void testCaseFindMediaByCode()
	{
		final SearchResult result = Mockito.mock(SearchResult.class);

		Mockito.when(service.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		dao.findMediaByCode("qualifierFoo");

		Mockito.verify(service).search(Mockito.eq("SELECT {pk} FROM {Media} WHERE {code}=?code ORDER BY {pk} ASC"),
				Mockito.argThat(new ArgumentMatcher<Map<String, Object>>()
				{

					@Override
					public boolean matches(final Object argument)
					{
						Assert.assertTrue(argument instanceof Map);
						final Map arguments = (Map) argument;
						Assert.assertEquals("qualifierFoo", arguments.get(MediaModel.CODE));
						return true;
					}
				}));
	}

	@Test
	public void testCaseFindMediaByCodeAndCatalogVersion()
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();

		final SearchResult result = Mockito.mock(SearchResult.class);

		Mockito.when(service.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		dao.findMediaByCode(catalogVersion, "qualifierFoo");

		Mockito.verify(service).search(
				Mockito.eq("SELECT {pk} FROM {Media} WHERE {CatalogVersion}=?catalogVersion AND {code}=?code ORDER BY {pk} ASC"),
				Mockito.argThat(new ArgumentMatcher<Map<String, Object>>()
				{

					@Override
					public boolean matches(final Object argument)
					{
						Assert.assertTrue(argument instanceof Map);
						final Map arguments = (Map) argument;
						Assert.assertEquals("qualifierFoo", arguments.get(MediaModel.CODE));
						Assert.assertEquals(catalogVersion, arguments.get(MediaModel.CATALOGVERSION));
						return true;
					}
				}));
	}

	@Test
	public void testCaseFindMediaByCodeAndNullCatalogVersion()
	{

		final SearchResult result = Mockito.mock(SearchResult.class);

		Mockito.when(service.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		dao.findMediaByCode(null, "qualifierFoo");

		Mockito.verify(service).search(
				Mockito.eq("SELECT {pk} FROM {Media} WHERE {CatalogVersion}=?catalogVersion AND {code}=?code ORDER BY {pk} ASC"),
				Mockito.argThat(new ArgumentMatcher<Map<String, Object>>()
				{

					@Override
					public boolean matches(final Object argument)
					{
						Assert.assertTrue(argument instanceof Map);
						final Map arguments = (Map) argument;
						Assert.assertEquals("qualifierFoo", arguments.get(MediaModel.CODE));
						Assert.assertEquals(null, arguments.get(MediaModel.CATALOGVERSION));
						return true;
					}
				}));
	}

	@Test
	public void testCaseFindMediaFolderByQualifier()
	{
		final SearchResult result = Mockito.mock(SearchResult.class);

		Mockito.when(service.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		dao.findMediaFolderByQualifier("qualifierFoo");

		Mockito.verify(service).search(Mockito.eq("SELECT {pk} FROM {MediaFolder} WHERE {qualifier}=?qualifier ORDER BY {pk} ASC"),
				Mockito.argThat(new ArgumentMatcher<Map<String, Object>>()
				{

					@Override
					public boolean matches(final Object argument)
					{
						Assert.assertTrue(argument instanceof Map);
						final Map arguments = (Map) argument;
						Assert.assertEquals("qualifierFoo", arguments.get(MediaFolderModel.QUALIFIER));
						return true;
					}
				}));
	}


	@Test
	public void testCaseFindMediaFormatByQualifier()
	{
		final SearchResult result = Mockito.mock(SearchResult.class);

		Mockito.when(service.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(result);

		dao.findMediaFormatByQualifier("qualifierFoo");

		Mockito.verify(service).search(Mockito.eq("SELECT {pk} FROM {MediaFormat} WHERE {qualifier}=?qualifier ORDER BY {pk} ASC"),
				Mockito.argThat(new ArgumentMatcher<Map<String, Object>>()
				{

					@Override
					public boolean matches(final Object argument)
					{
						Assert.assertTrue(argument instanceof Map);
						final Map arguments = (Map) argument;
						Assert.assertEquals("qualifierFoo", arguments.get(MediaFormatModel.QUALIFIER));
						return true;
					}
				}));
	}

}
