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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.util.Perf;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.collections.YFastMap;
import de.hybris.platform.util.collections.YMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.junit.Test;


@PerformanceTest
public class YFastMapPerformanceTest extends AbstractMapTest
{
	private final static Logger LOG = Logger.getLogger(YFastMapPerformanceTest.class);

	@Override
	protected Map createMapInstance()
	{
		return new YFastMap();
	}

	@Test
	public void testCreationFromMap()
	{
		final Map<String, String> template = new HashMap<String, String>();
		template.put("foo", "bar");
		template.put("hello", "world");

		// see PLA-9945 - test if creation really works with template map
		final YFastMap<String, String> testMap = new YFastMap<String, String>(template);

		assertEquals(template.get("foo"), testMap.get("foo"));
		assertEquals(template.get("hello"), testMap.get("hello"));
		assertEquals(template.get("xxx"), testMap.get("xxx"));

		testMap.put("x", "y");
	}

	@Test
	public void testPutAll()
	{
		// we want to make sure that putAll() is *not* calling put()
		// but delegates correctly to putAll() of the underlying map
		final Map putAllTestMap = new YFastMap()
		{
			@Override
			public Object put(final Object key, final Object value)
			{
				// add exception to YFastMap
				fail("YFastMap.putAll() has been calling YFastMap.put() instead of putAll() on delegate map");
				return null;
			}
		};

		// this must not throw a exception
		putAllTestMap.putAll(Collections.singletonMap("key", "value"));

		assertTrue(putAllTestMap.containsKey("key"));
		assertEquals("value", putAllTestMap.get("key"));
	}

	@Test
	public void testYMapConcurrency() throws Exception
	{
		final YMap map = new YFastMap();

		final Object KEY = new Object();

		final int MS_PER_RUN = 1000;
		final int THREADS = 200;
		//
		final Perf p = new Perf(THREADS, false)
		{
			@Override
			public void body() throws Exception
			{
				map.clear();
				map.put(KEY, Long.valueOf(getExecutions()));
				final Map.Entry o = map.getEntry(KEY);
				if (o != null && o.getValue() == null)
				{
					/* {} */throw new RuntimeException("entryvalue was null!");
				}
			}
		};
		p.go(MS_PER_RUN, THREADS);

		final long l = p.getExecutions();
		p.close();
		LOG.info(l + " YMap executions");
	}

	@Test
	public void testYMap0PercentReader() throws Exception
	{
		testYMapConcurrencyGeneric(100, 200, 0, 1000000, 5, 3, 40);
	}

	@Test
	public void testYMapConcurrency50PercentReader() throws Exception
	{
		testYMapConcurrencyGeneric(100, 100, 100, 1000000, 5, 3, 30);
	}

	@Test
	public void testYMapConcurrency90PercentReader() throws Exception
	{
		testYMapConcurrencyGeneric(100, 20, 180, 1000000, 5, 3, 25);
	}

	@Test
	public void testYMapConcurrency99PercentReader() throws Exception
	{
		testYMapConcurrencyGeneric(100, 2, 198, 1000000, 5, 3, 20);
	}

	private void testYMapConcurrencyGeneric(final int INITIAL_CAPACITY, final int WRITERS, final int READERS, final int RECORDS,
			final int TIME, final int TURNS, final int MAX_WAIT)
	{
		final String message = "Put/Get test for %d writers and %d readers on %s reached %,d puts and %,d gets\n";

		Map map = null;
		long totalPuts = 0;
		long totalGets = 0;
		for (int i = 0; i < TURNS; i++)
		{
			map = new ConcurrentHashMap(INITIAL_CAPACITY, 0.75f, 64);
			final long[] stats = testMapConcurrency(map, WRITERS, READERS, RECORDS, TIME, MAX_WAIT);
			totalPuts += stats[0];
			totalGets += stats[1];
		}
		System.out.printf(message, Integer.valueOf(WRITERS), Integer.valueOf(READERS), map.getClass().getName(),
				Long.valueOf(totalPuts / TURNS), Long.valueOf(totalGets / TURNS));

		totalPuts = 0;
		totalGets = 0;
		for (int i = 0; i < TURNS; i++)
		{
			map = Collections.synchronizedMap(new HashMap(INITIAL_CAPACITY));
			final long[] stats = testMapConcurrency(map, WRITERS, READERS, RECORDS, TIME, MAX_WAIT);
			totalPuts += stats[0];
			totalGets += stats[1];
		}
		System.out.printf(message, Integer.valueOf(WRITERS), Integer.valueOf(READERS), map.getClass().getName(),
				Long.valueOf(totalPuts / TURNS), Long.valueOf(totalGets / TURNS));

		totalPuts = 0;
		totalGets = 0;
		for (int i = 0; i < TURNS; i++)
		{
			map = new YFastMap(INITIAL_CAPACITY);
			final long[] stats = testMapConcurrency(map, WRITERS, READERS, RECORDS, TIME, MAX_WAIT);
			totalPuts += stats[0];
			totalGets += stats[1];
		}
		System.out.printf(message, Integer.valueOf(WRITERS), Integer.valueOf(READERS), map.getClass().getName(),
				Long.valueOf(totalPuts / TURNS), Long.valueOf(totalGets / TURNS));
	}

