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
import de.hybris.platform.core.suspend.SuspendResult;
import de.hybris.platform.core.suspend.SystemStatus;

import org.junit.Test;


@UnitTest
public class DefaultSuspendResultUnitTest
{

	@Test
	public void shouldReturnNullResumTokenWhenSystemIsSuspended()
	{
		final SuspendResult result = DefaultSuspendResult.systemIsSuspendedOrWaiting(SystemStatus.SUSPENDED);

		assertThat(result).isNotNull();
		assertThat(result.getCurrentStatus()).isSameAs(SystemStatus.SUSPENDED);
		assertThat(result.getResumeToken()).isNull();
	}

	@Test
	public void shouldReturnNullResumTokenWhenSystemIsWaiting()
	{
		final SuspendResult result = DefaultSuspendResult.systemIsSuspendedOrWaiting(SystemStatus.WAITING_FOR_SUSPEND);

		assertThat(result).isNotNull();
		assertThat(result.getCurrentStatus()).isSameAs(SystemStatus.WAITING_FOR_SUSPEND);
		assertThat(result.getResumeToken()).isNull();
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenSystemIsRunningAndTokenIsNotGiven()
	{
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> DefaultSuspendResult.systemIsSuspendedOrWaiting(SystemStatus.RUNNING));
	}

	@Test
	public void shouldReturnResumTokenWhenSystemHasBeenSuspended()
	{
		final SuspendResult result = DefaultSuspendResult.systemHasBeenRequestedToSuspend(SystemStatus.RUNNING, "TEST");

		assertThat(result).isNotNull();
		assertThat(result.getCurrentStatus()).isSameAs(SystemStatus.RUNNING);
		assertThat(result.getResumeToken()).isEqualTo("TEST");
	}

}
