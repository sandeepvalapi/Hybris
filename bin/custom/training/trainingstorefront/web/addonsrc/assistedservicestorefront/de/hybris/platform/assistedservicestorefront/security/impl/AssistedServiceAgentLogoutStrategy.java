/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicestorefront.security.impl;

import de.hybris.platform.servicelayer.security.spring.HybrisSessionFixationProtectionStrategy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;


public class AssistedServiceAgentLogoutStrategy
{
	private HybrisSessionFixationProtectionStrategy sessionFixationStrategy;

	public void logout(final HttpServletRequest request)
	{
		sessionFixationStrategy.onAuthentication(null, request, null);
	}

	protected HybrisSessionFixationProtectionStrategy getSessionFixationStrategy()
	{
		return sessionFixationStrategy;
	}

	@Required
	public void setSessionFixationStrategy(final HybrisSessionFixationProtectionStrategy sessionFixationStrategy)
	{
		this.sessionFixationStrategy = sessionFixationStrategy;
	}
}
