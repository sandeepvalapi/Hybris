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
package de.hybris.platform.servicelayer.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.MockTest;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.context.ApplicationListener;
import org.springframework.test.context.ContextConfiguration;


@UnitTest
@ContextConfiguration(locations =
{ "classpath:/servicelayer/test/servicelayer-mock-base-test.xml" })
public class MockEventServiceTest extends MockTest
{
	@Resource(name = "eventService")
	private EventService eventService;

	@Resource(name = "eventService1")
	private EventService eventService1;


	@Test
	public void testEventRegistration()
	{
		final MockTestListener listener = new MockTestListener();
		final MockTestListener listener2 = new MockTestListener();
		try
		{
			final int sizeBefore = eventService.getEventListeners().size();


			assertEquals(sizeBefore, eventService.getEventListeners().size());
			eventService.registerEventListener(listener);
			assertEquals(sizeBefore + 1, eventService.getEventListeners().size());

			//test duplicate registration
			eventService.registerEventListener(listener);
			assertEquals(sizeBefore + 1, eventService.getEventListeners().size());

			//test removal
			eventService.unregisterEventListener(listener);
			assertEquals(sizeBefore, eventService.getEventListeners().size());

			//test different listeners
			eventService.registerEventListener(listener);
			eventService.registerEventListener(listener2);
			assertEquals(sizeBefore + 2, eventService.getEventListeners().size());
			eventService.registerEventListener(listener);
			assertEquals(sizeBefore + 2, eventService.getEventListeners().size());
		}
		finally
		{
			eventService.unregisterEventListener(listener);
			eventService.unregisterEventListener(listener2);
		}
	}





	@Test
	public void testCustomEvents()
	{
		final MockTestListener listener = new MockTestListener();
		try
		{
			//some general custom events
			assertNull(listener.lastEvent);
			eventService.registerEventListener(listener);
			final AbstractEvent event = new CustomEvent();
			final AbstractEvent event2 = new Custom2Event();
			eventService.publishEvent(event);
			assertSame(listener.lastEvent, event);
			eventService.publishEvent(event2);
			assertSame(listener.lastEvent, event2);

			//test publish event without listener
			final AbstractEvent event3 = new Custom3Event();
			eventService.publishEvent(event3);
			assertNotSame(listener.lastEvent, event3); //still the old, because no listener for Custom3Event

			//test publish event and have listener for superclass
			final AbstractEvent event4 = new Custom4Event();
			eventService.publishEvent(event4);
			assertSame(listener.lastEvent, event4); //listener for subclass get's the event
		}
		finally
		{
			eventService.unregisterEventListener(listener);
		}
	}

	@Test
	public void testMixOfXMLConfigAndRegisterMethods()
	{
		ConfiguredTestListener listener1 = null;
		ConfiguredTestListener listener2 = null;
		ConfiguredTestListener listener3 = null;
		try
		{
			listener1 = getConfiguredTestListener(ConfiguredTestListener.class);
			final AbstractEvent event = new CustomEvent();
			eventService.publishEvent(event);
			assertSame(event, listener1.getLastEvent()); //configured listener did receive it

			final int sizeBefore = eventService.getEventListeners().size();
			assertEquals(sizeBefore, eventService.getEventListeners().size());
			listener2 = getConfiguredTestListener(ConfiguredTestListener.class);
			eventService.registerEventListener(listener2);
			assertEquals(sizeBefore, eventService.getEventListeners().size()); //should be the same, because it's already configured
			listener3 = new ConfiguredTestListener();
			eventService.registerEventListener(listener3);
			assertEquals(sizeBefore + 1, eventService.getEventListeners().size()); //this one should be added, it's a new
		}
		finally
		{
			if (listener1 != null)
			{
				eventService.unregisterEventListener(listener1);
			}
			if (listener2 != null)
			{
				eventService.unregisterEventListener(listener2);
			}
			if (listener3 != null)
			{
				eventService.unregisterEventListener(listener3);
			}
		}
	}


	@Test
	public void testClusterHandling()
	{
		ConfiguredTestListener listener = null;
		ConfiguredTestListener listener1 = null;
		try
		{
			listener = getConfiguredTestListener(ConfiguredTestListener.class);
			listener1 = getConfiguredTestListener(ConfiguredTestListener1.class);
			//this event is send only to "local clusternode"
			AbstractEvent event = new CustomClusterEvent();
			eventService.publishEvent(event);
			assertTrue(listener.lastEvent == event);
			assertFalse(listener1.lastEvent == event);

			event = new CustomClusterEvent();
			eventService1.publishEvent(event);
			assertTrue(listener1.lastEvent == event);
			assertFalse(listener.lastEvent == event);

			//send to all nodes
			event = new CustomClusterEvent1();
			eventService1.publishEvent(event);
			assertTrue(listener.lastEvent == event);
			assertTrue(listener1.lastEvent == event);
		}
		finally
		{
			if (listener != null)
			{
				eventService.unregisterEventListener(listener);
			}
			if (listener1 != null)
			{
				eventService.unregisterEventListener(listener1);
			}
		}
	}

	@Test
	public void testTenantHandling()
	{
		//
	}



	private ConfiguredTestListener getConfiguredTestListener(final Class clazz)
	{
		ConfiguredTestListener result = null;
		for (final ApplicationListener listener : eventService.getEventListeners())
		{
			if (listener.getClass().equals(clazz))
			{
				result = (ConfiguredTestListener) listener;
			}
		}
		assertNotNull("ConfiguredTestListener not found in spring configuration", result);
		return result;
	}


	class MockTestListener extends AbstractEventListener<AbstractEvent>
	{
		public AbstractEvent lastEvent;

		@Override
		protected void onEvent(final AbstractEvent event)
		{
			if (event instanceof Custom2Event)
			{
				this.lastEvent = event;
			}
			else if (event instanceof CustomEvent)
			{
				this.lastEvent = event;
			}
		}

	}


	class CustomEvent extends AbstractEvent
	{
		//noop
	}

	class Custom2Event extends AbstractEvent
	{
		//noop
	}

	class Custom3Event extends AbstractEvent //this one is NOT registered
	{
		//noop
	}

	class Custom4Event extends Custom2Event //extends an existing Event
	{
		//noop
	}


	static class CustomClusterEvent extends AbstractEvent implements ClusterAwareEvent
	{
		@Override
		public boolean publish(final int sourceNodeId, final int targetNodeId)
		{
			return sourceNodeId == targetNodeId;
		}
	}

	static class CustomClusterEvent1 extends CustomClusterEvent
	{
		@Override
		public boolean publish(final int sourceNodeId, final int targetNodeId)
		{
			return true;
		}
	}

}
