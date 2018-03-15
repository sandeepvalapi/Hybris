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
package de.hybris.platform.util.logging;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@ManualTest
public class LoggingFrameworksBridgeTest extends ServicelayerBaseTest
{
	@Test
	public void testCommonsLogging()
	{
		System.out.println("> > >");
		final Log commonsLogger = LogFactory.getLog(LoggingFrameworksBridgeTest.class);

		commonsLogger.trace("commons trace");
		commonsLogger.debug("commons debug");
		commonsLogger.info("commons info");
		commonsLogger.warn("commons warn");
		commonsLogger.error("commons error");
		commonsLogger.fatal("commons fatal");

		System.out.println("> > >");
		final Logger slf4jLogger = LoggerFactory.getLogger(LoggingFrameworksBridgeTest.class);
		slf4jLogger.trace("slf4j trace");
		slf4jLogger.debug("slf4j debug");
		slf4jLogger.info("slf4j info");
		slf4jLogger.warn("slf4j warn");
		slf4jLogger.error("slf4j error");

		final Marker fatal = MarkerFactory.getMarker("FATAL");
		slf4jLogger.error(fatal, "slf4j error");

		System.out.println("> > >");
		final org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger(LoggingFrameworksBridgeTest.class);

		log4jLogger.trace("log4j trace");
		log4jLogger.debug("log4j debug");
		log4jLogger.info("log4j info");
		log4jLogger.warn("log4j warn");
		log4jLogger.error("log4j error");
		log4jLogger.fatal("log4j fatal");

		System.out.println("> > >");
		final org.apache.logging.log4j.Logger log4j2Logger = org.apache.logging.log4j.LogManager.getLogger(LoggingFrameworksBridgeTest.class);

		log4j2Logger.trace("log4j2 trace");
		log4j2Logger.debug("log4j2 debug");
		log4j2Logger.info("log4j2 info");
		log4j2Logger.warn("log4j2 warn");
		log4j2Logger.error("log4j2 error");
		log4j2Logger.fatal("log4j2 fatal");
	}
}
