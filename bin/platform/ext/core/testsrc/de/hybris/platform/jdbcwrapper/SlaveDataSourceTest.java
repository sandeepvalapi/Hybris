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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config.SystemSpecificParams;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@IntegrationTest
public class SlaveDataSourceTest extends HybrisJUnit4Test
{
	private static final Logger log = Logger.getLogger(SlaveDataSourceTest.class);

	private static final String SPECIAL_TENANT_ID = "spTen";
	private static final String SPECIAL_DS_CAN_CONNECT = "canconnect";
	private static final String SPECIAL_DS_CANNOT_CONNECT = "cannotconnect";

	@Before
	public void setUp()
	{
		// We have to clear forceMaster flag BEFORE each test
		// due to the fact that everything is run inside a single thread
		// and even cleanup after a test might cause this thread to
		// be marked as requiring master data source only!
		final Tenant t = Registry.getCurrentTenantNoFallback();
		if (t != null)
		{
			((AbstractTenant) t).cancelForceMasterMode();
		}

		assertSame(t.getDataSource(), t.getMasterDataSource());
	}

	@Test
	public void testSlaveDataSource()
	{
		final Tenant t = Registry.getCurrentTenant();
		if (t instanceof MasterTenant)
		{
			log.warn("cannot run SlaveDataSourceTest.testSlaveDataSource on master tenant");
			return;
		}
		if (t.getAllSlaveDataSources().isEmpty())
		{
			log.warn("cannot run SlaveDataSourceTest.testSelection on tenant without slave data sources");
			return;
		}

		try
		{
			assertFalse(t.isSlaveDataSource());

			final HybrisDataSource masterDs = t.getMasterDataSource();
			assertNotNull(masterDs);
			assertSame(masterDs, t.getDataSource());
			assertFalse(masterDs.isReadOnly());

			t.activateSlaveDataSource();
			final HybrisDataSource slaveDs = t.getDataSource();
			assertNotNull(slaveDs);
			assertNotSame(slaveDs, masterDs);
			assertTrue(slaveDs.isReadOnly());

			t.deactivateAlternativeDataSource();
			assertSame(masterDs, t.getDataSource());
		}
		finally
		{
			t.deactivateAlternativeDataSource();
		}
	}

	@Test
	public void testAlternativeDataSource()
	{
		final Tenant t = Registry.getCurrentTenant();
		if (t instanceof MasterTenant)
		{
			log.warn("cannot run SlaveDataSourceTest.testSlaveDataSource on master tenant");
			return;
		}
		if (t.getAllAlternativeMasterDataSources().isEmpty())
		{
			log.warn("cannot run testAlternativeDataSource on tenant without alternative data sources");
			return;
		}

		try
		{
			final HybrisDataSource masterDs = t.getMasterDataSource();
			assertNotNull(masterDs);
			assertSame(masterDs, t.getDataSource());
			assertFalse(masterDs.isReadOnly());

			final Set<String> alternativeDSIds = t.getAllAlternativeMasterDataSourceIDs();

			assertTrue(alternativeDSIds.containsAll(Arrays.asList("alt1", "alt2")));

			t.activateAlternativeMasterDataSource("alt1");
			final HybrisDataSource alt1Ds = t.getDataSource();
			assertNotNull(alt1Ds);
			assertNotSame(alt1Ds, masterDs);
			assertFalse(alt1Ds.isReadOnly());

			t.deactivateAlternativeDataSource();
			assertSame(masterDs, t.getDataSource());

			t.activateAlternativeMasterDataSource("alt2");
			final HybrisDataSource alt2Ds = t.getDataSource();
			assertNotNull(alt2Ds);
			assertNotSame(alt2Ds, masterDs);
			assertNotSame(alt2Ds, alt1Ds);
			assertFalse(alt2Ds.isReadOnly());

			// alterative data sources are MASTER ones, therefore any write must not deactivate them 
			final Unit u = ProductManager.getInstance().createUnit("foo", "bar");
			assertTrue(u.isAlive());

			// make sure we're still getting the alternative data source
			assertSame(alt2Ds, t.getDataSource());

			t.deactivateAlternativeDataSource();
			assertSame(masterDs, t.getDataSource());

		}
		finally
		{
			t.deactivateAlternativeDataSource();
		}
	}


