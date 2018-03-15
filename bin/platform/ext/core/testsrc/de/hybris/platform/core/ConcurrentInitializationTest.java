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

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.Initialization.SystemCallable;
import de.hybris.platform.core.system.InitializationLockDao;
import de.hybris.platform.core.system.InitializationLockHandler;
import de.hybris.platform.core.system.InitializationLockInfo;
import de.hybris.platform.core.system.impl.DefaultInitLockDao;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.runlistener.ItemCreationListener;
import de.hybris.platform.util.JspContext;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test proofs that while initialization , lock flag remains unchanged ( even while restarting tenant which also happens
 * during initialization) so the CheckLockRunner should never get information that system is unlocked while
 * initialization, moreover even {@link InitializationLockInfo#getInstanceIdentifier()} should not change.
 */

@PerformanceTest
public class ConcurrentInitializationTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(ConcurrentInitializationTest.class.getName());

	private final static String TENANT_ID = "t1";

	private Tenant junitTenant;

	private Tenant slaveTenant;

	private TestThreadsHolder<CheckLockRunner> holder;

	private ItemCreationListener slaveTenantListener;

	@Mock
	private JspContext jspContext;
	@Mock
	private HttpServletRequest servletRequest;

	@Before
	public void prepareTenant() throws ConsistencyCheckException
	{
		MockitoAnnotations.initMocks(this);
		this.junitTenant = Registry.getCurrentTenantNoFallback();
		slaveTenant = createTenantIfNotExists(TENANT_ID);
		slaveTenantListener = new ItemCreationListener();

		Registry.setCurrentTenant(junitTenant);

		given(jspContext.getServletRequest()).willReturn(servletRequest);
		given(servletRequest.getParameter("init")).willReturn("true");
		given(servletRequest.getParameter("initmethod")).willReturn("init");
	}

	@After
	public void unprepareTenant() throws Exception
	{
		removeTenantIfExists(slaveTenant);
	}


	@Test
	public void testMultipleOperationsWhileInitialization() throws Exception
	{

		holder = new TestThreadsHolder<CheckLockRunner>(5, new de.hybris.platform.test.RunnerCreator<CheckLockRunner>()
		{
			@Override
			public CheckLockRunner newRunner(final int threadNumber)
			{
				return new CheckLockRunner(junitTenant);
			}
		});

		final Tenant slaveTenant = Registry.getSlaveTenants().get(TENANT_ID);
		Assert.assertNotNull(slaveTenant);
		Registry.setCurrentTenant(slaveTenant);
		try
		{
			final SystemCallable initUpdateCallable = new TestSystemCallable(slaveTenant);
			initializeTenant(initUpdateCallable);
			slaveTenantListener.testStarted(null);
		}
		catch (final Exception e)
		{
			LOG.error(e);
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Checks if from outside a initialization keeps a lock active and it is not changed in mean time until
	 * initialization finishes
	 */
	static private class CheckLockRunner implements Runnable
	{
		private final InitializationLockHandler handler;
		private final InitializationLockDao dao;
		private Exception exception;
		private final Tenant tenant;

		boolean interruped = false;//not using volatile here 

		public CheckLockRunner(final Tenant junitTenant)
		{
			tenant = junitTenant;
			dao = new DefaultInitLockDao();
			handler = new InitializationLockHandler(dao);
		}

		public Exception getException()
		{
			return exception;
		}

		public void interruptCurrent()
		{
			interruped = true;
		}

		@Override
		public void run()
		{
			try
			{
				Registry.setCurrentTenant(tenant);
				while (!Thread.currentThread().isInterrupted() && !interruped)
				{
					final InitializationLockInfo info = handler.getLockInfo();
					//Assert.assertNotNull(info);
					if (info == null)
					{
						throw new IllegalArgumentException("info is null ");
					}
					//Assert.assertEquals("Test initialization1", info.getProcessName());
					if (!"Test initialization".equals(info.getProcessName()))
					{
						throw new IllegalArgumentException("Unexpected lock info (process name) " + info.getProcessName());
					}
					if (!TENANT_ID.equals(info.getTenantId()))
					{
						throw new IllegalArgumentException("Unexpected lock info (tenant)" + info.getTenantId());
					}
					//Assert.assertEquals(TENANT_ID, info.getTenantId());
					if (dao.getUniqueInstanceIdentifier() != info.getInstanceIdentifier())
					{
						throw new IllegalArgumentException("Unexpected lock info (uid)" + info.getInstanceIdentifier());
					}
					//Assert.assertEquals(dao.getUniqueInstanceIdentifier(), info.getInstanceIdentifier());
				}
			}
			catch (final IllegalArgumentException ile)
			{
				LOG.error(ile);
				exception = ile;
			}
			finally
			{
				Registry.unsetCurrentTenant();
			}
		}
	}

	/**
	 * @throws Exception
	 */
	private void initializeTenant(final SystemCallable initUpdateCallable) throws Exception
	{

		final InitializationLockHandler handler = new InitializationLockHandler(new DefaultInitLockDao());

		if (!handler
				.performLocked(initUpdateCallable.getCurrentTenant(), initUpdateCallable, initUpdateCallable.getOperationName()))
		{
			final InitializationLockInfo info = handler.getLockInfo();
			Assert.fail("There is running administration task " + info.getProcessName() + " on tenant " + info.getTenantId()
					+ " started at " + info.getDate());
		}
	}


	/**
	 * @throws Exception
	 */
	private void removeTenantIfExists(final Tenant tenant) throws Exception
	{

		if (tenant != null && Registry.getSlaveTenants().get(tenant.getTenantID()) != null)
		{
			if (slaveTenantListener != null)
			{
				Registry.setCurrentTenant(tenant);
				if (junitTenant != null)
				{
					Registry.setCurrentTenant(junitTenant);
				}
				slaveTenantListener.testFinished(null);
			}
		}

	}

	/**
	 * @throws ConsistencyCheckException
	 */
	private SlaveTenant createTenantIfNotExists(final String id) throws ConsistencyCheckException
	{
		final MasterTenant masterTenant = Registry.getMasterTenant();
		Registry.setCurrentTenant(masterTenant);

		final SlaveTenant ret = Registry.getSlaveTenants().get(id);
		Assert.assertNotNull(ret);

		Registry.setCurrentTenant(ret);
		return ret;
	}



	class TestSystemCallable implements SystemCallable
	{
		private final Tenant tenant;

		TestSystemCallable(final Tenant tenant)
		{
			this.tenant = tenant;
		}

		@Override
		public Boolean call() throws Exception
		{
			holder.startAll();//starts threads for checking locks 

			Registry.setCurrentTenant(tenant);
			final Map props = new HashMap();
			props.put(Constants.Initialization.SYSTEM_NAME, "System-" + tenant.getTenantID());
			props.put("jspc", jspContext);
			SystemEJB.getInstance().setLocked(false);
			Initialization.initialize(props, null);

			//stops threads for checking locks 
			for (final CheckLockRunner runner : holder.getRunners())
			{
				runner.interruptCurrent();
			}

			holder.stopAndDestroy(5);

			for (final CheckLockRunner runner : holder.getRunners())
			{
				if (runner.getException() != null)
				{
					throw runner.getException();
				}
			}

			return null;
		}

		@Override
		public boolean isShowFormFlag()
		{
			return false;
		}

		@Override
		public String getOperationName()
		{
			return "Test initialization";
		}

		@Override
		public Tenant getCurrentTenant()
		{
			return tenant;
		}

	}
}
