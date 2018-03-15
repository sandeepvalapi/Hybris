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
package de.hybris.platform.product;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.daos.UnitDao;
import de.hybris.platform.product.impl.DefaultUnitService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class UnitServiceTest
{
	private static final String UNIT_CODE = "1 kg";

	private DefaultUnitService unitService;

	@Mock
	private UnitDao mockUnitDao;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		unitService = new DefaultUnitService();
		unitService.setUnitDao(mockUnitDao);
	}

	@Test
	public void testReturnsAllUnits()
	{
		final Set<UnitModel> units = createUnitsSet();
		Mockito.when(mockUnitDao.findAllUnits()).thenReturn(units);

		//when
		final Set<UnitModel> allUnits = unitService.getAllUnits();

		//then
		Assert.assertNotNull(allUnits);
		Assert.assertEquals(allUnits.size(), 2);
		assertThat(allUnits).isSameAs(units);
	}

	@Test
	public void testReturnsUnitsForUnitType()
	{
		final Set<UnitModel> units = createUnitsSet();
		Mockito.when(mockUnitDao.findUnitsByUnitType("weight")).thenReturn(units);

		//when
		final Set<UnitModel> allUnits = unitService.getUnitsForUnitType("weight");

		//then
		Assert.assertNotNull(allUnits);
		Assert.assertEquals(allUnits.size(), 2);
	}

	@Test
	public void testReturnsAllUnitTypes()
	{
		final Set<String> units = new HashSet<String>();
		units.add("weight");
		units.add("size");
		units.add("capacity");
		Mockito.when(mockUnitDao.findAllUnitTypes()).thenReturn(units);

		//when
		final Set<String> allUnits = unitService.getAllUnitTypes();

		//then
		Assert.assertNotNull(allUnits);
		Assert.assertEquals(allUnits.size(), 3);
	}

	@Test
	public void testReturnsUnitForCode()
	{
		//given
		final UnitModel mockUnitModel = Mockito.mock(UnitModel.class);
		Mockito.when(mockUnitDao.findUnitsByCode(UNIT_CODE)).thenReturn(Collections.singleton(mockUnitModel));
		//when
		final UnitModel unit = unitService.getUnitForCode(UNIT_CODE);

		//then
		Assert.assertNotNull(unit);
	}

	@Test
	public void testThrowsIllegalArgumentExceptionWhenCodeIsNull()
	{
		try
		{
			unitService.getUnitForCode(null);
			Assert.fail("Should throw IllegalArgumentException because code is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}
		catch (final Exception e)
		{
			Assert.fail("Should throw IllegalArgumentException but got " + e);
		}
	}

	@Test
	public void testThrowsUnknownIdentifierExceptionWhenUnitNotFound()
	{
		//given
		Mockito.when(mockUnitDao.findUnitsByCode("Unknown")).thenReturn(Collections.EMPTY_SET);
		//when
		try
		{
			unitService.getUnitForCode("Unknown");
			Assert.fail("Should throw UnknownIdentifierException because unit not found");

		}
		catch (final UnknownIdentifierException ex)
		{
			//OK
		}
	}

	@Test
	public void testThrowsAmbiguousIdentifierExceptionWhenMoreThanOneUnitFoundForGivenCode()
	{
		//given
		final UnitModel mockUnit = Mockito.mock(UnitModel.class);
		final UnitModel mockUnit2 = Mockito.mock(UnitModel.class);
		Mockito.when(mockUnitDao.findUnitsByCode(UNIT_CODE)).thenReturn(new HashSet(Arrays.asList(mockUnit, mockUnit2)));
		//when
		try
		{
			unitService.getUnitForCode(UNIT_CODE);
			Assert.fail("Should throw AmbiguousIdentifierException when more than one unit found for a given code");
		}
		catch (final AmbiguousIdentifierException ex)
		{
			//OK
		}
	}

	private Set<UnitModel> createUnitsSet()
	{
		final UnitModel firstMockUnit = Mockito.mock(UnitModel.class);
		final UnitModel secondMockUnit = Mockito.mock(UnitModel.class);
		final Set<UnitModel> units = new HashSet<UnitModel>();
		units.add(firstMockUnit);
		units.add(secondMockUnit);
		return units;
	}
}
