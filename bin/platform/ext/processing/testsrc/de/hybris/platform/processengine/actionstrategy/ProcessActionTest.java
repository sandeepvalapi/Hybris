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
package de.hybris.platform.processengine.actionstrategy;

import static org.junit.Assert.*;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.definition.ProcessDefinition;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.definition.ProcessDefinitionId;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.helpers.impl.DefaultProcessParameterHelper;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.processengine.process.ProcessengineTaskRunner;
import de.hybris.platform.servicelayer.action.ActionService;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.servicelayer.model.action.SimpleActionModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.utils.NeedsTaskEngine;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Config;

import java.io.StringReader;

import junit.framework.Assert;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.xml.sax.InputSource;


/**
 * Testing PROCESS type actions from Actions framework. See https://jira.hybris.com/browse/BAM-297, test has to be
 * rewritten
 */
@ManualTest
@NeedsTaskEngine
public class ProcessActionTest extends HybrisJUnit4Test
{
	private ApplicationContext applicationContext;
	private ProcessParameterHelper processParameterHelper;
	private ModelService modelService;
	private ActionService actionService;
	private ProcessDefinitionFactory processDefinitionFactory;
	private ProcessengineTaskRunner processengineTaskRunner;
	private static final String ACTION_BEANID = "testProcessAction";
	private static final String ERROR_ACTION_BEANID = "throwErrorAction";
	private TestAction testProcessActionBean;
	private ThrowErrorAction testProcessThrowErrorActionBean;
	private static final String PROCCESS_DEFINITION_NAME = "ProcessActionTest";
	private static final String ERROR_PROCCESS_DEFINITION_NAME = "ErrorProcessActionTest";
	private static final String WAIT_PROCCESS_DEFINITION_NAME = "WaitProcessActionTest";
	private static final String PROCESS_DEFINITION = //
	"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			+ //
			"<process xmlns=\"http://www.hybris.de/xsd/processdefinition\" start=\"start\" name=\"" + PROCCESS_DEFINITION_NAME
			+ "\">" + //
			"<action id=\"start\" bean=\"" + ACTION_BEANID + "\">\n" + //
			"<transition name=\"OK\" to=\"success\"/>\n" + //
			"</action>\n" + //
			"<end id=\"error\" state=\"ERROR\">All went wrong.</end>\n" + //
			"<end id=\"success\" state=\"SUCCEEDED\">Everything was fine</end>\n" + //
			"</process>\n";

	private static final String ERROR_PROCESS_DEFINITION = //
	"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			+ //
			"<process xmlns=\"http://www.hybris.de/xsd/processdefinition\" start=\"start\" name=\"" + ERROR_PROCCESS_DEFINITION_NAME
			+ "\">" + //
			"<action id=\"start\" bean=\"" + ERROR_ACTION_BEANID + "\" >\n" + //
			"<transition name=\"OK\" to=\"success\"/>\n" + //
			"</action>\n" + //
			"<end id=\"error\" state=\"ERROR\">All went wrong.</end>\n" + //
			"<end id=\"success\" state=\"SUCCEEDED\">Everything was fine</end>\n" + //
			"</process>\n";


	private static final String WAIT_PROCESS_DEFINITION = //
	"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			+ //
			"<process xmlns=\"http://www.hybris.de/xsd/processdefinition\" start=\"start\" name=\"" + WAIT_PROCCESS_DEFINITION_NAME
			+ "\">" + //
			"<wait id=\"start\" then=\"success\" prependProcessCode=\"false\">\n" + //
			"<event>name</event>\n" + //
			"</wait>\n" + //
			"<end id=\"error\" state=\"ERROR\">All went wrong.</end>\n" + //
			"<end id=\"success\" state=\"SUCCEEDED\">Everything was fine</end>\n" + //
			"</process>\n";


	private DefaultProcessParameterHelper helper;
	private final double timeFactor = Config.getDouble("platform.test.timefactor", 1.0);

	@Before
	public void setUp() throws Exception
	{
		this.applicationContext = Registry.getGlobalApplicationContext();
		this.modelService = (ModelService) applicationContext.getBean("modelService");
		this.actionService = (ActionService) applicationContext.getBean("actionService");
		processParameterHelper = (ProcessParameterHelper) applicationContext.getBean("processParameterHelper");
		processengineTaskRunner = applicationContext.getBean("taskRunner", ProcessengineTaskRunner.class);

		// set up action for process
		this.testProcessActionBean = new TestAction();
		((AbstractApplicationContext) applicationContext).getBeanFactory().registerSingleton(ACTION_BEANID, testProcessActionBean);

		// set up error action for error process
		this.testProcessThrowErrorActionBean = new ThrowErrorAction();
		((AbstractApplicationContext) applicationContext).getBeanFactory().registerSingleton(ERROR_ACTION_BEANID,
				testProcessThrowErrorActionBean);


		// set up process
		this.processDefinitionFactory = (ProcessDefinitionFactory) applicationContext.getBean("processDefinitionFactory");
		this.processDefinitionFactory.add(new InputSource(new StringReader(PROCESS_DEFINITION)));
		this.processDefinitionFactory.add(new InputSource(new StringReader(ERROR_PROCESS_DEFINITION)));
		this.processDefinitionFactory.add(new InputSource(new StringReader(WAIT_PROCESS_DEFINITION)));
		// set up helper
		helper = new DefaultProcessParameterHelper();
		helper.setModelService(modelService);

	}

