/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskEngine;
import de.hybris.platform.task.TaskEvent;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class TaskServiceStub implements TaskService
{
	private static final Logger LOG = LoggerFactory.getLogger(TaskServiceStub.class);
	private List<TaskModel> tasks = new ArrayList<TaskModel>();

	public void runTasks() throws RetryLaterException
	{
		final List<TaskModel> tmpTasks = new ArrayList<TaskModel>();
		for (final TaskModel task : tasks)
		{
			if (task.getConditions() == null || task.getConditions().isEmpty())
			{
				tmpTasks.add(task);
			}
		}
		for (final TaskModel task : tmpTasks)
		{
			runTask(task);
		}

	}

	public ProcessTaskModel runProcessTask(final String beanId) throws RetryLaterException
	{
		ProcessTaskModel processTask = null;
		for (final TaskModel task : tasks)
		{
			if (task instanceof ProcessTaskModel && ((ProcessTaskModel) task).getAction().equals(beanId))
			{
				processTask = (ProcessTaskModel) task;
				break;
			}
		}
		if (processTask != null)
		{
			runTask(processTask);

		}
		return processTask;
	}

	private void runTask(final TaskModel task) throws RetryLaterException
	{
		final TaskRunner ret = Registry.getApplicationContext().getBean(task.getRunnerBean(), TaskRunner.class);
		tasks.remove(task);
		ret.run(this, task);
	}

	public List<TaskModel> cleanup()
	{
		final List<TaskModel> res = tasks;
		tasks = new ArrayList<TaskModel>();
		return res;
	}

	@Override
	public void triggerEvent(final String event)
	{
		final List<TaskModel> tmpTasks = new ArrayList<TaskModel>();

		for (final TaskModel task : tasks)
		{
			for (final TaskConditionModel condition : task.getConditions())
			{
				if (condition.getUniqueID().equals(event))
				{
					tmpTasks.add(task);

				}
			}
		}

		for (final TaskModel task : tmpTasks)
		{
			try
			{
				runTask(task);
			}
			catch (final RetryLaterException e)
			{
				LOG.error("Exception : ", e);
			}
		}
	}

	@Override
	public boolean triggerEvent(final TaskEvent event)
	{
		triggerEvent(event.getId());
		return true;
	}


	@Override
	public void scheduleTask(final TaskModel task)
	{
		synchronized (tasks)
		{
			tasks.add(task);
		}

	}

	@Override
	public TaskEngine getEngine()
	{
		return null;
	}

	public List<TaskModel> getTasks()
	{
		return tasks;
	}


	@Override
	public void triggerEvent(final String event, final Date expirationDate)
	{
		throw new IllegalStateException("Not implemented");
	}


}
