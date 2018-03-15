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
package de.hybris.platform.category.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.impl.CatalogUtils;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.category.strategies.CategoryPrincipalStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * Default implementation for {@link CategoryService}.
 */
public class DefaultCategoryService extends AbstractBusinessService implements CategoryService
{

	private static final Logger LOG = Logger.getLogger(DefaultCategoryService.class);

	private CategoryDao categoryDao;
	private CatalogVersionService catalogVersionService;
	private volatile CategoryPrincipalStrategy categoryPrincipalStrategy;

	@Override
	public void disableSubcategoryRemovalCheck()
	{
		getSessionService().setAttribute(CategoryConstants.DISABLE_SUBCATEGORY_REMOVALCHECK, Boolean.TRUE);
	}

	@Override
	public void enableSubcategoryRemovalCheck()
	{
		getSessionService().removeAttribute(CategoryConstants.DISABLE_SUBCATEGORY_REMOVALCHECK);
	}

	@Override
	public boolean isSubcategoryRemovalCheckDisabled()
	{
		return Boolean.TRUE.equals(getSessionService().getAttribute(CategoryConstants.DISABLE_SUBCATEGORY_REMOVALCHECK));
	}

	@Override
	public boolean isSetAllowedPrincipalsRecursivelyDisabled()
	{
		return Boolean.TRUE.equals(getSessionService().getAttribute(CategoryConstants.DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY));
	}

	@Override
	public Collection<CategoryModel> getCategoriesForCode(final String code)
	{
		validateParameterNotNull(code, "Parameter 'code' was null.");
		return categoryDao.findCategoriesByCode(code);
	}

	@Override
	public List<CategoryModel> getCategoryPathForProduct(final ProductModel product, final Class... includeOnlyCategories)
	{
		final List<CategoryModel> result = new ArrayList<CategoryModel>();
		final Collection<CategoryModel> currentLevel = new ArrayList<CategoryModel>();
		currentLevel.addAll(product.getSupercategories());

		while (!CollectionUtils.isEmpty(currentLevel))
		{
			CategoryModel categoryModel = null;
			for (final CategoryModel category : currentLevel)
			{
				if (categoryModel == null && shouldAddPathElement(category.getClass(), includeOnlyCategories))
				{
					categoryModel = category;
				}
			}
			currentLevel.clear();
			if (categoryModel != null)
			{
				currentLevel.addAll(categoryModel.getSupercategories());
				result.add(categoryModel);
			}
		}

		Collections.reverse(result);
		return result;
	}

