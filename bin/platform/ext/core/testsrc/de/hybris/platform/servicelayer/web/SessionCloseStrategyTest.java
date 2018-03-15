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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;


@IntegrationTest
public class SessionCloseStrategyTest extends ServicelayerBaseTest
{
	@Resource
	SessionCloseStrategy sessionCloseStrategy;

	private HttpSession httpSession;
	private HttpSession httpSessionThrowingErrorInRemoveAttribute;
	private JaloSession session;

	@Before
	public void setUp() throws JaloSecurityException
	{
		session = JaloConnection.getInstance().createAnonymousCustomerSession();
		// have to activate Junit session again
		jaloSession.activate();

		httpSession = new MockHttpSession();
		httpSession.setAttribute(Constants.WEB.JALOSESSION, session);
		session.setHttpSessionId(httpSession.getId());

		httpSessionThrowingErrorInRemoveAttribute = new MockHttpSession()
		{
			@Override
			public void removeAttribute(final String name)
			{
				throw new RuntimeException("TestException - should not break SessionClosingStrategy!");
			}
		};
		httpSessionThrowingErrorInRemoveAttribute.setAttribute(Constants.WEB.JALOSESSION, session);
	}

	@Test
	public void testEmptySession()
	{
		final MockHttpSession emptyHttpSession = new MockHttpSession();
		assertNull(emptyHttpSession.getAttribute(Constants.WEB.JALOSESSION));

		sessionCloseStrategy.closeSessionInHttpSession(emptyHttpSession);
		// this should throw any exception!

		assertNull(emptyHttpSession.getAttribute(Constants.WEB.JALOSESSION));
	}

	@Test
	public void testAttributeRemoved()
	{
		assertSame(session, httpSession.getAttribute(Constants.WEB.JALOSESSION));

		sessionCloseStrategy.closeSessionInHttpSession(httpSession);

		assertNull(httpSession.getAttribute(Constants.WEB.JALOSESSION));
	}

	@Test
	public void testSessionClosed()
	{
		assertFalse(session.isClosed());

		sessionCloseStrategy.closeSessionInHttpSession(httpSession);

		assertTrue(session.isClosed());
	}

	@Test
	public void testJaloSessionAlreadyClosed()
	{
		assertFalse(session.isClosed());
		session.close();
		assertTrue(session.isClosed());

		sessionCloseStrategy.closeSessionInHttpSession(httpSession);
		// this should not throw any exception!

		assertTrue(session.isClosed());
		assertNull(httpSession.getAttribute(Constants.WEB.JALOSESSION));
	}

	@Test
	public void testErrorInHttpSession()
	{
		assertSame(session, httpSessionThrowingErrorInRemoveAttribute.getAttribute(Constants.WEB.JALOSESSION));

		sessionCloseStrategy.closeSessionInHttpSession(httpSessionThrowingErrorInRemoveAttribute);
		// this should not throw any exception!

		assertTrue(session.isClosed());
	}

}
