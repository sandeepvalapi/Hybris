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
package de.hybris.platform.util.localization.jdbc.executor;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.localization.jdbc.StatementWithParams;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BatchedStatementsExecutorUnitTest
{
	@Mock
	private ExecutorService excutorService;

	@Mock
	private DataSource dataSource;

	@Mock
	private TransactionTemplate transactionTemplate;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	TransactionStatus transactionStatus;

	private StatementWithParams testStatement;

	@Before
	public void setUp() throws SQLException
	{
		doAnswer(new Answer<Long>()
		{
			@Override
			public Long answer(final InvocationOnMock invocation) throws Throwable
			{
				final TransactionCallback<Long> callback = (TransactionCallback<Long>) invocation.getArguments()[0];

				return callback.doInTransaction(transactionStatus);
			}
		}).when(transactionTemplate).execute((TransactionCallback<Long>) notNull());

		doAnswer(new Answer<int[]>()
		{
			@Override
			public int[] answer(final InvocationOnMock invocation) throws Throwable
			{
				final List<Object[]> params = (List<Object[]>) invocation.getArguments()[1];
				final int[] result = new int[params.size()];
				for (int i = 0; i < result.length; i++)
				{
					result[i] = 1;
				}
				return result;
			}
		}).when(jdbcTemplate).batchUpdate((String) notNull(), (List<Object[]>) notNull());

		testStatement = new StatementWithParams("TEST_STATEMENT");

		when(excutorService.submit((Callable<Long>) notNull())).thenReturn(Futures.immediateFuture(Long.valueOf(1)));
	}

	@Test
	public void shouldSubmitExecutionToExecutor()
	{
		//given
		final BatchedStatementsExecutor statementsExecutor = new BatchedStatementsExecutor(excutorService, dataSource);

		//when
		statementsExecutor.execute(ImmutableList.of(testStatement));

		//then
		verify(excutorService).submit(any(BatchedStatementsExecutor.ExecuteStatementsTask.class));
	}

	@Test
	public void shouldSubmitAllStatementsOnlyOnce()
	{
		//given
		final BatchedStatementsExecutor statementsExecutor = new BatchedStatementsExecutor(excutorService, dataSource);
		final Iterable<StatementWithParams> statements = createStatements("ONE", "TWO", "ONE", "ONE", "THREE", "TWO");

		//when
		statementsExecutor.execute(statements);

		//then
		verify(excutorService, times(1)).submit(any(BatchedStatementsExecutor.ExecuteStatementsTask.class));
	}

	@Test
	public void shouldSubmitStatementsInOneGo()
	{
		//given
		final BatchedStatementsExecutor statementsExecutor = new BatchedStatementsExecutor(excutorService, dataSource);
		final Iterable<StatementWithParams> statements = createStatements("ONE", "TWO", "ONE", "ONE", "THREE", "TWO");

		//when
		statementsExecutor.execute(statements);

		//then
		verify(excutorService, times(1)).submit(any(BatchedStatementsExecutor.ExecuteStatementsTask.class));
	}

	@Test
	public void shouldBatchTheSameStatementsAndExecuteThemInOneTransaction() throws Exception
	{
		//given
		final Iterable<StatementWithParams> statements = createStatements("ONE", "TWO", "ONE", "ONE", "THREE", "TWO");
		final BatchedStatementsExecutor.ExecuteStatementsTask task = new BatchedStatementsExecutor.ExecuteStatementsTask(
				transactionTemplate, jdbcTemplate, statements);

		//when
		task.call();

		//then
		verify(jdbcTemplate, times(1)).batchUpdate(eq("ONE"), (List<Object[]>) notNull());
		verify(jdbcTemplate, times(1)).batchUpdate(eq("TWO"), (List<Object[]>) notNull());
		verify(jdbcTemplate, times(1)).batchUpdate(eq("THREE"), (List<Object[]>) notNull());
		verify(transactionTemplate, times(1)).execute((TransactionCallback<Long>) notNull());
		verifyNoMoreInteractions(jdbcTemplate, transactionTemplate);
	}

	@Test
	public void shouldReturnTotalNumberOfAffectedRows() throws Exception
	{
		//given
		final List<StatementWithParams> statements = createStatements("ONE", "TWO", "ONE", "ONE", "THREE", "TWO");
		final BatchedStatementsExecutor.ExecuteStatementsTask task = new BatchedStatementsExecutor.ExecuteStatementsTask(
				transactionTemplate, jdbcTemplate, statements);

		//when
		final Long totalNumberOfAffectedRows = task.call();

		//then
		assertThat(totalNumberOfAffectedRows).isNotNull().isEqualTo(statements.size());
	}

	private List<StatementWithParams> createStatements(final String... sqls)
	{
		final List<StatementWithParams> result = new LinkedList<>();

		for (final String sql : sqls)
		{
			result.add(new StatementWithParams(sql));
		}

		return result;
	}
}
