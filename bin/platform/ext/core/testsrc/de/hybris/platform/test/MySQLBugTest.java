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
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.media.MediaFormat;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;


@IntegrationTest
public class MySQLBugTest extends HybrisJUnit4Test
{

	@Test
	public void testSingleCreateInTXWithRollback()
	{
		MediaFormat format1 = null;
		final Transaction tx = Transaction.current();
		tx.begin();
		tx.enableDelayedStore(false);
		try
		{
			format1 = MediaManager.getInstance().createMediaFormat("testFormat1");
		}
		finally
		{
			tx.rollback();
			assertFalse("item is alive after rollback!!", format1.isAlive());
		}
	}

	@Test
	public void testDuplicateCreateInTXWithRollback()
	{
		MediaFormat format1 = null;
		final Transaction tx = Transaction.current();
		tx.begin();
		tx.enableDelayedStore(false);
		try
		{
			format1 = MediaManager.getInstance().createMediaFormat("testFormat1");
			MediaManager.getInstance().createMediaFormat("testFormat1");
		}
		catch (final Exception e)
		{
			//meek if exception it's ok
		}
		finally
		{
			tx.rollback();
			assertFalse("item is alive after rollback!!", format1.isAlive());
		}
	}


	/*
	 * test deactivated, but this test can show
	 */
	public void NOtestBugWithSQL()
	{
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = Registry.getCurrentTenant().getDataSource().getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			stmt.execute("INSERT INTO junit_MediaFormat (PK,typepkstring,createdTS,p_qualifier) VALUES (11,1,'98-12-31 11:30:45','11')");
			stmt.close();
			stmt = null;
			stmt = conn.createStatement();
			stmt.execute("INSERT INTO junit_MediaFormat (PK,typepkstring,createdTS,p_qualifier) VALUES (11,1,'98-12-31 11:30:45','12')");
			stmt.close();
			stmt = null;

		}
		catch (final Exception e)
		{
			System.out.println("exception:" + e.getMessage());
		}
		finally
		{
			Utilities.tryToCloseJDBC(null, stmt, null);
			try
			{
				ResultSet rs = null;

				System.out.println(conn.hashCode());
				conn.rollback();

				//ACTIVATE THESE LINES TO MAKE IT WORK 
				//((ConnectionImpl)conn).getUnderlayingConnection().close();
				//Registry.getCurrentTenant().getDataSource().invalidate( (ConnectionImpl)conn );
				//conn=null;
				//conn = Registry.getCurrentTenant().getDataSource().getConnection();
				//

				System.out.println(conn.hashCode());
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT COUNT(*) FROM junit_MediaFormat WHERE PK=11 OR PK=12");
				rs.next();
				final int cnt = rs.getInt(1);
				rs.close();
				rs = null;
				stmt.close();
				stmt = null;
				assertEquals("item still alive after rollback!", 0, cnt);
			}
			catch (final SQLException e)
			{
				// DOCTODO Document reason, why this block is empty
			}
			finally
			{
				Utilities.tryToCloseJDBC(null, stmt, null);
				try
				{
					stmt = conn.createStatement();
					stmt.executeQuery("DELETE FROM junit_mediaFormat WHERE PK=11 OR PK=12");
					stmt.close();
					stmt = null;
				}
				catch (final Exception e)
				{
					// DOCTODO Document reason, why this block is empty
				}
				finally
				{
					Utilities.tryToCloseJDBC(conn, stmt, null);
				}

			}

		}
	}
}
