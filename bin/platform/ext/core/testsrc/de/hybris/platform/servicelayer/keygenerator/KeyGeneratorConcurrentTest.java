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
package de.hybris.platform.servicelayer.keygenerator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.util.Config;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import com.google.common.base.Joiner;

/**
 * Making this non-transactional is on purpose the number series pros are created in separate thread - aka transaction.
 * Cleaning them in transaction  - in main test thread - is also not so wise since we actually get rid of them ....
 */
@PerformanceTest
@IntegrationTest
public class KeyGeneratorConcurrentTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(KeyGeneratorConcurrentTest.class.getName());

	private static final int THREADS = 20;
	private static final int DURATION_SECONDS = 30;

	private final Set<String> seriesToRemove = new LinkedHashSet<>();


	@After
	public void clearSeriesAfter()
	{
		LOG.info("Clearing created number series [" + Joiner.on(",").join(seriesToRemove) + "] ....");
		for (final String singleSerie : seriesToRemove)
		{
			removeSeries(singleSerie);
		}
	}

	// PLA-12605
	@Test
	public void testConcurrentInit()
	{
		// cannot test on hsqldb since it every now and then runs into
		// weird db locks that are never freed :( 
		if (!Config.isHSQLDBUsed())
		{
			testConcurrentInit(THREADS, DURATION_SECONDS);
		}
	}

	private void testConcurrentInit(final int threads, final int durationSeconds)
	{
		final long maxMillis = System.currentTimeMillis() + (durationSeconds * 1000);
		int round = 0;
		LOG.info("Starting concurrent test for PersistentKeyGenerator.init() - will take at least " + durationSeconds
				  + " seconds...");
		do
		{
			final String key = "concurrentKey-" + round++;

			try
			{
				final PersistentKeyGenerator gen = createKeyGenerator(key);
				initConcurrently(threads, gen);
			}
			finally
			{
				seriesToRemove.add(key);
			}

		}
		while (System.currentTimeMillis() < maxMillis);
	}

	private void removeSeries(final String key)
	{
		if (key != null)
		{
			try
			{
				NumberSeriesManager.getInstance().removeNumberSeries(key);
			}
			catch (final JaloInvalidParameterException e)
			{
				// ignore
			}
		}
	}

	private PersistentKeyGenerator createKeyGenerator(final String key)
	{
		final PersistentKeyGenerator generator = new PersistentKeyGenerator();
		generator.setKey(key);
		generator.setNumeric(true);
		generator.setDigits(3);
		generator.setStart("001");
		return generator;
	}

	private void initConcurrently(final int threads, final PersistentKeyGenerator generator)
	{
		final Runnable logic = new Runnable()
		{
			@Override
			public void run()
			{
				generator.generate();
			}
		};
		final TestThreadsHolder runners = new TestThreadsHolder(threads, logic, true);

		runners.startAll();

		assertTrue("Threads locked", runners.waitAndDestroy(30));
		assertEquals("Empty error set expected", Collections.EMPTY_MAP, runners.getErrors());
		assertNotNull("Could not generate next key", generator.generate());
	}

}
