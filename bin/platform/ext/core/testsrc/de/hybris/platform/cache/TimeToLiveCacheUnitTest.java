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
package de.hybris.platform.cache;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.StandardSearchResult;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class TimeToLiveCacheUnitTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(TimeToLiveCacheUnitTest.class.getName());

	private static final int TTL_TIME = 10; // sec
	private static final String EXAMPLE_PRODUCT_1 = "ExampleProduct_1";

	private Product product;

	@Before
	public void setUp()
	{
		product = ProductManager.getInstance().createProduct(EXAMPLE_PRODUCT_1);
	}

	@Test
	public void testTTLForItemWrappedResults()
	{
		assertNotNull(product);

		final FlexibleSearch flexibleSearch = FlexibleSearch.getInstance();

		final String query = "SELECT {p:" + Item.PK + "} FROM {Product AS p} WHERE {pk}=?pk";
		final Map params = Collections.singletonMap("pk", product.getPK());

		final SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
		ctx.setAttribute(FlexibleSearch.CACHE_TTL, Integer.valueOf(TTL_TIME));// set TTL

		final long time0 = System.currentTimeMillis();
		final StandardSearchResult<Product> rsFirst = (StandardSearchResult<Product>) flexibleSearch.search(ctx, query, params,
				Product.class);
		final long time1 = System.currentTimeMillis();

		final long tOutMin = time0 + (TTL_TIME * 1000);
		final long tOutMax = time1 + (TTL_TIME * 1000);

		assertFalse(rsFirst.isFromCache()); //first raw
		assertEquals(Collections.singletonList(product), rsFirst.getResult());

		final StandardSearchResult rsSecond = (StandardSearchResult<Product>) flexibleSearch.search(ctx, query, params,
				Product.class);

		assertTrue(rsSecond.isFromCache()); //second from cache
		assertNotSame(rsFirst, rsSecond);
		assertEqualsExceptFromCacheFlag(rsFirst, rsSecond);
		assertEquals(Collections.singletonList(product), rsSecond.getResult());

		// cause invalidation by changing description
		product.setDescription("Some changes there ....");

		if (System.currentTimeMillis() > tOutMin)
		{
			LOG.warn("cannot continue testing since TTL time has been exceeded");
		}
		final StandardSearchResult rsThird = (StandardSearchResult<Product>) flexibleSearch.search(ctx, query, params,
				Product.class);

		assertTrue(rsThird.isFromCache()); //third from cache too, because of TTL
		assertNotSame(rsFirst, rsThird);
		assertEqualsExceptFromCacheFlag(rsFirst, rsThird);
		assertEquals(Collections.singletonList(product), rsThird.getResult());

		// search without TTL -> should get fresh result
		final StandardSearchResult rsThirdNormal = (StandardSearchResult<Product>) flexibleSearch.search(query, params,
				Product.class);

		assertFalse(rsThirdNormal.isFromCache());
		assertNotSame(rsFirst, rsThirdNormal);
		assertEquals(Collections.singletonList(product), rsThirdNormal.getResult());

		try
		{
			Thread.sleep((tOutMax - System.currentTimeMillis()) + 100); // wait until TTL time has passed for sure
		}
		catch (final InterruptedException e)
		{
			// don't care
		}

		assertTrue(System.currentTimeMillis() > tOutMax);

		// now we should get a new result 
		final StandardSearchResult rsFourth = (StandardSearchResult<Product>) flexibleSearch.search(ctx, query, params,
				Product.class);

		assertFalse(rsFourth.isFromCache()); //any way from cache because of TTL 
		assertEquals(Collections.singletonList(product), rsFourth.getResult());

	}





	private void assertEqualsExceptFromCacheFlag(final StandardSearchResult<Product> rsFirst, final StandardSearchResult rsSecond)
	{
		final boolean before = rsSecond.isFromCache();
		try
		{
			rsSecond.setFromCache(rsFirst.isFromCache());
			//assertTrue(EqualsBuilder.reflectionEquals(rsFirst, rsSecond));
			assertEquals(rsFirst, rsSecond);
		}
		finally
		{
			rsSecond.setFromCache(before);
		}
	}
}