	private long[] testMapConcurrency(final Map map, final int writers, final int readers, final int records,
			final int secondsToRun, final int maxSecondsToWait)
	{
		assertNotNull(map);
		assertTrue(writers >= 0);
		assertTrue(readers >= 0);
		assertTrue(records > 0);
		assertTrue(secondsToRun > 0);

		final int WRITERS = writers;
		final int MAX = WRITERS + readers;
		final int RECORDS = 1000000;
		final int TIMESECONDS = 5;

		final Object[][] testData = new Object[RECORDS][2];
		generateTestData(testData);

		final Map<Integer, Throwable> errors = new ConcurrentHashMap<Integer, Throwable>(MAX);
		final AtomicLong putCounter = new AtomicLong(0);
		final AtomicLong getCounter = new AtomicLong(0);
		final List<Thread> threads = new ArrayList<Thread>();

		threads.addAll(setUpWriters(errors, 0, WRITERS, putCounter, testData, map));
		threads.addAll(setUpReaders(errors, WRITERS, MAX - WRITERS, getCounter, map, testData));
		startAll(threads);

		// wait for some time
		try
		{
			Thread.sleep(TIMESECONDS * 1000);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		stopThreads(threads);
		assertEquals("not all threads ended properly", 0, waitForThreads(maxSecondsToWait, threads));

		for (final Map.Entry<Integer, Throwable> error : errors.entrySet())
		{
			final boolean isReader = error.getKey().intValue() >= WRITERS;
			final Throwable t = error.getValue();
			LOG.error(Utilities.getStackTraceAsString(t));
			fail((isReader ? "reader error : " : "writer error : ") + t.getMessage());
		}
		return new long[]
		{ putCounter.get(), getCounter.get() };
	}

	private void stopThreads(final List<Thread> threads)
	{
		for (final Thread t : threads)
		{
			t.interrupt();
		}
	}

	private int waitForThreads(final int maxWaitSeconds, final List<Thread> threads)
	{
		final long maxWait = System.currentTimeMillis() + (maxWaitSeconds * 1000);
		int lifeCount = 0;
		do
		{
			lifeCount = 0;
			for (final Thread t : threads)
			{
				if (t.isAlive())
				{
					try
					{
						t.join(500);
					}
					catch (final InterruptedException e)
					{
						Thread.currentThread().interrupt();
						return lifeCount + 1; // just a guess but we have to stop here !!!
					}
					if (t.isAlive())
					{
						lifeCount++;
					}
				}
			}
		}
		while (lifeCount > 0 && System.currentTimeMillis() < maxWait);

		if (lifeCount > 0)
		{
			for (final Thread t : threads)
			{
				if (t.isAlive())
				{
					try
					{
						t.stop();
					}
					catch (final Exception e)
					{
						// swallow
					}
				}
			}
		}

		return lifeCount;
	}

	private void generateTestData(final Object[][] testRecords)
	{
		final int records = testRecords.length;
		for (int i = 0; i < records; i++)
		{
			testRecords[i][0] = Long.valueOf(i);
			testRecords[i][1] = Long.valueOf(i);
		}
	}

	private static void startAll(final List<Thread> threads)
	{
		for (final Thread t : threads)
		{
			t.start();
		}
	}

	private static List<Thread> setUpReaders(final Map<Integer, Throwable> errors, final int offset, final int count,
			final AtomicLong getCounter, final Map<Long, Long> map, final Object[][] testData)
	{
		final List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < count; i++)
		{
			final int idx = offset + i;
			threads.add(new RegistrableThread()
			{
				@Override
				public void internalRun()
				{
					final Thread currentThread = Thread.currentThread();
					long reads = 0;
					while (!currentThread.isInterrupted())
					{
						read();
						reads++;
					}
					getCounter.addAndGet(reads);// transfer writes at last - avoid inner loop sync other than map-inflicted
				}

				private void read()
				{
					try
					{
						final int testRowIndex = (int) (Math.random() * testData.length);
						final Object key = testData[testRowIndex][0];

						if (map.containsKey(key))
						{
							assertEquals(testData[testRowIndex][1], map.get(key));
						}
					}
					catch (final Throwable t)
					{
						errors.put(Integer.valueOf(idx), t);
					}
				}
			});
		}
		return threads;
	}

	private static List<Thread> setUpWriters(final Map<Integer, Throwable> errors, final int offset, final int count,
			final AtomicLong counter, final Object[][] testData, final Map map)
	{
		final List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < count; i++)
		{
			final int idx = offset + i;
			threads.add(new RegistrableThread()
			{
				@Override
				public void internalRun()
				{
					long writes = 0;
					final Thread currentThread = Thread.currentThread();
					while (!currentThread.isInterrupted())
					{
						write();
						writes++;
					}
					counter.addAndGet(writes); // transfer writes at last - avoid inner loop sync other than map-inflicted
				}

				private void write()
				{
					try
					{
						final int testRowIndex = (int) (Math.random() * testData.length);

						final Object key = testData[testRowIndex][0];
						final Object value = testData[testRowIndex][1];
						map.put(key, value);
					}
					catch (final Throwable t)
					{
						errors.put(Integer.valueOf(idx), t);
					}
				}
			});
		}
		return threads;
	}
}
