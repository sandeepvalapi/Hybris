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
package com.hybris.training.fulfilmentprocess.actions.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.servicelayer.time.TimeService;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public abstract class AbstractOrderAction<T extends OrderProcessModel> extends AbstractAction<T>
{
	protected TimeService timeService; // NOPMD

	/**
	 * Prepares order history entry {@link OrderHistoryEntryModel} for the given order and description and with the
	 * current timestamp. The {@link OrderHistoryEntryModel} is not saved!.
	 *
	 * @param description
	 * @param order
	 * @return {@link OrderHistoryEntryModel}
	 */
	protected OrderHistoryEntryModel createHistoryLog(final String description, final OrderModel order)
	{
		final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(getTimeService().getCurrentTime());
		historyEntry.setOrder(order);
		historyEntry.setDescription(description);
		return historyEntry;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}
}
