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
package de.hybris.platform.test.tx;

import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.PK;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveEventChangesCollector;
import de.hybris.platform.tx.AfterSaveListener;
import de.hybris.platform.tx.DefaultAfterSaveListenerRegistry;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;


/**
 * HORST-2252
 */
public class AfterSaveEventQueueFullTest extends HybrisJUnit4Test
{

	final PK pk1 = PK.createFixedCounterPK(102, 1);
	final PK pk2 = PK.createFixedCounterPK(102, 2);
	final PK pk3 = PK.createFixedCounterPK(102, 3);

	final AfterSaveEventChangesCollector eventCollector = new AfterSaveEventChangesCollector();

	@Test
	public void testQueueFullDuringNotification() throws Exception
	{
		// given
		final DefaultAfterSaveListenerRegistry registry = new DefaultAfterSaveListenerRegistry()
		{
			@Override
			protected boolean isAsyncParam()
			{
				return true;
			}

			@Override
			protected int getQueueSizeParam()
			{
				// just allow one queue entry !!!
				return 1;
			}
		};
		registry.afterPropertiesSet(); // start background thread

		final TestAfterSaveListener listener = new TestAfterSaveListener()
		{
			@Override
			void doOnFirstInvocation()
			{
				// queue one more change --> queue should be full now
				registry.publishChanges(generateChange(pk2));
				// queue another change --> this used to block !!!
				registry.publishChanges(generateChange(pk3));
				firstInvocationPassed.countDown();
			}
		};
		registry.addListener(listener);

		// when
		registry.publishChanges(generateChange(pk1));

		// then
		try
		{
			assertTrue("listener blocked or not finished in time", listener.waitForFirstEventPassed(30, TimeUnit.SECONDS));

			// make sure that 'first' event and 'blocking' event are received by listener
			final Collection<AfterSaveEvent> receivedEvents = listener.getAllEvents();
			assertTrue(receivedEvents.contains(new AfterSaveEvent(pk1, AfterSaveEvent.CREATE)));
			assertTrue(receivedEvents.contains(new AfterSaveEvent(pk2, AfterSaveEvent.CREATE)));
			assertTrue(receivedEvents.contains(new AfterSaveEvent(pk3, AfterSaveEvent.CREATE)));
		}
		finally
		{
			registry.destroy();
		}
	}

	protected byte[][] generateChange(final PK pk)
	{
		eventCollector.clear();
		eventCollector.collect(pk, AfterSaveEvent.CREATE);
		return eventCollector.getEncodedChanges();
	}

	static abstract class TestAfterSaveListener implements AfterSaveListener
	{
		final CountDownLatch firstInvocationPassed = new CountDownLatch(1);

		final Collection<AfterSaveEvent> events = new CopyOnWriteArrayList<>();
		volatile boolean firstTime = true;

		@Override
		public void afterSave(final Collection<AfterSaveEvent> events)
		{
			this.events.addAll(events);

			if (firstTime)
			{
				firstTime = false;
				doOnFirstInvocation();
			}
		}

		abstract void doOnFirstInvocation();

		Collection<AfterSaveEvent> getAllEvents()
		{
			return events;
		}

		boolean waitForFirstEventPassed(final long timeout, final TimeUnit unit) throws InterruptedException
		{
			return firstInvocationPassed.await(timeout, unit);
		}
	}

}
