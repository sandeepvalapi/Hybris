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

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;


/**
 * Base test class providing application context adjustments.
 */
@Ignore
public abstract class AbstractTaskTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(AbstractTaskTest.class);
	protected ApplicationContext applicationContext;
	protected TaskService taskService;
	protected ModelService modelService;
	private final Map<String, Object> customSingletons = new HashMap<String, Object>();
	private boolean runningBefore;

	@Before
	public void setUp() throws Exception
	{
		applicationContext = Registry.getGlobalApplicationContext();
		this.taskService = (TaskService) applicationContext.getBean(TaskService.BEAN_ID);
		this.modelService = (ModelService) applicationContext.getBean("modelService");

		this.customSingletons.clear();
		this.customSingletons.putAll(createCustomSingletons());
		for (final Map.Entry<String, Object> e : this.customSingletons.entrySet())
		{
			((AbstractApplicationContext) applicationContext).getBeanFactory().registerSingleton(e.getKey(), e.getValue());
		}
		this.runningBefore = taskService.getEngine().isRunning();
		turnOnTaskEngine(runningBefore);
	}

	@After
	public void tearDown()
	{
		for (final Map.Entry<String, Object> e : this.customSingletons.entrySet())
		{
			try
			{
				((DefaultSingletonBeanRegistry) ((AbstractApplicationContext) applicationContext).getBeanFactory())
						.destroySingleton(e.getKey());
			}
			catch (final Throwable t)
			{
				System.err.println("error removing '" + e.getKey() + "' from context : " + t.getMessage());
				t.printStackTrace();
			}
		}

		turnOffTaskEngine(runningBefore);
		this.customSingletons.clear();
		this.modelService = null;
		this.taskService = null;
	}

	private void turnOffTaskEngine(final boolean wasRunningBefore)
	{
		try
		{
			LOG.debug("Task engine was " + (wasRunningBefore ? "" : "not ") + "running before test execution");
			if (!wasRunningBefore)
			{
				LOG.debug("Stopping task engine...");
				taskService.getEngine().stop();
			}
		}
		catch (final Exception e)
		{
			LOG.error("error stopping task engine: " + e.getMessage(), e);
		}
	}

	private void turnOnTaskEngine(final boolean wasRunning)
	{
		try
		{
			LOG.debug("Task engine " + (wasRunning ? "already " : "not ") + "running");
			if (!wasRunning)
			{
				LOG.debug("Starting task engine...");
				taskService.getEngine().start();
			}
		}
		catch (final Exception e)
		{
			LOG.error("error starting task engine: " + e.getMessage(), e);
		}
	}

	protected abstract Map<String, Object> createCustomSingletons();
}
