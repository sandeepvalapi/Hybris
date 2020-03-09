/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.events.FraudErrorEvent;
import de.hybris.platform.orderprocessing.events.OrderFraudCustomerNotificationEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import com.hybris.training.fulfilmentprocess.actions.order.NotifyCustomerAboutFraudAction;

import junit.framework.Assert;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class SendOrderFraudCustomerNotificationEventTest
{
	@InjectMocks
	private final NotifyCustomerAboutFraudAction action = new NotifyCustomerAboutFraudAction();

	@Mock
	private EventService eventService;
	@Mock
	private ModelService modelService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExecuteAction()
	{
		final OrderProcessModel process = new OrderProcessModel();
		final OrderModel order = new OrderModel();
		process.setOrder(order);
		action.executeAction(process);
		Mockito.verify(eventService).publishEvent(Mockito.argThat(new BaseMatcher<FraudErrorEvent>()
		{

			@Override
			public boolean matches(final Object item)
			{
				if (item instanceof OrderFraudCustomerNotificationEvent)
				{
					final OrderFraudCustomerNotificationEvent event = (OrderFraudCustomerNotificationEvent) item;
					if (event.getProcess().equals(process))
					{
						return true;
					}
				}
				return false;
			}

			@Override
			public void describeTo(final Description description)
			{//nothing to do
			}
		}));

		Mockito.verify(modelService).save(order);
		Assert.assertEquals(OrderStatus.SUSPENDED, order.getStatus());
	}
}
