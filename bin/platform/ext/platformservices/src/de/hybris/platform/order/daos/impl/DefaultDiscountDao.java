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
package de.hybris.platform.order.daos.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.order.daos.DiscountDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.SearchTools;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Default implementation of the {@link DiscountDao}.
 */
public class DefaultDiscountDao extends AbstractItemDao implements DiscountDao
{

	@Override
	public List<DiscountModel> findDiscountsByCode(final String code)
	{
		final String query = "SELECT {" + DiscountModel.PK + "} FROM {" + DiscountModel._TYPECODE + "} WHERE {"
				+ DiscountModel.CODE + "}=?" + DiscountModel.CODE + " ORDER BY {" + DiscountModel.CREATIONTIME + "} DESC";
		final List<DiscountModel> discounts = doSearch(query, code);
		return discounts;
	}

	@Override
	public Collection<DiscountModel> findDiscountsByMatchedCode(final String matchedCode)
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(DiscountModel.PK).append("} FROM {").append(
				DiscountModel._TYPECODE).append("} WHERE {" + DiscountModel.CODE + "}");
		query.append(SearchTools.isLIKEPattern(matchedCode) ? " LIKE " : " = ").append("?").append(DiscountModel.CODE);
		query.append(" ORDER BY {" + DiscountModel.CODE + " } ASC, {" + DiscountModel.CREATIONTIME + "} DESC");
		final List<DiscountModel> discounts = doSearch(query.toString(), matchedCode);
		return discounts;
	}

	private List<DiscountModel> doSearch(final String query, final String codeParam)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameter(DiscountModel.CODE, codeParam);
		final SearchResult<DiscountModel> result = getFlexibleSearchService().search(fQuery);
		final List<DiscountModel> discounts = result.getResult();
		return discounts;
	}

	@Override
	public Collection<DiscountModel> findDiscountsByCurrencyIsoCode(final String currencyIsoCode)
	{
		final StringBuilder query = new StringBuilder("SELECT {d.").append(DiscountModel.PK).append("} FROM {").append(
				DiscountModel._TYPECODE).append(" AS d JOIN ").append(CurrencyModel._TYPECODE).append(" AS c ON {d.").append(
				DiscountModel.CURRENCY).append("}={c.").append(CurrencyModel.PK).append("}} WHERE {c.").append(CurrencyModel.ISOCODE)
				.append("} = ?c");
		final SearchResult<DiscountModel> result = getFlexibleSearchService().search(query.toString(),
				Collections.singletonMap("c", currencyIsoCode));
		final List<DiscountModel> discounts = result.getResult();
		return discounts == null ? Collections.EMPTY_LIST : discounts;
	}
}
