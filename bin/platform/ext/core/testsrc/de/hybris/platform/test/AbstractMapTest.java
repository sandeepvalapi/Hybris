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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * super class for tests of {@link Map} implementations
 * 
 * 
 * 
 */
@Ignore
public abstract class AbstractMapTest
{
	protected Map map;

	@Before
	public void setUp() throws Exception
	{
		map = createMapInstance();
	}

	protected abstract Map createMapInstance();

	@Test
	public void testClear()
	{
		assertTrue(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
		map.put("1", "eins");
		final Set keys = map.keySet();
		final Collection values = map.values();
		final Set entries = map.entrySet();
		assertFalse(map.isEmpty());
		assertFalse(keys.isEmpty());
		assertFalse(values.isEmpty());
		assertFalse(entries.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
		assertTrue(keys.isEmpty());
		assertTrue(values.isEmpty());
		assertTrue(entries.isEmpty());
	}

	@Test
	public void testContainsKeyValue()
	{
		map.put("1", "eins");
		assertTrue(map.containsKey("1"));
		assertTrue(map.containsValue("eins"));
		assertFalse(map.containsKey("eins"));
		assertFalse(map.containsValue("1"));
	}

	@Test
	public void testEqualsAndHash()
	{
		final Map map2 = createMapInstance();

		map.put("1", "eins");
		map2.put("1", "eins");

		map.put("2", Long.valueOf(2));
		map2.put("2", Long.valueOf(2));

		// are both maps equal?
		assertEquals(map, map2);

		// and do they have the same hashcode?
		assertTrue(map.hashCode() == map2.hashCode());

		// what about maps of different implementations, but the same content? 
		// if equals() is implemented with a wrong 'instanceof' condition, this might fail.
		final Map map3 = new HashMap();
		map3.put("1", "eins");
		map3.put("2", Long.valueOf(2));
		assertEquals(map, map3);
		assertEquals(map3, map);

		// and wrapped maps?  
		assertEquals(Collections.unmodifiableMap(map), Collections.unmodifiableMap(map2));
		assertEquals(Collections.unmodifiableMap(map2), Collections.unmodifiableMap(map));
	}

	/**
	 * Tests if this map's toString() method is applying the commonly used method to print its contents as a String (see
	 * {@link java.util.AbstractMap#toString()}, for example).
	 * 
	 * However, the 'Map contract' does not enforce the way this method works, so if your map implementation does not use
	 * this standard representation, you might want to override this method here in your own test.
	 */
	@Test
	public void testToString()
	{
		map.put("1", "eins");
		map.put("2", "zwei");
		//assertEquals( "{1=eins, 2=zwei}", map.toString() );
	}

}
