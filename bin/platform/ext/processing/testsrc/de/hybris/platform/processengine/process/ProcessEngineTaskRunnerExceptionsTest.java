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
package de.hybris.platform.processengine.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.processengine.definition.NodeExecutionException;
import de.hybris.platform.processengine.definition.ProcessDefinition;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.definition.ProcessDefinitionId;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionException;

import java.util.Date;
import java.util.concurrent.Callable;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.cglib.proxy.UndeclaredThrowableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionTemplate;


@UnitTest
public class ProcessEngineTaskRunnerExceptionsTest
{
	@Mock
	private TaskService taskService;

	@Mock
	private ModelService modelService;

	@Mock
	private ProcessDefinition processDefinition;

	@Mock
	private ProcessDefinitionFactory processDefinitionFactory;

	@Mock
	private ProcessParameterHelper processParameterHelper;

	@Mock
	private TransactionTemplate transactionTemplate;

	Level levelBefore = null;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(processDefinitionFactory.getProcessDefinition(new ProcessDefinitionId("name"))).thenReturn(processDefinition);
		Mockito.when(transactionTemplate.execute(Mockito.any())).thenReturn(Void.TYPE);

		final Logger logger = Logger.getLogger(ProcessengineTaskRunner.class);
		levelBefore = logger.getLevel();
		logger.setLevel(Level.OFF);
	}

	@After
	public void tearDown()
	{
		final Logger logger = Logger.getLogger(ProcessengineTaskRunner.class);
		logger.setLevel(levelBefore);

		while (Transaction.current().isRunning())
		{
			System.err.println("!!! transaction still running !!!");
			Transaction.current().rollback();
		}
	}

	@Test
	public void testRetryLaterException()
	{
		final ProcessTaskModel task = mockTask("foo");
		final TestProcessEngineTaskRunner runner = mockRunner(new Callable<String>()
		{
			@Override
			public String call() throws Exception
			{
				throw new RetryLaterException();
			}
		});
		try
		{
			runner.run(taskService, task);

			fail("RetryLaterException expected but not thrown");
		}
		catch (final RetryLaterException e)
		{
			// as expected
		}
		assertTrue(runner.rollbackTriggered);
		assertFalse(ProcessState.ERROR == task.getProcess().getState());
	}

	@Test
	public void testNodeExecutionException() throws RetryLaterException
	{
		final ProcessTaskModel task = mockTask("foo");
		final TestProcessEngineTaskRunner runner = mockRunner(new Callable<String>()
		{
			@Override
			public String call() throws Exception
			{
				throw new NodeExecutionException();
			}
		});
		runner.run(taskService, task);
		assertTrue(runner.rollbackTriggered);
		assertEquals(ProcessState.ERROR, task.getProcess().getState());
	}

	@Test
	public void testRuntimeException() throws RetryLaterException
	{
		final ProcessTaskModel task = mockTask("foo");
		final TestProcessEngineTaskRunner runner = mockRunner(new Callable<String>()
		{
			@Override
			public String call() throws Exception
			{
				throw new RuntimeException("Foo");
			}
		});
		runner.run(taskService, task);
		assertTrue(runner.rollbackTriggered);
		assertEquals(ProcessState.ERROR, task.getProcess().getState());
	}


	@Test
	public void testJaloTransactionException() throws RetryLaterException
	{
		final ProcessTaskModel task = mockTask("foo");
		final TestProcessEngineTaskRunner runner = mockRunner(new Callable<String>()
		{
			@Override
			public String call() throws Exception
			{
				throw new TransactionException("Foo");
			}
		});
		runner.run(taskService, task);
		assertTrue(runner.rollbackTriggered);
		assertEquals(ProcessState.ERROR, task.getProcess().getState());
	}

	@Test
	public void testSpringTransactionException() throws RetryLaterException
	{
		final ProcessTaskModel task = mockTask("foo");
		final TestProcessEngineTaskRunner runner = mockRunner(new Callable<String>()
		{
			@Override
			public String call() throws Exception
			{
				throw new TransactionSystemException("Spring Foo");
			}
		});
		runner.run(taskService, task);
		assertTrue(runner.rollbackTriggered);
		assertEquals(ProcessState.ERROR, task.getProcess().getState());
	}

	@Test
	public void testError() throws RetryLaterException
	{
		final ProcessTaskModel task = mockTask("foo");
		final TestProcessEngineTaskRunner runner = mockRunner(new Callable<String>()
		{
			@Override
			public String call() throws Exception
			{
				throw new LinkageError("Error");
			}
		});
		try
		{
			runner.run(taskService, task);
			fail("LinkageError expected but not thrown");
		}
		catch (final LinkageError e)
		{
			// as expected
		}
		assertTrue(runner.rollbackTriggered);
		assertEquals(ProcessState.ERROR, task.getProcess().getState());
	}


	private ProcessTaskModel mockTask(final String code)
	{
		final ProcessTaskModel testTask = new ProcessTaskModel();
		testTask.setAction("someAction");

		final BusinessProcessModel processengineProcessModel = new BusinessProcessModel();
		processengineProcessModel.setCode(code);
		processengineProcessModel.setState(ProcessState.CREATED);
		processengineProcessModel.setProcessDefinitionName("name");
		testTask.setProcess(processengineProcessModel);
		return testTask;
	}

	private TestProcessEngineTaskRunner mockRunner(final Callable<String> logic)
	{
		final TestProcessEngineTaskRunner runner = new TestProcessEngineTaskRunner(logic);
		runner.setModelService(modelService);
		runner.setProcessDefinitionFactory(processDefinitionFactory);
		runner.setProcessParameterHelper(processParameterHelper);
		runner.setTransactionTemplate(transactionTemplate);
		return runner;
	}

	static class TestProcessEngineTaskRunner extends ProcessengineTaskRunner
	{
		final Callable<String> logic;
		boolean rollbackTriggered = false;

		TestProcessEngineTaskRunner(final Callable<String> logic)
		{
			this.logic = logic;
		}

		// simulate TransactionTemplate behavior without actually using it: assuming that all exceptions mean 'rollback'
		@Override
		protected String runProcessTaskInTransaction(final ProcessTaskModel task) throws RetryLaterException,
				NodeExecutionException
		{
			try
			{
				return runProcessTask(task);
			}
			catch (final RetryLaterException e)
			{
				rollbackTriggered = true;
				throw e;
			}
			catch (final NodeExecutionException e)
			{
				rollbackTriggered = true;
				throw e;
			}
			catch (final RuntimeException e)
			{
				rollbackTriggered = true;
				throw e;
			}
			catch (final Error e)
			{
				rollbackTriggered = true;
				throw e;
			}
			catch (final Throwable e)
			{
				rollbackTriggered = true;
				throw new UndeclaredThrowableException(e);
			}
		}

		@Override
		protected String runProcessTask(final ProcessTaskModel task) throws RetryLaterException, NodeExecutionException
		{
			try
			{
				return logic.call();
			}
			catch (final RetryLaterException e)
			{
				throw e;
			}
			catch (final NodeExecutionException e)
			{
				throw e;
			}
			catch (final RuntimeException e)
			{
				throw e;
			}
			catch (final Error e)
			{
				throw e;
			}
			catch (final Throwable e)
			{
				throw new UndeclaredThrowableException(e);
			}
		}

		@Override
		protected FileAppender startLogging(final BusinessProcessModel process, final ProcessTaskModel task)
		{
			return null;
		}

		@Override
		protected void finishLogging(final BusinessProcessModel process, final ProcessTaskModel task, final FileAppender appender,
				final Date startDate, final String returnCode)
		{
			//
		}
	}
}
