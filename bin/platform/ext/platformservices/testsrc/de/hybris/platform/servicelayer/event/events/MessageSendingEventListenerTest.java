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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.cluster.ClusterService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


@UnitTest
public class MessageSendingEventListenerTest
{

	/**
	 * Custom event for testing purposes.
	 */
	private class MyCustomEvent extends AbstractEvent
	{ //
	}

	@InjectMocks
	private final MessageSendingEventListener eventListener = new MessageSendingEventListener();
	@Mock
	private MessageChannel messageChannel;
	@Mock
	private Message<AbstractEvent> message;
	@Mock
	private ClusterService clusterService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

	}

	/**
	 * Test method for
	 * {@link MessageSendingEventListener#onEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)} .
	 */
	@Test
	public void shouldNotSendMessageToTheChannelWhenEventClassIsNotAssignableFromPassedEvent()
	{
		// given
		eventListener.setEventClass(MyCustomEvent.class);
		final TestingEvent event = new TestingEvent("bar");

		// when
		eventListener.onApplicationEvent(event);

		// then
		verify(clusterService, times(1)).getClusterIslandId();
		verify(clusterService, times(1)).getClusterId();
		verify(messageChannel, times(0)).send((Message<?>) anyObject());
	}

	/**
	 * Test method for
	 * {@link MessageSendingEventListener#onEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)} .
	 */
	@Test
	public void shouldThrowMessageSendingExceptionWhenSendReturnsFalse()
	{
		// given
		eventListener.setEventClass(MyCustomEvent.class);
		final MyCustomEvent event = new MyCustomEvent();
		given(message.getPayload()).willReturn(event);
		given(Boolean.valueOf(messageChannel.send((Message<?>) anyObject()))).willReturn(Boolean.FALSE);
		try
		{
			// when
			eventListener.onApplicationEvent(event);
		}
		catch (final MessageSendingException e)
		{
			// then
			verify(messageChannel, times(1)).send(argThat(isValidMessage(event)));
			assertThat(e.getMessage()).contains("Message of type " + event.getClass() + " could not be sent");
		}

	}

	/**
	 * Test method for
	 * {@link MessageSendingEventListener#onEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)} .
	 */
	@Test
	public void shouldSendMessageToTheChannelWhenEventClassAssignableFromPassedEvent()
	{
		// given
		eventListener.setEventClass(MyCustomEvent.class);
		final MyCustomEvent event = new MyCustomEvent();
		given(Boolean.valueOf(messageChannel.send((Message<?>) anyObject()))).willReturn(Boolean.TRUE);

		// when
		eventListener.onApplicationEvent(event);

		// then
		verify(messageChannel, times(1)).send(argThat(isValidMessage(event)));
	}

	private ArgumentMatcher<Message<MyCustomEvent>> isValidMessage(final MyCustomEvent event)
	{
		return new ArgumentMatcher<Message<MyCustomEvent>>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				final Message<MyCustomEvent> message = (Message<MyCustomEvent>) argument;
				return event.equals(message.getPayload());
			}

		};
	}
}
