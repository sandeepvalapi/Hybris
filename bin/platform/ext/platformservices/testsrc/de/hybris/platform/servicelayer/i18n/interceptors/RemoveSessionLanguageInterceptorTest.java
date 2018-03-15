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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.session.SessionService;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test covering the {@link RemoveLastLanguageInterceptor} logic.
 */
@UnitTest
public class RemoveSessionLanguageInterceptorTest
{

	@Mock
	private SessionService sessionService;

	@Mock
	private InterceptorContext interceptorContext;

	private RemoveSessionLanguageInterceptor interceptor;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		interceptor = new RemoveSessionLanguageInterceptor();
		interceptor.setSessionService(sessionService);
	}

	@Test
	public void testRemoveInterceptorNoLanguageModel() throws InterceptorException
	{
		when(sessionService.getAttribute(Mockito.anyString())).thenReturn(null);

		interceptor.onRemove(new ProductModel(), interceptorContext);

		verify(sessionService, Mockito.never()).getAttribute(Mockito.anyString());
	}



	@Test
	public void testRemoveInterceptorLanguageModelPossible() throws InterceptorException
	{
		final LanguageModel model = new LanguageModel();

		//replay so getAttribute will return the model
		when(sessionService.getAttribute("language")).thenReturn(model);
		//call it with different model
		interceptor.onRemove(new LanguageModel(), interceptorContext);
		verify(sessionService, Mockito.times(2)).getAttribute("language");
	}


	@Test
	public void testRemoveInterceptorLanguageModelImPossible() throws InterceptorException
	{
		final LanguageModel model = new LanguageModel();

		//replay so getAttribute will return the model
		when(sessionService.getAttribute("language")).thenReturn(model);
		try
		{
			//call with the same instance
			interceptor.onRemove(model, interceptorContext);
			Assert.fail("Should not able to remove session language ");
		}
		catch (final InterceptorException ie)
		{
			//
		}
		verify(sessionService, Mockito.times(2)).getAttribute("language");
	}



}
