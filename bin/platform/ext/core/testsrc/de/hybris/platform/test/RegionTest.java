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

import static de.hybris.platform.testframework.Assert.assertCollectionElements;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class RegionTest extends HybrisJUnit4TransactionalTest
{
	Country brd;
	Region schleswigHolstein, sachsen, bayern;

	@Before
	public void setUp() throws Exception
	{
		brd = jaloSession.getC2LManager().createCountry("de");
		schleswigHolstein = brd.addNewRegion("sh");
		sachsen = brd.addNewRegion("sa");
		bayern = brd.addNewRegion("by");
	}

	@After
	public void tearDown() throws Exception
	{
		bayern.remove();
		sachsen.remove();
		schleswigHolstein.remove();
		brd.remove();
	}

	@Test
	public void testGetAllRegions()
	{
		final Collection c = jaloSession.getC2LManager().getAllRegions();
		assertTrue(c.contains(bayern));
		assertTrue(c.contains(sachsen));
		assertTrue(c.contains(schleswigHolstein));
		assertFalse(c.contains(brd));
	}

	@Test
	public void testRegionCodes() throws ConsistencyCheckException, JaloItemNotFoundException
	{
		final Country ch = jaloSession.getC2LManager().createCountry("ch");
		assertNotNull(ch);
		final Region schaffhausen = ch.addNewRegion("sh");
		assertNotNull(schaffhausen);
		assertCollectionElements(jaloSession.getC2LManager().getRegionsByCode("sh"), schleswigHolstein, schaffhausen);
		try
		{
			brd.addNewRegion("sh");
			fail("two regions with code sh");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine...
		}
		assertEquals(schleswigHolstein, brd.getRegionByCode("sh"));
		assertEquals(schaffhausen, ch.getRegionByCode("sh"));
	}



}
