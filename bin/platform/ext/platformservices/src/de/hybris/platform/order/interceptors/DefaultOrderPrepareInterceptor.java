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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.strategies.ordercloning.OrderPartOfMembersCloningStrategy;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import org.springframework.beans.factory.annotation.Required;


/**
 * Automatically clones the order's elements:
 * <ul>
 * <li>{@link AbstractOrderModel#PAYMENTINFO},</li>
 * <li>{@link AbstractOrderModel#PAYMENTADDRESS}</li>
 * <li>{@link AbstractOrderModel#DELIVERYADDRESS}</li>
 * </ul>
 * and sets the <b>clones</b> as actual values.<br>
 * If the cloned elements are clones themselves, the interceptor removes them.
 */
public class DefaultOrderPrepareInterceptor implements PrepareInterceptor
{
	private OrderPartOfMembersCloningStrategy orderPartOfMembersCloningStrategy;


	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof OrderModel)
		{
			final OrderModel order = (OrderModel) model;

			final ItemModelContextImpl context = (ItemModelContextImpl) order.getItemModelContext();
			final ModelValueHistory history = context.getValueHistory();

			//check if payment address was changed to a non null value
			if (ctx.isModified(order, OrderModel.PAYMENTADDRESS))
			{
				if (order.getPaymentAddress() != null)
				{
					final AddressModel paymentAddress = order.getPaymentAddress();
					//if the new payment address needs cloning, we need to clone it to be a part of this order's contract:				
					if (orderPartOfMembersCloningStrategy.addressNeedsCloning(paymentAddress, order))
					{
						final AddressModel paymentAddressClone = handleAddressCloning(paymentAddress, order);
						order.setPaymentAddress(paymentAddressClone);
						ctx.registerElementFor(paymentAddressClone, PersistenceOperation.SAVE);
					}
				}

				if (!ctx.isNew(order))
				{
					final AddressModel oldPaymentAddress = (AddressModel) history.getOriginalValue(OrderModel.PAYMENTADDRESS);
					registerAddressForRemovalIfNeeded(ctx, order, oldPaymentAddress);
				}
			}

			//check if payment address was changed to a non null value
			if (ctx.isModified(order, OrderModel.DELIVERYADDRESS))
			{
				if (order.getDeliveryAddress() != null)
				{
					final AddressModel deliveryAddress = order.getDeliveryAddress();
					//if the new delivery address needs cloning, we need to clone it to be a part of this order's contract:				
					if (orderPartOfMembersCloningStrategy.addressNeedsCloning(deliveryAddress, order))
					{
						final AddressModel deliveryAddressClone = handleAddressCloning(deliveryAddress, order);
						order.setDeliveryAddress(deliveryAddressClone);
						ctx.registerElementFor(deliveryAddressClone, PersistenceOperation.SAVE);
					}
				}

				if (!ctx.isNew(order))
				{
					final AddressModel oldDeliveryAddress = (AddressModel) history.getOriginalValue(OrderModel.DELIVERYADDRESS);
					registerAddressForRemovalIfNeeded(ctx, order, oldDeliveryAddress);
				}
			}

			//check if payment info was changed to a non null value
			if (ctx.isModified(order, OrderModel.PAYMENTINFO))
			{
				if (order.getPaymentInfo() != null)
				{
					final PaymentInfoModel paymentInfo = order.getPaymentInfo();
					//if the new payment info needs cloning, we need to clone it to be a part of this order's contract:				
					if (orderPartOfMembersCloningStrategy.paymentInfoNeedsCloning(paymentInfo, order))
					{
						final PaymentInfoModel paymentInfoClone = orderPartOfMembersCloningStrategy.clonePaymentInfoForOrder(
								paymentInfo, order);
						order.setPaymentInfo(paymentInfoClone);
						ctx.registerElement(paymentInfoClone, null);
					}
				}

				if (!ctx.isNew(order))
				{
					registerPaymentInfoForRemovalIfNeeded(ctx, order, history);
				}
			}

		}
	}

	private void registerPaymentInfoForRemovalIfNeeded(final InterceptorContext ctx, final OrderModel order,
			final ModelValueHistory history)
	{
		final PaymentInfoModel oldPaymentInfo = (PaymentInfoModel) history.getOriginalValue(OrderModel.PAYMENTINFO);
		if (oldPaymentInfo != null && //
				!oldPaymentInfo.equals(order.getPaymentInfo()) && //
				oldPaymentInfo.getDuplicate())
		{
			ctx.registerElementFor(oldPaymentInfo, PersistenceOperation.DELETE);
		}
	}

	private void registerAddressForRemovalIfNeeded(final InterceptorContext ctx, final OrderModel order,
			final AddressModel addressModel)
	{
		if (addressModel != null && //
				!addressModel.equals(order.getPaymentAddress()) && //
				!addressModel.equals(order.getDeliveryAddress()) && //
				addressModel.getDuplicate())
		{
			ctx.registerElementFor(addressModel, PersistenceOperation.DELETE);
		}
	}

	private AddressModel handleAddressCloning(final AddressModel address, final OrderModel order)
	{
		return orderPartOfMembersCloningStrategy.cloneAddressForOrder(address, order);
	}


	@Required
	public void setOrderPartOfMembersCloningStrategy(final OrderPartOfMembersCloningStrategy orderPartOfMembersCloningStrategy)
	{
		this.orderPartOfMembersCloningStrategy = orderPartOfMembersCloningStrategy;
	}

}
