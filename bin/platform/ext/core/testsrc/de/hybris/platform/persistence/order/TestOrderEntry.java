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
package de.hybris.platform.persistence.order;

import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.util.PriceValue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class TestOrderEntry extends OrderEntry
{
	public static final double PRICE = 123.45;


	@Override
	protected List findDiscounts() throws JaloPriceFactoryException
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	protected PriceValue findPrice() throws JaloPriceFactoryException
	{
		return new PriceValue(getOrder().getCurrency().getIsoCode(), PRICE, getOrder().isNet().booleanValue());
	}

	@Override
	protected Collection findTaxes() throws JaloPriceFactoryException
	{
		return Collections.EMPTY_LIST;
	}
}
