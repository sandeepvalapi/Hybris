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
package de.hybris.platform.scripting.events.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.tenant.TenantService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Tests for ScriptingTaskRunner
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultScriptingEventServiceTest
{
	@Mock
	private ScriptingLanguagesService scriptingLanguagesService;
	@Mock
	private EventService eventService;
	@Mock
	private TenantService tenantService;
	@Mock
	private ClusterService clusterService;

	private static final String WRONG_CONTENT_SCRIPT = "WrongContentScript";
	private static final String CORRECT_SCRIPT = "CorrectScript";

	@Mock
	private ScriptExecutable executableGood, executableBad;

	private MyScriptListener myScriptListener;
	private DefaultScriptingEventService scriptingEventService;

	@Before
	public void setUp() throws Exception
	{
		myScriptListener = new MyScriptListener();
		Mockito.when(scriptingLanguagesService.getExecutableByURI(CORRECT_SCRIPT)).thenReturn(executableGood);
		Mockito.when(scriptingLanguagesService.getExecutableByURI(WRONG_CONTENT_SCRIPT)).thenReturn(executableBad);
		Mockito.when(executableGood.getAsInterface(AbstractEventListener.class)).thenReturn(myScriptListener);
		Mockito.when(executableBad.getAsInterface(AbstractEventListener.class)).thenReturn(null);

		scriptingEventService = new DefaultScriptingEventService();
		scriptingEventService.setScriptingLanguagesService(scriptingLanguagesService);
		scriptingEventService.setEventService(eventService);
		scriptingEventService.setTenantService(tenantService);
		scriptingEventService.setClusterService(clusterService);

		final ScriptingListenerWrapper myScriptListenerWrapper = new ScriptingListenerWrapper(CORRECT_SCRIPT, scriptingEventService);

		Mockito.when(eventService.registerEventListener(myScriptListenerWrapper)).thenReturn(true);
		Mockito.when(eventService.unregisterEventListener(myScriptListenerWrapper)).thenReturn(true);
	}

	@Test
	public void testRegisterScriptingEventListenerCorrectScript() throws Exception
	{
		final boolean result = scriptingEventService.registerScriptingEventListener(CORRECT_SCRIPT);
		assertThat(result).isTrue();
	}

	@Test
	public void testRegisterScriptingEventListenerNotSuccessfulForWrongScript() throws Exception
	{
		try
		{
			scriptingEventService.registerScriptingEventListener(WRONG_CONTENT_SCRIPT);
			Assert.fail("IllegalStateException was expected, but not thrown");
		}
		catch (final Exception e)
		{
			assertThat(e).isExactlyInstanceOf(IllegalStateException.class);
		}
	}

	@Test
	public void testRegisterScriptingEventListenerNotSuccessfulForNullScript() throws Exception
	{
		try
		{
			scriptingEventService.registerScriptingEventListener(null);
			Assert.fail("NullPointerException was expected, but not thrown");
		}
		catch (final Exception e)
		{
			assertThat(e).isExactlyInstanceOf(NullPointerException.class);
		}
	}

	@Test
	public void testUnRegisterScriptingEventListenerCorrectScript() throws Exception
	{
		final boolean result = scriptingEventService.unregisterScriptingEventListener(CORRECT_SCRIPT);
		assertThat(result).isTrue();
	}

	@Test
	public void testUnRegisterScriptingEventListenerDoesntBreakForWrongScript() throws Exception
	{
		final boolean result = scriptingEventService.unregisterScriptingEventListener(WRONG_CONTENT_SCRIPT);
		assertThat(result).isFalse();
	}

	@Test
	public void testUnregisterScriptingEventListenerNotSuccessfulForNullScript() throws Exception
	{
		try
		{
			scriptingEventService.unregisterScriptingEventListener(null);
			Assert.fail("NullPointerException was expected, but not thrown");
		}
		catch (final Exception e)
		{
			assertThat(e).isExactlyInstanceOf(NullPointerException.class);
		}
	}

	static class MyScriptListener extends AbstractEventListener
	{
		@Override
		protected void onEvent(final AbstractEvent event)
		{
			// dummy listener
		}
	}
}
