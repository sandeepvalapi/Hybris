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
package de.hybris.platform.catalog.jalo;

import static de.hybris.platform.testframework.Assert.assertCollectionElements;
import static junit.framework.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ProductReferenceTest extends HybrisJUnit4TransactionalTest
{
	private CatalogManager cManager;
	private Product product1, product2, product3, product4, product5, product6;
	private ProductReference productReference12, productReference13, productReference56;

	@Before
	public void setUp() throws Exception
	{
		cManager = (CatalogManager) jaloSession.getExtensionManager().getExtension(CatalogConstants.EXTENSIONNAME);
	}

	@Test
	public void testProductReferences() throws Exception
	{
		// products		
		product1 = createProduct("product01", "product01Name", "product01Description");
		assertNotNull(product1);
		product2 = createProduct("product02", "product02Name", "product02Description");
		assertNotNull(product2);
		product3 = createProduct("product03", "product03Name", "product03Description");
		assertNotNull(product3);
		product4 = createProduct("product04", "product04Name", "product04Description");
		assertNotNull(product4);
		product5 = createProduct("product05", "product05Name", "product05Description");
		assertNotNull(product5);
		product6 = createProduct("product06", "product06Name", "product06Description");
		assertNotNull(product6);

		assertNotNull(productReference12 = cManager.createProductReference("sparepart", product1, product2, null));
		assertNotNull(productReference13 = cManager.createProductReference("diff_orderunit", product1, product3, null));
		assertNotNull(productReference56 = cManager
				.createProductReference("diff_orderunit", product5, product6, Integer.valueOf(6)));

		assertCollectionElements(cManager.getProductReferences(null, null, null), productReference12, productReference13,
				productReference56);
		assertCollectionElements(cManager.getProductReferences(null, product1, null), productReference12, productReference13);
		assertCollectionElements(cManager.getProductReferences("diff_orderunit", null, null), productReference13,
				productReference56);
		assertCollectionElements(cManager.getProductReferences("diff_orderunit", product1, product3), productReference13);
	}

	protected Product createProduct(final String id, final String name, final String desc) throws Exception
	{
		final ComposedType type = jaloSession.getTypeManager().getComposedType(Product.class);
		final Map values = new HashMap();
		values.put(Product.CODE, id);
		values.put(Product.NAME, name);
		values.put(Product.DESCRIPTION, desc);
		return (Product) type.newInstance(values);
	}

}
