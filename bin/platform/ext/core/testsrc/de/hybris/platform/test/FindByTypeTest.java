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

import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class FindByTypeTest extends HybrisJUnit4TransactionalTest
{
	private ProductManager pm;
	private Unit u1, u2, u3;
	private Collection previousUnitTypes;

	@Before
	public void setUp() throws Exception
	{
		pm = jaloSession.getProductManager();
		// create units
		u1 = pm.createUnit("weight", "kg");
		u2 = pm.createUnit("weight", "g");
		u3 = pm.createUnit("size", "m");
		previousUnitTypes = pm.getAllUnitTypes();
	}

	@After
	public void tearDown() throws Exception
	{
		u1.remove();
		u2.remove();
		u3.remove();
	}

	@Test
	public void testFind()
	{
		Collection coll = pm.getUnitsByUnitType("weight");
		assertTrue("did not find all 'weight' units : " + coll,
				coll != null && coll.size() >= 2 && coll.contains(u1) && coll.contains(u2) && !coll.contains(u3));
		coll = pm.getUnitsByUnitType("size");
		assertTrue("did not find all 'size' units : " + coll,
				coll != null && coll.size() >= 1 && coll.contains(u3) && !coll.contains(u1) && !coll.contains(u2));
		coll = pm.getUnitsByUnitType("none");
		assertTrue("did find a unit for non-existant type : " + coll, coll != null && coll.isEmpty());
		coll = pm.getAllUnitTypes();
		assertTrue(
				"did not find all types : " + coll,
				coll != null && coll.size() >= 2 && coll.contains("weight") && coll.contains("size")
						&& coll.containsAll(previousUnitTypes));
	}
}
