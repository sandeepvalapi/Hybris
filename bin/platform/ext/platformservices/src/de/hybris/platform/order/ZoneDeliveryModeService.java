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
package de.hybris.platform.order;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.exceptions.DeliveryModeInterceptorException;

import java.util.Collection;
import java.util.Map;


/**
 * Service around the {@link ZoneDeliveryModeModel}. You may find more info about delivery modes <a
 * href="https://wiki.hybris.com/display/release4/Payment+Transaction+and+Delivery+Mode+Handling">here</a>. The service
 * allows finding {@link ZoneDeliveryModeModel} by code. Service returns also specific delivery costs for specified
 * zone, currency and delivery mode.
 * 
 * @spring.bean zoneDeliveryModeService
 */
public interface ZoneDeliveryModeService extends DeliveryModeService
{
	/**
	 * Gets the {@link ZoneModel} with the specified code.
	 * 
	 * @param code
	 *           the zone code
	 * @return the found {@link ZoneModel} with the specified code
	 */
	ZoneModel getZoneForCode(String code);

	/**
	 * Gets all {@link ZoneModel}s.
	 * 
	 * @return a <code>Collection</code> of all {@link ZoneModel}s
	 */
	Collection<ZoneModel> getAllZones();

	/**
	 * Gets all zones for which price values are defined in the delivery mode.
	 * 
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 * @return all {@link ZoneModel}s for the delivery mode
	 */
	Collection<ZoneModel> getZonesForZoneDeliveryMode(ZoneDeliveryModeModel zoneDeliveryMode);

	/**
	 * Gets all currencies for which values are defined in the zone and in the delivery mode.
	 * 
	 * @param zone
	 *           the zone
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 * @return all found {@link CurrencyModel}s
	 */
	Collection<CurrencyModel> getCurrencies(ZoneModel zone, ZoneDeliveryModeModel zoneDeliveryMode);

	/**
	 * Checks whether the zone is allowed to be used for adding new values to the delivery mode. This is necessary
	 * because two zones may share countries so the delivery mode can no longer calculate distinct prices for a specific
	 * country.
	 * 
	 * @param zone
	 *           the zone to be checked
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 * 
	 * @return true if the zone is allowed for the delivery mode, false otherwise
	 */
	boolean isZoneAllowed(ZoneModel zone, ZoneDeliveryModeModel zoneDeliveryMode);

	/**
	 * Gets all delivery cost values for the currency in the zone and with the specific delivery mode.
	 * 
	 * @param currency
	 *           the currency
	 * @param zone
	 *           the zone
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 * @return All delivery cost values consisting of minimum threshold value and cost value.
	 */
	Map<Double, Double> getDeliveryValues(CurrencyModel currency, ZoneModel zone, ZoneDeliveryModeModel zoneDeliveryMode);

	/**
	 * Gets specific {@link ZoneDeliveryModeValueModel} for the given zone, currency, minimum value, and zone delivery
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
	 * @return found {@link ZoneDeliveryModeValueModel}
	 */
	ZoneDeliveryModeValueModel getDeliveryValue(ZoneModel zone, CurrencyModel currency, Double min,
			ZoneDeliveryModeModel zoneDeliveryMode);

	/**
	 * Sets the delivery cost for the given zone, currency, minimum value, and zone delivery mode. Creates a new
	 * {@link ZoneDeliveryModeValueModel} if no {@link ZoneDeliveryModeValueModel} can be found.
	 * 
	 * @param currency
	 *           the currency
	 * @param min
	 *           the minimum value
	 * @param value
	 *           the delivery cost value
	 * @param zone
	 *           the zone
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 * @return changed {@link ZoneDeliveryModeValueModel} with new delivery cost value or the created
	 *         {@link ZoneDeliveryModeValueModel}
	 * @throws DeliveryModeInterceptorException
	 *            if there is ambiguous zone for the {@link ZoneDeliveryModeModel}
	 */
	ZoneDeliveryModeValueModel setDeliveryCost(CurrencyModel currency, Double min, Double value, ZoneModel zone,
			ZoneDeliveryModeModel zoneDeliveryMode) throws DeliveryModeInterceptorException;

	/**
	 * Sets the special price property name for the {@link ZoneDeliveryModeModel} conveniently. If the property is set,
	 * the {@link ZoneDeliveryModeModel} would use order's subtotal as calculation base.
	 * 
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 */
	void setUsingPrice(ZoneDeliveryModeModel zoneDeliveryMode);

	/**
	 * Checks whether the {@link ZoneDeliveryModeModel} uses the order subtotal as calculation base.
	 * 
	 * @param zoneDeliveryMode
	 *           the zone delivery mode
	 */
	boolean isUsingPrice(ZoneDeliveryModeModel zoneDeliveryMode);

}
