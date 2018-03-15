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

import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.TestUtils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import com.google.common.base.Joiner;


/**
 *
 */
@Ignore
abstract public class BaseSlaveTenantTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(BaseSlaveTenantTest.class.getName());

	protected static final String FOO_TENANT_ID = "foo";


	protected Tenant currentTenatBefore;

	protected SlaveTenant fooTenant;


	@BeforeClass
	public static void initBefore() throws Exception
	{
		final Map<String, SlaveTenant> tenants = Registry.getSlaveTenants();
		Assert.assertTrue(tenants.get(FOO_TENANT_ID) != null);
		initTenant(tenants.get(FOO_TENANT_ID));

	}

	@After
	public void after() throws Exception
	{

		Registry.unregisterShutdownTenant(fooTenant.getTenantID());//

		if (currentTenatBefore != null)
		{
			Registry.setCurrentTenant(currentTenatBefore);
		}
		LOG.info("Current tenant after :" + Registry.getCurrentTenantNoFallback());


	}

	@Before
	public void before() throws Exception
	{
		currentTenatBefore = Registry.getCurrentTenantNoFallback();

		LOG.info("Current tenant before :" + currentTenatBefore);
		LOG.info("Slave tenants before :" + Joiner.on(";").join(Registry.getSlaveTenants().entrySet()));

		final Map<String, SlaveTenant> tenants = Registry.getSlaveTenants();
		Assert.assertTrue((fooTenant = tenants.get(FOO_TENANT_ID)) != null);

	}


	/**
	 * @throws Exception
	 * 
	 */
	private static void initTenant(final SlaveTenant slave) throws Exception
	{
		try
		{
			TestUtils
					.disableFileAnalyzer(" setting current tenant for a newly created dummy one can cause some background threads to fail");
			Registry.setCurrentTenant(slave);

			final Map props = new HashMap();
			props.put(Constants.Initialization.SYSTEM_NAME, "System-" + slave.getTenantID());

			SystemEJB.getInstance().setLocked(false);
			Initialization.initialize(props, null);

		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

}
