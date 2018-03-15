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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.framework.EntityProxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Test;


/**
 * Junit Test for EJBCollections (EJBMap/EJBSet/EJBList)
 */
@UnitTest
public class EJBCollectionTest
{

	private static Map newMapInstance()
	{
		return new HashMap();
	}

	private static Map newMapInstance(@SuppressWarnings("unused") final int size)
	{
		return new HashMap();
	}

	@Test
	public void testDummyEJBEquals()
	{
		try
		{
			new DummyEJBObject("4", "_1").equals(new DummyEJBObject("5", "_2"));
		}
		catch (final UnsupportedOperationException e)
		{
			// DOCTODO Document reason, why this block is empty
		}
	}

	@Test
	public void testList1()
	{
		List list = new ArrayList();
		list.add("test");
		list.add("test2");
		assertTrue("doesn't contain", list.contains("test"));
		list.remove(0);
		assertFalse("contain", list.contains("test"));

		list = new ArrayList();
		list.add(new DummyEJBObject("pk0", "_1"));
		list.add(new DummyEJBObject("pk1", "_2"));

		final List list2 = new ArrayList();
		list2.add(new DummyEJBObject("pk0", "_3"));
		list2.add(new DummyEJBObject("pk1", "_4"));

		assertTrue("equals", list.equals(list2) && list2.equals(list));

		list2.add(new DummyEJBObject("pk1", "_5"));
		list2.add(new DummyEJBObject("pk1", "_6"));

		assertTrue("index", list2.indexOf(new DummyEJBObject("pk1", "anders")) == 1);
		assertTrue("index", list2.lastIndexOf(new DummyEJBObject("pk1", "anders")) == list2.size() - 1);

	}


	@Test
	public void testRemote1()
	{
		//test if contains() works on a EJBMap
		Map map = EJBCollectionTest.newMapInstance();
		map.put(new DummyEJBObject("5", "_1"), "asdfasdf");
		assertTrue("contains remote", map.keySet().contains(new DummyEJBObject("5", "_2")));

		//replace keys with put
		map = EJBCollectionTest.newMapInstance();
		map.put(new DummyEJBObject("4", "_1"), "kamelle");
		map.put(new DummyEJBObject("4", "_9"), "guelle");
		assertTrue("size not one but " + map.size(), map.size() == 1);
		final String str = (String) map.get(new DummyEJBObject("4", "_2"));
		assertTrue(str != null && str.equals("guelle"));

		//some tests with Set

		Set set = new HashSet();
		set.add(new DummyEJBObject("4", "_1"));
		assertTrue("contains", set.contains(new DummyEJBObject("4", "_2")));
		set.add(new DummyEJBObject("4", "_2"));
		assertTrue(set.size() == 1);

		set = new HashSet();
		set.add(new DummyEJBObject("4", "_1"));
		assertTrue("contains", set.contains(new DummyEJBObject("4", "_2")));
		set.add(new DummyEJBObject("4", "_2"));
		assertTrue(set.size() == 1);
	}

	@Test
	public void testClear()
	{
		final int MAX = 1000;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", map.size(), MAX);
		map.remove(Integer.valueOf(MAX - 10));
		map.remove(Integer.valueOf(MAX - 500));
		assertEquals("Map count", map.size(), MAX - 2);
		map.clear();
		assertEquals("Map count", map.size(), 0);
		assertFalse("Contains key ", map.containsKey(Integer.valueOf(MAX - 200)));
	}

	@Test
	public void testContains()
	{
		final int MAX = 1000;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", map.size(), MAX);
		for (int i = MAX; --i >= 0;)
		{
			assertTrue("Contains key " + i, map.containsKey(Integer.valueOf(i)));
		}
		for (int i = MAX; --i >= 0;)
		{
			assertTrue("Contains value " + i, map.containsValue(String.valueOf(i)));
		}
		assertFalse("Does not Contains " + (MAX + 5), map.containsKey(Integer.valueOf(MAX + 5)));
	}

	@Test
	public void testPutGet()
	{
		final int MAX = 1000;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", MAX, map.size());
		for (int i = MAX; --i >= 0;)
		{
			assertEquals("Getting element " + i, map.get(Integer.valueOf(i)), String.valueOf(i));
		}
		map.put(Integer.valueOf(MAX - 500), "HAHA");
		assertEquals("Map count", MAX, map.size());
		assertNull("No elements for this key ", map.get(Integer.valueOf(MAX + 5)));
	}

	@Test
	public void testRemove()
	{
		final int MAX = 1000;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", map.size(), MAX);
		map.remove(Integer.valueOf(MAX - 10));
		map.remove(Integer.valueOf(MAX - 500));
		assertEquals("Map count", map.size(), MAX - 2);
		assertFalse("Contains " + (MAX - 10), map.containsKey(Integer.valueOf(MAX - 10)));
		assertFalse("Contains " + (MAX - 500), map.containsKey(Integer.valueOf(MAX - 500)));
		assertTrue("Contains " + (MAX - 300), map.containsKey(Integer.valueOf(MAX - 300)));
	}

