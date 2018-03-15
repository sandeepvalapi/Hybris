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
package de.hybris.platform.catalog.jalo.synchronization;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.testframework.HybrisJUnit4ClassRunner;
import de.hybris.platform.testframework.RunListeners;
import de.hybris.platform.testframework.runlistener.LogRunListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@PerformanceTest
@RunWith(HybrisJUnit4ClassRunner.class)
@RunListeners(
{ LogRunListener.class })
public class NewSyncLockingTest
{
	static final int MAX_CYCLES = 50;
	static final int MAX_PKS = 10000;
	static final int MAX_WORKERS = 64;
	static final int MAX_WAIT_SECONDS = 120 * 3;

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
	public void testLocking()
	{
		final WorkerItemLockHolder lockHolder = new ArrayBasedLockHolder(3);

		final int WORKER_A = 0;
		final int WORKER_B = 1;
		final int WORKER_C = 2;

		final PK pk1 = PK.createFixedCounterPK(1, 1);
		final PK pk2 = PK.createFixedCounterPK(1, 2);
		final PK pk3 = PK.createFixedCounterPK(1, 3);

		// test normal locking 
		assertLockable(lockHolder, pk1, WORKER_A);
		assertNotLockable(lockHolder, pk1, WORKER_B);
		assertReleasable(lockHolder, pk1, WORKER_A);
		assertLockable(lockHolder, pk1, WORKER_B);
		assertReleasable(lockHolder, pk1, WORKER_B);

		// test accumulation
		assertLockable(lockHolder, pk1, WORKER_A);
		assertLockable(lockHolder, pk2, WORKER_A);
		assertLockable(lockHolder, pk3, WORKER_A);

		assertNotLockable(lockHolder, pk1, WORKER_B);
		assertNotLockable(lockHolder, pk2, WORKER_B);
		assertNotLockable(lockHolder, pk3, WORKER_B);

		assertNotLockable(lockHolder, pk1, WORKER_C);
		assertNotLockable(lockHolder, pk2, WORKER_C);
		assertNotLockable(lockHolder, pk3, WORKER_C);

		assertReleasable(lockHolder, pk3, WORKER_A);
		assertLockable(lockHolder, pk3, WORKER_C);
		assertReleasable(lockHolder, pk2, WORKER_A);
		assertLockable(lockHolder, pk2, WORKER_B);
		assertReleasable(lockHolder, pk1, WORKER_A);
		assertLockable(lockHolder, pk1, WORKER_C);

		assertNotLockable(lockHolder, pk3, WORKER_A);
		assertReleasable(lockHolder, pk1, WORKER_C);
		assertReleasable(lockHolder, pk3, WORKER_C);
		assertReleasable(lockHolder, pk2, WORKER_B);

		// test reentrant - one pk 50 times locked
		for (int i = 0; i < 50; i++)
		{
			assertLockable(lockHolder, pk1, WORKER_B);
			assertNotLockable(lockHolder, pk1, WORKER_A);
		}
		// 50 times released
		for (int i = 0; i < 50; i++)
		{
			assertNotLockable(lockHolder, pk1, WORKER_A);
			assertReleasable(lockHolder, pk1, WORKER_B);
		}
		assertLockable(lockHolder, pk1, WORKER_A);
		assertReleasable(lockHolder, pk1, WORKER_A);

		// test reentrant - many pks

		assertLockable(lockHolder, pk3, WORKER_B);
		assertLockable(lockHolder, pk2, WORKER_B);
		// 50 times locked
		for (int i = 0; i < 50; i++)
		{
			assertLockable(lockHolder, pk1, WORKER_B);
			assertNotLockable(lockHolder, pk1, WORKER_A);
			assertNotLockable(lockHolder, pk2, WORKER_A);
			assertNotLockable(lockHolder, pk3, WORKER_A);
		}
		// 50 times locked
		for (int i = 0; i < 50; i++)
		{
			assertNotLockable(lockHolder, pk1, WORKER_A);
			assertNotLockable(lockHolder, pk2, WORKER_A);
			assertNotLockable(lockHolder, pk3, WORKER_A);
			assertReleasable(lockHolder, pk1, WORKER_B);
		}
		assertReleasable(lockHolder, pk2, WORKER_B);
		assertReleasable(lockHolder, pk3, WORKER_B);
		assertLockable(lockHolder, pk1, WORKER_A);
		assertReleasable(lockHolder, pk1, WORKER_A);
	}