	@After
	public void tearDown()
	{
		this.processDefinitionFactory.remove(new ProcessDefinitionId(PROCCESS_DEFINITION_NAME));
		this.processDefinitionFactory.remove(new ProcessDefinitionId(ERROR_PROCCESS_DEFINITION_NAME));
		this.processDefinitionFactory.remove(new ProcessDefinitionId(WAIT_PROCCESS_DEFINITION_NAME));

		try
		{
			((DefaultSingletonBeanRegistry) ((AbstractApplicationContext) applicationContext).getBeanFactory())
					.destroySingleton(ACTION_BEANID);

			((DefaultSingletonBeanRegistry) ((AbstractApplicationContext) applicationContext).getBeanFactory())
					.destroySingleton(ERROR_ACTION_BEANID);
		}
		catch (final Throwable t)
		{
			System.err.println("error removing " + ACTION_BEANID + " from context : " + t.getMessage());
			t.printStackTrace();
		}

		try
		{
			((DefaultSingletonBeanRegistry) ((AbstractApplicationContext) applicationContext).getBeanFactory())
					.destroySingleton(ERROR_ACTION_BEANID);
		}
		catch (final Throwable t)
		{
			System.err.println("error removing " + ERROR_ACTION_BEANID + " from context : " + t.getMessage());
			t.printStackTrace();
		}


		this.modelService = null;
		this.actionService = null;
		this.testProcessActionBean = null;
		this.processDefinitionFactory = null;
		this.helper = null;
	}

	@Test
	public void testProcessAction()
	{
		assertNotNull(processDefinitionFactory.getProcessDefinition(new ProcessDefinitionId(PROCCESS_DEFINITION_NAME)));
		assertNotNull(testProcessActionBean);
		assertNull(testProcessActionBean.process);
		assertEquals(0, testProcessActionBean.calls);

		// action 1

		final AbstractActionModel actionModel = new SimpleActionModel();
		actionModel.setCode("action1");
		actionModel.setType(ActionType.PROCESS);
		actionModel.setTarget(PROCCESS_DEFINITION_NAME);
		modelService.save(actionModel);

		final String argument = "This is a Test";
		final TriggeredProcessAction<String> triggeredAction = (TriggeredProcessAction<String>) actionService.prepareAction(
				actionModel, argument);

		assertNotNull(triggeredAction);
		assertEquals(actionModel, triggeredAction.getAction());
		assertEquals(argument, triggeredAction.getArgument());
		assertNull(testProcessActionBean.process);
		assertEquals(0, testProcessActionBean.calls);

		BusinessProcessModel process = triggeredAction.getProcess();

		assertNotNull(process);
		assertEquals(actionModel, getProcessParameter(process, ProcessActionExecutionStrategy.ACTION_PARAM));
		assertEquals(argument, getProcessParameter(process, ProcessActionExecutionStrategy.ARGUMENT_PARAM));

		actionService.triggerAction(triggeredAction);

		assertTrue("process did not run or wasn't successful", waitForProcessSuccess(process, 40 * 1000));
		assertEquals(process, testProcessActionBean.process);
		assertEquals(1, testProcessActionBean.calls);
		assertEquals(actionModel, getProcessParameter(testProcessActionBean.process, ProcessActionExecutionStrategy.ACTION_PARAM));
		assertEquals(argument, getProcessParameter(testProcessActionBean.process, ProcessActionExecutionStrategy.ARGUMENT_PARAM));

		final String argument2 = "This is another Test";

		final TriggeredProcessAction<String> triggeredAction2 = (TriggeredProcessAction<String>) actionService
				.prepareAndTriggerAction(actionModel, argument2);

		assertNotNull(triggeredAction2.getProcess());
		process = triggeredAction2.getProcess();
		assertTrue("task 2 did not complete", waitForProcessSuccess(process, 40 * 1000));

		assertEquals(process, testProcessActionBean.process);
		assertEquals(2, testProcessActionBean.calls);
		assertEquals(actionModel, getProcessParameter(testProcessActionBean.process, ProcessActionExecutionStrategy.ACTION_PARAM));
		assertEquals(argument2, getProcessParameter(testProcessActionBean.process, ProcessActionExecutionStrategy.ARGUMENT_PARAM));
		assertNotNull(triggeredAction2);
		assertNotSame(triggeredAction, triggeredAction2);
		assertEquals(actionModel, triggeredAction2.getAction());
		assertEquals(argument2, triggeredAction2.getArgument());
	}

