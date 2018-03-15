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

import de.hybris.platform.core.PK;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author juergen, ag
 */
public class TestTaskRunner implements TaskRunner<TaskModel>
{
	private final ConcurrentHashMap<PK, TaskRunnerResult> results = new ConcurrentHashMap<PK, TaskRunnerResult>();

	public static class TaskRunnerResult
	{
		private boolean _run = false;
		private Throwable _error;

		synchronized void setError(final Throwable error)
		{
			_error = error;
		}

		public synchronized Throwable getError()
		{
			return _error;
		}

		public synchronized boolean didRun()
		{
			return _run;
		}

		synchronized void setRun(final boolean run)
		{
			_run = run;
		}
	}

	private TaskRunnerResult getOrCreateResult(final TaskModel t)
	{
		final TaskRunnerResult newOne = new TaskRunnerResult();
		final TaskRunnerResult previousOne = results.putIfAbsent(t.getPk(), newOne);

		return previousOne == null ? newOne : previousOne;
	}

	@Override
	public void handleError(final TaskService taskService, final TaskModel task, final Throwable error)
	{
		final TaskRunnerResult res = getOrCreateResult(task);
		res.setError(error);
	}

	@Override
	public void run(final TaskService taskService, final TaskModel task) throws RetryLaterException
	{
		final TaskRunnerResult res = getOrCreateResult(task);
		res.setRun(true);
	}

	public void reset()
	{
		results.clear();
	}

	public TaskRunnerResult getResultFor(final TaskModel task)
	{
		return results.get(task.getPk());
	}
}
