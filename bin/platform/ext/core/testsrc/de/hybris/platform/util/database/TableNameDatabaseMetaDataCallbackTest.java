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
package de.hybris.platform.util.database;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.system.query.QueryProvider;
import de.hybris.platform.core.system.query.impl.QueryProviderFactory;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.RunListeners;
import de.hybris.platform.testframework.runlistener.LogRunListener;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.database.DropTablesTool.TableNameDatabaseMetaDataCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;


/**
 * Demonstrates how the DropTablesTool filters out its tenant specific tables.
 */
@RunListeners(LogRunListener.class)
@IntegrationTest
public class TableNameDatabaseMetaDataCallbackTest extends ServicelayerBaseTest
{
	private final List<String> createdTables = new ArrayList<>();

	private static final Logger LOG = Logger.getLogger(TableNameDatabaseMetaDataCallbackTest.class.getName());

	@Resource
	private DataSource dataSource;

	private Tenant before;

	static Properties DROP1_SETUP = new Properties();
	static Properties DROP2_SETUP = new Properties();
	static Properties DROP3_SETUP = new Properties();

	static
	{
		DROP1_SETUP.put("db.tableprefix", "");
		DROP2_SETUP.put("db.tableprefix", "dr2_");
		DROP3_SETUP.put("db.tableprefix", "dr3_");
	}

	@Mock
	private QueryProvider defaultQueryProvider;

	private Collection<SlaveTenant> allSlaves = new ArrayList<>();

