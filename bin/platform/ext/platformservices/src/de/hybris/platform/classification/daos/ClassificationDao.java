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

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.impl.PossibleAttributeValue;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * DAO to use by classification related services.
 * 
 * @spring.bean classificationDao
 */
public interface ClassificationDao extends Dao
{

	/**
	 * Finds possible attribute values for given category, ClassAttribtuteAssignments and attribute filter.
	 */
	List<PossibleAttributeValue> findPossibleAttributeValues(CategoryModel category,
			final Collection<ClassAttributeAssignmentModel> assignments,
			Map<ClassAttributeAssignmentModel, Object> filteredAttributeValues);

	/**
	 * Find all products by category and filter.
	 */
	SearchResult<ProductModel> findProductsByAttributeValues(CategoryModel category,
			Map<ClassAttributeAssignmentModel, Object> attributeValues, int start, int count);

	/**
	 * Find attribute values by code.
	 */
	List<ClassificationAttributeValueModel> findAttributeValuesByCode(String code);


	/**
	 * Find all attribute units of a system version.
	 */
	List<ClassificationAttributeUnitModel> findAttributeUnits(final ClassificationSystemVersionModel systemVersion);

}
