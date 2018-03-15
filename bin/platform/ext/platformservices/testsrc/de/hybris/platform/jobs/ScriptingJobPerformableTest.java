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
package de.hybris.platform.jobs;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.engine.impl.DefaultScriptExecutionResult;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.internal.model.ScriptingJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ScriptingJobPerformableTest
{
	@Mock
	private ModelService modelService;
	@Mock
	private ScriptingLanguagesService scriptingLanguagesService;

	private static final String NON_EXISTING_SCRIPT = "NonExistingScript";
	private static final String WRONG_CONTENT_SCRIPT = "WrongContentScript";
	private static final String CORRECT_SCRIPT_NO_RESULT = "CorrectScriptWithNoResult";
	private static final String CORRECT_SCRIPT_WITH_RESULT = "CorrectScriptWithPerformResult";

	private ScriptExecutionResult correctScriptExecutionResultWithNoResult;
	private ScriptExecutionResult wrongScriptExecutionResult;
	private ScriptExecutionResult correctScriptExecutionResultWithPerformResult;

	@Mock
	private ScriptExecutable executableGood, executableBad, executableGoodWithPerformResult;

	private ScriptingJobPerformable performable;

	private ScriptingJobModel myDynamicJob;
	private CronJobModel myDynamicCronJob;
	private ScriptModel script;

	@Before
	public void setUp() throws Exception
	{
		script = new ScriptModel();
		script.setCode("myGroovyScript");
		script.setContent("println 'hello groovy! '+ new Date()+ ' Crojob='+cronjob");

		myDynamicJob = new ScriptingJobModel();
		myDynamicJob.setCode("myDynamicJob");
		myDynamicJob.setScriptURI("model://myGroovyScript");

		myDynamicCronJob = new CronJobModel();
		myDynamicCronJob.setCode("myDynamicCronJob");
		myDynamicCronJob.setJob(myDynamicJob);

		final ScriptContext scriptContextOk = prepareScriptContext();
		final ScriptContext scriptContextWithErrors = prepareScriptContext();
		scriptContextWithErrors.getErrorWriter().write("error nr 1");
		correctScriptExecutionResultWithNoResult = new DefaultScriptExecutionResult(null, scriptContextOk);
		wrongScriptExecutionResult = new DefaultScriptExecutionResult(null, scriptContextWithErrors);
		correctScriptExecutionResultWithPerformResult = new DefaultScriptExecutionResult(new PerformResult(CronJobResult.UNKNOWN,
				CronJobStatus.PAUSED), scriptContextOk);

		final Map<String, Object> params = new HashMap<>();
		params.put("cronjob", myDynamicCronJob);
		params.put("log", Logger.getLogger(ScriptingJobPerformable.class.getName()));

		Mockito.when(scriptingLanguagesService.getExecutableByURI(NON_EXISTING_SCRIPT)).thenThrow(
				new ScriptNotFoundException("Script Not found, wrong URI"));
		Mockito.when(scriptingLanguagesService.getExecutableByURI(CORRECT_SCRIPT_NO_RESULT)).thenReturn(executableGood);
		Mockito.when(scriptingLanguagesService.getExecutableByURI(WRONG_CONTENT_SCRIPT)).thenReturn(executableBad);
		Mockito.when(scriptingLanguagesService.getExecutableByURI(CORRECT_SCRIPT_WITH_RESULT)).thenReturn(
				executableGoodWithPerformResult);
		Mockito.when(executableGood.execute(params)).thenReturn(correctScriptExecutionResultWithNoResult);
		Mockito.when(executableBad.execute(params)).thenReturn(wrongScriptExecutionResult);
		Mockito.when(executableGoodWithPerformResult.execute(params)).thenReturn(correctScriptExecutionResultWithPerformResult);
		performable = new ScriptingJobPerformable();
		performable.setScriptingLanguagesService(scriptingLanguagesService);
	}

	@Test
	public void testPerformCorrectScript() throws Exception
	{
		myDynamicJob.setScriptURI(CORRECT_SCRIPT_NO_RESULT);
		final PerformResult performResult = performable.perform(myDynamicCronJob);
		assertThat(performResult).isNotNull();
		assertThat(performResult.getResult()).isEqualTo(CronJobResult.SUCCESS);
		assertThat(performResult.getStatus()).isEqualTo(CronJobStatus.FINISHED);
	}

	@Test
	public void testPerformCorrectScriptReturningResult() throws Exception
	{
		myDynamicJob.setScriptURI(CORRECT_SCRIPT_WITH_RESULT);
		final PerformResult performResult = performable.perform(myDynamicCronJob);
		assertThat(performResult).isNotNull();
		assertThat(performResult.getResult()).isEqualTo(CronJobResult.UNKNOWN);
		assertThat(performResult.getStatus()).isEqualTo(CronJobStatus.PAUSED);
	}

	@Test
	public void testPerformWithNonExistingScript() throws Exception
	{
		myDynamicJob.setScriptURI(NON_EXISTING_SCRIPT);
		PerformResult performResult = null;
		try
		{
			performResult = performable.perform(myDynamicCronJob);
			Assert.fail("ScriptNotFoundException expected but not thrown");
		}
		catch (final Exception e)
		{
			// ok
			assertThat(e).isExactlyInstanceOf(ScriptNotFoundException.class);
			assertThat(performResult).isNull();
		}
	}

	@Test
	public void testPerformScriptWithErrors() throws Exception
	{
		myDynamicJob.setScriptURI(WRONG_CONTENT_SCRIPT);
		final PerformResult performResult = performable.perform(myDynamicCronJob);
		assertThat(performResult).isNotNull();
		assertThat(performResult.getResult()).isEqualTo(CronJobResult.ERROR);
		assertThat(performResult.getStatus()).isEqualTo(CronJobStatus.FINISHED);
	}

	private ScriptContext prepareScriptContext()
	{
		final ScriptContext scriptContext = new SimpleScriptContext();
		scriptContext.setWriter(new StringWriter());
		scriptContext.setErrorWriter(new StringWriter());
		return scriptContext;
	}
}
