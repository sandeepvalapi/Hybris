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
package de.hybris.platform.assistedservicestorefront.security;

/**
 * Handles asagent roles, to enable changing authorities dynamically.
 */
public interface AssistedServiceAgentAuthoritiesManager
{

	/**
	 * Add authorities (roles) from a customer to the current agent. It also adds the initial agent authorities to the
	 * session, so it can be restored later.
	 *
	 * @param customerId
	 *           The id of the user the roles will be merged to the agent.
	 */
	void addCustomerAuthoritiesToAgent(final String customerId);

	/**
	 * Restore agent initial authorities.
	 */
	void restoreInitialAuthorities();
}
