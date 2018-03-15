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


import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.logging.log4j2.HybrisToLog4j2PropsConverter;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;


@UnitTest
public class Log4j2HybrisConfigParserTest
{

	private final HybrisToLog4j2PropsConverter propertiesParser = new HybrisToLog4j2PropsConverter();

	@Test
	public void shouldGenerateLoggersProperty()
	{
		// given
		final Properties properties = new Properties();
		properties.put("log4j2.logger.hsqldb.name", "hsqldb.db");
		properties.put("log4j2.logger.hsqldb.level", "warn");
		properties.put("log4j2.logger.hsqldb.appenderRef.stdout.ref", "STDOUT");

		properties.put("log4j2.logger.hmc.name", "de.hybris.platform.servicelayer.hmc");
		properties.put("log4j2.logger.hmc.level", "warn");
		properties.put("log4j2.logger.hmc.appenderRef.stdout.ref", "STDOUT");

		// when
		final Properties log4jProperties = propertiesParser.convertToLog4jProps(properties);

		// then
		assertThat(log4jProperties.get("logger.hsqldb.name")).isEqualTo("hsqldb.db");
		assertThat(log4jProperties.get("logger.hsqldb.level")).isEqualTo("warn");
		assertThat(log4jProperties.get("logger.hsqldb.appenderRef.stdout.ref")).isEqualTo("STDOUT");

		final List<String> loggers = splitAndTrim(log4jProperties.get("loggers").toString());
		assertThat(loggers).containsOnly("hsqldb", "hmc");
	}

	@Test
	public void shouldGenerateAppendersProperty()
	{
		// given
		final Properties properties = new Properties();
		properties.put("log4j2.appender.console.type", "Console");
		properties.put("log4j2.appender.console.name", "STDOUT");

		properties.put("log4j2.appender.list.type", "List");
		properties.put("log4j2.appender.list.name", "List");

		// when
		final Properties log4jProperties = propertiesParser.convertToLog4jProps(properties);

		// then
		assertThat(log4jProperties.get("appender.console.type")).isEqualTo("Console");
		assertThat(log4jProperties.get("appender.console.name")).isEqualTo("STDOUT");

		assertThat(log4jProperties.get("appender.list.type")).isEqualTo("List");
		assertThat(log4jProperties.get("appender.list.name")).isEqualTo("List");

		final List<String> appenders = splitAndTrim(log4jProperties.get("appenders").toString());
		assertThat(appenders).containsOnly("console", "list");
	}

	@Test
	public void shouldGenerateFiltersProperty()
	{
		// given
		final Properties properties = new Properties();
		properties.put("log4j2.filter.threshold.type", "ThresholdFilter");
		properties.put("log4j2.filter.threshold.level", "error");

		properties.put("log4j2.filter.foo.type", "FooFilter");
		properties.put("log4j2.filter.foo.level", "error");

		// when
		final Properties log4jProperties = propertiesParser.convertToLog4jProps(properties);

		// then
		assertThat(log4jProperties.get("filter.threshold.type")).isEqualTo("ThresholdFilter");
		assertThat(log4jProperties.get("filter.threshold.level")).isEqualTo("error");

		assertThat(log4jProperties.get("filter.foo.type")).isEqualTo("FooFilter");
		assertThat(log4jProperties.get("filter.foo.level")).isEqualTo("error");

		final List<String> filters = splitAndTrim(log4jProperties.get("filters").toString());
		assertThat(filters).containsOnly("foo", "threshold");
	}

	private List<String> splitAndTrim(final String filters)
	{
		return Lists.newArrayList(Splitter.on(",").split(filters)).stream().map(i -> i.trim()).collect(Collectors.toList());
	}
}
