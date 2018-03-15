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

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.numberseries.NumberGenerator;
import de.hybris.platform.jalo.numberseries.NumberSeries;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.persistence.numberseries.SerialNumberGenerator;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@PerformanceTest
@Ignore("PLA-11427 PLA-11268")
public class NumberSeriesPerformanceTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(NumberSeriesPerformanceTest.class);

	private NumberSeriesManager numberSeriesManager;

	private static final int DECIMALS = 10;
	private static final String SERIES1 = "numSeries";
	private static final String SERIES2 = "alphanumSeries";
	private static final String SERIES3 = "nonTxSeries";

	@Before
	@SuppressWarnings("deprecation")
	public void setUp() throws Exception
	{
		numberSeriesManager = jaloSession.getNumberSeriesManager();

		numberSeriesManager.createNumberSeries(SERIES1, "034", NumberGenerator.NumberSeriesConstants.TYPES.NUMERIC); // digits = 3 this way
		numberSeriesManager.createNumberSeries(SERIES2, "1ZY", NumberGenerator.NumberSeriesConstants.TYPES.ALPHANUMERIC, DECIMALS);
	}

	@After
	public void tearDown() throws Exception
	{
		try
		{
			numberSeriesManager.removeNumberSeries(SERIES1);
		}
		catch (final JaloInvalidParameterException e) //NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries(SERIES2);
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries(SERIES3);
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries("foo");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries("bar");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries("rawTest1");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries("rawTest2");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries("rawTest3");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
	}

	@Test
	public void testParallel()
	{
		testParallel("foo", 10, 100, -1); // default cache size
		testParallel("bar", 10, 100, 1); // no cache mode
	}

	private void testParallel(final String key, final int seconds, final int numberOfThreads, final int presetCacheSize)
	{
		final int effectiveCacheSize;
		if (presetCacheSize > 0)
		{
			Config.setParameter(NumberSeriesManager.CONFIG_PARAM_NUMBER_CACHE + "." + key, Integer.toString(presetCacheSize));
			effectiveCacheSize = presetCacheSize;
		}
		else
		{
			effectiveCacheSize = Config.getInt(NumberSeriesManager.CONFIG_PARAM_NUMBER_CACHE,
					NumberSeriesManager.DEFAULT_NUMBER_CACHE_SIZE);
		}

		numberSeriesManager.createNumberSeries(key, "1", NumberGenerator.NumberSeriesConstants.TYPES.ALPHANUMERIC, 10);

		final TestThreadsHolder<TestNumberCollector> threads = new TestThreadsHolder<TestNumberCollector>(numberOfThreads, true)
		{
			@Override
			public TestNumberCollector newRunner(final int threadNumber)
			{
				return new TestNumberCollector(key);
			}
		};

		threads.startAll();

		try
		{
			Thread.sleep(seconds * 1000);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt(); // restore flag but continue work -> must stop workers
		}

		assertTrue("not all workers finished properly", threads.stopAndDestroy(30));

		assertEquals(Collections.EMPTY_MAP, threads.getErrors());

		final Set<String> allNumbers = new HashSet<String>();
		// check for duplicates
		for (final TestNumberCollector w : threads.getRunners())
		{
			if (w.numbers != null)
			{
				for (final String s : w.numbers)
				{
					assertTrue("found duplicate number '" + s + "'", allNumbers.add(s));
				}
			}
		}
		LOG.info("Created " + allNumbers.size() + " cached (" + effectiveCacheSize + ") numbers in " + seconds + " seconds ("
				+ allNumbers.size() / seconds + " numbers/s) by " + numberOfThreads + " threads");
	}

	@Test
	public void testNumberPerformance()
	{
		testNumberPerformance("rawTest1", 1, 10);
		testNumberPerformance("rawTest2", 10, 10);
		testNumberPerformance("rawTest3", 100, 10);
	}

	private long testNumberPerformance(final String key, final int numberOfThreads, final int durationSeconds)
	{
		final SerialNumberGenerator gen = Registry.getCurrentTenantNoFallback().getSerialNumberGenerator();
		gen.createSeries(key, NumberSeries.TYPE_NUMERIC, 0);

		final TestThreadsHolder<TestNumberCounter> threads = new TestThreadsHolder<TestNumberCounter>(numberOfThreads, true)
		{
			@Override
			public TestNumberCounter newRunner(final int threadNumber)
			{
				return new TestNumberCounter(key, gen);
			}
		};
		threads.startAll();
		try
		{
			Thread.sleep(durationSeconds * 1000L);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		assertTrue("not all threads finished properly", threads.stopAndDestroy(30));
		assertEquals(Collections.EMPTY_MAP, threads.getErrors());

		long result = 0;
		for (final TestNumberCounter c : threads.getRunners())
		{
			if (c.count > 0)
			{
				result += c.count;
			}
		}

		assertTrue("unexpected result " + result, result > 0);

		final int cacheSize = Config.getInt(NumberSeriesManager.CONFIG_PARAM_NUMBER_CACHE,
				NumberSeriesManager.DEFAULT_NUMBER_CACHE_SIZE);

		LOG.info("generated " + result + " cached (" + cacheSize + ") raw numbers in 10 seconds (" + (result / durationSeconds)
				+ " numbers/s) by " + numberOfThreads + " threads");

		return result;
	}

	private static class TestNumberCounter implements Runnable
	{
		private final String key;
		private final SerialNumberGenerator gen;
		private volatile long count = -1;

		public TestNumberCounter(final String key, final SerialNumberGenerator gen)
		{
			this.key = key;
			this.gen = gen;
		}

		@Override
		public void run()
		{
			JaloSession.getCurrentSession().activate();

			long tmp = 0;
			final Thread thread = Thread.currentThread();

			do
			{
				try
				{
					gen.getUniqueNumber(key);
				}
				catch (final IllegalStateException e)
				{
					// that's ok due to thread bing stopped via interrupt
					assertTrue(e.getCause() instanceof InterruptedException);
				}
				tmp++;
			}
			while (!thread.isInterrupted());

			count = tmp; // volatile write at last

			JaloSession.deactivate();
		}
	}

	private static class TestNumberCollector implements Runnable
	{
		private final String key;
		private volatile Set<String> numbers;

		public TestNumberCollector(final String key)
		{
			this.key = key;
		}

		@Override
		public void run()
		{
			JaloSession.getCurrentSession().activate();

			final HashSet<String> tmp = new HashSet<String>();
			final Thread thread = Thread.currentThread();
			final NumberSeriesManager mgr = new NumberSeriesManager(); // create new manager to force separate number caches

			do
			{
				try
				{
					tmp.add(mgr.getUniqueNumber(key));
				}
				catch (final IllegalStateException e)
				{
					// that's ok due to thread bing stopped via interrupt
					assertTrue(e.getCause() instanceof InterruptedException);
				}
			}
			while (!thread.isInterrupted());

			numbers = tmp; // volatile write at last

			JaloSession.deactivate();
		}
	}
}
