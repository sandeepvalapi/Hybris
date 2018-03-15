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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.task.TestTaskRunner.TaskRunnerResult;
import de.hybris.platform.task.constants.TaskConstants;
import de.hybris.platform.task.impl.DefaultTaskService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;


/**
 * Test needs refactoring, see https://jira.hybris.com/browse/BAM-186
 */
@IntegrationTest
public class TaskServiceTest extends AbstractTaskTest
{
	private TestTaskRunner testTaskRunner;
	private TxTestTaskRunner txTaskRunner;
	private final double timeFactor = Config.getDouble("platform.test.timefactor", 10.0);

	private final PropertyConfigSwitcher taskProcessingEnabledSwitcher = new PropertyConfigSwitcher(
			TaskConstants.Params.TASK_PROCESSING_ENABLED);

	private final PropertyConfigSwitcher taskProcessingEnabledLegacySwitcher = new PropertyConfigSwitcher(
			TaskConstants.Params.TASK_PROCESSING_ENABLED_LEGACY);

	@Override
	protected Map<String, Object> createCustomSingletons()
	{
		final Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("testTaskRunner", testTaskRunner = new TestTaskRunner());
		ret.put("txTestTaskRunner", txTaskRunner = new TxTestTaskRunner());
		return ret;
	}

	@Override
	public void tearDown()
	{
		testTaskRunner.reset();
		testTaskRunner = null;
		testTaskRunner = null;
		taskProcessingEnabledSwitcher.switchBackToDefault();
		taskProcessingEnabledLegacySwitcher.switchBackToDefault();
		super.tearDown();
	}

