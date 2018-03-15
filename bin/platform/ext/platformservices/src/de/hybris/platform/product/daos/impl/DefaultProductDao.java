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
package de.hybris.platform.product.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.FlexibleSearchUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of the {@link ProductDao}.
 */
public class DefaultProductDao extends DefaultGenericDao<ProductModel>implements ProductDao
{

	public DefaultProductDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public SearchResult<ProductModel> findProductsByCategory(final CategoryModel category, final int start, final int count)
	{
		validateParameterNotNull(category, "No category specified");

		// search for products. A CategoryProductRelation must exist where the given category is the source and the product is the target.
		final Map params = new HashMap();
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT {p:").append(ProductModel.PK).append("} ");
		stringBuilder.append("FROM {").append(ProductModel._TYPECODE).append(" AS p ");
		stringBuilder.append("JOIN ").append(CategoryConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS l ");
		stringBuilder.append("ON {l:").append(LinkModel.TARGET).append("}={p:").append(ProductModel.PK).append("} } ");

		final Collection<CategoryModel> allSubCategories = category.getAllSubcategories();

		final Collection<CategoryModel> cats = new ArrayList<CategoryModel>();
		cats.add(category);
		cats.addAll(allSubCategories);

		final String inPart = FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(//
				"{l:" + LinkModel.SOURCE + "} IN (?cat)", //
				"cat", "AND", cats, params//
		);//

		stringBuilder.append("WHERE ").append(inPart);
		stringBuilder.append(" ORDER BY {p:name}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
		query.setStart(start);
		query.setCount(count);
		query.setNeedTotal(true);
		query.addQueryParameters(params);

		return getFlexibleSearchService().search(query);
	}

	@Override
	public List<ProductModel> findProducts(final CategoryModel category, final boolean online)
	{
		if (online)
		{
			return findOnlineProductsByCategory(category);
		}
		else
		{
			return findOfflineProductsByCategory(category);
		}
	}

	@Override
	public List<ProductModel> findProductsByCode(final String code)
	{
		validateParameterNotNull(code, "Product code must not be null!");

		return find(Collections.singletonMap(ProductModel.CODE, (Object) code));
	}

	@Override
	public List<ProductModel> findProductsByCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		validateParameterNotNull(catalogVersion, "CatalogVersion must not be null!");

		return find(Collections.singletonMap(ProductModel.CATALOGVERSION, catalogVersion));
	}

	@Override
	public List<ProductModel> findProductsByCode(final CatalogVersionModel catalogVersion, final String code)
	{
		validateParameterNotNull(catalogVersion, "CatalogVersion must not be null!");
		validateParameterNotNull(code, "Product code must not be null!");

		final Map parameters = new HashMap();
		parameters.put(ProductModel.CODE, code);
		parameters.put(ProductModel.CATALOGVERSION, catalogVersion);

		return find(parameters);
	}


	@Override
	public Integer findAllProductsCountByCategory(final CategoryModel category)
	{
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT( {p:" + CategoryModel.PK + "} ) ");
		query.append("FROM {" + ProductModel._TYPECODE + " AS p ");
		query.append("JOIN " + ProductModel._CATEGORYPRODUCTRELATION + "* AS c2pRel ");
		query.append("ON {c2pRel." + Link.TARGET + "}={p:" + CategoryModel.PK + "} } ");
		query.append("WHERE {c2pRel." + Link.SOURCE + "} ");
		final Collection<CategoryModel> allSubCategories = category.getAllSubcategories();
		final List<Class> resultClasses = new ArrayList<Class>();
		resultClasses.add(Integer.class);
		if (allSubCategories.isEmpty())
		{
			query.append(" = ?").append(CategoryModel._TYPECODE);
			final Map<String, Object> params = (Map) Collections.singletonMap(CategoryModel._TYPECODE, category);
			final Integer count = (Integer) doSearch(query.toString(), params, resultClasses).iterator().next();
			return count;
		}
		else
		{
			//see PLA-5243
			final String categoriesParam = "categories";
			query.append(" IN ( ?" + categoriesParam + " )");
			int count = 0;
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>(allSubCategories);
			categoryList.add(category);
			int pageSize = Registry.getCurrentTenantNoFallback().getDataSource().getMaxPreparedParameterCount();
			if (pageSize == -1)
			{
				pageSize = categoryList.size();
			}
			int offset = 0;
			while (offset < categoryList.size())
			{
				final int currentPageEnd = Math.min(categoryList.size(), offset + pageSize);
				final Map<String, Object> params = new HashMap<String, Object>();
				params.put(categoriesParam, categoryList.subList(offset, currentPageEnd));
				final Integer subTotalCount = (Integer) doSearch(query.toString(), params, resultClasses).iterator().next();
				count += subTotalCount.intValue();

				// jump to next page for next turn
				offset += pageSize;
			}
			return Integer.valueOf(count);
		}
	}

	@Override
	public Integer findProductsCountByCategory(final CategoryModel category)
	{
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT( {p:" + CategoryModel.PK + "} ) ");
		query.append("FROM {" + ProductModel._TYPECODE + " AS p ");
		query.append("JOIN " + ProductModel._CATEGORYPRODUCTRELATION + "* AS c2pRel ");
		query.append("ON {c2pRel." + Link.TARGET + "}={p:" + CategoryModel.PK + "} } ");
		query.append("WHERE {c2pRel." + Link.SOURCE + "} ");
		query.append(" = ?").append(CategoryModel._TYPECODE);

		final List<Class> resultClasses = new ArrayList<>();
		resultClasses.add(Integer.class);
		final Map<String, Object> params = (Map) Collections.singletonMap(CategoryModel._TYPECODE, category);
		final Integer count = (Integer) doSearch(query.toString(), params, resultClasses).iterator().next();
		return count;
	}

	private <T> List<T> doSearch(final String query, final Map<String, Object> params, final List<Class> resultClasses)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}
		if (resultClasses != null)
		{
			fQuery.setResultClassList(resultClasses);
		}
		final SearchResult<T> result = getFlexibleSearchService().search(fQuery);
		final List<T> elements = result.getResult();
		return elements;
	}

	@Override
	public List<ProductModel> findOfflineProductsByCategory(final CategoryModel category)
	{
		final StringBuilder query = new StringBuilder();
		query.append("SELECT {ccr:" + LinkModel.TARGET + "} FROM { " + ProductModel._CATEGORYPRODUCTRELATION);
		query.append(" AS ccr JOIN " + ProductModel._TYPECODE + " AS p ON {ccr:" + LinkModel.TARGET + "} = {p:" + ProductModel.PK
				+ "} } ");
		query.append("WHERE {ccr:" + LinkModel.SOURCE + "} = ?" + CategoryModel._TYPECODE);

		query.append(" AND ( {p:" + ProductModel.ONLINEDATE + "} > ?session.user." + UserModel.CURRENTDATE);
		query.append("  OR {p:" + ProductModel.OFFLINEDATE + "} <= ?session.user." + UserModel.CURRENTDATE + " ) ");
		query.append("ORDER BY " + LinkModel.SEQUENCENUMBER + " ASC ");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(CategoryModel._TYPECODE, category);
		params.put(UserModel.CURRENTDATE, new Date());

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		searchQuery.addQueryParameters(params);
		searchQuery.setResultClassList(Collections.singletonList(ProductModel.class));
		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}

	@Override
	public List<ProductModel> findOnlineProductsByCategory(final CategoryModel category)
	{
		final StringBuilder query = new StringBuilder();
		query.append("SELECT {ccr:" + LinkModel.TARGET + "} FROM { " + ProductModel._CATEGORYPRODUCTRELATION);
		query.append(" AS ccr JOIN " + ProductModel._TYPECODE + " AS p ON {ccr:" + LinkModel.TARGET + "} = {p:" + ProductModel.PK
				+ "} } ");
		query.append("WHERE {ccr:" + LinkModel.SOURCE + "} = ?" + CategoryModel._TYPECODE);
		query.append(" AND  ( {p:" + ProductModel.ONLINEDATE + "} IS NULL OR {p:");
		query.append(ProductModel.ONLINEDATE + "} <= ?session.user." + UserModel.CURRENTDATE);
		query.append(" ) AND ( {p:" + ProductModel.OFFLINEDATE + "} IS NULL OR {p:");
		query.append(ProductModel.OFFLINEDATE + "} > ?session.user." + UserModel.CURRENTDATE + " ) ");

		query.append("ORDER BY " + LinkModel.SEQUENCENUMBER + " ASC ");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(CategoryModel._TYPECODE, category);
		params.put(UserModel.CURRENTDATE, new Date());

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		searchQuery.addQueryParameters(params);
		searchQuery.setResultClassList(Collections.singletonList(ProductModel.class));
		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}

}
