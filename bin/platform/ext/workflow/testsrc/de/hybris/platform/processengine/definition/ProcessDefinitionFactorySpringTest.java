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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


@IntegrationTest
public class ProcessDefinitionFactorySpringTest
{
	private DefaultProcessDefinitionFactory processDefinitionFactory;
	private static final String PROCESS_NAME = "process1";
	private static final String START_NODE_ID = "start";
	private static final String ERROR_NODE_ID = "error";
	private static final String RND_NODE_ID = "rnd";
	private static final String SAY_A_NODE_ID = "sayA";
	private static final String WAIT_FOREVER_NODE_ID = "waitForever";
	private static final String FINALLY_NODE_ID = "finally";
	private static final String SUCCESS_NODE_ID = "success";
	private static final String SPLIT_NODE_ID = "split";

	@Before
	public void setUp() throws Exception
	{
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"/processengine/test/processDefinitionFactory-test-spring.xml", getClass());

		final XMLProcessDefinitionsReader xmlDefinitionsReader = new XMLProcessDefinitionsReader(
				applicationContext.getBean("scriptingLanguagesService", ScriptingLanguagesService.class));

		final ProcessDefinitionsProvider processDefinitionsProvider = new TestProcessDefinitionsProvider(xmlDefinitionsReader);
		processDefinitionsProvider.setApplicationContext(applicationContext);

		final ProcessDefinitionsCache processDefinitionsCache = new ProcessDefinitionsCache();
		processDefinitionsCache.setProcessDefinitionsProvider(processDefinitionsProvider);

		processDefinitionFactory = new DefaultProcessDefinitionFactory();
		processDefinitionFactory.setApplicationContext(applicationContext);
		processDefinitionFactory.setDefinitionsCache(processDefinitionsCache);
		processDefinitionFactory.setXmlDefinitionsReader(xmlDefinitionsReader);

