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
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.support.TransactionTemplate;


@IntegrationTest
public class ProcessengineTaskRunnerTest extends HybrisJUnit4Test
{
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	private TransactionTemplate transactionTemplate;


	@Test
	public void runTest() throws RetryLaterException
	{
		final ProcessTaskModel testTask;

		testTask = new ProcessTaskModel();

		testTask.setAction("someAction");
		final BusinessProcessModel processengineProcessModel = new BusinessProcessModel();
		processengineProcessModel.setCode("code");
		processengineProcessModel.setState(ProcessState.CREATED);
		processengineProcessModel.setProcessDefinitionName("name");
		testTask.setProcess(processengineProcessModel);
		final ProcessengineTaskRunner runner = new ProcessengineTaskRunner()
		{
			@Override
			public void run(final TaskService taskManager, final ProcessTaskModel task) throws RetryLaterException
			{
				super.run(taskManager, task);
				assertEquals("testTask is not equal to task", testTask, task);
			}
		};
		runner.setTransactionTemplate(transactionTemplate);

		final ModelService modelService = EasyMock.createMock(ModelService.class);
		runner.setModelService(modelService);

		final TaskService taskService = EasyMock.createMock(TaskService.class);
		runner.run(taskService, testTask);
	}

	@Test
	public void handleErrorTest()
	{
		final Logger logger = Logger.getLogger(ProcessengineTaskRunner.class);
		final Level level = logger.getLevel();
		try
		{
			logger.setLevel(Level.OFF);
			final ProcessengineTaskRunner taskRunner = new ProcessengineTaskRunner();
			taskRunner.setProcessParameterHelper((ProcessParameterHelper) Registry.getApplicationContext().getBean(
					"processParameterHelper"));
			assertNotNull("taskRunner can't be null", taskRunner);
			final Exception exception = new Exception("test");
			final BusinessProcessModel businessProcess = new BusinessProcessModel();
			businessProcess.setCode("code");
			businessProcess.setState(ProcessState.CREATED);
			businessProcess.setProcessDefinitionName("name");
			final ProcessTaskModel task = new ProcessTaskModel();
			task.setRunnerBean("foo");
			task.setAction("ss");
			task.setProcess(businessProcess);

			task.setExecutionDate(new Date());
			final ModelService modelService = EasyMock.createMock(ModelService.class);
			taskRunner.setModelService(modelService);

			taskRunner.handleError(null, task, exception);
		}
		finally
		{
			logger.setLevel(level);
		}

	}
}
