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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.ThreadLocalUtilities;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Ignore;
import org.junit.Test;


/**
 *
 */
@Ignore("BAM-251 PLA-11427")
public class ThreadLocalMemoryCleanupTest
{

	final int THREADS = 5;
	final int LOCKS = 50000;
	final int IN_THREAD_CYCLES = 10;
	final int ALLOWED_GAIN_KB_PER_THREAD = 100;
	final int ALLOWED_GAIN = THREADS * ALLOWED_GAIN_KB_PER_THREAD;
	final int TEST_CYCLES = 10;

	@Test
	public void testTLPreserving()
	{
		final Integer integer = Integer.valueOf(1234);

		final ThreadLocal<Integer> preserved1 = new ThreadLocal<Integer>();
		preserved1.set(integer);

		final ThreadLocal<Integer> preserved2 = new ThreadLocal<Integer>();
		preserved2.set(integer);

		assertEquals(integer, preserved1.get());
		assertEquals(integer, preserved2.get());

		final Map<ThreadLocal, Object> beforeMap = ThreadLocalUtilities.extractThreadLocalValuesForCurrentThread();

		assertTrue(beforeMap.size() >= 2);

		final ThreadLocal<Integer> removed1 = new ThreadLocal<Integer>();
		removed1.set(integer);

		final ThreadLocal<Integer> removed2 = new ThreadLocal<Integer>();
		removed2.set(integer);

		assertEquals(integer, removed1.get());
		assertEquals(integer, removed2.get());

		ThreadLocalUtilities.clearThreadLocalMemoryForCurrentThread(beforeMap);

		assertEquals(integer, preserved1.get());
		assertEquals(integer, preserved2.get());

		assertNull(removed1.get());
		assertNull(removed2.get());

	}

	@Test
	public void testThreadLocalCleanup() throws InterruptedException
	{
		initUtilities();

		// create locks
		final long beforeLocks = TestUtils.dumpMemory();
		System.out.println("Before locks creation: " + beforeLocks);

		final List<ReentrantReadWriteLock> locks = createLocks();

		final List<Long> deltas = new ArrayList<Long>();

		for (int i = 0; i < TEST_CYCLES; i++)
		{
			deltas.add(Long.valueOf(innerTestThreadLocalCleanup(locks, true)));
		}

		long all = 0;
		for (final Long delta : deltas)
		{
			all += delta.longValue();
		}

		final long delta = all / deltas.size();
		assertTrue("ThreadLocal cleanup delta " + delta + " exceeded allowed gain " + ALLOWED_GAIN, delta <= ALLOWED_GAIN);
	}

	private long innerTestThreadLocalCleanup(final List<ReentrantReadWriteLock> locks, final boolean useCleanup)
			throws InterruptedException
	{
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch started = new CountDownLatch(THREADS);
		final CountDownLatch done = new CountDownLatch(THREADS);
		final CountDownLatch shouldEnd = new CountDownLatch(1);
		final CountDownLatch end = new CountDownLatch(THREADS);


		// start threads
		final long beforeThreads = TestUtils.dumpMemory();
		startThreads(createRunnable(start, started, done, shouldEnd, end, locks, useCleanup));
		started.await();

		// do work
		final long beforeWork = TestUtils.dumpMemory();
		start.countDown();
		done.await();


		// allow threads to die
		final long afterWork = TestUtils.dumpMemory();
		shouldEnd.countDown();
		end.await();

		final long afterThreads = TestUtils.dumpMemory();

		final long delta = (afterWork - beforeWork);

		System.out.println("Before threads creation: " + beforeThreads);
		System.out.println("Before work: " + beforeWork);
		System.out.println("After work: " + afterWork);
		System.out.println("After threads died: " + afterThreads);
		System.out.println("delta work:" + delta);

		return delta;
	}

	private void startThreads(final Runnable run)
	{
		for (int i = 0; i < THREADS; i++)
		{
			new RegistrableThread(run).start();
		}
	}

	private List<ReentrantReadWriteLock> createLocks()
	{
		final List<ReentrantReadWriteLock> locks = new ArrayList<ReentrantReadWriteLock>(LOCKS);
		for (int i = 0; i < LOCKS; i++)
		{
			locks.add(new ReentrantReadWriteLock());
		}
		return locks;
	}

	private void initUtilities()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(Utilities.getAllInterfaces(this.getClass()));

		// avoid dead code removal from above
		sb.setLength(0);
		System.out.print(sb.toString());

	}

	private Runnable createRunnable(final CountDownLatch start, final CountDownLatch started, final CountDownLatch done,
			final CountDownLatch shouldEnd, final CountDownLatch end, final List<ReentrantReadWriteLock> locks,
			final boolean useCleanup)
	{
		return new Runnable()
		{

			final ThreadLocal<Boolean> dummy = new ThreadLocal<Boolean>()
			{
				@Override
				protected Boolean initialValue()
				{
					return Boolean.TRUE;
				}
			};

			@Override
			public void run()
			{
				boolean sentDone = false;
				try
				{
					init();
					started.countDown();
					start.await();

					Map<ThreadLocal, Object> before = null;

					for (int i = 0; i < IN_THREAD_CYCLES; i++)
					{
						if (useCleanup)
						{
							before = ThreadLocalUtilities.extractThreadLocalValuesForCurrentThread();
						}
						innerRun();

						// update onw TL to simulate preservable entry
						final Boolean b = dummy.get();
						if (b.booleanValue()) // always true
						{
							// cleanup
							if (useCleanup)
							{
								ThreadLocalUtilities.clearThreadLocalMemoryForCurrentThread(before);
							}
						}
					}
					done.countDown();
					sentDone = true;
					shouldEnd.await();
				}
				catch (final InterruptedException e1)
				{
					// ok
				}
				finally
				{
					if (!sentDone)
					{
						done.countDown();
					}
					end.countDown();
				}
			}

			void init()
			{
				dummy.set(Boolean.TRUE);
				if (useCleanup)
				{
					// init
					ThreadLocalUtilities.extractThreadLocalValuesForCurrentThread();
				}
			}

			void innerRun()
			{
				for (final ReadWriteLock lock : locks)
				{
					lock.readLock().lock();
					lock.readLock().unlock();
				}
			}
		};
	}

}
