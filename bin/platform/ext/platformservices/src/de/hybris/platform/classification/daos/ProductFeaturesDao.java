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
package de.hybris.platform.classification.daos;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;


/**
 * DAO for finding product features.
 * 
 * @spring.bean productFeaturesDao
 */
public interface ProductFeaturesDao
{

	/**
	 * Find product features by product and assignment.
	 * 
	 * @param product
	 *           the product for which find features
	 * @param assignments
	 *           the assignments
	 * @return the result list
	 */
	List<List<ItemModel>> findProductFeaturesByProductAndAssignment(ProductModel product,
			List<ClassAttributeAssignmentModel> assignments);

	/**
	 * Find product features by product and assignment without these on <code>excludes</code> list.
	 * 
	 * @param product
	 *           the product for which find features
	 * @param assignments
	 *           the assignments
	 * @param excludes
	 *           the excludes
	 * @return the result list
	 */
	List<List<ItemModel>> findProductFeaturesByProductAndAssignment(ProductModel product,
			List<ClassAttributeAssignmentModel> assignments, List<ProductFeatureModel> excludes);

	/**
	 * Gets the product feature's max value position.
	 * 
	 * @param product
	 *           the product
	 * @param assignment
	 *           the assignment
	 * @return the product feature max value position
	 */
	List<Integer> getProductFeatureMaxValuePosition(ProductModel product, ClassAttributeAssignmentModel assignment);
}
