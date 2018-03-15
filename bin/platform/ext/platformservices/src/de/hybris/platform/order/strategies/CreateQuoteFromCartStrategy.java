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
 * Strategy for creating a {@link QuoteModel} instance based on a given {@link CartModel} instance.
 */
public interface CreateQuoteFromCartStrategy
{

	/**
	 * Creates a new {@link QuoteModel} based on the given {@link CartModel}.
	 *
	 * @param cart
	 *           the {@link CartModel} base on which to create a new {@link QuoteModel}
	 * @return the new {@link QuoteModel}
	 */
	QuoteModel createQuoteFromCart(final CartModel cart);
}
