/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.strategy.impl;

import de.hybris.platform.commerceservices.delivery.dao.PickupDeliveryModeDao;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;

import org.springframework.beans.factory.annotation.Required;


public class SplitByDeliveryMode extends AbstractSplittingStrategy
{
	private PickupDeliveryModeDao pickupDeliveryModeDao;

	@Override
	public Object getGroupingObject(final AbstractOrderEntryModel orderEntry)
	{
		return orderEntry.getDeliveryMode() != null ? orderEntry.getDeliveryMode()
				: orderEntry.getDeliveryPointOfService() == null ? orderEntry.getOrder().getDeliveryMode()
						: getPickupDeliveryModeDao().findPickupDeliveryModesForAbstractOrder(orderEntry.getOrder()).get(0);
	}

	@Override
	public void afterSplitting(final Object groupingObject, final ConsignmentModel createdOne)
	{
		createdOne.setDeliveryMode((DeliveryModeModel) groupingObject);
	}

	protected PickupDeliveryModeDao getPickupDeliveryModeDao()
	{
		return pickupDeliveryModeDao;
	}

	@Required
	public void setPickupDeliveryModeDao(final PickupDeliveryModeDao pickupDeliveryModeDao)
	{
		this.pickupDeliveryModeDao = pickupDeliveryModeDao;
	}
}
