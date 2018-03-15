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

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ParallelTest extends HybrisJUnit4Test
{
	private static final int PRODUCT_COUNT = 10;
	private static final String CODE_PREFIX = "parallel-";
	private static final List RES_CLASSES = Collections.singletonList(Product.class);

	static String getProductCode(final int i)
	{
		return CODE_PREFIX + (i % PRODUCT_COUNT);
	}

	private Map products;
	String PRODUCT_TYPE;

	@Before
	public void setUp() throws Exception
	{
		PRODUCT_TYPE = jaloSession.getTypeManager().getRootComposedTypeForJaloClass(Product.class).getCode();
		products = new HashMap();
		for (int i = 0; i < PRODUCT_COUNT; i++)
		{
			final String nextCode = getProductCode(i);
			final Product nextProduct = jaloSession.getProductManager().createProduct(nextCode);
			assertNotNull(nextProduct);
			products.put(nextCode, nextProduct);
		}
	}

	@After
	public void tearDown() throws Exception
	{
		products = null;
	}

	@Test
	public void testParallelParameterized() throws InterruptedException
	{
		final Tenant sys = Registry.getCurrentTenant();
		executeTest(new ThreadFactory()
		{
			@Override
			public ParallelTestThread createThread(final int i)
			{
				return new ParallelTestThread("SELECT {" + Item.PK + "} FROM {" + PRODUCT_TYPE + "} WHERE {" + Product.CODE + "}=?1",
						Collections.singletonMap(Integer.valueOf(1), getProductCode(i)), sys);
			}
		});
	}

	@Test
	public void testParallelUnparameterized() throws InterruptedException
	{
		final Tenant sys = Registry.getCurrentTenant();
		executeTest(new ThreadFactory()
		{
			@Override
			public ParallelTestThread createThread(final int i)
			{
				return new ParallelTestThread("SELECT {" + Item.PK + "} FROM {" + PRODUCT_TYPE + "} WHERE {" + Product.CODE + "}='"
						+ getProductCode(i) + "'", Collections.EMPTY_MAP, sys);
			}
		});
	}

	private void executeTest(final ThreadFactory factory) throws InterruptedException
	{
		final int THREAD_COUNT = 40;
		final ParallelTestThread[] threads = new ParallelTestThread[THREAD_COUNT];
		for (int i = 0; i < threads.length; i++)
		{
			final ParallelTestThread nextThread = factory.createThread(i);
			threads[i] = nextThread;
			nextThread.start();
		}
		final StringBuilder failed = new StringBuilder();
		for (int i = 0; i < threads.length; i++)
		{
			final ParallelTestThread next = threads[i];
			next.join();
			final Collection expected = Collections.singleton(products.get(getProductCode(i)));
			if (next.getFailureReason() == null)
			{
				final Collection found = next.getResult();
				if (!expected.containsAll(found) || !found.containsAll(expected))
				{
					failed.append("\nthread " + i + " expected <" + expected + "> but found <" + found + ">");
				}
			}
			else
			{
				final StringWriter stringWriter = new StringWriter();
				next.getFailureReason().printStackTrace(new PrintWriter(stringWriter));
				failed.append(stringWriter.toString());
			}
		}
		if (failed.length() != 0)
		{
			fail("tests failed:" + failed);
		}
	}

	private final class ParallelTestThread extends RegistrableThread
	{
		private final String query;
		private final Map values;
		private final Tenant sys;

		Collection result;
		Exception failureReason;

		ParallelTestThread(final String query, final Map values, final Tenant sys)
		{
			super();
			this.sys = sys;
			this.query = query;
			this.values = values;
		}

		Exception getFailureReason()
		{
			return failureReason;
		}

		Collection getResult()
		{
			return result;
		}

		@Override
		public void internalRun()
		{
			try
			{
				if (sys instanceof MasterTenant)
				{
					Registry.activateMasterTenantAndFailIfAlreadySet();
				}
				else
				{
					Registry.setCurrentTenant(sys);
				}

				jaloSession.activate();
				final SessionContext ctx = jaloSession.createSessionContext();
				final SearchResult searchResult = jaloSession.getFlexibleSearch().search(ctx, query, values, RES_CLASSES, true, // yes, fail on unknown fields
						false, // yes, need total
						0, -1 // range
						);
				result = searchResult.getResult();
			}
			catch (final Exception e)
			{
				failureReason = e;
			}
			finally
			{
				JaloSession.deactivate();
				Registry.unsetCurrentTenant();
			}
		}
	}

	interface ThreadFactory
	{
		ParallelTestThread createThread(int index);
	}
}
