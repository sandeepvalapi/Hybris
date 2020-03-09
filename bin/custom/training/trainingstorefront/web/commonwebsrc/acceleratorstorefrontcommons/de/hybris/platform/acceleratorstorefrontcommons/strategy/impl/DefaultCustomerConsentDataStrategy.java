/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.strategy.impl;

import de.hybris.platform.acceleratorstorefrontcommons.strategy.CustomerConsentDataStrategy;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of @{@link CustomerConsentDataStrategy}
 *
 * Deprecated, please use {@link de.hybris.platform.commercefacades.consent.impl.DefaultCustomerConsentDataStrategy}
 */
@Deprecated(since = "1905")
public class DefaultCustomerConsentDataStrategy implements CustomerConsentDataStrategy
{
    private de.hybris.platform.commercefacades.consent.CustomerConsentDataStrategy customerConsentDataStrategy;

    @Override
    public void populateCustomerConsentDataInSession()
    {
        getCustomerConsentDataStrategy().populateCustomerConsentDataInSession();
    }

    public de.hybris.platform.commercefacades.consent.CustomerConsentDataStrategy getCustomerConsentDataStrategy()
    {
        return customerConsentDataStrategy;
    }

    @Required
    public void setCustomerConsentDataStrategy(de.hybris.platform.commercefacades.consent.CustomerConsentDataStrategy customerConsentDataStrategy)
    {
        this.customerConsentDataStrategy = customerConsentDataStrategy;
    }
}
