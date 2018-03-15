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
package de.hybris.platform.task;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AfterSessionCreationEvent;
import de.hybris.platform.servicelayer.event.events.BeforeSessionCloseEvent;
import de.hybris.platform.task.runner.LatchTaskRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationListener;


/**
 * Test as proof of jalo session being closed 'explicitly' inside TaskService's Poll
 */
@IntegrationTest
public class TaskEngineSessionTest extends AbstractTaskTest
{
	private static final Logger LOG = Logger.getLogger(TaskEngineSessionTest.class);
	public static final int AMOUNT_OF_TASKS = 150;
	private final CountDownLatch latch = new CountDownLatch(AMOUNT_OF_TASKS);
	private final LatchTaskRunner runner = new LatchTaskRunner();

	private TaskService getTaskService()
	{
		return Registry.getApplicationContext().getBean(TaskService.BEAN_ID, TaskService.class);
	}

	private EventService getEventService()
	{
		return Registry.getApplicationContext().getBean("eventService", EventService.class);
	}

	@Override
	protected Map<String, Object> createCustomSingletons()
	{
		final Map<String, Object> ret = new HashMap<String, Object>();
		runner.setLatch(latch);
		ret.put("latchTestTaskRunner", runner);
		return ret;
	}

	@Test
	public void testTaskSessionLeakage() throws InterruptedException
	{
		final Map<String, Object> sessionIDs = new ConcurrentHashMap();
		final ApplicationListener<AfterSessionCreationEvent> sl1 = new ApplicationListener<AfterSessionCreationEvent>()
		{
			@Override
			public void onApplicationEvent(final AfterSessionCreationEvent event)
			{
				final JaloSession js = (JaloSession) event.getSource();
				sessionIDs.put(js.getSessionID(), js);
			}
		};
		final ApplicationListener<BeforeSessionCloseEvent> sl2 = new ApplicationListener<BeforeSessionCloseEvent>()
		{
			@Override
			public void onApplicationEvent(final BeforeSessionCloseEvent event)
			{
				final JaloSession js = (JaloSession) event.getSource();
				sessionIDs.remove(js.getSessionID());
			}
		};
		try
		{
			getEventService().registerEventListener(sl1);
			getEventService().registerEventListener(sl2);

			final int initialCount = sessionIDs.size();
			LOG.info("Initial session count: " + initialCount);
			this.runTasks();

			// Evaluation/check the sessions...
			final int countAfterTasks = sessionIDs.size();
			final int sessionsCreatedDuringTest = countAfterTasks - initialCount;

			LOG.info("Count after " + TaskEngineSessionTest.AMOUNT_OF_TASKS + " tasks: " + countAfterTasks);
			LOG.info("There were " + sessionsCreatedDuringTest + " sessions created ");
			Assert.assertTrue("Should there be less sessions created (" + sessionsCreatedDuringTest + ") " + "than tasks run ("
					+ TaskEngineSessionTest.AMOUNT_OF_TASKS + ").", sessionsCreatedDuringTest < TaskEngineSessionTest.AMOUNT_OF_TASKS);
		}
		finally
		{
			getEventService().unregisterEventListener(sl2);
			getEventService().unregisterEventListener(sl1);
		}
	}

	private void runTasks() throws InterruptedException
	{
		final String latchName = "testlatch_" + Math.random();

		for (int i = 0; i < TaskEngineSessionTest.AMOUNT_OF_TASKS; i++)
		{
			final TaskModel task = modelService.create(TaskModel.class);
			task.setContext(latchName);
			task.setRunnerBean("latchTestTaskRunner");
			getTaskService().scheduleTask(task);
		}
		LOG.info("Waiting for tasks to complete.");
		latch.await();
	}


}
