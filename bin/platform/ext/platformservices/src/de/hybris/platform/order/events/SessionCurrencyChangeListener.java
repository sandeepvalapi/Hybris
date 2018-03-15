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
package de.hybris.platform.order.events;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.event.events.AfterSessionAttributeChangeEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;

import org.apache.log4j.Logger;


/**
 *
 */
public class SessionCurrencyChangeListener extends AbstractEventListener<AfterSessionAttributeChangeEvent>
{

	private static final Logger LOG = Logger.getLogger(SessionCurrencyChangeListener.class);


	@Override
	protected void onEvent(final AfterSessionAttributeChangeEvent event)
	{

		if (I18NConstants.CURRENCY_SESSION_ATTR_KEY.equalsIgnoreCase(event.getAttributeName()))
		{
			CurrencyModel currency = null;
			try
			{
				currency = getCommonI18NServiceViaLookup().getCurrentCurrency();
			}
			catch (final Exception e)
			{
				LOG.warn("Cannot obtain commonI18NService.getCurrentCurrency() " + e.getMessage());
			}
			if (currency != null)
			{
				getCartServiceViaLookup().changeSessionCartCurrency(currency);
			}
		}
	}


	public CartService getCartServiceViaLookup()
	{
		throw new UnsupportedOperationException("Please define in the spring configuration a <lookup-method> for getCartService().");
	}



	public CommonI18NService getCommonI18NServiceViaLookup()
	{
		throw new UnsupportedOperationException("Please define in the spring configuration a <lookup-method> for getCartService().");
	}
}
