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

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.impl.DefaultCache;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Test {@link AbstractCacheUnit} and {@link Cache} behavior, especially in multi-threaded mode.
 */
@Ignore("BAM-520 PLA-11076")
public class AbstractCacheUnitThreadTest extends HybrisJUnit4Test
{

	private static final int THREADS = 100;
	private static final int CACHE_SIZE = 50000;
	private static final int MAX_GAIN_PER_THREAD = 50;
	private static final int DURATION_SEC = 20;


	/**
	 * Tests multiple threads accessing {@link AbstractCacheUnit} and implicitely {@link Cache}, without testing for
	 * correctness!
	 * 
	 * Instead it compares the memory before access, after filling up and after clearing the cache. The test will fail if
	 * after clear the memory gain is higher than a allowed gain (atm 50 bytes per thread, which is 5000 bytes for 100
	 * threads).
	 */
	@Test
	public void testMultiThreadedAccessAndMemory() throws InterruptedException
	{
		Registry.getCurrentTenant().getCache().clear();

		final Cache cache = new DefaultCache(Registry.getCurrentTenantNoFallback(), CACHE_SIZE);

		final long memoryBefore = TestUtils.dumpMemory();

		TestThreadsHolder<MyPerfRunner> threadHolder = new TestThreadsHolder<MyPerfRunner>(THREADS,
				new RunnerCreator<MyPerfRunner>()
				{
					@Override
					public MyPerfRunner newRunner(final int threadNumber)
					{
						return new MyPerfRunner(cache);
					}
				});

		threadHolder.startAll();

		Thread.sleep(DURATION_SEC * 1000);

		assertTrue("Not all runners finished orderly", threadHolder.stopAndDestroy(10));
		assertNoRunnerErrors(threadHolder);

		final long memoryFullCache = TestUtils.dumpMemory();

		cache.clear();

		threadHolder = null;

		final long memoryAfterClear = TestUtils.dumpMemory();

		System.out.println("Memory before run: " + memoryBefore + " kb");
		System.out.println("Memory after run: " + memoryFullCache + " kb");
		System.out.println("Memory after clear: " + memoryAfterClear + " kb");

		final long delta = memoryAfterClear - memoryBefore;
		final long maxAllowedDelta = THREADS * MAX_GAIN_PER_THREAD;

		assertTrue("memory delta of " + delta + " exceeds allowed delta " + maxAllowedDelta + "!", delta <= maxAllowedDelta);
	}

	private void assertNoRunnerErrors(final TestThreadsHolder threadHolder)
	{
		final Map<Integer, Exception> errors = threadHolder.getErrors();
		assertEquals("got unexpected runner errors: " + errors, Collections.EMPTY_MAP, errors);

	}

	/**
	 * Tests whether {@link Cache#getOrAddUnit(AbstractCacheUnit)} is ensuring that only one unit per key is being added
	 * even if multiple threads are trying that at the same time.
	 * 
	 * To enforce potential errors the test runs several turns of all threads trying to add a unit with the same key per
	 * turn.
	 */
	@Test
	public void testExclusiveUnitAddToCache() throws InterruptedException
	{
		testExclusiveUnitAddToCache(1000, THREADS, 1010, 30);
	}

	private void testExclusiveUnitAddToCache(final int TURNS, final int THREADS, final int CACHE_SIZE, final int waitSeconds)
			throws InterruptedException
	{

		final CyclicBarrier gate = new CyclicBarrier(THREADS/*
																			  * , new Runnable() { volatile int turn = 0;
																			  * 
																			  * @Override public void run() {
																			  * System.out.println("Starting turn " + turn); turn++; } }
																			  */);

		final Cache cache = new DefaultCache(Registry.getCurrentTenantNoFallback(), CACHE_SIZE);

		final TestThreadsHolder<ExclusiveAddUnitRunner> holder = new TestThreadsHolder<ExclusiveAddUnitRunner>(THREADS,
				new RunnerCreator<ExclusiveAddUnitRunner>()
				{
					@Override
					public ExclusiveAddUnitRunner newRunner(final int threadNumber)
					{
						return new ExclusiveAddUnitRunner(cache, gate, TURNS);
					}
				});

		// start turns
		holder.startAll();

		// wait for runners to finish
		assertTrue("not all runners finished", holder.waitAndDestroy(30));

		assertNoRunnerErrors(holder);

		cache.destroy();

		// assert same amount of units recorded
		for (final ExclusiveAddUnitRunner runner : holder.getRunners())
		{
			assertEquals(TURNS, runner.recordedUnits.length);
		}

		final Map<Integer, Integer> allTurnFailures = checkRecordedCacheUnits(holder, TURNS);

		assertEquals("Cache.getOrAddUnit() produced different units (failures per turn: " + allTurnFailures + ")", //
				Collections.EMPTY_MAP, allTurnFailures);
	}

