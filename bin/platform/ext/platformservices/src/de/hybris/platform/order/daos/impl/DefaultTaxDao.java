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

import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.order.daos.TaxDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.SearchTools;

import java.util.List;


/**
 * Default implementation of the {@link TaxDao}.
 */
public class DefaultTaxDao extends AbstractItemDao implements TaxDao
{

	@Override
	public List<TaxModel> findTaxesByCode(final String code)
	{
		final String query = "SELECT {" + TaxModel.PK + "} FROM {" + TaxModel._TYPECODE + "} WHERE {" + TaxModel.CODE + "}=?"
				+ TaxModel.CODE + " ORDER BY {" + TaxModel.CREATIONTIME + "} DESC";
		final List<TaxModel> taxes = doSearch(query, code);
		return taxes;
	}

	@Override
	public List<TaxModel> findTaxesByCodePattern(final String matchedCode)
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(TaxModel.PK).append("} FROM {").append(TaxModel._TYPECODE)
				.append("} WHERE {" + TaxModel.CODE + "}");
		query.append(SearchTools.isLIKEPattern(matchedCode) ? " LIKE " : " = ").append("?").append(TaxModel.CODE);
		final List<TaxModel> taxes = doSearch(query.toString(), matchedCode);
		return taxes;
	}

	private List<TaxModel> doSearch(final String query, final String codeParam)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameter(TaxModel.CODE, codeParam);
		final SearchResult<TaxModel> result = getFlexibleSearchService().search(fQuery);
		final List<TaxModel> taxes = result.getResult();
		return taxes;
	}

}
