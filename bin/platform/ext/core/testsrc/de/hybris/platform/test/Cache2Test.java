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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.SimpleItemInvalidationListener;
import de.hybris.platform.cache2.AbstractObjectCacheManager;
import de.hybris.platform.cache2.FIFOObjectCache;
import de.hybris.platform.cache2.ObjectCreator;
import de.hybris.platform.cache2.ObjectKey;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;

import java.util.Iterator;

import org.junit.Test;


@UnitTest
public class Cache2Test
{
	@Test
	public void testBasic()
	{
		final TestObjectCacheManager testObjectCacheManager = new TestObjectCacheManager();
		testObjectCacheManager.setCache(new FIFOObjectCache(2));

		Object object = testObjectCacheManager.fetch(new TestObjectKey(Integer.valueOf(2)));
		assertEquals(testObjectCacheManager.creationCounter, 1);
		assertEquals(object, Integer.valueOf(2));

		object = testObjectCacheManager.fetch(new TestObjectKey(Integer.valueOf(3)));
		assertEquals(testObjectCacheManager.creationCounter, 2);
		assertEquals(object, Integer.valueOf(3));

		object = testObjectCacheManager.fetch(new TestObjectKey(Integer.valueOf(2)));
		assertEquals(testObjectCacheManager.creationCounter, 2);

		object = testObjectCacheManager.fetch(new TestObjectKey(Integer.valueOf(4)));
		assertEquals(testObjectCacheManager.creationCounter, 3);

		object = testObjectCacheManager.fetch(new TestObjectKey(Integer.valueOf(2))); //FIFO has removed this
		assertEquals(testObjectCacheManager.creationCounter, 4);


		//test invalidate
		assertEquals(4, testObjectCacheManager.creationCounter);
		testObjectCacheManager.fetch(new TestObjectKey(Integer.valueOf(2)));
		assertEquals(4, testObjectCacheManager.creationCounter);
		assertEquals(0, testObjectCacheManager.invalidationCounter);
		testObjectCacheManager.invalidate(new TestObjectKey(Integer.valueOf(2)));
		assertEquals(1, testObjectCacheManager.invalidationCounter);
		assertEquals(4, testObjectCacheManager.creationCounter);
		testObjectCacheManager.fetch(new TestObjectKey(Integer.valueOf(2)));
		assertEquals(5, testObjectCacheManager.creationCounter);

	}

	public static class TestObjectKey implements ObjectKey
	{
		Integer integer;

		public TestObjectKey(final Integer integer)
		{
			this.integer = integer;
		}

		@Override
		public boolean getExpired() // NOPMD
		{
			return false;
		}

		@Override
		public ObjectCreator getObjectCreator()
		{
			return new ObjectCreator()
			{
				@Override
				public Object createObject()
				{
					return integer;
				}
			};
		}

		@Override
		public Object getSignature()
		{
			return integer;
		}


	}


	public class TestObjectCacheManager<T> extends AbstractObjectCacheManager<T>
	{
		public int expirationCounter = 0;
		public int invalidationCounter = 0;
		public int requestCounter = 0;
		public int creationCounter = 0;


		@Override
		protected void handleExpiration(final ObjectKey<T> objectKey)
		{
			expirationCounter++;
			super.handleExpiration(objectKey);
		}

		@Override
		protected void handleInvalidation(final ObjectKey<T> objectKey)
		{
			invalidationCounter++;
			super.handleInvalidation(objectKey);
		}

		@Override
		protected T handleRequest(final ObjectKey<T> objectKey)
		{
			requestCounter++;
			return super.handleRequest(objectKey);
		}

		@Override
		protected T handleCreation(final ObjectKey<T> objectKey)
		{
			creationCounter++;
			return super.handleCreation(objectKey);
		}
	}

	/**
	 * TEST METHOD
	 */
	public static void main(final String... args) throws Exception
	{
		//--prepare the system
		Registry.activateStandaloneMode();
		Registry.activateMasterTenant();

		final JaloSession jSession = JaloSession.getCurrentSession();
		jSession.setUser(jSession.getUserManager().getAdminEmployee());
		System.out.println("### CACHETEST START #################################################");

		//--prepare the data

		final Iterator<Product> allProds = ProductManager.getInstance().getAllProducts().iterator();
		final Product product1 = allProds.next();
		/* final Product p2 = */allProds.next();



		//--setup cache
		final Cache cache = Registry.getCurrentTenant().getCache();
		new MyCachedPriceInvalidationListener().start(cache);



		//--testing
		product1.setProperty("baseprice", new Double(10));
		System.out.println(new MyCachedPrice(product1, cache).get());

		product1.setProperty("baseprice", new Double(11));
		System.out.println(new MyCachedPrice(product1, cache).get());

		product1.setProperty("baseprice", new Double(12));
		System.out.println(new MyCachedPrice(product1, cache).get());



		//--testing 2

		System.out.println("### CACHETEST END ###################################################");
	}



}






class MyCachedPrice extends AbstractCacheUnit // NOPMD
{
	private final Product product;

	public MyCachedPrice(final Product product, final Cache cache)
	{
		super(cache);
		this.product = product;
	}

	@Override
	public Object compute() throws Exception
	{
		System.out.println("..computing price for " + product.getCode());

		final Double price = (Double) product.getProperty("baseprice");

		//CONNECT TO SAP AND DISCOUNT THE BASE PRICE. THIS WILL TAKE A LOT OF TIME
		Thread.sleep(1000);
		return price;
	}

	@Override
	public Object[] createKey()
	{
		return new Object[]
		{ "myprice", product.getPK() };
	}

}




class MyCachedPriceInvalidationListener extends SimpleItemInvalidationListener // NOPMD
{
	@Override
	public void itemModified(final InvalidationTarget cache, final PK pk, final boolean removed)
	{
		if (Constants.TC.Product == pk.getTypeCode())
		{
			invalidate(cache, new Object[]
			{ "myprice", pk }, removed);
		}
	}
}



/*
 * begin/commit tx.enableDelayedStore() isCachingSupport() and Date() expiration check //--perf testing long l =
 * System.nanoTime(); new MyCachedPrice( p1, cache ).get(); for( int i = 0; i<1000000; i++) { new MyCachedPrice(
 * p1,cache ).get(); } System.out.println( "time:"+ (System.nanoTime()-l)/1000000);
 */

