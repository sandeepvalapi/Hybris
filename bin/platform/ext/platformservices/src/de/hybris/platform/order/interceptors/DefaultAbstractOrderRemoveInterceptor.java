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
package de.hybris.platform.order.interceptors;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;


/**
 * Removes duplicates cloned for the purpose of this order:<br>
 * <ul>
 * <li>{@link AbstractOrderModel#PAYMENTADDRESS}</li>
 * <li>{@link AbstractOrderModel#DELIVERYADDRESS}</li>
 * <li>{@link AbstractOrderModel#PAYMENTINFO}</li>
 * </ul>
 * 
 */
public class DefaultAbstractOrderRemoveInterceptor implements RemoveInterceptor
{

	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof AbstractOrderModel)
		{
			final AbstractOrderModel order = (AbstractOrderModel) model;

			// remove (duplicate) payment and delivery addresses
			final AddressModel paymentAddress = order.getPaymentAddress();
			final AddressModel deliveryAddress = order.getDeliveryAddress();

			final boolean theSame = paymentAddress != null && deliveryAddress != null && paymentAddress.equals(deliveryAddress);
			if (paymentAddress != null)
			{
				if (Boolean.TRUE.equals(paymentAddress.getDuplicate()))
				{
					ctx.getModelService().remove(paymentAddress);
				}
			}
			if (!theSame && deliveryAddress != null)
			{
				if (Boolean.TRUE.equals(deliveryAddress.getDuplicate()))
				{
					ctx.getModelService().remove(deliveryAddress);
				}
			}

			// remove (duplicate) payment info object
			final PaymentInfoModel paymentInfo = order.getPaymentInfo();
			if (paymentInfo != null && Boolean.TRUE.equals(paymentInfo.getDuplicate()))
			{
				ctx.getModelService().remove(paymentInfo);
			}
		}
	}


}
