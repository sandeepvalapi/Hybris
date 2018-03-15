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

import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.testframework.runlistener.ItemCreationListener;
import de.hybris.platform.util.database.DropTablesTool;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;

import com.google.common.collect.ImmutableMap;


/**
 * Abstract for test initializing tenants on the fly
 */
@Ignore
abstract public class AbstractTenantInitializationTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(AbstractTenantInitializationTest.class.getName());
	private final Map<String, ItemCreationListener> creationListeners = new HashMap<>();

	abstract protected Collection<String> getTenantIds();

	protected Tenant junit;

	@Before
	public void setUp() throws Exception
	{

		junit = Registry.getCurrentTenant();
		for (final String tenantId : getTenantIds())
		{
			Assume.assumeTrue(getSlaveTenant(tenantId) != null);//we got offline config

			final AbstractTenant testTenant = getSlaveTenant(tenantId);
			LOG.info("Specific test tenant <" + tenantId + "> exists");

			final ItemCreationListener listener = new ItemCreationListener();

			LOG.info("Activating test tenant <" + tenantId + "> ... ");
			Registry.setCurrentTenant(testTenant);//activate tenant for getting master DS
			initTenant(tenantId, listener);

			creationListeners.put(tenantId, listener);
		}
		Registry.setCurrentTenant(junit);
	}

	@After
	public void tearDown() throws Exception
	{
		try
		{
			for (final String tenantId : getTenantIds())
			{
				final SlaveTenant testTenant = getSlaveTenant(tenantId);
				if (testTenant != null)
				{
					if (creationListeners.get(tenantId) != null)
					{
						Registry.setCurrentTenant(testTenant);
						creationListeners.get(tenantId).testFinished(null);
						Registry.setCurrentTenant(junit);
						testTenant.shutDown(AbstractTenant.ShutDownMode.INITIALIZATION);
						LOG.info("Dropping tables for tenant <" + testTenant + "> after test ... ");
						new DropTablesTool(testTenant, null).dropAllTables();
						//shutdown
					}
				}
			}
		}
		finally
		{
			Registry.setCurrentTenant(junit);
		}
	}


	private void initialize(final Tenant tenant) throws Exception
	{
		LOG.info("Activating test tenant <" + tenant + "> ... ");
		Registry.setCurrentTenant(tenant);

		final Map props = ImmutableMap.of(//
				  Constants.Initialization.SYSTEM_NAME, "System-" + tenant.getTenantID(), //
				  CoreBasicDataCreator.FORCE_CLEAN, "true",//
				  Initialization.JSPCONTEXT_KEY, Initialization.createDummyInitJspContext());

		LOG.info("Initializing system for " + tenant.getTenantID() + " ...");

		SystemEJB.getInstance().setLocked(false);
		try
		{
			TestUtils.disableFileAnalyzer("During initialization/update - might occur some exceptions ... ");
			new Initialization.SessionRecoveryAfterRegistryStartupAwareExecutor()
			{
				@Override
				public Void call() throws Exception
				{
					Initialization.initialize(props, null);
					return null;
				}
			}.execute(null);
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
		LOG.info("done initializing/update system for " + tenant.getTenantID() + ".");
	}

	private SlaveTenant initTenant(final String id, final ItemCreationListener listener) throws Exception
	{
		final SlaveTenant ret = getSlaveTenant(id);
		Assert.assertNotNull(ret);

		initialize(ret);
		listener.testStarted(null);

		return ret;
	}


	protected SlaveTenant getSlaveTenant(final String id)
	{
		return Registry.getSlaveTenants().get(id);
	}

}