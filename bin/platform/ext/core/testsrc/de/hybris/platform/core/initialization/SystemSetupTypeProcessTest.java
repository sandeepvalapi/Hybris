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
import de.hybris.platform.core.initialization.testbeans.SystemSetupTypeProcessTestBean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/core/systemsetup/systemsetup-test-applicationcontext.xml")
public class SystemSetupTypeProcessTest
{
    @Autowired
    private SystemSetupCollector systemSetupCollector;

	@Test
	public void testEssentialInit()
	{
		final SystemSetupContext systemSetupContext = new SystemSetupContext(null, Type.ESSENTIAL, Process.INIT,
				SystemSetupTypeProcessTestBean.SYSTEM_SETUP_TYPE_PROCESS_TEST_EXTENSION);

		boolean exceptionWasThrown = false;
		try
		{
			exceptionWasThrown = true;
			systemSetupCollector.executeMethods(systemSetupContext);
		}
		catch (final Exception e)
		{
			assertEquals(e.getCause().getMessage(), SystemSetupTypeProcessTestBean.ESSENTIAL_INIT);
		}

		assertTrue(exceptionWasThrown);
	}

	@Test
	public void testProjectInit()
	{
		final SystemSetupContext systemSetupContext = new SystemSetupContext(null, Type.PROJECT, Process.INIT,
				SystemSetupTypeProcessTestBean.SYSTEM_SETUP_TYPE_PROCESS_TEST_EXTENSION);

		boolean exceptionWasThrown = false;
		try
		{
			exceptionWasThrown = true;
			systemSetupCollector.executeMethods(systemSetupContext);
		}
		catch (final Exception e)
		{
			assertEquals(e.getCause().getMessage(), SystemSetupTypeProcessTestBean.PROJECT_INIT);
		}
		assertTrue(exceptionWasThrown);
	}

	@Test
	public void testEssentialUpdate()
	{
		final SystemSetupContext systemSetupContext = new SystemSetupContext(null, Type.ESSENTIAL, Process.UPDATE,
				SystemSetupTypeProcessTestBean.SYSTEM_SETUP_TYPE_PROCESS_TEST_EXTENSION);

		boolean exceptionWasThrown = false;
		try
		{
			exceptionWasThrown = true;
			systemSetupCollector.executeMethods(systemSetupContext);
		}
		catch (final Exception e)
		{
			assertEquals(e.getCause().getMessage(), SystemSetupTypeProcessTestBean.ESSENTIAL_UPDATE);
		}
		assertTrue(exceptionWasThrown);
	}

	@Test
	public void testProjectUpdate()
	{
		final SystemSetupContext systemSetupContext = new SystemSetupContext(null, Type.PROJECT, Process.UPDATE,
				SystemSetupTypeProcessTestBean.SYSTEM_SETUP_TYPE_PROCESS_TEST_EXTENSION);

		boolean exceptionWasThrown = false;
		try
		{
			exceptionWasThrown = true;
			systemSetupCollector.executeMethods(systemSetupContext);
		}
		catch (final Exception e)
		{
			assertEquals(e.getCause().getMessage(), SystemSetupTypeProcessTestBean.PROJECT_UPDATE);
		}
		assertTrue(exceptionWasThrown);
	}
}
