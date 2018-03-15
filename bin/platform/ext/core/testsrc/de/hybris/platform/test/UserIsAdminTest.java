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

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.Item.ItemAttributeMap;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.config.ConfigIntf;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@PerformanceTest
public class UserIsAdminTest extends HybrisJUnit4Test
{
	static final int THREADS = 50;
	static final int DURATION = 20;
	static final int CREATE_NEW_INTERVAL = 1;

	@Before
	public void setup() throws Exception
	{
		final CoreBasicDataCreator creator = new CoreBasicDataCreator();
		creator.createBasicC2L();
		creator.createBasicUserGroups();

		final ComposedType type = TypeManager.getInstance().getComposedType(Customer.class);
		final AttributeDescriptor descriptor = type.getAttributeDescriptor(User.GROUPS);
		final Object defaultValue = descriptor.getDefaultValue();
		Assert.assertNotNull(defaultValue);
		Assert.assertTrue(defaultValue instanceof Collection);

		final UserGroup customerGroup = UserManager.getInstance().getUserGroupByGroupID(Constants.USER.CUSTOMER_USERGROUP);
		Assert.assertEquals(Collections.singleton(customerGroup), defaultValue);

		final Customer anon = UserManager.getInstance().getAnonymousCustomer();
		Assert.assertTrue(anon.getGroups().contains(customerGroup));

		Assert.assertTrue(UserManager.getInstance().getAdminEmployee().isAdmin());
	}

	@Test
	public void testCorrectAdminCacheInvalidation() throws ConsistencyCheckException
	{
		final Employee emp = UserManager.getInstance().createEmployee("foo");

		Assert.assertFalse(emp.isAdmin());

		final UserGroup ug = UserManager.getInstance().createUserGroup("barGr");
		ug.setGroups((Set) Collections.singleton(UserManager.getInstance().getAdminUserGroup()));

		Assert.assertFalse(emp.isAdmin());

		emp.setGroups((Set) Collections.singleton(ug));

		Assert.assertTrue(emp.isAdmin());
	}

	@Test
	public void testAnonymousIsAdmin() throws ConsistencyCheckException
	{
		final Customer anon = UserManager.getInstance().getAnonymousCustomer();
		{
			final long iAnonWithGroups = testIsAdmin(true, Collections.singletonList(anon.getPK()));
			System.out.println("isAdmin(), anonymous, " + THREADS + " threads, new " + CREATE_NEW_INTERVAL
					+ " seconds, with groups -> " + NumberFormat.getIntegerInstance().format(iAnonWithGroups) + " invocations.");
		}
		{
			final long iAnonWoGroups = testIsAdmin(false, Collections.singletonList(anon.getPK()));
			System.out.println("isAdmin(), anonymous, " + THREADS + " threads, new " + CREATE_NEW_INTERVAL
					+ " seconds, without groups -> " + NumberFormat.getIntegerInstance().format(iAnonWoGroups) + " invocations.");
		}
		{
			final long iAnonWoCreate = testIsAdmin(false, Collections.singletonList(anon.getPK()), false);
			System.out.println("isAdmin(), anonymous, " + THREADS + " threads, no new customers -> "
					+ NumberFormat.getIntegerInstance().format(iAnonWoCreate) + " invocations.");
		}
	}

	@Test
	public void testAdminIsAdmin() throws ConsistencyCheckException
	{
		final Employee admin = UserManager.getInstance().getAdminEmployee();
		{
			final long iAnonWithGroups = testIsAdmin(true, Collections.singletonList(admin.getPK()));
			System.out.println("isAdmin(), admin, " + THREADS + " threads, new " + CREATE_NEW_INTERVAL + " seconds, with groups -> "
					+ NumberFormat.getIntegerInstance().format(iAnonWithGroups) + " invocations.");
		}
		{
			final long iAnonWoGroups = testIsAdmin(false, Collections.singletonList(admin.getPK()));
			System.out.println("isAdmin(), admin, " + THREADS + " threads, new " + CREATE_NEW_INTERVAL
					+ " seconds, without groups -> " + NumberFormat.getIntegerInstance().format(iAnonWoGroups) + " invocations.");
		}
		{
			final long iAnonWoCreate = testIsAdmin(false, Collections.singletonList(admin.getPK()), false);
			System.out.println("isAdmin(), admin, " + THREADS + " threads, no new customers -> "
					+ NumberFormat.getIntegerInstance().format(iAnonWoCreate) + " invocations.");
		}
	}

	@Test
	public void testCustomerIsAdminNotOptimized() throws ConsistencyCheckException
	{
		testCustomerIsAdmin(true);
	}

	@Test
	public void testCustomerIsAdminOptimized() throws ConsistencyCheckException
	{
		testCustomerIsAdmin(false);
	}

	protected void testCustomerIsAdmin(final boolean allowCustomerAsAdmin) throws ConsistencyCheckException
	{
		final Customer anon = UserManager.getInstance().getAnonymousCustomer();
		final List<PK> userPKs = generateCustomers(THREADS);
		userPKs.add(anon.getPK());
		final String before = setCustomerAsAdmin(allowCustomerAsAdmin);
		clearCustomerAsAdminCache();
		try
		{
			{
				final long iCustOptWoGroups = testIsAdmin(false, userPKs, false);
				System.out.println("isAdmin(), " + userPKs.size() + (!allowCustomerAsAdmin ? " optimized" : " not optimized")
						+ " customers, " + THREADS + " threads, no new customers -> "
						+ NumberFormat.getIntegerInstance().format(iCustOptWoGroups) + " invocations.");
			}
			{
				final long iCustOptWoGroups = testIsAdmin(false, userPKs);
				System.out.println("isAdmin(), " + userPKs.size() + (!allowCustomerAsAdmin ? " optimized" : " not optimized")
						+ " customers, " + THREADS + " threads, new " + CREATE_NEW_INTERVAL + " seconds, without groups -> "
						+ NumberFormat.getIntegerInstance().format(iCustOptWoGroups) + " invocations.");
			}
			{
				final long iCustOptWithGroups = testIsAdmin(true, userPKs);
				System.out.println("isAdmin(), " + userPKs.size() + (!allowCustomerAsAdmin ? " optimized" : " not optimized")
						+ " customers, " + THREADS + " threads, new " + CREATE_NEW_INTERVAL + " seconds, with groups -> "
						+ NumberFormat.getIntegerInstance().format(iCustOptWithGroups) + " invocations.");
			}
		}
		finally
		{
			Registry.getCurrentTenantNoFallback().getConfig().setParameter(UserManager.CONFIG_ALLOW_CUSTOMER_ADMIN, before);
			clearCustomerAsAdminCache();
		}
	}

