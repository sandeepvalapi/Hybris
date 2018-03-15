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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Perf;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PerformanceTest extends HybrisJUnit4Test
{
	protected Connection conn = null;
	protected Statement stmt = null;
	private static final Logger log = Logger.getLogger(PerformanceTest.class);

	@Before
	public void setUp() throws Exception
	{
		conn = Registry.getCurrentTenant().getDataSource().getConnection();
	}

	@After
	public void tearDown() throws Exception
	{
		if (stmt != null)
		{
			try
			{
				stmt.close();
			}
			catch (final Exception e)
			{
				if (log.isDebugEnabled())
				{
					log.debug(e.getMessage());
				}
			}
		}
		if (conn != null)
		{
			try
			{
				conn.close();
			}
			catch (final Exception e)
			{
				if (log.isDebugEnabled())
				{
					log.debug(e.getMessage());
				}
			}
		}
	}


	@Test
	public void testGetConnection() throws Exception
	{
		Transaction.current();
		final int THREADS = 1;
		final Perf p = new Perf(THREADS)
		{
			@Override
			public void body() throws Exception
			{
				final Transaction tx = Transaction.current();
				tx.begin();
				tx.commit();
			}
		};
		p.go(1000, THREADS);
		final long l = p.getExecutions();
		p.close();
		log.info("executing for 1 second: " + l + " iterations.");
	}




	@Test
	public void testFlexSearch() throws Exception
	{
		Transaction.current();
		final int THREADS = 1;
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAnonymousCustomer());
		FlexibleSearch.getInstance().search("SELECT {PK} FROM {Product}", null, PK.class);
		final Perf p = new Perf(THREADS)
		{
			@Override
			public void body() throws Exception
			{
				FlexibleSearch.getInstance().search("SELECT {PK} FROM {Product}", null, PK.class);
			}
		};
		p.go(3000);
		final long l = p.getExecutions();
		p.close();
		log.info("executing for 1 second: " + l + " iterations.");
	}


}
