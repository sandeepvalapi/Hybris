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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.DataSourceFactory;
import de.hybris.platform.core.DataSourceImplFactory;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Config.SystemSpecificParams;
import de.hybris.platform.util.config.ConfigIntf;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.support.JdbcUtils;


/**
 * Test return behavior in case the hybris data source and its contained pool has been closed/destroyed.
 */
@IntegrationTest
public class ConnectionPoolTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(ConnectionPoolTest.class);

	@Test
	@Ignore("HORST-121")
	public void testMultithreadedAccess() //NOPMD
	{
		final int RUN_SECONDS = Config.getInt("test.connectionpooltest.testmultithreadedaccess.duration", 10);
		final int THREADS = Config.getInt("test.connectionpooltest.testmultithreadedaccess.threads", 100);

		final boolean sendDummyStatement = Config.getBoolean("test.connectionpooltest.testmultithreadedaccess.dummystatement",
				false);

		doTestMultithreadedAccess(RUN_SECONDS, THREADS, 80, 0, true, sendDummyStatement);

		// 100% non-tx connections
		doTestMultithreadedAccess(RUN_SECONDS, THREADS, 100, 0, true, sendDummyStatement);

		// 50% non tx 50% tx connections - no rollbacks
		doTestMultithreadedAccess(RUN_SECONDS, THREADS, 50, 0, true, sendDummyStatement);

		// 80% non tx 20 % tx connections 1% rollbacks (this may break MySQL!)
		doTestMultithreadedAccess(RUN_SECONDS, THREADS, 80, 2, true, sendDummyStatement);
	}

	private void doTestMultithreadedAccess(final int RUN_SECONDS, final int THREADS, final int PERCENT_NO_TX,
			final int PERCENT_TX_ROLLBACK, final boolean useInterrupt, final boolean sendDummyStatement)
	{
		HybrisDataSource dataSource = null;

		LOG.info("--- test multithreaded access to connection pool duration:" + RUN_SECONDS + "s threads:" + THREADS + " nonTx:"
				+ PERCENT_NO_TX + "% rollback:" + PERCENT_TX_ROLLBACK + "% interrupt:" + useInterrupt
				+ "-----------------------------------");
		try
		{
			final Collection<TestConnectionImpl> allConnections = new ConcurrentLinkedQueue<TestConnectionImpl>();

			final AtomicLong rollbackCounter = new AtomicLong(0);
			final AtomicLong connectionCounter = new AtomicLong(0);
			final AtomicBoolean finished = new AtomicBoolean(false);

			dataSource = createDataSource(Registry.getCurrentTenantNoFallback(), allConnections, connectionCounter, false, false);

			assertEquals(0, dataSource.getNumInUse());
			assertEquals(1, dataSource.getNumPhysicalOpen());
			assertEquals(1, dataSource.getMaxInUse());
			assertEquals(1, dataSource.getMaxPhysicalOpen());

			final int maxConnections = dataSource.getMaxAllowedPhysicalOpen();

			final String runId = "[" + RUN_SECONDS + "|" + THREADS + "|" + PERCENT_NO_TX + "|" + PERCENT_TX_ROLLBACK + "|"
					+ useInterrupt + "]";

			final Runnable runnable = new ContinuousAccessRunnable(dataSource, PERCENT_NO_TX, PERCENT_TX_ROLLBACK, rollbackCounter,
					finished, runId, sendDummyStatement);

			final TestThreadsHolder threadsHolder = new TestThreadsHolder(THREADS, runnable)
			{
				@Override
				public void stopAll()
				{
					if (useInterrupt)
					{
						super.stopAll();
					}
					else
					{
						finished.set(true);
					}
				}
			};

			threadsHolder.startAll();

			waitDuration(RUN_SECONDS, maxConnections, dataSource, allConnections);

			threadsHolder.stopAll();
			final boolean allStoppedNormal = threadsHolder.waitForAll(30, TimeUnit.SECONDS);

			if (!allStoppedNormal)
			{
				// try fallback method
				finished.set(true);
				final boolean allStoppedFallback = threadsHolder.waitForAll(10, TimeUnit.SECONDS);
				if (allStoppedFallback)
				{
					LOG.error("Threads did not stop normally but only after using boolean flag!");
				}
				else
				{
					fail("db connection test threads did not stop correctly even after fallback method");
				}
			}


			// kill data source
			dataSource.destroy();
			assertTrue(dataSource.getConnectionPool().isPoolClosed());
			assertTrue(waitForAllInactive(dataSource.getConnectionPool(), 10, TimeUnit.SECONDS));

			if (PERCENT_TX_ROLLBACK > 0)
			{
				assertTrue(rollbackCounter.get() > 0);
			}

			final long maxAllowedConnections = maxConnections + rollbackCounter.get();

			final Stats stats = getStats(allConnections);

			LOG.info(//
			"max connections :" + maxConnections + "\n" + //
					"rollbacks :" + rollbackCounter.get() + "\n" + //
					"real connections :" + connectionCounter.get() + "\n" + //
					"closed:" + stats.closed + "\n" + //
					"open:" + stats.open + "\n" + //
					"borrowed :" + stats.borrowed + "\n" + //
					"returned :" + stats.returned + "\n" + //
					"invalidated :" + stats.invalidated + "\n");

			// we cannot be sure since not each rollbacked connections *must* be re-created
			assertTrue("handed out more than max connections (got:" + connectionCounter.get() + " > max:" + maxAllowedConnections
					+ ")",//
					connectionCounter.get() <= maxAllowedConnections);
			assertEquals("still got " + stats.borrowed + "borrowed connections", 0, stats.borrowed);
			assertEquals("connection count mismatch - total:" + connectionCounter.get() + " <> "
					+ (stats.returned + stats.invalidated) + " (returned:" + stats.returned + " + invalidated:" + stats.invalidated
					+ ")", //
					connectionCounter.get(), stats.returned + stats.invalidated);

			// make sure all connections have been finally closed

			assertEquals("data source " + dataSource + "still got " + dataSource.getNumInUse() + " connections in use", //
					0, dataSource.getNumInUse());
			assertEquals("data source " + dataSource + "still got " + dataSource.getNumPhysicalOpen()
					+ " physical connections open (despite none are in use)", //
					0, dataSource.getNumPhysicalOpen());
			assertTrue("data source " + dataSource + " had more than max allowed connections (max:" + maxConnections
					+ ", max in use:" + dataSource.getMaxInUse() + ")", //
					maxConnections >= dataSource.getMaxInUse());
			assertTrue("data source " + dataSource + " had more than max allowed physical connections (max:" + maxConnections
					+ ", max physical in use:" + dataSource.getMaxPhysicalOpen() + ")", //
					maxConnections >= dataSource.getMaxPhysicalOpen());
		}
		finally
		{
			destroyDataSource(dataSource);
		}

	}

	private void destroyDataSource(final HybrisDataSource dataSource)
	{
		if (dataSource != null)
		{
			try
			{
				dataSource.destroy();
			}
			catch (final Exception e)
			{
				fail(e.getMessage());
			}
		}
	}

	private void waitDuration(final int RUN_SECONDS, final int maxConnections, final HybrisDataSource dataSource,
			final Collection<TestConnectionImpl> allConnections)
	{
		final long start = System.currentTimeMillis();
		final long end = start + (RUN_SECONDS * 1000);
		do
		{
			final JDBCConnectionPool pool = dataSource.getConnectionPool();
			final Stats stats = getStats(allConnections);
			LOG.info("idle:" + pool.getNumIdle() + " active:" + pool.getNumActive() + " open:" + stats.open + " invalidated:"
					+ stats.invalidated + " closed:" + stats.closed);
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e)
			{
				// ok
			}
			assertTrue(dataSource.getMaxInUse() <= maxConnections);
		}
		while (System.currentTimeMillis() < end);
	}


	private boolean waitForAllInactive(final JDBCConnectionPool pool, final int time, final TimeUnit unit)
	{
		final long start = System.currentTimeMillis();
		final long end = start + TimeUnit.MILLISECONDS.convert(time, unit);
		do
		{
			final int idle = pool.getNumIdle();
			final int active = pool.getNumActive();
			if (idle == 0 && active == 0)
			{
				return true;
			}
			else
			{
				LOG.info("still waiting: idle:" + idle + ", active:" + active);
			}
			try
			{
				Thread.sleep(500);
			}
			catch (final InterruptedException e)
			{
				//
			}
		}
		while (System.currentTimeMillis() < end && !Thread.currentThread().isInterrupted());

		final int idle = pool.getNumIdle();
		if (idle != 0)
		{
			LOG.error("still got " + idle + " idle connections in pool");
		}
		final int active = pool.getNumActive();
		if (active != 0)
		{
			LOG.error("still got " + active + " active connections in pool");
		}
		return idle < 1 && active < 1;
	}

	@Test
	public void testJndiDataSource() throws SQLException
	{
		TestUtils.disableFileAnalyzer("log error expected");
		final Collection<TestConnectionImpl> allConnections = new ConcurrentLinkedQueue<TestConnectionImpl>();
		final AtomicLong connectionCounter = new AtomicLong(0);
		final HybrisDataSource dataSource = createDataSource(Registry.getCurrentTenantNoFallback(), allConnections,
				connectionCounter, false, true);

		final Connection conn = dataSource.getConnection();
		conn.close();

		// kill data source
		dataSource.destroy();
		assertTrue(dataSource.getConnectionPool().isPoolClosed());
		LOG.info("data source destroyed");
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void testReturnWhenClosed()
	{
		HybrisDataSource dataSource = null;
		TestThreadsHolder threadsHolder = null;
		try
		{
			final Collection<TestConnectionImpl> allConnections = new ConcurrentLinkedQueue<TestConnectionImpl>();
			final AtomicLong connectionCounter = new AtomicLong(0);
			dataSource = createDataSource(Registry.getCurrentTenantNoFallback(), allConnections, connectionCounter, false, false);

			final int maxConnections = dataSource.getMaxAllowedPhysicalOpen();

			final CountDownLatch allFetched = new CountDownLatch(maxConnections);
			final CountDownLatch release = new CountDownLatch(1);

			final Runnable runnable = new GetOneConnectionRunnable(dataSource, allFetched, release);
			threadsHolder = new TestThreadsHolder(maxConnections, runnable);

			threadsHolder.startAll();
			assertTrue(allFetched.await(30, TimeUnit.SECONDS));

			LOG.info("all connection fetched");

			assertEquals(maxConnections, dataSource.getNumInUse());
			assertEquals(maxConnections, dataSource.getNumPhysicalOpen());
			assertEquals(maxConnections, dataSource.getMaxInUse());
			assertEquals(maxConnections, dataSource.getMaxPhysicalOpen());

			// kill data source
			dataSource.destroy();
			assertTrue(dataSource.getConnectionPool().isPoolClosed());
			LOG.info("data source destroyed");

			// test get error
			try
			{
				dataSource.getConnection();
				fail("SQLExcpetion expected after destroy()");
			}
			catch (final SQLException e)
			{
				// fine
				LOG.info("no new connection allowed");
			}

			// check stats again -> should not have changed

			assertEquals(maxConnections, dataSource.getNumInUse());
			assertEquals(maxConnections, dataSource.getNumPhysicalOpen());
			assertEquals(maxConnections, dataSource.getMaxInUse());
			assertEquals(maxConnections, dataSource.getMaxPhysicalOpen());

			// now let all threads return their connections
			release.countDown();
			LOG.info("all threads close connections now...");
			threadsHolder.waitForAll(30, TimeUnit.SECONDS);
			LOG.info("all threads died");

			assertTrue(waitForAllInactive(dataSource.getConnectionPool(), 10, TimeUnit.SECONDS));

			// check final stats
			assertEquals(0, dataSource.getNumInUse());
			assertEquals(0, dataSource.getNumPhysicalOpen());
			assertEquals(maxConnections, dataSource.getMaxInUse());
			assertEquals(maxConnections, dataSource.getMaxPhysicalOpen());

			final Stats stats = getStats(allConnections);

			// make sure all connections have been finally closed
			assertEquals(0, stats.open);
		}
		catch (final InterruptedException e)
		{
			// ok
		}
		finally
		{
			stopThreads(threadsHolder);
			destroyDataSource(dataSource);
		}
	}

	private void stopThreads(final TestThreadsHolder threadsHolder)
	{
		if (threadsHolder != null)
		{
			try
			{
				threadsHolder.destroy();
			}
			catch (final Exception e)
			{
				// ignore silently
			}
		}
	}

	private static class ContinuousAccessRunnable implements Runnable
	{
		private final HybrisDataSource dataSource;
		private final int percentNoTX;
		private final int percentTxRollback;
		private final AtomicLong rollbackCounter;
		private final AtomicBoolean finished;
		private final String runId;
		private final boolean sendStatement;

		public ContinuousAccessRunnable(final HybrisDataSource dataSource, final int percentNoTX, final int percentTxRollback,
				final AtomicLong rollbackCounter, final AtomicBoolean finished, final String runId, final boolean sendStatement)
		{
			this.dataSource = dataSource;
			this.percentNoTX = percentNoTX;
			this.percentTxRollback = percentTxRollback;
			this.rollbackCounter = rollbackCounter;
			this.finished = finished;
			this.runId = runId;
			this.sendStatement = sendStatement;
		}

		public boolean isNotFinished()
		{
			return !finished.get() && !Thread.currentThread().isInterrupted();
		}

		@Override
		public void run()
		{
			while (isNotFinished())
			{
				final int mode = (int) (Math.random() * 100d);
				ConnectionImpl connection = null;
				try
				{
					connection = (ConnectionImpl) dataSource.getConnection();

					if (isTx(mode))
					{
						simulateTxConnection(connection, mode);
					}
					else
					{
						simulateNormalConnection(connection);
					}
				}
				catch (final JDBCConnectionPoolInterruptedException e)
				{
					// expected that
					Thread.currentThread().interrupt();
				}
				catch (final Exception e)
				{
					e.printStackTrace(System.err);
				}
				finally
				{
					cleanup(connection, mode);
				}
			}
		}

		private void simulateNormalConnection(final ConnectionImpl connection) throws SQLException
		{
			if (sendStatement)
			{
				Statement stmt = null;
				try
				{
					stmt = connection.createStatement();
					stmt.executeQuery("SELECT '" + runId + "/" + Thread.currentThread().getId() + "'");
				}
				finally
				{
					JdbcUtils.closeStatement(stmt);
				}
			}
		}

		private void simulateTxConnection(final ConnectionImpl connection, final int mode) throws SQLException
		{
			connection.setTxBoundUserTA(null);

			simulateNormalConnection(connection);

			if (isRollback(mode))
			{
				rollbackCounter.incrementAndGet();
				connection.rollback();
			}
			else
			{
				connection.commit();
			}
		}

		private void cleanup(final ConnectionImpl connection, final int mode)
		{
			if (connection != null)
			{
				if (isTx(mode))
				{
					connection.unsetTxBound();
				}
				JdbcUtils.closeConnection(connection);
			}
		}

		private boolean isTx(final int random)
		{
			return random >= percentNoTX;
		}

		private boolean isRollback(final int random)
		{
			return random >= (100 - percentTxRollback);
		}
	}


	private static class GetOneConnectionRunnable implements Runnable
	{
		private final HybrisDataSource dataSource;
		private final CountDownLatch allFetched;
		private final CountDownLatch release;

		public GetOneConnectionRunnable(final HybrisDataSource dataSource, final CountDownLatch allFetched,
				final CountDownLatch release)
		{
			this.dataSource = dataSource;
			this.allFetched = allFetched;
			this.release = release;
		}

		@Override
		public void run()
		{
			Connection connection = null;
			try
			{
				connection = dataSource.getConnection();
				allFetched.countDown();
				release.await();
			}
			catch (final InterruptedException e)
			{
				LOG.error("runner has been interrupted");
			}
			catch (final Exception e)
			{
				LOG.error(e.getMessage(), e);
			}
			finally
			{
				JdbcUtils.closeConnection(connection);
			}
		}
	}

	private HybrisDataSource createDataSource(final Tenant tenant, final Collection<TestConnectionImpl> allConnections,
			final AtomicLong connectionCounter, final boolean logToConsole, final boolean jndi)
	{
		final DataSourceFactory factory = new DataSourceImplFactory()
		{
			@Override
			public Connection wrapConnection(final HybrisDataSource wrappedDataSource, final Connection rawConnection)
			{
				final TestConnectionImpl testConnectionImpl = new TestConnectionImpl(wrappedDataSource, rawConnection,
						connectionCounter.incrementAndGet(), logToConsole);
				allConnections.add(testConnectionImpl);
				return testConnectionImpl;
			}

			@Override
			public HybrisDataSource createDataSource(final String id, final Tenant tenant,
					final Map<String, String> connectionParams, final boolean readOnly)
			{
				return new DataSourceImpl(tenant, id, connectionParams, readOnly, this)
				{
					@Override
					public Connection getConnection() throws SQLException
					{
						final TestConnectionImpl connection = (TestConnectionImpl) super.getConnection();
						final String caller = "ds.getConnection()";
						connection.assertBorrowed(caller);
						connection.assertNotReturned(caller);
						connection.assertNotInvalidated(caller);
						connection.assertNotClosedForReal(caller);

						return connection;
					}

					@Override
					public void invalidate(final ConnectionImpl conn)
					{
						super.invalidate(conn);
						final TestConnectionImpl testConnection = (TestConnectionImpl) conn;
						final String caller = "ds.invalidate()";

						testConnection.assertNotBorrowed(caller);
						testConnection.assertNotReturned(caller);
						testConnection.assertInvalidated(caller);
						testConnection.assertClosedForReal(caller);
					}
				};
			}

			@Override
			public HybrisDataSource createJNDIDataSource(final String id, final Tenant tenant, final String fromJNDI,
					final boolean readOnly)
			{
				return new DataSourceImpl(tenant, id, fromJNDI, readOnly, this)
				{
					@Override
					public Connection getConnection() throws SQLException
					{
						final TestConnectionImpl connection = (TestConnectionImpl) super.getConnection();
						final String caller = "ds.getConnection()";

						connection.assertBorrowed(caller);
						connection.assertNotReturned(caller);
						connection.assertNotInvalidated(caller);
						connection.assertNotClosedForReal(caller);

						return connection;
					}

					@Override
					public void invalidate(final ConnectionImpl conn)
					{
						super.invalidate(conn);
						final TestConnectionImpl testConnection = (TestConnectionImpl) conn;
						final String caller = "ds.invalidate()";

						testConnection.assertNotBorrowed(caller);
						testConnection.assertNotReturned(caller);
						testConnection.assertInvalidated(caller);
						testConnection.assertClosedForReal(caller);
					}
				};
			}

			@Override
			public JDBCConnectionPool createConnectionPool(final HybrisDataSource dataSource,
					final GenericObjectPoolConfig poolConfig)
			{
				final JDBCConnectionFactory factory = new JDBCConnectionFactory(dataSource)
				{
					@Override
					protected Connection createRawSQLConnection() throws SQLException
					{
						//if jndi is tested then we have simulate connection return
						return jndi ? tenant.getDataSource().getConnection() : super.createRawSQLConnection();
					}

					@Override
					public void destroyObject(final PooledObject<Connection> pooledConnection) throws SQLException
					{
						final TestConnectionImpl obj = (TestConnectionImpl) pooledConnection.getObject();
						final String caller = "factory.destroyObject()";

						if (!obj.getDataSource().getConnectionPool().isPoolClosed())
						{
							obj.assertBorrowed(caller);
							obj.assertNotReturned(caller);
						}
						obj.assertNotClosedForReal(caller);
						obj.assertNotInvalidated(caller);

						super.destroyObject(pooledConnection);

					}

				};
				return new TestJDBCConnectionPool(factory, poolConfig);
			}
		};

		final ConfigIntf cfg = tenant.getConfig();
		final Map<String, String> params = new HashMap<String, String>(5);
		params.put(SystemSpecificParams.DB_USERNAME, cfg.getParameter(SystemSpecificParams.DB_USERNAME));
		params.put(SystemSpecificParams.DB_PASSWORD, cfg.getParameter(SystemSpecificParams.DB_PASSWORD));
		params.put(SystemSpecificParams.DB_URL, cfg.getParameter(SystemSpecificParams.DB_URL));
		params.put(SystemSpecificParams.DB_DRIVER, cfg.getParameter(SystemSpecificParams.DB_DRIVER));
		params.put(SystemSpecificParams.DB_TABLEPREFIX, cfg.getParameter(SystemSpecificParams.DB_TABLEPREFIX));
		params.put("db.customsessionsql", cfg.getParameter("db.customsessionsql"));
		if (jndi)
		{
			return factory.createJNDIDataSource("test", tenant, "myJNDI", false);
		}
		else
		{
			return factory.createDataSource("test", tenant, params, false);
		}
	}

	private static class TestJDBCConnectionPool extends JDBCConnectionPool
	{

		public TestJDBCConnectionPool(final JDBCConnectionFactory factory,
				final GenericObjectPoolConfig cfg)
		{
			super(factory, cfg);
		}

		@Override
		public void invalidateConnection(final Connection obj)
		{
			final TestConnectionImpl testConnection = (TestConnectionImpl) obj;
			final String caller = "pool.invalidateObject()";

			testConnection.assertBorrowed(caller);
			testConnection.assertNotReturned(caller);
			testConnection.assertNotInvalidated(caller);

			super.invalidateConnection(obj);

			testConnection.assertClosedForReal(caller);
			testConnection.markInvalidated(caller);
		}

		@Override
		public ConnectionImpl borrowConnection() throws Exception //NOPMD
		{
			final TestConnectionImpl testConnection = (TestConnectionImpl) super.borrowConnection();
			final String caller = "pool.borrowConnection()";


			testConnection.assertNotInvalidated(caller);
			testConnection.assertNotBorrowed(caller);
			testConnection.assertNotClosedForReal(caller);
			// cannot check for returned since connection me be new here!!!

			testConnection.markBorrowed(caller);
			return testConnection;
		}

		@Override
		public void returnConnection(final Connection obj)
		{
			final TestConnectionImpl testConnection = (TestConnectionImpl) obj;
			final String caller = "pool.returnObject()";

			testConnection.assertBorrowed(caller);
			testConnection.assertNotClosedForReal(caller);
			testConnection.assertNotInvalidated(caller);
			testConnection.assertNotReturned(caller);

			// !!! Must mark returned BEFORE since pool hands out this connection during return !!!
			testConnection.markReturned(caller);

			super.returnConnection(obj);
		}
	}

	private static class Stats
	{
		private final int open;
		private final int closed;
		private final int borrowed;
		private final int returned;
		private final int invalidated;

		public Stats(final int open, final int closed, final int borrowed, final int returned, final int invalidated)
		{
			this.open = open;
			this.closed = closed;
			this.borrowed = borrowed;
			this.returned = returned;
			this.invalidated = invalidated;
		}
	}

	private Stats getStats(final Collection<TestConnectionImpl> connections)
	{
		int open = 0;
		int closed = 0;
		int borrowed = 0;
		int returned = 0;
		int invalidated = 0;
		for (final TestConnectionImpl con : connections)
		{
			synchronized (con)
			{
				if (con.isClosedForReal())
				{
					closed++;
				}
				else
				{
					open++;
				}
				if (con.isBorrowed())
				{
					borrowed++;
				}
				if (con.isReturned())
				{
					returned++;
				}
				if (con.isInvalidated())
				{
					invalidated++;
				}
			}
		}
		return new Stats(open, closed, borrowed, returned, invalidated);
	}


	private static class TestConnectionImpl extends ConnectionImpl
	{
		private final long number;
		private final boolean logToConsole;

		private String borrowingThread = null;
		private String returningThread = null;
		private String invalidatingThread = null;
		private String realClosingThread = null;

		public TestConnectionImpl(final HybrisDataSource dataSource, final Connection connection, final long number,
				final boolean logToConsole)
		{
			super(dataSource, connection);
			this.number = number;
			this.logToConsole = logToConsole;
		}

		public long getNumber()
		{
			return number;
		}

		@Override
		public void closeUnderlayingConnection() throws SQLException
		{
			final String caller = "conn.closeUnderlayingConnection()";

			assertNotClosedForReal(caller);
			super.closeUnderlayingConnection();
			markClosedForReal(caller);
		}

		public synchronized void assertNotBorrowed(final String caller)
		{
			if (borrowingThread != null)
			{
				LOG.error("connection " + this + " is borrowed by " + borrowingThread + ", caller is " + caller + "/"
						+ Thread.currentThread().getName());
			}
		}

		public synchronized void assertBorrowed(final String caller)
		{
			if (borrowingThread == null)
			{
				LOG.error("connection " + this + " is not borrowed, caller is " + caller + "/" + Thread.currentThread().getName());
			}
		}

		public synchronized boolean isBorrowed()
		{
			return borrowingThread != null;
		}

		public synchronized void assertNotReturned(final String caller)
		{
			if (returningThread != null)
			{
				LOG.error("connection " + this + " is returned by " + returningThread + ", caller is " + caller + "/"
						+ Thread.currentThread().getName());
			}
		}

		public synchronized void assertReturned(final String caller)
		{
			if (returningThread == null)
			{
				LOG.error("connection " + this + " is not returned, caller is " + caller + "/" + Thread.currentThread().getName());
			}
		}

		public synchronized boolean isReturned()
		{
			return returningThread != null;
		}

		public synchronized void assertNotInvalidated(final String caller)
		{
			if (invalidatingThread != null)
			{
				LOG.error("connection " + this + " is already invalidated by " + invalidatingThread + ", caller is " + caller + "/"
						+ Thread.currentThread().getName());
			}
		}

		public synchronized void assertInvalidated(final String caller)
		{
			if (invalidatingThread == null)
			{
				LOG.error("connection " + this + " is not invalidated, caller is " + caller + "/" + Thread.currentThread().getName());
			}
		}

		public synchronized boolean isInvalidated()
		{
			return invalidatingThread != null;
		}

		public synchronized void assertClosedForReal(final String caller)
		{
			if (realClosingThread == null)
			{
				LOG.error("connection " + this + " is not closed for real, caller is " + caller + "/"
						+ Thread.currentThread().getName());
			}
		}

		public synchronized void assertNotClosedForReal(final String caller)
		{
			if (realClosingThread != null)
			{
				LOG.error("connection " + this + " is already closed by " + realClosingThread + ", caller is " + caller + "/"
						+ Thread.currentThread().getName());
			}
		}

		public synchronized boolean isClosedForReal()
		{
			return realClosingThread != null;
		}

		public synchronized void markBorrowed(final String caller)
		{
			borrowingThread = Thread.currentThread().getName();
			returningThread = null;
			if (logToConsole)
			{
				LOG.info("borrowed " + this + " caller is " + caller + "/" + borrowingThread);
			}
		}

		public synchronized void markReturned(final String caller)
		{
			borrowingThread = null;
			returningThread = Thread.currentThread().getName();
			if (logToConsole)
			{
				LOG.info("returned " + this + " caller is " + caller + "/" + returningThread);
			}
		}

		public synchronized void markInvalidated(final String caller)
		{
			borrowingThread = null;
			invalidatingThread = Thread.currentThread().getName();
			if (logToConsole)
			{
				LOG.info("invalidated " + this + " caller is " + caller + "/" + invalidatingThread);
			}
		}

		public synchronized void markClosedForReal(final String caller)
		{
			realClosingThread = Thread.currentThread().getName();
			if (logToConsole)
			{
				LOG.info("closed for real " + this + " caller is " + caller + "/" + realClosingThread);
			}
		}

		@Override
		public boolean equals(final Object obj)
		{
			return super.equals(obj) || number == ((TestConnectionImpl) obj).number;
		}

		@Override
		public int hashCode()
		{
			return (int) number;
		}

		@Override
		public String toString()
		{
			return "TestConnection_" + getNumber();
		}

	}
}
