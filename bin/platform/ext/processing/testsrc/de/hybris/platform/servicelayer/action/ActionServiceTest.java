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
package de.hybris.platform.servicelayer.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.action.impl.DefaultActionService;
import de.hybris.platform.servicelayer.action.plain.PlainActionExecutionStrategy;
import de.hybris.platform.servicelayer.action.plain.TriggeredPlainAction;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.servicelayer.model.action.SimpleActionModel;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ByteArrayResource;


@IntegrationTest
public class ActionServiceTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private ActionService actionService;

	@Resource
	private TestActionPerformable testPlainPerformable;

	private ApplicationContext appCtxBefore;
	private PlainActionExecutionStrategy strategy;
	private ApplicationContext testAppCtx;

	private static final String BEAN_DEF = ""// NOPMD
			+ "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n"//
			+ "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"//
			+ "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n"//
			+ "                            http://www.springframework.org/schema/beans/spring-beans-3.1.xsd\">\n" //

			+ "       <bean\n"//
			+ "             id=\"testPlainPerformable\"\n"//
			+ "             class=\"de.hybris.platform.servicelayer.action.TestActionPerformable\"\n"//
			+ ">\n"//
			+ "       </bean>\n"//
			+ "</beans>";

	@Override
	public void prepareApplicationContextAndSession() throws Exception
	{
		final ApplicationContext parentApplicationContext = Registry.getApplicationContext();
		final GenericApplicationContext applicationContext = new GenericApplicationContext();
		applicationContext.setParent(parentApplicationContext);
		//	applicationContext.getBeanFactory().registerScope("tenant", new TenantScope());

		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(applicationContext);
		xmlReader.loadBeanDefinitions(new ByteArrayResource(BEAN_DEF.getBytes()));

		applicationContext.refresh();

		autowireProperties(applicationContext);

		this.testAppCtx = applicationContext;
	}

	@Before
	public void setUp()
	{
		strategy = (PlainActionExecutionStrategy) ((DefaultActionService) actionService).getExecutionStrategyRegistry()
				.getExecutionStrategy(ActionType.PLAIN);
		if (strategy != null)
		{
			appCtxBefore = strategy.getApplicationContext();
			strategy.setApplicationContext(testAppCtx);
		}
	}

	@After
	public void tearDown()
	{
		if (strategy != null)
		{
			strategy.setApplicationContext(appCtxBefore);
		}
	}

	@Test
	public void testPlainAction()
	{
		assertNotNull(testPlainPerformable);

		assertNull(testPlainPerformable.getAction());
		assertNull(testPlainPerformable.getArgument());
		assertEquals(0, testPlainPerformable.getCalls());

		final AbstractActionModel actionModel = new SimpleActionModel();
		actionModel.setCode("action1");
		actionModel.setType(ActionType.PLAIN);
		actionModel.setTarget("testPlainPerformable");
		modelService.save(actionModel);

		final String argument = "This is a Test";
		final TriggeredPlainAction<String> triggeredAction = (TriggeredPlainAction<String>) actionService.prepareAction(
				actionModel, argument);

		assertNull(testPlainPerformable.getAction());
		assertNull(testPlainPerformable.getArgument());
		assertEquals(0, testPlainPerformable.getCalls());

		actionService.triggerAction(triggeredAction);

		assertEquals(actionModel, testPlainPerformable.getAction());
		assertEquals(argument, testPlainPerformable.getArgument());
		assertEquals(1, testPlainPerformable.getCalls());

		assertNotNull(triggeredAction);
		assertEquals(actionModel, triggeredAction.getAction());
		assertEquals(argument, triggeredAction.getArgument());
		assertEquals(testPlainPerformable, triggeredAction.getPerformable());

		final String argument2 = "This is another Test";

		final TriggeredPlainAction<String> triggeredAction2 = (TriggeredPlainAction<String>) actionService.prepareAndTriggerAction(
				actionModel, argument2);

		assertEquals(actionModel, testPlainPerformable.getAction());
		assertEquals(argument2, testPlainPerformable.getArgument());
		assertEquals(2, testPlainPerformable.getCalls());

		assertNotNull(triggeredAction2);
		assertNotSame(triggeredAction, triggeredAction2);
		assertEquals(actionModel, triggeredAction2.getAction());
		assertEquals(argument2, triggeredAction2.getArgument());
		assertEquals(testPlainPerformable, triggeredAction2.getPerformable());

	}

	@Test
	public void testInvalidTarget()
	{
		final AbstractActionModel actionModel = new SimpleActionModel();
		actionModel.setCode("action2");
		actionModel.setType(ActionType.PLAIN);
		actionModel.setTarget("fooBar");
		modelService.save(actionModel);

		final String argument = "This is a Test";
		try
		{
			actionService.prepareAndTriggerAction(actionModel, argument);
			fail("InvalidActionException expected");
		}
		catch (final Exception e)
		{
			assertTrue(e instanceof InvalidActionException);
			final InvalidActionException ex = (InvalidActionException) e;
			assertEquals("fooBar", ex.getTarget());
			assertEquals(ActionType.PLAIN, ex.getType());
		}
	}

}
