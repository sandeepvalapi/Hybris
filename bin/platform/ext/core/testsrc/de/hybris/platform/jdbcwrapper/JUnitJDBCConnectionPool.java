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

import java.sql.Connection;


/**
 * For testing error handling when
 * <ul>
 * <li>all connections fail</li>
 * <li>single connections fail</li>
 * <li>the pool reports connection errors in general</li>
 * </ul>
 * 
 * @author hr, ag
 */
public interface JUnitJDBCConnectionPool
{
	void addFailingConnection(final Connection con);

	void removeFailingConnection(final Connection con);

	void setAllConnectionsFail(final boolean allFail);

	void resetTestMode();
}
