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
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.order.strategies.calculation.FindDeliveryCostStrategy;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.PriceValue;

import org.apache.log4j.Logger;


/**
 * Default implementation of {@link FindDeliveryCostStrategy}.
 */
public class DefaultFindDeliveryCostStrategy extends AbstractBusinessService implements FindDeliveryCostStrategy
{

	private static final Logger LOG = Logger.getLogger(DefaultFindDeliveryCostStrategy.class);

	//step 1 : delegate to jalo
	@Override
	public PriceValue getDeliveryCost(final AbstractOrderModel order)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		try
		{
			DeliveryModeModel deliveryMode = order.getDeliveryMode();
			if( deliveryMode != null )
			{
   			getModelService().save(order);
   			final AbstractOrder orderItem = getModelService().getSource(order);
   			final DeliveryMode dModeJalo = getModelService().getSource(deliveryMode);
   			return dModeJalo.getCost(orderItem);
			}
			else
			{
				return new PriceValue(order.getCurrency().getIsocode(), 0.0, order.getNet().booleanValue()); 
			}
		}
		catch (final Exception e)
		{
			LOG.warn("Could not find deliveryCost for order [" + order.getCode() + "] due to : " + e + "... skipping!");
			return new PriceValue(order.getCurrency().getIsocode(), 0.0, order.getNet().booleanValue());
		}
	}

}
