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
package de.hybris.platform.task.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.task.AbstractTaskTest;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.Task;
import de.hybris.platform.task.TaskInterruptedException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.TestTaskRunner;
import de.hybris.platform.task.constants.TaskConstants;
import de.hybris.platform.util.Config;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@IntegrationTest
public class TaskServiceErrorHandlingOnStartupTest extends AbstractTaskTest
{
	private final double timeFactor = Config.getDouble("platform.test.timefactor", 10.0);
	private TaskModel task;
	private ErrorHandlingTestTaskRunner errorHandlingTestTaskRunner;
	private volatile boolean handleErrorCalled = false;
	private int nodeId;
	private int results;
	private boolean wasRunningBefore;
	private User previousUser;

	@Before
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		nodeId = MasterTenant.getInstance().getClusterID();
		wasRunningBefore = taskService.getEngine().isRunning();

		// stop task engine
		taskService.getEngine().stop();

		// create task model
		task = modelService.create(TaskModel.class);
		task.setRunnerBean("errorHandlingTestTaskRunner");


		final JaloSession session = JaloSession.getCurrentSession();
		previousUser = session.getUser();
		// run as administrator to avoid search restrictions
		session.setUser(UserManager.getInstance().getAdminEmployee());


		// search for tasks marked running
		final SearchResult<Task> rs = performFlexibleSearch(nodeId);
		results = rs.getResult().size();

		// save
		modelService.save(task);
	}

	@Override
	protected Map<String, Object> createCustomSingletons()
	{
		final Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("errorHandlingTestTaskRunner", errorHandlingTestTaskRunner = new ErrorHandlingTestTaskRunner());
		return ret;
	}

	@Test
	public void testErrorHandlingOnStartupOfTasksMarkedRunning() throws InterruptedException
	{
		// lock because prepare interceptor sets runningonclusternode to -1
		final TaskDAO dao = new TaskDAO(jaloSession.getTenant());
		final Long hjmpts = HJMPUtils.getVersionForPk(task.getPk());
		dao.lock(task.getPk().getLong(), hjmpts == null ? 0 : hjmpts);

		// make sure flag is set to false
		assertFalse("handle error flag should be false here", handleErrorCalled);

		// search again, should be one more
		final SearchResult<Task> rs = performFlexibleSearch(nodeId);
		assertTrue(rs.getResult().size() == (results + 1));

		// start task engine
		taskService.getEngine().start();

		// wait for task execution
		final TestTaskRunner.TaskRunnerResult res = waitForTask(errorHandlingTestTaskRunner, task, 60,
				"error handling test task runner did not run");

		// make sure flag changed
		assertTrue("handleError method has not been called!", handleErrorCalled);
		// make sure error is instance of TaskInterruptedException
		assertTrue("error is not an instance of TaskInterruptedException", res.getError() instanceof TaskInterruptedException);
	}

	private SearchResult performFlexibleSearch(final int nodeId)
	{
		return FlexibleSearch.getInstance().search(//
				"select {" + Item.PK + "} " + //
						"from {" + TaskConstants.TC.TASK + "} " + //
						"where {" + Task.RUNNINGONCLUSTERNODE + "} = ?nodeId",//
				Collections.singletonMap("nodeId", Integer.valueOf(nodeId)),//
				Task.class);
	}

	@After
	@Override
	public void tearDown()
	{
		//clean up
		modelService.remove(task);
		if (wasRunningBefore)
		{
			taskService.getEngine().start();
		}
		// restore user
		final JaloSession session = JaloSession.getCurrentSession();
		session.setUser(previousUser);
		super.tearDown();
	}

	protected TestTaskRunner.TaskRunnerResult waitForTask(final TestTaskRunner runner, final TaskModel task, final int seconds,
			final String errorMessage)
	{
		final long t1 = System.currentTimeMillis();
		TestTaskRunner.TaskRunnerResult res = null;
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

	public class ErrorHandlingTestTaskRunner extends TestTaskRunner
	{
		@Override
		public void run(final TaskService taskService, final TaskModel task) throws RetryLaterException
		{
			super.run(taskService, task);
		}

		@Override
		public void handleError(final TaskService taskService, final TaskModel task, final Throwable error)
		{
			handleErrorCalled = true;
			super.handleError(taskService, task, error);
		}
	}
}
