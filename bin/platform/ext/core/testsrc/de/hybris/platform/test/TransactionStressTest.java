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

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.tx.TransactionException;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;



@PerformanceTest
public class TransactionStressTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(TransactionStressTest.class);

	@After
	public void tearDown()
	{
		// make sure no (nested) transaction remains even if a
		// single test aborts too early due to failure
		while (Transaction.current().isRunning())
		{
			try
			{
				LOG.warn("test " + getClass().getSimpleName() + " found open transaction - trying rollback ...");
				Transaction.current().rollback();
			}
			catch (final Exception e)
			{
				// can't help it
				break;
			}
		}
	}

	@Test
	public void testInvalidationCostInsideTransaction() throws Exception
	{
		final List<Title> sampleItems = createSampleTitles(10000);
		final int[] ranges = new int[]
		{ 10, 100, 1000, 10000 };
		final List<Long> updateTimes = new ArrayList<>(ranges.length);
		final List<Long> readTimes = new ArrayList<>(ranges.length);

		final Transaction tx = Transaction.current();
		tx.execute(new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				// make sure EVERY update is causing a invalidation
				tx.enableDelayedStore(false);

				final Title[] toModify = new Title[10];
				int index = 0;
				for (final int range : ranges)
				{
					int toModifyIdx = 0;
					// load item range
					for (; index < range; index++)
					{
						// load & attach item to tx
						final Title t = jaloSession.getItem(sampleItems.get(index).getPK());
						// make cache map loaded as well
						t.getCode();
						if (toModifyIdx < toModify.length)
						{
							toModify[toModifyIdx] = t;
							toModifyIdx++;
						}
					}
					// perform some modifications inside NESTED tx -> flush changes to DB at the end !!!
					long begin = System.currentTimeMillis();
					tx.execute(new TransactionBody()
					{

						@Override
						public Object execute() throws Exception
						{
							for (final Title t : toModify)
							{
								// 5 x update code -> should invalidate EACH time since delayed store is disabled
								for (int i = 0; i < 5; i++)
								{
									t.setCode(t.getCode() + "x");
								}
							}
							return null;
						}
					});
					long end = System.currentTimeMillis();
					updateTimes.add(Long.valueOf(end - begin));

					begin = System.currentTimeMillis();
					int dummy = 0;
					for (final Title t : toModify)
					{
						for (int i = 0; i < 5; i++)
						{
							dummy = dummy + (dummy ^ t.getCode().hashCode());
						}
					}
					if (dummy == 0)
					{
						LOG.info("dummy:" + dummy);
					}

					end = System.currentTimeMillis();
					readTimes.add(Long.valueOf(end - begin));

					tx.printContextInfo();
				}
				return null;
			}
		});

		int i = 0;
		for (final int r : ranges)
		{
			final Long updateTime = updateTimes.get(i);
			final Long readTime = readTimes.get(i);
			LOG.info("tx-bound times after adding " + r + " items was update:" + updateTime + " ms and read:" + readTime);
			i++;
		}
	}

	private List<Title> createSampleTitles(final int amount) throws ConsistencyCheckException
	{
		LOG.info("creating " + amount + " sample items..");
		final UserManager um = UserManager.getInstance();
		final Title[] items = new Title[amount];
		for (int i = 0; i < amount; i++)
		{
			items[i] = um.createTitle("TxTestTitle-" + i);
		}
		LOG.info("done.");
		return Arrays.asList(items);
	}

	// Performance test for getting numbers for PLA-12089 fix ...
	@Test
	public void testBulkUpdatePerformance() throws Exception
	{
		long milliSeconds = testBulkUpdatePerformance(10, 10, false);
		System.out.println("updating 10 items in 10 transactions took " + milliSeconds + "ms");

		milliSeconds = testBulkUpdatePerformance(10, 10, true);
		System.out.println("updating 10 items in 10 transactions (reverse order) took " + milliSeconds + "ms");

		milliSeconds = testBulkUpdatePerformance(10, 1000, false);
		System.out.println("updating 1000 items in 10 transactions took " + milliSeconds + "ms");

		milliSeconds = testBulkUpdatePerformance(10, 1000, true);
		System.out.println("updating 1000 items in 10 transactions (reverse order) took " + milliSeconds + "ms");
	}

	private long testBulkUpdatePerformance(final int CYCLES, final int COUNT, final boolean reverseOrder) throws Exception
	{
		final List<Item> toUpdate = new ArrayList<Item>(COUNT);
		final UserManager userManager = UserManager.getInstance();
		for (int i = 0; i < COUNT; i++)
		{
			final String code = "UpdateTest_" + i;
			toUpdate.add(userManager.createTitle(code));
		}
		if (reverseOrder)
		{
			Collections.reverse(toUpdate);
		}

		final long time1 = System.currentTimeMillis();
		for (int i = 0; i < CYCLES; i++)
		{
			Transaction.current().execute(new TransactionBody()
			{

				@Override
				public Object execute() throws Exception
				{
					for (final Item i : toUpdate)
					{
						i.setModificationTime(new Date((long) (Math.random() * Long.MAX_VALUE)));
					}
					return null;
				}
			});
		}
		final long time2 = System.currentTimeMillis();

		for (final Item i : toUpdate)
		{
			i.remove();
		}

		return time2 - time1;
	}

	public void testCacheInvalidationWithUserTransaction() throws Exception
	{
		Country country = null;
		assertNotNull(country = jaloSession.getC2LManager().createCountry("code1"));
		assertEquals("code1", country.getIsoCode());

		boolean done = false;
		try
		{
			Transaction.current().begin();
			assertEquals("code1", country.getIsoCode());
			country.setIsoCode("code2");
			assertEquals("code2", country.getIsoCode());
			Transaction.current().commit();
			done = true;
			assertEquals("code2", country.getIsoCode());
		}
		finally
		{
			if (!done)
			{
				Transaction.current().rollback();
			}
		}
		done = false;
		try
		{
			Transaction.current().begin();
			assertEquals("code2", country.getIsoCode());
			country.setIsoCode("code3");
			LOG.debug("setting code");
			assertEquals("code3", country.getIsoCode());
			LOG.debug("rolling back");
			Transaction.current().rollback();
			done = true;
			LOG.debug("getting code");
			assertEquals("code2", country.getIsoCode());
		}
		finally
		{
			if (!done)
			{
				Transaction.current().rollback();
			}
		}
	}



	@Test
	public void testIsolationStressTest() throws ConsistencyCheckException
	{
		if (Config.itemCacheIsolationActivated())
		{
			final long end = System.currentTimeMillis() + (30 * 1000);
			int counter = 0;
			do
			{
				testIsolationInternal((counter % 2) == 0);
				counter++;
			}
			while (System.currentTimeMillis() < end);
		}
		else
		{
			LOG.warn("jalo item isolation is currently disabled - skipped test");
		}
	}

	private void testIsolationInternal(final boolean rollbackChanges) throws ConsistencyCheckException
	{
		final Country country = jaloSession.getC2LManager().createCountry("code1");
		try
		{
			assertEquals("code1", country.getIsoCode());

			final RunnerCreator<IsolationTestRunnable> creator = new RunnerCreator<TransactionStressTest.IsolationTestRunnable>()
			{
				final CyclicBarrier txSync = new CyclicBarrier(2);
				final CountDownLatch waitForWrite = new CountDownLatch(1);
				final CountDownLatch codeWritten = new CountDownLatch(1);

				@Override
				public IsolationTestRunnable newRunner(final int threadNumber)
				{
					return new IsolationTestRunnable(country, threadNumber == 0, rollbackChanges, waitForWrite, codeWritten, txSync);
				}
			};

			final TestThreadsHolder<IsolationTestRunnable> threads = new TestThreadsHolder<TransactionStressTest.IsolationTestRunnable>(
					2, creator);

			threads.startAll();

			assertTrue("not all threads have finished properly", threads.waitAndDestroy(30));

			assertEquals(Collections.EMPTY_MAP, threads.getErrors());
		}
		finally
		{
			if (country != null && country.isAlive())
			{
				try
				{
					country.remove();
				}
				catch (final Exception e)
				{
					// for several reasons this may fail without meaning harm so we ignore it here
				}
			}
		}

	}

	private static class IsolationTestRunnable implements Runnable
	{
		final Tenant tenant;
		final JaloSession jaloSession;
		final Country country;
		final boolean first;
		final boolean rollbackChanges;
		final CountDownLatch waitForWrite, codeWritten;
		final CyclicBarrier txSync;

		IsolationTestRunnable(final Country country, final boolean first, final boolean rollbackChanges,
				final CountDownLatch waitForWrite, final CountDownLatch codeWritten, final CyclicBarrier txSync)
		{
			this.country = country;
			this.first = first;
			this.rollbackChanges = rollbackChanges;
			this.waitForWrite = waitForWrite;
			this.codeWritten = codeWritten;
			this.txSync = txSync;

			this.tenant = Registry.getCurrentTenantNoFallback();
			this.jaloSession = JaloSession.getCurrentSession();
		}

		@Override
		public void run()
		{
			try
			{
				Registry.setCurrentTenant(tenant);
				jaloSession.activate();
				if (first)
				{
					runFirst();
				}
				else
				{
					runSecond();
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			finally
			{
				JaloSession.deactivate();
				Registry.unsetCurrentTenant();
			}
		}

		private void runFirst() throws Exception
		{
			assertEquals("code1", country.getIsoCode());

			final Transaction tx = Transaction.current();
			tx.execute(new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					txSync.await(10, TimeUnit.SECONDS); // let both threads join here

					assertEquals("code1", country.getIsoCode()); // first read inside transaction

					waitForWrite.countDown(); // let thread 2 change the code in his transaction
					assertTrue(codeWritten.await(10, TimeUnit.SECONDS)); // wait for other thread to actually perform writing

					assertEquals("code1", country.getIsoCode());

					txSync.await(10, TimeUnit.SECONDS); // let both threads join here

					return null;
				}
			});
		}

		private void runSecond() throws Exception
		{
			assertEquals("code1", country.getIsoCode());

			final Transaction tx = Transaction.current();
			try
			{
				tx.execute(new TransactionBody()
				{
					@Override
					public Object execute() throws Exception
					{
						txSync.await(10, TimeUnit.SECONDS); // let both threads join here

						assertEquals("code1", country.getIsoCode());

						assertTrue(waitForWrite.await(10, TimeUnit.SECONDS)); // wait for other thread to have read old value

						country.setIsoCode("t2Code"); // set new value

						assertEquals("t2Code", country.getIsoCode());

						codeWritten.countDown(); // signal that code has been written

						assertEquals("t2Code", country.getIsoCode());

						txSync.await(10, TimeUnit.SECONDS); // let both threads join here

						if (rollbackChanges)
						{
							tx.setRollbackOnly();
						}

						return null;
					}
				});
			}
			catch (final TransactionException e)
			{
				// this is allowed in case we are supposed to rollback
				assertTrue("unexpected tx exception " + e, rollbackChanges);
			}
			if (rollbackChanges)
			{
				assertEquals("code1", country.getIsoCode());
			}
			else
			{
				assertEquals("t2Code", country.getIsoCode());
			}
		}
	}



}