	protected String setCustomerAsAdmin(final boolean allow)
	{
		final ConfigIntf cfg = Registry.getCurrentTenantNoFallback().getConfig();
		final String before = cfg.getParameter(UserManager.CONFIG_ALLOW_CUSTOMER_ADMIN);
		cfg.setParameter(UserManager.CONFIG_ALLOW_CUSTOMER_ADMIN, Boolean.toString(allow));
		return before;
	}

	protected void clearCustomerAsAdminCache()
	{
		try
		{
			final Field field = UserManager.class.getDeclaredField("allowCustomerAsAdminCache");
			field.setAccessible(true);
			field.set(null, null);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	protected List<PK> generateCustomers(final int amount) throws ConsistencyCheckException
	{
		final UserManager um = UserManager.getInstance();
		final List<PK> userPKs = new ArrayList<PK>();
		final String prefix = "NewCust-" + System.currentTimeMillis() + "-";
		for (int i = 0; i < amount; i++)
		{
			final Customer customer = um.createCustomer(prefix + i);
			Assert.assertFalse(customer.getGroups().isEmpty());
			userPKs.add(customer.getPK());
		}
		return userPKs;

	}

	protected long testIsAdmin(final boolean withGroups, final List<PK> userPKs)
	{
		return testIsAdmin(withGroups, userPKs, true);
	}

	protected long testIsAdmin(final boolean withGroups, final List<PK> userPKs, final boolean createNewCustomers)
	{
		final TestThreadsHolder<AbstractIsAdminRunner> runners = new TestThreadsHolder<AbstractIsAdminRunner>(THREADS
				+ (createNewCustomers ? 1 : 0), true)
		{
			@Override
			public AbstractIsAdminRunner newRunner(final int threadNumber)
			{
				return createNewCustomers && threadNumber == THREADS ? new CreateNewCustomersRunner("NewCust-"
						+ System.currentTimeMillis() + "-", CREATE_NEW_INTERVAL, withGroups) : new IsAdminRunner(userPKs);
			}
		};

		Assert.assertTrue(runners.runAll(DURATION, TimeUnit.SECONDS, DURATION / 4));
		Assert.assertEquals(Collections.EMPTY_MAP, runners.getErrors());

		long totalInvocations = 0;
		for (final AbstractIsAdminRunner r : runners.getRunners())
		{
			if (r instanceof IsAdminRunner && ((IsAdminRunner) r).invocations > 0)
			{
				totalInvocations += ((IsAdminRunner) r).invocations;
			}
		}
		return totalInvocations;
	}

	static abstract class AbstractIsAdminRunner implements Runnable
	{
		//
	}

	static class CreateNewCustomersRunner extends AbstractIsAdminRunner
	{
		final int interval;
		final boolean withGroups;
		final String prefix;
		volatile List<PK> newCustomerPKs;

		CreateNewCustomersRunner(final String prefix, final int interval, final boolean withGroups)
		{
			this.interval = interval;
			this.withGroups = withGroups;
			this.prefix = prefix;
		}

		@Override
		public void run()
		{
			final JaloSession session = JaloSession.getCurrentSession();
			final Thread currentThread = Thread.currentThread();
			final List<PK> tmp = new ArrayList<PK>();
			while (!currentThread.isInterrupted())
			{
				try
				{
					Thread.sleep(interval * 1000);
				}
				catch (final InterruptedException e)
				{
					currentThread.interrupt();
					break;
				}
				try
				{
					final ItemAttributeMap attributes = new ItemAttributeMap();
					attributes.put(User.UID, prefix + tmp.size());
					if (!withGroups)
					{
						attributes.put(User.GROUPS, Collections.EMPTY_LIST);
					}
					final Customer customer = ComposedType.newInstance(session.getSessionContext(), Customer.class, attributes);
					tmp.add(customer.getPK());
				}
				catch (final Exception e)
				{
					throw new RuntimeException(e);
				}
			}
			newCustomerPKs = tmp;
		}
	}

	static class IsAdminRunner extends AbstractIsAdminRunner
	{
		final List<PK> userPKs;

		volatile long invocations = -1;

		long adminCount = 0;
		long notAdminCount = 0;

		IsAdminRunner(final List<PK> userPKs)
		{
			this.userPKs = userPKs;
		}

		@Override
		public void run()
		{
			final JaloSession session = JaloSession.getCurrentSession();
			final long hash = hashCode();
			long l = 0;
			final long size = userPKs.size();



			final Thread currentThread = Thread.currentThread();
			while (!currentThread.isInterrupted())
			{
				final int rnd = (int) ((hash ^ l) % size);
				final User u = session.getItem(userPKs.get(rnd));
				if (u.isAdmin())
				{
					adminCount++;
				}
				else
				{
					notAdminCount++;
				}
				l++;
			}
			invocations = l;
		}
	}
}
