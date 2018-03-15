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
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;

import java.util.Collection;


/**
 * 
 * Service provides basic catalog version oriented functionality. Allows fetching catalog version information, managing
 * session catalog versions and determining whether user is eligible for read/write operations within the given catalog
 * version.
 */
public interface CatalogVersionService
{
	/**
	 * Sets the {@link CatalogVersionModel} specified by <code>catalogId</code> and <code>catalogVersionName</code> as
	 * the active CatalogVersion of the current session. Previously set active CatalogVersions are replaced.
	 * 
	 * 
	 * @param catalogId
	 *           the id of the Catalog the CatalogVersion belongs to
	 * @param catalogVersionName
	 *           the name of the Catalog version
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no CatalogVersion with the specified catalog id and version exists
	 * @throws AmbiguousIdentifierException
	 *            if more than one CatalogVersion is found with the specified catalog id and version
	 * @throws IllegalArgumentException
	 *            if <code>catalogId</code> or <code>catalogVersionName</code> is <code>null</code>
	 */
	void setSessionCatalogVersion(String catalogId, String catalogVersionName);

	/**
	 * Sets the specified collection of {@link CatalogVersionModel}s as the active CatalogVersions of the current
	 * session. The previous active session catalog versions are replaced.
	 * 
	 * 
	 * @param catalogVersions
	 *           the catalogVersions to be set as active session CatalogVersions
	 * @throws IllegalArgumentException
	 *            if catalogVersions is <code>null</code>
	 */
	void setSessionCatalogVersions(final Collection<CatalogVersionModel> catalogVersions);

	/**
	 * Adds the {@link CatalogVersionModel} specified by <code>catalogId</code> and <code>catalogVersionName</code> to
	 * the current active Session CatalogVersions.
	 * 
	 * 
	 * @param catalogVersion
	 *           CatalogVersion to add
	 * @throws IllegalArgumentException
	 *            if <code>catalogVersion</code> is <code>null</code>
	 */
	void addSessionCatalogVersion(CatalogVersionModel catalogVersion);

	/**
	 * Returns a collection of the {@link CatalogVersionModel}s which are activated for the current session.
	 * 
	 * @return an empty collection if no active catalog versions for the current session were found.
	 */
	Collection<CatalogVersionModel> getSessionCatalogVersions();

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
	 */
	CatalogVersionModel getCatalogVersion(String catalogId, String catalogVersionName);


	/**
	 * Returns the {@link CatalogVersionModel} in the session with the specified <code>catalogId</code>. This method
	 * expects only one catalog version per catalog in the session, which is true for majority of cases. If this is not
	 * the case, the {@link AmbiguousIdentifierException} will be thrown.
	 * 
	 * @param catalogId
	 *           the Catalog id the CatalogVersion belongs to
	 * @return the catalog version in the session with the specified <code>catalogId</code>, or <code>null</code> if no
	 *         such catalog version was found.
	 * @throws IllegalArgumentException
	 *            if <code>catalogId</code> is null
	 * @throws AmbiguousIdentifierException
	 *            if more than one {@link CatalogVersionModel} can be found in the session with the specified
	 *            <code>catalogId</code>
	 */
	CatalogVersionModel getSessionCatalogVersionForCatalog(String catalogId);

	/**
	 * Returns the {@link CatalogVersionModel}s in the session of the {@link CatalogModel} matching the specified
	 * <code>catalogId</code>.
	 * 
	 * @param catalogId
	 *           the Catalog id the CatalogVersion belongs to
	 * @return the collection of {@link CatalogVersionModel}s or empty collection if no matching catalog versions were
	 *         found in the session.
	 * @throws IllegalArgumentException
	 *            if <code>catalogId</code> is null
	 */
	Collection<CatalogVersionModel> getSessionCatalogVersionsForCatalog(String catalogId);


	/**
	 * Determines whether user can read from given catalogVersion.
	 * 
	 * @param catalogVersion
	 *           target {@link CatalogVersionModel}
	 * @param user
	 *           target {@link UserModel}
	 * 
	 */
	boolean canRead(CatalogVersionModel catalogVersion, UserModel user);

	/**
	 * Determines whether user can write to given catalogVersion.
	 * 
	 * @param catalogVersion
	 *           target {@link CatalogVersionModel}
	 * @param user
	 *           target {@link UserModel}
	 * 
	 */
	boolean canWrite(CatalogVersionModel catalogVersion, UserModel user);


	/**
	 * Returns all writable {@link CatalogVersionModel}s for the given principal.
	 * 
	 * @param principal
	 *           target principal
	 */
	Collection<CatalogVersionModel> getAllWritableCatalogVersions(final PrincipalModel principal);


	/**
	 * Returns all readable {@link CatalogVersionModel}s for the given principal.
	 * 
	 * @param principal
	 *           target principal
	 */
	Collection<CatalogVersionModel> getAllReadableCatalogVersions(final PrincipalModel principal);

	/**
	 * Returns all catalog versions defined in the system. Return empty collection if none was found.
	 */
	Collection<CatalogVersionModel> getAllCatalogVersions();

	/**
	 * Returns for the given <code>T</code> (upper bound CatalogVersionModel) all catalog versions of <code>T</code>
	 * defined in the system
	 * 
	 * @param <T>
	 *           This could be a {@link ClassificationSystemVersionModel} or a {@link CatalogVersionModel}
	 * 
	 * @return an empty collection if no {@link CatalogVersionModel}s or {@link ClassificationSystemVersionModel} were
	 *         found.
	 * 
	 * @throws IllegalArgumentException
	 *            if <code>versionType</code> is <code>null</code>
	 * 
	 */

	<T extends CatalogVersionModel> Collection<T> getAllCatalogVersionsOfType(Class<T> versionType);


	/**
	 * Returs collection of duplicated items in given catalog.
	 * @param catalogVersionModel
	 * @return
	 */
	Collection<DuplicatedItemIdentifier> findDuplicatedIds(final CatalogVersionModel catalogVersionModel);

}
