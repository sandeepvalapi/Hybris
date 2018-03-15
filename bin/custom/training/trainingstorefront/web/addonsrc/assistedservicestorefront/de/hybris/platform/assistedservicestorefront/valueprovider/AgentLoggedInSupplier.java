/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicestorefront.valueprovider;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Required;


/**
 * Boolean Supplier returning {@link Boolean#TRUE} if an assisted service agent is logged and {@link Boolean#FALSE}
 * otherwise.
 */
public class AgentLoggedInSupplier implements Supplier<Boolean>
{
	private AssistedServiceFacade assistedServiceFacade;

	@Override
	public Boolean get()
	{
		return Boolean.valueOf(getAssistedServiceFacade().isAssistedServiceAgentLoggedIn());
	}

	protected AssistedServiceFacade getAssistedServiceFacade()
	{
		return assistedServiceFacade;
	}

	@Required
	public void setAssistedServiceFacade(final AssistedServiceFacade assistedServiceFacade)
	{
		this.assistedServiceFacade = assistedServiceFacade;
	}

}