	private final SlaveTenant drop1Tenant = new SlaveTenantWithJUnitDatasource("drop1", DROP1_SETUP);
	private final SlaveTenant drop2Tenant = new SlaveTenantWithJUnitDatasource("drop2", DROP2_SETUP);
	private final SlaveTenant drop3Tenant = new SlaveTenantWithJUnitDatasource("drop3", DROP3_SETUP);

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(defaultQueryProvider.getTableName()).thenReturn(QueryProviderFactory.LOCK_TABLE);
		before = Registry.getCurrentTenantNoFallback();
		allSlaves = Arrays.asList(drop1Tenant, drop2Tenant, drop3Tenant);
	}

	// uses junit tenant
	@Test
	public void testDropOnlyTablesWithMyPrefix() throws MetaDataAccessException
	{

		final String currentPrefix = "dr2_";

		final String otherPrefix = "other_";
		final String nonePrefix = "";


		createTable(dataSource, currentPrefix + "foo");
		createTable(dataSource, currentPrefix + "bar");
		createTable(dataSource, currentPrefix + "boo");


		createTable(dataSource, otherPrefix + "foo");
		createTable(dataSource, otherPrefix + "bar");
		createTable(dataSource, otherPrefix + "boo");


		createTable(dataSource, nonePrefix + "foo");
		createTable(dataSource, nonePrefix + "bar");
		createTable(dataSource, nonePrefix + "boo");

		final TableNameDatabaseMetaDataCallback tablesFilterCallback = new TableNameDatabaseMetaDataCallback(defaultQueryProvider,
				drop2Tenant)
		{
			@Override
			Collection<SlaveTenant> getSlaveTenants()
			{
				return allSlaves;
			}

		};

		final List<String> tables = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, tablesFilterCallback);

		Assert.assertTrue(containsIgnoreCase(currentPrefix + "foo", tables));
		Assert.assertTrue(containsIgnoreCase(currentPrefix + "bar", tables));
		Assert.assertTrue(containsIgnoreCase(currentPrefix + "boo", tables));

		Assert.assertFalse(containsIgnoreCase(otherPrefix + "foo", tables));
		Assert.assertFalse(containsIgnoreCase(otherPrefix + "bar", tables));
		Assert.assertFalse(containsIgnoreCase(otherPrefix + "boo", tables));

		Assert.assertFalse(containsIgnoreCase(nonePrefix + "foo", tables));
		Assert.assertFalse(containsIgnoreCase(nonePrefix + "bar", tables));
		Assert.assertFalse(containsIgnoreCase(nonePrefix + "boo", tables));


		testOmmitAdminTables();
	}


	// never drop system tables
	@Test
	public void testOmmitAdminTables() throws MetaDataAccessException
	{
		//
		final TableNameDatabaseMetaDataCallback tablesFilterCallback = new TableNameDatabaseMetaDataCallback(defaultQueryProvider,
				Registry.getCurrentTenantNoFallback())
		{
			@Override
			Collection<SlaveTenant> getSlaveTenants()
			{
				return allSlaves;
			}
		};

		final List<String> tables = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, tablesFilterCallback);

		Assert.assertFalse(tables.contains("JGROUPSPING"));
		Assert.assertFalse(tables.contains(QueryProviderFactory.LOCK_TABLE));
	}


	// never drop system tables
	@Test
	public void testOmmitAdminTablesForCustomTablePrefix() throws MetaDataAccessException
	{

		final String TABLE_PREFIX = "FoO_"; // play around with case sensitiveness

		final String before = Config.getParameter("db.tableprefix");

		Mockito.reset(defaultQueryProvider);
		Mockito.when(defaultQueryProvider.getTableName()).thenReturn(TABLE_PREFIX + QueryProviderFactory.LOCK_TABLE);
		try
		{
			Config.setParameter("db.tableprefix", TABLE_PREFIX);

			createTable(dataSource, TABLE_PREFIX + QueryProviderFactory.LOCK_TABLE);
			//
			final TableNameDatabaseMetaDataCallback tablesFilterCallback = new TableNameDatabaseMetaDataCallback(
					defaultQueryProvider, Registry.getCurrentTenantNoFallback())
			{
				@Override
				Collection<SlaveTenant> getSlaveTenants()
				{
					return allSlaves;
				}
			};

			final List<String> tables = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, tablesFilterCallback);

			Assert.assertFalse(tables.contains("JGROUPSPING"));
			// Assert.assertTrue(tables.contains(QueryProviderFactory.LOCK_TABLE));
			Assert.assertFalse(tables.contains(TABLE_PREFIX + QueryProviderFactory.LOCK_TABLE));
			Assert.assertFalse(tables.contains((TABLE_PREFIX + QueryProviderFactory.LOCK_TABLE).toLowerCase()));
			Assert.assertFalse(tables.contains((TABLE_PREFIX + QueryProviderFactory.LOCK_TABLE).toUpperCase()));
		}
		finally
		{
			Config.setParameter("db.tableprefix", before);

			dropTable(dataSource, TABLE_PREFIX + QueryProviderFactory.LOCK_TABLE);
		}
	}


	// there is also drop2 on this url with other prefix
	@Test
	public void testDropAllMyNonPrefixedTablesLeaveOthersWithPrefixes() throws MetaDataAccessException
	{

		final String currentPrefix = getTablePrefix(drop1Tenant);

		Assume.assumeTrue(org.apache.commons.lang.StringUtils.isBlank(currentPrefix));// empty prefix


		final String otherPrefix = getTablePrefix(drop2Tenant);
		final String yetAnotherPrefix = getTablePrefix(drop3Tenant);

		createTable(dataSource, "foo");
		createTable(dataSource, "bar");
		createTable(dataSource, "boo");


		createTable(dataSource, otherPrefix + "foo");
		createTable(dataSource, otherPrefix + "bar");
		createTable(dataSource, otherPrefix + "boo");

		createTable(dataSource, yetAnotherPrefix + "foo");
		createTable(dataSource, yetAnotherPrefix + "bar");
		createTable(dataSource, yetAnotherPrefix + "boo");



		final TableNameDatabaseMetaDataCallback tablesFilterCallback = new TableNameDatabaseMetaDataCallback(defaultQueryProvider,
				drop1Tenant)
		{
			@Override
			Collection<SlaveTenant> getSlaveTenants()
			{
				return allSlaves;
			}

			@Override
			Tenant getMasterTenant()
			{
				// just to have different table prefix
				return drop3Tenant;
			}


		};

		final List<String> tables = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, tablesFilterCallback);

		Assert.assertTrue(containsIgnoreCase(currentPrefix + "foo", tables));
		Assert.assertTrue(containsIgnoreCase(currentPrefix + "bar", tables));
		Assert.assertTrue(containsIgnoreCase(currentPrefix + "boo", tables));


		Assert.assertFalse(containsIgnoreCase(otherPrefix + "foo", tables));
		Assert.assertFalse(containsIgnoreCase(otherPrefix + "bar", tables));
		Assert.assertFalse(containsIgnoreCase(otherPrefix + "boo", tables));

		Assert.assertFalse(containsIgnoreCase(yetAnotherPrefix + "foo", tables));
		Assert.assertFalse(containsIgnoreCase(yetAnotherPrefix + "bar", tables));
		Assert.assertFalse(containsIgnoreCase(yetAnotherPrefix + "boo", tables));

		testOmmitAdminTables();

	}

	@After
	public void cleanupDB()
	{
		for (final String table : createdTables)
		{
			dropTable(dataSource, table);
		}

		Registry.setCurrentTenant(before);
	}

	/**
	 * 
	 */
	private boolean containsIgnoreCase(final String text, final List<String> tables)
	{
		return tables.contains(text.toUpperCase()) || tables.contains(text.toLowerCase());
	}

	String getTablePrefix(final Tenant tenant)
	{
		return tenant.getConfig().getParameter("db.tableprefix") == null ? "" : tenant.getConfig().getParameter("db.tableprefix");

	}

	private void createTable(final DataSource dataSource, final String tableName)
	{

		final JdbcTemplate create = new JdbcTemplate(dataSource);


		create.execute("CREATE TABLE " + tableName + " ( ID VARCHAR(10))");
		createdTables.add(tableName);
	}

	private void dropTable(final DataSource dataSource, final String tableName)
	{

		final JdbcTemplate create = new JdbcTemplate(dataSource);

		try
		{
			create.execute("DROP TABLE " + tableName);
		}
		catch (final DataAccessException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
		}
	}

	@Override
	public void init() throws JaloSystemException
	{
		// nop
	}

	@Override
	public void finish() throws JaloSecurityException
	{
		// nop
	}


	private class SlaveTenantWithJUnitDatasource extends SlaveTenant {


		public SlaveTenantWithJUnitDatasource(final String systemName, final Properties props)
		{
			super(systemName, props);
		}

		@Override
		public HybrisDataSource getDataSource()
		{
			return (HybrisDataSource) dataSource;
		}

	}
}
