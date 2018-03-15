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
package de.hybris.platform.product.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;


/**
 * The {@link ProductModel} DAO.
 * 
 * @spring.bean productDao
 */
public interface ProductDao extends Dao
{
	/**
	 * Returns for the given {@link CategoryModel} and all sub categories all found {@link ProductModel}s.
	 * 
	 * @param category
	 *           the (root) category in which the search for the products starts
	 * @param start
	 *           the start number of the search range. Set this value to 0 for getting all products.
	 * @param count
	 *           the number of elements in the search range. Set this value to -1 for getting all products.
	 * @return a {@link SearchResult} with the found results. A SearchResult is used for the paging (start,count) of the
	 *         elements.
	 * @throws IllegalArgumentException
	 *            if <code>category</code> is <code>null</code>
	 */
	SearchResult<ProductModel> findProductsByCategory(CategoryModel category, int start, int count);

	/**
	 * @deprecated since ages - as of release 4.3, please use{@link #findOfflineProductsByCategory(CategoryModel)} or
	 *             {@link #findOnlineProductsByCategory(CategoryModel)}
	 * 
	 *             Returns all online or offline products that belong to the specified category.
	 * 
	 * @param category
	 *           the category the returned products belong to
	 * @param online
	 *           true for online products, or false for offline products
	 * @return all found products that belong to the specified category, or empty list if no product can be found.
	 */
	@Deprecated
	List<ProductModel> findProducts(CategoryModel category, boolean online);

	/**
	 * Returns all online products that belong to the specified category.
	 * 
	 * @param category
	 *           the category the returned products belong to
	 * @return all found products that belong to the specified category, or empty list if no product can be found.
	 */
	List<ProductModel> findOnlineProductsByCategory(CategoryModel category);

	/**
	 * Returns all offline products that belong to the specified category.
	 * 
	 * @param category
	 *           the category the returned products belong to
	 * @return all found products that belong to the specified category, or empty list if no product can be found.
	 */
	List<ProductModel> findOfflineProductsByCategory(CategoryModel category);

	/**
	 * Returns for the given product <code>code</code> the {@link ProductModel} collection.
	 * 
	 * @param code
	 *           the product <code>code</code>
	 * @return a {@link ProductModel}
	 * @throws IllegalArgumentException
	 *            if the given <code>code</code> is <code>null</code>
	 */
	List<ProductModel> findProductsByCode(final String code);

	/**
	 * Returns for the given product <code>catalogVersion</code> the {@link ProductModel} collection.
	 *
	 * @param catalogVersion
	 *           the product <code>catalogVersion</code>
	 * @return a {@link ProductModel}
	 * @throws IllegalArgumentException
	 *            if the given <code>catalogVersion</code> is <code>null</code>
	 */
	List<ProductModel> findProductsByCatalogVersion(final CatalogVersionModel catalogVersion);

	/**
	 * Returns for the given product <code>code</code> and the given {@link CatalogVersionModel} the {@link ProductModel}
	 * .
	 * 
	 * @param catalogVersion
	 *           the catalog version
	 * @param code
	 *           the product code
	 * @return a {@link ProductModel}
	 * @throws IllegalArgumentException
	 *            of <code>catalogVersion</code> or <code>code</code> is <code>null</code>
	 */
	List<ProductModel> findProductsByCode(final CatalogVersionModel catalogVersion, final String code);

	/**
	 * Counts all products of the specific {@link CategoryModel} excluding double linked products. This includes all its
	 * own products and products belonging to any sub category.
	 * 
	 * @return amount of all products of the category.
	 */
	Integer findAllProductsCountByCategory(CategoryModel category);

	/**
	 * Counts all products of the specific {@link CategoryModel}
	 *
	 * @return amount of all products of the category.
	 */
	Integer findProductsCountByCategory(CategoryModel category);
}
