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
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryMode;
import de.hybris.platform.util.PriceValue;


/**
 * Strategy focused on resolving delivery cost for a given order. Delivery cost depends on the delivery mode chosen at
 * the checkout. In case of {@link ZoneDeliveryMode}s, the cost usually depends on the delivery zone.
 */
public interface FindDeliveryCostStrategy
{

	/**
	 * Returns order's delivery cost of the given order.
	 * 
	 * @param order
	 *           {@link AbstractOrderModel}
	 * @return {@link PriceValue} representing delivery cost introduced in the order.
	 */
	PriceValue getDeliveryCost(AbstractOrderModel order);
}
