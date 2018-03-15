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
package com.hybris.training.storefront.checkout.steps.validation.impl;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.AbstractCheckoutStepValidator;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class DefaultDeliveryAddressCheckoutStepValidator extends AbstractCheckoutStepValidator
{
	@Override
	public ValidationResults validateOnEnter(final RedirectAttributes redirectAttributes)
	{
		if (!getCheckoutFlowFacade().hasValidCart())
		{
			return ValidationResults.REDIRECT_TO_CART;
		}

		if (!getCheckoutFacade().hasShippingItems())
		{
			return ValidationResults.REDIRECT_TO_PICKUP_LOCATION;
		}

		return ValidationResults.SUCCESS;
	}
}