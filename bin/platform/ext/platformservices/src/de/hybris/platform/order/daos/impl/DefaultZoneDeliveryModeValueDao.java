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

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.daos.ZoneDeliveryModeValueDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Data Access Object for {@link ZoneDeliveryModeValueModel}
 */
public class DefaultZoneDeliveryModeValueDao extends DefaultGenericDao<ZoneDeliveryModeValueModel> implements
		ZoneDeliveryModeValueDao
{

	public DefaultZoneDeliveryModeValueDao()
	{
		super(ZoneDeliveryModeValueModel._TYPECODE);
	}

	@Override
	public List<ZoneDeliveryModeValueModel> findZoneDeliveryModeValuesByCurrency(final CurrencyModel currency)
	{
		final SortParameters sortParameters = SortParameters.singletonDescending(ZoneDeliveryModeValueModel.CREATIONTIME);
		final Map<String, Object> params = Collections.<String, Object> singletonMap(ZoneDeliveryModeValueModel.CURRENCY, currency);
		return find(params, sortParameters);
	}

	@Override
	public List<ZoneDeliveryModeValueModel> findZoneDeliveryModeValuesByZone(final ZoneModel zone)
	{
		final SortParameters sortParameters = SortParameters.singletonDescending(ZoneDeliveryModeValueModel.CREATIONTIME);
		final Map<String, Object> params = Collections.<String, Object> singletonMap(ZoneDeliveryModeValueModel.ZONE, zone);
		return find(params, sortParameters);
	}


	@Override
	public Collection<ZoneDeliveryModeValueModel> findDeliveryValues(final ZoneModel zone, final CurrencyModel currency,
			final Double min, final ZoneDeliveryModeModel zoneDeliveryMode)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(ZoneDeliveryModeValueModel.ZONE, zone);
		params.put(ZoneDeliveryModeValueModel.CURRENCY, currency);
		params.put(ZoneDeliveryModeValueModel.MINIMUM, min);
		params.put(ZoneDeliveryModeValueModel.DELIVERYMODE, zoneDeliveryMode);
		final List<ZoneDeliveryModeValueModel> zoneDeliveryModeValues = find(params);
		return zoneDeliveryModeValues;
	}

}
