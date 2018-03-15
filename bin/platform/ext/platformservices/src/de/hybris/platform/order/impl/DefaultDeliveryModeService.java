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

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link DeliveryModeService}.
 */
public class DefaultDeliveryModeService extends AbstractBusinessService implements DeliveryModeService
{

	private DeliveryModeDao deliveryModeDao;

	@Override
	public DeliveryModeModel getDeliveryModeForCode(final String code)
	{
		validateParameterNotNullStandardMessage("code", code);
		final List<DeliveryModeModel> deliveryModes = deliveryModeDao.findDeliveryModesByCode(code);
		if (deliveryModes.isEmpty())
		{
			throw new UnknownIdentifierException("Delivery mode with code [" + code + "] not found!");
		}
		else if (deliveryModes.size() > 1)
		{
			throw new AmbiguousIdentifierException("Delivery mode code [" + code + "] is not unique, " + deliveryModes.size()
					+ " delivery modes found!");
		}
		return deliveryModes.get(0);
	}

	@Override
	public Collection<DeliveryModeModel> getAllDeliveryModes()
	{
		return deliveryModeDao.findAllDeliveryModes();
	}

	@Override
	public Collection<DeliveryModeModel> getSupportedDeliveryModes(final PaymentModeModel paymentMode)
	{
		validateParameterNotNullStandardMessage("paymentMode", paymentMode);
		return deliveryModeDao.findDeliveryModeByPaymentMode(paymentMode);
	}


	@Required
	public void setDeliveryModeDao(final DeliveryModeDao deliveryModeDao)
	{
		this.deliveryModeDao = deliveryModeDao;
	}



}
