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
package de.hybris.platform.classification.daos.impl;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.daos.ClassificationSystemVersionDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.Collections;


/**
 * Default implementation for {@link ClassificationSystemVersionDao}.
 */
public class DefaultClassificationSystemVersionDao extends DefaultGenericDao<ClassificationSystemVersionModel> implements
		ClassificationSystemVersionDao
{

	public DefaultClassificationSystemVersionDao()
	{
		super(ClassificationSystemVersionModel._TYPECODE);
	}

	@Override
	public Collection<ClassificationClassModel> findClassesByCode(final ClassificationSystemVersionModel systemVersion,
			final String code)
	{
		final StringBuilder query = new StringBuilder("SELECT {" + ClassificationClassModel.PK + "} FROM ");
		query.append(" {" + ClassificationClassModel._TYPECODE + "} WHERE ");
		query.append(" {" + ClassificationClassModel.CATALOGVERSION + "}=?" + ClassificationClassModel.CATALOGVERSION);
		query.append(" AND {" + ClassificationClassModel.CODE + "}=?" + ClassificationClassModel.CODE);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		searchQuery.addQueryParameter(ClassificationClassModel.CATALOGVERSION, systemVersion);
		searchQuery.addQueryParameter(ClassificationClassModel.CODE, code);

		final SearchResult<ClassificationClassModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Collection<ClassificationAttributeModel> findAttributesByCode(final ClassificationSystemVersionModel systemVersion,
			final String code)
	{
		final StringBuilder query = new StringBuilder("SELECT {" + ClassificationAttributeModel.PK + "} FROM ");
		query.append(" {" + ClassificationAttributeModel._TYPECODE + "} WHERE ");
		query.append(" {" + ClassificationAttributeModel.SYSTEMVERSION + "}=?" + ClassificationAttributeModel.SYSTEMVERSION);
		query.append(" AND {" + ClassificationAttributeModel.CODE + "}=?" + ClassificationAttributeModel.CODE);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		searchQuery.addQueryParameter(ClassificationAttributeModel.SYSTEMVERSION, systemVersion);
		searchQuery.addQueryParameter(ClassificationAttributeModel.CODE, code);

		final SearchResult<ClassificationAttributeModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Collection<ClassificationAttributeValueModel> findAttributeValuesByCode(
			final ClassificationSystemVersionModel systemVersion, final String code)
	{
		final StringBuilder query = new StringBuilder("SELECT {" + ClassificationAttributeValueModel.PK + "} FROM ");
		query.append(" {" + ClassificationAttributeValueModel._TYPECODE + "} WHERE ");
		query.append(" {" + ClassificationAttributeValueModel.SYSTEMVERSION + "}=?"
				+ ClassificationAttributeValueModel.SYSTEMVERSION);
		query.append(" AND {" + ClassificationAttributeValueModel.CODE + "}=?" + ClassificationAttributeValueModel.CODE);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		searchQuery.addQueryParameter(ClassificationAttributeValueModel.SYSTEMVERSION, systemVersion);
		searchQuery.addQueryParameter(ClassificationAttributeValueModel.CODE, code);

		final SearchResult<ClassificationAttributeValueModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Collection<ClassificationAttributeUnitModel> findConvertibleUnits(final ClassificationAttributeUnitModel attributeUnit)
	{
		final String unitType = attributeUnit.getUnitType();
		if (unitType == null)
		{
			return Collections.EMPTY_LIST;
		}
		else
		{
			final StringBuilder query = new StringBuilder("SELECT {" + ClassificationAttributeUnitModel.PK + "} FROM ");
			query.append(" {" + ClassificationAttributeUnitModel._TYPECODE + "} WHERE ");
			query.append(" {" + ClassificationAttributeUnitModel.SYSTEMVERSION + "}=?"
					+ ClassificationAttributeUnitModel.SYSTEMVERSION);
			query.append(" AND {" + ClassificationAttributeUnitModel.UNITTYPE + "}=?" + ClassificationAttributeUnitModel.UNITTYPE);
			query.append(" AND {" + ClassificationAttributeUnitModel.PK + "}<>?" + ClassificationAttributeUnitModel.PK);

			final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
			searchQuery.addQueryParameter(ClassificationAttributeUnitModel.SYSTEMVERSION, attributeUnit.getSystemVersion());
			searchQuery.addQueryParameter(ClassificationAttributeUnitModel.UNITTYPE, unitType);
			searchQuery.addQueryParameter(ClassificationAttributeUnitModel.PK, attributeUnit.getPk());

			final SearchResult<ClassificationAttributeUnitModel> result = getFlexibleSearchService().search(searchQuery);
			return result.getResult();
		}
	}

	@Override
	public Collection<ClassificationAttributeUnitModel> findAttributeUnitsByCode(
			final ClassificationSystemVersionModel systemVersion, final String code)
	{
		final StringBuilder query = new StringBuilder("SELECT {" + ClassificationAttributeUnitModel.PK + "} FROM ");
		query.append(" {" + ClassificationAttributeUnitModel._TYPECODE + "} WHERE ");
		query.append(" {" + ClassificationAttributeUnitModel.SYSTEMVERSION + "}=?" + ClassificationAttributeUnitModel.SYSTEMVERSION);
		query.append(" AND {" + ClassificationAttributeUnitModel.CODE + "}=?" + ClassificationAttributeUnitModel.CODE);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		searchQuery.addQueryParameter(ClassificationAttributeUnitModel.SYSTEMVERSION, systemVersion);
		searchQuery.addQueryParameter(ClassificationAttributeUnitModel.CODE, code);

		final SearchResult<ClassificationAttributeUnitModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Collection<ClassificationAttributeUnitModel> findAttributeUnitsBySystemVersion(
			final ClassificationSystemVersionModel systemVersion)
	{
		final StringBuilder query = new StringBuilder("SELECT {" + ClassificationAttributeUnitModel.PK + "} FROM ");
		query.append(" {" + ClassificationAttributeUnitModel._TYPECODE + "} WHERE ");
		query.append(" {" + ClassificationAttributeUnitModel.SYSTEMVERSION + "}=?" + ClassificationAttributeUnitModel.SYSTEMVERSION);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		searchQuery.addQueryParameter(ClassificationAttributeUnitModel.SYSTEMVERSION, systemVersion);

		final SearchResult<ClassificationAttributeUnitModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Collection<ClassificationAttributeUnitModel> findUnitsOfTypeBySystemVersion(
			final ClassificationSystemVersionModel systemVersion, final String type)
	{
		final StringBuilder query = new StringBuilder("SELECT {" + ClassificationAttributeUnitModel.PK + "} FROM ");
		query.append(" {" + ClassificationAttributeUnitModel._TYPECODE + "} WHERE ");
		query.append(" {" + ClassificationAttributeUnitModel.SYSTEMVERSION + "}=?" + ClassificationAttributeUnitModel.SYSTEMVERSION);
		query.append(" AND {" + ClassificationAttributeUnitModel.UNITTYPE + "}=?" + ClassificationAttributeUnitModel.UNITTYPE);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		searchQuery.addQueryParameter(ClassificationAttributeUnitModel.SYSTEMVERSION, systemVersion);
		searchQuery.addQueryParameter(ClassificationAttributeUnitModel.UNITTYPE, type);

		final SearchResult<ClassificationAttributeUnitModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Collection<String> findUnitTypesBySystemVersion(final ClassificationSystemVersionModel systemVersion)
	{
		final StringBuilder query = new StringBuilder("SELECT DISTINCT {" + ClassificationAttributeUnitModel.UNITTYPE);
		query.append("} FROM {" + ClassificationAttributeUnitModel._TYPECODE + "} WHERE {");
		query.append(ClassificationAttributeUnitModel.SYSTEMVERSION + "}=?" + ClassificationAttributeUnitModel.SYSTEMVERSION);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		searchQuery.addQueryParameter(ClassificationAttributeUnitModel.SYSTEMVERSION, systemVersion);
		searchQuery.setResultClassList(Collections.singletonList(String.class));

		final SearchResult<String> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Collection<ClassificationSystemVersionModel> findSystemVersions(final String systemId, final String systemVersion)
	{
		final StringBuilder sql = new StringBuilder("SELECT {systemVersion." + ClassificationSystemVersionModel.PK + "} ");
		sql.append(" FROM {" + ClassificationSystemVersionModel._TYPECODE + " AS systemVersion ");
		sql.append(" JOIN " + ClassificationSystemModel._TYPECODE + " as system ");
		sql.append(" ON {system." + ClassificationSystemModel.PK + "}={" + ClassificationSystemVersionModel.CATALOG + "} } ");
		sql.append(" WHERE {system." + ClassificationSystemModel.ID + "}=?id ");
		sql.append(" AND {systemVersion." + ClassificationSystemModel.VERSION + "}=?sv");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql.toString());
		query.addQueryParameter("id", systemId);
		query.addQueryParameter("sv", systemVersion);

		final SearchResult<ClassificationSystemVersionModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

}
