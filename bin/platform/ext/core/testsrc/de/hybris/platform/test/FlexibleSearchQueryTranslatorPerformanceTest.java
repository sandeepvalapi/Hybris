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

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.persistence.flexiblesearch.QueryParser;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.bethecoder.ascii_table.ASCIITable;


/**
 *
 */
@PerformanceTest
public class FlexibleSearchQueryTranslatorPerformanceTest extends HybrisJUnit4Test
{

	static final int TRANSLATIONS = 50 * 1000;

	@Test
	public void testSimpleQueryOneThread()
	{
		final String query = "SELECT {PK} FROM {Product}";
		final int threads = 1;
		final int transPerThread = TRANSLATIONS / threads;

		final long time = translateAll(//
				query, Collections.EMPTY_MAP, //
				null, // expected
				threads, // threads
				transPerThread, // translations per thread
				60 /// wait at most
		);
		writeResults(threads, transPerThread, time, query);
	}

	@Test
	public void testSimpleQuery50Threads()
	{
		final String query = "SELECT {PK} FROM {Product}";
		final int threads = 5;
		final int transPerThread = TRANSLATIONS / threads;

		final long time = translateAll(//
				query, Collections.EMPTY_MAP, //
				null, // expected
				threads, // threads
				transPerThread, // translations per thread
				60 /// wait at most
		);
		writeResults(threads, transPerThread, time, query);
	}

	@Test
	public void testComplexQueryOneThread()
	{
		final String query = "SELECT {PK} FROM {Item} WHERE {itemtype}=?foo ORDER BY {PK}";
		final int threads = 1;
		final int transPerThread = TRANSLATIONS / 4 / threads;

		final long time = translateAll(//
				query, Collections.singletonMap("foo", Integer.valueOf(123)), //
				null, // expected
				threads, // threads
				transPerThread, // translations per thread
				60 /// wait at most
		);
		writeResults(threads, transPerThread, time, query);
	}

	@Test
	public void testComplexQuery50Threads()
	{
		final String query = "SELECT {PK} FROM {Item} WHERE {itemtype}=?foo ORDER BY {PK}";
		final int threads = 50;
		final int transPerThread = TRANSLATIONS / 4 / threads;

		final long time = translateAll(//
				query, Collections.singletonMap("foo", Integer.valueOf(123)), //
				null, // expected
				threads, // threads
				transPerThread, // translations per thread
				60 /// wait at most
		);
		writeResults(threads, transPerThread, time, query);
	}

	private void writeResults(final int threads, final int transPerThread, final long ms, final String query)
	{
		final long timeMicroPerTrans = (ms * 1000) / (threads * transPerThread);

		final String[] header =
		{ "threads", "translations", "time", "time/trans", "query" };
		ASCIITable.getInstance().printTable(
				header,
				new String[][]
				{
				{ Integer.toString(threads), Integer.toString(threads * transPerThread), Long.toString(ms) + " ms",
						Long.toString(timeMicroPerTrans) + " microsec", query } });

	}

	private long translateAll(final String query, final Map params, final String expected, final int threads,
			final int translationsPerThread, final int maxWaitSeconds)
	{
		final QueryParser parser = new QueryParser(0);

		final TestThreadsHolder<TranslatorRunner> workers = new TestThreadsHolder<TranslatorRunner>(threads, true)
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new TranslatorRunner(parser, query, params, expected, translationsPerThread);
			}
		};

		workers.startAll();

		Assert.assertTrue("not all " + threads + " thread did stop within " + maxWaitSeconds + " seconds",
				workers.waitAndDestroy(maxWaitSeconds));
		Assert.assertEquals(Collections.EMPTY_MAP, workers.getErrors());

		return workers.getStartToFinishMillis();
	}

	static class TranslatorRunner implements Runnable
	{
		final int turns;
		final String query;
		final Map params;
		final String expectedSQL;
		final QueryParser parser;

		volatile long totalHashSum;

		TranslatorRunner(final QueryParser parser, final String query, final Map params, final String expected, final int turns)
		{
			this.parser = parser;
			this.query = query;
			this.params = params;
			this.expectedSQL = expected;
			this.turns = turns;
		}

		@Override
		public void run()
		{
			final Thread thread = Thread.currentThread();
			final User u = JaloSession.getCurrentSession().getUser();

			long hashSum = 0;

			for (int i = 0; i < turns && !thread.isInterrupted(); i++)
			{
				final TranslatedQuery translateQuery = parser
						.translateQuery(u, query, params.size(), false, true, false, false, null, null);
				if (expectedSQL != null)
				{
					Assert.assertEquals(expectedSQL, translateQuery.getSQLTemplate());
				}
				else
				{
					hashSum += translateQuery.getSQLTemplate().hashCode();
				}
			}

			totalHashSum = hashSum;
		}

	}
}
