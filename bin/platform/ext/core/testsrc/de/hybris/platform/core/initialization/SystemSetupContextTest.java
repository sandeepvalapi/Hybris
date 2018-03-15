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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


@UnitTest
public class SystemSetupContextTest
{
	@Test
	public void testInitDeprecated()
	{
		final Map<String, String[]> parameter = new HashMap<String, String[]>();
		parameter.put("test", new String[]
		{ "value1", "value2" });
		parameter.put("initmethod", new String[]
		{ "init" });
		final SystemSetupContext systemSetupContext = new SystemSetupContext(parameter, Type.ALL, "testextension");

		assertEquals(systemSetupContext.getProcess(), Process.INIT);
		assertEquals(Type.ALL, systemSetupContext.getType());
	}

	@Test
	public void testInit()
	{
		final Map<String, String[]> parameter = new HashMap<String, String[]>();
		parameter.put("test", new String[]
		{ "value1", "value2" });
		parameter.put("initmethod", new String[]
		{ "init" });
		final SystemSetupContext systemSetupContext = new SystemSetupContext(parameter, Type.ALL, Process.INIT, "testextension");

		assertEquals(systemSetupContext.getProcess(), Process.INIT);
		assertEquals(Type.ALL, systemSetupContext.getType());
	}

	@Test
	public void testUpdateDeprecated()
	{
		final Map<String, String[]> parameter = new HashMap<String, String[]>();
		parameter.put("test", new String[]
		{ "value1", "value2" });
		parameter.put("initmethod", new String[]
		{ "update" });
		final SystemSetupContext systemSetupContext = new SystemSetupContext(parameter, Type.ALL, "testextension");

		assertEquals(systemSetupContext.getProcess(), Process.UPDATE);
		assertEquals(Type.ALL, systemSetupContext.getType());
	}

	@Test
	public void testUpdate()
	{
		final Map<String, String[]> parameter = new HashMap<String, String[]>();
		parameter.put("test", new String[]
		{ "value1", "value2" });
		parameter.put("initmethod", new String[]
		{ "update" });
		final SystemSetupContext systemSetupContext = new SystemSetupContext(parameter, Type.ALL, Process.UPDATE, "testextension");

		assertEquals(systemSetupContext.getProcess(), Process.UPDATE);
		assertEquals(Type.ALL, systemSetupContext.getType());
	}

	@Test
	public void testProcessModeAll1()
	{
		final Map<String, String[]> parameter = new HashMap<String, String[]>();
		parameter.put("test", new String[]
		{ "value1", "value2" });

		final SystemSetupContext systemSetupContext = new SystemSetupContext(parameter, Type.ALL, "testextension");

		assertEquals(systemSetupContext.getProcess(), Process.ALL);
		assertEquals("testextension", systemSetupContext.getExtensionName());
		assertSame(parameter, systemSetupContext.getParameterMap());
		assertArrayEquals(parameter.get("test"), systemSetupContext.getParameters("test"));
		assertEquals("value1", systemSetupContext.getParameter("test"));
		assertNull(systemSetupContext.getParameters("xxx"));
		assertNull(systemSetupContext.getParameter("xxx"));
		assertNull(systemSetupContext.getJspContext());
		assertEquals(Type.ALL, systemSetupContext.getType());
	}

	@Test
	public void testProcessModeAll2()
	{
		final Map<String, String[]> parameter = new HashMap<String, String[]>();
		parameter.put("test", new String[]
		{ "value1", "value2" });
		parameter.put("initmethod", new String[]
		{ "all" });
		final SystemSetupContext systemSetupContext = new SystemSetupContext(parameter, Type.ALL, "testextension");

		assertEquals(systemSetupContext.getProcess(), Process.ALL);
		assertEquals(Type.ALL, systemSetupContext.getType());
	}
}
