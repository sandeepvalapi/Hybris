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
package de.hybris.platform.persistence.type.update.strategy.oracle;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.persistence.type.update.AbstractColumnAlternationTest;
import de.hybris.platform.persistence.type.update.misc.UpdateModelException;
import de.hybris.platform.util.Config;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class RawToBlobChangeStrategyIntegrationTest extends AbstractColumnAlternationTest
{

	@Override
	protected boolean checkDatabaseSupported()
	{
		return Config.isOracleUsed();
	}

	@Before
	public void setUp()
	{
		setTableNameSubfix("");
	}

	@Test
	public void alterRawToBlob() throws SQLException, UpdateModelException
	{
		checkTypeChange(new ColumnDefinitionImpl("RAW", Integer.valueOf(2000)), new ColumnDefinitionImpl("BLOB"),
				new ColumnDefinitionImpl("BLOB"), getData());
	}

	protected Object[] getData()
	{
		return new String[]
		{ "test" };
	}
}
