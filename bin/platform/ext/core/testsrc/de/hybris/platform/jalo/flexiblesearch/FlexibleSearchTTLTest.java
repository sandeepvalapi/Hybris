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
package de.hybris.platform.jalo.flexiblesearch;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch.FlexibleSearchCacheKey;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.StandardSearchResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 *
 */
@IntegrationTest
public class FlexibleSearchTTLTest extends HybrisJUnit4Test
{

	private static final int FIVE_SEC_TTL = 5;

	private static final String EXAMPLE_PRODUCT_1 = "TtlSampleProduct";

	private Product product;

	@Before
	public void setUp()
	{
		product = ProductManager.getInstance().createProduct(EXAMPLE_PRODUCT_1);
	}


	@After
	public void clearSession()
	{
		Registry.getCurrentTenantNoFallback().getCache().clear();//clear cache
		JaloSession.getCurrentSession().removeLocalSessionContext();
	}


	/**
	 * test different periods for given ttl. Generation is the same during the whole test
	 */
	@Test
	public void testBothKeysHaveTtls()
	{

		assertNotNull(product);

		final FlexibleSearch flexibleSearchFirst = createFlexibleSearchWithFixedTimeStamp(1000L);

		final String query = "SELECT {p:" + Item.PK + "} FROM {Product AS p} WHERE {pk}=?pk";
		final Map params = Collections.singletonMap("pk", product.getPK());


		final SessionContext ctxWithTtl = JaloSession.getCurrentSession().createSessionContext();
		ctxWithTtl.setAttribute(FlexibleSearch.CACHE_TTL, Integer.valueOf(FIVE_SEC_TTL));// set TTL 

		final StandardSearchResult<Product> first = (StandardSearchResult<Product>) flexibleSearchFirst.search(ctxWithTtl, query,
				params, Product.class);

		assureNotFromCache(first);


		final FlexibleSearch flexibleSearchSecond = createFlexibleSearchWithFixedTimeStamp(4000L);

		final StandardSearchResult<Product> second = (StandardSearchResult<Product>) flexibleSearchSecond.search(ctxWithTtl, query,
				params, Product.class);

		assureFromCache(
				"should be in cache since life span period has not passed (4000 from 1000 with ttl of 5000) and the hascodes matches",
				second);


		final FlexibleSearch flexibleSearchThird = createFlexibleSearchWithFixedTimeStamp(5999L);

		final StandardSearchResult<Product> third = (StandardSearchResult<Product>) flexibleSearchThird.search(ctxWithTtl, query,
				params, Product.class);

		assureFromCache(
				"should be in cache since life span period has not passed (5999 from 1000 with ttl of 5000) and the hascodes matches",
				third);


		final FlexibleSearch flexibleSearchFourth = createFlexibleSearchWithFixedTimeStamp(6001L);

		final StandardSearchResult<Product> fourth = (StandardSearchResult<Product>) flexibleSearchFourth.search(ctxWithTtl, query,
				params, Product.class);

		assureFromCache(
				"should be in cache since life span period has been passed (6001 from 1000 with ttl of 5000) but generation is the same",
				fourth);

		//here in cache is the 6001 time stamp unit

		final FlexibleSearch flexibleSearchFiveth = createFlexibleSearchWithFixedTimeStamp(20001L);

		final StandardSearchResult<Product> fifth = (StandardSearchResult<Product>) flexibleSearchFiveth.search(ctxWithTtl, query,
				params, Product.class);

		assureFromCache(
				"should be in cache since life span period has been passed (6001 from 1000 with ttl of 5000) but generation is the same",
				fifth);


		final FlexibleSearch flexibleSearchSixth = createFlexibleSearchWithFixedTimeStamp(999999L);

		final StandardSearchResult<Product> sixth = (StandardSearchResult<Product>) flexibleSearchSixth.search(ctxWithTtl, query,
				params, Product.class);

		assureFromCache(
				"should be in cache since life span period has been passed (999999 from 1000 with ttl of 5000) but generation is the same",
				sixth);
	}

