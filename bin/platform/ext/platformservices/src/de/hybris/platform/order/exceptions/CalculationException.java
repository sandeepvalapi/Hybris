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
package de.hybris.platform.order.exceptions;

import de.hybris.platform.order.CalculationService;
import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * A general exception used by {@link CalculationService} extensions if an (expected) error occurs during price
 * calculation or requesting price informations.
 */
public class CalculationException extends BusinessException
{

	public CalculationException(final String message)
	{
		super(message);
	}

	public CalculationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public CalculationException(final Throwable cause)
	{
		super(cause);
	}

}
