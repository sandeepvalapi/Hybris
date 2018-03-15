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
package de.hybris.platform.europe1.jalo;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;


@PerformanceTest
public class Europe1PriceFactoryConcurrentTest extends HybrisJUnit4Test
{

	private static final Logger LOG = Logger.getLogger(Europe1PriceFactoryConcurrentTest.class.getName());

	private static final int TURNS = 50;
	final int THREADS = 250;
	final int WAIT_SECONDS = 60;

	@Test
	public void testConcurentCacheCheck()
	{

		final Europe1PriceFactory factory = Europe1PriceFactory.getInstance();


		final TestThreadsHolder<PriceFactoryRunner> randomAccessHolder = new TestThreadsHolder<PriceFactoryRunner>(//
				THREADS, //
				new RunnerCreator<PriceFactoryRunner>()
				{

					@Override
					public PriceFactoryRunner newRunner(final int threadNumber)
					{

						if ((threadNumber % 2) == 0)
						{
							return new IsCachingPriceFactoryRunner(factory);
						}
						else
						{
							return new ClearCachingPriceFactoryRunner(factory);
						}

					}
				});

		randomAccessHolder.startAll();
		int alive = randomAccessHolder.getAlive();
		int changedWhileInWait = alive;
		do
		{
			try
			{
				Thread.sleep(WAIT_SECONDS * 1000);
			}
			catch (final InterruptedException e)
			{
				//ignoring!!!!
			}
			changedWhileInWait = alive - randomAccessHolder.getAlive();
			LOG.info("Waited " + WAIT_SECONDS + " seconds for finishing  (" + changedWhileInWait + " of " + alive + ") threads ");
			alive = randomAccessHolder.getAlive();
		}
		while (changedWhileInWait > 0 && randomAccessHolder.getAlive() > 0);

		org.junit.Assert.assertTrue("not all test threads shut down orderly", randomAccessHolder.stopAndDestroy(15));
		org.junit.Assert.assertEquals("found worker errors", Collections.EMPTY_MAP, randomAccessHolder.getErrors());
		for (final PriceFactoryRunner runner : randomAccessHolder.getRunners())
		{
			org.junit.Assert.assertEquals("runner " + runner + " had error turns", Collections.EMPTY_LIST, runner.errorTurns);
		}
	}

	static private abstract class PriceFactoryRunner implements Runnable
	{

		private final Tenant tenant;
		protected final Europe1PriceFactory factory;

		private volatile List<Exception> errorTurns;

		PriceFactoryRunner(final Europe1PriceFactory factory)
		{

			this.tenant = Registry.getCurrentTenantNoFallback();
			this.factory = factory;
		}

		@Override
		public void run()
		{
			try
			{
				Registry.setCurrentTenant(tenant);
				final List<Exception> recordedErrorTurns = new LinkedList<Exception>();

				for (int i = 0; i < TURNS && !Thread.currentThread().isInterrupted(); i++)
				{
					modifyIndex(recordedErrorTurns);
				}
				this.errorTurns = recordedErrorTurns; // volatile write
			}
			finally
			{
				Registry.unsetCurrentTenant();
			}

		}

		abstract protected void modifyIndex(final List<Exception> recordedErrorTurns);


		@Override
		public String toString()
		{
			return getClass().getSimpleName();
		}
	}

	static class IsCachingPriceFactoryRunner extends PriceFactoryRunner
	{

		IsCachingPriceFactoryRunner(final Europe1PriceFactory factory)
		{
			super(factory);
		}

		@Override
		protected void modifyIndex(final List<Exception> recordedErrorTurns)
		{
			try
			{
				factory.isCachingTaxes();
			}
			catch (final Exception e)
			{
				//e.printStackTrace();
				LOG.error(e);
				recordedErrorTurns.add(e);
				Thread.currentThread().interrupt();
			}
		}
	}

	static class ClearCachingPriceFactoryRunner extends PriceFactoryRunner
	{

		ClearCachingPriceFactoryRunner(final Europe1PriceFactory factory)
		{
			super(factory);
		}

		@Override
		protected void modifyIndex(final List<Exception> recordedErrorTurns)
		{
			try
			{
				factory.invalidateTaxCache();
			}
			catch (final Exception e)
			{
				//e.printStackTrace();
				LOG.error(e);
				recordedErrorTurns.add(e);
				Thread.currentThread().interrupt();
			}
		}
	}
}
