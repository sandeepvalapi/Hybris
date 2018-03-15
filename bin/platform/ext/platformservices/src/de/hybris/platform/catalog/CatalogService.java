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
package de.hybris.platform.catalog;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;

import java.util.Collection;
import java.util.Set;


/**
 * Provides access to Catalogs, CatalogVersions and Session CatalogVersions
 * 
 * @spring.bean catalogService
 */
public interface CatalogService
{
	/**
	 * Sets the {@link CatalogVersionModel} specified by <code>catalogId</code> and <code>catalogVersionName</code> as
	 * the active CatalogVersion of the current session. Previous set active CatalogVersions are replaced.
	 * 
	 * @param catalogId
	 *           the id of the Catalog the CatalogVersion belongs to
	 * @param catalogVersionName
	 *           the version of the Catalog version
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no CatalogVersion with the specified catalog id and version exists
	 * @throws AmbiguousIdentifierException
	 *            if more than one CatalogVersion is found with the specified catalog id and version
	 * @throws IllegalArgumentException
	 *            if <code>catalogId</code> or <code>catalogVersionName</code> is <code>null</code>
	 * @deprecated since ages - use{@link CatalogVersionService#setSessionCatalogVersion(String, String)} instead.
	 */
	@Deprecated
	void setSessionCatalogVersion(String catalogId, String catalogVersionName);

	/**
	 * Sets the specified Set of {@link CatalogVersionModel}s as the active CatalogVersions of the current session. The
	 * previous active Session CatalogVersions are replaced.
	 * 
	 * @param catalogVersions
	 *           the catalogVersions to be set as active session CatalogVersions
	 * @throws IllegalArgumentException
	 *            if catalogVersions is <code>null</code>
	 * @deprecated since ages - Use{@link CatalogVersionService#setSessionCatalogVersions(Collection)}
	 */
	@Deprecated
	void setSessionCatalogVersions(final Set<CatalogVersionModel> catalogVersions);

	/**
	 * Adds the {@link CatalogVersionModel} specified by <code>catalogId</code> and <code>catalogVersionName</code> to
	 * the current active Session CatalogVersions.
	 * 
	 * @param catalogId
	 *           the id of the Catalog the CatalogVersion belongs to
	 * @param catalogVersionName
	 *           the version of the Catalog version
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no CatalogVersion with the specified catalog id and version exists
	 * @throws AmbiguousIdentifierException
	 *            if more than one CatalogVersion is found with the specified catalog id and version
	 * @throws IllegalArgumentException
	 *            if <code>catalogId</code> or <code>catalogVersionName</code> is <code>null</code>
	 * @deprecated since ages - Use{@link CatalogVersionService#addSessionCatalogVersion(CatalogVersionModel)}
	 */
	@Deprecated
	void addSessionCatalogVersion(String catalogId, String catalogVersionName);

	/**
	 * Returns a Set of the {@link CatalogVersionModel}s which are activated for the current session.
	 * 
	 * @return an empty collection if no active catalog versions for the current session were found.
	 * @deprecated since ages - Use{@link CatalogVersionService#getSessionCatalogVersions()}
	 */
	@Deprecated
	Set<CatalogVersionModel> getSessionCatalogVersions();

	/**
	 * Returns the {@link CatalogVersionModel} with the specified <code>catalogId</code> and
	 * <code>catalogVersionName</code>.
	 * 
	 * @param catalogId
	 *           the id for the catalog
	 * @param catalogVersionName
	 *           the version string for the catalog version
	 * @return the CatalogVersion with the specified catalog id and version
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no CatalogVersion with the specified catalog id and version exists
	 * @throws AmbiguousIdentifierException
	 *            if more than one CatalogVersion is found with the specified catalog id and version
	 * @throws IllegalArgumentException
	 *            if <code>catalogId</code> or <code>catalogVersionName</code> is <code>null</code>
	 * @deprecated since ages - Use{@link CatalogVersionService#getCatalogVersion(String, String)}
	 */
	@Deprecated
	CatalogVersionModel getCatalogVersion(String catalogId, String catalogVersionName);

	/**
	 * Returns all {@link CatalogModel}s.
	 * 
	 * @return an empty collection if no CatalogModel were found.
	 */
	Collection<CatalogModel> getAllCatalogs();

	/**
	 * Returns for the given <code>T</code> (upper bound CatalogModel) all catalogs of <code>T</code> defined in the
	 * system
	 * 
	 * @param <T>
	 *           This could be a {@link ClassificationSystemModel} or a {@link CatalogModel}
	 * 
	 * @return an empty collection if no {@link ClassificationSystemModel}s or {@link CatalogModel} were found.
	 * 
	 * @throws IllegalArgumentException
	 *            if <code>catalogType</code> is <code>null</code>
	 * 
	 */
	<T extends CatalogModel> Collection<T> getAllCatalogsOfType(Class<T> catalogType);

	/**
	 * Returns the catalog with the specified <code>id</code>.
	 * 
	 * @return the catalog with the specified id.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no Catalog with the specified <code>id</code> is found
	 * @throws AmbiguousIdentifierException
	 *            if more than one Catalog is found with the specified <code>id</code>
	 * @throws IllegalArgumentException
	 *            if <code>id</code> is <code>null</code>
	 * @deprecated since ages - Use{@link #getCatalogForId(String)} instead.
	 */
	@Deprecated
	CatalogModel getCatalog(final String id);

	/**
	 * Returns the catalog with the specified <code>id</code>.
	 * 
	 * @return the catalog with the specified id.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no Catalog with the specified <code>id</code> is found
	 * @throws AmbiguousIdentifierException
	 *            if more than one Catalog is found with the specified <code>id</code>
	 * @throws IllegalArgumentException
	 *            if <code>id</code> is <code>null</code>
	 */
	CatalogModel getCatalogForId(final String id);

	/**
	 * Returns the <b>default<b> catalog basing on {@link CatalogModel#DEFAULTCATALOG} property. If multiple catalogs the
	 * one created the latest will be returned.
	 * 
	 * @return default {@link CatalogModel}, or null in case no default catalog was found.
	 * 
	 * @throws {@link AmbiguousIdentifierException} if there exists more then one default catalog in the data base.
	 */
	CatalogModel getDefaultCatalog();


	/**
	 * Returns the {@link CatalogVersionModel} in the session with the specified <code>catalogId</code>
	 * 
	 * @param catalogId
	 *           the Catalog id the CatalogVersion belongs to
	 * @return the catalog version in the session with the specified <code>catalogId</code>, or <code>null</code> if not
	 *         found
	 * @throws IllegalArgumentException
	 *            if <code>catalogId</code> is null
	 * @throws AmbiguousIdentifierException
	 *            if more than one {@link CatalogVersionModel} can be found in the session with the specified
	 *            <code>catalogId</code>
	 * @deprecated since ages - Use{@link CatalogVersionService#getSessionCatalogVersionForCatalog(String)}
	 */
	@Deprecated
	CatalogVersionModel getSessionCatalogVersion(String catalogId);

}
