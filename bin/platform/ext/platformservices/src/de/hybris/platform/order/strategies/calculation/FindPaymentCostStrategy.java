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
package de.hybris.platform.order.strategies.calculation;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.util.PriceValue;


/**
 * Strategy focused on resolving payment cost for a given order. Payment cost depends on the payment mode chosen at the
 * checkout.
 */
public interface FindPaymentCostStrategy
{

	/**
	 * Returns cost of the given order that is related with payment.
	 * 
	 * @param order
	 *           {@link AbstractOrderModel}
	 * @return {@link PriceValue} representing payment cost introduced in the order.
	 */
	PriceValue getPaymentCost(AbstractOrderModel order);
}
