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
package de.hybris.platform.catalog.jalo.classification;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.jalo.Item.ItemAttributeMap;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


/**
 * PLA-6677: Tests if how well we're able to store feature values holding rather large strings. For oracle this is a
 * obstacle since VARCHAR is actually limited to 4000 bytes, NVARCHAR to 2000 characters and also RAW(2000) obviously to
 * 2000 bytes.
 * 
 * Therefore we had to map {@link ProductFeature#RAWVALUE} to <code>BLOB</code> and {@link ProductFeature#STRINGVALUE}
 * to <code>CLOB</code>.
 * 
 * @author ag
 */
@IntegrationTest
public class PLA_6677_Test extends HybrisJUnit4Test
{
	Product product;

	@Before
	public void setUp()
	{
		product = ProductManager.getInstance().createProduct("PLA-6677-" + System.nanoTime());
	}

	@Test
	public void testLength1001()
	{
		testCreateFeature("testLength1001", product, 1001);
	}

	@Test
	public void testLength2001()
	{
		testCreateFeature("testLength2001", product, 2001);
	}

	@Test
	public void testLength4001()
	{
		testCreateFeature("testLength4001", product, 4001);
	}

	@Test
	public void testLength30k()
	{
		testCreateFeature("testLength30k", product, (30 * 1000) + 1);
	}

	@Test
	public void testLength64k()
	{
		testCreateFeature("testLength64k", product, (64 * 1000) + 1);
	}


	private void testCreateFeature(final String qualifier, final Product product, final int stringLength)
	{
		final String payload = createPayload(stringLength);

		final ItemAttributeMap attributes = new ItemAttributeMap();
		attributes.put(ProductFeature.PRODUCT, product);
		attributes.put(ProductFeature.QUALIFIER, qualifier);
		attributes.put(ProductFeature.VALUE, payload);

		final ProductFeature feature = CatalogManager.getInstance().createProductFeature(attributes);

		assertEquals(payload, feature.getValue());

	}

	private String createPayload(final int length)
	{
		final byte[] bytes = new byte[length];
		Arrays.fill(bytes, (byte) 'x');

		return new String(bytes);
	}
}
