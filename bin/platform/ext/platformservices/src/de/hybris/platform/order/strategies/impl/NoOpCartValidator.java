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
package de.hybris.platform.order.strategies.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.CartValidator;


/**
 * This validator does nothing TODO: remove this interface or fill with business code
 */
public class NoOpCartValidator implements CartValidator
{
	@Override
	public void validateCart(final CartModel cart) throws InvalidCartException
	{
		// DOCTODO Document reason, why this block is empty
	}

}
