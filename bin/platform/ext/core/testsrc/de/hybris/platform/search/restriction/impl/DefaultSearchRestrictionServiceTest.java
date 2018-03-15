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
package de.hybris.platform.search.restriction.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.jalo.flexiblesearch.ContextQueryFilter;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.search.restriction.dao.SearchRestrictionDao;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import de.hybris.platform.search.restriction.session.converter.SessionSearchRestrictionConverter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultSearchRestrictionServiceTest
{
	@InjectMocks
	private final DefaultSearchRestrictionService searchRestrictionService = new DefaultSearchRestrictionService();
	@Mock
	private ComposedTypeModel restrictedTypeMock1;
	@Mock
	private ComposedTypeModel restrictedTypeMock2;
	@Mock
	private ComposedTypeModel restrictedTypeMock3;
	@Mock
	private ComposedTypeModel restrictedTypeMock4;
	@Mock
	private ComposedTypeModel restrictedTypeMock5;
	@Mock
	private SessionService sessionServiceMock;
	@Mock
	private SearchRestrictionDao searchRestrictionDaoMock;
	@Mock
	private SearchRestrictionModel searchRestrictionModelMock1;
	@Mock
	private PrincipalModel principal;
	@Mock
	private SessionSearchRestrictionConverter converterMock;

	private final Collection<SessionSearchRestriction> sessionRestrictions = new ArrayList<SessionSearchRestriction>();
	private final Collection<SessionSearchRestriction> sessionRestrictionsToAdd = new ArrayList<SessionSearchRestriction>();
	private final Collection<ContextQueryFilter> rawQueryFilters = new ArrayList<ContextQueryFilter>();
	private final Collection<ContextQueryFilter> rawQueryFiltersToRemove = new ArrayList<ContextQueryFilter>();


	@Before
	public void setUp() throws Exception
	{
		// Init mocks
		MockitoAnnotations.initMocks(this);

		// Set up session search restrictions collection
		sessionRestrictions.add(new SessionSearchRestriction("fooBar", "foo bar", restrictedTypeMock1));
		sessionRestrictions.add(new SessionSearchRestriction("barBaz", "bar baz", restrictedTypeMock2));
		sessionRestrictions.add(new SessionSearchRestriction("bazBoom", "baz boom", restrictedTypeMock3));

		sessionRestrictionsToAdd.add(new SessionSearchRestriction("someAdd", "some add", restrictedTypeMock4));
		sessionRestrictionsToAdd.add(new SessionSearchRestriction("anotherAdd", "another add", restrictedTypeMock5));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#addSessionSearchRestrictions(java.util.Collection)}
	 * 
	 * Checks for setting restrictions collection when there was no restrictions in the session at all.
	 */
	@Test
	public void testAddSessionSearchRestrictions()
	{
		// Setup prerequisites
		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS)).thenReturn(null);
		when(converterMock.convertFromRestrictions(sessionRestrictionsToAdd)).thenReturn(rawQueryFilters);

		// Invoke tested method on service
		searchRestrictionService.addSessionSearchRestrictions(sessionRestrictionsToAdd);

		// Verify mocks
		verify(converterMock, times(1)).convertFromRestrictions(sessionRestrictionsToAdd);
		verify(sessionServiceMock, times(1)).getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);
		verify(sessionServiceMock, times(1)).setAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS, rawQueryFilters);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#addSessionSearchRestrictions(java.util.Collection)}
	 * 
	 * Checks for setting empty restrictions collection when there was no restrictions in the session at all.
	 */
	@Test
	public void testAddSessionSearchRestrictionsEmptyCollectionInitial()
	{
		// Setup prerequisites
		final Collection restrictions = Collections.EMPTY_LIST;
		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS)).thenReturn(null);

		// Invoke tested method on service
		searchRestrictionService.addSessionSearchRestrictions(restrictions);

		// Verify mocks
		verify(sessionServiceMock, times(1)).getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);
		verify(sessionServiceMock, times(1)).setAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS, restrictions);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSessionSearchRestrictionsWithNullParameter()
	{
		final Collection<SessionSearchRestriction> restrictions = null;
		searchRestrictionService.addSessionSearchRestrictions(restrictions);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#clearSessionSearchRestrictions()}
	 * .
	 */
	@Test
	public void testClearSessionSearchRestrictions()
	{
		// Invoke tested method on service
		searchRestrictionService.clearSessionSearchRestrictions();

		// Verify mocks
		verify(sessionServiceMock, times(1)).removeAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#disableSearchRestrictions()} .
	 */
	@Test
	public void testDisableSearchRestrictions()
	{
		// Invoke tested method on service
		searchRestrictionService.disableSearchRestrictions();

		// Verify mocks
		verify(sessionServiceMock, times(1)).setAttribute(DefaultSearchRestrictionService.DISABLE_RESTRICTIONS, Boolean.TRUE);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#enableSearchRestrictions()} .
	 */
	@Test
	public void testEnableSearchRestrictions()
	{
		// Invoke tested method on service
		searchRestrictionService.enableSearchRestrictions();

		// Verify mocks
		verify(sessionServiceMock, times(1)).setAttribute(DefaultSearchRestrictionService.DISABLE_RESTRICTIONS, Boolean.FALSE);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#getSessionSearchRestrictions()}
	 * 
	 * Test getting sessions search restrictions when currently there are some restrictions in the session.
	 * 
	 */
	@Test
	public void testGetSessionSearchRestrictions()
	{
		// Setup prerequisites
		rawQueryFilters.add(mock(ContextQueryFilter.class));
		rawQueryFilters.add(mock(ContextQueryFilter.class));
		rawQueryFilters.add(mock(ContextQueryFilter.class));
		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS)).thenReturn(rawQueryFilters);
		when(converterMock.convertFromFilters(rawQueryFilters)).thenReturn(sessionRestrictions);

		// Invoke tested method on service
		final Collection<SessionSearchRestriction> searchRestrictions = searchRestrictionService.getSessionSearchRestrictions();

		// Verify mocks
		verify(sessionServiceMock, times(1)).getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);

		assertThat(searchRestrictions).hasSize(3);
	}

	@Test
	public void testHasRestrictions()
	{
		// Setup prerequisites
		rawQueryFilters.add(mock(ContextQueryFilter.class));
		rawQueryFilters.add(mock(ContextQueryFilter.class));
		rawQueryFilters.add(mock(ContextQueryFilter.class));
		final Collection<ComposedTypeModel> types = new ArrayList<ComposedTypeModel>();
		types.add(restrictedTypeMock1);
		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS)).thenReturn(rawQueryFilters);
		when(converterMock.convertFromFilters(rawQueryFilters)).thenReturn(sessionRestrictions);

		// Invoke tested method on service
		final boolean restrictionsForTypeMock1 = searchRestrictionService.hasRestrictions(principal, false, restrictedTypeMock1);

		assertThat(restrictionsForTypeMock1).isTrue();
		verify(sessionServiceMock, times(1)).getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);
		verify(searchRestrictionDaoMock, never()).findSearchRestrictionsByPrincipalAndType(principal, false, types);

		// Reset mocks before next test part
		reset(sessionServiceMock);
		reset(searchRestrictionDaoMock);

		// Re-run test for restrictedTypeMock5
		final Collection<ComposedTypeModel> types2 = new ArrayList<ComposedTypeModel>();
		types2.add(restrictedTypeMock5);
		final List<SearchRestrictionModel> restrictions = new ArrayList<SearchRestrictionModel>();
		restrictions.add(searchRestrictionModelMock1);
		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS)).thenReturn(
				sessionRestrictions);
		when(searchRestrictionDaoMock.findSearchRestrictionsByPrincipalAndType(principal, false, types2)).thenReturn(
				Collections.EMPTY_LIST);

		// Invoke tested method on service
		final boolean restrictionsForTypeMock5 = searchRestrictionService.hasRestrictions(principal, false, restrictedTypeMock5);

		assertThat(restrictionsForTypeMock5).isFalse();
		verify(sessionServiceMock, times(1)).getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);
		verify(searchRestrictionDaoMock, times(1)).findSearchRestrictionsByPrincipalAndType(principal, false, types2);
	}

	@Test
	public void testGetSessionSearchRestrictionsForType()
	{
		// Setup prerequisites
		rawQueryFilters.add(mock(ContextQueryFilter.class));
		rawQueryFilters.add(mock(ContextQueryFilter.class));
		rawQueryFilters.add(mock(ContextQueryFilter.class));
		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS)).thenReturn(rawQueryFilters);
		when(converterMock.convertFromFilters(rawQueryFilters)).thenReturn(sessionRestrictions);

		// Invoke tested method on service
		final Collection<SessionSearchRestriction> foundRestrictions = searchRestrictionService
				.getSessionSearchRestrictions(restrictedTypeMock1);

		assertThat(foundRestrictions).isNotEmpty();
		assertThat(foundRestrictions.size()).isEqualTo(1);
		assertThat(foundRestrictions.iterator().next().getCode()).isEqualTo("fooBar");

		verify(sessionServiceMock, times(1)).getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);

		// Re-run test for not existend restriction in the session
		reset(sessionServiceMock);

		// Setup prerequisites
		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS)).thenReturn(
				sessionRestrictions);

		// Invoke tested method on service again
		final Collection<SessionSearchRestriction> foundRestrictions2 = searchRestrictionService
				.getSessionSearchRestrictions(restrictedTypeMock5);

		assertThat(foundRestrictions2).isEmpty();

		verify(sessionServiceMock, times(1)).getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#getSessionSearchRestrictions()}
	 * 
	 * Test getting sessions search restrictions when currently there is no sessions search restrictions at all.
	 * 
	 */
	@Test
	public void testGetSessionSearchRestrictionsInitial()
	{
		// Setup prerequisites
		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS)).thenReturn(null);

		// Invoke tested method on service
		final Collection<SessionSearchRestriction> searchRestrictions = searchRestrictionService.getSessionSearchRestrictions();

		// Verify mocks
		verify(sessionServiceMock, times(1)).getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);

		assertThat(searchRestrictions).isEqualTo(Collections.EMPTY_LIST);
	}

	@Test
	public void testIsSearchRestrictionsEnabled()
	{
		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.DISABLE_RESTRICTIONS)).thenReturn(Boolean.FALSE);
		assertThat(searchRestrictionService.isSearchRestrictionsEnabled()).isTrue();

		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.DISABLE_RESTRICTIONS)).thenReturn(Boolean.TRUE);
		assertThat(searchRestrictionService.isSearchRestrictionsEnabled()).isFalse();

		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.DISABLE_RESTRICTIONS)).thenReturn("Some fake");
		assertThat(searchRestrictionService.isSearchRestrictionsEnabled()).isFalse();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#removeSessionSearchRestrictions(java.util.Collection)}
	 * .
	 */
	@Test
	public void testRemoveSessionSearchRestrictions()
	{
		// Setup prerequisites
		final Collection<SessionSearchRestriction> sessionRestrictionsToRemove = new ArrayList<SessionSearchRestriction>();
		sessionRestrictionsToRemove.add(new SessionSearchRestriction("fooBar", "some query", restrictedTypeMock1));
		final ContextQueryFilter contextQueryFilter1 = new ContextQueryFilter("fooBar", mock(ComposedType.class), "some query");
		final ContextQueryFilter contextQueryFilter2 = new ContextQueryFilter("bazDam", mock(ComposedType.class), "some query");
		rawQueryFilters.add(contextQueryFilter1);
		rawQueryFilters.add(contextQueryFilter2);
		rawQueryFiltersToRemove.add(contextQueryFilter1);

		when(sessionServiceMock.getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS)).thenReturn(rawQueryFilters);
		when(converterMock.convertFromRestrictions(sessionRestrictionsToRemove)).thenReturn(rawQueryFiltersToRemove);

		// Invoke tested method on service
		searchRestrictionService.removeSessionSearchRestrictions(sessionRestrictionsToRemove);

		// Verify mocks
		verify(sessionServiceMock, times(1)).getAttribute(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS);
		verify(sessionServiceMock, times(1)).setAttribute(eq(DefaultSearchRestrictionService.CTX_SEARCH_RESTRICTIONS),
				anyCollection());
		verify(converterMock, times(1)).convertFromRestrictions(sessionRestrictionsToRemove);
	}
}
