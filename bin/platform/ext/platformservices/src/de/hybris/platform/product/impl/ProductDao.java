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
package de.hybris.platform.product.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.servicelayer.search.SearchResult;


/**
 * The {@link ProductModel} DAO.
 * 
 * @deprecated since ages - as of release 4.3, please use{@link de.hybris.platform.product.daos.ProductDao}
 */
@Deprecated
public interface ProductDao extends Dao
{
	/**
	 * @deprecated since ages - as of release 4.3, please use
	 *             {@link de.hybris.platform.product.daos.ProductDao#findProductsByCategory(CategoryModel, int, int)}
	 * 
	 *             Returns for the given {@link CategoryModel} and all sub categories all found {@link ProductModel}s.
	 * 
	 * @param category
	 *           the (root) category in which the search for the products starts
	 * @param start
	 *           the start number of the search range. Set this value to 0 for getting all products.
	 * @param count
	 *           the number of elements in the search range. Set this value to -1 for getting all products.
	 * @return a {@link SearchResult} with the found results. A SearchResult is used for the paging (start,count) of the
	 *         elements.
	 */
	@Deprecated
	SearchResult<ProductModel> findAllByCategory(CategoryModel category, int start, int count);

	/**
	 * @deprecated since ages - as of release 4.3, please use
	 *             {@link de.hybris.platform.product.daos.ProductDao#findProductsByCode(String)}
	 * 
	 *             Returns for the given product <code>code</code> the {@link ProductModel}.
	 * 
	 * @param code
	 *           the product <code>code</code>
	 * @return a {@link ProductModel}
	 * @throws IllegalArgumentException
	 *            if the given <code>code</code> is <code>null</code>
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no {@link ProductModel} with the given <code>code</code> was found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if for the given <code>code</code> more than one {@link ProductModel} was found.
	 */
	@Deprecated
	ProductModel findProduct(final String code);

	/**
	 * @deprecated since ages - as of release 4.3, please use
	 *             {@link de.hybris.platform.product.daos.ProductDao#findProductsByCode(CatalogVersionModel, String)}
	 * 
	 *             Returns for the given product <code>code</code> and the given {@link CatalogVersionModel} the
	 *             {@link ProductModel} .
	 * 
	 * @param catalogVersion
	 *           the catalog version
	 * @param code
	 *           the product code
	 * @return a {@link ProductModel}
	 * @throws IllegalArgumentException
	 *            of <code>catalogVersion</code> or <code>code</code> is <code>null</code>
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no {@link ProductModel} with the given <code>code</code> and with the given
	 *            <code>catalogVersion</code> was found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if for the given <code>code</code> ant for the given <code>catalogVersion</code> more than one
	 *            {@link ProductModel} was found.
	 */
	@Deprecated
	ProductModel findProduct(final CatalogVersionModel catalogVersion, final String code);
}
