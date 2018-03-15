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
package de.hybris.platform.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.deliveryzone.constants.ZoneDeliveryModeConstants;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.ZoneDeliveryModeService;
import de.hybris.platform.order.daos.ZoneDeliveryModeDao;
import de.hybris.platform.order.daos.ZoneDeliveryModeValueDao;
import de.hybris.platform.order.exceptions.DeliveryModeInterceptorException;
import de.hybris.platform.order.strategies.deliveryzone.ZDMVConsistencyStrategy;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class DefaultZoneDeliveryModeService extends DefaultDeliveryModeService implements ZoneDeliveryModeService
{
	private ZoneDeliveryModeValueDao zoneDeliveryModeValueDao;
	private ZDMVConsistencyStrategy zdmvConsistencyStrategy;
	private ZoneDeliveryModeDao zoneDeliveryModeDao;



	@Override
	public ZoneModel getZoneForCode(final String code)
	{
		validateParameterNotNullStandardMessage("code", code);
		final List<ZoneModel> zones = zoneDeliveryModeDao.findZonesByCode(code);
		if (zones.isEmpty())
		{
			throw new UnknownIdentifierException("Zone with code [" + code + "] not found!");
		}
		else if (zones.size() > 1)
		{
			throw new AmbiguousIdentifierException("Zone code [" + code + "] is not unique, " + zones.size() + " zones found!");
		}
		return zones.get(0);
	}

	@Override
	public Collection<ZoneModel> getAllZones()
	{
		final Collection<ZoneModel> zones = zoneDeliveryModeDao.findAllZones();
		return zones;
	}

	@Override
	public Collection<ZoneModel> getZonesForZoneDeliveryMode(final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		validateParameterNotNullStandardMessage("zoneDeliveryMode", zoneDeliveryMode);
		final Collection<ZoneModel> zones = zoneDeliveryModeDao.findZonesByZoneDeliveryMode(zoneDeliveryMode);
		return zones;
	}

	@Override
	public Collection<CurrencyModel> getCurrencies(final ZoneModel zone, final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		validateParameterNotNullStandardMessage("zone", zone);
		validateParameterNotNullStandardMessage("zoneDeliveryMode", zoneDeliveryMode);
		final Collection<CurrencyModel> currencies = zoneDeliveryModeDao.findCurrencies(zone, zoneDeliveryMode);
		return currencies;
	}

	@Override
	public boolean isZoneAllowed(final ZoneModel zone, final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		validateParameterNotNullStandardMessage("zone", zone);
		validateParameterNotNullStandardMessage("zoneDeliveryMode", zoneDeliveryMode);
		final Collection<ZoneModel> zones = getZonesForZoneDeliveryMode(zoneDeliveryMode);
		if (zones.contains(zone))
		{
			return true;
		}

		final Set<ZoneModel> newZones = new HashSet<ZoneModel>(zones);
		newZones.add(zone);
		final Map<CountryModel, Set<ZoneModel>> countriesAndZones = zdmvConsistencyStrategy.getAmbiguousCountriesForZones(newZones);
		final boolean allowed = countriesAndZones.isEmpty();
		return allowed;
	}

	@Override
	public Map<Double, Double> getDeliveryValues(final CurrencyModel currency, final ZoneModel zone,
			final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		validateParameterNotNullStandardMessage("currency", currency);
		validateParameterNotNullStandardMessage("zone", zone);
		validateParameterNotNullStandardMessage("zoneDeliveryMode", zoneDeliveryMode);
		final Map<Double, Double> deliveryValues = zoneDeliveryModeDao.findDeliveryValues(currency, zone, zoneDeliveryMode);
		return deliveryValues;
	}

	@Override
	public ZoneDeliveryModeValueModel getDeliveryValue(final ZoneModel zone, final CurrencyModel currency, final Double min,
			final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		validateParameterNotNullStandardMessage("currency", currency);
		validateParameterNotNullStandardMessage("zone", zone);
		validateParameterNotNullStandardMessage("min", min);
		validateParameterNotNullStandardMessage("zoneDeliveryMode", zoneDeliveryMode);
		final Collection<ZoneDeliveryModeValueModel> zoneDeliveryModeValues = zoneDeliveryModeValueDao.findDeliveryValues(zone,
				currency, min, zoneDeliveryMode);
		if (zoneDeliveryModeValues.isEmpty())
		{
			throw new UnknownIdentifierException("ZoneDeliveryModeValue in the zone [ " + zone.getCode() + "], with currency ["
					+ currency.getIsocode() + "], minimum value [" + min + "], and zoneDeliveryMode [" + zoneDeliveryMode.getCode()
					+ "] not found!");
		}
		else if (zoneDeliveryModeValues.size() > 1)
		{
			throw new AmbiguousIdentifierException("ZoneDeliveryModeValue in the zone [ " + zone.getCode() + "], with currency ["
					+ currency.getIsocode() + "], minimum value [" + min + "], and zoneDeliveryMode [" + zoneDeliveryMode.getCode()
					+ "]  is not unique, " + zoneDeliveryModeValues.size() + " values found!");
		}
		return zoneDeliveryModeValues.iterator().next();
	}

	@Override
	public ZoneDeliveryModeValueModel setDeliveryCost(final CurrencyModel currency, final Double min, final Double value,
			final ZoneModel zone, final ZoneDeliveryModeModel zoneDeliveryMode) throws DeliveryModeInterceptorException
	{
		validateParameterNotNullStandardMessage("currency", currency);
		validateParameterNotNullStandardMessage("zone", zone);
		validateParameterNotNullStandardMessage("min", min);
		validateParameterNotNullStandardMessage("value", value);
		validateParameterNotNullStandardMessage("zoneDeliveryMode", zoneDeliveryMode);
		ZoneDeliveryModeValueModel zoneDeliveryModeValue;
		try
		{
			zoneDeliveryModeValue = getDeliveryValue(zone, currency, min, zoneDeliveryMode);
			zoneDeliveryModeValue.setValue(value);
		}
		catch (final UnknownIdentifierException ue)
		{
			zoneDeliveryModeValue = getModelService().create(ZoneDeliveryModeValueModel.class);
			zoneDeliveryModeValue.setCurrency(currency);
			zoneDeliveryModeValue.setMinimum(min);
			zoneDeliveryModeValue.setValue(value);
			zoneDeliveryModeValue.setZone(zone);
			zoneDeliveryModeValue.setDeliveryMode(zoneDeliveryMode);
		}
		return zoneDeliveryModeValue;
	}

	@Override
	public void setUsingPrice(final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		validateParameterNotNullStandardMessage("zoneDeliveryMode", zoneDeliveryMode);
		zoneDeliveryMode.setPropertyName(ZoneDeliveryModeConstants.PRICE_PROPERTY_NAME);
	}

	@Override
	public boolean isUsingPrice(final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		validateParameterNotNullStandardMessage("zoneDeliveryMode", zoneDeliveryMode);
		final boolean usingPrice = ZoneDeliveryModeConstants.PRICE_PROPERTY_NAME.equals(zoneDeliveryMode.getPropertyName());
		return usingPrice;
	}

	@Required
	public void setZoneDeliveryModeValueDao(final ZoneDeliveryModeValueDao zoneDeliveryModeValueDao)
	{
		this.zoneDeliveryModeValueDao = zoneDeliveryModeValueDao;
	}

	@Required
	public void setZdmvConsistencyStrategy(final ZDMVConsistencyStrategy zdmvConsistencyStrategy)
	{
		this.zdmvConsistencyStrategy = zdmvConsistencyStrategy;
	}

	@Required
	public void setZoneDeliveryModeDao(final ZoneDeliveryModeDao zoneDeliveryModeDao)
	{
		this.zoneDeliveryModeDao = zoneDeliveryModeDao;
	}
}
