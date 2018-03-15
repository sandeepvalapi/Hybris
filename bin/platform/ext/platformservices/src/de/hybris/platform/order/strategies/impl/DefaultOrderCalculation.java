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
package de.hybris.platform.order.strategies.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.OrderCalculation;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link OrderCalculation} interface.
 */
public class DefaultOrderCalculation extends AbstractBusinessService implements OrderCalculation
{

	private CalculationService calculationService;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(DefaultOrderCalculation.class);

	@Override
	public boolean calculate(final AbstractOrderModel order)
	{
		final boolean orderUpToDate = getModelService().isUpToDate(order);
		if (orderUpToDate && Boolean.TRUE.equals(order.getCalculated()))
		{
			//return false if the order is refreshed and calculated;
			return false;
		}
		try
		{
			if (!orderUpToDate)
			{
				getModelService().save(order);
			}
			calculationService.calculate(order);
			return true;
		}
		catch (final CalculationException e)
		{
			throw new SystemException("Could not calculate order [" + order.getCode() + "] due to : " + e.getMessage(), e);
		}
	}

	@Required
	public void setCalculationService(final CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}

}
