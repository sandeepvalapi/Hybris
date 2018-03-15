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


import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TransactionProductTest extends HybrisJUnit4Test
{
	Product product;

	@Before
	public void setUp() throws Exception
	{
		product = ProductManager.getInstance().createProduct(null, "txtest");
	}

	@After
	public void tearDown() throws Exception
	{
		product.remove();
	}

	@Test
	public void testPropertyCaching() throws ConsistencyCheckException
	{
		final Transaction tx = Transaction.current();
		tx.begin();

		try
		{
			assertEquals(null, product.setProperty("xxname", "value"));
		}
		finally
		{
			tx.rollback();
		}

		assertEquals(null, product.getProperty("xxname"));
	}
}
