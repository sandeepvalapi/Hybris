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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.CSVReader;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ImpExImportScriptingIntegrationTest extends AbstractImpExTest
{
	private final PropertyConfigSwitcher configSwitcher = new PropertyConfigSwitcher("impex.legacy.scripting");

	@Before
	public void setUp() throws Exception
	{
		configSwitcher.switchToValue("false");
	}

	@After
	public void tearDown() throws Exception
	{
		configSwitcher.switchBackToDefault();
	}

	@Test
	public void shouldReturnNullLineWhenInBeforeEachIsCallToClearLineBSH() throws Exception
	{
		// given
		final ImpExImportReader reader = createTestImpExImportReader("beforeEach-marker.bsh.impex");

		// when
		final Object line = reader.readLine();

		// then
		assertThat(line).isNull();
	}

	@Test
	public void shouldReturnNullLineWhenInBeforeEachIsCallToClearLineBSHAlt() throws Exception
	{
		// given
		final ImpExImportReader reader = createTestImpExImportReader("beforeEach-marker.bsh.alt.impex");

		// when
		final Object line = reader.readLine();

		// then
		assertThat(line).isNull();
	}

	@Test
	public void shouldReturnNullLineWhenInBeforeEachIsCallToClearLineGROOVY() throws Exception
	{
		// given
		final ImpExImportReader reader = createTestImpExImportReader("beforeEach-marker.groovy.impex");

		// when
		final Object line = reader.readLine();

		// then
		assertThat(line).isNull();
	}

	@Test
	public void shouldProperlyInterpretBooleanExpressionsInIfMarkerBSH() throws Exception
	{
		// given
		final ImpExImportReader reader = createTestImpExImportReader("if-marker.bsh.impex");

		// when
		final Object line1 = reader.readLine();
		final Object line2 = reader.readLine();

		// then
		assertThat(line1).isInstanceOf(Title.class);
		assertThat(((Title) line1).getCode()).isEqualTo("foo");
		assertThat(line2).isNull();
	}

	@Test
	public void shouldProperlyInterpretBooleanExpressionsInIfMarkerBSHAlt() throws Exception
	{
		// given
		final ImpExImportReader reader = createTestImpExImportReader("if-marker.bsh.alt.impex");

		// when
		final Object line1 = reader.readLine();
		final Object line2 = reader.readLine();

		// then
		assertThat(line1).isInstanceOf(Title.class);
		assertThat(((Title) line1).getCode()).isEqualTo("foo");
		assertThat(line2).isNull();
	}

	@Test
	public void shouldProperlyInterpretBooleanExpressionsInIfMarkerGROOVY() throws Exception
	{
		// given
		final ImpExImportReader reader = createTestImpExImportReader("if-marker.groovy.impex");

		// when
		final Object line1 = reader.readLine();
		final Object line2 = reader.readLine();

		// then
		assertThat(line1).isInstanceOf(Title.class);
		assertThat(((Title) line1).getCode()).isEqualTo("foo");
		assertThat(line2).isNull();
	}

	@Test
	public void shouldExecuteCodeInAfterEachMarkerBSH() throws Exception
	{
		// given
		final ImpExImportReader reader = createTestImpExImportReader("afterEach-marker.bsh.impex");

		// when
		final Object line1 = reader.readLine();
		final Object line2 = reader.readLine();

		// then
		assertThat(line1).isInstanceOf(Title.class);
		assertThat(((Title) line1).getCode()).isEqualTo("foo_modified");
		assertThat(((Title) line2).getCode()).isEqualTo("bar_modified");
	}

	@Test
	public void shouldExecuteCodeInAfterEachMarkerBSHAlt() throws Exception
	{
		// given
		final ImpExImportReader reader = createTestImpExImportReader("afterEach-marker.bsh.alt.impex");

		// when
		final Object line1 = reader.readLine();
		final Object line2 = reader.readLine();

		// then
		assertThat(line1).isInstanceOf(Title.class);
		assertThat(((Title) line1).getCode()).isEqualTo("foo_modified");
		assertThat(((Title) line2).getCode()).isEqualTo("bar_modified");
	}

	@Test
	public void shouldExecuteCodeInAfterEachMarkerGROOVY() throws Exception
	{
		// given
		final ImpExImportReader reader = createTestImpExImportReader("afterEach-marker.groovy.impex");

		// when
		final Object line1 = reader.readLine();
		final Object line2 = reader.readLine();

		// then
		assertThat(line1).isInstanceOf(Title.class);
		assertThat(((Title) line1).getCode()).isEqualTo("foo_modified");
		assertThat(((Title) line2).getCode()).isEqualTo("bar_modified");
	}

	private ImpExImportReader createTestImpExImportReader(final String importFileName)
	{
		try
		{
			final InputStream inputStream = ImpExManager.class.getResourceAsStream("/impex/testfiles/scripting/" + importFileName);
			final ImpExImportReader importReader = new ImpExImportReader(new CSVReader(inputStream, "UTF-8"));
			importReader.enableCodeExecution(true);
			return importReader;
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
}