	@Test
	public void testConcurrency()
	{
		final int MAX = 1000;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", map.size(), MAX);
		final Set set = map.keySet();
		final Iterator iterator = set.iterator();
		final Integer position = (Integer) iterator.next();
		map.remove(position);
		assertEquals("new Map count", map.size(), MAX - 1);
		try
		{
			iterator.next();
			fail("ConcurrentException should be raised");
		}
		catch (final Exception e)
		{
			// DOCTODO Document reason, why this block is empty
		}
	}

	@Test
	public void testEmptyMap()
	{
		final Map map = EJBCollectionTest.newMapInstance();

		assertEquals("Map count", map.size(), 0);
		map.remove(Integer.valueOf(0));
		final Object object = map.get(Integer.valueOf(0));
		assertNull("null object", object);
		final Set set = map.keySet();
		for (final Iterator i = set.iterator(); i.hasNext();)
		{
			fail("Sould not come here");
		}
	}

	@Test
	public void testEntrySet()
	{
		final int MAX = 1000;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", map.size(), MAX);
		final Set set = map.entrySet();
		for (final Iterator i = set.iterator(); i.hasNext();)
		{
			final Map.Entry mapEntry = (Map.Entry) i.next();
			final Object key = mapEntry.getKey();
			final Object value = mapEntry.getValue();
			assertEquals("k==v", key.toString(), value);
		}
	}

	@Test
	public void testEquals()
	{
		final int MAX = 100;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		final HashMap hashMap1 = new HashMap();
		for (int i = MAX; --i >= 0;)
		{
			hashMap1.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("m==m1", map, hashMap1);
		assertEquals("m1==m", hashMap1, map);

		assertTrue("cleanmaps", EJBCollectionTest.newMapInstance().equals(new HashMap()));
		assertTrue("cleanmaps", EJBCollectionTest.newMapInstance().equals(EJBCollectionTest.newMapInstance()));
		assertTrue("samemaps", map.equals(map));
	}

	@Test
	public void testRemoteEquals()
	{
		final Map map = EJBCollectionTest.newMapInstance();
		map.put(new DummyEJBObject("4", "_1"), "irgendwas");
		map.put(new DummyEJBObject("5", "_2"), "irgendwas");

		final Map map2 = EJBCollectionTest.newMapInstance();
		map2.put(new DummyEJBObject("4", "_11"), "irgendwas");
		map2.put(new DummyEJBObject("5", "_22"), "irgendwas");

		assertTrue("equals", map.equals(map2) && map2.equals(map));

	}


	@Test
	public void testConsecutiveAddRemove()
	{
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = 100; --i >= 0;)
		{
			map.put(Integer.valueOf(2), "TEST");
			map.remove(Integer.valueOf(2));
		}
		assertTrue("no elements!", map.isEmpty());
		assertNull("no '2' element!", map.get(Integer.valueOf(2)));
	}

	@Test
	public void testConsecutiveAddRemove2()
	{
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = 0; i < 32; i++)
		{
			final Integer integer = Integer.valueOf(i);
			map.put(integer, integer);
			map.remove(integer);
		}

