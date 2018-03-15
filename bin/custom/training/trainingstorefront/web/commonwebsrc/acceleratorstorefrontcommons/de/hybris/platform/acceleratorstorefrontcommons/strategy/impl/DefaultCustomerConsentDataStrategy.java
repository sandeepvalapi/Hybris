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
package de.hybris.platform.acceleratorstorefrontcommons.strategy.impl;

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.strategy.CustomerConsentDataStrategy;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of @{@link CustomerConsentDataStrategy}
 */
public class DefaultCustomerConsentDataStrategy implements CustomerConsentDataStrategy
{
	private SessionService sessionService;
	private ConsentFacade consentFacade;

	@Override
	public void populateCustomerConsentDataInSession()
	{
		final Stream<ConsentTemplateData> activeConsentData = getConsentFacade().getConsentTemplatesWithConsents().stream()
				.filter(ConsentTemplateData::isExposed);

		final Map<String, String> consentsMap = activeConsentData.collect(Collectors.toMap(ConsentTemplateData::getId,
				item -> item.getConsentData() == null || item.getConsentData().getConsentWithdrawnDate() != null
						? WebConstants.CONSENT_WITHDRAWN : WebConstants.CONSENT_GIVEN));

		getSessionService().setAttribute(WebConstants.USER_CONSENTS, consentsMap);
	}

	protected ConsentFacade getConsentFacade()
	{
		return consentFacade;
	}

	@Required
	public void setConsentFacade(final ConsentFacade consentFacade)
	{
		this.consentFacade = consentFacade;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
