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
package de.hybris.platform.order.strategies.calculation.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.order.strategies.calculation.FindPaymentCostStrategy;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.util.PriceValue;

import org.apache.log4j.Logger;


/**
 * Default implementation of {@link FindPaymentCostStrategy}.
 */
public class DefaultFindPaymentCostStrategy extends AbstractBusinessService implements FindPaymentCostStrategy
{

	private final static Logger LOG = Logger.getLogger(DefaultFindPaymentCostStrategy.class);

	@Override
	public PriceValue getPaymentCost(final AbstractOrderModel order)
	{
		try
		{
			PaymentModeModel paymentMode = order.getPaymentMode();
			if( paymentMode != null )
			{
				getModelService().save(order);
   			final AbstractOrder orderItem = getModelService().getSource(order);
   			final PaymentMode pModeJalo = getModelService().getSource(paymentMode);
   			return pModeJalo.getCost(orderItem);
			}
			else
			{
				return new PriceValue(order.getCurrency().getIsocode(), 0.0, order.getNet().booleanValue());
			}
		}
		catch (final Exception e)
		{
			LOG.warn("Could not find paymentCost for order [" + order.getCode() + "] due to : " + e + "... skipping!");
			return new PriceValue(order.getCurrency().getIsocode(), 0.0, order.getNet().booleanValue());
		}
	}

}
