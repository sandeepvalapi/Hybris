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
package de.hybris.platform.scripting.engine.internal.impl;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.scripting.engine.internal.ScriptEngineType;
import de.hybris.platform.scripting.engine.internal.ScriptEnginesRegistry;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test/test-scripting-spring.xml")
public class DefaultScriptEnginesRegistryTest
{
	@Autowired
	private ScriptEnginesRegistry registry;


	@Test
	public void shouldReturnSetOfRegistredEngineTypes() throws Exception
	{
		// when
		final Set<ScriptEngineType> engineTypes = registry.getRegisteredEngineTypes();

		// then
		assertThat(engineTypes).isNotNull().hasSize(3);
	}

	@Test
	public void setOfRegisteredEngineTypesReturnedByRegistryShouldBeImmutable() throws Exception
	{
		// given
		final Set<ScriptEngineType> engineTypes = registry.getRegisteredEngineTypes();

		try
		{
            // when
			engineTypes.remove(engineTypes.iterator().next());
            fail("should throw UnsupportedOperationException");
		}
		catch (final UnsupportedOperationException e)
		{
            // then fine
		}
	}

	@Test
	public void shouldReturnRegisteredEngineTypeByItsName() throws Exception
	{
		// given
		final String scriptType = "groovy";

		// when
		final ScriptEngineType engineType = registry.getScriptEngineType(scriptType);

		// then
		assertThat(engineType).isNotNull();
		assertThat(engineType.getName()).isEqualTo("groovy");
		assertThat(engineType.getFileExtension()).isEqualTo("groovy");
		assertThat(engineType.getMime()).isEqualTo("application/x-groovy");
	}

	@Test
	public void shouldReturnRegisteredEngineTypeByItsFileExtension() throws Exception
	{
		// given
		final String scriptType = "js";

		// when
		final ScriptEngineType engineType = registry.getScriptEngineType(scriptType);

		// then
		assertThat(engineType).isNotNull();
		assertThat(engineType.getName()).isEqualTo("javascript");
		assertThat(engineType.getFileExtension()).isEqualTo("js");
		assertThat(engineType.getMime()).isEqualTo("application/x-javascript");
	}

	@Test
	public void shouldReturnRegisteredEngineTypeByItsMime() throws Exception
	{
		// given
		final String scriptType = "application/x-bsh";

		// when
		final ScriptEngineType engineType = registry.getScriptEngineType(scriptType);

		// then
		assertThat(engineType).isNotNull();
		assertThat(engineType.getName()).isEqualTo("beanshell");
		assertThat(engineType.getFileExtension()).isEqualTo("bsh");
		assertThat(engineType.getMime()).isEqualTo("application/x-bsh");
	}

	@Test
	public void shouldThrowIllegalStateExceptionWhenRequestingForNotRegisteredEngineType() throws Exception
	{
		// given
		final String scriptType = "non-existent";

		try
		{
			// when
			registry.getScriptEngineType(scriptType);
		}
		catch (final Exception e)
		{
			// then fine
		}
	}
}
