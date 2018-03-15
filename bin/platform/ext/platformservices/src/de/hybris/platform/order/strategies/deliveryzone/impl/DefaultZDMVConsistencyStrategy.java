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

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.daos.ZoneDeliveryModeDao;
import de.hybris.platform.order.strategies.deliveryzone.ZDMVConsistencyStrategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link ZDMVConsistencyStrategy}.
 */
public class DefaultZDMVConsistencyStrategy implements ZDMVConsistencyStrategy
{

	private ZoneDeliveryModeDao zoneDeliveryModeDao;

	@Override
	public Map<CountryModel, Set<ZoneModel>> getAmbiguousCountriesForZones(final Set<ZoneModel> zones)
	{
		if (zones == null)
		{
			throw new IllegalArgumentException("zones cannot be null.");
		}
		final List<List<ItemModel>> zonesAndCountries = zoneDeliveryModeDao.findZonesAndCountriesByZones(zones);

		final Map<CountryModel, Set<ZoneModel>> result = new HashMap<CountryModel, Set<ZoneModel>>();
		for (final List<ItemModel> row : zonesAndCountries)
		{
			final ZoneModel zone = (ZoneModel) row.get(0);
			final CountryModel country = (CountryModel) row.get(1);
			Set<ZoneModel> mappedZones = result.get(country);
			if (mappedZones == null)
			{
				mappedZones = new HashSet<ZoneModel>();
				result.put(country, mappedZones);
			}
			mappedZones.add(zone);
		}

		for (final Iterator<Map.Entry<CountryModel, Set<ZoneModel>>> it = result.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry<CountryModel, Set<ZoneModel>> entry = it.next();
			// just keep countries with more than one zone
			if (entry.getValue().size() <= 1)
			{
				it.remove();
			}
		}
		return result.isEmpty() ? Collections.EMPTY_MAP : result;
	}

	@Required
	public void setZoneDeliveryModeDao(final ZoneDeliveryModeDao zoneDeliveryModeDao)
	{
		this.zoneDeliveryModeDao = zoneDeliveryModeDao;
	}

}
