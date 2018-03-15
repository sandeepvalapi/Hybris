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
package de.hybris.platform.cronjob.jalo;

import static org.junit.Assert.*;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants.Enumerations.CronJobStatus;
import de.hybris.platform.cronjob.jalo.CronJob.CronJobResult;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jdbcwrapper.JDBCConnectionPool;
import de.hybris.platform.jdbcwrapper.JUnitConnectionErrorCheckingJDBCConnectionPool;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.testframework.runlistener.ItemCreationListener;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author hr
 */
@IntegrationTest
@Ignore
public class DatabaseReconnectTest extends HybrisJUnit4Test //NOPMD
{

	private static final String TEST_TENANT = "dbReconnect";

	private static final Logger LOG = Logger.getLogger(DatabaseReconnectTest.class);

	private TestJob testJob;

	private Tenant junitTenantBefore;

	private ItemCreationListener itemCreationListener;

	@Before
	public void setUp() throws Exception
	{

		Assume.assumeTrue(Registry.getSlaveTenants().get(TEST_TENANT) != null);
		LOG.info("Specific test tenant " + TEST_TENANT + " exists");

		junitTenantBefore = Registry.getCurrentTenant();

		final SlaveTenant ret = Registry.getSlaveTenants().get(TEST_TENANT);
		Assert.assertNotNull(ret);

		itemCreationListener = new ItemCreationListener();

		Registry.setCurrentTenant(ret);
		initialize(ret);

		itemCreationListener.testStarted(null);

		ComposedType type = null;
		final TypeManager manager = TypeManager.getInstance();
		type = manager.createComposedType(manager.getComposedType(Job.class), "TestReconnect");
		type.setJaloClass(TestJob.class);
		testJob = (TestJob) type.newInstance(Collections.singletonMap(Job.CODE, "TestReconnect"));
	}



	private void initialize(final Tenant t) throws Exception
	{
		Registry.setCurrentTenant(t);
		LOG.info("Initializing system for " + t.getTenantID() + " ...");

		final Map props = new HashMap();
		props.put(Constants.Initialization.SYSTEM_NAME, "System-" + t.getTenantID());

		SystemEJB.getInstance().setLocked(false);
		Initialization.initialize(props, null);
		LOG.info("done Initializing system for " + t.getTenantID() + ".");
	}



	@After
	public void cleanup() throws Exception
	{
		if (Registry.getSlaveTenants().get(TEST_TENANT) != null)
		{
			final JDBCConnectionPool pool = Registry.getCurrentTenant().getDataSource().getConnectionPool();

			if (pool instanceof JUnitConnectionErrorCheckingJDBCConnectionPool)
			{
				((JUnitConnectionErrorCheckingJDBCConnectionPool) pool).resetTestMode();
			}

			itemCreationListener.testFinished(null);

			Registry.setCurrentTenant(junitTenantBefore);
		}
		else
		{
			LOG.info("Nothing to clear up - no test tenant " + TEST_TENANT + " available");
		}

	}

	private boolean hasJUnitJDBCSetup()
	{
		return Registry.getCurrentTenantNoFallback().getDataSource().getConnectionPool() instanceof JUnitConnectionErrorCheckingJDBCConnectionPool;
	}

	@Test
	public void testReconnectDuringCronjob() throws InterruptedException
	{
		if (hasJUnitJDBCSetup())
		{
			final JDBCConnectionPool pool = Registry.getCurrentTenant().getDataSource().getConnectionPool();

			if (!(pool instanceof JUnitConnectionErrorCheckingJDBCConnectionPool))
			{
				return;
			}

			final JUnitConnectionErrorCheckingJDBCConnectionPool cp = (JUnitConnectionErrorCheckingJDBCConnectionPool) pool;
			final AbstractTenant tenant = (AbstractTenant) Registry.getCurrentTenant();
			final TestTenantListener listener = new TestTenantListener(tenant);

			// http://jira.hybris.de/browse/HOR-122
			// right now the initialization fails during "create objects" because the connection pool 
			// is unable to return a new connection
			boolean tenantRestarted = false;

			final ReconnectPerformable perf = new ReconnectPerformable();
			(testJob).setPerformable(perf);

			try
			{
				final CronJob testCronJob = CronJobManager.getInstance().createCronJob(testJob, "TestReconnectCronJob", true);
				testCronJob.getJob().perform(testCronJob, false);

				// wait for job to run
				assertTrue("Cronjob thread did not prepare properly", perf.waitForReadyToSwitchOff(10, TimeUnit.SECONDS));

				// register tenant listener
				Registry.registerTenantListener(listener);

				// now switch off database connections
				TestUtils.disableFileAnalyzer(10000);
				cp.setAllConnectionsFail(true);

				// make job continue
				perf.signalContinue();

				// wait for job to crash
				assertTrue("Cronjob thread did not crash", perf.waitForDone(30, TimeUnit.SECONDS));
				assertTrue(tenant.cannotConnect());
				assertTrue(tenant.connectionHasBeenBroken());

				// when accessing the tenant now we should get a exception
				assertFalse("Disconnected JUnit tenant did not cause exception upon activation",
						tryToActivateJUnitTenantFromOtherThread(true));

				// now make connection come around again -> this should allow tenant to be restarted
				cp.resetTestMode();

				// this access of tenant must trigger restart (shutdown and startup)
				// please note that after restart the tenant will have a *new* connection pool !!!
				assertTrue("Reconnected JUnit tenant did cause exception upon activation",
						tryToActivateJUnitTenantFromOtherThread(true));

				// wait for shutdown
				assertTrue("tenant " + tenant + " did not shut down as expected", listener.waitForShutdown(30, TimeUnit.SECONDS));

				// wait for startup
				tenantRestarted = listener.waitForStartup(60, TimeUnit.SECONDS);
				assertTrue("tenant " + tenant + " did not start up as expected", tenantRestarted);

				assertFalse("connections still broken", tenant.cannotConnect());
				assertFalse(tenant.connectionHasBeenBroken());

				// now the cronjob should be marked as aborted correctly
				// added: wait additionally at most 2 seconds for cronjob status change.
				for (int i = 0; i < 20; i++)
				{
					if (CronJobStatus.ABORTED.equals(testCronJob.getStatus().getCode()))
					{
						break;
					}
					LOG.info("awaiting for cronjob to be in ABORTED state...");
					Thread.sleep(1000);
				}
				assertEquals(CronJobStatus.ABORTED, testCronJob.getStatus().getCode());
			}
			finally
			{
				perf.waitForDone(30, TimeUnit.SECONDS);
				Registry.unregisterTenantListener(listener);
				cp.resetTestMode();
				if (!tenantRestarted)
				{
					tryToActivateTestTenant();
				}
				TestUtils.enableFileAnalyzer();
			}
		}
		else
		{
			LOG.warn("Cannot run DatabaseReconnectTest.testReconnectDuringCronjob() since current tenant doesnt have JUnit JDBC settings!");
		}
	}

