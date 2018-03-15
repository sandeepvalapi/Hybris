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
package de.hybris.platform.scripting.engine.content.impl;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.scripting.engine.exception.ScriptURIException;

import java.util.Map;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


public class ResourceScriptContentTest
{
	@Test
	public void shouldCreateClassPathScriptContentObject() throws Exception
	{
		// given
		final ClassPathResource resource = new ClassPathResource("test/test-script.groovy");
		final ResourceScriptContent scriptContent = new ResourceScriptContent(resource);

		// when
		final String content = scriptContent.getContent();
		final String engineName = scriptContent.getEngineName();
		final Map<String, Object> customContext = scriptContent.getCustomContext();

		// then
		assertThat(content).isEqualTo("def names = ['John', 'Richard', \"Peter\"]\nnames.sort().join(',')");
		assertThat(engineName).isEqualTo("groovy");
		assertThat(customContext).isEmpty();
	}

	@Test
	public void shouldThrowScriptURIExceptionWhenFileExtensionFromResourceCannotBeDetermined() throws Exception
	{
		// given
		final ClassPathResource resource = new ClassPathResource("test/test-script");

		try
		{
			// when
			new ResourceScriptContent(resource);
			fail("should throw ScriptURIException");
		}
		catch (final ScriptURIException e)
		{
			// then fine
		}

	}
}
