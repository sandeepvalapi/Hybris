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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.impl.RegionCacheAdapter;
import de.hybris.platform.core.AbstractTenantInitializationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.StandardSearchResult;
import de.hybris.platform.util.Utilities;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;



@IntegrationTest
public class SharedCacheTest extends AbstractTenantInitializationTest
{

	private static final Logger LOG = Logger.getLogger(SharedCacheTest.class.getName());

	@Override
	protected Collection<String> getTenantIds()
	{
		return Arrays.asList("t1", "t2");
	}

	@Test
	public void testSharedCache() throws ConsistencyCheckException
	{

		final SlaveTenant t1 = Registry.getSlaveTenants().get("t1");
		final SlaveTenant t2 = Registry.getSlaveTenants().get("t2");
		// test cache status

		assertEquals("true", t1.getConfig().getString(Cache.CONFIG_CACHE_SHARED, null));
		assertEquals("true", t2.getConfig().getString(Cache.CONFIG_CACHE_SHARED, null));

		final Cache c1 = t1.getCache();
		final Cache c2 = t2.getCache();

		assertEquals(c1.getMaxAllowedSize(), c2.getMaxAllowedSize());
		assertNotSame(c1, c2);

		assertTrue("cache bases are different", isSharingBase(c1, c2));


		final User ut2;

		final PK fixed = PK.createFixedCounterPK(Constants.TC.User, 22222);

		Registry.setCurrentTenant(t1);

		final User ut1 = UserManager.getInstance().createEmployee(fixed, "foo");
		ut1.setName("t1 empl");

		Registry.setCurrentTenant(t2);

		ut2 = UserManager.getInstance().createEmployee(fixed, "foo");
		ut2.setName("t2 empl");

		Registry.setCurrentTenant(t1);

		final User ut1Fresh = JaloSession.getCurrentSession().getItem(fixed);
		assertEquals(fixed, ut1Fresh.getPK());
		assertEquals("foo", ut1Fresh.getUID());
		assertEquals("t1 empl", ut1Fresh.getName());

		Registry.setCurrentTenant(t2);

		final User ut2Fresh = JaloSession.getCurrentSession().getItem(fixed);
		assertEquals("foo", ut2Fresh.getUID());
		assertEquals("t2 empl", ut2Fresh.getName());
		assertFalse(ut1Fresh == ut2Fresh);

		ut2Fresh.setDescription("t2 descr"); // now change user in t2 -> must be invalidated
		assertTrue(isGetterSetterCacheCleared(ut2Fresh));
		assertFalse(isGetterSetterCacheCleared(ut1Fresh));

		Registry.setCurrentTenant(t1);

		final User ut1Fresh2 = JaloSession.getCurrentSession().getItem(fixed);
		assertEquals(fixed, ut1Fresh2.getPK());
		assertEquals("foo", ut1Fresh2.getUID());
		assertEquals("t1 empl", ut1Fresh2.getName());
		assertNull(ut1Fresh2.getDescription());
		assertTrue(ut1Fresh == ut1Fresh2); // same instance - not invalidate by now

		Registry.setCurrentTenant(t2);

		final User ut2Fresh2 = JaloSession.getCurrentSession().getItem(fixed);
		assertEquals("foo", ut2Fresh2.getUID());
		assertEquals("t2 empl", ut2Fresh2.getName());
		assertEquals("t2 descr", ut2Fresh2.getDescription());
		// even though ut2 has been invalidated the jalo item still remains in cache
		// this is possible since it holds a internal cache map itself which has been invalidated
		// instead
		assertTrue(ut2Fresh2.isCacheBound());
		assertTrue(ut2Fresh.isCacheBound());
		assertTrue(ut2Fresh == ut2Fresh2);

		// now test queries

		final List<Employee> t2Res = FlexibleSearch.getInstance().search(//
				"SELECT {pk} FROM {Employee} WHERE {uid}='foo'", //
				Collections.EMPTY_MAP,//
				Employee.class//
				).getResult();

		assertEquals(Collections.singletonList(ut2Fresh2), t2Res);

		Registry.setCurrentTenant(t1);

		final List<Employee> t1Res = FlexibleSearch.getInstance().search(//
				"SELECT {pk} FROM {Employee} WHERE {uid}='foo'", //
				Collections.EMPTY_MAP,//
				Employee.class//
				).getResult();

		assertEquals(Collections.singletonList(ut1Fresh2), t1Res);

		ut1Fresh2.setName("name");

		Registry.setCurrentTenant(t2);

		final SearchResult<Employee> sr2 = FlexibleSearch.getInstance().search(//
				"SELECT {pk} FROM {Employee} WHERE {uid}='foo'", //
				Collections.EMPTY_MAP,//
				Employee.class//
				);
		assertTrue(((StandardSearchResult<Employee>) sr2).isFromCache());
		assertEquals(Collections.singletonList(ut2Fresh2), sr2.getResult());

		Registry.setCurrentTenant(t1);

		final SearchResult<Employee> sr1 = FlexibleSearch.getInstance().search(//
				"SELECT {pk} FROM {Employee} WHERE {uid}='foo'", //
				Collections.EMPTY_MAP,//
				Employee.class//
				);
		assertFalse(((StandardSearchResult<Employee>) sr1).isFromCache());
		assertEquals(Collections.singletonList(ut1Fresh2), sr1.getResult());

		// test clear

		Registry.getCurrentTenant().getCache().clear(); // t1

		final User ut1Fresh3 = JaloSession.getCurrentSession().getItem(fixed);
		assertEquals(fixed, ut1Fresh3.getPK());
		assertEquals("foo", ut1Fresh3.getUID());
		assertEquals("name", ut1Fresh3.getName());
		assertNull(ut1Fresh3.getDescription());
		assertFalse("items are same", ut1Fresh2 == ut1Fresh3);
		assertTrue("item is not cache bound", ut1Fresh3.isCacheBound());
		assertFalse("item is still cache bound", ut1Fresh.isCacheBound());
		assertFalse("item is still cache bound", ut1Fresh2.isCacheBound());

		Registry.setCurrentTenant(t2);

		final User ut2Fresh3 = JaloSession.getCurrentSession().getItem(fixed);
		assertEquals(fixed, ut2Fresh3.getPK());
		assertEquals("foo", ut2Fresh3.getUID());
		assertEquals("t2 empl", ut2Fresh3.getName());
		assertEquals("t2 descr", ut2Fresh3.getDescription());
		assertFalse("item instances are same", ut2Fresh2 == ut2Fresh3);
		assertTrue("item is not cache bound", ut2Fresh3.isCacheBound());
		assertFalse("item is still cache bound", ut2Fresh.isCacheBound());
		assertFalse("item is still cache bound", ut2Fresh2.isCacheBound());
	}

	boolean isSharingBase(final Cache c1, final Cache c2)
	{
		try
		{
			if (c1 instanceof RegionCacheAdapter)
			{
				return c1.equals(c2);
			}
			else
			{

				final Field f = ReflectionUtils.findField(c1.getClass(), "cacheBase");
				assertNotNull("no such field Cache.cacheBase", f);
				f.setAccessible(true);
				final Object base1 = ReflectionUtils.getField(f, c1);
				final Object base2 = ReflectionUtils.getField(f, c2);

				return base1 == base2;
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error checking getter/setter cache : " + e.getMessage());
			return false;
		}
	}

	boolean isGetterSetterCacheCleared(final Item i)
	{
		try
		{
			final Field f = ReflectionUtils.findField(i.getClass(), "localItemCacheRef");
			assertNotNull("no such field Item.localItemCacheRef", f);
			f.setAccessible(true);
			final AtomicReference cacheRef = (AtomicReference) ReflectionUtils.getField(f, i);
			return cacheRef != null && cacheRef.get() == null;
		}
		catch (final Exception e)
		{
			fail("error checking getter/setter cache : " + e.getMessage() + ":" + Utilities.getStackTraceAsString(e));
			return false;
		}
	}
}
