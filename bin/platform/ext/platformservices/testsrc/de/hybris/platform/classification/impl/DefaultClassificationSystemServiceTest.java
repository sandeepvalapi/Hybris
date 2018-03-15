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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.daos.ClassificationSystemDao;
import de.hybris.platform.classification.daos.ClassificationSystemVersionDao;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Mock test for the {@link DefaultClassificationSystemService}.
 */
@UnitTest
public class DefaultClassificationSystemServiceTest
{

	private DefaultClassificationSystemService classificationSystemService;

	@Mock
	private CategoryService mockCategoryService;
	@Mock
	private ClassificationSystemDao mockClassificationSystemDao;
	@Mock
	private ClassificationSystemVersionDao mockClassificationSystemVersionDao;
	@Mock
	private ClassificationSystemVersionModel mockClassificationSystemVersion;
	@Mock
	private ClassificationClassModel mockClassificationClass;
	@Mock
	private ClassificationAttributeModel mockClassificationAttribute;
	@Mock
	private ClassificationAttributeValueModel mockClassificationAttributeValue;
	@Mock
	private ClassificationAttributeUnitModel mockClassificationAttributeUnit;
	@Mock
	private ClassificationSystemModel mockClassificationSystemModel;
	@Mock
	private Collection<String> unitTypes;

	private final String classificationClassCode = "classCode";
	private final String classificationAttributeCode = "attributeCode";
	private final String classificationAttributeValueCode = "attributeValueCode";
	private final String classificationAttributeUnitCode = "attributeUnitCode";
	private final String type = "unitType";

	private final String classificationSystemCode = "classificationSystemCode";

