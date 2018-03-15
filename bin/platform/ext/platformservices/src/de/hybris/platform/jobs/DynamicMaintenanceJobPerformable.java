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
package de.hybris.platform.jobs;

import de.hybris.platform.core.BeanShellUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.internal.model.DynamicMaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import bsh.EvalError;
import bsh.Interpreter;


/**
 * Executes the beanshell code from the {@link DynamicMaintenanceCleanupJobModel}
 */
public class DynamicMaintenanceJobPerformable extends AbstractMaintenanceJobPerformable implements ApplicationContextAware
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DynamicMaintenanceJobPerformable.class.getName());
	private ApplicationContext applicationContext;
	private String searchCode = null, cleanupCode = null;



	/**
	 * The beanshell field from the job model is readed and executed. This code *must* return a FlexibleSearchQuery
	 * onject.
	 */
	@Override
	public FlexibleSearchQuery getFetchQuery(final CronJobModel cronJob)
	{
		checkJobDynamicParameter(cronJob);

		final Interpreter interpreter = setupBeanshellInterpreter(null);
		FlexibleSearchQuery fsq = null;
		try
		{
			fsq = (FlexibleSearchQuery) interpreter.eval(searchCode);
			if (fsq == null)
			{
				throw new IllegalStateException("The beanshell search code did not return a FlexibleSearchQuery object");
			}
		}
		catch (final EvalError e)
		{
			throw new IllegalStateException("Could not eval beanshell search code. Does it return a FlexibleSearchQuery object?", e);
		}
		return fsq;
	}

	/**
	 * If no beanshell code for the cleanup part is provided, the default behaviour is: iterate through the list and call
	 * on each model modelService.remove(model)
	 */
	@Override
	public void process(final List<ItemModel> elements, final CronJobModel cronJob)
	{
		if (cleanupCode == null)
		{
			//default remove behaviour
			for (final Iterator<ItemModel> iter = elements.iterator(); iter.hasNext();)
			{
				modelService.remove(iter.next());
			}
		}
		else
		{
			final Interpreter interpreter = setupBeanshellInterpreter(elements);
			try
			{
				interpreter.eval(cleanupCode);
			}
			catch (final EvalError e)
			{
				throw new IllegalStateException("Could not execute the cleanup beanshell code.", e);
			}
		}
	}

	private void checkJobDynamicParameter(final CronJobModel cronJob)
	{
		if (cronJob.getJob() instanceof DynamicMaintenanceCleanupJobModel)
		{
			final DynamicMaintenanceCleanupJobModel job = (DynamicMaintenanceCleanupJobModel) cronJob.getJob();
			if (job.getSearchScript() == null || job.getSearchScript().isEmpty())
			{
				throw new IllegalArgumentException("no beanshell search code to execute. This is mandatory");
			}
			searchCode = job.getSearchScript();

			if (job.getProcessScript() != null && !job.getProcessScript().isEmpty())
			{
				cleanupCode = job.getProcessScript();
			}
		}
		else
		{
			throw new IllegalStateException(
					"The job must be a DynamicMaintenanceCleanupJobModel. Cannot execute the beanshell search");
		}
	}

	/*
	 * Returns the beanshell interpretor
	 */
	private Interpreter setupBeanshellInterpreter(final List<ItemModel> elements)
	{
		final Interpreter interpreter = BeanShellUtils.createInterpreter();
		try
		{
			interpreter.getNameSpace().importPackage("de.hybris.platform.servicelayer.search");
			interpreter.set("context", applicationContext);
			interpreter.set("modelService", modelService);
			interpreter.set("elements", elements);
		}
		catch (final EvalError e)
		{
			throw new IllegalStateException("Caught eval exception during setup beanshell interpreter", e);
		}
		return interpreter;
	}

	@Override
	public String getType()
	{
		return DynamicMaintenanceCleanupJobModel._TYPECODE;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}
}
