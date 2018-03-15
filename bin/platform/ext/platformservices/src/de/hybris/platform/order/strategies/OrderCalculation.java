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

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.CalculationService;


/**
 * Provides functionality for the order calculation.
 * 
 * @spring.bean orderCalculation
 * @deprecated since ages - Use{@link CalculationService} to calculate orders.
 */
@Deprecated
public interface OrderCalculation
{
	/**
	 * Calculates the given <code>order</code> and returns <code>true</code> if each entry and after this the
	 * {@link AbstractOrderModel} was calculated. Thereby any invalid entry will be automatically removed.
	 * 
	 * @param order
	 *           the {@link AbstractOrderModel}
	 * @return <code>false</code> if the <code>order</code> was already calculated.
	 */
	boolean calculate(AbstractOrderModel order);
}
