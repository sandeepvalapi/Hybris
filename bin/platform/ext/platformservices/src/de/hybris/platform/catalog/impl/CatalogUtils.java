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

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.Collection;


/**
 * Some helper methods for the catalog service.
 */
public final class CatalogUtils
{
	/**
	 * Private constructor for avoiding instantiation.
	 */
	private CatalogUtils()
	{
		//empty
	}

	/**
	 * Returns the full qualified catalog/catalogversion name.
	 * 
	 * @param catalogVersion
	 *           the catalog version
	 * @return as String: <i>CatalogID.CatalogVersion</i>
	 * @throws IllegalArgumentException
	 *            if <code>catalogVersion<code> is null.
	 */
	public static String getFullCatalogVersionName(final CatalogVersionModel catalogVersion)
	{
		validateParameterNotNull(catalogVersion, "Parameter 'catalogVersion' must not be null!");
		final StringBuilder builder = new StringBuilder();

		builder.append(catalogVersion.getCatalog().getId());
		builder.append(".");
		builder.append(catalogVersion.getVersion());
		return builder.toString();
	}

	/**
	 * Returns for the given collection of {@link CatalogVersionModel}s the
	 * {@link #getFullCatalogVersionName(CatalogVersionModel)} of each element, separated by ','
	 * 
	 * @param catalogVersions
	 *           the collection with the catalog versions
	 * @return an empty string if the collection contains nothing.
	 * @throws IllegalArgumentException
	 *            if <code>catalogVersions<code> is null.
	 */
	public static String getCatalogVersionsString(final Collection<CatalogVersionModel> catalogVersions)
	{
		validateParameterNotNull(catalogVersions, "Parameter 'catalogVersions' must not be null!");

		if (catalogVersions.isEmpty())
		{
			return "";
		}
		final StringBuilder builder = new StringBuilder();
		for (final CatalogVersionModel catalogVersion : catalogVersions)
		{
			builder.append(getFullCatalogVersionName(catalogVersion));
			builder.append(", ");
		}
		return builder.substring(0, builder.length() - 2);
	}
}
