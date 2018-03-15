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
package de.hybris.platform.util.logging;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.core.Log4JUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


public class LogsTest
{
	static {
		Log4JUtils.startup();
	}
	private static Logger LOG = Logger.getLogger(LogsTest.class);

	private boolean calculateSomethingCalled;

	@Before
	public void setUp()
	{
		calculateSomethingCalled = false;
	}

	@Test
	public void shouldNotEvaluateLambdaWhenDebugIsDisabled()
	{
		LOG.setLevel(Level.INFO);
		Logs.debug(LOG, () -> "Something " + calculateSomething() + " is wrong");

		assertThat(calculateSomethingCalled).isFalse();
	}

	@Test
	public void shouldEvaluateLambdaWhenDebugIsEnabled()
	{
		LOG.setLevel(Level.DEBUG);

		Logs.debug(LOG, () -> "Something " + calculateSomething() + " is wrong");

		assertThat(calculateSomethingCalled).isTrue();
	}

	private String calculateSomething()
	{
		calculateSomethingCalled = true;
		return "TEST";
	}
}
