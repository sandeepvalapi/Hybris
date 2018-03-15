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
package de.hybris.platform.util.logging.log4j2;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Properties;

import org.junit.Test;

@UnitTest
public class HybrisToLog4j2PropsConverterTest
{


	private static final String LOG4J2 = "log4j2";
	private static final String APPENDER_CONSOLE_LAYOUT_PATTERN = "appender.console.layout.pattern";
	private static final String LOG4J2_APPENDER_CONSOLE_LAYOUT_PATTERN = LOG4J2 + "." + APPENDER_CONSOLE_LAYOUT_PATTERN;
	private static final String PATTERN_WITH_HIGHLIGHT_COLORS = "%highlight{%d [%t] %-5level: %msg%n%throwable}{FATAL=white, ERROR=red, WARN=blue, INFO=black, DEBUG=green, TRACE=blue}";
	private static final String PATTERN_WITH_HIGHLIGHT_DEFAULT = "%highlight{%d [%t] %-5level: %msg%n%throwable}";
	private static final String PATTERN_WITH_COLORS = "%black{%d [%t]} %cyan{%-5level:} %green{%msg%n%throwable}";
	private static final String PATTERN_WITH_HIGHLIGHT_STYLE = "%highlight{%d [%t] %-5level: %msg%n%throwable}{STYLE=Logback}";
	private static final String PATTERN_WITH_HIGHLIGHT_INLINE = "%d [%t] %highlight{%-5level: %msg%n%throwable}";
	private static final String PATTERN_WITH_HIGHLIGHT_INLINE_AND_STYLE = "%style{%d [%t]}{black} %highlight{%-5level: %msg%n%throwable}";
	private static final String PATTERN_WITH_STYLE = "%style{%d}{black} %style{[%t]}{blue} %style{%-5level:}{yellow} %style{%msg%n%throwable}{green}";
	private static final String PATTERN_WITHOUT_HIGHLIGHT = "%d [%t] %-5level: %msg%n%throwable";

	private final HybrisToLog4j2PropsConverter converter = new HybrisToLog4j2PropsConverter()
	{
		@Override
		public boolean isANSIDisable(final Properties platformProperties)
		{
			return true;
		}
	};
	
	@Test
	public void shouldUpdatePatternWhenAnsiDisabled() 
	{
		// given
		final Properties properties = givenAnsiColorsAreDisabledWithLayoutPattern(HybrisToLog4j2PropsConverterTest.PATTERN_WITH_HIGHLIGHT_DEFAULT);
		// when
		final Properties log4jProperties = converter.convertToLog4jProps(properties);
		// then
		assertThatPatternIsWithoutHighlights(properties, log4jProperties);
	}
	
	@Test
	public void shouldNotUpdatePatternWithoutHighlightWhenAnsiDisabled() 
	{
		// given
		final Properties properties = givenAnsiColorsAreDisabledWithLayoutPattern(PATTERN_WITHOUT_HIGHLIGHT);
		// when
		final Properties log4jProperties = converter.convertToLog4jProps(properties);
		// then
		assertThatPatternIsWithoutHighlights(properties, log4jProperties);
	}
	
	@Test
	public void shouldUpdatePatternWithColorsWhenAnsiDisabled() 
	{
		// given
		final Properties properties = givenAnsiColorsAreDisabledWithLayoutPattern(HybrisToLog4j2PropsConverterTest.PATTERN_WITH_HIGHLIGHT_COLORS);
		// when
		final Properties log4jProperties = converter.convertToLog4jProps(properties);
		// then
		assertThatPatternIsWithoutHighlights(properties, log4jProperties);
	}
	
	@Test
	public void shouldUpdatePatternWithStyleWhenAnsiDisabled() 
	{
		// given
		final Properties properties = givenAnsiColorsAreDisabledWithLayoutPattern(PATTERN_WITH_STYLE);
		// when
		final Properties log4jProperties = converter.convertToLog4jProps(properties);
		// then
		assertThatPatternIsWithoutHighlights(properties, log4jProperties);
	}
	
	@Test
	public void shouldUpdatePatternWithHighlightStyleWhenAnsiDisabled() 
	{
		// given
		final Properties properties = givenAnsiColorsAreDisabledWithLayoutPattern(PATTERN_WITH_HIGHLIGHT_STYLE);
		// when
		final Properties log4jProperties = converter.convertToLog4jProps(properties);
		// then
		assertThatPatternIsWithoutHighlights(properties, log4jProperties);
	}
	
