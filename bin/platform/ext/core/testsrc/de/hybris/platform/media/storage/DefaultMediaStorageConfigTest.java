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
package de.hybris.platform.media.storage;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class DefaultMediaStorageConfigTest
{
	private DefaultMediaStorageConfig config;
	private Map<String, Object> settings;


	@Before
	public void setUp() throws Exception
	{
		settings = new HashMap<String, Object>();
		settings.put("foo", "bar");
		settings.put("boolean", Boolean.TRUE);

		config = new DefaultMediaStorageConfig(settings);
	}


	@Test
	public void shouldReturnDefaultValueIfRequestedParamDoesNotExist()
	{
		// given
		final String key = "nonExistent";

		// when
		final String value = config.getParameter(key, String.class, "default");

		// then
		assertThat(value).isNotNull().isEqualTo("default");
	}

	@Test
	public void shouldThrowIllegalStateExceptionWhenRequestingParamWithWrongType()
	{
		// given
		final String key = "boolean";

		try
		{
			// when
			config.getParameter(key, String.class);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalStateException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldReturnParamAsRequiredType()
	{
		// given
		final String key = "boolean";

		// when
		final Boolean value = config.getParameter(key, Boolean.class);

		// then
		assertThat(value).isNotNull().isTrue();
	}

	@Test
	public void shouldReturnNullWhenRequestingParamAndKeyDoesNotExist()
	{
		// given
		final String key = "nonExistent";

		// when
		final String value = config.getParameter(key);

		// then
		assertThat(value).isNull();
	}

	@Test
	public void shouldReturnParamAsPlainString()
	{
		// given
		final String key = "foo";

		// when
		final String value = config.getParameter(key);

		// then
		assertThat(value).isNotNull().isEqualTo("bar");
	}
}
