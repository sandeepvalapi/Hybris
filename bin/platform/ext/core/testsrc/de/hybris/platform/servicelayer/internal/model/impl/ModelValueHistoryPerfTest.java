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
package de.hybris.platform.servicelayer.internal.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.test.TestThreadsHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;



@PerformanceTest
public class ModelValueHistoryPerfTest
{
	private static final String FOO = "foo";
	private static final String FOO_FIRST = "foo0";


	private static final Logger LOG = Logger.getLogger(ModelValueHistoryPerfTest.class.getName());

	protected final int WAIT_SECONDS = 20;
	protected final int THREADS = 40;

	private final ModelValueHistory modelValueHistory = new ModelValueHistory();

	@Test
	public void testPerf()
	{
		for (int i = 0; i < 1000; i++)
		{
			modelValueHistory.markDirty(FOO + i);
		}

		runAccessor(new TestThreadsHolder<Accessor>(THREADS, false)
		{
			@Override
			public Accessor newRunner(final int threadNumber)
			{
				if ((threadNumber % 2) == 0)
				{
					return new ModelMarkDirty();
				}
				else
				{
					return new ModelMarkUnchanged();
				}
			}
		});

	}


	static protected abstract class Accessor implements Runnable
	{

		protected List<Throwable> occurredErrors = new ArrayList<Throwable>();

		Accessor()
		{
			//
		}

		@Override
		public void run()
		{
			try
			{
				while (!Thread.currentThread().isInterrupted())
				{
					access();
				}

			}
			catch (final Exception e)
			{
				occurredErrors.add(e);
			}
			finally
			{
				//
			}
		}



		abstract void access() throws IllegalArgumentException;
	}

	protected void runAccessor(final TestThreadsHolder<Accessor> randomAccessHolder)
	{

		randomAccessHolder.startAll();
		try
		{
			Thread.sleep(WAIT_SECONDS * 1000);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		assertTrue("not all test threads shut down orderly", randomAccessHolder.stopAndDestroy(30));
		assertEquals("found worker errors", Collections.EMPTY_MAP, randomAccessHolder.getErrors());

		for (final Accessor r : randomAccessHolder.getRunners())
		{

			if (!r.occurredErrors.isEmpty())
			{
				for (final Throwable t : r.occurredErrors)
				{
					LOG.error(t);
					Assert.fail(t.getMessage());
				}
			}

		}

	}


	class ModelMarkDirty extends Accessor
	{

		@Override
		void access() throws IllegalArgumentException
		{
			modelValueHistory.markDirty(FOO_FIRST);
		}

	}

	class ModelMarkUnchanged extends Accessor
	{

		@Override
		void access() throws IllegalArgumentException
		{
			modelValueHistory.markUnchanged(FOO_FIRST);
		}

	}

}
