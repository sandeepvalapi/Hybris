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

import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.task.RetryLaterException.Method;
import de.hybris.platform.task.model.ScriptingTaskModel;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;


/**
 * Tests HORST-1113
 */
public class TaskRetryTest extends AbstractTaskTest
{
	private static final Logger LOG = Logger.getLogger(TaskRetryTest.class.getName());

	@Override
	protected Map<String, Object> createCustomSingletons()
	{
		return Collections.EMPTY_MAP;
	}

	@Test
	public void testRetrySameTime() throws InterruptedException
	{
		final long fixedDate = System.currentTimeMillis();

		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setCode("RetrySameTime");
		script.setActive(Boolean.TRUE);
		script.setContent("return new " + RetrySameTimeTaskRunner.class.getName() + "(" + fixedDate + ");");
		modelService.save(script);
		LOG.info("Created script " + script.getCode());

		final Object key = "RetrySameTimeTask";
		final ScriptingTaskModel retryingTask = modelService.create(ScriptingTaskModel.class);
		retryingTask.setContext(key);
		retryingTask.setScriptURI("model://" + script.getCode());
		LOG.info("Created task for " + key);

		final CountDownLatch expectedRetriesCounter = new CountDownLatch(3);
		try
		{
			retryCounters.put(key, expectedRetriesCounter);
			LOG.info("Registered counter for " + key + " ( count: " + expectedRetriesCounter.getCount() + ")");

			LOG.info("Scheduling task ...");
			taskService.scheduleTask(retryingTask);

			LOG.info("waiting ...");
			Assert.assertTrue("didn't perform 3 retries before 3 minutes", expectedRetriesCounter.await(3, TimeUnit.MINUTES));
		}
		finally
		{
			retryCounters.remove(key);
		}
	}

	@Test
	public void testRetryDelay() throws InterruptedException
	{
		final long delay = 100;// just 100ms

		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setCode("RetryDelay");
		script.setActive(Boolean.TRUE);
		script.setContent("return new " + RetryDelayTaskRunner.class.getName() + "(" + delay + ");");
		modelService.save(script);
		LOG.info("Created script " + script.getCode());

		final Object key = "RetryDelayTask";
		final ScriptingTaskModel retryingTask = modelService.create(ScriptingTaskModel.class);
		retryingTask.setContext(key);
		retryingTask.setScriptURI("model://" + script.getCode());
		LOG.info("Created task for " + key);

		final CountDownLatch expectedRetriesCounter = new CountDownLatch(3);
		try
		{
			retryCounters.put(key, expectedRetriesCounter);
			LOG.info("Registered counter for " + key + " ( count: " + expectedRetriesCounter.getCount() + ")");

			LOG.info("Scheduling task ...");
			taskService.scheduleTask(retryingTask);

			LOG.info("waiting ...");
			Assert.assertTrue("didn't perform 3 retries before 3 minutes", expectedRetriesCounter.await(3, TimeUnit.MINUTES));
		}
		finally
		{
			retryCounters.remove(key);
		}
	}

	private static final ConcurrentMap<Object, CountDownLatch> retryCounters = new ConcurrentHashMap<>();

	public static final class RetrySameTimeTaskRunner implements TaskRunner<ScriptingTaskModel>
	{
		private final long time;

		public RetrySameTimeTaskRunner(final long time)
		{
			this.time = time;
		}

		@Override
		public void run(final TaskService taskService, final ScriptingTaskModel task) throws RetryLaterException
		{
			LOG.info(RetrySameTimeTaskRunner.class.getSimpleName() + ".run(" + task + " context:" + task.getContext() + " retry:"
					+ task.getRetry() + ")");

			retryCounters.get(task.getContext()).countDown();

			final RetryLaterException later = new RetryLaterException();
			later.setMethod(Method.EXACT_DATE);
			later.setDelay(time);
			throw later;
		}

		@Override
		public void handleError(final TaskService taskService, final ScriptingTaskModel task, final Throwable error)
		{
			LOG.error(RetrySameTimeTaskRunner.class.getSimpleName() + ".handleError( context:" + task.getContext() + ")", error);
		}

	}

	public static final class RetryDelayTaskRunner implements TaskRunner<ScriptingTaskModel>
	{
		private final long delay;

		public RetryDelayTaskRunner(final long delay)
		{
			this.delay = delay;
		}

		@Override
		public void run(final TaskService taskService, final ScriptingTaskModel task) throws RetryLaterException
		{
			LOG.info(RetryDelayTaskRunner.class.getSimpleName() + ".run(" + task + " context:" + task.getContext() + " retry:"
					+ task.getRetry() + ")");

			retryCounters.get(task.getContext()).countDown();

			final RetryLaterException later = new RetryLaterException();
			later.setMethod(Method.LINEAR);
			later.setDelay(delay);
			throw later;
		}

		@Override
		public void handleError(final TaskService taskService, final ScriptingTaskModel task, final Throwable error)
		{
			LOG.error(RetryDelayTaskRunner.class.getSimpleName() + ".handleError( context:" + task.getContext() + ")", error);
		}

	}

}