	@Test
	public void testSlaveDSReadOnlyCheck() throws SQLException
	{
		final Tenant t = Registry.getCurrentTenant();
		if (t instanceof MasterTenant)
		{
			log.warn("cannot run SlaveDataSourceTest.testSlaveDataSource on master tenant");
			return;
		}
		final Set<String> slaveDataSourceIDs = t.getAllSlaveDataSourceIDs();
		if (slaveDataSourceIDs.isEmpty())
		{
			log.warn("cannot run SlaveDataSourceTest.testSelection on tenant without slave data sources");
			return;
		}

		assertFalse(t.isForceMaster());

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			final String id = t.activateSlaveDataSource();
			assertTrue(slaveDataSourceIDs.contains(id));
			final HybrisDataSource slaveDs = t.getDataSource();
			assertNotNull(slaveDs);
			assertTrue(slaveDs.isReadOnly());

			final String tablename = t.getConfig().getString("db.tableprefix", "") + "metainformations";

			// StatementImpl

			con = slaveDs.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + tablename);
			rs.close();
			rs = null;
			boolean gotRs = stmt.execute("SELECT * FROM " + tablename);
			assertTrue(gotRs);
			rs = stmt.getResultSet();
			assertNotNull(rs);
			rs.close();
			rs = null;
			try
			{
				stmt.executeUpdate("INSERT INTO " + tablename + " (SystemPK) VALUES ('foo')");
				fail("SQLException expected");
			}
			catch (final SQLException e)
			{
				// fine
				assertTrue(e.getMessage(), e.getMessage().contains("read-only"));
			}
			try
			{
				gotRs = stmt.execute("UPDATE " + tablename + " SET SystemPK='foo'");
				assertFalse(gotRs);
				fail("SQLException expected");
			}
			catch (final SQLException e)
			{
				// fine
				assertTrue(e.getMessage(), e.getMessage().contains("read-only"));
			}

			// PreparedStatementImpl