	@Test
	public void shouldUpdateInlinePatternWhenAnsiDisabled() 
	{
		// given
		final Properties properties = givenAnsiColorsAreDisabledWithLayoutPattern(PATTERN_WITH_HIGHLIGHT_INLINE);
		// when
		final Properties log4jProperties = converter.convertToLog4jProps(properties);
		// then
		assertThatPatternIsWithoutHighlights(properties, log4jProperties);
	}
	
	@Test
	public void shouldUpdatePatternWithInlineAndStyleWhenAnsiDisabled() 
	{
		// given
		final Properties properties = givenAnsiColorsAreDisabledWithLayoutPattern(PATTERN_WITH_HIGHLIGHT_INLINE_AND_STYLE);
		// when
		final Properties log4jProperties = converter.convertToLog4jProps(properties);
		// then
		assertThatPatternIsWithoutHighlights(properties, log4jProperties);
	}
	
	@Test
	public void shouldUpdatePatternWithColorStylesWhenAnsiDisabled() 
	{
		// given
		final Properties properties = givenAnsiColorsAreDisabledWithLayoutPattern(PATTERN_WITH_COLORS);
		// when
		final Properties log4jProperties = converter.convertToLog4jProps(properties);
		// then
		assertThatPatternIsWithoutHighlights(properties, log4jProperties);
	}

	/**
	 * Sets console layout to given pattern and disables ANSI colors.
	 */
	protected Properties givenAnsiColorsAreDisabledWithLayoutPattern(String layout) {
		final Properties properties = new Properties();
		properties.put("ansi.colors", "false");
		properties.put("log4j2.appender.console.layout.pattern", layout);
		return properties;
	}
	
	/**
	 * Asserts that the console layout pattern does not include highlight syntax.
	 */
	protected void assertThatPatternIsWithoutHighlights(Properties hybrisProperties, Properties log4jProperties) {
		assertThat(hybrisProperties.getProperty("log4j2.appender.console.layout.pattern")).isEqualTo(PATTERN_WITHOUT_HIGHLIGHT);
		assertThat(log4jProperties.get("ansi.colors")).isNull();
		assertThat(log4jProperties.get("appender.console.layout.pattern")).isEqualTo(PATTERN_WITHOUT_HIGHLIGHT);
	}
	
	@Test
	public void shouldNotSetPatternWhenAnsiDisabledAndPatternNotSet() 
	{
		// given
		final Properties properties = new Properties();
		properties.put("ansi.colors", "false");

		// when
		final Properties log4jProperties = converter.convertToLog4jProps(properties);

		// then
		assertThat(properties.getProperty("log4j2.appender.console.layout.pattern")).isNull();
		assertThat(log4jProperties.get("ansi.colors")).isNull();
		assertThat(log4jProperties.get("appender.console.layout.pattern")).isNull();
	}
	
	@Test
	public void shouldRemoveHighlightSyntaxWhenItCapturesWholeText()
	{
		convertAndAssertIfEqualTo("%highlight{%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]}",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldRemoveHighlightSyntaxWhenItCapturesBeginingOfText()
	{
		convertAndAssertIfEqualTo("%highlight{%-5p [%t] %X{RemoteAddr}}%X{Tenant}%X{CronJob}[%c{1}]",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldRemoveHighlightSyntaxWhenItCapturesMiddleOfText()
	{
		convertAndAssertIfEqualTo("%-5p [%t] %highlight{%X{RemoteAddr}%X{Tenant}%X{CronJob}}[%c{1}]",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldRemoveHighlightSyntaxWithStyleWhenItCapturesMiddleOfText()
	{
		convertAndAssertIfEqualTo("%-5p [%t] %highlight{%X{RemoteAddr}%X{Tenant}%X{CronJob}}{STYLE=Logback}[%c{1}]",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldRemoveHighlightSyntaxWithStyleWhenItCapturesEndOfText()
	{
		convertAndAssertIfEqualTo("%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}%highlight{[%c{1}]}{STYLE=Logback}",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldDoNothingWhenErrorInSyntax()
	{
		convertAndAssertIfEqualTo("%highlight{%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}{[%c{1}]",
				"%highlight{%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}{[%c{1}]");
	}

	@Test
	public void shouldDoNothingWhenErrorInSyntax2()
	{
		convertAndAssertIfEqualTo("%highlight%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}}[%c{1}]",
				"%highlight%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}}[%c{1}]");
	}




	private void convertAndAssertIfEqualTo(final String convertFrom, final String compareTo)
	{
		final Properties props = new Properties();

		props.put(LOG4J2_APPENDER_CONSOLE_LAYOUT_PATTERN, convertFrom);

		final Properties convertToLog4jProps = converter.convertToLog4jProps(props);

		assertThat(convertToLog4jProps.get(APPENDER_CONSOLE_LAYOUT_PATTERN)).isEqualTo(compareTo);



	}



}
