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
package de.hybris.platform.processengine.definition;

import static java.util.Objects.requireNonNull;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.utils.NeedsTaskEngine;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.xml.sax.InputSource;


@IntegrationTest
@NeedsTaskEngine
public class ReloadProcessDefinitionInRuntimeTest extends ServicelayerBaseTest
{
	private static final String JOURNAL_ID = "tests.journal";
	private static final String PROCEDURAL_ACTION_ID = "test.procedural.action";

	@Resource
	private ProcessDefinitionFactory processDefinitionFactory;
	@Resource
	private BusinessProcessService businessProcessService;
	@Resource
	private ProcessDefinitionsCache processDefinitionsCache;
	@Resource
	private ModelService modelService;

	@Before
	public void setUp()
	{
		final ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getBeanFactory();

		beanFactory.registerBeanDefinition(JOURNAL_ID, BeanDefinitionBuilder.rootBeanDefinition(ActionsJournal.class)
				.getBeanDefinition());

		beanFactory.registerBeanDefinition(
				PROCEDURAL_ACTION_ID,
				BeanDefinitionBuilder.rootBeanDefinition(RecordableProceduralAction.class)
						.addConstructorArgValue(PROCEDURAL_ACTION_ID).addConstructorArgReference(JOURNAL_ID).getBeanDefinition());
	}

	@After
	public void tearDown()
	{
		final ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getBeanFactory();

		beanFactory.removeBeanDefinition(PROCEDURAL_ACTION_ID);
		beanFactory.removeBeanDefinition(JOURNAL_ID);
		processDefinitionsCache.clear();
	}

	@Test
	public void processShouldContinueWithOldDefinition() throws InterruptedException, TimeoutException
	{
		final ActionsJournal journal = Registry.getApplicationContext().getBean(JOURNAL_ID, ActionsJournal.class);
		final BusinessProcessModel process100 = runProcess(100);
		waitForAndExecuteAdditionalAction(process100, new Runnable()
		{
			private int counter = 100;

			@Override
			public void run()
			{
				modifyProcessDefinition(counter += 97);
			}
		});
		journal.verify(process100.getCode(), PROCEDURAL_ACTION_ID, 100);
	}

	@Test
	public void processShouldStartWithLatestProcessDefinition() throws InterruptedException, TimeoutException
	{
		final ActionsJournal journal = Registry.getApplicationContext().getBean(JOURNAL_ID, ActionsJournal.class);
		modifyProcessDefinition(100);
		modifyProcessDefinition(200);
		modifyProcessDefinition(300);
		final BusinessProcessModel process400 = runProcess(400);
		waitFor(process400);
		journal.verify(process400.getCode(), PROCEDURAL_ACTION_ID, 400);
	}

	@Test
	public void smokeTest() throws InterruptedException, TimeoutException
	{
		final ActionsJournal journal = Registry.getApplicationContext().getBean(JOURNAL_ID, ActionsJournal.class);
		final BusinessProcessModel process100 = runProcess(100);
		final BusinessProcessModel process200 = runProcess(200);
		final BusinessProcessModel process300 = runProcess(300);
		final BusinessProcessModel process400 = runProcess(400);
		waitFor(process100);
		waitFor(process200);
		waitFor(process300);
		waitFor(process400);
		journal.verify(process100.getCode(), PROCEDURAL_ACTION_ID, 100);
		journal.verify(process200.getCode(), PROCEDURAL_ACTION_ID, 200);
		journal.verify(process300.getCode(), PROCEDURAL_ACTION_ID, 300);
		journal.verify(process400.getCode(), PROCEDURAL_ACTION_ID, 400);
	}

	private BusinessProcessModel runProcess(final int numberOfNodes)
	{
		modifyProcessDefinition(numberOfNodes);
		return businessProcessService.startProcess("process" + numberOfNodes, "process1");
	}

	private void modifyProcessDefinition(final int numberOfNodes)
	{
		processDefinitionFactory.add(new InputSource(new StringReader(getProcess(numberOfNodes))));
	}

	private void waitFor(final BusinessProcessModel model) throws InterruptedException, TimeoutException
	{
		waitForAndExecuteAdditionalAction(model, null);
	}

	private void waitForAndExecuteAdditionalAction(final BusinessProcessModel model, final Runnable action)
			throws InterruptedException, TimeoutException
	{
		final long maxWaitTime = System.currentTimeMillis() + 2 * 60 * 1000;
		while (model.getState() == ProcessState.RUNNING)
		{
			if (System.currentTimeMillis() > maxWaitTime)
			{
				throw new TimeoutException("Wait time exceeded for " + model.getCode());
			}
			if (action != null)
			{
				action.run();
			}
			modelService.refresh(model);
		}
		assertThat(model.getState()).isEqualTo(ProcessState.SUCCEEDED);
	}

	private static String getProcess(final int n)
	{
		final StringBuilder result = new StringBuilder();
		result.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		result.append("<process xmlns=\"http://www.hybris.de/xsd/processdefinition\" start=\"action0\" name=\"process1\">");
		for (int i = 0; i < (n - 1); i++)
		{
			result.append("   <action id=\"action").append(i).append("\" bean=\"test.procedural.action\">");
			result.append("      <transition name=\"OK\" to=\"action").append(i + 1).append("\"/>");
			result.append("   </action>");
		}
		result.append("   <action id=\"action").append(n - 1).append("\" bean=\"test.procedural.action\">");
		result.append("      <transition name=\"OK\" to=\"success\"/>");
		result.append("   </action>");
		result.append("   <end id=\"success\" state=\"SUCCEEDED\">Everything was fine</end>");
		result.append("</process>");
		return result.toString();
	}

	private static class ActionsJournal
	{
		private final Map<String, Map<String, Integer>> records = new HashMap<>();

		public synchronized void record(final String processCode, final String actionId)
		{
			requireNonNull(processCode);
			requireNonNull(actionId);
			Map<String, Integer> actions = records.get(processCode);
			if (actions == null)
			{
				actions = new HashMap<>();
				records.put(processCode, actions);
			}
			Integer cnt = actions.get(actionId);
			if (cnt == null)
			{
				cnt = Integer.valueOf(0);
				actions.put(actionId, cnt);
			}
			actions.put(actionId, Integer.valueOf(cnt.intValue() + 1));
		}

		public synchronized void verify(final String processCode, final String actionId, final int expectedCount)
		{
			assertThat(records.keySet()).contains(processCode);
			assertThat(records.get(processCode).keySet()).contains(actionId);
			assertThat(records.get(processCode).get(actionId)).isNotNull().isEqualTo(expectedCount);
		}
	}

	private static class RecordableProceduralAction extends AbstractProceduralAction<BusinessProcessModel>
	{
		private final ActionsJournal journal;
		private final String id;

		@SuppressWarnings("unused")
		public RecordableProceduralAction(final String id, final ActionsJournal journal)
		{
			this.id = requireNonNull(id);
			this.journal = requireNonNull(journal);
		}

		@Override
		public void executeAction(final BusinessProcessModel process) throws RetryLaterException, Exception
		{
			journal.record(process.getCode(), id);
		}
	}
}
