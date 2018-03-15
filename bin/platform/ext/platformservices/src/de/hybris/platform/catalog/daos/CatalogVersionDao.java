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
package de.hybris.platform.catalog.daos;

import de.hybris.platform.catalog.DuplicatedItemIdentifier;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Collection;


/**
 * The {@link CatalogVersionModel} DAO.
 * 
 * @spring.bean catalogVersionDao
 */
public interface CatalogVersionDao extends Dao
{
	/**
	 * Returns for the given <code>catalogId</code> and <code>version</code> a collection with the found
	 * {@link CatalogVersionModel}.
	 * 
	 * @return a collection with the found CatalogVersionModel or empty collection is nothing was found.
	 */
	Collection<CatalogVersionModel> findCatalogVersions(String catalogId, String version);

	/**
	 * Returns all {@link CatalogVersionModel}s which are writable for the given principal. The method takes the group
	 * membership into account.
	 * 
	 * @param principal
	 *           given {@link PrincipalModel}
	 * 
	 */
	Collection<CatalogVersionModel> findWritableCatalogVersions(final PrincipalModel principal);

	/**
	 * Returns all {@link CatalogVersionModel}s that are readable for the given principal. The method takes the group
	 * membership into account.
	 * 
	 * @param principal
	 *           given {@link PrincipalModel}
	 * 
	 */
	Collection<CatalogVersionModel> findReadableCatalogVersions(final PrincipalModel principal);

	/**
	 * Returns all {@link CatalogVersionModel}s defined in the system.
	 * 
	 * @return an empty collection if no catalog version was found
	 */
	Collection<CatalogVersionModel> findAllCatalogVersions();


	/**
	 * Returns the count of {@link CategoryModel }s which are assigned to this <code>CatalogVersionModel</code>.
	 * 
	 * @param catalogVersion
	 *           the {@link CatalogVersionModel}
	 * @return the number of {@link CategoryModel }s which are assigned to this <code>CatalogVersionModel</code>
	 */
	Integer findAllCategoriesCount(final CatalogVersionModel catalogVersion);


	/**
	 * Returns the count of {@link ProductModel}s which are assigned to this <code>CatalogVersionModel</code>.
	 * 
	 * @param catalogVersion
	 *           the {@link CatalogVersionModel}
	 * @return the number of {@link CategoryModel }s which are assigned to this <code>CatalogVersionModel</code>
	 */
	Integer findAllProductsCount(final CatalogVersionModel catalogVersion);


	/**
	 * Returns the count of {@link MediaModel }s which are assigned to this <code>CatalogVersionModel</code>.
	 * 
	 * @param catalogVersion
	 *           the {@link CatalogVersionModel}
	 * @return the number of {@link MediaModel }s which are assigned to this <code>CatalogVersionModel</code>
	 */
	Integer findAllMediasCount(final CatalogVersionModel catalogVersion);



	/**
	 * Returns the count of {@link KeywordModel }s which are assigned to this <code>CatalogVersionModel</code>.
	 * 
	 * @param catalogVersion
	 *           the {@link CatalogVersionModel}
	 * @return the number of {@link KeywordModel }s which are assigned to this <code>CatalogVersionModel</code>
	 */
	Integer findAllKeywordsCount(final CatalogVersionModel catalogVersion);


	/**
	 * Returns collection of duplicated identifiers (and their count) which are assigned to this
	 * <code>CatalogVersionModel</code>
	 * 
	 * @param catalogVersion
	 * @return collection of duplicated item identifiers data objects
	 */
	Collection<DuplicatedItemIdentifier> findDuplicatedIds(final CatalogVersionModel catalogVersion);
}
