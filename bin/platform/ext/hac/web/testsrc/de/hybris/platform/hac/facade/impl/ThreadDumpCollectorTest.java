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
package de.hybris.platform.hac.facade.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.hac.facade.impl.ThreadDumpCollector.DumpResult;

import org.junit.Assert;
import org.junit.Test;


@UnitTest
public class ThreadDumpCollectorTest
{
	private static final int SLEEP_TIME = 1000;
	private static final int THREADS_COUNT = 5;
	private static final int GRAB_PERIOD = 100;//millis

	@Test
	public void testStopCollectionWithoutStart() throws Exception
	{
		final ThreadDumpCollector collector = prepareCollector(1);

		try
		{
			collector.stopCollecting();
			Assert.fail("This should fail since no dumps avaialble");
		}
		catch (final DumpNotAvailableException e)
		{
			//fine
		}
	}

	@Test
	public void testRunTotalDumps() throws Exception
	{
		final ThreadDumpCollector collector = prepareCollector(THREADS_COUNT);

		collector.startCollecting(GRAB_PERIOD);
		Thread.sleep(1000);
		Assert.assertNotNull(collector.stopCollecting());

		final DumpResult dataResult = collector.getOrCalculateResult();

		Assert.assertTrue(dataResult != null);
		Assert.assertEquals(THREADS_COUNT, dataResult.getTotalFiles());

		final DumpResult dataResult2 = collector.getOrCalculateResult();

		Assert.assertTrue(dataResult2 != null);
		Assert.assertEquals(THREADS_COUNT, dataResult2.getTotalFiles());
	}

	@Test
	public void testRerunStopCollecting() throws Exception
	{
		final ThreadDumpCollector collector = prepareCollector(THREADS_COUNT);

		collector.startCollecting(GRAB_PERIOD); //interval not implemented

		Thread.sleep(SLEEP_TIME);

		final Object firstResult = collector.stopCollecting();

		Assert.assertNotNull(firstResult);

		final Object secondResult = collector.stopCollecting();

		Assert.assertNotNull(secondResult);

		Assert.assertSame(firstResult, secondResult);

		final DumpResult dataResult = collector.getOrCalculateResult();

		Assert.assertTrue(dataResult != null);
		Assert.assertEquals(THREADS_COUNT, dataResult.getTotalFiles());
	}

	@Test
	public void testStartCollectingWithoutStop() throws Exception
	{
		final ThreadDumpCollector collector = prepareCollector(THREADS_COUNT);

		collector.startCollecting(GRAB_PERIOD / 5); //interval not implemented
		Thread.sleep(2 * GRAB_PERIOD);

		collector.startCollecting(GRAB_PERIOD); //interval not implemented
		Thread.sleep(GRAB_PERIOD / 5);

		collector.startCollecting(GRAB_PERIOD); //interval not implemented
		Thread.sleep(GRAB_PERIOD);


		collector.startCollecting(GRAB_PERIOD); //interval not implemented

		Thread.sleep(SLEEP_TIME);

		final Object firstResult = collector.stopCollecting();

		Assert.assertNotNull(firstResult);

		final DumpResult dataResult = collector.getOrCalculateResult();

		Assert.assertTrue(dataResult != null);
		Assert.assertEquals(THREADS_COUNT, dataResult.getTotalFiles());
	}

	@Test
	public void testStartCollectingAfterStop() throws Exception
	{
		final ThreadDumpCollector collector = prepareCollector(THREADS_COUNT);

		try
		{
			collector.stopCollecting();
			Assert.fail("This should fail since no dumps avaialble");
		}
		catch (final DumpNotAvailableException e)
		{
			//fine
		}

		collector.startCollecting(GRAB_PERIOD / 5); //interval not implemented
		Thread.sleep(2 * GRAB_PERIOD);

		collector.startCollecting(GRAB_PERIOD); //interval not implemented

		Thread.sleep(SLEEP_TIME);

		final Object firstResult = collector.stopCollecting();

		Assert.assertNotNull(firstResult);

		final DumpResult dataResult = collector.getOrCalculateResult();

		Assert.assertTrue(dataResult != null);
		Assert.assertEquals(THREADS_COUNT, dataResult.getTotalFiles());
	}

	private ThreadDumpCollector prepareCollector(final int dumps) throws Exception
	{
		final ThreadDumpCollector collector = new ThreadDumpCollector();

		collector.dumpExecutors = 1;
		collector.maxDumps = dumps;
		collector.threadMonitor = new ThreadMonitor();

		collector.afterPropertiesSet(); ///emulate spring
		return collector;
	}
}
