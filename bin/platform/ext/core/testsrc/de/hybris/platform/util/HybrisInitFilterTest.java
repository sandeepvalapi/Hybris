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
package de.hybris.platform.util;

import static org.easymock.EasyMock.createMock;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.util.Set;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class HybrisInitFilterTest extends ServicelayerBaseTest
{
	private HybrisInitFilter initFilter;
	private HttpServletResponse responseMock;
	private HttpServletRequest requestMock;
	private JaloSession session;

	@Before
	public void setUp()
	{
		initFilter = new HybrisInitFilter();
		responseMock = createMock(HttpServletResponse.class);
		requestMock = createMock(HttpServletRequest.class);
		final HttpSession sessionMock = createMock(HttpSession.class);
		session = JaloSession.getCurrentSession();
		EasyMock.expect(sessionMock.getAttribute("jalosession")).andReturn(session).times(4);
		EasyMock.expect(sessionMock.getId()).andReturn("1234567").times(4);
		EasyMock.expect(requestMock.getSession()).andReturn(sessionMock).times(4);
		EasyMock.expect(requestMock.getQueryString()).andReturn("").times(4);
		EasyMock.expect(requestMock.getRequestURL()).andReturn(new StringBuffer("")).times(4);
		EasyMock.expect(requestMock.getParameter("CMS_SESSION")).andReturn(null);
		EasyMock.expect(sessionMock.getAttribute("de.hybris.platform.cms.LiveEditSession")).andReturn(null);
		sessionMock.removeAttribute("jalosession");
		EasyMock.expectLastCall();
		EasyMock.replay(sessionMock);
		EasyMock.replay(requestMock);

	}

	@Test
	public void testCachingModelServicesListOfTypesWithCaching() throws ServletException
	{
		final FilterConfig filterConfigMock = createCachingEnabledFilterMock();
		initFilter.init(filterConfigMock);
		initFilter.doPreRequest(requestMock, responseMock);
		final Set<PK> list = (Set<PK>) session.getSessionContext().getAttribute("cachingModelServiceListOfTypes");
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(Boolean.TRUE, session.getSessionContext().getAttribute("useBlacklist"));
		assertEquals(Boolean.TRUE, session.getSessionContext().getAttribute("fetchAlways"));
	}

	@Test
	public void testCachingModelServicesListOfTypesWithoutCaching() throws ServletException
	{
		final FilterConfig filterConfigMock = createCachingDisabledFilterMock();
		initFilter.init(filterConfigMock);
		initFilter.doPreRequest(requestMock, responseMock);
		assertNull(session.getSessionContext().getAttribute("cachingModelServiceListOfTypes"));
		assertNull(session.getSessionContext().getAttribute("useBlacklist"));
		assertNull(session.getSessionContext().getAttribute("fetchAlways"));
	}

	@Test
	public void testPLA13539()
	{
		final String requestUrl = "/ws410_junit/rest/something";
		final HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURL()).thenReturn(new StringBuffer(requestUrl));
		when(request.getRequestURI()).thenReturn(requestUrl);

		final FilerToTest filterToTest = new FilerToTest();
		final String returnedTenant = filterToTest.getTenantFromRequest(request);

		assertThat(returnedTenant, is("junit"));
	}

	private static class FilerToTest extends HybrisInitFilter
	{
		public String getTenantFromRequest(final HttpServletRequest request)
		{
			return getTenantInformationFrom(request).getTenantId();
		}
	}

	private FilterConfig createConfigFilterMock()
	{
		final FilterConfig filterConfigMock = createMock(FilterConfig.class);
		EasyMock.expect(filterConfigMock.getInitParameter("tenantIDPattern")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("enable.compression")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("enable.buffer.regexp")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("initialized.only")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("skip.initsystemtest")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("redirectOnSystemInit")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("tenantID.cookies")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("tenantID.cookie.name")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("touch.httpSession")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("model.enable.caching.for")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("tenant.mismatch.check")).andReturn(null);
		return filterConfigMock;
	}

	private FilterConfig createCachingEnabledFilterMock()
	{
		final FilterConfig filterConfigMock = createConfigFilterMock();
		EasyMock.expect(filterConfigMock.getInitParameter("caching.model.service.list.of.types")).andReturn("Language");
		EasyMock.expect(filterConfigMock.getInitParameter("fetchAlways")).andReturn("true").times(2);
		EasyMock.expect(filterConfigMock.getInitParameter("useServletContext")).andReturn("false").times(2);
		EasyMock.expect(filterConfigMock.getInitParameter("useBlacklist")).andReturn("true").times(2);
		EasyMock.replay(filterConfigMock);
		return filterConfigMock;
	}

	private FilterConfig createCachingDisabledFilterMock()
	{
		final FilterConfig filterConfigMock = createConfigFilterMock();
		EasyMock.expect(filterConfigMock.getInitParameter("caching.model.service.list.of.types")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("fetchAlways")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("useServletContext")).andReturn(null);
		EasyMock.expect(filterConfigMock.getInitParameter("useBlacklist")).andReturn(null).times(2);
		EasyMock.replay(filterConfigMock);
		return filterConfigMock;
	}
}
