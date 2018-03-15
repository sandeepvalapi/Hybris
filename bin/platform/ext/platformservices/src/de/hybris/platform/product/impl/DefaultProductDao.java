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
package de.hybris.platform.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @deprecated since ages - as of release 4.3, please use{@link de.hybris.platform.product.daos.impl.DefaultProductDao}
 * 
 *             Default implementation of the {@link ProductDao}.
 */
@Deprecated
@SuppressWarnings("deprecation")
public class DefaultProductDao extends AbstractItemDao implements ProductDao
{
	//TODO: should this method really use all subcategories
	@Override
	public SearchResult<ProductModel> findAllByCategory(final CategoryModel category, final int start, final int count)
	{
		validateParameterNotNull(category, "No category specified");
		// search for products. A CategoryProductRelation must exist where the given category is the source and the product is the target.
		final Map params = new HashMap();
		final StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT {p:").append(ProductModel.PK).append("} ");
		queryString.append("FROM {").append(ProductModel._TYPECODE).append(" AS p ");
		queryString.append("JOIN ").append(CategoryConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS l ");
		queryString.append("ON {l:").append(LinkModel.TARGET).append("}={p:").append(ProductModel.PK).append("} } ");
		queryString.append("WHERE ").append("{l:").append(LinkModel.SOURCE).append("}");
		queryString.append(" IN ( ?cat ");

		params.put("cat", category);
		final Collection<CategoryModel> allSubCategories = category.getAllSubcategories();
		if (!allSubCategories.isEmpty())
		{
			params.put("allSubCategories", allSubCategories);
			queryString.append(", ?allSubCategories ");
		}
		queryString.append(") ");
		queryString.append(" ORDER BY {p:name}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.setStart(start);
		query.setCount(count);
		query.setNeedTotal(true);
		query.addQueryParameters(params);

		return search(query);
	}

	@Override
	public ProductModel findProduct(final String code)
	{
		validateParameterNotNull(code, "Product code must not be null!");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(//
				"SELECT {" + ProductModel.PK + "} " //
						+ "FROM {" + ProductModel._TYPECODE + "} " //
						+ "WHERE {" + ProductModel.CODE + "}=?code");
		query.addQueryParameter(ProductModel.CODE, code);
		final SearchResult<ProductModel> result = getFlexibleSearchService().search(query);
		final int resultCount = result.getTotalCount();
		if (resultCount == 0)
		{
			throw new UnknownIdentifierException("Product with code '" + code + "' not found!");
		}
		else if (resultCount > 1)
		{
			throw new AmbiguousIdentifierException("Product code '" + code + "' is not unique, " + resultCount + " products found!");
		}
		return result.getResult().get(0);
	}

	@Override
	public ProductModel findProduct(final CatalogVersionModel catalogVersion, final String code)
	{
		validateParameterNotNull(catalogVersion, "catalogVersion must not be null!");
		validateParameterNotNull(code, "Code must not be null!");
		final FlexibleSearchQuery query = new FlexibleSearchQuery( //
				"SELECT {" + ProductModel.PK + "} " //
						+ "FROM {" + ProductModel._TYPECODE + "} " //
						+ "WHERE {" + ProductModel.CODE + "}=?code " //
						+ "AND {" + ProductModel.CATALOGVERSION + "}=?catalogVersion");
		query.addQueryParameter(ProductModel.CODE, code);
		query.addQueryParameter(ProductModel.CATALOGVERSION, catalogVersion);
		final SearchResult<ProductModel> result = getFlexibleSearchService().search(query);
		final List<ProductModel> products = result.getResult();

		if (products.isEmpty())
		{
			throw new UnknownIdentifierException("Product with code '" + code + "' and CatalogVersion '"
					+ catalogVersion.getCatalog().getId() + "." + catalogVersion.getVersion() + "' not found!");
		}
		else if (products.size() > 1)
		{
			throw new AmbiguousIdentifierException("Code '" + code + "' and CatalogVersion '" + catalogVersion.getCatalog().getId()
					+ "." + catalogVersion.getVersion() + "' are not unique. " + products.size() + " products found!");
		}
		return products.get(0);
	}
}
