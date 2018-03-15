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
package de.hybris.platform.task.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.servicelayer.action.ActionService;
import de.hybris.platform.servicelayer.action.InvalidActionException;
import de.hybris.platform.servicelayer.action.TestActionPerformable;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.servicelayer.model.action.SimpleActionModel;
import de.hybris.platform.task.AbstractTaskTest;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


@ManualTest
public class TaskActionTest extends AbstractTaskTest
{
	private final static String TASKRUNNER_BEANID = "beanID1";
	private final static String PERFORMABLE_BEANID = "beanID2";
	private ActionService actionService;
	private TestActionTaskRunner testActionTaskRunner;
	private TestActionPerformable testActionPerformable;
	private final double timeFactor = Config.getDouble("platform.test.timefactor", 1.0);

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		this.actionService = applicationContext.getBean("actionService", ActionService.class);
	}

	@Override
	protected Map<String, Object> createCustomSingletons()
	{
		final Map<String, Object> ret = new HashMap<String, Object>();
		ret.put(TASKRUNNER_BEANID, this.testActionTaskRunner = new TestActionTaskRunner());
		ret.put(PERFORMABLE_BEANID, this.testActionPerformable = new TestActionPerformable());
		return ret;
	}

	@Test
	public void testTaskActionPerformable()
	{
		assertNotNull(testActionPerformable);
		assertNull(testActionPerformable.getAction());
		assertNull(testActionPerformable.getArgument());
		assertEquals(0, testActionPerformable.getCalls());

		// action 1

		final AbstractActionModel actionModel = new SimpleActionModel();
		actionModel.setCode("action1");
		actionModel.setType(ActionType.TASK);
		actionModel.setTarget(PERFORMABLE_BEANID);
		modelService.save(actionModel);

		final String argument = "This is a Test";
		final TriggeredTaskAction<String> triggeredAction = (TriggeredTaskAction<String>) actionService.prepareAction(actionModel,
				argument);

		assertNotNull(triggeredAction);
		assertEquals(actionModel, triggeredAction.getAction());
		assertEquals(argument, triggeredAction.getArgument());
		assertNull(testActionPerformable.getAction());
		assertNull(testActionPerformable.getArgument());
		assertEquals(0, testActionPerformable.getCalls());

		final TaskModel taskModel = triggeredAction.getTask();
		assertNotNull(taskModel);
		assertEquals(actionModel, taskModel.getContextItem());
		assertEquals(argument, taskModel.getContext());

		actionService.triggerAction(triggeredAction);

		assertTrue("task did not complete", waitForTaskCompletion(taskModel, 40 * 1000));
		assertEquals(actionModel, testActionPerformable.getAction());
		assertEquals(argument, testActionPerformable.getArgument());
		assertEquals(1, testActionPerformable.getCalls());

		// action 2

		final String argument2 = "This is another Test";

		final TriggeredTaskAction<String> triggeredAction2 = (TriggeredTaskAction<String>) actionService.prepareAndTriggerAction(
				actionModel, argument2);

		assertNotNull(triggeredAction2.getTask());
		assertTrue("task 2 did not complete", waitForTaskCompletion(triggeredAction2.getTask(), 40 * 1000));
		assertEquals(actionModel, testActionPerformable.getAction());
		assertEquals(argument2, testActionPerformable.getArgument());
		assertEquals(2, testActionPerformable.getCalls());
		assertNotNull(triggeredAction2);
		assertNotSame(triggeredAction, triggeredAction2);
		assertEquals(actionModel, triggeredAction2.getAction());
		assertEquals(argument2, triggeredAction2.getArgument());
	}

	@Test
	public void testTaskActionRunner()
	{
		assertNotNull(testActionTaskRunner);
		assertNull(testActionTaskRunner.getContextItem());
		assertNull(testActionTaskRunner.getContextObject());
		assertEquals(0, testActionTaskRunner.getRunCalls());
		assertEquals(0, testActionTaskRunner.getHandleErrorCalls());

		// action 1

		final AbstractActionModel actionModel = new SimpleActionModel();
		actionModel.setCode("action1");
		actionModel.setType(ActionType.TASK);
		actionModel.setTarget(TASKRUNNER_BEANID);
		modelService.save(actionModel);

		final String argument = "This is a Test";
		final TriggeredTaskAction<String> triggeredAction = (TriggeredTaskAction<String>) actionService.prepareAction(actionModel,
				argument);

		assertNotNull(triggeredAction);
		assertEquals(actionModel, triggeredAction.getAction());
		assertEquals(argument, triggeredAction.getArgument());

		assertNull(testActionTaskRunner.getContextItem());
		assertNull(testActionTaskRunner.getContextObject());
		assertEquals(0, testActionTaskRunner.getRunCalls());
		assertEquals(0, testActionTaskRunner.getHandleErrorCalls());

		final TaskModel taskModel = triggeredAction.getTask();
		assertNotNull(taskModel);
		assertEquals(actionModel, taskModel.getContextItem());
		assertEquals(argument, taskModel.getContext());

		actionService.triggerAction(triggeredAction);

		assertTrue("task did not complete", waitForTaskCompletion(taskModel, 40 * 1000));
		assertEquals(actionModel, testActionTaskRunner.getContextItem());
		assertEquals(argument, testActionTaskRunner.getContextObject());
		assertEquals(1, testActionTaskRunner.getRunCalls());
		assertEquals(0, testActionTaskRunner.getHandleErrorCalls());

		// action 2

		final String argument2 = "This is another Test";

		final TriggeredTaskAction<String> triggeredAction2 = (TriggeredTaskAction<String>) actionService.prepareAndTriggerAction(
				actionModel, argument2);

		assertNotNull(triggeredAction2.getTask());
		assertTrue("task 2 did not complete", waitForTaskCompletion(triggeredAction2.getTask(), 40 * 1000));
		assertEquals(actionModel, testActionTaskRunner.getContextItem());
		assertEquals(argument2, testActionTaskRunner.getContextObject());
		assertEquals(2, testActionTaskRunner.getRunCalls());
		assertEquals(0, testActionTaskRunner.getHandleErrorCalls());
		assertNotNull(triggeredAction2);
		assertNotSame(triggeredAction, triggeredAction2);
		assertEquals(actionModel, triggeredAction2.getAction());
		assertEquals(argument2, triggeredAction2.getArgument());
	}

	@Test
	public void testInvalidTarget()
	{
		final AbstractActionModel actionModel = new SimpleActionModel();
		actionModel.setCode("action2");
		actionModel.setType(ActionType.TASK);
		actionModel.setTarget("fooBar");
		modelService.save(actionModel);

		final String argument = "This is a Test";
		try
		{
			actionService.prepareAndTriggerAction(actionModel, argument);
			fail("InvalidActionException expected");
		}
		catch (final Exception e)
		{
			assertTrue(e instanceof InvalidActionException);
			final InvalidActionException invalidActionException = (InvalidActionException) e;
			assertEquals("fooBar", invalidActionException.getTarget());
			assertEquals(ActionType.TASK, invalidActionException.getType());
		}
	}

	protected boolean waitForTaskCompletion(final TaskModel taskModel, final long maxWait)
	{
		final long time1 = System.currentTimeMillis();
		boolean removed = false;
		do
		{
			try
			{
				Thread.sleep((long) (200 * timeFactor));
			}
			catch (final InterruptedException e)
			{
				//fine
			}
			removed = modelService.isRemoved(taskModel);
		}
		while (!removed && (System.currentTimeMillis() - time1) < maxWait * timeFactor);

		return removed;
	}
}
