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
package de.hybris.platform.variants.jalo;

import static de.hybris.platform.testframework.Assert.assertCollection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for extension Variants
 * 
 * 
 */
@IntegrationTest
public class VariantsTest extends ServicelayerTransactionalTest
{
	private VariantsManager manager;
	private TypeManager typeManager;

	private VariantType shoeType;
	private VariantType shirtType;
	private EnumerationType shirtSizeEnumType;
	@SuppressWarnings("unused")
	private EnumerationValue s; //NOPMD
	@SuppressWarnings("unused")
	private EnumerationValue m; //NOPMD
	@SuppressWarnings("unused")
	private EnumerationValue l; //NOPMD
	@SuppressWarnings("unused")
	private EnumerationValue xl; //NOPMD
	private EnumerationValue xxl;
	private VariantAttributeDescriptor shoeSize;
	private VariantAttributeDescriptor shoeColor;
	private VariantAttributeDescriptor shirtSize;
	private VariantAttributeDescriptor shirtColor;

	private static final Logger LOG = Logger.getLogger(VariantsTest.class);

	@Before
	public void setUp() throws Exception
	{
		manager = VariantsManager.getInstance();
		typeManager = jaloSession.getTypeManager();
		assertNotNull(shoeType = manager.createVariantType(Collections.singletonMap(VariantType.CODE, "shoe")));
		assertNotNull(shoeSize = shoeType.createVariantAttributeDescriptor("size", (AtomicType) typeManager
				.getAtomicTypesForJavaClass(String.class).iterator().next(), AttributeDescriptor.REMOVE_FLAG
				+ AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.SEARCH_FLAG));
		assertNotNull(shoeColor = shoeType.createVariantAttributeDescriptor("color", (AtomicType) typeManager
				.getAtomicTypesForJavaClass(String.class).iterator().next(), AttributeDescriptor.REMOVE_FLAG
				+ AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.SEARCH_FLAG));

		final EnumerationManager eumerationManager = EnumerationManager.getInstance();
		assertNotNull(shirtSizeEnumType = eumerationManager.createEnumerationType("shirtSize", null));
		assertNotNull(s = eumerationManager.createEnumerationValue(shirtSizeEnumType, "s"));
		assertNotNull(m = eumerationManager.createEnumerationValue(shirtSizeEnumType, "m"));
		assertNotNull(l = eumerationManager.createEnumerationValue(shirtSizeEnumType, "l"));
		assertNotNull(xl = eumerationManager.createEnumerationValue(shirtSizeEnumType, "xl"));
		assertNotNull(xxl = eumerationManager.createEnumerationValue(shirtSizeEnumType, "xxl"));
		assertNotNull(shirtType = manager.createVariantType(Collections.singletonMap(VariantType.CODE, "shirt")));
		assertNotNull(shirtSize = shirtType.createVariantAttributeDescriptor("size", shirtSizeEnumType,
				AttributeDescriptor.REMOVE_FLAG + AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG
						+ AttributeDescriptor.SEARCH_FLAG));
		assertNotNull(shirtColor = shirtType.createVariantAttributeDescriptor("color", (AtomicType) typeManager
				.getAtomicTypesForJavaClass(String.class).iterator().next(), AttributeDescriptor.REMOVE_FLAG
				+ AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.SEARCH_FLAG));
	}

	@After
	public void tearDown() throws Exception
	{
		// make sure all variants are registered fo removal properly
		if (shirtType != null && shirtType.isAlive())
		{
			for (final VariantProduct vp : (Set<VariantProduct>) shirtType.getAllInstances())
			{
				try
				{
					vp.remove();
					assertNotNull(vp);
				}
				catch (final Exception e)
				{
					LOG.error("could not remove variant " + vp + " due to " + e.getMessage());
				}
			}
		}
		if (shoeType != null && shoeType.isAlive())
		{
			for (final VariantProduct vp : (Set<VariantProduct>) shoeType.getAllInstances())
			{
				try
				{
					vp.remove();
					assertNotNull(vp);
				}
				catch (final Exception e)
				{
					LOG.error("could not remove variant " + vp + " due to " + e.getMessage());
				}
			}
		}
	}

	protected VariantProduct createShoe(final Product base, final String code, final String size, final String color)
			throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final Map attributes = new HashMap();
		attributes.put(Product.CODE, code);
		attributes.put(VariantProduct.BASEPRODUCT, base);
		attributes.put(shoeSize.getQualifier(), size);
		attributes.put(shoeColor.getQualifier(), color);

