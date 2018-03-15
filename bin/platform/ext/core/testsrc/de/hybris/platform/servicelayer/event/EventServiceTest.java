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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AbstractPersistenceEvent;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import de.hybris.platform.servicelayer.event.events.AfterInitializationStartEvent;
import de.hybris.platform.servicelayer.event.events.AfterItemCreationEvent;
import de.hybris.platform.servicelayer.event.events.AfterItemRemovalEvent;
import de.hybris.platform.servicelayer.event.events.AfterSessionCreationEvent;
import de.hybris.platform.servicelayer.event.events.AfterSessionUserChangeEvent;
import de.hybris.platform.servicelayer.event.events.BeforeSessionCloseEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.event.impl.PlatformClusterEventSender;
import de.hybris.platform.tx.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
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


@IntegrationTest
public class EventServiceTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(EventServiceTest.class);

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
	public void testEvents()
	{
		final CustomEvent event = new CustomEvent();
		eventService.publishEvent(event);
		assertSame("Received event is not expected one", event, listener.getLastEvent());
	}

	@Test
	public void testTxEvents()
	{
		try
		{
			final CustomEvent event = new CustomEvent();

			Transaction tx = Transaction.current();
			tx.begin();
			eventService.publishEvent(event);
			assertSame("Not received expected event", event, listener.getLastEvent());
			tx.commit();

			tx = Transaction.current();
			tx.begin();
			eventService.publishEvent(event);
			tx.commit();
			assertSame("Received event is not expected one", event, listener.getLastEvent());
		}
		finally
		{
			eventService.unregisterEventListener(listener);

			while (Transaction.current().isRunning())
			{
				Transaction.current().rollback();
			}
		}
	}


	@Test
	public void testTxAwareEvents()
	{
		try
		{
			final CustomTxEvent event = new CustomTxEvent(true, "1");

			//test commit
			Transaction.current().begin();
			eventService.publishEvent(event);
			assertNull(listener.getLastEvent());
			Transaction.current().commit();
			assertSame("Received event is not expected one", event, listener.getLastEvent());
			listener.clearList();

			//test rollback
			Transaction.current().begin();
			eventService.publishEvent(event);
			assertNull(listener.getLastEvent());
			Transaction.current().rollback();
			assertNull(listener.getLastEvent());

			//test outside tx
			eventService.publishEvent(event);
			assertSame("Received event is not expected one", event, listener.getLastEvent());

		}
		finally
		{
			if (Transaction.current().isRunning())
			{
				Transaction.current().rollback();
			}
		}
	}

	@Test
	public void testTxSameEvents()
	{
		try
		{
			final CustomTxEvent event1 = new CustomTxEvent(true, "1");
			final CustomTxEvent event1a = new CustomTxEvent(true, "1");
			final CustomTxEvent event2 = new CustomTxEvent(true, "2");

			//test dont send same event twice 
			Transaction.current().begin();
			eventService.publishEvent(event1);
			eventService.publishEvent(event1a);
			assertNull(listener.getLastEvent());
			assertEquals(0, listener.getSize());
			Transaction.current().commit();
			assertTrue(listener.getLastEvent().equals(event1));
			assertEquals(1, listener.getSize());
			listener.clearList();

			//test rollback
			Transaction.current().begin();
			eventService.publishEvent(event1);
			eventService.publishEvent(event1a);
			eventService.publishEvent(event2);
			assertNull(listener.getLastEvent());
			assertEquals(0, listener.getSize());
			Transaction.current().rollback();
			assertNull(listener.getLastEvent());
			assertEquals(0, listener.getSize());
			listener.clearList();

			//test send different event  
			Transaction.current().begin();
			eventService.publishEvent(event1);
			eventService.publishEvent(event2);
			assertNull(listener.getLastEvent());
			assertEquals(0, listener.getSize());
			Transaction.current().commit();
			assertEquals(2, listener.getSize());
			listener.clearList();
		}
		finally
		{
			if (Transaction.current().isRunning())
			{
				Transaction.current().rollback();
			}
		}
	}

	@Test
	public void testInitializationStartEvent() throws Exception
	{
		final AfterInitializationStartEvent initializationStartEvent = new AfterInitializationStartEvent();
		eventService.publishEvent(initializationStartEvent);
		assertEquals(initializationStartEvent, listener.getLastEvent());
	}

	@Test
	public void testInitializationEndEvent() throws Exception
	{
		final AfterInitializationEndEvent initializationEndEvent = new AfterInitializationEndEvent();
		eventService.publishEvent(initializationEndEvent);
		assertEquals(initializationEndEvent, listener.getLastEvent());
	}

	@Test
	public void testSessionEvents() throws Exception
	{
		final JaloSession anon = JaloConnection.getInstance().createAnonymousCustomerSession();

		AbstractEvent lastSessionEvent = listener.getLastSessionEvent(anon);
		assertNotNull(lastSessionEvent);
		assertEquals(AfterSessionCreationEvent.class, lastSessionEvent.getClass());

		anon.setUser(UserManager.getInstance().getUserByLogin("admin"));

		lastSessionEvent = listener.getLastSessionEvent(anon);
		assertNotNull(lastSessionEvent);
		assertEquals(AfterSessionUserChangeEvent.class, lastSessionEvent.getClass());

		anon.close();

		lastSessionEvent = listener.getLastSessionEvent(anon);
		assertNotNull(lastSessionEvent);
		assertEquals(BeforeSessionCloseEvent.class, lastSessionEvent.getClass());
	}

	private static class CustomEvent extends AbstractEvent
	{
		//
	}

	private static class CustomClusterAwareEvent extends CustomEvent implements ClusterAwareEvent
	{
		@Override
		public boolean publish(final int sourceNodeId, final int targetNodeId)
		{
			return true;
		}
	}

	private static class CustomTxEvent extends AbstractEvent implements TransactionAwareEvent
	{
		private final String id;
		private final boolean onCommit;

		public CustomTxEvent(final boolean publishOnCommit, final String id)
		{
			super();
			this.id = id;
			this.onCommit = publishOnCommit;
		}

		@Override
		public Object getId()
		{
			return id;
		}


		@Override
		public boolean publishOnCommitOnly()
		{
			return onCommit;
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

	/* used for PLA10063 - testTenantListenerIdentityPLA10063 only */
	private static class EventTenantListener extends AbstractEventListener<CustomEvent>
	{
		final List<Tenant> eventTenants = new CopyOnWriteArrayList<Tenant>();

		@Override
		protected void onEvent(final CustomEvent event)
		{
			eventTenants.add(Registry.getCurrentTenantNoFallback());
		}
	}

	@Test
	public void testEventTenantsPLA10063()
	{
		final Tenant junitTenant = Registry.getCurrentTenant();
		final Tenant masterTenant = Registry.getMasterTenant();

		if (junitTenant.equals(masterTenant))
		{
			LOG.warn("cannot execute " + getClass().getSimpleName()
					+ ".testEventTenantsPLA10063() if master tenant is also junit tenant");
			return;
		}

		final EventTenantListener testIdentityListener = new EventTenantListener();
		try
		{
			eventService.registerEventListener(testIdentityListener);

			eventService.publishEvent(new CustomEvent());

			Registry.setCurrentTenant(masterTenant);
			eventService.publishEvent(new CustomEvent());
		}
		finally
		{
			Registry.setCurrentTenant(junitTenant);
			eventService.unregisterEventListener(testIdentityListener);
		}
		assertEquals(Arrays.asList(junitTenant, masterTenant), testIdentityListener.eventTenants);
	}

	// PLA-13307
	@Test
	public void testMessageProcessedMultipleTimesBug() throws InterruptedException
	{
		final Tenant junitTenant = Registry.getCurrentTenant();
		final Tenant masterTenant = Registry.getMasterTenant();

		final EventTenantListener listener = new EventTenantListener();

		final EventService junitEventService = eventService;
		EventService masterEventService = null;
		try
		{
			if (junitTenant.equals(masterTenant))
			{
				LOG.warn("cannot execute " + getClass().getSimpleName()
						+ ".testMessageProcessedMultipleTimesBug() if master tenant is also junit tenant");
				return;
			}

			Registry.setCurrentTenant(masterTenant);
			masterEventService = Registry.getCoreApplicationContext().getBean("eventService", EventService.class);
			Registry.setCurrentTenant(junitTenant);

			junitEventService.registerEventListener(listener);
			masterEventService.registerEventListener(listener);

			junitEventService.publishEvent(new CustomClusterAwareEvent());
			Thread.sleep(2000); // dirty way to wait for asynchronous processing
			assertEquals(Collections.singletonList(junitTenant), listener.eventTenants);
			listener.eventTenants.clear();

			Registry.setCurrentTenant(masterTenant);
			masterEventService.publishEvent(new CustomClusterAwareEvent());
			Thread.sleep(2000); // dirty way to wait for asynchronous processing
			assertEquals(Collections.singletonList(masterTenant), listener.eventTenants);
		}
		finally
		{
			Registry.setCurrentTenant(junitTenant);

			junitEventService.unregisterEventListener(listener);

			if (masterEventService != null)
			{
				masterEventService.unregisterEventListener(listener);
			}
		}
	}
}
