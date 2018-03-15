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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.initialization.testbeans.SystemSetupEmptyTestBean;
import de.hybris.platform.core.initialization.testbeans.SystemSetupParameterTestBean;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/core/systemsetup/systemsetup-test-applicationcontext.xml")
public class SystemSetupCollectorTest
{
    @Autowired
    private SystemSetupCollector systemSetupCollector;

	@Test
	public void testWithNothinExtension()
	{
		assertFalse(systemSetupCollector.hasEssentialData(SystemSetupEmptyTestBean.NOTHIN_TEST_EXTENSION));
		assertFalse(systemSetupCollector.hasProjectData(SystemSetupEmptyTestBean.NOTHIN_TEST_EXTENSION));
		assertTrue(systemSetupCollector.getDefaultParameterMap(SystemSetupEmptyTestBean.NOTHIN_TEST_EXTENSION).isEmpty());
		assertNull(systemSetupCollector.getParameterMap(SystemSetupEmptyTestBean.NOTHIN_TEST_EXTENSION));
		assertFalse(systemSetupCollector.hasParameter(SystemSetupEmptyTestBean.NOTHIN_TEST_EXTENSION));
	}

	@Test
	public void testWithSystemSetupParameterTestExtension()
	{
		assertTrue(systemSetupCollector.hasEssentialData(SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION));
		assertTrue(systemSetupCollector.hasProjectData(SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION));
		assertFalse(systemSetupCollector.getDefaultParameterMap(SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION)
				.isEmpty());
		assertNotNull(systemSetupCollector.getParameterMap(SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION));
		assertTrue(systemSetupCollector.hasParameter(SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION));

		final Map<String, String[]> defaultParamMap = systemSetupCollector
				.getDefaultParameterMap(SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION);
		final List<SystemSetupParameter> paramList = systemSetupCollector
				.getParameterMap(SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION);

		assertEquals(1, paramList.size());
		assertEquals(3, paramList.get(0).getValues().size());
		assertEquals(2, defaultParamMap.get("key").length);
	}


}
