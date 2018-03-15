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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.suspend.ResumeOptions;
import de.hybris.platform.core.suspend.ResumeTokenVerificationFailed;
import de.hybris.platform.core.suspend.RunningThread;
import de.hybris.platform.core.suspend.SuspendOptions;
import de.hybris.platform.core.suspend.SuspendResult;
import de.hybris.platform.core.suspend.SystemState;
import de.hybris.platform.core.suspend.SystemStatus;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.util.concurrent.CountDownLatch;

import org.junit.BeforeClass;
import org.junit.Test;


@IntegrationTest
public class DefaultSuspendResumeServiceUnitTest extends ServicelayerBaseTest
{
	@BeforeClass
	public static void startMasterTenant()
	{
		//Suspend resume is a feature which is not tenant aware. It logic depends on master tenant being active.
		final Tenant previousTenant = Registry.activateMasterTenant();
		if (previousTenant == null)
		{
			Registry.unsetCurrentTenant();
		}
		else
		{
			Registry.setCurrentTenant(previousTenant);
		}
	}

	@Test
	public void shouldBeInRunningStateWhenThereAreNoThreads()
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultSuspendResumeService service = givenSuspendResumeService(tr);

		final SystemStatus status = service.getSystemStatus();
		final SystemState state = service.getSystemState();

