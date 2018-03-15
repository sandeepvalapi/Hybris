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

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Tests automatic closing of statements and result sets when closing the associated connection or statement.
 */
@IntegrationTest
public class PreparedStatementImplTest extends HybrisJUnit4Test // make sure it's *not* transactional
{
	private final static Logger LOG = Logger.getLogger(PreparedStatementImplTest.class);

	@Test
	public void testAutoClose()
	{
		Connection conn = null;

		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;

		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;

		PreparedStatement pstmt3 = null;
		ResultSet rs3 = null;

		Statement stmt = null;
		ResultSet rs4 = null;

		final Tenant tenant = jaloSession.getTenant();
		final HybrisDataSource ds = tenant.getDataSource();
		final int activeBefore = ds.getConnectionPool().getNumActive();
		final String tablename = tenant.getConfig().getString("db.tableprefix", "") + "metainformations";

		try
		{
			// connection
			conn = ds.getConnection();
			// statememt 1
			pstmt1 = conn.prepareStatement("SELECT SystemPK FROM " + tablename + " WHERE SystemPK IS NOT NULL");
			rs1 = pstmt1.executeQuery();
			if (rs1.next())
			{
				LOG.info("QUERY 1: " + rs1.getString(1));
			}
			// statement 2
			pstmt2 = conn.prepareStatement("SELECT count(SystemPK ) FROM " + tablename + "  WHERE SystemPK > ?");
			pstmt2.setString(1, "1");
			rs2 = pstmt2.executeQuery();
			if (rs2.next())
			{
				LOG.info("QUERY 2: " + rs2.getString(1));
			}
			// statement 3
			try
			{
				pstmt3 = conn.prepareStatement("BAD STATEMENT SELECT count(SystemPK ) FROM " + tablename + "  WHERE SystemPK > ?");
				pstmt2.setString(1, "1");
				rs3 = pstmt3.executeQuery();
				if (rs3.next())
				{
					LOG.info("QUERY 3: " + rs3.getString(1));
				}
				fail("SQLException expected");
			}
			catch (final SQLException e)
			{
				//intension there should be sql with error
			}
			// statement 4
			stmt = conn.createStatement();
			rs4 = stmt.executeQuery("SELECT count(SystemPK ) FROM " + tablename + "  WHERE SystemPK > '0'");
			if (rs4.next())
			{
				LOG.info("QUERY 4: " + rs4.getString(1));
			}

			// close connection is automatically closing all statements and result sets
			conn.close();

			LOG.info("connections active before : " + activeBefore + ", currently : " + ds.getConnectionPool().getNumActive());
			// it is naive to check the number of active connection , what it if in mean time  e.g. TaskService runs its logic
			// assertEquals(activeBefore, ds.getConnectionPool().getNumActive());

			assertTrue(rs4.isClosed());
			rs4 = null;
			assertTrue(stmt.isClosed());
			stmt = null;

			if (rs3 != null)
			{
				assertTrue(rs3.isClosed());
				rs3 = null;
			}
			if (pstmt3 != null)
			{
				assertTrue(pstmt3.isClosed());
				pstmt3 = null;
			}

			assertTrue(rs2.isClosed());
			rs2 = null;
			assertTrue(pstmt2.isClosed());
			pstmt2 = null;

			assertTrue(rs1.isClosed());
			rs1 = null;
			assertTrue(pstmt1.isClosed());
			pstmt1 = null;

			conn = null;
		}
		catch (final SQLException e)
		{
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		finally
		{
			tryToClose(stmt, rs4);
			tryToClose(pstmt3, rs3);
			tryToClose(pstmt2, rs2);
			tryToClose(pstmt1, rs1);
			tryToClose(conn);
		}
	}

	private void tryToClose(final Statement pstmt, final ResultSet rs)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (final SQLException e)
			{
				// cant help it
			}
		}
		if (pstmt != null)
		{
			try
			{
				pstmt.close();
			}
			catch (final SQLException e)
			{
				// cant help it
			}
		}
	}

	private void tryToClose(final Connection conn)
	{
		if (conn != null)
		{
			try
			{
				conn.close();
			}
			catch (final SQLException e)
			{
				// cant help it
			}
		}
	}
}
