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

import static de.hybris.platform.jalo.type.AttributeDescriptor.INITIAL_FLAG;
import static de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG;
import static de.hybris.platform.jalo.type.AttributeDescriptor.PARTOF_FLAG;
import static de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG;
import static de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG;
import static de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.ItemCloneCreator;
import de.hybris.platform.jalo.type.ItemCloneCreator.CannotCloneException;
import de.hybris.platform.jalo.type.ItemCloneCreator.CopyContext;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Test generic cloning mechanism via {@link ItemCloneCreator}.
 */
@IntegrationTest
public class GenericItemCloneTest extends HybrisJUnit4Test
{
	private Product original;
	private Media partOf1, partOf2, partOf3;

	private Unit u;

	private ComposedType adjustedProductType, adjustedMediaType;
	private CollectionType collType;

	private Map<Language, String> names;

	@Before
	public void setUp() throws Exception
	{
		final TypeManager tm = TypeManager.getInstance();
		adjustedProductType = tm.createComposedType(tm.getComposedType(Product.class), "MyProductType");

		adjustedMediaType = tm.createComposedType(tm.getComposedType(Media.class), "MyMediaType");

		collType = tm.createCollectionType("MyMediaCollType", adjustedMediaType, CollectionType.LIST);

		int modifiers = READ_FLAG + WRITE_FLAG + OPTIONAL_FLAG + PARTOF_FLAG + REMOVE_FLAG;
		adjustedProductType.createAttributeDescriptor("testMedias", collType, modifiers);

		modifiers = READ_FLAG + WRITE_FLAG + OPTIONAL_FLAG + REMOVE_FLAG;
		adjustedProductType.createAttributeDescriptor("testMediaRef", adjustedMediaType, modifiers);

		modifiers = READ_FLAG + INITIAL_FLAG + REMOVE_FLAG;
		adjustedMediaType.createAttributeDescriptor("testProd", adjustedProductType, modifiers);

		modifiers = READ_FLAG + WRITE_FLAG + OPTIONAL_FLAG + REMOVE_FLAG;
		adjustedMediaType.createAttributeDescriptor("testCrossRef", adjustedMediaType, modifiers);

		u = ProductManager.getInstance().createUnit("blah", "fasel");

		names = new HashMap<Language, String>();
		names.put(getOrCreateLanguage("de"), "name-de");
		names.put(getOrCreateLanguage("en"), "name-en");

		final Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(Product.CODE, "foo");
		attributes.put(Product.UNIT, u);
		attributes.put(Product.NAME, names);

		original = (Product) adjustedProductType.newInstance(null, attributes);

		attributes.clear();
		attributes.put(Media.CODE, "partOf1");
		attributes.put("testProd", original);
		partOf1 = (Media) adjustedMediaType.newInstance(null, attributes);

		attributes.clear();
		attributes.put(Media.CODE, "partOf2");
		attributes.put("testProd", original);
		partOf2 = (Media) adjustedMediaType.newInstance(null, attributes);

		attributes.clear();
		attributes.put(Media.CODE, "partOf3");
		attributes.put("testProd", original);
		partOf3 = (Media) adjustedMediaType.newInstance(null, attributes);

		original.setAttribute(null, "testMedias", Arrays.asList(partOf1, partOf2, partOf3));
		original.setAttribute(null, "testMediaRef", partOf3);

		partOf1.setAttribute("testCrossRef", partOf2);
		partOf2.setAttribute("testCrossRef", partOf3);
		partOf3.setAttribute("testCrossRef", partOf1);

	}

	@After
	public void tearDown() throws Exception
	{
		if (adjustedMediaType != null)
		{
			adjustedMediaType.getAttributeDescriptorIncludingPrivate("testProd").remove();
			adjustedMediaType.getAttributeDescriptorIncludingPrivate("testCrossRef").remove();
		}
		if (adjustedProductType != null)
		{
			adjustedProductType.getAttributeDescriptorIncludingPrivate("testMedias").remove();
			adjustedProductType.getAttributeDescriptorIncludingPrivate("testMediaRef").remove();
		}
		if (collType != null)
		{
			collType.remove();
		}
	}

