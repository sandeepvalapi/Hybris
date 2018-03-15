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
package de.hybris.platform.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.test.TestThreadsHolder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.ObjectFactory;

import com.bethecoder.ascii_table.ASCIITable;


/**
 * 
 * @deprecated since 5.0 , {@link ThreadSafeScopeMap} is dropped.
 */
@Deprecated
@Ignore
@UnitTest
public class ThreadSafeScopeMapTest
{
	private static final int ITERATIONS = 1000;
	private static final int THREADS_NO = 50;
	private static final String KEY = "keyOne";
	private static final int DURATION_SEC = 10;


	private static final ObjectFactory DUMMY_FACTORY = new ObjectFactory()
	{
		@Override
		public Object getObject() throws org.springframework.beans.BeansException
		{
			try
			{
				Thread.sleep(System.nanoTime() % 5);
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
			return new Object();
		}
	};

	@Test
	public void testOnlySingleObjectCreated()
	{
		testLookupSingleKey();
	}

	@Test
	public void testOnlySingleObjectCreatedMultipleLoops()
	{
		for (int i = 0; i < 100; i++)
		{
			testLookupSingleKey();
		}
	}




	@Test
	public void testManyThreadsAccessDifferentData()
	{
		for (int i = 0; i < 10; i++)
		{
			testLookupMultipleKeys();
		}
	}


	private void testLookupSingleKey()
	{
		final ThreadSafeScopeMap scopeMap = new ThreadSafeScopeMap(10);

		final TestThreadsHolder<ScopedMapGetter> threads = new TestThreadsHolder<ScopedMapGetter>(THREADS_NO, false)
		{
			@Override
			public ScopedMapGetter newRunner(final int threadNumber)
			{
				return new ScopedMapGetter(scopeMap);
			}
		};
		threads.startAll();
		assertTrue("should have finished after " + DURATION_SEC + " seconds ", threads.waitAndDestroy(DURATION_SEC));
		assertEquals(Collections.EMPTY_MAP, threads.getErrors());

		assertEquals(1, scopeMap.size());

		Object previousResult = null;

		for (final ScopedMapGetter runner : threads.getRunners())
		{
			final Object runnerResult = runner.result;
			if (previousResult != null)
			{
				assertSame(previousResult, runnerResult);
			}
			previousResult = runnerResult;
		}
	}

	private void testLookupMultipleKeys()
	{
		final ThreadSafeScopeMap scopeMap = new ThreadSafeScopeMap(10);

		final String[][] keys = new String[THREADS_NO][ITERATIONS];
		for (int i = 0; i < THREADS_NO; i++)
		{
			keys[i] = new String[ITERATIONS];
			for (int j = 0; j < ITERATIONS; j++)
			{
				keys[i][j] = "some weird key [" + j + "]";
			}
		}

		final TestThreadsHolder<IterativeScopedMapGetter> threads = new TestThreadsHolder<IterativeScopedMapGetter>(THREADS_NO,
				false)
		{
			@Override
			public IterativeScopedMapGetter newRunner(final int threadNumber)
			{
				return new IterativeScopedMapGetter(scopeMap, keys[threadNumber]);
			}
		};

		threads.startAll();
		assertTrue("should have finished after " + DURATION_SEC + " seconds ", threads.waitAndDestroy(DURATION_SEC));
		assertEquals(Collections.EMPTY_MAP, threads.getErrors());

		assertEquals(ITERATIONS, scopeMap.size());

		Object[] previousResults = null;
		for (final IterativeScopedMapGetter r : threads.getRunners())
		{
			final Object[] runnerResults = r.results;
			if (previousResults != null)
			{
				assertEquals(previousResults.length, runnerResults.length);
				for (int i = 0; i < previousResults.length; i++)
				{
					assertSame(previousResults[i], runnerResults[i]);
				}
			}
			previousResults = runnerResults;
		}

		//Field beansField = ReflectionUtils.findField(ThreadSafeScopeMap.class,"beans");
		ASCIITable.getInstance().printTable(new String[]
		{ " execution concurrent fetch for " + THREADS_NO + " threads ", " time ms" }, new String[][]
		{
		{ String.valueOf(ITERATIONS), String.valueOf(threads.getStartToFinishMillis()) } });
	}

	static class ScopedMapGetter implements Runnable
	{
		protected final ThreadSafeScopeMap scopedMap;
		private volatile Object result;


		public ScopedMapGetter(final ThreadSafeScopeMap scopedMap)
		{
			this.scopedMap = scopedMap;
		}

		@Override
		public void run()
		{
			result = scopedMap.get(KEY, DUMMY_FACTORY);
		}
	}

	static class IterativeScopedMapGetter implements Runnable
	{
		private final ThreadSafeScopeMap scopedMap;
		private final String[] keys;
		private volatile Object[] results;

		public IterativeScopedMapGetter(final ThreadSafeScopeMap scopedMap, final String... keys)
		{
			this.scopedMap = scopedMap;
			this.keys = keys;
		}

		@Override
		public void run()
		{
			final List<Object> tmpResults = new LinkedList<Object>();
			for (final String single : keys)
			{
				tmpResults.add(scopedMap.get(single, DUMMY_FACTORY));
			}
			this.results = tmpResults.toArray();
		}
	}

}
