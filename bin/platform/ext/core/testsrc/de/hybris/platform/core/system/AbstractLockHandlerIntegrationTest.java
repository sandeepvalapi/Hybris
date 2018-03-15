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
package de.hybris.platform.core.system;



import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TestTenantStub;
import de.hybris.platform.core.system.impl.DefaultInitLockDao;
import de.hybris.platform.core.system.query.QueryProvider;
import de.hybris.platform.core.system.query.impl.QueryProviderFactory;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


@Ignore
public abstract class AbstractLockHandlerIntegrationTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(InitializationLockHandlerIntegrationTest.class.getName());

	protected final int WAIT_SECONDS = 40;
	protected final int THREADS = 100;

	protected InitializationLockHandler handler;

	protected InitializationLockHandler prepareHandler(final String tableName)
	{
		final HybrisDataSource dataSource = Registry.getCurrentTenantNoFallback().getDataSource();

		final QueryProvider provider = new QueryProviderFactory(dataSource.getDatabaseName())
		{
			@Override
			protected String getLockTableName()
			{
				return tableName;
			}
		}.getQueryProviderInstance();

		final InitializationLockDao lockDao = new DefaultInitLockDao(provider)
		{
			@Override
			protected HybrisDataSource getInitializedMasterDataSource()
			{
				return dataSource;
			}
		};

		return new TestInitHandler(lockDao, dataSource, tableName);
	}

	private static class TestInitHandler extends InitializationLockHandler
	{
		private final String tableName;
		private final DataSource dataSource;

		public TestInitHandler(final InitializationLockDao initializationDao, final DataSource dataSource, final String tableName)
		{
			super(initializationDao);
			this.tableName = tableName;
			this.dataSource = dataSource;
		}
	}


	protected void clearHandler(final InitializationLockHandler handler)
	{
		final TestInitHandler initHandler = (TestInitHandler) handler;
		try
		{
			System.err.println("------------------------------------------------");
			final JdbcTemplate jdbcTemplate = new JdbcTemplate(initHandler.dataSource);
			jdbcTemplate.execute("DROP TABLE  " + initHandler.tableName);
		}
		catch (final DataAccessException dae)
		{
			LOG.warn(" Table " + initHandler.tableName + " was not created during the test ");
			if (LOG.isDebugEnabled())
			{
				LOG.debug(dae);
			}
		}
	}

	protected AbstractTenant createOtherClusterIdTenant()
	{
		final AbstractTenant testTenant = (AbstractTenant) Registry.getCurrentTenantNoFallback();

		return new TestTenantStub("Foo", testTenant);

	}


	protected boolean checkTestTableExists(final InitializationLockHandler handler)
	{
		final TestInitHandler initHandler = (TestInitHandler) handler;
		return checkTestTableExists(initHandler.dataSource, initHandler.tableName);
	}

	protected boolean checkTestTableExists(final DataSource dataSource, final String tableName)
	{
		try
		{
			final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.execute(" SELECT 1 FROM  " + tableName);
			return true;
		}
		catch (final DataAccessException e)
		{
			return false;
		}
	}
}
