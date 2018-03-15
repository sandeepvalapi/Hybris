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
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.internal.ScriptEngineType;
import de.hybris.platform.scripting.engine.internal.ScriptEnginesRegistry;
import de.hybris.platform.util.CSVReader;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ScriptCodeLineFactoryTest
{
	private ScriptCodeLineFactory factory;
	@Mock
	private ReaderManager readerManager;
	@Mock
	private CSVReader reader;
	@Mock
	private ScriptEnginesRegistry scriptEnginesRegistry;
	@Mock
	private ScriptEngineType beanshell, javascript, groovy;
	@Mock
	private ScriptingLanguagesService scriptingLanguagesService;

	@Before
	public void setUp() throws Exception
	{
		given(beanshell.getName()).willReturn("beanshell");
		given(beanshell.getFileExtension()).willReturn("bsh");
		given(javascript.getName()).willReturn("javascript");
		given(javascript.getFileExtension()).willReturn("js");
		given(groovy.getName()).willReturn("groovy");
		given(groovy.getFileExtension()).willReturn("groovy");
		given(scriptEnginesRegistry.getRegisteredEngineTypes()).willReturn(Sets.newHashSet(beanshell, javascript, groovy));
		given(readerManager.getMainReader()).willReturn(reader);
		given(reader.getCommentOut()).willReturn(new char[]
		{ '#' });

		factory = new ScriptCodeLineFactory(readerManager, scriptingLanguagesService)
		{
			@Override
			ScriptEnginesRegistry getScriptEnginesRegistry()
			{
				return scriptEnginesRegistry;
			}
		};

		given(readerManager.peekReader()).willReturn(reader);
		given(readerManager.getCurrentLocation()).willReturn("-> fake location");
		given(Integer.valueOf(reader.getCurrentLineNumber())).willReturn(Integer.valueOf(1));

	}

	@Test
	public void shouldCreateBeanshellControlMarkerLessCodeLineFromNewScriptDefinitionUsingFileExtensionAsEngineName()
			throws Exception
	{
		// given
		final String scriptLine = "#%bsh% print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("bsh");
		assertThat(codeLine.getMarker()).isNull();
	}

	@Test
	public void shouldCreateBeanshellControlMarkerLessCodeLineFromNewScriptDefinitionUsingEngineName() throws Exception
	{
		// given
		final String scriptLine = "#%beanshell% print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("beanshell");
		assertThat(codeLine.getMarker()).isNull();
	}

	@Test
	public void shouldCreateBeanshellCodeLineWith_beforeEach_ControlMarkerFromNewScriptDefinitionUsingFileExtensionAsEngineName()
			throws Exception
	{
		// given
		final String scriptLine = "#%bsh% beforeEach: print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("bsh");
		assertThat(codeLine.getMarker()).isEqualTo("beforeEach:");
	}

	@Test
	public void shouldCreateBeanshellCodeLineWith_beforeEach_ControlMarkerFromNewScriptDefinitionUsingEngineName()
			throws Exception
	{
		// given
		final String scriptLine = "#%beanshell% beforeEach: print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("beanshell");
		assertThat(codeLine.getMarker()).isEqualTo("beforeEach:");
	}

	@Test
	public void shouldCreateBeanshellCodeLineWith_if_ControlMarkerFromNewScriptDefinitionUsingEngineName() throws Exception
	{
		// given
		final String scriptLine = "#%beanshell% if: print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("beanshell");
		assertThat(codeLine.getMarker()).isEqualTo("if:");
	}

	@Test
	public void shouldCreateBeanshellCodeLineWith_if_ControlMarkerFromNewScriptDefinitionUsingFileExtensionAsEngineName()
			throws Exception
	{
		// given
		final String scriptLine = "#%bsh% if: print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("bsh");
		assertThat(codeLine.getMarker()).isEqualTo("if:");
	}

	@Test
	public void shouldCreateBeanshellCodeLineWith_afterEach_ControlMarkerFromNewScriptDefinitionUsingEngineName() throws Exception
	{
		// given
		final String scriptLine = "#%beanshell% afterEach: print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("beanshell");
		assertThat(codeLine.getMarker()).isEqualTo("afterEach:");
	}

	@Test
	public void shouldCreateBeanshellCodeLineWith_afterEach_ControlMarkerFromNewScriptDefinitionUsingFileExtensionAsEngineName()
			throws Exception
	{
		// given
		final String scriptLine = "#%bsh% afterEach: print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("bsh");
		assertThat(codeLine.getMarker()).isEqualTo("afterEach:");
	}

	@Test
	public void shouldCreateBeanshellCodeLineWith_beforeEach_ControlMarkerFromOldScriptDefinitionUsingEngineName()
			throws Exception
	{
		// given
		final String scriptLine = "#% beforeEach: print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("bsh");
		assertThat(codeLine.getMarker()).isEqualTo("beforeEach:");
	}

	@Test
	public void shouldCreateBeanshellCodeLineWith_if_ControlMarkerFromOldScriptDefinitionUsingEngineName() throws Exception
	{
		// given
		final String scriptLine = "#% if: print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("bsh");
		assertThat(codeLine.getMarker()).isEqualTo("if:");
	}

	@Test
	public void shouldCreateBeanshellCodeLineWith_afterEach_ControlMarkerFromOldScriptDefinitionUsingEngineName() throws Exception
	{
		// given
		final String scriptLine = "#% afterEach: print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("bsh");
		assertThat(codeLine.getMarker()).isEqualTo("afterEach:");
	}


	@Test
	public void shouldCreateBeanshellControlMarkerLessCodeLineFromMultiLineScript() throws Exception
	{
		// given
		final String scriptLine = "#% beforeEach:\n print(\"foo\");";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEqualTo("print(\"foo\");");
		assertThat(codeLine).isInstanceOf(AbstractScriptingEngineCodeLine.class);
		assertThat(((AbstractScriptingEngineCodeLine) codeLine).getScriptingEngineName()).isEqualTo("bsh");
		assertThat(codeLine.getMarker()).isEqualTo("beforeEach:");
	}

	@Test
	public void shouldCreateCodeLineFrom_endif_ControlMarker() throws Exception
	{
		// given
		final String scriptLine = "#%endif:";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEmpty();
		assertThat(codeLine).isInstanceOf(SimpleCodeLine.class);
		assertThat(codeLine.getMarker()).isEqualTo("endif:");
	}

	@Test
	public void shouldCreateCodeLineFrom_beforeEachend_ControlMarker() throws Exception
	{
		// given
		final String scriptLine = "#%beforeEach:end";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEmpty();
		assertThat(codeLine).isInstanceOf(SimpleCodeLine.class);
		assertThat(codeLine.getMarker()).isEqualTo("beforeEach:end");
	}

	@Test
	public void shouldCreateCodeLineFrom_afterEachend_ControlMarker() throws Exception
	{
		// given
		final String scriptLine = "#%afterEach:end";
		final Integer colPos = Integer.valueOf(0);
		final Map<Integer, String> line = ImmutableMap.<Integer, String> builder().put(colPos, scriptLine).build();

		// when
		final AbstractCodeLine codeLine = factory.createCodeLineFromLine(line);

		// then
		assertThat(codeLine).isNotNull();
		assertThat(codeLine.getExecutableCode()).isEmpty();
		assertThat(codeLine).isInstanceOf(SimpleCodeLine.class);
		assertThat(codeLine.getMarker()).isEqualTo("afterEach:end");
	}

}
