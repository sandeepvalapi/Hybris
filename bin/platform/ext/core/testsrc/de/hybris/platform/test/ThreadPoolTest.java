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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.tx.DefaultTransaction;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.threadpool.PoolableThread;
import de.hybris.platform.util.threadpool.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ThreadPoolTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(ThreadPoolTest.class.getName());

	private static final int MAX_THREADS = 10;

	private final CyclicBarrier transacationStartingBarrier = new CyclicBarrier(MAX_THREADS + 1);

	private final CountDownLatch finishedStaleTransactionLatch = new CountDownLatch(MAX_THREADS);

	@Before
	public void disableFileAnalyzer()
	{
		TestUtils.disableFileAnalyzer(200);
	}

	@After
	public void restoreFileAnalyzer()
	{
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void testTransactionCleanUpSimple() throws InterruptedException, BrokenBarrierException, TimeoutException
	{
		final boolean flagBefore = Config.getBoolean("transaction.monitor.begin", false);
		Config.setParameter("transaction.monitor.begin", "true");
		ThreadPool pool = null;
		try
		{
			pool = new ThreadPool(Registry.getCurrentTenantNoFallback().getTenantID(), 1);

			final GenericObjectPool.Config config = new GenericObjectPool.Config();
			config.maxActive = 1;
			config.maxIdle = 1;
			config.maxWait = -1;
			config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
			config.testOnBorrow = true;
			config.testOnReturn = true;
			config.timeBetweenEvictionRunsMillis = 30 * 1000; // keep idle threads for at most 30 sec
			pool.setConfig(config);

			final CyclicBarrier gate = new CyclicBarrier(2);
			final AtomicReference<Throwable> threadError = new AtomicReference<Throwable>();
			final AtomicReference<RecordingTransaction> threadTransaction = new AtomicReference<ThreadPoolTest.RecordingTransaction>();
			final PoolableThread thread1 = pool.borrowThread();
			thread1.execute(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						final Transaction tx = new RecordingTransaction();
						tx.activateAsCurrentTransaction();
						tx.begin();
						tx.begin(); // second time
						tx.begin(); // third time
						assertTrue(tx.isRunning());
						assertEquals(3, tx.getOpenTransactionCount());
						threadTransaction.set((RecordingTransaction) tx);
						gate.await(10, TimeUnit.SECONDS);
					}
					catch (final Throwable t)
					{
						threadError.set(t);
					}
				}
			});
			gate.await(10, TimeUnit.SECONDS);
			assertNull(threadError.get());
			// busy waiting for correct number of rollbacks - at the moment there is no hook for a better solution
			final RecordingTransaction tx = threadTransaction.get();
			final long maxWait = System.currentTimeMillis() + (15 * 1000);
			while (tx.rollbackCounter.get() < 3 && System.currentTimeMillis() < maxWait)
			{
				Thread.sleep(100);
			}
			assertEquals(3, tx.rollbackCounter.get());

			final PoolableThread thread2 = pool.borrowThread();
			assertNotSame(thread1, thread2);
		}
		finally
		{
			if (pool != null)
			{
				try
				{
					pool.close();
				}
				catch (final Exception e)
				{
					// can't help it
				}
			}
			Config.setParameter("transaction.monitor.begin", BooleanUtils.toStringTrueFalse(flagBefore));
		}
	}


	static class RecordingTransaction extends DefaultTransaction
	{
		final AtomicInteger rollbackCounter;
		final AtomicInteger commitCounter;

		RecordingTransaction()
		{
			this.rollbackCounter = new AtomicInteger();
			this.commitCounter = new AtomicInteger();
		}

		@Override
		public void commit() throws TransactionException
		{
			try
			{
				super.commit();
			}
			finally
			{
				commitCounter.incrementAndGet();
			}
		}

		@Override
		public void rollback() throws TransactionException
		{
			try
			{
				super.rollback();
			}
			finally
			{
				rollbackCounter.incrementAndGet();
			}
		}
	}



	/**
	 * CORE-66PLA-10816 Potential chance to fetch a PoolableThread with pending transaction from previous run
	 * 
	 * together with setting logger level for a log4j.logger.de.hybris.platform.util.threadpool=DEBUG prints out
	 * information who/where started the stale transaction
	 */
	@Test
	public void testTransactionCleanUp() throws Exception
	{
		final Queue<Transaction> recordedTransactions = new ConcurrentLinkedQueue<Transaction>();

		final boolean flagBefore = Config.getBoolean("transaction.monitor.begin", false);
		Config.setParameter("transaction.monitor.begin", "true");
		ThreadPool pool = null;

		try
		{
			// create own pool since we don't want to mess up the system
			pool = new ThreadPool(Registry.getCurrentTenantNoFallback().getTenantID(), MAX_THREADS);

			final GenericObjectPool.Config config = new GenericObjectPool.Config();
			config.maxActive = MAX_THREADS;
			config.maxIdle = 1;
			config.maxWait = -1;
			config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
			config.testOnBorrow = true;
			config.testOnReturn = true;
			config.timeBetweenEvictionRunsMillis = 30 * 1000; // keep idle threads for at most 30 sec
			pool.setConfig(config);

			final int maxSize = pool.getMaxActive();
			final int activeBefore = pool.getNumActive();
			final List<NoClosingTransactionProcess> started = new ArrayList<NoClosingTransactionProcess>(maxSize);
			for (int i = activeBefore; i < maxSize; i++)
			{
				final PoolableThread poolableThread = pool.borrowThread();
				final NoClosingTransactionProcess noClosingTransactionProcess = new NoClosingTransactionProcess();
				started.add(noClosingTransactionProcess);
				poolableThread.execute(noClosingTransactionProcess);
			}
			Thread.sleep(1000);

			transacationStartingBarrier.await(); //await for all transacations to start

			//record all started  transactions
			for (final NoClosingTransactionProcess singleStarted : started)
			{
				recordedTransactions.add(singleStarted.getStartedTransaction());
			}

			finishedStaleTransactionLatch.await(180, TimeUnit.SECONDS);
			Thread.sleep(1000);//give them 1 second to finish

			final List<HasNoCurrentRunningTransactionProcess> ranAfter = new ArrayList<HasNoCurrentRunningTransactionProcess>(
					maxSize);
			Transaction recordedTransaction = recordedTransactions.poll();
			do
			{
				final PoolableThread poolableThread = pool.borrowThread();
				final HasNoCurrentRunningTransactionProcess hasNoCurrentRunningTransactionProcess = new HasNoCurrentRunningTransactionProcess(
						recordedTransaction);
				ranAfter.add(hasNoCurrentRunningTransactionProcess);
				poolableThread.execute(hasNoCurrentRunningTransactionProcess);
				recordedTransaction = recordedTransactions.poll();
			}
			while (recordedTransaction != null);
			//still can borrow
			Assert.assertNotNull(pool.borrowThread());
			Thread.sleep(1000);

			//verify if really Thread had a non started transaction on the enter
			for (final HasNoCurrentRunningTransactionProcess singleRanAfter : ranAfter)
			{
				if (singleRanAfter.getException() != null)
				{
					singleRanAfter.getException().printException();
					Assert.fail("Some of the thread(s) captured not finshied transaction in the pool ");
				}
			}
		}
		finally
		{
			if (pool != null)
			{
				try
				{
					pool.close();
				}
				catch (final Exception e)
				{
					// can't help it
				}
			}
			Config.setParameter("transaction.monitor.begin", BooleanUtils.toStringTrueFalse(flagBefore));

		}
	}

	/**
	 * ThreadPool's runnable which does not care about closing transaction.
	 */
	private class NoClosingTransactionProcess implements Runnable
	{

		private Transaction startedTransaction;

		@Override
		public void run()
		{
			try
			{
				startedTransaction = Transaction.current();
				startedTransaction.begin();
				LOG.info("Thread status (" + Thread.currentThread().isInterrupted() + ") ... ");
				transacationStartingBarrier.await(180, TimeUnit.SECONDS);
			}

			catch (final InterruptedException e)
			{
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
			catch (final BrokenBarrierException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (final TimeoutException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				finishedStaleTransactionLatch.countDown();
				LOG.info("finshed  transaction " + finishedStaleTransactionLatch.getCount());
				//note no commit or rollback
			}
		}


		public Transaction getStartedTransaction()
		{
			return startedTransaction;
		}
	}

	/**
	 * ThreadPool's runnable which is in the position to get the transaction which was not rolled-back, comitted by
	 * predecessing.
	 */
	private static class HasNoCurrentRunningTransactionProcess implements Runnable
	{
		static private AtomicInteger index = new AtomicInteger();

		final private Transaction recordedTransaction;

		private TransactionStateException exception;

		public HasNoCurrentRunningTransactionProcess(final Transaction transaction)
		{
			recordedTransaction = transaction;
		}

		@Override
		public void run()
		{
			try
			{
				LOG.info("Starting has no current transaction process (" + index.incrementAndGet() + ") ... ");
				final Transaction current = recordedTransaction;
				if (current == null)
				{
					throw new TransactionStateException("Recorded transaction should be not null ");
				}
				if (current.isRunning())
				{
					throw new TransactionStateException("There could not be any running transaction for any PoolableThread ",
							current.getBeginTransactionStack());
				}

			}
			catch (final TransactionStateException e)
			{
				exception = e; //store it
			}
			finally
			{
				//note no commit or rollback
			}
		}

		/**
		 * @return the exception
		 */
		public TransactionStateException getException()
		{
			return exception;
		}


	}

	static class TransactionStateException extends Exception
	{
		private Queue<Throwable> stack;

		/**
		 *
		 */
		public TransactionStateException(final String msg)
		{
			super(msg);
		}

		public TransactionStateException(final String msg, final Queue<Throwable> stack)
		{
			super(msg);
			this.stack = stack;
		}

		public void printException()
		{
			Throwable transactionStackElement = stack.poll();
			while (transactionStackElement != null)
			{
				transactionStackElement.printStackTrace();
				transactionStackElement = stack.poll();
			}
		}
	}

	/**
	 * See PLA-7607.
	 */
	@Test
	public void testBlockBehaviour() throws Exception
	{
		ThreadPool pool = null;
		try
		{
			// create own pool since we don't want to mess up the system
			pool = AbstractTenant.createDefaultThreadPool(Registry.getCurrentTenant().getTenantID(), 10);

			final int maxSize = pool.getMaxActive();
			final int activeBefore = pool.getNumActive();

			final List<BlockingProcess> processes = new ArrayList<BlockingProcess>();

			for (int i = activeBefore; i < maxSize; i++)
			{
				assertTrue("pool exhausted before", pool.getMaxActive() > pool.getNumActive());

				final PoolableThread poolableThread = pool.borrowThread();
				assertEquals(i + 1, pool.getNumActive());

				final BlockingProcess blockingProcess = new BlockingProcess();
				processes.add(blockingProcess);
				poolableThread.execute(blockingProcess);
			}

			assertEquals(pool.getMaxActive(), pool.getNumActive());

			// now try to get another thread -> blocking

			final PoolableThread[] newOne = new PoolableThread[]
			{ null };
			final boolean[] done = new boolean[]
			{ false };

			final ThreadPool pool2 = pool;

			new RegistrableThread()
			{
				@Override
				public void internalRun()
				{
					newOne[0] = pool2.borrowThread();
					done[0] = true;
				}
			}.start();
			// wait
			Thread.sleep(1000);
			// check if borrowing is still stuck
			assertNull(newOne[0]);
			assertFalse(done[0]);

			// finish one of the previous

			for (final BlockingProcess p : processes)
			{
				p.wait = false;
			}

			Thread.sleep(1000);

			for (final BlockingProcess p : processes)
			{
				assertNull(p.error);
			}

			// check if borrowing succeeded by now
			assertNotNull(newOne[0]);
			assertTrue(done[0]);

			pool.returnThread(newOne[0]);

			assertEquals(0, pool.getNumActive());
		}
		finally
		{
			if (pool != null)
			{
				try
				{
					pool.close();
				}
				catch (final Exception e)
				{
					// can't help it
				}
			}
		}
	}

	private static class BlockingProcess implements Runnable
	{
		boolean wait = true;
		Throwable error = null;

		@Override
		public void run()
		{
			try
			{
				while (this.wait)
				{
					Thread.sleep(100);
				}
			}
			catch (final Throwable t)
			{
				error = t;
			}
		}

	}

}
