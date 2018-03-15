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
package de.hybris.platform.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.order.DiscountService;
import de.hybris.platform.order.daos.DiscountDao;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link DiscountService}.
 */
public class DefaultDiscountService extends AbstractBusinessService implements DiscountService
{

	private DiscountDao discountDao;

	@Override
	public DiscountModel getDiscountForCode(final String code)
	{
		validateParameterNotNullStandardMessage("code", code);
		final List<DiscountModel> discounts = discountDao.findDiscountsByCode(code);
		if (discounts.isEmpty())
		{
			throw new UnknownIdentifierException("Discount with code [" + code + "] not found!");
		}
		else if (discounts.size() > 1)
		{
			throw new AmbiguousIdentifierException("Discount code [" + code + "] is not unique, " + discounts.size()
					+ " discounts found!");
		}
		return discounts.get(0);
	}

	@Override
	public Collection<DiscountModel> getDiscountsForCode(final String matchedCode)
	{
		validateParameterNotNullStandardMessage("matchedCode", matchedCode);
		final Collection<DiscountModel> discounts = discountDao.findDiscountsByMatchedCode(matchedCode);
		return discounts;
	}


	@Override
	public Collection<DiscountModel> getDiscountsForCurrency(final CurrencyModel currency)
	{
		validateParameterNotNullStandardMessage("currency", currency);
		final Collection<DiscountModel> discounts = discountDao.findDiscountsByCurrencyIsoCode(currency.getIsocode());
		return discounts;
	}

	@Required
	public void setDiscountDao(final DiscountDao discountDao)
	{
		this.discountDao = discountDao;
	}

}
