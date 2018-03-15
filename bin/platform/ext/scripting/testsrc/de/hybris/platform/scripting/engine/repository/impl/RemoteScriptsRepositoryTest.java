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
package de.hybris.platform.scripting.engine.repository.impl;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test/test-scripting-spring.xml")
public class RemoteScriptsRepositoryTest
{
	@Autowired
	private RemoteScriptsRepository repository;

	@Test
	public void shouldHandleHTTPProtocol() throws Exception
	{
		// given
		final String protocol = "http";

		// when
		final boolean valid = repository.getSupportedProtocols().contains(protocol);

		// then
		assertThat(valid).isTrue();
	}

	@Test
	public void shouldHandleHTTPSProtocol() throws Exception
	{
		// given
		final String protocol = "https";

		// when
		final boolean valid = repository.getSupportedProtocols().contains(protocol);

		// then
		assertThat(valid).isTrue();
	}

	@Test
	public void shouldHandleFTPProtocol() throws Exception
	{
		// given
		final String protocol = "ftp";

		// when
		final boolean valid = repository.getSupportedProtocols().contains(protocol);

		// then
		assertThat(valid).isTrue();
	}

	@Test
	public void shouldNotHandleInvalidProtocol() throws Exception
	{
		// given
		final String protocol = "non-existent";

		// when
		final boolean valid = repository.getSupportedProtocols().contains(protocol);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotHandleNullProtocol() throws Exception
	{
		// given
		final String protocol = null;

		// when
		final boolean valid = repository.getSupportedProtocols().contains(protocol);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldThrowScriptNotFoundExceptionWhenScriptCannotBeFoundInRepository() throws Exception
	{
		// given
		final String protocol = "http";
		final String path = "nonExistent";

		try
		{
			// when
			repository.lookupScript(protocol, path);
			fail("should throw ScriptNotFoundException");
		}
		catch (final ScriptNotFoundException e)
		{
			// then fine
		}

	}
}
