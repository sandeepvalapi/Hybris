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
package de.hybris.platform.order.strategies.calculation.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;


/**
 * Default, simple implementation of {@link OrderRequiresCalculationStrategy}. It simply checks the boolean flags on
 * {@link AbstractOrderModel#CALCULATED} and {@link AbstractOrderEntryModel#CALCULATED} correspondingly.
 */
public class DefaultOrderRequiresCalculationStrategy implements OrderRequiresCalculationStrategy
{

	@Override
	public boolean requiresCalculation(final AbstractOrderModel order)
	{
		return Boolean.FALSE.equals(order.getCalculated());
	}

	@Override
	public boolean requiresCalculation(final AbstractOrderEntryModel orderEntry)
	{
		return Boolean.FALSE.equals(orderEntry.getCalculated());
	}

}
