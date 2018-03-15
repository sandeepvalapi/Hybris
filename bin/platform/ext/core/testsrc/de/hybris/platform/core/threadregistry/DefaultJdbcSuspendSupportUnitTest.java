/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.core.threadregistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;

import org.junit.Test;


@UnitTest
public class DefaultJdbcSuspendSupportUnitTest
{

	@Test
	public void shouldNotThrowExceptionWhenThreadIsNotRegisteredAndSystemIsRunning()
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultJdbcSuspendSupport jdbcSupport = givenJdbcSuspendSupport(tr);

		assertThat(tr.getAllOperations()).isEmpty();

		jdbcSupport.aboutToBorrowTheConnection();

		assertThat("Exception was not thrown").isNotNull();
	}

	@Test
	public void shouldNotThrowExceptionWhenThreadIsSuspendableAndSystemIsRunning()
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultJdbcSuspendSupport jdbcSupport = givenJdbcSuspendSupport(tr);

		tr.register(OperationInfo.empty());

		assertThat(tr.getAllOperations()).containsOnlyKeys(Long.valueOf(Thread.currentThread().getId()));

		jdbcSupport.aboutToBorrowTheConnection();

		assertThat("Exception was not thrown").isNotNull();
	}

	@Test
	public void shouldNotThrowExceptionWhenThreadIsNotSuspendableAndSystemIsRunning()
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultJdbcSuspendSupport jdbcSupport = givenJdbcSuspendSupport(tr);

		tr.register(OperationInfo.builder().asNotSuspendableOperation().build());

		assertThat(tr.getAllOperations()).containsOnlyKeys(Long.valueOf(Thread.currentThread().getId()));

		jdbcSupport.aboutToBorrowTheConnection();

		assertThat("Exception was not thrown").isNotNull();
	}

	@Test
	public void shouldThrowExceptionWhenThreadIsNotRegisteredAndSystemIsNotRunning()
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultJdbcSuspendSupport jdbcSupport = givenJdbcSuspendSupport(tr);

		tr.blockNotSuspendableOperations();

		assertThat(tr.getAllOperations()).isEmpty();

		assertThatExceptionOfType(SystemIsSuspendedException.class).isThrownBy(jdbcSupport::aboutToBorrowTheConnection);
	}

	@Test
	public void shouldThrowExceptionWhenThreadIsSuspendableAndSystemIsNotRunning()
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultJdbcSuspendSupport jdbcSupport = givenJdbcSuspendSupport(tr);

		tr.register(OperationInfo.empty());
		tr.blockNotSuspendableOperations();

		assertThat(tr.getAllOperations()).containsOnlyKeys(Long.valueOf(Thread.currentThread().getId()));

		assertThatExceptionOfType(SystemIsSuspendedException.class).isThrownBy(jdbcSupport::aboutToBorrowTheConnection);

	}

	@Test
	public void shouldNotThrowExceptionWhenThreadIsNotSuspendableAndSystemIsNotRunning()
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultJdbcSuspendSupport jdbcSupport = givenJdbcSuspendSupport(tr);

		tr.register(OperationInfo.builder().asNotSuspendableOperation().build());
		tr.blockNotSuspendableOperations();

		assertThat(tr.getAllOperations()).containsOnlyKeys(Long.valueOf(Thread.currentThread().getId()));

		jdbcSupport.aboutToBorrowTheConnection();

		assertThat("Exception was not thrown").isNotNull();
	}

	private DefaultJdbcSuspendSupport givenJdbcSuspendSupport(final ThreadRegistry threadRegistry)
	{
		return new DefaultJdbcSuspendSupport(threadRegistry);
	}

	private ThreadRegistry givenThreadRegistry()
	{
		return new ThreadRegistry(() -> false);
	}
}