	private Map<Integer, Integer> checkRecordedCacheUnits(final TestThreadsHolder<ExclusiveAddUnitRunner> holder, final int TURNS)
	{
		final Map<Integer, Integer> allFailures = new HashMap<Integer, Integer>();

		// assert same unit per turn for all runners
		for (int turn = 0; turn < TURNS; turn++)
		{
			AbstractCacheUnit turnUnit = null;
			int turnFailures = 0;
			for (final ExclusiveAddUnitRunner runner : holder.getRunners())
			{
				final AbstractCacheUnit unit = runner.recordedUnits[turn];
				if (unit == null)
				{
					turnFailures++;
				}
				else if (turnUnit == null)
				{
					turnUnit = unit;
				}
				else if (turnUnit != unit)
				{
					turnFailures++;
				}
			}
			if (turnFailures > 0)
			{
				allFailures.put(Integer.valueOf(turn), Integer.valueOf(turnFailures));
			}
		}
		return allFailures;
	}

	private static class ExclusiveAddUnitRunner implements Runnable
	{
		private final Cache cache;
		private final CyclicBarrier gate;
		private final int numberOfTurns;
		private volatile AbstractCacheUnit[] recordedUnits;

		ExclusiveAddUnitRunner(final Cache c, final CyclicBarrier gate, final int numberOfTurns)
		{
			this.cache = c;
			this.gate = gate;
			this.numberOfTurns = numberOfTurns;
		}

		@Override
		public void run()
		{
			final AbstractCacheUnit[] units = new AbstractCacheUnit[numberOfTurns];
			try
			{
				for (int turn = 0; turn < numberOfTurns; turn++)
				{
					// create new unit as key
					final AbstractCacheUnit newCacheUnit = newUnit(Integer.valueOf(turn));

					//System.out.println("before turn " + turn);

					// wait for all runners to meet at this barrier to have the 
					// most 'brutal' effect on Cache.getOrAddUnit() 
					gate.await();

					// query or add this unit
					// register returned unit -> all runners should have the same each turn
					units[turn] = cache.getOrAddUnit(newCacheUnit);
				}
			}
			catch (final InterruptedException e)
			{
				// fine
			}
			catch (final BrokenBarrierException e)
			{
				// TODO ?
			}
			finally
			{
				this.recordedUnits = units; // volatile write 
			}
		}

		AbstractCacheUnit newUnit(final Object key)
		{
			return new ExclusiveAddUnitTestUnit(cache, key);
		}
	}

	private static class ExclusiveAddUnitTestUnit extends AbstractCacheUnit
	{
		private final Object key;

		public ExclusiveAddUnitTestUnit(final Cache c, final Object key)
		{
			super(c);
			this.key = key;
		}

		@Override
		public Object compute() throws Exception
		{
			return "Value";
		}

		@Override
		public Object[] createKey()
		{
			return new Object[]
			{ this.key };
		}
	}

	/**
	 * Tests whether {@link AbstractCacheUnit#compute()} is really working exclusively when
	 * {@link Cache#isForceExclusiveComputation()} is switched on.
	 * 
	 * To test this we let many threads try to access the cache using one unit per thread having the same key.
	 */
	@Test
	public void testExclusiveComputation() throws InterruptedException
	{
		testExclusiveComputation(THREADS, 1000, 1010, 30);
	}

	private void testExclusiveComputation(final int threads, final int turns, final int cacheSize, final int timeWaitSeconds)
			throws InterruptedException
	{
		final TestThreadsHolder<ExclusiveComputationRunner> threadHolder = prepareExclusiveComputationTest(threads, cacheSize,
				turns);

		threadHolder.startAll();
		assertTrue("Not all runners finished orderly", threadHolder.waitAndDestroy(timeWaitSeconds));
		assertNoRunnerErrors(threadHolder);

		final Map<Integer, Integer> allTurnErrors = checkRecordedComputations(threadHolder, turns);
		assertEquals("Testing exclusive computation mode found errors in some turns: " + allTurnErrors, //
				Collections.EMPTY_MAP, allTurnErrors);
	}