	@Test
	public void testClone() throws Exception
	{
		final CopyContext ctx = new CopyContext();

		// product codes must be unique due to database index !!!
		final String newProductCode = original.getCode() + "-" + UUID.randomUUID();
		ctx.addPreset(original, Product.CODE, newProductCode);

		final String newM1Code = partOf1.getCode() + "-" + UUID.randomUUID();
		ctx.addPreset(partOf1, Media.CODE, newM1Code);

		final String newM2Code = partOf2.getCode() + "-" + UUID.randomUUID();
		ctx.addPreset(partOf2, Media.CODE, newM2Code);

		final String newM3Code = partOf3.getCode() + "-" + UUID.randomUUID();
		ctx.addPreset(partOf3, Media.CODE, newM3Code);
		// ---

		final ItemCloneCreator creator = new ItemCloneCreator();

		final Product copy = (Product) creator.copy(original, ctx);

		assertNotNull(copy);

		assertTrue(ctx.mustBeTranslated(original));
		assertTrue(ctx.mustBeTranslated(partOf1));
		assertTrue(ctx.mustBeTranslated(partOf2));
		assertTrue(ctx.mustBeTranslated(partOf3));

		assertEquals(copy, ctx.getCopy(original));

		assertEquals(newProductCode, copy.getCode());
		assertEquals(original.getAllNames(null), copy.getAllNames(null));
		assertEquals(original.getUnit(), copy.getUnit());

		final Media copy1 = (Media) ctx.getCopy(partOf1);
		final Media copy2 = (Media) ctx.getCopy(partOf2);
		final Media copy3 = (Media) ctx.getCopy(partOf3);

		assertNotNull(copy1);
		assertEquals(newM1Code, copy1.getCode());
		assertEquals(copy, copy1.getAttribute("testProd"));
		assertEquals(copy2, copy1.getAttribute("testCrossRef"));


		assertNotNull(copy2);
		assertEquals(newM2Code, copy2.getCode());
		assertEquals(copy, copy2.getAttribute("testProd"));
		assertEquals(copy3, copy2.getAttribute("testCrossRef"));

		assertNotNull(copy3);
		assertEquals(newM3Code, copy3.getCode());
		assertEquals(copy, copy3.getAttribute("testProd"));
		assertEquals(copy1, copy3.getAttribute("testCrossRef"));

		assertEquals(Arrays.asList(copy1, copy2, copy3), copy.getAttribute("testMedias"));
		assertEquals(copy3, copy.getAttribute("testMediaRef"));
	}

	@Test
	public void testClone2() throws Exception
	{
		assertEquals("foo", original.getCode());

		final CopyContext ctx2 = new CopyContext();
		// btw this will also make the product code unique!
		ctx2.addPreset(original, Product.CODE, "xxx");
		ctx2.addPreset(original, Product.UNIT, null);
		
		
		// media codes must be unique due to database index !!!
		final String newM1Code2 = partOf1.getCode() + "-" + UUID.randomUUID();
		ctx2.addPreset(partOf1, Media.CODE, newM1Code2);

		final String newM2Code2 = partOf2.getCode() + "-" + UUID.randomUUID();
		ctx2.addPreset(partOf2, Media.CODE, newM2Code2);

		final String newM3Code2 = partOf3.getCode() + "-" + UUID.randomUUID();
		ctx2.addPreset(partOf3, Media.CODE, newM3Code2);
		// ---

		final ItemCloneCreator creator = new ItemCloneCreator();

		Product copy2 = (Product) creator.copy(original, ctx2);

		assertNotNull(copy2);
		assertEquals("xxx", copy2.getCode());
		assertNull(copy2.getUnit());
	}

	@Test
	public void testCycleError()
	{
		// by making media-media cross reference mandatory it's impossible to create partOf medias !!! 
		adjustedMediaType.getAttributeDescriptorIncludingPrivate("testCrossRef").setOptional(false);

		final CopyContext ctx = new CopyContext();
		// product codes must be unique due to database index !!!
		ctx.addPreset(original, Product.CODE, original.getCode()+"-"+UUID.randomUUID());
		
		final ItemCloneCreator creator = new ItemCloneCreator();

		try
		{
			creator.copy(original, ctx);
			fail("extexted " + CannotCloneException.class.getName());
		}
		catch (final CannotCloneException e)
		{
			assertEquals(ctx, e.getCopyContext());
			assertEquals(new HashSet(Arrays.asList(original, partOf1, partOf2, partOf3)), new HashSet(e.getPendingItems()));
			assertNotNull(ctx.getCopy(original));
			assertNull(ctx.getCopy(partOf1));
			assertNull(ctx.getCopy(partOf2));
			assertNull(ctx.getCopy(partOf3));
		}
		catch (final JaloBusinessException e)
		{
			e.printStackTrace();
			fail("unexpected error " + e);
		}
	}

}
