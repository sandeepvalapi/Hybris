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
package de.hybris.platform.directpersistence.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;


public class DefaultBatchCollectorTest
{
	private DefaultBatchCollector batchCollector;
	@Mock
	private PreparedStatementSetter statementSetter1, statementSetter2;
	@Mock
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		batchCollector = new DefaultBatchCollector();
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenSqlIsNullWhenCallingCollectQueryWithParams()
	{
		// given
		final String sql = null;
		final Object param = "fooBar";

		try
		{
			// when
			batchCollector.collectQuery(sql, param);
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("sql is required");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenParamsIsNullWhenCallingCollectQueryWithParams()
	{
		// given
		final String sql = "INSERT INTO fooBar VALUES (?)";
		final Object param = null;

		try
		{
			// when
			batchCollector.collectQuery(sql, param);
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("at least one param is required");
		}
	}

	@Test
	public void shouldCollectTwoParamsForOneBatchQuery()
	{
		// given
		final String sql = "INSERT INTO fooBar VALUES (?)";
		final String param1 = "someParam";
		final String param2 = "anotherParam";

		// when
		batchCollector.collectQuery(sql, param1);
		batchCollector.collectQuery(sql, param2);

		// then
		final Map<String, BatchGroup> batchGroups = batchCollector.getBatchGroups();
		assertThat(batchGroups).isNotNull().hasSize(1);
		assertThat(batchGroups.containsKey(sql)).isTrue();
		assertThat(batchGroups.get(sql)).isNotNull();
		assertThat(batchGroups.get(sql).isSettersBased()).isFalse();
		assertThat(batchGroups.get(sql).getParams()).isNotNull().hasSize(2);
		assertThat(batchGroups.get(sql).getParams().get(0)).containsOnly(param1);
		assertThat(batchGroups.get(sql).getParams().get(1)).containsOnly(param2);
	}

	@Test
	public void shouldCollectTwoStatementSettersForOneBatchQuery()
	{
		// given
		final String sql = "INSERT INTO fooBar VALUES (?)";

		// when
		batchCollector.collectQuery(sql, statementSetter1);
		batchCollector.collectQuery(sql, statementSetter2);

		// then
		final Map<String, BatchGroup> batchGroups = batchCollector.getBatchGroups();
		assertThat(batchGroups).isNotNull().hasSize(1);
		assertThat(batchGroups.containsKey(sql)).isTrue();
		assertThat(batchGroups.get(sql)).isNotNull();
		assertThat(batchGroups.get(sql).isSettersBased()).isTrue();
		assertThat(batchGroups.get(sql).getParams()).isNotNull().isEmpty();
		assertThat(batchGroups.get(sql).getBatchInfos()).isNotNull().hasSize(2);
		assertThat(batchGroups.get(sql).getBatchInfos().get(0).getStatementSetter()).isEqualTo(statementSetter1);
		assertThat(batchGroups.get(sql).getBatchInfos().get(1).getStatementSetter()).isEqualTo(statementSetter2);
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenSqlIsNullWhenCallingCollectQueryWithStatementSetter()
	{
		// given
		final String sql = null;

		try
		{
			// when
			batchCollector.collectQuery(sql, statementSetter1);
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("sql is required");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenMixingSettersAndParamsWithSameSqlQuery()
	{
		// given
		final String sql = "INSERT INTO fooBar VALUES (?)";
		final String param = "someParam";

		try
		{
			// when
			batchCollector.collectQuery(sql, param);
			batchCollector.collectQuery(sql, statementSetter1);
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage(
					"Inconsistent usage of sql for batch, cannot mix statementSetter and parameters with the same sql statement");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenStatementSetterIsNullWhenCallingCollectQueryWithStatementSetter()
	{
		// given
		final String sql = "INSERT INTO fooBar VALUES (?)";
		final PreparedStatementSetter statementSetter = null;

		try
		{
			// when
			batchCollector.collectQuery(sql, statementSetter);
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("statementSetter is required");
		}
	}


	@Test
	public void shouldExecuteBatchUpdateForStatementSetterBasedBatch()
	{
		// given
		final String sql = "INSERT INTO fooBar VALUES (?)";
		batchCollector.collectQuery(sql, statementSetter1);
		batchCollector.collectQuery(sql, statementSetter2);

		// when
		batchCollector.batchUpdate(jdbcTemplate);

		// then
		verify(jdbcTemplate).batchUpdate(eq(sql), any(BatchPreparedStatementSetter.class));
	}

	@Test
	public void shouldExecuteBatchUpdateForParamsBasedBatch()
	{
		// given
		final String sql = "INSERT INTO fooBar VALUES (?)";
		batchCollector.collectQuery(sql, "param1");
		batchCollector.collectQuery(sql, "param2");

		// when
		batchCollector.batchUpdate(jdbcTemplate);

		// then
		verify(jdbcTemplate).batchUpdate(sql, batchCollector.getBatchGroups().get(sql).getParams());
	}
}