	/**
	 * compare against first unit inserted with timestamp 1000 with TTL
	 */
	@Test
	public void testAgainstKeysWithTtl()
	{

		assertNotNull(product);

		final FlexibleSearch flexibleSearchFirst = createFlexibleSearchWithFixedTimeStamp(1000L);

		final String query = "SELECT {p:" + Item.PK + "} FROM {Product AS p} WHERE {pk}=?pk";
		final Map params = Collections.singletonMap("pk", product.getPK());


		final SessionContext ctxWithTtl = JaloSession.getCurrentSession().createSessionContext();
		ctxWithTtl.setAttribute(FlexibleSearch.CACHE_TTL, Integer.valueOf(FIVE_SEC_TTL));// set TTL 

		final StandardSearchResult<Product> first = (StandardSearchResult<Product>) flexibleSearchFirst.search(ctxWithTtl, query,
				params, Product.class);

		assureNotFromCache(first);


		final FlexibleSearch flexibleSearchSecond = createFlexibleSearchWithFixedTimeStamp(4000L);

		final SessionContext ctxNoTtl = JaloSession.getCurrentSession().createSessionContext();
		ctxNoTtl.removeAttribute(FlexibleSearch.CACHE_TTL); //no ttl since here 

		final StandardSearchResult<Product> second = (StandardSearchResult<Product>) flexibleSearchSecond.search(ctxNoTtl, query,
				params, Product.class);

		assureNotFromCache(second);


		final FlexibleSearch flexibleSearchThird = createFlexibleSearchWithFixedTimeStamp(99999L); //no matter the time span since generation the same 

		final StandardSearchResult<Product> third = (StandardSearchResult<Product>) flexibleSearchThird.search(ctxNoTtl, query,
				params, Product.class);

		assureFromCache("no matter the time span since generation the same", third);

		//change the generation here 
		product.setDescription("a whatever");

		//stting the ttle again 
		final FlexibleSearch flexibleSearchForth = createFlexibleSearchWithFixedTimeStamp(5999L);

		//ctx.setAttribute(FlexibleSearch.CACHE_TTL, Integer.valueOf(5));// set TTL 

		final StandardSearchResult<Product> forth = (StandardSearchResult<Product>) flexibleSearchForth.search(ctxWithTtl, query,
				params, Product.class);

		assureFromCache(
				"should be in cache since life span period has not passed (5999 from 1000 with ttl of 5000) and the hascodes matches",
				forth);


		final FlexibleSearch flexibleSearchFifth = createFlexibleSearchWithFixedTimeStamp(6001L);

		final StandardSearchResult<Product> fifth = (StandardSearchResult<Product>) flexibleSearchFifth.search(ctxWithTtl, query,
				params, Product.class);

		assureNotFromCache(fifth);

	}

	@Test
	public void testCachEntriesUnlessGenerationChanges()
	{

		assertNotNull(product);

		final FlexibleSearch flexibleSearchFirst = createFlexibleSearchWithFixedTimeStamp(1000L);

		final String query = "SELECT {p:" + Item.PK + "} FROM {Product AS p} WHERE {pk}=?pk";
		final Map params = Collections.singletonMap("pk", product.getPK());


		final SessionContext ctxWithTtl = JaloSession.getCurrentSession().createSessionContext();
		ctxWithTtl.setAttribute(FlexibleSearch.CACHE_TTL, Integer.valueOf(FIVE_SEC_TTL));// set TTL 

		flexibleSearchFirst.search(ctxWithTtl, query, params, Product.class); //put in cache


		final FlexibleSearch flexibleSearchSecond = createFlexibleSearchWithFixedTimeStamp(99999L); //no matter the time span since generation the same 

		final StandardSearchResult<Product> second = (StandardSearchResult<Product>) flexibleSearchSecond.search(ctxWithTtl, query,
				params, Product.class);

		assureFromCache("no matter the time span since generation the same", second);

		//here change generation
		product.setDescription("some change here");

		product.setDescription("don't mean the change ");

		final FlexibleSearch flexibleSearchThird = createFlexibleSearchWithFixedTimeStamp(1001L);

		final StandardSearchResult<Product> third = (StandardSearchResult<Product>) flexibleSearchThird.search(ctxWithTtl, query,
				params, Product.class);

		assureFromCache("the generation changed but ttl still valid (1001 against 1000)", third);

		product.setDescription("some change here");

		product.setDescription("don't mean the change ");

		final FlexibleSearch flexibleSearchFourth = createFlexibleSearchWithFixedTimeStamp(5999L);

		final StandardSearchResult<Product> fourth = (StandardSearchResult<Product>) flexibleSearchFourth.search(ctxWithTtl, query,
				params, Product.class);

		assureFromCache("the generation changed but ttl still valid (5999 against 1000)", fourth);

		product.setDescription("some change here");

		product.setDescription("don't mean the change ");

		final FlexibleSearch flexibleSearchFiveth = createFlexibleSearchWithFixedTimeStamp(6001L);

		final StandardSearchResult<Product> fifth = (StandardSearchResult<Product>) flexibleSearchFiveth.search(ctxWithTtl, query,
				params, Product.class);

		assureNotFromCache(fifth); //generation changed and ttl passed

	}

