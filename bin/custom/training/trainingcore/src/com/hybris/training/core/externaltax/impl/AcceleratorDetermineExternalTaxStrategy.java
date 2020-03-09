/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.externaltax.impl;

import de.hybris.platform.commerceservices.externaltax.DecideExternalTaxesStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;


/**
 * Accelerator
 * 
 */
public class AcceleratorDetermineExternalTaxStrategy implements DecideExternalTaxesStrategy
{
	/**
	 * Initially just to test if the delivery mode and address are set, than calculate the external taxes.
	 * 
	 * Products in cart, delivery mode, delivery address and payment information to determine whether or not to calculate
	 * taxes as tracked in https://jira.hybris.com/browse/ECP-845.
	 */
	@Override
	public boolean shouldCalculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		if (abstractOrder == null)
		{
			throw new IllegalStateException("Order is null. Cannot apply external tax to it.");
		}

		return Boolean.TRUE.equals(abstractOrder.getNet()) && abstractOrder.getDeliveryMode() != null && abstractOrder
				.getDeliveryAddress() != null;
	}
}
