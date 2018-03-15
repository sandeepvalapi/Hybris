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
package de.hybris.platform.order.daos.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.daos.ZoneDeliveryModeDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Default implementation of the {@link ZoneDeliveryModeDao}.
 */
public class DefaultZoneDeliveryModeDao extends AbstractItemDao implements ZoneDeliveryModeDao
{

	@Override
	public List<ZoneModel> findZonesByCode(final String code)
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(ZoneModel.PK);
		query.append("} FROM {").append(ZoneModel._TYPECODE);
		query.append("} WHERE {").append(ZoneModel.CODE).append("}=?").append(ZoneModel.CODE);
		query.append(" ORDER BY {").append(ZoneModel.CREATIONTIME).append("} ASC , {").append(ZoneModel.PK).append("} ASC");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(ZoneModel.CODE, code);
		final List<ZoneModel> zones = doSearch(query.toString(), params, null);
		return zones;
	}

	@Override
	public Collection<ZoneModel> findAllZones()
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(ZoneModel.PK);
		query.append("} FROM {").append(ZoneModel._TYPECODE);
		query.append("} ORDER BY {").append(ZoneModel.CREATIONTIME).append("} DESC , {").append(ZoneModel.PK).append("} ASC");
		final List<ZoneModel> zones = doSearch(query.toString(), null, null);
		return zones;
	}

	@Override
	public Collection<ZoneModel> findZonesByZoneDeliveryMode(final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		final StringBuilder query = new StringBuilder("SELECT DISTINCT {").append(ZoneDeliveryModeValueModel.ZONE);
		query.append("} FROM {").append(ZoneDeliveryModeValueModel._TYPECODE).append("} WHERE {");
		query.append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}=?").append(ZoneDeliveryModeValueModel.DELIVERYMODE);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(ZoneDeliveryModeValueModel.DELIVERYMODE, zoneDeliveryMode);
		final List<ZoneModel> zones = doSearch(query.toString(), params, null);
		return zones;
	}

	@Override
	public List<List<ItemModel>> findZonesAndCountriesByZones(final Set<ZoneModel> zones)
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(LinkModel.SOURCE).append("}, {");
		query.append(LinkModel.TARGET).append("} FROM {").append(CountryModel._ZONECOUNTRYRELATION);
		query.append("} WHERE {").append(LinkModel.SOURCE).append("} IN (?").append(CountryModel.ZONES).append(")");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(CountryModel.ZONES, zones);
		final List<Class> resultClasses = new ArrayList<Class>();
		resultClasses.add(ZoneModel.class);
		resultClasses.add(CountryModel.class);
		final List<List<ItemModel>> zonesAndCountries = doSearch(query.toString(), params, resultClasses);
		return zonesAndCountries;
	}

	@Override
	public Collection<CurrencyModel> findCurrencies(final ZoneModel zone, final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		final StringBuilder query = new StringBuilder("SELECT DISTINCT {").append(ZoneDeliveryModeValueModel.CURRENCY);
		query.append("} FROM {").append(ZoneDeliveryModeValueModel._TYPECODE);
		query.append("} WHERE {").append(ZoneDeliveryModeValueModel.DELIVERYMODE);
		query.append("}=?").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append(" AND {");
		query.append(ZoneDeliveryModeValueModel.ZONE).append("}=?").append(ZoneDeliveryModeValueModel.ZONE);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(ZoneDeliveryModeValueModel.ZONE, zone);
		params.put(ZoneDeliveryModeValueModel.DELIVERYMODE, zoneDeliveryMode);
		final List<CurrencyModel> currencies = doSearch(query.toString(), params, null);
		return currencies;
	}

	@Override
	public Map<Double, Double> findDeliveryValues(final CurrencyModel currency, final ZoneModel zone,
			final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(ZoneDeliveryModeValueModel.MINIMUM);
		query.append("}, {").append(ZoneDeliveryModeValueModel.VALUE).append("} FROM {");
		query.append(ZoneDeliveryModeValueModel._TYPECODE).append("} WHERE {");
		query.append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}=?").append(ZoneDeliveryModeValueModel.DELIVERYMODE);
		query.append(" AND {").append(ZoneDeliveryModeValueModel.CURRENCY).append("}=?");
		query.append(ZoneDeliveryModeValueModel.CURRENCY).append(" AND {").append(ZoneDeliveryModeValueModel.ZONE).append("}=?");
		query.append(ZoneDeliveryModeValueModel.ZONE);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(ZoneDeliveryModeValueModel.CURRENCY, currency);
		params.put(ZoneDeliveryModeValueModel.ZONE, zone);
		params.put(ZoneDeliveryModeValueModel.DELIVERYMODE, zoneDeliveryMode);
		final List<Class> resultClasses = new ArrayList<Class>();
		resultClasses.add(Double.class);
		resultClasses.add(Double.class);
		//rows consists of threshold value and delivery cost
		final List<List<Double>> rows = doSearch(query.toString(), params, resultClasses);
		if (rows.isEmpty())
		{
			return Collections.EMPTY_MAP;
		}
		else
		{
			final Map<Double, Double> deliveryValues = new HashMap<Double, Double>(rows.size());
			for (final List<Double> row : rows)
			{
				deliveryValues.put(row.get(0), row.get(1));
			}
			return deliveryValues;
		}
	}

	private <T> List<T> doSearch(final String query, final Map<String, Object> params, final List<Class> resultClasses)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}
		if (resultClasses != null)
		{
			fQuery.setResultClassList(resultClasses);
		}
		final SearchResult<T> result = getFlexibleSearchService().search(fQuery);
		final List<T> elements = result.getResult();
		return elements;
	}

}
