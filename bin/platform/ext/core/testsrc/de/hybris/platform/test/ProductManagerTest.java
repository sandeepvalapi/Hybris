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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.StopWatch;
import de.hybris.platform.util.Utilities;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ProductManagerTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger LOG = Logger.getLogger(ProductManagerTest.class.getName());

	private Product product1;
	private Product product2;
	private ProductManager productManager;

	private static final int count = 100;



	@Before
	public void setUp() throws Exception
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("ProductManagerTest.setup()");
		}
		productManager = jaloSession.getProductManager();
		product1 = productManager.createProduct("test.p11");
		product2 = productManager.createProduct("test.p22");
	}

	@Test
	public void testFindByCode()
	{
		// find productive: expected p1, p2
		Collection res = productManager.getProductsByCode("test.%");
		if (LOG.isDebugEnabled())
		{
			LOG.debug((Integer.valueOf(res.size())).toString());
		}
		assertTrue("find products by code [res=" + res + "]",
				res != null && res.size() == 2 && res.contains(product1) && res.contains(product2));
		// test failing 
		res = productManager.getProductsByCode("NO-test.%");
		assertTrue("fail-test for finding productive products by code [res=" + res + "]", res != null && res.isEmpty());
		// test failing because of staging: product exists, but is not visible
		/*
		 * res = pm.getProductsByCode( "test.p33" ); assertTrue(
		 * "fail-test for invisible (since create) products by code [res="+res+"]", res != null && res.size() == 0 );
		 */
		// -> removed staging test here <-
		// ... but in productive mode it should be found
		//jaloSession.getSessionContext().setStagingMethod( Constants.STAGING.PRODUCTIVE );
		res = productManager.getProductsByCode("test.p11");
		assertTrue("test for changed code [res=" + res + ",p1.code=" + product1.getCode() + "]", res != null && res.size() == 1
				&& res.contains(product1));
		// performance test: 100 times productive search
		final StopWatch stopWatch = new StopWatch("searching productive products by code " + count + " times ...");
		for (int i = 0; i < count; i++)
		{
			res = productManager.getProductsByCode("test.%");
		}
		stopWatch.stop();
		// --- end ---	   
	}

	@Test
	public void testGetAllProducts() throws Exception
	{
		// it should find only 2 products
		final Collection res = productManager.getAllProducts();
		assertEquals(2, res.size());
	}

	@Test
	public void testGetQuantityOfProducts() throws Exception
	{
		// it should have only 2 products
		assertEquals(2, productManager.getQuantityOfProducts());
	}

	@Test
	public void testGetAllProductsChangedAfter() throws Exception
	{
		// because this test is extremly unstable, i've changed the lines below (see: 'original')

		// original:
		// Date now = new Date();
		// Thread.sleep(1000);

		// new:
		Calendar now = Utilities.getDefaultCalendar();
		now.add(Calendar.HOUR_OF_DAY, 1);

		assertEquals(Collections.EMPTY_LIST, productManager.getAllProductsChangedAfter(now.getTime()));

		Thread.sleep(1000);

		// new:
		now = Utilities.getDefaultCalendar();

		assertFalse(product1.getCreationTime().equals(now.getTime()));

		Thread.sleep(1000);

		product1.setCode("test.p11_2");
		assertEquals("test.p11_2", product1.getCode());

		// original:
		// Thread.sleep(1000);

		assertEquals(Collections.singletonList(product1), productManager.getAllProductsChangedAfter(now.getTime()));
	}

}
