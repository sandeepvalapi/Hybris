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
package de.hybris.platform.order.strategies;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.order.strategies.impl.EventPublishingSubmitOrderStrategy;
import de.hybris.platform.servicelayer.event.EventService;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class EventPublishingSubmitOrderStrategyTest
{
	@InjectMocks
	EventPublishingSubmitOrderStrategy eventPublishingSubmitOrderStrategy = new EventPublishingSubmitOrderStrategy();

	@Mock
	EventService eventService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method for {@link EventPublishingSubmitOrderStrategy#submitOrder(OrderModel)} .
	 */
	@Test
	public void testSubmitOrder()
	{
		final OrderModel order = new OrderModel();
		eventPublishingSubmitOrderStrategy.submitOrder(order);

		final Matcher<SubmitOrderEvent> matcher = new BaseMatcher()
		{

			@Override
			public boolean matches(final Object compareTo)
			{
				if (compareTo instanceof SubmitOrderEvent)
				{
					final SubmitOrderEvent event = (SubmitOrderEvent) compareTo;

					return event.getOrder() == order;
				}
				return false;
			}

			@Override
			public void describeTo(final Description description)
			{
				description.appendText("Argument should be an SubmitOrderEvent for order= " + order);

			}
		};

		Mockito.verify(eventService).publishEvent(Mockito.argThat(matcher));
	}
}
