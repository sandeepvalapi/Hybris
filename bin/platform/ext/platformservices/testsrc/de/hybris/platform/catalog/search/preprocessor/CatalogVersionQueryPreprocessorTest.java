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
package de.hybris.platform.catalog.search.preprocessor;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.preprocessor.QueryPreprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CatalogVersionQueryPreprocessorTest
{
	@InjectMocks
	private final QueryPreprocessor preprocessor = new CatalogVersionQueryPreprocessor();
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private FlexibleSearchQuery query;
	@Mock
	private CatalogVersionModel catalogVersion;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldNotProcessWhenCatalogVersionsInQueryObjectIsNull() // NOPMD
	{
		// given
		given(query.getCatalogVersions()).willReturn(null);

		// when
		preprocessor.process(query);

		// then
		verify(catalogVersionService, times(0)).setSessionCatalogVersions((Collection) anyObject());
	}

	@Test
	public void shouldNotProcessWhenCatalogVersionsInQueryObjectIsEmpty() // NOPMD
	{
		// given
		given(query.getCatalogVersions()).willReturn(Collections.EMPTY_LIST);

		// when
		preprocessor.process(query);

		// then
		verify(catalogVersionService, times(0)).setSessionCatalogVersions(Collections.EMPTY_LIST);
	}

	@Test
	public void shouldProcessWhenThereIsCollectionOfCatalogVersionsInQueryObject() // NOPMD
	{
		// given
		final Collection<CatalogVersionModel> catalogVersions = new ArrayList<CatalogVersionModel>();
		catalogVersions.add(catalogVersion);
		given(query.getCatalogVersions()).willReturn(catalogVersions);

		// when
		preprocessor.process(query);

		// then
		verify(catalogVersionService, times(1)).setSessionCatalogVersions(catalogVersions);
	}

}
