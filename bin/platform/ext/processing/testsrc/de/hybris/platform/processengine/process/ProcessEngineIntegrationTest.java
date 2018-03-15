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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskLogModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import de.hybris.platform.util.Utilities;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;


@IntegrationTest
public class ProcessEngineIntegrationTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(ProcessEngineIntegrationTest.class);

	public static final String TEST_PROCESS = "test-proc";
	public static final String PROCESS_DEFINITION_NAME = "custUserTestProc";

	public static final String TEST_ACTION_BEAN = "TestActionBean";
	public static final String TEST_USER = "Tom";
	public static final String TEST_LOG_MSG = "9082Test2342";

	private final ClassPathResource processDefinition = new ClassPathResource("processengine/test/customUserTaskTest.xml");

	@Resource
	private BusinessProcessService businessProcessService;

	@Resource
	private ModelService modelService;

	@Resource
	private ProcessDefinitionFactory processDefinitionFactory;

	@Resource
	private UserService userService;

	@Resource
	private SessionService sessionService;


	private TestCustomUserAction testActionBean;

	private DefaultListableBeanFactory beanFactory;

	@Before
	public void doBefore() throws IOException
	{
		final ConfigurableApplicationContext appCtx = (ConfigurableApplicationContext) Registry.getApplicationContext();
		beanFactory = (DefaultListableBeanFactory) appCtx.getBeanFactory();

		testActionBean = new TestCustomUserAction(userService);
		beanFactory.registerSingleton(TEST_ACTION_BEAN, testActionBean);
		processDefinitionFactory.add(processDefinition);

		final BusinessProcessModel bpm = modelService.create(BusinessProcessModel.class);

		bpm.setCode(TEST_PROCESS);
		bpm.setProcessDefinitionName(PROCESS_DEFINITION_NAME);


		final UserModel tomUser = modelService.create(UserModel.class);
		tomUser.setUid(TEST_USER);

		modelService.saveAll();
	}

	@After
	public void doAfter()
	{
		beanFactory.destroySingleton(TEST_ACTION_BEAN);
	}


	@Test
	public void testRunningProcessAsCustomUser() throws InterruptedException, TimeoutException
	{
		// given
		final int tempFilesBefore = countProcessLogsTempFiles();
		final BusinessProcessModel bpm = businessProcessService.getProcess(TEST_PROCESS);
		bpm.setUser(userService.getUserForUID(TEST_USER));

		// when
		businessProcessService.startProcess(bpm);
		waitForBusinessProcess(bpm);

		// then
		final String uid = testActionBean.getUidQueue().poll(5, TimeUnit.SECONDS);
		assertThat(uid).isEqualTo(TEST_USER);

		final int tempFilesAfter = countProcessLogsTempFiles();
		assertThat(tempFilesAfter).isEqualTo(tempFilesBefore);
	}


	@Test
	public void shouldSaveLogsAsProcessTaskLogModel() throws InterruptedException, TimeoutException
	{
		// given
		final int tempFilesBefore = countProcessLogsTempFiles();
		final BusinessProcessModel bpm = businessProcessService.getProcess(TEST_PROCESS);

		// when
		businessProcessService.startProcess(bpm);
		waitForBusinessProcess(bpm);

		// then
		final Collection<ProcessTaskLogModel> taskLogs = bpm.getTaskLogs();

		assertThat(taskLogs).hasSize(1);

		final String logMessages = taskLogs.iterator().next().getLogMessages();
		assertThat(logMessages).contains(TEST_LOG_MSG);

		final int tempFilesAfter = countProcessLogsTempFiles();
		assertThat(tempFilesAfter).isEqualTo(tempFilesBefore);
	}

	int countProcessLogsTempFiles()
	{
		final File platformTempDir = Utilities.getPlatformTempDir();
		return platformTempDir.listFiles((dir, name) -> name.startsWith("task") && name.endsWith("log.txt")).length;
	}

	private void waitForBusinessProcess(final BusinessProcessModel bpm) throws TimeoutException
	{
		final long maxWaitTime = System.currentTimeMillis() + 2 * 60 * 1000;
		do
		{
			try
			{
				if (System.currentTimeMillis() > maxWaitTime)
				{
					throw new TimeoutException("Wait time exceeded for " + bpm.getCode());
				}

				final ProcessState state = bpm.getState();

				if (state == ProcessState.SUCCEEDED || state == ProcessState.ERROR || state == ProcessState.FAILED)
				{
					return;
				}
				Thread.sleep(1000);
				modelService.refresh(bpm);
			}
			catch (final InterruptedException e)
			{
				throw new SystemException(e);
			}
		}
		while (true);
	}

	@Test
	public void testRunningProcessAsAdmin() throws InterruptedException
	{
		// given
		final BusinessProcessModel bpm = businessProcessService.getProcess(TEST_PROCESS);
		bpm.setUser(userService.getAdminUser());

		// when
		businessProcessService.startProcess(bpm);

		// then
		final String uid = testActionBean.getUidQueue().poll(5, TimeUnit.SECONDS);
		assertThat(uid).isEqualTo("admin");
	}


	@Test
	public void testRunningProcessAsAnonymous() throws InterruptedException
	{
		// given
		final BusinessProcessModel bpm = businessProcessService.getProcess(TEST_PROCESS);

		// when
		businessProcessService.startProcess(bpm);

		// then
		final String uid = testActionBean.getUidQueue().poll(5, TimeUnit.SECONDS);
		assertThat(uid).isEqualTo("anonymous");
	}


	@Test
	public void testSessionClosingResetsUser()
	{
		sessionService.createNewSession();
		try
		{
			userService.setCurrentUser(userService.getUserForUID(TEST_USER));
		}
		finally
		{
			sessionService.closeCurrentSession();
		}

		assertThat(userService.getCurrentUser().getUid()).isEqualTo("anonymous");
	}



	public class TestCustomUserAction extends AbstractProceduralAction
	{
		private final UserService userService;
		private final BlockingQueue<String> uidQueue = new ArrayBlockingQueue(1);

		public TestCustomUserAction(final UserService userService)
		{
			this.userService = userService;
		}

		@Override
		public void executeAction(final BusinessProcessModel process) throws Exception
		{
			LOG.info(TEST_LOG_MSG);

			final String uid = userService.getCurrentUser().getUid();
			uidQueue.put(uid);
		}

		public BlockingQueue<String> getUidQueue()
		{
			return uidQueue;
		}
	}
}
