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
package de.hybris.platform.validation.events;

import static org.junit.Assert.assertSame;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.event.AbstractAsynchronousEventServiceTest;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;

import org.junit.Test;


@IntegrationTest
public class CustomEventListenerIntegrationTest extends AbstractAsynchronousEventServiceTest
{
	@Test
	public void testInistializationEndMessageSend() throws Exception
	{
		setExpectedEventClass(AfterInitializationEndEvent.class);
		final AfterInitializationEndEvent event = new AfterInitializationEndEvent();
		eventService.publishEvent(event);

		assertSame("Received event is not expected one.", event, pollEvent());
	}
}
