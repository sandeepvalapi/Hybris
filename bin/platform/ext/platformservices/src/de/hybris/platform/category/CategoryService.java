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
package de.hybris.platform.category;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.category.strategies.CategoryPrincipalStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;

import java.util.Collection;
import java.util.List;


/**
 * Provides methods for working with {@link CategoryModel}. Please be aware that since CategoryModel is catalog version
 * aware, all methods of this service will work according to the active session catalog versions (see
 * {@link CatalogVersionService#setSessionCatalogVersions(Collection)} for more details). So in consequence, the methods
 * may return different results for different session users. If required, the following example shows how to temporarily
 * switch these active catalog versions without changing the enclosing callers session context:
 * 
 * <pre>
 * SessionService sessionService = ...;
 * CatalogVersionService catalogVersionService = ...;
 * CatalogVersionModel myCatalogVersion = ...;
 * 
 * Collection&lt;CategoryModel&gt; rootCategories = (Collection&lt;CategoryModel&gt;) sessionService
 * 		.executeInLocalView(new SessionExecutionBody()
 * 		{
 * 			public Object execute()
 * 			{
 * 				catalogVersionService.setSessionCatalogVersions(Collections.singleton(myCatalogVersion));
 * 
 * 				return categoryService.getRootCategoriesForCatalogVersion(myCatalogVersion);
 * 			}
 * 		});
 * </pre>
 * 
 * @spring.bean categoryService
 * @see CatalogVersionService
 */
public interface CategoryService
{

	/**
	 * @deprecated since 4.3, please use {@link #getRootCategoriesForCatalogVersion(CatalogVersionModel)}.
	 * @return the found {@link CategoryModel}s with the specified <code>code</code>.
	 */
	@Deprecated
	Collection<CategoryModel> getRootCategories(CatalogVersionModel catalogVersion);

	/**
	 * Retrieves the root {@link CategoryModel}s of the specified {@link CatalogVersionModel}.
	 * 
	 * @return all found {@link CategoryModel}s or an empty collection if no root categories can be found
	 * @throws IllegalArgumentException
	 *            if the <code>catalogVersion</code> is <code>null</code>.
	 */
	Collection<CategoryModel> getRootCategoriesForCatalogVersion(CatalogVersionModel catalogVersion);

	/**
	 * @deprecated since 4.3, please use {@link #getCategoryForCode(String)} or {@link #getCategoriesForCode(String)}.
	 *             Retrieves the first found {@link CategoryModel} for the given <code>code</code>.
	 * @param code
	 *           the code of the to be found {@link CategoryModel}
	 * @return the found {@link CategoryModel} or <code>null</code> if no category with this code exists
	 */
	@Deprecated
	CategoryModel getCategory(String code);

	/**
	 * Retrieves the {@link CategoryModel} with the specific <code>code</code>.
	 * 
	 * @param code
	 *           the code of the to be found {@link CategoryModel}
	 * @return found {@link CategoryModel}
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no category with the specified code can be found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one category with the specified <code>code</code> and <code>catalogVersion</code> was
	 *            found.
	 * @throws IllegalArgumentException
	 *            if parameter <code>code</code> is <code>null</code>.
	 */
	CategoryModel getCategoryForCode(String code);

	/**
	 * Retrieves for the given <code>code</code> all found {@link CategoryModel}s.
	 * 
	 * @param code
	 *           the code of the category.
	 * @return all found {@link CategoryModel}s or an empty collection if no such category exists
	 * @see CatalogVersionService#setSessionCatalogVersions(Collection)
	 * @see CatalogVersionService#getSessionCatalogVersions()
	 * @see CatalogVersionService#addSessionCatalogVersion(CatalogVersionModel)
	 */
	Collection<CategoryModel> getCategoriesForCode(String code);

	/**
	 * @deprecated since 4.3, please use {@link #getCategoryForCode(CatalogVersionModel, String)}.
	 * @return the found {@link CategoryModel} with the specified <code>code</code>.
	 */
	@Deprecated
	CategoryModel getCategory(CatalogVersionModel catalogVersion, String code);

	/**
	 * Retrieves the {@link CategoryModel} with the specified <code>code</code> which belonging to the specified
	 * <code>catalogVersion</code>.
	 * 
	 * @param catalogVersion
	 *           the {@link CatalogVersionModel} where the returned category should be found
	 * @param code
	 *           the code of the {@link CategoryModel}
	 * @return the {@link CategoryModel} with the specified <code>code</code> and belonging to the
	 *         {@link CatalogVersionModel}
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no category with the specified code and catalogVersion can be found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one category with the specified <code>code</code> and <code>catalogVersion</code> was
	 *            found.
	 * @throws IllegalArgumentException
	 *            if parameter <code>code</code> or <code>catalogVersion</code> is <code>null</code>.
	 */
	CategoryModel getCategoryForCode(CatalogVersionModel catalogVersion, String code);

	/**
	 * @deprecated since 4.3, please use {@link #getPathForCategory(CategoryModel)}. Returns the path from the given
	 *             {@link CategoryModel} down to the root category. <b>This method should be used only in cases without
	 *             multiple super-categories; otherwise it cannot be predicted which path is chosen!</b><br/>
	 * @param category
	 *           the {@link CategoryModel}
	 * @return the first found path for this category
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code>.
	 */
	@Deprecated
	List<CategoryModel> getPath(CategoryModel category);

