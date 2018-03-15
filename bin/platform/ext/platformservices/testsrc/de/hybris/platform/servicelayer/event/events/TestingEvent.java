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
package de.hybris.platform.servicelayer.event.events;

/**
 * Testing event used only for testing current Spring Integration.
 * 
 */
public class TestingEvent extends AbstractEvent
{
	private final String message;

	public TestingEvent(final String message)
	{
		super();
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}
}
