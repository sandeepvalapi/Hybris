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

import static org.fest.assertions.Fail.fail;
import static org.junit.Assert.*;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.impex.jalo.imp.ImpExWorkerResult;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.CSVWriter;

import javax.annotation.Resource;

import org.apache.commons.io.output.NullWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests potential thread pool leak when running multi-threaded ImpEx with errors caught by workers.
 *
 * <b>See PLA-12491 for the whole story!</b>
 */
@IntegrationTest
public class PLA_12491_Test extends ServicelayerBaseTest
{

	@Resource
	private InterceptorRegistry interceptorRegistry;

	private PLA_12491_Test_Interceptor interceptor;

	static final String INTERCEPTOR_ERROR_CODE = "FooBar";
	static final String WORKER_ERROR_CODE = "FooBarWorker";
	static final int THREADS = 50;
	static final int LINES = THREADS * 20;
	static final int ERROR_LINE_NR = THREADS + 1;
	static final int MAX_WAIT_SEC = 30;


	@Before
	public void setUp()
	{
		this.interceptor = assertInterceptorInstalled();
		this.interceptor.setUpForTest(INTERCEPTOR_ERROR_CODE);
	}

	@After
	public void tearDown()
	{
		this.interceptor.reset();
	}

	protected PLA_12491_Test_Interceptor assertInterceptorInstalled()
	{
		for (final ValidateInterceptor i : interceptorRegistry.getValidateInterceptors(TitleModel._TYPECODE))
		{
			if (i instanceof PLA_12491_Test_Interceptor)
			{
				return (PLA_12491_Test_Interceptor) i;
			}
		}
		fail("PLA_12491_Test_Interceptor not installed! - got " + interceptorRegistry.getValidateInterceptors(TitleModel._TYPECODE)
				+ " instead");
		return null;
	}
	
	@Test
	public void testErrorInWorker()
	{
		final TestMTIR reader = new TestMTIR(
				createTestLines(LINES, ERROR_LINE_NR, WORKER_ERROR_CODE), //
				THREADS,//
				ERROR_LINE_NR,//
				WORKER_ERROR_CODE, //
				true // simulating error in *worker*
		);

		try
		{
			TestUtils.disableFileAnalyzer("PLA-12491 test requires item creation exception to be thrown");
			try
			{
				reader.readAll();
			}
			catch (final Exception e)
			{
				// for now we don't care - important is the fact of all threads having finished
				System.err.println("error from readAll() : " + e.getMessage());
			}

			waitForWorkersToFinish(reader, MAX_WAIT_SEC);

			assertFalse("interceptor error thrown but should not",interceptor.errorWasThrown());
			assertTrue("error line not processed",reader.wasErrorLineProcessed());
			assertTrue("reader not finished",reader.isReaderFinished());
			assertTrue("result worker not finished",reader.isResultProcessorFinished());
			assertTrue("workers not finished",reader.isAllWorkerFinished());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	@Test
	public void testErrorInInterceptor()
	{
		final TestMTIR reader = new TestMTIR(
				createTestLines(LINES, ERROR_LINE_NR, INTERCEPTOR_ERROR_CODE), // 
				THREADS,//
				ERROR_LINE_NR,//
				INTERCEPTOR_ERROR_CODE, //
				false // simulating error *not* in worker but in interceptor
		);

		try
		{
			TestUtils.disableFileAnalyzer("PLA-12491 test requires item creation exception to be thrown");
			try
			{
				reader.readAll();
			}
			catch (final Exception e)
			{
				// for now we don't care - important is the fact of all threads having finished
				System.err.println("error from readAll() : " + e.getMessage());
			}

			waitForWorkersToFinish(reader, MAX_WAIT_SEC);

			assertTrue("interceptor error not thrown",interceptor.errorWasThrown());
			assertTrue("error line not processed",reader.wasErrorLineProcessed());
			assertTrue("reader not finished",reader.isReaderFinished());
			assertTrue("result worker not finished",reader.isResultProcessorFinished());
			assertTrue("workers not finished",reader.isAllWorkerFinished());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
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

	String createTestLines(final int amount, final int errorPos, String errorCode )
	{
		final StringBuilder stringBuilder = new StringBuilder("INSERT Title; code").append('\n');
		for (int i = 0; i < amount; i++)
		{
			if (errorPos == i) // use existing code to cause error
			{
				stringBuilder.append(";").append(errorCode).append('\n');
			}
			else
			{
				stringBuilder.append(";TTT").append(i).append('\n');
			}
		}
		return stringBuilder.toString();
	}

	static class TestMTIR extends MultiThreadedImpExImportReader
	{
		volatile int errorLineNr;
		volatile boolean errorLineProcessed = false;
		
		final boolean simulateWorkerError;
		final String errorCode;

		TestMTIR(final String lines, final int threads, final int errorLineNr, final String errorCode, boolean simulateWorkerError)
		{
			super(lines);
			this.simulateWorkerError = simulateWorkerError;
			this.errorCode = errorCode;
			setMaxThreads(threads);
			this.errorLineNr = errorLineNr;
			setCSVWriter(new CSVWriter(new NullWriter()));
		}
		
		@Override
		protected Item processValueLineFromWorker(ValueLine line) throws ImpExException
		{
			if( simulateWorkerError && isErrorLine(line))
			{
				throw new RuntimeException("PLA_12491_Test: Simulating uncaught worker error!");
			}
			else
			{
				return super.processValueLineFromWorker(line);
			}
		}

		@Override
		protected boolean processPendingResult(final ImpExWorkerResult result)
		{
			final boolean ret = super.processPendingResult(result);
			if (isErrorLine(result.getLine()))
			{
				errorLineProcessed = true;
			}
			return ret;
		}
		
		protected boolean isErrorLine(ValueLine valueLine)
		{
			return valueLine.getSource().entrySet().stream().anyMatch(e -> errorCode.equals(e.getValue()));
		}

		@Override
		protected boolean readLineFromWorker() throws ImpExException
		{
			final boolean notDone = super.readLineFromWorker();

			// after reaching the error line we have to slow down
			// noticeably to avoid reading all lines before any worker
			// could throw a error !
			if (--errorLineNr <= 0 && !errorLineProcessed)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (final InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
			}
			return notDone;
		}

		public boolean wasErrorLineProcessed()
		{
			return errorLineProcessed;
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

}
