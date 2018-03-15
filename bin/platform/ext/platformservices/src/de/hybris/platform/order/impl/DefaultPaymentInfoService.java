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

import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.PaymentInfoService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;


public class DefaultPaymentInfoService extends AbstractBusinessService implements PaymentInfoService
{

	@Override
	public PaymentInfoModel createPaymentInfoForUser(final UserModel user, final String code)
	{
		validateParameterNotNullStandardMessage("user", user);
		validateParameterNotNullStandardMessage("code", code);
		final PaymentInfoModel paymentInfo = getModelService().create(PaymentInfoModel.class);
		paymentInfo.setUser(user);
		paymentInfo.setCode(code);
		return paymentInfo;
	}



}
