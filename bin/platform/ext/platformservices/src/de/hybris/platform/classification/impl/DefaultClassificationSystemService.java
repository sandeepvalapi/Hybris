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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.daos.ClassificationSystemDao;
import de.hybris.platform.classification.daos.ClassificationSystemVersionDao;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link ClassificationSystemService}.
 */
public class DefaultClassificationSystemService extends AbstractBusinessService implements ClassificationSystemService
{

	private CategoryService categoryService;
	private ClassificationSystemDao classificationSystemDao;
	private ClassificationSystemVersionDao classificationSystemVersionDao;

	@Override
	public ClassificationSystemModel getSystemForId(final String id)
	{
		validateParameterNotNull(id, "Parameter 'id' cannot be null!");
		final Collection<ClassificationSystemModel> classificationSystems = classificationSystemDao.findSystemsById(id);
		validateIfSingleResult(classificationSystems, "No Classification System with given id [" + id + "] was found",
				"More than one Classification System with given id [" + id + "] was found");

		return classificationSystems.iterator().next();
	}

	@Override
	public ClassificationSystemVersionModel getSystemVersion(final String systemId, final String systemVersion)
	{
		validateParameterNotNull(systemId, "Parameter 'systemId' cannot be null");
		validateParameterNotNull(systemVersion, "Parameter 'systemVersionName' cannot be null");

		final Collection<ClassificationSystemVersionModel> systemVersions = classificationSystemVersionDao.findSystemVersions(
				systemId, systemVersion);

		validateIfSingleResult(systemVersions, "ClassificationSystemVersion with systemId '" + systemId + "' and version '"
				+ systemVersion + "' not found!", "Specified parameters systemId '" + systemId + "' and version '" + systemVersion
				+ "' are not unique. " + systemVersions.size() + " ClassificationSystemVersion found!");

		return systemVersions.iterator().next();
	}

	@Override
	public ClassificationClassModel getClassForCode(final ClassificationSystemVersionModel systemVersion, final String code)
	{
		validateParameterNotNull(systemVersion, "Parameter 'systemVersion' cannot be null!");
		validateParameterNotNull(code, "Parameter 'code' cannot be null!");
		final Collection<ClassificationClassModel> classificationClasses = classificationSystemVersionDao.findClassesByCode(
				systemVersion, code);
		validateIfSingleResult(classificationClasses, "No ClassificationClass in the ClassificationSystemVersion [" + systemVersion
				+ "] with code [" + code + "] can be found", "More than one ClassificationClass in the ClassificationSystemVersion ["
				+ systemVersion + "] with code [" + code + "] was found");
		return classificationClasses.iterator().next();
	}

	@Override
	public ClassificationAttributeModel getAttributeForCode(final ClassificationSystemVersionModel systemVersion, final String code)
	{
		validateParameterNotNull(systemVersion, "Parameter 'systemVersion' cannot be null!");
		validateParameterNotNull(code, "Parameter 'code' cannot be null!");
		final Collection<ClassificationAttributeModel> classificationAttributes = classificationSystemVersionDao
				.findAttributesByCode(systemVersion, code);
		validateIfSingleResult(classificationAttributes, "No ClassificationAttribute in the ClassificationSystemVersion ["
				+ systemVersion + "] with code [" + code + "] can be found",
				"More than one ClassificationAttribute in the ClassificationSystemVersion [" + systemVersion + "] with code [" + code
						+ "] was found");
		return classificationAttributes.iterator().next();
	}

	@Override
	public ClassificationAttributeValueModel getAttributeValueForCode(final ClassificationSystemVersionModel systemVersion,
			final String code)
	{
		validateParameterNotNull(systemVersion, "Parameter 'systemVersion' cannot be null!");
		validateParameterNotNull(code, "Parameter 'code' cannot be null!");
		final Collection<ClassificationAttributeValueModel> classificationAttributeValues = classificationSystemVersionDao
				.findAttributeValuesByCode(systemVersion, code);
		validateIfSingleResult(classificationAttributeValues,
				"No ClassificationAttributeValue in the ClassificationSystemVersion [" + systemVersion + "] with code [" + code
						+ "] can be found", "More than one ClassificationAttributeValue in the ClassificationSystemVersion ["
						+ systemVersion + "] with code [" + code + "] was found");
		return classificationAttributeValues.iterator().next();
	}

