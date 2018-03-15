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
package de.hybris.platform.healthcheck.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.healthcheck.HealthCheck;
import de.hybris.platform.healthcheck.HealthCheckService;
import de.hybris.platform.healthcheck.internal.HealthCheckRegistry;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DefaultHealthCheckServiceTest
{
	@Mock
	private HealthCheckRegistry registry;
	@Mock
	private HealthCheck healthCheck1, healthCheck2;
	@InjectMocks
	private final HealthCheckService service = new DefaultHealthCheckService();

	@Before
	public void setUp() throws Exception
	{
		given(registry.getHealthChecks()).willReturn(ImmutableSet.<HealthCheck> builder().add(healthCheck1, healthCheck2).build());
	}

	/**
	 * Expected structure:
	 *
	 * <pre>
	 *     {
	 *         "foo": "one",
	 *         "bar": "two"
	 *     }
	 * </pre>
	 */
	@Test
	public void shouldAllowToBuildResultsWithFlatStructure() throws Exception
	{
		// given
		given(healthCheck1.perform()).willReturn(getHelathCheckResult("foo", "one"));
		given(healthCheck2.perform()).willReturn(getHelathCheckResult("bar", "two"));

		// when
		final Map<String, Object> result = service.performInstanceHealthCheck();

		// then
		assertThat(result).hasSize(2);
		assertThat(result.get("foo")).isEqualTo("one");
		assertThat(result.get("bar")).isEqualTo("two");
	}

	/**
	 * Expected structure:
	 *
	 * <pre>
	 *     {
	 *         "foo": {
	 *             "bar": {
	 *                 "baz": "one",
	 *                 "boom": "two"
	 *             }
	 *         }
	 *     }
	 * </pre>
	 */
	@Test
	public void shouldCombineHealthCheckResultsWithSameSubKeys() throws Exception
	{
		// given
		given(healthCheck1.perform()).willReturn(getHelathCheckResult("foo.bar.baz", "one"));
		given(healthCheck2.perform()).willReturn(getHelathCheckResult("foo.bar.boom", "two"));

		// when
		final Map<String, Object> result = service.performInstanceHealthCheck();

		// then
		assertThat(result).hasSize(1);
		assertThat((Map) result.get("foo")).hasSize(1);
		assertThat(((Map) ((Map) result.get("foo")).get("bar"))).hasSize(2);
		assertThat(((Map) ((Map) result.get("foo")).get("bar")).get("baz")).isEqualTo("one");
		assertThat(((Map) ((Map) result.get("foo")).get("bar")).get("boom")).isEqualTo("two");
	}

	/**
	 * Expected structure:
	 *
	 * <pre>
	 * {
	 *     "ala": {
	 *         "ela": {
	 *             "ala": "one",
	 *             "ola": {
	 *                 "ala": "two"
	 *             }
	 *         }
	 *     }
	 * }
	 * </pre>
	 */
	@Test
	public void should() throws Exception
	{
		// given
		given(healthCheck1.perform()).willReturn(getHelathCheckResult("ala.ela.ala", "one"));
		given(healthCheck2.perform()).willReturn(getHelathCheckResult("ala.ela.ola.ala", "two"));

		// when
		final Map<String, Object> result = service.performInstanceHealthCheck();

		// then
		assertThat(result).hasSize(1);
		assertThat((Map) result.get("ala")).hasSize(1);
		assertThat(((Map) ((Map) result.get("ala")).get("ela"))).hasSize(2);
		assertThat(((Map) ((Map) result.get("ala")).get("ela")).get("ala")).isEqualTo("one");
		assertThat((Map) ((Map) ((Map) result.get("ala")).get("ela")).get("ola")).hasSize(1);
		assertThat(((Map) ((Map) ((Map) result.get("ala")).get("ela")).get("ola")).get("ala")).isEqualTo("two");
	}

	@Test
	public void shouldReturnEmptyResultWhenChecksAreNotProductingResults() throws Exception
	{
		// given
		given(healthCheck1.perform()).willReturn(Collections.<MultiKey, Object> emptyMap());
		given(healthCheck2.perform()).willReturn(Collections.<MultiKey, Object> emptyMap());

		// when
		final Map<String, Object> result = service.performInstanceHealthCheck();

		// then
		assertThat(result).isEmpty();
	}

	@Test
	public void shouldReturnUnmodifableMapOfResults() throws Exception
	{
		// given
		given(healthCheck1.perform()).willReturn(getHelathCheckResult("foo.bar", "baz"));

		try
		{
			// when
			final Map<String, Object> result = service.performInstanceHealthCheck();
			result.put("baz", "boom");
			fail("should throw UnsupportedOperationException");
		}
		catch (final UnsupportedOperationException e)
		{
			// then
		}
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenSomeOfRegisteredChecksReturnsNullResult() throws Exception
	{
		// given
		given(healthCheck1.perform()).willReturn(null);

		try
		{
			// when
			service.performInstanceHealthCheck();
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then fine
		}
	}

	private Map<MultiKey, Object> getHelathCheckResult(final String key, final Object value)
	{
		final String[] keys = Iterables.toArray(Splitter.on('.').split(key), String.class);
		return ImmutableMap.<MultiKey, Object> builder().put(new MultiKey(keys), value).build();
	}
}
