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
package de.hybris.platform.commons.renderer.exceptions;

/**
 * Exception thrown when no one renderer was found
 */
public class RendererNotFoundException extends RuntimeException
{
	public RendererNotFoundException(final String message)
	{
		super(message);
	}
}
