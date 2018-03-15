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
package de.hybris.platform.persistence.security;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jgroups.util.UUID;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;


public class DigestCalculatorConcurrencyTest
{
	private DigestCalculator digestCalculator;

	@Before
	public void setUp()
	{
		digestCalculator = DigestCalculator.getInstance("MD5");
	}

	@Test
	public void test()
	{
		final Set<String> plainValues = plainValues();
		final Map<String, String> sequentiallyDigestedValues = createDigestsSequentially(plainValues);

		final Map<String, String> concurrenlyDigestedValues = createDigestsConcurrently(plainValues);

		assertThat(sequentiallyDigestedValues).isEqualTo(concurrenlyDigestedValues);
	}

	private Map<String, String> createDigestsConcurrently(final Set<String> plainValues)
	{
		final Map<String, String> digested = plainValues.parallelStream()
				.collect(Collectors.toMap(p -> p, p -> digestCalculator.calculateDigest(p)));
		return ImmutableMap.copyOf(digested);
	}

	private Set<String> plainValues()
	{
		return ImmutableSet
				.copyOf(IntStream.range(0, 1_000).mapToObj(i -> UUID.randomUUID().toString()).collect(Collectors.toSet()));
	}

	private Map<String, String> createDigestsSequentially(final Set<String> plainValues)
	{
		final Map<String, String> digested = plainValues.stream()
				.collect(Collectors.toMap(p -> p, p -> digestCalculator.calculateDigest(p)));
		return ImmutableMap.copyOf(digested);
	}
}
