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
package de.hybris.platform.processengine.action;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.event.events.MessageSendingException;
import de.hybris.platform.task.RetryLaterException;

import org.fest.assertions.Fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


@UnitTest
public class MessageSendingActionTest
{
	@InjectMocks
	private final MessageSendingAction messageSendingAction = new MessageSendingAction();

	@Mock
	MessageChannel channel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExecute() throws RetryLaterException, Exception
	{
		final BusinessProcessModel process = new BusinessProcessModel();
		Mockito.when(Boolean.valueOf(channel.send((Message<?>) Mockito.anyObject()))).thenReturn(Boolean.TRUE);
		messageSendingAction.execute(process);
	}

	@Test
	public void testExecuteError() throws RetryLaterException, Exception
	{
		final BusinessProcessModel process = new BusinessProcessModel();
		Mockito.when(Boolean.valueOf(channel.send((Message<?>) Mockito.anyObject()))).thenReturn(Boolean.FALSE);
		try
		{
			messageSendingAction.execute(process);
			Fail.fail();
		}
		catch (final MessageSendingException e)
		{
			// ok
		}
	}
}
