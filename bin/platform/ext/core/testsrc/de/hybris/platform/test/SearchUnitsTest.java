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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SearchUnitsTest extends HybrisJUnit4TransactionalTest
{
	private ProductManager productManager;
	private Unit unit1, unit2, unit3;

	@Before
	public void setUp() throws Exception
	{
		productManager = jaloSession.getProductManager();
		// create units
		unit1 = productManager.createUnit("t1", "u1");
		unit2 = productManager.createUnit("t2", "u2");
		unit3 = productManager.createUnit("t2", "u3");
	}

	@After
	public void tearDown() throws Exception
	{
		try
		{
			//jaloSession.getSessionContext().setStagingMethod( Constants.STAGING.PRODUCTIVE );
			unit1.remove();
			unit2.remove();
			unit3.remove();
		}
		finally
		{
			// DOCTODO Document reason, why this block is empty
		}
	}

	@Test
	public void testSearch()
	{
		// test exact match
		Collection res = productManager.searchUnits("u3", "t2");
		assertTrue("exact match: expected [" + unit3 + "] but got " + res, res != null && !res.isEmpty() && res.contains(unit3)
				&& !(res.contains(unit1) || res.contains(unit2)));
		// test match-all
		res = productManager.searchUnits(null, null);
		assertTrue("match-all: expected [" + unit1 + "," + unit2 + "," + unit3 + "] but got " + res, res != null && !res.isEmpty()
				&& res.size() >= 3 && res.contains(unit1) && res.contains(unit2) && res.contains(unit3));
		// test match type
		res = productManager.searchUnits(null, "t2");
		assertTrue("match type: expected [" + unit2 + "," + unit3 + "] but got " + res, res != null && !res.isEmpty()
				&& res.size() >= 2 && res.contains(unit2) && res.contains(unit3) && !res.contains(unit1));
		// test match code
		res = productManager.searchUnits("u1", null);
		assertTrue(
				"match code: expected [" + unit1 + "] but got " + res,
				res != null && !res.isEmpty() && res.size() >= 1 && res.contains(unit1)
						&& !(res.contains(unit2) || res.contains(unit3)));


		// test match nothing
		res = productManager.searchUnits("bad", "idea");
		assertTrue("match code: expected [] but got " + res,
				res != null && res.isEmpty() || !(res.contains(unit1) || res.contains(unit2) || res.contains(unit3)));
	}

	@Test
	public void testGetAllUnits() throws Exception
	{
		final Collection res = productManager.getAllUnits();
		assertTrue("expected [" + unit1 + "," + unit2 + "," + unit3 + "] but got " + res,
				res != null && res.size() >= 3 && res.contains(unit1) && res.contains(unit2) && res.contains(unit3));
	}
}