	@Override
	public Collection<ClassificationAttributeUnitModel> getConvertibleUnits(final ClassificationAttributeUnitModel attributeUnit)
	{
		validateParameterNotNull(attributeUnit, "Parameter 'attributeUnit' cannot be null!");
		final Collection<ClassificationAttributeUnitModel> classificationAttributeUnits = classificationSystemVersionDao
				.findConvertibleUnits(attributeUnit);
		return classificationAttributeUnits;
	}

	@Override
	public ClassificationAttributeUnitModel getAttributeUnitForCode(final ClassificationSystemVersionModel systemVersion,
			final String code)
	{
		validateParameterNotNull(systemVersion, "Parameter 'systemVersion' cannot be null!");
		validateParameterNotNull(code, "Parameter 'code' cannot be null!");
		final Collection<ClassificationAttributeUnitModel> classificationAttributeUnits = classificationSystemVersionDao
				.findAttributeUnitsByCode(systemVersion, code);
		validateIfSingleResult(classificationAttributeUnits, "No ClassificationAttributeUnit in the ClassificationSystemVersion ["
				+ systemVersion + "] with code [" + code + "] can be found",
				"More than one ClassificationAttributeUnit in the ClassificationSystemVersion [" + systemVersion + "] with code ["
						+ code + "] was found");
		return classificationAttributeUnits.iterator().next();
	}

	@Override
	public Collection<ClassificationAttributeUnitModel> getAttributeUnitsForSystemVersion(
			final ClassificationSystemVersionModel systemVersion)
	{
		validateParameterNotNull(systemVersion, "Parameter 'systemVersion' cannot be null!");
		final Collection<ClassificationAttributeUnitModel> classificationAttributeUnits = classificationSystemVersionDao
				.findAttributeUnitsBySystemVersion(systemVersion);
		return classificationAttributeUnits;
	}

	@Override
	public Collection<ClassificationAttributeUnitModel> getUnitsOfTypeForSystemVersion(
			final ClassificationSystemVersionModel systemVersion, final String type)
	{
		validateParameterNotNull(systemVersion, "Parameter 'systemVersion' cannot be null!");
		validateParameterNotNull(type, "Parameter 'type' cannot be null!");
		final Collection<ClassificationAttributeUnitModel> classificationAttributeUnits = classificationSystemVersionDao
				.findUnitsOfTypeBySystemVersion(systemVersion, type);
		return classificationAttributeUnits;
	}

	@Override
	public Collection<String> getUnitTypesForSystemVersion(final ClassificationSystemVersionModel systemVersion)
	{
		validateParameterNotNull(systemVersion, "Parameter 'systemVersion' cannot be null!");
		return classificationSystemVersionDao.findUnitTypesBySystemVersion(systemVersion);
	}

	@Override
	public Collection<ClassificationClassModel> getRootClassesForSystemVersion(final ClassificationSystemVersionModel systemVersion)
	{
		validateParameterNotNull(systemVersion, "Parameter 'systemVersion' cannot be null!");
		final Collection<CategoryModel> rootCategories = categoryService.getRootCategoriesForCatalogVersion(systemVersion);
		final List<ClassificationClassModel> rootClasses = new ArrayList<ClassificationClassModel>();
		for (final CategoryModel category : rootCategories)
		{
			if (category instanceof ClassificationClassModel)
			{
				rootClasses.add((ClassificationClassModel) category);
			}
		}
		return rootClasses;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	@Required
	public void setClassificationSystemDao(final ClassificationSystemDao classificationSystemDao)
	{
		this.classificationSystemDao = classificationSystemDao;
	}

	@Required
	public void setClassificationSystemVersionDao(final ClassificationSystemVersionDao classificationSystemVersionDao)
	{
		this.classificationSystemVersionDao = classificationSystemVersionDao;
	}

}