	private final String classificationSystemVersion = "1.0";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		classificationSystemService = new DefaultClassificationSystemService();
		classificationSystemService.setClassificationSystemDao(mockClassificationSystemDao);
		classificationSystemService.setClassificationSystemVersionDao(mockClassificationSystemVersionDao);
		classificationSystemService.setCategoryService(mockCategoryService);
	}

	@Test
	public void testGetClassForCode()
	{
		//assumed
		when(mockClassificationSystemVersionDao.findClassesByCode(mockClassificationSystemVersion, classificationClassCode))
				.thenReturn(Collections.singletonList(mockClassificationClass));
		//test
		final ClassificationClassModel classificationClass = classificationSystemService.getClassForCode(
				mockClassificationSystemVersion, classificationClassCode);
		assertNotNull(classificationClass);
		assertEquals(classificationClass, mockClassificationClass);
	}

	@Test
	public void testGetClassForCodeWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getClassForCode(null, classificationClassCode);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

		try
		{
			classificationSystemService.getClassForCode(mockClassificationSystemVersion, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetClassForCodeWithUnknownIdentifierException()
	{
		//assumed
		when(mockClassificationSystemVersionDao.findClassesByCode(mockClassificationSystemVersion, classificationClassCode))
				.thenReturn(Collections.EMPTY_LIST);
		//test
		try
		{
			classificationSystemService.getClassForCode(mockClassificationSystemVersion, classificationClassCode);
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException uie)
		{
			//expected
		}
	}

	@Test
	public void testGetClassForCodeWithAmbiguousIdentifierException()
	{
		final ClassificationClassModel class1 = new ClassificationClassModel();
		class1.setCode(classificationClassCode);
		class1.setCatalogVersion(mockClassificationSystemVersion);
		final ClassificationClassModel class2 = new ClassificationClassModel();
		class2.setCode(classificationClassCode);
		class2.setCatalogVersion(mockClassificationSystemVersion);

		//assumed
		when(mockClassificationSystemVersionDao.findClassesByCode(mockClassificationSystemVersion, classificationClassCode))
				.thenReturn(Arrays.asList(class1, class2));
		//test
		try
		{
			classificationSystemService.getClassForCode(mockClassificationSystemVersion, classificationClassCode);
			fail("AmbiguousIdentifierException expected");
		}
		catch (final AmbiguousIdentifierException aie)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeForCode()
	{
		//assumed
		when(mockClassificationSystemVersionDao.findAttributesByCode(mockClassificationSystemVersion, classificationAttributeCode))
				.thenReturn(Collections.singletonList(mockClassificationAttribute));
		//test
		final ClassificationAttributeModel classificationAttribute = classificationSystemService.getAttributeForCode(
				mockClassificationSystemVersion, classificationAttributeCode);
		assertNotNull(classificationAttribute);
		assertEquals(classificationAttribute, mockClassificationAttribute);
	}

	@Test
	public void testGetAttributeForCodeWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getAttributeForCode(null, classificationAttributeCode);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

		try
		{
			classificationSystemService.getAttributeForCode(mockClassificationSystemVersion, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeForCodeWithUnknownIdentifierException()
	{
		//assumed
		when(mockClassificationSystemVersionDao.findAttributesByCode(mockClassificationSystemVersion, classificationAttributeCode))
				.thenReturn(Collections.EMPTY_LIST);
		//test
		try
		{
			classificationSystemService.getAttributeForCode(mockClassificationSystemVersion, classificationAttributeCode);
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException uie)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeForCodeWithAmbiguousIdentifierException()
	{
		final ClassificationAttributeModel attribute1 = new ClassificationAttributeModel();
		attribute1.setCode(classificationAttributeCode);
		attribute1.setSystemVersion(mockClassificationSystemVersion);
		final ClassificationAttributeModel attribute2 = new ClassificationAttributeModel();
		attribute2.setCode(classificationAttributeCode);
		attribute2.setSystemVersion(mockClassificationSystemVersion);

		//assumed
		when(mockClassificationSystemVersionDao.findAttributesByCode(mockClassificationSystemVersion, classificationAttributeCode))
				.thenReturn(Arrays.asList(attribute1, attribute2));
		//test
		try
		{
			classificationSystemService.getAttributeForCode(mockClassificationSystemVersion, classificationAttributeCode);
			fail("AmbiguousIdentifierException expected");
		}
		catch (final AmbiguousIdentifierException aie)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeValueForCode()
	{
		//assumed
		when(
				mockClassificationSystemVersionDao.findAttributeValuesByCode(mockClassificationSystemVersion,
						classificationAttributeValueCode)).thenReturn(Collections.singletonList(mockClassificationAttributeValue));
		//test
		final ClassificationAttributeValueModel classificationAttributeValue = classificationSystemService
				.getAttributeValueForCode(mockClassificationSystemVersion, classificationAttributeValueCode);
		assertNotNull(classificationAttributeValue);
		assertEquals(classificationAttributeValue, mockClassificationAttributeValue);
	}

	@Test
	public void testGetAttributeValueForCodeWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getAttributeValueForCode(null, classificationAttributeValueCode);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

		try
		{
			classificationSystemService.getAttributeValueForCode(mockClassificationSystemVersion, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeValueForCodeWithUnknownIdentifierException()
	{
		//assumed
		when(
				mockClassificationSystemVersionDao.findAttributeValuesByCode(mockClassificationSystemVersion,
						classificationAttributeValueCode)).thenReturn(Collections.EMPTY_LIST);
		//test
		try
		{
			classificationSystemService.getAttributeValueForCode(mockClassificationSystemVersion, classificationAttributeValueCode);
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException uie)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeValueForCodeWithAmbiguousIdentifierException()
	{
		final ClassificationAttributeValueModel attributeValue1 = new ClassificationAttributeValueModel();
		attributeValue1.setCode(classificationAttributeValueCode);
		attributeValue1.setSystemVersion(mockClassificationSystemVersion);
		final ClassificationAttributeValueModel attributeValue2 = new ClassificationAttributeValueModel();
		attributeValue2.setCode(classificationAttributeValueCode);
		attributeValue2.setSystemVersion(mockClassificationSystemVersion);

		//assumed
		when(
				mockClassificationSystemVersionDao.findAttributeValuesByCode(mockClassificationSystemVersion,
						classificationAttributeValueCode)).thenReturn(Arrays.asList(attributeValue1, attributeValue2));
		//test
		try
		{
			classificationSystemService.getAttributeValueForCode(mockClassificationSystemVersion, classificationAttributeValueCode);
			fail("AmbiguousIdentifierException expected");
		}
		catch (final AmbiguousIdentifierException aie)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeUnitForCode()
	{
		//assumed
		when(
				mockClassificationSystemVersionDao.findAttributeUnitsByCode(mockClassificationSystemVersion,
						classificationAttributeUnitCode)).thenReturn(Collections.singletonList(mockClassificationAttributeUnit));
		//test
		final ClassificationAttributeUnitModel classificationAttributeUnit = classificationSystemService.getAttributeUnitForCode(
				mockClassificationSystemVersion, classificationAttributeUnitCode);
		assertNotNull(classificationAttributeUnit);
		assertEquals(classificationAttributeUnit, classificationAttributeUnit);
	}

	@Test
	public void testGetAttributeUnitForCodeWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getAttributeUnitForCode(null, classificationAttributeUnitCode);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

		try
		{
			classificationSystemService.getAttributeUnitForCode(mockClassificationSystemVersion, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeUnitForCodeWithUnknownIdentifierException()
	{
		//assumed
		when(
				mockClassificationSystemVersionDao.findAttributeUnitsByCode(mockClassificationSystemVersion,
						classificationAttributeUnitCode)).thenReturn(Collections.EMPTY_LIST);
		//test
		try
		{
			classificationSystemService.getAttributeUnitForCode(mockClassificationSystemVersion, classificationAttributeUnitCode);
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException uie)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeUnitForCodeWithAmbiguousIdentifierException()
	{
		final ClassificationAttributeUnitModel attributeUnit1 = new ClassificationAttributeUnitModel();
		attributeUnit1.setCode(classificationAttributeUnitCode);
		attributeUnit1.setSystemVersion(mockClassificationSystemVersion);
		final ClassificationAttributeUnitModel attributeUnit2 = new ClassificationAttributeUnitModel();
		attributeUnit2.setCode(classificationAttributeUnitCode);
		attributeUnit2.setSystemVersion(mockClassificationSystemVersion);

		//assumed
		when(
				mockClassificationSystemVersionDao.findAttributeUnitsByCode(mockClassificationSystemVersion,
						classificationAttributeUnitCode)).thenReturn(Arrays.asList(attributeUnit1, attributeUnit2));
		//test
		try
		{
			classificationSystemService.getAttributeUnitForCode(mockClassificationSystemVersion, classificationAttributeUnitCode);
			fail("AmbiguousIdentifierException expected");
		}
		catch (final AmbiguousIdentifierException aie)
		{
			//expected
		}
	}

	@Test
	public void testGetAttributeUnitsForSystemVersion()
	{
		//assumed
		when(mockClassificationSystemVersionDao.findAttributeUnitsBySystemVersion(mockClassificationSystemVersion)).thenReturn(
				Collections.singletonList(mockClassificationAttributeUnit));
		//test
		final Collection<ClassificationAttributeUnitModel> classificationAttributeUnits = classificationSystemService
				.getAttributeUnitsForSystemVersion(mockClassificationSystemVersion);
		assertEquals(1, classificationAttributeUnits.size());
	}

	@Test
	public void testGetAttributeUnitsForSystemVersionWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getAttributeUnitsForSystemVersion(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetUnitsOfTypeForSystemVersion()
	{
		final ClassificationAttributeUnitModel unit1 = new ClassificationAttributeUnitModel();
		unit1.setCode("unit1");
		final ClassificationAttributeUnitModel unit2 = new ClassificationAttributeUnitModel();
		unit2.setCode("unit2");
		//assumed
		when(mockClassificationSystemVersionDao.findUnitsOfTypeBySystemVersion(mockClassificationSystemVersion, type)).thenReturn(
				Arrays.asList(unit1, unit2));
		//test
		final Collection<ClassificationAttributeUnitModel> units = classificationSystemService.getUnitsOfTypeForSystemVersion(
				mockClassificationSystemVersion, type);
		assertEquals(2, units.size());
		assertTrue(units.contains(unit1));
		assertTrue(units.contains(unit2));
	}

	@Test
	public void testGetUnitsOfTypeForSystemVersionWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getUnitsOfTypeForSystemVersion(null, type);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}

		try
		{
			classificationSystemService.getUnitsOfTypeForSystemVersion(mockClassificationSystemVersion, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetConvertibleUnits()
	{
		final ClassificationAttributeUnitModel unit1 = new ClassificationAttributeUnitModel();
		unit1.setCode("unit1");
		final ClassificationAttributeUnitModel unit2 = new ClassificationAttributeUnitModel();
		unit2.setCode("unit2");
		//assumed
		when(mockClassificationSystemVersionDao.findConvertibleUnits(mockClassificationAttributeUnit)).thenReturn(
				Arrays.asList(unit1, unit2));
		//test
		final Collection<ClassificationAttributeUnitModel> units = classificationSystemService
				.getConvertibleUnits(mockClassificationAttributeUnit);
		assertEquals(2, units.size());
		assertTrue(units.contains(unit1));
		assertTrue(units.contains(unit2));
	}

	@Test
	public void testGetConvertibleUnitsWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getConvertibleUnits(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetUnitTypesForSystemVersion()
	{
		//assumed
		when(mockClassificationSystemVersionDao.findUnitTypesBySystemVersion(mockClassificationSystemVersion)).thenReturn(
				this.unitTypes);
		//test
		final Collection<String> unitTypes = classificationSystemService
				.getUnitTypesForSystemVersion(mockClassificationSystemVersion);
		assertEquals(this.unitTypes, unitTypes);
	}

	@Test
	public void testGetUnitTypesForSystemVersionWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getUnitTypesForSystemVersion(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	@Test
	public void testGetRootClassesForSystemVersion()
	{
		final CategoryModel category1 = new CategoryModel();
		category1.setCode("category1");
		final CategoryModel class1 = new ClassificationClassModel();
		class1.setCode("class1");
		final CategoryModel category2 = new CategoryModel();
		category2.setCode("category2");
		final CategoryModel class2 = new ClassificationClassModel();
		class2.setCode("class2");
		final CategoryModel class3 = new ClassificationClassModel();
		class3.setCode("class3");
		//assumed
		when(mockCategoryService.getRootCategoriesForCatalogVersion(mockClassificationSystemVersion)).thenReturn(
				Arrays.asList(category1, class1, category2, class2, class3));
		//test
		final Collection<ClassificationClassModel> classificationClassess = classificationSystemService
				.getRootClassesForSystemVersion(mockClassificationSystemVersion);
		assertEquals(3, classificationClassess.size());
	}

	@Test
	public void testGetRootClassesForSystemVersionWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getRootClassesForSystemVersion(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae)
		{
			//expected
		}
	}

	/**
	 * Checks that an {@link IllegalArgumentException} is thrown whenever a null is used as an ID or as a system version
	 */
	@Test
	public void testGetSystemVersionWithIlegalArgumentException()
	{
		try
		{
			classificationSystemService.getSystemVersion(classificationSystemCode, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//ok, IllegalArgumentException expected
		}
		try
		{
			classificationSystemService.getSystemVersion(null, classificationSystemVersion);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//ok, IllegalArgumentException expected
		}
	}

	/**
	 * Checks that an {@link UnknownIdentifierException} is thrown whenever a non existing ClassificationSystem ID is
	 * used or a non existing system version
	 */
	@Test
	public void testGetSystemVersionWithUnknownIdentifierException()
	{
		try
		{
			classificationSystemService.getSystemVersion("NonExistingClassificationSystem", "1.1");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//ok, UnknownIdentifierException expected
		}

		try
		{
			classificationSystemService.getSystemVersion(classificationSystemCode, "1.1");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//ok, UnknownIdentifierException expected
		}

		try
		{
			classificationSystemService.getSystemVersion("NonExistingClassificationSystem", classificationSystemVersion);
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//ok, UnknownIdentifierException expected
		}
	}

	/**
	 * Checks that an {@link AmbiguousIdentifierException} is thrown whenever two existing ClassificationSystem ID are
	 * found
	 * 
	 * */
	@Test
	public void testGetSystemVersionWithAmbiguousIdentifierException()
	{
		final ClassificationSystemModel sys = new ClassificationSystemModel();
		final ClassificationSystemVersionModel sysVer = new ClassificationSystemVersionModel();
		sys.setId(classificationSystemCode);
		sysVer.setVersion(classificationSystemVersion);
		sysVer.setCatalog(sys);

		final ClassificationSystemModel sys2 = new ClassificationSystemModel();
		final ClassificationSystemVersionModel sysVer2 = new ClassificationSystemVersionModel();
		sys2.setId(classificationSystemCode);
		sysVer2.setVersion(classificationSystemVersion);
		sysVer2.setCatalog(sys);

		when(mockClassificationSystemVersionDao.findSystemVersions(classificationSystemCode, classificationSystemVersion))
				.thenReturn(Arrays.asList(sysVer, sysVer2));

		//test
		try
		{
			classificationSystemService.getSystemVersion(classificationSystemCode, classificationSystemVersion);
			fail("AmbiguousIdentifierException expected");
		}
		catch (final AmbiguousIdentifierException e)
		{
			//ok, AmbiguousIdentifierException expected
		}
	}

	/**
	 * Checks that {@link DefaultClassificationSystemService} retrieves the correct ClassificationSystemVersion for an
	 * existing ClassificationSystem ID and a systemVersion
	 */
	@Test
	public void testGetSystemVersionWithExistingClassificationsSystemVersion()
	{
		//assumed
		when(mockClassificationSystemVersionDao.findSystemVersions(classificationSystemCode, classificationSystemVersion))
				.thenReturn(Collections.singletonList(mockClassificationSystemVersion));

		//test
		final ClassificationSystemVersionModel clsSysVersion = classificationSystemService.getSystemVersion(
				classificationSystemCode, classificationSystemVersion);

		assertNotNull(clsSysVersion);
		assertEquals(mockClassificationSystemVersion, clsSysVersion);
	}

	/**
	 * Checks that an {@link IllegalArgumentException} is thrown whenever a null is used as an ID
	 */
	@Test
	public void testGetSystemWithIllegalArgumentException()
	{
		try
		{
			assertEquals(mockClassificationSystemModel, classificationSystemService.getSystemForId(null));
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//ok, IllegalArgumentException expected
		}
	}

	/**
	 * Checks that an {@link UnknownIdentifierException} is thrown whenever a non existing ClassificationSystem ID is
	 * used
	 */
	@Test
	public void testGetSystemForIdWithUnknownIdentifierException()
	{
		try
		{
			classificationSystemService.getSystemForId("NonExistingClassificationSystem");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//ok, UnknownIdentifierException expected
		}
	}

	/**
	 * Checks that an {@link AmbiguousIdentifierException} is thrown whenever two existing ClassificationSystem ID are
	 * found
	 * 
	 */
	@Test
	public void testgetSystemForIdWithAmbiguousIdentifierException()
	{
		final ClassificationSystemModel sysVer = new ClassificationSystemModel();
		sysVer.setId(classificationSystemCode);

		final ClassificationSystemModel sysVer2 = new ClassificationSystemModel();
		sysVer2.setId(classificationSystemCode);

		//assumed
		when(mockClassificationSystemDao.findSystemsById(classificationSystemCode)).thenReturn(Arrays.asList(sysVer, sysVer2));

		try
		{
			classificationSystemService.getSystemForId(classificationSystemCode);
			fail("AmbiguousIdentifierException expected");
		}
		catch (final AmbiguousIdentifierException e)
		{
			//ok, AmbiguousIdentifierException expected
		}
	}

	/**
	 * Checks that {@link DefaultClassificationSystemService} retrieves the correct ClassificationSystem for an existing
	 * ClassificationSystem ID
	 */
	@Test
	public void testGetSystemForIdWithExistingId()
	{
		//assumed
		Mockito.when(mockClassificationSystemDao.findSystemsById(classificationSystemCode)).thenReturn(
				Collections.singletonList(mockClassificationSystemModel));

		//assumed
		when(mockClassificationSystemVersionDao.findSystemVersions(classificationSystemCode, classificationSystemVersion))
				.thenReturn(Collections.singletonList(mockClassificationSystemVersion));

		final ClassificationSystemModel clsSys = classificationSystemService.getSystemForId(classificationSystemCode);
		//test
		assertNotNull(clsSys);
		assertEquals(mockClassificationSystemModel, clsSys);
	}

}
