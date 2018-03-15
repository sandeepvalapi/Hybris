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
package de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation;


import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.QuoteFacade;
import de.hybris.platform.commercefacades.quote.data.QuoteData;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;


public class QuoteCheckoutStepValidationAspect
{
	private static final String REDIRECT_QUOTE_DETAILS = "redirect:/my-account/my-quotes/%s/";
	private QuoteFacade quoteFacade;
	private CartFacade cartFacade;

	public Object validateQuoteCheckoutStep(final ProceedingJoinPoint pjp) throws Throwable // NOSONAR
	{
		if (getCartFacade().hasSessionCart())
		{
			final QuoteData quoteData = getCartFacade().getSessionCart().getQuoteData();
			if (quoteData != null && !getQuoteFacade().isQuoteSessionCartValidForCheckout())
			{
				getQuoteFacade().removeQuoteCart(quoteData.getCode());
				GlobalMessages.addFlashMessage((RedirectAttributesModelMap) pjp.getArgs()[1], GlobalMessages.ERROR_MESSAGES_HOLDER,
						"quote.cart.checkout.error");
				return String.format(REDIRECT_QUOTE_DETAILS, quoteData.getCode());
			}
		}

		return pjp.proceed();
	}

	protected QuoteFacade getQuoteFacade()
	{
		return quoteFacade;
	}

	@Required
	public void setQuoteFacade(final QuoteFacade quoteFacade)
	{
		this.quoteFacade = quoteFacade;
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	@Required
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}
}
