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
package de.hybris.platform.classification.strategy;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;


/**
 * Strategy for loading features from database and storing features back to database.
 * 
 * @spring.bean loadStoreFeaturesStrategy
 */
public interface LoadStoreFeaturesStrategy
{
	/**
	 * Load features for particular product.
	 * 
	 * @param product
	 *           the product for which list of <code>Feature</code>s will be loaded
	 * @return the result list of <code>Feature</code> objects
	 * 
	 * @throws IllegalArgumentException
	 *            thrown when <code>product</code> is not persisted
	 */
	List<Feature> loadFeatures(List<ClassAttributeAssignmentModel> assignments, ProductModel product);

	/**
	 * Store features for particular product to the database. If <code>removeRemaining</code> flag is set to
	 * <code>true</code> all existing features which are not on the <code>features</code> list will be removed.
	 * 
	 * @param product
	 *           the product
	 * @param features
	 *           the list of features to store
	 * 
	 * @throws IllegalArgumentException
	 *            thrown when <code>product</code> is not persisted
	 */
	void storeFeatures(ProductModel product, List<Feature> features);

	/**
	 * Store features for particular product to the database. If <code>removeRemaining</code> flag is set to
	 * <code>true</code> all existing features which are not on the <code>features</code> list will be removed.
	 * 
	 * @param allAssignments
	 *           all assignment to take into consideration for replacement
	 * @param product
	 *           the product
	 * @param features
	 *           the list of features to store
	 * 
	 * @throws IllegalArgumentException
	 *            thrown when <code>product</code> is not persisted
	 */
	void replaceFeatures(List<ClassAttributeAssignmentModel> allAssignments, ProductModel product, List<Feature> features);

}
