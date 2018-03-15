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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.content.impl.ModelScriptContent;
import de.hybris.platform.scripting.engine.content.impl.SimpleScriptContent;
import de.hybris.platform.scripting.engine.exception.DisabledScriptException;
import de.hybris.platform.scripting.engine.exception.ScriptExecutionException;
import de.hybris.platform.scripting.engine.internal.cache.impl.ModelScriptCacheKey;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.test.TestThreadsHolder;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class DefaultScriptingLanguagesServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource(name = "scriptingLanguagesService")
	private ScriptingLanguagesService service;
	@Resource
	private ModelService modelService;
	@Resource
	private CacheRegion scriptsCacheRegion;
	private ScriptModel model;

	@Before
	public void setUp() throws Exception
	{
		prepareScriptModels();
	}

	private void prepareScriptModels() throws FileNotFoundException
	{
		prepareScriptModel("fooBar", "def value = one + two + three\n value");
		model = prepareScriptModel("bazBar", "spring.getBean \"modelService\"");
		prepareScriptModel("fancyStuff", "modelService");
	}

	private ScriptModel prepareScriptModel(final String code, final String content)
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setScriptType(ScriptType.GROOVY);
		script.setCode(code);
		script.setContent(content);

		modelService.save(script);

		return script;
	}

	@Test
	public void shouldExecuteSimpleGroovyScriptWithExternalParametersBackedByModel() throws Exception
	{
		// given
		final String scriptURI = "model://fooBar";
		final Map<String, Object> params = ImmutableMap.<String, Object> builder() //
				.put("one", Integer.valueOf(5)) //
				.put("two", Integer.valueOf(10)) //
				.put("three", Integer.valueOf(15)) //
				.build();

		// when
		final ScriptExecutable executable = service.getExecutableByURI(scriptURI);
		final ScriptExecutionResult result = executable.execute(params);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getScriptResult()).isEqualTo(Integer.valueOf(30));
	}

	@Test
	public void shouldExecuteGroovyScriptBackedByModelInteractingWithMagic_spring_Variable() throws Exception
	{
		// given
		final ModelScriptContent content = new ModelScriptContent(model);

		// when
		final ScriptExecutable executable = service.getExecutableByContent(content);
		final ScriptExecutionResult result = executable.execute();

		// then
		assertThat(result).isNotNull();
		assertThat(result.getScriptResult()).isInstanceOf(ModelService.class);
	}

	@Test
	public void shouldExecuteSimpleGroovyScriptBackedByModel() throws Exception
	{
		// given
		final String scriptURI = "model://bazBar";
		final Map<String, Object> ctx = new HashMap<>();
		ctx.put("spring", Registry.getApplicationContext());

		// when
		final ScriptExecutable executable = service.getExecutableByURI(scriptURI);
		final ScriptExecutionResult result = executable.execute(ctx);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getScriptResult()).isInstanceOf(ModelService.class);
	}

	@Test
	public void shouldExecuteSimpleGroovyScriptBackedByModelInteractingWithSpringBeanById() throws Exception
	{
		// given
		final String scriptURI = "model://fancyStuff";

		// when
		final ScriptExecutable executable = service.getExecutableByURI(scriptURI);
		final ScriptExecutionResult result = executable.execute();

		// then
		assertThat(result).isNotNull();
		assertThat(result.getScriptResult()).isInstanceOf(ModelService.class);
	}

	@Test
	public void shouldReturnInstanceOfAnInterfacePassingParameter() throws Exception
	{
		// given
		final String scriptURI = "classpath://test/test-script-returning-interface.groovy";
		final Map<String, Object> ctx1 = ImmutableMap.<String, Object> builder().put("id", "foo").build();
		final Map<String, Object> ctx2 = ImmutableMap.<String, Object> builder().put("id", "bar").build();

		// when
		final ScriptExecutable script = service.getExecutableByURI(scriptURI);
		final TenantService tenantService1 = script.getAsInterface(TenantService.class, ctx1);
		final TenantService tenantService2 = script.getAsInterface(TenantService.class, ctx2);

		// then
		assertThat(tenantService1.getCurrentTenantId()).isEqualTo("fakeTenant foo");
		assertThat(tenantService2.getCurrentTenantId()).isEqualTo("fakeTenant bar");
	}

	@Test
	public void shouldUseCacheWhenGettingExecutableByURIFromClasspathRepository() throws Exception
	{
		// given
		scriptsCacheRegion.clearCache();
		final String scriptURI = "classpath://test/test-script.groovy";

		// when
		service.getExecutableByURI(scriptURI);
		service.getExecutableByURI(scriptURI);
		service.getExecutableByURI(scriptURI);
		service.getExecutableByURI(scriptURI);

		// then
		final CacheStatistics statistics = scriptsCacheRegion.getCacheRegionStatistics();
		assertThat(statistics.getMissCount()).isEqualTo(1);
		assertThat(statistics.getHitCount()).isEqualTo(3);
	}

	@Test
	public void shouldUseCacheWhenGettingExecutableByURIFromModelRepository() throws Exception
	{
		// given
		scriptsCacheRegion.clearCache();
		final String scriptURI = "model://fooBar";

		// when
		service.getExecutableByURI(scriptURI);
		service.getExecutableByURI(scriptURI);
		service.getExecutableByURI(scriptURI);
		service.getExecutableByURI(scriptURI);

		// then
		final CacheStatistics statistics = scriptsCacheRegion.getCacheRegionStatistics();
		assertThat(statistics.getMissCount()).isEqualTo(1);
		assertThat(statistics.getHitCount()).isEqualTo(3);
	}

	@Test
	public void shouldAutoDisableScriptOnFirstScriptExecutionException() throws Exception
	{
		// given
		final String scriptURI = "model://badScript";
		prepareBadScriptModel("badScript", "var foo = [\"x\", \"y\"]\n foo.sor");
		final ScriptExecutable executable = service.getExecutableByURI(scriptURI);

		// when
		for (int i = 0; i < 10; i++)
		{
			try
			{
				executable.execute();
				fail("Should throw ScriptExecutionException/DisabledScriptException");
			}
			catch (final ScriptExecutionException | DisabledScriptException e)
			{
				// fine
			}
		}

		// then
		assertThat(executable.isDisabled()).isTrue();
	}

	@Test
	public void shouldAutoDisableScriptOnFirstScriptExecutionExceptionConcurrently() throws Exception
	{
		// given
		final ScriptModel scriptModel = prepareBadScriptModel("badScript", "foo.sort");
		assertThat(modelService.isNew(scriptModel)).isFalse();
		final ScriptExecutable executable = service.getExecutableByURI("model://badScript");
		final TestThreadsHolder<BadScriptRunner> holder = new TestThreadsHolder<>(300, new BadScriptRunner(executable), true);

		// when
		holder.startAll();
		holder.waitForAll(30, TimeUnit.SECONDS);

		// then
		final Map<Integer, Throwable> errors = holder.getErrors();
		if (!errors.isEmpty())
		{
			fail("Errors occured during threads execution: " + errors);
		}

		assertThat(scriptModel.isDisabled()).isTrue();
	}

	private ScriptModel prepareBadScriptModel(final String code, final String content)
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setScriptType(ScriptType.GROOVY);
		script.setCode(code);
		script.setContent(content);
		script.setAutodisabling(true);

		modelService.save(script);

		return script;
	}

	@Test
	public void onceDisabledScriptExecutableAfterRemovingFromCacheShouldBeStillDisabledWhenObtainedAgain() throws Exception
	{
		// given
		final String scriptURI = "model://badScript";
		prepareBadScriptModel("badScript", "var foo = [\"x\", \"y\"]\n foo.sor");
		final ScriptExecutable executable = service.getExecutableByURI(scriptURI);
		try
		{
			executable.execute();
		}
		catch (final Exception e)
		{
			// fine
		}

		// when
		scriptsCacheRegion.clearCache();
		final ScriptExecutable executableNewInstance = service.getExecutableByURI(scriptURI);

		// then
		assertThat(executableNewInstance.isDisabled()).isTrue();
	}

	@Test
	public void shouldUseCacheWhenGettingExecutableByURIWithRevisionFromModelRepository() throws Exception
	{
		// given
		scriptsCacheRegion.clearCache();
		final ScriptModel scriptModel = prepareScriptModel("versioned", "def value = 123\nvalue");
		final Long firstRevision = scriptModel.getVersion();
		final String firstRevisionURI = "model://versioned/" + firstRevision;


		// when
		scriptModel.setContent("def value = 456\nvalue");
		modelService.save(scriptModel);

		service.getExecutableByURI(firstRevisionURI);
		service.getExecutableByURI(firstRevisionURI);
		service.getExecutableByURI(firstRevisionURI);
		final ScriptExecutable firstRevisionExecutable = service.getExecutableByURI(firstRevisionURI);
		final ScriptExecutable latestExecutable = service.getExecutableByURI("model://versioned");

		// then
		final CacheStatistics statistics = scriptsCacheRegion.getCacheRegionStatistics();
		assertThat(statistics.getMissCount()).isEqualTo(2);
		assertThat(statistics.getHitCount()).isEqualTo(3);

		assertThat(firstRevisionExecutable.execute().getScriptResult()).isEqualTo(Integer.valueOf(123));
		assertThat(latestExecutable.execute().getScriptResult()).isEqualTo(Integer.valueOf(456));
	}

	@Test
	public void shouldInvalidateCacheWhenModelBasedScriptIsRemoved() throws Exception
	{
		// given
		scriptsCacheRegion.clearCache();
		final String scriptURI = "model://bazBar";
		service.getExecutableByURI(scriptURI); // fill in the cache
		final ModelScriptCacheKey cacheKey = new ModelScriptCacheKey(model.getPk(), "junit");
		assertThat(scriptsCacheRegion.get(cacheKey)).isNotNull();

		// when
		modelService.remove(model);
		final Object cached = scriptsCacheRegion.get(cacheKey);

		// then
		assertThat(cached).isNull();
	}

	@Test
	public void shouldExecuteTheSameGroovyScriptWithDifferentParamsConcurrently() throws Exception
	{
		runScriptConcurrently("classpath://test/test-script-with-params.groovy");
	}

	@Test
	public void shouldExecuteTheSameJavascriptScriptWithDifferentParamsConcurrently() throws Exception
	{
		runScriptConcurrently("classpath://test/test-script-with-params.js");
	}

	@Test
	public void shouldExecuteTheSameBeanshellScriptWithDifferentParamsConcurrently() throws Exception
	{
		runScriptConcurrently("classpath://test/test-script-with-params.bsh");
	}

	@Test
	public void shouldLoadTheSameClassForTheSameGroovyScript()
	{
		final SimpleScriptContent content = new SimpleScriptContent(ScriptType.GROOVY.getCode(),
				Joiner.on("\n").join( //
						"class TestSupplier implements java.util.function.Supplier", //
						"{", //
						"private static int COUNTER = 0;", //
						"@Override", //
						"public Object get()", //
						"{", //
						"	return COUNTER++;", //
						"}", //
						"}", //
						"new TestSupplier().get()"));

		assertThat(service.getExecutableByContent(content).execute().getScriptResult()).isEqualTo(Integer.valueOf(0));
		assertThat(service.getExecutableByContent(content).execute().getScriptResult()).isEqualTo(Integer.valueOf(1));
		assertThat(service.getExecutableByContent(content).execute().getScriptResult()).isEqualTo(Integer.valueOf(2));
	}

	private void runScriptConcurrently(final String scriptURI)
	{
		final TestThreadsHolder<ScriptRunner> holder = new TestThreadsHolder<>(300, new ScriptRunner(scriptURI), true);

		holder.startAll();
		holder.waitForAll(30, TimeUnit.SECONDS);

		final Map<Integer, Throwable> errors = holder.getErrors();

		if (!errors.isEmpty())
		{
			fail("Errors occured during threads execution: " + errors);
		}
	}

	class ScriptRunner implements Runnable
	{
		private final ScriptExecutable executable;

		ScriptRunner(final String scriptURI)
		{
			executable = service.getExecutableByURI(scriptURI);
		}

		@Override
		public void run()
		{
			final double one = RandomUtils.nextDouble();
			final double two = RandomUtils.nextDouble();
			final double three = RandomUtils.nextDouble();

			final Map<String, Object> params = new HashMap<>(3);
			params.put("one", Double.valueOf(one));
			params.put("two", Double.valueOf(two));
			params.put("three", Double.valueOf(three));
			final ScriptExecutionResult result = executable.execute(params);

			assertThat(result.getScriptResult()).isEqualTo(Double.valueOf(one + two + three));

			//System.out.println("Params: " + one + "," + two + "," + three + " Result: " + result.getScriptResult() + ", output: "
			//		+ result.getOutputWriter().toString());
		}
	}

	private static final Logger LOG = Logger.getLogger(DefaultScriptingLanguagesServiceIntegrationTest.class);

	class BadScriptRunner implements Runnable
	{
		private final ScriptExecutable executable;

		BadScriptRunner(final ScriptExecutable executable)
		{
			this.executable = executable;
		}

		@Override
		public void run()
		{
			try
			{
				executable.execute();
			}
			catch (final ScriptExecutionException | DisabledScriptException e)
			{
				LOG.debug("executed script with failure: " + e.getMessage());
			}
		}
	}

}
