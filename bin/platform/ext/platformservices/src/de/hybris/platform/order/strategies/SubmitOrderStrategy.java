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
package de.hybris.platform.order.strategies;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.OrderService;


public interface SubmitOrderStrategy
{
	/**
	 * Submits the order. One of strategies that is invoked by {@link OrderService#submitOrder(OrderModel)}. You can have
	 * your own implementation(s) there.
	 * 
	 * @param order
	 *           order to submit.
	 */
	void submitOrder(OrderModel order);
}
