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
package de.hybris.platform.jdbcwrapper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.util.Utilities;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.junit.Test;


/**
 * Tests apache commpons pool bug POOL-107 ...
 */
@PerformanceTest
public class GenericObjectPoolTest
{
	@Test
	public void testT50Min0Max10NoOutage()
	{
		runTest(50, 0, 10, 60, 0, 0, 0, 500);
	}

	@Test
	public void testT50Min0Max10NoOutageClose10SecBeforeEnd()
	{
		runTest(50, 0, 10, 60, 0, 0, 10, 500);
	}

	@Test
	public void testT50Min5Max10NoOutage()
	{
		runTest(50, 5, 10, 60, 0, 0, 0, 500);
	}

	@Test
	public void testT50Min0Max10Outage20Sec()
	{
		runTest(50, 0, 10, 60, 20, 5, 0, 500);
	}

	@Test
	public void testT50Min5Max10Outage20Sec()
	{
		runTest(50, 5, 10, 60, 20, 5, 0, 500);
	}

	private void runTest(final int threads, final int minIdle, final int max, final int durationSeconds,
			final int dbOutageIntervalSeconds, final int dbOutageDurationSeconds, final int closeSecondsBeforeEnd,
			final long holdTimeMs)
	{
		System.out.println("run test with " + threads + " threads, duration:" + durationSeconds + " s, minIdle:" + minIdle
				+ ", maxActive:" + max + ", dbOutageInt:" + dbOutageIntervalSeconds + ", dbOutageDuration:" + dbOutageDurationSeconds
				+ ", holdTimeAvg:" + holdTimeMs + " ms");

		final DummyPool pool = createPool(minIdle, max);

		final TestThreadsHolder<PoolAccessor> workers = new TestThreadsHolder<PoolAccessor>(threads, false)
		{
			@Override
			public PoolAccessor newRunner(final int threadNumber)
			{
				return new PoolAccessor(threadNumber, pool, holdTimeMs);
			}
		};

		workers.startAll();

		final long startMs = System.currentTimeMillis();
		final long endMs = startMs + (durationSeconds * 1000);
		final long closeBeforeEndMs = closeSecondsBeforeEnd > 0 ? endMs - (closeSecondsBeforeEnd * 1000) : -1;
		do
		{
			if (dbOutageDurationSeconds > 0)
			{
				if (!simulateOutage(pool, dbOutageIntervalSeconds, dbOutageDurationSeconds))
				{
					break;
				}
			}
			else if (closeBeforeEndMs > 0 && closeBeforeEndMs < System.currentTimeMillis())
			{
				try
				{
					pool.close();
				}
				catch (final Exception e)
				{
					System.err.println("error closing pool before workers end: " + e.getMessage());
					break;
				}
			}
			else
			{
				if (!sleepNoOutage(pool))
				{
					break;
				}
			}
		}
		while (endMs > System.currentTimeMillis());

		assertTrue("not all workers finished in time", workers.stopAndDestroy(durationSeconds / 2));
		assertEquals(Collections.EMPTY_MAP, workers.getErrors());

		try
		{
			pool.close();
		}
		catch (final Exception e)
		{
			fail("error closing pool : " + e.getMessage());
		}

		// wait for unused connections to be evicted
		final long t = 2 * pool.getMinEvictableIdleTimeMillis();
		final long maxWaitForEviction = System.currentTimeMillis() + t;
		System.out.println("Waiting " + t + "ms for pool to evict unused connections");
		do
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e1)
			{
				Thread.currentThread().interrupt();
				break;
			}
		}
		while (maxWaitForEviction > System.currentTimeMillis());

		//		assertEquals(minIdle, pool.getFactory().alive.get());
		//		assertEquals(0, pool.getFactory().active.get());


		assertEquals(0, pool.getFactory().alive.get());
		assertEquals(0, pool.getFactory().active.get());
	}

	private boolean simulateOutage(final DummyPool pool, final int dbOutageIntervalSeconds, final int dbOutageDurationSeconds)
	{
		final ConnectionFactory factory = pool.getFactory();
		try
		{
			// wait until outage should occur
			final long stopTime1 = System.currentTimeMillis() + (dbOutageIntervalSeconds * 1000);
			do
			{
				Thread.sleep(1000);
				printStatusInfo(factory);
			}
			while (stopTime1 > System.currentTimeMillis());
			System.out.println("db outage starts");
			pool.getFactory().failOnMake = true;
			pool.getFactory().failOnValidate = true;

			// busy waiting for end of outage
			final long stopTime2 = System.currentTimeMillis() + (dbOutageDurationSeconds * 1000);
			do
			{
				Thread.sleep(1000);
				printStatusInfo(factory);
			}
			while (stopTime2 > System.currentTimeMillis());
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
			return false;
		}
		finally
		{
			factory.failOnValidate = false;
			factory.failOnMake = false;
			System.out.println("db outage finished");
		}
		return true;
	}

	private void printStatusInfo(final ConnectionFactory factory)
	{
		System.out.println("pool status(alive:" + factory.getAllAlive() + ", active:" + factory.getActive() + ", passive:"
				+ factory.getPassive() + ")");
	}

	private boolean sleepNoOutage(final DummyPool pool)
	{
		final ConnectionFactory factory = pool.getFactory();
		try
		{
			Thread.sleep(1000);
			printStatusInfo(factory);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
			return false;
		}
		return true;
	}

	static class PoolAccessor implements Runnable
	{
		final int workerNr;
		final GenericObjectPool<ConnectionDummy> pool;
		final long holdTimeMsAvg;

		PoolAccessor(final int nr, final GenericObjectPool<ConnectionDummy> pool, final long holdTimeMsAvg)
		{
			this.workerNr = nr;
			this.pool = pool;
			this.holdTimeMsAvg = holdTimeMsAvg;
		}

		@Override
		public String toString()
		{
			return "PoolAccessor " + workerNr;
		}

		@Override
		public void run()
		{
			final Thread t = Thread.currentThread();
			final Random rnd = new Random(this.hashCode());
			do
			{
				ConnectionDummy con = null;
				try
				{
					con = pool.borrowObject();

					payload(con, rnd);
				}
				catch (final FailOnMakeException e)
				{
					// expected that
				}
				catch (final NoSuchElementException e)
				{
					// to be expected as well
				}
				catch (final IllegalStateException e)
				{
					// this happens if the pool is closed already - we may finish now
					break;
				}
				catch (final InterruptedException e)
				{
					// that's fine here
					t.interrupt();
				}
				catch (final Exception e)
				{
					System.err.println("error borrowing: " + e.getMessage() + "\n" + Utilities.getStackTraceAsString(e));
				}
				finally
				{
					if (con != null)
					{
						con.close();
					}
				}
			}
			while (!t.isInterrupted());
		}

		int payload(final ConnectionDummy con, final Random rnd)
		{
			if (con != null)
			{
				if (holdTimeMsAvg > 0)
				{
					try
					{
						Thread.sleep((long) (rnd.nextDouble() * holdTimeMsAvg));
					}
					catch (final InterruptedException e)
					{
						Thread.currentThread().interrupt();
					}
				}
				return con.hashCode();
			}
			return 0;
		}
	}

	static class ConnectionDummy
	{
		final long id;
		volatile GenericObjectPool<ConnectionDummy> pool;
		volatile boolean active = false;

		ConnectionDummy(final long id)
		{
			this.id = id;
		}

		void setPool(final GenericObjectPool<ConnectionDummy> pool)
		{
			this.pool = pool;
		}

		void close()
		{
			try
			{
				pool.returnObject(this);
			}
			catch (final Exception e)
			{
				System.err.println("error returning " + this + ": " + e.getMessage());
			}
		}

		boolean activate()
		{
			if (!active)
			{
				active = true;
				return true;
			}
			return false;
		}

		boolean passivate()
		{
			if (active)
			{
				active = false;
				return true;
			}
			return false;
		}
	}

	static class ConnectionFactory implements PoolableObjectFactory<ConnectionDummy>
	{
		volatile boolean failOnMake = false;
		volatile boolean failOnValidate = false;

		final AtomicInteger alive = new AtomicInteger();
		final AtomicInteger active = new AtomicInteger();

		ConnectionFactory()
		{
			//
		}

		long nextId()
		{
			return System.nanoTime();
		}

		int getActive()
		{
			return active.get();
		}

		int getAllAlive()
		{
			return alive.get();
		}

		int getPassive()
		{
			final int all = getAllAlive();
			final int active = getActive();
			return all - active;
		}

		@Override
		public ConnectionDummy makeObject() throws Exception
		{
			if (failOnMake)
			{
				throw new FailOnMakeException();
			}
			alive.incrementAndGet();
			return new ConnectionDummy(nextId());
		}

		@Override
		public void destroyObject(final ConnectionDummy obj) throws Exception
		{
			if (obj != null)
			{
				alive.decrementAndGet();
				if (obj.active)
				{
					active.decrementAndGet();
				}
			}
		}

		@Override
		public boolean validateObject(final ConnectionDummy obj)
		{
			return !failOnValidate;
		}

		@Override
		public void activateObject(final ConnectionDummy obj) throws Exception
		{
			if (obj.activate())
			{
				active.incrementAndGet();
			}
			else
			{
				System.err.println("activate(" + obj + ") on active object!");
			}
		}

		@Override
		public void passivateObject(final ConnectionDummy obj) throws Exception
		{
			if (obj.passivate())
			{
				active.decrementAndGet();
			}
			//			else
			//			{
			//				System.err.println("passivate(" + obj + ") on non-active object!");
			//			}
		}
	}

	static class FailOnMakeException extends RuntimeException
	{
		//
	}

	static class DummyPool extends GenericObjectPool<ConnectionDummy>
	{
		final ConnectionFactory factory;

		DummyPool(final ConnectionFactory factory, final GenericObjectPool.Config poolCfg)
		{
			super(factory, poolCfg);
			this.factory = factory;
		}

		ConnectionFactory getFactory()
		{
			return factory;
		}

		@Override
		public ConnectionDummy borrowObject() throws Exception
		{
			final ConnectionDummy ret = super.borrowObject();
			ret.setPool(this);
			return ret;
		}
	}

	DummyPool createPool(final int minIdle, final int max)
	{
		final GenericObjectPool.Config poolCfg = new Config();
		poolCfg.maxActive = max;
		poolCfg.maxIdle = max;
		poolCfg.minIdle = minIdle;

		poolCfg.timeBetweenEvictionRunsMillis = 1 * 1000;
		poolCfg.minEvictableIdleTimeMillis = 5 * 1000;
		poolCfg.testWhileIdle = true;
		poolCfg.numTestsPerEvictionRun = 1000;

		poolCfg.testOnBorrow = true;
		poolCfg.testOnReturn = true;

		poolCfg.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		poolCfg.maxWait = 2 * 1000;

		return new DummyPool(new ConnectionFactory(), poolCfg);
	}


}
