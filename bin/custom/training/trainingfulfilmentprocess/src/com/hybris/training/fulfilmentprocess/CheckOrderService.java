/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * Used by CheckOrderAction, this service is designed to validate the order prior to running the fulfilment process.
 */
public interface CheckOrderService
{
	/**
	 * Performs various order checks
	 *
	 * @param order
	 *           the order
	 * @return whether the order is ready for fulfillment or not
	 */
	boolean check(OrderModel order);
}
