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
package de.hybris.platform.classification;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.filter.ProductFilter;
import de.hybris.platform.classification.filter.ProductFilterResult;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;
import java.util.List;


/**
 * Service to obtain information about a product's features and to search for products with classifcation attribute
 * filters applied
 * 
 * @spring.bean classificationService
 */
public interface ClassificationService
{

	/**
	 * Returns a list of classification features for a given product.
	 * 
	 * @param product
	 *           the product to load the features for
	 * @return the list of features
	 * 
	 * @throws IllegalArgumentException
	 *            thrown when <code>product</code> is null or is not persisted
	 */
	FeatureList getFeatures(ProductModel product);

	/**
	 * Returns a classification feature for a given item.
	 * 
	 * @param item
	 *           the item to load the features for
	 * @return the features
	 * 
	 * @deprecated since ages - use{@link #getFeature(ProductModel, ClassAttributeAssignmentModel)} instead.
	 */
	@Deprecated
	Feature getFeature(ItemModel item, ClassAttributeAssignmentModel assignment);

	/**
	 * Returns a classification feature for a given product and assignment.
	 * 
	 * @param product
	 *           the item to load the features for
	 * @return the features
	 * 
	 * @throws IllegalArgumentException
	 *            thrown when <code>product</code> is null or is not persisted, <code>assignment</code> is null
	 */
	Feature getFeature(ProductModel product, ClassAttributeAssignmentModel assignment);

	/**
	 * Returns all products for which the given filter applies. The filter is defined in terms of a category and a number
	 * of classification attribute code/value pairs. Attribute values can be given as String. This method will convert
	 * string values to the type of the actual classification attribute before applying the filter. Scalar types are
	 * converted by using the <code>valueOf</code> method of attribute value's class. When the attribute is of type enum
	 * the string is interpreted as the code of the enum value.
	 * 
	 * @param filter
	 *           the filter to apply
	 * @return the filtered products
	 * @throws UnknownIdentifierException
	 *            when a attribute with a given code cannot be found
	 * @throws AmbiguousIdentifierException
	 *            when there's more than one attribute with a given code
	 */
	ProductFilterResult getProductsByFilter(ProductFilter filter) throws UnknownIdentifierException, AmbiguousIdentifierException;

	/**
	 * Sets the given list of classification {@link Feature}s to the given {@link ProductModel}. Any other existing
	 * feature at the product model which is not in the list won't change
	 * 
	 * @param product
	 *           the product
	 * @param featureList
	 *           the FeatureList object
	 * 
	 * @throws IllegalArgumentException
	 *            thrown when <code>product</code> is null or is not persisted, <code>featureList</code> is null
	 */
	void setFeatures(ProductModel product, FeatureList featureList);

	/**
	 * Sets all the features from the list to the product model. The existing features at the producr model which are not
	 * in the feature list are removed (untyped feature) or the feature values of this feature are cleared (typed
	 * feature)
	 * 
	 * @param product
	 *           the Product model
	 * @param featureList
	 *           contains the features with new/changed feature values. Or features with the same feature values which
	 *           are already stored - those won't change then.
	 * 
	 * @throws IllegalArgumentException
	 *            thrown when <code>product</code> is null or is not persisted, <code>featureList</code> is null
	 */
	void replaceFeatures(ProductModel product, FeatureList featureList);

	/**
	 * Sets a single {@link Feature} to the given {@link ProductModel}. Any other Feature at the product model won't be
	 * changed.
	 * 
	 * @param product
	 *           the product
	 * @param feature
	 *           the feature
	 * 
	 * @throws IllegalArgumentException
	 *            thrown when <code>product</code> is null or is not persisted, <code>feature</code> is null
	 */
	void setFeature(ProductModel product, Feature feature);

	/**
	 * Returns a list of all classification attribute units.
	 * 
	 * @param systemVersion
	 *           the system version of the classification
	 * @return the classification attribute units
	 */
	Collection<ClassificationAttributeUnitModel> getAttributeUnits(final ClassificationSystemVersionModel systemVersion);

	/**
	 * Returns a list of classification features for a given product and assignments.
	 *
	 * @param product
	 *           the product to load the features for
	 * @param assignments
	 *           list of assignments
	 * 
	 * @return the list of features
	 *
	 * @throws IllegalArgumentException
	 *            thrown when <code>product</code> is null or is not persisted
	 */
	FeatureList getFeatures(ProductModel product, List<ClassAttributeAssignmentModel> assignments);
}
