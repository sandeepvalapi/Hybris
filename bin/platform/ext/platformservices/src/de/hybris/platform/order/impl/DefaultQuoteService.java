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

import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.QuoteService;
import de.hybris.platform.order.strategies.CreateQuoteFromCartStrategy;
import de.hybris.platform.order.strategies.CreateQuoteSnapshotStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link QuoteService}
 */
public class DefaultQuoteService implements QuoteService
{
	protected static final String QUOTES_QUERY = "SELECT {quote:" + QuoteModel.PK + "} FROM {" + QuoteModel._TYPECODE
			+ " as quote}";
	protected static final String WHERE_CODE_CLAUSE = " WHERE {quote:" + QuoteModel.CODE + "}=?code";
	protected static final String WHERE_CODE_AND_VERSION = WHERE_CODE_CLAUSE + " AND {quote:" + QuoteModel.VERSION + "}=?version";
	protected static final String ORDER_BY_VERSION_DESC = " ORDER BY {quote:" + QuoteModel.VERSION + "} DESC";

	private FlexibleSearchService flexibleSearchService;
	private CreateQuoteFromCartStrategy createQuoteFromCartStrategy;
	private CreateQuoteSnapshotStrategy createQuoteSnapshotStrategy;

	@Override
	public QuoteModel getCurrentQuoteForCode(final String code)
	{
		validateParameterNotNullStandardMessage("code", code);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(QUOTES_QUERY + WHERE_CODE_CLAUSE + ORDER_BY_VERSION_DESC);
		searchQuery.addQueryParameter("code", code);
		searchQuery.setCount(1);

		return getFlexibleSearchService().searchUnique(searchQuery);
	}

	@Override
	public QuoteModel getQuoteForCodeAndVersion(final String code, final Integer version)
	{
		validateParameterNotNullStandardMessage("code", code);
		validateParameterNotNullStandardMessage("version", version);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(QUOTES_QUERY + WHERE_CODE_AND_VERSION);
		searchQuery.addQueryParameter("code", code);
		searchQuery.addQueryParameter("version", version);

		return getFlexibleSearchService().searchUnique(searchQuery);
	}

	@Override
	public List<QuoteModel> getQuotesForCode(final String code)
	{
		validateParameterNotNullStandardMessage("code", code);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(QUOTES_QUERY + WHERE_CODE_CLAUSE + ORDER_BY_VERSION_DESC);
		searchQuery.addQueryParameter("code", code);

		final SearchResult<QuoteModel> searchResult = getFlexibleSearchService().search(searchQuery);

		return searchResult.getResult();
	}

	@Override
	public QuoteModel createQuoteFromCart(final CartModel cart)
	{
		validateParameterNotNullStandardMessage("cart", cart);
		return getCreateQuoteFromCartStrategy().createQuoteFromCart(cart);
	}

	@Override
	public QuoteModel createQuoteSnapshot(final QuoteModel quote, final QuoteState quoteState)
	{
		validateParameterNotNullStandardMessage("quote", quote);
		validateParameterNotNullStandardMessage("quoteState", quoteState);
		return getCreateQuoteSnapshotStrategy().createQuoteSnapshot(quote, quoteState);
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	protected CreateQuoteFromCartStrategy getCreateQuoteFromCartStrategy()
	{
		return createQuoteFromCartStrategy;
	}

	@Required
	public void setCreateQuoteFromCartStrategy(final CreateQuoteFromCartStrategy createQuoteFromCartStrategy)
	{
		this.createQuoteFromCartStrategy = createQuoteFromCartStrategy;
	}

	protected CreateQuoteSnapshotStrategy getCreateQuoteSnapshotStrategy()
	{
		return createQuoteSnapshotStrategy;
	}

	@Required
	public void setCreateQuoteSnapshotStrategy(final CreateQuoteSnapshotStrategy createQuoteSnapshotStrategy)
	{
		this.createQuoteSnapshotStrategy = createQuoteSnapshotStrategy;
	}

}