	/**
	 * Gets the category path for product.
	 * 
	 * @param product
	 *           the product
	 * @param includeOnlyCategories
	 *           classes which limits resulting path
	 * @return the category path for product
	 */
	List<CategoryModel> getCategoryPathForProduct(ProductModel product, Class... includeOnlyCategories);

	/**
	 * Returns the path from root category to the given {@link CategoryModel}. To get all super-categories use
	 * {@link CategoryModel#getSupercategories()}.
	 * 
	 * @param category
	 *           the {@link CategoryModel}
	 * @return the found path for this category
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code>.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one path was found.
	 */
	List<CategoryModel> getPathForCategory(CategoryModel category);

	/**
	 * Returns <b>all</b> paths for this {@link CategoryModel}. Each path is a list of all categories in the path from
	 * root to this category.
	 * 
	 * @param category
	 *           the (sub){@link CategoryModel}
	 * @return all paths for this {@link CategoryModel}, or empty collection if this category has no super-categories.
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code>.
	 */
	Collection<List<CategoryModel>> getPathsForCategory(CategoryModel category);

	/**
	 * Checks if the {@link CategoryModel} has product or sub-category.
	 * 
	 * @param category
	 *           the category to be checked
	 * @return true if the {@link CategoryModel} has neither product nor sub-category
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code>.
	 */
	boolean isEmpty(CategoryModel category);

	/**
	 * Checks if the the {@link CategoryModel} is a root category (has no super category).
	 * 
	 * @param category
	 *           the category to be checked
	 * @return true if the {@link CategoryModel} is a root category, false otherwise.
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code>.
	 */
	boolean isRoot(CategoryModel category);

	/**
	 * Sets the principals for the category. The new principals will be set to the {@link CategoryModel} so that the user
	 * rights can be defined specifically.
	 * 
	 * @param category
	 *           the category where the principals will be applied to
	 * @param newPrincipals
	 *           the list with the new allowed principals of the category
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code> or <code>newPrincipals</code> is
	 *            <code>null</code>.
	 * @see CategoryPrincipalStrategy#replacePrincipalsForCategory(CategoryModel, List)
	 */
	void setAllowedPrincipalsForCategory(CategoryModel category, List<PrincipalModel> newPrincipals);

	/**
	 * Sets the principals for the category and all supercategories and subcategories of passed as parameter category.
	 * The new principals will be set to the {@link CategoryModel} so that the user rights can be defined specifically.
	 * 
	 * @param category
	 *           the category where the principals will be applied to
	 * @param newPrincipals
	 *           the list with the new allowed principals of the category
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code> or <code>newPrincipals</code> is
	 *            <code>null</code>.
	 * @see CategoryPrincipalStrategy#replacePrincipalsForCategory(CategoryModel, List)
	 */
	void setAllowedPrincipalsForAllRelatedCategories(CategoryModel category, List<PrincipalModel> newPrincipals);

	/**
	 * Disable subcategory removal check. This method puts into the session flag
	 * {@link de.hybris.platform.category.constants.CategoryConstants#DISABLE_SUBCATEGORY_REMOVALCHECK} with value
	 * <code>Boolean.TRUE</code>. This flag is next checked by
	 * {@link de.hybris.platform.category.interceptors.CategoryRemovalValidator} to ensure that category should be
	 * checked against possible subcategories during remove.
	 */
	void disableSubcategoryRemovalCheck();

	/**
	 * Enable subcategory removal check. This method removes flag
	 * {@link de.hybris.platform.category.constants.CategoryConstants#DISABLE_SUBCATEGORY_REMOVALCHECK} from the session
	 * and each possible <b>remove</b> action on category will always perform check against possible subcategories.
	 * Category which has subcategories will be not removed.
	 */
	void enableSubcategoryRemovalCheck();

	/**
	 * Checks if the attribute {@link CategoryConstants#DISABLE_SUBCATEGORY_REMOVALCHECK} exists in the session and
	 * evaluates its value.
	 * 
	 * @return true if {@link CategoryConstants#DISABLE_SUBCATEGORY_REMOVALCHECK} exists in the session <b>AND</b> the
	 *         value is true
	 */
	boolean isSubcategoryRemovalCheckDisabled();

	/**
	 * Checks if the attribute {@link CategoryConstants#DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY} exists in the session
	 * and evaluates its value. If the flag is set to false (default) then while setting the principals for a given
	 * category, the subcategories and supercategories are updated as well. Copying the prinicpals down to subcategories
	 * means mostly replacing them. Pulling the principals up however allows only adding new principals in
	 * supercategories, never remove.
	 *
	 * @return true if {@link CategoryConstants#DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY} exists in the session <b>AND</b>
	 *         the value is true
	 */
	default boolean isSetAllowedPrincipalsRecursivelyDisabled()
	{
		return false;
	}

	/**
	 * Gets all subcategories for category and all subcategories recursively.
	 * 
	 * @param category
	 *           the category for which subcategories should be obtained
	 * @return the collection of subcategories
	 */
	Collection<CategoryModel> getAllSubcategoriesForCategory(CategoryModel category);

	/**
	 * Gets the all supercategories for categories.
	 * 
	 * @param category
	 *           the category for which supercategories should be obtained
	 * @return the collection of supercategories
	 */
	Collection<CategoryModel> getAllSupercategoriesForCategory(CategoryModel category);
}
