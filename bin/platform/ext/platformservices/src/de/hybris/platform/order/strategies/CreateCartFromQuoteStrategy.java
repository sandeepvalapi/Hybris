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

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.QuoteModel;


/**
 * Strategy for creating a {@link CartModel} instance based on a given {@link QuoteModel} instance.
 */
public interface CreateCartFromQuoteStrategy
{

	/**
	 * Creates a new {@link CartModel} based on the given {@link QuoteModel}.
	 *
	 * @param quote
	 *           the {@link QuoteModel} base on which to create a new {@link CartModel}
	 * @return the new {@link CartModel}
	 */
	CartModel createCartFromQuote(QuoteModel quote);
}
