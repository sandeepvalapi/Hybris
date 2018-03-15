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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.util.Config;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.Verifier;


/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
@IntegrationTest
public class PlatformConfigurationCheckRunListenerRollbackTest extends ServicelayerBaseTest
{

	@ClassRule
	public static Verifier verifier = new Verifier()
	{
		@Override
		protected void verify() throws Throwable
		{
			assureCorrectConfigParameters();
		}
	};

	public static final String TEST_PROPERTY1_NOT_SET_BEFORE = "test.property1.not.set.before";
	public static final String TEST_PROPERTY1 = "test.property1";
	public static final String TEST_PROPERTY2 = "test.property2";
	public static final String TEST_VALUE1 = "test.value1";
	public static final String TEST_VALUE2 = "test.value2";
	public static final String NEW_VALUE = "new.value";
	public static final String CHANGED_VALUE = "changed.value";
	public static final String MASTER = "master";

	@Test
	public void shouldAddNewParameter()
	{
		Config.setParameter(TEST_PROPERTY1_NOT_SET_BEFORE, NEW_VALUE);
		assertEquals(NEW_VALUE, Config.getParameter(TEST_PROPERTY1_NOT_SET_BEFORE));
	}

	@Test
	public void shouldSetParameterValueToNull()
	{
		final String currentTenantId = Registry.getCurrentTenant().getTenantID();
		setConfigParameterToNull(MASTER, TEST_PROPERTY1);
		setConfigParameterToNull(currentTenantId, TEST_PROPERTY1);
		assertEquals(null, Config.getParameter(TEST_PROPERTY1));
	}

	@Test
	public void shouldChangeParameterValue()
	{
		Config.setParameter(TEST_PROPERTY2, CHANGED_VALUE);
		assertEquals(CHANGED_VALUE, Config.getParameter(TEST_PROPERTY2));
	}

	private static void assureCorrectConfigParameters()
	{
		assertEquals(TEST_VALUE1, Config.getParameter(TEST_PROPERTY1));
		assertEquals(TEST_VALUE2, Config.getParameter(TEST_PROPERTY2));
		assertNull(Config.getParameter(TEST_PROPERTY1_NOT_SET_BEFORE));
	}

	private void setConfigParameterToNull(final String tenantId, final String param)
	{
		Registry.setCurrentTenantByID(tenantId);
		Config.setParameter(param, null);
	}

}
