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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test for {@link ClassificationSystemVersionDao}.
 */
@IntegrationTest
public class ClassificationSystemVersionDaoTest extends ServicelayerTransactionalTest
{

	@Resource
	private ClassificationSystemVersionDao classificationSystemVersionDao;
	@Resource
	private ClassificationSystemService classificationSystemService;

	private final String noSuchCode = "noSuchCode";

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		createHardwareCatalog();
	}

	private ClassificationSystemVersionModel getSampleClassification()
	{
		return classificationSystemService.getSystemVersion("SampleClassification", "1.0");
	}

	/**
	 * Tests the findClassesByCode(systemVersion, code). One and only one ClassificationClass with code "electronics" can
	 * be found.
	 */
	@Test
	public void testFindClassesByCode()
	{
		final ClassificationSystemVersionModel classificationSystemVersion = getSampleClassification();
		Collection<ClassificationClassModel> classificationClasses = classificationSystemVersionDao.findClassesByCode(
				classificationSystemVersion, "electronics");
		assertEquals(1, classificationClasses.size());
		assertEquals("wrong ClassificationClass [electronics] found", "electronics", classificationClasses.iterator().next()
				.getCode());
		classificationClasses = classificationSystemVersionDao.findClassesByCode(classificationSystemVersion, noSuchCode);
		assertEquals(0, classificationClasses.size());
	}

	/**
	 * Tests the findAttributesByCode(systemVersion, code). One and only one ClassificationAttribute with code "weight"
	 * can be found.
	 */
	@Test
	public void testFindAttributesByCode()
	{
		final ClassificationSystemVersionModel classificationSystemVersion = getSampleClassification();
		Collection<ClassificationAttributeModel> classificationAttributes = classificationSystemVersionDao.findAttributesByCode(
				classificationSystemVersion, "weight");
		assertEquals(1, classificationAttributes.size());
		assertEquals("wrong ClassificationAttribute [weight] found", "weight", classificationAttributes.iterator().next().getCode());
		classificationAttributes = classificationSystemVersionDao.findAttributesByCode(classificationSystemVersion, noSuchCode);
		assertEquals(0, classificationAttributes.size());
	}

	/**
	 * Tests the findAttributeValuesByCode(systemVersion, code). One and only one ClassificationAttributeValue with code
	 * "S_370" can be found.
	 */
	@Test
	public void testFindAttributeValuesByCode()
	{
		final ClassificationSystemVersionModel classificationSystemVersion = getSampleClassification();
		Collection<ClassificationAttributeValueModel> classificationAttributeValues = classificationSystemVersionDao
				.findAttributeValuesByCode(classificationSystemVersion, "S_370");
		assertEquals(1, classificationAttributeValues.size());
		assertEquals("wrong ClassificationAttributeValue [S_370] found", "S_370", classificationAttributeValues.iterator().next()
				.getCode());
		classificationAttributeValues = classificationSystemVersionDao.findAttributeValuesByCode(classificationSystemVersion,
				noSuchCode);
		assertEquals(0, classificationAttributeValues.size());
	}

	/**
	 * Tests the findAttributeUnitsByCode(systemVersion, code). One and only one ClassificationAttributeUnit with code
	 * "minute" can be found.
	 */
	@Test
	public void testFindAttributeUnitsByCode()
	{
		final ClassificationSystemVersionModel classificationSystemVersion = getSampleClassification();
		Collection<ClassificationAttributeUnitModel> classificationAttributeUnits = classificationSystemVersionDao
				.findAttributeUnitsByCode(classificationSystemVersion, "minute");
		assertEquals(1, classificationAttributeUnits.size());
		assertEquals("wrong ClassificationAttributeUnit [minute] found", "minute", classificationAttributeUnits.iterator().next()
				.getCode());
		classificationAttributeUnits = classificationSystemVersionDao.findAttributeUnitsByCode(classificationSystemVersion,
				noSuchCode);
		assertEquals(0, classificationAttributeUnits.size());
	}

	/**
	 * Tests the findAttributeUnitsBySystemVersion(systemVersion). There will be 26 ClassificationAttributeUnits found.
	 */
	@Test
	public void testFindAttributeUnitsBySystemVersion()
	{
		final ClassificationSystemVersionModel classificationSystemVersion = getSampleClassification();
		final Collection<ClassificationAttributeUnitModel> allClassificationAttributeUnits = classificationSystemVersionDao
				.findAttributeUnitsBySystemVersion(classificationSystemVersion);
		assertEquals(26, allClassificationAttributeUnits.size());
	}

	/**
	 * Tests the findConvertibleUnits(attributeUnit). There will be 3 units found for the attribute unit "sec", and 2
	 * units for the attribute unit "kg".
	 */
	@Test
	public void testFindConvertibleUnits()
	{
		final ClassificationSystemVersionModel classificationSystemVersion = getSampleClassification();
		ClassificationAttributeUnitModel attributeUnit = classificationSystemService.getAttributeUnitForCode(
				classificationSystemVersion, "sec");
		List<String> expectedUnitCodes = Arrays.asList("minute", "hour", "day");
		testFindConvertibleUnits(attributeUnit, expectedUnitCodes);

		attributeUnit = classificationSystemService.getAttributeUnitForCode(classificationSystemVersion, "kg");
		expectedUnitCodes = Arrays.asList("g", "t");
		testFindConvertibleUnits(attributeUnit, expectedUnitCodes);
	}

	private void testFindConvertibleUnits(final ClassificationAttributeUnitModel attributeUnit,
			final List<String> expectedUnitCodes)
	{
		final Collection<ClassificationAttributeUnitModel> units = classificationSystemVersionDao
				.findConvertibleUnits(attributeUnit);
		final List<String> unitCodes = new ArrayList<String>();
		for (final ClassificationAttributeUnitModel unit : units)
		{
			unitCodes.add(unit.getCode());
		}
		assertEquals("incorrect size of all unit types", expectedUnitCodes.size(), unitCodes.size());
		assertTrue(expectedUnitCodes.containsAll(unitCodes));
		assertTrue(unitCodes.containsAll(expectedUnitCodes));
	}

	/**
	 * Tests the testFindUnitsOfTypeBySystemVersion(systemVersion, type). There will be 4 unit types found with type
	 * "time".
	 */
	@Test
	public void testFindUnitsOfTypeBySystemVersion()
	{
		final ClassificationSystemVersionModel classificationSystemVersion = getSampleClassification();
		final Collection<ClassificationAttributeUnitModel> attributeUnits = classificationSystemVersionDao
				.findUnitsOfTypeBySystemVersion(classificationSystemVersion, "time");
		final List<String> unitCodes = new ArrayList<String>();
		for (final ClassificationAttributeUnitModel unit : attributeUnits)
		{
			unitCodes.add(unit.getCode());
		}
		final List<String> expectedUnitCodes = Arrays.asList("sec", "minute", "hour", "day");
		assertEquals("incorrect size of all unit types", expectedUnitCodes.size(), unitCodes.size());
		assertTrue(expectedUnitCodes.containsAll(unitCodes));
		assertTrue(unitCodes.containsAll(expectedUnitCodes));
	}

	/**
	 * Tests the findUnitTypesBySystemVersion(systemVersion). There will be 11 different unit types found.
	 */
	@Test
	public void testFindUnitTypesBySystemVersion()
	{
		final ClassificationSystemVersionModel classificationSystemVersion = getSampleClassification();
		final Collection<String> unitTypes = classificationSystemVersionDao
				.findUnitTypesBySystemVersion(classificationSystemVersion);
		final List<String> expectedUnitTypes = Arrays.asList("time", "dataSize", "weight", "voltage", "clockSpeed", "transferRate",
				"length", "temperatureCelsius", "temperatureFahrenheit", "volume", "standard");
		assertEquals("incorrect size of all unit types", expectedUnitTypes.size(), unitTypes.size());
		assertTrue(expectedUnitTypes.containsAll(unitTypes));
		assertTrue(unitTypes.containsAll(expectedUnitTypes));
	}

	/**
	 * Tests the findSystemVersions(String systemId, String systemVersion). There will be 1 classification system version
	 * "SampleClassification 1.0" found.
	 */
	@Test
	public void testFindSystemVersions()
	{
		Collection<ClassificationSystemVersionModel> classificationSystemVersion = classificationSystemVersionDao
				.findSystemVersions("NonExistingClassification", "1.0");
		assertEquals("There should be no ClassificationSystemVersionModel found", 0, classificationSystemVersion.size());

		// Call a findSystemVersions with existing systemId and systemVersion
		classificationSystemVersion = classificationSystemVersionDao.findSystemVersions("SampleClassification", "1.0");

		// ClassificiationSystemVersionModels Collection should have 1 element of version 1.0
		assertEquals(1, classificationSystemVersion.size());
		assertEquals("wrong ClassificationSystemVersion found", "1.0", classificationSystemVersion.iterator().next().getVersion());
	}

}
