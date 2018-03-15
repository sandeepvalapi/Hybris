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
package de.hybris.platform.product.daos;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests UnitDao
 */
@IntegrationTest
public class UnitDaoTest extends ServicelayerTransactionalTest
{
	@Resource
	private UnitDao unitDao;

	@Before
	public void setUp() throws Exception
	{
		//given
		createCoreData();
		createDefaultCatalog();
	}

	@Test
	public void testFindAllUnits() throws Exception
	{
		//when
		final Set<UnitModel> allUnits = unitDao.findAllUnits();

		//then
		assertNotNull(allUnits);
		assertEquals(allUnits.size(), 10);
	}

	@Test
	public void testFindAllUnitTypes() throws Exception
	{
		//when
		final Set<String> allUnits = unitDao.findAllUnitTypes();

		//then
		assertNotNull(allUnits);
		assertEquals(allUnits.size(), 3);
		assertThat(allUnits, hasItems("pieces", "weight", "volume"));
	}

	@Test
	public void testFindUnitByCode() throws Exception
	{
		//when
		final Set<UnitModel> units = unitDao.findUnitsByCode("g");

		//then
		assertNotNull(units);
		assertEquals(units.size(), 1);
		final UnitModel unit = units.iterator().next();
		assertEquals(unit.getCode(), "g");
		assertEquals(unit.getUnitType(), "weight");
	}

	@Test
	public void testFindUnitByCodeWhenCodeIsNull() throws Exception
	{
		//when
		try
		{
			unitDao.findUnitsByCode(null);
			Assert.fail("Should throw IllegalArgumentException when code is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}

	}

	@Test
	public void testFindUnitByCodeWhenNoResults() throws Exception
	{
		//when
		final Set<UnitModel> units = unitDao.findUnitsByCode("unknown");

		//then
		Assert.assertTrue(units.isEmpty());
	}

	@Test
	public void testFindUnitsByUnitType() throws Exception
	{
		//when
		final Set<UnitModel> allUnits = unitDao.findUnitsByUnitType("weight");

		//then
		assertNotNull(allUnits);
		assertEquals(allUnits.size(), 4);
	}
}