		// the following method invocation never returns...
		assertTrue("empty size", map.isEmpty());
		assertNull("no 2!", map.get(Integer.valueOf(2)));
		assertFalse("no 2 contains!", map.containsKey(Integer.valueOf(2)));
		assertNull("no 2 remove!", map.remove(Integer.valueOf(2)));
	}

	@Test
	public void testHashcodeEqual()
	{
		final Integer integer = Integer.valueOf(1);
		final Long long1 = Long.valueOf(12884901890L);
		final Long long2 = Long.valueOf(1L);
		assertTrue("equal hashCode()", integer.hashCode() == long1.hashCode());
		assertTrue("equal hashCode() 2", long1.hashCode() == long2.hashCode());
		///*conv-log*/ log.debug(i.hashCode());
		///*conv-log*/ log.debug(l.hashCode());
		final Map map = EJBCollectionTest.newMapInstance();
		map.put(integer, "INT");
		map.put(long1, "LONG");
		map.put(long2, "LONG2");
		assertTrue("INT not there!", map.get(integer).equals("INT"));
		assertTrue("LONG not there!", map.get(long1).equals("LONG"));
		assertTrue("LONG2 not there!", map.get(long2).equals("LONG2"));
		map.remove(integer);
		assertNull("after remove:INT not there!", map.get(integer));
	}

	@Test
	public void testOversize()
	{
		final Map map = EJBCollectionTest.newMapInstance(340);
		map.put(Integer.valueOf(2), "2");
		map.put(Integer.valueOf(514), "514");
		map.remove(Integer.valueOf(514));
		assertTrue("A Countains 2", map.containsKey(Integer.valueOf(2)));
		assertFalse("A Countains 514", map.containsKey(Integer.valueOf(514)));
		final Object object = map.get(Integer.valueOf(2));
		if (object != null)
		{
			assertEquals(object, "2");
		}
		else
		{
			fail();
		}
		map.clear();
		map.put(Integer.valueOf(2), "2");
		map.put(Integer.valueOf(514), "514");
		map.remove(Integer.valueOf(2));
		assertFalse("B Countains 2", map.containsKey(Integer.valueOf(2)));
		assertTrue("B Countains 514", map.containsKey(Integer.valueOf(514)));
		map.put(Integer.valueOf(2), "2");
		assertTrue("C Countains 2", map.containsKey(Integer.valueOf(2)));
		assertTrue("C Countains 514", map.containsKey(Integer.valueOf(514)));
		map.clear();
		map.put(Integer.valueOf(2), "2");
		map.put(Integer.valueOf(514), "514");
		map.remove(Integer.valueOf(2));
		map.remove(Integer.valueOf(2));
		assertTrue("D Countains 514", map.containsKey(Integer.valueOf(514)));
		map.clear();
		map.put(Integer.valueOf(2), "2");
		map.put(Integer.valueOf(514), "514");
		map.put(Integer.valueOf(513), "513");
		assertTrue("E Countains 513", map.containsKey(Integer.valueOf(513)));
		assertTrue("E Countains 514", map.containsKey(Integer.valueOf(514)));
		assertTrue("E Countains 2", map.containsKey(Integer.valueOf(2)));
		map.remove(Integer.valueOf(514));
		assertTrue("E Countains 513", map.containsKey(Integer.valueOf(513)));
	}

	@Test
	public void testEquals2()
	{
		final int MAX = 0;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		final HashMap hashMap = new HashMap();
		for (int i = MAX; --i >= 0;)
		{
			hashMap.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertTrue("map.size=0", map.isEmpty());
		assertTrue("hashMap.size=0", hashMap.isEmpty());
		assertEquals("map==hashMap", map, hashMap);
	}

	@Test
	public void testKeySet()
	{
		final int MAX = 1000;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", map.size(), MAX);
		final Set set = map.keySet();
		for (final Iterator i = set.iterator(); i.hasNext();)
		{
			final Integer integer = (Integer) i.next();
			assertTrue("Iterator Test key=" + integer, map.containsKey(integer));
		}


		//keys (keyset.iterator().remove()

		final List list = new ArrayList();
		list.add(new DummyEJBObject("pk1", "_1"));
		list.add(new DummyEJBObject("pk2", "_2"));
		list.add(new DummyEJBObject("pk3", "_3"));

		assertTrue("size", list.size() == 3);

		final Iterator iterator = list.iterator();
		iterator.next();
		iterator.next();
		iterator.remove();
		assertTrue("contains", list.contains(new DummyEJBObject("pk1", "_4")));
		assertTrue("contains", list.contains(new DummyEJBObject("pk3", "_4")));
		assertFalse("!contains", list.contains(new DummyEJBObject("pk2", "_4")));
	}

	@Test
	public void testNotEquals()
	{
		final int MAX = 100;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		final HashMap hashMap = new HashMap();
		for (int i = MAX + 1; --i >= 0;)
		{
			hashMap.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertFalse("map!=hashMap", map.equals(hashMap));
	}

	@Test
	public void testNotEquals2()
	{
		final int MAX = 100;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		final HashMap hashMap = new HashMap();
		for (int i = MAX; --i >= 0;)
		{
			hashMap.put(Integer.valueOf(i), String.valueOf(i));
		}
		map.put(Integer.valueOf(50), "");
		assertFalse("map!=hashMap", map.equals(hashMap));
	}


	@Test
	public void testValues()
	{
		final int MAX = 1000;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", map.size(), MAX);
		final Collection collection = map.values();
		for (final Iterator i = collection.iterator(); i.hasNext();)
		{
			final Object object = i.next();
			assertTrue("Iterator Test value=" + object, map.containsValue(object));
		}
	}


	@Test
	public void testEntryItrChange()
	{
		final Map map = EJBCollectionTest.newMapInstance(350);
		for (int i = 350; --i >= 0;)
		{
			map.put(Integer.valueOf(i), Integer.valueOf(i));
		}

		for (final Iterator i = map.entrySet().iterator(); i.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) i.next();
			entry.setValue(Integer.valueOf(3000));
		}

		for (int i = 350; --i >= 0;)
		{
			final Integer value = (Integer) map.get(Integer.valueOf(i));
			assertTrue(value.intValue() == 3000);
		}
	}

	@Test
	public void testEntryIterator()
	{
		final Map map = EJBCollectionTest.newMapInstance(350);
		final Random rand = new Random();
		for (int i = 350; --i >= 0;)
		{
			final int random = rand.nextInt(1024) & 0xFFFE;
			map.put(Integer.valueOf(random), Integer.valueOf(random));
		}

		/*
		 * Iterator itr = m.entrySet().iterator(); while (itr.hasNext()) { Map.Entry entry = (Map.Entry)itr.next();
		 * Integer v = (Integer)entry.getValue();
		 * 
		 * if (v.intValue()%2 == 0) { itr.remove(); } } for (Iterator itr2 = m.values().iterator(); itr2.hasNext();) {
		 * Integer i = (Integer)itr2.next(); if (i.intValue()%2 == 0) fail("even value in the map!"); }
		 */
	}

	@Test
	public void testKeyIterator()
	{
		final int MAX = 50;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", map.size(), MAX);
		Set set = map.keySet();
		int iteratorsize = 0;
		for (final Iterator i = set.iterator(); i.hasNext();)
		{
			final Object object = i.next();
			if (!map.containsKey(object))
			{
				fail("Map contains an unknown key!");
			}
			iteratorsize++;
		}
		assertEquals("Map size=iterator size", map.size(), iteratorsize);
		set = map.keySet();
		iteratorsize = 0;

		/*
		 * Iterator i = s.iterator(); for (; i.hasNext();) { Object o = i.next(); i.remove(); if (m.containsKey(o)) {
		 * fail("Iterator.remove didn't remove the root element"); } }
		 */
	}



	@Test
	public void testConcurrency2()
	{
		final int MAX = 1000;
		final Map map = EJBCollectionTest.newMapInstance();
		for (int i = MAX; --i >= 0;)
		{
			map.put(Integer.valueOf(i), String.valueOf(i));
		}
		assertEquals("Map count", map.size(), MAX);

		/*
		 * Set s = m.keySet(); Iterator i = s.iterator(); Integer k = (Integer)i.next(); i.remove();
		 * assertEquals("new Map count", m.size(), MAX - 1);
		 * 
		 * try { k = (Integer)i.next(); assertTrue("next()", k != null); m.put(Integer.valueOf(75), ""); i.remove();
		 * fail("ConcurrentException should be raised"); } catch (Exception e) { }
		 */

	}






	/*
	 * public void testClone () { int MAX = 1000; Map m1 = EJBCollectionTest.newMapInstance(); for (int i = MAX; --i >=
	 * 0;) { m1.put(Integer.valueOf(i), "" + i); } m1.put(Integer.valueOf(MAX - 500), new MyClone(1001)); Map m2 =
	 * (Map)m1.clone(); assertEquals("m1.size=m2.size", m1.size(), m2.size()); Object o = m2.remove(Integer.valueOf(MAX -
	 * 200)); assertEquals("object removed is MAX-200", (MAX - 200) + "", o); assertEquals("m1.size=MAX-1", MAX - 1,
	 * m2.size()); assertTrue("m1.size!=m2.size", m1.size() != m2.size()); MyClone mc1 =
	 * (MyClone)m1.get(Integer.valueOf(MAX - 500)); MyClone mc2 = (MyClone)m2.get(Integer.valueOf(MAX - 500));
	 * assertTrue("m1.MyClone(MAX-500)==1001", mc1.getV() == 1001); assertTrue("m2.MyClone(MAX-500)==1001", mc2.getV() ==
	 * 1001); mc2.setV(9534); mc1 = (MyClone)m1.get(new Integer(MAX - 500)); mc2 = (MyClone)m2.get(Integer.valueOf(MAX -
	 * 500)); assertTrue("m1.MyClone(MAX-500)==1001", mc1.getV() == 1001); assertTrue("m2.MyClone(MAX-500)==9534",
	 * mc2.getV() == 9534); }
	 */


}

class DummyEJBObject implements EntityProxy // NOPMD
{
	private final String pk;

	public DummyEJBObject(final String pk, @SuppressWarnings("unused") final String ident)
	{
		this.pk = pk;
	}

	@Override
	public boolean equals(final Object object)
	{

		if (!(object instanceof DummyEJBObject))
		{
			return false;
		}
		return pk.equals(((DummyEJBObject) object).pk);
	}

	@Override
	public int hashCode()
	{
		return pk.hashCode();
	}



	public String getPrimaryKey()
	{
		return this.pk;
	}

	@Override
	public PK getPK()
	{
		return PK.parse(this.pk);
	}


	public void remove()
	{
		// DOCTODO Document reason, why this block is empty
	}
}