		return (VariantProduct) shoeType.newInstance(attributes);
	}

	protected VariantProduct createShirt(final Product base, final String code, final EnumerationValue size, final String color)
			throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final Map attributes = new HashMap();
		attributes.put(Product.CODE, code);
		attributes.put(VariantProduct.BASEPRODUCT, base);
		attributes.put(shirtSize.getQualifier(), size);
		attributes.put(shirtColor.getQualifier(), color);

		return (VariantProduct) shirtType.newInstance(attributes);
	}

	@Test
	public void testVariants() throws Exception
	{
		Product baseProduct2;
		VariantProduct variantProduct2;
		assertNotNull(baseProduct2 = manager.createBaseProduct("Shoe BaseProduct", shoeType.getCode()));
		assertNotNull(variantProduct2 = createShoe(baseProduct2, baseProduct2.getCode() + " (xx23)", "36 1/2", "red red wine"));

		/* final Collection baseProducts = */manager.getAllBaseProducts(jaloSession.getSessionContext());
		assertEquals(true, manager.isBaseProduct(baseProduct2));
		assertEquals(false, manager.isBaseProduct(variantProduct2));

		final VariantType vt1, vt2, vt3;
		assertNotNull(vt1 = manager.createVariantType("vt1"));
		assertNotNull(vt2 = manager.createVariantType("vt2"));
		assertNotNull(vt3 = manager.createVariantType("vt3"));


		assertNotNull(vt1);
		assertNotNull(vt2);
		assertNotNull(vt3);

		assertEquals("vt1", vt1.getCode());
		assertEquals("vt2", vt2.getCode());
		assertEquals("vt3", vt3.getCode());

		final String ATT1 = "att1";
		final String ATT2 = "att2";
		final String ATT3 = "att3";

		final Type att1Type = typeManager.getType("java.lang.String");
		final Type att2Type = typeManager.getComposedType(Unit.class);
		final Type att3Type = jaloSession.getEnumerationManager().getEnumerationType(Constants.TYPES.CreditCardTypeType);

		vt1.createVariantAttributeDescriptor(ATT1, att1Type, AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG
				+ AttributeDescriptor.OPTIONAL_FLAG);

		vt2.createVariantAttributeDescriptor(ATT1, att1Type, AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG
				+ AttributeDescriptor.OPTIONAL_FLAG);
		vt2.createVariantAttributeDescriptor(ATT2, typeManager.getComposedType(Item.class), AttributeDescriptor.READ_FLAG
				+ AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.OPTIONAL_FLAG);

		vt3.createVariantAttributeDescriptor(ATT2, att2Type, AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG
				+ AttributeDescriptor.OPTIONAL_FLAG);
		vt3.createVariantAttributeDescriptor(ATT3, att3Type, AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG
				+ AttributeDescriptor.OPTIONAL_FLAG);

		assertNotNull(vt1.getAttributeDescriptor(ATT1));
		assertEquals(ATT1, vt1.getAttributeDescriptor(ATT1).getQualifier());
		assertEquals(att1Type, vt1.getAttributeDescriptor(ATT1).getAttributeType());

		assertNotNull(vt2.getAttributeDescriptor(ATT1));
		assertEquals(ATT1, vt2.getAttributeDescriptor(ATT1).getQualifier());
		assertEquals(att1Type, vt2.getAttributeDescriptor(ATT1).getAttributeType());

		assertNotNull(vt2.getAttributeDescriptor(ATT2));
		assertEquals(ATT2, vt2.getAttributeDescriptor(ATT2).getQualifier());
		assertEquals(typeManager.getComposedType(Item.class), vt2.getAttributeDescriptor(ATT2).getAttributeType());

		assertNotNull(vt3.getAttributeDescriptor(ATT2));
		assertEquals(ATT2, vt3.getAttributeDescriptor(ATT2).getQualifier());
		assertEquals(att2Type, vt3.getAttributeDescriptor(ATT2).getAttributeType());

		assertNotNull(vt3.getAttributeDescriptor(ATT3));
		assertEquals(ATT3, vt3.getAttributeDescriptor(ATT3).getQualifier());
		assertEquals(att3Type, vt3.getAttributeDescriptor(ATT3).getAttributeType());

		final Map query = new HashMap();
		query.put(ATT1, att1Type);

		assertCollection(Arrays.asList(new Object[]
		{ vt1, vt2 }), manager.getVariantTypesByAttributes(query, false));
		assertCollection(Arrays.asList(new Object[]
		{ vt1 }), manager.getVariantTypesByAttributes(query, true));

		query.put(ATT2, att2Type);
		assertCollection(Collections.singletonList(vt2), manager.getVariantTypesByAttributes(query, false));

		query.clear();
		query.put(ATT2, att2Type);
		assertCollection(Arrays.asList(new Object[]
		{ vt2, vt3 }), manager.getVariantTypesByAttributes(query, false));

		query.put(ATT3, att3Type);
		assertCollection(Collections.singletonList(vt3), manager.getVariantTypesByAttributes(query, false));
		assertCollection(Collections.singletonList(vt3), manager.getVariantTypesByAttributes(query, true));

		query.put(ATT1, att1Type);
		assertEquals(Collections.EMPTY_LIST, manager.getVariantTypesByAttributes(query, false));

	}

	// see PLA-5083 - put here since variants provide easy test case ;)
	@Test
	public void testAttributeAccessorClash() throws JaloInvalidParameterException, JaloBusinessException
	{
		VariantAttributeDescriptor ad1, ad2;

		// shoe.foo as double
		assertNotNull(ad1 = shoeType.createVariantAttributeDescriptor("foo",
				(AtomicType) typeManager.getAtomicTypesForJavaClass(Double.class).iterator().next(), AttributeDescriptor.REMOVE_FLAG
						+ AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.SEARCH_FLAG));
		// shirt.foo as localized:double
		assertNotNull(ad2 = shirtType.createVariantAttributeDescriptor("foo", typeManager.getType("localized:java.lang.Double"),
				AttributeDescriptor.REMOVE_FLAG + AttributeDescriptor.READ_FLAG + AttributeDescriptor.WRITE_FLAG
						+ AttributeDescriptor.SEARCH_FLAG));
		assertFalse(ad1.isLocalized());
		assertTrue(ad2.isLocalized());

		final SessionContext ctx = new SessionContext(jaloSession.getSessionContext());

		ctx.setLanguage(null);

		Product base1, base2;
		VariantProduct variantProduct1, variantProduct2;

		assertNotNull(base1 = manager.createBaseProduct("Shoe Clash Base", shoeType.getCode()));
		assertNotNull(base2 = manager.createBaseProduct("Shirt Clash Base", shirtType.getCode()));

		assertEquals(shoeType, VariantsManager.getInstance().getVariantType(base1));
		assertEquals(shirtType, VariantsManager.getInstance().getVariantType(base2));

		assertNotNull(variantProduct1 = createShoe(base1, base1.getCode() + " juhu", "trallal", "jaja"));
		assertNotNull(variantProduct2 = createShirt(base2, base2.getCode() + " juhu", xxl, "jaja"));

		assertEquals(shoeType, variantProduct1.getComposedType());
		assertEquals(shirtType, variantProduct2.getComposedType());

		assertNull(variantProduct1.getAttribute("foo"));
		assertNull(variantProduct2.getAttribute("foo"));

		variantProduct1.setAttribute(ctx, "foo", new Double(123.4));
		// setting single value using session language shouldn't be a problem
		variantProduct2.setAttribute("foo", new Double(123.4));
		assertEquals(new Double(123.4), variantProduct2.getAttribute("foo"));

		try
		{
			// setting a single value without language should trigger an exception
			// if the correct localized access is being used (instead of the incorrect unlocalized one )
			variantProduct2.setAttribute(ctx, "foo", new Double(123.4));
			fail("exception expected");
		}
		catch (final Exception e)
		{
			// fine here
		}

		final Language testLang1 = getOrCreateLanguage("testLang1");
		final Language testLang2 = getOrCreateLanguage("testLang1");

		final Map values = new HashMap();
		values.put(testLang1, new Double(1.0));
		values.put(testLang2, new Double(2.0));
		variantProduct2.setAttribute(ctx, "foo", values);

		// since setAttribute( .. Map ) doesnt remove values for other languages we have to
		// add previously set value too
		values.put(jaloSession.getSessionContext().getLanguage(), new Double(123.4));

		assertEquals(new Double(123.4), variantProduct1.getAttribute("foo"));
		assertEquals(new Double(123.4), variantProduct2.getAttribute("foo")); // using session context lang
		assertEquals(values, variantProduct2.getAttribute(ctx, "foo"));
	}

}
