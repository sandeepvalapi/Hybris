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
package com.hybris.training.core.suggestion.impl;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import com.hybris.training.core.suggestion.SimpleSuggestionService;
import com.hybris.training.core.suggestion.dao.SimpleSuggestionDao;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of {@link SimpleSuggestionService}.
 */
public class DefaultSimpleSuggestionService implements SimpleSuggestionService
{
	private SimpleSuggestionDao simpleSuggestionDao;

	/**
	 * @deprecated Since 5.0.
	 */
	@Override
	@Deprecated
	public List<ProductModel> getReferencesForPurchasedInCategory(final CategoryModel category, final UserModel user,
			final ProductReferenceTypeEnum referenceType, final boolean excludePurchased, final Integer limit)
	{
		return getSimpleSuggestionDao().findProductsRelatedToPurchasedProductsByCategory(category, user, referenceType,
				excludePurchased, limit);
	}

	@Override
	public List<ProductModel> getReferencesForPurchasedInCategory(final CategoryModel category,
			final List<ProductReferenceTypeEnum> referenceTypes, final UserModel user, final boolean excludePurchased,
			final Integer limit)
	{
		return getSimpleSuggestionDao().findProductsRelatedToPurchasedProductsByCategory(category, referenceTypes, user,
				excludePurchased, limit);
	}

	@Override
	public List<ProductModel> getReferencesForProducts(final List<ProductModel> products, final List<ProductReferenceTypeEnum> referenceTypes, final UserModel user, final boolean excludePurchased, final Integer limit)
	{
		if (CollectionUtils.isEmpty(products))
		{
			return Collections.emptyList();
		}
		return getSimpleSuggestionDao().findProductsRelatedToProducts(products, referenceTypes, user,
				excludePurchased, limit);
	}

	protected SimpleSuggestionDao getSimpleSuggestionDao()
	{
		return simpleSuggestionDao;
	}

	public void setSimpleSuggestionDao(final SimpleSuggestionDao simpleSuggestionDao)
	{
		this.simpleSuggestionDao = simpleSuggestionDao;
	}
}