	@Test
	public void testWaitProcessState()
	{
		final ProcessDefinition processDefinition = processDefinitionFactory.getProcessDefinition(new ProcessDefinitionId(
				WAIT_PROCCESS_DEFINITION_NAME));
		assertNotNull(processDefinition);
		final BusinessProcessModel process;
		Class classDefinition;
		try
		{
			classDefinition = Class.forName(processDefinition.getProcessClass());
		}
		catch (final ClassNotFoundException e)
		{
			final RuntimeException exc = new RuntimeException(e);
			throw exc;
		}
		try
		{
			process = (BusinessProcessModel) classDefinition.newInstance();
		}
		catch (final InstantiationException e)
		{
			final RuntimeException exc = new RuntimeException(e);
			throw exc;
		}
		catch (final IllegalAccessException e)
		{
			final RuntimeException exc = new RuntimeException(e);
			throw exc;
		}

		process.setCode(WAIT_PROCCESS_DEFINITION_NAME);
		process.setProcessDefinitionName(WAIT_PROCCESS_DEFINITION_NAME);
		process.setState(ProcessState.CREATED);
		modelService.save(process);

		processDefinition.start(process);
		try
		{
			Thread.sleep((long) (200 * timeFactor));
		}
		catch (final InterruptedException e)
		{
			//fine
		}
		modelService.refresh(process);
		assertEquals(ProcessState.WAITING, process.getProcessState());

	}

	@Test
	public void testErrorProcessAction() throws RetryLaterException
	{
		try
		{
			TestUtils.disableFileAnalyzer(400);

			assertNotNull(processDefinitionFactory.getProcessDefinition(new ProcessDefinitionId(ERROR_PROCCESS_DEFINITION_NAME)));

			assertNotNull(testProcessThrowErrorActionBean);

			assertNull(testProcessThrowErrorActionBean.process);
			assertEquals(0, testProcessThrowErrorActionBean.calls);

			final ProcessDefinition processDefinition = processDefinitionFactory.getProcessDefinition(new ProcessDefinitionId(
					ERROR_PROCCESS_DEFINITION_NAME));

			final BusinessProcessModel process;

			Class classDefinition;
			try
			{
				classDefinition = Class.forName(processDefinition.getProcessClass());
			}
			catch (final ClassNotFoundException e)
			{
				final RuntimeException exc = new RuntimeException(e);
				throw exc;
			}
			try
			{
				process = (BusinessProcessModel) classDefinition.newInstance();
			}
			catch (final InstantiationException e)
			{
				final RuntimeException exc = new RuntimeException(e);
				throw exc;
			}
			catch (final IllegalAccessException e)
			{
				final RuntimeException exc = new RuntimeException(e);
				throw exc;
			}

			process.setCode(ERROR_PROCCESS_DEFINITION_NAME);
			process.setProcessDefinitionName(ERROR_PROCCESS_DEFINITION_NAME);
			process.setState(ProcessState.CREATED);
			modelService.save(process);

			process.setState(ProcessState.RUNNING);
			modelService.save(process);

			final TaskService taskService = EasyMock.createMock(TaskService.class);
			final ProcessTaskModel processTaskModel = modelService.create(ProcessTaskModel.class);
			processTaskModel.setProcess(process);
			processTaskModel.setAction("start");

			processengineTaskRunner.run(taskService, processTaskModel);
			modelService.refresh(process);
			Assert.assertNotNull("No process logs", processParameterHelper.getProcessParameterByName(process, "errorStackTrace"));
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	protected Object getProcessParameter(final BusinessProcessModel process, final String name)
	{
		// TODO why can't helper already offer object getters instead of the wrapping parameter model ?
		final BusinessProcessParameterModel parameter = helper.getProcessParameterByName(process, name);
		return parameter != null ? parameter.getValue() : null;
	}

	protected boolean waitForProcessSuccess(final BusinessProcessModel processModel, final long maxWait)
	{
		final long t1 = System.currentTimeMillis();
		boolean done = false;
		do
		{
			try
			{
				Thread.sleep((long) (200 * timeFactor));
			}
			catch (final InterruptedException e)
			{
				//fine
			}
			modelService.refresh(processModel);
			done = processModel.getState() == ProcessState.SUCCEEDED || processModel.getState() == ProcessState.ERROR
					|| processModel.getState() == ProcessState.FAILED;
		}
		while (!done && (System.currentTimeMillis() - t1) < maxWait * timeFactor);

		return processModel.getState() == ProcessState.SUCCEEDED;
	}
}
