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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.suspend.SuspendOptions;

import org.junit.Test;


@UnitTest
public class SuspenderThreadUnitTest
{

	@Test
	public void shouldBeNamedSuspenderThread()
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final SuspenderThread thread = givenSuspenderThread(tr);

		assertThat(thread.getName()).isEqualTo(SuspenderThread.class.getSimpleName());
	}

	@Test
	public void shouldRegisterItselfInTheRegistryAndUnregisterAtTheEnd() throws InterruptedException
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final SuspenderThread thread = givenSuspenderThread(tr);
		final Long threadId = Long.valueOf(thread.getId());

		thread.startAndWaitForThreadToBeRunning();
		assertThat(tr.getAllOperations()).containsOnlyKeys(threadId);

		thread.join();
		assertThat(tr.getAllOperations()).isEmpty();
	}

	@Test
	public void shouldFinishRightAfterThereIsNoNotSuspendableThreads() throws InterruptedException
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final SuspenderThread thread = givenSuspenderThread(tr);
		final Long threadId = Long.valueOf(thread.getId());

		RegistrableThread.registerThread(OperationInfo.builder().asNotSuspendableOperation().build(), tr);

		thread.startAndWaitForThreadToBeRunning();
		assertThat(tr.getAllOperations()).containsOnlyKeys(threadId, Long.valueOf(Thread.currentThread().getId()));

		thread.join(1000);
		assertThat(thread.isAlive()).isTrue();

		RegistrableThread.unregisterThread(tr);
		thread.join(1000);
		assertThat(tr.getAllOperations()).isEmpty();
	}

	@Test
	public void shouldStopOnDemand() throws InterruptedException
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final SuspenderThread thread = givenSuspenderThread(tr);
		final Long threadId = Long.valueOf(thread.getId());

		RegistrableThread.registerThread(OperationInfo.builder().asNotSuspendableOperation().build(), tr);

		thread.startAndWaitForThreadToBeRunning();
		assertThat(tr.getAllOperations()).containsOnlyKeys(threadId, Long.valueOf(Thread.currentThread().getId()));

		thread.stopAndWaitForThreadToBeFinished();

		assertThat(thread.isAlive()).isFalse();
		assertThat(tr.getAllOperations()).doesNotContainKey(threadId);
	}

	@Test
	public void shouldStopWhenInterrupted() throws InterruptedException
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final SuspenderThread thread = givenSuspenderThread(tr);
		final Long threadId = Long.valueOf(thread.getId());

		RegistrableThread.registerThread(OperationInfo.builder().asNotSuspendableOperation().build(), tr);

		thread.startAndWaitForThreadToBeRunning();
		assertThat(tr.getAllOperations()).containsOnlyKeys(threadId, Long.valueOf(Thread.currentThread().getId()));

		thread.join(1000);
		assertThat(thread.isAlive()).isTrue();

		thread.interrupt();

		thread.join(1000);
		assertThat(thread.isAlive()).isFalse();
		assertThat(tr.getAllOperations()).doesNotContainKey(threadId);
	}

	private SuspenderThread givenSuspenderThread(final ThreadRegistry threadRegistry)
	{
		final SuspenderThread thread = new SuspenderThread(SuspendOptions.defaultOptions(), threadRegistry);
		thread.setWaitTime(100);
		return thread;
	}

	private ThreadRegistry givenThreadRegistry()
	{
		return new ThreadRegistry(() -> false);
	}

}
