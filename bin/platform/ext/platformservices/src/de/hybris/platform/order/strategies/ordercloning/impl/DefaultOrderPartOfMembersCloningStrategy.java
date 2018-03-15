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
package de.hybris.platform.order.strategies.ordercloning.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.strategies.ordercloning.OrderPartOfMembersCloningStrategy;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.user.AddressService;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class DefaultOrderPartOfMembersCloningStrategy extends AbstractBusinessService implements
		OrderPartOfMembersCloningStrategy
{

	private AddressService addressService;

	private ModelCloningContext paymentInfoCloningContext;

	@Override
	public boolean addressNeedsCloning(final AddressModel address, final OrderModel order)
	{
		validateParameterNotNullStandardMessage("order", order);
		if (address == null)
		{
			return false;
		}
		return !order.equals(address.getOwner());
	}

	@Override
	public AddressModel cloneAddressForOrder(final AddressModel address, final OrderModel order)
	{
		return addressService.cloneAddressForOwner(address, order);
	}

	@Override
	public PaymentInfoModel clonePaymentInfoForOrder(final PaymentInfoModel paymentInfo, final OrderModel order)
	{
		validateParameterNotNullStandardMessage("order", order);
		validateParameterNotNullStandardMessage("paymentInfo", paymentInfo);
		final PaymentInfoModel newPaymentInfo = getModelService().clone(paymentInfo, this.paymentInfoCloningContext);
		newPaymentInfo.setOwner(order);
		newPaymentInfo.setDuplicate(Boolean.TRUE);
		newPaymentInfo.setOriginal(paymentInfo);
		return newPaymentInfo;
	}

	@Override
	public boolean paymentInfoNeedsCloning(final PaymentInfoModel paymentInfo, final OrderModel order)
	{
		validateParameterNotNullStandardMessage("order", order);
		if (paymentInfo == null)
		{
			return false;
		}
		return !order.equals(paymentInfo.getOwner());
	}

	@Required
	public void setAddressService(final AddressService addressService)
	{
		this.addressService = addressService;
	}

	public void setPaymentInfoCloningContext(final ModelCloningContext paymentInfoCloningContext)
	{
		this.paymentInfoCloningContext = paymentInfoCloningContext;
	}

}