	private boolean tryToActivateJUnitTenantFromOtherThread(final boolean wait) throws InterruptedException
	{
		final AtomicBoolean result = wait ? new AtomicBoolean(false) : null;
		final CountDownLatch waitState = wait ? new CountDownLatch(1) : null;
		new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				final boolean ok = tryToActivateTestTenant();
				if (wait)
				{
					result.set(ok);
					waitState.countDown();
				}
			}
		}.start();

		if (wait)
		{
			waitState.await();
			return result.get();
		}
		else
		{
			return true;
		}
	}

	private boolean tryToActivateTestTenant()
	{
		try
		{
			Registry.unsetCurrentTenant();
			//Utilities.setJUnitTenant();
			Registry.setCurrentTenantByID(TEST_TENANT);
			return true;
		}
		catch (final Exception e)
		{
			System.err.println("Activating JUnit tenant caused " + e.getMessage());
			// swallow to avoid hiding real test error
			return false;
		}
	}

	static class TestTenantListener implements TenantListener
	{
		private final Tenant toWatch;
		private final CountDownLatch shutdown = new CountDownLatch(1);
		private final CountDownLatch startup = new CountDownLatch(1);

		TestTenantListener(final Tenant t)
		{
			toWatch = t;
		}

		boolean waitForShutdown(final int time, final TimeUnit u) throws InterruptedException
		{
			return shutdown.await(time, u);
		}

		boolean waitForStartup(final int time, final TimeUnit u) throws InterruptedException
		{
			return startup.await(time, u);
		}

		@Override
		public void afterTenantStartUp(final Tenant tenant)
		{
			if (toWatch.equals(tenant))
			{
				startup.countDown();
			}
		}

		@Override
		public void beforeTenantShutDown(final Tenant tenant)
		{
			if (toWatch.equals(tenant))
			{
				shutdown.countDown();
			}
		}

		@Override
		public void afterSetActivateSession(final Tenant tenant)
		{
			// nothing
		}

		@Override
		public void beforeUnsetActivateSession(final Tenant tenant)
		{
			// nothing
		}

	}



	static class ReconnectPerformable implements TestJob.Performable
	{
		private final CountDownLatch readyToSwitchOff = new CountDownLatch(1);
		private final CountDownLatch allowedToContinue = new CountDownLatch(1);
		private final CountDownLatch done = new CountDownLatch(1);

		public boolean waitForReadyToSwitchOff(final int time, final TimeUnit u) throws InterruptedException
		{
			return readyToSwitchOff.await(time, u);
		}

		public boolean waitForDone(final int time, final TimeUnit u) throws InterruptedException
		{
			return done.await(time, u);
		}

		public void signalContinue()
		{
			allowedToContinue.countDown();
		}


		@Override
		public CronJobResult perform(final CronJob cronJob)
		{
			SearchResult rs = FlexibleSearch.getInstance().search(
					"SELECT {PK} FROM {Country} WHERE {" + Item.CREATION_TIME + "}<?now", Collections.singletonMap("now", new Date()),
					Item.class);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("#hits: " + rs.getResult().size());
				for (final Object entry : rs.getResult())
				{
					LOG.debug("result: " + entry);
				}
			}

			try
			{
				// 1. show we're ready
				readyToSwitchOff.countDown();
				// 2. wait for test thread to switch off connections pool
				allowedToContinue.await();
				// try query -> this should crash this thread 
				rs = FlexibleSearch.getInstance().search("SELECT {PK} FROM {Language} WHERE {" + Item.CREATION_TIME + "}<?now",
						Collections.singletonMap("now", new Date()), Item.class);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("#hits: " + rs.getResult().size());
					for (final Object entry : rs.getResult())
					{
						LOG.debug("result: " + entry);
					}
				}
			}
			catch (final InterruptedException e)
			{
				LOG.error("cronjob thread interrupted", e);
			}
			finally
			{
				done.countDown();
			}
			return cronJob.getFinishedResult(true);
		}
	}
}
