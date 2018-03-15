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
package de.hybris.platform.servicelayer.internal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;


/**
 * Localized values map using {@link ConcurrentHashMap} internally but masking NULL, which {@link ConcurrentHashMap} is
 * not able to.
 */
@UnitTest
public class LocMapTest
{
	@Test
	public void testMaskingNull()
	{
		final LocMap<Locale, String> testMap = new LocMap<Locale, String>();

		// before put
		assertNull(testMap.get(Locale.GERMAN));
		assertFalse(testMap.containsKey(Locale.GERMAN));

		// put NULL
		assertNull(testMap.put(Locale.GERMAN, null));
		assertNull(testMap.get(Locale.GERMAN));
		assertTrue(testMap.containsKey(Locale.GERMAN));
		// put NULL again -> previous was null again
		assertNull(testMap.put(Locale.GERMAN, null));

		// key set
		assertEquals(Collections.singleton(Locale.GERMAN), testMap.keySet());

		// value collection
		assertEquals(new HashSet<String>(Arrays.asList((String) null)), new HashSet<String>(testMap.values()));

		// entry set
		Set<Entry<Locale, String>> entrySet = testMap.entrySet();
		assertEquals(1, entrySet.size());
		final Entry<Locale, String> e = entrySet.iterator().next();
		assertEquals(Locale.GERMAN, e.getKey());
		assertNull(e.getValue());

		// put non-null
		assertNull(testMap.put(Locale.ENGLISH, "test"));
		assertEquals("test", testMap.get(Locale.ENGLISH));
		assertEquals("test", testMap.put(Locale.ENGLISH, "test"));
		// null value still in ?
		assertNull(testMap.get(Locale.GERMAN));
		assertTrue(testMap.containsKey(Locale.GERMAN));

		// key set
		assertEquals(new HashSet<Locale>(Arrays.asList(Locale.GERMAN, Locale.ENGLISH)), testMap.keySet());

		// value collection
		assertEquals(new HashSet<String>(Arrays.asList(null, "test")), new HashSet<String>(testMap.values()));

		// entry set
		entrySet = testMap.entrySet();
		assertEquals(2, entrySet.size());
		final Iterator<Entry<Locale, String>> it = entrySet.iterator();
		final Entry<Locale, String> e1 = it.next();
		final Entry<Locale, String> e2 = it.next();

		if (Locale.GERMAN.equals(e1.getKey()))
		{
			assertNull(e1.getValue());

			assertEquals(Locale.ENGLISH, e2.getKey());
			assertEquals("test", e2.getValue());
		}
		else
		{
			assertEquals(Locale.GERMAN, e2.getKey());
			assertNull(e2.getValue());

			assertEquals(Locale.ENGLISH, e1.getKey());
			assertEquals("test", e1.getValue());

		}
	}
}
