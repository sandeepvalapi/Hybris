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
package de.hybris.platform.task.utils;

import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.constants.TaskConstants;
import de.hybris.platform.testframework.runlistener.CustomRunListener;
import de.hybris.platform.util.Config;

import java.lang.annotation.Annotation;

import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.springframework.beans.factory.annotation.Required;


public class TaskEngineTestRunListener extends CustomRunListener
{

	private static final Logger LOG = Logger.getLogger(TaskEngineTestRunListener.class);

	private TaskService taskService;
	private Description description;
	private boolean needsTaskService;
	private boolean taskProcessingEnabled;


	@Override
	public void testRunStarted(final Description description) throws Exception
	{
		this.description = description;
		this.taskProcessingEnabled = Config.getBoolean(TaskConstants.Params.TASK_PROCESSING_ENABLED, true);

		if (hasNeedsTaskEngineAnnotation(description))
		{
			needsTaskService = true;
			if (!taskService.getEngine().isRunning())
			{
				taskService.getEngine().start();
			}
		}
		else
		{
			if (taskProcessingEnabled)
			{
				startTaskEngineIfIsNotRunning(true);
			}
			else
			{
				stopTaskEngineIfIsRunning(true);
			}
		}
	}

	@Override
	public void testRunFinished(final Result result) throws Exception
	{
		if (needsTaskService && !taskProcessingEnabled)
		{
			taskService.getEngine().stop();
		}
		else
		{
			if (taskProcessingEnabled)
			{
				startTaskEngineIfIsNotRunning(true);
			}
			else
			{
				stopTaskEngineIfIsRunning(true);
			}
		}
	}

	private void stopTaskEngineIfIsRunning(final boolean beforeTest)
	{
		if (taskService.getEngine().isRunning())
		{
			LOG.warn("Stopping task engine, because it was RUNNING " + (beforeTest ? "before" : "after") + " the test: "
					+ description.getTestClass() + ". Tests should leave task engine in the same state as before the test.");
			taskService.getEngine().stop();
		}
	}

	private void startTaskEngineIfIsNotRunning(final boolean beforeTest)
	{
		if (!taskService.getEngine().isRunning())
		{
			LOG.warn("Starting task engine, because it was NOT RUNNING " + (beforeTest ? "before" : "after") + " the test: "
					+ description.getTestClass() + ". Tests should leave task engine in the same state as before the test.");
			taskService.getEngine().start();
		}
	}

	private boolean hasNeedsTaskEngineAnnotation(final Description description)
	{
		for (final Annotation annotation : description.getAnnotations())
		{
			if (annotation.annotationType().equals(NeedsTaskEngine.class))
			{
				return true;
			}
		}

		return false;
	}

	@Required
	public void setTaskService(final TaskService taskService)
	{
		this.taskService = taskService;
	}
}
