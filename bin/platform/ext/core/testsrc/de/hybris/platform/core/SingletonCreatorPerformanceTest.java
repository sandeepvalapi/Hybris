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

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.SingletonCreator.Creator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


/**
 * Tests SingletonCreator via delegating methods like Registry.getSingleton(Creator).
 */
@PerformanceTest
public class SingletonCreatorPerformanceTest extends HybrisJUnit4Test
{
	@Test
	public void testSingletonMultiThreadedCreation()
	{
		for (int i = 0; i < 6; i++)
		{
			// loop several times to increase error probability ( each run
			// creates new key )
			testSingletonMultiThreadedCreation(50, 10);
		}
	}

	private void testSingletonMultiThreadedCreation(final int THREADS, final int SECONDS)
	{
		final Object key = generateRandomNmber();

		final TestThreadsHolder<CreatorRunner> threads = createCreatorThreadsHolder(THREADS, key);

		threads.startAll();
		try
		{
			Thread.sleep(SECONDS * 1000);
		}
		catch (final InterruptedException e)
		{
			// must continue to stop workers
		}
		assertTrue("not all threads finished properly", threads.stopAndDestroy(10));
		assertEquals("got runner errors", Collections.EMPTY_MAP, threads.getErrors());

		final Integer expectedValueForAll = Registry.getSingleton(createCreator(key, generateRandomNmber()));

		final Set<Integer> mergedValues = new HashSet<Integer>();
		for (final CreatorRunner runner : threads.getRunners())
		{
			mergedValues.addAll(runner.recordedValues);
		}
		assertEquals("got different values for singleton", Collections.singleton(expectedValueForAll), mergedValues);
	}

	private static TestThreadsHolder<CreatorRunner> createCreatorThreadsHolder(final int THERADS, final Object key)
	{
		final RunnerCreator<CreatorRunner> runnerCreator = new TestThreadsHolder.RunnerCreator<CreatorRunner>()
		{
			@Override
			public CreatorRunner newRunner(final int threadNumber)
			{
				return new CreatorRunner(key);
			}
		};
		return new TestThreadsHolder<CreatorRunner>(THERADS, runnerCreator, true);
	}

	static class CreatorRunner implements Runnable
	{
		final Object key;
		volatile Set<Integer> recordedValues;

		CreatorRunner(final Object key)
		{
			this.key = key;
		}

		@Override
		public void run()
		{
			final Set<Integer> allValues = new HashSet<Integer>();
			while (!Thread.currentThread().isInterrupted())
			{
				allValues.add(Registry.getSingleton(createCreator(key, generateRandomNmber())));
			}
			recordedValues = allValues; // thread-safe publication due to volatile write !!!
		}
	}

	private static Integer generateRandomNmber()
	{
		return Integer.valueOf((int) System.nanoTime());
	}

	private static Creator<Integer> createCreator(final Object key, final Integer value)
	{
		return new SingletonCreator.Creator<Integer>()
		{
			@Override
			protected Object getID()
			{
				return key;
			}

			@Override
			protected Integer create() throws Exception
			{
				return value;
			}
		};

	}
}