	private void assertReleasable(final WorkerItemLockHolder lockHolder, final PK pk, final int worker)
	{
		// TODO assert
		lockHolder.release(pk.getLongValue(), worker);
	}

	private void assertLockable(final WorkerItemLockHolder lockHolder, final PK pk, final int worker)
	{
		assertTrue(lockHolder.lock(pk.getLongValue(), worker, 0));
	}

	private void assertNotLockable(final WorkerItemLockHolder lockHolder, final PK pk, final int worker)
	{
		assertFalse(lockHolder.lock(pk.getLongValue(), worker, 0));
	}


	@Test
	public void testArrayBasedLocking() throws InterruptedException
	{
		final WorkerItemLockHolder lockHolder = new ArrayBasedLockHolder(MAX_WORKERS);

		final long time1 = System.currentTimeMillis();

		simulateLocking(lockHolder);

		final long time2 = System.currentTimeMillis();

		System.out.println("locking took " + (time2 - time1) + " ms using " + lockHolder.getClass());

		assertAllUnlocked();
	}

	@Test
	public void testMapBasedLocking() throws InterruptedException
	{
		final WorkerItemLockHolder lockHolder = new MapBasedLockHolder(MAX_WORKERS);

		final long time1 = System.currentTimeMillis();

		simulateLocking(lockHolder);

		final long time2 = System.currentTimeMillis();

		System.out.println("locking took " + (time2 - time1) + " ms using " + lockHolder.getClass());

		assertAllUnlocked();
	}

	protected void simulateLocking(final WorkerItemLockHolder lockHolder) throws InterruptedException
	{
		final ExecutorService exec = Executors.newFixedThreadPool(MAX_WORKERS);

		for (int i = 0; i < MAX_WORKERS; i++)
		{
			final int workerNumber = i;
			exec.execute(new Runnable()
			{
				@Override
				public void run()
				{
					for (int c = 0; c < MAX_CYCLES; c++)
					{
						for (int j = 0; j < MAX_PKS; j++)
						{
							final PK pk = pkList.get(j);
							if (lockHolder.lock(pk.getLongValue(), workerNumber, -1))
							{
								final AtomicInteger atomicInteger = pksUseMap.get(pk);
								final int current = atomicInteger.incrementAndGet();
								if (current > 1)
								{
									System.err.println("more than one worker locked " + pk);
								}
								final int newCurrent = atomicInteger.decrementAndGet();
								if (newCurrent > 0)
								{
									System.err.println("more than one worker locked " + pk);
								}
								lockHolder.release(pk.getLongValue(), workerNumber);
							}
						}
					}
				}
			});
		}
		exec.shutdown();
		assertTrue("Not all worker terminated", exec.awaitTermination(MAX_WAIT_SECONDS, TimeUnit.SECONDS));
	}

	protected void assertAllUnlocked()
	{
		final List<PK> violatingPKs = new ArrayList<PK>();
		for (int i = 0; i < MAX_PKS; i++)
		{
			final PK pk = pkList.get(i);
			final AtomicInteger atomicInteger = this.pksUseMap.get(pk);
			if (atomicInteger.get() > 0)
			{
				System.err.println("found locked PK " + pk + " count:" + atomicInteger.get());
			}
		}
		assertEquals(Collections.EMPTY_LIST, violatingPKs);
	}
}
