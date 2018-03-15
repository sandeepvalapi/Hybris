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
package de.hybris.platform.cronjob.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.jdbcwrapper.JDBCConnectionPool;
import de.hybris.platform.jdbcwrapper.JUnitJDBCConnectionPool;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Utilities;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;


public class DatabaseReconnectWithoutRestartTest extends HybrisJUnit4Test //NOPMD
{
	private static final Logger LOG = Logger.getLogger(DatabaseReconnectWithoutRestartTest.class);

	private Tenant tenantBefore;

	@Before
	public void prepare()
	{
		tenantBefore = Registry.getCurrentTenantNoFallback();
		Registry.setCurrentTenant(Registry.getTenantByID("foo"));
	}

	@After
	public void unprepare()
	{
		Registry.unsetCurrentTenant();
		if (tenantBefore != null)
		{
			Registry.setCurrentTenant(tenantBefore);
		}
	}


	@Test
	@Ignore("HORST-142")
	public void testReconnectDuringCronjobWithoutRestart() throws Exception
	{
		final AbstractTenant tenant = Registry.getCurrentTenantNoFallback();
		assertEquals("foo", tenant.getTenantID());
		final JDBCConnectionPool pool = tenant.getDataSource().getConnectionPool();

		assertTrue("expected JUnitJDBCConnectionPool but got ," + pool, pool instanceof JUnitJDBCConnectionPool);

		TestUtils.disableFileAnalyzer("Setting all connection to fail on purpose ....");
		try
		{
			testSetAllConnectionFail(tenant);
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	private void testSetAllConnectionFail(final AbstractTenant tenant) throws SQLException
	{
		final HybrisDataSource dataSource = tenant.getDataSource();
		final JDBCConnectionPool pool = dataSource.getConnectionPool();
		Connection connectionBefore = null;
		Connection connectionAfter = null;
		try
		{
			final int activeBefore = pool.getNumActive();
			connectionBefore = dataSource.getConnection();
			assertFalse(tenant.cannotConnect());
			assertFalse(tenant.connectionHasBeenBroken());

			final int activeAfter = pool.getNumActive();
			assertTrue(activeBefore + " < " + activeAfter, activeBefore < pool.getNumActive());

			((JUnitJDBCConnectionPool) pool).setAllConnectionsFail(true);

			connectionBefore.close(); // return to pool

			assertEquals(activeBefore, pool.getNumActive());
			assertTrue(tenant.cannotConnect());
			assertTrue(tenant.connectionHasBeenBroken());

			try
			{
				dataSource.getConnection();
				fail();
			}
			catch (final Exception e)
			{
				//OK
			}
			((JUnitJDBCConnectionPool) pool).setAllConnectionsFail(false);

			connectionAfter = dataSource.getConnection();

			assertFalse(connectionBefore.equals(connectionAfter));

			assertFalse(tenant.cannotConnect());
			assertFalse(tenant.connectionHasBeenBroken());
		}
		finally
		{
			Utilities.tryToCloseJDBC(connectionBefore, null, null, true);
			Utilities.tryToCloseJDBC(connectionAfter, null, null, true);
			((JUnitJDBCConnectionPool) pool).resetTestMode();
		}
	}
}
