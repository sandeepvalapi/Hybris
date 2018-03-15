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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;

import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;


/**
 * Test that measures locking performance of traditional <code>synchronized()</code> locks and {@link ReentrantLock}
 * based ones.
 */
@PerformanceTest
public class LockPerformanceTest
{
	@Test
	public void testSynchronizedVsReentrantLock()
	{
		final int LOOPS = 10;
		final int THREADS = 100;
		final int SINGLE_DURATION_SECONDS = 30;

		long synchronizedLockCount = 0;
		long reentrantLockCount = 0;

		for (int i = 0; i < LOOPS; i++)
		{
			if ((i % 2) == 0)
			{
				synchronizedLockCount += getSynchronizedLockCount(THREADS, SINGLE_DURATION_SECONDS);
				reentrantLockCount += getReentrantLockCount(THREADS, SINGLE_DURATION_SECONDS);
			}
			else
			{
				reentrantLockCount += getReentrantLockCount(THREADS, SINGLE_DURATION_SECONDS);
				synchronizedLockCount += getSynchronizedLockCount(THREADS, SINGLE_DURATION_SECONDS);
			}
		}

		final long average = (synchronizedLockCount + reentrantLockCount) / 2;
		final long delta = Math.abs(synchronizedLockCount - reentrantLockCount);
		final int percentage = (int) ((delta * 100) / average);

		System.out.println("synchronizedLockCount:" + synchronizedLockCount);
		System.out.println("reentrantLockCount:" + reentrantLockCount);
		System.out.println("delta:" + delta + " (" + percentage + "% from average)");
	}

	private long getReentrantLockCount(final int THREADS, final int durationSeconds)
	{
		final ReentrantLock lock = new ReentrantLock();

		final RunnerCreator<ReentrantLockRunner> creator = new RunnerCreator<ReentrantLockRunner>()
		{
			@Override
			public ReentrantLockRunner newRunner(final int threadNumber)
			{
				return new ReentrantLockRunner(lock);
			}
		};
		final TestThreadsHolder<ReentrantLockRunner> threads = new TestThreadsHolder<ReentrantLockRunner>(THREADS, creator);

		threads.startAll();
		try
		{
			Thread.sleep(durationSeconds * 1000);
		}
		catch (final InterruptedException e)
		{
			// ok
		}
		assertTrue(threads.stopAndDestroy(durationSeconds));
		assertEquals(Collections.EMPTY_MAP, threads.getErrors());

		long overallCount = 0;
		for (final AbstractLockRunner r : threads.getRunners())
		{
			overallCount += r.count;
		}

		return overallCount;
	}


	private long getSynchronizedLockCount(final int THREADS, final int durationSeconds)
	{
		final Object syncLockObj = new Object();

		final RunnerCreator<SyncLockRunner> creator = new RunnerCreator<SyncLockRunner>()
		{
			@Override
			public SyncLockRunner newRunner(final int threadNumber)
			{
				return new SyncLockRunner(syncLockObj);
			}
		};
		final TestThreadsHolder<SyncLockRunner> threads = new TestThreadsHolder<SyncLockRunner>(THREADS, creator);

		threads.startAll();
		try
		{
			Thread.sleep(durationSeconds * 1000);
		}
		catch (final InterruptedException e)
		{
			// ok
		}
		assertTrue(threads.stopAndDestroy(durationSeconds));
		assertEquals(Collections.EMPTY_MAP, threads.getErrors());

		long overallCount = 0;
		for (final AbstractLockRunner r : threads.getRunners())
		{
			overallCount += r.count;
		}

		return overallCount;
	}

	abstract static class AbstractLockRunner implements Runnable
	{
		volatile long count;
		long dummy = 0;

		@Override
		public void run()
		{
			final Thread t = Thread.currentThread();

			long tmp = 0;

			while (!t.isInterrupted())
			{
				lockUnlock();
				tmp++;
			}

			count = tmp;
		}

		void dummyOp()
		{
			dummy += System.nanoTime();
		}

		abstract void lockUnlock();
	}

	static class ReentrantLockRunner extends AbstractLockRunner
	{
		final ReentrantLock lock;

		ReentrantLockRunner(final ReentrantLock lock)
		{
			this.lock = lock;
		}

		@Override
		void lockUnlock()
		{
			lockUnlockNonInterruptibly();
		}

		private void lockUnlockNonInterruptibly()
		{
			lock.lock();
			dummyOp();
			lock.unlock();
		}

		@SuppressWarnings("unused")
		private void lockUnlockInterruptibly()
		{
			boolean locked = false;
			try
			{
				lock.lockInterruptibly();
				locked = true;

				dummyOp();
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
				// expected this due to runners being stopped
			}
			finally
			{
				if (locked)
				{
					lock.unlock();
				}
			}
		}
	}

	static class SyncLockRunner extends AbstractLockRunner
	{
		final Object toLock;

		SyncLockRunner(final Object toLock)
		{
			this.toLock = toLock;
		}

		@Override
		void lockUnlock()
		{
			synchronized (toLock)
			{
				dummyOp();
			}
		}

	}

}
