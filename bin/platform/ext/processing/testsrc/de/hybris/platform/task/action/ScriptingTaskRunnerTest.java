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
package de.hybris.platform.task.action;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.model.ScriptingTaskModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Tests for ScriptingTaskRunner
 */
@RunWith(MockitoJUnitRunner.class)
public class ScriptingTaskRunnerTest
{
	@Mock
	private ModelService modelService;
	@Mock
	private TaskService taskService;
	@Mock
	private ScriptingLanguagesService scriptingLanguagesService;

	private static final String NON_EXISTING_SCRIPT = "NonExistingScript";
	private static final String WRONG_CONTENT_SCRIPT = "WrongContentScript";
	private static final String CORRECT_SCRIPT = "CorrectScript";

	@Mock
	private ScriptExecutable executableGood, executableBad;

	private TaskRunner<ScriptingTaskModel> taskRunnerAsInterfaceCorrect, taskRunnerAsInterfaceWrong;

	private ScriptingTaskRunner scriptingTaskRunner;

	private ScriptingTaskModel myDynamicTask;
	private ScriptModel script;

	@Before
	public void setUp() throws Exception
	{
		taskRunnerAsInterfaceCorrect = new MyScriptingTaskRunner(false);
		taskRunnerAsInterfaceWrong = new MyScriptingTaskRunner(true);
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceCorrect).errorHandlingExecutedCorrectly).isFalse();
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceCorrect).runExecutedCorrectly).isFalse();

		Mockito.when(scriptingLanguagesService.getExecutableByURI(NON_EXISTING_SCRIPT)).thenThrow(
				new ScriptNotFoundException("Script Not found, wrong URI"));
		Mockito.when(scriptingLanguagesService.getExecutableByURI(CORRECT_SCRIPT)).thenReturn(executableGood);
		Mockito.when(scriptingLanguagesService.getExecutableByURI(WRONG_CONTENT_SCRIPT)).thenReturn(executableBad);
		Mockito.when(executableGood.getAsInterface(TaskRunner.class)).thenReturn(taskRunnerAsInterfaceCorrect);
		Mockito.when(executableBad.getAsInterface(TaskRunner.class)).thenReturn(taskRunnerAsInterfaceWrong);

		scriptingTaskRunner = new ScriptingTaskRunner();
		scriptingTaskRunner.setScriptingLanguagesService(scriptingLanguagesService);

		script = new ScriptModel();
		script.setCode("myGroovyScript");
		script.setContent("println 'hello groovy! '+ new Date()");

		myDynamicTask = new ScriptingTaskModel();
		myDynamicTask.setScriptURI("model://myGroovyScript");
	}

	@Test
	public void testRunCorrectScript() throws Exception
	{
		myDynamicTask.setScriptURI(CORRECT_SCRIPT);
		scriptingTaskRunner.run(taskService, myDynamicTask);
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceCorrect).runExecutedCorrectly).isTrue();
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceCorrect).errorHandlingExecutedCorrectly).isFalse();
	}

	@Test
	public void testRunNotExistingScript() throws Exception
	{
		myDynamicTask.setScriptURI(NON_EXISTING_SCRIPT);
		try
		{
			scriptingTaskRunner.run(taskService, myDynamicTask);
			Assert.fail("ScriptNotFoundException expected but not thrown");
		}
		catch (final Exception e)
		{
			// ok
			assertThat(e).isExactlyInstanceOf(ScriptNotFoundException.class);
		}
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceWrong).runExecutedCorrectly).isFalse();
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceWrong).errorHandlingExecutedCorrectly).isFalse();

	}

	@Test
	public void testRunScriptThrowingNullPointerException() throws Exception
	{
		myDynamicTask.setScriptURI(WRONG_CONTENT_SCRIPT);
		try
		{
			scriptingTaskRunner.run(taskService, myDynamicTask);
			Assert.fail("NullPointerException expected but not thrown");
		}
		catch (final Exception e)
		{
			// ok
			assertThat(e).isExactlyInstanceOf(NullPointerException.class);
		}
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceWrong).runExecutedCorrectly).isFalse();
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceWrong).errorHandlingExecutedCorrectly).isFalse();
	}

	@Test
	public void testHandleErrorInCorrectScript() throws Exception
	{
		myDynamicTask.setScriptURI(CORRECT_SCRIPT);
		scriptingTaskRunner.handleError(taskService, myDynamicTask, new Throwable());
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceCorrect).errorHandlingExecutedCorrectly).isTrue();
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceCorrect).runExecutedCorrectly).isFalse();
	}

	@Test
	public void testHandleErrorInScriptThrowingNullPointerException() throws Exception
	{
		myDynamicTask.setScriptURI(WRONG_CONTENT_SCRIPT);
		try
		{
			scriptingTaskRunner.handleError(taskService, myDynamicTask, new Throwable());
			Assert.fail("NullPointerException expected but not thrown");
		}
		catch (final Exception e)
		{
			// ok
			assertThat(e).isExactlyInstanceOf(NullPointerException.class);
		}
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceWrong).errorHandlingExecutedCorrectly).isFalse();
		assertThat(((MyScriptingTaskRunner) taskRunnerAsInterfaceWrong).runExecutedCorrectly).isFalse();
	}

	private class MyScriptingTaskRunner implements TaskRunner<ScriptingTaskModel>
	{
		private boolean errorHandlingExecutedCorrectly = false;
		private boolean runExecutedCorrectly = false;
		private boolean badContent = false;


		private MyScriptingTaskRunner(final boolean badContent)
		{
			this.badContent = badContent;
		}

		@Override
		public void run(final TaskService taskService, final ScriptingTaskModel task) throws RetryLaterException
		{
			if (badContent)
			{
				assertThat(task.getPk()).isNull();
				task.getPk().getLong();// this will throw null PointerException, which is expected here!
			}
			runExecutedCorrectly = true;
		}

		@Override
		public void handleError(final TaskService taskService, final ScriptingTaskModel task, final Throwable error)
		{
			if (badContent)
			{
				assertThat(task.getPk()).isNull();
				task.getPk().getLong();// this will throw null PointerException, which is expected here!
			}
			errorHandlingExecutedCorrectly = true;
		}
	}
}