	private boolean shouldAddPathElement(final Class element, final Class... includeOnlyCategories)
	{
		boolean result = false;
		if (ArrayUtils.isEmpty(includeOnlyCategories))
		{
			result = true;
		}
		else
		{
			if (ArrayUtils.contains(includeOnlyCategories, element))
			{
				result = true;
			}
		}
		return result;
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public CategoryModel getCategory(final CatalogVersionModel catalogVersion, final String code)
	{
		return getCategoryForCode(catalogVersion, code);
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public CategoryModel getCategory(final String code)
	{
		validateParameterNotNull(code, "Parameter 'code' was null.");
		final Collection<CategoryModel> categories = getCategoriesForCode(code);
		return categories.isEmpty() ? null : categories.iterator().next();
	}

	@Override
	public CategoryModel getCategoryForCode(final CatalogVersionModel catalogVersion, final String code)
	{
		validateParameterNotNull(catalogVersion, "Parameter 'catalogVersion' was null.");
		validateParameterNotNull(code, "Parameter 'code' was null.");
		final Collection<CategoryModel> categories = this.categoryDao.findCategoriesByCode(catalogVersion, code);

		if (categories.isEmpty())
		{
			throw new UnknownIdentifierException(
					"Category with code '" + code + "' in CatalogVersion '" + CatalogUtils.getFullCatalogVersionName(catalogVersion)
							+ "' not found! (Active session catalogversions: " + getCatalogVersionsString() + ")");
		}
		else if (categories.size() > 1)
		{
			throw new AmbiguousIdentifierException("Category with code '" + code + "' in CatalogVersion '"
					+ CatalogUtils.getFullCatalogVersionName(catalogVersion) + "' is not unique. " + categories.size()
					+ " categories found! (Active session catalogversions: " + getCatalogVersionsString() + ")");
		}
		return categories.iterator().next();
	}

	@Override
	public CategoryModel getCategoryForCode(final String code)
	{
		validateParameterNotNull(code, "Parameter 'code' was null.");
		final Collection<CategoryModel> categories = getCategoriesForCode(code);
		if (categories.isEmpty())
		{
			throw new UnknownIdentifierException("Category with code '" + code + "' not found! (Active session catalogversions: "
					+ getCatalogVersionsString() + ")");
		}
		else if (categories.size() > 1)
		{
			throw new AmbiguousIdentifierException("Category with code '" + code + "' is not unique. " + categories.size()
					+ " categories found! (Active session catalogversions: " + getCatalogVersionsString() + ")");
		}
		return categories.iterator().next();
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public List<CategoryModel> getPath(final CategoryModel category)
	{
		validateParameterNotNull(category, "Parameter 'category' was null.");
		final List<CategoryModel> ret = new LinkedList<CategoryModel>();
		CategoryModel cat = category;
		final Set<CategoryModel> controlSet = new HashSet<CategoryModel>();
		do
		{
			if (controlSet.add(cat))
			{
				ret.add(cat);
				final List<CategoryModel> superCategories = cat.getSupercategories();
				if (superCategories.isEmpty())
				{
					cat = null;
				}
				else
				{
					cat = superCategories.iterator().next();
				}
			}
			else
			{
				LOG.warn("path cycle found for category: [" + category.getCode() + "]");
			}
		}
		while (cat != null);
		return ret;
	}

	@Override
	public List<CategoryModel> getPathForCategory(final CategoryModel category)
	{
		final Collection<List<CategoryModel>> pathsForCategory = getPathsForCategory(category);
		if (pathsForCategory.size() == 1)
		{
			return pathsForCategory.iterator().next();
		}
		else
		{
			throw new AmbiguousIdentifierException(
					"Found more than one path for category " + category + " (found " + pathsForCategory.size() + ")");
		}
	}

	@Override
	public Collection<List<CategoryModel>> getPathsForCategory(final CategoryModel category)
	{
		validateParameterNotNull(category, "Parameter 'category' was null.");
		return getPathsInternal(category, new HashSet<CategoryModel>(Collections.singleton(category)));
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public Collection<CategoryModel> getRootCategories(final CatalogVersionModel catalogVersion)
	{
		return getRootCategoriesForCatalogVersion(catalogVersion);
	}

	@Override
	public Collection<CategoryModel> getRootCategoriesForCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		validateParameterNotNull(catalogVersion, "Parameter 'catalogVersion' was null.");
		return this.categoryDao.findRootCategoriesByCatalogVersion(catalogVersion);
	}

	@Override
	public boolean isEmpty(final CategoryModel category)
	{
		validateParameterNotNull(category, "Parameter 'category' was null.");
		return category.getCategories().isEmpty() && category.getProducts().isEmpty();
	}

	@Override
	public boolean isRoot(final CategoryModel category)
	{
		validateParameterNotNull(category, "Parameter 'category' was null.");
		return CollectionUtils.isEmpty(category.getSupercategories());
	}

	@Override
	public void setAllowedPrincipalsForCategory(final CategoryModel category, final List<PrincipalModel> newPrincipals)
	{
		Preconditions.checkArgument(category != null, "Parameter 'category' was null.");
		Preconditions.checkArgument(newPrincipals != null, "Parameter 'newPrincipals' was null.");
		categoryPrincipalStrategy.replacePrincipalsForCategory(category, newPrincipals);
	}

	@Override
	public void setAllowedPrincipalsForAllRelatedCategories(final CategoryModel category, final List<PrincipalModel> newPrincipals)
	{
		Preconditions.checkArgument(category != null, "Parameter 'category' was null.");
		Preconditions.checkArgument(newPrincipals != null, "Parameter 'newPrincipals' was null.");
		categoryPrincipalStrategy.replacePrincipalsForAllRelatedCategories(category, newPrincipals);
	}

	@Override
	public Collection<CategoryModel> getAllSubcategoriesForCategory(final CategoryModel category)
	{
		Preconditions.checkArgument(category != null, "Category is required to perform this operation, null given");
		return getAllSubcategories(Collections.singletonList(category));
	}

	private Collection<CategoryModel> getAllSubcategories(final Collection<CategoryModel> categories)
	{
		Collection<CategoryModel> result = null;
		Collection<CategoryModel> currentLevel = new ArrayList<CategoryModel>();
		for (final CategoryModel categoryModel : categories)
		{
			final List<CategoryModel> subCategories = categoryModel.getCategories();
			if (subCategories != null)
			{
				currentLevel.addAll(subCategories);
			}
		}

		while (!CollectionUtils.isEmpty(currentLevel))
		{
			for (final Iterator iterator = currentLevel.iterator(); iterator.hasNext();)
			{
				final CategoryModel categoryModel = (CategoryModel) iterator.next();
				if (result == null)
				{
					result = new HashSet<CategoryModel>();
				}
				if (!result.add(categoryModel))
				{
					// avoid cycles by removing all which are already found
					iterator.remove();
				}
			}

			if (currentLevel.isEmpty())
			{
				break;
			}
			final Collection<CategoryModel> nextLevel = getAllSubcategories(currentLevel);
			currentLevel = nextLevel;
		}

		return result == null ? Collections.EMPTY_LIST : result;
	}

	@Override
	public Collection<CategoryModel> getAllSupercategoriesForCategory(final CategoryModel category)
	{
		Preconditions.checkArgument(category != null, "Category is required to perform this operation, null given");
		return getAllSupercategories(Collections.singletonList(category));
	}

	private Collection<CategoryModel> getAllSupercategories(final Collection<CategoryModel> categories)
	{
		Collection<CategoryModel> result = null;
		Collection<CategoryModel> currentLevel = new ArrayList<CategoryModel>();
		for (final CategoryModel categoryModel : categories)
		{
			final List<CategoryModel> superCategories = categoryModel.getSupercategories();
			if (superCategories != null)
			{
				currentLevel.addAll(superCategories);
			}
		}

		while (!CollectionUtils.isEmpty(currentLevel))
		{
			for (final Iterator iterator = currentLevel.iterator(); iterator.hasNext();)
			{
				final CategoryModel categoryModel = (CategoryModel) iterator.next();
				if (result == null)
				{
					result = new HashSet<CategoryModel>();
				}
				if (!result.add(categoryModel))
				{
					// avoid cycles by removing all which are already found
					iterator.remove();
				}
			}

			if (currentLevel.isEmpty())
			{
				break;
			}
			final Collection<CategoryModel> nextLevel = getAllSupercategories(currentLevel);
			currentLevel = nextLevel;
		}

		return result == null ? Collections.EMPTY_LIST : result;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	@Required
	public void setCategoryDao(final CategoryDao categoryDao)
	{
		this.categoryDao = categoryDao;
	}

	@Required
	public void setCategoryPrincipalStrategy(final CategoryPrincipalStrategy categoryPrincipalStrategy)
	{
		this.categoryPrincipalStrategy = categoryPrincipalStrategy;
	}

	private String getCatalogVersionsString()
	{
		return CatalogUtils.getCatalogVersionsString(catalogVersionService.getSessionCatalogVersions());
	}

	private Collection<List<CategoryModel>> getPathsInternal(final CategoryModel category, final Set<CategoryModel> controlSet)
	{
		Collection<List<CategoryModel>> result = null;

		final Collection<CategoryModel> superCategories = category.getSupercategories();
		if (CollectionUtils.isNotEmpty(superCategories))
		{
			for (final CategoryModel parent : superCategories)
			{
				if (notVisited(parent, controlSet))
				{
					if (result == null)
					{
						result = new LinkedList<List<CategoryModel>>();
					}
					visitSuperCategory(category, parent, controlSet, result);
					markNotVisited(parent, controlSet);
				}
				else
				{
					LOG.warn("path cycle found for category: [" + category.getCode() + "]");
				}
			}
		}

		return result == null ? Collections.singletonList(Collections.singletonList(category)) : result;
	}

	/**
	 * @see #notVisited(CategoryModel, Set)
	 */
	private void markNotVisited(final CategoryModel cat, final Set<CategoryModel> visitedCategories)
	{
		visitedCategories.remove(cat);
	}

	/**
	 * @see #markNotVisited(CategoryModel, Set)
	 */
	private boolean notVisited(final CategoryModel cat, final Set<CategoryModel> visitedCategories)
	{
		return visitedCategories.add(cat);
	}

	private void visitSuperCategory(final CategoryModel category, final CategoryModel parent, final Set<CategoryModel> controlSet,
			final Collection<List<CategoryModel>> result)
	{
		for (List<CategoryModel> parentPath : getPathsInternal(parent, controlSet))
		{
			if (!(parentPath instanceof LinkedList))
			{
				parentPath = new LinkedList<CategoryModel>(parentPath);
			}
			parentPath.add(category);
			result.add(parentPath);
		}
	}

}
