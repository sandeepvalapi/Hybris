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

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.event.EventDecorator;
import de.hybris.platform.servicelayer.event.impl.AbstractEventDecorator;
import de.hybris.platform.servicelayer.event.impl.HybrisApplicationEventMulticaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ResolvableType;


/**
 *
 */
@UnitTest
public class EventDecoratorRegistrationUnitTest
{

	@Test
	public void testLookupByType()
	{
		final EventDecorator d1_1 = new TestEvent1Decorator(); //prio 0
		final EventDecorator d2_1 = new TestEvent2Decorator(); //prio 0
		final EventDecorator d1_2 = new TestEvent1PrioDecorator(10);
		final EventDecorator d_all = new UntypedDecorator(20);

		final EventRecordingMulticaster multicaster = new EventRecordingMulticaster();
		multicaster.setAllDecorators((Collection) Arrays.asList(d1_1, d2_1, d1_2, d_all));

		final AbstractTestEvent e1 = new TestEvent1();
		multicaster.multicastEvent(e1);
		Assert.assertSame(e1, multicaster.event);
		// expecting d_all, d1_2, d1_1
		Assert.assertEquals(Arrays.asList(d_all, d1_2, d1_1), e1.decoratorsVisited);

		final AbstractTestEvent e2 = new TestEvent2();
		multicaster.multicastEvent(e2);
		Assert.assertSame(e2, multicaster.event);
		// expecting d_all, d2_1
		Assert.assertEquals(Arrays.asList(d_all, d2_1), e2.decoratorsVisited);
	}

	@Test
	public void testBlocking()
	{
		final EventDecorator d1_1 = new TestEvent1Decorator(); //prio 0
		final EventDecorator d_block = new BlockTestEvent1Decorator(); // prio 5
		final EventDecorator d1_2 = new TestEvent1PrioDecorator(10);
		final EventDecorator d_all = new UntypedDecorator(20);

		final EventRecordingMulticaster multicaster = new EventRecordingMulticaster();
		multicaster.setAllDecorators((Collection) Arrays.asList(d1_1, d_block, d1_2, d_all));

		// TestEvent1 gets filtered
		final AbstractTestEvent e1 = new TestEvent1();
		multicaster.multicastEvent(e1);
		Assert.assertNull(multicaster.event);
		// expecting d_all, d1_2, d_block
		Assert.assertEquals(Arrays.asList(d_all, d1_2, d_block), e1.decoratorsVisited);

		// TestEvent2 does not get filtered
		final AbstractTestEvent e2 = new TestEvent2();
		multicaster.multicastEvent(e2);
		Assert.assertSame(e2, multicaster.event);
		// expecting d_all, d2_1, d_block
		Assert.assertEquals(Arrays.asList(d_all), e2.decoratorsVisited);
	}

	@Test
	public void testListenerErrorBehaviour()
	{
		final AtomicBoolean listenerWithErrorCalled = new AtomicBoolean(false);
		final AtomicBoolean listenerWithoutErrorCalled = new AtomicBoolean(false);

		final List<ApplicationListener<?>> listeners = ImmutableList.<ApplicationListener<?>> of(new ApplicationListener() {
			@Override
			public void onApplicationEvent(final ApplicationEvent event) {
				listenerWithErrorCalled.set(true);
				throw new RuntimeException("foo");
			}
		}, new ApplicationListener() {
			@Override
			public void onApplicationEvent(final ApplicationEvent event) {
				listenerWithoutErrorCalled.set(true);
			}
		});

		final HybrisApplicationEventMulticaster multi = new HybrisApplicationEventMulticaster()
		{
			@Override
			protected Collection<ApplicationListener<?>> getApplicationListeners(final ApplicationEvent event, final ResolvableType eventType)
			{
				// pretend that it's always these listeners
				return listeners;
			}
		};

		// this should not blow up as it's a AbstractEvent
		multi.multicastEvent(new TestEvent1());
		Assert.assertTrue("listener with error was not called at all", listenerWithErrorCalled.get());
		Assert.assertTrue("listener without error was not called at all", listenerWithoutErrorCalled.get());

		listenerWithErrorCalled.set(false);
		listenerWithoutErrorCalled.set(false);
		try
		{
			multi.multicastEvent(new PlainApplicationEvent("bar"));
			Assert.fail("Listener exception on plain application event was not thrown");
		}
		catch (final RuntimeException expected)
		{
			Assert.assertEquals("foo", expected.getMessage());
			Assert.assertTrue("listener with error was not called at all", listenerWithErrorCalled.get());
			Assert.assertFalse("listener without error was wrongly called", listenerWithoutErrorCalled.get());
		}
	}

	static class PlainApplicationEvent extends ApplicationEvent
	{
		public PlainApplicationEvent(final String message)
		{
			super(message);
		}
	}

	static class EventRecordingMulticaster extends HybrisApplicationEventMulticaster
	{
		ApplicationEvent event;

		@Override
		protected void notifyListeners(final ApplicationEvent event, final ResolvableType eventType)
		{
			this.event = event;
		}
	}

	private static abstract class AbstractTestEvent extends AbstractEvent
	{
		List<EventDecorator> decoratorsVisited = new ArrayList<>();

		void decoratedBy(final EventDecorator decorator)
		{
			decoratorsVisited.add(decorator);
		}
	}

	private static class TestEvent1 extends AbstractTestEvent
	{
		// 
	}

	private static class TestEvent2 extends AbstractTestEvent
	{
		//
	}

	private static class TestEvent1Decorator implements EventDecorator<TestEvent1>
	{
		@Override
		public int getPriority()
		{
			return 0;
		}

		@Override
		public TestEvent1 decorate(final TestEvent1 event)
		{
			event.decoratedBy(this);
			return event;
		}
	}

	private static class TestEvent2Decorator implements EventDecorator<TestEvent2>
	{
		@Override
		public int getPriority()
		{
			return 0;
		}

		@Override
		public TestEvent2 decorate(final TestEvent2 event)
		{
			event.decoratedBy(this);
			return event;
		}
	}

	private static class TestEvent1PrioDecorator extends AbstractEventDecorator<TestEvent1>
	{
		TestEvent1PrioDecorator(final int prio)
		{
			setPriority(prio);
		}

		@Override
		public TestEvent1 decorate(final TestEvent1 event)
		{
			event.decoratedBy(this);
			return event;
		}
	}


	private static class BlockTestEvent1Decorator implements EventDecorator<TestEvent1>
	{
		@Override
		public int getPriority()
		{
			return 5;
		}

		@Override
		public TestEvent1 decorate(final TestEvent1 event)
		{
			event.decoratedBy(this);
			return null;
		}
	}

	private static class UntypedDecorator extends AbstractEventDecorator
	{
		UntypedDecorator(final int prio)
		{
			setPriority(prio);
		}

		@Override
		public AbstractEvent decorate(final AbstractEvent event)
		{
			if (event instanceof AbstractTestEvent)
			{
				((AbstractTestEvent) event).decoratedBy(this);
			}
			return event;
		}
	}

}
