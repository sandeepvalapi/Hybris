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
