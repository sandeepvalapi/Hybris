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
package de.hybris.platform.core;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Utilities;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class BeanScopesIntegrationTest extends HybrisJUnit4Test
{


	private static final Logger LOG = Logger.getLogger(BeanScopesIntegrationTest.class.getName());

	private final String MODEL_SERVICE = "modelService";
	private final String GLOBAL_SINGLETON_BEAN = "cacheController";

	@Before
	public void setUp()
	{
		//this test requires master tenant
		Registry.activateMasterTenant();
		//this test requires to be run with other tenant than master
		Utilities.setJUnitTenant();
	}

	@Test
	public void testBeanReferencesComparisonAfterTenanChange() throws Exception
	{
		if (!Utilities.isMasterTenantAsTestSystem())
		{
			final Object tenantScopedBean1 = Registry.getCoreApplicationContext().getBean(MODEL_SERVICE);
			final Object globalScopedBean1 = Registry.getCoreApplicationContext().getBean(GLOBAL_SINGLETON_BEAN);
			LOG.info("modelServiceBean1: " + tenantScopedBean1 + " ct " + Registry.getCurrentTenantNoFallback());
			LOG.info("global singleton1: " + globalScopedBean1 + " ct " + Registry.getCurrentTenantNoFallback());

			final Tenant currentTenant = Registry.getCurrentTenant();
			try
			{
				final Tenant masterTenant = Registry.getMasterTenant();
				Registry.setCurrentTenant(masterTenant);

				final Object tenantScopedBean2 = Registry.getCoreApplicationContext().getBean(MODEL_SERVICE);
				final Object globalScopedBean2 = Registry.getCoreApplicationContext().getBean(GLOBAL_SINGLETON_BEAN);
				LOG.info("modelServiceBean2: " + tenantScopedBean2 + " ct " + Registry.getCurrentTenantNoFallback());
				LOG.info("global singleton2: " + globalScopedBean2 + " ct " + Registry.getCurrentTenantNoFallback());
				Assert.assertTrue(tenantScopedBean1 != tenantScopedBean2);
				Assert.assertTrue(globalScopedBean1 == globalScopedBean2);

			}
			finally
			{
				if (currentTenant != null)
				{
					Registry.setCurrentTenant(currentTenant);
				}
			}

		}
		else
		{
			LOG.info("Test skipped");
		}
	}
}
