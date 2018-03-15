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

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of the {@link DeliveryModeDao}.
 */
public class DefaultDeliveryModeDao extends AbstractItemDao implements DeliveryModeDao
{

	private final String LIKE_CHARACTER = "%";

	@Override
	public List<DeliveryModeModel> findDeliveryModesByCode(final String code)
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(DeliveryModeModel.PK);
		query.append("} FROM {").append(DeliveryModeModel._TYPECODE);
		query.append("} WHERE {").append(DeliveryModeModel.CODE).append("}=?").append(DeliveryModeModel.CODE);
		query.append(" ORDER BY {").append(DeliveryModeModel.CREATIONTIME).append("} DESC");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(DeliveryModeModel.CODE, code);
		final List<DeliveryModeModel> deliveryModes = doSearch(query.toString(), params, null);
		return deliveryModes;
	}

	@Override
	public Collection<DeliveryModeModel> findAllDeliveryModes()
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(DeliveryModeModel.PK);
		query.append("} FROM {").append(DeliveryModeModel._TYPECODE);
		query.append("} ORDER BY {").append(DeliveryModeModel.CODE).append("} ASC");
		final List<DeliveryModeModel> deliveryModes = doSearch(query.toString(), null, null);
		return deliveryModes;
	}

	@Override
	public Collection<DeliveryModeModel> findDeliveryModeByPaymentMode(final PaymentModeModel paymentMode)
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(DeliveryModeModel.PK);
		query.append("} FROM {").append(DeliveryModeModel._TYPECODE);
		query.append("} WHERE {").append(DeliveryMode.SUPPORTEDPAYMENTMODESINTERNAL);
		query.append("} LIKE ?").append(DeliveryMode.SUPPORTEDPAYMENTMODESINTERNAL);
		query.append(" ORDER BY {").append(DeliveryModeModel.CODE).append("} ASC");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(DeliveryMode.SUPPORTEDPAYMENTMODESINTERNAL, LIKE_CHARACTER + paymentMode.getPk().toString() + LIKE_CHARACTER);
		final List<DeliveryModeModel> deliveryModes = doSearch(query.toString(), params, null);
		return deliveryModes;
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
