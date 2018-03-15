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
package de.hybris.platform.order.strategies.deliveryzone.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.daos.ZoneDeliveryModeDao;
import de.hybris.platform.order.daos.impl.DefaultZoneDeliveryModeDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * Test the {@link DefaultZDMVConsistencyStrategy}.
 */
@UnitTest
public class DefaultZDMVConsistencyStrategyTest
{

	private DefaultZDMVConsistencyStrategy zdmvConsistencyStrategy;
	private ZoneDeliveryModeDao zoneDeliveryModeDao;

	private CountryModel deCountry;
	private CountryModel enCountry;
	private CountryModel frCountry;
	private ZoneModel deZone;
	private ZoneModel europeZone;
	private ZoneModel duplicateZone;

	/**
	 * Creates the core data, and necessary data for delivery modes.
	 */
	@Before
	public void setUp() throws Exception
	{
		zoneDeliveryModeDao = Mockito.mock(DefaultZoneDeliveryModeDao.class);
		zdmvConsistencyStrategy = new DefaultZDMVConsistencyStrategy();
		zdmvConsistencyStrategy.setZoneDeliveryModeDao(zoneDeliveryModeDao);

		setUpData();
	}

	private void setUpData()
	{
		deCountry = createCountry("de");
		enCountry = createCountry("en");
		frCountry = createCountry("fr");
		deZone = createZone("de");
		europeZone = createZone("europe");

		//relations between zones and countries
		deCountry.setZones(Collections.singleton(deZone));
		deZone.setCountries(Collections.singleton(deCountry));
		enCountry.setZones(Collections.singleton(europeZone));
		frCountry.setZones(Collections.singleton(europeZone));
		final Set<CountryModel> countries = new HashSet<CountryModel>();
		countries.add(enCountry);
		countries.add(frCountry);
		europeZone.setCountries(countries);

		//duplicate zones and countries
		duplicateZone = createZone("duplicate");
		countries.add(deCountry);
		duplicateZone.setCountries(countries);
		deCountry.setZones(getZones(deZone, duplicateZone));
		final Set<ZoneModel> zones = getZones(europeZone, duplicateZone);
		frCountry.setZones(zones);
		enCountry.setZones(zones);
	}

	private CountryModel createCountry(final String isoCode)
	{
		final CountryModel country = new CountryModel();
		country.setIsocode(isoCode);
		return country;
	}

	private ZoneModel createZone(final String code)
	{
		final ZoneModel zone = new ZoneModel();
		zone.setCode(code);
		return zone;
	}

	private Set<ZoneModel> getZones(final ZoneModel... zones)
	{
		final Set<ZoneModel> result = new HashSet<ZoneModel>();
		for (final ZoneModel zone : zones)
		{
			result.add(zone);
		}
		return result;
	}

	@Test
	public void testGetAmbiguousCountriesForZones()
	{
		try
		{
			zdmvConsistencyStrategy.getAmbiguousCountriesForZones(null);
			fail("should throw the IllegalArgumentException");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

		final Set<ZoneModel> zones = new HashSet<ZoneModel>();
		zones.add(deZone);
		zones.add(europeZone);
		final List<List<ItemModel>> zonesAndCountries = getDistinctZonesAndCountries();
		Mockito.when(zoneDeliveryModeDao.findZonesAndCountriesByZones(zones)).thenReturn(zonesAndCountries);
		Map<CountryModel, Set<ZoneModel>> countriesAndZones = zdmvConsistencyStrategy.getAmbiguousCountriesForZones(zones);
		assertEquals("should be empty", 0, countriesAndZones.size());

		zones.add(duplicateZone);
		zonesAndCountries.addAll(getDuplicateZonesAndCountries());
		countriesAndZones = zdmvConsistencyStrategy.getAmbiguousCountriesForZones(zones);
		assertEquals("3 duplicated zones and countries should be found", 3, countriesAndZones.size());
	}

	private List<ItemModel> getZoneAndCountry(final ZoneModel zone, final CountryModel country)
	{
		final List<ItemModel> row = new ArrayList<ItemModel>();
		row.add(zone);
		row.add(country);
		return row;
	}

	private List<List<ItemModel>> getDistinctZonesAndCountries()
	{
		final List<List<ItemModel>> result = new ArrayList<List<ItemModel>>();
		result.add(getZoneAndCountry(deZone, deCountry));
		result.add(getZoneAndCountry(europeZone, frCountry));
		result.add(getZoneAndCountry(europeZone, enCountry));
		return result;
	}

	private List<List<ItemModel>> getDuplicateZonesAndCountries()
	{
		final List<List<ItemModel>> result = new ArrayList<List<ItemModel>>();
		result.add(getZoneAndCountry(duplicateZone, deCountry));
		result.add(getZoneAndCountry(duplicateZone, frCountry));
		result.add(getZoneAndCountry(duplicateZone, enCountry));
		return result;
	}

}
