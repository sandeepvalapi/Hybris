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
package de.hybris.platform.category.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Default implementation for {@link CategoryDao}.
 */
public class DefaultCategoryDao extends AbstractItemDao implements CategoryDao
{
	/**
	 * {@inheritDoc}
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public Collection<CategoryModel> findCategories(final CatalogVersionModel catalogVersion, final String code)
	{
		return findCategoriesByCode(catalogVersion, code);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CategoryModel> findCategoriesByCode(final CatalogVersionModel catalogVersion, final String code)
	{
		final StringBuilder query = new StringBuilder("SELECT {cat." + CategoryModel.PK + "} ");
		query.append("FROM {" + CategoryModel._TYPECODE + " AS cat} ");
		query.append("WHERE {cat." + CategoryModel.CODE + "} = ?" + CategoryModel.CODE);
		query.append(" AND {cat." + CategoryModel.CATALOGVERSION + "} = (?" + CategoryModel.CATALOGVERSION + ")");

		final Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(CategoryModel.CATALOGVERSION, catalogVersion);
		params.put(CategoryModel.CODE, code);

		final SearchResult<CategoryModel> searchRes = search(query.toString(), params);
		return searchRes.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CategoryModel> findCategoriesByCode(final String code)
	{
		final StringBuilder query = new StringBuilder("SELECT {cat." + CategoryModel.PK + "} ");
		query.append("FROM {" + CategoryModel._TYPECODE + " AS cat} ");
		query.append("WHERE {cat." + CategoryModel.CODE + "} = ?" + CategoryModel.CODE);

		final Map<String, Object> params = (Map) Collections.singletonMap(CategoryModel.CODE, code);

		final SearchResult<CategoryModel> searchRes = search(query.toString(), params);
		return searchRes.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CategoryModel> findCategoriesByCatalogVersionAndProduct(final CatalogVersionModel catver,
			final ProductModel product)
	{
		final StringBuilder query = new StringBuilder();
		query.append("SELECT {").append(CategoryModel.PK).append("} ");
		query.append("FROM {").append(CategoryModel._TYPECODE).append(" AS cat ");
		query.append("JOIN ").append(CategoryConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS c2pRel ");
		query.append("ON {c2pRel:").append(LinkModel.SOURCE).append("}={cat:").append(CategoryModel.PK).append("} } ");
		query.append("WHERE {cat:").append(CategoryModel.CATALOGVERSION).append("}=?cv ");
		query.append("AND {c2pRel:").append(LinkModel.TARGET).append("}=?product ");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("cv", catver);
		params.put("product", product);

		final SearchResult<CategoryModel> searchResult = search(query.toString(), params);
		return searchResult.getResult();
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public Collection<CategoryModel> findRootCategories(final CatalogVersionModel catalogVersion)
	{
		return findRootCategoriesByCatalogVersion(catalogVersion);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CategoryModel> findRootCategoriesByCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		final StringBuilder query = new StringBuilder("SELECT {cat." + CategoryModel.PK + "} ");
		query.append("FROM {" + CategoryModel._TYPECODE + " AS cat} ");
		query.append("WHERE NOT EXISTS ({{");
		query.append("SELECT {pk} FROM {" + CategoryModel._CATEGORYCATEGORYRELATION + " AS rel ");
		query.append("JOIN " + CategoryModel._TYPECODE + " AS super ON {rel.source}={super.PK} } ");
		query.append("WHERE {rel:target}={cat.pk} ");
		query.append("AND {super." + CategoryModel.CATALOGVERSION + "}={cat." + CategoryModel.CATALOGVERSION + "} }}) ");
		query.append("AND {cat." + CategoryModel.CATALOGVERSION + "} = ?" + CategoryModel.CATALOGVERSION);

		final Map<String, Object> params = (Map) Collections.singletonMap(CategoryModel.CATALOGVERSION, catalogVersion);

		final SearchResult<CategoryModel> searchRes = search(query.toString(), params);
		return searchRes.getResult();

	}

}
