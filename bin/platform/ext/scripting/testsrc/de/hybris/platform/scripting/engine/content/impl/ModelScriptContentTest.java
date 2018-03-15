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
import static org.mockito.BDDMockito.given;

import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;

import java.util.Map;

import de.hybris.platform.servicelayer.model.ItemModelContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ModelScriptContentTest
{
	@Mock
	private ScriptModel model;
    @Mock
    private ItemModelContext itemModelCtx;

    @Before
    public void setUp() throws Exception
    {
    	given(model.getScriptType()).willReturn(ScriptType.GROOVY);
        given(model.getContent()).willReturn("def names = ['John', 'Richard', \"Peter\"]\n" +
                "names.sort().join(',')");
        given(model.getItemModelContext()).willReturn(itemModelCtx);
        given(Boolean.valueOf(itemModelCtx.isNew())).willReturn(Boolean.FALSE);
    }

	@Test
	public void shouldCreateModelScriptContentObject() throws Exception
	{
		// given
		final ModelScriptContent scriptContent = new ModelScriptContent(model);

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
