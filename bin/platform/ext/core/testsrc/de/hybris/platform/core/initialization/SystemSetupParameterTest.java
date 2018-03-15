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
package de.hybris.platform.core.initialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.testbeans.SystemSetupParameterTestBean;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/core/systemsetup/systemsetup-test-applicationcontext.xml")
public class SystemSetupParameterTest
{
    @Autowired
    private SystemSetupCollector systemSetupCollector;

	@Test
	public void testParameterMap()
	{
		final Map<String, String[]> parameterMap = new HashMap<String, String[]>();
		parameterMap.put(SystemSetupParameterTestBean.TEST_PARAMETER_KEY, new String[]
		{ SystemSetupParameterTestBean.TEST_PARAMETER_VALUE3 });
		final SystemSetupContext systemSetupContext = new SystemSetupContext(parameterMap, Type.ALL, Process.ALL,
				SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION);

		boolean exceptionWasThrown = false;
		try
		{
			systemSetupCollector.executeMethods(systemSetupContext);
		}
		catch (final Exception e)
		{
			exceptionWasThrown = true;
			assertEquals(SystemSetupParameterTestBean.TEST_PARAMETER_VALUE3, e.getCause().getMessage());
		}

		assertTrue(exceptionWasThrown);
	}

	@Test
	public void testParameterMapWithDefaults()
	{
		final SystemSetupContext systemSetupContext = new SystemSetupContext(null, Type.ALL, Process.ALL,
				SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION);

		boolean exceptionWasThrown = false;
		try
		{
			exceptionWasThrown = true;
			systemSetupCollector.executeMethods(systemSetupContext);
		}
		catch (final Exception e)
		{
			assertEquals(SystemSetupParameterTestBean.TEST_PARAMETER_VALUE2, e.getCause().getMessage());
		}
		assertTrue(exceptionWasThrown);
	}
}
