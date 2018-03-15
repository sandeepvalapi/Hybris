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

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;

import java.util.concurrent.atomic.AtomicInteger;


class TestActionTaskRunner implements TaskRunner<TaskModel>
{
	private final AtomicInteger handleErrorCalls = new AtomicInteger(0);
	private final AtomicInteger runCalls = new AtomicInteger(0);
	private ItemModel contextItem;
	private Object context;

	public int getHandleErrorCalls()
	{
		return handleErrorCalls.get();
	}

	public int getRunCalls()
	{
		return runCalls.get();
	}

	synchronized void setRunData(final TaskModel task)
	{
		this.contextItem = task.getContextItem();
		this.context = task.getContext();
	}

	public synchronized ItemModel getContextItem()
	{
		return contextItem;
	}

	public synchronized Object getContextObject()
	{
		return context;
	}

	@Override
	public void handleError(final TaskService taskService, final TaskModel task, final Throwable error)
	{
		handleErrorCalls.incrementAndGet();
	}

	@Override
	public void run(final TaskService taskService, final TaskModel task) throws RetryLaterException
	{
		runCalls.incrementAndGet();
		setRunData(task);
	}
}
