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
package de.hybris.platform.catalog.daos.impl;

import de.hybris.platform.catalog.daos.KeywordDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of interface {@link KeywordDao}.
 */
public class DefaultKeywordDao implements KeywordDao
{

	private FlexibleSearchService flexibleSearchService;

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public List<KeywordModel> getKeyword(final CatalogVersionModel catalogVersion, final String keywordValue)
	{
		return getKeywords(catalogVersion, keywordValue);
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public List<KeywordModel> getKeyword(final String typeCode, final CatalogVersionModel catalogVersion, final String keywordValue)
	{
		return getKeywords(typeCode, catalogVersion, keywordValue);
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public List<KeywordModel> getKeywords(final CatalogVersionModel catalogVersion, final String keywordValue)
	{
		return getKeywords(KeywordModel._TYPECODE, catalogVersion, keywordValue);
	}

	@Override
	public List<KeywordModel> getKeywords(final String typeCode, final CatalogVersionModel catalogVersion,
			final String keywordValue)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("catalogVersion", catalogVersion);
		ServicesUtil.validateParameterNotNullStandardMessage("keyword", keywordValue);
		ServicesUtil.validateParameterNotNullStandardMessage("typeCode", typeCode);

		final StringBuffer sql = new StringBuffer(50);
		final Map<String, Object> params = new HashMap<String, Object>();


		sql.append("select {").append(KeywordModel.PK).append('}');
		sql.append("  from {").append(typeCode).append('}');
		sql.append(" where {").append(KeywordModel.CATALOGVERSION).append("} = ?").append(KeywordModel.CATALOGVERSION);
		sql.append("   and {").append(KeywordModel.KEYWORD).append("} = ?").append(KeywordModel.KEYWORD);

		params.put(KeywordModel.CATALOGVERSION, catalogVersion);
		params.put(KeywordModel.KEYWORD, keywordValue);

		final SearchResult<KeywordModel> res = flexibleSearchService.search(sql.toString(), params);

		return res.getResult();
	}

}
