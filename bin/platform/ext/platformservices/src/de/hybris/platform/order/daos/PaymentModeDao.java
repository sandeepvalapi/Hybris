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

import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;


/**
 * Data Access Object oriented on PaymentMode
 */
public interface PaymentModeDao extends GenericDao<PaymentModeModel>
{
	/**
	 * Search for PaymentMode by code
	 * 
	 * @param code
	 *           the code
	 */
	List<PaymentModeModel> findPaymentModeForCode(final String code);

	/**
	 * Search for all PaymentModes
	 */
	List<PaymentModeModel> findAllPaymentModes();



}
