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

import static org.junit.Assert.assertTrue;

import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Tests multi-threaded import reader behaviour on premature close().
 * 
 * See PLA-12550 for details.
 */
@Ignore("PLA-12550 is not yet fixed")
public class PLA_12550_Test extends HybrisJUnit4Test
{
	@Test
	public void testCloseBeforeReaderFinished() throws ImpExException, IOException
	{
		final int THREADS = 5;
		final int LINES = 100;
		final int MAX_WAIT_SEC = 30;

		final TestMTIR reader = createTestReader(//
				createTestLines(LINES), //
				THREADS);

		reader.readLine();
		reader.close();

		waitForWorkersToFinish(reader, MAX_WAIT_SEC);

		assertTrue(reader.isReaderFinished());
		assertTrue(reader.isResultProcessorFinished());
		assertTrue(reader.isAllWorkerFinished());
	}

	void waitForWorkersToFinish(final TestMTIR reader, final int seconds)
	{
		int tick = 0;
		do
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
				break;
			}
		}
		while (++tick < seconds && !allWorkersFinished(reader));
	}

	boolean allWorkersFinished(final TestMTIR reader)
	{
		return reader.isReaderFinished() && reader.isResultProcessorFinished() && reader.isAllWorkerFinished();
	}

	String createTestLines(final int amount)
	{
		final StringBuilder stringBuilder = new StringBuilder("INSERT Title; code").append('\n');
		for (int i = 0; i < amount; i++)
		{
			stringBuilder.append(";TTT").append(i).append('\n');
		}
		return stringBuilder.toString();
	}

	static class TestMTIR extends MultiThreadedImpExImportReader
	{
		TestMTIR(final String lines, final int threads)
		{
			super(lines);
			setMaxThreads(threads);
		}

		@Override
		protected boolean readLineFromWorker() throws ImpExException
		{
			final boolean notDone = super.readLineFromWorker();

			// to make sure we're calling close() *before* all lines
			// had been read we're simply making reading *very* slow
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
			return notDone;
		}

		@Override
		public boolean isAllWorkerFinished()
		{
			return super.isAllWorkerFinished();
		}

		@Override
		public boolean isReaderFinished()
		{
			return super.isReaderFinished();
		}

		@Override
		public boolean isResultProcessorFinished()
		{
			return super.isResultProcessorFinished();
		}
	}

	TestMTIR createTestReader(final String lines, final int threads)
	{
		final TestMTIR reader = new TestMTIR(lines, threads);
		return reader;
	}
}
