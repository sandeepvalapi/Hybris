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

import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.property.EJBProperty;
import de.hybris.platform.persistence.property.EJBPropertyCache;
import de.hybris.platform.persistence.property.EJBPropertyRowCache;
import de.hybris.platform.persistence.test.TestItemHome;
import de.hybris.platform.persistence.test.TestItemRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Utilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;


@IntegrationTest
public class EJBPropertyCacheTest extends HybrisJUnit4Test
{
	@Test
	public void testEmptyInitialization()
	{
		final EJBPropertyCache cache = EJBPropertyCache.create(1234567L);
		assertFalse(cache.needsUpdate());
		assertEquals(Collections.EMPTY_MAP, cache.getPropertyValues(null));
		assertEquals(0, cache.getUpdateableProperties().size());
	}

	@Test
	public void testColumnChange()
	{
		final List NAMES = Arrays.asList(new String[]
		{ "1", "3", "4" });
		final EJBPropertyRowCache prc = EJBPropertyRowCache.create(0, NAMES);
		// test some basic settings
		assertEquals(3, prc.getColumnCount());
		assertEquals(PK.NULL_PK, prc.getLangPK());
		assertFalse(prc.hasChanged());
		assertFalse(prc.isInDatabase());
		// names
		assertEquals("1", prc.getName(0));
		assertEquals("3", prc.getName(1));
		assertEquals("4", prc.getName(2));
		// value
		assertNull(prc.getValue(0));
		assertNull(prc.getValue(1));
		assertNull(prc.getValue(2));

		// change some columns
		try
		{
			prc.changeColumns(Arrays.asList(new String[]
			{ "9", "2", "7", "1" }));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		// test some basic settings
		assertEquals(6, prc.getColumnCount());
		assertEquals(PK.NULL_PK, prc.getLangPK());
		assertFalse(prc.hasChanged());
		assertFalse(prc.isInDatabase());
		// names
		assertEquals("1", prc.getName(0));
		assertEquals("2", prc.getName(1));
		assertEquals("3", prc.getName(2));
		assertEquals("4", prc.getName(3));
		assertEquals("7", prc.getName(4));
		assertEquals("9", prc.getName(5));
		// value
		assertNull(prc.getValue(0));
		assertNull(prc.getValue(1));
		assertNull(prc.getValue(2));
		assertNull(prc.getValue(3));
		assertNull(prc.getValue(4));
		assertNull(prc.getValue(5));
	}

	@Test
	public void testPseudoChange()
	{
		final List props = Arrays.asList(new Object[]
		{ EJBProperty.load("prop1", null, "value1") });
		final EJBPropertyCache cache = EJBPropertyCache.load(987654321L, props);
		assertFalse(cache.needsUpdate());

		cache.setProperty("prop2", "value2");
		assertTrue(cache.needsUpdate());
		Collection update = cache.getUpdateableProperties();
		assertEquals(update.toString(), 1, update.size());
		final EJBProperty property = (EJBProperty) update.iterator().next();
		assertEquals(update.toString(), "prop2", property.getName());
		assertEquals(update.toString(), "value2", property.getValue1Internal());

		cache.removeProperty("prop2");
		update = cache.getUpdateableProperties();

		final Iterator iter = update.iterator();
		while (iter.hasNext())
		{
			final EJBProperty next = (EJBProperty) iter.next();
			assertFalse(next.hasChanged());
		}
		assertCollection(Collections.EMPTY_LIST, update);
		assertFalse(cache.needsUpdate());
	}

	@Test
	public void testTypeChange()
	{
		TestItemRemote item = null;
		try
		{
			final TypeManagerEJB typeManagerEJB = SystemEJB.getInstance().getTypeManager();
			final ComposedTypeRemote testItemType1 = typeManagerEJB.getRootComposedType(Constants.TC.TestItem);
			assertNotNull(testItemType1);

			final Transaction tx = Transaction.current();
			tx.begin();

			boolean success = false;

			try
			{
				final TestItemHome home = (TestItemHome) Registry.getCurrentTenant().getPersistencePool()
						.getHomeProxy(Registry.getPersistenceManager().getJNDIName(Constants.TC.TestItem));
				item = home.create();

				assertTrue(testItemType1.equals(item.getComposedType()));
				try
				{
					assertNull(typeManagerEJB.getAttributeDescriptor(testItemType1, "testProperty1"));
				}
				catch (final EJBItemNotFoundException e)
				{
					// fine
				}
				try
				{
					assertNotNull(typeManagerEJB.getAttributeDescriptor(testItemType1, "testProperty0"));
				}
				catch (final EJBItemNotFoundException e)
				{
					// bad
					fail("type1 doesnt have 'testProperty0' feature");
				}
				// set dump property
				item.setProperty("blah", "blubb");
				// set unloc property
				item.setProperty("testProperty0", "this will load unloc cache");
				// set as dump property
				item.setProperty("testProperty1", Integer.valueOf(10));

				assertEquals("blubb", item.getProperty("blah"));
				assertEquals("this will load unloc cache", item.getProperty("testProperty0"));
				assertEquals(Integer.valueOf(10), item.getProperty("testProperty1"));

				final ComposedTypeRemote testItemType2 = SystemEJB.getInstance().getTypeManager().getComposedType("TestItemType2");
				try
				{
					assertNotNull(typeManagerEJB.getAttributeDescriptor(testItemType2, "testProperty1"));
				}
				catch (final EJBItemNotFoundException e)
				{
					// bad
					fail("type2 doesnt have 'testProperty1' feature");
				}
				assertNotNull(testItemType2);
				assertFalse("type == type2", Utilities.ejbEquals(testItemType1, testItemType2));

				// set another type 
				item.setComposedType(testItemType2);
				// 'testProperty1' is typed now
				item.setProperty("testProperty1", Integer.valueOf(20));
				assertEquals(Integer.valueOf(20), item.getProperty("testProperty1"));
				success = true;
			}
			finally
			{
				if (success)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			item.remove();
			item = null;
		}
		catch (final Exception e)
		{
			item = null;
			e.printStackTrace();
			fail("error : " + e);
		}
		finally
		{
			if (item != null)
			{
				try
				{
					item.remove();
				}
				catch (final Exception e)
				{
					// DOCTODO Document reason, why this block is empty
				}
			}
		}
	}

}
