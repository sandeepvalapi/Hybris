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
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Service for resolving and returning the {@link ClassificationClassModel}s for the given {@link ItemModel}s (products,
 * categories). This service is developed as part of decoupling the FeutureContainer. However, it is still not used in
 * ClassificationService or any other related service.
 * 
 * @spring.bean classificationClassesResolverStrategy
 */
public interface ClassificationClassesResolverStrategy
{
	/**
	 * Returns for the given {@link ProductModel} all matching {@link ClassificationClassModel}s for <b>all</b>
	 * {@link ClassificationSystemVersionModel}s.
	 * 
	 * @param item
	 *           the {@link ProductModel} for which all connected {@link ClassificationClassModel}s should be returned
	 * 
	 * @return a set with the found ClassificationClassModels or an empty set if nothing was found
	 */
	Set<ClassificationClassModel> resolve(ProductModel item);

	/**
	 * Returns for the given {@link CategoryModel} all matching {@link ClassificationClassModel}s for <b>all</b>
	 * {@link ClassificationSystemVersionModel}s.
	 * 
	 * @param item
	 *           the {@link CategoryModel} for which all connected {@link ClassificationClassModel}s should be returned
	 * 
	 * @return a set with the found ClassificationClassModels or an empty set if nothing was found
	 */
	Set<ClassificationClassModel> resolve(CategoryModel item);

	/**
	 * Returns for the given {@link ProductModel} all matching {@link ClassificationClassModel}s for the given
	 * {@link ClassificationSystemVersionModel} version.
	 * 
	 * @param item
	 *           the {@link ProductModel} for which all connected {@link ClassificationClassModel}s should be returned
	 * 
	 * @param systemVersion
	 *           the kind of the classification system
	 * @return a set with the found ClassificationClassModels or an empty set if nothing was found
	 */
	Set<ClassificationClassModel> resolve(ProductModel item, ClassificationSystemVersionModel systemVersion);

	/**
	 * Returns for the given {@link CategoryModel} all matching {@link ClassificationClassModel}s for the given
	 * {@link ClassificationSystemVersionModel} version.
	 * 
	 * @param item
	 *           the {@link CategoryModel} for which all connected {@link ClassificationClassModel}s should be returned
	 * 
	 * @param systemVersion
	 *           the kind of the classification system
	 * @return a set with the found ClassificationClassModels or an empty set if nothing was found
	 */
	Set<ClassificationClassModel> resolve(CategoryModel item, ClassificationSystemVersionModel systemVersion);

	/**
	 * Returns for the given {@link ProductModel} all matching {@link ClassificationClassModel}s for the given
	 * {@link ClassificationSystemVersionModel} versions.
	 * 
	 * @param item
	 *           the {@link ProductModel} for which all connected {@link ClassificationClassModel}s should be returned
	 * 
	 * @param systemVersions
	 *           a collection of classification systems
	 * @return a set with the found ClassificationClassModels or an empty set if nothing was found
	 */
	Set<ClassificationClassModel> resolve(ProductModel item, Collection<ClassificationSystemVersionModel> systemVersions);

	/**
	 * Returns for the given {@link CategoryModel} all matching {@link ClassificationClassModel}s for the given
	 * {@link ClassificationSystemVersionModel} versions.
	 * 
	 * @param item
	 *           the {@link CategoryModel} for which all connected {@link ClassificationClassModel}s should be returned
	 * 
	 * @param systemVersions
	 *           a collection of classification systems
	 * @return a set with the found ClassificationClassModels or an empty set if nothing was found
	 */
	Set<ClassificationClassModel> resolve(CategoryModel item, Collection<ClassificationSystemVersionModel> systemVersions);

	/**
	 * Returns all the {@link ClassAttributeAssignmentModel} from the given {@link ClassificationClassModel} set.
	 * 
	 * @param classificationClasses
	 *           if using the resolve methods of this service the set is a {@link LinkedHashSet}
	 * @return a sorted list of ClassAttributeAssignmentModels or an empty list.
	 * 
	 * @deprecated since ages - use{@link #getDeclaredClassAttributeAssignments(Set)} instead.
	 */
	@Deprecated
	List<ClassAttributeAssignmentModel> getClassAttributeAssignments(Set<ClassificationClassModel> classificationClasses);

	/**
	 * Returns all the {@link ClassAttributeAssignmentModel} from the given {@link ClassificationClassModel} set.
	 * 
	 * @param classificationClasses
	 *           if using the resolve methods of this service the set is a {@link LinkedHashSet}
	 * @return a sorted list of ClassAttributeAssignmentModels or an empty list.
	 * 
	 */
	List<ClassAttributeAssignmentModel> getAllClassAttributeAssignments(Set<ClassificationClassModel> classificationClasses);

	/**
	 * Returns all declared {@link ClassAttributeAssignmentModel} from the given {@link ClassificationClassModel} set.
	 * 
	 * @param classificationClasses
	 *           if using the resolve methods of this service the set is a {@link LinkedHashSet}
	 * @return a sorted list of ClassAttributeAssignmentModels or an empty list.
	 */
	List<ClassAttributeAssignmentModel> getDeclaredClassAttributeAssignments(Set<ClassificationClassModel> classificationClasses);

}
