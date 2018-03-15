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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchConstants;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SearchContext;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.test.TestItem;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.type.TypeManagerManaged;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ComposedTypeTest extends HybrisJUnit4TransactionalTest
{
	private TypeManager typeManager;
	private ComposedType typeType;
	private ComposedType composedTypeType;
	private ComposedType atomicTypeType;
	private ComposedType itemType;
	private ComposedType attributeDescriptorType;
	private ComposedType typeManagerManagedType;

	@Before
	public void setUp() throws Exception, JaloItemNotFoundException
	{
		typeManager = jaloSession.getTypeManager();
		typeType = typeManager.getComposedType(Type.class);
		attributeDescriptorType = typeManager.getComposedType(AttributeDescriptor.class);
		composedTypeType = typeManager.getComposedType(ComposedType.class);
		atomicTypeType = typeManager.getComposedType(AtomicType.class);
		itemType = typeManager.getComposedType(Item.class);
		typeManagerManagedType = typeManager.getComposedType(TypeManagerManaged.class);
	}

	@Test
	public void testCreationWithSessionContext() throws ConsistencyCheckException, JaloGenericCreationException,
			JaloAbstractTypeException
	{
		final ComposedType countryType = jaloSession.getTypeManager().getComposedType(Country.class);
		assertNotNull(countryType);

		Language lang;
		assertNotNull(lang = jaloSession.getC2LManager().createLanguage("lang"));

		final SessionContext ctx = jaloSession.createSessionContext();
		ctx.setLanguage(lang);

		final Map values = new HashMap();
		values.put(Country.ISOCODE, "cIsoCode");
		values.put(Country.NAME, "langName");

		Country county;
		assertNotNull(county = (Country) countryType.newInstance(ctx, values));
		assertNotNull(county);
		assertEquals("cIsoCode", county.getIsoCode());
		assertEquals("langName", county.getName(ctx));


		final Map values2 = new HashMap();
		values2.put(Country.ISOCODE, "cIsoCode2");
		final Map names = new HashMap();
		names.put(lang, "langName2");
		values2.put(Country.NAME, names);

		ctx.setLanguage(null);
		Country county2;
		assertNotNull(county2 = (Country) countryType.newInstance(ctx, values2));
		assertNotNull(county2);
		assertEquals("cIsoCode2", county2.getIsoCode());
		ctx.setLanguage(lang);
		assertEquals("langName2", county2.getName(ctx));
	}

	@Test
	public void testDefaulValues() throws JaloGenericCreationException, JaloAbstractTypeException, JaloInvalidParameterException,
			JaloSecurityException
	{
		final ComposedType testItemType = jaloSession.getTypeManager().getComposedType(TestItem.class);
		assertNotNull(testItemType);
		final AttributeDescriptor primLongAD = testItemType.getAttributeDescriptor("primitiveLong");
		assertNotNull(primLongAD);
		assertEquals(Long.valueOf(12345), primLongAD.getDefaultValue());
		final AttributeDescriptor stringAD = testItemType.getAttributeDescriptor("string");
		assertEquals("Blah", stringAD.getDefaultValue());
		final Map defaultValues = testItemType.getAllDefaultValues();
		assertEquals("Blah", defaultValues.get("string"));
		assertEquals(Long.valueOf(12345), defaultValues.get("primitiveLong"));

		TestItem testItem;
		assertNotNull(testItem = (TestItem) testItemType.newInstance(Collections.EMPTY_MAP));
		assertNotNull(testItem);
		assertEquals(12345L, testItem.getPrimitiveLong());
		assertEquals(Long.valueOf(12345), testItem.getAttribute("primitiveLong"));
		assertEquals("Blah", testItem.getString());
		assertEquals("Blah", testItem.getAttribute("string"));
		assertNull(testItem.getA());
		assertNull(testItem.getB());
		assertFalse(testItem.getPrimitiveBoolean());
		assertNull(testItem.getDate());
	}

	@Test
	public void testItemType()
	{
		assertEquals(Item.class, itemType.getJaloClass());
		assertTrue(itemType.isAbstract());
		assertEquals(0, itemType.getItemTypeCode());
	}

	@Test
	public void testAtomicTypeType() throws JaloItemNotFoundException, JaloAbstractTypeException
	{
		assertEquals(AtomicType.class, atomicTypeType.getJaloClass());
		assertNotNull(atomicTypeType);
		assertEquals(typeType, atomicTypeType.getSuperType());
		assertEquals(Constants.TC.AtomicType, atomicTypeType.getItemTypeCode());
		assertFalse(atomicTypeType.isAbstract());
		final AttributeDescriptor attributeDescriptor = atomicTypeType.getAttributeDescriptor("javaClass");
		assertEquals("javaClass", attributeDescriptor.getQualifier());
		assertEquals(typeManager.getRootAtomicType(Class.class), attributeDescriptor.getAttributeType());
		assertEquals(atomicTypeType, attributeDescriptor.getEnclosingType());
	}

	@Test
	public void testTypeType() throws JaloItemNotFoundException
	{
		assertEquals(Type.class, typeType.getJaloClass());
		assertEquals(typeManagerManagedType, typeType.getSuperType());
		/*
		 * ComposedType mapTypeType = typeManager.getRootComposedType(MapType.class); ComposedType collectionTypeType =
		 * typeManager.getRootComposedType(CollectionType.class); ComposedType productTypeType =
		 * typeManager.getRootComposedType(ProductType.class); assertCollectionElements( composedTypeType, atomicTypeType,
		 * mapTypeType, collectionTypeType, productTypeType, typeType.getSubTypes() );
		 */
	}

	@Test
	public void testComposedType() throws JaloItemNotFoundException
	{
		assertEquals(ComposedType.class, composedTypeType.getJaloClass());
		assertEquals(typeType, composedTypeType.getSuperType());
		final AttributeDescriptor attributeDescriptor = composedTypeType.getAttributeDescriptor("declaredattributedescriptors");
		assertTrue("found " + attributeDescriptor.getAttributeType() + " (" + attributeDescriptor.getAttributeType().getClass()
				+ ")", attributeDescriptor.getAttributeType() instanceof CollectionType);
		assertEquals(attributeDescriptorType, ((CollectionType) attributeDescriptor.getAttributeType()).getElementType());
	}

	@Test
	public void testSearch() throws Exception
	{
		final SearchContext ctx = jaloSession.createSearchContext();
		final GenericQuery query = new GenericQuery(composedTypeType.getCode());
		query.addCondition(GenericCondition.createEqualCondition(new GenericSearchField(composedTypeType.getCode(), "code"),
				atomicTypeType.getCode()));
		final SearchResult result = jaloSession.search(query, ctx);
		assertCollection(new HashSet(Arrays.asList(new Object[]
		{ atomicTypeType })), new HashSet(result.getResult()));
	}

	@Test
	public void testSearchWithRange() throws Exception
	{
		final SearchContext ctx = jaloSession.createSearchContext();
		ctx.setRange(0, 15);
		final SearchResult result = jaloSession.search(new GenericQuery(composedTypeType.getCode()), ctx);
		assertEquals("but we searched only for 15 items", 15, result.getCount());
		if (result.getTotalCount() > 30)
		{
			ctx.setRange(15, 15);
			final SearchResult result2 = jaloSession.search(new GenericQuery(composedTypeType.getCode()), ctx);
			assertEquals("but we searched only for 15 items", 15, result2.getCount());
			assertEquals("total count should be equal", result.getTotalCount(), result2.getTotalCount());
		}
	}

	@Test
	public void testSearchWithRangeAndSubTypes() throws Exception
	{
		final SearchContext ctx = jaloSession.createSearchContext();
		final GenericQuery query = new GenericQuery(composedTypeType.getCode());
		ctx.setProperty(GenericSearchConstants.USE_SUBTYPES, Boolean.TRUE);
		ctx.setRange(0, 15);
		final SearchResult result = jaloSession.search(query, ctx);
		assertEquals("but we searched only for 15 items", 15, result.getCount());

		//we assume that there are more than 15 types in system
		ctx.setRange(15, 15);
		final SearchResult result2 = jaloSession.search(query, ctx);
		assertEquals("but we searched only for 15 items", 15, result2.getCount());
		assertEquals("total count should be equal", result.getTotalCount(), result2.getTotalCount());

	}

	@Test
	public void testSortedSearch() throws Exception
	{
		final SearchContext ctx = jaloSession.createSearchContext();
		final GenericQuery query = new GenericQuery(composedTypeType.getCode());
		query.addCondition(GenericCondition.createLikeCondition(new GenericSearchField(composedTypeType.getCode(), "code"), "%"));
		query.addOrderBy(new GenericSearchOrderBy(new GenericSearchField(composedTypeType.getCode(), "code"), true));
		jaloSession.search(query, ctx);
	}

	@Test
	public void testSingleton() throws JaloBusinessException, JaloSecurityException
	{
		assertEquals(false, composedTypeType.isSingleton());
		try
		{
			composedTypeType.setSingleton(true);
			fail("ComposedTypeType could be made singleton");
		}
		catch (final JaloInvalidParameterException e)
		{
			//fine here
		}
		finally
		{
			composedTypeType.setSingleton(false);
		}
	}

	@Test
	public void testInvalidationAtSetComposedType() throws Exception
	{
		final ComposedType productType = TypeManager.getInstance().getComposedType(Product.class);
		final ComposedType subProductType = TypeManager.getInstance().createComposedType(productType, "subProductType");
		assertNotNull(subProductType);
		final ComposedType subSubProductType = TypeManager.getInstance().createComposedType(subProductType, "subSubProductType");
		assertNotNull(subSubProductType);

		Product myProduct = (Product) productType.newInstance(Collections.singletonMap(Product.CODE, "1"));
		assertNotNull(myProduct);
		assertEquals(productType, myProduct.getComposedType());

		myProduct = (Product) myProduct.setComposedType(subSubProductType);
		assertEquals(subSubProductType, myProduct.getComposedType());
		assertEquals(myProduct.getCode(), "1");

		final Product realOne = (Product) myProduct.setComposedType(subProductType);
		assertEquals(subProductType, realOne.getComposedType());

		realOne.remove();
		assertFalse(realOne.isAlive());
		assertFalse(myProduct.isAlive());
	}
}
