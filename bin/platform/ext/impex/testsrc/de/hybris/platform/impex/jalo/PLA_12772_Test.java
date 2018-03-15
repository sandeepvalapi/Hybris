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
package de.hybris.platform.impex.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImportProcessor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.threadpool.PoolableThread;
import de.hybris.platform.util.threadpool.ThreadPool;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.junit.Test;


/**
 * Tests worker thread leakage due to unsafe abort upon global workers pool being exhausted...
 */
public class PLA_12772_Test extends HybrisJUnit4Test
{
	@Test
	public void testBehaviourOnExhaustedGlobalPool() throws ImpExException
	{
		doTest();
	}

	@Test
	public void testMultipleTimes() throws ImpExException
	{
		for (int i = 0; i < 100; i++)
		{
			doTest();
		}
	}

	private void doTest() throws ImpExException
	{
		final ThreadPool pool = createWorkerThreadPool(); // use pool of our own to avoid race conditions with cronjob and others
		try
		{
			final int max = pool.getMaxActive();

			// start first job using almost all workers ( max - 2 ( reader + result proc ) - 3 ( rest for next run ) )
			final MultiThreadedImporter importer1 = createImporter(max - 5, createTestCSV(1000, 0), pool);
			Item last1;
			last1 = importer1.importNext(); // this starts importing

			Thread.yield();
			assertTrue(pool.getNumActive() > 0);

			// start next job trying to get more workers than possible
			final MultiThreadedImporter importer2 = createImporter(10, createTestCSV(1000, 1000), pool);
			Item last2;
			last2 = importer2.importNext(); // this starts importing as well

			// now try to consume both 

			do
			{
				if (last1 != null)
				{
					last1 = importer1.importNext();
				}
				if (last2 != null)
				{
					last2 = importer2.importNext();
				}
			}
			while (last1 != null || last2 != null);

			assertEquals("still got used workers", 0, waitForWorkerToReturnToPoolAndGetUsedNow(pool, 30));
		}
		finally
		{
			pool.close();
		}
	}

	private int waitForWorkerToReturnToPoolAndGetUsedNow(final ThreadPool pool, final int maxSeconds)
	{
		final long maxWait = System.currentTimeMillis() + (maxSeconds * 1000);
		int usedNow = pool.getNumActive();
		while (usedNow > 0 && System.currentTimeMillis() < maxWait)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
				break;
			}
			usedNow = pool.getNumActive();
		}
		return usedNow;
	}

	ThreadPool createWorkerThreadPool()
	{
		final Tenant tenant = Registry.getCurrentTenantNoFallback();
		final ConfigIntf cfg = tenant.getConfig();
		final int poolSize = cfg.getInt("workers.maxthreads", 64);

		final ThreadPool ret = new ThreadPool(tenant.getTenantID(), poolSize);

		final GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.maxActive = poolSize;
		config.maxIdle = poolSize;
		config.maxWait = -1;
		config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
		config.testOnBorrow = true;
		config.testOnReturn = true;
		config.timeBetweenEvictionRunsMillis = 30 * 1000; // keep idle threads for at most 30 sec
		ret.setConfig(config);

		return ret;
	}

	MultiThreadedImporter createImporter(final int workers, final String lines, final ThreadPool threadPool) throws ImpExException
	{
		final MultiThreadedImporter importer = new MultiThreadedImporter(new CSVReader(lines))
		{
			@Override
			protected de.hybris.platform.impex.jalo.imp.ImpExImportReader createImportReader(final CSVReader csvReader)
			{
				return new MultiThreadedImpExImportReader(csvReader)
				{
					@Override
					protected PoolableThread tryToBorrowThread(final ThreadPool threadPool1)
					{
						return super.tryToBorrowThread(threadPool);
					}
				};
			}

			@Override
			protected ImpExImportReader createImportReaderForNextPass()
			{
				final MultiThreadedImpExImportReader currentReader = (MultiThreadedImpExImportReader) getReader();

				final MultiThreadedImpExImportReader reader = new MultiThreadedImpExImportReader(//
						getDumpHandler().getReaderOfLastDump(), //
						getDumpHandler().getWriterOfCurrentDump(), //
						currentReader.getDocumentIDRegistry(), //
						(MultiThreadedImportProcessor) currentReader.getImportProcessor(), //
						currentReader.getValidationMode())
				{
					@Override
					protected PoolableThread tryToBorrowThread(final ThreadPool threadPool1)
					{
						return super.tryToBorrowThread(threadPool);
					}
				};

				reader.setMaxThreads(getMaxThreads());
				reader.setIsSecondPass();
				reader.setLocale(currentReader.getLocale());
				reader.setLogFilter(getLogFilter());
				return reader;
			}
		};
		importer.setMaxThreads(workers);
		return importer;
	}

	String createTestCSV(final int lines, final int offset)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT_UPDATE Title; code[unique=true]").append('\n');

		for (int i = 0; i < lines; i++)
		{
			stringBuilder.append("; ttt-").append(i + offset).append(';').append('\n');
		}

		return stringBuilder.toString();
	}
}
