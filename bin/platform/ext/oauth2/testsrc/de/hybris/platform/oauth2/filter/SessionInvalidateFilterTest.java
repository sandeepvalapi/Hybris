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
package de.hybris.platform.oauth2.filter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import de.hybris.bootstrap.annotations.UnitTest;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableSet;

@UnitTest
public class SessionInvalidateFilterTest
{
	@Test
	public void testInvalidate() throws Exception
	{
		SessionInvalidateFilter filter = new SessionInvalidateFilter(ImmutableSet.of(".*/oauth/token"));
		filter.init();
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		doReturn("/oauth2/oauth/token").when(request).getRequestURI();
		final HttpSession session = mock(HttpSession.class);
		doReturn(session).when(request).getSession(eq(false));
		final FilterChain chain = mock(FilterChain.class);
		doNothing().when(chain).doFilter(any(), any());

		filter.doFilter(request, null, chain);
		verify(session).invalidate();
	}

	@Test
	public void testUridoesntMatch() throws Exception
	{
		SessionInvalidateFilter filter = new SessionInvalidateFilter(ImmutableSet.of(".*/oauth/token"));
		filter.init();
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		doReturn("/oauth2/oauth/authorize").when(request).getRequestURI();
		final HttpSession session = mock(HttpSession.class);
		doReturn(session).when(request).getSession(eq(false));
		final FilterChain chain = mock(FilterChain.class);
		doNothing().when(chain).doFilter(any(), any());

		filter.doFilter(request, null, chain);
		verifyZeroInteractions(session);
	}

	@Test
	public void testSessionNotPresent() throws Exception
	{
		SessionInvalidateFilter filter = new SessionInvalidateFilter(ImmutableSet.of(".*/oauth/token"));
		filter.init();
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		doReturn("/oauth2/oauth/authorize").when(request).getRequestURI();
		doReturn(null).when(request).getSession(eq(false));
		final FilterChain chain = mock(FilterChain.class);
		doNothing().when(chain).doFilter(any(), any());
		filter.doFilter(request, null, chain);
	}
}
