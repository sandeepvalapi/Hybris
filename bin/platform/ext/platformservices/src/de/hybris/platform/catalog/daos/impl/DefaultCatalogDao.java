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

import de.hybris.platform.catalog.daos.CatalogDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collection;
import java.util.Collections;


/**
 * Default implementation of the {@link CatalogDao}.
 */
public class DefaultCatalogDao extends DefaultGenericDao<CatalogModel> implements CatalogDao
{

	public DefaultCatalogDao()
	{
		super(CatalogModel._TYPECODE);
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public Collection<CatalogModel> findCatalogs(final String id)
	{

		if (id == null)
		{
			return findAllCatalogs();
		}
		else
		{
			return Collections.singletonList(findCatalogById(id));
		}
	}

	@Override
	public Collection<CatalogModel> findAllCatalogs()
	{
		return find(SortParameters.singletonAscending(CatalogModel.ID));
	}

	@Override
	public CatalogModel findCatalogById(final String id)
	{
		ServicesUtil.validateParameterNotNull(id, "Catalog id must not be null");

		final Collection<CatalogModel> catalogs = find(Collections.singletonMap(CatalogModel.ID, (Object) id));
		ServicesUtil.validateIfSingleResult(catalogs, "No catalog with given id [" + id + "] was found",
				"More than one catalog with given id [" + id + "] was found");
		return catalogs.iterator().next();
	}

	@Override
	public Collection<CatalogModel> findDefaultCatalogs()
	{
		return find(Collections.<String, Object> singletonMap(CatalogModel.DEFAULTCATALOG, Boolean.TRUE),
				SortParameters.singletonDescending(CatalogModel.CREATIONTIME));
	}



}
