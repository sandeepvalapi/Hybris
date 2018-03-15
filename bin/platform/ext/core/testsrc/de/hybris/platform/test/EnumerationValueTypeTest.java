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

import static de.hybris.platform.testframework.Assert.assertCollectionElements;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.HashMap;

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
public class EnumerationValueTypeTest extends HybrisJUnit4TransactionalTest
{
	static final Logger log = Logger.getLogger(EnumerationValueTypeTest.class.getName());

	TypeManager typeManager;
	EnumerationManager enumerationManager;
	EnumerationType enumerationType;
	ComposedType enumerationValueType, newComposedType, localizableItemType, localizationType;

	@Before
	public void setUp() throws Exception, JaloItemNotFoundException
	{
		typeManager = jaloSession.getTypeManager();
		enumerationManager = jaloSession.getEnumerationManager();
		newComposedType = jaloSession.getTypeManager().getRootComposedType(ComposedType.class);
		enumerationValueType = jaloSession.getTypeManager().getRootComposedType(EnumerationValue.class);
		localizableItemType = jaloSession.getTypeManager().getRootComposedType(LocalizableItem.class);
		localizationType = jaloSession.getTypeManager().getRootComposedType(Language.class);
		enumerationType = enumerationManager.createDefaultEnumerationType("EnumTestEnumerationType");
		assertNotNull(enumerationType);
	}

	@Test
	public void testEnumerationValueType() throws Exception
	{
		assertNotNull(enumerationValueType);
		assertEquals(localizableItemType, enumerationValueType.getSuperType());
		/*
		 * removed since creating enum meta type via jakarta.xml, TODO: find a way to assign the enumvalue item type the
		 * propert meta type right at creation time
		 * 
		 * assertEquals( enumerationManager.getEnumerationMetaType(), enumerationValueType.getComposedType() );
		 */
		assertFalse("Initially declared attributedescriptor is 'code'.", enumerationValueType.getDeclaredAttributeDescriptors()
				.isEmpty());
		assertNotNull("Expected attributedescriptor 'code'.", enumerationValueType.getDeclaredAttributeDescriptor("code"));
	}

	@Test
	public void testSuperType() throws Exception
	{
		final ComposedType colorEnumerationValueType = enumerationManager.createDefaultEnumerationType("colorEnumerationValueType");
		/* conv-log */log.debug("EnumerationValueTypeTest before remove");
		assertNotNull(colorEnumerationValueType);
		log.info(enumerationValueType.getCode() + ".getType()=" + enumerationValueType.getComposedType().getCode());
		log.info(colorEnumerationValueType.getCode() + ".getType()=" + colorEnumerationValueType.getComposedType().getCode());

		/*
		 * removed since jakarta.xml generation of enum meta type TODO: use this again when there is a way to definde
		 * default values in jakarta.xml
		 * 
		 * assertEquals( jaloSession.getTypeManager().getRootComposedType(EnumerationValue.class),
		 * colorEnumerationValueType.getComposedType().getAttributeDescriptor("superType").getDefaultValue() );
		 */
	}

	@Test
	public void testCreateEnumerationValueType() throws Exception
	{
		// create
		/* conv-log */log.debug("EnumerationValueTypeTest before remove222");
		final EnumerationType colorEnumerationValueType = enumerationManager
				.createDefaultEnumerationType("colorEnumerationValueType");

		assertNotNull(colorEnumerationValueType);

		assertEquals(enumerationManager.getEnumerationMetaType(), colorEnumerationValueType.getComposedType());
		assertEquals(enumerationValueType, colorEnumerationValueType.getSuperType());
		assertTrue("Initially there are no subtypes.", colorEnumerationValueType.getSubTypes().isEmpty());

		// add values
		final EnumerationValue white = enumerationManager.createEnumerationValue(colorEnumerationValueType, "white");
		assertNotNull(white);
		final EnumerationValue blue = enumerationManager.createEnumerationValue(colorEnumerationValueType, "blue");
		assertNotNull(blue);
		final EnumerationValue red = enumerationManager.createEnumerationValue(colorEnumerationValueType, "red");
		assertNotNull(red);
		assertCollectionElements(colorEnumerationValueType.getValues(), white, blue, red);

		// add property to product
		/*
		 * AttributeDescriptor fd = productType.createAttributeDescriptor( "color", colorEnumerationValueType,
		 * Features.AttributeDescriptor.ALL ); assertNotNull(fd); assertEquals( colorEnumerationValueType, fd.getType() );
		 */
	}

	@Test
	public void testGenericCreate() throws Exception
	{
		final EnumerationType colorEnumerationValueType = jaloSession.getEnumerationManager().createDefaultEnumerationType(
				"colorEnumerationValueType");

		assertNotNull(colorEnumerationValueType);

		final HashMap cm = new HashMap();
		cm.put("itemtype", colorEnumerationValueType);
		cm.put("code", "white");
		final EnumerationValue white = (EnumerationValue) colorEnumerationValueType.newInstance(cm);
		assertNotNull(white);

		cm.put("code", "blue");
		final EnumerationValue blue = (EnumerationValue) colorEnumerationValueType.newInstance(cm);
		assertNotNull(blue);

		cm.put("code", "red");
		final EnumerationValue red = (EnumerationValue) colorEnumerationValueType.newInstance(cm);
		assertNotNull(red);

		assertCollectionElements(colorEnumerationValueType.getValues(), white, blue, red);
	}

	/** test if the values of an enumerationtype are editable */
	@Test
	public void testEditingValues() throws Exception
	{
		// create
		final EnumerationType numberEnumerationValueType = jaloSession.getEnumerationManager().createDefaultEnumerationType(
				"numberEnumerationValueType");

		assertNotNull(numberEnumerationValueType);

		// add values
		final EnumerationValue one = enumerationManager.createEnumerationValue(numberEnumerationValueType, "one");
		assertNotNull(one);
		final EnumerationValue two = enumerationManager.createEnumerationValue(numberEnumerationValueType, "blue");
		assertCollectionElements(numberEnumerationValueType.getValues(), one, two);
		assertEquals(numberEnumerationValueType, one.getComposedType());

		//remove value
		two.remove();
		assertCollectionElements(numberEnumerationValueType.getValues(), one);
		assertFalse("Removed element", numberEnumerationValueType.getValues().contains(two));

		//change value
		one.setCode("four");
		assertEquals("four", one.getCode());
	}
}
