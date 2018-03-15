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
package de.hybris.b2cangularaddon.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


/**
 * A custom implementation of {@link LogoutSuccessHandler} used to invalidate OAuth token remove it from user's session
 */
public class AngularLogoutSuccessHandler implements LogoutSuccessHandler
{
	private LogoutSuccessHandler logoutSuccessHandler;
	private AngularAuthenticationStrategy angularAuthenticationStrategy;

	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		getAngularAuthenticationStrategy().logout(request, response, authentication);
		getLogoutSuccessHandler().onLogoutSuccess(request, response, authentication);
	}

	protected LogoutSuccessHandler getLogoutSuccessHandler()
	{
		return logoutSuccessHandler;
	}

	@Required
	public void setLogoutSuccessHandler(final LogoutSuccessHandler logoutSuccessHandler)
	{
		this.logoutSuccessHandler = logoutSuccessHandler;
	}

	protected AngularAuthenticationStrategy getAngularAuthenticationStrategy()
	{
		return angularAuthenticationStrategy;
	}

	@Required
	public void setAngularAuthenticationStrategy(final AngularAuthenticationStrategy angularAuthenticationStrategy)
	{
		this.angularAuthenticationStrategy = angularAuthenticationStrategy;
	}
}