			//YTODO ...
		}
		finally
		{
			Utilities.tryToCloseJDBC(con, stmt, rs, true);
			t.deactivateAlternativeDataSource();
		}
	}

	@Test
	public void testAlternativeMasterWrite() throws SQLException
	{
		final Tenant t = Registry.getCurrentTenant();
		if (t instanceof MasterTenant)
		{
			log.warn("cannot run SlaveDataSourceTest.testAlternativeMasterWrite on master tenant");
			return;
		}
		if (t.getAllAlternativeMasterDataSources().isEmpty())
		{
			log.warn("cannot run SlaveDataSourceTest.testAlternativeMasterWrite on tenant without slave data sources");
			return;
		}

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			t.activateAlternativeMasterDataSource("alt1");
			final HybrisDataSource altDs = t.getDataSource();
			assertNotNull(altDs);
			assertFalse(altDs.isReadOnly());
			assertEquals("alt1", altDs.getID());
			assertNotSame(t.getMasterDataSource(), altDs);

			final String tablename = t.getConfig().getString("db.tableprefix", "") + "metainformations";


			// StatementImpl

			con = altDs.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + tablename);
			rs.close();
			rs = null;
			final boolean gotRs = stmt.execute("SELECT * FROM " + tablename);
			assertTrue(gotRs);
			rs = stmt.getResultSet();
			assertNotNull(rs);
			rs.close();
			rs = null;
			stmt.executeUpdate("CREATE TABLE xyz ( id varchar(20) , text varchar(20))");
			stmt.executeUpdate("INSERT INTO xyz (id ) VALUES ( 'juhu' ) ");
			assertFalse(stmt.execute("UPDATE xyz SET text='foo' WHERE id = 'juhu' "));
			stmt.executeUpdate("DELETE FROM xyz WHERE id='juhu'");
			stmt.executeUpdate("DROP TABLE xyz");
		}
		finally
		{
			Utilities.tryToCloseJDBC(con, stmt, rs, true);
			t.deactivateAlternativeDataSource();
		}
	}


	@Test
	public void testSelection() throws InterruptedException
	{
		final Tenant t = Registry.getCurrentTenant();

		if (t instanceof MasterTenant)
		{
			log.warn("cannot run SlaveDataSourceTest.testSelection on master tenant");
			return;
		}

		if (t.getAllSlaveDataSources().isEmpty())
		{
			log.warn("cannot run SlaveDataSourceTest.testSelection on tenant without slave data sources");
			return;
		}

		final String ids[] = new String[7];


		ids[0] = Registry.getCurrentTenant().getDataSource().getID(); // should be 'master'

		// get master in new thread
		Thread thr = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(t);
					ids[1] = t.getDataSource().getID();
				}
				finally
				{
					Registry.unsetCurrentTenant();
				}
			}
		};
		thr.start();
		thr.join();

		// get slave in new thread -> A
		thr = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(t);
					t.activateSlaveDataSource();
					ids[2] = t.getDataSource().getID();
				}
				finally
				{
					t.deactivateAlternativeDataSource();
					Registry.unsetCurrentTenant();
				}
			}
		};
		thr.start();
		thr.join();

		// get slave in new thread -> B
		thr = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(t);
					t.activateSlaveDataSource();
					ids[3] = t.getDataSource().getID();
				}
				finally
				{
					t.deactivateAlternativeDataSource();
					Registry.unsetCurrentTenant();
				}
			}
		};
		thr.start();
		thr.join();

		// get slave in new thread -> C
		thr = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(t);
					t.activateSlaveDataSource();
					ids[4] = t.getDataSource().getID();
				}
				finally
				{
					t.deactivateAlternativeDataSource();
					Registry.unsetCurrentTenant();
				}
			}
		};
		thr.start();
		thr.join();

		// get slave in new thread -> A
		thr = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(t);
					t.activateSlaveDataSource();
					ids[5] = t.getDataSource().getID();
				}
				finally
				{
					t.deactivateAlternativeDataSource();
					Registry.unsetCurrentTenant();
				}
			}
		};
		thr.start();
		thr.join();

		// get slave in new thread -> B
		thr = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(t);
					t.activateSlaveDataSource();
					ids[6] = t.getDataSource().getID();
				}
				finally
				{
					t.deactivateAlternativeDataSource();
					Registry.unsetCurrentTenant();
				}
			}
		};
		thr.start();
		thr.join();

		assertEquals("master", ids[0]);
		assertEquals("master", ids[1]);

		/*
		 * This is kind of tricky since we cannot be sure of the loading order of slave data source. That's why we just
		 * test a|b|c for the first 3 IDs (excluding the previous ones) and then test if they repeat correctly.
		 */
		final Set<String> allowed = new HashSet<String>(Arrays.asList("a", "b", "c", "cannotconnect1"));
		assertTrue(allowed.contains(ids[2]));
		assertTrue(!ids[3].equals(ids[2]) && allowed.contains(ids[3]));
		assertTrue(!ids[4].equals(ids[2]) && !ids[4].equals(ids[3]) && allowed.contains(ids[4]));
		assertEquals(ids[2], ids[5]); // must repeat due to round robin
		assertEquals(ids[3], ids[6]); // must repeat due to round robin
	}

	@Test
	public void testForceMasterAndIsSlaveDataSource() throws InterruptedException
	{
		final Tenant tenant = Registry.getCurrentTenant();
		if (tenant instanceof MasterTenant)
		{
			log.warn("cannot run SlaveDataSourceTest.testSlaveDataSource on master tenant");
			return;
		}
		if (tenant.getAllSlaveDataSources().isEmpty())
		{
			log.warn("cannot run SlaveDataSourceTest.testSelection on tenant without slave data sources");
			return;
		}

		final String ids[] = new String[30];

		//switch to a slave thread and test if slave
		Thread thread = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(tenant);
					tenant.activateSlaveDataSource();

					ids[0] = String.valueOf(tenant.isSlaveDataSource()); //should be true
					ids[1] = tenant.getDataSource().getID(); //not master
					ids[2] = String.valueOf(tenant.isForceMaster()); //false
					ids[3] = String.valueOf(tenant.getMasterDataSource().equals(tenant.getDataSource())); //false
				}
				finally
				{
					tenant.deactivateAlternativeDataSource();

					ids[4] = String.valueOf(tenant.isSlaveDataSource()); //false
					ids[5] = String.valueOf(tenant.isForceMaster()); //
					ids[6] = tenant.getDataSource().getID(); //master
					ids[7] = String.valueOf(tenant.getMasterDataSource().equals(tenant.getDataSource())); //true
					Registry.unsetCurrentTenant();
				}
			}
		};
		thread.start();
		thread.join();


		//switch to a slave thread, forceMaster and check if still slave
		thread = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(tenant);
					tenant.activateSlaveDataSource();
					tenant.forceMasterDataSource();

					ids[8] = String.valueOf(tenant.isSlaveDataSource()); //should be false
					ids[9] = tenant.getDataSource().getID(); //master
					ids[10] = String.valueOf(tenant.isForceMaster()); //true
					ids[11] = String.valueOf(tenant.getMasterDataSource().equals(tenant.getDataSource())); //true
				}
				finally
				{
					tenant.deactivateAlternativeDataSource();

					ids[12] = String.valueOf(tenant.isSlaveDataSource()); //false
					ids[13] = String.valueOf(tenant.isForceMaster()); //
					ids[14] = tenant.getDataSource().getID(); //master
					ids[15] = String.valueOf(tenant.getMasterDataSource().equals(tenant.getDataSource())); //true
					Registry.unsetCurrentTenant();
				}
			}
		};
		thread.start();
		thread.join();

		//switch to a slave thread, forceMaster, switch back to master and switch again to same slave
		thread = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(tenant);
					final String id = tenant.activateSlaveDataSource();
					tenant.forceMasterDataSource();
					tenant.deactivateAlternativeDataSource();
					tenant.activateSlaveDataSource(id);

					ids[16] = String.valueOf(tenant.isSlaveDataSource()); //should be true
					ids[17] = tenant.getDataSource().getID(); //not master
					ids[18] = String.valueOf(tenant.isForceMaster()); //false
					ids[19] = String.valueOf(tenant.getMasterDataSource().equals(tenant.getDataSource())); //false

					tenant.deactivateAlternativeDataSource();
					tenant.activateSlaveDataSource(); //sould be another slave which has no force master
					ids[20] = String.valueOf(tenant.isSlaveDataSource()); //true
					ids[21] = String.valueOf(tenant.isForceMaster()); //false
					ids[22] = String.valueOf(id.equals(tenant.getDataSource().getID())); //false
				}
				finally
				{
					tenant.deactivateAlternativeDataSource();
					Registry.unsetCurrentTenant();
				}
			}
		};
		thread.start();
		thread.join();

		thread = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				try
				{
					Registry.setCurrentTenant(tenant);
					tenant.forceMasterDataSource();
					ids[23] = String.valueOf(tenant.isSlaveDataSource()); //false
					ids[24] = tenant.getDataSource().getID(); //master
					ids[25] = String.valueOf(tenant.isForceMaster()); //true

				}
				finally
				{
					tenant.deactivateAlternativeDataSource();
					Registry.unsetCurrentTenant();
				}
			}
		};
		thread.start();
		thread.join();

		assertEquals("true", ids[0]);
		assertNotSame("master", ids[1]);
		assertEquals("false", ids[2]);
		assertEquals("false", ids[3]);
		assertEquals("false", ids[4]);
		assertEquals("false", ids[5]);
		assertEquals("master", ids[6]);
		assertEquals("true", ids[7]);

		assertEquals("false", ids[8]);
		assertEquals("master", ids[9]);
		assertEquals("true", ids[10]);
		assertEquals("true", ids[11]);
		assertEquals("false", ids[12]);
		assertEquals("false", ids[13]);
		assertEquals("master", ids[14]);
		assertEquals("true", ids[15]);

		assertEquals("true", ids[16]);
		assertNotSame("master", ids[17]);
		assertEquals("false", ids[18]);
		assertEquals("false", ids[19]);
		assertEquals("true", ids[20]);
		assertEquals("false", ids[21]);
		assertEquals("false", ids[22]);

		assertEquals("false", ids[23]);
		assertEquals("master", ids[24]);
		assertEquals("true", ids[25]);
	}

	//	@Test
	//	public void testForceMasterWithTwoThreads()
	//	{
	//		final Tenant tenant = Registry.getCurrentTenant();
	//
	//		if (tenant instanceof MasterTenant)
	//		{
	//			log.warn("cannot run SlaveDataSourceTest.testSelection on master tenant");
	//			return;
	//		}
	//
	//
	//
	//		final Object waitobj = new Object();
	//		final String id[] = new String[1];
	//
	//		final Thread thread1 = new Thread()
	//		{
	//			@Override
	//			public void run()
	//			{
	//				Registry.setCurrentTenant(tenant);
	//				id[0] = tenant.activateSlaveDataSource();
	//
	//			}
	//		};
	//
	//		final Thread thread2 = new Thread()
	//		{
	//			@Override
	//			public void run()
	//			{
	//
	//			}
	//		};
	//
	//		thread1.start();
	//		thread2.start();
	//		thread1.join();
	//		thread2.join();
	//	}


	@Test
	public void testSwitchOnTxBegin() throws InterruptedException
	{
		final Tenant tenant = Registry.getCurrentTenant();
		if (tenant instanceof MasterTenant)
		{
			log.warn("cannot run SlaveDataSourceTest.testSlaveDataSource on master tenant");
			return;
		}
		if (tenant.getAllSlaveDataSources().isEmpty())
		{
			log.warn("cannot run SlaveDataSourceTest.testSelection on tenant without slave data sources");
			return;
		}

		assertNull(ProductManager.getInstance().getUnit("foo", "bar"));

		final TestThread tt = new TestThread()
		{
			@Override
			void doRun()
			{
				getTenant().activateSlaveDataSource();
				assertTrue(getTenant().isSlaveDataSource());

				final Transaction tx = Transaction.current();
				tx.begin();
				try
				{
					assertFalse(getTenant().isSlaveDataSource());
					assertTrue(getTenant().isForceMaster());

					// try to store something -> should work on master but fail on slave
					final Unit u = ProductManager.getInstance().createUnit("foo", "bar");

					assertTrue(u.isAlive());
				}
				finally
				{
					tx.rollback();
				}
				assertFalse(getTenant().isSlaveDataSource());
				assertTrue(getTenant().isForceMaster());
			}
		};

		tt.start();
		tt.join();
		tt.assertNoError();

		// check if rollback is working
		assertNull(ProductManager.getInstance().getUnit("foo", "bar"));
	}

	@Test
	public void testSwitchOnHJMPStore() throws InterruptedException
	{
		final Tenant tenant = Registry.getCurrentTenant();
		if (tenant instanceof MasterTenant)
		{
			log.warn("cannot run SlaveDataSourceTest.testSlaveDataSource on master tenant");
			return;
		}
		if (tenant.getAllSlaveDataSources().isEmpty())
		{
			log.warn("cannot run SlaveDataSourceTest.testSelection on tenant without slave data sources");
			return;
		}

		assertNull(ProductManager.getInstance().getUnit("foo", "bar"));

		TestThread tt = new TestThread()
		{
			@Override
			void doRun()
			{
				getTenant().activateSlaveDataSource();
				assertTrue(getTenant().isSlaveDataSource());
				assertFalse(getTenant().isForceMaster());

				// try to insert something -> should switch data source back to master
				final Unit u = ProductManager.getInstance().createUnit("foo", "bar");

				assertFalse(getTenant().isSlaveDataSource());
				assertTrue(getTenant().isForceMaster());

				assertTrue(u.isAlive());
			}
		};
		tt.start();
		tt.join();
		tt.assertNoError();

		// check if this really worked out
		Unit u = ProductManager.getInstance().getUnit("foo", "bar");
		assertNotNull(u);
		assertEquals("foo", u.getUnitType());
		assertEquals("bar", u.getCode());

		tt = new TestThread()
		{
			@Override
			void doRun()
			{
				getTenant().activateSlaveDataSource();
				// slave ?
				assertTrue(getTenant().isSlaveDataSource());
				assertFalse(getTenant().isForceMaster());

				final Unit u = ProductManager.getInstance().getUnit("foo", "bar");
				assertNotNull(u);
				assertTrue(u.isAlive());

				// still slave ?
				assertTrue(getTenant().isSlaveDataSource());
				assertFalse(getTenant().isForceMaster());

				u.setUnitType("foo2");

				// now we're master again
				assertFalse(getTenant().isSlaveDataSource());
				assertTrue(getTenant().isForceMaster());

				assertTrue(u.isAlive());
			}
		};
		tt.start();
		tt.join();
		tt.assertNoError();

		u = ProductManager.getInstance().getUnit("foo2", "bar");
		assertNotNull(u);
		assertEquals("foo2", u.getUnitType());
		assertEquals("bar", u.getCode());

		tt = new TestThread()
		{
			@Override
			void doRun() throws ConsistencyCheckException
			{
				getTenant().activateSlaveDataSource();
				// slave ?
				assertTrue(getTenant().isSlaveDataSource());
				assertFalse(getTenant().isForceMaster());

				final Unit u = ProductManager.getInstance().getUnit("foo2", "bar");
				assertNotNull(u);
				assertTrue(u.isAlive());

				// still slave ?
				assertTrue(getTenant().isSlaveDataSource());
				assertFalse(getTenant().isForceMaster());

				u.remove();

				// now we're master again
				assertFalse(getTenant().isSlaveDataSource());
				assertTrue(getTenant().isForceMaster());

				assertFalse(u.isAlive());
			}
		};
		tt.start();
		tt.join();
		tt.assertNoError();

		//		assertNull(ProductManager.getInstance().getUnit("foo2", "bar"));
		//		assertNull(ProductManager.getInstance().getUnit("foo", "bar"));
	}

	private static class TestThread extends RegistrableThread
	{
		private final Tenant t;

		private volatile Throwable error;

		TestThread()
		{
			super();
			this.t = Registry.getCurrentTenant();
		}

		Tenant getTenant()
		{
			return t;
		}

		void doFinally()
		{
			// nothing yet
		}

		void assertNoError()
		{
			if (error != null)
			{
				error.printStackTrace();
				fail("unexpected error " + error.getMessage());
			}
		}

		void doRun() throws Exception
		{
			// nothing yet
		}

		@Override
		public final void internalRun()
		{
			try
			{
				Registry.setCurrentTenant(t);
				doRun();
			}
			catch (final Throwable t)
			{
				this.error = t;
			}
			finally
			{
				doFinally();
				t.deactivateAlternativeDataSource();
				Registry.unsetCurrentTenant();
			}
		}
	}

	@Ignore("tenant list is immutable")
	@Test
	public void testCannotConnectToSlave() throws Exception
	{
		final Tenant testTenant = Registry.getCurrentTenantNoFallback();
		try
		{
			doTestCannotConnectToSlave();
		}
		finally
		{
			// switch back tenant
			Registry.setCurrentTenant(testTenant);

			final MasterTenant mt = MasterTenant.getInstance();
			if (mt.getSlaveTenant(SPECIAL_TENANT_ID) != null)
			{
				mt.removeSlaveTenant(SPECIAL_TENANT_ID);
			}
		}
	}

	private void doTestCannotConnectToSlave() throws Exception
	{
		Tenant t = Registry.getCurrentTenant();
		if (t instanceof MasterTenant)
		{
			log.warn("cannot run SlaveDataSourceTest.testSlaveDataSource on master tenant");
			return;
		}
		if (t.getAllSlaveDataSources().isEmpty())
		{
			log.warn("cannot run SlaveDataSourceTest.testSelection on tenant without slave data sources");
			return;
		}
		createAndInitSpecialTestTenant();

		t = Registry.getCurrentTenant();

		// make sure that we're not stuck in forceMaster mode !
		((AbstractTenant) t).cancelForceMasterMode();

		assertEquals(2, t.getAllSlaveDataSources().size());

		final Set<String> slaveIDs = new HashSet<String>();
		String currentID = null;
		do
		{
			if (currentID != null)
			{
				slaveIDs.add(currentID);
			}
			try
			{
				t.activateSlaveDataSource();

				assertTrue(t.isSlaveDataSource());
				final HybrisDataSource ds = t.getDataSource();
				assertNotSame(t.getMasterDataSource(), ds);
				currentID = ds.getID();
				assertNotSame(SPECIAL_DS_CANNOT_CONNECT, currentID);

			}
			finally
			{
				t.deactivateAlternativeDataSource();
			}
		}
		while (!slaveIDs.contains(currentID));

		assertTrue(t.getAllSlaveDataSourceIDs().containsAll(slaveIDs));

		try
		{
			t.activateSlaveDataSource(SPECIAL_DS_CANNOT_CONNECT);
			fail("IllegalStateException expected due to data source connection error");
		}
		catch (final IllegalStateException e)
		{
			//ok here
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail("unexpected error " + e.getMessage()); // every other exception is wrong
		}
		finally
		{
			t.deactivateAlternativeDataSource();
		}

		try
		{
			t.activateSlaveDataSource("doesntExistDS"); //does not exist
			fail("IllegalArgumentException expected due to non-existent data source");
		}
		catch (final IllegalArgumentException e)
		{
			//ok here
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail("unexpected error " + e.getMessage()); // every other exception is wrong
		}
		finally
		{
			t.deactivateAlternativeDataSource();
		}
	}

	private void createAndInitSpecialTestTenant() throws Exception
	{
		final MasterTenant mt = MasterTenant.getInstance();

		if (mt.getSlaveTenant(SPECIAL_TENANT_ID) != null)
		{
			mt.removeSlaveTenant(SPECIAL_TENANT_ID);
		}

		final ConfigIntf cfg = mt.getConfig();
		final String dbUrl = cfg.getParameter(SystemSpecificParams.DB_URL);
		final String driver = cfg.getParameter(SystemSpecificParams.DB_DRIVER);
		final String fromJNDI = cfg.getString(SystemSpecificParams.DB_POOL_FROMJNDI, null);
		final String dbuser = cfg.getParameter(SystemSpecificParams.DB_USERNAME);
		final String dbpass = cfg.getParameter(SystemSpecificParams.DB_PASSWORD);

		final Map<String, String> params = new HashMap<String, String>();

		params.put("tenant__id", SPECIAL_TENANT_ID);

		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CAN_CONNECT + "." + SystemSpecificParams.DB_DRIVER, driver);
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CAN_CONNECT + "." + SystemSpecificParams.DB_POOL_FROMJNDI,
				fromJNDI);
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CAN_CONNECT + "." + SystemSpecificParams.DB_URL, dbUrl);
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CAN_CONNECT + "." + SystemSpecificParams.DB_TABLEPREFIX,
				SPECIAL_TENANT_ID + "_");
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CAN_CONNECT + "." + SystemSpecificParams.DB_USERNAME, dbuser);
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CAN_CONNECT + "." + SystemSpecificParams.DB_PASSWORD, dbpass);


		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CANNOT_CONNECT + "." + SystemSpecificParams.DB_DRIVER, driver);
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CANNOT_CONNECT + "." + SystemSpecificParams.DB_POOL_FROMJNDI,
				fromJNDI);
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CANNOT_CONNECT + "." + SystemSpecificParams.DB_URL, dbUrl);
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CANNOT_CONNECT + "." + SystemSpecificParams.DB_TABLEPREFIX,
				SPECIAL_TENANT_ID + "_");
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CANNOT_CONNECT + "." + SystemSpecificParams.DB_USERNAME,
				"wrong");
		params.put(AbstractTenant.SLAVE_DATASOURCE + "." + SPECIAL_DS_CANNOT_CONNECT + "." + SystemSpecificParams.DB_PASSWORD,
				"wrong");


		// switch off cronjob timer
		params.put("cronjob.timertask.loadonstartup", "false");
		// switch off task service
		params.put("task.excluded.cluster.ids", Integer.toString(Registry.getMasterTenant().getClusterID()));

		final SlaveTenant st = mt.createSlaveTenant(SPECIAL_TENANT_ID, dbUrl, dbuser, dbpass, driver, SPECIAL_TENANT_ID + "_",
				fromJNDI, null, null);

		// store settings
		st.changeSystemSpecificProps(params);


		Registry.activateMasterTenant();

		if (Utilities.isSystemInitialized(Registry.getMasterTenant().getDataSource()))
		{
			if (Utilities.getTenantID(Registry.getMasterTenant().getDataSource()).equals(st.getTenantID()))
			{
				log.info("ok -- do nothing special here");
			}
			else
			{
				Registry.setCurrentTenant(st);
			}
		}

		final Map props = new HashMap();
		props.put(Constants.Initialization.SYSTEM_NAME, SPECIAL_TENANT_ID + "_");
		SystemEJB.getInstance().setLocked(false);
		Initialization.initialize(props, null);
		new CoreBasicDataCreator().createSupportedEncodings();
		if (log.isInfoEnabled())
		{
			log.info(SPECIAL_TENANT_ID + " done initialization");
		}
	}
}
