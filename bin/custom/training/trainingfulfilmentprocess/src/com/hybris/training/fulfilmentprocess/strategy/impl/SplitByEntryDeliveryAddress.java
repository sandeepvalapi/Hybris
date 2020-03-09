/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;


/**
 * Splitting by Delivery Address with support for Pickup In Store
 */
public class SplitByEntryDeliveryAddress extends AbstractSplittingStrategy
{

	@Override
	public Object getGroupingObject(final AbstractOrderEntryModel orderEntry)
	{
		AddressModel shippingAddress = null;

		if (orderEntry.getDeliveryAddress() != null)
		{
			shippingAddress = orderEntry.getDeliveryAddress();
		}
		else if (orderEntry.getDeliveryPointOfService() != null && orderEntry.getDeliveryPointOfService().getAddress() != null)
		{
			shippingAddress = orderEntry.getDeliveryPointOfService().getAddress();
		}
		else
		{
			shippingAddress = orderEntry.getOrder().getDeliveryAddress();
		}
		return shippingAddress;
	}

	@Override
	public void afterSplitting(final Object groupingObject, final ConsignmentModel createdOne)
	{
		createdOne.setShippingAddress((AddressModel) groupingObject);
	}

}
