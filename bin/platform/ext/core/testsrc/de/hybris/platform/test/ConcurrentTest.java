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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class ConcurrentTest extends HybrisJUnit4Test
{
	ProductManager pm;

	private static final Logger log = Logger.getLogger(ConcurrentTest.class);

	@Test
	public void testSinglethreadCacheGetter()
	{
		final SavedQuery q = FlexibleSearch.getInstance().createSavedQuery("$$",
				TypeManager.getInstance().getComposedType(Product.class), "SELECT {PK} FROM {Product}", new HashMap());
		assertNotNull(q);
		q.getCode();
		long l = System.nanoTime();
		for (int i = 0; i < 10000000; i++)
		{
			q.getCode();
		}
		l = (System.nanoTime() - l) / 1000 / 1000;
		log.info("calling 10.000.000 times getCode(): " + l + "ms");


	}

	@Test
	public void testPrimaryKey()
	{
		final SavedQuery q = FlexibleSearch.getInstance().createSavedQuery("$$",
				TypeManager.getInstance().getComposedType(Product.class), "SELECT {PK} FROM {Product}", new HashMap());
		assertNotNull(q);
		q.getPK();
		long l = System.nanoTime();
		for (int i = 0; i < 10000000; i++)
		{
			q.getPK();
		}
		l = (System.nanoTime() - l) / 1000 / 1000;
		log.info("calling 10.000.000 times getPK(): " + l + "ms");
	}

	@Test
	public void testModificationTime() throws Exception
	{
		final SavedQuery q = FlexibleSearch.getInstance().createSavedQuery("$$",
				TypeManager.getInstance().getComposedType(Product.class), "SELECT {PK} FROM {Product}", new HashMap());
		assertNotNull(q);

		final Date oldTime = q.getModificationTime();
		Thread.sleep(2000);
		q.setCode("test");
		final Date newTime = q.getModificationTime();
		log.info("currentTime: " + new Date());
		log.info("old: " + oldTime + " ,new: " + newTime);
		assertFalse(oldTime.equals(newTime));



		long l = System.nanoTime();
		for (int i = 0; i < 10000000; i++)
		{
			q.getModificationTime();
		}
		l = (System.nanoTime() - l) / 1000 / 1000;
		log.info("calling 10.000.000 times getModificationTime(): " + l + "ms");
	}




}
