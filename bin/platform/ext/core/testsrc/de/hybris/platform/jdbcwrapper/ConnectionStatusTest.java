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
package de.hybris.platform.jdbcwrapper;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.jdbcwrapper.ConnectionStatus.ConnectionStatusInfo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;


@UnitTest
public class ConnectionStatusTest
{
	private final ConnectionStatus connectionStatus = new ConnectionStatus();

	private static final int NUMBER_OF_THREADS = 80;
	private static final int NUMBER_OF_ITERATIONS = 400;
	private static final int PAUSE = 100;
	final CountDownLatch testFinished = new CountDownLatch(NUMBER_OF_THREADS * 2);

	private final AtomicBoolean errorFound = new AtomicBoolean(false);


	private synchronized void verifyConnectionStatusIntegrity()
	{
		final ConnectionStatusInfo info = connectionStatus.getConnectionStatusInfo();

		if (info.isFoundError() != info.isLastErrorTime())
		{
			errorFound.set(true);
		}
	}

	final Runnable lambdaLogError = () -> {
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
		{
			try
			{
				Thread.sleep(PAUSE);
				connectionStatus.logError();
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		testFinished.countDown();
	};


	final Runnable lambdaResetError = () -> {
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
		{
			try
			{
				Thread.sleep(PAUSE);
				connectionStatus.resetError();
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		testFinished.countDown();
	};

	final Runnable lambdaVerify = () -> {
		while (true)
		{
			try
			{
				Thread.sleep(80);
				verifyConnectionStatusIntegrity();
			}

			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	};

	@Test
	public void shouldSuccessfullyTestConnectionStatusUnderHeavyLoad() throws InterruptedException
	{
		for (int i = 0; i < NUMBER_OF_THREADS; i++)
		{
			final Thread logErrorThread = new Thread(lambdaLogError);
			final Thread resetErrorThread = new Thread(lambdaResetError);

			logErrorThread.start();
			resetErrorThread.start();
		}

		final Thread verifyThread = new Thread(lambdaVerify);
		verifyThread.start();

		testFinished.await(60, TimeUnit.SECONDS);
		assertThat(errorFound.get()).isFalse();
	}
}
