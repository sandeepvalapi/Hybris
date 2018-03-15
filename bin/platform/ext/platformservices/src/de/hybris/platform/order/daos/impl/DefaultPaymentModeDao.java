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

import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.order.daos.PaymentModeDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import de.hybris.platform.servicelayer.internal.dao.SortParameters.SortOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class DefaultPaymentModeDao extends DefaultGenericDao<PaymentModeModel> implements PaymentModeDao
{
	public DefaultPaymentModeDao()
	{
		super(PaymentModeModel._TYPECODE);
	}


	private DefaultPaymentModeDao(final String typecode)
	{
		super(typecode);

	}


	@Override
	public List<PaymentModeModel> findPaymentModeForCode(final String code)
	{
		final SortParameters sortParameters = new SortParameters();
		sortParameters.addSortParameter(PaymentModeModel.CREATIONTIME, SortOrder.DESCENDING);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(PaymentModeModel.CODE, code);

		return find(params, sortParameters);
	}


	@Override
	public List<PaymentModeModel> findAllPaymentModes()
	{
		final SortParameters sortParameters = new SortParameters();
		sortParameters.addSortParameter(PaymentModeModel.CODE, SortOrder.ASCENDING);

		return find(sortParameters);
	}




}
