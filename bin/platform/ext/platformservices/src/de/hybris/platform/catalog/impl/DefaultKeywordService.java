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
package de.hybris.platform.catalog.impl;

import de.hybris.platform.catalog.KeywordService;
import de.hybris.platform.catalog.daos.KeywordDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link KeywordService}. Use {@link KeywordDao} to search keywords.
 */
public class DefaultKeywordService implements KeywordService
{

	private KeywordDao keywordDao;

	@Override
	public KeywordModel getKeyword(final CatalogVersionModel catalogVersion, final String keywordValue)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("catalogVersion", catalogVersion);
		ServicesUtil.validateParameterNotNullStandardMessage("keyword", keywordValue);

		final List<KeywordModel> res = keywordDao.getKeywords(catalogVersion, keywordValue);

		ServicesUtil.validateIfSingleResult(res, KeywordModel.class, "catalogVersion, keyword", keywordValue + ", "
				+ catalogVersion.getPk());

		return res.get(0);
	}

	@Override
	public KeywordModel getKeyword(final String typeCode, final CatalogVersionModel catalogVersion, final String keywordValue)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("catalogVersion", catalogVersion);
		ServicesUtil.validateParameterNotNullStandardMessage("keyword", keywordValue);
		ServicesUtil.validateParameterNotNullStandardMessage("typeCode", typeCode);

		final List<KeywordModel> res = keywordDao.getKeywords(typeCode, catalogVersion, keywordValue);

		ServicesUtil.validateIfSingleResult(res, KeywordModel.class, "typeCode, catalogVersion, keyword", typeCode + ", "
				+ keywordValue + ", " + catalogVersion.getPk());

		return res.get(0);
	}

	@Required
	public void setKeywordDao(final KeywordDao keywordDao)
	{
		this.keywordDao = keywordDao;
	}

}
