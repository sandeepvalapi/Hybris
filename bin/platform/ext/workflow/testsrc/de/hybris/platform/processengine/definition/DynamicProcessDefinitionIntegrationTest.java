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
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.utils.NeedsTaskEngine;

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


@IntegrationTest
@NeedsTaskEngine
public class DynamicProcessDefinitionIntegrationTest extends ServicelayerBaseTest
{
	private static final int INITIAL_NUMBER_OF_NODES = 100;
	private static final int UPDATED_NUMBER_OF_NODES = 200;
	private static final String INITIAL_CONTET = getProcessDefinition(INITIAL_NUMBER_OF_NODES);
	private static final String UPDATED_CONTENT = getProcessDefinition(UPDATED_NUMBER_OF_NODES);
	private static final String TEST_DEFINITION_CODE = "TEST_DEFINITION";
	private static final String TEST_PROCESS_CODE = "TEST_PROCESS";
	private static final String JOURNAL_ID = "tests.journal";
	private static final String PROCEDURAL_ACTION_ID = "test.procedural.action";

	@Resource
	private BusinessProcessService businessProcessService;

	@Resource
	private ModelService modelService;

	@Resource
	private ProcessDefinitionsCache processDefinitionsCache;

	@Before
	public void setUp()
	{
		final ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getBeanFactory();

		beanFactory.registerBeanDefinition(JOURNAL_ID,
				BeanDefinitionBuilder.rootBeanDefinition(ActionsJournal.class).setScope("singleton").getBeanDefinition());

		beanFactory.registerBeanDefinition(PROCEDURAL_ACTION_ID,
				BeanDefinitionBuilder.rootBeanDefinition(RecordableProceduralAction.class).setScope("singleton")
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
	public void businessProcessShouldPickUpTheOnlyOneExistingProcessDefinition() throws InterruptedException,
            TimeoutException
    {
		final ActionsJournal journal = Registry.getApplicationContext().getBean(JOURNAL_ID, ActionsJournal.class);
		givenInitialProcessDefinition();

		final BusinessProcessModel businessProcess = businessProcessService.startProcess(TEST_PROCESS_CODE, TEST_DEFINITION_CODE);
		waitFor(businessProcess);

		assertThat(businessProcess.getProcessDefinitionName()).isEqualTo(TEST_DEFINITION_CODE);
		assertThat(businessProcess.getProcessDefinitionVersion()).isNotNull().isEqualTo(Long.toString(0L));
		journal.verify(businessProcess.getCode(), PROCEDURAL_ACTION_ID, INITIAL_NUMBER_OF_NODES);
	}

	@Test
	public void businessProcessShouldPickUpLatestProcessDefinition() throws InterruptedException, TimeoutException
    {
		final ActionsJournal journal = Registry.getApplicationContext().getBean(JOURNAL_ID, ActionsJournal.class);
		givenUpdatedProcessDefinition();

		final BusinessProcessModel businessProcess = businessProcessService.startProcess(TEST_PROCESS_CODE, TEST_DEFINITION_CODE);
		waitFor(businessProcess);

		assertThat(businessProcess.getProcessDefinitionName()).isEqualTo(TEST_DEFINITION_CODE);
		assertThat(businessProcess.getProcessDefinitionVersion()).isNotNull().isEqualTo(Long.toString(1L));
		journal.verify(businessProcess.getCode(), PROCEDURAL_ACTION_ID, UPDATED_NUMBER_OF_NODES);
	}

	@Test
	public void existingNotStartedBusinessProcessShouldPickUpLatestProcessDefinition() throws InterruptedException,
            TimeoutException
    {
		final ActionsJournal journal = Registry.getApplicationContext().getBean(JOURNAL_ID, ActionsJournal.class);
		final DynamicProcessDefinitionModel initialProcessDefinition = givenInitialProcessDefinition();

		final BusinessProcessModel businessProcess = businessProcessService.createProcess(TEST_PROCESS_CODE, TEST_DEFINITION_CODE);
		givenUpdatedProcessDefinitionOf(initialProcessDefinition);
		businessProcessService.startProcess(businessProcess);
		waitFor(businessProcess);

		assertThat(businessProcess.getProcessDefinitionName()).isEqualTo(TEST_DEFINITION_CODE);
		assertThat(businessProcess.getProcessDefinitionVersion()).isNotNull().isEqualTo(Long.toString(1L));
		journal.verify(businessProcess.getCode(), PROCEDURAL_ACTION_ID, UPDATED_NUMBER_OF_NODES);
	}

	@Test
	public void runningBusinessProcessShouldFinishWithOldProcessDefinition() throws InterruptedException,
            TimeoutException
    {
		final ActionsJournal journal = Registry.getApplicationContext().getBean(JOURNAL_ID, ActionsJournal.class);
		final DynamicProcessDefinitionModel initialProcessDefinition = givenInitialProcessDefinition();

		final BusinessProcessModel businessProcess = businessProcessService.createProcess(TEST_PROCESS_CODE, TEST_DEFINITION_CODE);
		businessProcessService.startProcess(businessProcess);
		final DynamicProcessDefinitionModel latestProcessDefinition = givenUpdatedProcessDefinitionOf(initialProcessDefinition);
		waitFor(businessProcess);

		assertThat(businessProcess.getProcessDefinitionName()).isEqualTo(TEST_DEFINITION_CODE);
		assertThat(businessProcess.getProcessDefinitionVersion()).isNotNull().isNotEqualTo(latestProcessDefinition.getChecksum());
		journal.verify(businessProcess.getCode(), PROCEDURAL_ACTION_ID, INITIAL_NUMBER_OF_NODES);
	}

	private DynamicProcessDefinitionModel givenUpdatedProcessDefinition()
	{
		final DynamicProcessDefinitionModel result = givenInitialProcessDefinition();
		return givenUpdatedProcessDefinitionOf(result);
	}

	private DynamicProcessDefinitionModel givenUpdatedProcessDefinitionOf(final DynamicProcessDefinitionModel initialDefinition)
	{
		initialDefinition.setContent(UPDATED_CONTENT);
		modelService.save(initialDefinition);
		return initialDefinition;
	}

	private DynamicProcessDefinitionModel givenInitialProcessDefinition()
	{
		final DynamicProcessDefinitionModel result = modelService.create(DynamicProcessDefinitionModel.class);
		result.setContent(INITIAL_CONTET);
		result.setCode(TEST_DEFINITION_CODE);
		modelService.save(result);
		return result;
	}

	private void waitFor(final BusinessProcessModel model) throws InterruptedException, TimeoutException
    {
        final long maxWaitTime = System.currentTimeMillis() + 2 * 60 * 1000;
        while (model.getState() == ProcessState.RUNNING)
        {
            if (System.currentTimeMillis() > maxWaitTime)
            {
                throw new TimeoutException("Wait time exceeded for " + model.getCode());
            }
            modelService.refresh(model);
        }
        assertThat(model.getState()).isEqualTo(ProcessState.SUCCEEDED);
	}

	private static String getProcessDefinition(final int n)
	{
		final StringBuilder result = new StringBuilder();
		result.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		result.append("<process xmlns=\"http://www.hybris.de/xsd/processdefinition\" start=\"action0\" name=\"")
				.append(TEST_DEFINITION_CODE).append("\">");
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
			assertThat(records.containsKey(processCode)).isTrue();
			assertThat(records.get(processCode).containsKey(actionId)).isTrue();
			assertThat(records.get(processCode).get(actionId)).isEqualTo(expectedCount);
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