	private TestThreadsHolder<ExclusiveComputationRunner> prepareExclusiveComputationTest(final int threads, final int cacheSize,
			final int turns)
	{
		final Tenant tenant = Registry.getCurrentTenantNoFallback();

		final CyclicBarrier gate = new CyclicBarrier(threads/*
																			  * , new Runnable() { private volatile int turn = 0;
																			  * 
																			  * @Override public void run() {
																			  * System.out.println("Exclusive computation test: before turn "
																			  * + turn); turn++; } }
																			  */);

		final Cache cache = new DefaultCache(tenant, cacheSize, //
				true /* we switch in forceExclusiveComputation no matter what global setting has been chosen */);

		return new TestThreadsHolder<ExclusiveComputationRunner>(threads, new RunnerCreator<ExclusiveComputationRunner>()
		{
			@Override
			public ExclusiveComputationRunner newRunner(final int threadNumber)
			{
				return new ExclusiveComputationRunner(cache, gate, turns);
			}
		});

	}

	private Map<Integer, Integer> checkRecordedComputations(final TestThreadsHolder<ExclusiveComputationRunner> holder,
			final int turns)
	{
		final Map<Integer, Integer> ret = new LinkedHashMap<Integer, Integer>();
		for (int turn = 0; turn < turns; turn++)
		{
			int computingRunnerIndex = -1;
			int turnErrors = 0;
			int runnerIdx = 0;
			for (final ExclusiveComputationRunner r : holder.getRunners())
			{
				if (r.recordedComputationStates[turn]) // runner did compute
				{
					if (computingRunnerIndex == -1) // first one -> fine
					{
						computingRunnerIndex = runnerIdx;
					}
					else
					// second one -> error
					{
						turnErrors++;
					}
				}
				runnerIdx++;
			}
			if (computingRunnerIndex == -1) // no runner did compute -> error too
			{
				turnErrors = -1; // use special number
			}
			if (turnErrors != 0)
			{
				ret.put(Integer.valueOf(turn), Integer.valueOf(turnErrors));
			}
		}
		return ret;
	}

	private static class ExclusiveComputationRunner implements Runnable
	{
		private final Cache cache;
		private final CyclicBarrier gate;
		private final int turns;

		private volatile boolean[] recordedComputationStates;

		ExclusiveComputationRunner(final Cache cache, final CyclicBarrier gate, final int turns)
		{
			this.cache = cache;
			this.turns = turns;
			this.gate = gate;
		}

		@Override
		public void run()
		{
			final boolean[] results = new boolean[turns];
			try
			{
				for (int turn = 0; turn < turns; turn++)
				{
					final ExclusiveComputationCacheUnit u = new ExclusiveComputationCacheUnit(cache, turn);

					// let all workers meet here to have the maximum effect per turn
					gate.await();

					results[turn] = u.simulateGet();
				}
			}
			catch (final Exception e)
			{
				throw new IllegalStateException(e);
			}
			finally
			{
				this.recordedComputationStates = results; // volatile write for safe publishing
			}
		}
	}

	private static class ExclusiveComputationCacheUnit extends AbstractCacheUnit
	{
		private final int turnNumber;
		private boolean computed = false;

		ExclusiveComputationCacheUnit(final Cache c, final int turnNumber)
		{
			super(c);
			this.turnNumber = turnNumber;
		}

		// returns true only if this very unit actually did perform computation
		boolean simulateGet() throws Exception
		{
			final Object result = super.get();

			Assert.assertNotNull(result);

			// we assume that each runner has its own unit and therefore it's safe to
			// access 'computed' without any synchronization
			return this.computed;
		}

		@Override
		public Object compute() throws Exception
		{
			this.computed = true;
			return "Foo";
		}

		@Override
		public Object[] createKey()
		{
			return new Object[]
			{ Integer.valueOf(turnNumber) };
		}
	}

	private static class MyPerfRunner implements Runnable
	{
		private final Cache cache;
		private long overallSum = 0;
		private int serial = 0;

		MyPerfRunner(final Cache cache)
		{
			this.cache = cache;
		}

		@Override
		public void run()
		{
			while (!Thread.currentThread().isInterrupted())
			{
				final TestCacheUnit unit = new TestCacheUnit(cache, serial++);
				Integer result;
				try
				{
					result = (Integer) unit.get();
					overallSum += result.intValue();
				}
				catch (final Exception e)
				{
					throw new IllegalStateException(e);
				}
			}
		}
	}

	private static class TestCacheUnit extends AbstractCacheUnit
	{
		private final int serialNumber;

		TestCacheUnit(final Cache c, final int serialNumber)
		{
			super(c);
			this.serialNumber = serialNumber;
		}

		@Override
		public Object compute() throws Exception
		{
			return Integer.valueOf((int) (Integer.MAX_VALUE * Math.random()));
		}

		@Override
		public Object[] createKey()
		{
			return new Object[]
			{ "test", "unit", Integer.valueOf(serialNumber) };
		}

	}


}
