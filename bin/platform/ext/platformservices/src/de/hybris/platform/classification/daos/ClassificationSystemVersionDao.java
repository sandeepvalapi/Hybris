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

import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;

import java.util.Collection;


/**
 * DAO for the {@link ClassificationSystemVersionModel}.
 * 
 * @spring.bean classificationSystemVersionDao
 */
public interface ClassificationSystemVersionDao
{

	/**
	 * Finds all {@link ClassificationSystemVersionModel}s with its classification system id and the classification
	 * system version.
	 * 
	 * @param systemId
	 *           the classification system ID
	 * @param systemVersion
	 *           the classification system version
	 * @return a collection of all found {@link ClassificationSystemVersionModel}s
	 */
	Collection<ClassificationSystemVersionModel> findSystemVersions(String systemId, String systemVersion);

	/**
	 * Finds all {@link ClassificationClassModel}s in the classification system version with its unique code.
	 * 
	 * @param systemVersion
	 *           the classification system version of the classification class
	 * @param code
	 *           the code of the classification class
	 * @return a collection of all found {@link ClassificationClassModel}s
	 */
	Collection<ClassificationClassModel> findClassesByCode(ClassificationSystemVersionModel systemVersion, String code);

	/**
	 * Finds all {@link ClassificationAttributeModel}s in the classification system version with the code.
	 * 
	 * @param systemVersion
	 *           the classification system version of the classification attribute
	 * @param code
	 *           the code of the classification attribute
	 * @return a collection of all found {@link ClassificationAttributeModel}s
	 */
	Collection<ClassificationAttributeModel> findAttributesByCode(ClassificationSystemVersionModel systemVersion, String code);

	/**
	 * Finds all {@link ClassificationAttributeValueModel}s in the classification system version with the code.
	 * 
	 * @param systemVersion
	 *           the classification system version of the classification attribute value
	 * @param code
	 *           the code of the classification attribute value
	 * @return a collection of all found {@link ClassificationAttributeValueModel}s
	 */
	Collection<ClassificationAttributeValueModel> findAttributeValuesByCode(ClassificationSystemVersionModel systemVersion,
			String code);

	/**
	 * Finds all other unites of the same type in the system version for the specific attribute unit.
	 * 
	 * @param attributeUnit
	 *           the classification attribute unit which has convertible units
	 * @return all OTHER {@link ClassificationAttributeUnitModel}s that have the same type and in the same system version
	 */
	Collection<ClassificationAttributeUnitModel> findConvertibleUnits(ClassificationAttributeUnitModel attributeUnit);

	/**
	 * Finds all {@link ClassificationAttributeUnitModel}s in the classification system version with the code.
	 * 
	 * @param systemVersion
	 *           the classification system version of the classification attribute unit
	 * @param code
	 *           the code of the classification attribute unit
	 * @return a collection of all found {@link ClassificationAttributeUnitModel}s
	 */
	Collection<ClassificationAttributeUnitModel> findAttributeUnitsByCode(ClassificationSystemVersionModel systemVersion,
			String code);

	/**
	 * Finds all {@link ClassificationAttributeUnitModel}s in the classification system version.
	 * 
	 * @param systemVersion
	 *           the classification system version that contains the classification attribute units
	 * @return the collection of all found {@link ClassificationAttributeUnitModel}s
	 */
	Collection<ClassificationAttributeUnitModel> findAttributeUnitsBySystemVersion(ClassificationSystemVersionModel systemVersion);

	/**
	 * Finds all {@link ClassificationAttributeUnitModel}s in the classification system version with the type.
	 * 
	 * @param systemVersion
	 *           the classification system version that contains the classification attribute units
	 * @param type
	 *           unit type of the classification attribute units
	 * @return the collection of all found {@link ClassificationAttributeUnitModel}s
	 */
	Collection<ClassificationAttributeUnitModel> findUnitsOfTypeBySystemVersion(ClassificationSystemVersionModel systemVersion,
			String type);

	/**
	 * Finds all unit types of the {@link ClassificationAttributeUnitModel}s in the classification system version.
	 * 
	 * @param systemVersion
	 *           the classification system version that contains the classification attribute units
	 * @return the collection of all unit types
	 */
	Collection<String> findUnitTypesBySystemVersion(ClassificationSystemVersionModel systemVersion);

}
