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
package de.hybris.platform.order;

import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * Thrown when creation or modification of cart is not possible.
 */
public class InvalidCartException extends BusinessException
{

	public InvalidCartException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public InvalidCartException(final String message)
	{
		super(message);
	}

	public InvalidCartException(final Throwable cause)
	{
		super(cause);
	}

}
