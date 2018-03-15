/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.impex.jalo.imp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader.InitialThreads;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader.PoolableThreadSource;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.threadpool.PoolableThread;
import de.hybris.platform.util.threadpool.ThreadPool;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class InitialThreadsIntegrationTest extends ServicelayerBaseTest
{
	private final List<TestablePoolableThreadSource> poolsToDestroy = new LinkedList<>();

	private final PropertyConfigSwitcher retriesSwitcher = new PropertyConfigSwitcher(
			"impex.number.of.retries.to.acquire.initial.threads");

	@Before
	public void setUp()
	{
		retriesSwitcher.switchToValue("1");
	}

	@After
	public void tearDown()
	{
		poolsToDestroy.forEach(TestablePoolableThreadSource::close);
		poolsToDestroy.clear();
	}

	@Test
	public void shouldRequireExactlyOneThreadInSingleThreadedMode()
	{
		final PoolableThreadSource poolableThreadSource = givenPoolableThreadSource(1);

		final InitialThreads threads = new InitialThreads(false, poolableThreadSource);
		threads.ensureAllThreadsAreAllocated();

		assertThat(poolableThreadSource.tryToBorrowThread()).isNull();
		assertThat(threads.getReaderThread()).isNotNull();
		assertThat(threads.getProcessorThread()).isNull();
		assertThat(threads.getWorkerThread()).isNull();
	}

	@Test
	public void shouldRequireExactlyThreeThreadsInMultiThreadedMode()
	{
		final PoolableThreadSource poolableThreadSource = givenPoolableThreadSource(3);

		final InitialThreads threads = new InitialThreads(true, poolableThreadSource);
		threads.ensureAllThreadsAreAllocated();

		assertThat(poolableThreadSource.tryToBorrowThread()).isNull();
		assertThat(threads.getReaderThread()).isNotNull();
		assertThat(threads.getProcessorThread()).isNotNull();
		assertThat(threads.getWorkerThread()).isNotNull();
	}

	@Test
	public void shouldFailAndReturnThreadsToThePoolWhenThereIsNotEnoughThreadsInThePool()
	{
		final PoolableThreadSource poolableThreadSource = givenPoolableThreadSource(2);

		final InitialThreads threads = new InitialThreads(true, poolableThreadSource);
		assertThatExceptionOfType(SystemException.class).isThrownBy(threads::ensureAllThreadsAreAllocated).withNoCause()
				.withMessage("Couldn't allocate required threads to perform an import.");
		assertThat(poolableThreadSource.tryToBorrowThread()).isNotNull();
		assertThat(poolableThreadSource.tryToBorrowThread()).isNotNull();
		assertThat(poolableThreadSource.tryToBorrowThread()).isNull();
	}

	TestablePoolableThreadSource givenPoolableThreadSource(final int numberOfThreads)
	{
		final TestablePoolableThreadSource result = new TestablePoolableThreadSource(numberOfThreads);
		poolsToDestroy.add(result);
		return result;
	}

	class TestablePoolableThreadSource implements PoolableThreadSource
	{
		private final ThreadPool pool;

		public TestablePoolableThreadSource(final int numberOfThreads)
		{
			pool = new ThreadPool(null, numberOfThreads);

			final GenericObjectPool.Config config = new GenericObjectPool.Config();
			config.maxActive = numberOfThreads;
			config.maxIdle = 0;
			config.maxWait = -1;
			config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
			config.testOnBorrow = true;
			config.testOnReturn = true;
			config.minEvictableIdleTimeMillis = 0;
			config.timeBetweenEvictionRunsMillis = 0; // keep idle threads for at most 30 sec
			pool.setConfig(config);
		}

		@Override
		public PoolableThread tryToBorrowThread()
		{
			try
			{
				return pool.borrowThread();
			}
			catch (final NoSuchElementException e)
			{
				return null;
			}
		}

		public void close()
		{
			pool.close();
		}
	}
}
