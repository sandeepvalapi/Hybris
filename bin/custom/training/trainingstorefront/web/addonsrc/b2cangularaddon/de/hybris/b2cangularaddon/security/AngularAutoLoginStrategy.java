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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.acceleratorstorefrontcommons.security.AutoLoginStrategy;


/**
 * A custom implementation of {@link AutoLoginStrategy} used to generate OAuth token and set it in the user's session
 */
public class AngularAutoLoginStrategy implements AutoLoginStrategy
{
	private AutoLoginStrategy autoLoginStrategy;
	private AngularAuthenticationStrategy angularAuthenticationStrategy;

	@Override
	public void login(final String username, final String password, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		getAutoLoginStrategy().login(username, password, request, response);
		getAngularAuthenticationStrategy().login(request, response);
	}

	protected AutoLoginStrategy getAutoLoginStrategy()
	{
		return autoLoginStrategy;
	}

	@Required
	public void setAutoLoginStrategy(final AutoLoginStrategy autoLoginStrategy)
	{
		this.autoLoginStrategy = autoLoginStrategy;
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
