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

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.DefaultTransaction;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;


/**
 * Tests HJMP persistence layer behavior concerning concurrent modification checks.
 * 
 * There is a config parameter hjmp.throw.concurrent.modification.exceptions which either enables or disables checking
 * that upon update a item row must have the same version that has been read when fetching the HJMP entity object.
 * 
 * <b>We discovered in PLA-10894 that switching off this check actually every conflicting update did not happen at all
 * instead of both updates happening one after the other!</b>
 */
@PerformanceTest
public class HJMPOptimisticConcurrencyPerformanceTest extends HybrisJUnit4Test
{
	private static String CFG_KEY = "hjmp.throw.concurrent.modification.exceptions";

	@Test
	public void testMissingUpdateProblemCheckEnabledStressTest() throws JaloBusinessException, InterruptedException
	{
		final long endTime = System.currentTimeMillis() + (30 * 1000);
		do
		{
			Boolean previous = null;
			try
			{
				previous = setConcurrentModificationCheckEnabled(Boolean.TRUE);
				doTestMissingUpdateProblem();
			}
			finally
			{
				setConcurrentModificationCheckEnabled(previous);
			}
		}
		while (System.currentTimeMillis() < endTime);
	}

	@Test
	// PLA-10894
	public void testMissingUpdateProblemCheckDisabledStressTest() throws JaloBusinessException, InterruptedException
	{
		final long endTime = System.currentTimeMillis() + (30 * 1000);
		do
		{
			Boolean previous = null;
			try
			{
				previous = setConcurrentModificationCheckEnabled(Boolean.FALSE);
				doTestMissingUpdateProblem();
			}
			finally
			{
				setConcurrentModificationCheckEnabled(previous);
			}
		}
		while (System.currentTimeMillis() < endTime);
	}

	private void doTestMissingUpdateProblem() throws JaloBusinessException, InterruptedException
	{
		final Media m = MediaManager.getInstance().createMedia("codeBefore");
		try
		{
			m.setMime("mimeBefore");
			m.setDescription("descriptionBefore");

			final CyclicBarrier txStartJoinPoint = new CyclicBarrier(2);
			final AtomicReference<Throwable> tx1Error = new AtomicReference<Throwable>();
			final AtomicReference<Throwable> tx2Error = new AtomicReference<Throwable>();

			final Thread[] threads = createTxThreads(//
					new TxRunnable()
					{
						@Override
						public void run() throws Exception
						{
							txStartJoinPoint.await(10, TimeUnit.SECONDS); // wait for other tx to begin
							assertEquals("codeBefore", m.getCode());
							txStartJoinPoint.await(10, TimeUnit.SECONDS); // wait for other tx to have read same version
							m.setMime("tx1Mime");
							m.setCode("tx1Code");
						}
					}, //
					new TxRunnable()
					{
						@Override
						public void run() throws Exception
						{
							txStartJoinPoint.await(10, TimeUnit.SECONDS); // wait for other tx
							assertEquals("codeBefore", m.getCode());
							txStartJoinPoint.await(10, TimeUnit.SECONDS); // wait for other tx to have read same version
							m.setMime("tx2Mime");
							m.setDescription("tx2Description");
						}
					}, tx1Error, tx2Error);

			threads[0].start();
			threads[1].start();

			threads[0].join(50 * 1000);
			threads[1].join(50 * 1000);

			assertFalse(threads[0].isAlive());
			assertFalse(threads[1].isAlive());

			if (isConcurrentModificationCheckEnabled())
			{
				assertEquals("missing update from tx1", "tx1Code", m.getCode());
				assertEquals("missing update from tx1", "tx1Mime", m.getMime());
				assertEquals("unexpected update from tx2", "descriptionBefore", m.getDescription());
				assertNull("unexpected error from tx1", tx1Error.get());
				assertNotNull("expected error from tx2", tx2Error.get());
			}
			else
			{
				assertNull("unexpected error from tx1", tx1Error.get());
				assertNull("unexpected error from tx2", tx2Error.get());
				assertEquals("missing update from tx1", "tx1Code", m.getCode());
				assertEquals("missing update from tx2", "tx2Description", m.getDescription());
				assertEquals("missing update from tx2", "tx2Mime", m.getMime());
			}
		}
		finally
		{
			m.remove();
		}
	}

	private interface TxRunnable
	{
		void run() throws Exception;
	}

	private Thread[] createTxThreads(final TxRunnable tx1Runnable, final TxRunnable tx2Runnable,
			final AtomicReference<Throwable> tx1Error, final AtomicReference<Throwable> tx2Error)
	{
		final CountDownLatch afterTx1Write = new CountDownLatch(1);
		final Tenant t = Registry.getCurrentTenantNoFallback();

		final Thread tx1Thread = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				Registry.setCurrentTenant(t);
				try
				{
					// this tx flush its changes and signals write via CountDownLatch
					final Transaction tx = new TestTransaction(afterTx1Write, false);
					tx.activateAsCurrentTransaction();
					tx.execute(new TransactionBody()
					{
						@Override
						public Object execute() throws Exception
						{
							prepareTransaction(tx);
							tx1Runnable.run();
							return null;
						}
					});
				}
				catch (final Throwable e)
				{
					tx1Error.set(e);
				}
			}
		};
		final Thread tx2Thread = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				Registry.setCurrentTenant(t);
				try
				{
					// this tx does wait for other tx to flush its changes to ensure conflict here
					final Transaction tx = new TestTransaction(afterTx1Write, true);
					tx.activateAsCurrentTransaction();
					tx.execute(new TransactionBody()
					{
						@Override
						public Object execute() throws Exception
						{
							prepareTransaction(tx);
							tx2Runnable.run();
							return null;
						}
					});
				}
				catch (final Exception e)
				{
					tx2Error.set(e);
				}
			}
		};
		return new Thread[]
		{ tx1Thread, tx2Thread };
	}

	private void prepareTransaction(final Transaction tx)
	{
		tx.setTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
		tx.enableDelayedStore(true);
	}

	private boolean isConcurrentModificationCheckEnabled()
	{
		return Config.getBoolean("hjmp.throw.concurrent.modification.exceptions", true);
	}

	private Boolean setConcurrentModificationCheckEnabled(final Boolean enabled)
	{
		final String previous = Config.getParameter(CFG_KEY);
		Config.setParameter(CFG_KEY, enabled != null ? enabled.toString() : null);

		return StringUtils.isEmpty(previous) ? null : Boolean.valueOf(previous);
	}

	static class TestTransaction extends DefaultTransaction
	{
		final CountDownLatch gate;
		final boolean waitBeforeFlush;

		TestTransaction(final CountDownLatch gate, final boolean waitBeforeFlush)
		{
			super();
			this.gate = gate;
			this.waitBeforeFlush = waitBeforeFlush;
		}

		@Override
		public void flushDelayedStore()
		{
			if (waitBeforeFlush)
			{
				try
				{
					assertTrue(gate.await(10, TimeUnit.SECONDS));
				}
				catch (final InterruptedException e)
				{
					e.printStackTrace();
				}
				super.flushDelayedStore();
			}
			else
			{
				super.flushDelayedStore();
				gate.countDown();
			}
		}
	}
}
