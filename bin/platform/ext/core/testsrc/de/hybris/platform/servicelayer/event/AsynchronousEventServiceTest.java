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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import de.hybris.platform.servicelayer.event.events.AfterInitializationStartEvent;
import de.hybris.platform.servicelayer.event.events.AfterSessionCreationEvent;
import de.hybris.platform.servicelayer.event.events.AfterSessionUserChangeEvent;
import de.hybris.platform.servicelayer.event.events.BeforeSessionCloseEvent;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.tx.Transaction;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import org.junit.Test;


@IntegrationTest
public class AsynchronousEventServiceTest extends AbstractAsynchronousEventServiceTest
{

	@Test
	public void testEvents() throws Exception
	{
		setExpectedEventClass(CustomEvent.class);
		final CustomEvent event = new CustomEvent();
		eventService.publishEvent(event);

		assertSame("Received event is not expected one", event, pollEvent());
	}

	@Test
	public void testTxEvents() throws Exception
	{
		setExpectedEventClass(CustomEvent.class);
		try
		{
			boolean success;

			final CustomEvent event = new CustomEvent();

			//Tx1
			success = false;
			final Transaction tx1 = Transaction.current();
			tx1.begin();
			try
			{
				eventService.publishEvent(event);
				assertSame("Not received expected event", event, pollEvent());
				success = true;
			}
			finally
			{
				if (success)
				{
					tx1.commit();
				}
				else
				{
					tx1.rollback();
				}
			}

			//Tx2
			success = false;
			final Transaction tx2 = Transaction.current();
			tx2.begin();
			try
			{
				eventService.publishEvent(event);
				assertSame("Received event is not expected one", event, pollEvent());
				success = true;
			}
			finally
			{
				if (success)
				{
					tx2.commit();
				}
				else
				{
					tx2.rollback();
				}
			}
		}
		finally
		{
			//eventService.unregisterEventListener(listener);
		}
	}

	@Test
	public void testTxAwareEvents() throws Exception
	{
		setExpectedEventClass(CustomTxEvent.class);
		boolean success;

		final CustomTxEvent event = new CustomTxEvent(true, "1");

		//test commit
		success = false;
		final Transaction tx1 = Transaction.current();
		tx1.begin();
		try
		{
			eventService.publishEvent(event);
			success = true;
		}
		finally
		{
			if (success)
			{
				tx1.commit();
			}
			else
			{
				tx1.rollback();
			}
		}
		assertSame("Received event is not expected one", event, pollEvent());

		//test rollback
		success = false;
		final Transaction tx2 = Transaction.current();
		tx2.begin();
		try
		{
			eventService.publishEvent(event);
		}
		finally
		{
			tx2.rollback();
		}

		//test outside tx
		eventService.publishEvent(event);

		assertSame("Received event is not expected one", event, pollEvent());
	}

	@Test
	public void testInitializationStartEvent() throws Exception
	{
		setExpectedEventClass(AfterInitializationStartEvent.class);
		final AfterInitializationStartEvent initializationStartEvent = new AfterInitializationStartEvent();
		eventService.publishEvent(initializationStartEvent);

		assertEquals("Received event is not expected one", initializationStartEvent, pollEvent());
	}



	@Test
	public void testInitializationEndEvent() throws Exception
	{
		setExpectedEventClass(AfterInitializationEndEvent.class);
		final AfterInitializationEndEvent initializationEndEvent = new AfterInitializationEndEvent();
		eventService.publishEvent(initializationEndEvent);

		assertSame("Received event is not expected one", initializationEndEvent, pollEvent());
	}


	@Test
	public void testSessionEvents() throws Exception
	{
		final UUID testSessionId = UUID.randomUUID();

		setExpectedEventClass(AfterSessionCreationEvent.class);
		setSourceFilter(sourcePredicate(testSessionId));
		final JaloSession anon = JaloConnection.getInstance()
				.createAnonymousCustomerSession(Collections.singletonMap("testSessionId", testSessionId));
		assertEquals("Received event is not of expected class", AfterSessionCreationEvent.class, pollEvent().getClass());

		setExpectedEventClass(AfterSessionUserChangeEvent.class);
		setSourceFilter(sourcePredicate(anon));
		anon.setUser(UserManager.getInstance().getUserByLogin("admin"));
		assertEquals("Received event is not of expected class", AfterSessionUserChangeEvent.class, pollEvent().getClass());

		setExpectedEventClass(BeforeSessionCloseEvent.class);
		setSourceFilter(sourcePredicate(anon));
		anon.close();
		assertEquals("Received event is not of expected class", BeforeSessionCloseEvent.class, pollEvent().getClass());
	}

	@Test
	public void testSessionEventsWhenPrecedingListenersAreFailing() throws Exception
	{
		final UUID testSessionId = UUID.randomUUID();

		TestUtils.disableFileAnalyzer("Scenrio with exceptions", 1000);
		try
		{
			AtomicBoolean exceptionThrown;

			setExpectedEventClass(AfterSessionCreationEvent.class);
			setSourceFilter(sourcePredicate(testSessionId));
			exceptionThrown = throwExceptionInThePrecedingListener(
					e -> e instanceof AfterSessionCreationEvent && sourcePredicate(testSessionId).test(e.getSource()));
			final JaloSession anon = JaloConnection.getInstance()
					.createAnonymousCustomerSession(Collections.singletonMap("testSessionId", testSessionId));
			assertEquals("Received event is not of expected class", AfterSessionCreationEvent.class, pollEvent().getClass());
			assertTrue(exceptionThrown.get());

			setExpectedEventClass(AfterSessionUserChangeEvent.class);
			setSourceFilter(sourcePredicate(anon));
			exceptionThrown = throwExceptionInThePrecedingListener(
					e -> e instanceof AfterSessionUserChangeEvent && sourcePredicate(anon).test(e.getSource()));
			anon.setUser(UserManager.getInstance().getUserByLogin("admin"));
			assertEquals("Received event is not of expected class", AfterSessionUserChangeEvent.class, pollEvent().getClass());
			assertTrue(exceptionThrown.get());

			setExpectedEventClass(BeforeSessionCloseEvent.class);
			setSourceFilter(sourcePredicate(anon));
			exceptionThrown = throwExceptionInThePrecedingListener(
					e -> e instanceof BeforeSessionCloseEvent && sourcePredicate(anon).test(e.getSource()));
			anon.close();
			assertEquals("Received event is not of expected class", BeforeSessionCloseEvent.class, pollEvent().getClass());
			assertTrue(exceptionThrown.get());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	private Predicate sourcePredicate(final JaloSession session)
	{
		return s -> s instanceof JaloSession && session.equals(s);
	}

	private Predicate sourcePredicate(final UUID testSessionId)
	{
		return s -> s instanceof JaloSession && testSessionId.equals(((JaloSession) s).getAttribute("testSessionId"));
	}

	private AtomicBoolean throwExceptionInThePrecedingListener(final Predicate<AbstractEvent> predicate)
	{
		final AtomicBoolean exceptionThrown = new AtomicBoolean();
		adjustPrecedingListener(predicate, e -> {
			try
			{
				throw new RuntimeException("Test exception for event: " + e);
			}
			finally
			{
				exceptionThrown.set(true);
			}
		});
		return exceptionThrown;
	}


	private static class CustomEvent extends AbstractEvent
	{
		//
	}

	@SuppressWarnings("unused")
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


}
