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
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.cluster.BroadcastService;
import de.hybris.platform.cluster.DefaultBroadcastService;
import de.hybris.platform.cluster.InvalidationBroadcastHandler;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.media.MediaFormat;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.jdbcwrapper.ConnectionImpl;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.jdbcwrapper.JUnitConnectionErrorCheckingJDBCConnectionPool;
import de.hybris.platform.jdbcwrapper.JUnitConnectionImpl;
import de.hybris.platform.jdbcwrapper.JUnitConnectionImpl.CommitMode;
import de.hybris.platform.persistence.framework.EntityInstance;
import de.hybris.platform.persistence.framework.EntityInstanceContext;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.tx.DefaultTransaction;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.tx.TransactionException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.StopWatch;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;


@IntegrationTest
public class TransactionTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(TransactionTest.class);

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

	private boolean hasJUnitJDBCSetup()
	{
		return Registry.getCurrentTenantNoFallback().getDataSource()
				.getConnectionPool() instanceof JUnitConnectionErrorCheckingJDBCConnectionPool;
	}

	@Test
	public void testCommitWithActivateFalse() throws Exception
	{
		try
		{
			// simulate config setting with threadlocal flag -> has the same semantics!
			Transaction.enableUserTransactionForThread(false);

			final Title title = (Title) Transaction.current().execute(new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					return UserManager.getInstance().createTitle("TTT");
				}
			});

			assertNotNull(title);
			assertTrue(title.isAlive());
		}
		finally
		{
			Transaction.enableUserTransactionForThread(true);
		}
	}

	@Test
	public void testIneffectiveRollbackWithActivateFalse() throws Exception
	{
		try
		{
			final Transaction tx = Transaction.current();
			// simulate config setting with threadlocal flag -> has the same semantics!
			Transaction.enableUserTransactionForThread(false);

			final AtomicReference<PK> titlePKref = new AtomicReference<PK>();
			try
			{
				tx.execute(new TransactionBody()
				{
					@Override
					public Object execute() throws Exception
					{
						titlePKref.set(UserManager.getInstance().createTitle("TTT").getPK());
						throw new RuntimeException("rollback please");
					}
				});
				fail("RuntimeException expected");
			}
			catch (final RuntimeException e)
			{
				assertEquals("rollback please", e.getMessage());
			}

			assertNotSame(tx, Transaction.current());
			assertNotNull(titlePKref.get());
			final Title title = jaloSession.getItem(titlePKref.get());
			assertNotNull(title);
			assertTrue(title.isAlive());
		}
		finally
		{
			Transaction.enableUserTransactionForThread(true);
		}
	}

	@Test
	public void testErrorOnBeginWithNullConnection() throws Exception
	{
		final DefaultTransaction transaction = new DefaultTransaction()
		{
			@Override
			protected ConnectionImpl getConnectionToBind(final HybrisDataSource dataSource) throws SQLException
			{
				return null;
			}
		};
		transaction.activateAsCurrentTransaction();
		assertSame(transaction, Transaction.current());

		try
		{
			transaction.begin();
			fail("Expected exception on Transaction.begin() with NULL connection !!!");
		}
		catch (final Exception e)
		{
			// fair enough
		}

		assertFalse(transaction.isRunning());
		assertNotSame(transaction, Transaction.current());
	}

	@Test
	public void testErrorOnBeginWithExceptionInDataSource() throws Exception
	{
		final DefaultTransaction transaction = new DefaultTransaction()
		{
			@Override
			protected ConnectionImpl getConnectionToBind(final HybrisDataSource dataSource) throws SQLException
			{
				throw new SQLException("weird test exception");
			}
		};
		transaction.activateAsCurrentTransaction();
		assertSame(transaction, Transaction.current());

		try
		{
			transaction.begin();
			fail("Expected exception on Transaction.begin() with NULL connection !!!");
		}
		catch (final Exception e)
		{
			// fair enough
		}

		assertFalse(transaction.isRunning());
		assertNotSame(transaction, Transaction.current());
	}



	@Test
	public void testItemAccessDuringCommit_PLA10839() throws Exception
	{
		final Title title1 = UserManager.getInstance().createTitle("t1");
		final Title title2 = UserManager.getInstance().createTitle("t2");
		final Title title3 = UserManager.getInstance().createTitle("t3");
		final Title title4 = UserManager.getInstance().createTitle("t4");

		final AtomicBoolean listenerHasBeenCalled = new AtomicBoolean(false);

		final InvalidationListener listener = new InvalidationListener()
		{
			@Override
			public void keyInvalidated(final Object[] key, final int invalidationType, final InvalidationTarget target,
					final RemoteInvalidationSource remoteSrc)
			{
				listenerHasBeenCalled.set(true);
				// access t1 here
				if (StringUtils.isEmpty(title1.getName()))
				{
					System.err.println("title1 name is empty");
				}
			}
		};
		final InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic(new Object[]
		{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY });
		try
		{
			topic.addInvalidationListener(listener);

			final Transaction tx = Transaction.current();
			tx.execute(new TransactionBody()
			{

				@Override
				public Object execute() throws Exception
				{
					title2.setName("foo");
					title3.setName("foo");
					title4.setName("foo");
					return null;
				}
			});
			assertEquals("foo", title2.getName());
			assertEquals("foo", title3.getName());
			assertEquals("foo", title4.getName());
			assertTrue(listenerHasBeenCalled.get());
		}
		finally
		{
			topic.removeInvalidationListener(listener);
		}
	}

	@Test
	public void testItemUpdateDuringCommit_PLA10839() throws Exception
	{
		final Title title1 = UserManager.getInstance().createTitle("t1");
		final Title title2 = UserManager.getInstance().createTitle("t2");
		final Title title3 = UserManager.getInstance().createTitle("t3");
		final Title title4 = UserManager.getInstance().createTitle("t4");

		final AtomicBoolean listenerHasBeenCalled = new AtomicBoolean(false);

		final InvalidationListener listener = new InvalidationListener()
		{
			@Override
			public void keyInvalidated(final Object[] key, final int invalidationType, final InvalidationTarget target,
					final RemoteInvalidationSource remoteSrc)
			{
				listenerHasBeenCalled.set(true);
				// change t1 here
				title1.setName("newOne");
			}
		};
		final InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic(new Object[]
		{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY });
		try
		{
			topic.addInvalidationListener(listener);

			final Transaction tx = Transaction.current();
			tx.execute(new TransactionBody()
			{

				@Override
				public Object execute() throws Exception
				{
					title2.setName("foo");
					title3.setName("foo");
					title4.setName("foo");
					return null;
				}
			});
			assertEquals("foo", title2.getName());
			assertEquals("foo", title3.getName());
			assertEquals("foo", title4.getName());
			assertEquals("newOne", title1.getName());
			assertTrue(listenerHasBeenCalled.get());
		}
		finally
		{
			topic.removeInvalidationListener(listener);
		}
	}

	@Test
	public void shouldExecuteLogicOnCommit() throws Exception
	{
        // given
		final Transaction tx = Transaction.current();
        final TestOnTxEndExecution ex = new TestOnTxEndExecution();

        // when
        tx.begin();
        tx.executeOnCommit(ex);
        tx.commit();

        // then
        assertThat(ex.getCounter()).isEqualTo(1);
	}

    @Test
    public void shouldExecuteLogicOnRollback() throws Exception
    {
    	// given
        final Transaction tx = Transaction.current();
        final TestOnTxEndExecution ex = new TestOnTxEndExecution();


    	// when
        tx.begin();
        tx.executeOnRollback(ex);
        tx.rollback();

    	// then
        assertThat(ex.getCounter()).isEqualTo(1);
    }

    @Test
    public void shouldExecuteLogicOnCommit_NestedTX() throws Exception
    {
        // given
        final Transaction tx = Transaction.current();
        final TestOnTxEndExecution ex = new TestOnTxEndExecution();

        // when
        tx.begin();
        tx.begin();
        tx.executeOnCommit(ex);
        tx.commit();

        // then
        assertThat(ex.getCounter()).isEqualTo(0);

        // when
        tx.commit();

        // then
        assertThat(ex.getCounter()).isEqualTo(1);
    }

    @Test
    public void shouldExecuteLogicOnRollback_NestedTX() throws Exception
    {
        // given
        final Transaction tx = Transaction.current();
        final TestOnTxEndExecution ex = new TestOnTxEndExecution();

        // when
        tx.begin();
        tx.begin();
        tx.executeOnRollback(ex);
        tx.rollback();

        // then
        assertThat(ex.getCounter()).isEqualTo(0);

        // when
        tx.rollback();

        // then
        assertThat(ex.getCounter()).isEqualTo(1);
    }

    private class TestOnTxEndExecution extends Transaction.TransactionAwareExecution
    {
        private int counter = 0;

        @Override
        public void execute(final Transaction tx) throws Exception
        {
            counter++;
        }

        @Override
        public Object getId()
        {
            return Integer.valueOf(counter);
        }

        public int getCounter()
        {
            return counter;
        }

    }

	@Test
	public void testErrorInNestedCommit()
	{
		final Transaction tx = new DefaultTransaction()
		{
			@Override
			public void flushDelayedStore()
			{
				throw new TestTxException();
			}
		};
		tx.activateAsCurrentTransaction();
		assertSame("Wrong current transaction", tx, Transaction.current());
		try
		{
			// 1. outer begin must be fine
			tx.begin();

			assertSame("Wrong current transaction", tx, Transaction.current());
			assertEquals("Open connection count is not correct", 1, tx.getOpenTransactionCount());

			// 2. nested begin calls flushDelayedStore() -> error
			try
			{
				tx.begin();
				fail("TransactionException expected");
			}
			catch (final TestTxException e)
			{
				// fine
			}
			catch (final Exception e)
			{
				fail("unexpected transaction exception " + e);
			}
			assertSame("Wrong current transaction", tx, Transaction.current());
			assertEquals("Open connection count is wrongly increased", 1, tx.getOpenTransactionCount());
			assertTrue("Transaction is not marked as rollback-only", tx.isRollbackOnly());
			assertTrue("Transaction is no longer running", tx.isRunning());

			// 3. rollback() -> must be fine again
			tx.rollback();

			assertFalse(tx.isRunning());
			assertNotSame(tx, Transaction.current());
		}
		finally
		{
			cleanup(tx, null);
		}
	}

	private static class TestTxException extends RuntimeException
	{
		//
	}

	@Test
	public void testErrorInNestedCommitUsingTxBody()
	{
		testErrorInNestedCommitUsingTxBody(true);
		testErrorInNestedCommitUsingTxBody(false);
	}

	private void testErrorInNestedCommitUsingTxBody(final boolean throwErrorInBegin)
	{
		final Transaction tx = new DefaultTransaction()
		{
			private boolean inBegin = false;
			private boolean inCommit = false;

			@Override
			public void begin() throws TransactionException
			{
				inBegin = true;
				try
				{
					super.begin();
				}
				finally
				{
					inBegin = false;
				}
			}

			@Override
			public void commit() throws TransactionException
			{
				inCommit = true;
				try
				{
					super.commit();
				}
				finally
				{
					inCommit = false;
				}
			}

			@Override
			public void flushDelayedStore()
			{
				if ((throwErrorInBegin && inBegin) || (!throwErrorInBegin && inCommit))
				{
					throw new TestTxException();
				}
			}
		};
		tx.activateAsCurrentTransaction();
		assertSame("Wrong current transaction", tx, Transaction.current());
		try
		{
			// 1. outer begin must be fine
			try
			{
				tx.execute(new TransactionBody()
				{
					@Override
					public Object execute() throws Exception
					{
						assertSame("Wrong current transaction", tx, Transaction.current());
						assertEquals("Open connection count is not correct", 1, tx.getOpenTransactionCount());

						// 2. nested begin calls flushDelayedStore() -> error
						try
						{
							tx.execute(new TransactionBody()
							{
								@Override
								public Object execute() throws Exception
								{
									if (throwErrorInBegin)
									{
										fail("TransactionException expected");
									}
									return null;
								}
							});
						}
						catch (final TestTxException e)
						{
							// check that we got the correct error
							assertSame("Wrong current transaction after begin() error", tx, Transaction.current());
							assertEquals("Open connection count is not correct after inner begin() error", 1,
									tx.getOpenTransactionCount());
							// pass on to outer tx
							throw e;
						}
						catch (final Exception e)
						{
							fail("unexpected inner transaction exception " + e);
						}
						return null;
					}
				});
			}
			catch (final TestTxException e)
			{
				// fine
			}
			catch (final Exception e)
			{
				fail("unexpected outer transaction exception " + e);
			}

			assertFalse(tx.isRunning());
			assertNotSame(tx, Transaction.current());
		}
		finally
		{
			cleanup(tx, null);
		}
	}

	@Test
	public void testRollbackOnCommitError() throws SQLException
	{
		if (hasJUnitJDBCSetup())
		{
			final Transaction tx = Transaction.current();
			JUnitConnectionImpl con = null;
			try
			{
				tx.begin();
				tx.setRollbackOnCommitError(true);
				con = ((JUnitConnectionImpl) tx.getTXBoundConnection());

				// simulate error which leaves transaction open !
				con.setCommitMode(CommitMode.NO_COMMIT_ERROR);

				assertFalse("autocommit is still on after begin", con.getAutoCommit());
				try
				{
					tx.commit();
				}
				catch (final TransactionException e)
				{
					assertTrue("unexpected commit exception " + e.getCause(), e.getCause() instanceof SQLTransactionRollbackException);
				}
				assertFalse("transaction is still running after rollback-on-commit-error", tx.isRunning());
				assertTrue("autocommit is still off after commit/rollback", con.getAutoCommit());
				assertFalse("rollbacked (real) connection is closed", con.getUnderlayingConnection().isClosed());
			}
			finally
			{
				cleanup(tx, con);
			}
		}
		else
		{
			LOG.warn("Cannot run TransactionTest.testRollbackOnCommitError() since current tennat doesnt have JUnit JDBC setup!");
		}
	}

	@Test
	public void testConnectionNoClosedAfterRollback() throws SQLException
	{
		if (hasJUnitJDBCSetup())
		{
			final Transaction tx = Transaction.current();
			JUnitConnectionImpl con = null;
			try
			{
				tx.begin();
				con = ((JUnitConnectionImpl) tx.getTXBoundConnection());

				assertFalse("autocommit is still on", con.getAutoCommit());

				tx.rollback();

				assertFalse("rollbacked (real) connection is closed", con.getUnderlayingConnection().isClosed());
				assertTrue("autocommit is still off", con.getAutoCommit());
			}
			finally
			{
				cleanup(tx, con);
			}
		}
		else
		{
			LOG.warn(
					"Cannot run TransactionTest.testConnectionNoClosedAfterRollback() since current tennat doesnt have JUnit JDBC setup!");
		}
	}

	@Test
	public void testErrorInCommit() throws SQLException
	{
		if (hasJUnitJDBCSetup())
		{
			final Transaction tx = Transaction.current();

			JUnitConnectionImpl con = null;
			try
			{
				tx.begin();
				con = ((JUnitConnectionImpl) tx.getTXBoundConnection());
				// simulate error in commit ( tx will be committed though )
				con.setCommitMode(CommitMode.COMMIT_AND_ERROR);

				assertFalse("autocommit is still on", con.getAutoCommit());

				try
				{
					tx.commit();
					fail("TransactionException expected");
				}
				catch (final TransactionException e)
				{
					assertTrue("unexpected commit exception " + e.getCause(), e.getCause() instanceof SQLTransactionRollbackException);
				}
				assertTrue("autocommit is still off", con.getAutoCommit());
			}
			finally
			{
				cleanup(tx, con);
			}
		}
		else
		{
			LOG.warn("Cannot run TransactionTest.testErrorInCommit() since current tennat doesnt have JUnit JDBC setup!");
		}
	}

	private void cleanup(final Transaction tx, final JUnitConnectionImpl con)
	{
		if (con != null)
		{
			con.resetTestMode();
		}
		try
		{
			if (tx.isRunning())
			{
				tx.rollback();
			}
		}
		catch (final Exception e)
		{
			LOG.error("error cleaning up transaction", e);
		}
	}

	@Test
	public void testLocking() throws Exception
	{

		if (Config.isHSQLDBUsed())
		{
			LOG.warn("HDSQLDB doesnt seem to support SELECT FOR UPDATE properly so we don't test it any more");
			return;
		}

		final ProductManager productManager = ProductManager.getInstance();

		final Currency curr = C2LManager.getInstance().createCurrency("TestCurr");

		/** Verify that we can begin a transaction, lock an entity, then commit without an exception occurring. */
		{
			final Transaction transaction = Transaction.current();
			try
			{
				assertNotNull("Transaction object is null", transaction);
				assertFalse("A previous transaction is already running.", transaction.isRunning());
				transaction.begin();
				final Product productForTest1 = productManager.createProduct("transactionLockingTest1");
				transaction.commit();
				transaction.begin();
				transaction.lock(productForTest1);
				transaction.commit();
			}
			catch (final Exception e)
			{
				transaction.rollback();
				throw e;
			}
		}

		{
			/** Verify that an IllegalStateException is thrown if we attempt to lock outside of a transaction. */
			final Transaction transaction = Transaction.current();
			try
			{
				assertNotNull("Transaction object is null", transaction);
				assertFalse("A previous transaction is already running.", transaction.isRunning());
				final Product productForTest2 = productManager.createProduct("transactionLockingTest2");
				transaction.lock(productForTest2);
				fail("Expected IllegalStateException to occur when attempting to lock an item outside of a transaction.");
			}
			// An IllegalStateException is expected for this test to pass.
			catch (final IllegalStateException e)
			{
				//
			}
		}

		/**
		 * Verify that if we attempt to acquire a lock on the same entity multiple times from the same transaction, that
		 * no errors occur.
		 */
		{
			final Transaction transaction = Transaction.current();
			try
			{
				assertNotNull("Transaction object is null", transaction);
				assertFalse("A previous transaction is already running.", transaction.isRunning());
				final Product productForTest3 = productManager.createProduct("transactionLockingTest3");
				transaction.begin();
				for (int i = 0; i < 10; i++)
				{
					transaction.lock(productForTest3);
				}
				transaction.commit();
			}
			catch (final Exception e)
			{
				transaction.rollback();
				throw e;
			}
		}

		/**
		 * Verify that if we begin a transaction, lock an entity, then commit multiple times that a lock can be acquired
		 * each time.
		 */
		{
			final Transaction transaction = Transaction.current();
			try
			{
				final Product productForTest4 = productManager.createProduct("transactionLockingTest4");
				for (int i = 0; i < 10; i++)
				{
					assertNotNull("Transaction object is null", transaction);
					assertFalse("A previous transaction is already running.", transaction.isRunning());
					transaction.begin();
					transaction.lock(productForTest4);
					transaction.commit();
				}
			}
			catch (final Exception e)
			{
				transaction.rollback();
				throw e;
			}
		}

		/**
		 * Verify that if we begin a transaction, lock an entity, then rollback multiple times that a lock can be acquired
		 * each time.
		 */
		{
			final Transaction transaction = Transaction.current();
			try
			{
				final Product productForTest5 = productManager.createProduct("transactionLockingTest5");
				for (int i = 0; i < 10; i++)
				{
					assertNotNull("Transaction object is null", transaction);
					assertFalse("A previous transaction is already running.", transaction.isRunning());
					transaction.begin();
					transaction.lock(productForTest5);
					transaction.rollback();
				}
			}
			catch (final Exception e)
			{
				transaction.rollback();
				throw e;
			}
		}

		/**
		 * Verify that we can not lock after a transaction has been committed.
		 */
		{
			final Transaction transaction = Transaction.current();
			try
			{
				final Product productForTest6 = productManager.createProduct("transactionLockingTest6");
				assertNotNull("Transaction object is null", transaction);
				assertFalse("A previous transaction is already running.", transaction.isRunning());
				transaction.begin();
				transaction.commit();
				transaction.lock(productForTest6);
				fail("A lock was acquired after the transaction has been committed.");
			}
			// An IllegalStateException is expected for the test to pass
			catch (final IllegalStateException e)
			{
				//
			}
		}

		/**
		 * Verify that we can not lock after a transaction has been rolled back.
		 */
		{
			final Transaction transaction = Transaction.current();
			try
			{
				final Product productForTest7 = productManager.createProduct("transactionLockingTest7");
				assertNotNull("Transaction object is null", transaction);
				assertFalse("A previous transaction is already running.", transaction.isRunning());
				transaction.begin();
				transaction.rollback();
				transaction.lock(productForTest7);
				fail("A lock was acquired after the transaction has been rolled back.");
			}
			// An IllegalStateException is expected for the test to pass
			catch (final IllegalStateException e)
			{
				//
			}
		}

		/**
		 * Verify multiple threads attempting to lock the same object and the behavior that occurs.
		 */
		try
		{
			final Order lockedOrder = OrderManager.getInstance().createOrder(//
					"lockedOrder", //
					JaloSession.getCurrentSession().getUser(), //
					curr, //
					Calendar.getInstance().getTime(), //
					true);
			lockedOrder.setTotal(0.0d);

			final ComposedType composedType = lockedOrder.getComposedType();

			final String checkQuery = "SELECT "
					+ composedType.getAttributeDescriptorIncludingPrivate(Order.TOTAL).getDatabaseColumn() + " FROM "
					+ composedType.getTable() + " WHERE PK = ?";

			final int THREADS = 16;

			// Create an executor service that uses 16 threads to test
			// the transaction locking
			final ExecutorService executor = Executors.newFixedThreadPool(//
					THREADS, //
					new ThreadFactory()
					{
						final Tenant threadFactoryTenant = Registry.getCurrentTenant();

						@Override
						public Thread newThread(final Runnable runnable)
						{
							return new RegistrableThread()
							{
								protected void prepareThread()
								{
									Registry.setCurrentTenant(threadFactoryTenant);
								}

								protected void unprepareThread()
								{
									JaloSession.deactivate();
									Registry.unsetCurrentTenant();
								}

								@Override
								public void internalRun()
								{
									try
									{
										prepareThread();
										runnable.run();
									}
									finally
									{
										unprepareThread();
									}
								}
							};
						}
					});

			// Create 8 callables that will concurrently
			// attempt to lock the same object.
			final AtomicInteger stackCounter = new AtomicInteger();
			final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
			for (int j = 0; j < THREADS; j++)
			{
				callables.add(new Callable<Object>()
				{
					@Override
					public Object call() throws Exception
					{
						final PK pk = lockedOrder.getPK();
						if (pk == null)
						{
							throw new IllegalStateException();
						}

						for (int k = 0; k < 100; k++)
						{
							final Transaction transaction = Transaction.current();

							assertNotNull("Transaction object is null", transaction);

							PreparedStatement statement = null;
							ResultSet resultSet = null;
							try
							{
								transaction.begin();
								transaction.setTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
								transaction.lock(lockedOrder);
								final int stack = stackCounter.incrementAndGet();
								if (stack > 1)
								{
									stackCounter.decrementAndGet();
									throw new IllegalStateException("Got " + stack + " threads in protected area!");
								}

								statement = transaction.getTXBoundConnection().prepareStatement(checkQuery);
								statement.setLong(1, lockedOrder.getPK().getLongValue());
								resultSet = statement.executeQuery();
								if (!resultSet.next())
								{
									throw new IllegalStateException("Expected result set");
								}
								final double dbValue = resultSet.getDouble(1);
								final double jaloValue = lockedOrder.getTotal();
								if (Math.abs(dbValue - jaloValue) >= 1d)
								{
									throw new IllegalStateException("Jalo value differs from db value : " + jaloValue + "<>" + dbValue);
								}

								lockedOrder.setTotal(jaloValue + 1.0d);

								stackCounter.decrementAndGet();
								transaction.commit();
							}
							catch (final Exception e)
							{
								e.printStackTrace();
								transaction.rollback();
								throw e;
							}
							finally
							{
								Utilities.tryToCloseJDBC(null, statement, resultSet, true);
							}
						}
						return null;
					}
				});
			}
			// Get the value of each future to determine if an exception was thrown.
			for (final Future<Object> future : executor.invokeAll(callables))
			{
				future.get();
			}
			final double expected = THREADS * 100;
			assertEquals(//
					"Total value of order after all transaction differs", //
					expected, //
					((Order) JaloSession.getCurrentSession().getItem(lockedOrder.getPK())).getTotal(), 0.000001);
		}
		catch (final IllegalStateException e)
		{
			e.printStackTrace();
			throw e;
		}

		/**
		 * Verify changes to a value on a lock
		 */

		// TODO:


		/**
		 * Tests related to caching
		 */

		// TODO:
	}

	@Test
	public void testGetAllAttributes() throws Exception
	{
		final Product product = ProductManager.getInstance().createProduct("acode");

		final int CNT = 10000;

		StopWatch stopWatch = new StopWatch("getAllAttributes");
		for (int i = 0; i < CNT; i++)
		{
			product.getAllAttributes();
		}
		stopWatch.stop();

		stopWatch = new StopWatch("getAllProperties");
		for (int i = 0; i < CNT; i++)
		{
			product.getAllProperties();
		}
		stopWatch.stop();
	}



	@Test
	public void testInTXModification1() throws Exception
	{
		final Country country = C2LManager.getInstance().createCountry("before");
		assertNotNull(country);
		Transaction.current().execute(new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				Transaction.current().enableDelayedStore(false);
				try
				{
					country.setIsoCode("after");
					C2LManager.getInstance().getCountryByIsoCode("before"); //should find the old
					fail("Jaloitemnotfound should occur!");
				}
				catch (final JaloItemNotFoundException e)
				{
					// DOCTODO Document reason, why this block is empty
				}
				try
				{
					C2LManager.getInstance().getCountryByIsoCode("after"); //and not! the new
				}
				catch (final JaloItemNotFoundException e)
				{
					fail("Jaloitemnotfound should not occur!");
				}
				return null;
			}
		});
	}

	@Test
	public void testInTXModification2() throws Exception
	{
		final Country country = C2LManager.getInstance().createCountry("before");
		assertNotNull(country);
		Transaction.current().execute(new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				Transaction.current().enableDelayedStore(true);
				try
				{
					country.setIsoCode("after");
					C2LManager.getInstance().getCountryByIsoCode("after"); //should find the old
					fail("JaloItemNotFound should occur!");
				}
				catch (final JaloItemNotFoundException e)
				{
					// DOCTODO Document reason, why this block is empty
				}
				try
				{
					C2LManager.getInstance().getCountryByIsoCode("before"); //and not! the new
				}
				catch (final JaloItemNotFoundException e)
				{
					fail("Jaloitemnotfound should not occur!");
				}
				Transaction.current().flushDelayedStore();
				try
				{
					C2LManager.getInstance().getCountryByIsoCode("after"); //should find the new
				}
				catch (final JaloItemNotFoundException e)
				{
					fail("Jaloitemnotfound should not occur!");
				}
				return null;
			}
		});
	}

	@Test
	public void testInTXRemoval() throws Exception
	{

		final String iso = PK.createUUIDPK(0).getHex();
		final Country country = C2LManager.getInstance().createCountry(iso);

		Transaction.current().execute(new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				Transaction.current().enableDelayedStore(false);
				country.remove();
				try
				{
					C2LManager.getInstance().getCountryByIsoCode(iso);
					fail("Jaloitemnotfound should occur!");
				}
				catch (final JaloItemNotFoundException e)
				{
					//ok, good
				}
				return null;
			}
		});

	}

	@Test
	public void testGlobalCacheInvalidationMessageSent() throws Exception
	{
		final int MAX_WAIT_SECONDS = 15;

		assertFalse(Transaction.current().isRunning());
		final Country country = jaloSession.getC2LManager().createCountry("Country");
		final PK pk = country.getPK();

		final DefaultBroadcastService broadcastService = DefaultBroadcastService.getInstance();
		final TestInvalidationMessageHandler messageHandler = new TestInvalidationMessageHandler(broadcastService);
		try
		{
			broadcastService.registerBroadcastListener(messageHandler, false);

			Transaction.current().execute(new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					assertEquals("Country", country.getIsoCode());
					assertFalse("got message before update in tx", messageHandler.gotEventFor(pk));
					country.setIsocode("CountryNew");
					assertFalse("got message after update but still in tx", messageHandler.gotEventFor(pk));
					return null;
				}
			});

			assertEquals("CountryNew", country.getIsoCode());
			final long maxWait = System.currentTimeMillis() + (MAX_WAIT_SECONDS * 1000);
			boolean gotEvent = false;
			do
			{
				Thread.sleep(100);
				gotEvent = messageHandler.gotEventFor(pk);
			}
			while (!gotEvent && System.currentTimeMillis() < maxWait);
			assertTrue(gotEvent);
		}
		finally
		{
			broadcastService.unregisterBroadcastListener(messageHandler);
		}
	}

	static class TestInvalidationMessageHandler extends InvalidationBroadcastHandler
	{
		public TestInvalidationMessageHandler(final BroadcastService broadcastService)
		{
			super(broadcastService);
		}

		final List<InvalidationEvent> events = new CopyOnWriteArrayList<InvalidationBroadcastHandler.InvalidationEvent>();

		@Override
		protected void processInvalidation(final InvalidationEvent event, final de.hybris.platform.cluster.RawMessage message)
		{
			events.add(event);
		}

		boolean gotEventFor(final PK itemPK)
		{
			for (final InvalidationEvent e : events)
			{
				final Object[] key = e.getKey();
				if (key.length == 4 && Cache.CACHEKEY_HJMP.equals(key[0]) && Cache.CACHEKEY_ENTITY.equals(key[1])
						&& Integer.toString(itemPK.getTypeCode()).equals(key[2]) && itemPK.equals(key[3]))
				{
					return true;
				}
			}
			return false;
		}
	}


	@Test
	public void testJaloCacheInvalidation() throws Exception
	{
		Country country = null;
		assertFalse(Transaction.current().isRunning());
		assertNotNull(country = jaloSession.getC2LManager().createCountry("code11"));
		assertEquals("code11", country.getIsoCode());

		Transaction.current().begin();
		assertEquals("code11", country.getIsoCode());
		country.setIsoCode("code2");
		assertEquals("code2", country.getIsoCode());
		LOG.debug("before commit");
		Transaction.current().commit();
		LOG.debug("after commit");
		assertEquals("code2", country.getIsoCode());

		Transaction.current().begin();
		LOG.debug("RRgetting iso!");
		assertEquals("code2", country.getIsoCode());
		LOG.debug("RRsetting iso!");
		country.setIsoCode("code3");
		LOG.debug("RRgetting iso!");
		assertEquals("code3", country.getIsoCode());
		LOG.debug("before: " + country.getIsoCode());
		Transaction.current().rollback();

		LOG.debug("after: " + country.getIsoCode());
		assertEquals("code2", country.getIsoCode());
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
	public void testIsolation() throws ConsistencyCheckException
	{
		if (Config.itemCacheIsolationActivated())
		{
			testIsolationInternal(false);
			testIsolationInternal(true);
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

			final RunnerCreator<IsolationTestRunnable> creator = new RunnerCreator<TransactionTest.IsolationTestRunnable>()
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

			final TestThreadsHolder<IsolationTestRunnable> threads = new TestThreadsHolder<TransactionTest.IsolationTestRunnable>(2,
					creator);

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

	@Test
	public void testDeadlocks() throws Exception
	{
		Product product = null;

		try
		{
			Transaction.current().begin();

			assertNotNull(product = ProductManager.getInstance().createProduct("txtest"));
			final ComposedType productType1 = jaloSession.getTypeManager().getRootComposedType(Constants.TC.Product);
			final ComposedType productType2 = product.getComposedType();

			assertEquals(productType1.getCode(), productType2.getCode());

			Transaction.current().commit();
		}
		catch (final Exception e)
		{
			Transaction.current().rollback();
			if (product != null)
			{
				assertNotNull(product);
			}
			throw e;
		}
	}

	@Test
	public void testNestedTransactionCommit() throws ConsistencyCheckException
	{
		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());
		Transaction.current().begin(); // 1 x begin -> 1 open
		assertTrue(Transaction.current().isRunning());
		assertEquals(1, Transaction.current().getOpenTransactionCount());

		final Country country = C2LManager.getInstance().createCountry("Thalerland");
		assertNotNull(country);
		assertNotNull(country);
		assertEquals(country, C2LManager.getInstance().getCountryByIsoCode("Thalerland"));

		for (int i = 0; i < 1000; i++) // 1000 x begin -> 1001 open
		{
			Transaction.current().begin();
		}

		assertEquals(1001, Transaction.current().getOpenTransactionCount());
		assertTrue(Transaction.current().isRunning());
		assertEquals(country, C2LManager.getInstance().getCountryByIsoCode("Thalerland"));

		for (int i = 0; i < 999; i++) // 999 x commit -> 2 open
		{
			Transaction.current().commit();
		}

		assertEquals(2, Transaction.current().getOpenTransactionCount());
		assertTrue(Transaction.current().isRunning());
		assertEquals(country, C2LManager.getInstance().getCountryByIsoCode("Thalerland"));

		Transaction.current().rollback(); // rollback on 2nd level -> mark rollback only, 1 open now

		assertEquals(1, Transaction.current().getOpenTransactionCount());
		assertTrue(Transaction.current().isRunning());
		assertTrue(Transaction.current().isRollbackOnly());
		assertEquals(country, C2LManager.getInstance().getCountryByIsoCode("Thalerland"));
		try
		{
			Transaction.current().commit(); // commit on first level -> error
			fail("exepcted TransactionException");
		}
		catch (final TransactionException e)
		{
			// fine - transaction has been rolled back
			assertTrue(e.getMessage().contains("rollback-only"));
			assertFalse(Transaction.current().isRollbackOnly());
			assertFalse(Transaction.current().isRunning());
		}

		assertFalse(Transaction.current().isRunning());
		assertEquals(0, Transaction.current().getOpenTransactionCount());

		try
		{
			C2LManager.getInstance().getCountryByIsoCode("Thalerland");
			fail("JaloItemNotFoundException exepcted");
		}
		catch (final JaloItemNotFoundException e)
		{
			// fine
		}
	}

	@Test
	public void testNestedTransactionRollback() throws ConsistencyCheckException
	{
		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());
		Transaction.current().begin();
		assertTrue(Transaction.current().isRunning());
		assertEquals(1, Transaction.current().getOpenTransactionCount());

		final Country country = C2LManager.getInstance().createCountry("Thalerland");
		assertNotNull(country);
		assertEquals(country, C2LManager.getInstance().getCountryByIsoCode("Thalerland"));

		Transaction.current().begin();

		assertTrue(Transaction.current().isRunning());
		assertEquals(2, Transaction.current().getOpenTransactionCount());

		Transaction.current().commit();

		assertEquals(1, Transaction.current().getOpenTransactionCount());
		assertTrue(Transaction.current().isRunning());
		assertEquals(country, C2LManager.getInstance().getCountryByIsoCode("Thalerland"));

		assertNotNull(country);

		Transaction.current().rollback();

		try
		{
			C2LManager.getInstance().getCountryByIsoCode("Thalerland");
			fail("JaloItemNotFoundException expected");
		}
		catch (final JaloItemNotFoundException e)
		{
			// OK
		}
		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());
	}

	@Test
	public void testNestedTransaction() throws ConsistencyCheckException
	{
		final Transaction tx = Transaction.current();
		assertTxState(tx, true, 0, false, false);

		// BEGIN -> 1
		tx.begin();
		assertTxState(tx, true, 1, true, false);

		// 	BEGIN -> 2
		tx.begin();
		assertTxState(tx, true, 2, true, false);

		// 	1 <- ROLLBACK + rollback-only
		tx.rollback();
		assertTxState(tx, true, 1, true, true);

		// 	BEGIN -> 2 + rollback-only
		tx.begin();
		assertTxState(tx, true, 2, true, true);

		// 	1 <- COMMIT + rollback-only
		tx.commit();
		assertTxState(tx, true, 1, true, true);

		// 0 <- ROLLBACK => no longer current !
		tx.rollback();
		assertTxState(tx, false, 0, false, false);

		final Transaction txNew = Transaction.current();
		assertNotSame(tx, txNew);

		try
		{
			txNew.commit();
			fail("TransactionException expected, because there was a commit without preceeding begin");
		}
		catch (final TransactionException e)
		{
			// OK
		}

		final Transaction txSame = Transaction.current();
		assertSame(txNew, txSame);

		try
		{
			txSame.rollback();
			fail("TransactionException expected, because there was a rollback without preceeding begin");
		}
		catch (final TransactionException e)
		{
			// OK
		}
	}

	private void assertTxState(final Transaction tx, final boolean isCurrent, final int openTxCount, final boolean running,
			final boolean rollbackOnly)
	{
		assertEquals(isCurrent, tx.isCurrent());
		assertEquals(openTxCount, tx.getOpenTransactionCount());
		assertEquals(running, tx.isRunning());
		assertEquals(rollbackOnly, tx.isRollbackOnly());
	}

	@Test
	public void testNestedTransactionExecute() throws Exception
	{
		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());

		Transaction.current().execute(new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				assertEquals(1, Transaction.current().getOpenTransactionCount());
				assertTrue(Transaction.current().isRunning());

				Transaction.current().execute(new TransactionBody()
				{
					@Override
					public Object execute()
					{
						assertEquals(2, Transaction.current().getOpenTransactionCount());
						assertTrue(Transaction.current().isRunning());
						return null;
					}
				});


				return null;
			}
		});

		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());
	}

	@Test
	public void testNestedTransactionWithCreate() throws Exception
	{
		for (int i = 0; i < 10; i++)
		{
			Transaction.current().begin();
			try
			{
				final MediaFormat format3 = MediaManager.getInstance().createMediaFormat("testFormat3_tx6");
				MediaManager.getInstance().createMedia("testMedia1WithFormat3_tx", format3);
			}
			finally
			{
				Transaction.current().rollback();
			}
		}
	}

	@Test
	public void testNestedTAError() throws Exception
	{

		final AtomicBoolean storeCalled = new AtomicBoolean();
		final AtomicBoolean commitCalled = new AtomicBoolean();
		final AtomicBoolean rollbackCalled = new AtomicBoolean();

		final Transaction transaction = new DefaultTransaction()
		{
			@Override
			public void rollback() throws TransactionException
			{
				rollbackCalled.set(true);
				super.rollback();
			}

			@Override
			public void commit() throws TransactionException
			{
				commitCalled.set(true);
				super.commit();
			}
		};
		transaction.enableDelayedStore(true);

		final EntityInstanceContext eCtx = new EntityInstanceContext()
		{

			@Override
			public ItemDeployment getItemDeployment()
			{
				return null;
			}

			@Override
			public PK getPK()
			{
				return PK.NULL_PK;
			}

			@Override
			public PersistencePool getPersistencePool()
			{
				return null;
			}

			@Override
			public void setPK(final PK pk)
			{
				// mock
			}
		};

		final EntityInstance mockEntity = new EntityInstance()
		{
			final EntityInstanceContext ctx = eCtx;

			@Override
			public PK ejbFindByPrimaryKey(final PK pkValue) throws YObjectNotFoundException
			{
				return null;
			}

			@Override
			public void ejbLoad()
			{
				// mock
			}

			@Override
			public void ejbRemove()
			{
				// mock
			}

			@Override
			public void ejbStore()
			{
				storeCalled.set(true);
				throw new IllegalArgumentException("let's rollback ;)");
			}

			@Override
			public EntityInstanceContext getEntityContext()
			{
				return ctx;
			}

			@Override
			public boolean needsStoring()
			{
				return true;
			}

			@Override
			public void setEntityContext(final EntityInstanceContext ctx)
			{
				// mock
			}

			@Override
			public void setNeedsStoring(final boolean needs)
			{
				// mock
			}

		};

		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final PrintStream printstream = new PrintStream(bos);

		final PrintStream err = System.err;

		final AtomicReference<Title> itemRef = new AtomicReference<Title>();

		try
		{
			System.setErr(printstream);

			// outer TA
			transaction.execute(new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					// inner TA
					transaction.execute(new TransactionBody()
					{
						@Override
						public Object execute() throws Exception
						{
							itemRef.set(UserManager.getInstance().createTitle("T" + System.currentTimeMillis()));

							// inject mock entity to call ejbStore upon -> throws exception
							transaction.registerEntityInstance(mockEntity);

							return null;
						}

					});
					return null;
				}

			});
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException ex)
		{
			assertTrue(storeCalled.get());
			assertEquals("let's rollback ;)", ex.getMessage());

			assertFalse(transaction.isRunning());
			assertEquals(0, transaction.getOpenTransactionCount());
			assertNotNull(itemRef.get());
			assertFalse(itemRef.get().isAlive());

			final String errorLog = new String(bos.toByteArray());

			assertFalse(errorLog.contains("no transaction running"));
		}
		catch (final Exception e)
		{
			fail("unexpected error " + e.getMessage());
		}
		finally
		{
			System.setErr(err);
		}
	}

	@Test
	public void testPermittedExceptions() throws Exception
	{
		final Title title = UserManager.getInstance().createTitle("t4tx");
		title.setName("foo");
		assertEquals("foo", title.getName());

		// test normal behavior
		try
		{
			Transaction.current().execute(new TransactionBody()
			{

				@Override
				public Object execute() throws Exception
				{
					title.setName("bar");
					assertEquals("bar", title.getName());
					throw new IllegalArgumentException();
				}
			});
			fail("IllegalArgumentException expected");
		}
		catch (final Exception e)
		{
			assertTrue(e instanceof IllegalArgumentException);
		}
		// change has been rolled back
		assertEquals("foo", title.getName());

		// test new behavior
		try
		{
			Transaction.current().execute(new TransactionBody()
			{

				@Override
				public Object execute() throws Exception
				{
					title.setName("bar");
					assertEquals("bar", title.getName());
					throw new IllegalArgumentException();
				}
			}, IllegalArgumentException.class, JaloSystemException.class);
			fail("IllegalArgumentException expected");
		}
		catch (final Exception e)
		{
			assertTrue(e instanceof IllegalArgumentException);
		}
		assertFalse("found transaction still running", Transaction.current().isRunning());
		// change has *not* been rolled back
		assertEquals("bar", title.getName());

		// test success case for new behavior too
		Transaction.current().execute(new TransactionBody()
		{

			@Override
			public Object execute() throws Exception
			{
				title.setName("blubb");
				assertEquals("blubb", title.getName());
				return null;
			}
		}, IllegalArgumentException.class, JaloSystemException.class);
		assertFalse("found transaction still running", Transaction.current().isRunning());
		// change has *not* been rolled back
		assertEquals("blubb", title.getName());
	}

	@Test
	public void testToException()
	{
		final ConsistencyCheckException businessExceptionSubClass = new ConsistencyCheckException("a", 0);
		final JaloBusinessException businessException = new JaloBusinessException("a", 0);
		final RuntimeException runtimeException = new RuntimeException();
		final Exception exception = new Exception();
		final Error error = new OutOfMemoryError();
		final Throwable throwable = new Throwable();

		//Business exception should be returned from the method
		{
			final JaloBusinessException result = Transaction.toException(businessException, JaloBusinessException.class);
			assertTrue(businessException == result);
		}

		//Subclass of business exception should be returned from the method (return type is the business exception)
		{
			final JaloBusinessException result = Transaction.toException(businessExceptionSubClass, JaloBusinessException.class);
			assertTrue(businessExceptionSubClass == result);
		}

		//This should not match and throw new RuntimeException
		{
			try
			{
				Transaction.toException(businessException, ConsistencyCheckException.class);
			}
			catch (final Throwable t)
			{
				assertEquals(RuntimeException.class, t.getClass());
				assertTrue(businessException == t.getCause());
			}
		}

		{
			try
			{
				Transaction.toException(runtimeException, JaloBusinessException.class);
			}
			catch (final Throwable t)
			{
				assertEquals(runtimeException, t);
			}
		}
		{
			try
			{
				Transaction.toException(exception, JaloBusinessException.class);
			}
			catch (final Throwable t)
			{
				assertEquals(RuntimeException.class, t.getClass());
				assertTrue(exception == t.getCause());
			}
		}
		{
			try
			{
				Transaction.toException(error, JaloBusinessException.class);
			}
			catch (final Throwable t)
			{
				assertEquals(error, t);
			}
		}
		{
			try
			{
				Transaction.toException(throwable, JaloBusinessException.class);
			}
			catch (final Throwable t)
			{
				assertEquals(RuntimeException.class, t.getClass());
				assertTrue(throwable == t.getCause());
			}
		}
	}

	// PLA-8819
	@Test
	public void testManualRollbackNoException() throws ConsistencyCheckException
	{
		final Transaction tx = Transaction.current();

		PK pk = null;

		tx.begin();

		final Title title = UserManager.getInstance().createTitle("Foo" + System.nanoTime());
		pk = title.getPK();
		assertTrue(title.isAlive());

		tx.rollback(); // no exception shall be thrown here !!!

		try
		{
			jaloSession.getItem(pk);
			fail("rollbacked title " + pk + " still alive after rollback!");
		}
		catch (final JaloItemNotFoundException e)
		{
			// fine
		}
	}

	// PLA-11600
	@Test
	public void testWrongOpenTxCountAfterErrorDuringRollback()
	{
		final ErrorInUnsetTxBoundConnectionTransactionDummy tx = new ErrorInUnsetTxBoundConnectionTransactionDummy();
		TestUtils.disableFileAnalyzer("expecting two exceptions from TransactionTest.testWrongOpenTxCountAfterErrorDuringRollback");
		try
		{
			tx.execute(new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					throw new IllegalStateException("break it!");
				}
			});
		}
		catch (final Exception e)
		{
			assertTrue(e instanceof IllegalStateException);
			assertEquals("break it!", e.getMessage());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
		assertFalse(tx.isRunning());
		assertEquals(0, tx.getOpenTransactionCount());

		assertNotSame(tx, Transaction.current());
	}

	private static class ErrorInUnsetTxBoundConnectionTransactionDummy extends DefaultTransaction
	{
		@Override
		protected void unsetTxBoundConnection()
		{
			throw new RuntimeException("foo");
		}

		@Override
		public boolean isCurrent()
		{
			// need to fake it since we do not want to become current one for real
			return true;
		}

		@Override
		protected void setAsCurrent()
		{
			// don't want that
		}
	}

	@Test
	public void testItemNotAliveAfterTransactionRollback()
	{
		final AtomicReference<Title> titleRef = new AtomicReference<>();
		try
		{
			Transaction.current().execute(new TransactionBody()
			{
				@Override
				public Object execute() throws Exception
				{
					final Title t = UserManager.getInstance().createTitle("RollbackTitle");
					assertTrue(t.isAlive());
					titleRef.set(t);
					throw new RuntimeException("abort please");
				}
			});
		}
		catch (final Exception e)
		{
			assertEquals("abort please", e.getMessage());
		}
		assertNotNull(titleRef.get());
		assertFalse(titleRef.get().isAlive());
		assertFalse(titleRef.get().isCacheBound());
	}

}
