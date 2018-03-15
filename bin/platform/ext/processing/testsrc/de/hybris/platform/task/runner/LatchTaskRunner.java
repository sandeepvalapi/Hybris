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
package de.hybris.platform.task.runner;

import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;


public class LatchTaskRunner implements TaskRunner<TaskModel>
{
	private static final Logger LOG = Logger.getLogger(LatchTaskRunner.class);
	private CountDownLatch latch;

	@Override
	public void handleError(final TaskService service, final TaskModel task, final Throwable fault)
	{
		LOG.error("Failed to run task '" + task + "' (context: " + task.getContext() + ").", fault);
	}

	@Override
	public void run(final TaskService service, final TaskModel task) throws RetryLaterException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Running on '" + task + "' (context: " + task.getContext() + "').");
		}
		if (latch != null)
		{
			latch.countDown();
		}
	}

	public void setLatch(final CountDownLatch latch)
	{
		this.latch = latch;
	}
}
