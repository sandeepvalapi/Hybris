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
package de.hybris.platform.servicelayer.internal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.test.TestThreadsHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;


/**
 * Standalone performance test for testing {@link LocMap}.
 */
@PerformanceTest
public class LocMapPerformanceTest
{
	//
	@Test
	public void testConcurrentAccess()
	{
		testConcurrentAccess(30);
	}


	private void testConcurrentAccess(final int DURATION_SEC)
	{
		final List<Locale> countries = Arrays.asList(Locale.getAvailableLocales());

		final Map<Locale, String> values = new LocMap<Locale, String>();

		for (final Locale ctry : countries)
		{
			values.put(ctry, "BASE-" + ctry.getLanguage());
		}


		final TestThreadsHolder<Runnable> threads = new TestThreadsHolder<Runnable>(100, true)
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return threadNumber == 0 ? new UpdateRunner(values, countries) : new AccessRunner(values, countries);
			}
		};
		threads.startAll();
		try
		{
			Thread.sleep(DURATION_SEC * 1000);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		assertTrue(threads.stopAndDestroy(30));

		for (final Throwable runner : threads.getErrors().values())
		{
			runner.printStackTrace();
		}
		assertEquals(Collections.EMPTY_MAP, threads.getErrors());

	}

	static class UpdateRunner implements Runnable
	{
		final Map<Locale, String> values;
		final List<Locale> countries;

		public UpdateRunner(final Map<Locale, String> values, final List<Locale> countries)
		{
			this.countries = countries;
			this.values = values;
		}

		@Override
		public void run()
		{
			final Thread currentThread = Thread.currentThread();
			while (!currentThread.isInterrupted())
			{
				for (final Locale ctry : countries)
				{
					values.put(ctry, ctry.getLanguage() + "-" + System.nanoTime());
				}
				try
				{
					Thread.sleep((long) (Math.random() * 1000));
				}
				catch (final InterruptedException e)
				{
					currentThread.interrupt();
				}
			}
		}
	}

	static class AccessRunner implements Runnable
	{

		final List<Locale> countries;
		final Map<Locale, String> values;

		public AccessRunner(final Map<Locale, String> values, final List<Locale> countries)
		{

			this.countries = countries;
			this.values = values;
		}

		@Override
		public void run()
		{


			final Thread currentThread = Thread.currentThread();
			while (!currentThread.isInterrupted())
			{

				for (final Locale ctry : countries)
				{
					final String name = values.get(ctry);
					assertNotNull("value for the " + ctry + " should not be null ", name);
				}
			}
		}
	}
}
