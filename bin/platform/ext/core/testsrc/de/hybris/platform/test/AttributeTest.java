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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AttributeTest extends HybrisJUnit4TransactionalTest
{
	private TypeManager typeManager;
	private ComposedType productType, productSubType1, productSubType2;

	@Before
	public void setUp() throws Exception, JaloItemNotFoundException
	{
		typeManager = jaloSession.getTypeManager();
		productType = typeManager.getRootComposedType(Constants.TC.Product);
		productSubType1 = typeManager.createComposedType(productType, "productSubType1");
		assertNotNull("Composed type creation results in null", productSubType1);
		productSubType2 = typeManager.createComposedType(productType, "productSubType2");
		assertNotNull("Composed type creation results in null", productSubType2);
	}

	private static final String SIFF = "siff";
	private static final String SIFF_2 = "siff2";
	private static final String SIFF_3 = "siff3";
	private static final String SIFF_4 = "siff4";
	private static final String SIFF_5 = "siff5";
	private static final String SIFF_6 = "siff6";

	@Test
	public void testRelationOrderFlagAtDescriptor()
	{
		// PrincipalGroupRelation: Principal(members,false)->(groups,false)Group
		assertRelationOrderedFlags("PrincipalGroupRelation", "groups", false, "members", false);

		// OrderDiscountRelation: AbstractOrder(orders,false)->(discounts,true)Discount
		assertRelationOrderedFlags("OrderDiscountRelation", "discounts", true, "orders", false);
	}

	private void assertRelationOrderedFlags(final String relName, final String srcQualifier, final boolean srcAttrOrderd,
			final String tgtQualifier, final boolean tgtAttrOrdered)
	{
		final RelationType p2grpRel = (RelationType) typeManager.getComposedType(relName);
		assertNotNull(p2grpRel);
		assertFalse(p2grpRel.isOneToMany());
		assertTrue(p2grpRel.isOrdered()); // yeah, silly, we should change that !!!
		final RelationDescriptor srcAttr = p2grpRel.getSourceAttributeDescriptor();
		assertEquals(srcQualifier, srcAttr.getQualifier());
		assertNotNull(srcAttr);
		assertEquals(srcAttrOrderd, srcAttr.isOrdered());
		final RelationDescriptor tgtAttr = p2grpRel.getTargetAttributeDescriptor();
		assertEquals(tgtQualifier, tgtAttr.getQualifier());
		assertNotNull(tgtAttr);
		assertEquals(tgtAttrOrdered, tgtAttr.isOrdered());
	}

	// PLA-6886
	@Test
	public void createAttributeWithEmptyQualifier() throws JaloAbstractTypeException, JaloGenericCreationException,
			JaloDuplicateQualifierException
	{
		final ComposedType productType = TypeManager.getInstance().getComposedType(Product.class);
		final ComposedType attributeType = TypeManager.getInstance().getComposedType(AttributeDescriptor.class);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(AttributeDescriptor.QUALIFIER, "");
		params.put(AttributeDescriptor.ENCLOSING_TYPE, productType);
		params.put(AttributeDescriptor.ATTRIBUTETYPE, productType);

		try
		{
			attributeType.newInstance(params);
			fail("AttributeDescriptor with empty qualifier created!!");
		}
		catch (final JaloInvalidParameterException e) //NOPMD
		{
			// OK
		}

		try
		{
			productType.createAttributeDescriptor("", productType, 0);
			fail("AttributeDescriptor with empty qualifier created!!");
		}
		catch (final JaloInvalidParameterException e) //NOPMD
		{
			// OK
		}
	}

	/**
	 * Tests bug PLA-4962
	 */
	@Test
	public void testCorruptDefaultValue() throws JaloDuplicateQualifierException, JaloInvalidParameterException,
			JaloItemNotFoundException
	{
		AttributeDescriptor attribute;
		attribute = productType.createAttributeDescriptor("testAd",
				TypeManager.getInstance().getType("localized:java.lang.String"), AttributeDescriptor.READ_FLAG
						+ AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.OPTIONAL_FLAG + AttributeDescriptor.REMOVE_FLAG);
		assertNotNull("Creation of attribute results in null", attribute);

		// now set a plain string as default value ( bypassing setDefaultValue() !)
		attribute.setProperty(AttributeDescriptor.DEFAULTVALUE, "some string");

		assertNull(attribute.getDefaultValue());
		assertNull(attribute.getDefaultValue(null));
		// self healing tested
		assertNull(attribute.getProperty(AttributeDescriptor.DEFAULTVALUE));

		// again set a plain string as default value ( bypassing setDefaultValue() !)
		attribute.setProperty(AttributeDescriptor.DEFAULTVALUE, "some string 2");

		// test setDefaultValue works even with corrupt value
		final Map<Language, String> defaultValue = Collections.singletonMap(jaloSession.getSessionContext().getLanguage(),
				"correct one");
		attribute.setDefaultValue(null, defaultValue);
		assertEquals(defaultValue, attribute.getDefaultValue(null));
		assertEquals("correct one", attribute.getDefaultValue());
	}

	@Test
	public void testPropagate() throws Exception
	{
		assertNotNull(productType.createAttributeDescriptor(SIFF, typeManager.getRootAtomicType(String.class),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG));

		exists(productSubType1, SIFF);
		exists(productSubType2, SIFF);
	}

	@Test
	public void testOverrideOK() throws Exception
	{
		final AttributeDescriptor fd1 = productSubType1.createAttributeDescriptor(SIFF_2,
				typeManager.getRootAtomicType(String.class), AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.REMOVE_FLAG);
		assertNotNull(fd1);

		notExists(productType, SIFF_2);
		exists(productSubType1, SIFF_2);
		notExists(productSubType2, SIFF_2);

		assertNotNull(productType.createAttributeDescriptor(SIFF_3, typeManager.getRootAtomicType(String.class),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG));

		assertEquals(fd1, productSubType1.getAttributeDescriptor(SIFF_2));

		exists(productType, SIFF_3);
		exists(productSubType1, SIFF_3);
		exists(productSubType2, SIFF_3);
	}

	@Test
	public void testOverrideFail() throws Exception
	{
		assertNotNull(productType.createAttributeDescriptor(SIFF, typeManager.getRootAtomicType(String.class),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG));

		final AttributeDescriptor fd1 = productSubType1.createAttributeDescriptor(SIFF_4,
				typeManager.getRootAtomicType(Integer.class), AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.REMOVE_FLAG);
		assertNotNull(fd1);

		notExists(productType, SIFF_4);
		exists(productSubType1, SIFF_4);
		notExists(productSubType2, SIFF_4);

		try
		{
			assertNotNull(productType.createAttributeDescriptor(SIFF, typeManager.getRootAtomicType(String.class),
					AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG));
			fail(JaloDuplicateQualifierException.class.getName() + " expected");
		}
		catch (final JaloDuplicateQualifierException e)
		{
			exists(productType, SIFF);
			exists(productSubType1, SIFF);
			exists(productSubType2, SIFF);
		}
	}

	@Test
	public void testPrimitiveFlag() throws JaloDuplicateQualifierException
	{
		// wrapper atomic with primitive
		AttributeDescriptor attribute = productType.createAttributeDescriptor("test", typeManager.getRootAtomicType(Integer.class),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG
						| AttributeDescriptor.PRIMITIVE_FLAG);
		assertNotNull(attribute);
		assertEquals(Integer.valueOf(0), attribute.getDefaultValue());
		assertEquals(null, attribute.getDefaultValueDefinitionString());

		// wrapper atomic without primitive
		attribute = productType.createAttributeDescriptor("test1", typeManager.getRootAtomicType(Integer.class),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG);
		assertNotNull(attribute);
		assertEquals(null, attribute.getDefaultValue());
		assertEquals(null, attribute.getDefaultValueDefinitionString());

		// wrapper atomic without primitive and optional
		attribute = productType.createAttributeDescriptor("test3", typeManager.getRootAtomicType(Integer.class),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG
						| AttributeDescriptor.OPTIONAL_FLAG);
		assertNotNull(attribute);
		assertEquals(null, attribute.getDefaultValue());
		assertEquals(null, attribute.getDefaultValueDefinitionString());

		// non-wrapper atomic with primitive
		try
		{
			attribute = productType.createAttributeDescriptor("test4", typeManager.getRootAtomicType(PK.class),
					AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG
							| AttributeDescriptor.PRIMITIVE_FLAG);
			fail("You should not be able to create a non-primitive marked as primitive");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			// OK
		}

		// non-wrapper atomic without primitive
		attribute = productType.createAttributeDescriptor("test5", typeManager.getRootAtomicType(PK.class),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG);
		assertNotNull(attribute);
		assertEquals(null, attribute.getDefaultValue());
		assertEquals(null, attribute.getDefaultValueDefinitionString());

		// composed with primitive
		try
		{
			attribute = productType.createAttributeDescriptor("test6", typeManager.getRootComposedTypeForJaloClass(Product.class),
					AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG
							| AttributeDescriptor.PRIMITIVE_FLAG);
			fail("You should not be able to create a non-atomic marked as primitive");
		}
		catch (final JaloInvalidParameterException e) //NOPMD
		{
			// OK
		}

		// composed without primitive
		attribute = productType.createAttributeDescriptor("test7", typeManager.getRootComposedTypeForJaloClass(Product.class),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG);
		assertNotNull(attribute);
		assertEquals(null, attribute.getDefaultValue());
		assertEquals(null, attribute.getDefaultValueDefinitionString());
	}

	@Test
	public void testRemove() throws Exception
	{
		assertNotNull(productSubType1.createAttributeDescriptor(SIFF_5, typeManager.getRootAtomicType(String.class),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG));

		final AttributeDescriptor attribute = productType.createAttributeDescriptor(SIFF_6,
				typeManager.getRootAtomicType(String.class), AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG
						| AttributeDescriptor.REMOVE_FLAG);
		assertNotNull(attribute);

		exists(productType, SIFF_6);
		exists(productSubType1, SIFF_6);
		exists(productSubType2, SIFF_6);

		attribute.remove();
		assertNotNull(attribute);

		notExists(productType, SIFF_5);
		exists(productSubType1, SIFF_5);
		notExists(productSubType2, SIFF_5);
	}

	@Test
	public void testGetSubAtts() throws JaloDuplicateCodeException, JaloDuplicateQualifierException
	{
		// create  type hierarchy
		final ComposedType genericItemCT = TypeManager.getInstance().getComposedType(GenericItem.class);
		final ComposedType test1Type = TypeManager.getInstance().createComposedType(genericItemCT, "test1");

		final ComposedType test2Type = TypeManager.getInstance().createComposedType(test1Type, "test2");

		final ComposedType test3Type = TypeManager.getInstance().createComposedType(test2Type, "test3");
		final AttributeDescriptor att1 = test1Type.createAttributeDescriptor("att", genericItemCT, AttributeDescriptor.READ_FLAG
				+ AttributeDescriptor.READ_FLAG + AttributeDescriptor.INITIAL_FLAG + AttributeDescriptor.REMOVE_FLAG);
		final AttributeDescriptor att2 = test2Type.getAttributeDescriptor("att");
		final AttributeDescriptor att3 = test3Type.getAttributeDescriptor("att");

		final Set<AttributeDescriptor> subAtts = att1.getSubAttributeDescriptors();
		assertTrue("getSubAttributeDescriptors returns wrong set size - " + subAtts, subAtts.size() == 1);
		assertFalse("getSubAttributeDescriptors contains super attribute " + att1 + " - " + subAtts, subAtts.contains(att1));
		assertTrue("getSubAttributeDescriptors does not contain attribute " + att2 + " - " + subAtts, subAtts.contains(att2));
		assertFalse("getSubAttributeDescriptors contains subsub attribute " + att3 + " - " + subAtts, subAtts.contains(att3));

		final Set<AttributeDescriptor> allSubAtts = att1.getAllSubAttributeDescriptors();
		assertTrue("getAllSubAttributeDescriptors returns wrong set size " + subAtts, allSubAtts.size() == 2);
		assertFalse(allSubAtts.contains(att1));
		assertTrue(allSubAtts.contains(att2));
		assertTrue(allSubAtts.contains(att3));
	}

	protected void notExists(final ComposedType type, final String qualifier)
	{
		try
		{
			type.getAttributeDescriptor(qualifier);
			fail(type.getCode() + "." + qualifier + " should not be present. " + JaloItemNotFoundException.class.getName()
					+ " expected");
		}
		catch (final JaloItemNotFoundException e)
		{
			return;
		}
	}

	protected void exists(final ComposedType type, final String qualifier) throws JaloItemNotFoundException
	{
		assertNotNull("Can not find attribute with qualifier " + qualifier, type.getAttributeDescriptor(qualifier));
	}
}
