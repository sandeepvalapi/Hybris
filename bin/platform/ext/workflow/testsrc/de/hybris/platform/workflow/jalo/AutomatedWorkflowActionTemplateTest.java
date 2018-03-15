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
package de.hybris.platform.workflow.jalo;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.jalo.JaloSystemException;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Testing logic of {@link AutomatedWorkflowActionTemplate} class
 */
@UnitTest
public class AutomatedWorkflowActionTemplateTest
{
	@Mock
	private WorkflowAction action1;

	@Mock
	private WorkflowAction action2;

	private MyTestProperJobClass myTestProperJobClass;

	private MyTestProperHandlerClass myTestProperHandlerClass;

	private WorkflowAutomatedAction workflowAutomatedAction;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		myTestProperJobClass = new MyTestProperJobClass();
		myTestProperHandlerClass = new MyTestProperHandlerClass();
		workflowAutomatedAction = new WorkflowAutomatedAction();
	}

	@Test
	public void testPerformJobClass() throws InstantiationException, IllegalAccessException
	{
		final WorkflowDecision workflowdecision = new WorkflowDecision();
		given(action1.getDecisions()).willReturn(Collections.singletonList(workflowdecision));
		BDDMockito.willDoNothing().given(action1).setDecisions(Mockito.anyCollection());
		BDDMockito.willDoNothing().given(action1).activate();

		myTestProperJobClass.perform(action1);
		verify(action1).decide();

	}

	@Test
	public void testPerformJobHandler() throws InstantiationException, IllegalAccessException
	{
		final WorkflowDecision workflowdecision = new WorkflowDecision();
		given(action2.getDecisions()).willReturn(Collections.singletonList(workflowdecision));
		//BDDMockito.willDoNothing().given(action2).setDecisions(Mockito.anyCollection());
		BDDMockito.willDoNothing().given(action2).activate();

		myTestProperHandlerClass.perform(action2);
		verify(action2).decide();

	}

	@Test(expected = AutomatedWorkflowActionException.class)
	public void testPerformInvalideType() throws InstantiationException, IllegalAccessException
	{
		final MyTestIncorrectTypeJobClass myTestIncorrectTypeJobClass = new MyTestIncorrectTypeJobClass();
		myTestIncorrectTypeJobClass.perform(action1);
	}

	private class MyTestClass
	{
		//
	}

	private class MyTestAutomatedWorkflowActionTemplate extends AutomatedWorkflowActionTemplate
	{

		@Override
		public void writeAutomatedComment(final WorkflowAction action, final String message, final String[] messageParams)
				throws JaloSystemException
		{

			//do nothing
		}
	}

	private class MyTestProperJobClass extends MyTestAutomatedWorkflowActionTemplate
	{
		@Override
		public Class getJobClass()
		{

			return WorkflowAutomatedAction.class;
		}

		@Override
		public String getJobHandler()
		{
			return null;
		}

	}

	private class MyTestProperHandlerClass extends MyTestAutomatedWorkflowActionTemplate
	{
		@Override
		public Class getJobClass()
		{

			return null;
		}

		@Override
		public String getJobHandler()
		{
			return "de.hybris.platform.workflow.jalo.WorkflowAutomatedAction";
		}

		@Override
		protected Object getJobHnadlerBean()
		{
			return workflowAutomatedAction;
		}

	}

	private class MyTestIncorrectTypeJobClass extends MyTestAutomatedWorkflowActionTemplate
	{
		@Override
		public Class getJobClass()
		{
			//return WorkflowAutomatedAction.class;
			return MyTestClass.class;
		}
	}

}
