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

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;

import java.util.Collection;
import java.util.List;


/**
 * The {@link DeliveryModeModel} DAO.
 * 
 * @spring.bean deliveryModeDao
 */
public interface DeliveryModeDao
{

	/**
	 * Finds the {@link DeliveryModeModel}s with the specified code.
	 * 
	 * @param code
	 *           the delivery mode code
	 * @return the found {@link DeliveryModeModel}s with the specified code, or empty list if not found.
	 */
	List<DeliveryModeModel> findDeliveryModesByCode(final String code);

	/**
	 * Finds all {@link DeliveryModeModel}s.
	 * 
	 * @return a <code>Collection</code> of all {@link DeliveryModeModel}s, or empty list if not found.
	 */
	Collection<DeliveryModeModel> findAllDeliveryModes();

	/**
	 * Finds all {@link DeliveryModeModel}s supported by given {@link PaymentModeModel}.
	 * 
	 * @param paymentMode
	 *           the payment mode
	 * @return a <code>Collection</code> of supported {@link DeliveryModeModel}s, or empty list if not found.
	 */
	Collection<DeliveryModeModel> findDeliveryModeByPaymentMode(PaymentModeModel paymentMode);



}
