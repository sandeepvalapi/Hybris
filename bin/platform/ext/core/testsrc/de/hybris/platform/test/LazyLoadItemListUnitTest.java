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
package de.hybris.platform.test;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Unit test for checking thread safety of read operation for {@link LazyLoadItemList}.
 */
@PerformanceTest
public class LazyLoadItemListUnitTest
{
	private static final Logger LOG = Logger.getLogger(LazyLoadItemListUnitTest.class.getName());

	/**
	 * The idea is to access a {@link LazyLoadItemList} with multiple threads each one hitting a different page. This is
	 * considered the worst case scenario simply because each time a different page is requested the internal page buffer
	 * needs to be re-calculated.
	 * 
	 * Please also note that we replaces the actual Item loading logic by one that is just wrapping underlying PKs into
	 * Longs.
	 */
	@Test
	public void testConcurrentAccess()
	{
		final int TURNS = 100 * 1000 * 1000;
		final int THREADS = 100;
		final int PAGE_SIZE = 10;

		//now this is the real test
		testConcurrentAccess(TURNS, THREADS, PAGE_SIZE, 60, true);
		testConcurrentAccess(TURNS, THREADS, PAGE_SIZE, 60, false);
	}

	@Test
	public void testSynchronousAccess()
	{
		final int TURNS = 100 * 1000 * 1000;

		// test sanity check: all should be fine with just one thread
		testConcurrentAccess(TURNS, 1, 100, 10, true);
		testConcurrentAccess(TURNS, 1, 100, 10, false);
	}

	/**
	 * Tests
	 * <p>
	 * a random access of entries on the page
	 * <p>
	 * a consecutive access of entries on the page
	 */
	private void testConcurrentAccess(final int TURNS, final int THREADS, final int PAGE_SIZE, final int WAIT_SECONDS,
			final boolean listUsesSynchronization)
	{

		final int PK_COUNT = THREADS * PAGE_SIZE;
		final List<PK> pks = new ArrayList<PK>(PK_COUNT);
		for (int i = 0; i < PK_COUNT; i++)
		{
			pks.add(PK.createFixedCounterPK(1, i));
		}
		//the list has as keys PK
		final LazyLoadItemList<Long> testList = new LazyLoadItemList<Long>(null, pks, PAGE_SIZE)
		{
			// test with or without synchronization
			@Override
			protected de.hybris.platform.core.LazyLoadItemList.BufferedPage<Long> switchBufferedPage(final int totalIndex)
			{
				return listUsesSynchronization ? super.switchBufferedPageSynchronized(totalIndex) : super
						.switchBufferedPageNoLock(totalIndex);
			}

			//the list has as values for keys consecutive long values
			@Override
			protected List<Long> loadPage(final List<PK> pks)
			{
				final List<Long> page = new ArrayList<Long>(pks.size());
				for (final PK pk : pks)
				{
					page.add(Long.valueOf(pk.getCounter()));
				}
				return page;
			}
		};
		//random access test
		final TestThreadsHolder<LazyListRandomAccessRunner> randomAccessHolder = new TestThreadsHolder<LazyListRandomAccessRunner>(//
				THREADS, //
				new RunnerCreator<LazyListRandomAccessRunner>()
				{
					@Override
					public LazyListRandomAccessRunner newRunner(final int threadNumber)
					{
						final int startIdx = threadNumber * PAGE_SIZE;
						return new LazyListRandomAccessRunner(startIdx, PAGE_SIZE, TURNS, testList);
					}
				});

		randomAccessHolder.startAll();
		try
		{
			Thread.sleep(WAIT_SECONDS * 1000);
		}
		catch (final InterruptedException e)
		{
			//ignoring!!!!
		}
		org.junit.Assert.assertTrue("not all test threads shut down orderly", randomAccessHolder.stopAndDestroy(15));
		org.junit.Assert.assertEquals("found worker errors", Collections.EMPTY_MAP, randomAccessHolder.getErrors());
		for (final LazyListRandomAccessRunner runner : randomAccessHolder.getRunners())
		{
			org.junit.Assert.assertEquals("runner " + runner + " had error turns", Collections.EMPTY_LIST, runner.errorTurns);
		}

		//ordered test
		final TestThreadsHolder<LazyListRandomAccessRunner> orderedHolder = new TestThreadsHolder<LazyListRandomAccessRunner>(//
				THREADS, //
				new RunnerCreator<LazyListRandomAccessRunner>()
				{
					@Override
					public LazyListRandomAccessRunner newRunner(final int threadNumber)
					{
						final int startIdx = threadNumber * PAGE_SIZE;
						return new LazyListConsecutiveAccessRunner(startIdx, PAGE_SIZE, TURNS, testList);
					}
				});

		orderedHolder.startAll();
		try
		{
			Thread.sleep(WAIT_SECONDS * 1000);
		}
		catch (final InterruptedException e)
		{
			//ignoring
		}
		org.junit.Assert.assertTrue("not all test threads shut down orderly", orderedHolder.stopAndDestroy(15));
		org.junit.Assert.assertEquals("found worker errors", Collections.EMPTY_MAP, orderedHolder.getErrors());
		for (final LazyListRandomAccessRunner runner : orderedHolder.getRunners())
		{
			org.junit.Assert.assertEquals("runner " + runner + " had error turns", Collections.EMPTY_LIST, runner.errorTurns);
		}

	}

