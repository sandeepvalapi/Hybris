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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.daos.CatalogDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link CatalogService}.
 */
public class DefaultCatalogService extends AbstractBusinessService implements CatalogService
{
	private CatalogDao catalogDao;
	private CatalogVersionService catalogVersionService;

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public void setSessionCatalogVersion(final String catalogId, final String catalogVersionName)
	{
		//delegating deprecated method
		catalogVersionService.setSessionCatalogVersion(catalogId, catalogVersionName);
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public void setSessionCatalogVersions(final Set<CatalogVersionModel> catalogVersions)
	{
		//delegating deprecated method
		catalogVersionService.setSessionCatalogVersions(catalogVersions);
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public Set<CatalogVersionModel> getSessionCatalogVersions()
	{
		//delegating deprecated method
		return new HashSet<CatalogVersionModel>(catalogVersionService.getSessionCatalogVersions());
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public void addSessionCatalogVersion(final String catalogId, final String catalogVersionName)
	{
		//delegating deprecated method
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogId, catalogVersionName);
		catalogVersionService.addSessionCatalogVersion(catalogVersion);
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public CatalogVersionModel getCatalogVersion(final String catalogId, final String catalogVersionName)
	{
		//delegating deprecated method
		return catalogVersionService.getCatalogVersion(catalogId, catalogVersionName);
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public CatalogVersionModel getSessionCatalogVersion(final String catalogId)
	{
		//delegating deprecated method
		return catalogVersionService.getSessionCatalogVersionForCatalog(catalogId);
	}

	@Override
	public Collection<CatalogModel> getAllCatalogs()
	{
		return catalogDao.findAllCatalogs();
	}

	@Override
	public <T extends CatalogModel> Collection<T> getAllCatalogsOfType(final Class<T> catalogType)
	{

		validateParameterNotNull(catalogType, "Parameter 'catalogType' must not be null!");

		final Collection<CatalogModel> catalogs = getAllCatalogs();

		if (catalogs.isEmpty())
		{
			return Collections.EMPTY_LIST;
		}

		final Collection<T> ret = new ArrayList(catalogs.size());
		for (final CatalogModel c : catalogs)
		{
			if (catalogType.isInstance(c))
			{
				ret.add((T) c);
			}
		}
		return ret;
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public CatalogModel getCatalog(final String id)
	{
		return getCatalogForId(id);
	}

	@Override
	public CatalogModel getCatalogForId(final String id)
	{
		validateParameterNotNull(id, "Parameter 'id' must not be null!");
		return catalogDao.findCatalogById(id);
	}

	@Override
	public CatalogModel getDefaultCatalog()
	{
		final Collection<CatalogModel> defaultCatalogs = catalogDao.findDefaultCatalogs();
		if (defaultCatalogs.size() > 1)
		{
			throw new AmbiguousIdentifierException("More than one default catalog was found!");
		}
		if (defaultCatalogs.isEmpty())
		{
			return null;
		}
		return defaultCatalogs.iterator().next();
	}

	@Required
	public void setCatalogDao(final CatalogDao catalogDao)
	{
		this.catalogDao = catalogDao;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}



}
