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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.util.jdbc.DBColumn;
import de.hybris.platform.util.jdbc.DBTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;


@UnitTest
public class RawToBlobChangeStrategyTest
{

	private static final String ORIGINAL_COLUMN_NAME = "p_original";
	private static final String TABLE_NAME = "testtable";

	@Test
	public void testDoChangeColumn() throws SQLException //NOPMD
	{
		final Connection connection = Mockito.mock(Connection.class);
		final PreparedStatement statement = Mockito.mock(PreparedStatement.class);
		Mockito.when(connection.prepareStatement(Matchers.anyString())).thenReturn(statement);
		Mockito.when(connection.getCatalog()).thenReturn(null);

		final RawToBlobChangeStrategy strategy = new RawToBlobChangeStrategy()
		{
			@Override
			protected boolean isOracleRawToBlob(final String targetDefinition,
					final de.hybris.platform.util.jdbc.DBColumn originalMetaData)
			{
				return true;
			}

			@Override
			protected Connection getConnection() throws SQLException
			{
				return connection;
			}

			@Override
			protected String getOriginalColumnName(final DBColumn originalMetaData)
			{
				return ORIGINAL_COLUMN_NAME;
			}
		};

		final DBColumn originalMetaData = Mockito.mock(DBColumn.class);
		final DBTable table = Mockito.mock(DBTable.class);
		Mockito.when(table.getName()).thenReturn(TABLE_NAME);
		Mockito.when(originalMetaData.getTable()).thenReturn(table);
		final AttributeDescriptorRemote attributeDescr = Mockito.mock(AttributeDescriptorRemote.class);
		Assertions.assertThat(strategy.doChangeColumn("BLOB", originalMetaData, attributeDescr)).isTrue();

		Mockito.verify(connection).prepareStatement(
				"alter table " + TABLE_NAME + " add " + ORIGINAL_COLUMN_NAME + RawToBlobChangeStrategy.SUFFIX + " BLOB");
		Mockito.verify(connection).prepareStatement(
				"update " + TABLE_NAME + " set " + ORIGINAL_COLUMN_NAME + RawToBlobChangeStrategy.SUFFIX + " = "
						+ ORIGINAL_COLUMN_NAME);
		Mockito.verify(connection).prepareStatement("alter table " + TABLE_NAME + " drop column " + ORIGINAL_COLUMN_NAME);
		Mockito.verify(connection).prepareStatement(
				"alter table " + TABLE_NAME + " rename column " + ORIGINAL_COLUMN_NAME + RawToBlobChangeStrategy.SUFFIX + " to "
						+ ORIGINAL_COLUMN_NAME);
		Mockito.verify(statement, Mockito.times(4)).execute();
		Mockito.verify(statement, Mockito.times(4)).close();
		Mockito.verify(connection).close();
	}
}
