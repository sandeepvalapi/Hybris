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
package de.hybris.platform.scripting.engine.impl;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.content.impl.ResourceScriptContent;
import de.hybris.platform.scripting.engine.content.impl.SimpleScriptContent;
import de.hybris.platform.scripting.engine.exception.ScriptURIException;
import de.hybris.platform.servicelayer.tenant.TenantService;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test/test-scripting-spring.xml")
public class DefaultScriptingLanguagesServiceTest
{
	@Autowired
	private TestScriptingLanguagesService service;
	@Autowired
	private ApplicationContext appContext;

	@Before
	public void setUp() throws Exception
	{
        service.setApplicationContext(appContext);
	}

	@Test
	public void shouldReturnExecutableFromClasspathRepository() throws Exception
	{
		// given
		final String scriptURI = "classpath://test/test-script.groovy";

		// when
		final ScriptExecutable executable = service.getExecutableByURI(scriptURI);

		// then
		assertThat(executable).isNotNull();
	}

	@Test
	public void shouldThrowScriptURIExceptionIfURIDoesNotContainProtocolPart() throws Exception
	{
		// given
		final String scriptURI = "foo/bar/baz";

		try
		{
			// when
			service.getExecutableByURI(scriptURI);
			fail("should throw ScriptURIException");
		}
		catch (final ScriptURIException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowScriptURIExceptionIfURIContainEmptyProtocolPart() throws Exception
	{
		// given
		final String scriptURI = "://foo/bar/baz";

		try
		{
			// when
			service.getExecutableByURI(scriptURI);
			fail("should throw ScriptURIException");
		}
		catch (final ScriptURIException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowScriptURIExceptionIfURIContainEmptyPathPart() throws Exception
	{
		// given
		final String scriptURI = "classpath://";

		try
		{
			// when
			service.getExecutableByURI(scriptURI);
			fail("should throw ScriptURIException");
		}
		catch (final ScriptURIException e)
		{
			// then fine
			System.out.println(e);
		}
	}

	@Test
	public void shouldExecuteScriptBasedOnResourceHavingJustAnOutput() throws Exception
	{
		// given
		final ResourceScriptContent content = new ResourceScriptContent(
				new ClassPathResource("test/test-script-with-output.groovy"));

		// when
		final ScriptExecutable script = service.getExecutableByContent(content);
		final ScriptExecutionResult result = script.execute();

		// then
		assertThat(result).isNotNull();
		assertThat(result.getScriptResult()).isNull();
		assertThat(result.getOutputWriter().toString()).isEqualTo("Hello World\n");
	}

	@Test
	public void shouldExecuteSimpleGroovyScriptWithExternalParameters() throws Exception
	{
		// given
		final ClassPathResource resource = new ClassPathResource("test/test-script-with-params.groovy");
		final ResourceScriptContent content = new ResourceScriptContent(resource);
		final Map<String, Object> params = ImmutableMap.<String, Object> builder() //
				.put("one", Integer.valueOf(5)) //
				.put("two", Integer.valueOf(10)) //
				.put("three", Integer.valueOf(15)) //
				.build();


		// when
		final ScriptExecutable script = service.getExecutableByContent(content);
		final ScriptExecutionResult result = script.execute(params);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getScriptResult()).isEqualTo(Integer.valueOf(30));
	}

	@Test
	public void shouldReturnInstanceOfAnInterfacePassingParameter() throws Exception
	{
		// given
		final ClassPathResource resource = new ClassPathResource("test/test-script-returning-interface.groovy");
		final ResourceScriptContent content = new ResourceScriptContent(resource);
		final Map<String, Object> ctx1 = ImmutableMap.<String, Object> builder().put("id", "foo").build();
		final Map<String, Object> ctx2 = ImmutableMap.<String, Object> builder().put("id", "bar").build();

		// when
		final ScriptExecutable script = service.getExecutableByContent(content);
		final TenantService tenantService1 = script.getAsInterface(TenantService.class, ctx1);
		final TenantService tenantService2 = script.getAsInterface(TenantService.class, ctx2);

		// then
		assertThat(tenantService1.getCurrentTenantId()).isEqualTo("fakeTenant foo");
		assertThat(tenantService2.getCurrentTenantId()).isEqualTo("fakeTenant bar");
	}

	@Test
	public void shouldReturnInstanceOfModelServiceTakenFromSpringCtx() throws Exception
	{
		// given
		final String body = "spring.getBean \"testBean\"";
		final SimpleScriptContent content = new SimpleScriptContent("groovy", body);

		// when
		final ScriptExecutable script = service.getExecutableByContent(content);
		final ScriptExecutionResult result = script.execute();

		// then
		assertThat(result.getScriptResult()).isNotNull().isInstanceOf(HashMap.class);
	}

}
