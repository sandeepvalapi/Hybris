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

import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.system.impl.DefaultInitLockDao;
import de.hybris.platform.core.system.query.QueryProvider;
import de.hybris.platform.core.system.query.impl.QueryProviderFactory;


/**
 * {@link InitializationLockHandler} implementation to support a overridden {@link QueryProvider#getTableName()} and
 * remain serializable (without any mocks)
 */
class TestInitializationLockHandler extends InitializationLockHandler
{
	static private class TestQueryProvider implements QueryProvider
	{

		private final String testTablName;
		private final QueryProvider wrappedQP;

		/**
		 * 
		 */
		private TestQueryProvider(final Tenant tenant, final String tableName)
		{
			final QueryProviderFactory factory = new QueryProviderFactory(tenant.getDataSource().getDatabaseName());
			testTablName = tableName;
			wrappedQP = factory.getQueryProviderInstance();
		}

		@Override
		public String getQueryForSelect()
		{
			return wrappedQP.getQueryForSelect();
		}

		@Override
		public String getQueryForLock()
		{
			return wrappedQP.getQueryForLock();
		}

		@Override
		public String getQueryForUnlock()
		{
			return wrappedQP.getQueryForUnlock();
		}

		@Override
		public String getQueryForTableCreate()
		{
			return wrappedQP.getQueryForTableCreate();
		}

		@Override
		public String getQueryForRowInsert()
		{
			return wrappedQP.getQueryForRowInsert();
		}

		@Override
		public String getTableName()
		{
			return testTablName;
		}

		@Override
		public String getQueryForTransactionsIsolation()
		{
			// nothing to test here
			return null;
		}

	}


	public TestInitializationLockHandler(final Tenant tenant, final String tableTestName)
	{
		super(new DefaultInitLockDao(new TestQueryProvider(tenant, tableTestName)));
	}

}
