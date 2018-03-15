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
package de.hybris.platform.regioncache.generation.impl;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.regioncache.generation.GenerationalCounterService;
import de.hybris.platform.regioncache.key.legacy.LegacyCacheKey;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.util.Key;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.common.base.Joiner;


/**
 *
 */
@PerformanceTest
public class CounterServicePerformanceTest
{
	private static final Logger LOGGER = Logger.getLogger(CounterServicePerformanceTest.class);

	private static final int THREADS = 100;
	private static final int SLEEP = 10;

	private final GenerationalCounterService service = new TypeCodeGenerationalCounterService();


	@Test
	public void testIncrease()
	{
		final TestThreadsHolder<CounterServiceAction> threads = new TestThreadsHolder<CounterServiceAction>(THREADS, false)
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new IncCounterServiceAction(service, "dummyKey");
			}
		};
		Assert.assertTrue("Should stop all threads within 30 sec ", threads.runAll(SLEEP, TimeUnit.SECONDS, 30));

		for (final Throwable runner : threads.getErrors().values())
		{
			runner.printStackTrace();
		}

		assertInternalMap();
	}


	/**
	 * 
	 */
	private void assertInternalMap()
	{
		final Map<?, AtomicLong> mapObject = ((TypeCodeGenerationalCounterService) service).generations;

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Joiner.on(",").join(mapObject.entrySet()));
		}
		Assert.assertEquals(1, mapObject.size());
		Assert.assertEquals(THREADS, mapObject.get(Key.get("dummyKey", "master")).get());
	}

	static abstract class CounterServiceAction implements Runnable
	{

		protected final GenerationalCounterService<String> service;
		protected final String type;

		public CounterServiceAction(final GenerationalCounterService service, final String type)
		{
			this.service = service;
			this.type = type;
		}


		@Override
		abstract public void run();

	}

	static class IncCounterServiceAction extends CounterServiceAction
	{

		/**
		 * 
		 */
		public IncCounterServiceAction(final GenerationalCounterService service, final String type)
		{
			super(service, type);
		}

		@Override
		public void run()
		{
			service.incrementGeneration(type, "master");
		}
	}

	static class TestLegacyCacheKey extends LegacyCacheKey
	{

		/**
		 * 
		 */
		public TestLegacyCacheKey(final String type)
		{
			super(new Object[]
			{ "key1", type }, "master");
			// YTODO Auto-generated constructor stub
		}

	}

}
