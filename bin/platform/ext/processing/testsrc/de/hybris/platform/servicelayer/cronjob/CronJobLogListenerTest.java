/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.servicelayer.cronjob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.jalo.CronJobLogListener;
import de.hybris.platform.cronjob.jalo.CronJobLogListener.CronJobLogContext;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;



/**
 *
 */
@IntegrationTest
public class CronJobLogListenerTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(CronJobLogListenerTest.class.getName());

	@Test
	public void testErrorInLogListenerLogging()
	{
		final TestLogContext testCtx = new TestLogContext();

		try
		{
			CronJobLogListener.setCurrentContext(testCtx);

			// plain log
			LOG.info("test log");

			// simulate inner info log
			LOG.info(TestLogContext.RECUSIVE_LOG_INFO);

			// simulate inner error
			LOG.error(TestLogContext.RECUSIVE_LOG_ERROR);

			// simulate inner exception
			LOG.error(TestLogContext.EXCEPTION_IN_LOG);

			assertEquals(4, testCtx.messages.size());
			assertTrue(testCtx.messages.get(0).contains("test log"));
			assertTrue(testCtx.messages.get(1).contains(TestLogContext.RECUSIVE_LOG_INFO));
			assertTrue(testCtx.messages.get(2).contains(TestLogContext.RECUSIVE_LOG_ERROR));
			assertTrue(testCtx.messages.get(3).contains(TestLogContext.EXCEPTION_IN_LOG));

		}
		catch (final StackOverflowError e)
		{
			fail("error dealing with reentrant logging: " + e);
		}
		finally
		{
			CronJobLogListener.unsetsetCurrentContext();
		}
	}

	static class TestLogContext implements CronJobLogContext
	{
		static final String RECUSIVE_LOG_INFO = "RECUSIVE_LOG_INFO";
		static final String RECUSIVE_LOG_ERROR = "RECUSIVE_LOG_ERROR";
		static final String EXCEPTION_IN_LOG = "EXCEPTION_IN_LOG";

		private final List<String> messages = new ArrayList<>();

		@Override
		public void writeLog(final String msg, final Level level)
		{
			messages.add(msg);

			if (msg.contains(EXCEPTION_IN_LOG))
			{
				throw new RuntimeException(EXCEPTION_IN_LOG);
			}
			else if (msg.contains(RECUSIVE_LOG_INFO))
			{
				LOG.info(RECUSIVE_LOG_INFO);
			}
			else if (msg.contains(RECUSIVE_LOG_ERROR))
			{
				LOG.info(RECUSIVE_LOG_ERROR);
			}
		}

		@Override
		public boolean isEnabledFor(final Level level)
		{
			return true;
		}
	}

}