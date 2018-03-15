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

import de.hybris.platform.catalog.DuplicatedItemIdentifier;
import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.jalo.TypeViewUtilities;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for the {@link CatalogVersionDao}.
 */
public class DefaultCatalogVersionDao implements CatalogVersionDao
{
	private FlexibleSearchService flexibleSearchService;

	@Override
	public Collection<CatalogVersionModel> findCatalogVersions(final String catalogId, final String catalogVersionName)
	{
		ServicesUtil.validateParameterNotNull(catalogId, "catalog Id must not be null");
		ServicesUtil.validateParameterNotNull(catalogVersionName, "catalog Id must not be null");
		//TODO: result should only contains ONE element, not more
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT {catalogVersion.");
		sql.append(CatalogVersionModel.PK);
		sql.append("} FROM {");
		sql.append(CatalogVersionModel._TYPECODE);
		sql.append(" AS catalogVersion JOIN ");
		sql.append(CatalogModel._TYPECODE);
		sql.append(" as catalog ON {catalog.");
		sql.append(CatalogModel.PK);
		sql.append("}={");
		sql.append(CatalogVersionModel.CATALOG);
		sql.append("} } WHERE {catalog.");
		sql.append(CatalogModel.ID);
		sql.append("}=?id AND {catalogVersion.");
		sql.append(CatalogVersionModel.VERSION);
		sql.append("}=?cv");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql.toString());
		query.addQueryParameter("id", catalogId);
		query.addQueryParameter("cv", catalogVersionName);
		final SearchResult<CatalogVersionModel> result = flexibleSearchService.search(query);

		return result.getResult();
	}

	@Override
	public Collection<CatalogVersionModel> findWritableCatalogVersions(final PrincipalModel principal)
	{
		return getCatalogVersionsFromPrincipalRelation(principal, CatalogVersionModel._PRINCIPAL2WRITEABLECATALOGVERSIONRELATION);
	}

	@Override
	public Collection<CatalogVersionModel> findReadableCatalogVersions(final PrincipalModel principal)
	{
		return getCatalogVersionsFromPrincipalRelation(principal, CatalogVersionModel._PRINCIPAL2READABLECATALOGVERSIONRELATION);
	}

	@Override
	public Collection<CatalogVersionModel> findAllCatalogVersions()
	{
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT {");
		sql.append(CatalogVersionModel.PK);
		sql.append("} FROM {");
		sql.append(CatalogVersionModel._TYPECODE);
		sql.append("}");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql.toString());
		final SearchResult<CatalogVersionModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	private Collection<PrincipalModel> getRelatedPrincipals(final PrincipalModel user)
	{
		final Collection<PrincipalModel> principals = new HashSet<PrincipalModel>(10);
		principals.add(user);
		principals.addAll(user.getAllGroups());
		return principals;
	}

	private Collection<CatalogVersionModel> getCatalogVersionsFromPrincipalRelation(final PrincipalModel principal,
			final String relation)
	{
		ServicesUtil.validateParameterNotNull(principal, "principal must not be null");
		final Collection<PrincipalModel> principals = getRelatedPrincipals(principal);

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT DISTINCT {rel:").append(Link.TARGET).append("} ").append("FROM {").append(relation)
				.append("* AS rel }").append(" WHERE {rel:").append(Link.SOURCE).append("} in (?principals)");

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("principals", principals);
		final SearchResult<CatalogVersionModel> result = getFlexibleSearchService().search(searchQuery.toString(), queryParams);
		return result.getResult();
	}

	@Override
	public Integer findAllProductsCount(final CatalogVersionModel catalogVersion)
	{
		final String sql = "SELECT COUNT( {" + ProductModel.PK + "} ) FROM {" + ProductModel._TYPECODE + "} " + "WHERE {"
				+ ProductModel.CATALOGVERSION + "}=?catalogVersion ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql, Collections.singletonMap("catalogVersion", catalogVersion));
		query.setResultClassList(Arrays.asList(Integer.class));

		final SearchResult<Integer> result = getFlexibleSearchService().search(query);
		return result.getResult().get(0);
	}

	@Override
	public Integer findAllMediasCount(final CatalogVersionModel catalogVersion)
	{
		final String sql = "SELECT COUNT( {" + MediaModel.PK + "} ) FROM {" + MediaModel._TYPECODE + "} " + "WHERE {"
				+ MediaModel.CATALOGVERSION + "}=?catalogVersion ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql, Collections.singletonMap("catalogVersion", catalogVersion));
		query.setResultClassList(Arrays.asList(Integer.class));

		final SearchResult<Integer> result = getFlexibleSearchService().search(query);
		return result.getResult().get(0);
	}

	@Override
	public Integer findAllKeywordsCount(final CatalogVersionModel catalogVersion)
	{
		final String sql = "SELECT COUNT( {" + KeywordModel.PK + "} ) FROM {" + KeywordModel._TYPECODE + "} " + "WHERE {"
				+ KeywordModel.CATALOGVERSION + "}=?catalogVersion ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql, Collections.singletonMap("catalogVersion", catalogVersion));
		query.setResultClassList(Arrays.asList(Integer.class));

		final SearchResult<Integer> result = getFlexibleSearchService().search(query);
		return result.getResult().get(0);
	}

	@Override
	public Integer findAllCategoriesCount(final CatalogVersionModel catalogVersion)
	{
		final String sql = "SELECT COUNT( {" + CategoryModel.PK + "} ) FROM {" + CategoryModel._TYPECODE + "} " + "WHERE {"
				+ CategoryModel.CATALOGVERSION + "}=?catalogVersion ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql, Collections.singletonMap("catalogVersion", catalogVersion));
		query.setResultClassList(Arrays.asList(Integer.class));

		final SearchResult<Integer> result = getFlexibleSearchService().search(query);

		return result.getResult().get(0);
	}

	@Override
	public Collection<DuplicatedItemIdentifier> findDuplicatedIds(final CatalogVersionModel catalogVersionModel)
	{
		final Collection<DuplicatedItemIdentifier> duplicatedItems = new ArrayList<>();
		final TypeViewUtilities utils = new TypeViewUtilities();
		final String duplicatedIdsQuery = utils.generateDuplicateCodesForCatalogVersionQuery();

		final FlexibleSearchQuery query = new FlexibleSearchQuery(duplicatedIdsQuery);
		query.addQueryParameter("cv", catalogVersionModel);
		query.setResultClassList(Arrays.asList(ComposedTypeModel.class, String.class, Integer.class, CatalogVersionModel.class));

		final SearchResult<List<Object>> queryResult = flexibleSearchService.search(query);
		for (final List<Object> row : queryResult.getResult())
		{
			if (row != null)
			{
				final ComposedTypeModel typeModel = (ComposedTypeModel) row.get(0);
				final String duplicatedId = (String) row.get(1);
				final Integer count = (Integer) row.get(2);

				duplicatedItems.add(new DuplicatedItemIdentifier(typeModel.getCode(), duplicatedId, count));
			}
		}

		return duplicatedItems;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}



}
