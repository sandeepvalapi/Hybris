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

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.event.events.AfterSessionUserChangeEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.log4j.Logger;


/**
 * Event listener to change the cart user if the current user has changed.
 * <p>
 * {@link AfterSessionUserChangeEvent} events are processed, which are fired after the current user is changed.
 * 
 * @see CartService#changeCurrentCartUser(UserModel)
 */
public class AfterSessionUserChangeListener extends AbstractEventListener<AfterSessionUserChangeEvent>
{
	private static final Logger LOG = Logger.getLogger(AfterSessionUserChangeListener.class);

	@Override
	protected void onEvent(final AfterSessionUserChangeEvent event)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("AfterSessionUserChangeEvent received.");
		}
		final UserModel user = getUserService().getCurrentUser();
		getCartService().changeCurrentCartUser(user);
	}

	protected CartService getCartService()
	{
		throw new UnsupportedOperationException("Please define in the spring configuration a <lookup-method> for getCartService().");
	}

	protected UserService getUserService()
	{
		throw new UnsupportedOperationException("Please define in the spring configuration a <lookup-method> for getUserService().");
	}
}
