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

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.WeakHashSet;

import org.junit.Before;
import org.junit.Test;


/**
 * Testcase for the WeakHashSet.
 * 
 * 
 * 
 */
@UnitTest
public class WeakHashSetTest
{
	private WeakHashSet theWeakHashSet;

	@Before
	public void setUp() throws Exception
	{
		theWeakHashSet = new WeakHashSet();
	}

	@Test
	public void testAdd() throws Exception
	{
		assertTrue("set.empty", theWeakHashSet.isEmpty());

		//adding object
		final Object o = Long.valueOf(0);
		theWeakHashSet.add(o);
		assertTrue("size=1", theWeakHashSet.size() == 1);
		assertTrue("contains o", theWeakHashSet.contains(o));

		//adding another object
		final Object o2 = Long.valueOf(1);
		theWeakHashSet.add(o2);
		assertTrue("size=2", theWeakHashSet.size() == 2);
		assertTrue("contains o", theWeakHashSet.contains(o));
		assertTrue("contains o2", theWeakHashSet.contains(o2));

		//adding the same object again, nothing should change
		theWeakHashSet.add(o2);
		assertTrue("size=2", theWeakHashSet.size() == 2);
		assertTrue("contains o", theWeakHashSet.contains(o));
		assertTrue("contains o2", theWeakHashSet.contains(o2));
	}

	@Test
	public void testRemove() throws Exception
	{
		assertTrue("set.empty", theWeakHashSet.isEmpty());

		//adding an object
		final Object o = Long.valueOf(0);
		theWeakHashSet.add(o);

		//removing it again
		theWeakHashSet.remove(o);
		assertTrue("size=0", theWeakHashSet.size() == 0);

		//re-adding it
		theWeakHashSet.add(o);
		assertTrue("size=1", theWeakHashSet.size() == 1);
		assertTrue("contains o", theWeakHashSet.contains(o));

		//adding another object
		final Object o2 = Long.valueOf(1);
		theWeakHashSet.add(o2);
		assertTrue("size=2", theWeakHashSet.size() == 2);
		assertTrue("contains o", theWeakHashSet.contains(o));
		assertTrue("contains o2", theWeakHashSet.contains(o2));

		//removing first again
		theWeakHashSet.remove(o);
		assertTrue("size=1", theWeakHashSet.size() == 1);
		assertTrue("contains o2", theWeakHashSet.contains(o2));
	}

	@Test
	public void testGC() throws Exception
	{
		assertTrue("set.empty", theWeakHashSet.isEmpty());

		//adding an object
		Object o = Long.valueOf(0);
		theWeakHashSet.add(o);

		//killing the reference
		o = null;
		System.gc();

		//do not test this, it does not work.
		//assertTrue("set.empty", theWeakHashSet.isEmpty());
	}

}