		assertThat(status).isNotNull().isSameAs(SystemStatus.RUNNING);
		assertThat(state).isNotNull();
		assertThat(state.getStatus()).isNotNull().isSameAs(SystemStatus.RUNNING);
		assertThat(state.getRootThreads()).isNotNull().isEmpty();
	}

	@Test
	public void shouldBeInRunningStateWhenThereAreRunning()
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultSuspendResumeService service = givenSuspendResumeService(tr);

		tr.register(OperationInfo.empty());

		final SystemStatus status = service.getSystemStatus();
		final SystemState state = service.getSystemState();

		assertThat(status).isNotNull().isSameAs(SystemStatus.RUNNING);
		assertThat(state).isNotNull();
		assertThat(state.getStatus()).isNotNull().isSameAs(SystemStatus.RUNNING);
		assertThat(state.getRootThreads()).isNotNull().hasSize(1);

		final RunningThread thread = state.getRootThreads().iterator().next();
		assertThat(thread).isNotNull();
		assertThat(thread.getThreadId()).isEqualTo(Thread.currentThread().getId());
		assertThat(thread.getThreadName()).isEqualTo(Thread.currentThread().getName());
		assertThat(thread.getChildThreads()).isEmpty();
	}

	@Test
	public void shouldReportThreadAsARootThreadWhenItsParentIsGone() throws InterruptedException
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultSuspendResumeService service = givenSuspendResumeService(tr);
		final CountDownLatch childIsRunning = new CountDownLatch(1);
		final CountDownLatch finishChildLatch = new CountDownLatch(1);
		final CountDownLatch finishParentLatch = new CountDownLatch(1);


		final Thread parent = new RegistrableThread("parent")
		{
			@Override
			protected void internalRun()
			{
				new RegistrableThread("child")
				{
					@Override
					protected void internalRun()
					{
						childIsRunning.countDown();
						try
						{
							finishChildLatch.await();
						}
						catch (final InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}.usingThreadRegistry(tr).start();
				try
				{
					finishParentLatch.await();
				}
				catch (final InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}.usingThreadRegistry(tr);

		parent.start();
		childIsRunning.await();

		final SystemState state = service.getSystemState();

		assertThat(state).isNotNull();
		assertThat(state.getStatus()).isNotNull().isSameAs(SystemStatus.RUNNING);
		assertThat(state.getRootThreads()).isNotNull().hasSize(1);

		final RunningThread parentThread = state.getRootThreads().iterator().next();
		assertThat(parentThread).isNotNull();
		assertThat(parentThread.getThreadName()).isEqualTo("parent");
		assertThat(parentThread.getChildThreads()).hasSize(1);

		final RunningThread childThread = parentThread.getChildThreads().iterator().next();
		assertThat(childThread).isNotNull();
		assertThat(childThread.getThreadName()).isEqualTo("child");
		assertThat(childThread.getChildThreads()).isEmpty();

		finishParentLatch.countDown();
		parent.join();

		final SystemState newState = service.getSystemState();
		assertThat(newState).isNotNull();
		assertThat(newState.getStatus()).isNotNull().isSameAs(SystemStatus.RUNNING);
		assertThat(newState.getRootThreads()).isNotNull().hasSize(1);

		final RunningThread newParentThread = newState.getRootThreads().iterator().next();
		assertThat(newParentThread).isNotNull();
		assertThat(newParentThread.getThreadName()).isEqualTo("child");
		assertThat(newParentThread.getChildThreads()).isEmpty();

		finishChildLatch.countDown();
	}

	@Test
	public void shouldReturnResumeTokenAndChangeStateWhenSystemWasRequestedToSuspended() throws InterruptedException
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultSuspendResumeService service = givenSuspendResumeService(tr);

		tr.register(OperationInfo.builder().asNotSuspendableOperation().build());

		final SuspendResult firstAttemptResult = service.suspend(SuspendOptions.defaultOptions());

		assertThat(firstAttemptResult).isNotNull();
		assertThat(firstAttemptResult.getCurrentStatus()).isNotNull().isSameAs(SystemStatus.WAITING_FOR_SUSPEND);
		assertThat(firstAttemptResult.getResumeToken()).isNotNull().isNotEmpty();

		final SuspendResult secondAttemptResult = service.suspend(SuspendOptions.defaultOptions());

		assertThat(secondAttemptResult).isNotNull();
		assertThat(secondAttemptResult.getCurrentStatus()).isNotNull().isSameAs(SystemStatus.WAITING_FOR_SUSPEND);
		assertThat(secondAttemptResult.getResumeToken()).isNull();

		tr.unregister();
		service.waitForSuspenderThread();

		final SuspendResult thirdAttemptResult = service.suspend(SuspendOptions.defaultOptions());

		assertThat(thirdAttemptResult).isNotNull();
		assertThat(thirdAttemptResult.getCurrentStatus()).isNotNull().isSameAs(SystemStatus.SUSPENDED);
		assertThat(thirdAttemptResult.getResumeToken()).isNull();
	}

	@Test
	public void shouldResumeSystemAfterSuspension() throws InterruptedException, ResumeTokenVerificationFailed
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultSuspendResumeService service = givenSuspendResumeService(tr);

		final SuspendResult suspendResult = service.suspend(SuspendOptions.defaultOptions());

		service.waitForSuspenderThread();
		assertThat(service.getSystemStatus()).isSameAs(SystemStatus.SUSPENDED);

		service.resume(ResumeOptions.builder().withResumeToken(suspendResult.getResumeToken()).build());
		assertThat(service.getSystemStatus()).isSameAs(SystemStatus.RUNNING);
	}

	@Test
	public void shouldFailWhenResumingWithInvalidToken() throws InterruptedException
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultSuspendResumeService service = givenSuspendResumeService(tr);

		service.suspend(SuspendOptions.defaultOptions());

		service.waitForSuspenderThread();
		assertThat(service.getSystemStatus()).isSameAs(SystemStatus.SUSPENDED);

		assertThatExceptionOfType(ResumeTokenVerificationFailed.class)
				.isThrownBy(() -> service.resume(ResumeOptions.builder().withResumeToken("WRONG").build()));
		assertThat(service.getSystemStatus()).isSameAs(SystemStatus.SUSPENDED);
	}

	@Test
	public void shouldResumeSystemWhenWaitingForSuspensions() throws InterruptedException, ResumeTokenVerificationFailed
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultSuspendResumeService service = givenSuspendResumeService(tr);

		tr.register(OperationInfo.builder().asNotSuspendableOperation().build());

		final SuspendResult suspendResult = service.suspend(SuspendOptions.defaultOptions());
		assertThat(service.getSystemStatus()).isSameAs(SystemStatus.WAITING_FOR_SUSPEND);

		service.resume(ResumeOptions.builder().withResumeToken(suspendResult.getResumeToken()).build());
		assertThat(service.getSystemStatus()).isSameAs(SystemStatus.RUNNING);
	}

	//@Test
	public void shouldShutdownSystemWhenRequestedToDoSo() throws InterruptedException
	{
		final ThreadRegistry tr = givenThreadRegistry();
		final DefaultSuspendResumeService service = givenSuspendResumeService(tr);

		service.suspend(SuspendOptions.builder().shutdownWhenSuspended().build());
		service.waitForSuspenderThread();

		assertThat("System should be down ;)").isNotNull();
	}

	private DefaultSuspendResumeService givenSuspendResumeService(final ThreadRegistry threadRegistry)
	{
		return new DefaultSuspendResumeService(threadRegistry);
	}

	private ThreadRegistry givenThreadRegistry()
	{
		return new ThreadRegistry(() -> false);
	}
}
