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

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.DataSourceFactory;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Config.SystemSpecificParams;
import de.hybris.platform.util.Utilities;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class DataSourceFactoryTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(DataSourceFactoryTest.class.getName());

	@Test
	public void testJUnitTenantSetup() throws SQLException
	{
		final Tenant t = Registry.getCurrentTenant();
		if (!(t instanceof SlaveTenant) || !"junit".equalsIgnoreCase(t.getTenantID()))
		{
			LOG.warn("cannot run DataSourceFactoryTest.testJUnitTenantSetup since current tenant <> junit!");
			return;
		}
		final DataSourceFactory factory = t.getDataSource().getDataSourceFactory();
		assertNotNull(factory);
		assertTrue(factory instanceof JUnitDataSourceFactory);

		final HybrisDataSource ds = t.getDataSource();
		assertTrue(ds.getConnectionPool() instanceof JUnitConnectionErrorCheckingJDBCConnectionPool);

		Connection con = null;
		PreparedStatement stmt = null;
		try
		{
			con = t.getDataSource().getConnection();
			assertTrue(con instanceof JUnitConnectionImpl);

			stmt = con.prepareStatement("SELECT * FROM junit_metainformations");
			assertTrue(stmt instanceof JUnitPreparedStatementImpl);
		}
		finally
		{
			Utilities.tryToCloseJDBC(con, stmt, null, true);
		}
	}

	@Test
	public void testConnectionTestedAfterPrepareError() throws SQLException
	{
		final Tenant t = Registry.getCurrentTenant();
		if (!(t instanceof SlaveTenant) || !"junit".equalsIgnoreCase(t.getTenantID()))
		{
			LOG.warn("cannot run DataSourceFactoryTest.testJUnitTenantSetup since current tenant <> junit!");
			return;
		}

		final DataSourceFactory factory = t.getDataSource().getDataSourceFactory();
		assertNotNull(factory);
		assertTrue(factory instanceof JUnitDataSourceFactory);

		final HybrisDataSource ds = t.getDataSource();
		assertTrue(ds.getConnectionPool() instanceof JUnitConnectionErrorCheckingJDBCConnectionPool);

		//
		// 1. test error on destroy
		//
		Connection con = null;
		PreparedStatement stmt = null;
		try
		{
			con = t.getDataSource().getConnection();
			assertTrue(con instanceof JUnitConnectionImpl);

			// prepare 'magic' query which causes a SQLExeption
			stmt = con.prepareStatement(JUnitConnectionImpl.PREPARE_ERROR_QUERY);

			fail("SQLException expected");
		}
		catch (final SQLException e)
		{
			// after error the connection MUST have the error flag set --> otherwise it would NOT be tested !!!
			assertTrue("connection should have error flag set",
					((JUnitConnectionErrorCheckingJDBCConnectionPool) ds.getConnectionPool()).mustValidate(con));

			// mark connection as 'broken' --> this destroys it on return (need to simulate it since the underlying connection is still valid )
			((JUnitConnectionErrorCheckingJDBCConnectionPool) ds.getConnectionPool()).addFailingConnection(con);
		}
		finally
		{
			Utilities.tryToCloseJDBC(con, stmt, null, true);
			((JUnitConnectionErrorCheckingJDBCConnectionPool) ds.getConnectionPool()).resetTestMode();
		}
		assertTrue("connection hasn't been destroyed despite", ((JUnitConnectionImpl) con).hasBeenDestroyed());

		//
		// 2. test that we don't get broken connection afterwards
		//
		Connection con2 = null;
		try
		{
			con2 = t.getDataSource().getConnection();
			assertNotSame("got broken connection", con, con2);
		}
		finally
		{
			Utilities.tryToCloseJDBC(con2, stmt, null, true);
		}
	}

	@Test
	public void testPoolShutdownAfterError()
	{
		final Tenant t = Registry.getCurrentTenantNoFallback();
		final Map<String, String> params = new HashMap<String, String>(t.getMasterDataSource().getConnectionParameters());
		// make it fail on connect by messing up the user name
		params.put(SystemSpecificParams.DB_USERNAME, "FooDosntExist");

		final DataSourceFactory dataSourceFactory = t.getMasterDataSource().getDataSourceFactory();

		final AtomicReference<WeakReference<JDBCConnectionPool>> poolRef = new AtomicReference<WeakReference<JDBCConnectionPool>>();

		final DataSourceFactory f = new DataSourceFactory()
		{

			@Override
			public HybrisDataSource createJNDIDataSource(final String id, final Tenant tenant, final String jndiName,
					final boolean readOnly)
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public HybrisDataSource createDataSource(final String id, final Tenant tenant,
					final Map<String, String> connectionParams, final boolean readOnly)
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public JDBCConnectionPool createConnectionPool(final HybrisDataSource dataSource,
					final GenericObjectPoolConfig poolConfig)
			{
				final JDBCConnectionPool ret = dataSourceFactory.createConnectionPool(dataSource, poolConfig);
				poolRef.set(new WeakReference<JDBCConnectionPool>(ret));
				return ret;
			}

			@Override
			public Connection wrapConnection(final HybrisDataSource wrappedDataSource, final Connection rawConnection)
			{
				return dataSourceFactory.wrapConnection(wrappedDataSource, rawConnection);
			}

			@Override
			public Statement wrapStatement(final Connection wrappedConnection, final Statement rawStatement)
			{
				return dataSourceFactory.wrapStatement(wrappedConnection, rawStatement);
			}

			@Override
			public PreparedStatement wrapPreparedStatement(final Connection wrappedConnection, final PreparedStatement rawStatement,
					final String query)
			{
				return dataSourceFactory.wrapPreparedStatement(wrappedConnection, rawStatement, query);
			}

			@Override
			public ResultSet wrapResultSet(final Statement wrappedStatement, final ResultSet rawResultSet)
			{
				return dataSourceFactory.wrapResultSet(wrappedStatement, rawResultSet);
			}
		};

		HybrisDataSource ds = null;
		try
		{
			TestUtils.disableFileAnalyzer("DataSource creation should throw exception");
			ds = new DataSourceImpl(t, "DummyDS", params, false, f);
		}
		catch (final Exception e)
		{
			// fine so far - now check how the pool behaves
			assertPoolIsShutDown(poolRef.get(), 30);
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
		if (ds != null)
		{
			// in case data source creation did not fail as expected we must clean up for sure
			ds.destroy();
			fail("data source creation was supposed to fail but did not");
		}
	}

	private void assertPoolIsShutDown(final WeakReference<JDBCConnectionPool> ref, final int waitSeconds)
	{
		assertNotNull(ref);
		for (int i = 0; i < waitSeconds && ref.get() != null; i++)
		{
			TestUtils.forceGC();
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e1)
			{
				Thread.currentThread().interrupt();
				break;
			}
			System.out.print('.');
		}
		System.out.println();
		final JDBCConnectionPool p = ref.get();
		assertTrue("pool is still alive but should not be p:" + p, p == null || p.isPoolClosed());
	}

}
