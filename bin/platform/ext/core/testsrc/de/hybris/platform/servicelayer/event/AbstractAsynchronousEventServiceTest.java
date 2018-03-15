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

import static org.junit.Assert.fail;

import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.event.impl.PlatformClusterEventSender;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;


/**
 * Extracted base logic to use for a integration tests of event system using {@link TestEventQueueBasedListener}.
 */
@Ignore
abstract public class AbstractAsynchronousEventServiceTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(AbstractAsynchronousEventServiceTest.class);

	@Resource
	protected EventService eventService;

	@Resource
	private PlatformClusterEventSender platformClusterEventSender;

	private PrecedingListener precedingListener;
	private TestEventQueueBasedListener listener;

	private Executor oldExecutor;

	@Before
	public void setUp()
	{
		precedingListener = new PrecedingListener();
		eventService.registerEventListener(precedingListener);

		oldExecutor = platformClusterEventSender.getExecutor();
		platformClusterEventSender.setExecutor(Executors.newCachedThreadPool(RegistrableThread::new));
		listener = new TestEventQueueBasedListener(10); // wait at most 10 seconds at event queue
		eventService.registerEventListener(listener);
		LOG.info("registered test event listener " + listener);
	}

	@After
	public void tearDown()
	{
		listener.setExpectedEventClass(null); // make listener not handling events any more
		listener.setSourceFilter(null);
		eventService.unregisterEventListener(listener); // remove listener
		eventService.unregisterEventListener(precedingListener);
		platformClusterEventSender.setExecutor(oldExecutor);
		LOG.info("unregistered test event listener " + listener);
	}

	protected void setExpectedEventClass(final Class<? extends AbstractEvent> eventClass)
	{
		listener.setExpectedEventClass(eventClass);
	}

	protected void setSourceFilter(final Predicate<?> sourceFilter)
	{
		listener.setSourceFilter(sourceFilter);
	}

	protected void adjustPrecedingListener(final Predicate<AbstractEvent> filter, final Consumer<AbstractEvent> consumer)
	{
		precedingListener.setConsumer(consumer);
		precedingListener.setFilter(filter);
	}

	protected AbstractEvent pollEvent()
	{
		return listener.pollEvent();
	}

	protected static class PrecedingListener extends AbstractEventListener<AbstractEvent>
	{
		private volatile Predicate<AbstractEvent> filter;
		private volatile Consumer<AbstractEvent> consumer;

		@Override
		protected void onEvent(final AbstractEvent event)
		{
			final Predicate<AbstractEvent> f = filter;
			final Consumer<AbstractEvent> c = consumer;
			if (c != null)
			{
				if (f != null && !f.test(event))
				{
					return;
				}
				c.accept(event);
			}
		}

		public void setFilter(final Predicate<AbstractEvent> filter)
		{
			this.filter = filter;
		}

		public void setConsumer(final Consumer<AbstractEvent> consumer)
		{
			this.consumer = consumer;
		}

	}

	protected static class TestEventQueueBasedListener extends AbstractEventListener<AbstractEvent>
	{
		private volatile Class<? extends AbstractEvent> expectedClass = null;
		private volatile Predicate sourceFilter = null;
		private final EventQueue eventQueue;

		public TestEventQueueBasedListener(final int timeoutSeconds)
		{
			super();
			eventQueue = new EventQueue<AbstractEvent>(timeoutSeconds);
		}

		public void setSourceFilter(final Predicate<?> sourceFilter)
		{
			this.sourceFilter = sourceFilter;
		}

		@Override
		protected void onEvent(final AbstractEvent event)
		{
			try
			{
				if (expectedClass != null && expectedClass.isAssignableFrom(event.getClass()))
				{
					if (sourceFilter == null || sourceFilter.test(event.getSource()))
					{
						eventQueue.push(event);
					}
				}
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt(); // preserve interrupted status
				LOG.error("Interrupted while pushing event to queue", e);
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug(event);
			}
		}

		public AbstractEvent pollEvent()
		{
			AbstractEvent event = null;
			try
			{
				do
				{
					event = eventQueue.poll();
				}
				while (event != null && expectedClass != null && (!expectedClass.isAssignableFrom(event.getClass())));
				return event;
			}
			catch (final InterruptedException e)
			{
				fail(e.getMessage());
				return event;
			}
		}

		public void setExpectedEventClass(final Class<? extends AbstractEvent> clazz)
		{
			this.expectedClass = clazz;
		}
	}

	protected static class EventQueue<T extends AbstractEvent>
	{
		private final int timeoutSeconds;
		private final BlockingQueue<T> queue = new SynchronousQueue<T>();

		EventQueue(final int timeoutSeconds)
		{
			this.timeoutSeconds = timeoutSeconds;
		}

		public void push(final T event) throws InterruptedException
		{
			queue.offer(event, timeoutSeconds, TimeUnit.SECONDS);
		}

		public T poll() throws InterruptedException
		{
			return queue.poll(timeoutSeconds, TimeUnit.SECONDS);
		}
	}
}