	static class LazyListRunnerError
	{
		final int turn;
		final long valueDifference;
		final int value;
		final String exceptionStackTrace;

		LazyListRunnerError(final int turn, final long diff, final int idx)
		{
			this.turn = turn;
			this.valueDifference = diff;
			this.exceptionStackTrace = null;
			this.value = idx;
		}

		LazyListRunnerError(final int turn, final int idx, final Exception exception)
		{
			this.turn = turn;
			this.valueDifference = 0;
			this.exceptionStackTrace = Utilities.getStackTraceAsString(exception);
			this.value = idx;
		}

		@Override
		public String toString()
		{
			if (valueDifference != 0)
			{
				return "ValueDifference[" + turn + "]<" + valueDifference + "> for  " + value;
			}
			else
			{
				return "Exception[" + turn + "]<" + exceptionStackTrace + "> for " + value;
			}
		}
	}

	static private class LazyListRandomAccessRunner implements Runnable
	{
		private final int start;
		private final int count;
		private final int turns;
		protected final List<Long> testList;

		//int hitsSucceded = 0;

		volatile List<LazyListRunnerError> errorTurns;

		LazyListRandomAccessRunner(final int start, final int count, final int turns, final List<Long> testList)
		{
			this.turns = turns;
			this.start = start;
			this.count = count;
			this.testList = testList;
		}

		@Override
		public void run()
		{
			final Random random = new Random(System.nanoTime());
			final int pageSize = count;
			final int startIdx = start;
			final int max = turns;

			final List<LazyListRunnerError> recordedErrorTurns = new LinkedList<LazyListRunnerError>();

			for (int i = 0; i < max && !Thread.currentThread().isInterrupted(); i++)
			{
				probeList(random, pageSize, startIdx, recordedErrorTurns, i);
			}
			this.errorTurns = recordedErrorTurns; // volatile write
		}

		protected void probeList(final Random random, final int pageSize, final int startIdx,
				final List<LazyListRunnerError> recordedErrorTurns, final int indexAt)
		{
			int idx = 0;
			try
			{
				//idx should
				idx = startIdx + (Math.abs(random.nextInt()) % pageSize);
				idx = Math.abs(idx);
				//be equal a value for this idx
				final Long value = testList.get(idx);
				final long diff = value.longValue() - idx;
				if (diff != 0)
				{
					//ups but it is not !!!
					recordedErrorTurns.add(new LazyListRunnerError(indexAt, diff, idx));
				}

			}
			catch (final Exception e)
			{
				LOG.error(e);
				recordedErrorTurns.add(new LazyListRunnerError(indexAt, idx, e));
			}
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "(" + start + "," + count + "x)";
		}
	}

	static private class LazyListConsecutiveAccessRunner extends LazyListRandomAccessRunner
	{


		LazyListConsecutiveAccessRunner(final int start, final int count, final int turns, final List<Long> testList)
		{
			super(start, count, turns, testList);
		}


		@Override
		protected void probeList(final Random random, final int pageSize, final int startIdx,
				final List<LazyListRunnerError> recordedErrorTurns, final int indexAt)
		{
			int idx = 0;
			try
			{
				//idx should
				idx = (indexAt % pageSize);
				idx = Math.abs(idx);
				//be equal a value for this idx
				final Long value = testList.get(idx);
				final long diff = value.longValue() - idx;
				if (diff != 0)

				{
					//ups but it is not !!!
					recordedErrorTurns.add(new LazyListRunnerError(indexAt, diff, idx));
				}
			}
			catch (final Exception e)
			{
				LOG.error(e);
				recordedErrorTurns.add(new LazyListRunnerError(indexAt, idx, e));
			}
		}
	}
}
