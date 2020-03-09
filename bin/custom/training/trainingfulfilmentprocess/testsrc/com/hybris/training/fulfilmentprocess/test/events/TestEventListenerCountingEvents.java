/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test.events;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Event Listener which only count number of onEvent() invocations
 * 
 * @param <T>
 * 
 */
public class TestEventListenerCountingEvents<T extends AbstractEvent> extends AbstractEventListener<T>
{
	// must be thread-safe since it's being hit by different threads
	private final AtomicInteger numberOfEvents = new AtomicInteger(0);

	@Override
	protected void onEvent(final T event)
	{
		numberOfEvents.incrementAndGet();
	}

	/**
	 * @return number of onEvent invocation since beginning or last resetCounter() invocation
	 */
	public int getNumberOfEvents()
	{
		return numberOfEvents.get();
	}

	/**
	 * reset counter which store number of onEvent() invocations
	 */
	public void resetCounter()
	{
		numberOfEvents.set(0);
	}
}
