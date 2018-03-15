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
package de.hybris.platform.test.tx;

import static org.junit.Assert.assertTrue;

import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Performance test for Transaction.current()
 */
@Ignore
public class TransactionGetCurrentPerfTest extends HybrisJUnit4Test
{
	ThreadLocal txThreadLocalToClear;

	@Before
	public void prepare() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		final Field currentTransactionField = Transaction.class.getDeclaredField("currentTransaction");
		currentTransactionField.setAccessible(true);
		txThreadLocalToClear = (ThreadLocal) currentTransactionField.get(null);
	}

	@Test
	public void testSingleThread()
	{
		final int CYCLES = 1000 * 10000;

		int hash = 0;

		final long time1 = System.currentTimeMillis();

		for (int i = 0; i < CYCLES; i++)
		{
			hash ^= doGetCurrent().hashCode();
			unsetCurrent();
		}

		final long time2 = System.currentTimeMillis();

		System.out.println("Transaction.current() invoked: " + CYCLES + ", times took: " + (time2 - time1) + " ms , hash: " + hash);
	}

	public void testMultiThreaded() throws Exception
	{
		final int THREADS = 1;
		final int CYCLES = 1000 * 10000;

		final long time1 = System.currentTimeMillis();
		doNonTransactionalConcurrentCreation(50, CYCLES, 2);
		final long time2 = System.currentTimeMillis();

		System.out.println("Threads: " + THREADS + ", Transaction.current() invoked: " + CYCLES + ", times took: " + (time2 - time1)
				+ " ms");
	}

	Transaction doGetCurrent()
	{
		return Transaction.current();
	}

	void unsetCurrent()
	{
		txThreadLocalToClear.remove();
	}

	private void doNonTransactionalConcurrentCreation(final int maxThreads, final int rounds, final int waitingMinutes)
			throws Exception
	{
		final ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
		for (int i = 1; i <= rounds; i++)
		{
			executor.execute(new Runnable()
			{
				@Override
				public void run()
				{
					doGetCurrent().hashCode();
					unsetCurrent();
				}
			});
		}

		executor.shutdown();
		assertTrue(executor.awaitTermination(waitingMinutes, TimeUnit.MINUTES));
	}

}
