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

import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.order.payment.PaymentInfo;


/**
 * Service around the {@link PaymentInfoModel}. {@link PaymentInfoModel} represents the information about a customers
 * chosen payment mode, like credit card informations or debitentries.
 *
 * @spring.bean paymentInfoService
 */
public interface PaymentInfoService
{
	/**
	 * Creates a new, not persisted {@link PaymentInfo} for the given user with a given code.
	 *
	 * @param user
	 *           creates an payment info for this {@link UserModel}
	 * @param code
	 *           payment info code
	 * @return a new, not persisted {@link PaymentInfo}
	 * @throws IllegalArgumentException
	 *            if the given user or code is <code>null</code>
	 */
	PaymentInfoModel createPaymentInfoForUser(UserModel user, String code);
}
