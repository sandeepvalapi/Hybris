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

import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;

import java.util.Collection;


/**
 * Provides access to ClassificationSystems, and ClassificationSystemVersions, ClassificationClasses,
 * ClassificationAttributes, ClassificationAttributeUnits, and ClassificationAttributeValues.
 * 
 * @spring.bean classificationSystemService
 */
public interface ClassificationSystemService
{

	/**
	 * Retrieves the {@link ClassificationSystemModel} with the specified <code>id</code>.
	 * 
	 * @param id
	 *           the id of the classification system
	 * @return the found {@link ClassificationSystemModel}.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no ClassificationSystem with the specified <code>id</code> can be found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one ClassificationSystem can be found with the specified <code>id</code>.
	 * @throws IllegalArgumentException
	 *            if <code>id</code> is <code>null</code>
	 */
	ClassificationSystemModel getSystemForId(final String id);

	/**
	 * Retrieves the {@link ClassificationSystemVersionModel} with the specified <code>systemId</code> and
	 * <code>versionName</code>.
	 * 
	 * @param systemId
	 *           the id of the classification system
	 * @param systemVersion
	 *           the version of the classification system version
	 * @return the found {@link ClassificationSystemVersionModel}
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no ClassificationSystemVersion with the specified system id and version exists
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one ClassificationSystemVersionModel can be found with the specified system id and version
	 * @throws IllegalArgumentException
	 *            if <code>systemId</code> or <code>systemVersion</code> is <code>null</code>
	 */
	ClassificationSystemVersionModel getSystemVersion(String systemId, String systemVersion);

	/**
	 * Retrieves the {@link ClassificationClassModel} in the classification system version with its unique code.
	 * 
	 * @param systemVersion
	 *           the classification system version of the classification class
	 * @param code
	 *           the code of the classification class
	 * @return the found {@link ClassificationClassModel}
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no classification class in the system version with the code exists
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one classification class can be found in the specified system version with the code
	 * @throws IllegalArgumentException
	 *            if <code>systemVersion</code> or <code>code</code> is <code>null</code>
	 */
	ClassificationClassModel getClassForCode(ClassificationSystemVersionModel systemVersion, String code);

	/**
	 * Retrieves the {@link ClassificationAttributeModel} in the classification system version with the code.
	 * 
	 * @param systemVersion
	 *           the classification system version of the classification attribute
	 * @param code
	 *           the code of the classification attribute
	 * @return the found {@link ClassificationAttributeModel}
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no classification attribute in the system version with the code exists
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one classification attribute can be found in the specified system version with the code
	 * @throws IllegalArgumentException
	 *            if <code>systemVersion</code> or <code>code</code> is <code>null</code>
	 */
	ClassificationAttributeModel getAttributeForCode(ClassificationSystemVersionModel systemVersion, String code);

	/**
	 * Retrieves the {@link ClassificationAttributeValueModel} in the classification system version with the code.
	 * 
	 * @param systemVersion
	 *           the classification system version of the classification attribute value
	 * @param code
	 *           the code of the classification attribute value
	 * @return the found {@link ClassificationAttributeValueModel}
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no classification attribute value in the system version with the code exists
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one classification attribute value can be found in the specified system version with the
	 *            code
	 * @throws IllegalArgumentException
	 *            if <code>systemVersion</code> or <code>code</code> is <code>null</code>
	 */
	ClassificationAttributeValueModel getAttributeValueForCode(ClassificationSystemVersionModel systemVersion, String code);

	/**
	 * Retrieves all other unites of the same type in the system version for the specific attribute unit.
	 * 
	 * @param attributeUnit
	 *           the classification attribute unit which has convertible units
	 * @return all OTHER {@link ClassificationAttributeUnitModel}s that have the same type and in the same system version
	 * @throws IllegalArgumentException
	 *            if <code>attributeUnit</code> is <code>null</code>
	 */
	Collection<ClassificationAttributeUnitModel> getConvertibleUnits(ClassificationAttributeUnitModel attributeUnit);

	/**
	 * Retrieves the {@link ClassificationAttributeUnitModel} in the classification system version with the code.
	 * 
	 * @param systemVersion
	 *           the classification system version of the classification attribute unit
	 * @param code
	 *           the code of the classification attribute unit
	 * @return the found {@link ClassificationAttributeUnitModel}
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no classification attribute unit in the system version with the code exists
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one classification attribute unit can be found in the specified system version with the
	 *            code
	 * @throws IllegalArgumentException
	 *            if <code>systemVersion</code> or <code>code</code> is <code>null</code>
	 */
	ClassificationAttributeUnitModel getAttributeUnitForCode(ClassificationSystemVersionModel systemVersion, String code);

	/**
	 * Retrieves all {@link ClassificationAttributeUnitModel}s in the classification system version.
	 * 
	 * @param systemVersion
	 *           the classification system version that contains the classification attribute units
	 * @return the collection of all found {@link ClassificationAttributeUnitModel}s
	 * @throws IllegalArgumentException
	 *            if <code>systemVersion</code> is <code>null</code>
	 */
	Collection<ClassificationAttributeUnitModel> getAttributeUnitsForSystemVersion(ClassificationSystemVersionModel systemVersion);

	/**
	 * Retrieves all {@link ClassificationAttributeUnitModel}s in the classification system version with the specific
	 * type.
	 * 
	 * @param systemVersion
	 *           the classification system version that contains the classification attribute units
	 * @param type
	 *           unit type of the classification attribute units
	 * @return the collection of all found {@link ClassificationAttributeUnitModel}s
	 * @throws IllegalArgumentException
	 *            if <code>systemVersion</code> or <code>type</code> is <code>null</code>
	 */
	Collection<ClassificationAttributeUnitModel> getUnitsOfTypeForSystemVersion(ClassificationSystemVersionModel systemVersion,
			String type);

	/**
	 * Retrieves all unit types of the {@link ClassificationAttributeUnitModel}s in the classification system version.
	 * 
	 * @param systemVersion
	 *           the classification system version that contains the classification attribute units
	 * @return the collection of all unit types
	 * @throws IllegalArgumentException
	 *            if <code>systemVersion</code> is <code>null</code>
	 */
	Collection<String> getUnitTypesForSystemVersion(ClassificationSystemVersionModel systemVersion);

	/**
	 * Retrieves all root {@link ClassificationClassModel}s in the classification system version.
	 * 
	 * @param systemVersion
	 *           the classification system version that contains the root classification attribute classes
	 * @return the collection of all found root {@link ClassificationClassModel}s
	 * @throws IllegalArgumentException
	 *            if <code>systemVersion</code> is <code>null</code>
	 */
	Collection<ClassificationClassModel> getRootClassesForSystemVersion(ClassificationSystemVersionModel systemVersion);

}
