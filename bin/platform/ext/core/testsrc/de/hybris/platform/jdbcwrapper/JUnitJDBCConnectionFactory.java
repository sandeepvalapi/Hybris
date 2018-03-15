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

import org.apache.commons.pool2.PooledObject;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class JUnitJDBCConnectionFactory extends ConnectionErrorCheckingJDBCConnectionFactory
{
	private final AtomicBoolean forceAllConnectionsFail = new AtomicBoolean(false);
	private final Map<Connection, Connection> forceValidationErrorConnections = new ConcurrentHashMap<Connection, Connection>();
	private final ConnectionStatus connectionStatus;

	public JUnitJDBCConnectionFactory(final HybrisDataSource dataSource, final ConnectionStatus connectionStatus)
	{
		super(dataSource, connectionStatus);
		this.connectionStatus = connectionStatus;
	}

	@Override
	public boolean validateObject(final PooledObject<Connection> pooledConnection)
	{
		final Connection connection = pooledConnection.getObject();
		final boolean result = !forceAllConnectionsFail.get() && !forceValidationErrorConnections.containsKey(connection)
				&& super.validateObject(pooledConnection);
		if (!result)
		{
			connectionStatus.notifyConnectionError();
		}
		return result;
	}

	public void setAllConnectionsFail(final boolean allFail)
	{
		forceAllConnectionsFail.set(allFail);
	}

	public void addFailingConnection(final Connection con)
	{
		forceValidationErrorConnections.put(con, con);
	}

	public void removeFailingConnection(final Connection con)
	{
		forceValidationErrorConnections.remove(con);
	}

	public void removeAllFailingConnections()
	{
		forceValidationErrorConnections.clear();
	}
}
