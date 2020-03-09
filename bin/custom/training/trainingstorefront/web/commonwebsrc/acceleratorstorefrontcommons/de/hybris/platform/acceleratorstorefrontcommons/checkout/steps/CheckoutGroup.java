/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.checkout.steps;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


public class CheckoutGroup
{
	private String groupId;
	private Map<String, CheckoutStep> checkoutStepMap;
	private Map<String, String> validationResultsMap;
	private Map<String, CheckoutStep> checkoutProgressBar;

	public String getGroupId()
	{
		return groupId;
	}

	@Required
	public void setGroupId(final String groupId)
	{
		this.groupId = groupId;
	}

	public Map<String, CheckoutStep> getCheckoutStepMap()
	{
		return checkoutStepMap;
	}

	@Required
	public void setCheckoutStepMap(final Map<String, CheckoutStep> checkoutStepMap)
	{
		this.checkoutStepMap = checkoutStepMap;
	}

	public Map<String, String> getValidationResultsMap()
	{
		return validationResultsMap;
	}

	@Required
	public void setValidationResultsMap(final Map<String, String> validationResultsMap)
	{
		this.validationResultsMap = validationResultsMap;
	}

	public Map<String, CheckoutStep> getCheckoutProgressBar()
	{
		return checkoutProgressBar;
	}

	@Required
	public void setCheckoutProgressBar(final Map<String, CheckoutStep> checkoutProgressBar)
	{
		this.checkoutProgressBar = checkoutProgressBar;
	}
}
