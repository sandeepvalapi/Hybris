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

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;


public class SimpleScriptContentTest
{
	@Test
	public void shouldCreateSimpleScriptContentObject() throws Exception
	{
		// given
		final SimpleScriptContent scriptContent = new SimpleScriptContent("groovy",
				"def names = ['John', 'Richard', \"Peter\"]\nnames.sort().join(',')");

		// when
        final String content = scriptContent.getContent();
        final String engineName = scriptContent.getEngineName();
		final Map<String, Object> customContext = scriptContent.getCustomContext();

		// then
        assertThat(content).isEqualTo("def names = ['John', 'Richard', \"Peter\"]\nnames.sort().join(',')");
		assertThat(engineName).isEqualTo("groovy");
		assertThat(customContext).isEmpty();
	}
}
