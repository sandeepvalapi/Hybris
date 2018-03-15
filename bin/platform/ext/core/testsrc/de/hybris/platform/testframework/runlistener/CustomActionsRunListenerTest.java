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
package de.hybris.platform.testframework.runlistener;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;

import com.google.common.collect.ImmutableMap;


@UnitTest
public class CustomActionsRunListenerTest
{

	private Map<Integer, CustomRunListener> originalListeners;

	@Before
	public void setUp()
	{
		originalListeners = CustomActionsRunListener.getOrderedListeners();
	}

	@After
	public void tearDown()
	{
		CustomActionsRunListener.setOrderedListeners(originalListeners);
	}

	@Test
	public void shouldExecuteCustomListenersObeyingPriority() throws Exception
	{
		// given
		final List<CustomRunListener> orderOfStarting = new ArrayList<>();
		final List<CustomRunListener> orderOfFinishing = new ArrayList<>();

		final CustomRunListener l1 = new TestCustomRunListener(1, orderOfStarting, orderOfFinishing);
		final CustomRunListener l2 = new TestCustomRunListener(2, orderOfStarting, orderOfFinishing);
		final CustomRunListener l3 = new TestCustomRunListener(3, orderOfStarting, orderOfFinishing);

		final CustomActionsRunListener tested = new CustomActionsRunListener()
		{
			@Override
			Map<String, CustomRunListener> getCustomListenerBeans()
			{
				return ImmutableMap.of( //
						"l1", l1, //
						"l2", l2, //
						"l3", l3);
			}
		};
		tested.initListeners();

		// when testRunStarted is called
		final Description collector = Description.createTestDescription(CustomActionsRunListenerTest.class, "description");
		tested.testRunStarted(collector);

		// then
		assertThat(orderOfStarting).hasSize(3);
		assertThat(orderOfStarting.get(0)).isSameAs(l1);
		assertThat(orderOfStarting.get(1)).isSameAs(l2);
		assertThat(orderOfStarting.get(2)).isSameAs(l3);

		// when testRunFinished is called
		tested.testRunFinished(new Result());

		// then
		assertThat(orderOfFinishing).hasSize(3);
		assertThat(orderOfFinishing.get(0)).isSameAs(l3);
		assertThat(orderOfFinishing.get(1)).isSameAs(l2);
		assertThat(orderOfFinishing.get(2)).isSameAs(l1);
	}

	class TestCustomRunListener extends CustomRunListener
	{

		private final int prio;
		private final List<CustomRunListener> orderOfStarting;
		private final List<CustomRunListener> orderOfFinishing;

		public TestCustomRunListener(final int prio, final List<CustomRunListener> orderOfStarting,
				final List<CustomRunListener> orderOfFinishing)
		{
			this.prio = prio;
			this.orderOfStarting = orderOfStarting;
			this.orderOfFinishing = orderOfFinishing;
		}

		@Override
		public int getPriority()
		{
			return prio;
		}

		@Override
		public void testRunStarted(final Description description) throws Exception
		{
			this.orderOfStarting.add(this);
		}

		@Override
		public void testRunFinished(final Result result) throws Exception
		{
			this.orderOfFinishing.add(this);
		}
	}
}