	@Test
	public void testTX() throws InterruptedException
	{
		try
		{
			TestUtils.disableFileAnalyzer(100);
			assertEquals(testTaskRunner, Registry.getApplicationContext().getBean("testTaskRunner"));
			assertTrue("task engine is not running", taskService.getEngine().isRunning());

			final TaskModel task = modelService.create(TaskModel.class);
			task.setRunnerBean("txTestTaskRunner");
			taskService.scheduleTask(task);

			final TaskRunnerResult res = waitForTask(txTaskRunner, task, 60, "Tx Task didn't run");
			if (res.didRun() && res.getError() == null)
			{
				Thread.sleep((long) (2000 * timeFactor));
			}
			assertTrue("Tx Task did not run at all", res.didRun());
			assertNotNull("Tx Task did not receive expected exception", res.getError());
			assertNotNull(txTaskRunner.getTitle());
			assertNotNull(txTaskRunner.getCreationTime());
			assertFalse(txTaskRunner.getTitle().isAlive());
			assertTrue(res.getError() instanceof SystemException);

			try
			{
				final Task item = modelService.getSource(task);
				for (int i = 0; i < 10; i++) // wait at most 10 seconds
				{
					// check if it is already remove by now
					if (item.isAlive())
					{
						Thread.sleep((long) (1000 * timeFactor));
					}
					else
					{
						break;
					}
				}
				assertFalse("processed task item has not been removed after " + timeFactor + " seconds", item.isAlive()); // *now* it must be removed or this is a error !!!
			}
			catch (final IllegalStateException | InterruptedException e)
			{
				// that's OK - task has already been removed
			}
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	@Test
	public void testScheduleTask() throws Exception
	{
		assertThat(Registry.getApplicationContext().getBean("testTaskRunner")).isEqualTo(testTaskRunner);
		assertThat(taskService.getEngine().isRunning()).isTrue();

		final TaskModel task = modelService.create(TaskModel.class);
		task.setRunnerBean("testTaskRunner");
		taskService.getEngine().stop();
		taskService.scheduleTask(task);
		final Task taskItem = modelService.getSource(task);
		assertThat(taskItem.getRunnerBean()).isEqualTo("testTaskRunner");
		assertThat(taskItem.getExecutionDate()).isNotNull();
		assertThat(taskItem.getNodeId()).isNull();
		assertThat(taskItem.getExpirationDate()).isNotNull();
		assertThat(taskItem.getRunningOnClusterNodeAsPrimitive()).isEqualTo(-1);
		taskService.getEngine().start();

		// due to missing accuracy in database (adjusted) execution date may be same as (adjusted) now
		final Date now = new Date();
		assertThat(taskItem.getExecutionDate()).isBeforeOrEqualsTo(now);

		final TaskRunnerResult res = waitForTask(testTaskRunner, task, 20, "Task didn't run");
		assertThat(res.didRun()).isTrue();

		Thread.sleep((long) (1000 * timeFactor));

		assertThat(taskItem.isAlive()).isFalse();
	}

	@Test
	public void testEvent() throws Exception
	{
		assertEquals(testTaskRunner, Registry.getApplicationContext().getBean("testTaskRunner"));
		assertTrue("task engine is not running", taskService.getEngine().isRunning());

		final TaskModel task2 = modelService.create(TaskModel.class);
		task2.setRunnerBean("testTaskRunner");
		final TaskConditionModel tm = modelService.create(TaskConditionModel.class);
		tm.setUniqueID("MyEvent");
		task2.setConditions(Collections.singleton(tm));
		taskService.scheduleTask(task2);

		final Task taskItem2 = modelService.getSource(task2);
		final Set<TaskCondition> conditionItems = taskItem2.getConditions();
		assertFalse(conditionItems.isEmpty());
		final TaskCondition cond = conditionItems.iterator().next();
		assertTrue(cond.isAlive());
		assertEquals("MyEvent", cond.getUniqueID());
		assertEquals(taskItem2, cond.getTask());
		assertFalse(cond.isFulfilledAsPrimitive());

		taskService.getEngine().triggerRepoll(null, null);
		Thread.sleep((long) (1000 * timeFactor));
		assertNull(testTaskRunner.getResultFor(task2));

		taskService.triggerEvent("MyEvent");

		final TaskRunnerResult res = waitForTask(testTaskRunner, task2, 20, "Task 2 didn't run");
		assertTrue("Task 2 didn't finish", res.didRun());

		Thread.sleep((long) (1000 * timeFactor));

		assertFalse(taskItem2.isAlive());
	}

	@Test
	public void testPrematureEvent() throws Exception
	{
		assertEquals(testTaskRunner, Registry.getApplicationContext().getBean("testTaskRunner"));
		assertTrue("task engine is not running", taskService.getEngine().isRunning());

		taskService.triggerEvent("MyEvent");

		final TaskModel task2 = modelService.create(TaskModel.class);
		task2.setRunnerBean("testTaskRunner");
		final TaskConditionModel tm = modelService.create(TaskConditionModel.class);
		tm.setUniqueID("MyEvent");
		task2.setConditions(Collections.singleton(tm));
		taskService.scheduleTask(task2);

		final Task taskItem2 = modelService.getSource(task2);
		final TaskRunnerResult res = waitForTask(testTaskRunner, task2, 20, "Premature Event Task didn't run");
		assertTrue("Premature Event Task did not finish", res.didRun());

		Thread.sleep((long) (1000 * timeFactor));

		assertFalse(taskItem2.isAlive());
	}

	@Test
	public void testSchedulingErrors() throws Exception
	{
		assertEquals(testTaskRunner, Registry.getApplicationContext().getBean("testTaskRunner"));
		assertTrue("task engine is not running", taskService.getEngine().isRunning());
		TaskModel task = modelService.create(TaskModel.class);

		try
		{
			taskService.scheduleTask(task);
			waitForTask(testTaskRunner, task, 20, "Illegal Task did not run");
			fail("missing runner bean not dectected");
		}
		catch (final IllegalArgumentException e)
		{
			// fine
		}
		task.setRunnerBean("testTaskRunner");
		task.setExpirationDate(new Date(System.currentTimeMillis() - 1 * 60 * 1000));
		try
		{
			taskService.scheduleTask(task);
			waitForTask(testTaskRunner, task, 20, "Illegal Task did not run");
			fail("past expiration date not detected");
		}
		catch (final IllegalArgumentException e)
		{
			// fine
		}
		task.setExecutionDate(new Date(System.currentTimeMillis() + 1 * 60 * 1000));
		try
		{
			taskService.scheduleTask(task);
			waitForTask(testTaskRunner, task, 20, "Illegal Task did not run");
			fail("past expiration date not detected (2)");
		}
		catch (final IllegalArgumentException e)
		{
			// fine
		}
		task.setExecutionDate(null);
		task.setExpirationDate(null);

		// now it should pass
		taskService.scheduleTask(task);
		// let it perform before we continue
		waitForTask(testTaskRunner, task, 20, "Legal Task did not run");

		task = modelService.create(TaskModel.class); // create a new one just to be sure
		task.setRunnerBean("testTaskRunner");

		final TaskConditionModel cond1 = modelService.create(TaskConditionModel.class);
		cond1.setUniqueID("foo");
		final TaskConditionModel cond2 = modelService.create(TaskConditionModel.class);
		task.setConditions(new LinkedHashSet<TaskConditionModel>(Arrays.asList(cond1, cond2)));
		try
		{
			taskService.scheduleTask(task);
			waitForTask(testTaskRunner, task, 20, "Illegal Task did not run");
			fail("missing condition ID not detected");
		}
		catch (final IllegalArgumentException e)
		{
			// fine
		}
		cond2.setUniqueID("bar");
		cond2.setExpirationDate(new Date(System.currentTimeMillis() - 1 * 60 * 1000));
		try
		{
			taskService.scheduleTask(task);
			waitForTask(testTaskRunner, task, 20, "Illegal Task did not run");
			fail("past condition expiration date not detected");
		}
		catch (final IllegalArgumentException e)
		{
			// fine
		}

		cond2.setExpirationDate(new Date(System.currentTimeMillis() + 1 * 60 * 1000));

		// now it should pass
		taskService.scheduleTask(task);
		// trigger both events
		taskService.triggerEvent("foo");
		taskService.triggerEvent("bar");

		// let it perform before we continue
		waitForTask(testTaskRunner, task, 20, "Legal Task did not run");
	}

	@Test
	public void testExpirationDate() throws InterruptedException
	{
		assertEquals(testTaskRunner, Registry.getApplicationContext().getBean("testTaskRunner"));
		assertTrue("task engine is not running", taskService.getEngine().isRunning());

		// 1. test task expires - no conditions
		TaskModel expiredTask = modelService.create(TaskModel.class);
		expiredTask.setRunnerBean("testTaskRunner");
		final Date scheduled = new Date(System.currentTimeMillis() - 1 * 60 * 1000);
		Date expires = new Date(System.currentTimeMillis() - 1000);
		expiredTask.setExecutionDate(scheduled);
		expiredTask.setExpirationDate(expires);

		taskService.scheduleTask(expiredTask);

		TaskRunnerResult res = waitForTask(testTaskRunner, expiredTask, 20, "Expired task did not get processed");
		assertFalse("Expired task did run but should not", res.didRun());
		assertNotNull("Expired task did not record error", res.getError());
		assertTrue(res.getError() instanceof TaskTimeoutException);
		TaskTimeoutException timeout = (TaskTimeoutException) res.getError();
		assertEquals("expiration time differs by " + (expires.getTime() - timeout.getExpirationDate().getTime()) + " ms",
				expires.getTime(), timeout.getExpirationDate().getTime());

		// 2. test task expires since condition did not fire

		expiredTask = modelService.create(TaskModel.class);
		expiredTask.setRunnerBean("testTaskRunner");
		expires = new Date(System.currentTimeMillis() + (5 * 1000)); // expires in 5 sec so it could be fetched once (wrongly)
		expiredTask.setExpirationDate(expires);
		final TaskConditionModel tm = modelService.create(TaskConditionModel.class);
		tm.setUniqueID("xxx");
		expiredTask.setConditions(Collections.singleton(tm));

		taskService.scheduleTask(expiredTask);

		res = waitForTask(testTaskRunner, expiredTask, 20, "Expired task did not get processed (2)");
		assertFalse("Expired task did run but should not (2)", res.didRun());
		assertNotNull("Expired task did not record error (2)", res.getError());
		assertTrue(res.getError() instanceof TaskTimeoutException);
		timeout = (TaskTimeoutException) res.getError();
		assertEquals(expires, timeout.getExpirationDate());
	}

	@Test
	public void testDisableTaskProcessing()
	{
		final DefaultTaskService service = new DefaultTaskService();

		taskProcessingEnabledSwitcher.switchToValue("true");
		assertThat(service.getEngine().isAllowedToStart()).isTrue();

		taskProcessingEnabledSwitcher.switchToValue("false");
		assertThat(service.getEngine().isAllowedToStart()).isFalse();
	}

	@Test
	public void testLegacyDisableTaskProcessing()
	{
		final DefaultTaskService service = new DefaultTaskService();

		//Disable new parameter name, remove key also from fallback config in master tenant
		taskProcessingEnabledSwitcher.switchToValue(null);
		final String oldValue = Registry.getMasterTenant().getConfig()
				.removeParameter(TaskConstants.Params.TASK_PROCESSING_ENABLED);

		try
		{
			taskProcessingEnabledLegacySwitcher.switchToValue("true");
			assertThat(service.getEngine().isAllowedToStart()).isTrue();

			taskProcessingEnabledLegacySwitcher.switchToValue("false");
			assertThat(service.getEngine().isAllowedToStart()).isFalse();
		}
		finally
		{
			//Reset value in master tenant
			Registry.getMasterTenant().getConfig().setParameter(TaskConstants.Params.TASK_PROCESSING_ENABLED, oldValue);
		}
	}

	@Test
	public void testDisableTaskProcessingOverridesLegacy()
	{
		final DefaultTaskService service = new DefaultTaskService();

		taskProcessingEnabledLegacySwitcher.switchToValue("false");
		taskProcessingEnabledSwitcher.switchToValue("true");
		assertThat(service.getEngine().isAllowedToStart()).isTrue();

		taskProcessingEnabledLegacySwitcher.switchToValue("true");
		taskProcessingEnabledSwitcher.switchToValue("false");
		assertThat(service.getEngine().isAllowedToStart()).isFalse();
	}

	protected TaskRunnerResult waitForTask(final TestTaskRunner runner, final TaskModel task, final int seconds,
			final String errorMessage)
	{
		final long t1 = System.currentTimeMillis();
		TaskRunnerResult res = null;
		do
		{
			try
			{
				Thread.sleep((long) (100 * timeFactor));
			}
			catch (final InterruptedException e)
			{
				// can't help it
			}
			res = runner.getResultFor(task);
		}
		while (res == null && (((System.currentTimeMillis() - t1)) / 1000) < seconds * timeFactor);
		final long t2 = System.currentTimeMillis();
		assertNotNull(errorMessage + " after " + ((t2 - t1) / 1000) * timeFactor + " seconds. Possible that timeout value "
				+ "is to small. Adiust platform.test.timefactor property for value bigger then 1.", res);

		return res;
	}
}
