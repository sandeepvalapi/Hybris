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

import de.hybris.platform.core.model.order.payment.PaymentModeModel;

import java.util.List;


/**
 * Service around the {@link PaymentModeModel}. {@link PaymentModeModel} is used in calculation and store informations
 * about payment on order.
 * 
 * @spring.bean paymentModeService
 */
public interface PaymentModeService
{

	/**
	 * Gets the {@link PaymentModeModel} with the specified code.
	 * 
	 * @param code
	 *           the code
	 * @return the found list of {@link PaymentModeModel} with the specified code
	 */
	PaymentModeModel getPaymentModeForCode(final String code);

	/**
	 * Gets all payment modes
	 */
	List<PaymentModeModel> getAllPaymentModes();


}
