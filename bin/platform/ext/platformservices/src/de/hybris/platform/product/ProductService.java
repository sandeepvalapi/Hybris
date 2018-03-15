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
package de.hybris.platform.product;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;


/**
 * Service to read and update {@link ProductModel ProductModel}s.
 * 
 * @spring.bean productService
 */
public interface ProductService
{
	/**
	 * @deprecated since ages - as of release 4.3, please use{@link #getProductForCode(String)}
	 * 
	 *             Returns the Product with the specified code. As default the search uses the current session user, the
	 *             current session language and the current active catalog versions (which are stored at the session in
	 *             the attribute {@link de.hybris.platform.catalog.constants.CatalogConstants#SESSION_CATALOG_VERSIONS
	 *             SESSION_CATALOG_VERSIONS}). For modifying the search session context see
	 *             {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery FlexibleSearchQuery}.
	 * 
	 * @param code
	 *           the code of the Product
	 * @return the Product with the specified code.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no Product with the specified code is found
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one Product with the specified code is found
	 * @throws IllegalArgumentException
	 *            if parameter code is <code>null</code>
	 */
	@Deprecated
	ProductModel getProduct(String code);

	/**
	 * Returns the Product with the specified code. As default the search uses the current session user, the current
	 * session language and the current active catalog versions (which are stored at the session in the attribute
	 * {@link de.hybris.platform.catalog.constants.CatalogConstants#SESSION_CATALOG_VERSIONS SESSION_CATALOG_VERSIONS}).
	 * For modifying the search session context see {@link de.hybris.platform.servicelayer.search.FlexibleSearchQuery
	 * FlexibleSearchQuery}.
	 * 
	 * @param code
	 *           the code of the Product
	 * @return the Product with the specified code.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no Product with the specified code is found
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one Product with the specified code is found
	 * @throws IllegalArgumentException
	 *            if parameter code is <code>null</code>
	 */
	ProductModel getProductForCode(String code);

	/**
	 * @deprecated since ages - as of release 4.3, please use
	 *             {@link de.hybris.platform.product.ProductService#getProductForCode(CatalogVersionModel, String)},
	 *             however this method does not use any restrictions
	 * 
	 *             Returns the Product with the specified code belonging to the specified CatalogVersion. If the
	 *             specified catalog version is not in the search restriction for the current user this method won't find
	 *             any product (but it still exists). The admin user searches without any restrictions. As default the
	 *             search uses the current session user, the current session language and the current active catalog
	 *             versions (which are stored at the session in the attribute
	 *             {@link de.hybris.platform.catalog.constants.CatalogConstants#SESSION_CATALOG_VERSIONS
	 *             SESSION_CATALOG_VERSIONS}). If there is no active catalog version specified CatalogVersion is set as
	 *             active catalog version.
	 * 
	 * @param catalogVersion
	 *           the CatalogVersion of the Product
	 * @param code
	 *           the code of the Product
	 * @return the Product matching the specified code and CatalogVersion.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no Product with the specified code and Catalog is found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one Product with the specified code and Catalog is found
	 */
	@Deprecated
	ProductModel getProduct(CatalogVersionModel catalogVersion, String code);

	/**
	 * @deprecated since ages - as of release 4.3, TODO: please provide information where there is replacement
	 * 
	 *             Returns for the given <code>product</code> the first found {@link UnitModel} from the
	 *             PriceInformations of the product.
	 * 
	 * @param product
	 *           the {@link ProductModel}
	 * @return {@link ProductModel#UNIT} if no PriceInformations is available at the {@link ProductModel}
	 * @throws IllegalArgumentException
	 *            if <code>product</code> is <code>null</code>.
	 * @throws de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
	 *            if no {@link UnitModel} was found for the given <code>product</code>
	 */
	@Deprecated
	UnitModel getOrderableUnit(ProductModel product);

	/**
	 * 
	 * @deprecated since ages - as of release 4.3, please use{@link de.hybris.platform.product.UnitService#getUnitForCode(String)}
	 *             Returns the Unit with the specified code.
	 * 
	 * @param code
	 *           the code of the Unit
	 * @return the Unit with the specified code
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no Product with the specified code and Catalog is found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one Product with the specified code and Catalog is found
	 */
	@Deprecated
	UnitModel getUnit(String code);

