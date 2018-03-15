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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AbstractPersistenceEvent;
import de.hybris.platform.servicelayer.event.events.AfterItemCreationEvent;
import de.hybris.platform.servicelayer.event.events.AfterItemRemovalEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.event.impl.PlatformClusterEventSender;
import de.hybris.platform.tx.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@PerformanceTest
public class EventServiceStressTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(EventServiceStressTest.class);

	@Resource
	private EventService eventService;


	private TestListener listener;
	private Executor oldExecutor;

	@Before
	public void setUp()
	{
		final PlatformClusterEventSender sender = (PlatformClusterEventSender) Registry.getApplicationContext().getBean(
				"platformClusterEventSender");
		oldExecutor = sender.getExecutor();
		sender.setExecutor(null);

		listener = new TestListener();
		eventService.registerEventListener(listener);
	}

	@After
	public void tearDown()
	{
		eventService.unregisterEventListener(listener);
		final PlatformClusterEventSender sender = (PlatformClusterEventSender) Registry.getApplicationContext().getBean(
				"platformClusterEventSender");
		sender.setExecutor(oldExecutor);
	}

	@Test
	public void testClusterEvents() throws Exception
	{
		final String id = "id-" + System.nanoTime();
		final AbstractEvent event = new CustomAlwaysClusterEvent(id);
		eventService.publishEvent(event);

		final CustomAlwaysClusterEvent receivedEvent = listener.waitForEvent(CustomAlwaysClusterEvent.class, 30);
		assertNotNull("did not receive cluster event after waiting for 30 sec", receivedEvent);
		assertTrue(receivedEvent.isFromCluster());
		assertEquals(id, (receivedEvent).id);

		assertEquals("Received more than one event", Collections.singletonList(receivedEvent),
				listener.getEventsOfType(CustomAlwaysClusterEvent.class));
	}

	@Test
	public void testClusterEventsStressTest() throws Exception
	{
		final long end = System.currentTimeMillis() + (30 * 1000);
		do
		{
			listener.clearList();

			testClusterEvents();

			listener.clearList();
		}
		while (System.currentTimeMillis() < end);
	}

	@Test
	public void testItemCreateRemoveEvents() throws ConsistencyCheckException, InterruptedException
	{
		assertFalse(Transaction.current().isRunning());

		final ItemCreateRemoveListener listener = new ItemCreateRemoveListener();
		try
		{
			eventService.registerEventListener(listener);

			final Product product = ProductManager.getInstance().createProduct("Product");
			final PK pk = product.getPK();

			assertTrue("did not get creation event for " + pk + " after waiting for 15 sec", listener.waitForCreationEvent(pk, 15));

			product.remove();

			assertTrue("did not get removal event for " + pk + " after waiting for 30 sec", listener.waitForRemovalEvent(pk, 30));
		}
		finally
		{
			eventService.unregisterEventListener(listener);
		}
	}

	@Test
	public void testItemCreateRemoveEventsStressTest() throws ConsistencyCheckException, InterruptedException
	{
		final long end = System.currentTimeMillis() + (30 * 1000);
		do
		{
			testItemCreateRemoveEvents();
		}
		while (System.currentTimeMillis() < end);
	}

	private static class CustomEvent extends AbstractEvent
	{
		//
	}

	private static class CustomAlwaysClusterEvent extends AbstractEvent implements ClusterAwareEvent
	{
		public final String id;

		public CustomAlwaysClusterEvent(final String id)
		{
			super();
			this.id = id;
		}

		@Override
		public boolean publish(final int sourceNodeId, final int targetNodeId)
		{
			return targetNodeId != 10;
		}

	}

	private static class ItemCreateRemoveListener extends AbstractEventListener<AbstractPersistenceEvent>
	{
		private final Collection<PK> _createdPKs = new ConcurrentLinkedQueue<PK>();
		private final Collection<PK> _removedPKs = new ConcurrentLinkedQueue<PK>();

		@Override
		protected void onEvent(final AbstractPersistenceEvent event)
		{
			if (event instanceof AfterItemCreationEvent)
			{
				final AfterItemCreationEvent aice = (AfterItemCreationEvent) event;

				_createdPKs.add((PK) aice.getSource());
			}
			if (event instanceof AfterItemRemovalEvent)
			{
				final AfterItemRemovalEvent aire = (AfterItemRemovalEvent) event;
				final Object source = aire.getSource();

				_removedPKs.add((PK) source);
			}
		}

		public boolean waitForRemovalEvent(final PK removedPK, final int maxWaitSeconds)
		{
			return waitForEvent(_removedPKs, removedPK, maxWaitSeconds);
		}

		public boolean waitForCreationEvent(final PK createdPK, final int maxWaitSeconds)
		{
			return waitForEvent(_createdPKs, createdPK, maxWaitSeconds);
		}

		private boolean waitForEvent(final Collection<PK> pks, final PK itemPK, final int maxWaitSeconds)
		{
			final long end = System.currentTimeMillis() + (maxWaitSeconds * 1000);
			do
			{
				if (pks.contains(itemPK))
				{
					return true;
				}
				try
				{
					Thread.sleep(100);
				}
				catch (final InterruptedException e)
				{
					Thread.currentThread().interrupt(); // preserve interrupted flag
					return pks.contains(itemPK); // return now without more waiting
				}
			}
			while (System.currentTimeMillis() < end);
			throw new RuntimeException("Timeout waiting for item event for pk " + itemPK);
		}

	}

	private static class TestListener extends AbstractEventListener
	{
		private final List<AbstractEvent> lastEvents = new CopyOnWriteArrayList<AbstractEvent>();

		@Override
		protected void onEvent(final AbstractEvent event)
		{
			this.lastEvents.add(0, event);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(event);
			}
		}

		public <T extends AbstractEvent> T waitForEvent(final Class<T> eventClass, final int maxWaitSeconds)
		{
			T eventOfType = null;

			final long end = System.currentTimeMillis() + (maxWaitSeconds * 1000);
			do
			{
				eventOfType = getFistEventOfType(eventClass);
				if (eventOfType == null)
				{
					try
					{
						Thread.sleep(100);
					}
					catch (final InterruptedException e)
					{
						Thread.currentThread().interrupt(); // preserve interrupted flag
						eventOfType = getFistEventOfType(eventClass);
					}
				}
			}
			while (eventOfType == null && System.currentTimeMillis() < end);

			return eventOfType;
		}

		private <T extends AbstractEvent> T getFistEventOfType(final Class<T> eventClass)
		{
			for (final AbstractEvent e : lastEvents)
			{
				if (eventClass.isInstance(e))
				{
					return (T) e;
				}
			}
			return null;
		}

		public <T extends AbstractEvent> Collection<T> getEventsOfType(final Class<T> eventClass)
		{
			final Collection<T> ret = new ArrayList();
			for (final AbstractEvent e : lastEvents)
			{
				if (eventClass.isInstance(e))
				{
					ret.add((T) e);
				}
			}
			return ret;
		}

		public AbstractEvent getLastEvent()
		{
			return lastEvents.isEmpty() ? null : lastEvents.get(0);
		}

		public AbstractEvent getLastSessionEvent(final JaloSession session)
		{
			for (final AbstractEvent aevent : lastEvents)
			{
				if (session.equals(aevent.getSource()))
				{
					return aevent;
				}
			}
			return null;
		}

		public void clearList()
		{
			lastEvents.clear();
		}

		public int getSize()
		{
			return lastEvents.size();
		}
	}

}
