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
package de.hybris.platform.classification.impl;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Set;


/**
 * The SuperCategoriesResolver is used to return the super categories of the given type in a special way. For
 * {@link CategoryModel} the implementation could just return the connected super categories, for products (
 * {@link VariantProductModel}!) the super categories for the base product could be returned. Other implementations
 * (product code starts with '10' then return a certain {@link ClassificationClassModel}) are also possible.
 * <p/>
 * example:
 * 
 * <pre>
 * new SuperCategoriesResolver&lt;CategoryModel&gt;()
 * {
 * 	&#064;Override
 * 	public Set&lt;CategoryModel&gt; getSuperCategories(final CategoryModel item)
 * 	{
 * 		return item == null || item.getSupercategories() == null ? Collections.EMPTY_SET : new LinkedHashSet&lt;CategoryModel&gt;(
 * 				item.getSupercategories());
 * 	}
 * };
 * </pre>
 */
public interface SuperCategoriesResolver<T extends ItemModel>
{
	/**
	 * Implement this method for getting the super categories in a special way. <code>Null</code> as return value is
	 * allowed.
	 */
	Set<CategoryModel> getSuperCategories(T item);
}