	/**
	 * @deprecated since ages - as of release 4.3, please use{@link ProductService#getProductsForCategory(CategoryModel)} Returns all
	 *             Products belonging to the specified Category as a List. For the search the current session active
	 *             catalog versions of the current user are used.
	 * 
	 * @param category
	 *           the category the returned Products belong to
	 * @return a list of Products which belong to the specified Category
	 * @throws IllegalArgumentException
	 *            if parameter category is null
	 */
	@Deprecated
	List<ProductModel> getProducts(CategoryModel category);

	/**
	 * Returns all Products belonging to the specified Category as a List. For the search the current session active
	 * catalog versions of the current user are used.
	 * 
	 * @param category
	 *           the category the returned Products belong to
	 * @return a list of Products which belong to the specified Category
	 * @throws IllegalArgumentException
	 *            if parameter category is null
	 */
	List<ProductModel> getProductsForCategory(CategoryModel category);

	/**
	 * Returns all online products that belong to the specified category.
	 * 
	 * @param category
	 *           the category the returned products belong to
	 * @return all found products that belong to the specified category, or empty collection if no product can be found.
	 * @throws IllegalArgumentException
	 *            if parameter category is null
	 */
	List<ProductModel> getOnlineProductsForCategory(CategoryModel category);

	/**
	 * Returns all offline products that belong to the specified category.
	 * 
	 * @param category
	 *           the category the returned products belong to
	 * @return all found products that belong to the specified category, or empty collection if no product can be found.
	 * @throws IllegalArgumentException
	 *            if parameter category is null
	 */
	List<ProductModel> getOfflineProductsForCategory(CategoryModel category);

	/**
	 * @deprecated since ages - as of release 4.3, please use{@link ProductService#getProductsForCategory(CategoryModel, int, int)}
	 * 
	 *             Returns the specified range of Products belonging to the specified Category as a List. For the search
	 *             the current session active catalog versions of the current user are used.
	 * 
	 * @param category
	 *           the category the returned Products belong to
	 * @param start
	 *           index position of the first Product, which will be included in the returned List
	 * @param count
	 *           number of Products which will be returned in the List
	 * @return a list of Products with belong to the specified Category
	 * @throws IllegalArgumentException
	 *            if parameter category is null
	 */
	@Deprecated
	SearchResult<ProductModel> getProducts(CategoryModel category, int start, int count);

	/**
	 * Returns the specified range of Products belonging to the specified Category as a List. For the search the current
	 * session active catalogversions of the current user are used.
	 * 
	 * @param category
	 *           the category the returned Products belong to
	 * @param start
	 *           index position of the first Product, which will be included in the returned List
	 * @param count
	 *           number of Products which will be returned in the List
	 * @return a list of Products with belong to the specified Category
	 * @throws IllegalArgumentException
	 *            if parameter category is null
	 */
	List<ProductModel> getProductsForCategory(CategoryModel category, int start, int count);

	/**
	 * Counts all products of the specific {@link CategoryModel}.
	 * 
	 * @param category
	 *           the {@link CategoryModel}
	 * @return amount of all products of the category
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code>.
	 */
	Integer getProductsCountForCategory(CategoryModel category);

	/**
	 * Counts all products of the specific {@link CategoryModel} excluding double linked products. This includes all its
	 * own products and products belonging to any sub category.
	 *
	 * @param category
	 *           the {@link CategoryModel}
	 * @return amount of all products of the category
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code>.
	 */
	Integer getAllProductsCountForCategory(CategoryModel category);

	/**
	 * Checks whether or not the specific {@link CategoryModel} or any of its sub-categories contains at least one
	 * product.
	 * 
	 * @param category
	 *           the {@link CategoryModel}
	 * @return true if at least one product belong the the {@link CategoryModel} or any of its sub-categories, false
	 *         otherwise.
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code>.
	 */
	boolean containsProductsForCategory(CategoryModel category);

	/**
	 * Returns the Product with the specified code belonging to the specified CatalogVersion.
	 * 
	 * @param catalogVersion
	 *           the CatalogVersion of the Product
	 * 
	 * @param code
	 *           the code of the Product
	 * 
	 * @return the Product matching the specified code and CatalogVersion.
	 * 
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no Product with the specified code and Catalog is found.
	 * 
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one Product with the specified code and Catalog is found
	 */
	ProductModel getProductForCode(CatalogVersionModel catalogVersion, String code);

	/**
	 * Returns all {@link ProductModel} list belonging to the specified {@link CatalogVersionModel}
	 * 
	 * @param catalogVersion
	 *           the CatalogVersion of the Products
	 * @return Product list belonging to the CatalogVersion
	 */
	List<ProductModel> getAllProductsForCatalogVersion(CatalogVersionModel catalogVersion);
}
