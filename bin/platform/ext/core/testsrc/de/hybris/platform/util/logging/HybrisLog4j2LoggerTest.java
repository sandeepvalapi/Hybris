/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.util.logging;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.logging.log4j2.HybrisLog4j2Logger;

import org.apache.logging.log4j.Level;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;


@UnitTest
public class HybrisLog4j2LoggerTest
{
	@Test
	public void testMappingV2toV1WithCustomLoggers()
	{
		final org.apache.logging.log4j.Logger log4j2Logger = org.apache.logging.log4j.LogManager
				.getLogger(LoggingFrameworksBridgeTest.class);

		final org.apache.log4j.Level betweenTraceAndDebugLevel = HybrisLog4j2Logger.mapV2ToV1Level(Level.forName("TEST1", 550));
		assertThat(betweenTraceAndDebugLevel).isEqualTo(org.apache.log4j.Level.DEBUG);

		final org.apache.log4j.Level betweenOffAndFatalLevel = HybrisLog4j2Logger.mapV2ToV1Level(Level.forName("TEST2", 50));
		assertThat(betweenOffAndFatalLevel).isEqualTo(org.apache.log4j.Level.FATAL);

		final org.apache.log4j.Level betweenErrorAndWarn = HybrisLog4j2Logger.mapV2ToV1Level(Level.forName("TEST3", 250));
		assertThat(betweenErrorAndWarn).isEqualTo(org.apache.log4j.Level.ERROR);

		final org.apache.log4j.Level offLevel = HybrisLog4j2Logger.mapV2ToV1Level(Level.forName("TEST4", 0));
		assertThat(offLevel).isEqualTo(org.apache.log4j.Level.OFF);

		final org.apache.log4j.Level allLevel = HybrisLog4j2Logger.mapV2ToV1Level(Level.forName("TEST5", Integer.MAX_VALUE));
		assertThat(allLevel).isEqualTo(org.apache.log4j.Level.ALL);

	}

	@Test
	public void testCustomLogLevelForListeners()
	{

		final HybrisLogListener listener = Mockito.mock(HybrisLogListener.class);
		Mockito.when(listener.isEnabledFor(Mockito.any())).thenReturn(true);

		try
		{
			HybrisLogger.addListener(listener);

			final org.apache.logging.log4j.Logger log4j2Logger = org.apache.logging.log4j.LogManager
					.getLogger(HybrisLog4j2LoggerTest.class);

			log4j2Logger.log(org.apache.logging.log4j.Level.forName("FATAL_WARN", 150), "log message");

			Mockito.verify(listener, Mockito.atLeast(1))
					.log(Mockito.argThat(new LogLevelArgumentMatcher(org.apache.log4j.Level.FATAL)));
		}
		finally
		{
			HybrisLogger.removeListener(listener);
		}
	}

	private static class LogLevelArgumentMatcher extends ArgumentMatcher<HybrisLoggingEvent>
	{
		private final org.apache.log4j.Level level;

		private LogLevelArgumentMatcher(final org.apache.log4j.Level level)
		{
			this.level = level;
		}

		@Override
		public boolean matches(final Object argument)
		{
			final HybrisLoggingEvent hybrisEvent = (HybrisLoggingEvent) argument;

			return hybrisEvent.getLevel().equals(level);
		}
	}

}