	/**
	 * compare against first unit inserted with timestamp 1000 with TTL
	 */
	@Test
	public void testAgainstKeysWithGenerationOnly()
	{

		assertNotNull(product);

		final FlexibleSearch flexibleSearchFirst = createFlexibleSearchWithFixedTimeStamp(1000L);

		final String query = "SELECT {p:" + Item.PK + "} FROM {Product AS p} WHERE {pk}=?pk";
		final Map params = Collections.singletonMap("pk", product.getPK());


		final SessionContext ctxNoTtl = JaloSession.getCurrentSession().createSessionContext();
		//ctx.setAttribute(FlexibleSearch.CACHE_TTL, Integer.valueOf(5));// set TTL 

		final StandardSearchResult<Product> first = (StandardSearchResult<Product>) flexibleSearchFirst.search(ctxNoTtl, query,
				params, Product.class);

		assureNotFromCache(first);


		final FlexibleSearch flexibleSearchSecond = createFlexibleSearchWithFixedTimeStamp(4000L);

		final StandardSearchResult<Product> second = (StandardSearchResult<Product>) flexibleSearchSecond.search(ctxNoTtl, query,
				params, Product.class);

		assureFromCache("no matter the time span since generation the same", second);


		final FlexibleSearch flexibleSearchThird = createFlexibleSearchWithFixedTimeStamp(99999L); //no matter the time span since generation the same 

		final StandardSearchResult<Product> third = (StandardSearchResult<Product>) flexibleSearchThird.search(ctxNoTtl, query,
				params, Product.class);

		assureFromCache("no matter the time span since generation the same", third);

		//change the generation here 

		product.setDescription("a whatever");

		//stting the ttle again 
		final FlexibleSearch flexibleSearchForth = createFlexibleSearchWithFixedTimeStamp(1001L);


		final StandardSearchResult<Product> forth = (StandardSearchResult<Product>) flexibleSearchForth.search(ctxNoTtl, query,
				params, Product.class);

		assureNotFromCache(forth);


		final FlexibleSearch flexibleSearchFifth = createFlexibleSearchWithFixedTimeStamp(99999L); //no matter the time span since generation the same 

		final StandardSearchResult<Product> fifth = (StandardSearchResult<Product>) flexibleSearchFifth.search(ctxNoTtl, query,
				params, Product.class);

		assureFromCache("no matter the time span since generation the same", fifth);



	}

	private void assureFromCache(final String message, final StandardSearchResult<Product> rs)
	{
		assertTrue(message, rs.isFromCache()); //first raw
		assertEquals("Result list does not match expected " + Collections.singletonList(product),
				Collections.singletonList(product), rs.getResult());
	}

	private void assureNotFromCache(final StandardSearchResult<Product> rs)
	{
		assertFalse("Should not be cached", rs.isFromCache()); //first raw
		assertEquals("Result list does not match expected " + Collections.singletonList(product),
				Collections.singletonList(product), rs.getResult());
	}

	abstract static class FixedTimeFlexibleSearchCacheKey extends FlexibleSearchCacheKey
	{


		/**
		 * 
		 */
		public FixedTimeFlexibleSearchCacheKey(final TranslatedQuery tq, final Map values, final PK langPK,
				final List<Class<?>> resultClasses, final boolean dontNeedTotal, final int start, final int count,
				final Set<Integer> beanTCs, final boolean executeQuery, final int ttlPeriod, final String tenantId)
		{
			super(tq, values, langPK, resultClasses, dontNeedTotal, start, count, beanTCs, executeQuery, ttlPeriod, tenantId);
		}


		@Override
		abstract long getCurrentTime();

	}


	private FlexibleSearch createFlexibleSearchWithFixedTimeStamp(final long timestamp)
	{

		final String tenantid = Registry.getCurrentTenantNoFallback().getTenantID();


		return new FlexibleSearch()
		{

			@Override
			FlexibleSearchCacheKey createCacheKey(final java.util.List resultClasses, final boolean dontNeedTotal, final int start,
					final int count, final de.hybris.platform.persistence.flexiblesearch.TranslatedQuery tQuery,
					final boolean doExecuteQuery, final de.hybris.platform.core.PK langPK, final java.util.Map values,
					final java.util.Set<Integer> beanTCs, final int ttl)
			{
				return new FixedTimeFlexibleSearchCacheKey(tQuery, values, langPK, resultClasses, dontNeedTotal, start, count,
						beanTCs, doExecuteQuery, ttl, tenantid)
				{
					@Override
					long getCurrentTime()
					{
						return timestamp;
					}
				};
			}
		};
	}

}
