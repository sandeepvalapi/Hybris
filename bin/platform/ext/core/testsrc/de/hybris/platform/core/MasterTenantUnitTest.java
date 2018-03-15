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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.AbstractTenant.ShutDownMode;
import de.hybris.platform.core.AbstractTenant.State;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;


/**
 *
 */
@UnitTest
public class MasterTenantUnitTest
{

	class ExpectedException extends RuntimeException
	{
		//
	}

	@Test
	public void testShutdownMasterDuringInitCallsShutdownDuringInitForSlaves()
	{

		final SlaveTenant foo = Mockito.spy(new TestSlaveTenantStub("foo"));
		foo.setState(State.STARTED);
		//

		final SlaveTenant bar = Mockito.spy(new TestSlaveTenantStub("bar"));
		bar.setState(State.STARTED);


		final Map<String, SlaveTenant> tenants = new HashMap<String, SlaveTenant>();
		tenants.put("foo", foo);
		tenants.put("bar", bar);

		final MasterTenant master = new MasterTenant()
		{
			@Override
			protected java.util.Map<String, SlaveTenant> getSlaveTenantsMap()
			{
				return tenants;
			}

			@Override
			List<TenantListener> getTenantListeners()
			{
				return java.util.Collections.EMPTY_LIST;
			}
		};

		master.setState(State.STARTED);

		master.doShutDownDuringInitialization();

		Mockito.verifyZeroInteractions(foo);
		Mockito.verifyZeroInteractions(bar);

	}


	@Test
	public void testShutdownMasterCallsShutdownForSlaves()
	{

		final SlaveTenant foo = Mockito.spy(new TestSlaveTenantStub("foo"));
		foo.setState(State.STARTED);

		final SlaveTenant bar = Mockito.spy(new TestSlaveTenantStub("bar"));
		bar.setState(State.STARTED);

		final Map<String, SlaveTenant> tenants = new HashMap<String, SlaveTenant>();
		tenants.put("foo", foo);
		tenants.put("bar", bar);

		final MasterTenant master = new MasterTenant()
		{
			@Override
			protected java.util.Map<String, SlaveTenant> getSlaveTenantsMap()
			{
				return tenants;
			}

			@Override
			List<TenantListener> getTenantListeners()
			{
				return java.util.Collections.EMPTY_LIST;
			}
		};

		master.setState(State.STARTED);

		master.doShutDown();

		Mockito.verify(foo).shutDown(ShutDownMode.SYSTEM_SHUTDOWN);
		Mockito.verify(bar).shutDown(ShutDownMode.SYSTEM_SHUTDOWN);

	}

	@Test
	public void testShutdownDuringReconnectMasterCallsShutdownDuringReconnectForSlaves()
	{

		final SlaveTenant foo = Mockito.spy(new TestSlaveTenantStub("foo"));
		foo.setState(State.STARTED);
		BDDMockito.doReturn(null).when(foo).getMasterDataSource();

		foo.cannotConnect();

		final SlaveTenant bar = Mockito.spy(new TestSlaveTenantStub("bar"));
		bar.setState(State.STARTED);
		BDDMockito.doReturn(null).when(bar).getMasterDataSource();

		bar.cannotConnect();

		final Map<String, SlaveTenant> tenants = new HashMap<String, SlaveTenant>();
		tenants.put("foo", foo);
		tenants.put("bar", bar);

		final MasterTenant master = new MasterTenant()
		{
			@Override
			public HybrisDataSource getMasterDataSource()
			{
				return null;
			}

			@Override
			protected java.util.Map<String, SlaveTenant> getSlaveTenantsMap()
			{
				return tenants;
			}

			@Override
			List<TenantListener> getTenantListeners()
			{
				return java.util.Collections.EMPTY_LIST;
			}
		};

		master.setState(State.STARTED);
		master.cannotConnect();//mark as cannot connect
	}

}
