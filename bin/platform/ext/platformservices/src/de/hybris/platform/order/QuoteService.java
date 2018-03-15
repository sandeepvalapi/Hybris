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
package de.hybris.platform.order;

import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.interceptors.DefaultQuotePrepareInterceptor;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;


/**
 * Business service for handling quotes.
 */
public interface QuoteService
{
	/**
	 * Returns the latest quote snapshot for the given code, i.e. the one with the highest version.
	 *
	 * @param code
	 *           the code to query for
	 * @return latest quote snapshot for the given code
	 * @throws IllegalArgumentException
	 *            in case code is null
	 * @throws ModelNotFoundException
	 *            in case there is no latest snapshot for the given code
	 */
	QuoteModel getCurrentQuoteForCode(final String code);

	/**
	 * Returns a unique {@link QuoteModel} for the given code and version.
	 *
	 * @param code
	 *           the code to query for
	 * @param version
	 *           the version to query for
	 * @return unique quote for the given code and version
	 * @throws IllegalArgumentException
	 *            in case one of the parameters is null
	 * @throws ModelNotFoundException
	 *            in case there is no unique quote for the given code and version
	 */
	QuoteModel getQuoteForCodeAndVersion(final String code, final Integer version);

	/**
	 * Returns a list of all quotes snapshots for the given code, ordered by version in descending order, i.e. the first
	 * item in the returned list is the latest snapshot of the quote.
	 *
	 * @param code
	 *           the code to query for
	 * @return a list of ordered quote snapshots for the given code
	 * @throws IllegalArgumentException
	 *            in case code is null
	 */
	List<QuoteModel> getQuotesForCode(final String code);

	/**
	 * Creates a new quote based on the given cart. Please note that it is the caller's responsibility to persist the
	 * quote that is returned by this method. Callers may either set quote specific attributes before persisting the item
	 * or leave it to the {@link DefaultQuotePrepareInterceptor} to set default values. The cart that is passed into this
	 * method is not affected by its logic.
	 *
	 * @param cart
	 *           the cart based on which the new quote will be created
	 * @return the new quote
	 * @throws IllegalArgumentException
	 *            in case cart is null
	 */
	QuoteModel createQuoteFromCart(final CartModel cart);

	/**
	 * Creates a new quote snapshot by cloning the given {@link QuoteModel}. The state of the new snapshot is determined
	 * by the given {@link QuoteState}, its version is the given quote's version incremented by 1. Please note that it is
	 * the caller's responsibility to persist the quote that is returned by this method. The original quote passed into
	 * this method is not affected by its logic.
	 *
	 * @param quote
	 *           quote to create the snapshot from
	 * @param quoteState
	 *           desired state of the quote
	 * @return the new quote snapshot.
	 * @throws IllegalArgumentException
	 *            in case one of the parameters is null
	 */
	QuoteModel createQuoteSnapshot(QuoteModel quote, QuoteState quoteState);

}
