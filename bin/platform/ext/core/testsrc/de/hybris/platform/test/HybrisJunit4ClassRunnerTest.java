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
package de.hybris.platform.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.testframework.HybrisJUnit4ClassRunner;
import de.hybris.platform.testframework.RunListeners;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;


@UnitTest
public class HybrisJunit4ClassRunnerTest
{
	@Test
	public void testListenerConcept() throws Exception
	{
		final HybrisJUnit4ClassRunner runner = new HybrisJUnit4ClassRunner(SubTestClass.class);

		final RunNotifier notifier = new RunNotifier();
		final Result result = new Result();
		final RunListener externalListener = new TestRunListener3();
		notifier.addFirstListener(externalListener);

		notifier.fireTestRunStarted(runner.getDescription());

		runner.run(notifier);

		notifier.fireTestRunFinished(result);

		final Iterator<String> iter = AbstractTestRunListener.events.iterator();

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener3.class.getName() + "runStarted", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener4.class.getName() + "runStarted", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener2.class.getName() + "runStarted", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener1.class.getName() + "runStarted", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener3.class.getName() + "started", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener4.class.getName() + "started", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener2.class.getName() + "started", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener1.class.getName() + "started", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener1.class.getName() + "finished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener2.class.getName() + "finished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener4.class.getName() + "finished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener3.class.getName() + "finished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener3.class.getName() + "started", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener4.class.getName() + "started", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener2.class.getName() + "started", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener1.class.getName() + "started", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener1.class.getName() + "finished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener2.class.getName() + "finished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener4.class.getName() + "finished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener3.class.getName() + "finished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener1.class.getName() + "runFinished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener2.class.getName() + "runFinished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener4.class.getName() + "runFinished", iter.next());

		assertTrue(iter.hasNext());
		assertEquals(TestRunListener3.class.getName() + "runFinished", iter.next());

		assertFalse(iter.hasNext());
	}

	abstract static class AbstractTestRunListener extends RunListener
	{
		protected static List<String> events = new LinkedList<String>();

		@Override
		public void testRunStarted(final Description description) throws Exception
		{
			events.add(getClass().getName() + "runStarted");
		}

		@Override
		public void testRunFinished(final Result result) throws Exception
		{
			events.add(getClass().getName() + "runFinished");
		}

		@Override
		public void testStarted(final Description description) throws Exception
		{
			events.add(getClass().getName() + "started");
		}

		@Override
		public void testFinished(final Description description) throws Exception
		{
			events.add(getClass().getName() + "finished");
		}
	}

	public static class TestRunListener1 extends AbstractTestRunListener
	{
		//
	}

	public static class TestRunListener2 extends AbstractTestRunListener
	{
		//
	}

	public static class TestRunListener3 extends AbstractTestRunListener
	{
		//
	}

	public static class TestRunListener4 extends AbstractTestRunListener
	{
		//
	}

	@RunListeners(
	{ TestRunListener1.class, TestRunListener4.class })
	public static class SuperTestClass
	{
		@Test
		public void superTestTest()
		{
			//
		}
	}

	@RunListeners(
	{ TestRunListener1.class, TestRunListener2.class })
	public static class SubTestClass extends SuperTestClass
	{
		@Test
		public void subTestTest()
		{
			//
		}
	}
}
