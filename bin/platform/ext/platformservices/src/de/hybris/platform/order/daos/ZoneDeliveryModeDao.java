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
package de.hybris.platform.order.daos;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * The {@link ZoneDeliveryModeModel} DAO.
 * 
 * @spring.bean zoneDeliveryModeDao
 */
public interface ZoneDeliveryModeDao
{
	/**
	 * Finds the {@link ZoneModel}s with the specified code.
	 * 
	 * @param code
	 *           the zone code
	 * @return the found {@link ZoneModel}s with the specified code, or empty list if not found.
	 */
	List<ZoneModel> findZonesByCode(final String code);

	/**
	 * Finds all {@link ZoneModel}s.
	 * 
	 * @return a <code>Collection</code> of all {@link ZoneModel}s, or empty list if not found.
	 */
	Collection<ZoneModel> findAllZones();

	/**
	 * Finds all zones for which price values are defined in the delivery mode.
	 * 
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 * @return a <code>Collection</code> of all {@link ZoneModel}s for the delivery mode, or empty list if not found.
	 */
	Collection<ZoneModel> findZonesByZoneDeliveryMode(ZoneDeliveryModeModel zoneDeliveryMode);

	/**
	 * Finds all countries of the zones.
	 * 
	 * @param zones
	 *           the zones
	 * @return a <code>List</code> contains a <code>List</code> of {@link ItemModel}s, which consists of exactly one
	 *         {@link ZoneModel} and one {@link CountryModel}, or empty list if not found.
	 */
	List<List<ItemModel>> findZonesAndCountriesByZones(Set<ZoneModel> zones);

	/**
	 * Finds all currencies for which values are defined in the zone and in the delivery mode.
	 * 
	 * @param zone
	 *           the zone
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 * @return all found {@link CurrencyModel}s, or empty list if not found.
	 */
	Collection<CurrencyModel> findCurrencies(final ZoneModel zone, final ZoneDeliveryModeModel zoneDeliveryMode);

	/**
	 * Finds all delivery cost values for the specific currency and in the zone and with the delivery mode.
	 * 
	 * @param currency
	 *           the currency
	 * @param zone
	 *           the zone
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 * @return All delivery cost values consisting of minimum threshold value and cost value.
	 */
	Map<Double, Double> findDeliveryValues(CurrencyModel currency, ZoneModel zone, ZoneDeliveryModeModel zoneDeliveryMode);
}
