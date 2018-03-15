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

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.Collection;
import java.util.List;


/**
 * Data Access Object fetching {@link ZoneDeliveryModeValueModel} objects.
 * 
 * @spring.bean zoneDeliveryModeValueDao
 */
public interface ZoneDeliveryModeValueDao extends GenericDao<ZoneDeliveryModeValueModel>
{

	/**
	 * Finds all {@link ZoneDeliveryModeValueModel}s with matching currency.
	 * 
	 * @param currency
	 *           - currency to match
	 * @return List of {@link ZoneDeliveryModeValueModel}s with the given currency. Returning collection is sorted in
	 *         descending order by creation time.
	 */
	List<ZoneDeliveryModeValueModel> findZoneDeliveryModeValuesByCurrency(final CurrencyModel currency);


	/**
	 * Finds all {@link ZoneDeliveryModeValueModel}s with matching zone.
	 * 
	 * @param zone
	 *           - zone to match
	 * @return List of {@link ZoneDeliveryModeValueModel}s with the given zone. Returning collection is sorted in
	 *         descending order by creation time.
	 */
	List<ZoneDeliveryModeValueModel> findZoneDeliveryModeValuesByZone(final ZoneModel zone);

	/**
	 * Finds specific {@link ZoneDeliveryModeValueModel}s for the given zone, currency, minimum value, and zone delivery
	 * mode.
	 * 
	 * @param zone
	 *           the zone
	 * @param currency
	 *           the currency
	 * @param min
	 *           the minimum value
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 * @return all found {@link ZoneDeliveryModeValueModel}s, or empty list if not found.
	 */
	Collection<ZoneDeliveryModeValueModel> findDeliveryValues(ZoneModel zone, CurrencyModel currency, Double min,
			ZoneDeliveryModeModel zoneDeliveryMode);

}
