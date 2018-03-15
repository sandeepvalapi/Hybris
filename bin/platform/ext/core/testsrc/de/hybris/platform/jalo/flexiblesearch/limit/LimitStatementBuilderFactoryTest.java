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
package de.hybris.platform.jalo.flexiblesearch.limit;


import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.FallbackLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.HanaLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.HsqlLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.MySqlLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.OracleLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.PostgreSqlLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.SqlServerLimitStatementBuilder;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery.ExecutableQuery;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery.OrderByClauseInfo;
import de.hybris.platform.util.Config.DatabaseNames;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class LimitStatementBuilderFactoryTest
{
	private LimitStatementBuilderFactoryStub factory;
	private final int originalStart = 0, originalCount = 1;

	@Mock
	private Tenant tenant;
	@Mock
	private ExecutableQuery originalQuery;

	@Before
	public void setUp() throws Exception
	{
		factory = new LimitStatementBuilderFactoryStub(tenant);
		given(originalQuery.getSQL()).willReturn("SELECT * FROM FooBar");
		given(originalQuery.getOrderByClauseInfo()).willReturn(OrderByClauseInfo.NO_ORDER_BY_CLAUSE);
	}

	@Test
	public void shouldCreateFallbackLimitBuilderWhenSpecificDbLimitSupportIsDisabled()
	{
		// given
		factory.setSpecificDbLimitSupportEnabled(false);

		// when
		final LimitStatementBuilder builder = factory.getLimitStatementBuilder(originalQuery, originalStart, originalCount);

		// then
		assertThat(builder).isNotNull().isInstanceOf(FallbackLimitStatementBuilder.class);
	}

	@Test
	public void shouldCreateFallbackLimitBuilderWhenSpecificDbLimitSupportIsEnabledButThereIsNoSpecificBuilderForDB()
	{
		// given
		factory.setSpecificDbLimitSupportEnabled(true);
		factory.setDbName("DB_without_limit_builder");

		// when
		final LimitStatementBuilder builder = factory.getLimitStatementBuilder(originalQuery, originalStart, originalCount);

		// then
		assertThat(builder).isNotNull().isInstanceOf(FallbackLimitStatementBuilder.class);
	}

	@Test
	public void shouldCreateMySQLLimitBuilderWhenSpecificDbLimitSupportIsEnabledAndCurrentDbIsMySQL()
	{
		// given
		factory.setSpecificDbLimitSupportEnabled(true);
		factory.setDbName(DatabaseNames.MYSQL);

		// when
		final LimitStatementBuilder builder = factory.getLimitStatementBuilder(originalQuery, originalStart, originalCount);

		// then
		assertThat(builder).isNotNull().isInstanceOf(MySqlLimitStatementBuilder.class);
	}

	@Test
	public void shouldCreateOracleLimitBuilderWhenSpecificDbLimitSupportIsEnabledAndCurrentDbIsOracle()
	{
		// given
		factory.setSpecificDbLimitSupportEnabled(true);
		factory.setDbName(DatabaseNames.ORACLE);

		// when
		final LimitStatementBuilder builder = factory.getLimitStatementBuilder(originalQuery, originalStart, originalCount);

		// then
		assertThat(builder).isNotNull().isInstanceOf(OracleLimitStatementBuilder.class);
	}

	@Test
	public void shouldCreateHanaLimitBuilderWhenSpecificDbLimitSupportIsEnabledAndCurrentDbIsHana()
	{
		// given
		factory.setSpecificDbLimitSupportEnabled(true);
		factory.setDbName(DatabaseNames.HANA);

		// when
		final LimitStatementBuilder builder = factory.getLimitStatementBuilder(originalQuery, originalStart, originalCount);

		// then
		assertThat(builder).isNotNull().isInstanceOf(HanaLimitStatementBuilder.class);
	}

	@Test
	public void shouldCreateSqlServerLimitBuilderWhenSpecificDbLimitSupportIsEnabledAndCurrentDbIsSqlServerAndLimitSupportForSqlServer2012IsEnabled()
	{
		// given
		factory.setSpecificDbLimitSupportEnabled(true);
		factory.setDbName(DatabaseNames.SQLSERVER);
		factory.enableLimitSupportForSqlServer2012();

		// when
		final LimitStatementBuilder builder = factory.getLimitStatementBuilder(originalQuery, originalStart, originalCount);

		// then
		assertThat(builder).isNotNull().isInstanceOf(SqlServerLimitStatementBuilder.class);
	}

	@Test
	public void shouldNotCreateSqlServerLimitBuilderWhenSpecificDbLimitSupportIsEnabledAndCurrentDbIsSqlServerAndLimitSupportForSqlServer2012IsDisabled()
	{
		// given
		factory.setSpecificDbLimitSupportEnabled(true);
		factory.setDbName(DatabaseNames.SQLSERVER);
		factory.disableLimitSupportForSqlServer2012();

		// when
		final LimitStatementBuilder builder = factory.getLimitStatementBuilder(originalQuery, originalStart, originalCount);

		// then
		assertThat(builder).isNotNull().isInstanceOf(FallbackLimitStatementBuilder.class);
	}

	@Test
	public void shouldCreateHsqlLimitBuilderWhenSpecificDbLimitSupportIsEnabledAndCurrentDbIsHsql()
	{
		// given
		factory.setSpecificDbLimitSupportEnabled(true);
		factory.setDbName(DatabaseNames.HSQLDB);

		// when
		final LimitStatementBuilder builder = factory.getLimitStatementBuilder(originalQuery, originalStart,
				originalCount);

		// then
		assertThat(builder).isNotNull().isInstanceOf(HsqlLimitStatementBuilder.class);
	}

	@Test
	public void shouldCreatePostgreSqlLimitBuilderWhenSpecificDbLimitSupportIsEnabledAndCurrentDbIsPostgreSql()
	{
		// given
		factory.setSpecificDbLimitSupportEnabled(true);
		factory.setDbName(DatabaseNames.POSTGRESQL);

		// when
		final LimitStatementBuilder builder = factory.getLimitStatementBuilder(originalQuery, originalStart,
				originalCount);

		// then
		assertThat(builder).isNotNull().isInstanceOf(PostgreSqlLimitStatementBuilder.class);
	}

	private class LimitStatementBuilderFactoryStub extends LimitStatementBuilderFactory
	{
		private boolean isLimitSupportEnabledForSqlServer2012 = false;
		private boolean specificDbLimitSupportEnabled;
		private String dbName;

		public LimitStatementBuilderFactoryStub(final Tenant tenant)
		{
			super(tenant);
		}

		public void setSpecificDbLimitSupportEnabled(final boolean enabled)
		{
			this.specificDbLimitSupportEnabled = enabled;
		}

		public void setDbName(final String dbName)
		{
			this.dbName = dbName;
		}

		@Override
		protected boolean isDbUsed(final String dbName)
		{
			return dbName.equals(this.dbName);
		}

		@Override
		protected boolean isSpecificDbLimitSupportEnabled()
		{
			return specificDbLimitSupportEnabled;
		}

		@Override
		protected boolean isLimitSupportEnabledForSqlServer2012()
		{
			return isLimitSupportEnabledForSqlServer2012;
		}

		public void enableLimitSupportForSqlServer2012()
		{
			isLimitSupportEnabledForSqlServer2012 = true;
		}

		public void disableLimitSupportForSqlServer2012()
		{
			isLimitSupportEnabledForSqlServer2012 = false;
		}
	}
}
