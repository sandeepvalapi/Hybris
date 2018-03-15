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
package de.hybris.platform.test;

import static de.hybris.platform.testframework.Assert.assertCollection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.testframework.TestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * BechtleEnumerationType is used for enums that are product properties. EnumerationValueType is used for enums that are
 * used elsewhere.
 * 
 * 
 * 
 */
@IntegrationTest
public class EnumerationTypeTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger LOG = Logger.getLogger(EnumerationTypeTest.class.getName()); //NOPMD

	private TypeManager typeManager;
	private EnumerationManager enumerationManager;
	private ComposedType typeMetaType, enumerationMetaType, enumerationValueType;

	@Before
	public void setUp() throws Exception, JaloItemNotFoundException
	{
		typeManager = jaloSession.getTypeManager();
		enumerationManager = jaloSession.getEnumerationManager();
		typeMetaType = typeManager.getRootComposedType(ComposedType.class);
		enumerationMetaType = enumerationManager.getEnumerationMetaType();
		enumerationValueType = typeManager.getRootComposedType(EnumerationValue.class);
	}

	@Test
	public void testDefaultEnumerationTypes() throws Exception
	{
		/*
		 * test meta type
		 */
		assertNotNull(enumerationMetaType);
		assertNotNull(typeMetaType);
		assertNotNull(enumerationValueType);

		assertEquals(EnumerationType.class, enumerationMetaType.getJaloClass());
		assertEquals(typeMetaType, enumerationMetaType.getComposedType());

		// create new default enum type
		EnumerationType enumType;
		assertNotNull(enumType = enumerationManager.createDefaultEnumerationType("newProductPropertyType"));
		assertNotNull(enumType);
		assertEquals("newProductPropertyType", enumType.getCode());
		assertEquals(enumerationMetaType, enumType.getComposedType());
		assertTrue(enumType.isDefaultEnum());
		assertTrue(enumType.isSorted());
		assertTrue(enumType.isResortable());
		assertEquals(enumerationValueType, enumType.getValueType());
		assertEquals(enumType.getAttributeDescriptorIncludingPrivate(EnumerationValue.SEQUENCENUMBER),
				enumType.getComparationAttribute());
		try
		{
			enumType.setComparationAttribute(enumType.getAttributeDescriptor(Type.CODE));
			fail("default enum comparation attributes shoud not be changeable");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		// create EnumerationValues for new property				
		EnumerationValue white;
		assertNotNull(white = enumerationManager.createEnumerationValue(enumType, "white"));
		assertEquals(enumType, white.getComposedType());
		assertTrue(enumType.isInstance(white));
		EnumerationValue black;
		assertNotNull(black = enumerationManager.createEnumerationValue(enumType, "black"));
		assertEquals(enumType, black.getComposedType());
		assertTrue(enumType.isInstance(black));
		assertCollection(Arrays.asList(new Object[]
		{ white, black }), enumType.getValues());

		// add property to product
		TestUtils.disableFileAnalyzer("log error expected");
		final ComposedType productType = typeManager.getRootComposedTypeForJaloClass(Product.class);
		assertNotNull(productType);
		AttributeDescriptor attributeDescriptor;
		assertNotNull(attributeDescriptor = productType.createAttributeDescriptor("colorTest", enumType,
				AttributeDescriptor.PROPERTY_FLAG | AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.OPTIONAL_FLAG | AttributeDescriptor.REMOVE_FLAG));
		assertEquals(enumType, attributeDescriptor.getAttributeType());
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void testItemEnumerationType() throws JaloItemNotFoundException, ConsistencyCheckException, JaloDuplicateCodeException
	{
		final ComposedType countryType = typeManager.getComposedType(Country.class);
		assertNotNull(countryType);

		final EnumerationType countryEnum;
		assertNotNull(countryEnum = jaloSession.getEnumerationManager().createEnumerationType("CountryEnumType", countryType));
		assertEquals(enumerationMetaType, countryEnum.getComposedType());
		assertEquals(countryType, countryEnum.getValueType());
		assertFalse(countryEnum.isDefaultEnum());
		assertFalse(countryEnum.isSorted());
		assertFalse(countryEnum.isResortable());
		assertNull(countryEnum.getComparationAttribute());
		assertCollection(Collections.EMPTY_LIST, countryEnum.getValues());
		Country county1, county2, county3;
		assertNotNull(county1 = jaloSession.getC2LManager().createCountry("enumCountry1"));
		assertNotNull(county2 = jaloSession.getC2LManager().createCountry("enumCountry2"));
		assertNotNull(county3 = jaloSession.getC2LManager().createCountry("enumCountry3"));

		county1.setComposedType(countryEnum);
		county2.setComposedType(countryEnum);
		county3.setComposedType(countryEnum);
		assertCollection(Arrays.asList(new Object[]
		{ county1, county2, county3 }), countryEnum.getValues());
		countryEnum.setComparationAttribute(countryEnum.getAttributeDescriptor(Country.ISOCODE));
		assertEquals(countryEnum.getAttributeDescriptor(Country.ISOCODE), countryEnum.getComparationAttribute());
		assertTrue(countryEnum.isSorted());
		assertFalse(countryEnum.isResortable());
		final List values = countryEnum.getValues();
		assertTrue("expected [" + county1 + "," + county2 + "," + county3 + "] but got " + values,
				values.size() == 3 && county1.equals(values.get(0)) && county2.equals(values.get(1)) && county3.equals(values.get(2)));

		try
		{
			countryEnum.sortValues(Arrays.asList(new Object[]
			{ county2, county3, county1 }));
			fail("non-default enum types without Integer comparation attribute cannot sort their values");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		try
		{
			enumerationManager.createEnumerationValue(countryEnum, "blah");
			fail("enum manager should not create default enum values for non-default enum types");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
	}
}
