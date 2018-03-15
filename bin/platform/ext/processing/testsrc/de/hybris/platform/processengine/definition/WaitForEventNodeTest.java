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
package de.hybris.platform.processengine.definition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskEngine;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


@IntegrationTest
public class WaitForEventNodeTest extends ServicelayerBaseTest
{
	private String event;

	@Before
	public void setUp()
	{
		this.event = null;
	}

	@Test
	public void testReplaceProcess()
	{
		final TesstBusinessProcessModel process = new TesstBusinessProcessModel();
		process.setCode("process");
		final OrderModel order = new OrderModel();
		order.setCode("order");
		process.setOrder(order);

		final WaitNode node = new TestWaitNode("id", "event_${process.order.code}", "id", true);
		node.trigger(process);
		assertNotNull("Event must not be null", event);
		assertEquals("Event", "process_event_order", event);

		final WaitNode othernode = new TestWaitNode("id", "event_${process.order.code}", "id", false);
		othernode.trigger(process);
		assertNotNull("Event must not be null", event);
		assertEquals("Event", "event_order", event);
	}

	@Test
	public void testReplaceParams()
	{
		final TesstBusinessProcessModel process = new TesstBusinessProcessModel();
		process.setCode("process");
		final BusinessProcessParameterModel param = new BusinessProcessParameterModel();
		param.setName("param");
		param.setValue("value");
		param.setProcess(process);
		process.setContextParameters(Collections.singleton(param));
		final WaitNode node = new TestWaitNode("id", "event_${params['param']}", "id", true);
		node.trigger(process);
		assertNotNull("Event must not be null", event);
		assertEquals("Event", "process_event_value", event);

		final WaitNode othernode = new TestWaitNode("id", "event_${params['param']}", "id", false);
		othernode.trigger(process);
		assertNotNull("Event must not be null", event);
		assertEquals("Event", "event_value", event);
	}

	private class TestWaitNode extends WaitNode
	{
		TestWaitNode(final String id, final String event, final String then, final boolean addProcessCode)
		{
			super(id, event, then, addProcessCode, Collections.EMPTY_MAP, TimeoutConfiguration.none());
		}

		@Override
		protected TaskService getTaskManager()
		{
			return new TaskService()
			{
				@Override
				public void triggerEvent(final String event)
				{
					// empty
				}

				@Override
				public void scheduleTask(final TaskModel task)
				{
					final Set<TaskConditionModel> conditions = task.getConditions();
					assertNotNull("Task conditions must not be null", conditions);
					assertFalse("Task conditions must not be empty", conditions.isEmpty());
					assertEquals("Size of taskconditions", 1, conditions.size());
					final TaskConditionModel taskCondition = conditions.iterator().next();
					WaitForEventNodeTest.this.event = taskCondition.getUniqueID();
				}

				@Override
				public TaskEngine getEngine()
				{
					return null;
				}

				@Override
				public void triggerEvent(final String event, final Date expirationDate)
				{
					// empty

				}
			};
		}


		@Override
		protected ProcessDefinition getProcessDefinition(final ProcessDefinitionId id)
		{
			return Mockito.mock(ProcessDefinition.class);
		}
	}

	static private class TesstBusinessProcessModel extends BusinessProcessModel
	{
		private OrderModel order;

		public TesstBusinessProcessModel()
		{
			setProcessDefinitionName("testProcess");
		}

		public void setOrder(final OrderModel order)
		{
			this.order = order;
		}

		@SuppressWarnings("unused")
		public OrderModel getOrder()
		{
			return order;
		}
	}

}
