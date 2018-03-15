/*
 *  [y] hybris Platform
 *
 *  Copyright (c) 2000-2017 SAP SE
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of SAP
 *  Hybris ("Confidential Information"). You shall not disclose such
 *  Confidential Information and shall use it only in accordance with the
 *  terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.core.threadregistry;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.threadregistry.OperationInfo.StandardAttributes;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.util.threadpool.NamedProcess;
import de.hybris.platform.util.threadpool.PoolableThread;
import de.hybris.platform.util.threadpool.ThreadPool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;


@IntegrationTest
public class ThreadRegistryIntegrationTest extends ServicelayerBaseTest
{

	private final long SECONDS = 1000;

	@Test
	public void shouldNotFailWhenThreadIsRegisteredTwiceInJunitThread()
	{
		defaultThreadRegistry().register(OperationInfo.empty());
		defaultThreadRegistry().register(OperationInfo.empty());
	}

	@Test
	public void shouldNotFailWhenThreadIsUnregisteredTwiceInJunitThread()
	{
		defaultThreadRegistry().unregister();
		defaultThreadRegistry().unregister();
	}

	@Test
	public void shouldSuccessfullyRegisterRegistrableThread() throws Exception
	{

		final AtomicBoolean isRegistered = new AtomicBoolean(false);
		//given
		final Thread t = new RegistrableThread()
		{
			@Override
			protected void internalRun()
			{
				isRegistered.set(testSupportForDefaultRegistry().isCurrentThreadRegistered());
			}
		};

		//when
		t.start();
		t.join(10 * SECONDS);

		//then
		assertThat(isRegistered.get()).isTrue();
	}

	@Test
	public void shouldSuccessfullyRegisterPoolableThread() throws Exception
	{
		final AtomicBoolean isRegistered = new AtomicBoolean(false);
		//given

		try (final ThreadPool pool = new ThreadPool("junit", 5))
		{
			final PoolableThread poolableThread = pool.borrowThread();
			final CountDownLatch testFinished = new CountDownLatch(1);
			final Runnable runnable = () -> {

				isRegistered.set(testSupportForDefaultRegistry().isCurrentThreadRegistered());
				testFinished.countDown();
			};

			//when
			poolableThread.execute(runnable);
			testFinished.await(30, TimeUnit.SECONDS);
		}

		//then
		assertThat(isRegistered.get()).isTrue();
	}

	@Test
	public void shouldSuccessfullyRenamePoolableThread() throws Exception
	{
		final String NAME = "SomeTestNameForRunnable";

		final AtomicReference<String> namedThreadName = new AtomicReference<>("WrongName");
		final AtomicReference<String> threadNameBefore = new AtomicReference<>("before");
		final AtomicReference<String> threadNameAfter = new AtomicReference<>("after");
		final CountDownLatch testFinished = new CountDownLatch(1);
		//given
		try (final ThreadPool pool = new ThreadPool("junit", 5))
		{
			final PoolableThread poolableThread = pool.borrowThread();
			threadNameBefore.set(poolableThread.getName());

			//when
			poolableThread.execute(new NamedRunnable()
			{

				@Override
				public void run()
				{
					namedThreadName.set(
							(String) testSupportForDefaultRegistry().getAttributeFromCurrentOperation(StandardAttributes.THREAD_NAME));
					testFinished.countDown();
				}

				@Override
				public String getProcessName()
				{
					return NAME;
				}
			});
			testFinished.await(30, TimeUnit.SECONDS);
			//We need to wait some time until thread is really returned to the pool
			Thread.sleep(100);
			threadNameAfter.set(poolableThread.getName());
		}
		//then
		assertThat(namedThreadName.get()).isEqualTo(NAME);
		assertThat(threadNameBefore.get()).isEqualTo(threadNameAfter.get());
	}

	@Test
	public void shouldSuccessfullySetTenantForCurrentThread() throws Exception
	{
		final AtomicReference before = new AtomicReference();
		final AtomicReference after = new AtomicReference();

		///given
		final Thread t = new RegistrableThread()
		{
			@Override
			protected void internalRun()
			{
				//				super.internalRun();
				before.set(testSupportForDefaultRegistry().getAttributeFromCurrentOperation(StandardAttributes.TENANT_ID));
				Registry.setCurrentTenantByID("junit");
				after.set(testSupportForDefaultRegistry().getAttributeFromCurrentOperation(StandardAttributes.TENANT_ID));
			}
		};

		//when
		t.start();
		t.join(30 * SECONDS);

		//then
		assertThat(before.get()).isNull();
		assertThat(after.get()).isEqualTo("junit");

	}

	@Test
	public void shouldNotBeRegisteredWhenRunInStandardThread() throws Exception
	{
		final AtomicBoolean isRegistered = new AtomicBoolean(true);
		//given
		final Thread t = new Thread()
		{
			@Override
			public void run()
			{
				isRegistered.set(testSupportForDefaultRegistry().isCurrentThreadRegistered());
			}
		};

		//when
		t.start();
		t.join(10 * SECONDS);

		//then
		assertThat(isRegistered.get()).isFalse();
	}

	@Test
	public void shouldThrowISEWhenRegisteringThreadTwice() throws Exception
	{
		final AtomicReference<Throwable> exception = new AtomicReference<>();

		///given
		final Thread t = new RegistrableThread()
		{
			@Override
			protected void internalRun()
			{
				RegistrableThread.registerThread(OperationInfo.empty());
			}
		};
		t.setUncaughtExceptionHandler((thread, e) -> exception.set(e));

		//when
		t.start();
		t.join(10 * SECONDS);

		//then
		assertThat(exception.get()).isInstanceOf(IllegalStateException.class)
				.hasMessageStartingWith("Unable to register thread with");
	}


	@Test
	public void shouldThrowISEWhenUnregisteringThreadTwice() throws Exception
	{
		final AtomicReference<Throwable> exception = new AtomicReference<>();

		///given
		final Thread t = new RegistrableThread()
		{
			@Override
			protected void internalRun()
			{
				RegistrableThread.unregisterThread();
			}
		};

		t.setUncaughtExceptionHandler((thread, e) -> {
			exception.set(e);

		});

		//when
		t.start();
		t.join(10 * SECONDS);

		//then
		assertThat(exception.get()).isInstanceOf(IllegalStateException.class)
				.hasMessageStartingWith("Unable to unregister thread with");
	}


	@Test
	public void shouldRevertToPreviousOperationInfo() throws InterruptedException
	{

		final AtomicBoolean methodInvoked = new AtomicBoolean(false);
		final AtomicReference<AssertionError> assertException = new AtomicReference<>();

		final Thread t = new RegistrableThread()
		{
			@Override
			protected void internalRun()
			{
				try
				{
					//given
					methodInvoked.set(true);
					final OperationInfo previousOperationInfo = testSupportForDefaultRegistry().getCurrentOperationInfo();

					assertThat(previousOperationInfo).isNotNull();
					assertThat(testSupportForDefaultRegistry().getAttributeFromCurrentOperation("foo")).isNull();

					final RevertibleUpdate revertibleUpdate = OperationInfo
							.updateThread(OperationInfo.builder().withAdditionalAttribute("foo", "bar").build());

					assertThat(revertibleUpdate).isNotNull();
					assertThat(testSupportForDefaultRegistry().getAttributeFromCurrentOperation("foo")).isNotNull().isEqualTo("bar");

					//when
					revertibleUpdate.revert();

					//then
					assertThat(testSupportForDefaultRegistry().getCurrentOperationInfo()).isEqualTo(previousOperationInfo);
					assertThat(testSupportForDefaultRegistry().getAttributeFromCurrentOperation("foo")).isNull();
				}
				catch (final AssertionError assertionError)
				{
					assertException.set(assertionError);
				}
			}
		};

		t.start();
		t.join(10 * SECONDS);

		assertThat(methodInvoked.get()).isTrue();

		if (assertException.get() != null)
		{
			throw assertException.get();
		}
	}

	private ThreadRegistry defaultThreadRegistry()
	{
		return SuspendResumeServices.getInstance().getThreadRegistry();
	}

	private TestSupport testSupportForDefaultRegistry()
	{
		return TestSupport.forRegistry(defaultThreadRegistry());
	}

	private interface NamedRunnable extends NamedProcess, Runnable
	{
		//only for tests
	}
}
