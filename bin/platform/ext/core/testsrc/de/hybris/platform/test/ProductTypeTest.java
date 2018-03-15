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
import static junit.framework.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ProductTypeTest extends HybrisJUnit4TransactionalTest
{
	TypeManager typeManager;
	ComposedType productType;
	Language de, en;
	List myLanguages;

	private static final Logger log = Logger.getLogger(ProductTypeTest.class);

	@Before
	public void setUp() throws Exception, JaloItemNotFoundException
	{
		typeManager = jaloSession.getTypeManager();
		productType = typeManager.getRootComposedTypeForJaloClass(Product.class);
		de = getOrCreateLanguage("de");
		en = getOrCreateLanguage("en");
		final ComposedType locType = typeManager.getRootComposedType(Language.class);
		log.info("loc type is " + locType);
		de.setComposedType(locType);
		en.setComposedType(locType);
		myLanguages = Arrays.asList(new Object[]
		{ de, en });
		log.info("languages are " + de + " , " + en);
	}

	@Test
	public void testLocTypes() throws Exception
	{
		final ComposedType locType = typeManager.getRootComposedType(Language.class);
		assertEquals(de.getComposedType(), locType);
		assertEquals(en.getComposedType(), locType);
	}

	@Test
	public void testInstance() throws ConsistencyCheckException, JaloInvalidParameterException
	{
		final Product product = jaloSession.getProductManager().createProduct("product1");
		assertNotNull(product);
		assertEquals(productType, product.getComposedType());
	}

	@Test
	public void testCreateViaType() throws Exception
	{
		final Map attributeValues = new HashMap();
		attributeValues.put("code", "blubb");

		final Product product = (Product) productType.newInstance(attributeValues);
		assertNotNull(product);

		assertEquals("blubb", product.getCode());
	}

	/** set some attributevalues at all like EditorChildNode needs */
	@Test
	public void testSave() throws Exception
	{
		final Product product = jaloSession.getProductManager().createProduct("product");
		assertNotNull(product);

		final Map attributeValues = new HashMap();
		attributeValues.put("code", "blubb");
		final Map locMap = new HashMap();
		locMap.put(de, "de:blubber");
		locMap.put(en, "en:blubber");
		attributeValues.put("name", locMap);

		final SessionContext ctx = jaloSession.createSessionContext();
		ctx.setLanguage(null);

		for (final Iterator i = attributeValues.entrySet().iterator(); i.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) i.next();
			product.setAttribute(ctx, (String) entry.getKey(), (Serializable) entry.getValue());
		}

		assertEquals(product.getAttribute(ctx, "code"), "blubb");
		assertEquals(product.getAttribute(ctx, "name"), locMap);
		ctx.setLanguage(de);
		assertEquals(product.getAttribute(ctx, "name"), "de:blubber");
		ctx.setLanguage(en);
		assertEquals(product.getAttribute(ctx, "name"), "en:blubber");
	}

	/** set values like GenericLayoutEditorChip: no exception expected. */
	@Test
	public void testSave2() throws Exception
	{
		final Product product = jaloSession.getProductManager().createProduct("product");
		assertNotNull(product);

		for (final Iterator it = productType.getAttributeDescriptors().iterator(); it.hasNext();)
		{
			final AttributeDescriptor descriptor = (AttributeDescriptor) it.next();
			final String attributeName = descriptor.getQualifier();
			if (descriptor.isWritable() && attributeName.compareTo("code") != 0)
			{
				if (descriptor.isLocalized())
				{
					final SessionContext ctx = jaloSession.createSessionContext();
					final Map values = new HashMap();

					for (final Iterator i = myLanguages.iterator(); i.hasNext();)
					{
						final Language language = (Language) i.next();
						ctx.setLanguage(language);
						values.put(language, descriptor.getDefaultValue(ctx));
					}

					ctx.setLanguage(null);
					product.setAttribute(ctx, attributeName, values);
				}
				else
				{
					product.setAttribute(attributeName, descriptor.getDefaultValue());
				}
			}
		}
	}

	/** there's no exception during getAttributeValue */
	@Test
	public void testGetAttributeValue() throws Exception
	{
		final Product product = jaloSession.getProductManager().createProduct("product");
		assertNotNull(product);

		for (final Iterator it = productType.getAttributeDescriptors().iterator(); it.hasNext();)
		{
			final AttributeDescriptor descriptor = (AttributeDescriptor) it.next();
			if (descriptor.isLocalized())
			{
				final SessionContext ctx = jaloSession.createSessionContext();
				ctx.setLanguage(null);
				product.getAttribute(ctx, descriptor.getQualifier());
			}
			else
			{
				product.getAttribute(descriptor.getQualifier());
			}
		}

	}
}
