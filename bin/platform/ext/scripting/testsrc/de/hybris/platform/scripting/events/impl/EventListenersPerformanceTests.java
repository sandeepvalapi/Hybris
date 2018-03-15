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

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.scripting.events.TestDummyEvent;
import de.hybris.platform.scripting.events.TestPerformanceEvent;
import de.hybris.platform.scripting.events.impl.TestStandardEventListener;
import de.hybris.platform.scripting.events.impl.DefaultScriptingEventService;
import de.hybris.platform.scripting.events.impl.ScriptingListenerWrapper;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.user.daos.TitleDao;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;


/**
 * Performance tests for scripting event listeners
 */
@PerformanceTest
public class EventListenersPerformanceTests extends ServicelayerTransactionalBaseTest
{
	@Resource
	private TitleDao titleDao;
	@Resource
	private DefaultScriptingEventService scriptingEventService;

	private Stopwatch stopwatch;
	private TestPerformanceEvent testPerformanceEvent;
	private TestDummyEvent testDummyEvent;

	private static final String URI_FOR_SCRIPT = "classpath://test/test-script-performance-listener.groovy";
	private static final int ITEMS_TO_SAVE = 30;
	private static final int DUMMY_EVENTS_TO_BE_EXECUTED = 200;

	@Before
	public void setUp() throws Exception
	{
		stopwatch = Stopwatch.createUnstarted();
		testPerformanceEvent = new TestPerformanceEvent(ITEMS_TO_SAVE);
		testDummyEvent = new TestDummyEvent();
		for (int i = 0; i < ITEMS_TO_SAVE; i++)
		{
			assertThat(titleDao.findTitleByCode("testTitle" + i)).isNull();
		}
	}

	@Test
	public void testStandardListenerOneSlowEvent() throws Exception
	{
		final TestStandardEventListener myStandardEventListener = new TestStandardEventListener();

		stopwatch.start();
		myStandardEventListener.onEvent(testPerformanceEvent);
		stopwatch.stop();
		System.out.println("##########standard event listener result#############");
		System.out.println("One slow event handling processed in: " + stopwatch.toString());
		System.out.println("########################################");

		assertAllItemsCreated();
	}

	@Test
	public void testStandardListenerManyFastEvents() throws Exception
	{
		final TestStandardEventListener myStandardEventListener = new TestStandardEventListener();

		stopwatch.start();
		for (int i = 0; i < DUMMY_EVENTS_TO_BE_EXECUTED; i++)
		{
			myStandardEventListener.onEvent(testDummyEvent);
		}
		stopwatch.stop();
		System.out.println("##########standard event listener result#############");
		System.out.println("Many fast events handling processed in: " + stopwatch.toString());
		System.out.println("########################################");
	}

	private void assertAllItemsCreated()
	{
		for (int i = 0; i < ITEMS_TO_SAVE; i++)
		{
			assertThat(titleDao.findTitleByCode("testTitle" + i)).isNotNull();
		}
	}

	@Test
	public void testScriptingListenerOneSlowEvent() throws Exception
	{
		final ScriptingListenerWrapper myScriptingListener = getScriptingListenerWrapper();

		stopwatch.start();
		myScriptingListener.onApplicationEvent(testPerformanceEvent);
		stopwatch.stop();
		System.out.println("##########scripting event listener result#############");
		System.out.println("One slow event handling processed in: " + stopwatch.toString());
		System.out.println("########################################");

		assertAllItemsCreated();
	}

	@Test
	public void testScriptingListenerManyFastEvents() throws Exception
	{
		final ScriptingListenerWrapper myScriptingListener = getScriptingListenerWrapper();

		stopwatch.start();
		for (int i = 0; i < DUMMY_EVENTS_TO_BE_EXECUTED; i++)
		{
			myScriptingListener.onApplicationEvent(testDummyEvent);
		}
		stopwatch.stop();
		System.out.println("##########scripting event listener result#############");
		System.out.println("Many fast events handling processed in: " + stopwatch.toString());
		System.out.println("########################################");
	}

	private ScriptingListenerWrapper getScriptingListenerWrapper()
	{
		return new ScriptingListenerWrapper(URI_FOR_SCRIPT, scriptingEventService);
	}
}
