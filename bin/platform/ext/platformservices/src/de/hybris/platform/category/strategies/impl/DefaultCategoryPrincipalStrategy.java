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
package de.hybris.platform.category.strategies.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.category.strategies.CategoryPrincipalStrategy;
import de.hybris.platform.core.model.security.PrincipalModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Default implementation for {@link CategoryPrincipalStrategy}.
 */
public class DefaultCategoryPrincipalStrategy implements CategoryPrincipalStrategy
{

	@Override
	public void replacePrincipalsForCategory(final CategoryModel category, final List<PrincipalModel> newPrincipals)
	{
		//set the principals for the current category
		category.setAllowedPrincipals(newPrincipals);
	}

	@Override
	public void replacePrincipalsForAllRelatedCategories(final CategoryModel category, final List<PrincipalModel> newPrincipals)
	{
		replacePrincipalsForCategory(category, newPrincipals);

		//first, set the principals for sub-categories
		for (final CategoryModel subCategory : category.getAllSubcategories())
		{
			subCategory.setAllowedPrincipals(newPrincipals);
		}

		//second, add the principals for super-categories
		for (final CategoryModel superCatetory : category.getAllSupercategories())
		{
			addPrincipalsToCategory(superCatetory, newPrincipals);
		}
	}

	/**
	 * Adds principals which are <b>not yet</b> included in allowed principal set. Missing principals will <b>not</b> be
	 * removed!
	 * 
	 * @param category
	 *           the category
	 * @param newPrincipals
	 *           the principal set to pull up
	 */
	private void addPrincipalsToCategory(final CategoryModel category, final List<PrincipalModel> newPrincipals)
	{
		final Set<PrincipalModel> principals = new HashSet(category.getAllowedPrincipals());
		principals.addAll(newPrincipals);
		category.setAllowedPrincipals(new ArrayList<PrincipalModel>(principals));
	}
}
