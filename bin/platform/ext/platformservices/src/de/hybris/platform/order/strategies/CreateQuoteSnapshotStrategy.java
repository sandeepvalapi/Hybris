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

import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.QuoteModel;


/**
 * Strategy for creating a new snapshot of a {@link QuoteModel}.
 */
public interface CreateQuoteSnapshotStrategy
{

	/**
	 * Creates a new quote snapshot.
	 *
	 * @param quote
	 *           the {@link QuoteModel} to create a new snapshot for
	 * @param quoteState
	 *           the desired {@link QuoteState} for the new quote snapshot
	 * @return the new quote snapshot
	 */
	QuoteModel createQuoteSnapshot(QuoteModel quote, QuoteState quoteState);
}
