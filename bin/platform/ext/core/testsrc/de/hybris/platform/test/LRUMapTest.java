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

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.junit.Test;


@UnitTest
public class LRUMapTest extends AbstractMapTest
{

	@Override
	protected Map createMapInstance()
	{
		return new LRUMap(100);
	}

	@Test
	public void testBug()
	{
		final LRUMap map = new LRUMap(2);
		map.put("1", "eins");
		map.put("2", "zwei");
		assertEquals("eins", map.get("1"));
		assertEquals("zwei", map.get("2"));

		map.put("3", "drei");
		assertEquals(null, map.get("1"));
		assertEquals("zwei", map.get("2"));
		assertEquals("drei", map.get("3"));

		map.get("2");
		map.put("4", "vier");

		assertEquals(null, map.get("1"));
		assertEquals("zwei", map.get("2"));
		assertEquals(null, map.get("3"));
		assertEquals("vier", map.get("4"));
	}

	@Test
	public void testAgain()
	{
		final LRUMap map = new LRUMap(2);
		map.put("2", "zwei");
		map.put("3", "drei");

		map.get("2");
		map.put("4", "vier");

		assertEquals("zwei", map.get("2"));
		assertEquals(null, map.get("3"));
		assertEquals("vier", map.get("4"));
	}
}
