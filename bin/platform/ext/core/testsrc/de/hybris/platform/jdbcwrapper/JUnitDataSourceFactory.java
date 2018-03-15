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

import de.hybris.platform.core.DataSourceImplFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


/**
 * @author hr
 */
public class JUnitDataSourceFactory extends DataSourceImplFactory
{

	@Override
	public JDBCConnectionPool createConnectionPool(final HybrisDataSource dataSource, final GenericObjectPoolConfig poolConfig)
	{
		final JUnitConnectionStatus connectionStatus = new JUnitConnectionStatus();
		final JUnitJDBCConnectionFactory factory = new JUnitJDBCConnectionFactory(dataSource, connectionStatus);

		// always use connection error checking pool
		final JDBCConnectionPool pool = new JUnitConnectionErrorCheckingJDBCConnectionPool(factory, poolConfig, connectionStatus);
		pool.setDumpStackOnConnectionError(dataSource.getTenant().getConfig()
				.getBoolean(DBConstants.POOL_DUMP_STACK_ON_CONNECTION_ERROR, DBConstants.DEFAULT_POOL_DUMP_STACK_ON_CONNECTION_ERROR));
		return pool;
	}

	@Override
	public Connection wrapConnection(final HybrisDataSource wrappedDataSource, final Connection rawConnection)
	{
		return new JUnitConnectionImpl(wrappedDataSource, rawConnection);
	}

	@Override
	public PreparedStatement wrapPreparedStatement(final Connection wrappedConnection, final PreparedStatement rawStatement,
			final String query)
	{
		return new JUnitPreparedStatementImpl((ConnectionImpl) wrappedConnection, rawStatement, query);
	}
}
