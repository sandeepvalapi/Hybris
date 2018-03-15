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
package de.hybris.platform.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.HybrisContextFactory.GlobalContextFactory;
import de.hybris.platform.test.TestThreadsHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 *
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test/dummy-test-spring.xml")
public class HybrisContextHolderPerformanceTest
{

	@Value("classpath:test/global-test-spring.xml")
	private org.springframework.core.io.Resource globalContextResource;

	private final static int DURATION_SEC = 10;


	@Before
	public void prepare()
	{
		CounterBasedGenericApplicationContext.COUNTER = 0;
	}

	@Test
	public void testAssureAlwayshaveTheSameGlobalContext()
	{

		final GlobalContextFactory factory = new TestGlobalContextFactory(globalContextResource);

		final HybrisContextHolder holder = new HybrisContextHolder()
		{
			@Override
			HybrisContextFactory getGlobalCtxFactory()
			{
				return factory;
			}
		};




		final TestThreadsHolder<GlobalContextGetter> threads = new TestThreadsHolder<GlobalContextGetter>(100, false)
		{
			@Override
			public GlobalContextGetter newRunner(final int threadNumber)
			{
				return new GlobalContextGetter(holder);
			}
		};

		threads.startAll();
		assertTrue("should have finished after " + DURATION_SEC + " seconds ", threads.waitAndDestroy(DURATION_SEC));
		assertEquals(Collections.EMPTY_MAP, threads.getErrors());

		Object previous = null;

		for (final GlobalContextGetter single : threads.getRunners())
		{
			if (previous != null)
			{
				Assert.assertSame(previous, single.result);

			}
			previous = single.result;
		}

		CounterBasedGenericApplicationContext.assureContextRun(1);

	}

	static class CounterBasedGenericApplicationContext extends GenericApplicationContext
	{
		static int COUNTER = 0;
		static int REFRESH_COUNTER = 0;


		CounterBasedGenericApplicationContext()
		{
			COUNTER++;
		}

		public static void assureContextRun(final int expected)
		{
			Assert.assertEquals("Context should be run  only expected times :" + expected, expected, COUNTER);
			Assert.assertEquals("Context should be refreshed  only expected times :" + expected, expected, REFRESH_COUNTER);
		}

		@Override
		public void refresh() throws BeansException, IllegalStateException
		{
			REFRESH_COUNTER++;
		}
	}



	static class TestGlobalContextFactory extends GlobalContextFactory
	{
		final private Resource resource;

		TestGlobalContextFactory(final Resource resource)
		{
			this.resource = resource;
		}

		@Override
		protected Collection<String> getPlatformExtensions()
		{
			return Arrays.asList("foo");
		}

		@Override
		protected Resource getResource(final String extension, final String location)
		{
			return resource;
		}

		@Override
		protected GenericApplicationContext createNewContext()
		{
			return new CounterBasedGenericApplicationContext();
		}

	}


	static class GlobalContextGetter implements Runnable
	{
		protected final HybrisContextHolder holder;
		private Object result;

		public GlobalContextGetter(final HybrisContextHolder holder)
		{
			this.holder = holder;
		}

		@Override
		public void run()
		{
			result = holder.getGlobalInstance();
		}
	}



}
