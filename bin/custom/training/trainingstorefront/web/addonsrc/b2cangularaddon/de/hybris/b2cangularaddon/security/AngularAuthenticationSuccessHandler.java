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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * A custom implementation of {@link AuthenticationSuccessHandler} used to generate OAuth token and set it in the user's
 * session
 */
public class AngularAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	private AngularAuthenticationStrategy angularAuthenticationStrategy;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		getAngularAuthenticationStrategy().login(request, response);
		getAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);
	}

	protected AuthenticationSuccessHandler getAuthenticationSuccessHandler()
	{
		return authenticationSuccessHandler;
	}

	@Required
	public void setAuthenticationSuccessHandler(final AuthenticationSuccessHandler authenticationSuccessHandler)
	{
		this.authenticationSuccessHandler = authenticationSuccessHandler;
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
