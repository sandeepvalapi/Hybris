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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;
import de.hybris.platform.util.AtomicCounter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;


@UnitTest
public class AtomicCounterTest
{
	@Test
	public void testSingleThreaded()
	{
		assertOverflowException(new AtomicCounter(Integer.MAX_VALUE - 100000));
		assertOverflowException(new AtomicCounter(-123456, 123456));

		assertOverflow(new AtomicCounter(Integer.MAX_VALUE - 100000, true));
		assertOverflow(new AtomicCounter(-123456, 123456, true));
	}

	@Test
	public void testMultiThreaded()
	{
		final int THREADS = 100;
		final int END = 1000000;
		final int RANGE_A = END / 2;
		// we cause overflow by exceeding end value by 1000
		final int RANGE_B = END - RANGE_A + 1000;

		final AtomicCounter counterNoOverflow = new AtomicCounter(0, END);
		testMultiThreadedCounter(counterNoOverflow, THREADS, RANGE_A);
		testMultiThreadedCounter(counterNoOverflow, THREADS, RANGE_B);

		final AtomicCounter counterWithOverflow = new AtomicCounter(0, END, true);
		testMultiThreadedCounter(counterWithOverflow, THREADS, RANGE_A);
		testMultiThreadedCounter(counterWithOverflow, THREADS, RANGE_B);
	}

	private void testMultiThreadedCounter(final AtomicCounter counter, final int THREADS, final int RANGE)
	{
		final AtomicBoolean gotOverflow = new AtomicBoolean(false);
		final AtomicBoolean gotError = new AtomicBoolean(false);

		final boolean expectOverflow = counter.getCurrent() + RANGE >= counter.getEndValue();

		final int expectedCounterValue = expectOverflow ? counter.getCurrent() + RANGE - counter.getEndValue() : counter
				.getCurrent() + RANGE;

		final TestThreadsHolder threadsHolder = new TestThreadsHolder(THREADS, new RunnerCreator()
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return createRunnable(THREADS, threadNumber, RANGE, counter, gotOverflow, gotError);
			}
		});

		// run
		System.out.println("counter before run " + counter);
		threadsHolder.startAll();
		assertTrue(threadsHolder.waitForAll(10, TimeUnit.SECONDS));
		System.out.println("counter after run " + counter);

		// check
		if (expectOverflow && !counter.allowsOverflow())
		{
			// special case -> overflow is not allowed and therefore we got the overflow error
			assertTrue(gotOverflow.get());
		}
		else
		{
			// otherwise we have no overflow error and counter has expected value (including reset)
			assertFalse(gotOverflow.get());
			assertEquals(expectedCounterValue, counter.getCurrent());
		}
		// no other errors are allowed
		assertFalse(gotError.get());
	}

	private Runnable createRunnable(final int THREADS, final int threadNumber, final int RANGE, final AtomicCounter counter,
			final AtomicBoolean gotOverflow, final AtomicBoolean gotError)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				final String id = threadNumber + "/" + THREADS;
				final int myCycles = threadNumber == THREADS - 1 ? (RANGE / THREADS) + (RANGE % THREADS) : (RANGE / THREADS);
				long sum = 0;
				final int lastNr = -1;
				try
				{
					for (int i = 0; i < myCycles; i++)
					{
						final int newNr = counter.generateNext();
						assertTrue("new number " + newNr + " is same as last number " + lastNr, lastNr != newNr);
						assertTrue("new number " + newNr + "<" + counter.getStartValue(), newNr >= counter.getStartValue());
						assertTrue("new number " + newNr + ">=" + counter.getEndValue(), newNr < counter.getEndValue());
						sum = sum + newNr;
					}
					assertTrue("sum was " + sum + " after " + myCycles + " cycles in runner " + id, sum > 0);
				}
				catch (final AssertionError e)
				{
					e.printStackTrace(System.err);
					gotError.set(true);
				}
				catch (final IllegalStateException e)
				{
					gotOverflow.set(true);
				}
			}
		};
	}

	private void assertOverflowException(final AtomicCounter c)
	{
		final int end = c.getEndValue();
		for (int l = c.getStartValue(); l < end; l++)
		{
			assertEquals(l, c.generateNext());
		}
		try
		{
			c.generateNext();
			fail("IllegalStateException expected");
		}
		catch (final IllegalStateException e)
		{
			// fine
		}
	}

	private void assertOverflow(final AtomicCounter c)
	{
		final long end = c.getEndValue();
		for (long l = c.getStartValue(); l < end; l++)
		{
			assertEquals(l, c.generateNext());
		}
		assertEquals(c.getStartValue(), c.generateNext());
		assertEquals(c.getStartValue() + 1, c.generateNext());

	}
}
