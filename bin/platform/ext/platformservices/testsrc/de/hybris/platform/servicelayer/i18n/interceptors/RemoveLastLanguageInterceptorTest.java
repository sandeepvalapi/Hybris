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
package de.hybris.platform.servicelayer.i18n.interceptors;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionService;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


/**
 * Test covering the {@link RemoveLastLanguageInterceptor} logic.
 */
@UnitTest
public class RemoveLastLanguageInterceptorTest
{

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private ModelService modelService;


	@Mock
	private InterceptorContext interceptorContext;

	@Mock
	private SearchResult<Integer> searchResult;

	@Mock
	private Session session;


	//Real objects
	private SessionService sessionService;


	//interceptor instance
	private RemoveLastLanguageInterceptor interceptor;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		//set up stub session service
		sessionService = new TestDefaultSessionService(session);
		((DefaultSessionService) sessionService).setModelService(modelService);

		//prepare interceptor
		interceptor = new RemoveLastLanguageInterceptor();
		interceptor.setFlexibleSearchService(flexibleSearchService);
		interceptor.setSessionService(sessionService);
	}

	@Test
	public void testRemoveInterceptorNoLanguageModel() throws InterceptorException
	{
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(null);

		interceptor.onRemove(new ProductModel(), interceptorContext);

		verify(flexibleSearchService, Mockito.never()).search(Mockito.any(FlexibleSearchQuery.class));
	}


	/**
	 * Case when the {@link FlexibleSearchService#search(FlexibleSearchQuery)} returns as result 100
	 */
	@Test
	public void testRemoveInterceptorLanguageModelPossible() throws InterceptorException
	{
		//result returns 100 of existing languages
		final Integer alotLanguages = Integer.valueOf(100);
		final List<Integer> alotResult = Arrays.asList(alotLanguages);

		doNothing().when(session).setAttribute("disableRestrictions", Boolean.TRUE);
		when(modelService.toPersistenceLayer(Boolean.TRUE)).thenReturn(Boolean.TRUE);
		when(searchResult.getResult()).thenReturn(alotResult);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenAnswer(new Answer<SearchResult<Integer>>()
		{
			@Override
			public SearchResult<Integer> answer(final InvocationOnMock invocation) throws Throwable
			{
				return searchResult;
			}
		});
		interceptor.onRemove(new LanguageModel(), interceptorContext);

		verify(flexibleSearchService, Mockito.times(1)).search(Mockito.any(FlexibleSearchQuery.class));
		verify(session, Mockito.times(1)).setAttribute("disableRestrictions", Boolean.TRUE);
	}

	/**
	 * Case when the {@link FlexibleSearchService#search(FlexibleSearchQuery)} returns as result 1
	 */
	@Test
	public void testRemoveInterceptorLanguageModelImPossible1() throws InterceptorException
	{
		//result returns 1 of existing language
		final Integer alotLanguages = Integer.valueOf(1);
		final List<Integer> alotResult = Arrays.asList(alotLanguages);

		doNothing().when(session).setAttribute("disableRestrictions", Boolean.TRUE);
		when(modelService.toPersistenceLayer(Boolean.TRUE)).thenReturn(Boolean.TRUE);
		when(searchResult.getResult()).thenReturn(alotResult);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenAnswer(new Answer<SearchResult<Integer>>()
		{
			@Override
			public SearchResult<Integer> answer(final InvocationOnMock invocation) throws Throwable
			{
				return searchResult;
			}
		});

		try
		{
			interceptor.onRemove(new LanguageModel(), interceptorContext);
			Assert.fail("Should not able to remove last language ");
		}
		catch (final InterceptorException ie)
		{
			//
		}

		verify(flexibleSearchService, Mockito.times(1)).search(Mockito.any(FlexibleSearchQuery.class));
		verify(session, Mockito.times(1)).setAttribute("disableRestrictions", Boolean.TRUE);
	}

	/**
	 * Case when the {@link FlexibleSearchService#search(FlexibleSearchQuery)} returns as result 0
	 */
	@Test
	public void testRemoveInterceptorLanguageModelImPossible0() throws InterceptorException
	{
		//result returns 0 of existing language
		final Integer alotLanguages = Integer.valueOf(0);
		final List<Integer> alotResult = Arrays.asList(alotLanguages);

		doNothing().when(session).setAttribute("disableRestrictions", Boolean.TRUE);
		when(modelService.toPersistenceLayer(Boolean.TRUE)).thenReturn(Boolean.TRUE);
		when(searchResult.getResult()).thenReturn(alotResult);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenAnswer(new Answer<SearchResult<Integer>>()
		{
			@Override
			public SearchResult<Integer> answer(final InvocationOnMock invocation) throws Throwable
			{
				return searchResult;
			}
		});

		try
		{
			interceptor.onRemove(new LanguageModel(), interceptorContext);
			Assert.fail("Should not able to remove last language ");
		}
		catch (final InterceptorException ie)
		{
			//
		}

		verify(flexibleSearchService, Mockito.times(1)).search(Mockito.any(FlexibleSearchQuery.class));
		verify(session, Mockito.times(1)).setAttribute("disableRestrictions", Boolean.TRUE);
	}

	/**
	 * Border condition when the {@link FlexibleSearchService#search(FlexibleSearchQuery)} return null
	 */
	@Test
	public void testRemoveInterceptorLanguageModelImPossibleBorderCondition() throws InterceptorException
	{

		doNothing().when(session).setAttribute("disableRestrictions", Boolean.TRUE);
		when(modelService.toPersistenceLayer(Boolean.TRUE)).thenReturn(Boolean.TRUE);
		when(searchResult.getResult()).thenReturn(null);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenAnswer(new Answer<SearchResult<Integer>>()
		{
			@Override
			public SearchResult<Integer> answer(final InvocationOnMock invocation) throws Throwable
			{
				return searchResult;
			}
		});

		try
		{
			interceptor.onRemove(new LanguageModel(), interceptorContext);
			Assert.fail("Should not able to remove last language ");
		}
		catch (final InterceptorException ie)
		{
			//
		}

		verify(flexibleSearchService, Mockito.times(1)).search(Mockito.any(FlexibleSearchQuery.class));
		verify(session, Mockito.times(1)).setAttribute("disableRestrictions", Boolean.TRUE);
	}

	//Stub session service
	class TestDefaultSessionService extends MockSessionService
	{

		private final Session sessionMock;

		TestDefaultSessionService(final Session sessionMock)
		{
			super();
			this.sessionMock = sessionMock;
		}

		@Override
		public Session getCurrentSession()
		{
			return sessionMock;
		}
	}
}
