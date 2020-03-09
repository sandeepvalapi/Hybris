/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


public class SplitByPoS extends AbstractSplittingStrategy
{
	@Override
	public Object getGroupingObject(final AbstractOrderEntryModel orderEntry)
	{
		return orderEntry.getDeliveryPointOfService();
	}

	@Override
	public void afterSplitting(final Object groupingObject, final ConsignmentModel consignmentModel)
	{
		consignmentModel.setDeliveryPointOfService((PointOfServiceModel) groupingObject);
	}
}
