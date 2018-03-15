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
package de.hybris.platform.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.WorkerValueQueue.ExecuteWhileWaiting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;



@PerformanceTest
public class WorkerValueQueueTest extends HybrisJUnit4Test
{
	static final int MAX_CYCLES = 30;
	static final int MAX_PKS = 6000;
	static final int MAX_WORKERS = 16;
	static final int MAX_WAIT_QUEUE_SECONDS = 120;

	List<PK> pkList;
	Map<PK, AtomicInteger> pksUseMap;

	@Before
	public void setUp()
	{
		this.pkList = new ArrayList<PK>(MAX_PKS);
		for (int i = 0; i < MAX_PKS; i++)
		{
			pkList.add(PK.createFixedCounterPK(1, i + 1));
		}
		this.pksUseMap = new HashMap<PK, AtomicInteger>();
		for (int i = 0; i < MAX_PKS; i++)
		{
			this.pksUseMap.put(pkList.get(i), new AtomicInteger(0));
		}
	}

	@Test
	public void testPerformance()
	{
		final WorkerValueQueue<PK> queue1 = new DefaultWorkerValueQueue<PK>(MAX_WORKERS, MAX_CYCLES * MAX_PKS);

		long t1 = System.currentTimeMillis();
		performLinearPerformanceTest(queue1, MAX_CYCLES, MAX_PKS);
		long t2 = System.currentTimeMillis();
		System.out.println("Performance test of " + queue1.getClass() + " took " + (t2 - t1) + " ms");

		final WorkerValueQueue<PK> queue2 = new DeprecatedWorkerValueQueue<PK>(MAX_CYCLES * MAX_PKS);

		t1 = System.currentTimeMillis();
		performLinearPerformanceTest(queue2, MAX_CYCLES, MAX_PKS);
		t2 = System.currentTimeMillis();
		System.out.println("Performance test of " + queue2.getClass() + " took " + (t2 - t1) + " ms");
	}

	@Test
	public void testQueueMultipleCycles() throws InterruptedException
	{
		final WorkerValueQueue<PK> queue = new DefaultWorkerValueQueue<PK>(MAX_WORKERS, MAX_CYCLES * MAX_PKS);

		final Master master = simulateWriter(queue, MAX_CYCLES);
		final Collection<Thread> workers = simulateWorkers(queue);

		waitForMaster(master);

		waitForEmptyQueue(queue);

		waitForWorkers(workers);

		assertAllPKsUsed(MAX_CYCLES);
	}

	@Test
	public void testQueueCompleteness() throws InterruptedException
	{
		final WorkerValueQueue<PK> queue = new DefaultWorkerValueQueue<PK>(MAX_WORKERS);

		final Master master = simulateWriter(queue, 1);
		final Collection<Thread> workers = simulateWorkers(queue);

		waitForMaster(master);

		waitForEmptyQueue(queue);

		waitForWorkers(workers);

		assertAllPKsUsed(1);
	}

	protected void performLinearPerformanceTest(final WorkerValueQueue<PK> queue1, final int cycles, final int pkCount)
	{
		assertEquals(pkCount, pkList.size());

		for (int c = 0; c < cycles; c++)
		{
			for (int i = 0; i < pkCount; i++)
			{
				queue1.put(pkList.get(i));
			}
		}
		for (int c = 0; c < MAX_CYCLES; c++)
		{
			for (int i = 0; i < MAX_PKS; i++)
			{
				final int workerNumber = ((c * MAX_PKS) + i) % MAX_WORKERS;
				assertEquals(pkList.get(i), queue1.take(workerNumber));
				queue1.clearValueTaken(workerNumber);
			}
		}
		assertFalse(queue1.isValueTakenOrQueueNotEmpty());
	}

	protected void assertAllPKsUsed(final int expectedUseCount)
	{
		for (int i = 0; i < MAX_PKS; i++)
		{
			final PK pk = pkList.get(i);
			final AtomicInteger ai = this.pksUseMap.get(pk);

			assertEquals("PK " + pk + " got wrong use count " + ai.get(), expectedUseCount, ai.get());
		}
	}

	protected void waitForEmptyQueue(final WorkerValueQueue<PK> queue)
	{
		final long waitUntil = System.currentTimeMillis() + (1000 * MAX_WAIT_QUEUE_SECONDS);

		// 2. now we wait for the queue to become empty again
		queue.waitUntilEmpty(new ExecuteWhileWaiting<PK>()
		{
			@Override
			public boolean execute(final WorkerValueQueue<PK> queue, final PK value)
			{
				final boolean canWait = System.currentTimeMillis() < waitUntil;

				if (!canWait)
				{
					System.err.println("Time is up");
				}
				// will abort waiting if we reach max time
				return canWait;
			}
		});

		assertFalse("queue was not empty", queue.isValueTakenOrQueueNotEmpty());

	}

	protected void waitForMaster(final Thread master) throws InterruptedException
	{
		if (master.isAlive())
		{
			master.join(1000 * MAX_WAIT_QUEUE_SECONDS);
		}
		assertFalse(master.isAlive());
	}

	protected void waitForWorkers(final Collection<Thread> workers)
	{
		// interrupt workers
		for (final Thread worker : workers)
		{
			if (worker.isAlive())
			{
				worker.interrupt();
			}
		}
		Thread.yield();
		// interrupt workers
		for (final Thread worker : workers)
		{
			if (worker.isAlive())
			{
				try
				{
					worker.join(5000);
				}
				catch (final InterruptedException e)
				{
					// 
				}
			}
		}
		for (final Thread worker : workers)
		{
			assertFalse("worker " + worker + " is still alive", worker.isAlive());
		}
	}

	protected static class Master extends RegistrableThread
	{
		final int cycles;
		final List<PK> pkList;
		final WorkerValueQueue<PK> queue;
		AtomicBoolean firstPKadded = new AtomicBoolean(false);

		Master(final WorkerValueQueue<PK> queue, final List<PK> pkList, final int cycles)
		{
			super("TestMaster");
			this.queue = queue;
			this.pkList = pkList;
			this.cycles = cycles;
		}

		@Override
		public void internalRun()
		{
			for (int c = 0; c < cycles; c++)
			{
				System.out.println("master cycle " + c);
				for (int i = 0; i < MAX_PKS; i++)
				{
					final PK pk = pkList.get(i);
					queue.put(pk);
					firstPKadded.set(true);
				}
				if (c + 1 < cycles)
				{
					System.out.println("master cycle " + c + " waits for empty queue");
					queue.waitUntilEmpty();
				}
			}
		}
	}

	protected Master simulateWriter(final WorkerValueQueue<PK> queue, final int cycles)
	{
		final Master t = new Master(queue, pkList, cycles);
		t.start();

		return t;
	}

	protected Collection<Thread> simulateWorkers(final WorkerValueQueue<PK> queue) throws InterruptedException
	{
		final Collection<Thread> threads = new ArrayList<Thread>(MAX_WORKERS);

		for (int i = 0; i < MAX_WORKERS; i++)
		{
			final int workerNumber = i;
			final Thread t = new RegistrableThread("TestWorker-" + workerNumber)
			{
				@Override
				public void internalRun()
				{
					for (PK taken = queue.take(workerNumber); taken != null; taken = queue.take(workerNumber))
					{
						final AtomicInteger ai = pksUseMap.get(taken);
						ai.addAndGet(1);
						queue.clearValueTaken(workerNumber);
					}
				}
			};
			t.start();
			threads.add(t);
		}
		return threads;
	}
}
