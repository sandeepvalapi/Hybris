/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms.payment.impl;

import de.hybris.platform.acceleratorstorefrontcommons.forms.payment.MerchantCallbackUrlStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


@Component("merchantCallbackUrlStrategy")
public class DefaultMerchantCallbackUrlStrategy implements MerchantCallbackUrlStrategy
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public String getUrl()
	{
		return configurationService.getConfiguration().getString("merchant.callback.url", "");
	}
}
