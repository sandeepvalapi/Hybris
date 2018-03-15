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
package de.hybris.platform.servicelayer.web;

import static de.hybris.platform.servicelayer.web.SessionFilter.SESSION_SERIALIZATION_CHECK_PROPERTY;
import static de.hybris.platform.servicelayer.web.SessionFilter.SESSION_SERIALIZATION_EXTENSIONS_PROPERTY;
import static java.util.Collections.enumeration;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLogger;
import de.hybris.platform.util.logging.HybrisLoggingEvent;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SessionFilterSerializationCheckTest extends ServicelayerBaseTest
{
	@Resource
	SessionFilter sessionFilter;

	private final PropertyConfigSwitcher serializationCheck = new PropertyConfigSwitcher(SESSION_SERIALIZATION_CHECK_PROPERTY);

	private final PropertyConfigSwitcher serializationExtensions = new PropertyConfigSwitcher(
			SESSION_SERIALIZATION_EXTENSIONS_PROPERTY);

	private static final String SERIALIZATION_CHECK_ENABLED_FIELD = "enabled";

	private HybrisLogListener listener;

	private final AtomicInteger failedSerializations = new AtomicInteger(0);

	@Before
	public void setTestProperties() throws IllegalAccessException, NoSuchFieldException
	{
		unsetSerializationCheckEnabledSetting();
		serializationCheck.switchToValue("true");
		serializationExtensions.switchToValue("hac");
		failedSerializations.set(0);
		listener = new HybrisLogListener()
		{
			@Override
			public boolean isEnabledFor(final Level level)
			{
				return true;
			}

			@Override
			public void log(final HybrisLoggingEvent event)
			{
				if (event.getMessage().toString().contains("Failed to serialize attribute: foo"))
				{
					failedSerializations.incrementAndGet();
				}
			}
		};

		HybrisLogger.addListener(listener);
	}

	private void unsetSerializationCheckEnabledSetting() throws NoSuchFieldException, IllegalAccessException
	{
		final Field f = sessionFilter.getClass().getDeclaredField(SERIALIZATION_CHECK_ENABLED_FIELD);
		f.setAccessible(true);
		f.set(sessionFilter, null);
	}

	@After
	public void resetProperties() throws NoSuchFieldException, IllegalAccessException
	{
		HybrisLogger.removeListener(listener);
		serializationCheck.switchBackToDefault();
		serializationExtensions.switchBackToDefault();
		unsetSerializationCheckEnabledSetting();
	}

	@Test
	public void shouldLogErrorForNotSerializableObjectInSession() throws IOException, ServletException
	{
		final ServletContext mockContext = mock(ServletContext.class);
		when(mockContext.getAttribute("hybris.web.tenant")).thenReturn("junit");
		when(mockContext.getContextPath()).thenReturn("/hac_junit");

		final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		when(mockRequest.getServletContext()).thenReturn(mockContext);

		final HttpSession mockSession = mock(HttpSession.class);

		when(mockSession.getAttributeNames()).thenReturn(enumeration(singleton("foo")));
		when(mockSession.getAttribute("foo")).thenReturn(sessionFilter);
		when(mockRequest.getSession()).thenReturn(mockSession);

		final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		final FilterChain mockChain = mock(FilterChain.class);

		sessionFilter.doFilter(mockRequest, mockResponse, mockChain);

		assertThat(failedSerializations.get()).isEqualTo(1);
	}

	@Test
	public void serializableObjectsInSessionShouldNotLogError() throws IOException, ServletException
	{
		final ServletContext mockContext = mock(ServletContext.class);
		when(mockContext.getAttribute("hybris.web.tenant")).thenReturn("junit");
		when(mockContext.getContextPath()).thenReturn("/hac_junit");

		final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		when(mockRequest.getServletContext()).thenReturn(mockContext);

		final HttpSession mockSession = mock(HttpSession.class);

		when(mockSession.getAttributeNames()).thenReturn(enumeration(singleton("foo")));
		when(mockSession.getAttribute("foo")).thenReturn("bar");
		when(mockRequest.getSession()).thenReturn(mockSession);

		final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		final FilterChain mockChain = mock(FilterChain.class);

		sessionFilter.doFilter(mockRequest, mockResponse, mockChain);

		assertThat(failedSerializations.get()).isEqualTo(0);
	}

	@Test
	public void shouldNotCheckSerializationIfDisabled() throws IOException, ServletException
	{
		serializationCheck.switchToValue("false");

		final ServletContext mockContext = mock(ServletContext.class);
		when(mockContext.getAttribute("hybris.web.tenant")).thenReturn("junit");
		when(mockContext.getContextPath()).thenReturn("/hac_junit");

		final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		when(mockRequest.getServletContext()).thenReturn(mockContext);

		final HttpSession mockSession = mock(HttpSession.class);

		when(mockSession.getAttributeNames()).thenReturn(enumeration(singleton("foo")));
		when(mockSession.getAttribute("foo")).thenReturn(sessionFilter);
		when(mockRequest.getSession()).thenReturn(mockSession);

		final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		final FilterChain mockChain = mock(FilterChain.class);

		sessionFilter.doFilter(mockRequest, mockResponse, mockChain);

		assertThat(failedSerializations.get()).isEqualTo(0);
	}

	@Test
	public void shouldCheckSerializationIfExtensionIsInCsvList() throws IOException, ServletException
	{
		serializationExtensions.switchToValue("foo,hac,bazinga");

		final ServletContext mockContext = mock(ServletContext.class);
		when(mockContext.getAttribute("hybris.web.tenant")).thenReturn("junit");
		when(mockContext.getContextPath()).thenReturn("/hac_junit");

		final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		when(mockRequest.getServletContext()).thenReturn(mockContext);

		final HttpSession mockSession = mock(HttpSession.class);

		when(mockSession.getAttributeNames()).thenReturn(enumeration(singleton("foo")));
		when(mockSession.getAttribute("foo")).thenReturn(sessionFilter);
		when(mockRequest.getSession()).thenReturn(mockSession);

		final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		final FilterChain mockChain = mock(FilterChain.class);

		sessionFilter.doFilter(mockRequest, mockResponse, mockChain);

		assertThat(failedSerializations.get()).isEqualTo(1);
	}
}
