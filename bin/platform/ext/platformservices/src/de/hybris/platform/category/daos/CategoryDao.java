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
package de.hybris.platform.category.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Collection;


/**
 * The DAO for the {@link CategoryModel}.
 * 
 * @spring.bean categoryDao
 */
public interface CategoryDao extends Dao
{

	/**
	 * @deprecated since 4.3, please use {@link #findRootCategoriesByCatalogVersion(CatalogVersionModel)}.
	 * @return all found {@link CategoryModel}s, or empty collection if the {@link CatalogVersionModel} contains no
	 *         categories or is not an active session catalog version.
	 */
	@Deprecated
	Collection<CategoryModel> findRootCategories(CatalogVersionModel catalogVersion);

	/**
	 * Finds all root categories for the specific {@link CatalogVersionModel}.
	 * 
	 * @param catalogVersion
	 *           the {@link CatalogVersionModel}
	 * @return all found {@link CategoryModel}s, or empty collection if the {@link CatalogVersionModel} contains no
	 *         categories or is not an active session catalog version.
	 */
	Collection<CategoryModel> findRootCategoriesByCatalogVersion(CatalogVersionModel catalogVersion);

	/**
	 * @deprecated since 4.3, please use {@link #findCategoriesByCode(CatalogVersionModel, String)}.
	 * @return all found {@link CategoryModel}s, or an empty collection if no category can be found by the
	 *         <code>code</code> or given {@link CatalogVersionModel} is not an active session catalog version.
	 * 
	 */
	@Deprecated
	Collection<CategoryModel> findCategories(CatalogVersionModel catalogVersion, String code);

	/**
	 * Finds all {@link CategoryModel}s that belong to the specific {@link CatalogVersionModel} with the category
	 * <code>code</code>.
	 * 
	 * @param catalogVersion
	 *           the {@link CatalogVersionModel}
	 * @param code
	 *           the category code
	 * @return all found {@link CategoryModel}s, or an empty collection if no category can be found by the
	 *         <code>code</code> or given {@link CatalogVersionModel} is not an active session catalog version.
	 */
	Collection<CategoryModel> findCategoriesByCode(CatalogVersionModel catalogVersion, String code);

	/**
	 * Finds all {@link CategoryModel}s with the specific <code>code</code>.
	 * 
	 * @param code
	 *           the category code
	 * @return all found {@link CategoryModel}s, or an empty collection if no category can be found by the
	 *         <code>code</code> is not an active session catalog version.
	 */
	Collection<CategoryModel> findCategoriesByCode(String code);

	/**
	 * Finds categories by catalog version and product.
	 * 
	 * @param catver
	 *           the catalog version for which search is performed
	 * @param product
	 *           the product for which search is performed
	 * @return the resulting collection of found categories
	 */
	Collection<CategoryModel> findCategoriesByCatalogVersionAndProduct(CatalogVersionModel catver, ProductModel product);
}
