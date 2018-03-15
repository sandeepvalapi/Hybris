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


import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


/**
 * Listener for the event of class {@link #eventClass}. After event is received {@link MessageSendingEventListener} will
 * send it into {@link #channel}.
 */
public class MessageSendingEventListener extends AbstractEventListener<AbstractEvent>
{
	private static final Logger LOG = Logger.getLogger(MessageSendingEventListener.class);
	private Class<? extends AbstractEvent> eventClass;
	private MessageChannel channel;
	private Long timeout;


	@Override
	protected void onEvent(final AbstractEvent event)
	{
		if (event == null)
		{
			throw new IllegalArgumentException("Event is required, null given");
		}

		if (eventClass.isAssignableFrom(event.getClass()))
		{
			send(event);
		}
		else if (LOG.isDebugEnabled())
		{
			LOG.debug("Event has not been sent (reason: event class " + eventClass + " is not assignable from " + event.getClass());
		}
	}

	protected void send(final AbstractEvent event)
	{
		final boolean sent;
		final Message<AbstractEvent> payload = MessageBuilder.withPayload(event).build();
		if (timeout == null)
		{
			sent = channel.send(payload);
		}
		else
		{
			sent = channel.send(payload, timeout.longValue());
		}
		if (!sent)
		{
			throw new MessageSendingException("Message of type " + event.getClass() + " could not be sent");
		}
	}

	public void setTimeout(final Long timeout)
	{
		this.timeout = timeout;
	}

	@Required
	public void setChannel(final MessageChannel channel)
	{
		this.channel = channel;
	}

	@Required
	public void setEventClass(final Class<? extends AbstractEvent> eventClass)
	{
		this.eventClass = eventClass;
	}

}