		processDefinitionFactory.afterPropertiesSet();
	}

	@Test
	public void testDetectProcessDefinitions() throws Exception
	{
		final ProcessDefinition processDefinition = processDefinitionFactory.getProcessDefinition(new ProcessDefinitionId(
				PROCESS_NAME));
		assertNotNull("Process definition not created", processDefinition);
	}


	@Test
	public void testProcessDefinition() throws Exception
	{
		ActionNode startNode;
		EndNode errorNode;
		EndNode successNode;
		ActionNode rndNode;
		ActionNode sayANode;
		WaitNode waitForeverNode;
		ActionNode finallyNode;
		SplitNode splitNode;

		//Process
		final ProcessDefinition processDefinition = processDefinitionFactory.getProcessDefinition(new ProcessDefinitionId(
				PROCESS_NAME));
		assertNotNull("Process definition not created", processDefinition);
		assertEquals("incorrect PROCESS_NAME", PROCESS_NAME, processDefinition.getName());

		//Start node
		assertEquals("incorrect START_NODE_ID", START_NODE_ID, processDefinition.getStartNode().getId());
		assertTrue("incorrect START_NODE_ID type", ActionNode.class.isInstance(processDefinition.retrieve(START_NODE_ID)));
		startNode = (ActionNode) processDefinition.retrieve(START_NODE_ID);
		assertEquals("incorrect startNode transition", RND_NODE_ID, startNode.getTransistion("OK"));

		//Error node
		assertEquals("ERROR_NODE_ID", ERROR_NODE_ID, processDefinition.retrieve(ERROR_NODE_ID).getId());
		assertTrue("EndNode.class", EndNode.class.isInstance(processDefinition.retrieve(ERROR_NODE_ID)));
		errorNode = (EndNode) processDefinition.retrieve(ERROR_NODE_ID);
		assertEquals("errorNode.getMessage()", "All went wrong.", errorNode.getMessage());
		assertEquals("errorNode.getType()", EndNode.Type.ERROR, errorNode.getType());

		//Success node
		assertEquals("SUCCESS_NODE_ID", SUCCESS_NODE_ID, processDefinition.retrieve(SUCCESS_NODE_ID).getId());
		assertTrue("EndNode.class", EndNode.class.isInstance(processDefinition.retrieve(SUCCESS_NODE_ID)));
		successNode = (EndNode) processDefinition.retrieve(SUCCESS_NODE_ID);
		assertEquals("successNode.getMessage", "Everything was fine", successNode.getMessage());
		assertEquals("successNode.getType", EndNode.Type.SUCCEEDED, successNode.getType());

		//rnd action node
		assertEquals("RND_NODE_ID", RND_NODE_ID, processDefinition.retrieve(RND_NODE_ID).getId());
		assertTrue("ActionNode.class", ActionNode.class.isInstance(processDefinition.retrieve(RND_NODE_ID)));
		rndNode = (ActionNode) processDefinition.retrieve(RND_NODE_ID);
		assertEquals("rndNode.getTransistion(\"OK\")", SAY_A_NODE_ID, rndNode.getTransistion("OK"));
		assertEquals("rndNode.getTransistion(\"NOK\")", SPLIT_NODE_ID, rndNode.getTransistion("NOK"));

		//sayA node
		assertEquals("SAY_A_NODE_ID", SAY_A_NODE_ID, processDefinition.retrieve(SAY_A_NODE_ID).getId());
		assertTrue("ActionNode.class", ActionNode.class.isInstance(processDefinition.retrieve(SAY_A_NODE_ID)));
		sayANode = (ActionNode) processDefinition.retrieve(SAY_A_NODE_ID);
		assertEquals("sayANode.getTransistion(\"OK\")", FINALLY_NODE_ID, sayANode.getTransistion("OK"));

		//wait forever node
		assertEquals("WAIT_FOREVER_NODE_ID", WAIT_FOREVER_NODE_ID, processDefinition.retrieve(WAIT_FOREVER_NODE_ID).getId());
		assertTrue("WaitNode.class", WaitNode.class.isInstance(processDefinition.retrieve(WAIT_FOREVER_NODE_ID)));
		waitForeverNode = (WaitNode) processDefinition.retrieve(WAIT_FOREVER_NODE_ID);
		assertEquals("waitForeverNode.getEevent", "SomethingToHappen", waitForeverNode.getEvent());
		assertEquals("FINALLY_NODE_ID", FINALLY_NODE_ID, waitForeverNode.getThen());

		//split node
		assertEquals("SPLIT_NODE_ID", SPLIT_NODE_ID, processDefinition.retrieve(SPLIT_NODE_ID).getId());
		assertTrue("SplitNode.class", SplitNode.class.isInstance(processDefinition.retrieve(SPLIT_NODE_ID)));
		splitNode = (SplitNode) processDefinition.retrieve(SPLIT_NODE_ID);
		assertTrue("WAIT_FOREVER_NODE_ID", splitNode.getSuccessors().contains(WAIT_FOREVER_NODE_ID));
		assertTrue("ERROR_NODE_ID", splitNode.getSuccessors().contains(ERROR_NODE_ID));
		assertTrue("FINALLY_NODE_ID", splitNode.getSuccessors().contains(FINALLY_NODE_ID));
		assertTrue("splitNode.getSuccessors().size()", splitNode.getSuccessors().size() == 3);

		//finally node
		assertEquals("FINALLY_NODE_ID", FINALLY_NODE_ID, processDefinition.retrieve(FINALLY_NODE_ID).getId());
		assertTrue("ActionNode.class", ActionNode.class.isInstance(processDefinition.retrieve(FINALLY_NODE_ID)));
		finallyNode = (ActionNode) processDefinition.retrieve(FINALLY_NODE_ID);
		assertEquals("SUCCESS_NODE_ID", SUCCESS_NODE_ID, finallyNode.getTransistion("OK"));
	}
}
