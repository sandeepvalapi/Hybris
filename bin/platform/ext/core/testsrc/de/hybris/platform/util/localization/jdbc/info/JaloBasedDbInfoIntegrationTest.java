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
package de.hybris.platform.util.localization.jdbc.info;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import org.junit.Test;


@IntegrationTest
public class JaloBasedDbInfoIntegrationTest extends HybrisJUnit4Test
{
	@Test
	public void shouldReturnProperTableNameForGivenTypecode()
	{
		//given
		final JaloBasedDbInfo dbInfo = new JaloBasedDbInfo();

		//when
		final String tableName = dbInfo.getTableNameFor(ComposedTypeModel._TYPECODE);

		//then
		assertThat(tableName).isNotNull().isEqualToIgnoringCase("junit_composedtypes");
	}

	@Test
	public void shouldReturnProperColumnNameForGivenTypecodeAndColumnName()
	{
		//given
		final JaloBasedDbInfo dbInfo = new JaloBasedDbInfo();

		//when
		final String columnName = dbInfo.getColumnNameFor(ComposedTypeModel._TYPECODE, ComposedTypeModel.NAME);

		//then
		assertThat(columnName).isNotNull().isEqualToIgnoringCase("p_name");
	}

	@Test
	public void shouldThrowExceptionWhenTryingToGetTypeNameForUnknownTypeCode()
	{
		final JaloBasedDbInfo dbInfo = new JaloBasedDbInfo();

		try
		{
			dbInfo.getTableNameFor("UNKNOWN_TYPECODE_:-)");
		}
		catch (final Exception exception)
		{
			assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(
					"Can't find table name for UNKNOWN_TYPECODE_:-)");
			return;
		}
		fail("Exception should be thrown.");
	}

	@Test
	public void shouldThrowExceptionWhenTryingToGetColumnNameForUnknownTypeCode()
	{
		final JaloBasedDbInfo dbInfo = new JaloBasedDbInfo();

		try
		{
			dbInfo.getColumnNameFor("UNKNOWN_TYPECODE_:-)", "name");
		}
		catch (final Exception exception)
		{
			assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(
					"Can't find column name for UNKNOWN_TYPECODE_:-).name");
			return;
		}
		fail("Exception should be thrown.");
	}

	@Test
	public void shouldThrowExceptionWhenTryingToGetColumnNameForUnknownAttributeName()
	{
		final JaloBasedDbInfo dbInfo = new JaloBasedDbInfo();

		try
		{
			dbInfo.getColumnNameFor(ComposedTypeModel._TYPECODE, "Schmetterling");
		}
		catch (final Exception exception)
		{
			assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage(
					"Can't find column name for ComposedType.Schmetterling");
			return;
		}
		fail("Exception should be thrown.");
	}
}
