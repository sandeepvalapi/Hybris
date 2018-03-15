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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.events.TestScriptingEvent;
import de.hybris.platform.scripting.events.TestSingleton;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.daos.TitleDao;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.fest.assertions.Assertions;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;


/**
 * Integration tests for scripting event listeners.
 */
@IntegrationTest
public class ScriptingEventListenersIntegrationTests extends ServicelayerTransactionalBaseTest
{
	@Resource
	private DefaultScriptingEventService scriptingEventService;
	@Resource
	private EventService eventService;
	@Resource
	private TitleDao titleDao;
	@Resource
	private ModelService modelService;

	private static final String URI_FOR_CORRECT_SCRIPT = "classpath://test/test-script-correct-listener-interface.groovy";
	private static final String URI_FOR_CORRECT_SCRIPT_WITH_TYPED_EVENT = "classpath://test/test-script-correct-listener-interface-with-typed-event.groovy";
	private static final String URI_FOR_ERROR_SCRIPT = "classpath://test/test-script-error-listener-interface.groovy";
	final double timeFactor = Math.max(5.0, Config.getDouble("platform.test.timefactor", 5.0));

	@Test
	public void testHandleEventForCorrectListener() throws Exception
	{
		final String testEventName = "testEventNameAndTitleCodeForScript";
		assertThat(scriptingEventService.registerScriptingEventListener(URI_FOR_CORRECT_SCRIPT)).isTrue();

		Thread.sleep((long) (1000 * timeFactor));
		assertThat(titleDao.findTitleByCode(testEventName)).isNull();
		eventService.publishEvent(new TestScriptingEvent(testEventName));
		Thread.sleep((long) (1000 * timeFactor));
		assertThat(titleDao.findTitleByCode(testEventName)).isNotNull();

		assertThat(scriptingEventService.unregisterScriptingEventListener(URI_FOR_CORRECT_SCRIPT)).isTrue();

		// publish event again - the listener should not react since it's unregistered now
		eventService.publishEvent(new TestScriptingEvent("blabla"));
		Thread.sleep((long) (1000 * timeFactor));
		assertThat(titleDao.findTitleByCode("blabla")).isNull();
	}

	@Test
	public void testHandleTypedEventForCorrectListener() throws Exception
	{
		final String testEventName = "testTypedEventNameAndTitleCodeForScript";
		assertThat(scriptingEventService.registerScriptingEventListener(URI_FOR_CORRECT_SCRIPT_WITH_TYPED_EVENT)).isTrue();

		Thread.sleep((long) (1000 * timeFactor));
		assertThat(titleDao.findTitleByCode(testEventName)).isNull();
		eventService.publishEvent(new TestScriptingEvent(testEventName));
		Thread.sleep((long) (1000 * timeFactor));
		assertThat(titleDao.findTitleByCode(testEventName)).isNotNull();

		assertThat(scriptingEventService.unregisterScriptingEventListener(URI_FOR_CORRECT_SCRIPT_WITH_TYPED_EVENT)).isTrue();

		// publish event again - the listener should not react since it's unregistered now
		eventService.publishEvent(new TestScriptingEvent("blabla"));
		Thread.sleep((long) (1000 * timeFactor));
		assertThat(titleDao.findTitleByCode("blabla")).isNull();
	}

	@Test
	public void testHandleEventForListenerThrowingException() throws Exception
	{
		final String testEventName = "testEventNameAndTitleCodeForScriptWithErrors";
		assertThat(scriptingEventService.registerScriptingEventListener(URI_FOR_ERROR_SCRIPT)).isTrue();
		try
		{
			Thread.sleep((long) (1000 * timeFactor));
			assertThat(titleDao.findTitleByCode(testEventName)).isNull();
			eventService.publishEvent(new TestScriptingEvent(testEventName));
			Thread.sleep((long) (1000 * timeFactor));
		}
		finally
		{
			assertThat(titleDao.findTitleByCode(testEventName)).isNull();
			assertThat(scriptingEventService.unregisterScriptingEventListener(URI_FOR_ERROR_SCRIPT)).isTrue();
			// publish event again - the listener should not react since it's unregistered now
			eventService.publishEvent(new TestScriptingEvent("foobar"));
			Thread.sleep((long) (1000 * timeFactor));
			assertThat(titleDao.findTitleByCode("foobar")).isNull();
		}
	}

	@Test
	public void testRegisterListenerUnsuccessfulForNotExistingScript() throws Exception
	{
		try
		{
			scriptingEventService.registerScriptingEventListener("classpath://notExistingScript");
			Assert.fail("ScriptNotFoundException was expected but not thrown");
		}
		catch (final Exception e)
		{
			assertThat(e).isExactlyInstanceOf(ScriptNotFoundException.class);
		}
	}

	@Test
	public void shouldReloadScriptingEventListenerAfterModification() throws Exception
	{
		// given
		TestSingleton.value = 0;
		final ScriptModel scriptModel = prepareScriptModel("fooBar", //
				contentOf("test/test-script-listener-for-invalidation-v1.groovy"), //
				ScriptType.GROOVY);
		scriptingEventService.registerScriptingEventListener("model://fooBar");

		Assertions.assertThat(TestSingleton.value).isEqualTo(0);

		eventService.publishEvent(new TestScriptingEvent("foobar"));

		assertThat(TestSingleton.value).isEqualTo(1);

		// when
		scriptModel.setContent(contentOf("test/test-script-listener-for-invalidation-v2.groovy"));
		modelService.save(scriptModel);
		eventService.publishEvent(new TestScriptingEvent("foobar"));

		// then
		assertThat(TestSingleton.value).isEqualTo(2);
	}

	@Test
	public void shouldUnregisterScriptingEventListenerAfterRemoval() throws Exception
	{
		// given
		TestSingleton.value = 0;
		final ScriptModel scriptModel = prepareScriptModel("fooBar", //
				contentOf("test/test-script-listener-for-invalidation-v1.groovy"), //
				ScriptType.GROOVY);
		scriptingEventService.registerScriptingEventListener("model://fooBar");

		assertThat(TestSingleton.value).isEqualTo(0);

		eventService.publishEvent(new TestScriptingEvent("foobar"));

		assertThat(TestSingleton.value).isEqualTo(1);

		// when
		TestSingleton.value = -1;
		modelService.remove(scriptModel);
		eventService.publishEvent(new TestScriptingEvent("foobar"));

		// then
		assertThat(TestSingleton.value).isEqualTo(-1);
		assertListenerNotRegisteredWithUri("model://fooBar");
	}

	private void assertListenerNotRegisteredWithUri(final String uri)
	{
		final Collection<ScriptingListenerWrapper> registeredWrappers = scriptingEventService.getRegisteredWrappers();
		for (final ScriptingListenerWrapper registeredWrapper : registeredWrappers)
		{
			if (registeredWrapper.getScriptURI().equals(uri))
			{
				Assert.fail("Found listener registered with uri " + uri);
			}
		}
	}

	private String contentOf(final String v1) throws IOException
	{
		final URL url = Resources.getResource(v1);
		return Resources.toString(url, Charsets.UTF_8);
	}

	private ScriptModel prepareScriptModel(final String code, final String content, final ScriptType scriptType)
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setScriptType(scriptType);
		script.setCode(code);
		script.setContent(content);

		modelService.save(script);

		return script;
	}

}
