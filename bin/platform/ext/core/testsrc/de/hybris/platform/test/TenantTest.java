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
package de.hybris.platform.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.meta.MetaInformationManager;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.StopWatch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;


@IntegrationTest
public class TenantTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(TenantTest.class.getName());

	ThreadLocal tl = new ThreadLocal();

	/*
	 * Test: configured tenant_junit.properties to add 'test/test-core-spring.xml' to core.application-context in order
	 * to load the exclusive bean 'exclusiveJunitBean'!
	 */
	@Test
	public void testTenantSpecificSpringConfig()
	{
		final Tenant tenant = Registry.getCurrentTenantNoFallback();
		assertNotNull(tenant);
		if (!"junit".equalsIgnoreCase(tenant.getTenantID())) // special case: test cannot run outside the junit tenant - ignore
		{
			LOG.error("TenantTest.testTenantSpecificSpringConfig() requires junit tenant to run!");
		}
		else
		{
			final String exclusiveBean = Registry.getCoreApplicationContext().getBean("exclusiveJunitBean", String.class);
			assertEquals("Hello Junit World!", exclusiveBean);

			try
			{
				Registry.activateMasterTenant();
				final String mustNotExist = Registry.getCoreApplicationContext().getBean("exclusiveJunitBean", String.class);
				fail("expected missing bean 'exclusiveJunitBean' but found " + mustNotExist);
			}
			catch (final NoSuchBeanDefinitionException e)
			{
				// fine
			}
			finally
			{
				Registry.setCurrentTenant(tenant);
			}
		}
	}

	// see PLA-12166 as well
	@Test
	public void testTenantSwitch()
	{
		final Tenant currentTenant = Registry.getCurrentTenantNoFallback();

		// test activating same tenant -> should not fail
		Registry.setCurrentTenant(currentTenant);
		Registry.setCurrentTenant(currentTenant);
		Registry.setCurrentTenant(currentTenant);

		// test switching to master indirectly
		try
		{
			Registry.activateMasterTenant();
			Registry.activateMasterTenant();
			Registry.activateMasterTenant();
		}
		finally
		{
			Registry.setCurrentTenant(currentTenant);
		}

		// test switching to master by ID
		try
		{
			Registry.setCurrentTenantByID(MasterTenant.MASTERTENANT_ID);
			Registry.setCurrentTenantByID(MasterTenant.MASTERTENANT_ID);
			Registry.setCurrentTenantByID(MasterTenant.MASTERTENANT_ID);
		}
		finally
		{
			Registry.setCurrentTenant(currentTenant);
		}

		// test switching to master explicitly
		final MasterTenant masterTenant = Registry.getMasterTenant();
		try
		{
			Registry.setCurrentTenant(masterTenant);
			Registry.setCurrentTenant(masterTenant);
			Registry.setCurrentTenant(masterTenant);
		}
		finally
		{
			Registry.setCurrentTenant(currentTenant);
		}
	}

	@Test
	public void testSettings()
	{
		final Tenant t = Registry.getCurrentTenant();
		if ("junit".equalsIgnoreCase(t.getTenantID()))
		{
			final int pref = Registry.getPreferredClusterID();
			if (pref > -1)
			{
				assertTrue(t instanceof SlaveTenant);
				final SlaveTenant st = (SlaveTenant) t;
				assertEquals(pref, st.getClusterID());
				assertEquals(pref, st.getConfig().getInt(Config.Params.CLUSTER_ID, -1));
				assertEquals(pref, Registry.getMasterTenant().getClusterID());
			}
		}
	}

	@Test
	public void testStartupPerformance() throws Exception
	{
		final StopWatch sw = new StopWatch("test");

		JaloSession.getCurrentSession();
		final Tenant t = Registry.getCurrentTenant();
		for (int i = 0; i < 100000000; i++)
		{
			JaloSession.getCurrentSession(t);
		}
		sw.stop();
	}

	@Test
	public void testTenantErrors()
	{
		final Tenant t = Registry.getCurrentTenantNoFallback();

		// test exception in Registry.getCurrentTenant()
		if (Registry.FAILSAVE_NOTENANTACTIVE)
		{
			LOG.warn("cannot test Registry.getCurrentTenant() error since failsafe mode is enabled!");
		}
		else
		{
			try
			{
				Registry.unsetCurrentTenant();

				try
				{
					Registry.getCurrentTenant();
					fail("Registry.getCurrentTenant(): IllegalStateException since no tenant is active");
				}
				catch (final IllegalStateException e)
				{
					// fine
				}
			}
			finally
			{
				Registry.setCurrentTenant(t);
			}
		}
		// test wrong item in session context
		if (t instanceof SlaveTenant && t.getConfig().getBoolean("session.assert.tenant", false))
		{
			User masterAdmin = null;
			try
			{
				Registry.activateMasterTenant();
				assertFalse(t.equals(Registry.getCurrentTenantNoFallback()));
				masterAdmin = UserManager.getInstance().getAdminEmployee();
				assertFalse(t.equals(masterAdmin.getTenant()));
			}
			finally
			{
				Registry.setCurrentTenant(t);
			}
			try
			{
				jaloSession.setUser(masterAdmin);
				fail("SessionContext.assertTenant: IllegalArgumentException expected");
			}
			catch (final IllegalArgumentException e)
			{
				// expected
			}
			try
			{
				// try to hide item in list
				jaloSession.getSessionContext().setAttribute("foo", Arrays.asList(masterAdmin, "trallal"));
				fail("SessionContext.assertTenant: IllegalArgumentException expected");
			}
			catch (final IllegalArgumentException e)
			{
				// expected
			}
			try
			{
				// try to hide item in map as key
				jaloSession.getSessionContext().setAttribute("bar", Collections.singletonMap(masterAdmin, "trallal"));
				fail("SessionContext.assertTenant: IllegalArgumentException expected");
			}
			catch (final IllegalArgumentException e)
			{
				// expected
			}
			try
			{
				// try to hide item in map as key
				jaloSession.getSessionContext().setAttribute("blubb", Collections.singletonMap("trallal", masterAdmin));
				fail("SessionContext.assertTenant: IllegalArgumentException expected");
			}
			catch (final IllegalArgumentException e)
			{
				// expected
			}
		}
		else
		{
			LOG.warn("cannot test tenant check in session context since we're either "
					+ "in master tenant or checking has been switched off");
		}
	}

	@Test
	public void testExtensionSerialization() throws IOException, ClassNotFoundException
	{
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutputStream os = new ObjectOutputStream(bos);

		os.writeObject(ExtensionManager.getInstance().getExtensions());

		os.close();

		final ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		final ObjectInputStream is = new ObjectInputStream(bis);

		final List<? extends Extension> read = (List<? extends Extension>) is.readObject();

		is.close();

		assertEquals(ExtensionManager.getInstance().getExtensions(), read);

		for (final Extension rExt : read)
		{
			assertSame(ExtensionManager.getInstance().getExtension(rExt.getName()), rExt);
		}
	}

	@Test
	public void testManagerSerialization() throws IOException, ClassNotFoundException
	{
		ByteArrayOutputStream bos = null;
		ObjectOutputStream out = null;

		ByteArrayInputStream bis = null;
		ObjectInputStream in = null;
		try
		{
			//write 2 different managers into the stream and then alle managers as list
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(OrderManager.getInstance());
			out.writeObject(C2LManager.getInstance());
			out.writeObject(getManagers());
			out.close();

			//read the 2 diff. managers, than the list again
			bis = new ByteArrayInputStream(bos.toByteArray());
			in = new ObjectInputStream(bis);
			final OrderManager ordman = (OrderManager) in.readObject();
			assertEquals(OrderManager.getInstance(), ordman);
			final C2LManager c2lman = (C2LManager) in.readObject();
			assertEquals(C2LManager.getInstance(), c2lman);
			final List<Manager> manlist = (List<Manager>) in.readObject();

			for (int index = 0; index < getManagers().size(); index++)
			{
				assertSame(getManagers().get(index), manlist.get(index));
			}
		}
		catch (final IOException ex)
		{
			LOG.error("IOException", ex);
		}
		finally
		{
			if (out != null)
			{
				out.close();
			}
			if (bos != null)
			{
				bos.close();
			}
			if (in != null)
			{
				in.close();
			}
			if (bis != null)
			{
				bis.close();
			}
		}
	}

	private final List<Manager> getManagers()
	{
		return Arrays.asList(MetaInformationManager.getInstance(), TypeManager.getInstance(), EnumerationManager.getInstance(),
				C2LManager.getInstance(), UserManager.getInstance(), OrderManager.getInstance(), ProductManager.getInstance(),
				MediaManager.getInstance(), FlexibleSearch.getInstance(), LinkManager.getInstance(),
				NumberSeriesManager.getInstance(), AccessManager.getInstance(),
				// extensions must be managed as last ones since they might
				// depend upon some system functions
				ExtensionManager.getInstance());
	}

	@Test
	public void testTenantItemBehaviour() throws ConsistencyCheckException
	{
		final Tenant current = Registry.getCurrentTenant();
		if (current instanceof SlaveTenant)
		{
			final PK fixedPK = PK.createFixedPK(Constants.TC.Title, 1234567);
			Title t2Title = null;
			try
			{
				final Title t1Title = UserManager.getInstance().createTitle(fixedPK, "title");

				assertEquals(current, t1Title.getTenant());

				Registry.activateMasterTenant();

				final Tenant master = Registry.getCurrentTenant();
				assertTrue(master instanceof MasterTenant);

				if (master.getJaloConnection().isSystemInitialized())
				{
					t2Title = UserManager.getInstance().createTitle(fixedPK, "title");

					assertEquals(master, t2Title.getTenant());
					assertEquals(t1Title.getPK(), t2Title.getPK());
					assertFalse(t1Title.equals(t2Title));
				}
				else
				{
					System.err.println("Cannot use master tenant since it's not initialized");
				}
			}
			finally
			{
				if (t2Title != null)
				{
					try
					{
						t2Title.remove();
					}
					catch (final Exception e)
					{
						// can't help it
					}
				}
				Registry.setCurrentTenant(current);
			}
		}
	}
}
