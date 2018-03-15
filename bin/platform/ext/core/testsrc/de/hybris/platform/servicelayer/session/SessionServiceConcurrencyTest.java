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
package de.hybris.platform.servicelayer.session;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.test.TestThreadsHolder.RunnerCreator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Concurrency Test for {@link SessionService}
 */
@IntegrationTest
public class SessionServiceConcurrencyTest extends ServicelayerTransactionalBaseTest
{
	private static final Logger LOG = Logger.getLogger(SessionServiceConcurrencyTest.class);
	private static final String TEST_KEY = "testKey";
	private static final int THREADS = 10;

	@Resource
	private SessionService sessionService;

	private final Set<Integer> result = Collections.synchronizedSet(new HashSet<Integer>());

	@Test
	public void testMultiThreadedGetOrCreate()
	{
		final JaloSession currentSession = JaloSession.getCurrentSession();
		final AbstractTenant tenant = (AbstractTenant) Registry.getCurrentTenant();
		final RunnerCreator<Runnable> runnerCreator = new TestThreadsHolder.RunnerCreator<Runnable>()
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							// artificially force all threads to use the same JaloSession
							Registry.setCurrentTenant(tenant);
							tenant.setActiveSessionForCurrentThread(currentSession);
							result.add(sessionService.getOrLoadAttribute(TEST_KEY, new SessionService.SessionAttributeLoader<Integer>()
							{
								@Override
								public Integer load()
								{
									LOG.warn("Thread " + threadNumber + " load for session " + sessionService);
									return Integer.valueOf(threadNumber);
								}
							}));
						}
						catch (final Exception e)
						{
							LOG.error("Initialization failed", e);
						}
					}
				};
			}
		};
		final TestThreadsHolder<Runnable> threads = new TestThreadsHolder<Runnable>(THREADS, runnerCreator);

		threads.startAll();
		Assert.assertTrue(threads.waitAndDestroy(5));
		Assert.assertEquals(Collections.EMPTY_MAP, threads.getErrors());
		Assert.assertEquals("did get more than one result: " + result, 1, result.size());
	}
}
