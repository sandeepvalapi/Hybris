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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.FallbackLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.MySqlLimitStatementBuilder;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery.ExecutableQuery;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Config.DatabaseNames;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@IntegrationTest
public class LimitStatementBuilderFactoryIntegrationTest extends HybrisJUnit4Test
{
	private LimitStatementBuilderFactoryStub factory;
	@Mock
	private ExecutableQuery query;
	private boolean disableDbLimitSupportBackup;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		factory = new LimitStatementBuilderFactoryStub(jaloSession.getTenant());
		disableDbLimitSupportBackup = Config.getBoolean(LimitStatementBuilderFactory.DISABLE_SPECIFIC_DB_LIMIT_SUPPORT, false);
	}

	@After
	public void cleanUpSystemProps()
	{
		Config.setParameter(LimitStatementBuilderFactory.DISABLE_SPECIFIC_DB_LIMIT_SUPPORT,
				Boolean.toString(disableDbLimitSupportBackup));
	}

	@Test
	public void shouldReturnFallbackBuilderIfDbLimitSupportIsDisabledLocallyAndEnabledGloballyByDefault()
	{
		// given
		disableLocalDbLimitSupport();

		// when
		final LimitStatementBuilder limitStatementBuilder = factory.getLimitStatementBuilder(query, 1, 10);

		// then
		assertThat(limitStatementBuilder).isNotNull();
		assertThat(limitStatementBuilder).isInstanceOf(FallbackLimitStatementBuilder.class);
	}

	@Test
	public void shouldReturnMySQLDbBuilderIfDbLimitSupportIsEnabledLocallyAndDisabledGlobally()
	{
		// given
		disableGlobalDbLimitSupport();
		enableLocalDbLimitSupport();

		// when
		final LimitStatementBuilder limitStatementBuilder = factory.getLimitStatementBuilder(query, 1, 10);

		// then
		assertThat(limitStatementBuilder).isNotNull();
		assertThat(limitStatementBuilder).isInstanceOf(MySqlLimitStatementBuilder.class);
	}

	private void enableLocalDbLimitSupport()
	{
		final SessionContext ctx = jaloSession.createLocalSessionContext();
		ctx.setAttribute(LimitStatementBuilderFactory.DISABLE_SPECIFIC_DB_LIMIT_SUPPORT, Boolean.FALSE);
	}

	private void disableLocalDbLimitSupport()
	{
		final SessionContext ctx = jaloSession.createLocalSessionContext();
		ctx.setAttribute(LimitStatementBuilderFactory.DISABLE_SPECIFIC_DB_LIMIT_SUPPORT, Boolean.TRUE);
	}

	private void disableGlobalDbLimitSupport()
	{
		Config.setParameter(LimitStatementBuilderFactory.DISABLE_SPECIFIC_DB_LIMIT_SUPPORT, Boolean.toString(true));
	}

	/**
	 * This stub sticks current DB to MySQL always. Nothing more is needed by the test.
	 */
	private class LimitStatementBuilderFactoryStub extends LimitStatementBuilderFactory
	{
		public LimitStatementBuilderFactoryStub(final Tenant tenant)
		{
			super(tenant);
		}

		@Override
		protected boolean isDbUsed(final String dbName)
		{
			return dbName.equals(DatabaseNames.MYSQL);
		}
	}
}
