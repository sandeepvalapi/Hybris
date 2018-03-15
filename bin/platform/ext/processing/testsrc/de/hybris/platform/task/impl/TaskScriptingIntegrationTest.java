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
package de.hybris.platform.task.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.daos.TitleDao;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.model.ScriptingTaskModel;
import de.hybris.platform.task.utils.NeedsTaskEngine;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Config;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration tests for task scripting
 */
@IntegrationTest
@NeedsTaskEngine
public class TaskScriptingIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private TaskService taskService;
	@Resource
	private ModelService modelService;

	@Resource
	TitleDao titleDao;
	ScriptingTaskModel scriptingTaskForExistingScript;
	ScriptingTaskModel scriptingTaskForNotExistingScript;
	ScriptingTaskModel scriptingTaskForBadScript;

	private static final String CORRECT_TITLE = "CorrectGroovyTitle";
	private static final String ERROR_TITLE = "ErrorGroovyTitle";

	final double timeFactor = Math.max(15.0, Config.getDouble("platform.test.timefactor", 15.0));

	@Before
	public void setUp() throws Exception
	{
		scriptingTaskForExistingScript = modelService.create(ScriptingTaskModel.class);
		scriptingTaskForExistingScript.setScriptURI("classpath://task/test/test-script-correct-task-interface.groovy");

		scriptingTaskForNotExistingScript = modelService.create(ScriptingTaskModel.class);
		scriptingTaskForNotExistingScript.setScriptURI("model://myGroovyScriptDoesNotExist");

		scriptingTaskForBadScript = modelService.create(ScriptingTaskModel.class);
		scriptingTaskForBadScript.setScriptURI("classpath://task/test/test-script-error-task-interface.groovy");

		assertThat(titleDao.findTitleByCode(CORRECT_TITLE)).isNull();
		assertThat(titleDao.findTitleByCode(ERROR_TITLE)).isNull();
		assertThat(taskService.getEngine().isRunning()).isTrue();
	}
	
	@Test
	public void testRunTaskForCorrectScript() throws Exception
	{
		taskService.scheduleTask(scriptingTaskForExistingScript);
		Thread.sleep((long) (1000 * timeFactor));

		assertThat(titleDao.findTitleByCode(CORRECT_TITLE)).isNotNull();
		assertThat(titleDao.findTitleByCode(ERROR_TITLE)).isNull();
	}

	@Test
	public void testRunTaskForCorrectScriptEventBased() throws Exception
	{
		final TaskConditionModel condition = modelService.create(TaskConditionModel.class);
		condition.setUniqueID("MyEvent");
		scriptingTaskForExistingScript.setConditions(Collections.singleton(condition));

		taskService.scheduleTask(scriptingTaskForExistingScript);
		Thread.sleep((long) (1000 * timeFactor));
		// not triggered yet - nothing happens
		assertThat(titleDao.findTitleByCode(CORRECT_TITLE)).isNull();
		assertThat(titleDao.findTitleByCode(ERROR_TITLE)).isNull();

		taskService.triggerEvent("MyEvent");
		Thread.sleep((long) (100 * timeFactor));

		assertThat(titleDao.findTitleByCode(CORRECT_TITLE)).isNotNull();
		assertThat(titleDao.findTitleByCode(ERROR_TITLE)).isNull();
	}

	@Test
	public void testRunTaskForNonExistingScript() throws Exception
	{
		taskService.scheduleTask(scriptingTaskForNotExistingScript);
		Thread.sleep((long) (1000 * timeFactor));
		// during script lookup - ScripNotFoundException is thrown but the task execution logic swallows it
		// This way the Exception is not visible inside the taskRunner (only error logs)
		assertThat(titleDao.findTitleByCode(CORRECT_TITLE)).isNull();
		assertThat(titleDao.findTitleByCode(ERROR_TITLE)).isNull();
	}

	@Test
	public void testRunTaskScriptThrowingException() throws Exception
	{
		TestUtils.disableFileAnalyzer("de.hybris.platform.task.impl.TaskScriptingIntegrationTest.testRunTaskScriptThrowingException()");
		try
		{
   		taskService.scheduleTask(scriptingTaskForBadScript);
   		Thread.sleep((long) (1000 * timeFactor));
   		assertThat(titleDao.findTitleByCode(CORRECT_TITLE)).isNull();
   		assertThat(titleDao.findTitleByCode(ERROR_TITLE)).isNotNull();
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}
}
